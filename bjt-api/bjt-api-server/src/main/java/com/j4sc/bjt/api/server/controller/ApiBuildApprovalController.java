package com.j4sc.bjt.api.server.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.ParkBuildApprovalClient;
import com.j4sc.bjt.api.server.client.UserManageClient;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildApproval;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildUser;
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
 * @Description: 服务申请
 * @Author: chengyz
 * @CreateDate: 2018 2018/4/9 09:49
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/buildApproval")
@Api(tags = {"帮家团服务申请"}, description = "帮家团服务申请 - 需授权")
public class ApiBuildApprovalController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiBuildApprovalController.class);

    @Autowired
    private ParkBuildApprovalClient parkBuildApprovalClient;
    @Autowired
    private UserManageClient userManageClient;

    @ApiOperation(value = "用户提交服务申请", notes = "用户提交服务申请")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "申请事项", name = "application", required = true),
            @ApiImplicitParam(value = "具体内容", name = "content", required = true),
            @ApiImplicitParam(value = "审批进度", name = "progress(userId:下一步处理人编号, userRealname:下一步处理人姓名, username:下一步处理人账号)", required = true)
    })
    @RequestMapping(value = "save/BuildApproval", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveApproval(@RequestBody Map<String, Object> params) {
        LOGGER.info("提交服务申请 = >saveApproval");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on((String) params.get("application"), new NotNullValidator("申请事项"))
                .on((String) params.get("content"), new NotNullValidator("具体内容"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(getUserId());
        if (userBaseResult.getData() == null || userBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus())
            return userBaseResult;
        params.put("userId", getUserId());
        params.put("userRealname", userBaseResult.getData().getRealname());
        params.put("username", userBaseResult.getData().getUsername());
        return parkBuildApprovalClient.saveBuildApproval(params);
    }

    @ApiOperation(value = "获取服务申请记录", notes = "获取服务申请记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页显示记录数", dataType = "int"),
    })
    @RequestMapping(value = "select/BuildApprovalPage", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkBuildApproval>> selectBuildApprovalPage(
            @RequestParam(name = "page", defaultValue = "1", required = false) int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) int size,
            @RequestParam(name = "status", defaultValue = "0", required = false) int status) {
        LOGGER.info("获取服务申请记录 = >selectBuildApprovalPage" + "用户ID：" + getUserId() + " 楼宇ID：" + getBuildId());
        Map<String, Object> params = new HashMap<>();
        params.put("size", size);
        params.put("page", page);
        params.put("user_id", getUserId());
        params.put("orderBy", "ctime");
        return parkBuildApprovalClient.selectPage(params);
    }

    @ApiOperation(value = "获取楼宇管理员列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "buildId", value = "楼宇编号(转批时从申请记录中取)")
    })
    @RequestMapping(value = "select/BuildUserList", method = RequestMethod.GET)
    BaseResult<List<BjtParkBuildUser>> selectBuildUserList(@RequestParam(name = "buildId", required = false, defaultValue = "-1")int buildId) {
        LOGGER.info("获取楼宇管理员列表");
        return parkBuildApprovalClient.selectBuildUserList(getUserId(), buildId);
    }

}
