package com.soonphe.portal.mapper;

import com.soonphe.portal.entity.TmsTrade;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soonphe.portal.vo.TradeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 交易表 Mapper 接口
 * </p>
 *
 * @author soonphe
 * @since 2020-05-27
 */
@Mapper
public interface TmsTradeMapper extends BaseMapper<TmsTrade> {

    /**
     * 叠加获取
     * @param state
     * @param type
     * @return
     */
    @Select("select price,sum(amount-deal_amount) amount from tms_trade where state = #{state} " +
            "and type = #{type} " +
            "GROUP BY price " +
            "ORDER BY price ASC LIMIT 0,5 ")
    List<TradeVo> getTradeList(@Param(value = "state") int state,
                               @Param(value = "type") int type);
}
