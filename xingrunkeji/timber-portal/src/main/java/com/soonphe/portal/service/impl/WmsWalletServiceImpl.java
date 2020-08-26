package com.soonphe.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soonphe.portal.commons.constant.CommonsEnum;
import com.soonphe.portal.commons.exception.entity.BusinessException;
import com.soonphe.portal.commons.golbal.result.ResultCode;
import com.soonphe.portal.entity.SmsBase;
import com.soonphe.portal.entity.WmsWallet;
import com.soonphe.portal.entity.WmsWalletHistory;
import com.soonphe.portal.mapper.WmsWalletMapper;
import com.soonphe.portal.service.ISmsBaseService;
import com.soonphe.portal.service.IWmsWalletHistoryService;
import com.soonphe.portal.service.IWmsWalletService;
import com.soonphe.portal.util.rate.TronApiUtil;
import com.soonphe.portal.vo.StatsCurrencyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 钱包 服务实现类
 * </p>
 *
 * @author soonphe
 * @since 2020-05-27
 */
@Service
public class WmsWalletServiceImpl extends ServiceImpl<WmsWalletMapper, WmsWallet> implements IWmsWalletService {

    private static Lock lock = new ReentrantLock();

    @Autowired
    private ISmsBaseService smsBaseService;

    @Autowired
    private IWmsWalletHistoryService historyService;

    @Override
    public int getTotalGame() {
        return baseMapper.getTotalGame();
    }

    @Override
    public StatsCurrencyVo getTotalCurrencyAmount() {
        return baseMapper.getTotalCurrencyAmount();
    }

    /**
     * 钱包转入转出
     *
     * @param tyAdvert
     * @return
     */
    @Override
    public WmsWalletHistory transferWallet(WmsWalletHistory tyAdvert) {
        // 加锁防止其他线程同时操作
        lock.lock();
        try {
            List<SmsBase> list = smsBaseService.list();
            if (list.size() > 0) {
                if (list.get(0).getTransferState() > 0) {
                    throw new BusinessException(ResultCode.VALIDATE_FAILED, "转账功能关闭");
                }
            }
            WmsWallet sendWallet = this.getById(tyAdvert.getSendId());
            if (sendWallet == null) {
                throw new BusinessException(ResultCode.VALIDATE_FAILED, "钱包不存在");
            }
            // 查找当前用户是否有推荐人
            List<WmsWallet> wmsWalletList = this.list(new QueryWrapper<WmsWallet>().lambda()
                    .likeLeft(WmsWallet::getRecommendId, sendWallet.getId()));
            // 当前用户如果没有推荐不允许转出钱包
            if (CollectionUtils.isEmpty(wmsWalletList) && tyAdvert.getState().equals(CommonsEnum.WALLET_TRANSFER_ENTER.getCode())) {
                throw new BusinessException(ResultCode.VALIDATE_FAILED, "没有推荐用户不允许提现");
            }
            if (sendWallet.getTrxAmount().compareTo(new BigDecimal("0.05")) < 0) {
                throw new BusinessException(ResultCode.VALIDATE_FAILED, "钱包手续费不足");
            }

            tyAdvert.setReceiveAddress(sendWallet.getTrxAddress());
            sendWallet.setTrxAmount(sendWallet.getTrxAmount().subtract(new BigDecimal("0.05")));
            if (tyAdvert.getType() == 1) {
                if (sendWallet.getTrxAmount().compareTo(tyAdvert.getAmount()) == -1) {
                    throw new BusinessException(ResultCode.VALIDATE_FAILED, "钱包余额不足");
                }
                String code = TronApiUtil.WALLET_TRANSFER(tyAdvert.getReceiveAddress(), "USDT", tyAdvert.getAmount().doubleValue());
                if (!"200".equals(code)) {
                    throw new BusinessException(ResultCode.VALIDATE_FAILED, "钱包区块异常");
                }

                sendWallet.setTrxAmount(sendWallet.getTrxAmount().subtract(tyAdvert.getAmount()));
                sendWallet.setWithdrawal(sendWallet.getWithdrawal().add(tyAdvert.getAmount()));
                this.updateById(sendWallet);
            } else if (tyAdvert.getType() == 2) {
                if (sendWallet.getFtAmount().compareTo(tyAdvert.getAmount()) == -1) {
                    throw new BusinessException(ResultCode.VALIDATE_FAILED, "钱包余额不足");
                }
                String code = TronApiUtil.WALLET_TRANSFER(tyAdvert.getReceiveAddress(), "FT", tyAdvert.getAmount().doubleValue());
                if (!"200".equals(code)) {
                    throw new BusinessException(ResultCode.VALIDATE_FAILED, "钱包区块异常");
                }
                sendWallet.setFtAmount(sendWallet.getFtAmount().subtract(tyAdvert.getAmount()));
                this.updateById(sendWallet);
            } else {
                throw new BusinessException(ResultCode.VALIDATE_FAILED, "交易类型错误");
            }
            tyAdvert.setSendAddress(sendWallet.getTrxAddress());
            tyAdvert.setSendId(sendWallet.getId());
            historyService.save(tyAdvert);
        } finally {
            lock.unlock();
            return tyAdvert;
        }
    }
}
