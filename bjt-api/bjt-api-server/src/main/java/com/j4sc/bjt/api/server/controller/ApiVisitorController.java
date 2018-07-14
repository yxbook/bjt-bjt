package com.j4sc.bjt.api.server.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.ParkBuildGuardClient;
import com.j4sc.bjt.api.server.client.ParkBuildVisitorClient;
import com.j4sc.bjt.api.server.client.UserManageClient;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildVisitor;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/4 17:07
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/visitor")
@Api(tags = {"帮家团访客申请服务"}, description = "帮家团访客申请服务 - 需授权")
public class ApiVisitorController extends BaseJwtController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiVisitorController.class);
    @Autowired
    private ParkBuildVisitorClient parkBuildVisitorClient;
    @Autowired
    private UserManageClient userManageClient;
    @Autowired
    ParkBuildGuardClient parkBuildGuardClient;

    @ApiOperation(value = "处理访客申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "visitorId", value = "记录编号", required = true),
            @ApiImplicitParam(name = "type", value = "操作类型：type为2表示同意、为3表示拒绝", required = true)
    })
    @RequestMapping(value = "update/BuildVisitor", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateVisitor(@RequestBody Map<String, Object> params) {
        LOGGER.info("处理访客申请");
        params.put("userId", getUserId());
        params.put("buildId", -1);
        return parkBuildVisitorClient.updateBuildVisitor(params);
    }

    @ApiOperation(value = "获取访客记录/邀请记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
            @ApiImplicitParam(name = "type", value = "类型：1、访客记录；2、邀请记录", dataType = "int")
    })
    @RequestMapping(value = "select/BuildVisitorPage", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkBuildVisitor>> selectBuildVisitorPage(@RequestParam Map<String, Object> params) {
        LOGGER.info("获取访客记录");
        params.put("userId", getUserId());
        return parkBuildVisitorClient.selectBuildVisitorPage(params);
    }

    @ApiOperation(value = "获取被邀请记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
    })
    @RequestMapping(value = "select/VisitorPage", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkBuildVisitor>> selectVisitorPage(@RequestParam Map<String, Object> params) {
        LOGGER.info("获取被邀请记录");
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(getUserId());
        if (userBaseResult.status != BaseResultEnum.SUCCESS.getStatus() || userBaseResult.getData() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "用户不存在");
        }
        params.put("visitor_phone", userBaseResult.getData().getUsername());
        params.put("type", 2);
        return parkBuildVisitorClient.selectPage(params);
    }

    @ApiOperation(value = "新增邀请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "visitorPhone", value = "访客手机号", required = true),
            @ApiImplicitParam(name = "visitorName", value = "访客姓名", required = true),
            @ApiImplicitParam(name = "visitMatter", value = "访问事宜", required = true),
            @ApiImplicitParam(name = "visitTime", value = "访问时间", required = true)
    })
    @RequestMapping(value = "add/BuildVisitor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addVisitor(@RequestBody Map<String, Object> params) {
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(params.get("visitorPhone").toString(), new NotNullValidator("访客手机号"))
                .on(params.get("visitorName").toString(), new NotNullValidator("访客姓名"))
                .on(params.get("visitMatter").toString(), new NotNullValidator("访问事宜"))
                .on(params.get("visitTime").toString(), new NotNullValidator("访问时间"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(getUserId());
        if (userBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return userBaseResult;
        }
        if (userBaseResult.getData() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "被访者不存在");
        }
        params.put("interviewUserId", userBaseResult.getData().getUserId());
        params.put("interviewName", userBaseResult.getData().getRealname());
        params.put("type", 2);//类型：1、访客记录；2、邀请记录
        params.put("status", 2);//状态:1、待处理；2、已通过；3、未通过
        params.put("interviewPhone", userBaseResult.getData().getUsername());

        return parkBuildVisitorClient.addBuildVisitor(params);
    }

    @ApiOperation(value = "删除访客记录", notes = "删除访客记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "1表示访客记录，2表示邀请记录"),
            @ApiImplicitParam(name = "who", value = "1表示邀请者，2表示被邀请者"),
            @ApiImplicitParam(name = "list", value = "访客记录编号数组"),
    })
    @RequestMapping(value = "delete/BuildVisitor", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult deleteBuildVisitor(@RequestBody Map<String, Object> params) {
        LOGGER.info("deleteBuildVisitor = >删除访客记录");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(params.get("type").toString(), new NotNullValidator("访客类型"))
                .on(params.get("who").toString(), new NotNullValidator("访客标识"))
                .on(params.get("list").toString(), new NotNullValidator("访客记录编号数组"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        List<String> list = (List<String>) params.get("list");
        return parkBuildVisitorClient.deleteBuildVisitor(params.get("type").toString(), params.get("who").toString(), list);
    }

}
