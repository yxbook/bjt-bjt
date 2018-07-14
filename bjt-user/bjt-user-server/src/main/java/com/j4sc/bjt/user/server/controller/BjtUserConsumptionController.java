package com.j4sc.bjt.user.server.controller;

import com.j4sc.bjt.user.dao.entity.BjtUserConsumption;
import com.j4sc.bjt.user.dao.entity.BjtUserRecharge;
import com.j4sc.bjt.user.rest.api.BjtUserConsumptionService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/10 14:21
 * @Version: 1.0
 **/
@RestController
@RequestMapping("BjtUserConsumption")
@ApiModel
@Api(tags = {"用户消费服务"}, description = "用户消费服务")
public class BjtUserConsumptionController extends BaseController<BjtUserConsumption, BjtUserConsumptionService> implements BaseApiService<BjtUserConsumption> {

    private static final Logger LOGGER=LoggerFactory.getLogger(BjtUserConsumptionController.class);

}
