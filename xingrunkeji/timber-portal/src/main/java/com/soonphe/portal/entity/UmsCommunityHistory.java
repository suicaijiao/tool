package com.soonphe.portal.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.math.BigDecimal;
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
 * 社群收益记录表
 * </p>
 *
 * @author soonphe
 * @since 2020-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UmsCommunityHistory对象", description = "社群收益记录表")
public class UmsCommunityHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "收益用户ID")
    private Integer uid;

    @ApiModelProperty(value = "社群收益DOT")
    private BigDecimal minerDot;

    @ApiModelProperty(value = "矿工收益USDT")
    private BigDecimal minerUsdt;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty(value = "收益类型 1.社群游戏收益1 2.社群权益分红")
    private Integer type;

    public UmsCommunityHistory() {
    }

    public UmsCommunityHistory(Integer guid, Integer uid, BigDecimal minerDot, BigDecimal minerUsdt, Date createDate, Integer type) {
        this.uid = uid;
        this.minerDot = minerDot;
        this.minerUsdt = minerUsdt;
        this.createDate = createDate;
        this.type = type;
    }
}
