package com.j4sc.bjt.system.server.controller;

import com.j4sc.bjt.system.dao.entity.BjtSystemAppCarousel;
import com.j4sc.bjt.system.rest.api.BjtSystemAppCarouselService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/18 14:27
 * @Version: 1.0
 **/
@RestController
@RequestMapping("BjtSystemAppCarousel")
@ApiModel
@Api(tags = {"移动端首页轮播图管理"}, description = "移动端首页轮播图管理")
public class BjtSystemAppCarouselServiceController extends BaseController<BjtSystemAppCarousel, BjtSystemAppCarouselService> implements BaseApiService<BjtSystemAppCarousel>{
}
