package com.j4sc.bjt.api.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.ParkBuildGuardClient;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuard;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import com.j4sc.common.util.AESUtil;
import com.j4sc.common.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 闸机服务
 * @Author: chengyz
 * @CreateDate: 2018 2018/5/2 16:50
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/buildGate")
@Api(tags = {"帮家团闸机服务"}, description = "帮家团闸机服务 - 需授权")
public class ApiBuildGateController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiBuildGateController.class);

    @Autowired
    private ParkBuildGuardClient parkBuildGuardClient;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @ApiOperation("获取二维码信息")
    @RequestMapping(value = "select/BuildGuardCode", method = RequestMethod.GET)
    public BaseResult selectBuildGuardCode() throws Exception{
        LOGGER.info("获取二维码信息");
        BaseResult<BjtParkBuildGuard> guardBaseResult = parkBuildGuardClient.selectGuard(getUserId());
        if (guardBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return guardBaseResult;
        }
        if (null == guardBaseResult.getData()) {
            return new BaseResult(BaseResultEnum.ERROR, "用户无门禁权限");
        }
        Map<String, String> map = new HashMap<>();
        map.put("type", "1");
        map.put("key", getUserId());
        if (redisTemplate.hasKey(getUserId())) {
            return new BaseResult(BaseResultEnum.SUCCESS, URLEncoder.encode(AESUtil.aesEncode(JSONObject.toJSONString(map)),"utf-8"));
        }
        redisTemplate.opsForValue().set(getUserId(), RandomUtil.generateLowerString(32), 3600 * 24, TimeUnit.SECONDS);
        return new BaseResult(BaseResultEnum.SUCCESS, URLEncoder.encode(AESUtil.aesEncode(JSONObject.toJSONString(map)),"utf-8"));
    }

}
