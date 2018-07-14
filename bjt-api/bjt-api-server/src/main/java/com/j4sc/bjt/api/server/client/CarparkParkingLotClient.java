package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkCar;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkParkingLot;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.*;
import java.util.List;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/14 10:05
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-carpark-server", path = "/BjtCarparkParkingLot")
public interface CarparkParkingLotClient extends BaseApiService<BjtCarparkParkingLot>{

    @RequestMapping(value = "add/ParkingLot", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult addParkingLot(@RequestBody List<BjtCarparkParkingLot> list);

    @RequestMapping(value = "delete/ParkingLot", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult deleteParkingLot(@RequestBody List<Integer> list, @RequestParam("spaceId") int spaceId);
}
