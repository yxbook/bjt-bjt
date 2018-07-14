package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.user.dao.entity.BjtUserUser;
import com.j4sc.bjt.user.dao.entity.BjtUserUserRole;
import com.j4sc.common.base.BaseApiService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/13 12:06
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-user-server",path = "/BjtUserUser")
public interface UserUserClient extends BaseApiService<BjtUserUser>{
}
