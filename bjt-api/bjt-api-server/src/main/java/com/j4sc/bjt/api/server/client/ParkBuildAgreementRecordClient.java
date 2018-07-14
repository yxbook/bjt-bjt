package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.park.dao.entity.BjtParkBuildAgreement;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildAgreementRecord;
import com.j4sc.common.base.BaseApiService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/4 18:15
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-park-server",path = "/ParkBuildAgreementRecord")
public interface ParkBuildAgreementRecordClient extends BaseApiService<BjtParkBuildAgreementRecord> {
}
