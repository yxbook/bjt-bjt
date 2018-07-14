package com.j4sc.bjt.system.server.controller;

import com.j4sc.bjt.system.dao.entity.BjtSystemLog;
import com.j4sc.bjt.system.rest.api.BjtSystemLogService;
import com.j4sc.bjt.system.server.client.AuthLogClient;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/4 9:54
 * @Version: 1.0
 **/
@RestController
@RequestMapping("BjtSystemLog")
@Api(tags = {"日志管理"}, description = "日志管理")
public class BjtSystemLogController extends BaseController<BjtSystemLog,BjtSystemLogService> implements BaseApiService<BjtSystemLog> {
    @Autowired
    AuthLogClient authLogClient;

    @RequestMapping("get")
    public BaseResult get(@RequestParam Map<String, Object> params){
        return authLogClient.selectPage(params);
    }
}
