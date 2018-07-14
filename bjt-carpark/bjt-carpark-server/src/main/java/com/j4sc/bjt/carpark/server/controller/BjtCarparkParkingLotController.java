package com.j4sc.bjt.carpark.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkParkingLot;
import com.j4sc.bjt.carpark.rest.api.BjtCarparkParkingLotService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/14 10:09
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/BjtCarparkParkingLot")
@ApiModel
@Api(tags = {"帮家团停车场系统车辆信息录入服务"}, description = "车辆信息录入服务-需授权")
public class BjtCarparkParkingLotController extends BaseController<BjtCarparkParkingLot, BjtCarparkParkingLotService> implements BaseApiService<BjtCarparkParkingLot> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtCarparkParkingLotController.class);

    @Autowired
    private BjtCarparkParkingLotService parkingLotService;

    @ApiOperation("添加车位信息")
    @RequestMapping(value = "add/ParkingLot", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addParkingLot(@RequestBody List<BjtCarparkParkingLot> list) {
        if (parkingLotService.insertBatch(list)) {
            return new BaseResult(BaseResultEnum.SUCCESS, "添加车位信息成功");
        }
        return new BaseResult(BaseResultEnum.ERROR, "添加车位信息失败");
    }

    @ApiOperation("删除车位信息")
    @RequestMapping(value = "delete/ParkingLot", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult deleteParkingLot(@RequestBody List<Integer> list, @RequestParam("spaceId") int spaceId) {
        List<BjtCarparkParkingLot> lotList = parkingLotService.selectList(new EntityWrapper<BjtCarparkParkingLot>().eq("space_id", spaceId).in("lot_id", list));
        if (lotList == null || lotList.size() < 1) {
            return new BaseResult(BaseResultEnum.ERROR, "车位信息不存在");
        }
        List<Integer> lotIdList = new ArrayList<>();
        lotList.forEach(v -> {
            lotIdList.add(v.getLotId());
        });
        if (parkingLotService.deleteBatchIds(list)) {
            return new BaseResult(BaseResultEnum.SUCCESS, "删除车位信息成功");
        }
        return new BaseResult(BaseResultEnum.ERROR, "删除车位信息失败");
    }
}
