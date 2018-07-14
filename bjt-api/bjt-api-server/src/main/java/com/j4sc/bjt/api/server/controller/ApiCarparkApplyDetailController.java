package com.j4sc.bjt.api.server.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.CarparkApplyDetailClient;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkApplyDetail;
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

import java.util.List;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/12 18:10
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/carparkApplyDetail")
@Api(tags = {"帮家团停车申请权限服务"}, description = "帮家团停车申请权限服务 - 需授权")
public class ApiCarparkApplyDetailController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCarparkApplyDetailController.class);

    @Autowired
    private CarparkApplyDetailClient carparkApplyDetailClient;

    @ApiOperation("获取停车申请权限列表")
    @RequestMapping(value = "select/ApplyDetailList", method = RequestMethod.GET)
    public BaseResult<List<BjtCarparkApplyDetail>> selectApplyDetailList(){
        LOGGER.info("获取停车申请权限列表");
        return carparkApplyDetailClient.selectApplyDetailList(getUserId());
    }

    @ApiOperation(value = "删除停车权限列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "list", value = "停车权限编号数组", required = true)
    })
    @RequestMapping(value = "delete/CarparkDetailList", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult deleteCarparkDetailList(@RequestBody List<String> list) {
        LOGGER.info("deleteCarparkCarList = >删除停车权限列表");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(list.toString(), new NotNullValidator("停车权限编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }

        return carparkApplyDetailClient.deleteCarparkDetailList(getUserId(), list);
    }

}
