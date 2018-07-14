package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.park.dao.entity.BjtParkCompanyProgress;
import com.j4sc.common.base.BaseApiService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/14 9:37
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-park-server", path = "BjtParkCompanyProgress")
public interface ParkCompanyProgressClient extends BaseApiService<BjtParkCompanyProgress>{
}
