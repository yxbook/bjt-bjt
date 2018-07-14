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
 * 邮箱表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-19
 */
@TableName("bjt_user_email_box")
public class BjtUserEmailBox extends Model<BjtUserEmailBox> {

    private static final long serialVersionUID = 1L;

    /**
     * 邮件ID
     */
    @ApiModelProperty(value = "邮件编号")
    @TableId(value = "email_id", type = IdType.AUTO)
    private Integer emailId;
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
     * 收件人
     */
    @ApiModelProperty(value = "收件人")
    private String receiver;
    /**
     * 收件人编号(Json字符串)
     */
    @ApiModelProperty(value = "收件人编号(Json字符串)")
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
     * 发件人邮箱类型：1：草稿箱、2：发件箱、3：垃圾箱
     */
    @ApiModelProperty(value = "发件人邮箱类型：1：草稿箱、2：发件箱、3：垃圾箱")
    @TableField("box_type")
    private Integer boxType;
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
     * 发件人账号
     */
    @ApiModelProperty(value = "发件人账号")
    @TableField("sender_username")
    private String senderUsername;
    /**
     * 收件人头像
     */
    @ApiModelProperty(value = "收件人头像")
    @TableField("receiver_avatar")
    private String receiverAvatar;
    /**
     * 发件人头像
     */
    @ApiModelProperty(value = "发件人头像")
    @TableField("sender_avatar")
    private String senderAvatar;


    public Integer getEmailId() {
        return emailId;
    }

    public void setEmailId(Integer emailId) {
        this.emailId = emailId;
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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
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

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverAvatar() {
        return receiverAvatar;
    }

    public void setReceiverAvatar(String receiverAvatar) {
        this.receiverAvatar = receiverAvatar;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    @Override
    protected Serializable pkVal() {
        return this.emailId;
    }

    @Override
    public String toString() {
        return "BjtUserEmailBox{" +
        "emailId=" + emailId +
        ", sender=" + sender +
        ", senderId=" + senderId +
        ", receiver=" + receiver +
        ", receiverId=" + receiverId +
        ", copyer=" + copyer +
        ", copyerId=" + copyerId +
        ", title=" + title +
        ", content=" + content +
        ", sendType=" + sendType +
        ", senderAvatar=" + senderAvatar +
        ", sendDate=" + sendDate +
        ", readDate=" + readDate +
        ", boxType=" + boxType +
        ", senderUsername=" + senderUsername +
        ", receiverAvatar=" + receiverAvatar +
        ", ctime=" + ctime +
        ", resource=" + resource +
        ", remark=" + remark +
        "}";
    }
}
