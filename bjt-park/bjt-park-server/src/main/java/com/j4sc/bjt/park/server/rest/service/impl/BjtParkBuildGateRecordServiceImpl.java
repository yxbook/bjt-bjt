package com.j4sc.bjt.park.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.park.dao.mapper.BjtParkBuildGateRecordMapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGateRecord;
import com.j4sc.bjt.park.rest.api.BjtParkBuildGateRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtParkBuildGateRecord Service实现
* @Author: LongRou
* @CreateDate: 2018/5/2.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtParkBuildGateRecordServiceImpl extends BaseServiceImpl<BjtParkBuildGateRecordMapper, BjtParkBuildGateRecord> implements BjtParkBuildGateRecordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildGateRecordServiceImpl.class);

}