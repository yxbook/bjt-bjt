package com.j4sc.bjt.api.server.controller;

import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.SystemAppCarouselClient;
import com.j4sc.bjt.system.dao.entity.BjtSystemAppCarousel;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.util.MD5Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/18 16:30
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/carousel")
@Api(tags = {"帮家团首页轮播图服务"}, description = "帮家团首页轮播图服务-需授权")
public class ApiSystemAppCarouselController extends BaseJwtController{
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSystemAppCarouselController.class);
    @Autowired
    private SystemAppCarouselClient appCarouselClient;

    @ApiOperation(value = "获取轮播图列表")
    @RequestMapping(value = "select/AppCarouselList", method = RequestMethod.GET)
    public BaseResult<List<BjtSystemAppCarousel>> selectAppCarouselList() {
        Map<String, Object> params = new HashMap<>();
        params.put("orderByAsc", "order_value");
        params.put("status", 2);//状态为2表示正常，为1表示禁用
        return appCarouselClient.selectAll(params);
    }


}
