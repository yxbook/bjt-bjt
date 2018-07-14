package com.j4sc.bjt.park.server.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.park.dao.entity.*;
import com.j4sc.bjt.park.rest.api.*;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018 2018/4/9 11:30
 * @Version: 1.0
 **/
@RestController
@ApiModel
@RequestMapping("BjtParkBuildGuard")
public class BjtParkBuildGuardController extends BaseController<BjtParkBuildGuard, BjtParkBuildGuardService> implements BaseApiService<BjtParkBuildGuard> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildGuardController.class);

    @Autowired
    private BjtParkBuildUserService parkBuildUserService;
    @Autowired
    private BjtParkCompanyUserService parkCompanyUserService;
    @Autowired
    private BjtParkBuildGuardService parkBuildGuardService;


    @ApiOperation(value = "获取门禁申请记录明细")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "select/GuardList", method = RequestMethod.GET)
    public BaseResult<List<BjtParkBuildGuard>> selectGuardList(@RequestParam Map<String, Object> params) {
        LOGGER.info("获取门禁申请记录明细 = > selectGuardList");
        List<BjtParkBuildGuard> list = new ArrayList<>();
        //查询当前用户是否是楼宇管理员
        BjtParkBuildUser _bjtParkBuildUser = parkBuildUserService.selectOne(new EntityWrapper<BjtParkBuildUser>().eq("user_id", params.get("userId")).eq("build_id", params.get("buildId")));
        if (_bjtParkBuildUser != null) {
            Map map = new HashMap<>();
            map.put("build_id", params.get("buildId"));
            map.put("guard_main_id", params.get("guardMainId"));
            list = parkBuildGuardService.selectByMap(map);
            return new BaseResult(BaseResultEnum.SUCCESS, list);
        }
        //查询当前用户是否是公司管理员
        BjtParkCompanyUser _bjtParkCompanyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", params.get("userId")).eq("type", 3));
        if (_bjtParkCompanyUser != null) {
            Map map = new HashMap<>();
            map.put("apply_user_id", params.get("userId"));
            map.put("guard_main_id", params.get("guardMainId"));
            list = parkBuildGuardService.selectByMap(map);
            return new BaseResult(BaseResultEnum.SUCCESS, list);
        }
        return new BaseResult(BaseResultEnum.SUCCESS, list);
    }

    @ApiOperation(value = "通过用户ID删除用户门禁权限")
    @RequestMapping(value = "delete/Guard", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult deleteGuard(@RequestBody List<String> userIdList) {
        if (parkBuildGuardService.deleteBatchIds(userIdList)) return new BaseResult(BaseResultEnum.SUCCESS, "删除成功");
        return new BaseResult(BaseResultEnum.SUCCESS, "删除失败");
    }

    @ApiOperation(value = "通过用户ID查询用户门禁权限")
    @RequestMapping(value = "select/Guard", method = RequestMethod.GET)
    public BaseResult<BjtParkBuildGuard> selectGuard(@RequestParam("userId")String userId) {
        LOGGER.info("通过用户ID查询用户门禁权限");//权限type  1.正常 0.申请中 -1.禁止
        BjtParkBuildGuard guard = parkBuildGuardService.selectOne(new EntityWrapper<BjtParkBuildGuard>().eq("user_id", userId).eq("type", 1));
        if (guard == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前用户无门禁权限");
        }
        return new BaseResult(BaseResultEnum.SUCCESS, guard);
    }

    @ApiOperation(value = "通过公司编号删除用户门禁权限")
    @RequestMapping(value = "delete/GuardList", method = RequestMethod.DELETE)
    public BaseResult deleteGuardList(@RequestParam("companyId") String companyId) {
        List<BjtParkCompanyUser> companyUserList = parkCompanyUserService.selectList(new EntityWrapper<BjtParkCompanyUser>().eq("company_id", companyId));
        List<String> userIdList = new ArrayList<>();
        List<Integer> guardIdList = new ArrayList<>();
        if (companyUserList == null || companyUserList.size() < 1)
            return new BaseResult(BaseResultEnum.SUCCESS, "删除成功");
        companyUserList.forEach(v -> {
            userIdList.add(v.getUserId());
        });
        userIdList.add("-1");
        List<BjtParkBuildGuard> buildGuardList = parkBuildGuardService.selectList(new EntityWrapper<BjtParkBuildGuard>().in("user_id", userIdList));
        if (buildGuardList == null || buildGuardList.size() < 1) return new BaseResult(BaseResultEnum.SUCCESS, "删除成功");
        buildGuardList.forEach(v -> {
            guardIdList.add(v.getGuardId());
        });
        if (parkBuildGuardService.deleteBatchIds(guardIdList)) return new BaseResult(BaseResultEnum.SUCCESS, "删除成功");
        return new BaseResult(BaseResultEnum.SUCCESS, "删除失败");
    }


}
