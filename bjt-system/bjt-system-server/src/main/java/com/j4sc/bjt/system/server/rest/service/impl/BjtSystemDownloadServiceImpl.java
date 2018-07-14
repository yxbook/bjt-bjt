package com.j4sc.bjt.system.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.system.dao.mapper.BjtSystemDownloadMapper;
import com.j4sc.bjt.system.dao.entity.BjtSystemDownload;
import com.j4sc.bjt.system.rest.api.BjtSystemDownloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtSystemDownload Service实现
* @Author: LongRou
* @CreateDate: 2018/5/15.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtSystemDownloadServiceImpl extends BaseServiceImpl<BjtSystemDownloadMapper, BjtSystemDownload> implements BjtSystemDownloadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtSystemDownloadServiceImpl.class);

}