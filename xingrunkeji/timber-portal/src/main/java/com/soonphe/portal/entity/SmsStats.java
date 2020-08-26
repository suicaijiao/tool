package com.soonphe.portal.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 消息表
 * </p>
 *
 * @author soonphe
 * @since 2020-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "MmsNews对象", description = "消息表")
public class SmsStats implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty(value = "用户游戏次数")
    private Integer userGameCount;
    @ApiModelProperty(value = "用户获奖次数")
    private Integer userWinCount;

    @ApiModelProperty(value = "CNY奖励")
    @TableField(exist = false)
    private BigDecimal gameCny;
    @ApiModelProperty(value = "FT奖励")
    @TableField(exist = false)
    private BigDecimal gameFt;
    @ApiModelProperty(value = "沉淀数量")
    @TableField(exist = false)
    private BigDecimal deposition;
    @ApiModelProperty(value = "有效沉淀")
    @TableField(exist = false)
    private BigDecimal depositionValid;
    @ApiModelProperty(value = "机器人CNY矿工费")
    @TableField(exist = false)
    private BigDecimal androidCny;
    @ApiModelProperty(value = "机器人FT矿工费")
    @TableField(exist = false)
    private BigDecimal androidFt;
    @ApiModelProperty(value = "机器人门票总数")
    @TableField(exist = false)
    private BigDecimal androidTicket;



    @ApiModelProperty(value = "机器人游戏次数")
    private Integer androidGameCount;
    @ApiModelProperty(value = "机器人获奖次数")
    private Integer androidWinCount;
    /**
     * 当天游戏总门票数
     */
    @ApiModelProperty(value = "门票总计")
    private BigDecimal ticketCount;

    /**
     * DOT总数量
     */
    @ApiModelProperty(value = "矿工FT总计")
    private BigDecimal minerFtCount;
    /**
     * 平台收取游戏用户的USDT ,扣除费率0.13
     */
    @ApiModelProperty(value = "矿工CNY数量")
    private BigDecimal minerCnyCount;

    @ApiModelProperty(value = "合约锁仓")
    private BigDecimal contractCount;
    @ApiModelProperty(value = "增益包锁仓")
    private BigDecimal gainpacketCount;
    @ApiModelProperty(value = "手续费")
    private BigDecimal transferCount;

    @ApiModelProperty(value = "交易购买")
    private BigDecimal tradeBuy;
    @ApiModelProperty(value = "交易卖出")
    private BigDecimal tradeSell;

    @ApiModelProperty(value = "推荐奖励FT")
    private BigDecimal recommendFt;
    @ApiModelProperty(value = "推荐奖励CNY")
    private BigDecimal recommendCny;
    @ApiModelProperty(value = "增益包奖励FT")
    private BigDecimal gainpacketFt;
    @ApiModelProperty(value = "增益包奖励CNY")
    private BigDecimal gainpacketCny;
    @ApiModelProperty(value = "社群分红FT")
    private BigDecimal teamFt;
    @ApiModelProperty(value = "社群分红CNY")
    private BigDecimal teamCny;

    @ApiModelProperty(value = "矿池释放记录")
    private BigDecimal oreCount;
}
