package com.j4sc.bjt.park.server.client;

import com.j4sc.bjt.park.common.util.BjtUserBill;
import com.j4sc.common.base.BaseApiService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description: 用户账单
 * @Author: chengyz
 * @CreateDate: 2018/4/10 14:36
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-user-server",path = "/BjtUserBill")
public interface UserBillClient extends BaseApiService<BjtUserBill> {
}
