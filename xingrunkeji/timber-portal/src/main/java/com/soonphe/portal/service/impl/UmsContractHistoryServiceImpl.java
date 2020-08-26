package com.soonphe.portal.service.impl;

import com.soonphe.portal.entity.UmsContractHistory;
import com.soonphe.portal.mapper.UmsContractHistoryMapper;
import com.soonphe.portal.model.vo.GameContractVo;
import com.soonphe.portal.service.IUmsContractHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 用户合约关联表 服务实现类
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@Service
public class UmsContractHistoryServiceImpl extends ServiceImpl<UmsContractHistoryMapper, UmsContractHistory> implements IUmsContractHistoryService {

    @Override
    public BigDecimal getContractTotal(Date startTime, Date endTime) {
        return baseMapper.getContractTotal(startTime, endTime);
    }

    /**
     * 查询用户合约内获得奖励
     *
     * @param userId
     * @return
     */
    @Override
    public GameContractVo contractGameReward(Integer userId) {
        return baseMapper.contractGameReward(userId);
    }


}
