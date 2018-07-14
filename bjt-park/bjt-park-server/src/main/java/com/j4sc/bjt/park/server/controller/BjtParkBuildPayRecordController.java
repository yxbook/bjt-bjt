package com.j4sc.bjt.park.server.controller;

import com.j4sc.bjt.park.dao.entity.BjtParkBuildPayRecord;
import com.j4sc.bjt.park.rest.api.BjtParkBuildPayRecordService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import io.swagger.annotations.ApiModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/18 17:52
 * @Version: 1.0
 **/
@RestController
@ApiModel
@RequestMapping("/BjtParkBuildPayRecord")
public class BjtParkBuildPayRecordController extends BaseController<BjtParkBuildPayRecord, BjtParkBuildPayRecordService> implements BaseApiService<BjtParkBuildPayRecord>{
}
