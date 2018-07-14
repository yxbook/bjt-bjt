package com.j4sc.bjt.api.server.client;

import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkApply;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGate;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/8 16:59
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-carpark-server", path = "/BjtCarparkApply")
public interface CarparkApplyClient extends BaseApiService<BjtCarparkApply>{

    @RequestMapping(value = "save/CarparkApply", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult saveCarparkApply(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "update/CarparkApply", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult updateCarparkApply(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "select/CarparkApplyPage", method = RequestMethod.GET)
    BaseResult<Page<BjtCarparkApply>> selectCarparkApplyPage(@RequestParam Map<String, Object> params);
}
