package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.park.dao.entity.BjtParkCompanyAdmission;
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
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/4 14:54
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-park-server",path = "/BjtParkCompanyAdmission")
public interface ParkCompanyAdmissionClient extends BaseApiService<BjtParkCompanyAdmission> {
    @RequestMapping(value = "select/CompanyAdmission", method = RequestMethod.GET)
    BaseResult selectCompanyAdmission(@RequestParam("userId")String userId);

    @RequestMapping(value = "update/CompanyAdmission", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult updateCompanyAdmission(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "delete/CompanyAdmission", method = RequestMethod.DELETE)
    BaseResult deleteAdmission(@RequestParam("companyId")String companyId, @RequestParam("buildId")String buildId);
}
