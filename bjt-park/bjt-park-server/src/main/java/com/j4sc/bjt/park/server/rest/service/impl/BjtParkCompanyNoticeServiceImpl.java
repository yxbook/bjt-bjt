package com.j4sc.bjt.park.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.park.dao.mapper.BjtParkCompanyNoticeMapper;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyNotice;
import com.j4sc.bjt.park.rest.api.BjtParkCompanyNoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtParkCompanyNotice Service实现
* @Author: LongRou
* @CreateDate: 2018/4/3.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtParkCompanyNoticeServiceImpl extends BaseServiceImpl<BjtParkCompanyNoticeMapper, BjtParkCompanyNotice> implements BjtParkCompanyNoticeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkCompanyNoticeServiceImpl.class);

}