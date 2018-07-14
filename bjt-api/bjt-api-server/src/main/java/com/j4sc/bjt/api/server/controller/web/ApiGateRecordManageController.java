package com.j4sc.bjt.api.server.controller.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.ParkBuildGateRecordClient;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGateRecord;
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

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/17 17:53
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "admin/gateRecord")
@Api(tags = {"帮家团闸机服务"}, description = "帮家团闸机服务 - web授权")
public class ApiGateRecordManageController extends BaseJwtController{
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiVisitorManageController.class);

    @Autowired
    private ParkBuildGateRecordClient parkBuildGateRecordClient;

    @ApiOperation(value = "闸机记录查询")
    @RequestMapping(path = "select/PageGateRecord", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int")
    })
    public BaseResult<Page<BjtParkBuildGateRecord>> selectPageGateRecord(@RequestParam Map<String, Object> params){
        LOGGER.info("获取闸机记录");
        params.put("build_id", getBuildId());
        return parkBuildGateRecordClient.selectPage(params);
    }
}
