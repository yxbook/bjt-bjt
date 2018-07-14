package com.j4sc.bjt.carpark.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.carpark.dao.mapper.BjtCarparkCarMapper;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkCar;
import com.j4sc.bjt.carpark.rest.api.BjtCarparkCarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtCarparkCar Service实现
* @Author: LongRou
* @CreateDate: 2018/4/13.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtCarparkCarServiceImpl extends BaseServiceImpl<BjtCarparkCarMapper, BjtCarparkCar> implements BjtCarparkCarService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtCarparkCarServiceImpl.class);

}