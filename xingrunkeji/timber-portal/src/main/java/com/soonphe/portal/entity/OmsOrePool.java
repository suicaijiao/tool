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
    * 矿池表
    * </p>
*
* @author soonphe
* @since 2020-05-27
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    @ApiModel(value="OmsOrePool对象", description="矿池表")
    public class OmsOrePool implements Serializable {

    private static final long serialVersionUID = 1L;

            @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

            @ApiModelProperty(value = "矿池余额")
    private BigDecimal balance;

            @ApiModelProperty(value = "累计释放")
    private BigDecimal totalRelease;

            @ApiModelProperty(value = "释放系数")
    private BigDecimal ratio;


}
