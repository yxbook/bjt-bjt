package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuard;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildHouse;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/14 9:38
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-park-server", path = "/BjtParkBuildHouse")
public interface ParkBuildHouseClient extends BaseApiService<BjtParkBuildHouse>{
    @RequestMapping(value = "add/BuildHouse", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult addBuildHouse(@RequestBody List<BjtParkBuildHouse> list);

    @RequestMapping(value = "select/BuildHouseList", method = RequestMethod.GET)
    BaseResult<List<BjtParkBuildHouse>> selectBuildHouseList(@RequestParam Map<String, Object> params);
}
