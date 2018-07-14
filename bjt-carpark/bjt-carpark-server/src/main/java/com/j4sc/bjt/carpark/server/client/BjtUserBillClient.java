package com.j4sc.bjt.carpark.server.client;

import com.j4sc.bjt.carpark.common.BjtUserBill;
import com.j4sc.common.base.BaseApiService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/14 20:30
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-user-server", path = "/BjtUserBill")
public interface BjtUserBillClient extends BaseApiService<BjtUserBill>{
}
