package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.user.dao.entity.BjtUserConsumption;
import com.j4sc.common.base.BaseApiService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/10 15:44
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-user-server",path = "/api")
public interface UserConsumptionClient extends BaseApiService<BjtUserConsumption>{
}
