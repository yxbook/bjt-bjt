package com.j4sc.bjt.api.server.client;

import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkSpace;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/9 18:03
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-carpark-server", path = "/BjtCarparkSpace")
public interface CarparkSpaceClient extends BaseApiService<BjtCarparkSpace> {

    @RequestMapping(value = "select/CarparkSpacePage", method = RequestMethod.GET)
    BaseResult<Map<String, Object>> selectCarparkSpacePage(@RequestParam Map<String, Object> params);

    @RequestMapping(value = "select/CarparkSpacePageAdmin", method = RequestMethod.GET)
    BaseResult<Page<BjtCarparkSpace>> selectCarparkSpacePageAdmin(@RequestParam Map<String, Object> params);
}
