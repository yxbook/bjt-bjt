package com.j4sc.bjt.park.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.park.dao.mapper.BjtParkBuildHouseMapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildHouse;
import com.j4sc.bjt.park.rest.api.BjtParkBuildHouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtParkBuildHouse Service实现
* @Author: LongRou
* @CreateDate: 2018/4/14.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtParkBuildHouseServiceImpl extends BaseServiceImpl<BjtParkBuildHouseMapper, BjtParkBuildHouse> implements BjtParkBuildHouseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildHouseServiceImpl.class);

}