package com.j4sc.bjt.user.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.user.dao.entity.BjtUserRole;
import com.j4sc.bjt.user.dao.entity.BjtUserRolePermission;
import com.j4sc.bjt.user.rest.api.BjtUserRolePermissionService;
import com.j4sc.bjt.user.rest.api.BjtUserRoleService;
import com.j4sc.bjt.user.rest.api.BjtUserUserRoleService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/13 12:07
 * @Version: 1.0
 **/
@RestController
@RequestMapping("BjtUserRolePermission")
@Api(tags = {"角色权限关联管理"}, description = "角色权限关联管理")
public class BjtUserRolePermissionController extends BaseController<BjtUserRolePermission, BjtUserRolePermissionService> implements BaseApiService<BjtUserRolePermission> {

}
