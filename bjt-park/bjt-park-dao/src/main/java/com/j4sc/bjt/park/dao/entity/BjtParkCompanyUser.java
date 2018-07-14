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
 * 公司管理员表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-18
 */
@TableName("bjt_park_company_user")
public class BjtParkCompanyUser extends Model<BjtParkCompanyUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    @TableId(value = "company_user_id", type = IdType.AUTO)
    private Integer companyUserId;
    /**
     * 公司ID
     */
    @ApiModelProperty(value = "公司编号")
    @TableField("company_id")
    private Integer companyId;
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户编号")
    @TableField("user_id")
    private String userId;
    /**
     * 员工类型：1、普通员工；2、工作台管理员；3、公司管理员
     */
    @ApiModelProperty(value = "员工类型：1、普通员工；2、工作台管理员；3、公司管理员")
    private Integer type;
    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    @TableField("company_name")
    private String companyName;
    /**
     * 职务
     */
    @ApiModelProperty(value = "职务")
    private String job;
    /**
     * 员工姓名
     */
    @ApiModelProperty(value = "员工姓名")
    private String realname;
    /**
     * 员工手机号
     */
    @ApiModelProperty(value = "员工手机号")
    private String phone;

    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    private String idNumber;
    /**
     * 部门
     */
    @ApiModelProperty(value = "部门")
    private String department;
    /**
     * 性别：1男，2女
     */
    @ApiModelProperty(value = "性别：1男，2女")
    private Integer sex;


    public Integer getCompanyUserId() {
        return companyUserId;
    }

    public void setCompanyUserId(Integer companyUserId) {
        this.companyUserId = companyUserId;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    @Override
    protected Serializable pkVal() {
        return this.companyUserId;
    }

    @Override
    public String toString() {
        return "BjtParkCompanyUser{" +
        "companyUserId=" + companyUserId +
        ", companyId=" + companyId +
        ", userId=" + userId +
        ", type=" + type +
        ", companyName=" + companyName +
        ", job=" + job +
        ", realname=" + realname +
        ", department=" + department +
        ", phone=" + phone +
        ", idNumber=" + idNumber +
        ", sex=" + sex +
        "}";
    }
}
