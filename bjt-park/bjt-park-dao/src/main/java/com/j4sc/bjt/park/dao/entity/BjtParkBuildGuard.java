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
 * 用户门禁权限表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-26
 */
@TableName("bjt_park_build_guard")
public class BjtParkBuildGuard extends Model<BjtParkBuildGuard> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ApiModelProperty(value = "门禁编号")
    @TableId(value = "guard_id", type = IdType.AUTO)
    private Integer guardId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long ctime;
    /**
     * 楼宇ID
     */
    @ApiModelProperty(value = "楼宇ID")
    @TableField("build_id")
    private Integer buildId;
    /**
     * 楼宇名称
     */
    @ApiModelProperty(value = "楼宇名称")
    @TableField("build_name")
    private String buildName;
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    @TableField("user_id")
    private String userId;
    /**
     * 用户账号
     */
    @ApiModelProperty(value = "用户账号")
    private String username;
    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名")
    @TableField("user_realname")
    private String userRealname;
    /**
     * 帮助申请人ID
     */
    @ApiModelProperty(value = "帮助申请人ID")
    @TableField("apply_user_id")
    private String applyUserId;
    /**
     * 帮助申请人账号
     */
    @ApiModelProperty(value = "帮助申请人账号")
    @TableField("apply_username")
    private String applyUsername;
    /**
     * 帮助申请人真实姓名
     */
    @ApiModelProperty(value = "帮助申请人真实姓名")
    @TableField("apply_realname")
    private String applyRealname;
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
     * 门牌号
     */
    @ApiModelProperty(value = "门牌号")
    @TableField("door_plate")
    private String doorPlate;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    @TableField("id_number")
    private String idNumber;
    /**
     * 门禁申请主表编号
     */
    @ApiModelProperty(value = "门禁申请主表编号")
    @TableField("guard_main_id")
    private Integer guardMainId;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remake;
    /**
     * 权限  1.正常 0.申请中 -1.禁止
     */
    @ApiModelProperty(value = "权限  1.正常 0.申请中 -1.禁止")
    private Integer type;
    /**
     * 权限失效日期
     */
    @ApiModelProperty(value = "权限失效日期")
    private Long endTime;


    public Integer getGuardId() {
        return guardId;
    }

    public void setGuardId(Integer guardId) {
        this.guardId = guardId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getApplyUsername() {
        return applyUsername;
    }

    public void setApplyUsername(String applyUsername) {
        this.applyUsername = applyUsername;
    }

    public String getApplyRealname() {
        return applyRealname;
    }

    public void setApplyRealname(String applyRealname) {
        this.applyRealname = applyRealname;
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

    public String getDoorPlate() {
        return doorPlate;
    }

    public void setDoorPlate(String doorPlate) {
        this.doorPlate = doorPlate;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Integer getGuardMainId() {
        return guardMainId;
    }

    public void setGuardMainId(Integer guardMainId) {
        this.guardMainId = guardMainId;
    }

    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.guardId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BjtParkBuildGuard{" +
        "guardId=" + guardId +
        ", ctime=" + ctime +
        ", buildId=" + buildId +
        ", buildName=" + buildName +
        ", userId=" + userId +
        ", username=" + username +
        ", userRealname=" + userRealname +
        ", applyUserId=" + applyUserId +
        ", applyUsername=" + applyUsername +
        ", applyRealname=" + applyRealname +
        ", companyName=" + companyName +
        ", companyId=" + companyId +
        ", doorPlate=" + doorPlate +
        ", idNumber=" + idNumber +
        ", type=" + type +
        ", guardMainId=" + guardMainId +
        ", remake=" + remake +
        ", endTime=" + endTime +
        "}";
    }
}
