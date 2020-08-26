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
 * 用户合约关联表
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UmsContractHistory对象", description = "用户合约关联表")
public class UmsContractHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    private Integer uid;

    @ApiModelProperty(value = "合约ID")
    private Integer cid;

    @ApiModelProperty(value = "合约规格")
    private BigDecimal spec;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    /**
     * 合约状态 0.合约到期，1.合约未到期
     */
    @ApiModelProperty(value = "合约状态")
    private Integer status;

    /**
     * 合约结束时间
     */
    @ApiModelProperty(value = "合约结束时间")
    private Date contractEndDate;

    /**
     * 合约提前到期时间
     */
    @ApiModelProperty(value = "合约提前到期时间")
    private Date contractActualEndDate;


}
