package com.j4sc.bjt.park.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.park.dao.mapper.BjtParkBuildApprovalMapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildApproval;
import com.j4sc.bjt.park.rest.api.BjtParkBuildApprovalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtParkBuildApproval Service实现
* @Author: LongRou
* @CreateDate: 2018/5/3.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtParkBuildApprovalServiceImpl extends BaseServiceImpl<BjtParkBuildApprovalMapper, BjtParkBuildApproval> implements BjtParkBuildApprovalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildApprovalServiceImpl.class);

}