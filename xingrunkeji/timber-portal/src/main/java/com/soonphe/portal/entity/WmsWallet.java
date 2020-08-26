package com.soonphe.portal.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 钱包
 * </p>
 *
 * @author soonphe
 * @since 2020-05-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "WmsWallet对象", description = "钱包")
public class WmsWallet implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "私钥地址")
    private String privateKey;
    @ApiModelProperty(value = "TRX地址")
    private String trxAddress;

    /**
     * USDT数量
     */
    @ApiModelProperty(value = "USDT数量")
    private BigDecimal trxAmount;
    /**
     * DOT数量
     */
    @ApiModelProperty(value = "ft数量")
    private BigDecimal ftAmount;

    @ApiModelProperty(value = "cny数量")
    private BigDecimal cnyAmount;
    @ApiModelProperty(value = "游戏FT数量")
    private BigDecimal gameFtAmount;
    @ApiModelProperty(value = "助记词")
    private String wordKey;
    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "状态 0未更新 1已更新 2已绑定")
    private Integer state;
    @ApiModelProperty(value = "密钥")
    private String gSecret;
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "推荐人")
    private String recommendId;
    @ApiModelProperty(value = "推荐码")
    private String recommendCode;
    @ApiModelProperty(value = "推荐人数")
    private Integer recommendCount;
    @ApiModelProperty(value = "推荐有效人数")
    private Integer recommendValidCount;
    @ApiModelProperty(value = "推荐增加游戏局数")
    private Integer recommendGameNum;
    @ApiModelProperty(value = "推荐增加释放速度")
    private Double recommendReleaseSpeed;
    @ApiModelProperty(value = "团队人数")
    private Integer TeamNum;
    @ApiModelProperty(value = "团队有效人数")
    private Integer TeamValidNum;
    /**
     * 用户等级  -1体验  0有效  1s1  2s2  3s3  4s4
     */
    @ApiModelProperty(value = "用户等级  -1体验  0有效  1s1  2s2  3s3  4s4")
    private Integer userLevel;

    @ApiModelProperty(value = "低级用户持有数量")
    private Integer userLevelLower;


    @ApiModelProperty(value = "合约等级")
    private Integer contractLeve;
    @ApiModelProperty(value = "合约每日游戏局数")
    private Integer contractGameNum;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "合约到期时间")
    private Date contractExpireDate;

    @ApiModelProperty(value = "New合约等级")
    private Integer contractNewLeve;
    @ApiModelProperty(value = "New合约每日游戏局数")
    private Integer contractNewGameNum;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "New合约到期时间")
    private Date contractNewExpireDate;

    @ApiModelProperty(value = "增益包等级")
    private Integer gainpackLevel;
    @ApiModelProperty(value = "增益包每日游戏局数")
    private Integer gainpackGameNum;

    @ApiModelProperty(value = "每日游戏局数")
    private Integer gameNum;

    /**
     * USDT
     */
    @ApiModelProperty(value = "赢得的cny数量")
    private BigDecimal winCny;

    /**
     * DOT
     */
    @ApiModelProperty(value = "赢得的FT数量")
    private BigDecimal winFt;
    @ApiModelProperty(value = "累计释放的FT数量")
    private BigDecimal releaseTotal;
    @ApiModelProperty(value = "释放系数")
    private BigDecimal releaseRatio;

    @ApiModelProperty(value = "总游戏次数")
    private Integer gameTotal;
    @ApiModelProperty(value = "总中奖")
    private Integer WinnerTotal;

    @ApiModelProperty(value = "增益包分红FT分红")
    private BigDecimal gainpacketFtAmount;
    @ApiModelProperty(value = "增益包分红CNY分红")
    private BigDecimal gainpacketCnyAmount;
    @ApiModelProperty(value = "游戏FT分红")
    private BigDecimal recommendFtAmount;
    @ApiModelProperty(value = "游戏CNY分红")
    private BigDecimal recommendCnyAmount;
    @ApiModelProperty(value = "社群FT分红")
    private BigDecimal teamFtAmount;
    @ApiModelProperty(value = "社群CNY分红")
    private BigDecimal teamCnyAmount;

    @ApiModelProperty(value = "USDT充值")
    private BigDecimal recharge;
    @ApiModelProperty(value = "USDT提现")
    private BigDecimal withdrawal;


}
