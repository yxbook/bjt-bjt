package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.carpark.dao.entity.BjtCarparkSpaceUser;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/10 14:10
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-carpark-server", path = "/BjtCarparkSpaceUser")
public interface CarparkSpaceUserClient extends BaseApiService<BjtCarparkSpaceUser> {

    @RequestMapping(value = "delete/SpaceUser", method = RequestMethod.DELETE)
    BaseResult deleteSpaceUser(@RequestParam("spaceId")String spaceId, @RequestParam("userId")String userId);

    @RequestMapping(value = "select/SpaceUser", method = RequestMethod.GET)
    BaseResult<BjtCarparkSpaceUser> selectSpaceUser(@RequestParam("userId")String userId);
}
