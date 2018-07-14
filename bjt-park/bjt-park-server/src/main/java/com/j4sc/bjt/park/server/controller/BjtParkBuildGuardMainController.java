package com.j4sc.bjt.park.server.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.park.dao.entity.*;
import com.j4sc.bjt.park.rest.api.*;
import com.j4sc.bjt.park.server.client.SystemApiClient;
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
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018 2018/4/9 11:30
 * @Version: 1.0
 **/
@RestController
@ApiModel
@RequestMapping("BjtParkBuildGuardMain")
public class BjtParkBuildGuardMainController extends BaseController<BjtParkBuildGuardMain, BjtParkBuildGuardMainService> implements BaseApiService<BjtParkBuildGuardMain> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildGuardMainController.class);

    @Autowired
    private BjtParkBuildUserService parkBuildUserService;
    @Autowired
    private BjtParkBuildService parkBuildService;
    @Autowired
    private BjtParkCompanyUserService parkCompanyUserService;
    @Autowired
    private BjtParkBuildGuardService parkBuildGuardService;
    @Autowired
    private BjtParkCompanyService parkCompanyService;
    @Autowired
    private BjtParkBuildGuardMainService parkBuildGuardMainService;
    @Autowired
    private BjtParkCompanyAdmissionService parkCompanyAdmissionService;
    @Autowired
    private SystemApiClient systemApiClient;

    @ApiOperation(value = "公司管理员提交门禁申请")
    @Transactional
    @RequestMapping(value = "save/GuardMain", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveGuardMain(@RequestBody Map<String, Object> params) {
        LOGGER.info("公司管理员提交门禁申请 = >saveGuard");
        BjtParkCompanyUser companyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", params.get("userId")));
        if (companyUser == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前用户不存在");
        }
        if (companyUser.getType() != 3) {
            return new BaseResult(BaseResultEnum.ERROR, "当前用户无权限");
        }
        BjtParkCompany parkCompany = parkCompanyService.selectById(companyUser.getCompanyId());//查询公司信息
        if (parkCompany == null) {
            return new BaseResult(BaseResultEnum.ERROR, "公司信息不存在");
        }
        if ("".equals(parkCompany.getDoorPlate()) || parkCompany.getDoorPlate() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "公司未入驻楼宇");
        }
        BjtParkCompanyAdmission companyAdmission = parkCompanyAdmissionService.selectOne(new EntityWrapper<BjtParkCompanyAdmission>().eq("company_id", parkCompany.getCompanyId()).eq("build_id", parkCompany.getBuildId()));
        if (companyAdmission == null || companyAdmission.getStatus() != 2) {
            return new BaseResult(BaseResultEnum.ERROR, "公司未入驻楼宇");
        }
        List<String> userIdList = (List<String>) params.get("idList");
        userIdList.add("-1");
        List<BjtParkCompanyUser> companyUserList = parkCompanyUserService.selectList(new EntityWrapper<BjtParkCompanyUser>().in("user_id", userIdList));
        if (companyUserList == null || companyUserList.size() < 1) {
            return new BaseResult(BaseResultEnum.ERROR, "提交门禁申请失败");
        }
        //检测是否在当前楼宇已经有门禁申请明细
        List<BjtParkBuildGuard> buildGuardList = parkBuildGuardService.selectList(new EntityWrapper<BjtParkBuildGuard>().in("user_id", userIdList).eq("build_id", companyAdmission.getBuildId()));
        if (buildGuardList != null && buildGuardList.size() > 0) {
            return new BaseResult(BaseResultEnum.ERROR, "不能重复提交申请");
        }
        BjtParkBuildGuardMain guardMain = this.constructGuardMain(params, parkCompany);//构造门禁申请并提交
        if (guardMain == null) {
            return new BaseResult(BaseResultEnum.ERROR, "门禁申请提交失败");
        }
        List<BjtParkBuildGuard> guardList = new ArrayList<>();
        companyUserList.forEach(v -> {
            BjtParkBuildGuard guard = this.constructGuard(v, parkCompany, guardMain.getGuardMainId(), guardMain.getEndTime(), params);//构造门禁申请明细
            guardList.add(guard);
        });

        if (!parkBuildGuardService.insertBatch(guardList)) {
            return new BaseResult(BaseResultEnum.ERROR, "门禁申请提交失败");
        }
        String detail = JSON.toJSONString(guardList);
        guardMain.setDetail(detail);//设置门禁申请明细
        if (!parkBuildGuardMainService.updateById(guardMain)) {
            return new BaseResult(BaseResultEnum.ERROR, "门禁申请提交失败");
        }
        List<BjtParkBuildUser> buildUserList = parkBuildUserService.selectList(new EntityWrapper<BjtParkBuildUser>().eq("build_id", guardMain.getBuildId()));
        List<String> usernameList = new ArrayList<>();
        buildUserList.forEach(v -> {
            usernameList.add(v.getUsername());
        });
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("title", "门禁申请");
        paramsMap.put("body", "门禁申请待审批");
        paramsMap.put("target", "BuildGuard");
        paramsMap.put("id", guardMain.getGuardMainId() + "");
        paramsMap.put("phoneList", usernameList);
        systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
        return new BaseResult(BaseResultEnum.SUCCESS, "门禁申请提交成功");
    }

    //类型:1、待审批，2、已通过，3、未通过
    @ApiOperation(value = "门禁申请审批", notes = "操作类型type：1、拒绝；2、同意")
    @RequestMapping(value = "update/GuardMain", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public BaseResult updateGuardMain(@RequestBody Map<String, Object> params) {
        LOGGER.info("门禁申请审批 = >updateGuard");
        BjtParkBuild _bjtParkBuild = parkBuildService.selectOne(new EntityWrapper<BjtParkBuild>().eq("build_id", params.get("buildId")));
        if (_bjtParkBuild == null) return new BaseResult(BaseResultEnum.ERROR, "楼宇不存在");

        BjtParkBuildUser _bjtParkBuildUser = parkBuildUserService.selectOne(new EntityWrapper<BjtParkBuildUser>().eq("user_id", params.get("approvalUserId")));
        if (_bjtParkBuildUser == null) return new BaseResult(BaseResultEnum.ERROR, "当前用户不是楼宇管理者");

        BjtParkBuildGuardMain parkBuildGuardMain = parkBuildGuardMainService.selectOne(new EntityWrapper<BjtParkBuildGuardMain>().eq("guard_main_id", params.get("guardMainId")));
        if (parkBuildGuardMain == null) return new BaseResult(BaseResultEnum.ERROR, "门禁申请不存在");
        if ("1".equals(params.get("type").toString()) || "2".equals(params.get("type").toString())) {
            parkBuildGuardMain.setApprovalUserRealname(params.get("approvalRealname") + "");
            parkBuildGuardMain.setApprovalUsername(params.get("approvalUsername") + "");
            parkBuildGuardMain.setApprovalUserId(params.get("approvalUserId") + "");
            parkBuildGuardMain.setApprovalTime(System.currentTimeMillis());
        }
        //操作类型type：1、拒绝；2、同意
        if ("1".equals(params.get("type").toString()) && parkBuildGuardMain.getType() == 1) {
            parkBuildGuardMain.setType(3);
            if (params.get("opinion") == null || "".equals(params.get("opinion"))) {
                return new BaseResult(BaseResultEnum.ERROR, "审批意见为空");
            }
            parkBuildGuardMain.setOpinion(params.get("opinion") + "");
            List<BjtParkBuildGuard> buildGuardList = parkBuildGuardService.selectList(new EntityWrapper<BjtParkBuildGuard>().eq("guard_main_id", parkBuildGuardMain.getGuardMainId()));
            if (buildGuardList == null || buildGuardList.size() < 1) {
                return new BaseResult(BaseResultEnum.ERROR, "无门禁申请明细");
            }
            parkBuildGuardMain.setDetail(JSON.toJSONString(buildGuardList));
            if (!parkBuildGuardMainService.updateById(parkBuildGuardMain)) {
                return new BaseResult(BaseResultEnum.ERROR, "门禁申请审批失败");
            }
            List<Integer> guardIdList = new ArrayList<>();
            buildGuardList.forEach(v -> {
                guardIdList.add(v.getGuardId());
            });
            if (!parkBuildGuardService.deleteBatchIds(guardIdList)) {
                return new BaseResult(BaseResultEnum.ERROR, "门禁申请审批失败");
            }
        }
        //操作类型type：1、拒绝；2、同意
        if ("2".equals(params.get("type").toString()) && parkBuildGuardMain.getType() == 1) {
            if (params.get("endTime") == null) {
                return new BaseResult(BaseResultEnum.ERROR, "门禁申请权限失效时间为空");
            }
            parkBuildGuardMain.setType(2);//通过
            parkBuildGuardMain.setEndTime(Long.valueOf(params.get("endTime").toString()));
            List<BjtParkBuildGuard> buildGuardList = parkBuildGuardService.selectList(new EntityWrapper<BjtParkBuildGuard>().eq("guard_main_id", parkBuildGuardMain.getGuardMainId()));
            if (buildGuardList == null || buildGuardList.size() < 1) {
                return new BaseResult(BaseResultEnum.ERROR, "无门禁申请明细");
            }
            buildGuardList.forEach(v -> {
                v.setType(1);//权限  1.正常 0.申请中 -1.禁止
                v.setEndTime(parkBuildGuardMain.getEndTime());
            });
            if (!parkBuildGuardService.updateAllColumnBatchById(buildGuardList)) {
                return new BaseResult(BaseResultEnum.ERROR, "门禁申请审批失败");
            }
            parkBuildGuardMain.setDetail(JSON.toJSONString(buildGuardList));
            if (!parkBuildGuardMainService.updateById(parkBuildGuardMain)) {
                return new BaseResult(BaseResultEnum.ERROR, "门禁申请审批失败");
            }
        }
        List<String> usernameList = new ArrayList<>();
        usernameList.add(parkBuildGuardMain.getApplyUsername());//设置申请人账号（手机号）
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("title", "门禁申请");
        if (parkBuildGuardMain.getType() == 2) {
            paramsMap.put("body", "门禁申请已通过");
        } else {
            paramsMap.put("body", "门禁申请已拒绝");
        }

        paramsMap.put("target", "BuildGuard");
        paramsMap.put("id", parkBuildGuardMain.getGuardMainId() + "");
        paramsMap.put("phoneList", usernameList);
        systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
        return new BaseResult(BaseResultEnum.SUCCESS, "门禁申请审批成功");
    }

    @ApiOperation(value = "获取门禁申请记录", notes = "获取门禁申请记录")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "select/GuardMainPage", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkBuildGuardMain>> selectGuardMainPage(@RequestParam Map<String, Object> params) {
        LOGGER.info("获取门禁申请记录 = > ");
        Page<BjtParkBuildGuardMain> pageModel = new Page<>();
        Page<BjtParkBuildGuardMain> pageResult = new Page<>();
        pageModel.setSize(Integer.parseInt(params.get("size").toString()));
        pageModel.setCurrent(Integer.parseInt(params.get("page").toString()));
        List<BjtParkBuildUser> buildUserList = parkBuildUserService.selectList(new EntityWrapper<BjtParkBuildUser>().eq("user_id", params.get("userId")));
        if (buildUserList.size() > 0) {
            if (buildUserList.size() == 1) {
                pageResult = parkBuildGuardMainService.selectPage(pageModel, new EntityWrapper<BjtParkBuildGuardMain>().eq("build_id", buildUserList.get(0).getBuildId()));
                return new BaseResult(BaseResultEnum.SUCCESS, pageResult);
            }
            if (buildUserList.size() > 1) {
                List<Integer> buildIdList = buildUserList.stream().map(v -> v.getBuildId()).collect(Collectors.toList());
                pageResult = parkBuildGuardMainService.selectPage(pageModel, new EntityWrapper<BjtParkBuildGuardMain>().in("build_id", buildIdList));
                return new BaseResult(BaseResultEnum.SUCCESS, pageResult);
            }
        }
        //查询当前用户是否是公司管理员
        BjtParkCompanyUser _bjtParkCompanyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", params.get("userId")).eq("type", 3));
        if (_bjtParkCompanyUser != null) {
            pageResult = parkBuildGuardMainService.selectPage(pageModel, new EntityWrapper<BjtParkBuildGuardMain>()
                    .eq("apply_user_id", params.get("userId")));
            return new BaseResult(BaseResultEnum.SUCCESS, pageResult);
        }
        return new BaseResult(BaseResultEnum.SUCCESS, pageResult);
    }

    //构造门禁申请
    private BjtParkBuildGuardMain constructGuardMain(Map<String, Object> userMap, BjtParkCompany parkCompany) {
        //构造门禁申请
        BjtParkBuildGuardMain bjtParkBuildGuardMain = new BjtParkBuildGuardMain();
        bjtParkBuildGuardMain.setApplyTime(System.currentTimeMillis());
        bjtParkBuildGuardMain.setCtime(System.currentTimeMillis());
        bjtParkBuildGuardMain.setApplyUserId(userMap.get("userId") + "");
        bjtParkBuildGuardMain.setApplyUsername(userMap.get("username") + "");
        bjtParkBuildGuardMain.setApplyUserRealname(userMap.get("realname") + "");
        bjtParkBuildGuardMain.setBuildId(parkCompany.getBuildId());
        bjtParkBuildGuardMain.setCompanyId(parkCompany.getCompanyId());
        bjtParkBuildGuardMain.setCompanyName(parkCompany.getName());
        bjtParkBuildGuardMain.setType(1);//类型:1、待审批，2、已通过，3、未通过
        if (parkBuildGuardMainService.insert(bjtParkBuildGuardMain)) {
            return bjtParkBuildGuardMain;
        }
        return null;
    }

    //构造门禁申请明细
    private BjtParkBuildGuard constructGuard(BjtParkCompanyUser v, BjtParkCompany parkCompany, Integer guardMainId, Long endTime, Map<String, Object> userMap) {
        BjtParkBuildGuard guard = new BjtParkBuildGuard();
        guard.setCtime(System.currentTimeMillis());
        guard.setBuildId(parkCompany.getBuildId());
        guard.setBuildName(parkCompany.getBuildName());
        guard.setCompanyName(parkCompany.getName());
        guard.setCompanyId(parkCompany.getCompanyId());
        guard.setUsername(v.getPhone());
        guard.setUserRealname(v.getRealname());
        guard.setUserId(v.getUserId());
        guard.setGuardMainId(guardMainId);
        guard.setType(0);//权限  1.正常 0.申请中 -1.禁止
        guard.setApplyRealname(userMap.get("realname") + "");
        guard.setApplyUserId(userMap.get("userId") + "");
        guard.setApplyUsername(userMap.get("username") + "");
        guard.setIdNumber(v.getIdNumber());
        guard.setDoorPlate(parkCompany.getDoorPlate());
        guard.setEndTime(endTime);
        return guard;
    }

}
