package com.j4sc.bjt.api.server.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.ParkCompanyLeaveClient;
import com.j4sc.bjt.api.server.client.UserManageClient;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyLeave;
import com.j4sc.bjt.user.dao.entity.BjtUserUser;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import com.j4sc.common.validator.LengthValidator;
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
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/14 9:43
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/companyLeave")
@Api(tags = {"帮家团工作台请假、加班、外出申请服务"}, description = "帮家团工作台请假、加班、外出申请服务-需授权")
public class ApiCompanyLeaveController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCompanyLeaveController.class);

    @Autowired
    private ParkCompanyLeaveClient companyLeaveClient;
    @Autowired
    private UserManageClient userManageClient;

    @ApiOperation(value = "提交申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reason", value = "事由", required = true),
            @ApiImplicitParam(name = "beginTime", value = "开始时间", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = true),
            @ApiImplicitParam(name = "progress", value = "下一步审批人审批进度对象userId,userRealname,username", required = true),
            @ApiImplicitParam(name = "type", value = "申请类型：1、事假；2、病假；3、出差；4、其他", required = true),
    })
    @RequestMapping(value = "save/CompanyLeave", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCompanyLeave(@RequestBody Map<String, Object> params) {
        LOGGER.info("提交申请 = > CompanyLeave");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on((String) params.get("reason"), new NotNullValidator("事由"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(getUserId());
        if (userBaseResult.getData() == null || userBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) return userBaseResult;
        params.put("userId", getUserId());
        params.put("applyer", userBaseResult.getData().getRealname());
        return companyLeaveClient.saveCompanyLeave(params);
    }

    @ApiOperation(value = "申请审批:1表示同意，2表示拒绝，3表示转批")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "leaveId", value = "申请编号", required = true),
            @ApiImplicitParam(name = "type", value = "操作类型1表示同意，2表示拒绝，3表示转批", required = true),
            @ApiImplicitParam(name = "progress", value = "下一步审批人审批进度对象，转批操作时必填userId,userRealname,username"),
    })
    @RequestMapping(value = "update/CompanyLeave", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateCompanyLeave(@RequestBody Map<String, Object> params) {
        LOGGER.info("申请审批 = > updateCompanyLeave");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(getUserId(), new NotNullValidator("用户编号"))
                .on(params.get("leaveId").toString(), new NotNullValidator("申请编号"))
                .on(params.get("type").toString(), new NotNullValidator("操作类型"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(getUserId());
        if (userBaseResult.getData() == null || userBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) return userBaseResult;
        params.put("userRealname", userBaseResult.getData().getRealname());
        params.put("userId", getUserId());
        return companyLeaveClient.updateCompanyLeave(params);
    }

    @ApiOperation(value = "待审批事项和审批记录（status为1则是待审批事项，否则查询所有记录）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "1、待审批事项 2、查询所有记录"),
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
    })
    @RequestMapping(value = "select/CompanyLeavePage", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkCompanyLeave>> selectCompanyLeavePage(@RequestParam Map<String, Object> params) {
        LOGGER.info("待审批事项和审批记录 = > selectCompanyLeavePage");
        params.put("userId", getUserId());
        params.put("orderBy", "ctime");
        return companyLeaveClient.selectCompanyLeavePage(params);
    }

    @ApiOperation(value = "用户查询申请记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "可不传：1、待审批、2、通过、3、未通过", name = "status"),
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
    })
    @RequestMapping(value = "select/LeaveSelfPage", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkCompanyLeave>> selectLeaveSelfPage(@RequestParam Map<String, Object> params) {
        LOGGER.info("用户查询申请记录 = > selectLeaveSelfPage");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(getUserId(), new NotNullValidator("userId")).doValidate().result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        params.put("user_id", getUserId());
        params.put("orderBy", "ctime");
        return companyLeaveClient.selectPage(params);
    }

}
