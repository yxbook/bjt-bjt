package com.j4sc.bjt.system.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 短信平台配置
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/28 10:47
 * @Version: 1.0
 **/
@Configuration
public class SMSConfig {
    @Value("${j4sc.sms.account-sid}")
    private String accountSid;
    @Value("${j4sc.sms.auth-token}")
    private String authToken;

    public String getAccountSid() {
        return accountSid;
    }

    public void setAccountSid(String accountSid) {
        this.accountSid = accountSid;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
