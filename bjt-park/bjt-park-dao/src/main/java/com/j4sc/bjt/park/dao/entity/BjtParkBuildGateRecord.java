package com.j4sc.bjt.park.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 闸机记录
 * </p>
 *
 * @author LongRou
 * @since 2018-05-02
 */
@TableName("bjt_park_build_gate_record")
public class BjtParkBuildGateRecord extends Model<BjtParkBuildGateRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 记录编号
     */
    @ApiModelProperty("记录编号")
    @TableId(value = "g_record_id", type = IdType.AUTO)
    private Integer gRecordId;
    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String realname;
    /**
     * 用户名（手机号）
     */
    @ApiModelProperty("用户名（手机号）")
    private String username;
    /**
     * 设备编号
     */
    @ApiModelProperty("设备编号")
    @TableField("device_identifier")
    private String deviceIdentifier;
    /**
     * 公司名称
     */
    @ApiModelProperty("公司名称")
    @TableField("company_name")
    private String companyName;
    /**
     * 公司编号
     */
    @ApiModelProperty("公司编号")
    @TableField("company_id")
    private Integer companyId;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Long ctime;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
    /**
     * 进或出的时间
     */
    @ApiModelProperty("进或出的时间")
    private Long time;
    /**
     * 类型：1、进；2、出
     */
    @ApiModelProperty("类型：1、进；2、出")
    private Integer type;
    /**
     * 楼宇ID
     */
    @ApiModelProperty(value = "楼宇ID")
    @TableField("build_id")
    private Integer buildId;


    public Integer getgRecordId() {
        return gRecordId;
    }

    public void setgRecordId(Integer gRecordId) {
        this.gRecordId = gRecordId;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    @Override
    protected Serializable pkVal() {
        return this.gRecordId;
    }

    @Override
    public String toString() {
        return "BjtParkBuildGateRecord{" +
        "gRecordId=" + gRecordId +
        ", realname=" + realname +
        ", username=" + username +
        ", deviceIdentifier=" + deviceIdentifier +
        ", companyName=" + companyName +
        ", companyId=" + companyId +
        ", ctime=" + ctime +
        ", remark=" + remark +
        ", time=" + time +
        ", type=" + type +
        ", buildId=" + buildId +
        "}";
    }
}
