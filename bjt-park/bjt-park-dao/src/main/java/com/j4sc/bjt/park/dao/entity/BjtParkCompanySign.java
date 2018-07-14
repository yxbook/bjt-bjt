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
 * 签到表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-17
 */
@TableName("bjt_park_company_sign")
public class BjtParkCompanySign extends Model<BjtParkCompanySign> {

    private static final long serialVersionUID = 1L;

    /**
     * 签到编号
     */
    @ApiModelProperty(value = "签到编号")
    @TableId(value = "sign_id", type = IdType.AUTO)
    private Integer signId;
    /**
     * 用户编号
     */
    @ApiModelProperty(value = "用户编号")
    @TableId("user_id")
    private String userId;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    @TableField("user_realname")
    private String userRealname;
    /**
     * 签到时间
     */
    @ApiModelProperty(value = "签到时间")
    @TableField("sign_time")
    private Long signTime;
    /**
     * 签到地址
     */
    @ApiModelProperty(value = "签到地址")
    private String address;
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
     * json字符串
     */
    @ApiModelProperty(value = "json字符串")
    private String resource;
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


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserRealname() {
        return userRealname;
    }

    public void setUserRealname(String userRealname) {
        this.userRealname = userRealname;
    }

    public Long getSignTime() {
        return signTime;
    }

    public void setSignTime(Long signTime) {
        this.signTime = signTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
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

    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

    public Integer getSignId() {
        return signId;
    }

    public void setSignId(Integer signId) {
        this.signId = signId;
    }

    @Override
    public String toString() {
        return "BjtParkCompanySign{" +
                "signId=" + signId +
                ", userId=" + userId +
                ", userRealname=" + userRealname +
                ", signTime=" + signTime +
                ", address=" + address +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", resource=" + resource +
                ", remark=" + remark +
                ", ctime=" + ctime +
                "}";
    }
}
