package com.j4sc.bjt.api.server.client;

import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuard;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuardMain;
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
@FeignClient(value = "bjt-park-server", path = "/BjtParkBuildGuardMain")
public interface ParkBuildGuardMainClient extends BaseApiService<BjtParkBuildGuardMain> {
    @RequestMapping(value = "save/GuardMain", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult saveGuardMain(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "update/GuardMain", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult updateGuardMain(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "select/GuardMainPage", method = RequestMethod.GET)
    BaseResult<Page<BjtParkBuildGuardMain>> selectGuardMainPage(@RequestParam Map<String, Object> params);
    /*
    @RequestMapping(value = "delete/GuardMain", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult deleteGuard(@RequestBody List<String> userIdList);

    @RequestMapping(value = "delete/GuardMainList", method = RequestMethod.DELETE)
    BaseResult deleteGuardMainList(@RequestParam("companyId") String companyId);
    */
}
