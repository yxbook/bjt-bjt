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
 * 公司申请入驻记录表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-18
 */
@TableName("bjt_park_company_admission")
public class BjtParkCompanyAdmission extends Model<BjtParkCompanyAdmission> {

    private static final long serialVersionUID = 1L;

    /**
     * 公司编号
     */
    @ApiModelProperty(value = "公司编号")
    @TableId(value = "company_id")
    private Integer companyId;
    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    @TableField("company_name")
    private String companyName;
    /**
     * 邀请码
     */
    @ApiModelProperty(value = "邀请码")
    private String code;
    /**
     * 楼宇编号
     */
    @ApiModelProperty(value = "楼宇编号")
    @TableField("build_id")
    private Integer buildId;
    /**
     * 楼宇名称
     */
    @ApiModelProperty(value = "楼宇名称")
    @TableField("build_name")
    private String buildName;
    /**
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间")
    @TableField("apply_time")
    private Long applyTime;
    /**
     * 审批时间
     */
    @ApiModelProperty(value = "审批时间")
    @TableField("approval_time")
    private Long approvalTime;
    /**
     * 申请人用户编号
     */
    @ApiModelProperty(value = "申请人用户编号")
    @TableField("apply_user_id")
    private String applyUserId;
    /**
     * 审批人用户编号
     */
    @ApiModelProperty(value = "审批人用户编号")
    @TableField("approval_user_id")
    private String approvalUserId;
    /**
     * 状态：1、待审批；2、已通过、3未通过
     */
    @ApiModelProperty(value = "状态：1、待审批；2、已通过、3未通过")
    private Integer status;
    /**
     * 审批意见
     */
    @ApiModelProperty(value = "审批意见")
    private String opinion;
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
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long ctime;


    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    public Long getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(Long approvalTime) {
        this.approvalTime = approvalTime;
    }

    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getApprovalUserId() {
        return approvalUserId;
    }

    public void setApprovalUserId(String approvalUserId) {
        this.approvalUserId = approvalUserId;
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

    public String getApplyer() {
        return applyer;
    }

    public void setApplyer(String applyer) {
        this.applyer = applyer;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    @Override
    protected Serializable pkVal() {
        return this.companyId;
    }

    @Override
    public String toString() {
        return "BjtParkCompanyAdmission{" +
        "companyId=" + companyId +
        ", companyName=" + companyName +
        ", code=" + code +
        ", buildId=" + buildId +
        ", buildName=" + buildName +
        ", applyTime=" + applyTime +
        ", approvalTime=" + approvalTime +
        ", applyUserId=" + applyUserId +
        ", approvalUserId=" + approvalUserId +
        ", status=" + status +
        ", opinion=" + opinion +
        ", remark=" + remark +
        ", applyer=" + applyer +
        ", ctime=" + ctime +
        "}";
    }
}
