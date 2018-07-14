package com.j4sc.bjt.api.server.client;

import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.user.dao.entity.BjtUserBill;
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
 * @Description: 用户账单
 * @Author: chengyz
 * @CreateDate: 2018/4/10 14:36
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-user-server",path = "/BjtUserBill")
public interface UserBillClient extends BaseApiService<BjtUserBill>{
    @RequestMapping(value = "select/BjtUserBill", method = RequestMethod.GET)
    BaseResult<BjtUserBill> selectBjtUserBill(@RequestParam("userId")String userId, @RequestParam("billId")int billId);

    @RequestMapping(value = "select/PageBill", method = RequestMethod.GET)
    BaseResult<Page<BjtUserBill>> selectPageBill(@RequestParam Map<String, Object> params);

    @RequestMapping(value = "update/BjtUserBill", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult updateBjtUserBill(@RequestBody Map<String, Object> params);
}
