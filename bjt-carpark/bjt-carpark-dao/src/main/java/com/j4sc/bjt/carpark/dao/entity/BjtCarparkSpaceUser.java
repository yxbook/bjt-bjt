package com.j4sc.bjt.carpark.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 停车场管理员中间表
 * </p>
 *
 * @author LongRou
 * @since 2018-05-15
 */
@TableName("bjt_carpark_space_user")
public class BjtCarparkSpaceUser extends Model<BjtCarparkSpaceUser> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "space_user_id", type = IdType.AUTO)
    private Integer spaceUserId;
    /**
     * 用户编号
     */
    @TableField("user_id")
    private String userId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 停车场编号
     */
    @TableField("space_id")
    private Integer spaceId;
    /**
     * 用户姓名
     */
    private String realname;
    /**
     * 停车场名称
     */
    @TableField("space_name")
    private String spaceName;


    public Integer getSpaceUserId() {
        return spaceUserId;
    }

    public void setSpaceUserId(Integer spaceUserId) {
        this.spaceUserId = spaceUserId;
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

    public Integer getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Integer spaceId) {
        this.spaceId = spaceId;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    @Override
    protected Serializable pkVal() {
        return this.spaceUserId;
    }

    @Override
    public String toString() {
        return "BjtCarparkSpaceUser{" +
        "spaceUserId=" + spaceUserId +
        ", userId=" + userId +
        ", username=" + username +
        ", spaceId=" + spaceId +
        ", realname=" + realname +
        ", spaceName=" + spaceName +
        "}";
    }
}
