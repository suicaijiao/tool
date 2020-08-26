package com.soonphe.portal.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

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

/**
 * <p>
 * 游戏记录表
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "GmsHistory对象", description = "游戏记录表")
public class GmsHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "局数ID")
    private String gid;

    @ApiModelProperty(value = "中奖人序号")
    private Integer wno;


    @ApiModelProperty(value = "基础奖励USDT")
    private BigDecimal baseUsdt;

    @ApiModelProperty(value = "基础奖励DOT")
    private BigDecimal baseDot;


    @ApiModelProperty(value = "用户ID")
    private Integer uid;

    @ApiModelProperty(value = "门票")
    private BigDecimal ticket;

    @ApiModelProperty(value = "奖励USDT")
    private BigDecimal rewardUsdt;

    @ApiModelProperty(value = "奖励DOT")
    private BigDecimal rewardDot;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    /**
     * 座位号
     */
    @ApiModelProperty(value = "座位号")
    private Integer seat;

    /**
     * 游戏状态 0-未结束 1-已结束
     */
    @ApiModelProperty(value = "游戏状态 0-未结束 1-已结束")
    private Integer status;

    /**
     * 中奖状态 0-未中奖 1-已中奖
     */
    @ApiModelProperty(value = "中奖状态 0-未中奖 1-已中奖")
    private Integer winStatus;

}
