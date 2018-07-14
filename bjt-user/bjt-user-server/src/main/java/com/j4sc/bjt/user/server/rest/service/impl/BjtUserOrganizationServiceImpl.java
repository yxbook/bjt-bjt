package com.j4sc.bjt.user.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.user.dao.mapper.BjtUserOrganizationMapper;
import com.j4sc.bjt.user.dao.entity.BjtUserOrganization;
import com.j4sc.bjt.user.rest.api.BjtUserOrganizationService;
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
* @Description: BjtUserOrganization Service实现
* @Author: LongRou
* @CreateDate: 2018/3/30.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
@RestController
@RequestMapping("BjtUserOrganization")
@ApiIgnore
public class BjtUserOrganizationServiceImpl extends BaseServiceImpl<BjtUserOrganizationMapper, BjtUserOrganization> implements BjtUserOrganizationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtUserOrganizationServiceImpl.class);

}