package com.j4sc.bjt.park.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 房屋信息表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-18
 */
@TableName("bjt_park_build_house")
public class BjtParkBuildHouse extends Model<BjtParkBuildHouse> {

    private static final long serialVersionUID = 1L;

    /**
     * 房屋编号
     */
    @TableId(value = "house_id", type = IdType.AUTO)
    private Integer houseId;
    /**
     * 所在楼层
     */
    private Integer floor;
    /**
     * 房间号
     */
    @TableField("house_number")
    private String houseNumber;
    /**
     * 面积
     */
    private Integer area;
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
     * 产权方
     */
    private String attribution;
    /**
     * 公司
     */
    private String company;
    /**
     * 公司编号
     */
    @TableField("company_id")
    private Integer companyId;
    /**
     * 租赁合同编号
     */
    @TableField("lease_agreement_id")
    private Integer leaseAgreementId;
    /**
     * 合同名称
     */
    @TableField("lease_agreement_name")
    private String leaseAgreementName;
    /**
     * 创建时间
     */
    private Long ctime;


    public Integer getHouseId() {
        return houseId;
    }

    public void setHouseId(Integer houseId) {
        this.houseId = houseId;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getLeaseAgreementId() {
        return leaseAgreementId;
    }

    public void setLeaseAgreementId(Integer leaseAgreementId) {
        this.leaseAgreementId = leaseAgreementId;
    }

    public String getLeaseAgreementName() {
        return leaseAgreementName;
    }

    public void setLeaseAgreementName(String leaseAgreementName) {
        this.leaseAgreementName = leaseAgreementName;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    @Override
    protected Serializable pkVal() {
        return this.houseId;
    }

    @Override
    public String toString() {
        return "BjtParkBuildHouse{" +
        "houseId=" + houseId +
        ", floor=" + floor +
        ", houseNumber=" + houseNumber +
        ", area=" + area +
        ", buildId=" + buildId +
        ", buildName=" + buildName +
        ", attribution=" + attribution +
        ", company=" + company +
        ", companyId=" + companyId +
        ", leaseAgreementId=" + leaseAgreementId +
        ", leaseAgreementName=" + leaseAgreementName +
        ", ctime=" + ctime +
        "}";
    }
}
