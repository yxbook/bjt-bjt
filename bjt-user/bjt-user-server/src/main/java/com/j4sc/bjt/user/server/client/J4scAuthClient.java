package com.j4sc.bjt.user.server.client;

import com.j4sc.auth.rest.api.AuthApiService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/3/29 0029 下午 3:41
 * @Version: 1.0
 **/
@FeignClient(name = "j4sc-auth-server",fallback = J4scAuthClientHystrix.class,url="auth")
public interface J4scAuthClient extends AuthApiService{
}
