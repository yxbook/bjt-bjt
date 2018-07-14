package com.j4sc.bjt.api.server.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.ParkCompanyClockRuleClient;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyClockRule;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/14 9:43
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/companyClockRule")
@Api(tags = {"帮家团工作台打卡规则"}, description = "帮家团工作台打卡规则-需授权")
public class ApiCompanyClockRuleController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCompanyClockRuleController.class);

    @Autowired
    private ParkCompanyClockRuleClient companyClockRuleClient;

    @ApiOperation(value = "添加打卡规则")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "地址", name = "address", paramType = "body"),
            @ApiImplicitParam(value = "WiFi名称", name = "wifiName", paramType = "body"),
            @ApiImplicitParam(value = "经度", name = "longitude", paramType = "body"),
            @ApiImplicitParam(value = "纬度", name = "latitude", paramType = "body"),
            @ApiImplicitParam(value = "上班时间", name = "inTime", paramType = "body"),
            @ApiImplicitParam(value = "下班时间", name = "outTime", paramType = "body"),
            @ApiImplicitParam(value = "上班日：1,2,3,4,5；用,号分开，表示周一到周五都上班", name = "workDay", paramType = "body")
    })
    @RequestMapping(value = "save/CompanyClockRule", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCompanyClockRule(@RequestBody Map<String, Object> params) {
        LOGGER.info("添加打卡规则 = > saveCompanyClock");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(params.get("address") + "", new NotNullValidator("地址"))
                .on(params.get("longitude") + "", new NotNullValidator("经度"))
                .on(params.get("latitude") + "", new NotNullValidator("纬度"))
                .on(params.get("inTime") + "", new NotNullValidator("上班时间"))
                .on(params.get("outTime") + "", new NotNullValidator("下班时间"))
                .on(params.get("workDay") + "", new NotNullValidator("工作日"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        params.put("userId", getUserId());
        return companyClockRuleClient.saveCompanyClockRule(params);
    }

    @ApiOperation(value = "修改打卡规则")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "地址", name = "address", paramType = "body"),
            @ApiImplicitParam(value = "wifi名称", name = "wifiName", paramType = "body"),
            @ApiImplicitParam(value = "经度", name = "longitude", paramType = "body"),
            @ApiImplicitParam(value = "纬度", name = "latitude", paramType = "body"),
            @ApiImplicitParam(value = "上班时间", name = "inTime", paramType = "body"),
            @ApiImplicitParam(value = "下班时间", name = "outTime", paramType = "body"),
            @ApiImplicitParam(value = "工作日", name = "workDay", paramType = "body")
    })
    @RequestMapping(value = "update/CompanyClockRule", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateCompanyClockRule(@RequestBody BjtParkCompanyClockRule bjtParkCompanyClockRule) {
        LOGGER.info("修改打卡规则 = > updateCompanyClock");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(getUserId(), new NotNullValidator("userId"))
                .on(bjtParkCompanyClockRule.getInTime(), new NotNullValidator("上班时间"))
                .on(bjtParkCompanyClockRule.getOutTime(), new NotNullValidator("下班时间"))
                .on(bjtParkCompanyClockRule.getWorkDay(), new NotNullValidator("工作日"))
                .on(String.valueOf(bjtParkCompanyClockRule.getCompanyId()), new NotNullValidator("companyId"))
                .on(String.valueOf(bjtParkCompanyClockRule.getRuleId()), new NotNullValidator("ruleId"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return companyClockRuleClient.updateById(bjtParkCompanyClockRule);
    }

    @ApiOperation(value = "删除打卡规则")
    @RequestMapping(value = "delete/CompanyClockRule", method = RequestMethod.DELETE)
    public BaseResult deleteCompanyClockRule() {
        LOGGER.info("删除打卡规则 = > deleteCompanyClockRule");
        return companyClockRuleClient.deleteCompanyClockRule(getUserId());
    }

    @ApiOperation(value = "查询打卡规则")
    @RequestMapping(value = "select/CompanyClockRule", method = RequestMethod.GET)
    public BaseResult<BjtParkCompanyClockRule> selectCompanyClockRule() {
        LOGGER.info("查询打卡规则 = > selectCompanyClockRule");
        return companyClockRuleClient.selectCompanyClockRule(getUserId());
    }


}
