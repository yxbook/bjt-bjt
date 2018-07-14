package com.j4sc.bjt.api.server.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.ParkBuildGuardClient;
import com.j4sc.bjt.api.server.client.ParkBuildGuardMainClient;
import com.j4sc.bjt.api.server.client.UserManageClient;
import com.j4sc.bjt.park.dao.entity.BjtParkBuild;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuard;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuardMain;
import com.j4sc.bjt.user.dao.entity.BjtUserUser;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import com.j4sc.common.validator.NotNullValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 门禁申请
 * @Author: chengyz
 * @CreateDate: 2018 2018/4/9 09:49
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/guard")
@Api(tags = {"帮家团门禁申请服务"}, description = "帮家团门禁申请服务 - 需授权")
public class ApiBuildGuardController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiBuildGuardController.class);

    @Autowired
    ParkBuildGuardClient parkBuildGuardClient;
    @Autowired
    UserManageClient userManageClient;
    @Autowired
    ParkBuildGuardMainClient parkBuildGuardMainClient;


    @ApiOperation(value = "公司管理员提交门禁申请")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户编号列表")
    })
    @RequestMapping(value = "save/GuardMain", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveGuardMain(@RequestBody List<String> userIdList) {
        LOGGER.info("提交门禁申请 = >saveGuard");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(userIdList.toString(), new NotNullValidator("用户编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(getUserId());
        if (userBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus() || userBaseResult.getData() == null) {
            return userBaseResult;
        }
        BaseResult<List<BjtUserUser>> userListBaseResult = userManageClient.findListByIdList(userIdList);//查询用户列表
        if (userListBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus() || userListBaseResult.getData() == null) {
            return userListBaseResult;
        }
        List<BjtUserUser> userList = userListBaseResult.getData();
        if (userList.size() < 1) {
            return new BaseResult(BaseResultEnum.ERROR, "提交门禁申请失败");
        }
        //检测提交的用户中有没有状态不正常的用户
        String locked = userList.stream().filter(x -> x.getLocked() != 0)
                .map(BjtUserUser::getRealname)
                .findAny().orElse("1");
        if (!"1".equals(locked)) {
            return new BaseResult(BaseResultEnum.ERROR, "有状态不正常的用户");
        }
        //检测提交的用户中有没有真实姓名为空的用户
        String realname = userList.stream().filter(x -> "".equals(x.getRealname()) || x.getRealname() == null)
                .map(BjtUserUser::getRealname)
                .findAny().orElse("1");
        if (!"1".equals(realname)) {
            return new BaseResult(BaseResultEnum.ERROR, "有姓名为空的用户");
        }
        //检测提交的用户中有没有身份证号为空的用户
        String idNumber = userList.stream().filter(x -> "".equals(x.getIdNumber()) || x.getIdNumber() == null)
                .map(BjtUserUser::getIdNumber)
                .findAny().orElse("1");
        if (!"1".equals(idNumber)) {
            return new BaseResult(BaseResultEnum.ERROR, "有身份证号为空的用户");
        }

        //封装公司管理员信息
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userBaseResult.getData().getUserId());
        params.put("username", userBaseResult.getData().getUsername());
        params.put("realname", userBaseResult.getData().getRealname());

        List<String> idList = new ArrayList<>();

        userList.forEach(v -> {
            idList.add(v.getUserId());
        });
        params.put("idList", idList);
        return parkBuildGuardMainClient.saveGuardMain(params);
    }

    @ApiOperation(value = "获取门禁申请记录", notes = "获取门禁申请记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页显示记录数", dataType = "int")
    })
    @RequestMapping(value = "select/GuardMainPage", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkBuildGuardMain>> selectGuardMainPage(
            @RequestParam(name = "page", defaultValue = "1")int page,
            @RequestParam(name = "size", defaultValue = "20")int size) {
        LOGGER.info("获取门禁申请记录 = > selectGuardMainPage" + "用户ID：" + getUserId() + " 楼宇ID：" + getBuildId());
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("size", size);
        params.put("userId", getUserId());
        return parkBuildGuardMainClient.selectGuardMainPage(params);
    }

    @ApiOperation(value = "获取门禁申请明细", notes = "获取门禁申请明细")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "申请记录编号(公司管理员查询时必需)", name = "guardMainId")
    })
    @RequestMapping(value = "select/GuardList", method = RequestMethod.GET)
    public BaseResult<List<BjtParkBuildGuard>> selectGuardList(@RequestParam Map<String, Object> params) {
        LOGGER.info("获取门禁申请记录 = > selectGuardList" + "用户ID：" + getUserId() + " 楼宇ID：" + getBuildId());
        params.put("userId", getUserId());
        params.put("buildId", getBuildId());
        return parkBuildGuardClient.selectGuardList(params);
    }
    //权限  1.正常 0.申请中 -1.禁止
    @ApiOperation(value = "获取用户个人门禁权限", notes = "获取用户个人门禁权限")
    @RequestMapping(value = "select/BuildGuard", method = RequestMethod.GET)
    public BaseResult<BjtParkBuildGuard> selectBuildGuard() {
        LOGGER.info("获取用户个人门禁权限 = > selectBuildGuard");
       return parkBuildGuardClient.selectGuard(getUserId());
    }

}
