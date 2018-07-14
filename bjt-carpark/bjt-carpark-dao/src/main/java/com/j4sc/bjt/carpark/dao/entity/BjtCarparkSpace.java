package com.j4sc.bjt.carpark.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 停车场信息表
 * </p>
 *
 * @author LongRou
 * @since 2018-05-15
 */
@TableName("bjt_carpark_space")
public class BjtCarparkSpace extends Model<BjtCarparkSpace> {

    private static final long serialVersionUID = 1L;

    /**
     * 停车场编号
     */
    @TableId(value = "space_id", type = IdType.AUTO)
    private Integer spaceId;
    /**
     * 名称
     */
    private String name;
    /**
     * 位置
     */
    private String address;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 车位数量
     */
    private Integer number;
    /**
     * 是否对外开放：1、是；2、否
     */
    private Integer open;
    /**
     * 月卡单价
     */
    @TableField("month_fee")
    private BigDecimal monthFee;
    /**
     * 年卡单价
     */
    @TableField("year_fee")
    private BigDecimal yearFee;
    /**
     * 每小时费用
     */
    @TableField("hour_fee")
    private BigDecimal hourFee;
    /**
     * 创建时间
     */
    private Long ctime;
    /**
     * 临时停车位总数量
     */
    @TableField("temporary_number")
    private Integer temporaryNumber;
    /**
     * 固定停车位总数量
     */
    @TableField("fixed_number")
    private Integer fixedNumber;
    /**
     * 剩余临时停车位数量
     */
    private Integer temporary;
    /**
     * 剩余固定停车位数量
     */
    private Integer fixed;
    /**
     * 联系人姓名
     */
    private String contact;
    /**
     * 联系方式
     */
    @TableField("contact_way")
    private String contactWay;
    /**
     * 停车场系统的停车场编号
     */
    @TableField("park_id")
    private String parkId;


    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getOpen() {
        return open;
    }

    public void setOpen(Integer open) {
        this.open = open;
    }

    public BigDecimal getMonthFee() {
        return monthFee;
    }

    public void setMonthFee(BigDecimal monthFee) {
        this.monthFee = monthFee;
    }

    public BigDecimal getYearFee() {
        return yearFee;
    }

    public void setYearFee(BigDecimal yearFee) {
        this.yearFee = yearFee;
    }

    public BigDecimal getHourFee() {
        return hourFee;
    }

    public void setHourFee(BigDecimal hourFee) {
        this.hourFee = hourFee;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Integer getTemporaryNumber() {
        return temporaryNumber;
    }

    public void setTemporaryNumber(Integer temporaryNumber) {
        this.temporaryNumber = temporaryNumber;
    }

    public Integer getFixedNumber() {
        return fixedNumber;
    }

    public void setFixedNumber(Integer fixedNumber) {
        this.fixedNumber = fixedNumber;
    }

    public Integer getTemporary() {
        return temporary;
    }

    public void setTemporary(Integer temporary) {
        this.temporary = temporary;
    }

    public Integer getFixed() {
        return fixed;
    }

    public void setFixed(Integer fixed) {
        this.fixed = fixed;
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

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    @Override
    protected Serializable pkVal() {
        return this.spaceId;
    }

    @Override
    public String toString() {
        return "BjtCarparkSpace{" +
        "spaceId=" + spaceId +
        ", name=" + name +
        ", address=" + address +
        ", longitude=" + longitude +
        ", latitude=" + latitude +
        ", number=" + number +
        ", open=" + open +
        ", monthFee=" + monthFee +
        ", yearFee=" + yearFee +
        ", hourFee=" + hourFee +
        ", ctime=" + ctime +
        ", temporaryNumber=" + temporaryNumber +
        ", fixedNumber=" + fixedNumber +
        ", temporary=" + temporary +
        ", fixed=" + fixed +
        ", contact=" + contact +
        ", contactWay=" + contactWay +
        ", parkId=" + parkId +
        "}";
    }
}
