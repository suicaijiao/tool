package com.soonphe.portal.service;

import com.soonphe.portal.entity.UmsCommunityHistory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.soonphe.portal.vo.ExtensionVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 * 社群收益记录表 服务类
 * </p>
 *
 * @author soonphe
 * @since 2020-06-05
 */
public interface IUmsCommunityHistoryService extends IService<UmsCommunityHistory> {

    ExtensionVo getCommunityHistory(Date startTime, Date endTime);

    /**
     * 社群奖励分配
     */
    void communityRewardDistribution();

}
