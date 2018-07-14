package com.j4sc.bjt.user.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.user.dao.mapper.BjtUserEmailBoxMapper;
import com.j4sc.bjt.user.dao.entity.BjtUserEmailBox;
import com.j4sc.bjt.user.rest.api.BjtUserEmailBoxService;
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
* @Description: BjtUserEmailBox Service实现
* @Author: LongRou
* @CreateDate: 2018/4/10.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
@RestController
@RequestMapping("BjtUserEmailBox")
@ApiIgnore
public class BjtUserEmailBoxServiceImpl extends BaseServiceImpl<BjtUserEmailBoxMapper, BjtUserEmailBox> implements BjtUserEmailBoxService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtUserEmailBoxServiceImpl.class);

}