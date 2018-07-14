package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.user.dao.entity.BjtUserRole;
import com.j4sc.bjt.user.dao.entity.BjtUserUserRole;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import io.swagger.annotations.ApiOperation;
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
 * @CreateDate: 2018 2018/4/13 12:06
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-user-server",path = "/BjtUserRole")
public interface UserRoleClient extends BaseApiService<BjtUserRole>{
    @RequestMapping(value = "delete/RoleAuto", method = RequestMethod.DELETE)
    BaseResult deleteRoleAuto(@RequestParam("roleId")String roleId,
                                     @RequestParam("buildId")String buildId);
    //为角色添加权限
    @RequestMapping(value = "add/addPermissionByRoleId", method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult insertPermissionByRoleId(@RequestBody Map<String, Object> params);

}
