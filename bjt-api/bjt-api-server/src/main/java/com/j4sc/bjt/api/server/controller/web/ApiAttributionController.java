package com.j4sc.bjt.api.server.controller.web;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.ParkAttributionClient;
import com.j4sc.bjt.api.server.client.ParkCompanyClient;
import com.j4sc.bjt.park.dao.entity.BjtParkAttribution;
import com.j4sc.bjt.park.dao.entity.BjtParkCompany;
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

import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018 2018/4/9 12:11
 * @Version: 1.0
 **/
@Deprecated
@RestController
@RequestMapping(value = "admin/company")
@Api(tags = {"帮家团产权方服务"}, description = "帮家团产权方服务 - WEB授权")
public class ApiAttributionController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiAttributionController.class);

    @Autowired
    ParkAttributionClient parkAttributionClient;

    /*
    * 产权方API
    *
    * */
    @ApiOperation(value = "新建产权方", notes = "新建产权方")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "add/Attribution", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addCompany(@RequestBody BjtParkAttribution bjtParkAttribution) {
        LOGGER.info(" 新建产权方...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(String.valueOf(bjtParkAttribution.getAttributionId()), new NotNullValidator("产权方编号"))
                .on(bjtParkAttribution.getAttribution(), new NotNullValidator("产权方"))
                .on(bjtParkAttribution.getAddress(), new NotNullValidator("地址"))
                .on(bjtParkAttribution.getContact(), new NotNullValidator("联系电话"))
                .on(String.valueOf(bjtParkAttribution.getBuildId()), new NotNullValidator("楼宇编号"))
                .on(bjtParkAttribution.getName(), new NotNullValidator("楼宇名称"))
                .on(String.valueOf(bjtParkAttribution.getType()), new NotNullValidator("产权方类型"))
                .on(bjtParkAttribution.getProperty(), new NotNullValidator("物业方"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        bjtParkAttribution.setUtime(new Date().getTime());
        return parkAttributionClient.insert(bjtParkAttribution);
    }

    @ApiOperation(value = "修改产权方", notes = "修改产权方")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "update/Attribution", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateAttribution(BjtParkAttribution bjtParkAttribution) {
        LOGGER.info(" 修改产权方...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(String.valueOf(bjtParkAttribution.getAttributionId()), new NotNullValidator("产权方编号"))
                .on(bjtParkAttribution.getAttribution(), new NotNullValidator("产权方"))
                .on(bjtParkAttribution.getAddress(), new NotNullValidator("地址"))
                .on(bjtParkAttribution.getContact(), new NotNullValidator("联系电话"))
                .on(String.valueOf(bjtParkAttribution.getBuildId()), new NotNullValidator("楼宇编号"))
                .on(bjtParkAttribution.getName(), new NotNullValidator("楼宇名称"))
                .on(String.valueOf(bjtParkAttribution.getType()), new NotNullValidator("产权方类型"))
                .on(bjtParkAttribution.getProperty(), new NotNullValidator("物业方"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        bjtParkAttribution.setUtime(System.currentTimeMillis());
        BaseResult baseResult = parkAttributionClient.updateById(bjtParkAttribution);
        return baseResult;
    }

    @ApiOperation(value = "删除产权方", notes = "删除产权方")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "delete/Attribution", method = RequestMethod.DELETE)
    public BaseResult deleteAttribution(@RequestParam("attributionId") int attributionId) {
        LOGGER.info(" 删除产权方 ");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(String.valueOf(attributionId), new NotNullValidator("产权方编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return parkAttributionClient.deleteById(String.valueOf(attributionId));
    }

    @ApiOperation(value = "根据ID查询产权方", notes = "根据ID查询产权方")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "select/Attribution", method = RequestMethod.GET)
    public BaseResult selectAttribution(@RequestParam("attributionId") int attributionId) {
        LOGGER.info(" 根据ID查询产权方...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(String.valueOf(attributionId), new NotNullValidator("产权方编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return parkAttributionClient.selectById(String.valueOf(attributionId));
    }

    @ApiOperation(value = "分页查询产权方", notes = "分页查询产权方")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
    })
    @RequestMapping(value = "select/PageAttribution", method = RequestMethod.GET)
    public BaseResult selectPageAttribution(@RequestParam Map<String, Object> params) {
        LOGGER.info(" 分页查询产权方...");
        Map<String, Object> map = new HashMap<>();
        if (!"".equals(params.get("attribution"))) {
            map.put("attribution", params.get("attribution"));//产权方名称
        }
        if (!"".equals(params.get("name"))) {
            map.put("name", params.get("name"));//楼宇名称
        }
        map.put("build_id", getBuildId());//楼宇编号
        return parkAttributionClient.selectPage(params);
    }

}
