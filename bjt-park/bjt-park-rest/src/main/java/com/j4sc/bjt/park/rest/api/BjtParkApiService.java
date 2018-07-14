package com.j4sc.bjt.park.rest.api;

import com.j4sc.common.base.BaseResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description: 园区服务暴露
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/3 10:32
 * @Version: 1.0
 **/
public interface BjtParkApiService {
    //新建楼宇管理者
    @RequestMapping(value = "/addBuildUser", method = RequestMethod.POST)
    BaseResult addBuildUser(@RequestParam("buildId") int buildId, @RequestParam("userId") String userId, @RequestParam("username") String username, @RequestParam("realname") String realname);


}
