package com.j4sc.bjt.park.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.park.dao.mapper.BjtParkCompanyMapper;
import com.j4sc.bjt.park.dao.entity.BjtParkCompany;
import com.j4sc.bjt.park.rest.api.BjtParkCompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtParkCompany Service实现
* @Author: LongRou
* @CreateDate: 2018/4/3.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtParkCompanyServiceImpl extends BaseServiceImpl<BjtParkCompanyMapper, BjtParkCompany> implements BjtParkCompanyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkCompanyServiceImpl.class);

}