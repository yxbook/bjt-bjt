package com.j4sc.bjt.park.dao.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 申请审批进度表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-16
 */
@TableName("bjt_park_company_progress")
public class BjtParkCompanyProgress extends Model<BjtParkCompanyProgress> {

    private static final long serialVersionUID = 1L;

    /**
     * 进度编号
     */
    @ApiModelProperty(value = "进度编号")
    @TableId(value = "progress_id", type = IdType.AUTO)
    private Integer progressId;
    /**
     * 申请审批编号
     */
    @ApiModelProperty(value = "申请审批编号")
    @TableField("leave_id")
    private Integer leaveId;
    /**
     * 上一步处理人编号
     */
    @ApiModelProperty(value = "上一步处理人编号")
    @TableField("last_user_id")
    private String lastUserId;
    /**
     * 上一步处理时间
     */
    @ApiModelProperty(value = "上一步处理时间")
    @TableField("last_time")
    private Long lastTime;
    /**
     * 上一步处理人真实姓名
     */
    @ApiModelProperty(value = "上一步处理人真实姓名")
    @TableField("last_user_realname")
    private String lastUserRealname;
    /**
     * 当前处理人编号
     */
    @ApiModelProperty(value = "当前处理人编号")
    @TableField("user_id")
    private String userId;
    /**
     * 当前处理人真实姓名
     */
    @ApiModelProperty(value = "当前处理人真实姓名")
    @TableField("user_realname")
    private String userRealname;
    /**
     * 当前处理人账号（手机号）
     */
    @ApiModelProperty(value = "当前处理人账号（手机号）")
    private String username;
    /**
     * 处理状态：0表示待审批，1表示同意，2表示拒绝，3表示转批
     */
    @ApiModelProperty(value = "处理状态：0表示待审批，1表示同意，2表示拒绝，3表示转批")
    private Integer status;
    /**
     * 处理意见
     */
    @ApiModelProperty(value = "处理意见")
    private String opinion;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long ctime;


    public Integer getProgressId() {
        return progressId;
    }

    public void setProgressId(Integer progressId) {
        this.progressId = progressId;
    }

    public Integer getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Integer leaveId) {
        this.leaveId = leaveId;
    }

    public String getLastUserId() {
        return lastUserId;
    }

    public void setLastUserId(String lastUserId) {
        this.lastUserId = lastUserId;
    }

    public Long getLastTime() {
        return lastTime;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }

    public String getLastUserRealname() {
        return lastUserRealname;
    }

    public void setLastUserRealname(String lastUserRealname) {
        this.lastUserRealname = lastUserRealname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserRealname() {
        return userRealname;
    }

    public void setUserRealname(String userRealname) {
        this.userRealname = userRealname;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    protected Serializable pkVal() {
        return this.progressId;
    }

    @Override
    public String toString() {
        return "BjtParkCompanyProgress{" +
        "progressId=" + progressId +
        ", leaveId=" + leaveId +
        ", lastUserId=" + lastUserId +
        ", lastTime=" + lastTime +
        ", lastUserRealname=" + lastUserRealname +
        ", userId=" + userId +
        ", userRealname=" + userRealname +
        ", status=" + status +
        ", username=" + username +
        ", opinion=" + opinion +
        ", remark=" + remark +
        ", ctime=" + ctime +
        "}";
    }
}
