package com.j4sc.bjt.carpark.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 停车申请明细表
 * </p>
 *
 * @author LongRou
 * @since 2018-05-15
 */
@TableName("bjt_carpark_apply_detail")
public class BjtCarparkApplyDetail extends Model<BjtCarparkApplyDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * 明细编号
     */
    @TableId(value = "detail_id", type = IdType.AUTO)
    private Integer detailId;
    /**
     * 车主姓名
     */
    @TableField("car_name")
    private String carName;
    /**
     * 车牌号
     */
    @TableField("plate_number")
    private String plateNumber;
    /**
     * 用户编号
     */
    @TableField("user_id")
    private String userId;
    /**
     * 申请编号
     */
    @TableField("apply_id")
    private Integer applyId;
    /**
     * 类型：1、固定；2、临时
     */
    private Integer type;
    /**
     * 创建时间
     */
    private Long ctime;
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
    /**
     * 车位编号
     */
    @TableField("lot_number")
    private String lotNumber;
    /**
     * 状态:1、待审批；2、（已通过）但未缴费；3、未通过；4、正常
     */
    private Integer status;
    /**
     * 生效日期
     */
    @TableField("begin_time")
    private Long beginTime;
    /**
     * 车辆信息编号
     */
    @TableField("car_id")
    private Integer carId;
    /**
     * 失效日期
     */
    @TableField("end_time")
    private Long endTime;
    /**
     * 终止时间(跟缴费无关)
     */
    @TableField("terminal_time")
    private Long terminalTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 车辆照片路径
     */
    private String resource;


    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
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

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(Integer carId) {
        this.carId = carId;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Long getTerminalTime() {
        return terminalTime;
    }

    public void setTerminalTime(Long terminalTime) {
        this.terminalTime = terminalTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.detailId;
    }

    @Override
    public String toString() {
        return "BjtCarparkApplyDetail{" +
        "detailId=" + detailId +
        ", carName=" + carName +
        ", plateNumber=" + plateNumber +
        ", userId=" + userId +
        ", applyId=" + applyId +
        ", type=" + type +
        ", ctime=" + ctime +
        ", spaceId=" + spaceId +
        ", spaceName=" + spaceName +
        ", lotNumber=" + lotNumber +
        ", status=" + status +
        ", beginTime=" + beginTime +
        ", carId=" + carId +
        ", endTime=" + endTime +
        ", remark=" + remark +
        ", resource=" + resource +
        ", terminalTime=" + terminalTime +
        "}";
    }
}
