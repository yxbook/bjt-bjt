package com.j4sc.bjt.carpark.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.carpark.dao.mapper.BjtCarparkPaymentMapper;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkPayment;
import com.j4sc.bjt.carpark.rest.api.BjtCarparkPaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtCarparkPayment Service实现
* @Author: LongRou
* @CreateDate: 2018/5/11.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtCarparkPaymentServiceImpl extends BaseServiceImpl<BjtCarparkPaymentMapper, BjtCarparkPayment> implements BjtCarparkPaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtCarparkPaymentServiceImpl.class);

}