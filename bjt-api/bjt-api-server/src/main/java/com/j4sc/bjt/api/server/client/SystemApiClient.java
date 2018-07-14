package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.system.rest.api.SystemApiSerivece;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/3/30 17:38
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-system-server",path = "/api")
public interface SystemApiClient extends SystemApiSerivece{
}
