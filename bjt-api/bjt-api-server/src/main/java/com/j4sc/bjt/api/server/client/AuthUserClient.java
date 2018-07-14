package com.j4sc.bjt.api.server.client;

import com.j4sc.auth.dao.entity.AuthUser;
import com.j4sc.common.base.BaseApiService;
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
 * @CreateDate: 2018 2018/3/31 11:13
 * @Version: 1.0
 **/
@FeignClient(value = "j4sc-auth-server", path = "/user")
public interface AuthUserClient extends BaseApiService<AuthUser> {

    @RequestMapping(value = "update/Password", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult updatePassword(@RequestBody Map<String, Object> params);

}
