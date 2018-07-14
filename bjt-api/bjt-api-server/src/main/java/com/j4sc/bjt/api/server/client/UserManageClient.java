package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.user.dao.entity.BjtUserRole;
import com.j4sc.bjt.user.dao.entity.BjtUserUser;
import com.j4sc.bjt.user.rest.api.BjtUserApiService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/3/30 16:41
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-user-server",path = "api")
public interface UserManageClient extends BjtUserApiService{
    @RequestMapping(
            value = {"/login"},
            method = {RequestMethod.POST},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    BaseResult<BjtUserUser> login(@RequestBody BjtUserUser bjtUserUser);
}
