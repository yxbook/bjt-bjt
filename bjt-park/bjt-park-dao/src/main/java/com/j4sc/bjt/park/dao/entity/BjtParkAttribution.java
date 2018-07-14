package com.j4sc.bjt.park.dao.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 产权方表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-18
 */
@TableName("bjt_park_attribution")
public class BjtParkAttribution extends Model<BjtParkAttribution> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId("attribution_id")
    private Integer attributionId;
    /**
     * 楼宇编号
     */
    @TableField("build_id")
    private Integer buildId;
    /**
     * 楼宇名称
     */
    private String name;
    /**
     * 楼宇地址
     */
    private String address;
    /**
     * 创建时间
     */
    private Long ctime;
    /**
     * 修改时间
     */
    private Long utime;
    /**
     * 产权方
     */
    private String attribution;
    /**
     * 物业方
     */
    private String property;
    /**
     * 产权方类型：0是个人、1是公司等
     */
    private Integer type;
    /**
     * 所属园区
     */
    private String park;
    /**
     * 备注
     */
    private String remark;
    /**
     * 产权方简称
     */
    private String simple;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 联系方式
     */
    @TableField("contact_way")
    private String contactWay;
    /**
     * 公司地址
     */
    @TableField("company_address")
    private String companyAddress;


    public Integer getAttributionId() {
        return attributionId;
    }

    public void setAttributionId(Integer attributionId) {
        this.attributionId = attributionId;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getUtime() {
        return utime;
    }

    public void setUtime(Long utime) {
        this.utime = utime;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPark() {
        return park;
    }

    public void setPark(String park) {
        this.park = park;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSimple() {
        return simple;
    }

    public void setSimple(String simple) {
        this.simple = simple;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    @Override
    protected Serializable pkVal() {
        return this.attributionId;
    }

    @Override
    public String toString() {
        return "BjtParkAttribution{" +
        "attributionId=" + attributionId +
        ", buildId=" + buildId +
        ", name=" + name +
        ", address=" + address +
        ", ctime=" + ctime +
        ", utime=" + utime +
        ", attribution=" + attribution +
        ", property=" + property +
        ", type=" + type +
        ", park=" + park +
        ", remark=" + remark +
        ", simple=" + simple +
        ", contact=" + contact +
        ", contactWay=" + contactWay +
        ", companyAddress=" + companyAddress +
        "}";
    }
}
