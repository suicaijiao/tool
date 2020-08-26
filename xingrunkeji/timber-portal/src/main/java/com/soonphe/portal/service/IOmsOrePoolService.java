package com.soonphe.portal.service;

import com.soonphe.portal.entity.OmsOrePool;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 矿池表 服务类
 * </p>
 *
 * @author soonphe
 * @since 2020-05-27
 */
public interface IOmsOrePoolService extends IService<OmsOrePool> {


    BigDecimal getFtRate();

    BigDecimal getYesterdayFtRate();
    //
    BigDecimal getFtTicket();
}
