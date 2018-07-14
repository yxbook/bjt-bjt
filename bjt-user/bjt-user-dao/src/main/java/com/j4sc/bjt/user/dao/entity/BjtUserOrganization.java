package com.j4sc.bjt.user.dao.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 组织机构
 * </p>
 *
 * @author LongRou
 * @since 2018-04-19
 */
@TableName("bjt_user_organization")
public class BjtUserOrganization extends Model<BjtUserOrganization> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId("organization_id")
    private Integer organizationId;
    /**
     * 名称
     */
    private String name;
    /**
     * 上级机构编号
     */
    private Integer pid;
    /**
     * 描述
     */
    private String description;
    /**
     * 创建时间
     */
    private Long ctime;


    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    @Override
    protected Serializable pkVal() {
        return this.organizationId;
    }

    @Override
    public String toString() {
        return "BjtUserOrganization{" +
        "organizationId=" + organizationId +
        ", name=" + name +
        ", pid=" + pid +
        ", description=" + description +
        ", ctime=" + ctime +
        "}";
    }
}
