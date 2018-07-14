package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.park.dao.entity.BjtParkAttribution;
import com.j4sc.common.base.BaseApiService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018 2018/4/9 9:58
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-park-server",path = "/ParkAttribution")
public interface ParkAttributionClient extends BaseApiService<BjtParkAttribution> {

}
