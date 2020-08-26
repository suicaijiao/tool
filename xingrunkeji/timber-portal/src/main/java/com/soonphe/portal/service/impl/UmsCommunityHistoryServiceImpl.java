package com.soonphe.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soonphe.portal.commons.constant.SysConfInterface;
import com.soonphe.portal.entity.UmsCommunityHistory;
import com.soonphe.portal.entity.UmsCommunityProfit;
import com.soonphe.portal.entity.WmsWallet;
import com.soonphe.portal.mapper.UmsCommunityHistoryMapper;
import com.soonphe.portal.service.IGmsHistoryService;
import com.soonphe.portal.service.IUmsCommunityHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soonphe.portal.service.IUmsCommunityProfitService;
import com.soonphe.portal.service.IWmsWalletService;
import com.soonphe.portal.vo.DayUserGameStatisticsVo;
import com.soonphe.portal.vo.ExtensionVo;
import com.soonphe.portal.vo.QueryUserGameStatisticsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 社群收益记录表 服务实现类
 * </p>
 *
 * @author soonphe
 * @since 2020-06-05
 */
@Service
@Slf4j
public class UmsCommunityHistoryServiceImpl extends ServiceImpl<UmsCommunityHistoryMapper, UmsCommunityHistory> implements IUmsCommunityHistoryService {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static Map<String, Object> userMap = new ConcurrentHashMap<>();
    /**
     * 存放升级规格信息
     */
    private static Map<Integer, UmsCommunityProfit> map = new HashMap<>();

    @Autowired
    private IGmsHistoryService gmsHistoryService;

    @Autowired
    private IWmsWalletService wmsWalletService;

    @Autowired
    private IUmsCommunityProfitService communityProfitService;

    @Override
    public ExtensionVo getCommunityHistory(Date startTime, Date endTime) {
        return baseMapper.getCommunityHistory(startTime, endTime);
    }

    /**
     * 社群奖励分配(定时任务调用)
     */
    @Override
    public void communityRewardDistribution() {

        // 查询直推规格
        List<UmsCommunityProfit> profitList = communityProfitService.list(new QueryWrapper<UmsCommunityProfit>().lambda()
                .orderByDesc(UmsCommunityProfit::getDirectSpec));

        for (UmsCommunityProfit profit : profitList) {
            map.put(profit.getLevel(), profit);
        }


        //获取时间
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.plusDays(-1);
        // 每次拉取的数据
        int pageSize = 100;

        QueryUserGameStatisticsVo query = new QueryUserGameStatisticsVo();
        query.setStartDate(localDateTime.format(dateTimeFormatter) + " 00:00:00");
        query.setEndDate(localDateTime.format(dateTimeFormatter) + " 23:59:59");
        query.setPageSize(100);
        // 查询游戏总数
        int rowCount = gmsHistoryService.countDayUserGameStatistics(query);
        if (rowCount == 0) {
            log.info("游戏记录异常，无法分配收益");
            return;
        }


        //获得总页数
        int pageCount = (rowCount % pageSize == 0) ? (rowCount / pageSize) : (rowCount / pageSize + 1);

        for (int i = 1; i <= pageCount; i++) {
            query.setPageIndex(i);
            List<DayUserGameStatisticsVo> gameStatisticsList = gmsHistoryService.getPageUserGameStatistics(query);
            List<WmsWallet> wmsWalletList = new ArrayList<>();
            // 查询当前用户
            for (DayUserGameStatisticsVo dayUserGameStatisticsVo : gameStatisticsList) {
                // 分配直推间推用户奖励
                WmsWallet wmsWallet = wmsWalletService.getById(dayUserGameStatisticsVo.getUserId());
                // 判断用户升级
                if (wmsWallet.getGameTotal() >= 10) {
                    if (userAddLevel(wmsWallet) != null) {
                        wmsWalletList.add(userAddLevel(wmsWallet));
                    }
                }
            }
            // 修改用户
            if (!CollectionUtils.isEmpty(wmsWalletList)) {
                wmsWalletService.updateBatchById(wmsWalletList);
            }
        }
    }

    private WmsWallet communityReward(WmsWallet wmsWallet,DayUserGameStatisticsVo dayUserGameStatisticsVo) {
        if (StringUtils.isBlank(wmsWallet.getRecommendId())) {
            log.info("");
        }
        // 推荐人
        List<WmsWallet> wmsCommunityWalletList = wmsWalletService.list(new QueryWrapper<WmsWallet>().lambda()
                .like(WmsWallet::getRecommendId, wmsWallet.getId())
                .ge(WmsWallet::getGameTotal, 9));
        if (CollectionUtils.isEmpty(wmsCommunityWalletList)) {
            return null;
        }
        // 获取直推用户
        WmsWallet wmsWalletDirect = wmsCommunityWalletList.get(wmsCommunityWalletList.size() - 1);
        if(wmsWalletDirect.getTrxAmount()!=null){
//            wmsWalletDirect.setTrxAmount(wmsWalletDirect.getTrxAmount().add(wmsWallet))
        }

        // 获取间推用户
        WmsWallet wmsWalletIndirect = wmsCommunityWalletList.get(wmsCommunityWalletList.size() - 2);

        return null;
    }

    /**
     * 处理用户升级
     *
     * @param wmsWallet
     * @return
     */
    private WmsWallet userAddLevel(WmsWallet wmsWallet) {


        // 查询社群有效用户
        List<WmsWallet> wmsCommunityWalletList = wmsWalletService.list(new QueryWrapper<WmsWallet>().lambda()
                .like(WmsWallet::getRecommendId, wmsWallet.getId())
                .ge(WmsWallet::getGameTotal, 9));
        // 查询直推用户
        List<WmsWallet> wmsWalletList = wmsWalletService.list(new QueryWrapper<WmsWallet>().lambda()
                .likeLeft(WmsWallet::getRecommendId, wmsWallet.getId())
                .ge(WmsWallet::getGameTotal, 9));
        int s1 = 0, s2 = 0, s3 = 0, s4 = 0;
        // 判断团队有效人数等级
        if (CollectionUtils.isEmpty(wmsWalletList)) {
            log.info("当前用户没有推荐用户");
            return null;
        }
        // 获取团队各个等级人数
        for (WmsWallet wallet : wmsWalletList) {
            switch (wallet.getUserLevel()) {
                case 1:
                    s1 += 1;
                    break;
                case 2:
                    s2 += 1;
                    break;
                case 3:
                    s3 += 1;
                    break;
                case 4:
                    s4 += 1;
                    break;
                default:
                    break;
            }
        }

        UmsCommunityProfit profit = map.get(wmsWallet.getUserLevel());

        if (wmsWalletList.size() >= profit.getDirectSpec() && wmsCommunityWalletList.size() >= profit.getDirectSpec()) {
            // 如果当前用户等级为S2 判断直推是否包含两个S1用户
            if (wmsWallet.getUserLevel().equals(SysConfInterface.communityLevelSecond) && s1 < 2) {
                return null;
                // 当前用户级别为S3判断是否包含3个S2
            } else if (wmsWallet.getUserLevel().equals(SysConfInterface.communityLevelThird) && s2 < 3) {
                return null;
                // 当前用户级别为S4判断是否包含3个S3
            } else if (wmsWallet.getUserLevel().equals(SysConfInterface.communityLevelFourth) && s3 < 3) {
                return null;
            }
            wmsWallet.setUserLevel(profit.getId());
            wmsWallet.setRecommendGameNum(profit.getDailyGameNum());
            wmsWallet.setGameNum(wmsWallet.getGameNum() + profit.getDailyGameNum());
            return wmsWallet;
        }
        return null;
    }


    public static void main(String[] args) {
        //获取时间
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        System.out.println(list.get(list.size() - 2));

    }
}
