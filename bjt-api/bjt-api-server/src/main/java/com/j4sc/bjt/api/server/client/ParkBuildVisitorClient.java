package com.j4sc.bjt.api.server.client;

import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildVisitor;
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
 * @CreateDate: 2018/5/4 16:56
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-park-server",path = "/BjtParkBuildVisitor")
public interface ParkBuildVisitorClient extends BaseApiService<BjtParkBuildVisitor> {
    @RequestMapping(value = "add/BuildVisitor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult addBuildVisitor(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "update/BuildVisitor", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult updateBuildVisitor(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "select/PageBuildVisitor", method = RequestMethod.GET)
    BaseResult<Page<BjtParkBuildVisitor>> selectBuildVisitorPage(@RequestParam Map<String,Object> params);

    @RequestMapping(value = "select/BuildVisitorLatest", method = RequestMethod.GET)
    BaseResult<BjtParkBuildVisitor> selectBuildVisitorLatest(@RequestParam("phone")String phone);

    @RequestMapping(value = "delete/BuildVisitor", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult deleteBuildVisitor(@RequestParam("type") String type,@RequestParam("who")String who, @RequestBody List<String> list);
}
