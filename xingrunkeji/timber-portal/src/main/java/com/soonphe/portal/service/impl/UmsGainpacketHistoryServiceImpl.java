package com.soonphe.portal.service.impl;

import com.soonphe.portal.entity.UmsGainpacketHistory;
import com.soonphe.portal.mapper.UmsGainpacketHistoryMapper;
import com.soonphe.portal.service.IUmsGainpacketHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 增益表 服务实现类
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@Service
public class UmsGainpacketHistoryServiceImpl extends ServiceImpl<UmsGainpacketHistoryMapper, UmsGainpacketHistory> implements IUmsGainpacketHistoryService {

    @Override
    public BigDecimal getGainpacketTotal(Date startTime, Date endTime) {
        return baseMapper.getGainpacketTotal(startTime,endTime);
    }
}
