package com.j4sc.bjt.park.server.controller;

import com.j4sc.bjt.park.dao.entity.BjtParkBuildAgreement;
import com.j4sc.bjt.park.rest.api.BjtParkBuildAgreementService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import io.swagger.annotations.ApiModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/11 12:21
 * @Version: 1.0
 **/
@RestController
@ApiModel
@RequestMapping("ParkBuildAgreementRecord")
public class BjtParkBuildAgreementRecordController extends BaseController<BjtParkBuildAgreement, BjtParkBuildAgreementService> implements BaseApiService<BjtParkBuildAgreement> {
}
