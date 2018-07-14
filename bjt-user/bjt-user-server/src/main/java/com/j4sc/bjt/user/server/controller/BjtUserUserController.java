package com.j4sc.bjt.user.server.controller;

import com.j4sc.bjt.user.dao.entity.BjtUserUser;
import com.j4sc.bjt.user.rest.api.BjtUserUserService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/14 11:05
 * @Version: 1.0
 **/
@RestController
@RequestMapping("BjtUserUser")
@Api(tags = {"用户"}, description = "用户")
public class BjtUserUserController extends BaseController<BjtUserUser, BjtUserUserService> implements BaseApiService<BjtUserUser> {
}
