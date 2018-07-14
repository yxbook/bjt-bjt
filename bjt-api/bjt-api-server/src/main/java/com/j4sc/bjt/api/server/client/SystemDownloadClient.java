package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.system.dao.entity.BjtSystemDownload;
import com.j4sc.common.base.BaseApiService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/15 11:16
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-system-server",path = "/BjtSystemDownload")
public interface SystemDownloadClient extends BaseApiService<BjtSystemDownload>{
}
