package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuard;
import com.j4sc.bjt.user.dao.entity.BjtUserUserRole;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/13 12:06
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-user-server",path = "/BjtUserUserRole")
public interface UserUserRoleClient extends BaseApiService<BjtUserUserRole>{
    //为用户添加角色
    @RequestMapping(value = "add/replaceRoleByUserId", method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult insertRoleByUserId(@RequestBody Map<String, Object> params);
}
