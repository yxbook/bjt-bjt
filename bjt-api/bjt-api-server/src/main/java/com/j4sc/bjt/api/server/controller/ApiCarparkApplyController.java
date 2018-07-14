package com.j4sc.bjt.api.server.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.*;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkApply;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkCar;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkParkingLot;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyUser;
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
 * @CreateDate: 2018/5/8 18:21
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/carparkApply")
@Api(tags = {"帮家团停车申请服务"}, description = "帮家团停车申请服务 - 需授权")
public class ApiCarparkApplyController extends BaseJwtController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCarparkApplyController.class);

    @Autowired
    private ParkCompanyClient parkCompanyClient;
    @Autowired
    private CarparkApplyClient carparkApplyClient;
    @Autowired
    private CarparkSpaceClient carparkSpaceClient;
    @Autowired
    private ParkCompanyUserClient parkCompanyUserClient;
    @Autowired
    private CarparkCarClient carparkCarClient;
    @Autowired
    private CarparkParkingLotClient carparkParkingLotClient;

    @ApiOperation(value = "公司管理员提交停车申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "spaceName", value = "停车场名称", required = true),
            @ApiImplicitParam(name = "spaceId", value = "停车场编号", required = true),
            @ApiImplicitParam(name = "detailList", value = "(carName:车主姓名; plateNumber:车牌号; userId:用户编号; type:类型：1、固定,2、临时; carId:车辆信息编号)", required = true)
    })
    @RequestMapping(value = "save/CarparkApply", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCarparkApply(@RequestBody Map<String, Object> params) {
        LOGGER.info("公司管理员提交停车申请 = >saveCarparkApply");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(params.get("spaceName").toString(), new NotNullValidator("停车场名称"))
                .on(params.get("spaceId").toString(), new NotNullValidator("停车场编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        //查询公司及管理员的相关信息
        BaseResult<Map<String, Object>> companyBaseResult = parkCompanyClient.selectCompany(getUserId());
        if (companyBaseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return companyBaseResult;
        }
        if (companyBaseResult.getData() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "公司信息不存在");
        }
        params.putAll(companyBaseResult.getData());
        return carparkApplyClient.saveCarparkApply(params);
    }

    @ApiOperation(value = "停车场管理员审批停车申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyId", value = "申请编号", required = true),
            @ApiImplicitParam(name = "type", value = "操作类型1表示同意，2表示拒绝", required = true),
            @ApiImplicitParam(name = "detailList", value = "(userId:用户编号; type:类型：1、固定,2、临时; lotNumber:车位编号;detailId:明细编号;terminalTime:终止时间)")
    })
    @RequestMapping(value = "update/CarparkApply", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateCarparkApply(@RequestBody Map<String, Object> params) {
        LOGGER.info("停车场管理员审批停车申请 = >saveCarparkApply");
        params.put("spaceUserId", getUserId());
        return carparkApplyClient.updateCarparkApply(params);
    }

    @ApiOperation(value = "获取停车场列表(停车申请时调用)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
            @ApiImplicitParam(name = "key", value = "关键词")
    })
    @RequestMapping(value = "select/CarparkSpacePage", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> selectCarparkSpacePage(@RequestParam Map<String, Object> params) {
        BaseResult baseResult = parkCompanyClient.selectCompanyBuildSpaceId(getUserId());
        if (baseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return baseResult;
        }
        params.put("spaceId", baseResult.getData());
        return carparkSpaceClient.selectCarparkSpacePage(params);
    }

    @ApiOperation(value = "查询员工车辆信息列表")
    @RequestMapping(value = "select/CarparkCarList", method = RequestMethod.GET)
    public BaseResult<List<BjtCarparkCar>> selectCarparkCarList() {
        BaseResult<List<BjtParkCompanyUser>> companyUserListResult = parkCompanyUserClient.selectCompanyUserList(getUserId());
        if (companyUserListResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return new BaseResult(BaseResultEnum.ERROR, companyUserListResult.getMessage());
        }
        List<BjtParkCompanyUser> companyUserList = companyUserListResult.getData();
        if (companyUserList == null || companyUserList.size() < 1) {
            return new BaseResult(BaseResultEnum.ERROR, "当前公司无员工");
        }
        List<String> userIdList = new ArrayList<>();
        userIdList.add("-1");
        companyUserList.forEach(v -> {
            userIdList.add(v.getUserId());
        });
        return carparkCarClient.selectCarparkCarList(userIdList);
    }

    @ApiOperation(value = "查询停车申请记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
            @ApiImplicitParam(name = "spaceId", value = "停车场编号（若当前用户是停车场管理员则需要传，不传则查询本人所申请的）")
    })
    @RequestMapping(value = "select/CarparkApplyPage", method = RequestMethod.GET)
    public BaseResult<Page<BjtCarparkApply>> selectCarparkApplyPage(@RequestParam Map<String, Object> params) {
        LOGGER.info("查询停车申请记录列表");
        if (params.get("spaceId") == null) {
            params.put("spaceId", -1);
        }
        params.put("userId", getUserId());
        return carparkApplyClient.selectCarparkApplyPage(params);
    }

    @ApiOperation(value = "查询停车场车位列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "spaceId", value = "停车场编号", required = true)
    })
    @RequestMapping(value = "select/ParkingLotList", method = RequestMethod.GET)
    public BaseResult<List<BjtCarparkParkingLot>> selectParkingLot(@RequestParam("spaceId") int spaceId) {
        LOGGER.info("查询停车场车位列表");
        Map<String, Object> params = new HashMap<>();
        params.put("space_id", spaceId);
        params.put("status", 1);//状态：1、空闲；2、使用中
        params.put("type", 1);//类型：1、固定车位；2、临时车位
        return carparkParkingLotClient.selectAll(params);
    }

}
