package com.soonphe.portal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合约表
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UmsContract对象", description = "合约表")
public class UmsContract implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "合约等级")
    private Integer level;

    @ApiModelProperty(value = "合约规格")
    private BigDecimal spec;

    @ApiModelProperty(value = "最大收益")
    private BigDecimal specMax;

    @ApiModelProperty(value = "是否可用 1可用 2不可用")
    private Integer state;

    @ApiModelProperty(value = "天数")
    private Integer expireDate;

    @ApiModelProperty(value = "每日游戏局数")
    private Integer dailyGameNum;


}
