package com.j4sc.bjt.park.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.park.dao.mapper.BjtParkBuildPayRecordMapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildPayRecord;
import com.j4sc.bjt.park.rest.api.BjtParkBuildPayRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtParkBuildPayRecord Service实现
* @Author: LongRou
* @CreateDate: 2018/4/18.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtParkBuildPayRecordServiceImpl extends BaseServiceImpl<BjtParkBuildPayRecordMapper, BjtParkBuildPayRecord> implements BjtParkBuildPayRecordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildPayRecordServiceImpl.class);

}