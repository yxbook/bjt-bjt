package com.j4sc.bjt.park.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.park.dao.mapper.BjtParkBuildGuardMainMapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuardMain;
import com.j4sc.bjt.park.rest.api.BjtParkBuildGuardMainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtParkBuildGuardMain Service实现
* @Author: LongRou
* @CreateDate: 2018/4/26.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtParkBuildGuardMainServiceImpl extends BaseServiceImpl<BjtParkBuildGuardMainMapper, BjtParkBuildGuardMain> implements BjtParkBuildGuardMainService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildGuardMainServiceImpl.class);

}