package com.j4sc.bjt.user.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 通讯录表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-19
 */
@TableName("bjt_user_address_list")
public class BjtUserAddressList extends Model<BjtUserAddressList> {

    private static final long serialVersionUID = 1L;

    /**
     * 通讯录主键ID
     */
    @ApiModelProperty(value = "通讯录编号")
    @TableId(value = "address_list_id", type = IdType.AUTO)
    private Integer addressListId;
    /**
     * 添加者的用户编号
     */
    @ApiModelProperty(value = "用户编号")
    @TableField("user_id")
    private String userId;
    /**
     * 被添加者的用户编号
     */
    @ApiModelProperty(value = "被添加者的用户编号")
    @TableField("slave_user_id")
    private String slaveUserId;
    /**
     * 状态: 1是正常、2是删除、3是黑名单
     */
    @ApiModelProperty(value = "状态: 1是正常、2是删除、3是黑名单")
    private Integer status;
    /**
     * 被添加者的昵称(添加者可改为备注)
     */
    @ApiModelProperty(value = "被添加者的昵称")
    @TableField("slave_nickname")
    private String slaveNickname;
    /**
     * 被添加者账户
     */
    @ApiModelProperty(value = "被添加者账户")
    @TableField("slave_username")
    private String slaveUsername;
    /**
     * 被添加者头像
     */
    @ApiModelProperty(value = "被添加者头像")
    private String avatar;

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


    public Integer getAddressListId() {
        return addressListId;
    }

    public void setAddressListId(Integer addressListId) {
        this.addressListId = addressListId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSlaveUserId() {
        return slaveUserId;
    }

    public void setSlaveUserId(String slaveUserId) {
        this.slaveUserId = slaveUserId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSlaveNickname() {
        return slaveNickname;
    }

    public void setSlaveNickname(String slaveNickname) {
        this.slaveNickname = slaveNickname;
    }

    public String getSlaveUsername() {
        return slaveUsername;
    }

    public void setSlaveUsername(String slaveUsername) {
        this.slaveUsername = slaveUsername;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    protected Serializable pkVal() {
        return this.addressListId;
    }

    @Override
    public String toString() {
        return "BjtUserAddressList{" +
                "addressListId=" + addressListId +
                ", userId=" + userId +
                ", slaveUserId=" + slaveUserId +
                ", status=" + status +
                ", avatar=" + avatar +
                ", slaveNickname=" + slaveNickname +
                ", slaveUsername=" + slaveUsername +
                ", remark=" + remark +
                ",ctime=" + ctime +
                "}";
    }
}
