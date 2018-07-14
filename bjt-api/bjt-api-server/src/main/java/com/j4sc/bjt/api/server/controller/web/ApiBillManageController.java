package com.j4sc.bjt.api.server.controller.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.UserBillClient;
import com.j4sc.bjt.user.dao.entity.BjtUserBill;
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
 * @CreateDate: 2018/4/18 17:57
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "admin/userBill")
@Api(tags = {"帮家团楼宇缴费查询服务"}, description = "帮家团楼宇缴费查询服务 - WEB授权")
public class ApiBillManageController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiBillManageController.class);

    @Autowired
    private UserBillClient userBillClient;

    @ApiOperation(value = "缴费查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
    })
    @RequestMapping(path = "select/PageUserBill", method = RequestMethod.GET)
    public BaseResult<Page<BjtUserBill>> selectPageUserBill(@RequestParam Map<String, Object> params){
        LOGGER.info("缴费查询");
        params.put("build_id", getBuildId());
        return userBillClient.selectPage(params);
    }
}
