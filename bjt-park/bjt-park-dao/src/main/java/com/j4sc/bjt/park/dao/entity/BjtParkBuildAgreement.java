package com.j4sc.bjt.park.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 合同表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-21
 */
@TableName("bjt_park_build_agreement")
public class BjtParkBuildAgreement extends Model<BjtParkBuildAgreement> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号（主键）
     */
    @TableId(value = "agreement_id", type = IdType.AUTO)
    private Integer agreementId;
    /**
     * 合同名称
     */
    private String name;
    /**
     * 甲方（物业公司或产权方）
     */
    @TableField("first_party")
    private String firstParty;
    /**
     * 房间号
     */
    @TableField("door_plate")
    private String doorPlate;
    /**
     * 乙方：公司
     */
    @TableField("secend_party")
    private String secendParty;
    /**
     * 合同编号
     */
    private String number;
    /**
     * 合同开始日期
     */
    @TableField("begin_time")
    private Long beginTime;
    /**
     * 合同结束日期
     */
    @TableField("end_time")
    private Long endTime;
    /**
     * 缴费方式：1、按月缴纳；2、按季度缴纳；3、按年缴纳
     */
    private Integer payment;
    /**
     * 缴纳费用
     */
    @TableField("pay_fee")
    private String payFee;
    /**
     * 下次缴费日期
     */
    @TableField("next_time")
    private Long nextTime;
    /**
     * 附件信息json字符串
     */
    private String resource;
    /**
     * 合同类型：1、物业合同；2、租赁合同
     */
    private Integer type;
    /**
     * 公司编号
     */
    @TableField("company_id")
    private Integer companyId;
    /**
     * 面积
     */
    private Integer area;
    /**
     * 单价：/平方米/月
     */
    @TableField("unit_price")
    private Integer unitPrice;
    /**
     * 总价
     */
    @TableField("total_price")
    private Integer totalPrice;
    /**
     * 押金
     */
    private Integer deposit;
    /**
     * 合同生效日期
     */
    private Long contract;
    /**
     * 创建时间
     */
    private Long ctime;
    /**
     * 是否开启缴费(1、开启；2、关闭)
     */
    private Integer open;
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


    public Integer getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Integer agreementId) {
        this.agreementId = agreementId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstParty() {
        return firstParty;
    }

    public void setFirstParty(String firstParty) {
        this.firstParty = firstParty;
    }

    public String getDoorPlate() {
        return doorPlate;
    }

    public void setDoorPlate(String doorPlate) {
        this.doorPlate = doorPlate;
    }

    public String getSecendParty() {
        return secendParty;
    }

    public void setSecendParty(String secendParty) {
        this.secendParty = secendParty;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
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

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    public String getPayFee() {
        return payFee;
    }

    public void setPayFee(String payFee) {
        this.payFee = payFee;
    }

    public Long getNextTime() {
        return nextTime;
    }

    public void setNextTime(Long nextTime) {
        this.nextTime = nextTime;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getDeposit() {
        return deposit;
    }

    public void setDeposit(Integer deposit) {
        this.deposit = deposit;
    }

    public Long getContract() {
        return contract;
    }

    public void setContract(Long contract) {
        this.contract = contract;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Integer getOpen() {
        return open;
    }

    public void setOpen(Integer open) {
        this.open = open;
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

    @Override
    protected Serializable pkVal() {
        return this.agreementId;
    }

    @Override
    public String toString() {
        return "BjtParkBuildAgreement{" +
        "agreementId=" + agreementId +
        ", name=" + name +
        ", firstParty=" + firstParty +
        ", doorPlate=" + doorPlate +
        ", secendParty=" + secendParty +
        ", number=" + number +
        ", beginTime=" + beginTime +
        ", endTime=" + endTime +
        ", payment=" + payment +
        ", payFee=" + payFee +
        ", nextTime=" + nextTime +
        ", resource=" + resource +
        ", type=" + type +
        ", companyId=" + companyId +
        ", area=" + area +
        ", unitPrice=" + unitPrice +
        ", totalPrice=" + totalPrice +
        ", deposit=" + deposit +
        ", contract=" + contract +
        ", ctime=" + ctime +
        ", open=" + open +
        ", buildId=" + buildId +
        ", buildName=" + buildName +
        "}";
    }
}
