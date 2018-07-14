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
 * 邮件发送记录表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-19
 */
@TableName("bjt_user_email_box_record")
public class BjtUserEmailBoxRecord extends Model<BjtUserEmailBoxRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 邮件ID
     */
    @ApiModelProperty(value = "邮件ID")
    @TableId(value = "record_id", type = IdType.AUTO)
    private Integer recordId;
    /**
     * 发件人
     */
    @ApiModelProperty(value = "发件人")
    private String sender;
    /**
     * 发件人编号
     */
    @ApiModelProperty(value = "发件人编号")
    @TableField("sender_id")
    private String senderId;
    /**
     * 收件人编号
     */
    @ApiModelProperty(value = "收件人编号")
    @TableField("receiver_id")
    private String receiverId;
    /**
     * 抄送人
     */
    @ApiModelProperty(value = "抄送人")
    private String copyer;
    /**
     * 抄送人编号
     */
    @ApiModelProperty(value = "抄送人编号")
    @TableField("copyer_id")
    private String copyerId;
    /**
     * 主题
     */
    @ApiModelProperty(value = "主题")
    private String title;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;
    /**
     * 发送类型：0：普通、1：急件
     */
    @ApiModelProperty(value = "发送类型：0：普通、1：急件")
    @TableField("send_type")
    private Integer sendType;
    /**
     * 发送时间
     */
    @ApiModelProperty(value = "发送时间")
    @TableField("send_date")
    private Long sendDate;
    /**
     * 读取时间
     */
    @ApiModelProperty(value = "读取时间")
    @TableField("read_date")
    private Long readDate;
    /**
     * 邮箱类型：1：收件箱、2：垃圾箱
     */
    @ApiModelProperty(value = "邮箱类型：1：收件箱、2：垃圾箱")
    @TableField("box_type")
    private Integer boxType;
    /**
     * 邮件状态：0：未读、1：已读、2：回复、3：转发、4：全部转发
     */
    @ApiModelProperty(value = "邮件状态：0：未读、1：已读、2：回复、3：转发、4：全部转发")
    @TableField("email_status")
    private Integer emailStatus;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long ctime;
    /**
     * 附件json字符串
     */
    @ApiModelProperty(value = "附件json字符串")
    private String resource;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 发件的编号
     */
    @ApiModelProperty(value = "发件的编号")
    @TableField("email_id")
    private Integer emailId;
    /**
     * 收件人账号
     */
    @ApiModelProperty(value = "收件人账号")
    @TableField("receiver_username")
    private String receiverUsername;
    /**
     * 发件人头像
     */
    @ApiModelProperty(value = "发件人头像")
    @TableField("sender_avatar")
    private String senderAvatar;
    /**
     * 发件人账号
     */
    @ApiModelProperty(value = "发件人账号")
    @TableField("sender_username")
    private String senderUsername;


    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getCopyer() {
        return copyer;
    }

    public void setCopyer(String copyer) {
        this.copyer = copyer;
    }

    public String getCopyerId() {
        return copyerId;
    }

    public void setCopyerId(String copyerId) {
        this.copyerId = copyerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Long getSendDate() {
        return sendDate;
    }

    public void setSendDate(Long sendDate) {
        this.sendDate = sendDate;
    }

    public Long getReadDate() {
        return readDate;
    }

    public void setReadDate(Long readDate) {
        this.readDate = readDate;
    }

    public Integer getBoxType() {
        return boxType;
    }

    public void setBoxType(Integer boxType) {
        this.boxType = boxType;
    }

    public Integer getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(Integer emailStatus) {
        this.emailStatus = emailStatus;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getEmailId() {
        return emailId;
    }

    public void setEmailId(Integer emailId) {
        this.emailId = emailId;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    @Override
    protected Serializable pkVal() {
        return this.recordId;
    }

    @Override
    public String toString() {
        return "BjtUserEmailBoxRecord{" +
        "recordId=" + recordId +
        ", sender=" + sender +
        ", senderId=" + senderId +
        ", receiverId=" + receiverId +
        ", copyer=" + copyer +
        ", copyerId=" + copyerId +
        ", title=" + title +
        ", content=" + content +
        ", sendType=" + sendType +
        ", sendDate=" + sendDate +
        ", readDate=" + readDate +
        ", boxType=" + boxType +
        ", emailStatus=" + emailStatus +
        ", ctime=" + ctime +
        ", resource=" + resource +
        ", receiverUsername=" + receiverUsername +
        ", senderAvatar=" + senderAvatar +
        ", remark=" + remark +
        ", emailId=" + emailId +
        ", senderUsername=" + senderUsername +
        "}";
    }
}
