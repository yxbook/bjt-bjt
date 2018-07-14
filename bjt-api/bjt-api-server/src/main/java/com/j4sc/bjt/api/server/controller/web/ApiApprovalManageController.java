package com.j4sc.bjt.api.server.controller.web;

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
@RequestMapping(value = "admin/buildApproval")
@Api(tags = {"帮家团服务申请管理"}, description = "帮家团服务申请管理 - WEB 授权")
public class ApiApprovalManageController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiApprovalManageController.class);

    @Autowired
    private ParkBuildApprovalClient parkBuildApprovalClient;
    @Autowired
    private UserManageClient userManageClient;


    @ApiOperation(value = "服务申请审批", notes = "服务申请审批")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bApprovalId",value = "申请编号",  required = true),
            @ApiImplicitParam(name = "remark",value = "拒绝原因：拒绝时必填"),
            @ApiImplicitParam(name = "type", value = "操作类型1表示同意，2表示拒绝，3表示转批", required = true),
            @ApiImplicitParam(name = "progress", value = "下一步审批人审批进度对象，转批操作时必填(userId:下一步处理人编号, userRealname:下一步处理人姓名, username:下一步处理人账号)"),
    })
    @RequestMapping(value = "update/BuildApproval", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateApproval(@RequestBody Map<String, Object> params) {
        LOGGER.info("服务申请审批 = >updateApproval");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(getUserId(), new NotNullValidator("用户编号"))
                .on(params.get("bApprovalId").toString(), new NotNullValidator("申请编号"))
                .on(params.get("type").toString(), new NotNullValidator("类型"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        if ("3".equals(params.get("type"))) {
            if (params.get("progress") == null ) {
                return new BaseResult(BaseResultEnum.ERROR, "下一步审批进度不能为空");
            }
        }
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(getUserId());
        if (userBaseResult.getData() == null || userBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) return userBaseResult;
        params.put("userRealname", userBaseResult.getData().getRealname());
        params.put("userId", getUserId());
        return parkBuildApprovalClient.updateBuildApproval(params);
    }
    @ApiOperation(value = "获取服务申请记录", notes = "获取服务申请记录status为1则是待审批事项，否则查询所有记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页显示记录数", dataType = "int"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "int")
    })
    @RequestMapping(value = "select/BuildApprovalPage", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkBuildApproval>> selectBuildApprovalPage(
            @RequestParam(name = "page", defaultValue = "1", required = false)int page,
            @RequestParam(name = "size", defaultValue = "20", required = false)int size,
            @RequestParam(name = "status", defaultValue = "0", required = false)int status) {
        LOGGER.info("获取服务申请记录 = >selectBuildApprovalPage" + "用户ID：" +getUserId()+" 楼宇ID："+getBuildId());
        Map<String, Object> params = new HashMap<>();
        params.put("size", size);
        params.put("page", page);
        params.put("status", status);
        params.put("userId", getUserId());
        params.put("buildId", getBuildId());
        return parkBuildApprovalClient.selectBuildApprovalPage(params);
    }

    @ApiOperation(value = "获取楼宇管理员列表")
    @RequestMapping(value = "select/BuildUserList", method = RequestMethod.GET)
    BaseResult<List<BjtParkBuildUser>> selectBuildUserList() {
            LOGGER.info("获取楼宇管理员列表");
        return parkBuildApprovalClient.selectBuildUserList(getUserId(), getBuildId());
    }


}
