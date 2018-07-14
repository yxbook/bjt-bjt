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
 * 访客记录表
 * </p>
 *
 * @author LongRou
 * @since 2018-05-02
 */
@TableName("bjt_park_build_visitor")
public class BjtParkBuildVisitor extends Model<BjtParkBuildVisitor> {

    private static final long serialVersionUID = 1L;

    /**
     * 访客编号
     */
    @ApiModelProperty("访客编号")
    @TableId(value = "visitor_id", type = IdType.AUTO)
    private Integer visitorId;
    /**
     * 访客手机号
     */
    @ApiModelProperty("访客手机号")
    @TableField("visitor_phone")
    private String visitorPhone;
    /**
     * 访客姓名
     */
    @ApiModelProperty("访客姓名")
    @TableField("visitor_name")
    private String visitorName;
    /**
     * 身份证正面照path
     */
    @ApiModelProperty("身份证正面照path")
    @TableField("idcard_front")
    private String idcardFront;
    /**
     * 身份证反面照path
     */
    @ApiModelProperty("身份证反面照path")
    @TableField("idcard_back")
    private String idcardBack;
    /**
     * 访客正面照
     */
    @ApiModelProperty("访客正面照")
    @TableField("visitor_front")
    private String visitorFront;
    /**
     * 访问事宜
     */
    @ApiModelProperty("访问事宜")
    @TableField("visit_matter")
    private String visitMatter;
    /**
     * 被访人手机号
     */
    @ApiModelProperty("被访人手机号")
    @TableField("interview_phone")
    private String interviewPhone;
    /**
     * 访问时间
     */
    @ApiModelProperty("访问时间")
    @TableField("visit_time")
    private Long visitTime;
    /**
     * 状态:1、待处理；2、已通过；3、未通过
     */
    @ApiModelProperty("状态:1、待处理；2、已通过；3、未通过")
    private Integer status;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Long ctime;
    /**
     * 被访人姓名
     */
    @ApiModelProperty("被访人姓名")
    @TableField("interview_name")
    private String interviewName;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
    /**
     * 被访人编号
     * @return
     */
    @ApiModelProperty("被访人编号")
    @TableField("interview_user_id")
    private String interviewUserId;
    /**
     * 楼宇编号
     */
    @ApiModelProperty("楼宇编号")
    @TableField("build_id")
    private Integer buildId;
    /**
     * 使用次数
     */
    @ApiModelProperty("使用次数")
    @TableField("use_count")
    private Integer useCount;
    /**
     * 邀请者或被拜访者逻辑删除字段，默认为1，表示不删除，为2表示已删除
     */
    @ApiModelProperty("邀请者或被拜访者逻辑删除字段，默认为1，表示不删除，为2表示已删除")
    @TableField("interview_delete")
    private Integer interviewDelete;
    /**
     * 被邀请者逻辑删除字段，默认为1，表示不删除，为2表示已删除
     */
    @ApiModelProperty("被邀请者逻辑删除字段，默认为1，表示不删除，为2表示已删除")
    @TableField("visitor_delete")
    private Integer visitorDelete;
    /**
     * 类型：1、访客记录；2、邀请记录
     */
    @ApiModelProperty("类型：1、访客记录；2、邀请记录")
    private Integer type;


    public Integer getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Integer visitorId) {
        this.visitorId = visitorId;
    }

    public String getVisitorPhone() {
        return visitorPhone;
    }

    public void setVisitorPhone(String visitorPhone) {
        this.visitorPhone = visitorPhone;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getIdcardFront() {
        return idcardFront;
    }

    public void setIdcardFront(String idcardFront) {
        this.idcardFront = idcardFront;
    }

    public String getIdcardBack() {
        return idcardBack;
    }

    public void setIdcardBack(String idcardBack) {
        this.idcardBack = idcardBack;
    }

    public String getVisitorFront() {
        return visitorFront;
    }

    public void setVisitorFront(String visitorFront) {
        this.visitorFront = visitorFront;
    }

    public String getVisitMatter() {
        return visitMatter;
    }

    public void setVisitMatter(String visitMatter) {
        this.visitMatter = visitMatter;
    }

    public String getInterviewPhone() {
        return interviewPhone;
    }

    public void setInterviewPhone(String interviewPhone) {
        this.interviewPhone = interviewPhone;
    }

    public Long getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Long visitTime) {
        this.visitTime = visitTime;
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

    public String getInterviewName() {
        return interviewName;
    }

    public void setInterviewName(String interviewName) {
        this.interviewName = interviewName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInterviewUserId() {
        return interviewUserId;
    }

    public void setInterviewUserId(String interviewUserId) {
        this.interviewUserId = interviewUserId;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUseCount() {
        return useCount;
    }

    public void setUseCount(Integer useCount) {
        this.useCount = useCount;
    }

    public Integer getInterviewDelete() {
        return interviewDelete;
    }

    public void setInterviewDelete(Integer interviewDelete) {
        this.interviewDelete = interviewDelete;
    }

    public Integer getVisitorDelete() {
        return visitorDelete;
    }

    public void setVisitorDelete(Integer visitorDelete) {
        this.visitorDelete = visitorDelete;
    }

    @Override
    protected Serializable pkVal() {
        return this.visitorId;
    }

    @Override
    public String toString() {
        return "BjtParkBuildVisitor{" +
        "visitorId=" + visitorId +
        ", visitorPhone=" + visitorPhone +
        ", visitorName=" + visitorName +
        ", idcardFront=" + idcardFront +
        ", idcardBack=" + idcardBack +
        ", visitorFront=" + visitorFront +
        ", visitMatter=" + visitMatter +
        ", interviewPhone=" + interviewPhone +
        ", visitTime=" + visitTime +
        ", status=" + status +
        ", interviewUserId=" + interviewUserId +
        ", buildId=" + buildId +
        ", ctime=" + ctime +
        ", interviewName=" + interviewName +
        ", remark=" + remark +
        ", type=" + type +
        ", useCount=" + useCount +
        ", interviewDelete=" + interviewDelete +
        ", visitorDelete=" + visitorDelete +
        "}";
    }
}
