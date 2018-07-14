package com.j4sc.bjt.api.server.client;

import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.park.dao.entity.BjtParkCompany;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import io.swagger.annotations.ApiOperation;
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
@FeignClient(value = "bjt-park-server", path = "/BjtParkCompany")
public interface ParkCompanyClient extends BaseApiService<BjtParkCompany> {

    @RequestMapping(value = "delete/Company", method = RequestMethod.DELETE)
    BaseResult deleteCompany(@RequestParam("companyId") String companyId, @RequestParam("userId") String userId);

    @RequestMapping(value = "save/CompanyAdmission", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult saveCompanyAdmission(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "save/Company", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult saveCompany(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "select/BuildUserInfo", method = RequestMethod.GET)
    BaseResult<Map<String, String>> selectBuildUserInfo(@RequestParam("userId") String userId);

    @RequestMapping(value = "select/Company", method = RequestMethod.GET)
    BaseResult<Map<String, Object>> selectCompany(@RequestParam("userId") String userId);

    @RequestMapping(value = "select/CompanyBuildSpaceId", method = RequestMethod.GET)
    BaseResult selectCompanyBuildSpaceId(@RequestParam("userId")String userId);

    @RequestMapping(value = "select/PageCompany", method = RequestMethod.GET)
    BaseResult<Page<BjtParkCompany>> selectPageCompany(@RequestParam Map<String, Object> params);

    @RequestMapping(value = "select/CompanyDetailInfo", method = RequestMethod.GET)
    BaseResult<Map<String,Object>> selectCompanyDetailInfo(@RequestParam("houseId")int houseId, @RequestParam("userId")String userId, @RequestParam("buildId")int buildId);
}
