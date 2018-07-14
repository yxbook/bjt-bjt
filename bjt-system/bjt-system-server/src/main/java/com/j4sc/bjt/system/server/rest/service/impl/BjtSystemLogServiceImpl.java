package com.j4sc.bjt.system.server.rest.service.impl;

import com.j4sc.bjt.system.dao.entity.BjtSystemLog;
import com.j4sc.bjt.system.dao.mapper.BjtSystemLogMapper;
import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.system.rest.api.BjtSystemLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtSystemLog Service实现
* @Author: LongRou
* @CreateDate: 2018/4/17.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtSystemLogServiceImpl extends BaseServiceImpl<BjtSystemLogMapper, BjtSystemLog> implements BjtSystemLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtSystemLogServiceImpl.class);

}