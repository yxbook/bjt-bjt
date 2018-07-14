package com.j4sc.bjt.system.server.rest.service.impl;

import com.j4sc.bjt.system.dao.entity.BjtSystemNotice;
import com.j4sc.bjt.system.dao.mapper.BjtSystemNoticeMapper;
import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.system.rest.api.BjtSystemNoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtSystemNotice Service实现
* @Author: LongRou
* @CreateDate: 2018/4/4.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtSystemNoticeServiceImpl extends BaseServiceImpl<BjtSystemNoticeMapper, BjtSystemNotice> implements BjtSystemNoticeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtSystemNoticeServiceImpl.class);

}