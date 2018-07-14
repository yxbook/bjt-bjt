package com.j4sc.bjt.api.server.controller.web;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.ParkBuildGuardClient;
import com.j4sc.bjt.api.server.client.ParkBuildGuardMainClient;
import com.j4sc.bjt.api.server.client.UserManageClient;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuard;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuardMain;
import com.j4sc.bjt.user.dao.entity.BjtUserUser;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 门禁申请
 * @Author: chengyz
 * @CreateDate: 2018 2018/4/9 09:49
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "admin/guard")
@Api(tags = {"帮家团门禁申请审批服务"}, description = "帮家团门禁申请审批服务 - web授权: 要加上token名称为：j4sc-b-token，值为楼宇编号")
public class ApiGuardManageController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiGuardManageController.class);

    @Autowired
    ParkBuildGuardClient parkBuildGuardClient;
    @Autowired
    UserManageClient userManageClient;
    @Autowired
    ParkBuildGuardMainClient parkBuildGuardMainClient;

    //门禁申请审批
    @ApiOperation(value = "门禁申请审批", notes = "门禁申请审批")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "门禁申请编号", name = "guardMainId", required = true),
            @ApiImplicitParam(value = "操作类型：1、拒绝；2、同意", name = "type", required = true),
            @ApiImplicitParam(value = "权限失效时间：拒绝时可不填", name = "endTime"),
            @ApiImplicitParam(value = "拒绝时必填的审批意见", name = "opinion"),
    })
    @RequestMapping(value = "update/GuardMain", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateGuardMain(@RequestBody Map<String, Object> params) {
        LOGGER.info("门禁申请审批 = >updateGuardMain");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(params.get("guardMainId") + "", new NotNullValidator("门禁申请编号"))
                .on(params.get("type") + "", new NotNullValidator("操作类型"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        //检测当前用户是否存在
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(getUserId());
        if (userBaseResult.getData() == null || userBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return userBaseResult;
        }
        //检测当前用户姓名是否为空
        if (userBaseResult.getData().getRealname() == null || "".equals(userBaseResult.getData().getRealname())) {
            return new BaseResult(BaseResultEnum.ERROR, "当前用户姓名为空");
        }
        params.put("approvalUserId", getUserId());
        params.put("approvalUsername", userBaseResult.getData().getUsername());
        params.put("approvalRealname", userBaseResult.getData().getRealname());
        params.put("buildId", getBuildId());
        return parkBuildGuardMainClient.updateGuardMain(params);
    }

    @ApiOperation(value = "获取门禁申请记录", notes = "获取门禁申请记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页显示记录数", dataType = "int")
    })
    @RequestMapping(value = "select/GuardMainPage", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkBuildGuardMain>> selectGuardMainPage(
            @RequestParam(name = "page", defaultValue = "1")int page,
            @RequestParam(name = "size", defaultValue = "20")int size) {
        LOGGER.info("获取门禁申请记录 = > selectGuardMainPage" + "用户ID：" + getUserId() + " 楼宇ID：" + getBuildId());
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("userId", getUserId());
        params.put("buildId", getBuildId());
        return parkBuildGuardMainClient.selectGuardMainPage(params);
    }

    @ApiOperation(value = "获取门禁申请记录(后台管理)", notes = "获取门禁申请记录(后台管理)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页显示记录数", dataType = "int")
    })
    @RequestMapping(value = "select/PageGuardMain", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkBuildGuardMain>> selectGuardMainPage(@RequestParam Map<String, Object> params) {
        LOGGER.info("获取门禁申请记录后台管理 = >");
        params.put("build_id", getBuildId());
        return parkBuildGuardMainClient.selectPage(params);
    }

    @ApiOperation(value = "获取门禁申请明细", notes = "获取门禁申请明细")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "申请记录编号(公司管理员查询时必需)", name = "guardMainId")
    })
    @RequestMapping(value = "select/GuardList", method = RequestMethod.GET)
    public BaseResult<List<BjtParkBuildGuard>> selectGuardList(@RequestParam Map<String, Object> params) {
        LOGGER.info("获取门禁申请记录 = > selectGuardList" + "用户ID：" + getUserId() + " 楼宇ID：" + getBuildId());
        params.put("userId", getUserId());
        params.put("buildId", getBuildId());
        return parkBuildGuardClient.selectGuardList(params);
    }

    @ApiOperation(value = "获取门禁权限列表", notes = "获取门禁权限列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页显示记录数", dataType = "int")
    })
    @RequestMapping(value = "select/PageGuard", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkBuildGuard>> selectPageGuard(@RequestParam Map<String, Object> params) {
        LOGGER.info("获取门禁权限列表 = > selectGuardList" + "用户ID：" + getUserId() + " 楼宇ID：" + getBuildId());
        params.put("build_id", getBuildId());
        params.put("orderBy", "ctime");
        return parkBuildGuardClient.selectPage(params);
    }

    @ApiOperation(value = "禁止/启用门禁权限", notes = "禁止/启用门禁权限")
    @RequestMapping(value = "update/BuildGuard", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateBuildGuard(@RequestBody Map<String, Object> params) {
        LOGGER.info("禁止/启用门禁权限 = > selectGuardList" + "用户ID：" + getUserId() + " 楼宇ID：" + getBuildId());
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(params.get("operate") + "", new NotNullValidator("操作类型"))
                .on(params.get("user_id") + "", new NotNullValidator("用户编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        String operate = params.get("operate").toString();
        params.remove("operate");
        params.put("build_id", getBuildId());
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(params.get("user_id").toString());
        if (userBaseResult.status != BaseResultEnum.SUCCESS.getStatus() || userBaseResult.getData() == null) {
            return userBaseResult;
        }
        BaseResult<List<BjtParkBuildGuard>> guardBaseResult = parkBuildGuardClient.selectAll(params);
        if (guardBaseResult.status != BaseResultEnum.SUCCESS.getStatus() || guardBaseResult.getData().size() <1) {
            return guardBaseResult;
        }
        //operate为1表示加入黑名单，operate为2表示启用
        if ("1".equals(operate)) {
            if (getUserId().equals(userBaseResult.getData().getUserId())) {
                return new BaseResult(BaseResultEnum.ERROR, "不能将自己加入黑名单");
            }
            userBaseResult.getData().setLocked(1);//设置为锁定状态
            guardBaseResult.getData().get(0).setType(-1);//权限  1.正常 0.申请中 -1.禁止
        }
        if ("2".equals(operate)){
            userBaseResult.getData().setLocked(0);//设置为正常状态
            guardBaseResult.getData().get(0).setType(1);//权限  1.正常 0.申请中 -1.禁止
        }
        userManageClient.updateUser(userBaseResult.getData());
        return parkBuildGuardClient.updateById(guardBaseResult.getData().get(0));
    }


}
