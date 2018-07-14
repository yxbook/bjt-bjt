package com.j4sc.bjt.api.server.client;

import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyLeave;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/14 9:36
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-park-server", path = "BjtParkCompanyLeave")
public interface ParkCompanyLeaveClient extends BaseApiService<BjtParkCompanyLeave>{

    @RequestMapping(value = "save/CompanyLeave", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult saveCompanyLeave(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "update/CompanyLeave", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult updateCompanyLeave(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "select/CompanyLeavePage", method = RequestMethod.GET)
    BaseResult<Page<BjtParkCompanyLeave>> selectCompanyLeavePage(@RequestParam Map<String, Object> params);
}
