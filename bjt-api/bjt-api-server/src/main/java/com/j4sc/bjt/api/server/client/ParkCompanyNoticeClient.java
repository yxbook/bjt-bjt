package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.park.dao.entity.BjtParkCompanyNotice;
import com.j4sc.common.base.BaseApiService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "bjt-park-server",path = "/BjtParkCompanyNotice")
public interface ParkCompanyNoticeClient extends BaseApiService<BjtParkCompanyNotice> {
}
