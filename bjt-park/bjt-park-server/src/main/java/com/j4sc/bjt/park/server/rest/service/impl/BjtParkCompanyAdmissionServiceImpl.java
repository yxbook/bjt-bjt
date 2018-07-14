package com.j4sc.bjt.park.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.park.dao.mapper.BjtParkCompanyAdmissionMapper;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyAdmission;
import com.j4sc.bjt.park.rest.api.BjtParkCompanyAdmissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtParkCompanyAdmission Service实现
* @Author: LongRou
* @CreateDate: 2018/4/17.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtParkCompanyAdmissionServiceImpl extends BaseServiceImpl<BjtParkCompanyAdmissionMapper, BjtParkCompanyAdmission> implements BjtParkCompanyAdmissionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkCompanyAdmissionServiceImpl.class);

}