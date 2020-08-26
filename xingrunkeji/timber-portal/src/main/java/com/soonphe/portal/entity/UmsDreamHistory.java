package com.soonphe.portal.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 圆梦计划历史表
 * </p>
 *
 * @author soonphe
 * @since 2020-08-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UmsDreamHistory对象", description = "圆梦计划历史表")
public class UmsDreamHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    private Integer uid;

    @ApiModelProperty(value = "圆梦计划ID")
    private Integer did;

    @ApiModelProperty(value = "圆梦计划名称")
    private String name;

    @ApiModelProperty(value = "金额")
    private BigDecimal spec;

    @ApiModelProperty(value = "计划收益")
    private BigDecimal specProfit;

    @ApiModelProperty(value = "计划天数")
    private Integer planDate;

    @ApiModelProperty(value = "已进行天数")
    private Integer haveDate;

    @ApiModelProperty(value = "总收益")
    private BigDecimal totalProfit;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty(value = "过期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireDate;

    @ApiModelProperty(value = "是否可用 1可用 2不可用")
    private Integer state;

}
