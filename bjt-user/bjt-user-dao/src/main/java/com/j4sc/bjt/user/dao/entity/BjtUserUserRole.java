package com.j4sc.bjt.user.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 用户角色关联表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-19
 */
@TableName("bjt_user_user_role")
public class BjtUserUserRole extends Model<BjtUserUserRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "user_role_id", type = IdType.AUTO)
    private Integer userRoleId;
    /**
     * 用户编号
     */
    @TableField("user_id")
    private String userId;
    /**
     * 角色编号
     */
    @TableField("role_id")
    private Integer roleId;
    /**
     * 楼宇ID
     */
    @TableField("build_id")
    private Integer buildId;


    public Integer getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Integer userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    @Override
    protected Serializable pkVal() {
        return this.userRoleId;
    }

    @Override
    public String toString() {
        return "BjtUserUserRole{" +
        "userRoleId=" + userRoleId +
        ", userId=" + userId +
        ", roleId=" + roleId +
        ", buildId=" + buildId +
        "}";
    }
}
