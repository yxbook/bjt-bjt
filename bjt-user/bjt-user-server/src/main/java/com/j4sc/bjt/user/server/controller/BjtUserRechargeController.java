package com.j4sc.bjt.user.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.user.dao.entity.BjtUserBill;
import com.j4sc.bjt.user.dao.entity.BjtUserRecharge;
import com.j4sc.bjt.user.rest.api.BjtUserBillService;
import com.j4sc.bjt.user.rest.api.BjtUserRechargeService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/10 14:21
 * @Version: 1.0
 **/
@RestController
@ApiModel
@RequestMapping("BjtUserRecharge")
@Api(tags = {"充值服务"}, description = "充值服务")
public class BjtUserRechargeController extends BaseController<BjtUserRecharge, BjtUserRechargeService> implements BaseApiService<BjtUserRecharge> {

    private static final Logger LOGGER=LoggerFactory.getLogger(BjtUserRechargeController.class);

    @Autowired
    private BjtUserRechargeService userRechargeService;
    @ApiOperation(value = "用户充值")
    @ApiImplicitParams({

    })
    @RequestMapping(value = "save/Recharge", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveRecharge(@RequestBody BjtUserRecharge bjtUserRecharge) {
        if (StringUtils.isBlank(bjtUserRecharge.getUserId())) {
            return new BaseResult(BaseResultEnum.ERROR, "用户编号不能为空！");
        }
        if (bjtUserRecharge.getMoney() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "充值金额不能为空！");
        }
        /*--------调用支付的业务逻辑，给用户添加相应的余额---------*/
        LOGGER.info("----------调用支付业务逻辑尚未实现--------");
        boolean result = userRechargeService.insert(bjtUserRecharge);

        return result ? new BaseResult(BaseResultEnum.SUCCESS, bjtUserRecharge) : new BaseResult(BaseResultEnum.ERROR,"添加用户充值记录失败");

    }

}
