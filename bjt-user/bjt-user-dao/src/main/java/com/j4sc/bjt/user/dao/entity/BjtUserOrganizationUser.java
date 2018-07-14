package com.j4sc.bjt.user.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 用户和组织机构关联表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-19
 */
@TableName("bjt_user_organization_user")
public class BjtUserOrganizationUser extends Model<BjtUserOrganizationUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "organization_user_id", type = IdType.AUTO)
    private Integer organizationUserId;
    /**
     * 用户编号
     */
    @TableField("user_id")
    private String userId;
    /**
     * 组织机构编号
     */
    @TableField("organization_id")
    private Integer organizationId;


    public Integer getOrganizationUserId() {
        return organizationUserId;
    }

    public void setOrganizationUserId(Integer organizationUserId) {
        this.organizationUserId = organizationUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    protected Serializable pkVal() {
        return this.organizationUserId;
    }

    @Override
    public String toString() {
        return "BjtUserOrganizationUser{" +
        "organizationUserId=" + organizationUserId +
        ", userId=" + userId +
        ", organizationId=" + organizationId +
        "}";
    }
}
