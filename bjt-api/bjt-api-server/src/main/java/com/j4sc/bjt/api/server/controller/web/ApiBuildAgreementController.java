package com.j4sc.bjt.api.server.controller.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.ParkBuildAgreementClient;
import com.j4sc.bjt.api.server.client.ParkBuildAgreementRecordClient;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildAgreement;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildAgreementRecord;
import com.j4sc.common.base.BaseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/4 18:16
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "admin/agreement")
@Api(tags = {"帮家团合同管理服务"}, description = "帮家团合同管理服务 - web授权")
public class ApiBuildAgreementController extends BaseJwtController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiBuildAgreementController.class);

    @Autowired
    private ParkBuildAgreementClient parkBuildAgreementClient;
    @Autowired
    private ParkBuildAgreementRecordClient parkBuildAgreementRecordClient;
    @ApiOperation(value = "查看正在生效的合同列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
    })
    @RequestMapping(value = "select/BuildAgreementPage", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkBuildAgreement>> selectBuildAgreementPage(@RequestParam Map<String, Object> params) {
        LOGGER.info("查看合同记录");
        params.put("build_id", getBuildId());
        return parkBuildAgreementClient.selectPage(params);
    }

    @ApiOperation(value = "查看合同历史记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
    })
    @RequestMapping(value = "select/BuildAgreementRecordPage", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkBuildAgreementRecord>> selectBuildAgreementRecordPage(@RequestParam Map<String, Object> params) {
        LOGGER.info("查看合同历史记录");
        params.put("build_id", getBuildId());
        return parkBuildAgreementRecordClient.selectPage(params);
    }
}
