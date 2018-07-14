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
 * 楼宇公司表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-17
 */
@TableName("bjt_park_company")
public class BjtParkCompany extends Model<BjtParkCompany> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ApiModelProperty(value = "公司编号")
    @TableId(value = "company_id", type = IdType.AUTO)
    private Integer companyId;
    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String name;
    /**
     * 公司具体地址
     */
    @ApiModelProperty(value = "公司具体地址")
    private String address;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long ctime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Long utime;
    /**
     * 门牌号
     */
    @ApiModelProperty(value = "门牌号")
    @TableField("door_plate")
    private String doorPlate;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 公司简称
     */
    @ApiModelProperty(value = "公司简称")
    private String simple;
    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contact;
    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    @TableField("contact_way")
    private String contactWay;
    /**
     * 楼宇编号
     */
    @ApiModelProperty(value = "楼宇编号")
    @TableField("build_id")
    private Integer buildId;
    /**
     * 楼宇名称
     */
    @ApiModelProperty(value = "楼宇名称")
    @TableField("build_name")
    private String buildName;
    /**
     * 邀请码
     */
    @ApiModelProperty(value = "邀请码")
    private String code;
    /**
     * 所在行业
     */
    @ApiModelProperty(value = "所在行业")
    private String industry;
    /**
     * 所在楼层
     */
    @ApiModelProperty(value = "所在楼层")
    private String floor;



    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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

    public String getDoorPlate() {
        return doorPlate;
    }

    public void setDoorPlate(String doorPlate) {
        this.doorPlate = doorPlate;
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

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    @Override
    protected Serializable pkVal() {
        return this.companyId;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    @Override
    public String toString() {
        return "BjtParkCompany{" +
        "companyId=" + companyId +
        ", name=" + name +
        ", address=" + address +
        ", ctime=" + ctime +
        ", utime=" + utime +
        ", doorPlate=" + doorPlate +
        ", remark=" + remark +
        ", simple=" + simple +
        ", contact=" + contact +
        ", contactWay=" + contactWay +
        ", buildId=" + buildId +
        ", buildName=" + buildName +
        ", industry=" + industry +
        ", code=" + code +
        ", floor=" + floor +
        "}";
    }
}
