package com.j4sc.bjt.park.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.park.dao.entity.*;
import com.j4sc.bjt.park.rest.api.*;
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
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/4 14:52
 * @Version: 1.0
 **/
@RestController
@ApiModel
@RequestMapping("BjtParkCompany")
public class BjtParkCompanyController extends BaseController<BjtParkCompany, BjtParkCompanyService> implements BaseApiService<BjtParkCompany> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkCompanyController.class);

    @Autowired
    private BjtParkCompanyService parkCompanyService;
    @Autowired
    private BjtParkCompanyUserService parkCompanyUserService;
    @Autowired
    private BjtParkBuildGuardService parkBuildGuardService;
    @Autowired
    private BjtParkCompanyAdmissionService parkCompanyAdmissionService;
    @Autowired
    private BjtParkBuildService parkBuildService;
    @Autowired
    private BjtParkBuildHouseService parkBuildHouseService;
    @Autowired
    private BjtParkBuildAgreementService parkBuildAgreementService;
    @Autowired
    private BjtParkCompanyClockService parkCompanyClockService;
    @Autowired
    private BjtParkBuildUserService parkBuildUserService;


    @ApiOperation(value = "新建公司", notes = "新建公司")
    @Transactional
    @RequestMapping(value = "save/Company", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCompany(@RequestBody Map<String, Object> params) {
        LOGGER.info("新建公司...");
        BjtParkCompany bjtParkCompany = MapUtil.mapToBean(params, new BjtParkCompany());
        bjtParkCompany.setSimple(bjtParkCompany.getName());
        BjtParkCompany _bjtParkCompany = parkCompanyService.selectOne(new EntityWrapper<BjtParkCompany>().eq("name", bjtParkCompany.getName()));
        if (_bjtParkCompany != null) return new BaseResult(BaseResultEnum.ERROR, "当前公司已存在");
        bjtParkCompany.setCtime(System.currentTimeMillis());
        if (!parkCompanyService.insert(bjtParkCompany)) return new BaseResult(BaseResultEnum.ERROR, "新建公司失败");

        BjtParkCompanyUser _parkCompanyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", params.get("userId")));
        if (_parkCompanyUser != null) return new BaseResult(BaseResultEnum.ERROR, "当前用户已创建");

        BjtParkCompanyUser bjtParkCompanyUser = new BjtParkCompanyUser();
        bjtParkCompanyUser.setCompanyId(bjtParkCompany.getCompanyId());
        bjtParkCompanyUser.setUserId(params.get("userId").toString());
        bjtParkCompanyUser.setCompanyName(bjtParkCompany.getName());
        bjtParkCompanyUser.setPhone(params.get("phone").toString());
        bjtParkCompanyUser.setRealname(params.get("realname").toString());
        bjtParkCompanyUser.setIdNumber(params.get("idNumber").toString());
        bjtParkCompanyUser.setType(3);//1是普通员工，2是工作台管理员, 3是公司管理员

        if (parkCompanyUserService.insert(bjtParkCompanyUser)) return new BaseResult(BaseResultEnum.SUCCESS, "新建公司成功");
        return new BaseResult(BaseResultEnum.ERROR, "新建公司失败");
    }

    @ApiOperation(value = "删除公司")
    @Transactional
    @RequestMapping(value = "delete/Company", method = RequestMethod.DELETE)
    public BaseResult deleteCompany(@RequestParam("companyId") String companyId, @RequestParam("userId") String userId) {
        LOGGER.info("删除公司...");
        BjtParkCompanyUser _companyAdminUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>()
                .eq("company_id", companyId).eq("user_id", userId));
        if (_companyAdminUser == null) return new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司员工");
        if (_companyAdminUser.getType() != 3) new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司管理员");
        //查询公司下的关联用户
        List<BjtParkCompanyUser> listCompanyUser = parkCompanyUserService.selectList(new EntityWrapper<BjtParkCompanyUser>().eq("company_id", companyId));
        if (listCompanyUser.size() > 1) {
            return new BaseResult(BaseResultEnum.ERROR, "当前公司存在多个员工");
        }
        BjtParkBuildAgreement agreement = parkBuildAgreementService.selectOne(new EntityWrapper<BjtParkBuildAgreement>().eq("company_id", companyId));
        if (agreement != null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前存在履行的合同");
        }
        List<String> userIdList = new ArrayList<>();
        List<Integer> companyUserIdList = new ArrayList<>();
        if (listCompanyUser != null && listCompanyUser.size() > 0) {
            listCompanyUser.forEach(v -> {
                userIdList.add(v.getUserId());
                companyUserIdList.add(v.getCompanyUserId());
            });
            userIdList.add("-1");
            List<BjtParkBuildGuard> guardList = parkBuildGuardService.selectList(new EntityWrapper<BjtParkBuildGuard>().in("user_id", userIdList));
            if (guardList.size() > 0) {
                List<Integer> guardIdList = guardList.stream().map(v -> v.getGuardId()).collect(Collectors.toList());
                //删除用户门禁权限
                if (!parkBuildGuardService.deleteBatchIds(guardIdList)) {
                    return new BaseResult(BaseResultEnum.ERROR, "删除公司失败");
                }
            }
            List<BjtParkCompanyClock> clockList = parkCompanyClockService.selectList(new EntityWrapper<BjtParkCompanyClock>().in("user_id", userIdList));
            if (clockList.size() > 0) {
                List<Integer> clockIdList = clockList.stream().map(v->v.getClockId()).collect(Collectors.toList());
                if (!parkCompanyClockService.deleteBatchIds(clockIdList)) {
                    return new BaseResult(BaseResultEnum.ERROR, "删除公司失败");
                }
            }
            if (!parkCompanyUserService.deleteBatchIds(companyUserIdList)) {
                return new BaseResult(BaseResultEnum.ERROR, "删除公司失败");
            }
        }
        if (parkCompanyService.deleteById(Integer.parseInt(companyId))) {
            return new BaseResult(BaseResultEnum.SUCCESS, "删除成功");
        }
        return new BaseResult(BaseResultEnum.ERROR, "删除失败");
    }

    @ApiOperation(value = "公司管理员获取楼宇管理者联系方式", notes = "公司管理员获取楼宇管理者联系方式")
    @RequestMapping(value = "select/BuildUserInfo", method = RequestMethod.GET)
    public BaseResult<Map<String, String>> selectBuildUserInfo(@RequestParam("userId") String userId) {
        BjtParkCompanyUser companyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", userId));
        if (companyUser == null) {
            return new BaseResult(BaseResultEnum.ERROR, "非法操作");
        }
        if (companyUser.getType() != 3) {
            return new BaseResult(BaseResultEnum.ERROR, "当前用户无操作权限");
        }
        BjtParkCompany company = parkCompanyService.selectById(companyUser.getCompanyId());
        if (company == null) {
            return new BaseResult(BaseResultEnum.ERROR, "公司信息为空");
        }
        if (company.getBuildId() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "公司未入驻楼宇");
        }
        BjtParkBuild parkBuild = parkBuildService.selectOne(new EntityWrapper<BjtParkBuild>().eq("build_id", company.getBuildId()));
        if (parkBuild == null) {
            return new BaseResult(BaseResultEnum.ERROR, "楼宇不存在");
        }
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("contact", parkBuild.getContact());
        resultMap.put("contactWay", parkBuild.getContactWay());
        return new BaseResult(BaseResultEnum.SUCCESS, resultMap);
    }

    @ApiOperation(value = "停车申请时获取公司及管理员信息")
    @RequestMapping(value = "select/Company", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> selectCompany(@RequestParam("userId") String userId) {
        LOGGER.info("停车申请时获取公司及管理员信息...");
        BjtParkCompanyUser _companyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", userId));
        if (_companyUser == null) return new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司员工");
        if (_companyUser.getType() != 3) return new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司管理员");
        BjtParkCompany _company = parkCompanyService.selectOne(new EntityWrapper<BjtParkCompany>().eq("company_id", _companyUser.getCompanyId()));
        if (_company == null) return new BaseResult(BaseResultEnum.ERROR, "当前公司不存在");
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("buildId", _company.getBuildId());
        resultMap.put("buildName", _company.getBuildName());
        resultMap.put("companyId", _company.getCompanyId());
        resultMap.put("company", _company.getName());
        resultMap.put("applyer", _companyUser.getRealname());
        resultMap.put("applyerId", _companyUser.getUserId());
        resultMap.put("applyUsername", _companyUser.getPhone());
        return new BaseResult(BaseResultEnum.SUCCESS, resultMap);
    }

    @ApiOperation(value = "查询公司所处楼宇对应的停车场编号")
    @RequestMapping(value = "select/CompanyBuildSpaceId", method = RequestMethod.GET)
    public BaseResult selectCompanyBuildSpaceId(@RequestParam("userId") String userId) {
        LOGGER.info("查询公司所处楼宇对应的停车场编号...");
        BjtParkCompanyUser _companyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", userId));
        if (_companyUser == null) return new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司员工");
        if (_companyUser.getType() != 3) return new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司管理员");
        BjtParkCompany _company = parkCompanyService.selectOne(new EntityWrapper<BjtParkCompany>().eq("company_id", _companyUser.getCompanyId()));
        if (_company == null) return new BaseResult(BaseResultEnum.ERROR, "当前公司不存在");
        if (_company.getDoorPlate() == null && _company.getBuildId() == null) {
            return new BaseResult(BaseResultEnum.SUCCESS, "-1");
        }
        BjtParkBuild parkBuild = parkBuildService.selectOne(new EntityWrapper<BjtParkBuild>().eq("build_id", _company.getBuildId()));
        return parkBuild == null || parkBuild.getSpaceId() == null ? new BaseResult(BaseResultEnum.SUCCESS, "-1") : new BaseResult(BaseResultEnum.SUCCESS, parkBuild.getSpaceId());
    }

    @ApiOperation(value = "公司申请入驻楼宇", notes = "公司申请入驻楼宇")
    @Transactional
    @RequestMapping(value = "save/CompanyAdmission", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCompanyAdmission(@RequestBody Map<String, Object> params) {
        LOGGER.info("公司申请入驻楼宇...");
        BjtParkCompanyUser _companyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", params.get("userId")));
        if (_companyUser == null) return new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司员工");
        if (_companyUser.getType() == 1) return new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司管理员");
        BjtParkCompany _company = parkCompanyService.selectOne(new EntityWrapper<BjtParkCompany>().eq("company_id", _companyUser.getCompanyId()));
        if (_company == null) return new BaseResult(BaseResultEnum.ERROR, "当前公司不存在");
        BjtParkBuild _bjtParkBuild = parkBuildService.selectOne(new EntityWrapper<BjtParkBuild>().eq("code", params.get("code")));
        if (_bjtParkBuild == null) return new BaseResult(BaseResultEnum.ERROR, "当前楼宇不存在");

        BjtParkCompanyAdmission _companyAdmission = parkCompanyAdmissionService.selectOne(new EntityWrapper<BjtParkCompanyAdmission>().eq("company_id", _companyUser.getCompanyId()));
        //状态：1、待审批;2、已通过;3未通过
        if (_companyAdmission == null || _companyAdmission.getStatus() == 1 || _companyAdmission.getStatus() == 3) {
            BjtParkCompanyAdmission admission = new BjtParkCompanyAdmission();
            admission.setApplyer((String) params.get("realname"));
            admission.setApplyUserId((String) params.get("userId"));
            admission.setApplyTime(System.currentTimeMillis());
            admission.setBuildId(_bjtParkBuild.getBuildId());
            admission.setBuildName(_bjtParkBuild.getName());
            admission.setCode((String) params.get("code"));
            admission.setCompanyId(_company.getCompanyId());
            admission.setCompanyName(_company.getName());
            admission.setStatus(1);
            admission.setCtime(System.currentTimeMillis());
            if (!parkCompanyAdmissionService.insertOrUpdateAllColumn(admission))
                return new BaseResult(BaseResultEnum.ERROR, "申请提交失败");
            _company.setBuildId(_bjtParkBuild.getBuildId());
            _company.setBuildName(_bjtParkBuild.getName());
            _company.setCode(_bjtParkBuild.getCode());
            if (parkCompanyService.updateById(_company)) return new BaseResult(BaseResultEnum.SUCCESS, "申请提交成功");
            return new BaseResult(BaseResultEnum.ERROR, "申请提交失败");
        } else if (_companyAdmission.getStatus() == 2) {
            return new BaseResult(BaseResultEnum.ERROR, "申请已通过，请勿重新提交申请");
        }
        return new BaseResult(BaseResultEnum.ERROR, "申请提交失败");
    }
    @ApiOperation("获取公司列表")
    @RequestMapping(value = "select/PageCompany", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkCompany>> selectPageCompany(@RequestParam Map<String, Object> params) {
        Map<String, Object> resultMap = new HashMap<>();
        Page<BjtParkCompany> pageModel = new Page<>();
        pageModel.setSize(Integer.parseInt(params.get("size").toString()));
        pageModel.setCurrent(Integer.parseInt(params.get("page").toString()));
        BjtParkBuildUser buildUser = parkBuildUserService.selectOne(new EntityWrapper<BjtParkBuildUser>().eq("user_id", params.get("userId")).eq("build_id", params.get("buildId")));
        int companyId = 0;
        if (buildUser != null) {
            BjtParkCompanyUser companyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", params.get("userId")));
            if (companyUser != null) {
                companyId = companyUser.getCompanyId();
            }
        }
        Wrapper entityWrapper = new EntityWrapper<BjtParkCompany>();
        if (params.get("_name") != null) {
            entityWrapper.like("name", params.get("_name")+"");
        }
        if (params.get("_address") != null) {
            entityWrapper.like("address", params.get("_address")+"");
        }
        entityWrapper.andNew("build_id="+params.get("buildId")).or("company_id="+companyId);
        Page<BjtParkCompany> pageResult = parkCompanyService.selectPage(pageModel, entityWrapper);
        return new BaseResult(BaseResultEnum.SUCCESS, pageResult);
    }
    @ApiOperation("查询房屋入驻的公司详细信息及员工信息")
    @RequestMapping(value = "select/CompanyDetailInfo", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> selectCompanyDetailInfo(@RequestParam("houseId") int houseId, @RequestParam("userId") String userId, @RequestParam("buildId") int buildId) {
        BjtParkBuildHouse house = parkBuildHouseService.selectOne(new EntityWrapper<BjtParkBuildHouse>().eq("build_id", buildId).eq("house_id", houseId));
        if (house == null) {
            return new BaseResult(BaseResultEnum.ERROR, "房屋信息不存在");
        }
        Map<String, Object> resultMap = new HashMap<>();
        if (house.getCompanyId() != null) {
            BjtParkCompany company = parkCompanyService.selectOne(new EntityWrapper<BjtParkCompany>().eq("company_id", house.getCompanyId()));
            List<BjtParkCompanyUser> companyUserList = parkCompanyUserService.selectList(new EntityWrapper<BjtParkCompanyUser>().eq("company_id", house.getCompanyId()));
            List<BjtParkBuildGuard> guardList = parkBuildGuardService.selectList(new EntityWrapper<BjtParkBuildGuard>().eq("company_id", house.getCompanyId()));
            List<String> userIdList = companyUserList.stream().map(v -> v.getUserId()).collect(Collectors.toList());
            resultMap.put("company", company);
            resultMap.put("userList", companyUserList);
            resultMap.put("guardList", guardList);
            resultMap.put("userIdList", userIdList);
            return new BaseResult(BaseResultEnum.SUCCESS, resultMap);
        }
        return new BaseResult(BaseResultEnum.SUCCESS, resultMap);
    }

}
