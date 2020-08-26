package com.soonphe.portal.service;

import com.soonphe.portal.entity.WmsWallet;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soonphe.portal.entity.WmsWalletHistory;
import com.soonphe.portal.vo.StatsCurrencyVo;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * <p>
 * 钱包 服务类
 * </p>
 *
 * @author soonphe
 * @since 2020-05-27
 */
public interface IWmsWalletService extends IService<WmsWallet> {


    int getTotalGame();

    StatsCurrencyVo getTotalCurrencyAmount();

    /**
     * 转出/转入钱包
     *
     * @param tyAdvert
     * @return
     */
    WmsWalletHistory transferWallet(WmsWalletHistory tyAdvert);

}
