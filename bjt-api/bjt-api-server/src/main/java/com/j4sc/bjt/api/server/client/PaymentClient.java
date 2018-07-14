package com.j4sc.bjt.api.server.client;

import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/11 16:14
 * @Version: 1.0
 **/
@FeignClient(value = "j4sc-pay-server")
public interface PaymentClient{
    @RequestMapping(value = "auth/ali/pay", method = RequestMethod.GET)
    BaseResult aliPay(@RequestParam Map<String,String> params);

    @RequestMapping(value = "auth/wechat/pay", method = RequestMethod.GET)
    BaseResult wechatPay(@RequestParam Map<String,String> params);
}
