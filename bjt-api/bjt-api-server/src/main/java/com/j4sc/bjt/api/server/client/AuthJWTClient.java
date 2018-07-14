package com.j4sc.bjt.api.server.client;

import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/3/31 11:13
 * @Version: 1.0
 **/
@FeignClient(value = "j4sc-auth-server",path = "/jwt")
public interface AuthJWTClient {
    @RequestMapping(value = {"/getToken"},method = {RequestMethod.GET}    )
    BaseResult getToken(@RequestParam("userName") String var1, @RequestParam("nickName") String var2, @RequestParam("userId") String var3);
}
