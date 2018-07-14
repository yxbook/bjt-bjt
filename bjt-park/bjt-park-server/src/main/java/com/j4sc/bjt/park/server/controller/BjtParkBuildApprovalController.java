package com.j4sc.bjt.park.server.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.park.dao.entity.*;
import com.j4sc.bjt.park.rest.api.*;
import com.j4sc.bjt.park.server.client.SystemApiClient;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import com.j4sc.common.util.MapUtil;
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


/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018 2018/4/9 11:30
 * @Version: 1.0
 **/
@RestController
@ApiModel
@RequestMapping("BjtParkBuildApproval")
public class BjtParkBuildApprovalController extends BaseController<BjtParkBuildApproval, BjtParkBuildApprovalService> implements BaseApiService<BjtParkBuildApproval> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkBuildApprovalController.class);

    @Autowired
    private BjtParkBuildApprovalService parkBuildApprovalService;
    @Autowired
    private BjtParkBuildProgressService parkBuildProgressService;
    @Autowired
    private BjtParkCompanyService parkCompanyService;
    @Autowired
    private BjtParkCompanyAdmissionService parkCompanyAdmissionService;
    @Autowired
    private BjtParkCompanyUserService parkCompanyUserService;
    @Autowired
    private SystemApiClient systemApiClient;
    @Autowired
    private BjtParkBuildUserService parkBuildUserService;

    @ApiOperation(value = "提交申请")
    @Transactional
    @RequestMapping(value = "save/BuildApproval", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveBuildApproval(@RequestBody Map<String, Object> params) {
        LOGGER.info("提交申请 = > saveBuildApproval");
        BjtParkCompanyUser companyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", params.get("userId")));
        if (companyUser == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前用户不存在");
        }
        if (companyUser.getType() != 3) {
            return new BaseResult(BaseResultEnum.ERROR, "当前用户无权限");
        }
        BjtParkCompany parkCompany = parkCompanyService.selectOne(new EntityWrapper<BjtParkCompany>().eq("company_id", companyUser.getCompanyId()));
        if (parkCompany == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前公司不存在");
        }
        if ("".equals(parkCompany.getDoorPlate()) || parkCompany.getDoorPlate() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "公司未入驻楼宇");
        }
        BjtParkCompanyAdmission companyAdmission = parkCompanyAdmissionService.selectOne(new EntityWrapper<BjtParkCompanyAdmission>().eq("company_id", parkCompany.getCompanyId()).eq("build_id", parkCompany.getBuildId()));
        if (companyAdmission == null || companyAdmission.getStatus() != 2) {
            return new BaseResult(BaseResultEnum.ERROR, "公司未入驻楼宇");
        }
        BjtParkBuildApproval bjtParkBuildApproval = MapUtil.mapToBean(params, new BjtParkBuildApproval());
        bjtParkBuildApproval.setStatus(1);//申请进度：1、待审批、2、申请通过、3、未通过
        bjtParkBuildApproval.setCtime(System.currentTimeMillis());
        bjtParkBuildApproval.setBuildId(parkCompany.getBuildId());
        bjtParkBuildApproval.setCompanyId(parkCompany.getCompanyId());
        bjtParkBuildApproval.setCompanyName(parkCompany.getName());
        if (!parkBuildApprovalService.insert(bjtParkBuildApproval))
            return new BaseResult(BaseResultEnum.ERROR, "提交申请失败");

        BjtParkBuildProgress buildProgress = MapUtil.mapToBean((Map<String, Object>) params.get("progress"), new BjtParkBuildProgress());
        buildProgress.setCtime(System.currentTimeMillis());
        buildProgress.setLastTime(System.currentTimeMillis());
        buildProgress.setLastUserId(bjtParkBuildApproval.getUserId());
        buildProgress.setLastUserRealname(bjtParkBuildApproval.getUserRealname());
        buildProgress.setbApprovalId(bjtParkBuildApproval.getbApprovalId());
        buildProgress.setStatus(1);//处理状态：1、待处理、2、已处理
        if (!parkBuildProgressService.insert(buildProgress)) {
            return new BaseResult(BaseResultEnum.ERROR, "提交申请失败");
        }
        bjtParkBuildApproval.setApprovalUser(JSON.toJSONString(buildProgress));//将审批信息设置到申请中
        if (!parkBuildApprovalService.updateById(bjtParkBuildApproval)) {
            return new BaseResult(BaseResultEnum.ERROR, "提交申请失败");
        }
        List<String> usernameList = new ArrayList<>();
        usernameList.add(buildProgress.getUsername());//设置申请人账号（手机号）
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("title", "服务申请");
        paramsMap.put("body", "服务申请待审批");
        paramsMap.put("target", "BuildApproval");
        paramsMap.put("id", bjtParkBuildApproval.getbApprovalId() + "");
        paramsMap.put("phoneList", usernameList);
        systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
        return new BaseResult(BaseResultEnum.SUCCESS, "提交申请成功");
    }

    @ApiOperation(value = "申请审批:1表示同意，2表示拒绝，3表示转批")
    @Transactional
    @RequestMapping(value = "update/BuildApproval", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateBuildApproval(@RequestBody Map<String, Object> params) {
        LOGGER.info("申请审批 = > updateBuildApproval");
        if ("1".equals(params.get("type").toString())) {
            return this.dataValidation(params, "1");
        }
        if ("2".equals(params.get("type").toString())) {
            if (params.get("remark") == null || "".equals(params.get("remark").toString())) {
                return new BaseResult(BaseResultEnum.ERROR, "请填写审批意见");
            }
            return this.dataValidation(params, "2");
        }
        if ("3".equals(params.get("type").toString())) {
            BjtParkBuildApproval bjtParkBuildApproval = parkBuildApprovalService.selectOne(new EntityWrapper<BjtParkBuildApproval>().eq("b_approval_id", params.get("bApprovalId")));
            if (bjtParkBuildApproval == null) {
                return new BaseResult(BaseResultEnum.ERROR, "服务申请不存在");
            }
            //当前处理人的进度
            BjtParkBuildProgress buildProgress = parkBuildProgressService.selectOne(new EntityWrapper<BjtParkBuildProgress>().eq("b_approval_id", params.get("bApprovalId")).eq("user_id", params.get("userId")).eq("status", 1));
            if (buildProgress == null) {
                return new BaseResult(BaseResultEnum.ERROR, "当前用户无操作权限");
            }
            buildProgress.setStatus(2);
            buildProgress.setUtime(System.currentTimeMillis());
            if (!parkBuildProgressService.updateById(buildProgress))
                return new BaseResult(BaseResultEnum.ERROR, "审批失败");
            if (params.get("progress") == null) return new BaseResult(BaseResultEnum.ERROR, "下一步审批信息为空");
            //下一步处理人的进度
            BjtParkBuildProgress parkBuildProgress = MapUtil.mapToBean((Map<String, Object>) params.get("progress"), new BjtParkBuildProgress());
            parkBuildProgress.setbApprovalId(Integer.parseInt(params.get("bApprovalId").toString()));
            parkBuildProgress.setLastUserRealname(params.get("userRealname") + "");
            parkBuildProgress.setLastUserId(params.get("userId") + "");
            parkBuildProgress.setLastTime(System.currentTimeMillis());
            parkBuildProgress.setCtime(System.currentTimeMillis());
            parkBuildProgress.setStatus(1);//处理状态：1、待处理、2、已处理
            if (!parkBuildProgressService.insert(parkBuildProgress))
                return new BaseResult(BaseResultEnum.ERROR, "审批失败");
            //获取当前服务申请对应的审批记录明细，设置到服务申请中
            List<BjtParkBuildProgress> buildProgressList = parkBuildProgressService.selectList(new EntityWrapper<BjtParkBuildProgress>().eq("b_approval_id", params.get("bApprovalId")));
            if (buildProgressList == null || buildProgressList.size() < 1) {
                bjtParkBuildApproval.setApprovalUser(JSON.toJSONString(buildProgress));//将审批信息设置到申请中
            } else {
                buildProgressList.add(buildProgress);
                bjtParkBuildApproval.setApprovalUser(JSON.toJSONString(buildProgressList));
            }
            if (!parkBuildApprovalService.updateById(bjtParkBuildApproval)) {
                return new BaseResult(BaseResultEnum.ERROR, "审批失败");
            }
            List<String> phoneList = new ArrayList<>();
            phoneList.add(buildProgress.getUsername());//设置审批人账号（手机号）
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("title", "服务申请");
            paramsMap.put("body", "服务申请");
            paramsMap.put("target", "BuildApproval");
            paramsMap.put("id", bjtParkBuildApproval.getbApprovalId() + "");
            paramsMap.put("phoneList", phoneList);
            systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
            return new BaseResult(BaseResultEnum.SUCCESS, "审批成功");
        }
        return new BaseResult(BaseResultEnum.ERROR, "审批失败");
    }

    @ApiOperation(value = "待审批事项和审批记录（status为1则是待审批事项，否则查询所有记录）")
    @RequestMapping(value = "select/BuildApprovalPage", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkBuildApproval>> selectBuildApprovalPage(@RequestParam Map<String, Object> params) {
        LOGGER.info("待审批事项和审批记录 = > selectBuildApprovalPage");
        Page<BjtParkBuildApproval> pageModel = new Page<>();
        pageModel.setSize(Integer.parseInt(params.get("size").toString()));
        pageModel.setCurrent(Integer.parseInt(params.get("page").toString()));
        Wrapper<BjtParkBuildApproval> approvalWrapper = null;
        //buildId为-1表示非楼宇管理员
        int buildId = Integer.parseInt(params.get("buildId").toString());
        if (buildId == -1) {
            approvalWrapper = new EntityWrapper<BjtParkBuildApproval>().eq("user_id", params.get("userId")).orderBy("ctime", false);
        }
        //大于0表示为楼宇管理员
        if (buildId > 0) {
            Wrapper<BjtParkBuildProgress> entityWrapper = Integer.parseInt(params.get("status").toString()) == 1 ? new EntityWrapper<BjtParkBuildProgress>().eq("user_id", params.get("userId")).eq("status", 1) : new EntityWrapper<BjtParkBuildProgress>().eq("user_id", params.get("userId"));
            List<BjtParkBuildProgress> buildProgressList = parkBuildProgressService.selectList(entityWrapper);
            if (buildProgressList.size() < 1 || buildProgressList == null)
                return new BaseResult(BaseResultEnum.SUCCESS, pageModel);
            List<Integer> approvalIdList = new ArrayList<>();
            buildProgressList.forEach(v -> {
                approvalIdList.add(v.getbApprovalId());
            });
            approvalWrapper = new EntityWrapper<BjtParkBuildApproval>().in("b_approval_id", approvalIdList).orderBy("ctime", false);
        }

        Page<BjtParkBuildApproval> pageResult = parkBuildApprovalService.selectPage(pageModel, approvalWrapper);
        return new BaseResult(BaseResultEnum.SUCCESS, pageResult);
    }

    @ApiOperation(value = "获取楼宇管理员")
    @RequestMapping(value = "select/BuildUserList", method = RequestMethod.GET)
    public BaseResult<List<BjtParkBuildUser>> selectBuildUserList(@RequestParam("userId") String userId, @RequestParam("buildId") int buildId) {
        List<BjtParkBuildUser> buildUserList = new ArrayList<>();
        if (buildId > 0) {
            buildUserList = parkBuildUserService.selectList(new EntityWrapper<BjtParkBuildUser>().eq("build_id", buildId));
            return new BaseResult(BaseResultEnum.SUCCESS, buildUserList);
        }
        if (buildId == -1) {
            BjtParkCompanyUser companyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", userId).eq("type", 3));
            if (companyUser == null) {
                return new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司管理员");
            }
            BjtParkCompany company = parkCompanyService.selectOne(new EntityWrapper<BjtParkCompany>().eq("company_id", companyUser.getCompanyId()));
            if (company == null) {
                return new BaseResult(BaseResultEnum.ERROR, "当前公司不存在");
            }
            if (company.getBuildId() == null) {
                return new BaseResult(BaseResultEnum.ERROR, "当前公司未入驻楼宇");
            }
            buildUserList = parkBuildUserService.selectList(new EntityWrapper<BjtParkBuildUser>().eq("build_id", company.getBuildId()));
        }
        return new BaseResult(BaseResultEnum.SUCCESS, buildUserList);
    }

    /**
     * 申请审批:type为1表示同意，2表示拒绝，3表示转批
     *
     * @param params
     * @param type
     * @return
     */
    private BaseResult dataValidation(Map<String, Object> params, String type) {
        BjtParkBuildApproval bjtParkBuildApproval = parkBuildApprovalService.selectOne(new EntityWrapper<BjtParkBuildApproval>().eq("b_approval_id", params.get("bApprovalId")));
        if (params.get("remark") != null && !"".equals(params.get("remark").toString())) {
            bjtParkBuildApproval.setRemark(params.get("remark").toString());
        }
        if ("1".equals(type)) {
            bjtParkBuildApproval.setStatus(2);//申请进度：1、待审批、2、申请通过、3、未通过
        }
        if ("2".equals(type)) {
            bjtParkBuildApproval.setStatus(3);//申请进度：1、待审批、2、申请通过、3、未通过
        }
        bjtParkBuildApproval.setUtime(System.currentTimeMillis());//完成时间
        BjtParkBuildProgress bjtParkBuildProgress = parkBuildProgressService.selectOne(new EntityWrapper<BjtParkBuildProgress>().eq("b_approval_id", params.get("bApprovalId")).eq("user_id", params.get("userId")).eq("status", 1));
        if (bjtParkBuildProgress == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前用户无操作权限");
        }
        bjtParkBuildProgress.setStatus(2);
        bjtParkBuildProgress.setUtime(System.currentTimeMillis());
        if (!parkBuildProgressService.updateById(bjtParkBuildProgress))
            return new BaseResult(BaseResultEnum.ERROR, "审批失败");
        List<BjtParkBuildProgress> buildProgressList = parkBuildProgressService.selectList(new EntityWrapper<BjtParkBuildProgress>().eq("b_approval_id", params.get("bApprovalId")));
        bjtParkBuildApproval.setApprovalUser(JSON.toJSONString(buildProgressList));
        //审批人操作
        if (!parkBuildApprovalService.updateById(bjtParkBuildApproval))
            return new BaseResult(BaseResultEnum.ERROR, "审批失败");
        List<String> phoneList = new ArrayList<>();
        phoneList.add(bjtParkBuildApproval.getUsername());//设置申请人账号（手机号）
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("title", "服务申请");
        if (bjtParkBuildApproval.getStatus() == 2) {
            paramsMap.put("body", "服务申请已通过");
        } else {
            paramsMap.put("body", "服务申请已拒绝");
        }
        paramsMap.put("target", "BuildApproval");
        paramsMap.put("id", bjtParkBuildApproval.getbApprovalId() + "");
        paramsMap.put("phoneList", phoneList);
        systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
        return new BaseResult(BaseResultEnum.SUCCESS, "审批成功");
    }
}
