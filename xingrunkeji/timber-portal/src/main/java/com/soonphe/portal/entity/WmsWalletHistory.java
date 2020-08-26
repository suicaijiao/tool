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
 * 钱包转出转入历史记录
 * </p>
 *
 * @author soonphe
 * @since 2020-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "WmsWalletHistory对象", description = "钱包转出转入历史记录")
public class WmsWalletHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "类型 1转入 2转出")
    private Integer state;

    @ApiModelProperty(value = "币种 1TRX 2FT")
    private Integer type;

    @ApiModelProperty(value = "数量")
    private BigDecimal amount;


    @ApiModelProperty(value = "接收者ID")
    private Integer receiveId;

    @ApiModelProperty(value = "发送者ID")
    private Integer sendId;

    @ApiModelProperty(value = "接收地址")
    private String receiveAddress;

    @ApiModelProperty(value = "发送地址")
    private String sendAddress;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


}
