package com.j4sc.bjt.system.server.client;

import com.j4sc.auth.rest.api.AuthApiService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/3/29 16:38
 * @Version: 1.0
 **/
@FeignClient(value = "j4sc-auth-server",path = "/api")
public interface AuthApiClient extends AuthApiService{
}