package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.park.dao.entity.BjtParkBuild;
import com.j4sc.bjt.park.rest.api.BjtParkApiService;
import com.j4sc.common.base.BaseApiService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/3 10:38
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-park-server",path = "/api")
public interface ParkApiClient extends BjtParkApiService{
}
