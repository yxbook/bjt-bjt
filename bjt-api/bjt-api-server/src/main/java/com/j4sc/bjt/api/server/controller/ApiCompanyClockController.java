package com.j4sc.bjt.api.server.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.ParkCompanyClockClient;
import com.j4sc.bjt.api.server.client.ParkCompanyClockRuleClient;
import com.j4sc.bjt.api.server.client.ParkCompanyUserClient;
import com.j4sc.bjt.api.server.client.UserManageClient;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyClock;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyClockRule;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanySign;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyUser;
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
@RequestMapping(value = "api/companyClock")
@Api(tags = {"帮家团工作台打卡服务"}, description = "帮家团工作台打卡服务-需授权")
public class ApiCompanyClockController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCompanyClockController.class);

    @Autowired
    private ParkCompanyClockClient companyClockClient;
    @Autowired
    private UserManageClient userManageClient;
    @Autowired
    private ParkCompanyClockRuleClient companyClockRuleClient;
    @Autowired
    private ParkCompanyUserClient parkCompanyUserClient;

    @ApiOperation(value = "上班打卡")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "上班打卡地址", name = "inAddress", required = true),
            @ApiImplicitParam(value = "上班打卡经度", name = "inLongitude", required = true),
            @ApiImplicitParam(value = "上班打卡纬度", name = "inLatitude", required = true),
            @ApiImplicitParam(value = "打卡类型", name = "inType", required = true),
            @ApiImplicitParam(value = "上班打卡方式", name = "inWay", required = true),
    })
    @RequestMapping(value = "save/CompanyClock", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCompanyClock(@RequestBody BjtParkCompanyClock bjtParkCompanyClock){
        LOGGER.info("上班打卡 = > saveCompanyClock");
        if (bjtParkCompanyClock.getInType() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "上班打卡类型不能为空");
        }
        if (bjtParkCompanyClock.getInWay() == null) return new BaseResult(BaseResultEnum.ERROR, "上班打卡方式不能为空");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(bjtParkCompanyClock.getInAddress(), new NotNullValidator("上班打卡地址"))
                .on(bjtParkCompanyClock.getInLongitude(), new NotNullValidator("上班打卡经度"))
                .on(bjtParkCompanyClock.getInLatitude(), new NotNullValidator("上班打卡纬度"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(getUserId());
        if (userBaseResult.getData() == null || userBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) return new BaseResult(BaseResultEnum.ERROR, "当前用户不存在");
        bjtParkCompanyClock.setCtime(System.currentTimeMillis());
        bjtParkCompanyClock.setInTime(System.currentTimeMillis());
        bjtParkCompanyClock.setUserId(getUserId());
        bjtParkCompanyClock.setUserRealname(userBaseResult.getData().getRealname());
        return companyClockClient.insert(bjtParkCompanyClock);
    }

    @ApiOperation(value = "下班打卡")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "下班打卡地址", name = "outAddress", required = true),
            @ApiImplicitParam(value = "下班打卡经度", name = "outLongitude", required = true),
            @ApiImplicitParam(value = "下班打卡纬度", name = "outLatitude", required = true),
            @ApiImplicitParam(value = "下班打卡类型", name = "outType", required = true),
            @ApiImplicitParam(value = "打卡方式", name = "outWay", required = true),
            @ApiImplicitParam(value = "打卡编号", name = "clockId"),
    })
    @RequestMapping(value = "update/CompanyClock", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateCompanyClock(@RequestBody BjtParkCompanyClock bjtParkCompanyClock){
        LOGGER.info("下班打卡 = > updateCompanyClock");
        if (bjtParkCompanyClock.getOutWay() == null) return new BaseResult(BaseResultEnum.ERROR, "下班打卡方式不能为空");
        if (bjtParkCompanyClock.getOutType() == null) return new BaseResult(BaseResultEnum.ERROR, "下班打卡类型不能为空");

        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(bjtParkCompanyClock.getOutAddress(), new NotNullValidator("下班打卡地址"))
                .on(bjtParkCompanyClock.getOutLatitude(), new NotNullValidator("下班打卡纬度"))
                .on(bjtParkCompanyClock.getOutLongitude(), new NotNullValidator("下班打卡经度"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        bjtParkCompanyClock.setOutTime(System.currentTimeMillis());
        if ((bjtParkCompanyClock.getClockId() == null || "".equals(bjtParkCompanyClock.getClockId())) && !"".equals(bjtParkCompanyClock.getInAddress())) {
            BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(getUserId());
            if (userBaseResult.getData() == null || userBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) return new BaseResult(BaseResultEnum.ERROR, "当前用户不存在");
            bjtParkCompanyClock.setCtime(System.currentTimeMillis());
            bjtParkCompanyClock.setUserId(getUserId());
            bjtParkCompanyClock.setUserRealname(userBaseResult.getData().getRealname());
            return companyClockClient.insert(bjtParkCompanyClock);
        }
        return companyClockClient.updateById(bjtParkCompanyClock);
    }

    @ApiOperation(value = "获取个人打卡记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "开始时间", name = "beginTime"),
            @ApiImplicitParam(value = "结束时间", name = "endTime"),
    })
    @RequestMapping(value = "select/CompanyClockList", method = RequestMethod.GET)
    public BaseResult<List<BjtParkCompanyClock>> selectCompanyClockList(@RequestParam Map<String, Object> params){
        LOGGER.info("获取个人打卡记录 = > selectCompanyClockList");
        params.put("userId", getUserId());
        params.put("adminUserId", getUserId());
        return companyClockClient.selectCompanyClockList(params);
    }

    @ApiOperation(value = "获取单个员工的打卡记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "beginTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "公司编号"),
            @ApiImplicitParam(name = "userId", value = "用户编号", required = true)
    })
    @RequestMapping(value = "select/CompanyUserClockList", method = RequestMethod.GET)
    public BaseResult<List<BjtParkCompanyClock>> selectCompanyUserClockList(@RequestParam Map<String, Object> params){
        LOGGER.info("获取单个员工的打卡记录 = > selectCompanySignList");
        params.put("adminUserId", getUserId());
        return companyClockClient.selectCompanyClockList(params);
    }

    @ApiOperation(value = "获取员工自己的打卡统计信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "beginTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "公司编号"),
            @ApiImplicitParam(name = "userId", value = "用户编号", required = true)
    })
    @RequestMapping(value = "select/UserClockTotalList", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> selectUserClockTotalList(@RequestParam Map<String, Object> params){
        LOGGER.info("获取员工自己的打卡统计信息 = > selectUserClockTotalList");
        params.put("adminUserId", getUserId());
        BaseResult<List<BjtParkCompanyClock>> clockResult = companyClockClient.selectCompanyClockList(params);
        BaseResult<BjtParkCompanyClockRule> clockRuleBaseResult = companyClockRuleClient.selectCompanyClockRule(getUserId());
        Map<String, Object> resultMap = new HashMap<>();
        if (clockResult.getStatus() == BaseResultEnum.SUCCESS.getStatus()) {
            resultMap.put("companyClockList", clockResult.getData());
        } else {
            resultMap.put("companyClockList", null);
        }
        if (clockRuleBaseResult.getStatus() == BaseResultEnum.SUCCESS.getStatus()) {
            resultMap.put("companyClockRule", clockRuleBaseResult.getData());
        } else {
            resultMap.put("companyClockRule", null);
        }
        return new BaseResult(BaseResultEnum.SUCCESS, resultMap);
    }

    @ApiOperation(value = "获取所有员工打卡记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "beginTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "公司编号"),
    })
    @RequestMapping(value = "select/CompanyClockAllList", method = RequestMethod.GET)
    public BaseResult<List<BjtParkCompanyClock>> selectCompanyClockAllList(@RequestParam Map<String, Object> params){
        LOGGER.info("获取所有员工打卡记录 = > selectCompanyClockAllList");
        params.put("userId", getUserId());
        return companyClockClient.selectCompanyClockAllList(params);
    }

    @ApiOperation(value = "获取打卡记录统计信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "beginTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间")
    })
    @RequestMapping(value = "select/selectCompanyClockTotal", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> selectCompanyClockTotal(@RequestParam Map<String, Object> params){
        LOGGER.info("获取打卡记录统计信息 = > selectCompanyClockTotal");
        params.put("userId", getUserId());
        Map<String, Object> resultMap = new HashMap<>();
        BaseResult<BjtParkCompanyClockRule> clockRuleBaseResult = companyClockRuleClient.selectCompanyClockRule(getUserId());
        if (clockRuleBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return new BaseResult(BaseResultEnum.ERROR, clockRuleBaseResult.getData());
        }
        if (clockRuleBaseResult.getData() == null) {
            return new BaseResult(BaseResultEnum.SUCCESS, "打卡规则不存在");
        }
        resultMap.put("companyClockRule", clockRuleBaseResult.getData());
        BaseResult<List<BjtParkCompanyClock>> companyClockResult= companyClockClient.selectCompanyClockAllList(params);
        BaseResult<List<BjtParkCompanyUser>> companyUserResult = parkCompanyUserClient.selectCompanyUserList(getUserId());

        if (companyClockResult.getStatus() == BaseResultEnum.SUCCESS.getStatus()) {
            resultMap.put("companyClockList", companyClockResult.getData());
        } else {
            resultMap.put("companyClockList", null);
        }
        if (companyUserResult.getStatus() == BaseResultEnum.SUCCESS.getStatus()) {
            resultMap.put("companyUserList", companyUserResult.getData());
        } else {
            resultMap.put("companyUserList", null);
        }
        return new BaseResult(BaseResultEnum.SUCCESS, resultMap);
    }

}
