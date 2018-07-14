package com.j4sc.bjt.carpark.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.carpark.dao.mapper.BjtCarparkApplyDetailMapper;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkApplyDetail;
import com.j4sc.bjt.carpark.rest.api.BjtCarparkApplyDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtCarparkApplyDetail Service实现
* @Author: LongRou
* @CreateDate: 2018/4/13.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtCarparkApplyDetailServiceImpl extends BaseServiceImpl<BjtCarparkApplyDetailMapper, BjtCarparkApplyDetail> implements BjtCarparkApplyDetailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtCarparkApplyDetailServiceImpl.class);

}