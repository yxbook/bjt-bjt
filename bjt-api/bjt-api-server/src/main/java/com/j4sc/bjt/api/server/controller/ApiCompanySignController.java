package com.j4sc.bjt.api.server.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.ParkCompanySignClient;
import com.j4sc.bjt.api.server.client.UserManageClient;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanySign;
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

import java.math.BigInteger;
import java.util.List;
import java.util.Map;


/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/14 9:43
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/companySign")
@Api(tags = {"帮家团工作台签到服务"}, description = "帮家团工作台签到服务-需授权")
public class ApiCompanySignController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCompanySignController.class);

    @Autowired
    private ParkCompanySignClient companySignClient;
    @Autowired
    private UserManageClient userManageClient;

    @ApiOperation(value = "签到")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "签到地址", name = "address", required = true),
            @ApiImplicitParam(value = "经度", name = "longitude", required = true),
            @ApiImplicitParam(value = "纬度", name = "latitude", required = true)
    })
    @RequestMapping(value = "save/CompanySign", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCompanySign(@RequestBody BjtParkCompanySign bjtParkCompanySign){
        LOGGER.info("签到 = > saveCompanyClock");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(getUserId(), new NotNullValidator("用户编号"))
                .on(bjtParkCompanySign.getAddress(), new NotNullValidator("签到地址"))
                .on(bjtParkCompanySign.getLatitude(), new NotNullValidator("纬度"))
                .on(bjtParkCompanySign.getLongitude(), new NotNullValidator("经度"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(getUserId());
        if (userBaseResult.status != BaseResultEnum.SUCCESS.getStatus() || userBaseResult.getData() == null)
            return new BaseResult(BaseResultEnum.ERROR, "当前用户不存在");
        bjtParkCompanySign.setCtime(System.currentTimeMillis());
        bjtParkCompanySign.setUserId(getUserId());
        bjtParkCompanySign.setUserRealname(userBaseResult.getData().getRealname());
        return companySignClient.insert(bjtParkCompanySign);
    }

    @ApiOperation(value = "获取个人签到记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "beginTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间"),
    })
    @RequestMapping(value = "select/CompanySignList", method = RequestMethod.GET)
    public BaseResult<List<BjtParkCompanySign>> selectCompanySignList(@RequestParam Map<String, Object> params){
        LOGGER.info("获取个人签到记录 = > selectCompanySignList");
        params.put("userId", getUserId());
        params.put("adminUserId", getUserId());
        return companySignClient.selectCompanySignList(params);
    }

    @ApiOperation(value = "获取单个员工签到记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "beginTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "公司编号"),
            @ApiImplicitParam(name = "userId", value = "用户编号", required = true),
    })
    @RequestMapping(value = "select/CompanyUserSignList", method = RequestMethod.GET)
    public BaseResult<List<BjtParkCompanySign>> selectCompanyUserSignList(@RequestParam Map<String, Object> params){
        LOGGER.info("获取单个员工签到记录 = > selectCompanyUserSignList");
        params.put("adminUserId", getUserId());
        return companySignClient.selectCompanySignList(params);
    }

    @ApiOperation(value = "获取所有员工签到记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "beginTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "公司编号"),
    })
    @RequestMapping(value = "select/CompanySignAllList", method = RequestMethod.GET)
    public BaseResult<List<BjtParkCompanySign>> selectCompanySignAllList(@RequestParam Map<String, Object> params){
        LOGGER.info("获取所有员工签到记录 = > selectCompanySignAllList");
        params.put("userId", getUserId());
        return companySignClient.selectCompanySignAllList(params);
    }

}
