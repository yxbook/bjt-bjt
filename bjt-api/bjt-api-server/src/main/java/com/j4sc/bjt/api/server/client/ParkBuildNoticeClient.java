package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.park.dao.entity.BjtParkBuildNotice;
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
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/3 10:38
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-park-server",path = "/BjtParkBuildNotice")
public interface ParkBuildNoticeClient extends BaseApiService<BjtParkBuildNotice>{
    @RequestMapping(value = "select/BuildNoticeAll", method = RequestMethod.GET)
    BaseResult<List<BjtParkBuildNotice>> selectBuildNoticeAll(@RequestParam("userId")String userId);

    @RequestMapping(value = "save/BuildNotice", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult saveBuildNotice(@RequestBody BjtParkBuildNotice bjtParkBuildNotice);
}
