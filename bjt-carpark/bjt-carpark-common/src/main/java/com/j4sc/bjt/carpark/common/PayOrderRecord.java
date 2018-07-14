package com.j4sc.bjt.carpark.common;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 支付订单记录
 * </p>
 *
 * @author LongRou
 * @since 2018-05-11
 */
@TableName("pay_order_record")
public class PayOrderRecord extends Model<PayOrderRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(value = "record_id", type = IdType.AUTO)
    private Integer recordId;
    /**
     * 订单ID
     */
    @TableField("order_id")
    private String orderId;
    /**
     * 记录内容
     */
    private String content;
    /**
     * 记录类型
     */
    private Integer type;
    /**
     * 生成时间
     */
    private Long ctime;


    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    @Override
    protected Serializable pkVal() {
        return this.recordId;
    }

    @Override
    public String toString() {
        return "PayOrderRecord{" +
        "recordId=" + recordId +
        ", orderId=" + orderId +
        ", content=" + content +
        ", type=" + type +
        ", ctime=" + ctime +
        "}";
    }
}
