package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.park.dao.entity.BjtParkCompanyClock;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/14 9:29
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-park-server",path = "/BjtParkCompanyClock")
public interface ParkCompanyClockClient extends BaseApiService<BjtParkCompanyClock>{

    @RequestMapping(value = "select/CompanyClockAllList", method = RequestMethod.GET)
    BaseResult<List<BjtParkCompanyClock>> selectCompanyClockAllList(@RequestParam Map<String, Object> params);

    @RequestMapping(value = "select/CompanyClockList", method = RequestMethod.GET)
    BaseResult<List<BjtParkCompanyClock>> selectCompanyClockList(@RequestParam Map<String, Object> params);
}
