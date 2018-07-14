package com.j4sc.bjt.carpark.dao.entity;

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
 * 停车缴费记录表
 * </p>
 *
 * @author LongRou
 * @since 2018-05-15
 */
@TableName("bjt_carpark_payment")
public class BjtCarparkPayment extends Model<BjtCarparkPayment> {

    private static final long serialVersionUID = 1L;

    /**
     * 缴费编号
     */
    @ApiModelProperty("缴费编号")
    @TableId(value = "payment_id", type = IdType.AUTO)
    private Integer paymentId;
    /**
     * 车辆编号
     */
    @ApiModelProperty("车辆编号")
    @TableField("car_id")
    private Integer carId;
    /**
     * 车牌号
     */
    @ApiModelProperty("车牌号")
    @TableField("plate_number")
    private String plateNumber;
    /**
     * 每月/每年 单价费用
     */
    @ApiModelProperty("每月/每年 单价费用")
    private BigDecimal fee;
    /**
     * 缴纳月数/年数
     */
    @ApiModelProperty("缴纳月数/年数")
    private Integer number;
    /**
     * 生效日期
     */
    @ApiModelProperty("生效日期")
    @TableField("begin_time")
    private Long beginTime;
    /**
     * 截止日期
     */
    @ApiModelProperty("截止日期")
    @TableField("end_time")
    private Long endTime;
    /**
     * 合计金额
     */
    @ApiModelProperty("合计金额")
    @TableField("total_fee")
    private BigDecimal totalFee;
    /**
     * 缴费方式(1是支付宝，2是微信，3是银行卡)
     */
    @ApiModelProperty("缴费方式(1是支付宝，2是微信，3是银行卡)")
    @TableField("pay_way")
    private Integer payWay;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Long ctime;
    /**
     * 状态：1、待缴费；2、缴费成功；3；缴费失败
     */
    @ApiModelProperty("状态：1、待缴费；2、缴费成功；3；缴费失败")
    private Integer status;
    /**
     * 停车场编号
     */
    @ApiModelProperty("停车场编号")
    @TableField("space_id")
    private Integer spaceId;
    /**
     * 停车场名称
     */
    @ApiModelProperty("停车场名称")
    @TableField("space_name")
    private String spaceName;
    /**
     * 用户编号
     */
    @ApiModelProperty("用户编号")
    @TableField("user_id")
    private String userId;
    /**
     * 用户账号
     */
    @ApiModelProperty("用户账号")
    private String username;
    /**
     * 用户姓名
     */
    @ApiModelProperty("用户姓名")
    private String realname;
    /**
     * 支付截止时间
     */
    @ApiModelProperty("支付截止时间")
    @TableField("cut_off_time")
    private Long cutOffTime;
    /**
     * 缴费类型：1、按月缴费；2、按年缴费
     */
    @ApiModelProperty("缴费类型：1、按月缴费；2、按年缴费")
    private Integer type;
    /**
     * 车辆照片路径
     */
    @ApiModelProperty("车辆照片路径")
    private String resource;


    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public Long getCutOffTime() {
        return cutOffTime;
    }

    public void setCutOffTime(Long cutOffTime) {
        this.cutOffTime = cutOffTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    @Override
    protected Serializable pkVal() {
        return this.paymentId;
    }

    @Override
    public String toString() {
        return "BjtCarparkPayment{" +
        "paymentId=" + paymentId +
        ", carId=" + carId +
        ", plateNumber=" + plateNumber +
        ", fee=" + fee +
        ", number=" + number +
        ", beginTime=" + beginTime +
        ", endTime=" + endTime +
        ", totalFee=" + totalFee +
        ", payWay=" + payWay +
        ", remark=" + remark +
        ", ctime=" + ctime +
        ", status=" + status +
        ", spaceId=" + spaceId +
        ", spaceName=" + spaceName +
        ", userId=" + userId +
        ", username=" + username +
        ", realname=" + realname +
        ", cutOffTime=" + cutOffTime +
        ", type=" + type +
        ", resource=" + resource +
        "}";
    }
}
