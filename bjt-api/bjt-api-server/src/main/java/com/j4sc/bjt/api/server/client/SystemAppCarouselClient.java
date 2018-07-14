package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.system.dao.entity.BjtSystemAppCarousel;
import com.j4sc.common.base.BaseApiService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/18 15:59
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-system-server",path = "/BjtSystemAppCarousel")
public interface SystemAppCarouselClient extends BaseApiService<BjtSystemAppCarousel>{
}
