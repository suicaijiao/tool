package com.soonphe.portal.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soonphe.portal.commons.constant.CommonsEnum;
import com.soonphe.portal.commons.constant.SysConfInterface;
import com.soonphe.portal.commons.exception.entity.BusinessException;
import com.soonphe.portal.commons.golbal.result.ResultCode;
import com.soonphe.portal.commons.redis.RedisService;
import com.soonphe.portal.commons.redis.RedisSettings;
import com.soonphe.portal.commons.util.StringUtil;
import com.soonphe.portal.entity.*;
import com.soonphe.portal.mapper.GmsHistoryMapper;
import com.soonphe.portal.model.GameMatchingUser;
import com.soonphe.portal.model.vo.GameContractVo;
import com.soonphe.portal.model.vo.GameEndResultVo;
import com.soonphe.portal.model.vo.GameUser;
import com.soonphe.portal.service.*;
import com.soonphe.portal.socket.GameSocketServer;
import com.soonphe.portal.vo.DayUserGameStatisticsVo;
import com.soonphe.portal.vo.QueryUserGameStatisticsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

import static java.util.Calendar.HOUR_OF_DAY;

/**
 * <p>
 * 游戏记录表 服务实现类
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@Service
@Slf4j
public class GmsHistoryServiceImpl extends ServiceImpl<GmsHistoryMapper, GmsHistory> implements IGmsHistoryService {

    @Resource
    private GmsHistoryMapper gmsHistoryMapper;
    @Autowired
    private IGmsDailyNumService iGmsDailyNumService;
    @Autowired
    private IWmsWalletService walletService;
    @Autowired
    private IOmsOrePoolService orePoolService;
    @Autowired
    private IOmsOrePoolHistoryService omsOrePoolHistoryService;
    @Autowired
    private ISmsBaseService smsBaseService;
    @Autowired
    private IUmsCommunityHistoryService communityHistoryService;
    @Autowired
    private ISmsStatsService smsStatsService;
    @Autowired
    private IGmsDailyNumService dailyNumService;
    @Autowired
    private IUmsCommunityProfitService communityProfitService;
    @Autowired
    private IGmsGametimeService gmsGametimeService;

    @Autowired
    private IUmsContractHistoryService umsContractHistoryService;

    @Autowired
    private RedisService redisService;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 存放游戏用户
     */
    private static Map<Integer, String> gameMap = new ConcurrentHashMap<>();

    /**
     * 存放游戏用户
     */
    private static Queue<GameMatchingUser> gameUserQueue;

    static {
        if (null == gameUserQueue) {
            //基于链接节点的无界线程安全队列
            gameUserQueue = new ConcurrentLinkedQueue<GameMatchingUser>();
        }
    }

    // 线程池
    static ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 30,
            0L, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(20));

    //发令枪
    CountDownLatch countDownLatch = new CountDownLatch(10);

    /**
     * 验证用户有效性
     *
     * @param userId
     * @return
     */
    @Override
    public GameMatchingUser checkUserGameValid(Integer userId) {
        JSONObject message;
        GameMatchingUser gameMatchingUser;
        gameMatchingUser = redisService.getObject(String.format(RedisSettings.GAME_USER, userId), GameMatchingUser.class);
        WmsWallet wmsWallet = walletService.getById(userId);
        if (wmsWallet == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "用户异常");
        }
        if (gameMatchingUser == null) {
            gameMatchingUser = new GameMatchingUser();
            gameMatchingUser.setUserId(wmsWallet.getId());
            gameMatchingUser.setTrxAddress(wmsWallet.getTrxAddress());
            gameMatchingUser.setStatus(CommonsEnum.USER_GAME_FALSE.getCode());
            gameMatchingUser.setDayGameCount(0);
            // 缓存申请游戏用户信息
            redisService.setObject(String.format(RedisSettings.GAME_USER, userId), gameMatchingUser, StringUtil.getSecondsNextEarlyMorning());
            // 判断当日游戏次数
        } else if (gameMatchingUser.getDayGameCount() >= wmsWallet.getGameNum()) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "今日游戏次数已用尽");
            // 如果用户还在游戏中
        } else if (gameMatchingUser.getStatus().equals(CommonsEnum.USER_GAME_TRUE.getCode())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "当前用户已在游戏房间内");
        } else if (gameMatchingUser.getStatus().equals(CommonsEnum.USER_GAME_FALSE.getCode())) {
            if (gameUserQueue.size() == 0) {
                return gameMatchingUser;
            } else {
                throw new BusinessException(ResultCode.VALIDATE_FAILED, "游戏已在匹配中，请稍等");
            }
        }
        // 验证用户游戏有效性、
        checkUserGameStatus(wmsWallet);
        // 验证用户有效性
        boolean contractStatus = checkUserGameContract(wmsWallet);
        if (!contractStatus) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "当前合约到期，请购买新合约");
        }

        return gameMatchingUser;
    }


    /**
     * 获取10 个申请游戏用户
     *
     * @return
     */
    @Override
    public GameEndResultVo matchingGameUser(List<GameMatchingUser> matchingGameUserList) {
        System.out.println(Thread.currentThread());
        long time = System.currentTimeMillis();
        // 获得游戏用户
        String gameRoomId = StringUtil.GUID();
        Callable<GameEndResultVo> resultCallable = new Callable() {
            @Override
            public GameEndResultVo call() throws Exception {
                GameEndResultVo result = gameStart(matchingGameUserList, gameRoomId);
                return result;
            }
        };

        try {
            Future<GameEndResultVo> typeResult = executor.submit(resultCallable);
            return typeResult.get();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "系统异常");
        }

    }

    /**
     * 开始游戏
     *
     * @param gameUserList
     * @return
     */
    @Override
    @Transactional
    public GameEndResultVo gameStart(List<GameMatchingUser> gameUserList, String gameRoomId) {

        //计算汇率
        BigDecimal rate = orePoolService.getFtTicket();

        // 获取游戏房间用户地址
        if (CollectionUtils.isEmpty(gameUserList)) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "游戏匹配异常");
        }
        // 修改缓存用户为游戏中
        for (GameMatchingUser gameUser : gameUserList) {
            gameUser.setStatus(CommonsEnum.USER_GAME_TRUE.getCode());
            int gameCount = 0;
            if (gameUser.getDayGameCount() == null) {
                gameUser.setDayGameCount(1);
            } else {
                gameCount = gameUser.getDayGameCount();
                gameUser.setDayGameCount(gameCount + 1);
            }
            // 保存修改记录
            redisService.setObject(String.format(RedisSettings.GAME_USER, gameUser.getUserId()), gameUser);
        }

        // 矿池余额减少
        OmsOrePool pool = orePoolService.list().get(0);
        // 汇率乘以15
        BigDecimal releaseOne = rate.multiply(new BigDecimal(15));
        // 矿池余额-需要释放的数量
        pool.setBalance(pool.getBalance().subtract(releaseOne));
        //累计释放 +需要释放的数量
        pool.setTotalRelease(pool.getTotalRelease().add(releaseOne));
        // 修改矿池表
        orePoolService.updateById(pool);

        // 新增矿池释放记录表
        omsOrePoolHistoryService.save(new OmsOrePoolHistory(releaseOne, new Date()));

        addSmsStats(gameUserList.size(), rate);
        //开奖
        Random random = new Random();
        int prizeId = random.nextInt(gameUserList.size()) + 1;

        // 保存用户游戏房间
        GmsHistory history;
        List<GmsHistory> gmsHistories = new ArrayList<>();
        List<WmsWallet> wmsWalletList = new ArrayList<>();

        // 存放游戏用户奖励结果
        List<GameEndResultVo> gameEndResultVoList = new ArrayList<>();

        Integer winUserId = null;
        // 用户获取奖励DOT = 0.975*15*发行量汇率
        BigDecimal winnerDOT = SysConfInterface.USER_GET_DOT.multiply(rate).setScale(4, BigDecimal.ROUND_DOWN);
        int index = 0;

        // 存放当前游戏用户
        GameUser gameUserItem;
        List<GameUser> gameUserResultList = new ArrayList<>();
        for (GameMatchingUser gameUser : gameUserList) {
            index++;
            gameUserItem = new GameUser();
            gameUserItem.setTrxAddress(gameUser.getTrxAddress());
            gameUserItem.setUserId(gameUser.getUserId());
            gameUserResultList.add(gameUserItem);
            // 实例化游戏记录
            history = new GmsHistory();

            // 查询用户钱包
            WmsWallet wmsWallet = walletService.getById(gameUser.getUserId());

            if (wmsWallet == null) {
                throw new BusinessException(ResultCode.VALIDATE_FAILED, "游戏异常");
            }
            // 返回结果用户id
            winUserId = wmsWallet.getId();
            // 如当前用户为中奖用户
            if (index == prizeId) {

                // 中奖用户的游戏记录
                history.setRewardUsdt(new BigDecimal("0"));
                // 用户获取奖励DOT = 0.975*15*发行量汇率
                history.setRewardDot(winnerDOT);
                // 基础奖励DOT =  0.975*15*发行量汇率
                history.setBaseDot(SysConfInterface.USER_GET_DOT.multiply(rate).setScale(4, BigDecimal.ROUND_DOWN));
                history.setWinStatus(CommonsEnum.WIN_STATUS_TRUE.getCode());
                // 实例化钱包数据
                // trx数量 = trx数量-15+0.15 当局游戏门票0.15与中奖拿出的15USDT
                wmsWallet.setTrxAmount(wmsWallet.getTrxAmount().subtract(SysConfInterface.USER_USDT.add(SysConfInterface.TICKET)));
                // 赢得的DOT数量 = 赢得的FT数量+0.975 * 15*计算汇率，结果四舍五入保留4位小数
                wmsWallet.setWinFt(wmsWallet.getWinFt().add(SysConfInterface.USER_GET_DOT.multiply(rate).setScale(4, BigDecimal.ROUND_DOWN)));
                // 总中奖+1
                wmsWallet.setWinnerTotal(wmsWallet.getWinnerTotal() + 1);
            } else { // 获奖和未获奖的区别
                // 为获奖用户 usdt = 总数-门票+1.2285
                wmsWallet.setTrxAmount(wmsWallet.getTrxAmount().subtract(SysConfInterface.TICKET).add(SysConfInterface.BASE_USDT));
                // 未中奖用户分得USDT
                wmsWallet.setWinCny(wmsWallet.getWinCny().add(SysConfInterface.BASE_USDT));
                history.setWinStatus(CommonsEnum.WIN_STATUS_FALSE.getCode());
                // 未中奖用户分配的USDT
                history.setBaseUsdt(SysConfInterface.BASE_USDT);
                history.setRewardUsdt(SysConfInterface.BASE_USDT);
            }

            history.setUid(wmsWallet.getId());
            history.setTicket(SysConfInterface.TICKET);
            history.setGid(gameRoomId);
            history.setWno(prizeId);
            history.setCreateDate(new Date());
            history.setSeat(index);
            history.setStatus(CommonsEnum.STATUS_END.getCode());

            // 当日游戏记录+1
            wmsWallet.setGameTotal(wmsWallet.getGameTotal() + 1);
            if (wmsWallet.getGameTotal() == 10) {
                // 用户升级为有效用户
                wmsWallet.setUserLevel(CommonsEnum.USER_LEVEL_YX.getCode());
            }
            GameEndResultVo gameEndResultVo = new GameEndResultVo();
            // 保存游戏结果
            gameEndResultVoList.add(gameEndResultVo);
            // 保存钱包新
            wmsWalletList.add(wmsWallet);

            // 保存用户游戏记录
            gmsHistories.add(history);
        }

        //统计当前游戏信息
        addSmsStats(gameUserList.size(), rate);
        // 新增游戏记录表
        this.saveBatch(gmsHistories);

        // 修改用户钱包
        walletService.updateBatchById(wmsWalletList);

        // 修改用户游戏状态
        ;
        for (GameMatchingUser gameEndUser : gameUserList) {
            gameEndUser.setStatus(CommonsEnum.USER_GAME_END.getCode());
            // 修改缓存用户游戏状态
            redisService.setObject(String.format(RedisSettings.GAME_USER, gameEndUser.getUserId()), gameEndUser);
        }

        GameEndResultVo endResultVo = new GameEndResultVo();
        endResultVo.setGameBonusDOT(winnerDOT);
        endResultVo.setStatus(CommonsEnum.WIN_STATUS_TRUE.getCode());
        endResultVo.setGameBonusUSDT(SysConfInterface.BASE_USDT);
        endResultVo.setWinUserId(winUserId);
        redisService.setObject(String.format(RedisSettings.GAME_ROOM, gameRoomId), endResultVo);
        log.info("游戏结束，游戏结果为：{}", JSONObject.toJSONString(endResultVo));
        return endResultVo;
    }


    /**
     * 检查用户游戏合约
     *
     * @param wmsWallet
     */
    @Transactional
    public boolean checkUserGameContract(WmsWallet wmsWallet) {
        GameContractVo gameContractVo = umsContractHistoryService.contractGameReward(wmsWallet.getId());

        // 判断用户获得奖励总数是否大于定义最大值
        if (gameContractVo == null || gameContractVo.getSumRewardUSDT().compareTo(gameContractVo.getSpecMax()) >= 0) {
            // 获取用户合约记录
            List<UmsContractHistory> contractHistoryList = umsContractHistoryService.list(new QueryWrapper<UmsContractHistory>().lambda()
                    .eq(UmsContractHistory::getStatus, CommonsEnum.CONTRACT_STATUS_TRUE.getCode())
                    .eq(UmsContractHistory::getUid, wmsWallet.getId()));
            if (CollectionUtils.isEmpty(contractHistoryList)) {
                throw new BusinessException(ResultCode.VALIDATE_FAILED, "用户合约异常");
            }
            UmsContractHistory contractHistory = contractHistoryList.get(0);
            contractHistory.setStatus(CommonsEnum.CONTRACT_STATUS_FALSE.getCode());
            contractHistory.setContractActualEndDate(new Date());
            // 执行修改
            umsContractHistoryService.updateById(contractHistory);

            // 修改用户钱包记录
            wmsWallet.setUserLevel(CommonsEnum.USER_LEVEL_YX.getCode());
            wmsWallet.setContractGameNum(0);
            wmsWallet.setGameNum(0);
            walletService.updateById(wmsWallet);
            return false;
        }
        return true;

    }


    /**
     * 统计当前游戏信息
     *
     * @param gameCount
     * @param rate
     */
    private void addSmsStats(int gameCount, BigDecimal rate) {
        LocalDateTime now = LocalDateTime.now();
        // 查询游戏统计模块表，查询当日游戏记录
        List<SmsStats> smsStatsList = smsStatsService.list(new QueryWrapper<SmsStats>().lambda()
                .eq(SmsStats::getCreateDate, now.format(dateTimeFormatter)));
        SmsStats smsStats;
        if (smsStatsList.size() > 0) {
            smsStats = smsStatsList.get(0);
        } else {
            smsStats = new SmsStats();
            smsStats.setCreateDate(new Date());
            smsStats.setAndroidGameCount(0);
            smsStats.setUserGameCount(0);
            smsStats.setAndroidWinCount(0);
            smsStats.setUserWinCount(0);
            smsStats.setTicketCount(new BigDecimal(0));
            smsStats.setMinerFtCount(new BigDecimal(0));
            smsStats.setMinerCnyCount(new BigDecimal(0));
        }
        smsStats.setCreateDate(new Date());
        // 赋值机器人游戏次数 机器人游戏次数+10-list.size()
        smsStats.setAndroidGameCount(smsStats.getAndroidGameCount() + 10 - gameCount);
        // 用户游戏次数
        smsStats.setUserGameCount(smsStats.getUserGameCount() + gameCount);
        //门票总计 = 门票总数+每次游戏门票*游戏用户数量
        smsStats.setTicketCount(smsStats.getTicketCount().add(SysConfInterface.TICKET.multiply(new BigDecimal(gameCount))));
        // 用户中奖次数
        smsStats.setUserWinCount(smsStats.getUserWinCount() + 1);
        // 矿工DUT总计 = 矿工DUT总计+15USDT*10025*发行量费率四舍五入保留四位小数
        smsStats.setMinerFtCount(smsStats.getMinerFtCount().add(SysConfInterface.GAME_DOT_COST.multiply(rate).setScale(4, BigDecimal.ROUND_DOWN)));
        // 旷工USDT = 旷工USDT+获奖用户拿出的15U扣除2.5%*游戏人数-获奖用户
        smsStats.setMinerCnyCount(smsStats.getMinerCnyCount().add(SysConfInterface.GAME_USDT_COST.multiply(new BigDecimal(gameCount - 1))));
        // todo  recommendFt推荐奖励DOT recommendCny 推荐奖励USDT 放到统计游戏里面添加
        // 执行修改
        smsStatsService.saveOrUpdate(smsStats);

    }


    /**
     * 验证游戏有效性
     *
     * @param wmsWallet
     */
    private void checkUserGameStatus(WmsWallet wmsWallet) {
//        List<GmsGametime> gametimes = gmsGametimeService.list();
//        if (gametimes.size() > 0) {
//            Calendar calendar = Calendar.getInstance();
//            int curHour24 = calendar.get(HOUR_OF_DAY);
//            Boolean result = false;
//            for (GmsGametime gmsGametime : gametimes) {
//                if (curHour24 >= gmsGametime.getGameStart() && curHour24 < gmsGametime.getGameEnd()) {
//                    result = true;
//                }
//            }
//            if (!result) {
//                throw new BusinessException(ResultCode.VALIDATE_FAILED, "游戏时段不对");
//            }
//        }

        if (StringUtils.isBlank(wmsWallet.getRecommendId())) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "请先绑定推荐人");
        }
        if (wmsWallet.getGameNum() == null || wmsWallet.getGameNum() < 1) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "游戏次数不足");
        }

        if (wmsWallet.getGainpackLevel() == 0 && wmsWallet.getContractLeve() == 0 && wmsWallet.getContractNewLeve() == 0) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "请先购买合约或增益包");
        }

        // 判断入场金额
        if (wmsWallet.getTrxAmount().compareTo(new BigDecimal(15).add(SysConfInterface.TICKET)) < 0) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED, "钱包余额不足");
        }

    }

    /**
     * 查询当前所有用户游戏记录统计
     *
     * @param query
     * @return
     */
    @Override
    public List<DayUserGameStatisticsVo> getPageUserGameStatistics(QueryUserGameStatisticsVo query) {
        query.setPageIndex((query.getPageSize() - 1) * query.getPageSize());
        return gmsHistoryMapper.selectDayUserGameStatistics(query);
    }

    /**
     * 查询当前所有用户游戏记录统计数量
     *
     * @param query
     * @return
     */
    @Override
    public int countDayUserGameStatistics(QueryUserGameStatisticsVo query) {
        return gmsHistoryMapper.countDayUserGameStatistics(query);
    }

    public static void main(String[] args) {
        long count = Stream.of(4, 5, 3, 9, 1, 2, 6)
                .filter(s -> {
                    System.out.println(Thread.currentThread() + ", s = " + s);
                    return true;
                })
                .count();
        System.out.println("count = " + count);
    }


}
