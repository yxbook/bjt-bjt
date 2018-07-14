package com.j4sc.bjt.api.server.controller.web;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.ParkBuildHouseClient;
import com.j4sc.bjt.api.server.client.ParkCompanyAdmissionClient;
import com.j4sc.bjt.api.server.client.ParkCompanyClient;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildHouse;
import com.j4sc.bjt.park.dao.entity.BjtParkCompany;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyAdmission;
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

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018 2018/4/18 10:49
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "admin/admission")
@Api(tags = {"帮家团楼宇公司入驻服务"}, description = "帮家团楼宇公司入驻服务 - WEB授权")
public class ApiCompanyAdmissionController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCompanyAdmissionController.class);

    @Autowired
    ParkCompanyClient parkCompanyClient;
    @Autowired
    ParkCompanyAdmissionClient parkCompanyAdmissionClient;
    @Autowired
    ParkBuildHouseClient parkBuildHouseClient;

    @ApiOperation(value = "公司入驻申请审批",notes = "公司入驻申请审批")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "公司编号", name = "companyId", required = true),
            @ApiImplicitParam(value = "状态（0表示拒绝、1表示通过且只有物业合同、2表示通过且有物业合同和租赁合同）", name = "state", required =true),
            @ApiImplicitParam(value = "物业合同", name = "wyAgreement"),
            @ApiImplicitParam(value = "租赁合同", name = "zlAgreement"),
    })
    @RequestMapping(value = "update/CompanyAdmission", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateCompanyAdmission(@RequestBody Map<String, Object> params){
        LOGGER.info(" 公司入驻申请审批...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(params.get("companyId")+"", new NotNullValidator("公司编号"))
                .on(params.get("wyAgreement")+"", new NotNullValidator("物业合同"))
                .on(params.get("state")+"", new NotNullValidator("状态"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtParkCompany> companyBaseResult = parkCompanyClient.selectById(params.get("companyId")+"");
        if (companyBaseResult.status != BaseResultEnum.SUCCESS.getStatus()){
            return companyBaseResult;
        }
        if (companyBaseResult.getData() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "公司信息为空");
        }
        if (companyBaseResult.getData().getBuildId() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "公司申请入驻楼宇信息为空");
        }
        if (companyBaseResult.getData().getBuildId() != getBuildId()) return new BaseResult(BaseResultEnum.ERROR, "非法操作");
        params.put("approvalUserId", getUserId());
        params.put("buildId", getBuildId());
        return parkCompanyAdmissionClient.updateCompanyAdmission(params);
    }

    @ApiOperation(value = "根据ID查询公司信息",notes = "根据ID查询公司信息")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "select/Company", method = RequestMethod.GET)
    public BaseResult selectCompany(@RequestParam("companyId") String companyId){
        LOGGER.info(" 查询公司信息...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(companyId, new NotNullValidator("公司编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return parkCompanyClient.selectById(companyId);
    }

    @ApiOperation(value = "查询公司信息列表",notes = "查询公司信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
    })
    @RequestMapping(value = "select/PageCompany", method = RequestMethod.GET)
    public BaseResult selectPageCompany(@RequestParam Map<String, Object> params){
        LOGGER.info(" PAGE查询公司信息列表...");
        params.put("buildId",getBuildId());
        params.put("userId", getUserId());
        return parkCompanyClient.selectPageCompany(params);
    }

    @ApiOperation(value = "查询公司申请审批列表",notes = "查询公司申请审批列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
    })
    @RequestMapping(value = "select/PageCompanyAdmission", method = RequestMethod.GET)
    public BaseResult selectPageCompanyAdmission(@RequestParam Map<String, Object> params){
        LOGGER.info(" PAGE查询公司申请审批列表...");
        params.put("build_id",getBuildId());
        return parkCompanyAdmissionClient.selectPage(params);
    }

    @ApiOperation(value = "查询所有房屋列表",notes = "查询所有房屋列表")
    @RequestMapping(value = "select/BuildHouseList", method = RequestMethod.GET)
    public BaseResult<List<BjtParkBuildHouse>> selectBuildHouseList(@RequestParam Map<String, Object> params){
        LOGGER.info("selectBuildHouseList...");
        params.put("buildId",getBuildId());
        return parkBuildHouseClient.selectBuildHouseList(params);
    }
    @ApiOperation("删除公司入驻信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "companyId", value = "公司编号", required = true)
    })
    @RequestMapping(value = "delete/CompanyAdmission", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult deleteAdmission(@RequestBody Map<String, Object> params){
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(params.get("companyId").toString(), new NotNullValidator("公司编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return parkCompanyAdmissionClient.deleteAdmission(params.get("companyId").toString(), String.valueOf(getBuildId()));
    }

}
