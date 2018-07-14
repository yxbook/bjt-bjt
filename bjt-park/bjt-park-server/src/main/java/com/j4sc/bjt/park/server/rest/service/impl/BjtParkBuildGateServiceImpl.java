package com.j4sc.bjt.park.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.park.dao.mapper.BjtParkBuildGateMapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGate;
import com.j4sc.bjt.park.rest.api.BjtParkBuildGateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtParkBuildGate Service实现
* @Author: LongRou
* @CreateDate: 2018/5/2.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtParkBuildGateServiceImpl extends BaseServiceImpl<BjtParkBuildGateMapper, BjtParkBuildGate> implements BjtParkBuildGateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildGateServiceImpl.class);

}