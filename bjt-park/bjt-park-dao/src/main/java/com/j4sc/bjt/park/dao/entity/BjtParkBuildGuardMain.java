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
 * 门禁申请主表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-26
 */
@TableName("bjt_park_build_guard_main")
public class BjtParkBuildGuardMain extends Model<BjtParkBuildGuardMain> {

    private static final long serialVersionUID = 1L;

    /**
     * 门禁申请主表编号
     */
    @ApiModelProperty(value = "门禁申请主表编号")
    @TableId(value = "guard_main_id", type = IdType.AUTO)
    private Integer guardMainId;
    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    @TableField("company_name")
    private String companyName;
    /**
     * 公司编号
     */
    @ApiModelProperty(value = "公司编号")
    @TableField("company_id")
    private Integer companyId;
    /**
     * 申请人编号
     */
    @ApiModelProperty(value = "申请人编号")
    @TableField("apply_user_id")
    private String applyUserId;
    /**
     * 申请人姓名
     */
    @ApiModelProperty(value = "申请人姓名")
    @TableField("apply_user_realname")
    private String applyUserRealname;
    /**
     * 申请人账号
     */
    @ApiModelProperty(value = "申请人账号")
    @TableField("apply_username")
    private String applyUsername;
    /**
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间")
    @TableField("apply_time")
    private Long applyTime;
    /**
     * 审批人编号
     */
    @ApiModelProperty(value = "审批人编号")
    @TableField("approval_user_id")
    private String approvalUserId;
    /**
     * 审批人姓名
     */
    @ApiModelProperty(value = "审批人姓名")
    @TableField("approval_user_realname")
    private String approvalUserRealname;
    /**
     * 审批人账号
     */
    @ApiModelProperty(value = "审批人账号")
    @TableField("approval_username")
    private String approvalUsername;
    /**
     * 审批时间
     */
    @ApiModelProperty(value = "审批时间")
    @TableField("approval_time")
    private Long approvalTime;
    /**
     * 申请明细Json字符串
     */
    @ApiModelProperty(value = "申请明细Json字符串")
    private String detail;
    /**
     * 类型:1、待审批，2、已通过，3、未通过
     */
    @ApiModelProperty(value = "类型:1、待审批，2、已通过，3、未通过")
    private Integer type;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long ctime;
    /**
     * 楼宇编号
     */
    @ApiModelProperty(value = "楼宇编号")
    @TableField("build_id")
    private Integer buildId;
    /**
     * 额外备注
     */
    @ApiModelProperty(value = "额外备注")
    private String remark;
    /**
     * 拒绝时可能要填写的处理意见
     */
    @ApiModelProperty(value = "拒绝时可能要填写的处理意见")
    private String opinion;
    /**
     * 权限失效日期
     */
    @ApiModelProperty(value = "权限失效日期")
    private Long endTime;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getGuardMainId() {
        return guardMainId;
    }

    public void setGuardMainId(Integer guardMainId) {
        this.guardMainId = guardMainId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getApplyUserRealname() {
        return applyUserRealname;
    }

    public void setApplyUserRealname(String applyUserRealname) {
        this.applyUserRealname = applyUserRealname;
    }

    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    public String getApprovalUserId() {
        return approvalUserId;
    }

    public void setApprovalUserId(String approvalUserId) {
        this.approvalUserId = approvalUserId;
    }

    public String getApprovalUserRealname() {
        return approvalUserRealname;
    }

    public void setApprovalUserRealname(String approvalUserRealname) {
        this.approvalUserRealname = approvalUserRealname;
    }

    public Long getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(Long approvalTime) {
        this.approvalTime = approvalTime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApplyUsername() {
        return applyUsername;
    }

    public void setApplyUsername(String applyUsername) {
        this.applyUsername = applyUsername;
    }

    public String getApprovalUsername() {
        return approvalUsername;
    }

    public void setApprovalUsername(String approvalUsername) {
        this.approvalUsername = approvalUsername;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.guardMainId;
    }

    @Override
    public String toString() {
        return "BjtParkBuildGuardMain{" +
                "guardMainId=" + guardMainId +
                ", companyName=" + companyName +
                ", companyId=" + companyId +
                ", applyUserId=" + applyUserId +
                ", applyUserRealname=" + applyUserRealname +
                ", applyUsername=" + applyUsername +
                ", applyTime=" + applyTime +
                ", type=" + type +
                ", approvalUserId=" + approvalUserId +
                ", approvalUserRealname=" + approvalUserRealname +
                ", approvalUsername=" + approvalUsername +
                ", approvalTime=" + approvalTime +
                ", detail=" + detail +
                ", ctime=" + ctime +
                ", buildId=" + buildId +
                ", remark=" + remark +
                ", opinion=" + opinion +
                ", endTime=" + endTime +
                "}";
    }
}
