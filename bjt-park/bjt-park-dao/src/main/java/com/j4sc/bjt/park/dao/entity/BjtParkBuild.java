package com.j4sc.bjt.park.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 楼宇表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-21
 */
@TableName("bjt_park_build")
public class BjtParkBuild extends Model<BjtParkBuild> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "build_id", type = IdType.AUTO)
    private Integer buildId;
    /**
     * 楼宇名称
     */
    private String name;
    /**
     * 地址
     */
    private String address;
    /**
     * 创建时间时间
     */
    private Long ctime;
    /**
     * 修改时间
     */
    private Long utime;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 物业方
     */
    private String property;
    /**
     * 产权方
     */
    private String attribution;
    /**
     * 楼层高度
     */
    private Integer floor;
    /**
     * 所属园区
     */
    private String park;
    /**
     * 备注
     */
    private String remake;
    /**
     * 邀请码
     */
    private String code;
    /**
     * 物业方联系人
     */
    private String contact;
    /**
     * 联系方式：手机号或电话号码
     */
    @TableField("contact_way")
    private String contactWay;
    /**
     * 总面积
     */
    private String area;
    /**
     * 停车场编号
     */
    @TableField("space_id")
    private Integer spaceId;
    /**
     * 停车场名称
     */
    @TableField("space_name")
    private String spaceName;

    @Override
    public String toString() {
        return "BjtParkBuild{" +
                "buildId=" + buildId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", ctime=" + ctime +
                ", utime=" + utime +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", property='" + property + '\'' +
                ", attribution='" + attribution + '\'' +
                ", floor=" + floor +
                ", park='" + park + '\'' +
                ", remake='" + remake + '\'' +
                ", code='" + code + '\'' +
                ", contact='" + contact + '\'' +
                ", contactWay='" + contactWay + '\'' +
                ", area='" + area + '\'' +
                ", spaceId=" + spaceId +
                ", spaceName='" + spaceName + '\'' +
                '}';
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public String getPark() {
        return park;
    }

    public void setPark(String park) {
        this.park = park;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    @Override
    protected Serializable pkVal() {
        return this.buildId;
    }

}
