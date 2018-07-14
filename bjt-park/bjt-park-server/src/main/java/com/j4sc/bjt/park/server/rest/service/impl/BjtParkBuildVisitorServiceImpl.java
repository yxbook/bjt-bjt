package com.j4sc.bjt.park.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.park.dao.mapper.BjtParkBuildVisitorMapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildVisitor;
import com.j4sc.bjt.park.rest.api.BjtParkBuildVisitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtParkBuildVisitor Service实现
* @Author: LongRou
* @CreateDate: 2018/5/2.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtParkBuildVisitorServiceImpl extends BaseServiceImpl<BjtParkBuildVisitorMapper, BjtParkBuildVisitor> implements BjtParkBuildVisitorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildVisitorServiceImpl.class);

}