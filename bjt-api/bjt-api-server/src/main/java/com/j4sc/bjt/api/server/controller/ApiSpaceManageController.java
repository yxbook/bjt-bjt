package com.j4sc.bjt.api.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.*;
import com.j4sc.bjt.carpark.dao.entity.*;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/19 11:48
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/space")
@Api(tags = {"帮家团停车场管理服务"}, description = "帮家团停车场管理服务 - 授权")
public class ApiSpaceManageController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSpaceManageController.class);

    @Autowired
    private CarparkSpaceUserClient spaceUserClient;
    @Autowired
    private CarparkSpaceClient spaceClient;
    @Autowired
    private CarparkPaymentClient paymentClient;
    @Autowired
    private CarparkParkingLotClient parkingLotClient;
    @Autowired
    private CarparkApplyDetailClient applyDetailClient;
    @Autowired
    private CarparkApplyClient applyClient;

    @ApiOperation("修改停车场信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "spaceId", value = "停车场编号", required = true),
            @ApiImplicitParam(name = "name", value = "名称", required = true),
            @ApiImplicitParam(name = "address", value = "地址", required = true),
            @ApiImplicitParam(name = "longitude", value = "经度", required = true),
            @ApiImplicitParam(name = "latitude", value = "纬度", required = true),
            @ApiImplicitParam(name = "number", value = "车位数量", required = true),
            @ApiImplicitParam(name = "open", value = "是否对外开放类型：1、是；2、否", required = true),
            @ApiImplicitParam(name = "monthFee", value = "月卡单价", required = true),
            @ApiImplicitParam(name = "yearFee", value = "年卡单价", required = true),
            @ApiImplicitParam(name = "hourFee", value = "每小时费用", required = true),
            @ApiImplicitParam(name = "temporaryNumber", value = "临时停车位总数量", required = true),
            @ApiImplicitParam(name = "fixedNumber", value = "固定停车位总数量", required = true),
            @ApiImplicitParam(name = "fixed", value = "剩余固定停车位数量", required = true),
    })
    @RequestMapping(value = "carpark/update/CarparkSpace", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateCarparkSpace(@RequestBody BjtCarparkSpace carparkSpace) {
        LOGGER.info("修改停车场信息 = >updateCarparkSpace");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(carparkSpace.getSpaceId() + "", new NotNullValidator("停车场编号"))
                .on(carparkSpace.getName(), new NotNullValidator("停车场名称"))
                .on(carparkSpace.getAddress(), new NotNullValidator("地址"))
                .on(carparkSpace.getLongitude(), new NotNullValidator("经度"))
                .on(carparkSpace.getLatitude(), new NotNullValidator("纬度"))
                .on(carparkSpace.getNumber() + "", new NotNullValidator("车位数量"))
                .on(carparkSpace.getOpen() + "", new NotNullValidator("是否对外开放类型"))
                .on(carparkSpace.getMonthFee() + "", new NotNullValidator("月卡单价"))
                .on(carparkSpace.getYearFee() + "", new NotNullValidator("年卡单价"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult baseResult = validationSpaceUser(getUserId());
        if (baseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return baseResult;
        }
        return spaceClient.updateById(carparkSpace);
    }

    @ApiOperation(value = "停车场缴费记录查询")
    @RequestMapping(path = "select/PageCarparkPayment", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int")
    })
    public BaseResult<Page<BjtCarparkPayment>> selectPageCarparkPayment(@RequestParam Map<String, Object> params) {
        BaseResult baseResult = validationSpaceUser(getUserId());
        if (baseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return baseResult;
        }
        return paymentClient.selectPage(params);

    }

    @ApiOperation(value = "停车场车位查询")
    @RequestMapping(path = "select/PageParkingLot", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int")
    })
    public BaseResult<Page<BjtCarparkParkingLot>> selectPageParkingLot(@RequestParam Map<String, Object> params) {
        BaseResult<BjtCarparkSpaceUser> baseResult = validationSpaceUser(getUserId());
        if (baseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return new BaseResult(BaseResultEnum.ERROR, "查询失败");
        }
        params.put("space_id", baseResult.getData().getSpaceId());
        return parkingLotClient.selectPage(params);
    }

    @ApiOperation("修改停车位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lotId", value = "车位主键编号", required = true),
            @ApiImplicitParam(name = "number", value = "车位编号", required = true),
            @ApiImplicitParam(name = "type", value = "类型：1、固定车位；2、临时车位", required = true),
            @ApiImplicitParam(name = "status", value = "状态：1、空闲；2、使用中", required = true),
            @ApiImplicitParam(name = "spaceId", value = "停车场编号", required = true),
            @ApiImplicitParam(name = "spaceName", value = "停车场名称", required = true),
    })
    @RequestMapping(value = "update/ParkingLot", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateParkingLot(@RequestBody BjtCarparkParkingLot parkingLot) {
        LOGGER.info("修改停车位信息 = >updateParkingLot");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(parkingLot.getLotId().toString(), new NotNullValidator("车位主键编号"))
                .on(parkingLot.getNumber(), new NotNullValidator("车位编号"))
                .on(parkingLot.getType().toString(), new NotNullValidator("类型：1、固定车位；2、临时车位"))
                .on(parkingLot.getStatus().toString(), new NotNullValidator("状态：1、空闲；2、使用中"))
                .on(parkingLot.getSpaceId().toString(), new NotNullValidator("停车场编号"))
                .on(parkingLot.getSpaceName().toString(), new NotNullValidator("停车场名称"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtCarparkSpaceUser> baseResult = validationSpaceUser(getUserId());
        if (baseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return baseResult;
        }
        return parkingLotClient.updateById(parkingLot);
    }

    @ApiOperation("批量添加停车位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "number", value = "车位编号", required = true),
            @ApiImplicitParam(name = "type", value = "类型：1、固定车位；2、临时车位", required = true),
            @ApiImplicitParam(name = "status", value = "状态：1、空闲；2、使用中", required = true),
    })
    @RequestMapping(value = "add/ParkingLot", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addParkingLot(@RequestBody List<BjtCarparkParkingLot> list) {
        LOGGER.info("批量添加停车位信息 = >addParkingLot");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(list.toString(), new NotNullValidator("车位信息"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtCarparkSpaceUser> baseResult = validationSpaceUser(getUserId());
        if (baseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return baseResult;
        }
        list.forEach(v -> {
            v.setSpaceId(baseResult.getData().getSpaceId());
            v.setSpaceName(baseResult.getData().getSpaceName());
            v.setStatus(1);//状态：1、空闲；2、使用中
        });
        return parkingLotClient.addParkingLot(list);
    }

    @ApiOperation("批量删除停车位信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lotId", value = "车位主键编号数组", required = true),
    })
    @RequestMapping(value = "delete/ParkingLot", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult deleteParkingLot(@RequestBody List<Integer> list) {
        LOGGER.info("批量删除停车位信息 = >deleteParkingLot");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(list.toString(), new NotNullValidator("车位主键编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtCarparkSpaceUser> baseResult = validationSpaceUser(getUserId());
        if (baseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return baseResult;
        }
        return parkingLotClient.deleteParkingLot(list, baseResult.getData().getSpaceId());
    }

    @ApiOperation(value = "停车场停车权限查询")
    @RequestMapping(path = "select/PageApplyDetail", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
            @ApiImplicitParam(name = "status", value = "状态:1、待审批；2、（已通过）但未缴费；3、未通过；4、正常", dataType = "int"),
    })
    public BaseResult<Page<BjtCarparkApplyDetail>> selectPageApplyDetail(@RequestParam Map<String, Object> params) {
        LOGGER.info("停车场停车权限查询 = >selectPageApplyDetail");
        BaseResult<BjtCarparkSpaceUser> baseResult = validationSpaceUser(getUserId());
        if (baseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return new BaseResult(BaseResultEnum.ERROR, "获取失败");
        }
        params.put("space_id", baseResult.getData().getSpaceId());
        return applyDetailClient.selectPage(params);
    }

    @ApiOperation(value = "停车场停车记录查询")
    @RequestMapping(path = "select/ParkingRecord", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "endTime", value = "结束时间"),
            @ApiImplicitParam(name = "beginTime", value = "开始时间"),
            @ApiImplicitParam(name = "timeType", value = "0 为查询入场时间,1 为查询出场时间", dataType = "int"),
            @ApiImplicitParam(name = "carNum", value = "车牌号")
    })
    public BaseResult selectParkingRecord(@RequestParam Map<String, Object> params) throws UnsupportedEncodingException {
        LOGGER.info("停车场停车记录查询 = >selectParkingRecord");
        BaseResult<BjtCarparkSpaceUser> baseResult = validationSpaceUser(getUserId());
        if (baseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return baseResult;
        }
        BaseResult<BjtCarparkSpace> spaceBaseResult = spaceClient.selectById(baseResult.getData().getSpaceId().toString());
        if (spaceBaseResult.status != BaseResultEnum.SUCCESS.getStatus() || spaceBaseResult.getData() == null) {
            return spaceBaseResult;
        }
        if ("".equals(spaceBaseResult.getData().getParkId())) {
            return new BaseResult(BaseResultEnum.ERROR, "停车场编号parkId为空");
        }
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://www.dongluhitec.net/api/pullParkingRecordData.action";
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        postParameters.add("parkId", spaceBaseResult.getData().getParkId());
        if (params.get("carNum") != null) {
            postParameters.add("carNum", params.get("carNum"));
        }
        if (params.get("timeType") != null) {
            postParameters.add("timeType", params.get("timeType"));
        }
        if (params.get("beginTime") != null) {
            postParameters.add("beginTime", params.get("beginTime"));
        }
        if (params.get("endTime") != null) {
            postParameters.add("endTime", params.get("endTime"));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(postParameters, headers);
        String data = restTemplate.postForObject(url, r, String.class);
        Map<String, String> map = (Map) JSONObject.parseObject(data);
        if (map.get("result") != null && "success".equals(map.get("result"))) {
            return new BaseResult(BaseResultEnum.SUCCESS, URLDecoder.decode(map.get("data"), "utf-8"));
        }
        return new BaseResult(BaseResultEnum.ERROR, "获取失败");
    }

    @ApiOperation(value = "删除停车场权限")
    @RequestMapping(path = "delete/ApplyDetail", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "detailId", value = "停车权限编号数组", required = true)
    })
    public BaseResult deleteApplyDetail(@RequestBody List<String> list) {
        LOGGER.info("删除停车场权限 = >deleteApplyDetail");
        BaseResult<BjtCarparkSpaceUser> baseResult = validationSpaceUser(getUserId());
        if (baseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return baseResult;
        }
        return applyDetailClient.deleteDetailList(baseResult.getData().getSpaceId().toString(), list);
    }

    @ApiOperation(value = "查询当前用户是否是停车场管理员")
    @RequestMapping(path = "select/SpaceUser", method = RequestMethod.GET)
    public BaseResult<BjtCarparkSpaceUser> selectSpaceUser() {
        LOGGER.info("查询当前用户是否是停车场管理员");
        return spaceUserClient.selectSpaceUser(getUserId());
    }

    @ApiOperation(value = "查询停车申请记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
    })
    @RequestMapping(value = "select/CarparkApplyPage", method = RequestMethod.GET)
    public BaseResult<Page<BjtCarparkApply>> selectCarparkApplyPage(@RequestParam Map<String, Object> params) {
        LOGGER.info("查询停车申请记录列表");
        BaseResult<BjtCarparkSpaceUser> baseResult = validationSpaceUser(getUserId());
        if (baseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return new BaseResult(BaseResultEnum.ERROR, "获取失败");
        }
        params.put("space_id", baseResult.getData().getSpaceId());
        return applyClient.selectPage(params);
    }

    @ApiOperation(value = "查询停车场基本信息")
    @RequestMapping(path = "select/Space", method = RequestMethod.GET)
    public BaseResult<BjtCarparkSpace> selectSpace() {
        LOGGER.info("查询停车场基本信息");
        BaseResult<BjtCarparkSpaceUser> spaceUserBaseResult = spaceUserClient.selectSpaceUser(getUserId());
        if (spaceUserBaseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return new BaseResult(BaseResultEnum.ERROR, "停车场信息不存在");
        }
        return spaceClient.selectById(spaceUserBaseResult.getData().getSpaceId().toString());
    }

    @ApiOperation(value = "查询停车场缴费统计")
    @RequestMapping(path = "select/CountPayment", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> selectCountPayment() {
        LOGGER.info("查询停车场基本信息");
        BaseResult<BjtCarparkSpaceUser> spaceUserBaseResult = spaceUserClient.selectSpaceUser(getUserId());
        if (spaceUserBaseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return new BaseResult(BaseResultEnum.ERROR, "停车场信息不存在");
        }
        return paymentClient.selectCountPayment(spaceUserBaseResult.getData().getSpaceId().toString());
    }


    /**
     * 校验当前用户是否是停车场管理员
     *
     * @param userId
     * @return
     */
    public BaseResult<BjtCarparkSpaceUser> validationSpaceUser(String userId) {
        BaseResult<BjtCarparkSpaceUser> spaceUserBaseResult = spaceUserClient.selectSpaceUser(userId);
        if (spaceUserBaseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return spaceUserBaseResult;
        }
        if (spaceUserBaseResult.getData() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "无操作权限");
        } else {
            return spaceUserBaseResult;
        }
    }

}
