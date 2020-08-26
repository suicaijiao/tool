package com.soonphe.portal.service;

import com.soonphe.portal.entity.UmsGainpacketHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 增益表 服务类
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
public interface IUmsGainpacketHistoryService extends IService<UmsGainpacketHistory> {


    BigDecimal getGainpacketTotal(Date startTime, Date endTime);
}
