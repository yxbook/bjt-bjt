package com.j4sc.bjt.api.server.client;

import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildApproval;
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
 * @Author: chengyz
 * @CreateDate: 2018 2018/4/9 9:58
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-park-server",path = "/BjtParkBuildApproval")
public interface ParkBuildApprovalClient extends BaseApiService<BjtParkBuildApproval> {

    @RequestMapping(value = "save/BuildApproval", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult saveBuildApproval(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "update/BuildApproval", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult updateBuildApproval(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "select/BuildApprovalPage", method = RequestMethod.GET)
    BaseResult<Page<BjtParkBuildApproval>> selectBuildApprovalPage(@RequestParam Map<String, Object> params);

    @RequestMapping(value = "select/BuildUserList", method = RequestMethod.GET)
    BaseResult<List<BjtParkBuildUser>> selectBuildUserList(@RequestParam("userId")String userId, @RequestParam("buildId")int buildId);
}
