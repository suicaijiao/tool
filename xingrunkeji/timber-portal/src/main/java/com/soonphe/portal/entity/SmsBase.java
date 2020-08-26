package com.soonphe.portal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 平台基础数据表
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "SmsBase对象", description = "平台基础数据表")
public class SmsBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "是否开启转账 0开启  1关闭")
    private Integer transferState;

    @ApiModelProperty(value = "汇率")
    private BigDecimal priceCny;

    @ApiModelProperty(value = "发行价")
    private BigDecimal pricePublic;

    @ApiModelProperty(value = "矿工费")
    private BigDecimal miner;

    @ApiModelProperty(value = "未中奖可分金额")
    private BigDecimal noWin;

    @ApiModelProperty(value = "游戏模式 1单机 2多人匹配")
    private Integer gameModel;

    @ApiModelProperty(value = "中奖释放速度")
    private BigDecimal winSpeed;

    @ApiModelProperty(value = "新用户赠送FT数量")
    private BigDecimal newUserGiveNum;

    @ApiModelProperty(value = "直推收益")
    private BigDecimal direct;

    @ApiModelProperty(value = "间推收益")
    private BigDecimal indirect;

    @ApiModelProperty(value = "无限收益")
    private BigDecimal infinite;


}
