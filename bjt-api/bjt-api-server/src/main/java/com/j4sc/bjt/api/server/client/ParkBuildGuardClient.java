package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuard;
import com.j4sc.bjt.park.dao.entity.BjtParkCompany;
import com.j4sc.bjt.user.dao.entity.BjtUserUser;
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
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/4 14:54
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-park-server", path = "/BjtParkBuildGuard")
public interface ParkBuildGuardClient extends BaseApiService<BjtParkBuildGuard> {

    @RequestMapping(value = "select/GuardList", method = RequestMethod.GET)
    BaseResult<List<BjtParkBuildGuard>> selectGuardList(@RequestParam Map<String, Object> params);

    @RequestMapping(value = "delete/Guard", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult deleteGuard(@RequestBody List<String> userIdList);

    @RequestMapping(value = "delete/GuardList", method = RequestMethod.DELETE)
    BaseResult deleteGuardList(@RequestParam("companyId") String companyId);

    @RequestMapping(value = "select/Guard", method = RequestMethod.GET)
    BaseResult<BjtParkBuildGuard> selectGuard(@RequestParam("userId")String userId);
}
