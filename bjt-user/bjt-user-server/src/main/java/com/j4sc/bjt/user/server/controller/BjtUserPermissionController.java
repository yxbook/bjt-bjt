package com.j4sc.bjt.user.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.user.dao.entity.BjtUserPermission;
import com.j4sc.bjt.user.dao.entity.BjtUserRole;
import com.j4sc.bjt.user.rest.api.BjtUserPermissionService;
import com.j4sc.bjt.user.rest.api.BjtUserRoleService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("BjtUserPermission")
@Api(tags = {"权限管理"}, description = "权限管理")
public class BjtUserPermissionController extends BaseController<BjtUserPermission, BjtUserPermissionService> implements BaseApiService<BjtUserPermission> {

    @RequestMapping(value = "/selectBaseAll" ,method = RequestMethod.GET)
    public BaseResult<List<BjtUserPermission>> selectBaseAll() {
        return new BaseResult(BaseResultEnum.SUCCESS,service.selectList(new EntityWrapper<BjtUserPermission>()
        .ge("permission_id",14)));
    }
}
