package com.j4sc.bjt.user.server.rest.service.impl;

import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.user.dao.mapper.BjtUserFeedbackMapper;
import com.j4sc.bjt.user.dao.entity.BjtUserFeedback;
import com.j4sc.bjt.user.rest.api.BjtUserFeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

/**
* @Description: BjtUserFeedback Service实现
* @Author: LongRou
* @CreateDate: 2018/4/10.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
@RestController
@RequestMapping("BjtUserFeedback")
@ApiIgnore
public class BjtUserFeedbackServiceImpl extends BaseServiceImpl<BjtUserFeedbackMapper, BjtUserFeedback> implements BjtUserFeedbackService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtUserFeedbackServiceImpl.class);

}