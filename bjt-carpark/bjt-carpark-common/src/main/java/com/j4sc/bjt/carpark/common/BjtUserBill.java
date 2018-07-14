package com.j4sc.bjt.carpark.common;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 用户账单
 * </p>
 *
 * @author LongRou
 * @since 2018-04-19
 */
@TableName("bjt_user_bill")
public class BjtUserBill extends Model<BjtUserBill> {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "账单编号")
    @TableId(value = "bill_id", type = IdType.AUTO)
    private Integer billId;
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户编号")
    @TableField("user_id")
    private String userId;
    /**
     * 缴费金额
     */
    @ApiModelProperty(value = "缴费金额")
    private BigDecimal money;
    /**
     * 状态（0逾期、1已缴纳、2未缴纳等）
     */
    @ApiModelProperty(value = "状态（0逾期、1已缴纳、2未缴纳等）")
    private Integer status;
    /**
     * 账单类型（租金、停车、物业费等）
     */
    @ApiModelProperty(value = "账单类型（1租金、2停车、3物业费等）")
    private Integer type;
    /**
     * 缴费方式(1是支付宝，2是微信，3是银行卡)
     */
    @ApiModelProperty(value = "缴费方式(1是支付宝，2是微信，3是银行卡)")
    @TableField("pay_way")
    private Integer payWay;
    /**
     * 截止时间
     */
    @ApiModelProperty(value = "截止时间")
    @TableField("cut_off_time")
    private Long cutOffTime;
    /**
     * 缴纳时间
     */
    @ApiModelProperty(value = "缴纳时间")
    @TableField("pay_time")
    private Long payTime;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long ctime;
    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    private String username;

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public Long getCutOffTime() {
        return cutOffTime;
    }

    public void setCutOffTime(Long cutOffTime) {
        this.cutOffTime = cutOffTime;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    protected Serializable pkVal() {
        return this.billId;
    }

    @Override
    public String toString() {
        return "BjtUserBill{" +
                "billId=" + billId +
                ", userId=" + userId +
                ", money=" + money +
                ", status=" + status +
                ", type=" + type +
                ", payWay=" + payWay +
                ", cutOffTime=" + cutOffTime +
                ", payTime=" + payTime +
                ", remark=" + remark +
                ", ctime=" + ctime +
                ", username=" + username +
                "}";
    }
}

