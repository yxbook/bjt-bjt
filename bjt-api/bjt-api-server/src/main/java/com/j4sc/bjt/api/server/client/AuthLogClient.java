package com.j4sc.bjt.api.server.client;

import com.j4sc.auth.dao.entity.AuthLog;
import com.j4sc.common.base.BaseApiService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/2 10:20
 * @Version: 1.0
 **/
@FeignClient(value = "j4sc-auth-server",path = "/AuthLog")
public interface AuthLogClient extends BaseApiService<AuthLog>{
}
