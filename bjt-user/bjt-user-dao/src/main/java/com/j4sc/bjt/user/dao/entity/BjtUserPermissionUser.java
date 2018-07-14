package com.j4sc.bjt.user.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 用户权限关联表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-19
 */
@TableName("bjt_user_permission_user")
public class BjtUserPermissionUser extends Model<BjtUserPermissionUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "user_permission_id", type = IdType.AUTO)
    private Integer userPermissionId;
    /**
     * 用户编号
     */
    @TableField("user_id")
    private String userId;
    /**
     * 权限编号
     */
    @TableField("permission_id")
    private Integer permissionId;
    /**
     * 权限类型(-1:减权限,1:增权限)
     */
    private Integer type;
    /**
     * 楼宇ID
     */
    @TableField("build_id")
    private Integer buildId;


    public Integer getUserPermissionId() {
        return userPermissionId;
    }

    public void setUserPermissionId(Integer userPermissionId) {
        this.userPermissionId = userPermissionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getBuildId() {
        return buildId;
    }

    public void setBuildId(Integer buildId) {
        this.buildId = buildId;
    }

    @Override
    protected Serializable pkVal() {
        return this.userPermissionId;
    }

    @Override
    public String toString() {
        return "BjtUserPermissionUser{" +
        "userPermissionId=" + userPermissionId +
        ", userId=" + userId +
        ", permissionId=" + permissionId +
        ", type=" + type +
        ", buildId=" + buildId +
        "}";
    }
}
