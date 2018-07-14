package com.j4sc.bjt.park.dao.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 缴费记录表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-18
 */
@TableName("bjt_park_build_pay_record")
public class BjtParkBuildPayRecord extends Model<BjtParkBuildPayRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 记录主键编号
     */
    @TableId("p_record_id")
    private Integer pRecordId;
    /**
     * 公司编号
     */
    @TableField("company_id")
    private Integer companyId;
    /**
     * 公司名称
     */
    @TableField("company_name")
    private String companyName;
    /**
     * 缴费时间
     */
    @TableField("pay_time")
    private Long payTime;
    /**
     * 有效期
     */
    private Long validity;
    /**
     * 合同到期日
     */
    private Long expire;
    /**
     * 单价
     */
    private String price;
    /**
     * 创建时间
     */
    private Long ctime;
    /**
     * 类型（1租金、2停车、3物业费）
     */
    private String type;
    /**
     * 门牌号
     */
    @TableField("door_plate")
    private String doorPlate;
    /**
     * 产权方
     */
    private String attribution;
    /**
     * 产权方联系人
     */
    private String contact;
    /**
     * 产权方联系方式
     */
    @TableField("contact_way")
    private String contactWay;
    /**
     * 楼宇名称
     */
    @TableField("build_name")
    private String buildName;
    /**
     * 楼宇编号
     */
    @TableField("build_id")
    private Integer buildId;


    public Integer getpRecordId() {
        return pRecordId;
    }

    public void setpRecordId(Integer pRecordId) {
        this.pRecordId = pRecordId;
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

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public Long getValidity() {
        return validity;
    }

    public void setValidity(Long validity) {
        this.validity = validity;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDoorPlate() {
        return doorPlate;
    }

    public void setDoorPlate(String doorPlate) {
        this.doorPlate = doorPlate;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
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

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    @Override
    protected Serializable pkVal() {
        return this.pRecordId;
    }

    @Override
    public String toString() {
        return "BjtParkBuildPayRecord{" +
        "pRecordId=" + pRecordId +
        ", companyId=" + companyId +
        ", companyName=" + companyName +
        ", payTime=" + payTime +
        ", validity=" + validity +
        ", expire=" + expire +
        ", price=" + price +
        ", ctime=" + ctime +
        ", type=" + type +
        ", doorPlate=" + doorPlate +
        ", attribution=" + attribution +
        ", contact=" + contact +
        ", contactWay=" + contactWay +
        ", buildName=" + buildName +
        ", buildId=" + buildId +
        "}";
    }
}
