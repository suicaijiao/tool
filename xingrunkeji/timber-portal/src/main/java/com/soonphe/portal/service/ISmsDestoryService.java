package com.soonphe.portal.service;

import com.soonphe.portal.entity.SmsDestory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 销毁表 服务类
 * </p>
 *
 * @author soonphe
 * @since 2020-06-29
 */
public interface ISmsDestoryService extends IService<SmsDestory> {


    BigDecimal getTotal();
}
