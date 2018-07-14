package com.j4sc.bjt.api.server.controller;

import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.ParkBuildNoticeClient;
import com.j4sc.bjt.api.server.client.ParkCompanyNoticeClient;
import com.j4sc.bjt.api.server.client.SystemNoticeClient;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildNotice;
import com.j4sc.bjt.system.dao.entity.BjtSystemNotice;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/4 10:09
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/sys")
@Api(tags = {"帮家团系统服务"}, description = "帮家团系统服务 - 需授权")
public class ApiUserSystemController extends BaseJwtController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUserSystemController.class);
    @Autowired
    SystemNoticeClient systemNoticeClient;
    @Autowired
    ParkBuildNoticeClient buildNoticeClient;
    @Autowired
    ParkCompanyNoticeClient companyNoticeClient;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @ApiOperation(value = "获取公告",notes = "获取公告")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "getAllNotice", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> getAllNotice(){
        LOGGER.info("getAllNotice = >");
        Map<String,Object> map = new HashMap<>();
        BaseResult<List<BjtSystemNotice>> sysNoticeResult = systemNoticeClient.selectSysNoticeAll();//获取系统公告
        BaseResult<List<BjtParkBuildNotice>> buildNoticeResult = buildNoticeClient.selectBuildNoticeAll(getUserId());//获取楼宇公告
        map.put("sysNoticeList", sysNoticeResult.getData());
        map.put("buildNoticeList", buildNoticeResult.getData());
        return new BaseResult(BaseResultEnum.SUCCESS, map);
    }

}