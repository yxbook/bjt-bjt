package com.j4sc.bjt.user.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.auth.dao.entity.AuthRole;
import com.j4sc.auth.dao.entity.AuthRolePermission;
import com.j4sc.auth.dao.entity.AuthUser;
import com.j4sc.auth.dao.entity.AuthUserRole;
import com.j4sc.bjt.user.dao.entity.BjtUserRole;
import com.j4sc.bjt.user.dao.entity.BjtUserRolePermission;
import com.j4sc.bjt.user.dao.entity.BjtUserUser;
import com.j4sc.bjt.user.dao.entity.BjtUserUserRole;
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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/13 12:07
 * @Version: 1.0
 **/
@RestController
@RequestMapping("BjtUserRole")
@Api(tags = {"角色管理"}, description = "角色管理")
public class BjtUserRoleController extends BaseController<BjtUserRole, BjtUserRoleService> implements BaseApiService<BjtUserRole> {
    @Autowired
    BjtUserRolePermissionService userRolePermissionService;
    @Autowired
    BjtUserUserRoleService userUserRoleService;

    @ApiOperation(value = "根据ID删除楼宇角色",notes = "根据ID删除楼宇角色")
    @RequestMapping(value = "delete/RoleAuto", method = RequestMethod.DELETE)
    public BaseResult deleteRoleAuto(@RequestParam("roleId")String roleId,
                                     @RequestParam("buildId")String buildId) {

        BjtUserRole userRole = service.selectOne(new EntityWrapper<BjtUserRole>().eq("role_id",roleId)
       .eq("build_id",buildId));
        if (null == userRole)return new BaseResult(BaseResultEnum.ERROR,"删除失败");
        return super.deleteById(roleId);
    }
    @ApiOperation(value = "为角色添加权限",notes = "为角色添加权限")
    @RequestMapping(value = "add/addPermissionByRoleId", method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult insertPermissionByRoleId(@RequestBody Map<String, Object> params) {
        BjtUserRole authRole = service.selectById((Integer)params.get("rodeId"));
        if (null == authRole)return new BaseResult(BaseResultEnum.ERROR,"角色不存在！");
        List<Integer> list = (List<Integer>) params.get("list");

        if (userRolePermissionService.replacePermissionByRoleId(authRole.getRoleId(),list))return new BaseResult(BaseResultEnum.SUCCESS,"保存成功");
        return new BaseResult(BaseResultEnum.ERROR,"更新权限失败");
    }

}
