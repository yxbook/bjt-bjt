package com.j4sc.bjt.user.dao.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author LongRou
 * @since 2018-04-19
 */
@TableName("bjt_user_user")
public class BjtUserUser extends Model<BjtUserUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @ApiModelProperty(value = "用户编号")
    @TableId(value = "user_id", type = IdType.UUID)
    private String userId;
    /**
     * 帐号
     */
    @ApiModelProperty(value = "帐号（用户名）")
    private String username;
    /**
     * 密码MD5(密码+盐)
     */
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * 盐
     */
    private String salt;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String realname;
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像json字符串")
    private String avatar;
    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private String phone;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;
    /**
     * 性别（1、男；2、女）
     */
    @ApiModelProperty(value = "性别（1、男；2、女）")
    private Integer sex;
    /**
     * 状态(0:正常,1:锁定)
     */
    @ApiModelProperty(value = "状态(0:正常,1:锁定)")
    private Integer locked;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long ctime;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * openId,第三方登录使用
     */
    @ApiModelProperty(value = "openId,第三方登录使用")
    @TableField("open_id")
    private String openId;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickname;
    /**
     * 生日
     */
    @ApiModelProperty(value = "生日")
    private String birthday;
    /**
     * 公司
     */
    @ApiModelProperty(value = "公司")
    private String company;
    /**
     * 职务
     */
    @ApiModelProperty(value = "职务")
    private String job;
    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号")
    @TableField("id_number")
    private String idNumber;


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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getLocked() {
        return locked;
    }

    public void setLocked(Integer locked) {
        this.locked = locked;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

    @Override
    public String toString() {
        return "BjtUserUser{" +
        "userId=" + userId +
        ", username=" + username +
        ", password=" + password +
        ", salt=" + salt +
        ", realname=" + realname +
        ", avatar=" + avatar +
        ", phone=" + phone +
        ", email=" + email +
        ", sex=" + sex +
        ", locked=" + locked +
        ", ctime=" + ctime +
        ", remark=" + remark +
        ", openId=" + openId +
        ", nickname=" + nickname +
        ", birthday=" + birthday +
        ", company=" + company +
        ", job=" + job +
        ", idNumber=" + idNumber +
        "}";
    }
}
