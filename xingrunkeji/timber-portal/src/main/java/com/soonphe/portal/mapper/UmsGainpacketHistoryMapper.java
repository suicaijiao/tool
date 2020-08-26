package com.soonphe.portal.mapper;

import com.soonphe.portal.entity.UmsGainpacketHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 增益表 Mapper 接口
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@Mapper
public interface UmsGainpacketHistoryMapper extends BaseMapper<UmsGainpacketHistory> {

    /**
     * 合约统计
     * @return
     */
    @Select("select sum(spec) from ums_gainpacket c,ums_gainpacket_history h " +
            "where c.id = h.gid " +
            "and h.create_date >= #{startTime} " +
            "and  h.create_date <= #{endTime}")
    BigDecimal getGainpacketTotal(@Param(value = "startTime") Date startTime,
                                  @Param(value = "endTime") Date endTime);

}
