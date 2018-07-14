package com.j4sc.bjt.user.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.user.dao.mapper.BjtUserBillMapper;
import com.j4sc.bjt.user.dao.entity.BjtUserBill;
import com.j4sc.bjt.user.rest.api.BjtUserBillService;
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
* @Description: BjtUserBill Service实现
* @Author: LongRou
* @CreateDate: 2018/4/10.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
@RestController
@RequestMapping("BjtUserBill")
@ApiIgnore
public class BjtUserBillServiceImpl extends BaseServiceImpl<BjtUserBillMapper, BjtUserBill> implements BjtUserBillService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtUserBillServiceImpl.class);

}