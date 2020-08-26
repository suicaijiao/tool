package com.soonphe.portal.service.impl;

import com.soonphe.portal.entity.SmsDestory;
import com.soonphe.portal.mapper.SmsDestoryMapper;
import com.soonphe.portal.service.ISmsDestoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 销毁表 服务实现类
 * </p>
 *
 * @author soonphe
 * @since 2020-06-29
 */
@Service
public class SmsDestoryServiceImpl extends ServiceImpl<SmsDestoryMapper, SmsDestory> implements ISmsDestoryService {

    @Override
    public BigDecimal getTotal() {
        return baseMapper.getTotal();
    }
}
