package com.j4sc.bjt.park.server.rest.service.impl;

import com.j4sc.bjt.park.dao.entity.BjtParkCompanyProgress;
import com.j4sc.bjt.park.dao.mapper.BjtParkCompanyProgressMapper;
import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.park.rest.api.BjtParkCompanyProgressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtParkCompanyProgress Service实现
* @Author: LongRou
* @CreateDate: 2018/4/13.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtParkCompanyProgressServiceImpl extends BaseServiceImpl<BjtParkCompanyProgressMapper, BjtParkCompanyProgress> implements BjtParkCompanyProgressService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkCompanyProgressServiceImpl.class);

}