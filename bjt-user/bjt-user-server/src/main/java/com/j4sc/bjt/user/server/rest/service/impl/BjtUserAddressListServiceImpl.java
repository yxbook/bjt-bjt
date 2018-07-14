package com.j4sc.bjt.user.server.rest.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseServiceImpl;
import com.j4sc.bjt.user.dao.mapper.BjtUserAddressListMapper;
import com.j4sc.bjt.user.dao.entity.BjtUserAddressList;
import com.j4sc.bjt.user.rest.api.BjtUserAddressListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

/**
* @Description: BjtUserAddressList Service实现
* @Author: LongRou
* @CreateDate: 2018/4/10.
* @Version: 1.0
**/
@Service
@Transactional
@BaseService
@RestController
@RequestMapping("BjtUserAddressList")
@ApiIgnore
public class BjtUserAddressListServiceImpl extends BaseServiceImpl<BjtUserAddressListMapper, BjtUserAddressList> implements BjtUserAddressListService {

}