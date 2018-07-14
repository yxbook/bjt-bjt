package com.j4sc.bjt.carpark.server.client;

import com.j4sc.bjt.carpark.common.PayOrder;
import com.j4sc.common.base.BaseApiService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/2 10:20
 * @Version: 1.0
 **/
@FeignClient(value = "j4sc-pay-server",path = "/PayOrder")
public interface PayOrderClient extends BaseApiService<PayOrder>{
}
