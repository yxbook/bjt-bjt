package com.j4sc.bjt.user.server.rest.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.j4sc.bjt.user.common.UserNameValidation;
import com.j4sc.bjt.user.dao.entity.*;
import com.j4sc.bjt.user.rest.api.*;
import com.j4sc.bjt.user.server.client.J4scAuthClient;
import com.j4sc.bjt.user.server.util.ValidationIdCard;
import com.j4sc.common.annotation.BaseService;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import com.j4sc.common.util.MD5Util;
import com.j4sc.common.util.MapUtil;
import com.j4sc.common.validator.LengthValidator;
import com.j4sc.common.validator.NotNullValidator;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/3/29 0029 下午 3:13
 * @Version: 1.0
 **/
@Service
@ApiModel
@Transactional
@BaseService
@RestController
@RequestMapping(value = "api", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"用户管理"}, description = "用户管理")
public class BjtUserApiServiceImpl implements BjtUserApiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtUserApiServiceImpl.class);

    @Autowired
    BjtUserUserService userService;
    @Autowired
    J4scAuthClient j4scAuthClient;
    @Autowired
    BjtUserRoleService roleService;
    @Autowired
    BjtUserUserRoleService userUserRoleService;
    @Autowired
    BjtUserOrganizationService organizationService;
    @Autowired
    BjtUserPermissionService userPermissionService;
    @Autowired
    BjtUserWalletService userWalletService;
    @Autowired
    BjtUserRechargeService userRechargeService;
    @Autowired
    BjtUserConsumptionService userConsumptionService;
    @Autowired
    BjtUserFeedbackService userFeedBackService;
    @Autowired
    BjtUserBillService userBillService;
    @Autowired
    BjtUserPermissionUserService userPermissionUserService;
    @Autowired
    BjtUserRolePermissionService userRolePermissionService;


    @ApiOperation(value = "管理用户登录", notes = "登录之后获取token，根据项目约定使用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "用户密码", required = true)
    })
    @Override
    public BaseResult<BjtUserUser> login(@RequestBody BjtUserUser bjtUserUser) {
        LOGGER.info("登录输入参数-BjtUserUser={}", JSON.toJSONString(bjtUserUser));
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(bjtUserUser.getUsername(), new LengthValidator(1, 20, "用户名"))
                .on(bjtUserUser.getPassword(), new LengthValidator(5, 32, "密码"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        // 查询用户信息
        BjtUserUser _bjtUserUser = userService.selectOne(new EntityWrapper<BjtUserUser>().eq("username", bjtUserUser.getUsername()));
        if (null == _bjtUserUser) {
            return new BaseResult(BaseResultEnum.ERROR, "账号或密码错误！");
        }
        if (!_bjtUserUser.getPassword().equals(MD5Util.md5(bjtUserUser.getPassword() + _bjtUserUser.getSalt()))) {
            return new BaseResult(BaseResultEnum.ERROR, "账号或密码错误！");
        }
        if (_bjtUserUser.getLocked() == 1) {
            return new BaseResult(BaseResultEnum.ERROR, "账号被锁定！");
        }
        return new BaseResult(BaseResultEnum.SUCCESS, _bjtUserUser);
    }

    @ApiOperation(value = "用户注册", notes = "用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户编号"),
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "用户密码", required = true),
            @ApiImplicitParam(name = "realname", value = "姓名"),
            @ApiImplicitParam(name = "avatar", value = "头像"),
            @ApiImplicitParam(name = "phone", value = "电话"),
            @ApiImplicitParam(name = "email", value = "邮箱"),
            @ApiImplicitParam(name = "sex", value = "性别"),
            @ApiImplicitParam(name = "remark", value = "备注"),
            @ApiImplicitParam(name = "openId", value = "openId"),
            @ApiImplicitParam(name = "nickname", value = "昵称"),
            @ApiImplicitParam(name = "birthday", value = "生日"),
            @ApiImplicitParam(name = "company", value = "公司"),
            @ApiImplicitParam(name = "job", value = "职务")
    })
    @Override
    public BaseResult<BjtUserUser> saveUser(@RequestBody BjtUserUser bjtUserUser) {
        LOGGER.error(bjtUserUser.toString());
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(bjtUserUser.getUsername(), new LengthValidator(1, 20, "用户名"))
                .on(bjtUserUser.getPassword(), new LengthValidator(5, 32, "密码"))
                .on(bjtUserUser.getPhone(), new NotNullValidator("电话"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }

        //检测当前用户名是否存在
        BjtUserUser _bjtUserUser = userService.selectOne(new EntityWrapper<BjtUserUser>().eq("username", bjtUserUser.getUsername()));
        if (_bjtUserUser != null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前用户名已存在!");
        }

        bjtUserUser.setCtime(System.currentTimeMillis());
        bjtUserUser.setLocked(0);
        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        bjtUserUser.setSalt(salt);
        bjtUserUser.setPassword(MD5Util.md5(bjtUserUser.getPassword() + bjtUserUser.getSalt()));
        boolean result = userService.insert(bjtUserUser);

        //初始化钱包
        BjtUserWallet bjtUserWallet = new BjtUserWallet();
        bjtUserWallet.setConsumption(new BigDecimal(0));
        bjtUserWallet.setBalance(new BigDecimal(0));
        bjtUserWallet.setRecharge(new BigDecimal(0));
        bjtUserWallet.setScore(0);
        bjtUserWallet.setUserId(bjtUserUser.getUserId());
        bjtUserWallet.setCtime(System.currentTimeMillis());

        userWalletService.insert(bjtUserWallet);

        return result ? new BaseResult<BjtUserUser>(BaseResultEnum.SUCCESS, bjtUserUser) : new BaseResult(BaseResultEnum.ERROR, "注册或修改用户失败!");
    }

    @ApiOperation(value = "用户修改", notes = "用户修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户编号"),
            @ApiImplicitParam(name = "realname", value = "姓名"),
            @ApiImplicitParam(name = "avatar", value = "头像"),
            @ApiImplicitParam(name = "phone", value = "电话"),
            @ApiImplicitParam(name = "email", value = "邮箱"),
            @ApiImplicitParam(name = "sex", value = "性别"),
            @ApiImplicitParam(name = "remark", value = "备注"),
            @ApiImplicitParam(name = "openId", value = "openId"),
            @ApiImplicitParam(name = "nickname", value = "昵称"),
            @ApiImplicitParam(name = "birthday", value = "生日"),
            @ApiImplicitParam(name = "company", value = "公司"),
            @ApiImplicitParam(name = "job", value = "职务")
    })
    @Override
    public BaseResult<BjtUserUser> updateUser(@RequestBody BjtUserUser bjtUserUser) {
        LOGGER.error(bjtUserUser.toString());
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(bjtUserUser.getUsername(), new LengthValidator(11, 13, "用户名"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BjtUserUser userUser = userService.selectOne(new EntityWrapper<BjtUserUser>().eq("user_id", bjtUserUser.getUserId()));
        if (userUser == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前用户不存在");
        }
        if (userUser.getIdNumber() != null && !"".equals(userUser.getIdNumber())) {
            bjtUserUser.setIdNumber(userUser.getIdNumber());
        }
        if (userUser.getIdNumber() == null || "".equals(userUser.getIdNumber())) {
            if (bjtUserUser.getIdNumber() != null && !"".equals(userUser.getIdNumber())) {
                if (!ValidationIdCard.verify(bjtUserUser.getIdNumber())) {
                    return new BaseResult(BaseResultEnum.ERROR, "身份证号错误");
                }
            }
        }
        boolean result = userService.update(bjtUserUser, new EntityWrapper<BjtUserUser>().eq("user_id", bjtUserUser.getUserId()));

        return result ? new BaseResult(BaseResultEnum.SUCCESS, bjtUserUser) : new BaseResult(BaseResultEnum.ERROR, "修改用户失败!");
    }
    @ApiOperation(value = "通过用户账号搜索用户")
    @Override
    public BaseResult<List<BjtUserUser>> selectUserList(@RequestParam("username") String username){
        List<BjtUserUser> list = userService.selectList(new EntityWrapper<BjtUserUser>().eq("username", username));
        list.forEach(v->{
            v.setIdNumber("");
            v.setPassword("");
            v.setSalt("");
            v.setOpenId("");
        });
        return new BaseResult(BaseResultEnum.SUCCESS, list);
    }
    @ApiOperation(value = "用户修改密码", notes = "用户修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户编号", required = true),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true),
            @ApiImplicitParam(name = "oldPassword", value = "原密码", required = true)
    })
    @Override
    public BaseResult updatePassword(@RequestBody Map<String, Object> params) {
        BjtUserUser _bjtUserUser = userService.selectOne(new EntityWrapper<BjtUserUser>().eq("user_id", params.get("userId")));

        if (_bjtUserUser == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前用户为空！");
        }
        if (!_bjtUserUser.getPassword().equals(MD5Util.md5(params.get("oldPassword") + _bjtUserUser.getSalt()))) {
            return new BaseResult(BaseResultEnum.ERROR, "原密码输入错误！");
        }
        _bjtUserUser.setPassword(MD5Util.md5(params.get("newPassword") + _bjtUserUser.getSalt()));

        boolean result = userService.update(_bjtUserUser, new EntityWrapper<BjtUserUser>().eq("user_id", params.get("userId")));

        return result ? new BaseResult(BaseResultEnum.SUCCESS, "密码修改成功！") : new BaseResult(BaseResultEnum.ERROR, "密码修改失败！");
    }

    @ApiOperation(value = "查询用户信息", notes = "存在返回200，不存在返回503")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户编号", required = true, dataType = "String"),
    })
    @Override
    public BaseResult<BjtUserUser> findUserByUserId(@RequestParam("userId") String userId) {
        if (StringUtils.isBlank(userId)) {
            return new BaseResult(BaseResultEnum.ERROR, "用户编号不能为空！");
        }
        BjtUserUser _bjtUserUser = userService.selectOne(new EntityWrapper<BjtUserUser>().eq("user_id", userId));

        return _bjtUserUser == null ? new BaseResult(BaseResultEnum.ERROR, "当前用户不存在！") : new BaseResult<BjtUserUser>(BaseResultEnum.SUCCESS, _bjtUserUser);
    }

    @ApiOperation(value = "通过手机号查询用户", notes = "通过手机号查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String"),
    })
    @Override
    public BaseResult findUserByPhone(@RequestParam("phone") String phone) {
        if (StringUtils.isBlank(phone)) {
            return new BaseResult(BaseResultEnum.ERROR, "手机号不能为空！");
        }
        BjtUserUser bjtUserUser = userService.selectOne(new EntityWrapper<BjtUserUser>().eq("phone", phone));

        return bjtUserUser == null ? new BaseResult(BaseResultEnum.SUCCESS, "当前手机号不存在！") : new BaseResult(BaseResultEnum.ERROR, "当前手机号存在！");
    }

    @ApiOperation(value = "通过手机号查询用户Id", notes = "通过手机号查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String"),
    })
    @Override
    public BaseResult<BjtUserUser> findUserByUserTel(@RequestParam("phone") String phone) {
        if (StringUtils.isBlank(phone)) {
            return new BaseResult(BaseResultEnum.ERROR, "手机号不能为空！");
        }
        BjtUserUser bjtUserUser = userService.selectOne(new EntityWrapper<BjtUserUser>().eq("phone", phone));

        return bjtUserUser != null ? new BaseResult(BaseResultEnum.SUCCESS, bjtUserUser) : new BaseResult(BaseResultEnum.ERROR, "当前用户不存在");
    }

    @ApiOperation(value = "通过账号查询用户", notes = "通过账号查询用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户账号", required = true, dataType = "String"),
    })
    @Override
    public BaseResult<BjtUserUser> findUserByUsername(@RequestParam("username") String username) {
        if (StringUtils.isBlank(username)) {
            return new BaseResult(BaseResultEnum.ERROR, "账号不能为空！");
        }
        BjtUserUser bjtUserUser = userService.selectOne(new EntityWrapper<BjtUserUser>().eq("username", username));
        return new BaseResult(BaseResultEnum.SUCCESS, bjtUserUser);
    }

    @ApiOperation(value = "删除用户信息", notes = "用户编号用,拼接")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userIds", value = "用户编号", required = true, dataType = "String"),
    })
    @Override
    public BaseResult deleteUsers(@RequestParam("userIds") String userIds) {
        if (StringUtils.isBlank(userIds)) {
            return new BaseResult<>(BaseResultEnum.ERROR, "用户编号不能为空！");
        }
        List<String> idList = new ArrayList<>();
        String[] list = userIds.split(",");
        for (String id : list) {
            idList.add(id);
        }
        boolean result = userService.deleteBatchIds(idList);
        return result ? new BaseResult<>(BaseResultEnum.SUCCESS, "删除用户成功！") : new BaseResult<>(BaseResultEnum.ERROR, "删除用户失败！");
    }


    @ApiOperation(value = "通过用户ID查询用户列表", notes = "通过用户ID查询用户列表")
    @ApiImplicitParams({
    })
    @Override
    public BaseResult<List<BjtUserUser>> findListByIdList(@RequestBody List<String> list) {
//        List<String> list =  (List<String>)JSONUtils.parse(idList);
        System.out.println("---------------------" + JSONUtils.toJSONString(list));
        List<BjtUserUser> userList = userService.selectList(new EntityWrapper<BjtUserUser>().in("user_id", list));
        return new BaseResult(BaseResultEnum.SUCCESS, userList);
    }

    //   ==================================================
    @Override
    public BaseResult<List<BjtUserPermission>> getAllPermission() {
        return new BaseResult(BaseResultEnum.SUCCESS, userPermissionService.selectList(new EntityWrapper<>()));
    }

    @Override
    public BaseResult<List<BjtUserPermission>> selectPermissionByUserId(@RequestParam("userId") String userId, @RequestParam("buildId") String buildId) {
        //信息校验
        ComplexResult result = FluentValidator.checkAll()
                .on(userId, new NotNullValidator("用户"))
                .on(buildId, new NotNullValidator("楼宇"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, result.getErrors().get(0).getErrorMsg());
        }
        // 用户不存在或锁定状态
        BjtUserUser bjtUserUser = userService.selectById(userId);
        if (null == bjtUserUser || 1 == bjtUserUser.getLocked()) {
            LOGGER.info("selectPermissionByUserId : authUserId={}", userId);
            return null;
        }
        List<BjtUserPermissionUser> bjtUserPermissionUserList = userPermissionUserService.selectList(new EntityWrapper<BjtUserPermissionUser>().eq("user_id", userId)
                .eq("build_Id", buildId));
        List<Integer> inpermissionIdList = new ArrayList<Integer>();
        List<Integer> unpermissionIdList = new ArrayList<Integer>();
        bjtUserPermissionUserList.forEach(v -> {
            if (v.getType() == 1) inpermissionIdList.add(v.getPermissionId());
            else unpermissionIdList.add(v.getPermissionId());
        });
        List<BjtUserUserRole> bjtUserRoleList = userUserRoleService.selectList(new EntityWrapper<BjtUserUserRole>().eq("user_id", userId)
                .eq("build_Id", buildId));
        List<Integer> roleIdList = new ArrayList<Integer>();
        bjtUserRoleList.forEach(v -> {
            roleIdList.add(v.getRoleId());
        });
        if (roleIdList.size() != 0) {
            List<BjtUserRolePermission> authRolePermissionList = userRolePermissionService.selectList(new EntityWrapper<BjtUserRolePermission>().in("role_id", roleIdList));
            authRolePermissionList.forEach(v -> {
                inpermissionIdList.add(v.getPermissionId());
            });
        }
        if (inpermissionIdList.size() == 0) return new BaseResult(BaseResultEnum.SUCCESS, new ArrayList<>());

        return new BaseResult(BaseResultEnum.SUCCESS, userPermissionService.selectList(new EntityWrapper<BjtUserPermission>().in("permission_id",
                inpermissionIdList).notIn("permission_id", unpermissionIdList).eq("status", 1)));
    }

    @Override
    @Cacheable(value = "bjt-user-server-ehcache", key = "'selectPermissionByUserId_' + #userId + #buildId ")
    public BaseResult<List<BjtUserPermission>> selectPermissionByUserIdByCache(@RequestParam("userId") String userId, @RequestParam("buildId") String buildId) {
        return selectPermissionByUserId(userId, buildId);
    }

    @Override
    public BaseResult<List<BjtUserRole>> selectBjtUserRoleByUserId(@RequestParam("userId") String userId, @RequestParam("buildId") String buildId) {
        //信息校验
        ComplexResult result = FluentValidator.checkAll()
                .on(userId, new NotNullValidator("用户"))
                .on(buildId, new NotNullValidator("楼宇"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!result.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, result.getErrors().get(0).getErrorMsg());
        }
        // 用户不存在或锁定状态
        BjtUserUser bjtUserUser = userService.selectById(userId);
        if (null == bjtUserUser || 1 == bjtUserUser.getLocked()) {
            LOGGER.info("selectPermissionByUserId : authUserId={}", userId);
            return null;
        }

        List<BjtUserUserRole> authUserRoleList = userUserRoleService.selectList(new EntityWrapper<BjtUserUserRole>().eq("user_id", userId)
                .eq("build_Id", buildId));
        List<Integer> roleIdList = new ArrayList<Integer>();
        authUserRoleList.forEach(v -> {
            roleIdList.add(v.getRoleId());
        });
        return new BaseResult(BaseResultEnum.SUCCESS,
                roleService.selectList(new EntityWrapper<BjtUserRole>().in("role_id", roleIdList)));
    }

    @Override
    @Cacheable(value = "bjt-user-server-ehcache", key = "'selectBjtUserRoleByUserId' + #userId + #buildId ")
    public BaseResult<List<BjtUserRole>> selectBjtUserRoleByUserIdByCache(@RequestParam("userId") String userId, @RequestParam("buildId") String buildId) {
        return selectBjtUserRoleByUserId(userId, userId);
    }

    @Override
    public BaseResult<List<BjtUserRolePermission>> selectBjtUserRolePermisstionByBjtUserRoleId(String roleId) {
        return new BaseResult(BaseResultEnum.SUCCESS,
                userRolePermissionService.selectList(new EntityWrapper<BjtUserRolePermission>().eq("role_id", roleId)));
    }

    @Override
    public BaseResult<List<Map<String, Object>>> findUserBuildInfoByBU(@RequestBody Map<String, Object> params) {
//        try {
//            List<BjtUserUser> list2 =MapUtil.mapsToObjects((List<Map<String,Object>>) params.get("list"),BjtUserUser.class);
//            LOGGER.error(buildId +";"+list2.get(0).getUsername());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        //Integer buildId = Integer.parseInt((String)params.get("buildId"));
        List<String> list = (List<String>) params.get("list");
        if (list.size() == 0) return new BaseResult(BaseResultEnum.SUCCESS,
                list);
        //LOGGER.error(buildId +";"+list);
        List<BjtUserUser> userList = userService.selectList(new EntityWrapper<BjtUserUser>().in("user_id", list));
        List<BjtUserUserRole> userRoleList = userUserRoleService.selectList(new EntityWrapper<BjtUserUserRole>().eq("build_id", params.get("buildId")).in("user_id", list));
        List<Map<String, Object>> resultList = new ArrayList<>();
        userList.forEach(user -> {
            Map<String, Object> map = MapUtil.beanToMap(user);
            map.put("roles", userRoleList.stream().filter(obj -> obj.getUserId().equals(user.getUserId())).toArray());
            resultList.add(map);
        });
        return new BaseResult(BaseResultEnum.SUCCESS,
                resultList);
    }

    @Override
    public BaseResult removeRolePermissionByBU(@RequestBody Map<String, Object> params) {
        userPermissionUserService.delete(new EntityWrapper<BjtUserPermissionUser>().eq("user_Id", params.get("user_Id"))
                .eq("build_Id", params.get("build_Id")));
        userUserRoleService.delete(new EntityWrapper<BjtUserUserRole>().eq("user_Id", params.get("user_Id"))
                .eq("build_Id", params.get("build_Id")));
        return new BaseResult(BaseResultEnum.SUCCESS, "");
    }

    @ApiOperation(value = "查看用户余额")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户编号", required = true, dataType = "String"),
    })
    @Override
    public BaseResult<BjtUserWallet> findWallet(@RequestParam("userId") String userId) {
        if (StringUtils.isBlank(userId)) {
            return new BaseResult(BaseResultEnum.ERROR, "用户编号不能为空！");
        }
        BjtUserWallet wallet = this.userWalletService.selectOne(new EntityWrapper<BjtUserWallet>().eq("user_id", userId));

        return new BaseResult(BaseResultEnum.SUCCESS, wallet);
    }

    @ApiOperation(value = "用户提交反馈", notes = "用户提交反馈")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "content", value = "反馈内容", required = true, dataType = "String"),
    })
    @Override
    public BaseResult saveFeedBack(@RequestBody BjtUserFeedback bjtUserFeedback) {
        boolean result = userFeedBackService.insert(bjtUserFeedback);
        return result ? new BaseResult(BaseResultEnum.SUCCESS, "提交反馈成功！") : new BaseResult(BaseResultEnum.SUCCESS, "提交反馈失败！");
    }


}
