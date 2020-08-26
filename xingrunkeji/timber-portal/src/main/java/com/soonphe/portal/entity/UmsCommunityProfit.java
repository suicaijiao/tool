package com.soonphe.portal.entity;

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
 * 社群收益表
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UmsCommunityProfit对象", description = "社群收益表")
public class UmsCommunityProfit implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "等级")
    private Integer level;

    @ApiModelProperty(value = "直推规格")
    private Integer directSpec;

    @ApiModelProperty(value = "团队规格")
    private Integer teamSpec;

    @ApiModelProperty(value = "每日游戏增加局数")
    private Integer dailyGameNum;

    @ApiModelProperty(value = "释放加速")
    private Double dailyRelease;


}
