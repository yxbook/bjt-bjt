package com.j4sc.bjt.api.server.controller;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.ParkCompanyAdmissionClient;
import com.j4sc.bjt.api.server.client.ParkCompanyClient;
import com.j4sc.bjt.api.server.client.ParkCompanyUserClient;
import com.j4sc.bjt.api.server.client.UserManageClient;
import com.j4sc.bjt.park.dao.entity.BjtParkCompany;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyAdmission;
import com.j4sc.bjt.park.dao.entity.BjtParkCompanyUser;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/4/4 14:49
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/company")
@Api(tags = {"帮家团公司服务"}, description = "帮家团公司服务 - 需授权")
public class ApiCompanyController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCompanyController.class);

    @Autowired
    ParkCompanyClient parkCompanyClient;
    @Autowired
    ParkCompanyUserClient parkCompanyUserClient;
    @Autowired
    UserManageClient userManageClient;
    @Autowired
    ParkCompanyAdmissionClient parkCompanyAdmissionClient;

    @ApiOperation(value = "新建公司", notes = "新建公司")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "公司名称", name = "name", required = true),
            @ApiImplicitParam(value = "地址", name = "address", required = true),
            @ApiImplicitParam(value = "行业", name = "industry")
    })
    @RequestMapping(value = "add/Company", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addCompany(@RequestBody Map<String, Object> params) {
        LOGGER.info(" 新建公司...");
        System.out.println("-------------------------" + params.toString());
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on((String) params.get("name"), new NotNullValidator("公司名称"))
                .on((String) params.get("address"), new NotNullValidator("公司地址"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(getUserId());
        if (userBaseResult.getData() == null || userBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus())
            return new BaseResult(BaseResultEnum.ERROR, "当前用户不存在");
        if (userBaseResult.getData().getRealname() == null || "".equals(userBaseResult.getData().getRealname())) {
            return new BaseResult(BaseResultEnum.ERROR, "用户姓名为空");
        }
        if (userBaseResult.getData().getIdNumber() == null || "".equals(userBaseResult.getData().getIdNumber())) {
            return new BaseResult(BaseResultEnum.ERROR, "用户身份证号为空");
        }
        if (userBaseResult.getData().getPhone() == null || "".equals(userBaseResult.getData().getPhone())) {
            return new BaseResult(BaseResultEnum.ERROR, "用户手机号为空");
        }
        params.put("phone", userBaseResult.getData().getUsername());
        params.put("userId", userBaseResult.getData().getUserId());
        params.put("realname", userBaseResult.getData().getRealname());
        params.put("idNumber", userBaseResult.getData().getIdNumber());

        return parkCompanyClient.saveCompany(params);
    }

    @ApiOperation(value = "修改公司信息", notes = "修改公司信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "公司编号", name = "companyId", required = true),
            @ApiImplicitParam(value = "公司地址", name = "address", required = true),
    })
    @RequestMapping(value = "update/Company", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateCompany(@RequestBody BjtParkCompany bjtParkCompany) {
        LOGGER.info(" 修改公司信息...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(bjtParkCompany.getName(), new NotNullValidator("公司名称"))
                .on(String.valueOf(bjtParkCompany.getCompanyId()), new NotNullValidator("公司编号"))
                .on(bjtParkCompany.getAddress(), new NotNullValidator("公司地址"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtParkCompany> parkCompanyBaseResult = parkCompanyClient.selectById(String.valueOf(bjtParkCompany.getCompanyId()));
        if (parkCompanyBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus() || parkCompanyBaseResult.getData() == null)
            return parkCompanyBaseResult;

        bjtParkCompany.setUtime(System.currentTimeMillis());
        return parkCompanyClient.updateById(bjtParkCompany);
    }

    @ApiOperation(value = "删除公司信息", notes = "删除公司信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "公司编号", name = "companyId", required = true),
    })
    @RequestMapping(value = "delete/Company", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult deleteCompany(@RequestBody Map<String, Object> params) {
        LOGGER.info(" 删除公司信息 ");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(params.get("companyId").toString(), new NotNullValidator("公司编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return parkCompanyClient.deleteCompany(params.get("companyId").toString(), getUserId());
    }

    @ApiOperation(value = "根据ID查询公司信息", notes = "根据ID查询公司信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "公司编号", name = "companyId", required = true)
    })
    @RequestMapping(value = "select/Company", method = RequestMethod.GET)
    public BaseResult<BjtParkCompany> selectCompany(@RequestParam("companyId") String companyId) {
        LOGGER.info(" 查询公司信息...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(companyId, new NotNullValidator("公司编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult baseResult = parkCompanyClient.selectById(companyId);
        return baseResult;
    }

    @ApiOperation(value = "查询公司入驻楼宇信息", notes = "查询公司入驻楼宇信息")
    @RequestMapping(value = "select/CompanyAdmission", method = RequestMethod.GET)
    public BaseResult<BjtParkCompanyAdmission> selectCompanyAdmission() {
        LOGGER.info(" 查询公司入驻楼宇信息...");
        System.out.println("---" + getUserId());
        return parkCompanyAdmissionClient.selectCompanyAdmission(getUserId());
    }


    @ApiOperation(value = "公司申请入驻楼宇", notes = "公司申请入驻楼宇")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "邀请码", name = "code", required = true),
    })
    @RequestMapping(value = "save/CompanyAdmission", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCompanyAdmission(@RequestBody Map<String, Object> params) {
        LOGGER.info(" 公司申请入驻楼宇...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on((String) params.get("code"), new NotNullValidator("邀请码"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(getUserId());
        if (userBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus() || userBaseResult.getData() == null)
            return userBaseResult;
        params.put("userId", getUserId());
        params.put("realname", userBaseResult.getData().getRealname());
        return parkCompanyClient.saveCompanyAdmission(params);
    }

    @ApiOperation(value = "公司管理员获取楼宇管理者联系方式")
    @RequestMapping(value = "select/BuildUserInfo", method = RequestMethod.GET)
    public BaseResult<Map<String, String>> selectBuildUserInfo() {
        LOGGER.info(" 公司管理员获取楼宇管理者联系方式...");
        return parkCompanyClient.selectBuildUserInfo(getUserId());
    }

    @ApiOperation(value = "用户申请加入公司", notes = "用户申请加入公司")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "公司编号", name = "companyId", required = true)
    })
    @RequestMapping(value = "save/CompanyUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCompanyUser(@RequestBody Map<String, Object> params) {
        LOGGER.info(" 用户申请加入公司...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on((String) params.get("companyId"), new NotNullValidator("公司编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(getUserId());
        if (userBaseResult.getData() == null || userBaseResult.status != BaseResultEnum.SUCCESS.getStatus())
            return new BaseResult(BaseResultEnum.ERROR, "当前用户不存在");
        if (userBaseResult.getData().getRealname() == null || "".equals(userBaseResult.getData().getRealname())) {
            return new BaseResult(BaseResultEnum.ERROR, "用户姓名为空");
        }
        if (userBaseResult.getData().getIdNumber() == null || "".equals(userBaseResult.getData().getIdNumber())) {
            return new BaseResult(BaseResultEnum.ERROR, "用户身份证号为空");
        }
        if (userBaseResult.getData().getPhone() == null || "".equals(userBaseResult.getData().getPhone())) {
            return new BaseResult(BaseResultEnum.ERROR, "用户手机号为空");
        }
        params.put("phone", userBaseResult.getData().getPhone());
        params.put("userId", userBaseResult.getData().getUserId());
        params.put("realname", userBaseResult.getData().getRealname());
        params.put("idNumber", userBaseResult.getData().getIdNumber());
        params.put("sex", userBaseResult.getData().getSex());
        return parkCompanyUserClient.saveCompanyUser(params);
    }

    @ApiOperation(value = "公司管理员添加员工")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "员工姓名", name = "realname", required = true),
            @ApiImplicitParam(value = "手机号", name = "phone", required = true),
            @ApiImplicitParam(value = "职务", name = "job"),
            @ApiImplicitParam(value = "部门", name = "department"),
            @ApiImplicitParam(value = "身份证号", name = "idNumber", required = true),
            @ApiImplicitParam(value = "性别：整型，1表示男，2表示女", name = "sex")
    })
    @RequestMapping(value = "save/CompanyUserByAdmin", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCompanyUserByAdmin(@RequestBody Map<String, Object> params) {
        LOGGER.info(" 公司管理员添加员工...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on((String) params.get("realname"), new NotNullValidator("员工姓名"))
                .on((String) params.get("phone"), new NotNullValidator("手机号"))
                .on((String) params.get("idNumber"), new NotNullValidator("身份证号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUsername(params.get("phone") + "");
        if (userBaseResult.getData() == null || userBaseResult.status != BaseResultEnum.SUCCESS.getStatus())
            return new BaseResult(BaseResultEnum.ERROR, "当前用户不存在");
        if (userBaseResult.getData().getRealname() == null || "".equals(userBaseResult.getData().getRealname())) {
            return new BaseResult(BaseResultEnum.ERROR, "用户姓名为空");
        }
        if (userBaseResult.getData().getIdNumber() == null || "".equals(userBaseResult.getData().getIdNumber())) {
            return new BaseResult(BaseResultEnum.ERROR, "用户身份证号为空");
        }
        if (!userBaseResult.getData().getIdNumber().equals(params.get("idNumber").toString())) {
            return new BaseResult(BaseResultEnum.ERROR, "身份证号不匹配");
        }
        if (userBaseResult.getData().getPhone() == null || "".equals(userBaseResult.getData().getPhone())) {
            return new BaseResult(BaseResultEnum.ERROR, "用户手机号为空");
        }
        params.put("phone", userBaseResult.getData().getPhone());
        params.put("userId", userBaseResult.getData().getUserId());
        params.put("realname", userBaseResult.getData().getRealname());
        params.put("idNumber", userBaseResult.getData().getIdNumber());
        params.put("adminUserId", getUserId());
        return parkCompanyUserClient.saveCompanyUserByAdmin(params);
    }

    @ApiOperation(value = "修改员工信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "编号", name = "companyUserId", required = true),
            @ApiImplicitParam(value = "姓名", name = "realname", required = true),
            @ApiImplicitParam(value = "用户编号", name = "userId", required = true),
            @ApiImplicitParam(value = "职位", name = "job"),
            @ApiImplicitParam(value = "部门", name = "department"),
            @ApiImplicitParam(value = "性别：整型，1表示男，2表示女", name = "sex")
    })
    @RequestMapping(value = "update/CompanyUser", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateCompanyUser(@RequestBody BjtParkCompanyUser bjtParkCompanyUser) {
        LOGGER.info(" 修改员工信息...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(bjtParkCompanyUser.getCompanyUserId() + "", new NotNullValidator("编号"))
                .on(bjtParkCompanyUser.getRealname() + "", new NotNullValidator("姓名"))
                .on(bjtParkCompanyUser.getUserId() + "", new NotNullValidator("用户编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return parkCompanyUserClient.updateCompanyUser(bjtParkCompanyUser, getUserId());
    }

    @ApiOperation(value = "给员工添加管理员权限", notes = "给员工添加管理员权限")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "被添加者的用户编号数组", name = "userIdList", required = true)
    })
    @RequestMapping(value = "save/CompanyAdminUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCompanyAdminUser(@RequestBody List<String> userIdList) {
        LOGGER.info(" 给员工添加管理员权限...");//可进行多个增加
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(userIdList.toString(), new NotNullValidator("被添加者的用户编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(getUserId());
        if (userBaseResult.getData() == null || userBaseResult.status != BaseResultEnum.SUCCESS.getStatus())
            return new BaseResult(BaseResultEnum.ERROR, "当前用户不存在");
        return parkCompanyUserClient.saveCompanyAdminUser(getUserId(), userIdList);
    }

    @ApiOperation(value = "工作台管理员权限转交")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "被转交者的用户编号", name = "userId", required = true)
    })
    @RequestMapping(value = "update/CompanyAdminUser", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateCompanyAdminUser(@RequestBody Map<String, Object> params) {
        LOGGER.info(" 工作台管理员权限转交...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on((String) params.get("userId"), new NotNullValidator("被转交者的用户编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(getUserId());
        if (userBaseResult.getData() == null || userBaseResult.status != BaseResultEnum.SUCCESS.getStatus())
            return new BaseResult(BaseResultEnum.ERROR, "当前用户不存在");
        params.put("adminUserId", getUserId());
        return parkCompanyUserClient.updateCompanyAdminUser(params);
    }

    @ApiOperation(value = "删除公司管理员权限", notes = "删除公司管理员权限")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "被删除者的用户编号数组", name = "userIdList", required = true)
    })
    @RequestMapping(value = "delete/CompanyAdminUser", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult deleteCompanyAdminUser(@RequestBody List<String> userIdList) {
        LOGGER.info(" 删除公司管理员权限...");//可进行多个删除
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(userIdList.toString(), new NotNullValidator("被删除者的用户编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> _adminUserBaseResult = userManageClient.findUserByUserId(getUserId());
        if (_adminUserBaseResult.getData() == null || _adminUserBaseResult.status != BaseResultEnum.SUCCESS.getStatus())
            return new BaseResult(BaseResultEnum.ERROR, "当前管理员不存在");

        return parkCompanyUserClient.deleteCompanyAdminUser(getUserId(), userIdList);
    }

    @ApiOperation(value = "删除公司员工", notes = "删除公司员工")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "被删除者的用户编号", name = "userId", required = true)
    })
    @RequestMapping(value = "delete/CompanyUser", method = RequestMethod.DELETE)
    public BaseResult deleteCompanyUser(@RequestParam("userId") String userId) {
        LOGGER.info(" 删除公司员工...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(userId, new NotNullValidator("被删除者的用户编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> _adminUserBaseResult = userManageClient.findUserByUserId(getUserId());
        if (_adminUserBaseResult.getData() == null || _adminUserBaseResult.status != BaseResultEnum.SUCCESS.getStatus())
            return new BaseResult(BaseResultEnum.ERROR, "当前管理员不存在");

        BaseResult<BjtUserUser> _userBaseResult = userManageClient.findUserByUserId(userId);
        if (_userBaseResult.getData() == null || _userBaseResult.status != BaseResultEnum.SUCCESS.getStatus())
            return new BaseResult(BaseResultEnum.ERROR, "当前用户不存在");
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("adminUserId", getUserId());
        return parkCompanyUserClient.deleteCompanyUser(params);
    }

    @ApiOperation(value = "员工申请退出公司", notes = "员工申请退出公司")
    @RequestMapping(value = "delete/CompanyUserSelf", method = RequestMethod.DELETE)
    public BaseResult deleteCompanyUserSelf() {
        LOGGER.info(" 员工申请退出公司...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(getUserId(), new NotNullValidator("用户编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> _userBaseResult = userManageClient.findUserByUserId(getUserId());
        if (_userBaseResult.getData() == null || _userBaseResult.status != BaseResultEnum.SUCCESS.getStatus())
            return new BaseResult(BaseResultEnum.ERROR, "当前用户不存在");

        return parkCompanyUserClient.deleteCompanyUserSelf(getUserId());
    }

    @ApiOperation(value = "查询员工列表", notes = "查询员工列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
            @ApiImplicitParam(name = "key", value = "关键词"),
            @ApiImplicitParam(name = "type", value = "查询类型：1表示查询所有员工、2表示没有开门权限的员工列表、3表示该公司的所有管理员、4、该公司所有非管理员", dataType = "int"),
    })
    @RequestMapping(value = "select/PageCompanyUser", method = RequestMethod.GET)
    public BaseResult<Page<BjtParkCompanyUser>> selectPageCompanyUser(@RequestParam Map<String, Object> params) {
        LOGGER.info(" 查询员工列表信息...");
        params.put("userId", getUserId());
        return parkCompanyUserClient.selectPageCompanyUser(params);
    }

    @ApiOperation(value = "扫码添加员工时获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户账号", name = "username", required = true)
    })
    @RequestMapping(value = "select/UserUser", method = RequestMethod.GET)
    public BaseResult<BjtUserUser> selectBjtUserUser(@RequestParam("username") String username) {
        LOGGER.info(" 扫码添加员工时获取用户信息...");
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUsername(username);
        if (userBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus() || userBaseResult.getData() == null) {
            return userBaseResult;
        }
        userBaseResult.getData().setPassword("");
        userBaseResult.getData().setSalt("");
        return userBaseResult;
    }
}
