package com.j4sc.bjt.park.dao.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 考勤打卡表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-16
 */
@TableName("bjt_park_company_clock")
public class BjtParkCompanyClock extends Model<BjtParkCompanyClock> {

    private static final long serialVersionUID = 1L;

    /**
     * 打卡编号
     */
    @ApiModelProperty(value = "打卡编号")
    @TableId(value = "clock_id", type = IdType.AUTO)
    private Integer clockId;
    /**
     * 用户编号
     */
    @ApiModelProperty(value = "用户编号")
    @TableField("user_id")
    private String userId;
    /**
     * 上班打卡时间
     */
    @ApiModelProperty(value = "上班打卡时间")
    @TableField("in_time")
    private Long inTime;
    /**
     * 上班打卡类型：1、正常；2、迟到
     */
    @ApiModelProperty(value = "上班打卡类型：1、正常；2、迟到；3、外勤打卡")
    @TableField("in_type")
    private Integer inType;
    /**
     * 打卡的地址
     */
    @ApiModelProperty(value = "上班打卡的地址")
    @TableField("in_address")
    private String inAddress;
    /**
     * 上班打卡的经度
     */
    @ApiModelProperty(value = "上班打卡的经度")
    @TableField("in_longitude")
    private String inLongitude;
    /**
     * 上班打卡的纬度
     */
    @ApiModelProperty(value = "上班打卡的纬度")
    @TableField("in_latitude")
    private String inLatitude;
    /**
     * 下班打卡的时间
     */
    @ApiModelProperty(value = "下班打卡的时间")
    @TableField("out_time")
    private Long outTime;
    /**
     * 下班打卡的类型：1、正常;2、早退
     */
    @ApiModelProperty(value = "下班打卡的类型：1、正常;2、早退；3、外勤打卡")
    @TableField("out_type")
    private Integer outType;
    /**
     * 下班打卡的地址
     */
    @ApiModelProperty(value = "下班打卡的地址")
    @TableField("out_address")
    private String outAddress;
    /**
     * 下班打卡的经度
     */
    @ApiModelProperty(value = "下班打卡的经度")
    @TableField("out_longitude")
    private String outLongitude;
    /**
     * 下班打卡的纬度
     */
    @ApiModelProperty(value = "下班打卡的纬度")
    @TableField("out_latitude")
    private String outLatitude;
    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名")
    @TableField("user_realname")
    private String userRealname;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 上班打卡方式：1、WiFi、2、流量
     */
    @ApiModelProperty(value = "上班打卡方式：1、WiFi、2、流量")
    @TableField("in_way")
    private Integer inWay;
    /**
     * 下班打卡方式：1、WiFi、2、流量
     */
    @ApiModelProperty(value = " 下班打卡方式：1、WiFi、2、流量")
    @TableField("out_way")
    private Integer outWay;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long ctime;
    /**
     * 迟到备注
     */
    @TableField("late_remark")
    @ApiModelProperty(value = "迟到备注")
    private String lateRemark;
    /**
     * 早退备注
     */
    @TableField("leave_remark")
    @ApiModelProperty(value = "早退备注")
    private String leaveRemark;


    public Integer getClockId() {
        return clockId;
    }

    public void setClockId(Integer clockId) {
        this.clockId = clockId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getInTime() {
        return inTime;
    }

    public void setInTime(Long inTime) {
        this.inTime = inTime;
    }

    public Integer getInType() {
        return inType;
    }

    public void setInType(Integer inType) {
        this.inType = inType;
    }

    public String getInAddress() {
        return inAddress;
    }

    public void setInAddress(String inAddress) {
        this.inAddress = inAddress;
    }

    public String getInLongitude() {
        return inLongitude;
    }

    public void setInLongitude(String inLongitude) {
        this.inLongitude = inLongitude;
    }

    public String getInLatitude() {
        return inLatitude;
    }

    public void setInLatitude(String inLatitude) {
        this.inLatitude = inLatitude;
    }

    public Long getOutTime() {
        return outTime;
    }

    public void setOutTime(Long outTime) {
        this.outTime = outTime;
    }

    public Integer getOutType() {
        return outType;
    }

    public void setOutType(Integer outType) {
        this.outType = outType;
    }

    public String getOutAddress() {
        return outAddress;
    }

    public void setOutAddress(String outAddress) {
        this.outAddress = outAddress;
    }

    public String getOutLongitude() {
        return outLongitude;
    }

    public void setOutLongitude(String outLongitude) {
        this.outLongitude = outLongitude;
    }

    public String getOutLatitude() {
        return outLatitude;
    }

    public void setOutLatitude(String outLatitude) {
        this.outLatitude = outLatitude;
    }

    public String getUserRealname() {
        return userRealname;
    }

    public void setUserRealname(String userRealname) {
        this.userRealname = userRealname;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getInWay() {
        return inWay;
    }

    public void setInWay(Integer inWay) {
        this.inWay = inWay;
    }

    public Integer getOutWay() {
        return outWay;
    }

    public void setOutWay(Integer outWay) {
        this.outWay = outWay;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public String getLateRemark() {
        return lateRemark;
    }

    public void setLateRemark(String lateRemark) {
        this.lateRemark = lateRemark;
    }

    public String getLeaveRemark() {
        return leaveRemark;
    }

    public void setLeaveRemark(String leaveRemark) {
        this.leaveRemark = leaveRemark;
    }

    @Override
    protected Serializable pkVal() {
        return this.clockId;
    }

    @Override
    public String toString() {
        return "BjtParkCompanyClock{" +
        "clockId=" + clockId +
        ", userId=" + userId +
        ", inTime=" + inTime +
        ", inType=" + inType +
        ", inAddress=" + inAddress +
        ", inLongitude=" + inLongitude +
        ", inLatitude=" + inLatitude +
        ", outTime=" + outTime +
        ", outType=" + outType +
        ", outAddress=" + outAddress +
        ", outLongitude=" + outLongitude +
        ", outLatitude=" + outLatitude +
        ", userRealname=" + userRealname +
        ", lateRemark=" + lateRemark +
        ", leaveRemark=" + leaveRemark +
        ", remark=" + remark +
        ", inWay=" + inWay +
        ", outWay=" + outWay +
        ", ctime=" + ctime +
        "}";
    }
}
