package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.user.dao.entity.BjtUserAddressList;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description: 用户通讯录
 * @Author: chengyz
 * @CreateDate: 2018/4/10 14:36
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-user-server",path = "api/addressList")
public interface UserAddressListClient extends BaseApiService<BjtUserAddressList>{

    @RequestMapping(value = "select/AllAddressList", method = RequestMethod.GET)
    BaseResult<List<BjtUserAddressList>> selectAllAddressList(@RequestParam("userId")String userId, @RequestParam("key")String key);
}
