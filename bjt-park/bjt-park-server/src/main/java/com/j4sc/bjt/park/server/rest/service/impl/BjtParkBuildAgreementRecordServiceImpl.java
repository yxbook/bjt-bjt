package com.j4sc.bjt.park.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.park.dao.mapper.BjtParkBuildAgreementRecordMapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildAgreementRecord;
import com.j4sc.bjt.park.rest.api.BjtParkBuildAgreementRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtParkBuildAgreementRecord Service实现
* @Author: LongRou
* @CreateDate: 2018/5/14.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtParkBuildAgreementRecordServiceImpl extends BaseServiceImpl<BjtParkBuildAgreementRecordMapper, BjtParkBuildAgreementRecord> implements BjtParkBuildAgreementRecordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildAgreementRecordServiceImpl.class);

}