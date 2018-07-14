package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.system.dao.entity.BjtSystemNotice;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/4 9:56
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-system-server",path = "/BjtSystemNotice")
public interface SystemNoticeClient extends BaseApiService<BjtSystemNotice> {
    @RequestMapping(value = "select/SysNoticeAll", method = RequestMethod.GET)
    BaseResult<List<BjtSystemNotice>> selectSysNoticeAll();
}
