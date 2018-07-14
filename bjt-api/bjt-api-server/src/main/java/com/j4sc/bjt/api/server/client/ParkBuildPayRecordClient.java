package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.park.dao.entity.BjtParkBuildPayRecord;
import com.j4sc.common.base.BaseApiService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/18 17:54
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-park-server",path = "/BjtParkBuildPayRecord")
public interface ParkBuildPayRecordClient extends BaseApiService<BjtParkBuildPayRecord> {
}
