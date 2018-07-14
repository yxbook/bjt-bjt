package com.j4sc.bjt.user.common;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 支付订单
 * </p>
 *
 * @author LongRou
 * @since 2018-05-11
 */
@TableName("pay_order")
public class PayOrder extends Model<PayOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId(value = "order_id",type = IdType.UUID)
    private String orderId;
    /**
     * 系统ID
     */
    @TableField("system_id")
    private Integer systemId;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;
    /**
     * 操作用户账号
     */
    private String username;
    /**
     * 生成时间
     */
    private Long ctime;
    /**
     * 订单金额
     */
    private BigDecimal money;
    /**
     * 订单标题（支付宝 微信用）
     */
    private String name;
    /**
     * 订单内容（支付宝 微信用）
     */
    private String info;
    /**
     * 订单内容
     */
    @TableField("info_self")
    private String infoSelf;
    /**
     * 订单类型-由系统自定义
     */
    private Integer type;
    /**
     * 订单状态-0：交易创建，-1：未付款交易超时关闭，或支付完成后全额退款，1：交易支付完成，2：交易结束，不可退款
     */
    private Integer state;
    /**
     * 回调接口
     */
    @TableField("notify_url")
    private String notifyUrl;
    /**
     * 回调JSON请求体
     */
    @TableField("notify_json")
    private String notifyJson;
    /**
     * 支付渠道
     */
    private String channel;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getSystemId() {
        return systemId;
    }

    public void setSystemId(Integer systemId) {
        this.systemId = systemId;
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

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfoSelf() {
        return infoSelf;
    }

    public void setInfoSelf(String infoSelf) {
        this.infoSelf = infoSelf;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getNotifyJson() {
        return notifyJson;
    }

    public void setNotifyJson(String notifyJson) {
        this.notifyJson = notifyJson;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    protected Serializable pkVal() {
        return this.orderId;
    }

    @Override
    public String toString() {
        return "PayOrder{" +
        "orderId=" + orderId +
        ", systemId=" + systemId +
        ", userId=" + userId +
        ", username=" + username +
        ", ctime=" + ctime +
        ", money=" + money +
        ", name=" + name +
        ", info=" + info +
        ", infoSelf=" + infoSelf +
        ", type=" + type +
        ", state=" + state +
        ", notifyUrl=" + notifyUrl +
        ", notifyJson=" + notifyJson +
        ", channel=" + channel +
        "}";
    }
}
