package com.soonphe.portal.vo;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author: soonphe
 * @date: 2020-06-30
 * @description: 每日游戏次数Vo
 */
@Data
public class GameDailyNumVo {

    @ApiParam(value = "钱包ID")
    private int uid;

    @ApiParam(value = "起始时间",example = "2020-06-01")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

}