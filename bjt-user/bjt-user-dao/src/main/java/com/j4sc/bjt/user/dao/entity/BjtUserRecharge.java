package com.j4sc.bjt.user.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 用户充值记录
 * </p>
 *
 * @author LongRou
 * @since 2018-04-19
 */
@TableName("bjt_user_recharge")
public class BjtUserRecharge extends Model<BjtUserRecharge> {

    private static final long serialVersionUID = 1L;

    /**
     * 充值编号
     */
    @ApiModelProperty(value = "充值编号")
    @TableId(value = "recharge_id", type = IdType.AUTO)
    private Integer rechargeId;
    /**
     * 用户编号
     */
    @ApiModelProperty(value = "用户编号")
    @TableField("user_id")
    private String userId;
    /**
     * 充值金额
     */
    @ApiModelProperty(value = "充值金额")
    private BigDecimal money;
    /**
     * 交易流水号
     */
    @ApiModelProperty(value = "交易流水号")
    @TableField("trade_no")
    private String tradeNo;
    /**
     * 充值详情
     */
    @ApiModelProperty(value = "充值详情")
    private String detail;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long ctime;
    /**
     * 充值的类型（0支付宝、1微信、2银行卡）
     */
    @ApiModelProperty(value = "充值的类型（0支付宝、1微信、2银行卡）")
    private Integer type;


    public Integer getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(Integer rechargeId) {
        this.rechargeId = rechargeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    protected Serializable pkVal() {
        return this.rechargeId;
    }

    @Override
    public String toString() {
        return "BjtUserRecharge{" +
        "rechargeId=" + rechargeId +
        ", userId=" + userId +
        ", money=" + money +
        ", tradeNo=" + tradeNo +
        ", detail=" + detail +
        ", ctime=" + ctime +
        ", type=" + type +
        "}";
    }
}
