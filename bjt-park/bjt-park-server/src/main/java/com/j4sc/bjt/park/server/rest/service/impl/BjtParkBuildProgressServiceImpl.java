package com.j4sc.bjt.park.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.park.dao.mapper.BjtParkBuildProgressMapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildProgress;
import com.j4sc.bjt.park.rest.api.BjtParkBuildProgressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtParkBuildProgress Service实现
* @Author: LongRou
* @CreateDate: 2018/4/27.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtParkBuildProgressServiceImpl extends BaseServiceImpl<BjtParkBuildProgressMapper, BjtParkBuildProgress> implements BjtParkBuildProgressService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildProgressServiceImpl.class);

}