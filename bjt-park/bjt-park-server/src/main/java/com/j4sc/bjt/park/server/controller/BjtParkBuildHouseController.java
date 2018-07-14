package com.j4sc.bjt.park.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuard;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildHouse;
import com.j4sc.bjt.park.rest.api.BjtParkBuildHouseService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/14 9:14
 * @Version: 1.0
 **/
@RestController
@RequestMapping("BjtParkBuildHouse")
public class BjtParkBuildHouseController extends BaseController<BjtParkBuildHouse, BjtParkBuildHouseService> implements BaseApiService<BjtParkBuildHouse>{
    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildHouseController.class);

    @Autowired
    private BjtParkBuildHouseService parkBuildHouseService;

    @ApiOperation("新增房屋信息")
    @Transactional
    @RequestMapping(value = "add/BuildHouse", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addBuildHouse(@RequestBody List<BjtParkBuildHouse> list) {
        LOGGER.info("新增房屋信息...");
        if (parkBuildHouseService.insertBatch(list)) return new BaseResult(BaseResultEnum.SUCCESS, "新增房屋信息成功");
        return new BaseResult(BaseResultEnum.ERROR, "新增房屋失败");
    }

    @ApiOperation("查询未入驻的房屋信息")
    @RequestMapping(value = "select/BuildHouseList", method = RequestMethod.GET)
    public BaseResult<List<BjtParkBuildHouse>> selectBuildHouseList(@RequestParam Map<String, Object> params){
        List<BjtParkBuildHouse> parkBuildHouseList = parkBuildHouseService.selectList(new EntityWrapper<BjtParkBuildHouse>()
                .eq("build_id", params.get("buildId")).isNull("company_id"));
        return new BaseResult(BaseResultEnum.SUCCESS, parkBuildHouseList);
    }

}

