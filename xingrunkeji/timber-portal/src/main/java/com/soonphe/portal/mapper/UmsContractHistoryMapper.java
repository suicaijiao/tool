package com.soonphe.portal.mapper;

import com.soonphe.portal.entity.UmsContractHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soonphe.portal.model.vo.GameContractVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户合约关联表 Mapper 接口
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@Mapper
public interface UmsContractHistoryMapper extends BaseMapper<UmsContractHistory> {

    /**
     * 合约统计
     *
     * @return
     */
    @Select("select sum(spec) from ums_contract c,ums_contract_history h " +
            "where c.id = h.cid " +
            "and h.create_date >= #{startTime} " +
            "and h.create_date <= #{endTime}")
    BigDecimal getContractTotal(@Param(value = "startTime") Date startTime,
                                @Param(value = "endTime") Date endTime);

    /**
     * 查询用户合约内获得奖励
     *
     * @param userId
     * @return
     */
    GameContractVo contractGameReward(@Param("userId") Integer userId);
}
