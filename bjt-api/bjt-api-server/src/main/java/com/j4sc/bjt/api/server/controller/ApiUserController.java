package com.j4sc.bjt.api.server.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.*;

import com.j4sc.bjt.carpark.dao.entity.BjtCarparkSpaceUser;
import com.j4sc.bjt.park.dao.entity.BjtParkBuild;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildUser;
import com.j4sc.bjt.park.dao.entity.BjtParkCompany;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyUser;
import com.j4sc.bjt.user.dao.entity.*;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import com.j4sc.common.util.MapUtil;
import com.j4sc.common.validator.NotNullValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/2 14:09
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/user")
@Api(tags = {"帮家团用户服务"}, description = "帮家团用户服务 - 需授权")
public class ApiUserController extends BaseJwtController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUserController.class);
    @Autowired
    UserManageClient userManageClient;
    @Autowired
    ParkBuildUserClient parkBuildUserClient;
    @Autowired
    ParkBuildClient parkBuildClient;
    @Autowired
    ParkCompanyUserClient parkCompanyUserClient;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private CarparkSpaceUserClient carparkSpaceUserClient;
    @Autowired
    private ParkCompanyClient parkCompanyClient;


    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "getUserInfo", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> getUserInfo() {
        LOGGER.info("getUserInfo = >" + getUserId());
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(getUserId());
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", getUserId());
        BaseResult<List<BjtParkCompanyUser>> companyUserListResult = parkCompanyUserClient.selectAll(params);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("userInfo", userBaseResult.getData());
        if (companyUserListResult.getData().size() > 0) {
            BjtParkCompanyUser companyUser = companyUserListResult.getData().get(0);
            resultMap.put("companyUser", companyUser);
            BaseResult<BjtParkCompany> companyBaseResult = parkCompanyClient.selectById(companyUser.getCompanyId().toString());
            if (companyBaseResult.getData() != null && companyBaseResult.getData().getDoorPlate() != null && !"".equals(companyBaseResult.getData().getDoorPlate())) {
                resultMap.put("buildId", companyBaseResult.getData().getBuildId().toString());
                resultMap.put("buildName", companyBaseResult.getData().getBuildName().toString());
                BaseResult<BjtParkBuild> buildBaseResult = parkBuildClient.selectById(companyBaseResult.getData().getBuildId().toString());
                resultMap.put("park", buildBaseResult.getData().getPark());//园区
            } else {
                resultMap.put("buildId", null);
            }
        } else {
            resultMap.put("companyUser", null);
        }
        BaseResult<List<BjtParkBuildUser>> buildUserBaseResult = parkBuildUserClient.selectAll(params);
        if (buildUserBaseResult.getData().size() > 0 && buildUserBaseResult.status == BaseResultEnum.SUCCESS.getStatus()) {
            resultMap.put("buildUser", buildUserBaseResult.getData());
            if (resultMap.get("park") == null) {
                BaseResult<BjtParkBuild> buildBaseResult = parkBuildClient.selectById(buildUserBaseResult.getData().get(0).getBuildId().toString());
                resultMap.put("park", buildBaseResult.getData().getPark());//园区
            }
        } else {
            resultMap.put("buildUser", null);
        }
        BaseResult<List<BjtCarparkSpaceUser>> spaceUserBaseResult = carparkSpaceUserClient.selectAll(params);
        if (spaceUserBaseResult.getData().size() > 0 && spaceUserBaseResult.status == BaseResultEnum.SUCCESS.getStatus()) {
            resultMap.put("spaceUser", spaceUserBaseResult.getData());
        } else {
            resultMap.put("spaceUser", null);
        }
        BaseResult countCompanyUserBaseResult = parkCompanyUserClient.selectCountCompanyUser(getUserId());
        if (countCompanyUserBaseResult.getData() != null && countCompanyUserBaseResult.status == BaseResultEnum.SUCCESS.getStatus()){
            resultMap.put("count", countCompanyUserBaseResult.getData());
        } else {
            resultMap.put("count", 0);
        }

        return new BaseResult(BaseResultEnum.SUCCESS, resultMap);
    }

    @ApiOperation(value = "获取有管理功能的楼宇ID-(后台菜单生成，前端特殊功能展现判断)", notes = "获取有管理功能的楼宇ID-(后台菜单生成，前端特殊功能展现判断)")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "getBuilds", method = RequestMethod.GET)
    public BaseResult getBuilds() {
        LOGGER.info("getBuilds = >" + getUserId());
        Map<String, Object> params = new HashMap<>();
        params.put("user_id",getUserId());
        BaseResult<List<BjtParkBuildUser>> buildUserResult = parkBuildUserClient.selectAll(params);
        if (buildUserResult.getStatus() != BaseResultEnum.SUCCESS.getStatus())return buildUserResult;
        List<Integer> buildIdList = new ArrayList<>();
        buildUserResult.getData().forEach(v ->{
            buildIdList.add(v.getBuildId());
        });
        return parkBuildClient.getBuildListByIdList(buildIdList);
    }

    @ApiOperation(value = "获取用户权限-(后台菜单生成，前端特殊功能展现判断)", notes = "获取用户权限-(后台菜单生成，前端特殊功能展现判断)")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "getPermission", method = RequestMethod.GET)
    public BaseResult getPermission(@RequestParam("buildId") String buildId) {
        LOGGER.info("getPermission = >" + getUserId());
        return userManageClient.selectPermissionByUserId(getUserId(),buildId);
    }

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "updateUserInfo", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updataUserInfo(@RequestBody BjtUserUser bjtUserUser) {
        LOGGER.info("updataUserInfo = >" + getUserId());
        bjtUserUser.setUserId(getUserId());
        return userManageClient.updateUser(bjtUserUser);
    }

    @ApiOperation(value = "查看用户余额", notes = "查看用户余额")
    @RequestMapping(value = "findWallet", method = RequestMethod.GET)
    public BaseResult<BjtUserWallet> findWallet() {
        LOGGER.info("findWallet = >" + getUserId());
        return userManageClient.findWallet(getUserId());
    }

    @ApiOperation(value = "用户提交反馈", notes = "用户提交反馈")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "内容", name = "content", required = true)
    })
    @RequestMapping(value = "saveFeedBack", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveFeedBack(@RequestBody BjtUserFeedback userFeedback) {
        LOGGER.info("saveFeedBack = >" + getUserId());
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(userFeedback.getContent(), new NotNullValidator("内容")).doValidate().result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        userFeedback.setUserId(getUserId());
        userFeedback.setCtime(System.currentTimeMillis());
        return userManageClient.saveFeedBack(userFeedback);
    }

}
