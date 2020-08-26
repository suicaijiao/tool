package com.soonphe.portal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.soonphe.portal.entity.OmsOrePool;
import com.soonphe.portal.entity.SmsBase;
import com.soonphe.portal.mapper.OmsOrePoolMapper;
import com.soonphe.portal.service.IOmsOrePoolService;
import com.soonphe.portal.service.ISmsBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 矿池表 服务实现类
 * </p>
 *
 * @author soonphe
 * @since 2020-05-27
 */
@Service
public class OmsOrePoolServiceImpl extends ServiceImpl<OmsOrePoolMapper, OmsOrePool> implements IOmsOrePoolService {

    @Autowired
    private ISmsBaseService smsBaseService;

    @Override
    public BigDecimal getFtRate() {
        //手动汇率
//        BigDecimal rate1 = new BigDecimal(1);
//        List<SmsBase> bases = smsBaseService.list();
//        if (bases.size() > 0) {
//            rate1 = bases.get(0).getPricePublic();
//            return rate1;
//        }


        //计算汇率 0.15-0.29 0.3-0.59 0.6-1.19 1.2-1.4
        OmsOrePool pool = this.list().get(0);
        BigDecimal releaseNum = pool.getTotalRelease();
        BigDecimal rate;
        if (releaseNum.compareTo(new BigDecimal("50000000")) >= 1) {
            rate = (releaseNum.subtract(new BigDecimal(50000000)))
                    .multiply(new BigDecimal("0.000000004")).add(new BigDecimal("1.2"));
        } else if (releaseNum.compareTo(new BigDecimal("10000000")) >= 1) {
            rate = (releaseNum.subtract(new BigDecimal(10000000)))
                    .multiply(new BigDecimal("0.00000001475")).add(new BigDecimal("0.6"));
        } else if (releaseNum.compareTo(new BigDecimal("5000000")) >= 1) {
            rate = (releaseNum.subtract(new BigDecimal(5000000)))
                    .multiply(new BigDecimal("0.000000058")).add(new BigDecimal("0.3"));
        } else {
            rate = releaseNum.multiply(new BigDecimal("0.000000028")).add(new BigDecimal("0.15"));
        }
        return rate.setScale(4, BigDecimal.ROUND_DOWN);

    }

    @Override
    public BigDecimal getYesterdayFtRate() {
        OmsOrePool pool = this.list().get(0);

        BigDecimal releaseNum = pool.getRatio();
        BigDecimal rate;
        if (releaseNum.compareTo(new BigDecimal("50000000")) >= 1) {
            rate = (releaseNum.subtract(new BigDecimal(50000000)))
                    .multiply(new BigDecimal("0.000000004")).add(new BigDecimal("1.2"));
        } else if (releaseNum.compareTo(new BigDecimal("10000000")) >= 1) {
            rate = (releaseNum.subtract(new BigDecimal(10000000)))
                    .multiply(new BigDecimal("0.00000001475")).add(new BigDecimal("0.6"));
        } else if (releaseNum.compareTo(new BigDecimal("5000000")) >= 1) {
            rate = (releaseNum.subtract(new BigDecimal(5000000)))
                    .multiply(new BigDecimal("0.000000058")).add(new BigDecimal("0.3"));
        } else {
            rate = releaseNum.multiply(new BigDecimal("0.000000028")).add(new BigDecimal("0.15"));
        }
        return rate.setScale(4, BigDecimal.ROUND_DOWN);
    }

    @Override
    public BigDecimal getFtTicket() {
        //1CNY对应的FT
        BigDecimal ticket = new BigDecimal("1.00").divide(getFtRate(), 4, BigDecimal.ROUND_DOWN);
        return ticket;
    }
}
