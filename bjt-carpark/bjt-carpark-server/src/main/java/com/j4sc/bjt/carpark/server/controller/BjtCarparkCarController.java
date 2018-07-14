package com.j4sc.bjt.carpark.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkApplyDetail;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkCar;
import com.j4sc.bjt.carpark.rest.api.BjtCarparkApplyDetailService;
import com.j4sc.bjt.carpark.rest.api.BjtCarparkCarService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/12 10:43
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/BjtCarparkCar")
@ApiModel
@Api(tags = {"帮家团停车场系统车辆信息录入服务"}, description = "车辆信息录入服务-需授权")
public class BjtCarparkCarController extends BaseController<BjtCarparkCar, BjtCarparkCarService> implements BaseApiService<BjtCarparkCar> {

    private static final Logger LOGGER= LoggerFactory.getLogger(BjtCarparkCarController.class);

    @Autowired
    private BjtCarparkCarService carparkCarService;

    @ApiOperation("获取车辆信息列表")
    @RequestMapping(value = "select/CarparkCarList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult<List<BjtCarparkCar>> selectCarparkCarList(@RequestBody List<String> list) {
        return new BaseResult(BaseResultEnum.SUCCESS, carparkCarService.selectList(new EntityWrapper<BjtCarparkCar>().in("user_id", list)));
    }
    @ApiOperation("删除车辆信息列表")
    @RequestMapping(value = "delete/CarparkCarList", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult deleteCarparkCarList(@RequestParam("userId") String userId, @RequestBody List<String> list) {
        LOGGER.info("deleteCarparkCarList = > 删除车辆信息列表");
        list.add("-1");
        List<BjtCarparkCar> carparkCarList = carparkCarService.selectList(new EntityWrapper<BjtCarparkCar>().eq("user_id", userId).in("car_id", list));
        if (carparkCarList == null || carparkCarList.size() < 1) {
            return new BaseResult(BaseResultEnum.ERROR, "车辆信息列表为空");
        }
        List<Integer> idList = new ArrayList<>();
        carparkCarList.forEach(v -> {
            idList.add(v.getCarId());
        });
        if (carparkCarService.deleteBatchIds(idList)) {
            return new BaseResult(BaseResultEnum.SUCCESS, "删除成功");
        }
        return new BaseResult(BaseResultEnum.ERROR, "删除失败" );
    }
}
