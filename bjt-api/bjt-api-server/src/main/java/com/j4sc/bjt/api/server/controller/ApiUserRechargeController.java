package com.j4sc.bjt.api.server.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.UserRechargeClient;
import com.j4sc.bjt.user.dao.entity.BjtUserRecharge;
import com.j4sc.common.base.BaseResult;
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
import java.util.Map;

/**
 * @Description: 用户充值服务
 * @Author: chengyz
 * @CreateDate: 2018/4/10 15:45
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/recharge")
@Api(tags = {"帮家团用户充值服务"}, description = "帮家团用户充值服务 - 需授权")
public class ApiUserRechargeController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUserController.class);

    @Autowired
    private UserRechargeClient userRechargeClient;

    @ApiOperation(value = "用户充值", notes = "用户充值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "支付方式", dataType = "int"),
    })
    @RequestMapping(value = "save/Recharge", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveRecharge(@RequestBody BjtUserRecharge bjtUserRecharge) {
        LOGGER.info("recharge = >用户充值");
        LOGGER.info("recharge = >调用支付。。。未实现" + getUserId());

        return userRechargeClient.saveRecharge(bjtUserRecharge);
    }

    @ApiOperation(value = "查看用户充值记录", notes = "查看用户充值记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页数", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页显示记录数", dataType = "int"),
    })
    @RequestMapping(value = "select/PageRecharge", method = RequestMethod.GET)
    public BaseResult<Page<BjtUserRecharge>> selectPageRecharge(@RequestParam Map<String, Object> params) {
        LOGGER.info("selectPageRecharge = >查看用户充值记录" + getUserId());
        params.put("user_id", getUserId());
        return userRechargeClient.selectPage(params);
    }
}
