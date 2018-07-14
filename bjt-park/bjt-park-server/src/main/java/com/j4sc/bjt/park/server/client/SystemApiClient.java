package com.j4sc.bjt.park.server.client;

import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/3/30 17:38
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-system-server",path = "/api")
public interface SystemApiClient{
    @RequestMapping(value = "sendEmail", method = RequestMethod.POST)
    BaseResult createAuthenticationToken(@RequestParam("username") String username);

    @RequestMapping(value = "sendRegisterSMS", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult sendRegisterSMS(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "generateCode", method = RequestMethod.POST)
    BaseResult generateCode(@RequestParam("username") String username,@RequestParam("codeType") String codeType);

    @RequestMapping(value = "getCode", method = RequestMethod.GET)
    BaseResult getCode(@RequestParam("username") String username,@RequestParam("codeType") String codeType);

    @RequestMapping(value = "removeCode", method = RequestMethod.DELETE)
    BaseResult removeCode(@RequestParam("username") String username,@RequestParam("codeType") String codeType);

    @RequestMapping(value = "sendToPushQueue", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult sendToPushQueue(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "sendMessage", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult sendMessage(@RequestBody Map<String, Object> params);
}
