package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.carpark.dao.entity.BjtCarparkCar;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/10 16:35
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-carpark-server", path = "/BjtCarparkCar")
public interface CarparkCarClient extends BaseApiService<BjtCarparkCar> {

    @RequestMapping(value = "select/CarparkCarList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult<List<BjtCarparkCar>> selectCarparkCarList(@RequestBody List<String> list);

    @RequestMapping(value = "delete/CarparkCarList", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult deleteCarparkCarList(@RequestParam("userId") String userId, @RequestBody List<String> list);

}
