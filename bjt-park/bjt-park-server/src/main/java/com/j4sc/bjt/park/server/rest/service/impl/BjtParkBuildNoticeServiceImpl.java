package com.j4sc.bjt.park.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.park.dao.mapper.BjtParkBuildNoticeMapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildNotice;
import com.j4sc.bjt.park.rest.api.BjtParkBuildNoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtParkBuildNotice Service实现
* @Author: LongRou
* @CreateDate: 2018/4/3.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtParkBuildNoticeServiceImpl extends BaseServiceImpl<BjtParkBuildNoticeMapper, BjtParkBuildNotice> implements BjtParkBuildNoticeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildNoticeServiceImpl.class);

}