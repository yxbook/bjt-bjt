package com.j4sc.bjt.api.server.client;

import com.j4sc.bjt.carpark.dao.entity.BjtCarparkApply;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkApplyDetail;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/8 16:59
 * @Version: 1.0
 **/
@FeignClient(value = "bjt-carpark-server", path = "/BjtCarparkApplyDetail")
public interface CarparkApplyDetailClient extends BaseApiService<BjtCarparkApplyDetail>{

    @RequestMapping(value = "select/CarparkApplyDetail", method = RequestMethod.GET)
    BaseResult selectCarparkApplyDetail(@RequestParam("userId")String userId, @RequestParam("carId")int carId, @RequestParam("spaceId")int spaceId);

    @RequestMapping(value = "update/CarparkApplyDetail", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult updateCarparkApply(@RequestBody Map<String, Object> params);

    @RequestMapping(value = "select/ApplyDetailList", method = RequestMethod.GET)
    BaseResult<List<BjtCarparkApplyDetail>> selectApplyDetailList(@RequestParam("userId")String userId);

    @RequestMapping(value = "select/ListByUserIdList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult<List<BjtCarparkApplyDetail>> selectListByUserIdList(@RequestBody List<String> userIdList);

    @RequestMapping(value = "delete/CarparkDetailList", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult deleteCarparkDetailList(@RequestParam("userId") String userId, @RequestBody List<String> list);

    @RequestMapping(value = "delete/DetailList", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult deleteDetailList(@RequestParam("spaceId") String spaceId, @RequestBody List<String> list);

}
