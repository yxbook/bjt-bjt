package com.j4sc.bjt.user.server.client;


import com.j4sc.bjt.user.common.BjtParkBuildAgreement;
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
 * @CreateDate: 2018/5/4 18:15
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-park-server",path = "/ParkBuildAgreement")
public interface ParkBuildAgreementClient extends BaseApiService<BjtParkBuildAgreement> {

    @RequestMapping(path = "update/StopAgreement", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult updateStopAgreement(@RequestBody Map<String, Object> params);
}
