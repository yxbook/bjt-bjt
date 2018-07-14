package com.j4sc.bjt.user.server.controller;

import com.j4sc.bjt.user.dao.entity.BjtUserRecharge;
import com.j4sc.bjt.user.dao.entity.BjtUserUserRole;
import com.j4sc.bjt.user.rest.api.BjtUserRechargeService;
import com.j4sc.bjt.user.rest.api.BjtUserUserRoleService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/13 12:07
 * @Version: 1.0
 **/
@RestController
@RequestMapping("BjtUserUserRole")
@Api(tags = {"用户角色"}, description = "用户角色")
public class BjtUserUserRoleController extends BaseController<BjtUserUserRole, BjtUserUserRoleService> implements BaseApiService<BjtUserUserRole> {
    @ApiOperation(value = "为用户添加角色 过滤总管理",notes = "为用户添加角色 过滤总管理")
    @RequestMapping(value = "add/replaceRoleByUserId", method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult insertRoleByUserId(@RequestBody Map<String, Object> params){
        List<Integer> list = (List<Integer>) params.get("list");
        String userId = (String) params.get("userId");
        String buildId = (String) params.get("buildId");
        System.out.println(list + ";" + userId +";" + buildId);
        if (service.replaceRoleByBU(userId,buildId,list))return new BaseResult(BaseResultEnum.SUCCESS,"保存成功");
        return new BaseResult(BaseResultEnum.ERROR,"更新用户角色失败");
    }
}
