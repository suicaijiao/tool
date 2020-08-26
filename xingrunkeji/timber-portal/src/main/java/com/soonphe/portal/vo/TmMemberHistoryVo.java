package com.soonphe.portal.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author: soonphe
 * @date: 2019-05-16 16:33
 * @description:  历史记录查询VO
 */
@Getter
@Setter
public class TmMemberHistoryVo {

    @ApiParam(required = true, value = "页码")
    private int pageNum = 0;
    @ApiParam(required = true, value = "叶长")
    private int pageSize= 10;

    @ApiParam(value = "会员ID")
    private Long memberId;
    @ApiParam(value = "起始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startDate;
    @ApiParam(value = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endDate;
}
