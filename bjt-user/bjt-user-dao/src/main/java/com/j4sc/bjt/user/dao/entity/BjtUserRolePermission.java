package com.j4sc.bjt.user.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 角色权限关联表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-19
 */
@TableName("bjt_user_role_permission")
public class BjtUserRolePermission extends Model<BjtUserRolePermission> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "role_permission_id", type = IdType.AUTO)
    private Integer rolePermissionId;
    /**
     * 角色编号
     */
    @TableField("role_id")
    private Integer roleId;
    /**
     * 权限编号
     */
    @TableField("permission_id")
    private Integer permissionId;


    public Integer getRolePermissionId() {
        return rolePermissionId;
    }

    public void setRolePermissionId(Integer rolePermissionId) {
        this.rolePermissionId = rolePermissionId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    protected Serializable pkVal() {
        return this.rolePermissionId;
    }

    @Override
    public String toString() {
        return "BjtUserRolePermission{" +
        "rolePermissionId=" + rolePermissionId +
        ", roleId=" + roleId +
        ", permissionId=" + permissionId +
        "}";
    }
}
