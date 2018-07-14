package com.j4sc.bjt.user.server.rest.service.impl;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.user.dao.entity.BjtUserUser;
import com.j4sc.bjt.user.dao.mapper.BjtUserUserMapper;
import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.user.rest.api.BjtUserUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
* @Description: BjtUserUser Service实现
* @Author: LongRou
* @CreateDate: 2018/3/29.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
public class BjtUserUserServiceImpl extends BaseServiceImpl<BjtUserUserMapper, BjtUserUser> implements BjtUserUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtUserUserServiceImpl.class);

}