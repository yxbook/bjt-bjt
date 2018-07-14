package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.user.dao.entity.BjtUserRecharge;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/10 15:42
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-user-server",path = "/BjtUserRecharge")
public interface UserRechargeClient extends BaseApiService<BjtUserRecharge>{

    @RequestMapping(value = "save/Recharge", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult saveRecharge(@RequestBody BjtUserRecharge bjtUserRecharge);

}
