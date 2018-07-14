package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.park.dao.entity.BjtParkBuild;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildUser;
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
 * @CreateDate: 2018 2018/4/3 10:38
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-park-server",path = "/BjtParkBuild")
public interface ParkBuildClient extends BaseApiService<BjtParkBuild>{
    @RequestMapping("test")
    String getT();

    @RequestMapping("buildUserList")
    BaseResult<List<BjtParkBuildUser>> selectBuildUserList(@RequestParam("buildId")int buildId);

    @RequestMapping(value = "getBuildListByIdList", method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult<List<BjtParkBuild>> getBuildListByIdList(@RequestBody List<Integer> buildIdList);
}
