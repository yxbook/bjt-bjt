package com.j4sc.bjt.user.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.user.dao.mapper.BjtUserRechargeMapper;
import com.j4sc.bjt.user.dao.entity.BjtUserRecharge;
import com.j4sc.bjt.user.rest.api.BjtUserRechargeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

/**
* @Description: BjtUserRecharge Service实现
* @Author: LongRou
* @CreateDate: 2018/4/2.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
@RestController
@RequestMapping("BjtUserRecharge")
@ApiIgnore
public class BjtUserRechargeServiceImpl extends BaseServiceImpl<BjtUserRechargeMapper, BjtUserRecharge> implements BjtUserRechargeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtUserRechargeServiceImpl.class);

}