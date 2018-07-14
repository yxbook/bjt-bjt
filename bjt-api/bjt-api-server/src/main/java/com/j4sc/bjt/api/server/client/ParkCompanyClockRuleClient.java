package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.park.dao.entity.BjtParkCompanyClock;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyClockRule;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/14 9:31
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-park-server", path = "api/companyClockRule")
public interface ParkCompanyClockRuleClient extends BaseApiService<BjtParkCompanyClockRule>{

    @RequestMapping(value = "save/CompanyClockRule", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult saveCompanyClockRule(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "delete/CompanyClockRule", method = RequestMethod.DELETE)
    BaseResult deleteCompanyClockRule(@RequestParam("userId")String userId);

    @RequestMapping(value = "select/CompanyClockRule", method = RequestMethod.GET)
    BaseResult<BjtParkCompanyClockRule> selectCompanyClockRule(@RequestParam("userId")String userId);

}
