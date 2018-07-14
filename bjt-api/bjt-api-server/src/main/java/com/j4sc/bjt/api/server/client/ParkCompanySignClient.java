package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.park.dao.entity.BjtParkCompanySign;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/14 9:34
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-park-server", path = "/BjtParkCompanySign")
public interface ParkCompanySignClient extends BaseApiService<BjtParkCompanySign>{

    @RequestMapping(value = "select/CompanySignAllList", method = RequestMethod.GET)
    BaseResult<List<BjtParkCompanySign>> selectCompanySignAllList(@RequestParam Map<String, Object> params);

    @RequestMapping(value = "select/CompanySignList", method = RequestMethod.GET)
    BaseResult<List<BjtParkCompanySign>> selectCompanySignList(@RequestParam Map<String, Object> params);
}
