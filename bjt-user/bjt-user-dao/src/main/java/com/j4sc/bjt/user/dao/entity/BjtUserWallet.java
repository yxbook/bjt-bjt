package com.j4sc.bjt.user.dao.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 用户钱包表
 * </p>
 *
 * @author LongRou
 * @since 2018-04-19
 */
@TableName("bjt_user_wallet")
public class BjtUserWallet extends Model<BjtUserWallet> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     */
    @ApiModelProperty(value = "用户编号")
    @TableId("user_id")
    private String userId;
    /**
     * 余额
     */
    @ApiModelProperty(value = "余额")
    private BigDecimal balance;
    /**
     * 累计消费
     */
    @ApiModelProperty(value = "累计消费")
    private BigDecimal consumption;
    /**
     * 累计充值
     */
    @ApiModelProperty(value = "累计充值")
    private BigDecimal recharge;
    /**
     * 积分
     */
    @ApiModelProperty(value = "积分")
    private Integer score;

    @ApiModelProperty(value = "创建时间")
    private Long ctime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getConsumption() {
        return consumption;
    }

    public void setConsumption(BigDecimal consumption) {
        this.consumption = consumption;
    }

    public BigDecimal getRecharge() {
        return recharge;
    }

    public void setRecharge(BigDecimal recharge) {
        this.recharge = recharge;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

    @Override
    public String toString() {
        return "BjtUserWallet{" +
        "userId=" + userId +
        ", balance=" + balance +
        ", consumption=" + consumption +
        ", recharge=" + recharge +
        ", score=" + score +
        ", ctime=" + ctime +
        "}";
    }
}
