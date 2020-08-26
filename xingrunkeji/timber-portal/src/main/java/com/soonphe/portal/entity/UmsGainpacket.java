package com.soonphe.portal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 增益表
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UmsGainpacket对象", description = "增益表")
public class UmsGainpacket implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "增益包等级")
    private Integer level;

    @ApiModelProperty(value = "增益包锁仓规格")
    private BigDecimal spec;

    @ApiModelProperty(value = "每日游戏增加局数")
    private Integer dailyGameNum;

    @ApiModelProperty(value = "是否可用 1可用 2不可用")
    private Integer state;

    @ApiModelProperty(value = "余量")
    private Integer amount;
}
