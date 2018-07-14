package com.j4sc.bjt.user.common;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/2 0002 下午 3:52
 * @Version: 1.0
 **/
public enum RechargeTypeEnum {
    ALI_PAY(0, "支付宝支付"),

    WEIXIN_PAY(1, "微信支付"),

    BANK_PAY(2, "银行卡支付");

    public int status;
    public String msg;

    private RechargeTypeEnum(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
