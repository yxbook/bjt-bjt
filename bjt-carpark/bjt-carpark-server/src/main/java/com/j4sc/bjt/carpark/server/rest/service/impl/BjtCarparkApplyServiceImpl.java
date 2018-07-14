package com.j4sc.bjt.carpark.server.rest.service.impl;

import com.j4sc.bjt.carpark.dao.entity.BjtCarparkApply;
import com.j4sc.bjt.carpark.dao.mapper.BjtCarparkApplyMapper;
import com.j4sc.bjt.carpark.rest.api.BjtCarparkApplyService;
import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtCarparkApply Service实现
* @Author: LongRou
* @CreateDate: 2018/4/13.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtCarparkApplyServiceImpl extends BaseServiceImpl<BjtCarparkApplyMapper, BjtCarparkApply> implements BjtCarparkApplyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtCarparkApplyServiceImpl.class);

}