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
 * 员工向公司申请审批表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-17
 */
@TableName("bjt_park_company_approval")
public class BjtParkCompanyApproval extends Model<BjtParkCompanyApproval> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ApiModelProperty(value = "申请编号")
    @TableId(value = "c_approval_id", type = IdType.AUTO)
    private Integer cApprovalId;
    /**
     * 审批名称
     */
    @ApiModelProperty(value = "审批名称")
    private String name;
    /**
     * 审批内容
     */
    @ApiModelProperty(value = "审批内容")
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
    @TableField("company_id")
    private Integer companyId;
    /**
     * 审批发起人
     */
    @ApiModelProperty(value = "审批发起人编号")
    @TableField("user_id")
    private String userId;
    /**
     * 附件JSON字符串
     */
    @ApiModelProperty(value = "附件JSON字符串")
    private String sources;
    /**
     * 审批类型
     */
    @ApiModelProperty(value = "审批类型")
    private Integer type;
    /**
     * 备注
     */
    @ApiModelProperty(value = "楼宇编号")
    private String remake;


    public Integer getcApprovalId() {
        return cApprovalId;
    }

    public void setcApprovalId(Integer cApprovalId) {
        this.cApprovalId = cApprovalId;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    @Override
    protected Serializable pkVal() {
        return this.cApprovalId;
    }

    @Override
    public String toString() {
        return "BjtParkCompanyApproval{" +
        "cApprovalId=" + cApprovalId +
        ", name=" + name +
        ", content=" + content +
        ", ctime=" + ctime +
        ", utime=" + utime +
        ", companyId=" + companyId +
        ", userId=" + userId +
        ", sources=" + sources +
        ", type=" + type +
        ", remake=" + remake +
        "}";
    }
}
