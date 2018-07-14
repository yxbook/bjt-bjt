package com.j4sc.bjt.system.server.rest.service.impl;

import com.j4sc.bjt.system.dao.entity.BjtSystemAppCarousel;
import com.j4sc.bjt.system.dao.mapper.BjtSystemAppCarouselMapper;
import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.system.rest.api.BjtSystemAppCarouselService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @Description: BjtSystemAppCarousel Service实现
* @Author: LongRou
* @CreateDate: 2018/4/12.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtSystemAppCarouselServiceImpl extends BaseServiceImpl<BjtSystemAppCarouselMapper, BjtSystemAppCarousel> implements BjtSystemAppCarouselService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtSystemAppCarouselServiceImpl.class);

}