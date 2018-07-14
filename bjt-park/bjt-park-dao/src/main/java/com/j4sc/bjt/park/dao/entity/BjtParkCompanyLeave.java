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
 * 请假、外出等申请表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-16
 */
@TableName("bjt_park_company_leave")
public class BjtParkCompanyLeave extends Model<BjtParkCompanyLeave> {

    private static final long serialVersionUID = 1L;

    /**
     * 申请编号
     */
    @ApiModelProperty(value = "申请编号")
    @TableId(value = "leave_id", type = IdType.AUTO)
    private Integer leaveId;
    /**
     * 申请人编号
     */
    @ApiModelProperty(value = "申请人编号")
    @TableField("user_id")
    private String userId;
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    @TableField("begin_time")
    private Long beginTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    @TableField("end_time")
    private Long endTime;
    /**
     * 时长
     */
    @ApiModelProperty(value = "时长")
    @TableField("long_time")
    private Integer longTime;
    /**
     * 事由
     */
    @ApiModelProperty(value = "事由")
    private String reason;
    /**
     * 图片json字符串
     */
    @ApiModelProperty(value = "图片json字符串")
    private String resource;
    /**
     * 审批人
     */
    @ApiModelProperty(value = "审批人")
    private String approver;
    /**
     * 抄送人
     */
    @ApiModelProperty(value = "抄送人")
    private String copyer;
    /**
     * 抄送人编号
     */
    @ApiModelProperty(value = "抄送人编号")
    @TableField("copyer_id")
    private String copyerId;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 申请人姓名
     */
    @ApiModelProperty(value = "申请人姓名")
    private String applyer;
    /**
     * 申请人账号
     */
    @ApiModelProperty(value = "申请人账号")
    private String username;
    /**
     * 申请进度：1、待审批、2、通过、3、未通过
     */
    @ApiModelProperty(value = "申请进度：1、待审批、2、通过、3、未通过")
    private Integer status;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long ctime;
    /**
     * 申请类型：1、事假；2、病假；3、出差；4、其他
     */
    @ApiModelProperty("申请类型：1、事假；2、病假；3、出差；4、其他")
    private Integer type;


    public Integer getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Integer leaveId) {
        this.leaveId = leaveId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Integer getLongTime() {
        return longTime;
    }

    public void setLongTime(Integer longTime) {
        this.longTime = longTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getCopyer() {
        return copyer;
    }

    public void setCopyer(String copyer) {
        this.copyer = copyer;
    }

    public String getCopyerId() {
        return copyerId;
    }

    public void setCopyerId(String copyerId) {
        this.copyerId = copyerId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApplyer() {
        return applyer;
    }

    public void setApplyer(String applyer) {
        this.applyer = applyer;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        return this.leaveId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BjtParkCompanyLeave{" +
        "leaveId=" + leaveId +
        ", userId=" + userId +
        ", beginTime=" + beginTime +
        ", endTime=" + endTime +
        ", longTime=" + longTime +
        ", reason=" + reason +
        ", resource=" + resource +
        ", approver=" + approver +
        ", copyer=" + copyer +
        ", copyerId=" + copyerId +
        ", remark=" + remark +
        ", applyer=" + applyer +
        ", status=" + status +
        ", ctime=" + ctime +
        ", username=" + username +
        ", type=" + type +
        "}";
    }
}
