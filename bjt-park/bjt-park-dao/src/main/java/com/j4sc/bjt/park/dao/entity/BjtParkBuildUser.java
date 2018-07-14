package com.j4sc.bjt.park.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 楼宇管理员表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-18
 */
@TableName("bjt_park_build_user")
public class BjtParkBuildUser extends Model<BjtParkBuildUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "build_user_id", type = IdType.AUTO)
    private Integer buildUserId;
    /**
     * 楼宇ID
     */
    @TableField("build_id")
    private Integer buildId;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 姓名
     */
    private String realname;


    public Integer getBuildUserId() {
        return buildUserId;
    }

    public void setBuildUserId(Integer buildUserId) {
        this.buildUserId = buildUserId;
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

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    @Override
    protected Serializable pkVal() {
        return this.buildUserId;
    }

    @Override
    public String toString() {
        return "BjtParkBuildUser{" +
        "buildUserId=" + buildUserId +
        ", buildId=" + buildId +
        ", userId=" + userId +
        ", username=" + username +
        ", realname=" + realname +
        "}";
    }
}
