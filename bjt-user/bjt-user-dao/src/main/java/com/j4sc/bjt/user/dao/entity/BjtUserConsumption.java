package com.j4sc.bjt.user.dao.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 用户消费记录
 * </p>
 *
 * @author LongRou
 * @since 2018-04-19
 */
@TableName("bjt_user_consumption")
public class BjtUserConsumption extends Model<BjtUserConsumption> {

    private static final long serialVersionUID = 1L;

    /**
     * 消费编号
     */
    @ApiModelProperty(value = "消费编号")
    @TableId(value = "consumption_id", type = IdType.AUTO)
    private Integer consumptionId;
    /**
     * 用户编号
     */
    @ApiModelProperty(value = "用户编号")
    @TableField("user_id")
    private String userId;
    /**
     * 消费金额
     */
    @ApiModelProperty(value = "消费金额")
    private BigDecimal money;
    /**
     * 消费详情
     */
    @ApiModelProperty(value = "消费详情")
    private String detail;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long ctime;
    /**
     * 交易流水号
     */
    @ApiModelProperty(value = "交易流水号")
    @TableField("trade_no")
    private String tradeNo;


    public Integer getConsumptionId() {
        return consumptionId;
    }

    public void setConsumptionId(Integer consumptionId) {
        this.consumptionId = consumptionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    @Override
    protected Serializable pkVal() {
        return this.consumptionId;
    }

    @Override
    public String toString() {
        return "BjtUserConsumption{" +
        "consumptionId=" + consumptionId +
        ", userId=" + userId +
        ", money=" + money +
        ", detail=" + detail +
        ", ctime=" + ctime +
        ", tradeNo=" + tradeNo +
        "}";
    }
}
