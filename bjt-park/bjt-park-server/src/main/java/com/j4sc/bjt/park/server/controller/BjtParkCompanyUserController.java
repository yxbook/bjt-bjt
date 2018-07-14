package com.j4sc.bjt.park.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuard;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildUser;
import com.j4sc.bjt.park.dao.entity.BjtParkCompany;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyUser;
import com.j4sc.bjt.park.rest.api.BjtParkBuildGuardService;
import com.j4sc.bjt.park.rest.api.BjtParkBuildUserService;
import com.j4sc.bjt.park.rest.api.BjtParkCompanyService;
import com.j4sc.bjt.park.rest.api.BjtParkCompanyUserService;
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

import java.util.*;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/4 14:52
 * @Version: 1.0
 **/
@ApiModel
@RestController
@RequestMapping("BjtParkCompanyUser")
public class BjtParkCompanyUserController extends BaseController<BjtParkCompanyUser, BjtParkCompanyUserService> implements BaseApiService<BjtParkCompanyUser> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkCompanyUserController.class);

    @Autowired
    private BjtParkCompanyUserService parkCompanyUserService;
    @Autowired
    private BjtParkBuildGuardService parkBuildGuardService;
    @Autowired
    private BjtParkCompanyService parkCompanyService;
    @Autowired
    private BjtParkBuildUserService parkBuildUserService;
    @Autowired
    private SystemApiClient systemApiClient;

    @ApiOperation(value = "给员工添加工作台管理员权限", notes = "给员工添加工作台管理员权限")
    @RequestMapping(value = "save/CompanyAdminUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCompanyAdminUser(@RequestParam("userId") String userId, @RequestBody List<String> userIdList) {
        LOGGER.info(" 给员工添加工作台管理员权限...");
        //数据校验及处理
        BaseResult<List<BjtParkCompanyUser>> baseResult = dataValidation(userId, userIdList, 1, 2);
        if (baseResult.getData() == null || baseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return new BaseResult(BaseResultEnum.ERROR, "添加工作台管理员失败");
        }
        if (parkCompanyUserService.updateBatchById(baseResult.getData())) {
            return new BaseResult(BaseResultEnum.SUCCESS, "添加工作台管理员成功");
        }
        return new BaseResult(BaseResultEnum.ERROR, "添加工作台管理员失败");
    }

    @ApiOperation(value = "管理员权限转交", notes = "管理员权限转交")
    @Transactional
    @RequestMapping(value = "update/CompanyAdminUser", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateCompanyAdminUser(@RequestBody Map<String, Object> params) {
        LOGGER.info(" 管理员权限转交...");
        //查看当前用户是否是工作台管理员
        BjtParkCompanyUser companyAdminUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>()
                .eq("user_id", params.get("adminUserId")).andNew("type={0}", "2").or("type=3"));
        if (companyAdminUser == null) return new BaseResult(BaseResultEnum.ERROR, "非法操作");
        BjtParkCompanyUser adminUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>()
                .eq("user_id", params.get("userId")).eq("company_id", companyAdminUser.getCompanyId()));
        if (adminUser == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前员工不存在");
        }
        if (adminUser.getType() == 2 || adminUser.getType() == 3) {
            return new BaseResult(BaseResultEnum.ERROR, "当前员工已是管理员");
        }
        if (adminUser.getType() == 1) {
            int type = adminUser.getType();
            adminUser.setType(companyAdminUser.getType());//设置为工作台管理员
            if (parkCompanyUserService.updateById(adminUser)) {
                companyAdminUser.setType(type);//将转交者设置为普通管理员
                if (parkCompanyUserService.updateById(companyAdminUser)) {
                    List<String> list = new ArrayList<>();
                    list.add(companyAdminUser.getPhone());
                    Map<String, Object> paramsMap = new HashMap<>();
                    paramsMap.put("title", "权限转交");
                    paramsMap.put("body", "权限转交");
                    paramsMap.put("target", "CompanyUser");
                    paramsMap.put("id", "");
                    paramsMap.put("phoneList", list);
                    systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
                    return new BaseResult(BaseResultEnum.SUCCESS, "权限转交成功");
                }
            }
        }
        return new BaseResult(BaseResultEnum.ERROR, "权限转交失败");
    }

    @ApiOperation(value = "删除工作台管理员权限", notes = "删除工作台管理员权限")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "delete/CompanyAdminUser", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult deleteCompanyAdminUser(@RequestParam("userId") String userId, @RequestBody List<String> userIdList) {
        LOGGER.info(" 删除工作台管理员权限...");
        //数据校验及处理
        BaseResult<List<BjtParkCompanyUser>> baseResult = dataValidation(userId, userIdList, 2, 1);
        if (baseResult.getData() == null || baseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return new BaseResult(BaseResultEnum.ERROR, "删除公司管理员权限失败");
        }
        if (parkCompanyUserService.updateBatchById(baseResult.getData())) {
            return new BaseResult(BaseResultEnum.SUCCESS, "删除公司管理员权限成功");
        }
        return new BaseResult(BaseResultEnum.ERROR, "删除公司管理员权限失败");
    }

    @ApiOperation(value = "删除公司员工", notes = "删除公司员工")
    @Transactional
    @RequestMapping(value = "delete/CompanyUser", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult deleteCompanyUser(@RequestBody Map<String, Object> params) {
        LOGGER.info(" 删除公司员工...");
        BaseResult<BjtParkCompanyUser> baseResult = this.dataValidation(params);
        if (baseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) return baseResult;
        if (!parkCompanyUserService.delete(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", params.get("userId"))))
            return new BaseResult(BaseResultEnum.ERROR, "删除员工失败");
        BjtParkBuildGuard _bjtParkBuildGuard = parkBuildGuardService.selectOne(new EntityWrapper<BjtParkBuildGuard>().eq("user_id", params.get("userId")));
        if (_bjtParkBuildGuard != null) {
            parkBuildGuardService.deleteById(_bjtParkBuildGuard.getGuardId());//移除门禁权限
        }
        List<String> list = new ArrayList<>();
        list.add(baseResult.getData().getPhone());
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("title", "已被公司管理员删除");
        paramsMap.put("body", "已被公司管理员删除");
        paramsMap.put("target", "CompanyUser");
        paramsMap.put("id", "");
        paramsMap.put("phoneList", list);
        systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
        return new BaseResult(BaseResultEnum.SUCCESS, "删除员工成功");
    }

    @ApiOperation(value = "员工申请退出公司", notes = "员工申请退出公司")
    @Transactional
    @RequestMapping(value = "delete/CompanyUserSelf", method = RequestMethod.DELETE)
    public BaseResult deleteCompanyUserSelf(@RequestParam("userId") String userId) {
        LOGGER.info(" 员工申请退出公司...");
        BjtParkCompanyUser _companyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>()
                .eq("user_id", userId));
        List<BjtParkBuildUser> buildUserList = parkBuildUserService.selectList(new EntityWrapper<BjtParkBuildUser>().eq("user_id", userId));
        if (buildUserList != null && buildUserList.size() > 0) {
            return new BaseResult(BaseResultEnum.ERROR, "楼宇管理员不能退出公司");
        }
        if (_companyUser == null) return new BaseResult(BaseResultEnum.ERROR, "该用户不是该公司员工");
        if (_companyUser.getType() == 3) {
            //查询公司管理员列表
            List<BjtParkCompanyUser> companyUserList = parkCompanyUserService.selectList(new EntityWrapper<BjtParkCompanyUser>().eq("company_id", _companyUser.getCompanyId()).eq("type", 3));
            if (companyUserList.size() < 2 || companyUserList == null)
                return new BaseResult(BaseResultEnum.ERROR, "公司必须保留一个公司管理员");
        }
        if (!parkCompanyUserService.delete(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", userId)))
            return new BaseResult(BaseResultEnum.ERROR, "申请退出公司失败");
        BjtParkBuildGuard _bjtParkBuildGuard = parkBuildGuardService.selectOne(new EntityWrapper<BjtParkBuildGuard>().eq("user_id", userId));
        if (_bjtParkBuildGuard != null) {
            parkBuildGuardService.deleteById(_bjtParkBuildGuard.getGuardId());//移除门禁权限
        }
        return new BaseResult(BaseResultEnum.SUCCESS, "申请退出公司成功");
    }

    //查询类型：1表示查询所有员工、2表示没有开门权限的员工列表、3表示该公司的所有管理员、4、该公司所有非管理员
    @ApiOperation(value = "查询公司员工列表", notes = "查询公司员工列表")
    @RequestMapping(value = "select/PageCompanyUser", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkCompanyUser>> selectPageCompanyUser(@RequestParam Map<String, Object> params) {
        LOGGER.info(" 查询公司员工列表...");
        BjtParkCompanyUser companyAdminUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>()
                .eq("user_id", params.get("userId")));
        if (companyAdminUser == null) return new BaseResult(BaseResultEnum.ERROR, "非法操作");
        Page<BjtParkCompanyUser> pageParams = new Page<>();
        int size = params.get("size") == null ? 20 : Integer.parseInt(params.get("size").toString());
        int currentPage = params.get("page") == null ? 1 : Integer.parseInt(params.get("page").toString());
        pageParams.setSize(size);
        pageParams.setCurrent(currentPage);
        Page<BjtParkCompanyUser> page = new Page<>();
        List<BjtParkCompanyUser> list;
        //查询没有门禁权限的员工
        if ("2".equals(params.get("type").toString())) {
            List<BjtParkBuildGuard> buildGuardList = parkBuildGuardService.selectList(new EntityWrapper<BjtParkBuildGuard>().eq("company_id", companyAdminUser.getCompanyId()));
            if (buildGuardList.size() > 0 && buildGuardList != null) {
                List<String> userIdList = new ArrayList<>();
                buildGuardList.forEach(v -> {
                    userIdList.add(v.getUserId());
                });//过滤用户编号
                Wrapper<BjtParkCompanyUser> wrapper2 = new EntityWrapper<BjtParkCompanyUser>().notIn("user_id", userIdList).eq("company_id", companyAdminUser.getCompanyId());
                if (params.get("key") != null) {
                    wrapper2.like("phone", params.get("key").toString()).like("realname", params.get("key").toString());
                }
                list = parkCompanyUserService.selectList(wrapper2);
                page.setRecords(list);
                return new BaseResult(BaseResultEnum.SUCCESS, page);
            }
        }
        if ("3".equals(params.get("type").toString())) {
            Wrapper<BjtParkCompanyUser> wrapper3 = new EntityWrapper<BjtParkCompanyUser>().eq("company_id", companyAdminUser.getCompanyId()).andNew("type={0}", "2").or("type=3");
            if (params.get("key") != null) {
                wrapper3.like("phone", params.get("key").toString()).like("realname", params.get("key").toString());
            }
            list = parkCompanyUserService.selectList(wrapper3);
            page.setRecords(list);
            return new BaseResult(BaseResultEnum.SUCCESS, page);
        }

        if ("4".equals(params.get("type").toString())) {
            Wrapper<BjtParkCompanyUser> wrapper4 = new EntityWrapper<BjtParkCompanyUser>().eq("company_id", companyAdminUser.getCompanyId()).eq("type", "1");
            if (params.get("key") != null) {
                wrapper4.like("phone", params.get("key").toString()).like("realname", params.get("key").toString());
            }
            page = parkCompanyUserService.selectPage(pageParams, wrapper4);
            return new BaseResult(BaseResultEnum.SUCCESS, page);
        }
        Wrapper<BjtParkCompanyUser> wrapper = new EntityWrapper<BjtParkCompanyUser>().eq("company_id", companyAdminUser.getCompanyId());
        if (params.get("key") != null) {
            wrapper.like("phone", params.get("key").toString()).like("realname", params.get("key").toString());
        }
        page = parkCompanyUserService.selectPage(pageParams, wrapper);
        return new BaseResult(BaseResultEnum.SUCCESS, page);
    }

    @ApiOperation(value = "查询公司员工列表", notes = "查询公司员工列表")
    @RequestMapping(value = "select/CompanyUserList", method = RequestMethod.GET)
    public BaseResult<List<BjtParkCompanyUser>> selectCompanyUserList(@RequestParam("userId") String userId) {
        LOGGER.info(" 查询公司员工列表...");
        BjtParkCompanyUser companyAdminUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>()
                .eq("user_id", userId).andNew("type={0}", "2").or("type=3"));
        if (companyAdminUser == null) return new BaseResult(BaseResultEnum.ERROR, "非法操作");

        List<BjtParkCompanyUser> list = parkCompanyUserService.selectList(new EntityWrapper<BjtParkCompanyUser>().eq("company_id", companyAdminUser.getCompanyId()));
        return new BaseResult(BaseResultEnum.SUCCESS, list);
    }

    @ApiOperation(value = "用户申请加入公司")
    @RequestMapping(value = "save/CompanyUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCompanyUser(@RequestBody Map<String, Object> params) {
        LOGGER.info("用户申请加入公司...");
        BjtParkCompanyUser adminCompanyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("company_id", params.get("companyId")).eq("type", 3));
        return this.addCompanyUser(params, adminCompanyUser);//执行内部方法
    }

    @ApiOperation(value = "公司管理员添加员工")
    @Transactional
    @RequestMapping(value = "save/CompanyUserByAdmin", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCompanyUserByAdmin(@RequestBody Map<String, Object> params) {
        LOGGER.info("公司管理员添加员工...");
        BjtParkCompanyUser companyAdminUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>()
                .eq("user_id", params.get("adminUserId")).eq("type", 3));
        if (companyAdminUser == null) return new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司管理员");
        params.put("companyId", companyAdminUser.getCompanyId());
        BaseResult baseResult = this.addCompanyUser(params, companyAdminUser);//执行内部方法
        if (baseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return baseResult;
        }

        List<String> list = new ArrayList<>();
        BjtParkCompanyUser companyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", params.get("userId")));
        list.add(companyUser.getPhone());
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("title", "加入公司");
        paramsMap.put("body", "加入公司");
        paramsMap.put("target", "CompanyUser");
        paramsMap.put("id", "");
        paramsMap.put("phoneList", list);
        systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
        return new BaseResult(BaseResultEnum.SUCCESS, "添加成功");
    }

    @ApiOperation(value = "修改员工信息")
    @RequestMapping(value = "update/CompanyUser", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateCompanyUser(@RequestBody BjtParkCompanyUser bjtParkCompanyUser, @RequestParam("userId") String userId) {
        LOGGER.info("修改员工信息...");
        if (bjtParkCompanyUser.getUserId().equals(userId)) {
            return this.update(bjtParkCompanyUser, userId);//修改自己的信息，普通员工即可以修改
        }
        BjtParkCompanyUser companyAdminUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", userId).eq("type", 3));
        if (companyAdminUser == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司管理员");
        }
        return this.update(bjtParkCompanyUser, userId);
    }

    @ApiOperation(value = "统计公司总人数")
    @RequestMapping(value = "select/CountCompanyUser", method = RequestMethod.GET)
    public BaseResult selectCountCompanyUser(@RequestParam("userId") String userId) {
        BjtParkCompanyUser companyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", userId));
        if (companyUser == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前用户不是公司员工");
        }
        if (companyUser.getType() != 2 && companyUser.getType() != 3) {
            return new BaseResult(BaseResultEnum.ERROR, "当前用户无权限");
        }
        int count = parkCompanyUserService.selectCount(new EntityWrapper<BjtParkCompanyUser>().eq("company_id", companyUser.getCompanyId()));
        return new BaseResult(BaseResultEnum.SUCCESS, count);
    }

    /**
     * 公共数据校验方法
     *
     * @param params
     * @return
     */
    private BaseResult<BjtParkCompanyUser> dataValidation(Map<String, Object> params) {
        BjtParkCompanyUser companyAdminUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>()
                .eq("user_id", params.get("adminUserId")).eq("type", 3));
        if (companyAdminUser == null) return new BaseResult(BaseResultEnum.ERROR, "非法操作");
        BjtParkCompanyUser _companyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>()
                .eq("user_id", params.get("userId")).eq("company_id", companyAdminUser.getCompanyId()));
        if (_companyUser == null) return new BaseResult(BaseResultEnum.ERROR, "该用户不是该公司员工");

        return new BaseResult(BaseResultEnum.SUCCESS, _companyUser);
    }

    /**
     * 公共数据校验方法
     *
     * @param userId
     * @param userIdList
     * @return
     */
    private BaseResult<List<BjtParkCompanyUser>> dataValidation(String userId, List<String> userIdList, int queryType, int setType) {
        BjtParkCompanyUser companyAdminUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>()
                .eq("user_id", userId).andNew("type={0}", "2").or("type=3"));
        if (companyAdminUser == null) return new BaseResult(BaseResultEnum.ERROR, "非法操作");
        userIdList.forEach(v -> {
            if (v.equals(userId)) {
                userIdList.remove(v);
            }//移除当前用户
        });
        List<BjtParkCompanyUser> companyUserList = parkCompanyUserService.selectList(new EntityWrapper<BjtParkCompanyUser>()
                .in("user_id", userIdList).eq("company_id", companyAdminUser.getCompanyId()).eq("type", queryType));
        companyUserList.forEach(v -> {
            v.setType(setType);//设置为普通员工
        });
        return new BaseResult(BaseResultEnum.SUCCESS, companyUserList);
    }

    private BaseResult addCompanyUser(Map<String, Object> params, BjtParkCompanyUser adminCompanyUser) {
        BjtParkCompany _company = parkCompanyService.selectOne(new EntityWrapper<BjtParkCompany>().eq("company_id", params.get("companyId")));
        if (_company == null) return new BaseResult(BaseResultEnum.ERROR, "当前公司不存在");
        BjtParkCompanyUser _companyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", params.get("userId")).eq("company_id", params.get("companyId")));
        if (_companyUser != null) return new BaseResult(BaseResultEnum.ERROR, "当前用户已加入该公司");
        BjtParkCompanyUser companyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", params.get("userId")));
        if (companyUser != null) return new BaseResult(BaseResultEnum.ERROR, "当前用户已是其他公司员工");
        if ("".equals(params.get("idNumber").toString()) || params.get("idNumber") == null) {
            return new BaseResult(BaseResultEnum.ERROR, "身份证号不能为空");
        }
        //查看当前公司管理员是否是楼宇管理员
        List<BjtParkBuildUser> buildUserList = parkBuildUserService.selectList(new EntityWrapper<BjtParkBuildUser>().eq("user_id", params.get("adminUserId")));
        if (buildUserList.size() > 0 && adminCompanyUser != null) {
            List<BjtParkBuildGuard> buildGuardList = parkBuildGuardService.selectList(new EntityWrapper<BjtParkBuildGuard>().eq("user_id", params.get("adminUserId")));
            if (buildGuardList.size() > 0 ) {
                List<BjtParkBuildGuard> guardList = new ArrayList<>();
                buildGuardList.forEach(v->{
                    //给楼宇管理员添加门禁权限
                    BjtParkBuildGuard guard = new BjtParkBuildGuard();
                    guard.setCtime(System.currentTimeMillis());
                    guard.setBuildId(v.getBuildId());
                    guard.setBuildName(v.getBuildName());
                    guard.setUsername(params.get("phone").toString());
                    guard.setUserRealname(params.get("realname").toString());
                    guard.setUserId(params.get("userId").toString());
                    guard.setType(1);//权限  1.正常 0.申请中 -1.禁止
                    guard.setApplyRealname(adminCompanyUser.getRealname());
                    guard.setApplyUserId(adminCompanyUser.getUserId());
                    guard.setApplyUsername(adminCompanyUser.getPhone());
                    guard.setCompanyId(adminCompanyUser.getCompanyId());
                    guard.setCompanyName(adminCompanyUser.getCompanyName());
                    guard.setDoorPlate(_company.getDoorPlate());
                    guard.setIdNumber(params.get("idNumber").toString());
                    Calendar curr = Calendar.getInstance();
                    curr.set(Calendar.YEAR, curr.get(Calendar.YEAR) + 1);
                    guard.setEndTime(curr.getTimeInMillis());
                    guardList.add(guard);
                });

                if (!parkBuildGuardService.insertOrUpdateAllColumnBatch(guardList)) {
                    return new BaseResult(BaseResultEnum.ERROR, "新增失败");
                }
            }
        }
        BjtParkCompanyUser bjtParkCompanyUser = new BjtParkCompanyUser();
        bjtParkCompanyUser.setCompanyId(_company.getCompanyId());
        bjtParkCompanyUser.setUserId((String) params.get("userId"));
        bjtParkCompanyUser.setCompanyName(_company.getName());
        bjtParkCompanyUser.setPhone((String) params.get("phone"));
        bjtParkCompanyUser.setJob(params.get("job") + "");
        bjtParkCompanyUser.setDepartment(params.get("department") + "");
        bjtParkCompanyUser.setRealname((String) params.get("realname"));
        bjtParkCompanyUser.setIdNumber((String) params.get("idNumber"));
        if (params.get("sex") == null || "".equals(params.get("sex").toString())){
            bjtParkCompanyUser.setSex(1);//默认设置为1表示为男性
        } else {
            bjtParkCompanyUser.setSex(Integer.parseInt(params.get("sex").toString()));
        }

        bjtParkCompanyUser.setType(1);//1是普通员工，2是工作台管理员，3是公司管理员
        if (parkCompanyUserService.insert(bjtParkCompanyUser)) return new BaseResult(BaseResultEnum.SUCCESS, "加入公司成功");
        return new BaseResult(BaseResultEnum.ERROR, "添加失败");
    }

    //修改员工信息公共方法
    private BaseResult update(BjtParkCompanyUser bjtParkCompanyUser, String userId) {
        BjtParkCompanyUser companyUser = parkCompanyUserService.selectOne(new EntityWrapper<BjtParkCompanyUser>().eq("user_id", bjtParkCompanyUser.getUserId()).eq("company_user_id", bjtParkCompanyUser.getCompanyUserId()));
        if (companyUser == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前员工信息不存在");
        }
        companyUser.setDepartment(bjtParkCompanyUser.getDepartment());
        companyUser.setJob(bjtParkCompanyUser.getJob());
        companyUser.setRealname(bjtParkCompanyUser.getRealname());
        companyUser.setSex(bjtParkCompanyUser.getSex());
        if (parkCompanyUserService.updateById(companyUser)) {
            return new BaseResult(BaseResultEnum.SUCCESS, "修改成功");
        }
        return new BaseResult(BaseResultEnum.ERROR, "修改失败");
    }
}
