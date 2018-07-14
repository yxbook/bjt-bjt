package com.j4sc.bjt.park.server.rest.service.impl;

import com.j4sc.bjt.park.dao.entity.BjtParkCompanySign;
import com.j4sc.bjt.park.dao.mapper.BjtParkCompanySignMapper;
import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.park.rest.api.BjtParkCompanySignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtParkCompanySign Service实现
* @Author: LongRou
* @CreateDate: 2018/4/13.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtParkCompanySignServiceImpl extends BaseServiceImpl<BjtParkCompanySignMapper, BjtParkCompanySign> implements BjtParkCompanySignService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkCompanySignServiceImpl.class);

}