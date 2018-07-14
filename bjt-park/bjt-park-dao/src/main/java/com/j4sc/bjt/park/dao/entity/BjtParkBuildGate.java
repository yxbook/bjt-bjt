package com.j4sc.bjt.park.dao.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 闸机信息表
 * </p>
 *
 * @author LongRou
 * @since 2018-05-02
 */
@TableName("bjt_park_build_gate")
public class BjtParkBuildGate extends Model<BjtParkBuildGate> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId("gate_id")
    private Integer gateId;
    /**
     * 设备编号
     */
    @TableField("device_identifier")
    private String deviceIdentifier;
    /**
     * 楼宇编号
     */
    @TableField("build_id")
    private Integer buildId;
    /**
     * 楼宇名称
     */
    @TableField("build_name")
    private String buildName;
    /**
     * 描述
     */
    private String describe;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Long ctime;


    public Integer getGateId() {
        return gateId;
    }

    public void setGateId(Integer gateId) {
        this.gateId = gateId;
    }

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    @Override
    protected Serializable pkVal() {
        return this.gateId;
    }

    @Override
    public String toString() {
        return "BjtParkBuildGate{" +
        "gateId=" + gateId +
        ", deviceIdentifier=" + deviceIdentifier +
        ", buildId=" + buildId +
        ", describe=" + describe +
        ", remark=" + remark +
        ", ctime=" + ctime +
        ", buildName=" + buildName +
        "}";
    }
}
