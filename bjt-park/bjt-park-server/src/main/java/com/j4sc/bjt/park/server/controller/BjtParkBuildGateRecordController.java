package com.j4sc.bjt.park.server.controller;

import com.j4sc.bjt.park.dao.entity.BjtParkBuildGateRecord;
import com.j4sc.bjt.park.rest.api.BjtParkBuildGateRecordService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import io.swagger.annotations.ApiModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/18 13:44
 * @Version: 1.0
 **/
@RestController
@ApiModel
@RequestMapping("BjtParkBuildGateRecord")
public class BjtParkBuildGateRecordController extends BaseController<BjtParkBuildGateRecord, BjtParkBuildGateRecordService> implements BaseApiService<BjtParkBuildGateRecord> {

}
