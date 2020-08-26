package com.soonphe.portal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soonphe.portal.entity.UmsContractHistory;
import com.soonphe.portal.model.vo.GameContractVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户合约关联表 服务类
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
public interface IUmsContractHistoryService extends IService<UmsContractHistory> {

    BigDecimal getContractTotal(Date startTime, Date endTime);

    /**
     * 查询用户合约内获得奖励
     *
     * @param userId
     * @return
     */
    GameContractVo contractGameReward(Integer userId);

}
