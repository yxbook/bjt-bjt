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
 * 公司向楼宇申请审批表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-27
 */
@TableName("bjt_park_build_approval")
public class BjtParkBuildApproval extends Model<BjtParkBuildApproval> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ApiModelProperty(value = "申请编号")
    @TableId(value = "b_approval_id", type = IdType.AUTO)
    private Integer bApprovalId;
    /**
     * 申请事项
     */
    @ApiModelProperty(value = "申请事项")
    private String application;
    /**
     * 具体内容
     */
    @ApiModelProperty(value = "具体内容")
    private String content;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long ctime;
    /**
     * 完成时间
     */
    @ApiModelProperty(value = "完成时间")
    private Long utime;
    /**
     * 审批管理方
     */
    @ApiModelProperty(value = "审批管理方")
    @TableField("build_id")
    private Integer buildId;
    /**
     * 审批申请方
     */
    @ApiModelProperty(value = "审批申请方")
    @TableField("company_id")
    private Integer companyId;
    /**
     * 审批发起人
     */
    @ApiModelProperty(value = "审批发起人")
    @TableField("user_id")
    private String userId;
    /**
     * 附件JSON字符串
     */
    @ApiModelProperty(value = "附件JSON字符串")
    private String sources;
    /**
     * 状态:1、待审批；2、已通过；3、拒绝
     */
    @ApiModelProperty(value = "状态:1、待审批；2、已通过；3、拒绝")
    private Integer status;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 审批人信息（Json字符串）
     */
    @ApiModelProperty(value = "审批人信息（Json字符串）")
    @TableField("approval_user")
    private String approvalUser;
    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    @TableField("company_name")
    private String companyName;
    /**
     * 申请人用户名
     */
    @ApiModelProperty(value = "申请人用户名")
    @TableField("username")
    private String username;
    /**
     * 申请人姓名
     */
    @ApiModelProperty(value = "申请人姓名")
    @TableField("user_realname")
    private String userRealname;


    public Integer getbApprovalId() {
        return bApprovalId;
    }

    public void setbApprovalId(Integer bApprovalId) {
        this.bApprovalId = bApprovalId;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
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

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSources() {
        return sources;
    }

    public void setSources(String sources) {
        this.sources = sources;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApprovalUser() {
        return approvalUser;
    }

    public void setApprovalUser(String approvalUser) {
        this.approvalUser = approvalUser;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserRealname() {
        return userRealname;
    }

    public void setUserRealname(String userRealname) {
        this.userRealname = userRealname;
    }

    @Override
    protected Serializable pkVal() {
        return this.bApprovalId;
    }

    @Override
    public String toString() {
        return "BjtParkBuildApproval{" +
        "bApprovalId=" + bApprovalId +
        ", application=" + application +
        ", content=" + content +
        ", ctime=" + ctime +
        ", utime=" + utime +
        ", buildId=" + buildId +
        ", companyId=" + companyId +
        ", userId=" + userId +
        ", sources=" + sources +
        ", status=" + status +
        ", username=" + username +
        ", userRealname=" + userRealname +
        ", remark=" + remark +
        ", approvalUser=" + approvalUser +
        ", companyName=" + companyName +
        "}";
    }
}
