package com.j4sc.bjt.system.server.controller;

import com.j4sc.bjt.system.dao.entity.BjtSystemDownload;
import com.j4sc.bjt.system.rest.api.BjtSystemDownloadService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/15 10:45
 * @Version: 1.0
 **/
@RestController
@RequestMapping("BjtSystemDownload")
@Api(tags = {"下载链接管理"}, description = "下载链接管理")
public class BjtSystemDownloadController extends BaseController<BjtSystemDownload,BjtSystemDownloadService> implements BaseApiService<BjtSystemDownload> {
}
