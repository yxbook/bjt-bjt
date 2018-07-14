package com.j4sc.bjt.carpark.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.carpark.dao.mapper.BjtCarparkSpaceMapper;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkSpace;
import com.j4sc.bjt.carpark.rest.api.BjtCarparkSpaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtCarparkSpace Service实现
* @Author: LongRou
* @CreateDate: 2018/4/18.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtCarparkSpaceServiceImpl extends BaseServiceImpl<BjtCarparkSpaceMapper, BjtCarparkSpace> implements BjtCarparkSpaceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtCarparkSpaceServiceImpl.class);

}