package com.j4sc.bjt.park.server.controller;

import com.j4sc.bjt.park.dao.entity.BjtParkCompanyProgress;
import com.j4sc.bjt.park.rest.api.BjtParkCompanyProgressService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import io.swagger.annotations.ApiModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 请假、外出、加班申请审批进度controller
 * @Author: chengyz
 * @CreateDate: 2018/4/14 9:10
 * @Version: 1.0
 **/
@RestController
@ApiModel
@RequestMapping("BjtParkCompanyProgress")
public class BjtParkCompanyProgressController extends BaseController<BjtParkCompanyProgress, BjtParkCompanyProgressService> implements BaseApiService<BjtParkCompanyProgress> {
}
