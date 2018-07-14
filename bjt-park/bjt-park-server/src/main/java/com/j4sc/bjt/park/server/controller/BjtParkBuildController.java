package com.j4sc.bjt.park.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.park.dao.entity.BjtParkBuild;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildUser;
import com.j4sc.bjt.park.rest.api.BjtParkBuildService;
import com.j4sc.bjt.park.rest.api.BjtParkBuildUserService;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/3 10:35
 * @Version: 1.0
 **/
@RestController
@RequestMapping("BjtParkBuild")
public class BjtParkBuildController extends BaseController<BjtParkBuild, BjtParkBuildService> implements BaseApiService<BjtParkBuild> {
    @Autowired
    private BjtParkBuildUserService bjtParkBuildUserService;

    @RequestMapping("test")
    public String getT() {
        return service.getTest();
    }

    @RequestMapping("buildUserList")
    public BaseResult<List<BjtParkBuildUser>> selectBuildUserList(@RequestParam("buildId") int buildId) {
        List<BjtParkBuildUser> list = bjtParkBuildUserService.selectList(new EntityWrapper<BjtParkBuildUser>().eq("build_id", buildId));
        return new BaseResult(BaseResultEnum.SUCCESS, list);
    }

    @RequestMapping(value = "getBuildListByIdList", method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult<List<BjtParkBuild>> getBuildListByIdList(@RequestBody List<Integer> buildIdList){
        return service.getBuildListByIdList(buildIdList);
    }

}
