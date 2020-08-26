package com.soonphe.portal.entity;

    import java.math.BigDecimal;

    import com.baomidou.mybatisplus.annotation.FieldFill;
    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableField;
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
    * 游戏充提记录
    * </p>
*
* @author soonphe
* @since 2020-06-11
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @ApiModel(value="WmsWalletFtHistory对象", description="游戏充提记录")
    public class WmsWalletFtHistory implements Serializable {

    private static final long serialVersionUID = 1L;

            @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "uid")
    private Integer uid;

            @ApiModelProperty(value = "类型 1充值 2提现")
    private Integer type;

            @ApiModelProperty(value = "cny数量")
    private BigDecimal ftAmount;

            @ApiModelProperty(value = "ft数量")
    private BigDecimal gameFtAmount;

            @ApiModelProperty(value = "矿工费用")
    private BigDecimal minerAmount;

            @ApiModelProperty(value = "创建时间")
            @TableField(fill = FieldFill.INSERT)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


}
