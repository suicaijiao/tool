package com.soonphe.portal.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @description 查询用户游戏记录参数实体类
 * @author: suicaijiao
 * @create: 2020-08-22 17:12
 **/
@Getter
@Setter
public class QueryUserGameStatisticsVo {

    /**
     * 当前页号
     */
    private int pageIndex;

    /**
     * 每页查询数据数量
     */
    private int pageSize;

    /**
     * 查询开始时间
     */
    private String startDate;

    /**
     * 查询结束时间
     */
    private String endDate;
}
