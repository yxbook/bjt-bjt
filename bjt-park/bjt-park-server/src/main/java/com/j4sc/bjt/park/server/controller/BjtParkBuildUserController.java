package com.j4sc.bjt.park.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.park.dao.entity.*;
import com.j4sc.bjt.park.rest.api.*;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/3 11:04
 * @Version: 1.0
 **/
@RestController
@RequestMapping("BjtParkBuildUser")
public class BjtParkBuildUserController extends BaseController<BjtParkBuildUser, BjtParkBuildUserService> implements BaseApiService<BjtParkBuildUser> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildUserController.class);

    @Autowired
    private BjtParkBuildGuardService bjtParkBuildGuardService;

    @RequestMapping(path = "select/Count", method = RequestMethod.GET)
    public BaseResult selectCount(@RequestParam("buildId") String buildId) {
        LOGGER.info("统计楼宇总人数");
        int count = bjtParkBuildGuardService.selectCount(new EntityWrapper<BjtParkBuildGuard>().eq("build_id", buildId));
        return new BaseResult(BaseResultEnum.SUCCESS, count);
    }

}
