package com.j4sc.bjt.api.server.controller.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.ParkBuildVisitorClient;
import com.j4sc.bjt.api.server.client.UserManageClient;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildVisitor;
import com.j4sc.bjt.user.dao.entity.BjtUserUser;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/4 17:07
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "admin/visitor")
@Api(tags = {"帮家团访客申请管理服务"}, description = "帮家团访客申请服务 - web授权")
public class ApiVisitorManageController extends BaseJwtController{
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiVisitorManageController.class);

    @Autowired
    private ParkBuildVisitorClient parkBuildVisitorClient;

    @ApiOperation(value = "处理访客申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "visitorId", value = "访客记录编号", required = true),
            @ApiImplicitParam(name = "type", value = "type为2表示同意、为3表示拒绝", required = true),
    })
    @RequestMapping(value = "update/BuildVisitor", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateVisitor(@RequestBody Map<String, Object> params) {
        LOGGER.info("处理访客申请");
        params.put("userId", getUserId());
        params.put("buildId", getBuildId());
        return parkBuildVisitorClient.updateBuildVisitor(params);
    }
    @ApiOperation(value = "获取访客记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int")
    })
    @RequestMapping(value = "select/BuildVisitorPage", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkBuildVisitor>> selectBuildVisitorPage(@RequestParam Map<String, Object> params) {
        LOGGER.info("获取访客记录");
        params.put("build_id", getBuildId());
        params.put("orderBy", "ctime");
        return parkBuildVisitorClient.selectPage(params);
    }
}
