package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.user.dao.entity.BjtUserPermission;
import com.j4sc.bjt.user.dao.entity.BjtUserRole;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/13 12:06
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-user-server",path = "/BjtUserPermission")
public interface UserPermissionClient extends BaseApiService<BjtUserPermission>{
    @RequestMapping(value = "/selectBaseAll" ,method = RequestMethod.GET)
    BaseResult<List<BjtUserPermission>> selectBaseAll();
}
