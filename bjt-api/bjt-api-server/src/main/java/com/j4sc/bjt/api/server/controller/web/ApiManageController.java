package com.j4sc.bjt.api.server.controller.web;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.j4sc.auth.dao.entity.AuthRole;
import com.j4sc.auth.dao.entity.AuthUser;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.*;
import com.j4sc.bjt.park.dao.entity.BjtParkBuild;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuard;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildUser;
import com.j4sc.bjt.user.dao.entity.BjtUserRole;
import com.j4sc.bjt.user.dao.entity.BjtUserRolePermission;
import com.j4sc.bjt.user.dao.entity.BjtUserUser;
import com.j4sc.bjt.user.dao.entity.BjtUserUserRole;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import com.j4sc.common.validator.NotNullValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/14 10:40
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "admin")
@Api(tags = {"楼宇管理员管理服务"}, description = "楼宇管理员管理服务 - WEB授权")
public class ApiManageController extends BaseJwtController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiManageController.class);
    @Autowired
    ParkBuildUserClient parkBuildUserClient;
    @Autowired
    UserUserClient userUserClient;
    @Autowired
    UserRoleClient userRoleClient;
    @Autowired
    UserUserRoleClient userUserRoleClient;
    @Autowired
    UserPermissionClient userPermissionClient;
    @Autowired
    UserRolePermissionClient userRolePermissionClient;
    @Autowired
    UserManageClient userManageClient;
    @Autowired
    ParkBuildClient parkBuildClient;
    @Autowired
    ParkBuildGuardClient parkBuildGuardClient;
    @Autowired
    AuthUserClient authUserClient;

    /*
     * 用户权限  user
     * */
    @ApiOperation(value = "查询楼宇管理员", notes = "查询楼宇管理员")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "user/select/UserAll", method = RequestMethod.GET)
    public BaseResult selectUserAll(@RequestParam Map<String, Object> params) {
        LOGGER.info(" 查询楼宇管理员");
        params.put("build_id", getBuildId());
        BaseResult buildUserResult = parkBuildUserClient.selectAll(params);
        if (buildUserResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) return buildUserResult;
        List<BjtParkBuildUser> buildUserList = (List<BjtParkBuildUser>) buildUserResult.getData();
        String[] userIdList = buildUserList.stream().map(i -> i.getUserId()).toArray(String[]::new);
        Map<String, Object> map = new HashMap();
        map.put("buildId", getBuildId());
        map.put("list", Arrays.asList(userIdList));
        return userManageClient.findUserBuildInfoByBU(map);
    }

    @ApiOperation(value = "增加楼宇管理员", notes = "增加楼宇管理员")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "user/add/BuildUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addBuildUser(@RequestBody Map<String, Object> params) {
        LOGGER.info(" 增加楼宇管理员");
        String username = (String) params.get("username");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(username, new NotNullValidator("用户名"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> userResult = userManageClient.findUserByUsername(username);
        if (userResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) return userResult;
        if (userResult.getData() == null) return new BaseResult(BaseResultEnum.ERROR, "当前用户不存在");
        if (userResult.getData().getLocked() != 0) return new BaseResult(BaseResultEnum.ERROR, "当前用户已被锁定");
        Map<String, Object> _params = new HashMap<>();
        _params.put("build_id", getBuildId());
        _params.put("user_id", userResult.getData().getUserId());
        BaseResult<List<BjtParkBuildUser>> buildUserListResult = parkBuildUserClient.selectAll(_params);
        if (buildUserListResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) return buildUserListResult;
        if (buildUserListResult.getData() != null && buildUserListResult.getData().size() > 0)
            return new BaseResult(BaseResultEnum.ERROR, "当前用户已是楼宇管理员");
        if (buildUserListResult.getData().size() == 0) {
            BjtParkBuildUser bjtParkBuildUser = new BjtParkBuildUser();
            bjtParkBuildUser.setBuildId(getBuildId());
            bjtParkBuildUser.setUserId(userResult.getData().getUserId());
            bjtParkBuildUser.setRealname(userResult.getData().getRealname());
            bjtParkBuildUser.setUsername(userResult.getData().getUsername());
            BaseResult baseResult = parkBuildUserClient.insert(bjtParkBuildUser);
            if (baseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
                return baseResult;
            }
            BaseResult<BjtParkBuild> buildBaseResult = parkBuildClient.selectById(String.valueOf(getBuildId()));
            //给楼宇管理员添加门禁权限
            BjtParkBuildGuard guard = new BjtParkBuildGuard();
            guard.setCtime(System.currentTimeMillis());
            guard.setBuildId(getBuildId());
            guard.setBuildName(buildBaseResult.getData().getName());
            guard.setUsername(username);
            guard.setUserRealname(userResult.getData().getRealname());
            guard.setUserId(userResult.getData().getUserId());
            guard.setType(1);//权限  1.正常 0.申请中 -1.禁止
            Calendar curr = Calendar.getInstance();
            curr.set(Calendar.YEAR, curr.get(Calendar.YEAR) + 1);
            guard.setEndTime(curr.getTimeInMillis());
            return parkBuildGuardClient.insert(guard);
        }
        return new BaseResult(BaseResultEnum.ERROR, "增加管理员失败");
    }

    @ApiOperation(value = "重置邀请码", notes = "重置邀请码")
    @RequestMapping(value = "build/update/resetCode", method = RequestMethod.PUT)
    public BaseResult resetCode() {
        LOGGER.info(" 重置邀请码...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(String.valueOf(getBuildId()), new NotNullValidator("楼宇编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        if (getBuildId() != getBuildId()) return new BaseResult(BaseResultEnum.ERROR, "非法操作");
        BaseResult<BjtParkBuild> buildResult = parkBuildClient.selectById(String.valueOf(getBuildId()));
        if (buildResult.getData() == null || buildResult.getStatus() != BaseResultEnum.SUCCESS.getStatus())
            return new BaseResult(BaseResultEnum.ERROR, "非法操作");
        buildResult.getData().setCode(getCode());
        buildResult.getData().setUtime(System.currentTimeMillis());
        buildResult.getData().setBuildId(getBuildId());
        return parkBuildClient.updateById(buildResult.getData());
    }
    //修改密码
    @ApiOperation(value = "用户修改密码", notes = "修改密码，修改后不需要重新登录。后期可能会修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", required = true),
            @ApiImplicitParam(name = "oldPassword", value = "老用户密码", required = true),
            @ApiImplicitParam(name = "newPassword", value = "新用户密码", required = true)
    })
    @ResponseBody
    @RequestMapping(value = "update/auth/Password", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateAuthPassword(@RequestBody Map<String, Object> params) {
        LOGGER.info(" 修改密码...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(params.get("oldPassword") + "", new NotNullValidator("旧密码"))
                .on(params.get("newPassword") + "", new NotNullValidator("新密码"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }

        BaseResult baseResult = authUserClient.updatePassword(params);
        return baseResult;
    }
    /**
     * 生成邀请码
     *
     * @return
     */
    private String getCode() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10).toUpperCase();
    }

    @ApiOperation(value = "查询楼宇详情", notes = "查询楼宇详情")
    @RequestMapping(value = "build/select/Build", method = RequestMethod.GET)
    public BaseResult selectBuild() {
        LOGGER.info(" 查询楼宇详情");
        return parkBuildClient.selectById(getBuildId() + "");
    }

    @ApiOperation(value = "查询楼宇角色", notes = "查询楼宇角色")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "user/select/RoleAll", method = RequestMethod.GET)
    public BaseResult selectRoleAll() {
        LOGGER.info(" 查询楼宇角色");
        Map<String, Object> map = new HashMap();
        map.put("build_id", getBuildId());
        BaseResult roleResult = userRoleClient.selectAll(map);
        return roleResult;
    }

    @ApiOperation(value = "为用户添加角色", notes = "为用户添加角色")
    @RequestMapping(value = "user/add/addRoleByUserId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addRoleByUserId(@RequestBody Map<String, Object> params) {
        Map<String, Object> map = new HashMap();
        map.put("build_id", getBuildId());
        map.put("user_id", params.get("userId"));

        BaseResult<List<BjtParkBuildUser>> buildUserResult = parkBuildUserClient.selectAll(map);
        LOGGER.error(buildUserResult.toString());
        if (buildUserResult.getStatus() != BaseResultEnum.SUCCESS.getStatus() || buildUserResult.getData().size() == 0)
            return new BaseResult(BaseResultEnum.ERROR, "数据有误");
        params.put("buildId", getBuildId() + "");
        return userUserRoleClient.insertRoleByUserId(params);
//        LOGGER.error(params.toString());
//        AuthUser authUser = service.selectById((Serializable) params.get("userId"));
//        if (null == authUser)return new BaseResult(BaseResultEnum.ERROR,"用户不存在！");
//        List<Integer> list = (List<Integer>) params.get("list");
//        LOGGER.error(list.toString());
//        if (authUserRoleService.replaceRoleByRoleId(authUser.getUserId(),list))return new BaseResult(BaseResultEnum.SUCCESS,"保存成功");
//        return new BaseResult(BaseResultEnum.ERROR,"更新用户角色失败");
    }

    /*
     * 角色权限
     *
     *
     *
     *
     * */
    @ApiOperation(value = "查询楼宇角色分页", notes = "查询楼宇角色分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", required = false, dataType = "int"),
    })
    @RequestMapping(value = "role/select/RolePage", method = RequestMethod.GET)
    public BaseResult selectRolePage(@RequestParam Map<String, Object> params) {
        LOGGER.info("查询楼宇角色分页");
        params.put("build_id", getBuildId());
        BaseResult roleResult = userRoleClient.selectPage(params);
        return roleResult;
    }

    @ApiOperation(value = "查询楼所有权限", notes = "查询楼所有权限")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "role/select/PermissionAll", method = RequestMethod.GET)
    public BaseResult selectPermissionAll() {
        LOGGER.info(" 查询楼所有权限");
        return userPermissionClient.selectBaseAll();
    }

    @ApiOperation(value = "添加楼宇角色", notes = "添加楼宇角色")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "role/add/Role", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addRole(@RequestBody BjtUserRole bjtUserRole) {
        LOGGER.info(" 添加楼宇角色...");
        bjtUserRole.setBuildId(getBuildId());
        bjtUserRole.setCtime(new Date().getTime());
        BaseResult baseResult = userRoleClient.insert(bjtUserRole);
        return baseResult;
    }

    @ApiOperation(value = "修改楼宇角色", notes = "修改楼宇角色")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "role/update/Role", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateRole(@RequestBody BjtUserRole bjtUserRole) {
        LOGGER.info(" 修改楼宇角色...");
        Map<String, Object> map = new HashMap();
        map.put("role_id", bjtUserRole.getRoleId());
        BaseResult<List<BjtUserRole>> listResult = userRoleClient.selectAll(map);
        if (listResult.getStatus() != BaseResultEnum.SUCCESS.getStatus() || listResult.getData().size() == 0)
            return listResult;
        if (listResult.getData().get(0).getBuildId() != getBuildId()) return new BaseResult(BaseResultEnum.ERROR, "");
        bjtUserRole.setBuildId(getBuildId());
        BaseResult baseResult = userRoleClient.updateById(bjtUserRole);
        return baseResult;
    }

    @ApiOperation(value = "根据角色id获取所拥有的权限", notes = "根据角色id获取所拥有的权限")
    @RequestMapping(value = "role/select/getPermissionByRoleId", method = RequestMethod.GET)
    public BaseResult<List<BjtUserRolePermission>> selectAuthRolePermisstionByAuthRoleId(@RequestParam("id") String id) {
        Map<String, Object> map = new HashMap();
        map.put("role_id", id);
        return userRolePermissionClient.selectAll(map);
    }

    @ApiOperation(value = "根据用户id获取所拥有的角色", notes = "根据角色id获取所拥有的权限")
    @RequestMapping(value = "select/getRoleByUserId", method = RequestMethod.GET)
    public BaseResult<List<BjtUserUserRole>> selectRoleByUserId(@RequestParam("userId") String userId) {
        Map<String, Object> map = new HashMap();
        map.put("user_id", userId);
        map.put("build_id", getBuildId());
        return userUserRoleClient.selectAll(map);
    }

    @ApiOperation(value = "为角色添加权限", notes = "为角色添加权限")
    @RequestMapping(value = "role/add/addPermissionByRoleId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult insertPermissionByRoleId(@RequestBody Map<String, Object> params) {
        return userRoleClient.insertPermissionByRoleId(params);
    }

    @ApiOperation(value = "删除楼宇角色", notes = "删除楼宇角色")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "role/delete/Role", method = RequestMethod.DELETE)
    public BaseResult deleteRoleAuto(@RequestParam String roleId) {
        LOGGER.info(" 删除楼宇角色..." + roleId);
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(roleId, new NotNullValidator("角色"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult baseResult = userRoleClient.deleteRoleAuto(roleId, getBuildId() + "");
        return baseResult;
    }

    @ApiOperation(value = "删除楼宇管理者", notes = "删除楼宇管理者")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, paramType = "body"),
    })
    @RequestMapping(value = "user/delete/BuildUser", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult<String> deleteBuildUser(@RequestBody Map<String, Object> params) {
        LOGGER.info(" 删除楼宇管理者" + params);
        String userId = params.get("userId").toString();
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(userId, new NotNullValidator("用户"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        if (getUserId().equals(userId)) {
            return new BaseResult(BaseResultEnum.ERROR, "不能删除自己");
        }
        params.put("build_id", getBuildId());
        params.put("user_id", userId);
        params.remove("userId");
        BaseResult<List<BjtParkBuildUser>> baseResult = parkBuildUserClient.selectAll(params);
        List<BjtParkBuildUser> list = baseResult.getData();

        if (list.size() == 0) return new BaseResult<>(BaseResultEnum.ERROR, "错误的请求");
        userManageClient.removeRolePermissionByBU(params);
        //删除门禁权限
        BaseResult<List<BjtParkBuildGuard>> guardBaseResult = parkBuildGuardClient.selectAll(params);
        if (guardBaseResult.status == BaseResultEnum.SUCCESS.getStatus() && baseResult.getData() != null && baseResult.getData().size() > 0) {
            parkBuildGuardClient.deleteById(guardBaseResult.getData().get(0).getGuardId().toString());
        }
        return parkBuildUserClient.deleteById(list.get(0).getBuildUserId() + "");
    }

    @ApiOperation(value = "统计楼宇总人数")
    @RequestMapping(value = "user/select/Count", method = RequestMethod.GET)
    public BaseResult selectCount() {
        LOGGER.info(" 统计楼宇总人数");
        return parkBuildUserClient.selectCount(String.valueOf(getBuildId()));
    }


}
