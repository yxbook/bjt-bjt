package com.j4sc.bjt.carpark.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.carpark.dao.mapper.BjtCarparkSpaceUserMapper;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkSpaceUser;
import com.j4sc.bjt.carpark.rest.api.BjtCarparkSpaceUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtCarparkSpaceUser Service实现
* @Author: LongRou
* @CreateDate: 2018/5/8.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtCarparkSpaceUserServiceImpl extends BaseServiceImpl<BjtCarparkSpaceUserMapper, BjtCarparkSpaceUser> implements BjtCarparkSpaceUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtCarparkSpaceUserServiceImpl.class);

}