package com.j4sc.bjt.system.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * APP首页轮播图
 * </p>
 *
 * @author LongRou
 * @since 2018-05-15
 */
@TableName("bjt_system_app_carousel")
public class BjtSystemAppCarousel extends Model<BjtSystemAppCarousel> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "carousel_id", type = IdType.AUTO)
    @ApiModelProperty("主键ID")
    private Integer carouselId;
    /**
     * 类型：1为广告，2为普通图片
     */
    @ApiModelProperty("类型：1为广告，2为普通图片")
    private Integer type;
    /**
     * 图片路径
     */
    @ApiModelProperty("图片路径")
    @TableField("image_path")
    private String imagePath;
    /**
     * 显示顺序
     */
    @ApiModelProperty("显示顺序")
    @TableField("order_value")
    private Integer orderValue;
    /**
     * 状态：1为禁用、2为启用
     */
    @ApiModelProperty("状态：1为禁用、2为启用")
    private Integer status;
    /**
     * 跳转链接
     */
    @ApiModelProperty("跳转链接")
    private String link;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Long ctime;
    /**
     * 图片名称
     */
    @ApiModelProperty("图片名称")
    private String name;
    /**
     * 投放在APP首页的开始时间
     */
    @ApiModelProperty("投放在APP首页的开始时间")
    @TableField("show_start_time")
    private Long showStartTime;
    /**
     * 投放在APP首页的结束时间
     */
    @ApiModelProperty("投放在APP首页的结束时间")
    @TableField("show_end_time")
    private Long showEndTime;
    /**
     * 所属的APP
     */
    @ApiModelProperty("所属的APP")
    private Integer belong;


    public Integer getCarouselId() {
        return carouselId;
    }

    public void setCarouselId(Integer carouselId) {
        this.carouselId = carouselId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(Integer orderValue) {
        this.orderValue = orderValue;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getShowStartTime() {
        return showStartTime;
    }

    public void setShowStartTime(Long showStartTime) {
        this.showStartTime = showStartTime;
    }

    public Long getShowEndTime() {
        return showEndTime;
    }

    public void setShowEndTime(Long showEndTime) {
        this.showEndTime = showEndTime;
    }

    public Integer getBelong() {
        return belong;
    }

    public void setBelong(Integer belong) {
        this.belong = belong;
    }

    @Override
    protected Serializable pkVal() {
        return this.carouselId;
    }

    @Override
    public String toString() {
        return "BjtSystemAppCarousel{" +
        "carouselId=" + carouselId +
        ", type=" + type +
        ", imagePath=" + imagePath +
        ", orderValue=" + orderValue +
        ", status=" + status +
        ", link=" + link +
        ", remark=" + remark +
        ", ctime=" + ctime +
        ", name=" + name +
        ", showStartTime=" + showStartTime +
        ", showEndTime=" + showEndTime +
        ", belong=" + belong +
        "}";
    }
}
