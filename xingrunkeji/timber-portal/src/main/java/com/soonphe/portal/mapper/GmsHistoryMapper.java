package com.soonphe.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.soonphe.portal.entity.GmsHistory;
import com.soonphe.portal.vo.DayUserGameStatisticsVo;
import com.soonphe.portal.vo.QueryUserGameStatisticsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 游戏记录表 Mapper 接口
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@Mapper
public interface GmsHistoryMapper extends BaseMapper<GmsHistory> {

    List<DayUserGameStatisticsVo> selectDayUserGameStatistics(QueryUserGameStatisticsVo query);

    int countDayUserGameStatistics(QueryUserGameStatisticsVo query);
}
