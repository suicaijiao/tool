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
    * 矿池释放记录表
    * </p>
*
* @author soonphe
* @since 2020-05-27
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @ApiModel(value="OmsOrePoolHistory对象", description="矿池释放记录表")
    public class OmsOrePoolHistory implements Serializable {

    private static final long serialVersionUID = 1L;

            @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

            @ApiModelProperty(value = "释放数量")
    private BigDecimal amount;

            @ApiModelProperty(value = "创建时间")
            @TableField(fill = FieldFill.INSERT)
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public OmsOrePoolHistory( ) {
    }

    public OmsOrePoolHistory(BigDecimal amount, Date createTime) {
        this.amount = amount;
        this.createTime = createTime;
    }
}
