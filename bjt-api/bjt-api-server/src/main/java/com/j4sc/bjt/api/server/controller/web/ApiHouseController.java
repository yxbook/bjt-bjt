package com.j4sc.bjt.api.server.controller.web;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.*;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkApplyDetail;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkCar;
import com.j4sc.bjt.park.dao.entity.BjtParkBuild;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildHouse;
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
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018 2018/4/16 9:11
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "admin/house")
@Api(tags = {"帮家团房屋服务"}, description = "帮家团房屋服务 - WEB授权")
public class ApiHouseController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiHouseController.class);

    @Autowired
    ParkBuildHouseClient parkBuildHouseClient;
    @Autowired
    ParkBuildClient parkBuildClient;
    @Autowired
    ParkCompanyClient parkCompanyClient;
    @Autowired
    ParkBuildGuardClient parkBuildGuardClient;
    @Autowired
    ParkCompanyAdmissionClient parkCompanyAdmissionClient;
    @Autowired
    CarparkCarClient carparkCarClient;

    @ApiOperation(value = "新建房屋信息", notes = "新建房屋信息")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "add/House", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addHouse(@RequestBody List<BjtParkBuildHouse> list) {
        LOGGER.info(" 新建房屋信息...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(list.toString(), new NotNullValidator("房屋信息"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtParkBuild> buildBaseResult = parkBuildClient.selectById(String.valueOf(getBuildId()));
        if (buildBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) return buildBaseResult;
        if (buildBaseResult.getData() == null || (buildBaseResult.getData()).getBuildId() != getBuildId())
            return new BaseResult(BaseResultEnum.ERROR, "非法操作");
        list.forEach(v -> {
            v.setBuildId(getBuildId());
            v.setBuildName(buildBaseResult.getData().getName());
            v.setCtime(System.currentTimeMillis());
        });
        return parkBuildHouseClient.addBuildHouse(list);
    }

    @ApiOperation(value = "修改房屋", notes = "修改房屋")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "update/House", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateHouse(@RequestBody BjtParkBuildHouse bjtParkBuildHouse) {
        LOGGER.info(" 修改房屋...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(String.valueOf(bjtParkBuildHouse.getHouseId()), new NotNullValidator("房屋编号"))
                .on(bjtParkBuildHouse.getAttribution(), new NotNullValidator("产权方"))
                .on(String.valueOf(bjtParkBuildHouse.getBuildId()), new NotNullValidator("楼宇编号"))
                .on(String.valueOf(bjtParkBuildHouse.getArea()), new NotNullValidator("面积"))
                .on(String.valueOf(bjtParkBuildHouse.getFloor()), new NotNullValidator("所在楼层"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        Map<String, Object> map = new HashMap();
        map.put("house_id", bjtParkBuildHouse.getHouseId());
        BaseResult<List<BjtParkBuildHouse>> listResult = parkBuildHouseClient.selectAll(map);
        if (listResult.getStatus() != BaseResultEnum.SUCCESS.getStatus() || listResult.getData().size() == 0)
            return listResult;
        if (listResult.getData().get(0).getBuildId() != getBuildId())
            return new BaseResult(BaseResultEnum.ERROR, "非法操作");
        bjtParkBuildHouse.setBuildId(getBuildId());
        return parkBuildHouseClient.updateById(bjtParkBuildHouse);
    }

    @ApiOperation(value = "删除房屋", notes = "删除房屋")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "delete/House", method = RequestMethod.DELETE)
    public BaseResult deleteHouse(@RequestParam("houseId") String houseId) {
        LOGGER.info(" 删除房屋 ");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(String.valueOf(houseId), new NotNullValidator("房屋编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult houseResult = parkBuildHouseClient.selectById(String.valueOf(houseId));
        if (houseResult.getData() != null && ((BjtParkBuildHouse) houseResult.getData()).getBuildId() == getBuildId()) {
            if (((BjtParkBuildHouse) houseResult.getData()).getCompanyId() != null) {
                parkBuildGuardClient.deleteGuardList(((BjtParkBuildHouse) houseResult.getData()).getCompanyId().toString());//删除房屋中公司员工的门禁权限
                parkCompanyAdmissionClient.deleteById(((BjtParkBuildHouse) houseResult.getData()).getCompanyId().toString());//删除入驻申请记录
            }
            return parkBuildHouseClient.deleteById(houseId);
        }
        return new BaseResult(BaseResultEnum.ERROR, "非法操作");
    }

    @ApiOperation(value = "根据ID查询房屋", notes = "根据ID查询房屋")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "select/House", method = RequestMethod.GET)
    public BaseResult selectHouse(@RequestParam("houseId") String houseId) {
        LOGGER.info(" 查询房屋...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(houseId, new NotNullValidator("房屋编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return parkBuildHouseClient.selectById(houseId);
    }

    @ApiOperation(value = "分页查询房屋", notes = "分页查询房屋")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
    })
    @RequestMapping(value = "select/PageHouse", method = RequestMethod.GET)
    public BaseResult selectPageHouse(@RequestParam Map<String, Object> params) {
        LOGGER.info(" 分页查询房屋...");
        params.put("build_id", getBuildId());
        return parkBuildHouseClient.selectPage(params);
    }

    @ApiOperation(value = "删除公司信息", notes = "删除公司信息")
    @RequestMapping(value = "delete/Company", method = RequestMethod.DELETE)
    public BaseResult deleteCompany(@RequestParam("houseId") String houseId) {
        LOGGER.info(" 删除公司信息 ");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(houseId, new NotNullValidator("房屋编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtParkBuildHouse> houseResult = parkBuildHouseClient.selectById(houseId);
        if (houseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus())
            return new BaseResult(BaseResultEnum.ERROR, "当前房屋信息不存在");
        if (houseResult.getData() != null && (houseResult.getData()).getBuildId() == getBuildId()) {
            parkBuildGuardClient.deleteGuardList(houseResult.getData().getCompanyId() + "");//删除门禁权限
            BjtParkBuildHouse _house = houseResult.getData();
            _house.setBuildId(getBuildId());
            _house.setCompany("");
            _house.setCompanyId(null);
            _house.setLeaseAgreementId(null);
            _house.setLeaseAgreementName("");
            if (parkBuildHouseClient.updateById(_house).getStatus() != BaseResultEnum.SUCCESS.getStatus())
                return new BaseResult(BaseResultEnum.ERROR, "删除公司信息失败");
        }
        return new BaseResult(BaseResultEnum.ERROR, "非法操作");
    }

    @ApiOperation("获取房屋对应的公司详细信息")
    @RequestMapping(value = "select/CompanyDetailInfo", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> selectCompanyDetailInfo(@RequestParam("houseId") int houseId) {
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(houseId+"", new NotNullValidator("房屋编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<Map<String, Object>> baseResult = parkCompanyClient.selectCompanyDetailInfo(houseId, getUserId(), getBuildId());
        if (baseResult.getData().get("userIdList") != null) {
            List<String> userIdList = (List<String>) baseResult.getData().get("userIdList");
            BaseResult<List<BjtCarparkCar>> carListBaseResult = carparkCarClient.selectCarparkCarList(userIdList);
            baseResult.getData().remove("userIdList");
            baseResult.getData().put("carList", carListBaseResult.getData());
        }
        return baseResult;
    }

}
