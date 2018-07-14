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
 * 楼宇公告表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-16
 */
@TableName("bjt_park_build_notice")
public class BjtParkBuildNotice extends Model<BjtParkBuildNotice> {

    private static final long serialVersionUID = 1L;

    /**
     * 公告编号
     */
    @ApiModelProperty(value = "公告编号")
    @TableId(value = "b_notice_id", type = IdType.AUTO)
    private Integer bNoticeId;
    /**
     * 楼宇ID
     */
    @ApiModelProperty(value = "楼宇编号")
    @TableField("build_id")
    private Integer buildId;
    /**
     * 发布用户ID
     */
    @ApiModelProperty(value = "发布用户ID")
    @TableField("user_id")
    private String userId;
    /**
     * 发布者姓名
     */
    @ApiModelProperty(value = "发布者姓名")
    private String name;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;
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
     * 额外备注
     */
    @ApiModelProperty(value = "额外备注")
    private String source;


    public Integer getbNoticeId() {
        return bNoticeId;
    }

    public void setbNoticeId(Integer bNoticeId) {
        this.bNoticeId = bNoticeId;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    protected Serializable pkVal() {
        return this.bNoticeId;
    }

    @Override
    public String toString() {
        return "BjtParkBuildNotice{" +
        "bNoticeId=" + bNoticeId +
        ", buildId=" + buildId +
        ", userId=" + userId +
        ", name=" + name +
        ", content=" + content +
        ", ctime=" + ctime +
        ", utime=" + utime +
        ", source=" + source +
        "}";
    }
}
