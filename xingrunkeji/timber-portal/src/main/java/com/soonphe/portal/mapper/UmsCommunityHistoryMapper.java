package com.soonphe.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soonphe.portal.entity.UmsCommunityHistory;
import com.soonphe.portal.vo.ExtensionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

/**
 * <p>
 * 社群收益记录表 Mapper 接口
 * </p>
 *
 * @author soonphe
 * @since 2020-06-05
 */
@Mapper
public interface UmsCommunityHistoryMapper extends BaseMapper<UmsCommunityHistory> {

    /**
     * 获取社群FT和CNY总数
     * @param startTime
     * @param endTime
     * @return
     */
    @Select("select sum(miner_ft) personalFt,sum(miner_cny) personalCny " +
            "from ums_community_history " +
            "where create_date >= #{startTime} " +
            "and  create_date <= #{endTime}")
    ExtensionVo getCommunityHistory(@Param(value = "startTime") Date startTime,
                                    @Param(value = "endTime") Date endTime);

}
