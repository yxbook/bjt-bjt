package com.j4sc.bjt.park.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.park.dao.mapper.BjtParkAttributionMapper;
import com.j4sc.bjt.park.dao.entity.BjtParkAttribution;
import com.j4sc.bjt.park.rest.api.BjtParkAttributionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtParkAttribution Service实现
* @Author: LongRou
* @CreateDate: 2018/4/9.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtParkAttributionServiceImpl extends BaseServiceImpl<BjtParkAttributionMapper, BjtParkAttribution> implements BjtParkAttributionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkAttributionServiceImpl.class);

}