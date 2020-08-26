package com.soonphe.portal.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 圆梦计划表
 * </p>
 *
 * @author soonphe
 * @since 2020-08-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UmsDream对象", description = "圆梦计划表")
public class UmsDream implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "计划名称")
    private String name;

    @ApiModelProperty(value = "计划金额")
    private BigDecimal spec;

    @ApiModelProperty(value = "最大收益")
    private BigDecimal specProfit;

    @ApiModelProperty(value = "是否可用 1可用 2不可用")
    private Integer state;

    @ApiModelProperty(value = "计划天数")
    private Integer planDate;


}
