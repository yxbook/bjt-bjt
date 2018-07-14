package com.j4sc.bjt.api.server.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.CarparkCarClient;
import com.j4sc.bjt.api.server.client.UserManageClient;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkCar;
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
 * @CreateDate: 2018/5/11 11:43
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/carparkCar")
@Api(tags = {"帮家团车辆信息服务"}, description = "帮家团车辆信息服务 - 需授权")
public class ApiCarparkCarController extends BaseJwtController{

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCarparkCarController.class);

    @Autowired
    private UserManageClient userManageClient;
    @Autowired
    private CarparkCarClient carparkCarClient;
    @ApiOperation(value = "添加车辆信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "车主姓名", required = true),
            @ApiImplicitParam(name = "idNumber", value = "身份证号", required = true),
            @ApiImplicitParam(name = "licenceNumber", value = "驾驶证号", required = true),
            @ApiImplicitParam(name = "driveNumber", value = "行驶证号", required = true),
            @ApiImplicitParam(name = "resource", value = "车辆照片json数据", required = true),
            @ApiImplicitParam(name = "plateNumber", value = "车牌号", required = true),
            @ApiImplicitParam(name = "brand", value = "品牌", required = true),
            @ApiImplicitParam(name = "model", value = "型号", required = true),
            @ApiImplicitParam(name = "color", value = "颜色", required = true),
    })
    @RequestMapping(value = "save/CarparkCar", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCarparkApply(@RequestBody BjtCarparkCar bjtCarparkCar) {
        LOGGER.info("添加车辆信息");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(bjtCarparkCar.getDriveNumber(), new NotNullValidator("行驶证号"))
                .on(bjtCarparkCar.getIdNumber(), new NotNullValidator("身份证号"))
                .on(bjtCarparkCar.getName(), new NotNullValidator("车主姓名"))
                .on(bjtCarparkCar.getLicenceNumber(), new NotNullValidator("驾驶证号"))
                .on(bjtCarparkCar.getResource(), new NotNullValidator("车辆照片json数据"))
                .on(bjtCarparkCar.getPlateNumber(), new NotNullValidator("车牌号"))
                .on(bjtCarparkCar.getBrand(), new NotNullValidator("品牌"))
                .on(bjtCarparkCar.getModel(), new NotNullValidator("型号"))
                .on(bjtCarparkCar.getColor(), new NotNullValidator("颜色"))
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
            return new BaseResult(BaseResultEnum.ERROR, "当前用户不存在");
        }
        bjtCarparkCar.setUserId(getUserId());
        bjtCarparkCar.setUsername(userBaseResult.getData().getUsername());
        return carparkCarClient.insert(bjtCarparkCar);
    }
    @ApiOperation(value = "查询车辆信息列表")
    @RequestMapping(value = "select/CarparkCarList", method = RequestMethod.GET)
    public BaseResult<List<BjtCarparkCar>> selectCarparkCarList(){
        LOGGER.info("查询车辆信息列表");
        List<String> userIdList = new ArrayList<>();
        userIdList.add(getUserId());
        return carparkCarClient.selectCarparkCarList(userIdList);
    }
    @ApiOperation(value = "删除车辆信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "list", value = "车辆编号数组", required = true)
    })
    @RequestMapping(value = "delete/CarparkCarList", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult deleteCarparkCarList(@RequestBody List<String> list) {
        LOGGER.info("deleteCarparkCarList = >删除车辆信息");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(list.toString(), new NotNullValidator("车辆编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }

        return carparkCarClient.deleteCarparkCarList(getUserId(), list);
    }
}
