package com.soonphe.portal.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author: soonphe
 * @date: 2020-06-30
 * @description: 交易历史记录分页查询VO
 */
@Data
public class TradeHistoryPageVo {

    @ApiParam(required = true, value = "页码")
    private int pageNum = 0;
    @ApiParam(required = true, value = "叶长")
    private int pageSize= 10;
    @ApiParam(required = true, value = "起始时间",example = "2020-06-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @ApiParam(required = true, value = "截至时间",example = "2020-07-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    @ApiParam(required = true, value = "根据price排序")
    private int price;
    @ApiParam(required = true, value = "根据id排序")
    private int id;

}