package com.j4sc.bjt.api.server.controller;

import com.alibaba.fastjson.JSON;
import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ResultCollectors;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.api.server.base.BaseJwtController;
import com.j4sc.bjt.api.server.client.PaymentClient;
import com.j4sc.bjt.api.server.client.UserBillClient;
import com.j4sc.bjt.user.dao.entity.BjtUserBill;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 用户账单服务
 * @Author: chengyz
 * @CreateDate: 2018/4/10 15:45
 * @Version: 1.0
 **/
@RestController
@RequestMapping(value = "api/userBill")
@Api(tags = {"帮家团用户账单服务"}, description = "帮家团用户账单服务 - 需授权")
public class ApiUserBillController extends BaseJwtController{

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiUserController.class);

    @Autowired
    private UserBillClient userBillClient;
    @Autowired
    private PaymentClient paymentClient;
    @Value("${billNotifyUrl}")
    private String notifyUrl;

    @ApiOperation(value = "用户查询账单", notes = "用户查询账单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int"),
            @ApiImplicitParam(name = "beginTime", value = "条数", dataType = "int"),
            @ApiImplicitParam(name = "endTime", value = "条数", dataType = "int"),

    })
    @RequestMapping(value = "select/PageBill", method = RequestMethod.GET)
    public BaseResult<Page<BjtUserBill>> selectPageBill(@RequestParam Map<String,Object> params) {
        LOGGER.info("selectPageBill = >" + getUserId());
        if (params.get("page") == null) {
            params.put("page", 1);
        }
        if (params.get("size") == null) {
            params.put("size", 20);
        }
        params.put("userId", getUserId());
        return userBillClient.selectPageBill(params);
    }

    @ApiOperation(value = "用户账单缴费", notes = "用户账单缴费")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "billId", value = "账单编号", required = true),
            @ApiImplicitParam(name = "payWay", value = "缴费方式(1是支付宝，2是微信)", required = true),
            @ApiImplicitParam(name = "money", value = "缴费金额", required = true)
    })
    @RequestMapping(value = "pay/BjtUserBill", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult payBjtUserBill(@RequestBody Map<String,Object> params) {
        LOGGER.info("用户账单缴费");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(params.get("billId").toString(), new NotNullValidator("账单编号"))
                .on(params.get("payWay").toString().toString(), new NotNullValidator("缴费方式"))
                .on(params.get("money").toString().toString(), new NotNullValidator("金额"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        BaseResult<BjtUserBill> billBaseResult = userBillClient.selectBjtUserBill(getUserId(), Integer.parseInt(params.get("billId").toString()));
        if (billBaseResult.getStatus() != BaseResultEnum.SUCCESS.getStatus()) {
            return billBaseResult;
        }
        if (new BigDecimal(params.get("money").toString()).compareTo(billBaseResult.getData().getMoney()) != 0) {
            return new BaseResult(BaseResultEnum.ERROR, "缴费金额有误");
        }
        Map<String, String> map = new HashMap<>();
        map.put("money", billBaseResult.getData().getMoney().toString());
        map.put("info", "账单缴费");
        map.put("infoSelf", JSON.toJSONString(billBaseResult.getData()));
        map.put("name", "账单缴费");
        map.put("userId", getUserId());
        map.put("username", billBaseResult.getData().getUsername());
        map.put("systemId", "3");
        map.put("type", "3");
        map.put("notifyJson", params.get("billId").toString());
        map.put("notifyUrl", notifyUrl+"/BjtUserBill/notifyUrl/UserBill");//回调接口
        if ("1".equals(params.get("payWay").toString())) {
            return paymentClient.aliPay(map);//调用支付宝
        }
        if ("2".equals(params.get("payWay").toString()))  {
            return paymentClient.wechatPay(map);//调用微信支付
        }
        return new BaseResult(BaseResultEnum.ERROR, "用户账单缴费失败");
    }
    @ApiOperation(value = "用户账单手动录入", notes = "用户账单手动录入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "billId", value = "账单编号", required = true),
            @ApiImplicitParam(name = "userId", value = "用户编号", required = true),
    })
    @RequestMapping(value = "update/BjtUserBill", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateBjtUserBill(@RequestBody Map<String,Object> params){
        LOGGER.info("用户账单手动录入");
        //信息校验
        ComplexResult complexResult = FluentValidator.checkAll()
                .on(params.get("billId").toString(), new NotNullValidator("账单编号"))
                .on(params.get("userId").toString(), new NotNullValidator("用户编号"))
                .doValidate()
                .result(ResultCollectors.toComplex());
        if (!complexResult.isSuccess()) {
            return new BaseResult(BaseResultEnum.ERROR, complexResult.getErrors().get(0).getErrorMsg());
        }
        return userBillClient.updateBjtUserBill(params);
    }

}
