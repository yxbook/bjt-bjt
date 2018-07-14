package com.j4sc.bjt.park.rest.api;

import com.j4sc.bjt.park.dao.entity.BjtParkBuildUser;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseService;
import com.j4sc.bjt.park.dao.entity.BjtParkBuild;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
* @Description: BjtParkBuild Service接口
* @Author: LongRou
* @CreateDate: 2018/4/3.
* @Version: 1.0
**/
public interface BjtParkBuildService extends BaseService<BjtParkBuild> {
    String getTest();

    @RequestMapping(value = "getBuildListByIdList", method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
    BaseResult<List<BjtParkBuild>> getBuildListByIdList(@RequestBody List<Integer> buildIdList);
}