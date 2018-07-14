package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.carpark.dao.entity.BjtCarparkPayment;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/11 14:31
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-carpark-server", path = "/BjtCarparkPayment")
public interface CarparkPaymentClient extends BaseApiService<BjtCarparkPayment>{
    @RequestMapping(value = "select/AndValidation", method = RequestMethod.GET)
    BaseResult selectAndValidation(@RequestParam Map<String, Object> params);

    @RequestMapping(value = "save/CarparkPayment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult saveCarparkPayment(@RequestBody BjtCarparkPayment carparkPayment);

    @RequestMapping(value = "update/CarparkPayment", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult updateCarparkPayment(@RequestBody Map<String,Object> params);

    @RequestMapping(value = "select/CountPayment", method = RequestMethod.GET)
    BaseResult<Map<String, Object>> selectCountPayment(@RequestParam("spaceId") String spaceId);
}
