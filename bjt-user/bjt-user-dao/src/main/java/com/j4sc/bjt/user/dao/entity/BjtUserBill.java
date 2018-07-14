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
     * 账单类型（1房租、2物业费）
     */
    @ApiModelProperty(value = "账单类型（1房租、2物业费）")
    private Integer type;
    /**
     * 缴费方式(1是支付宝，2是微信，3是银行卡，4是其它)
     */
    @ApiModelProperty(value = "缴费方式(1是支付宝，2是微信，3是银行卡，4是其它)")
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
    /**
     * 公司编号
     */
    @ApiModelProperty(value = "公司编号")
    @TableField("company_id")
    private Integer companyId;
    /**
     * 楼宇编号
     */
    @ApiModelProperty(value = "楼宇编号")
    @TableField("build_id")
    private Integer buildId;
    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    @TableField("company_name")
    private String companyName;
    /**
     * 门牌号
     */
    @ApiModelProperty(value = "门牌号")
    @TableField("house_number")
    private String houseNumber;
    /**
     * 合同主键编号
     */
    @TableId(value = "agreement_id", type = IdType.AUTO)
    private Integer agreementId;

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

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Integer getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Integer agreementId) {
        this.agreementId = agreementId;
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
        ", companyName=" + companyName +
        ", companyId=" + companyId +
        ", houseNumber=" + houseNumber +
        ", buildId=" + buildId +
        ", agreementId=" + agreementId +
        "}";
    }
}
