package com.j4sc.bjt.api.server.client;

import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyUser;
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
 * @CreateDate: 2018/4/9 0029 12:33
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-park-server",path = "/BjtParkCompanyUser")
public interface ParkCompanyUserClient extends BaseApiService<BjtParkCompanyUser> {
    @RequestMapping(value = "save/CompanyAdminUser", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult saveCompanyAdminUser(@RequestParam("userId") String userId, @RequestBody List<String> userIdList);

    @RequestMapping(value = "delete/CompanyAdminUser", method = RequestMethod.DELETE,consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult deleteCompanyAdminUser(@RequestParam("userId") String userId, @RequestBody List<String> userIdList);

    @RequestMapping(value = "delete/CompanyUser", method = RequestMethod.DELETE,consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult deleteCompanyUser(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "delete/CompanyUserSelf", method = RequestMethod.DELETE)
    BaseResult deleteCompanyUserSelf(@RequestParam("userId")String userId);

    @RequestMapping(value = "select/PageCompanyUser", method = RequestMethod.GET)
    BaseResult<Page<BjtParkCompanyUser>> selectPageCompanyUser(@RequestParam Map<String, Object> params);

    @RequestMapping(value = "save/CompanyUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult saveCompanyUser(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "select/CountCompanyUser", method = RequestMethod.GET)
    BaseResult selectCountCompanyUser(@RequestParam("userId")String userId);

    @RequestMapping(value = "select/CompanyUserList", method = RequestMethod.GET)
    BaseResult<List<BjtParkCompanyUser>> selectCompanyUserList(@RequestParam("userId")String userId);

    @RequestMapping(value = "save/CompanyUserByAdmin", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult saveCompanyUserByAdmin(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "update/CompanyUser", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult updateCompanyUser(@RequestBody BjtParkCompanyUser bjtParkCompanyUser, @RequestParam("userId") String userId);

    @RequestMapping(value = "update/CompanyAdminUser", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult updateCompanyAdminUser(@RequestBody Map<String, Object> params);
}
