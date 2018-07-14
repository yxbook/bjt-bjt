package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.system.dao.entity.BjtSystemLog;
import com.j4sc.common.base.BaseApiService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/4 9:56
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-system-server",path = "/BjtSystemLog")
public interface SystemLogClient extends BaseApiService<BjtSystemLog> {
}
