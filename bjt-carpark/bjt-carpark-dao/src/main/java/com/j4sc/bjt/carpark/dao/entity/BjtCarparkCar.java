package com.j4sc.bjt.carpark.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 车辆信息表
 * </p>
 *
 * @author LongRou
 * @since 2018-05-15
 */
@TableName("bjt_carpark_car")
public class BjtCarparkCar extends Model<BjtCarparkCar> {

    private static final long serialVersionUID = 1L;

    /**
     * 车辆信息编号
     */
    @TableId(value = "car_id", type = IdType.AUTO)
    private Integer carId;
    /**
     * 车主姓名
     */
    private String name;
    /**
     * 身份证号
     */
    @TableField("id_number")
    private String idNumber;
    /**
     * 驾驶证号
     */
    @TableField("licence_number")
    private String licenceNumber;
    /**
     * 行驶证号
     */
    @TableField("drive_number")
    private String driveNumber;
    /**
     * 车辆照片json数据
     */
    private String resource;
    /**
     * 车牌号
     */
    @TableField("plate_number")
    private String plateNumber;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 型号
     */
    private String model;
    /**
     * 颜色
     */
    private String color;
    /**
     * 创建时间
     */
    private Long ctime;
    /**
     * 用户编号
     */
    @TableField("user_id")
    private String userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 备注
     */
    private String remark;


    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getDriveNumber() {
        return driveNumber;
    }

    public void setDriveNumber(String driveNumber) {
        this.driveNumber = driveNumber;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    protected Serializable pkVal() {
        return this.carId;
    }

    @Override
    public String toString() {
        return "BjtCarparkCar{" +
        "carId=" + carId +
        ", name=" + name +
        ", idNumber=" + idNumber +
        ", licenceNumber=" + licenceNumber +
        ", driveNumber=" + driveNumber +
        ", resource=" + resource +
        ", plateNumber=" + plateNumber +
        ", brand=" + brand +
        ", model=" + model +
        ", color=" + color +
        ", ctime=" + ctime +
        ", userId=" + userId +
        ", username=" + username +
        ", remark=" + remark +
        "}";
    }
}
