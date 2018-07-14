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
 * 打卡规则表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-16
 */
@TableName("bjt_park_company_clock_rule")
public class BjtParkCompanyClockRule extends Model<BjtParkCompanyClockRule> {

    private static final long serialVersionUID = 1L;

    /**
     * 规则编号
     */
    @ApiModelProperty(value = "规则编号")
    @TableId(value = "rule_id", type = IdType.AUTO)
    private Integer ruleId;
    /**
     * 上班时间
     */
    @ApiModelProperty(value = "上班时间")
    @TableField("in_time")
    private String inTime;
    /**
     * 下班时间
     */
    @ApiModelProperty(value = "下班时间")
    @TableField("out_time")
    private String outTime;
    /**
     * 上班日：1,2,3,4,5；用,号分开，表示周一到周五都上班
     */
    @ApiModelProperty(value = "上班日：1,2,3,4,5；用,号分开，表示周一到周五都上班")
    @TableField("work_day")
    private String workDay;
    /**
     * 公司编号
     */
    @ApiModelProperty(value = "公司编号")
    @TableField("company_id")
    private Integer companyId;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private String longitude;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private String latitude;
    /**
     * 楼宇名称
     */
    @ApiModelProperty(value = "楼宇名称")
    private String address;
    /**
     * WiFi名称
     */
    @ApiModelProperty(value = "WiFi名称")
    @TableField("wifi_name")
    private String wifiName;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long ctime;


    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getWorkDay() {
        return workDay;
    }

    public void setWorkDay(String workDay) {
        this.workDay = workDay;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    @Override
    protected Serializable pkVal() {
        return this.ruleId;
    }

    @Override
    public String toString() {
        return "BjtParkCompanyClockRule{" +
        "ruleId=" + ruleId +
        ", inTime=" + inTime +
        ", outTime=" + outTime +
        ", workDay=" + workDay +
        ", companyId=" + companyId +
        ", remark=" + remark +
        ", longitude=" + longitude +
        ", latitude=" + latitude +
        ", address=" + address +
        ", wifiName=" + wifiName +
        ", ctime=" + ctime +
        "}";
    }
}
