package com.j4sc.bjt.park.server.rest.service.impl;

import com.j4sc.bjt.park.dao.entity.BjtParkCompanyClockRule;
import com.j4sc.bjt.park.dao.mapper.BjtParkCompanyClockRuleMapper;
import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.park.rest.api.BjtParkCompanyClockRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtParkCompanyClockRule Service实现
* @Author: LongRou
* @CreateDate: 2018/4/13.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtParkCompanyClockRuleServiceImpl extends BaseServiceImpl<BjtParkCompanyClockRuleMapper, BjtParkCompanyClockRule> implements BjtParkCompanyClockRuleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkCompanyClockRuleServiceImpl.class);

}