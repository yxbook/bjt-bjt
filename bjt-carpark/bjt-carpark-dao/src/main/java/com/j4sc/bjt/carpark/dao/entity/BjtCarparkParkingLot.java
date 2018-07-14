package com.j4sc.bjt.carpark.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 停车位信息
 * </p>
 *
 * @author LongRou
 * @since 2018-05-15
 */
@TableName("bjt_carpark_parking_lot")
public class BjtCarparkParkingLot extends Model<BjtCarparkParkingLot> {

    private static final long serialVersionUID = 1L;

    /**
     * 车位主键编号
     */
    @TableId(value = "lot_id", type = IdType.AUTO)
    private Integer lotId;
    /**
     * 车位编号
     */
    private String number;
    /**
     * 类型：1、固定车位；2、临时车位
     */
    private Integer type;
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
     * 创建时间
     */
    private Long ctime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态：1、空闲；2、使用中
     */
    private Integer status;


    public Integer getLotId() {
        return lotId;
    }

    public void setLotId(Integer lotId) {
        this.lotId = lotId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    protected Serializable pkVal() {
        return this.lotId;
    }

    @Override
    public String toString() {
        return "BjtCarparkParkingLot{" +
        "lotId=" + lotId +
        ", number=" + number +
        ", type=" + type +
        ", spaceId=" + spaceId +
        ", spaceName=" + spaceName +
        ", ctime=" + ctime +
        ", remark=" + remark +
        ", status=" + status +
        "}";
    }
}
