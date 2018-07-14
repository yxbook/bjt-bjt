package com.j4sc.bjt.carpark.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 停车申请表
 * </p>
 *
 * @author LongRou
 * @since 2018-05-15
 */
@TableName("bjt_carpark_apply")
public class BjtCarparkApply extends Model<BjtCarparkApply> {

    private static final long serialVersionUID = 1L;

    /**
     * 申请ID
     */
    @TableId(value = "apply_id", type = IdType.AUTO)
    private Integer applyId;
    /**
     * 公司名称
     */
    private String company;
    /**
     * 公司编号
     */
    @TableField("company_id")
    private Integer companyId;
    /**
     * 申请人
     */
    private String applyer;
    /**
     * 状态:1待审核，2已通过，3未通过
     */
    private Integer status;
    /**
     * 申请人编号
     */
    @TableField("applyer_id")
    private String applyerId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 审批人
     */
    private String approver;
    /**
     * 审批人编号
     */
    @TableField("approver_id")
    private String approverId;
    /**
     * 审批时间
     */
    @TableField("approval_time")
    private Long approvalTime;
    /**
     * 申请时间
     */
    @TableField("apply_time")
    private Long applyTime;
    /**
     * 楼宇编号
     */
    @TableField("build_id")
    private Integer buildId;
    /**
     * 楼宇名称
     */
    @TableField("build_name")
    private String buildName;
    /**
     * 停车场名称
     */
    @TableField("space_name")
    private String spaceName;
    /**
     * 停车场编号
     */
    @TableField("space_id")
    private Integer spaceId;
    /**
     * 停车申请明细
     */
    private String detail;
    /**
     * 审批意见
     */
    private String opinion;
    /**
     * 申请人账号
     */
    @TableField("apply_username")
    private String applyUsername;
    /**
     * 创建时间
     */
    private Long ctime;


    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
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

    public String getApplyerId() {
        return applyerId;
    }

    public void setApplyerId(String applyerId) {
        this.applyerId = applyerId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getApproverId() {
        return approverId;
    }

    public void setApproverId(String approverId) {
        this.approverId = approverId;
    }

    public Long getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(Long approvalTime) {
        this.approvalTime = approvalTime;
    }

    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getApplyUsername() {
        return applyUsername;
    }

    public void setApplyUsername(String applyUsername) {
        this.applyUsername = applyUsername;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    @Override
    protected Serializable pkVal() {
        return this.applyId;
    }

    @Override
    public String toString() {
        return "BjtCarparkApply{" +
        "applyId=" + applyId +
        ", company=" + company +
        ", companyId=" + companyId +
        ", applyer=" + applyer +
        ", status=" + status +
        ", applyerId=" + applyerId +
        ", remark=" + remark +
        ", approver=" + approver +
        ", approverId=" + approverId +
        ", approvalTime=" + approvalTime +
        ", applyTime=" + applyTime +
        ", buildId=" + buildId +
        ", buildName=" + buildName +
        ", spaceName=" + spaceName +
        ", spaceId=" + spaceId +
        ", detail=" + detail +
        ", opinion=" + opinion +
        ", applyUsername=" + applyUsername +
        ", ctime=" + ctime +
        "}";
    }
}
