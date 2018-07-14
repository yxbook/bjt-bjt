package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.park.dao.entity.BjtParkBuildGate;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGateRecord;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/2 16:59
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-park-server", path = "/BjtParkBuildGateRecord")
public interface ParkBuildGateRecordClient extends BaseApiService<BjtParkBuildGateRecord>{
}
