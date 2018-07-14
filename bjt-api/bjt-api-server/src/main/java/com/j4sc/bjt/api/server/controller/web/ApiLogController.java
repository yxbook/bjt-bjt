package com.j4sc.bjt.api.server.controller.web;

import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.SystemLogClient;
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
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/3 11:29
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "admin/log")
@Api(tags = {"帮家团楼宇日志服务"}, description = "帮家团楼宇日志服务 - WEB授权")
public class ApiLogController extends BaseJwtController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiLogController.class);
    @Autowired
    SystemLogClient systemLogClient;

    @ApiOperation(value = "查询楼宇日志",notes = "查询楼宇日志")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", required = false, dataType = "int"),
    })
    @RequestMapping(value = "select/PageLog", method = RequestMethod.GET)
    public BaseResult selectPageLog(@RequestParam Map<String, Object> params){
        LOGGER.info(" 查询楼宇日志...");
        params.put("build_id",getBuildId());
        params.put("orderBy","log_id");
        BaseResult baseResult = systemLogClient.selectPage(params);
        return baseResult;
    }


}
