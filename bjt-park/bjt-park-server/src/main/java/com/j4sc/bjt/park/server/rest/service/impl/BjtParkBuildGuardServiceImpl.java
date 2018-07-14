package com.j4sc.bjt.park.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.park.dao.mapper.BjtParkBuildGuardMapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuard;
import com.j4sc.bjt.park.rest.api.BjtParkBuildGuardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtParkBuildGuard Service实现
* @Author: LongRou
* @CreateDate: 2018/4/4.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtParkBuildGuardServiceImpl extends BaseServiceImpl<BjtParkBuildGuardMapper, BjtParkBuildGuard> implements BjtParkBuildGuardService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildGuardServiceImpl.class);

}