package com.j4sc.bjt.api.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.j4sc.auth.dao.entity.AuthUser;
import com.j4sc.bjt.api.common.constant.DownloadImage;
import com.j4sc.bjt.api.common.constant.IdcardValidation;
import com.j4sc.bjt.api.server.client.*;
import com.j4sc.bjt.park.dao.entity.BjtParkBuild;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildGuard;
import com.j4sc.bjt.park.dao.entity.BjtParkBuildVisitor;
import com.j4sc.bjt.system.dao.entity.BjtSystemDownload;
import com.j4sc.bjt.user.dao.entity.BjtUserUser;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import com.j4sc.common.util.AESUtil;
import com.j4sc.common.util.MD5Util;
import com.j4sc.common.util.NumberCheckUtil;
import com.j4sc.common.util.RandomUtil;
import com.j4sc.common.validator.NotNullValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.j4sc.bjt.api.common.constant.CommonConstants.CACHE_PASSWORD_VERIFICATION_CODE;
import static com.j4sc.bjt.api.common.constant.CommonConstants.CACHE_REGISTER_VERIFICATION_CODE;
import static com.j4sc.bjt.api.common.constant.CommonConstants.CACHE_VISITOR_VERIFICATION_CODE;

//import com.j4sc.auth.rest.api.AuthJWTService;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/3/30 16:40
 * @Version: 1.0
 **/
@Controller
@RequestMapping(value = "auth")
@Api(tags = {"帮家团系统鉴权服务"}, description = "帮家团系统鉴权服务 - 无需授权")
public class ApiAuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiAuthController.class);
    @Autowired
    AuthLogClient authLogClient;
    @Autowired
    AuthJWTClient authJWTClient;
    @Autowired
    UserManageClient userManageClient;
    @Autowired
    SystemApiClient systemApiClient;
    @Autowired
    private ParkBuildVisitorClient parkBuildVisitorClient;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private SystemDownloadClient systemDownloadClient;
    @Autowired
    private ParkBuildGateClient parkBuildGateClient;
    @Autowired
    private ParkBuildGuardClient parkBuildGuardClient;


    @Value("${imagePath}")
    private String path;

    @ApiOperation(value = "用户获取验证码", notes = "通过手机短信发送;code为验证码类型，都为5分钟时效" +
            "1 - 注册验证码验证时跟手机绑定" +
            "2 - 服务验证码" +
            "3 - 访客验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "codeType", value = "验证码类型", required = true, dataType = "int")
    })
    @ResponseBody
    @RequestMapping(value = "getRegisterCode", method = RequestMethod.GET)
    public BaseResult getRegisterCode(
            @RequestParam(value = "phone", required = true) String phone,
            @RequestParam(value = "codeType", required = true) int codeType) {
        //验证手机号是否正确
        if (!NumberCheckUtil.isChinaPhoneLegal(phone)) {
            return new BaseResult(BaseResultEnum.ERROR, "请检查手机号是否正确");
        }
        //手机号是否存在验证
        BaseResult<String> phoneResult = userManageClient.findUserByPhone(phone);
        switch (codeType) {
            case 1://注册
                if (phoneResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
                    return phoneResult;
                }
                //1分钟内是否已经发送过
                if (!redisTemplate.hasKey("getRegisterCode" + phone)) {
                    redisTemplate.opsForValue().set("getRegisterCode" + phone, "", 60, TimeUnit.SECONDS);
                    BaseResult code = systemApiClient.generateCode(phone, CACHE_REGISTER_VERIFICATION_CODE);
                    if (code.getStatus() == BaseResultEnum.SUCCESS.getStatus()) {
                        //发送短信代码
                        Map<String, Object> params = new HashMap<>();
                        params.put("code", code.getData());
                        params.put("tel", phone);
                        return systemApiClient.sendRegisterSMS(params);
                    } else {
                        return code;
                    }
                } else {
                    return new BaseResult(BaseResultEnum.ERROR, "每60S只能发送一次");
                }
            case 2://找回密码
                if (phoneResult.getStatus() == BaseResultEnum.SUCCESS.getStatus()) {
                    return phoneResult;
                }
                //1分钟内是否已经发送过
                if (!redisTemplate.hasKey("getPasswordCode" + phone)) {
                    redisTemplate.opsForValue().set("getPasswordCode" + phone, "", 60, TimeUnit.SECONDS);
                    BaseResult code = systemApiClient.generateCode(phone, CACHE_PASSWORD_VERIFICATION_CODE);
                    if (code.getStatus() == BaseResultEnum.SUCCESS.getStatus()) {
                        //发送短信代码
                        Map<String, Object> params = new HashMap<>();
                        params.put("code", code.getData());
                        params.put("tel", phone);
                        return systemApiClient.sendRegisterSMS(params);
                    } else {
                        return code;
                    }
                } else {
                    return new BaseResult(BaseResultEnum.ERROR, "每60S只能发送一次");
                }
            case 3://访客验证
                //1分钟内是否已经发送过
                if (!redisTemplate.hasKey("getVisitorCode" + phone)) {
                    redisTemplate.opsForValue().set("getVisitorCode" + phone, "", 60, TimeUnit.SECONDS);
                    BaseResult code = systemApiClient.generateCode(phone, CACHE_VISITOR_VERIFICATION_CODE);
                    if (code.getStatus() == BaseResultEnum.SUCCESS.getStatus()) {
                        //发送短信代码
                        Map<String, Object> params = new HashMap<>();
                        params.put("code", code.getData());
                        params.put("tel", phone);
                        return systemApiClient.sendRegisterSMS(params);
                    } else {
                        return code;
                    }
                } else {
                    return new BaseResult(BaseResultEnum.ERROR, "每60S只能发送一次");
                }
            default:
                if (phoneResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
                    new BaseResult(BaseResultEnum.ERROR, "用户不存在");
                }

                return new BaseResult(BaseResultEnum.ERROR, "验证码类型不存在");
        }

    }

    //用户注册。
    @ApiOperation(value = "用户注册", notes = "注册成功之后可直接获取token，根据项目约定使用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "用户密码", required = true),
            @ApiImplicitParam(name = "phone", value = "手机", required = true),
            @ApiImplicitParam(name = "code", value = "注册验证码", required = true)
    })
    @ResponseBody
    @RequestMapping(value = "register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult register(@RequestBody Map<String, Object> params) {
        //验证码验证
        BaseResult<String> codeResult = systemApiClient.getCode(params.get("phone") + "", CACHE_REGISTER_VERIFICATION_CODE);
        if (codeResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return codeResult;
        }
        if (!codeResult.getData().equals(params.get("code"))) {
            return new BaseResult(BaseResultEnum.ERROR, "验证码错误");
        }
        //生成用户
        BjtUserUser _bjtUserUser = new BjtUserUser();
        _bjtUserUser.setUsername(params.get("username") + "");
        _bjtUserUser.setPassword(params.get("password") + "");
        _bjtUserUser.setPhone(params.get("phone") + "");
        BaseResult baseResult = userManageClient.saveUser(_bjtUserUser);
        if (baseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return baseResult;
        }
        systemApiClient.removeCode(params.get("phone") + "", CACHE_REGISTER_VERIFICATION_CODE);

        //获取token
        BaseResult<String> tokenResult = authJWTClient.getToken(params.get("username") + "", "邦家团用户", ((BjtUserUser) baseResult.getData()).getUserId());
        if (tokenResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return tokenResult;
        }
        return new BaseResult(BaseResultEnum.SUCCESS, AESUtil.aesEncode(tokenResult.getData()));
    }


    //登录。获取token
    @ApiOperation(value = "用户登录", notes = "登录之后获取token，根据项目约定使用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "用户密码", required = true)
    })
    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult createAuthenticationToken(@RequestBody BjtUserUser _bjtUserUser) {
        BaseResult baseResult = userManageClient.login(_bjtUserUser);
        LOGGER.info(baseResult.toString());
        if (baseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return baseResult;
        }
        //获取token
        BaseResult<String> tokenResult = authJWTClient.getToken(_bjtUserUser.getUsername(), "", ((BjtUserUser) baseResult.getData()).getUserId());
        LOGGER.info(tokenResult.toString());
        if (tokenResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return tokenResult;
        }
        return new BaseResult(BaseResultEnum.SUCCESS, AESUtil.aesEncode(tokenResult.getData()));
    }

    //修改密码
    @ApiOperation(value = "用户修改密码", notes = "修改密码，修改后不需要重新登录。后期可能会修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", required = true),
            @ApiImplicitParam(name = "oldPassword", value = "老用户密码", required = true),
            @ApiImplicitParam(name = "newPassword", value = "新用户密码", required = true)
    })
    @ResponseBody
    @RequestMapping(value = "updatePassword", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updatePassword(@RequestBody Map<String, Object> params) {
        LOGGER.info(" 修改密码...");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(params.get("oldPassword") + "", new NotNullValidator("旧密码"))
                .on(params.get("newPassword") + "", new NotNullValidator("新密码"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult baseResult = userManageClient.updatePassword(params);
        return baseResult;
    }


    @ApiOperation(value = "找回密码验证", notes = "找回密码验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "code", value = "验证码", required = true)
    })
    @ResponseBody
    @RequestMapping(value = "validateCode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult validateCode(@RequestBody Map<String, Object> params) {
        //验证码验证
        BaseResult<String> codeResult = systemApiClient.getCode(params.get("username") + "", CACHE_PASSWORD_VERIFICATION_CODE);
        if (codeResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return codeResult;
        }
        if (!codeResult.getData().equals(params.get("code"))) {
            return new BaseResult(BaseResultEnum.ERROR, "验证码错误");
        }
        return new BaseResult(BaseResultEnum.SUCCESS, "验证成功");
    }

    @ApiOperation(value = "访客申请验证", notes = "访客申请验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = true),
            @ApiImplicitParam(name = "code", value = "验证码", required = true)
    })
    @ResponseBody
    @RequestMapping(value = "visitorValidateCode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult visitorValidateCode(@RequestBody Map<String, Object> params) {
        //访客申请验证
        BaseResult<String> codeResult = systemApiClient.getCode(params.get("phone") + "", CACHE_VISITOR_VERIFICATION_CODE);
        if (codeResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return codeResult;
        }
        if (!codeResult.getData().equals(params.get("code"))) {
            return new BaseResult(BaseResultEnum.ERROR, "验证码错误");
        }
        return new BaseResult(BaseResultEnum.SUCCESS, "验证成功");
    }

    @ApiOperation(value = "找回密码", notes = "找回密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true),
    })
    @ResponseBody
    @RequestMapping(value = "getBackPassword", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult getBackPassword(@RequestBody Map<String, Object> params) {
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUsername(params.get("username") + "");
        if (userBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return userBaseResult;
        }
        if (userBaseResult.getData() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前用户不存在");
        }
        BjtUserUser user = userBaseResult.getData();
        user.setPassword(MD5Util.md5(params.get("newPassword") + user.getSalt()));
        if (userManageClient.updateUser(user).status != BaseResultEnum.SUCCESS.getStatus()) {
            return new BaseResult(BaseResultEnum.ERROR, "找回密码失败");
        }
        systemApiClient.removeCode(params.get("username") + "", CACHE_PASSWORD_VERIFICATION_CODE);//移除验证码
        return new BaseResult(BaseResultEnum.SUCCESS, "找回密码成功");
    }

    @ApiOperation(value = "访客申请提交")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "visitorPhone", value = "访客手机号", required = true),
            @ApiImplicitParam(name = "visitorName", value = "访客姓名", required = true),
            @ApiImplicitParam(name = "idcardFront", value = "身份证正面照path", required = true),
            @ApiImplicitParam(name = "idcardBack", value = "身份证反面照path", required = true),
            @ApiImplicitParam(name = "visitorFront", value = "访客正面照", required = true),
            @ApiImplicitParam(name = "visitMatter", value = "访问事宜", required = true),
            @ApiImplicitParam(name = "interviewPhone", value = "被访人手机号", required = true),
            @ApiImplicitParam(name = "interviewName", value = "被访人姓名", required = true),
            @ApiImplicitParam(name = "buildId", value = "楼宇编号")
    })
    @ResponseBody
    @RequestMapping(value = "add/BuildVisitor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult addVisitor(@RequestBody Map<String, Object> params) {
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(params.get("visitorPhone").toString(), new NotNullValidator("访客手机号"))
                .on(params.get("visitorName").toString(), new NotNullValidator("访客姓名"))
                .on(params.get("idcardFront").toString(), new NotNullValidator("身份证正面照path"))
                .on(params.get("idcardBack").toString(), new NotNullValidator("身份证反面照path"))
                .on(params.get("visitorFront").toString(), new NotNullValidator("访客正面照"))
                .on(params.get("visitMatter").toString(), new NotNullValidator("访问事宜"))
                .on(params.get("interviewPhone").toString(), new NotNullValidator("被访人手机号"))
                .on(params.get("interviewName").toString(), new NotNullValidator("被访人姓名"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        //查询访客是否为注册用户，且是否被锁定，被锁定则不能访问
        BaseResult<BjtUserUser> userBaseResults = userManageClient.findUserByUsername(params.get("visitorPhone") + "");
        if (userBaseResults.getData() != null) {
            if (userBaseResults.getData().getLocked() == 1) {
                return new BaseResult(BaseResultEnum.ERROR, "访客被锁定");
            }
        }
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUsername(params.get("interviewPhone") + "");
        if (userBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return userBaseResult;
        }
        if (userBaseResult.getData() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "被访者不存在");
        }
        if (userBaseResult.getData().getLocked() == 1) {
            return new BaseResult(BaseResultEnum.ERROR, "被访者被锁定");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userBaseResult.getData().getUserId());
        BaseResult<List<BjtParkBuildGuard>> baseResult = parkBuildGuardClient.selectAll(map);
        if (baseResult.status != BaseResultEnum.SUCCESS.getStatus() || baseResult.getData() == null) {
            return baseResult;
        }
        if (baseResult.getData().size() < 1) {
            return new BaseResult(BaseResultEnum.ERROR, "被访者无门禁权限");
        }
        String fileFront = DownloadImage.downloadImageToLocal(params.get("idcardFront").toString(), path, "GET");
        if (!IdcardValidation.validation("front", fileFront)) {
            return new BaseResult(BaseResultEnum.ERROR, "身份证正面照错误");
        }
//        String fileBack = DownloadImage.downloadImageToLocal(params.get("idcardBack").toString(), path, "GET");
//        if (!IdcardValidation.validation("back", fileBack)) {
//            return new BaseResult(BaseResultEnum.ERROR, "身份证反面照错误");
//        }
        params.put("status", 1);//状态:1、待处理；2、已通过；3、未通过
        params.put("interviewUserId", userBaseResult.getData().getUserId());
        params.put("interviewName", userBaseResult.getData().getRealname());
        params.put("type", 1);//类型：1、访客记录；2、邀请记录
        params.put("visitorTime", System.currentTimeMillis());
        return parkBuildVisitorClient.addBuildVisitor(params);
    }

    @ApiOperation("获取访客最近的访客记录")
    @ResponseBody
    @RequestMapping(value = "select/BuildVisitorLatest", method = RequestMethod.GET)
    public BaseResult<BjtParkBuildVisitor> selectBuildVisitorLatest(@RequestParam("phone") String phone) {
        if (phone == null || "".equals(phone)) {
            return new BaseResult(BaseResultEnum.ERROR, "访客手机号为空");
        }
        return parkBuildVisitorClient.selectBuildVisitorLatest(phone);
    }

    @ApiOperation("获取被访者所属楼宇信息")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "interviewPhone", value = "被访者手机号", required = true),
    })
    @RequestMapping(value = "select/BuildList", method = RequestMethod.GET)
    public BaseResult<List<Map<String, Object>>> selectBuildList(@RequestParam("interviewPhone") String interviewPhone) {
        if (interviewPhone == null || "".equals(interviewPhone)) {
            return new BaseResult(BaseResultEnum.ERROR, "被访者手机号为空");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("username", interviewPhone);
        params.put("type", 1);
        BaseResult<List<BjtParkBuildGuard>> baseResult = parkBuildGuardClient.selectAll(params);
        if (baseResult.status != BaseResultEnum.SUCCESS.getStatus() || baseResult.getData() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "获取失败");
        }
        List<Map<String, Object>> list = new ArrayList<>();
        if (baseResult.getData().size() > 0) {
            baseResult.getData().stream().forEach(v -> {
                Map<String, Object> map = new HashMap<>();
                map.put("buildId", v.getBuildId());
                map.put("buildName", v.getBuildName());
                list.add(map);
            });
        }
        return new BaseResult(BaseResultEnum.SUCCESS, list);
    }

    @ApiOperation("获取访客二维码信息")
    @ResponseBody
    @RequestMapping(value = "select/BuildVisitorCode", method = RequestMethod.GET)
    public BaseResult selectBuildVisitorCode(@RequestParam("visitorId") String visitorId) {
        if (visitorId == null || "".equals(visitorId)) {
            return new BaseResult(BaseResultEnum.ERROR, "访客编号为空");
        }
        BaseResult<BjtParkBuildVisitor> visitorBaseResult = parkBuildVisitorClient.selectById(visitorId);
        if (visitorBaseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return new BaseResult(BaseResultEnum.ERROR, "访客信息错误");
        }
        if (visitorBaseResult.getData() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "访客信息为空");
        }
        BjtParkBuildVisitor visitor = visitorBaseResult.getData();
        //状态:1、待处理；2、已通过；3、未通过
        if (visitor.getStatus() == 1) {
            return new BaseResult(BaseResultEnum.ERROR, "访客申请待处理中");
        }
        if (visitor.getStatus() == 3) {
            return new BaseResult(BaseResultEnum.ERROR, "访客申请被拒绝");
        }
        if (visitor.getStatus() == 2) {
            if (redisTemplate.hasKey(visitor.getVisitorId().toString() + visitor.getVisitorPhone())) {
                if (visitor.getUseCount() >= 2) {
                    return new BaseResult(BaseResultEnum.ERROR, "使用次数已达上限");
                }
                return new BaseResult(BaseResultEnum.SUCCESS, visitor.getVisitorId().toString() + visitor.getVisitorPhone());
            }
        }
        return new BaseResult(BaseResultEnum.ERROR, "获取失败");
    }

    @ApiOperation(value = "二维码开门")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "设备编号", name = "deviceIdentifier", required = true),
            @ApiImplicitParam(value = "key", name = "key", required = true),
            @ApiImplicitParam(value = "type为1表示有门禁权限的用户，2表示访客", name = "type", required = true),
    })
    @RequestMapping(value = "open/VisitorDoor", method = RequestMethod.POST)
    @ResponseBody
    public String openVisitorDoor(@RequestParam Map<String, Object> params) throws Exception {
        LOGGER.info("开门 = >openDoor");
        Map<String, Object> openMap = new HashedMap();
        openMap.put("code", 0);
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(params.get("deviceIdentifier").toString(), new NotNullValidator("设备编号"))
                .on(params.get("cardIdentifier").toString(), new NotNullValidator("cardIdentifier"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return JSONObject.toJSONString(openMap);
        }
        String cardIdentifier = AESUtil.aesDecode(params.get("cardIdentifier").toString());
        Map<String, String> map = JSONObject.parseObject(cardIdentifier, Map.class);
        String value = redisTemplate.opsForValue().get(map.get("key"));
        if (value == null || "".equals(value)) {
            System.out.println("value:" + value);
            return JSONObject.toJSONString(openMap);
        }
        //调用拥有门禁权限的用户
        if ("1".equals(map.get("type").toString())) {
            params.put("userId", map.get("key"));
            BaseResult baseResult = parkBuildGateClient.openVisitorDoor(params);
            if (baseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
                return JSONObject.toJSONString(openMap);
            }
            return baseResult.getData().toString();
        }
        //调用访客开门业务
        if ("2".equals(map.get("type").toString())) {
            params.put("userId", value);
            params.put("key", map.get("key"));
            BaseResult baseResult = parkBuildGateClient.openVisitorDoor(params);
            if (baseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
                return JSONObject.toJSONString(openMap);
            }
            return baseResult.getData().toString();
        }
        return JSONObject.toJSONString(openMap);
    }

    @ApiOperation("获取下载链接")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyId", value = "链接编号", required = true)
    })
    @RequestMapping(value = "select/SystemDownload", method = RequestMethod.GET)
    public String selectSystemDownload(@RequestParam("keyId") String keyId) {
        if (keyId == null || "".equals(keyId)) {
            return "链接keyId为空";
        }
        BaseResult<BjtSystemDownload> baseResult = systemDownloadClient.selectById(keyId);
        if (baseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return "获取失败";
        }
        if (baseResult.getData() == null) {
            return "链接为空";
        }
        return "redirect:" + baseResult.getData().getValue();
    }

    @ApiOperation("获取链接信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyId", value = "链接编号", required = true)
    })
    @ResponseBody
    @RequestMapping(value = "select/SystemValue", method = RequestMethod.GET)
    public BaseResult<BjtSystemDownload> selectSystemValue(@RequestParam("keyId") String keyId) {
        if (keyId == null || "".equals(keyId)) {
            return new BaseResult(BaseResultEnum.ERROR, "链接keyId为空");
        }
        return systemDownloadClient.selectById(keyId);
    }

}
