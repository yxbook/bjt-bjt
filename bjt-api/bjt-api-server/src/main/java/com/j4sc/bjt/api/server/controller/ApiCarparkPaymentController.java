package com.j4sc.bjt.api.server.controller;

import com.alibaba.fastjson.JSON;
import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.*;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkApplyDetail;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkPayment;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkSpace;
import com.j4sc.bjt.user.dao.entity.BjtUserUser;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import com.j4sc.common.validator.NotNullValidator;
import com.netflix.discovery.converters.Auto;
import freemarker.template.utility.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/11 15:13
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/carparkPayment")
@Api(tags = {"帮家团停车缴费服务"}, description = "帮家团停车缴费服务 - 需授权")
public class ApiCarparkPaymentController extends BaseJwtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiCarparkCarController.class);

    @Autowired
    private CarparkPaymentClient carparkPaymentClient;
    @Autowired
    private PaymentClient paymentClient;
    @Autowired
    private UserManageClient userManageClient;
    @Autowired
    private CarparkSpaceClient carparkSpaceClient;
    @Value("${carparkNotifyUrl}")
    private String notifyUrl;

    @ApiOperation("停车缴费支付接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "缴费信息编号", name = "paymentId", required = true),
            @ApiImplicitParam(value = "缴费金额", name = "money", required = true),
    })
    @RequestMapping(value = "pay/CarparkPayment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult payCarparkPayment(@RequestBody Map<String, Object> params) {
        LOGGER.info("停车缴费支付");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(params.get("paymentId") + "", new NotNullValidator("缴费信息编号"))
                .on(params.get("money") + "", new NotNullValidator("缴费金额"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(getUserId());
        if (userBaseResult.getData() == null || userBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return userBaseResult;
        }
        BaseResult<BjtCarparkPayment> paymentBaseResult = carparkPaymentClient.selectById(params.get("paymentId") + "");
        if (paymentBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return paymentBaseResult;
        }
        if (paymentBaseResult.getData() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前缴费信息不存在");
        }
        if (new BigDecimal(params.get("money").toString()).compareTo(paymentBaseResult.getData().getTotalFee()) != 0) {
            return new BaseResult(BaseResultEnum.ERROR, "缴费金额有误");
        }
        BjtCarparkPayment payment = paymentBaseResult.getData();
        if (payment.getCutOffTime() == null) {
            payment.setCutOffTime(payment.getCtime() + 30 * 60 * 1000);//设置支付截止时间为30分钟
            BaseResult result = carparkPaymentClient.updateById(payment);
            if (result.status != BaseResultEnum.SUCCESS.getStatus()) {
                return result;
            }
        }
        if (System.currentTimeMillis() > payment.getCutOffTime()) {
            return new BaseResult(BaseResultEnum.ERROR, "支付已超时");
        }

        Map<String, String> map = new HashMap<>();
        map.put("money", paymentBaseResult.getData().getTotalFee().toString());
        map.put("info", "停车缴费");
        map.put("infoSelf", JSON.toJSONString(paymentBaseResult.getData()));
        map.put("name", "停车缴费");
        map.put("userId", getUserId());
        map.put("username", userBaseResult.getData().getUsername());
        map.put("systemId", "3");
        map.put("type", "3");//3是停车缴费
        map.put("notifyUrl", notifyUrl+"/BjtCarparkPayment/notifyUrl/CarparkPayment");//回调接口
        map.put("notifyJson", params.get("paymentId").toString());//回调信息
        if (paymentBaseResult.getData().getPayWay() == 1) {
            return paymentClient.aliPay(map);//调用支付宝
        }
        if (paymentBaseResult.getData().getPayWay() == 2) {
            return paymentClient.wechatPay(map);//调用微信支付
        }
        return new BaseResult(BaseResultEnum.SUCCESS, "缴费成功");
    }

    @ApiOperation("停车缴费信息添加")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "车辆编号", name = "carId", required = true),
            @ApiImplicitParam(value = "停车场编号", name = "spaceId", required = true),
            @ApiImplicitParam(value = "停车场名称", name = "spaceName", required = true),
            @ApiImplicitParam(value = "车牌号", name = "plateNumber", required = true),
            @ApiImplicitParam(value = "每月/每年 单价费用", name = "fee", required = true),
            @ApiImplicitParam(value = "缴纳月数/年数", name = "number", required = true),
            @ApiImplicitParam(value = "生效日期", name = "beginTime", required = true),
            @ApiImplicitParam(value = "截止日期", name = "endTime", required = true),
            @ApiImplicitParam(value = "合计金额", name = "totalFee", required = true),
            @ApiImplicitParam(value = "缴费方式(1表示支付宝，2表示微信)", name = "payWay", required = true),
            @ApiImplicitParam(value = "缴费类型：1、按月缴费；2、按年缴费", name = "type", required = true),
            @ApiImplicitParam(value = "备注", name = "remark")
    })
    @RequestMapping(value = "save/CarparkPayment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCarparkPayment(@RequestBody BjtCarparkPayment carparkPayment) {
        LOGGER.info("添加停车缴费信息");
        BaseResult<BjtUserUser> userBaseResult = userManageClient.findUserByUserId(getUserId());
        if (userBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return userBaseResult;
        }
        if (userBaseResult.getData() == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前用户不存在");
        }
        carparkPayment.setUserId(userBaseResult.getData().getUserId());
        carparkPayment.setUsername(userBaseResult.getData().getUsername());
        carparkPayment.setRealname(userBaseResult.getData().getRealname());
        carparkPayment.setCtime(System.currentTimeMillis());
        carparkPayment.setStatus(1);//状态：1、待缴费；2、缴费成功；3；缴费失败
        return carparkPaymentClient.saveCarparkPayment(carparkPayment);
    }

    @ApiOperation("获取停车缴费记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "关键词"),
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int")
    })
    @RequestMapping(value = "select/CarparkPaymentPage", method = RequestMethod.GET)
    public BaseResult<Page<BjtCarparkPayment>> selectCarparkPaymentPage(@RequestParam Map<String, Object> params) {
        LOGGER.info("获取停车缴费记录列表" + params);
        params.put("orderBy", "ctime");
        params.put("user_id", getUserId());
        if (params.get("key") == null || "".equals(params.get("key").toString())) {
            params.remove("key");
        }
        params.put("status", 2);//状态：1、待缴费；2、缴费成功；3；缴费失败"
        return carparkPaymentClient.selectPage(params);
    }

    @ApiOperation(value = "获取停车场详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "spaceId", value = "停车场编号", dataType = "String")
    })
    @RequestMapping(value = "carpark/select/CarparkSpace", method = RequestMethod.GET)
    public BaseResult<BjtCarparkSpace> selectCarparkSpacePage(@RequestParam("spaceId") String spaceId) {
        LOGGER.info("获取停车场详情");
        return carparkSpaceClient.selectById(spaceId);
    }
}
