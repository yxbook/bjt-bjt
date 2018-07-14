package com.j4sc.bjt.user.server.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.j4sc.bjt.user.common.BjtParkBuildAgreement;
import com.j4sc.bjt.user.common.PayOrder;
import com.j4sc.bjt.user.dao.entity.BjtUserBill;
import com.j4sc.bjt.user.rest.api.BjtUserBillService;
import com.j4sc.bjt.user.server.client.ParkBuildAgreementClient;
import com.j4sc.bjt.user.server.client.PayOrderClient;
import com.j4sc.bjt.user.server.client.SystemApiClient;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import io.swagger.annotations.*;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/4/10 14:21
 * @Version: 1.0
 **/
@RestController
@RequestMapping("BjtUserBill")
@ApiModel
@Api(tags = {"用户账单服务"}, description = "用户账单服务")
public class BjtUserBillController extends BaseController<BjtUserBill, BjtUserBillService> implements BaseApiService<BjtUserBill> {

    private static final Logger LOGGER=LoggerFactory.getLogger(BjtUserBillController.class);

    @Autowired
    private BjtUserBillService userBillService;
    @Autowired
    private PayOrderClient payOrderClient;
    @Autowired
    private SystemApiClient systemApiClient;
    @Autowired
    private ParkBuildAgreementClient parkBuildAgreementClient;

    @ApiOperation(value = "查询账单")
    @RequestMapping(value = "select/BjtUserBill", method = RequestMethod.GET)
    public BaseResult<BjtUserBill> selectBjtUserBill(@RequestParam("userId")String userId, @RequestParam("billId")int billId) {
        LOGGER.error("查询账单");
        BjtUserBill bjtUserBill = userBillService.selectOne(new EntityWrapper<BjtUserBill>().eq("user_id", userId).eq("bill_id", billId));
        if (bjtUserBill == null) {
            return new BaseResult(BaseResultEnum.ERROR, "账单不存在");
        }
        return new BaseResult(BaseResultEnum.SUCCESS, bjtUserBill);
    }

    @ApiOperation(value = "用户查看账单记录(分页)", notes = "用户查看账单记录(分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "条数", dataType = "int")
    })
    @RequestMapping(value = "select/PageBill", method = RequestMethod.GET)
    public BaseResult<Page<BjtUserBill>> selectPageBill(@RequestParam Map<String, Object> params) {
        Wrapper entityWrapper = new EntityWrapper<BjtUserBill>().eq("user_id", params.get("userId"));
        if (params.get("beginTime")!= null && params.get("endTime") != null) {
            entityWrapper.between(true, "ctime", params.get("beginTime"), params.get("endTime"));
        }
        if (params.get("beginTime") != null && params.get("endTime") == null) {
            entityWrapper.ge("ctime",params.get("beginTime"));
        }
        if (params.get("beginTime") == null && params.get("endTime") != null) {
            entityWrapper.le("ctime", params.get("endTime"));
        }
        Page<BjtUserBill> pageModel = new Page<>();
        pageModel.setCurrent(Integer.parseInt(params.get("page").toString()));
        pageModel.setSize(Integer.parseInt(params.get("size").toString()));
        Page<BjtUserBill> _pageData = userBillService.selectPage(pageModel, entityWrapper);
        return new BaseResult(BaseResultEnum.SUCCESS, _pageData);
    }

    @ApiOperation("账单缴费支付回调接口")
    @RequestMapping(value = "notifyUrl/UserBill", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String notifyUrlUserBill(@RequestBody Map<String, Object> params) {
        LOGGER.info("账单缴费支付回调接口");
        if (params.get("state") == null) {
            return "fail";
        }
        if (params.get("orderId") == null) {
            return "fail";
        }
        String resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
        Map<String, Object> map = new HashMap<>();
        map.put("order_id", params.get("orderId"));
        BaseResult<PayOrder> orderResult = payOrderClient.selectById(params.get("orderId").toString());
        if (orderResult.status != BaseResultEnum.SUCCESS.getStatus()) {
            return "fail";
        }
        if (orderResult.getData() == null) {
            return "fail";
        }
        PayOrder order = orderResult.getData();
        //1、修改账单信息中的状态
        if ("1".equals(params.get("state").toString()) && order.getState() == Integer.parseInt(params.get("state").toString())) {
            BjtUserBill bjtUserBill = userBillService.selectOne(new EntityWrapper<BjtUserBill>().eq("user_id", order.getUserId()).eq("bill_id", order.getNotifyJson()));
            if (bjtUserBill == null) {
                return "fail";
            }
            bjtUserBill.setPayTime(System.currentTimeMillis());
            if ("AliPay".equals(order.getChannel())) {
                bjtUserBill.setPayWay(1);//支付方式（1支付宝、2微信、3银行卡）
            }
            if ("WeChatPay".equals(order.getChannel())) {
                bjtUserBill.setPayWay(2);
            }
            bjtUserBill.setStatus(1);//状态（0逾期、1已缴纳、2未缴纳等）
            //修改合同中的下次缴费日期
            BaseResult<BjtParkBuildAgreement> agreementBaseResult = parkBuildAgreementClient.selectById(bjtUserBill.getAgreementId().toString());
            if (agreementBaseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
                return "fail";
            }
            BjtParkBuildAgreement agreement = agreementBaseResult.getData();
            //缴费方式：1、按月缴纳；2、按季度缴纳；3、按年缴纳
            Calendar curr = Calendar.getInstance();
            if (agreement.getPayment() == 1) {
                curr.set(Calendar.MONTH,curr.get(Calendar.MONTH)+1);
            }
            if (agreement.getPayment() == 2) {
                curr.set(Calendar.MONTH,curr.get(Calendar.MONTH)+3);
            }
            if (agreement.getPayment() == 3) {
                curr.set(Calendar.MONTH,curr.get(Calendar.MONTH)+12);
            }
            agreementBaseResult.getData().setNextTime(curr.getTimeInMillis());//设置下次缴费时间
            if (parkBuildAgreementClient.updateById(agreement).status != BaseResultEnum.SUCCESS.getStatus()) {
                return "fail";
            }
            List<String> list = new ArrayList<>();
            list.add(bjtUserBill.getUsername());
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("title", "账单缴费");
            paramsMap.put("body", "账单缴费");
            paramsMap.put("target", "BjtUserBill");
            paramsMap.put("id", bjtUserBill.getBillId()+"");
            paramsMap.put("phoneList", list);
            systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
            if (userBillService.updateById(bjtUserBill)) {
                if (bjtUserBill.getPayWay() == 1) {
                    return "success";//1是支付宝
                }
                if (bjtUserBill.getPayWay() == 2) {
                    return resXml;//2是微信
                }
            }
            return "fail";
        }
        return "fail";
    }

    @ApiOperation("账单缴费手动录入")
    @RequestMapping(value = "update/BjtUserBill", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult updateBjtUserBill(@RequestBody Map<String, Object> params) {
        LOGGER.info("账单缴费手动录入");
        //1、修改账单信息中的状态
            BjtUserBill bjtUserBill = userBillService.selectOne(new EntityWrapper<BjtUserBill>().eq("user_id", params.get("userId")).eq("bill_id", params.get("billId")));
            if (bjtUserBill == null) {
                return new BaseResult(BaseResultEnum.ERROR, "账单不存在");
            }
            if (bjtUserBill.getStatus() != 2) {
                return new BaseResult(BaseResultEnum.ERROR, "账单已逾期或已缴纳");
            }
            bjtUserBill.setPayTime(System.currentTimeMillis());
            bjtUserBill.setPayWay(4);//支付方式（1支付宝、2微信、3银行卡、4、其它）
            bjtUserBill.setStatus(1);//状态（0逾期、1已缴纳、2未缴纳等）
            //修改合同中的下次缴费日期
            BaseResult<BjtParkBuildAgreement> agreementBaseResult = parkBuildAgreementClient.selectById(bjtUserBill.getAgreementId().toString());
            if (agreementBaseResult.status != BaseResultEnum.SUCCESS.getStatus()) {
                return new BaseResult(BaseResultEnum.ERROR, "手动录入失败");
            }
            BjtParkBuildAgreement agreement = agreementBaseResult.getData();
            //缴费方式：1、按月缴纳；2、按季度缴纳；3、按年缴纳
            Calendar curr = Calendar.getInstance();
            if (agreement.getPayment() == 1) {
                curr.set(Calendar.MONTH,curr.get(Calendar.MONTH)+1);
            }
            if (agreement.getPayment() == 2) {
                curr.set(Calendar.MONTH,curr.get(Calendar.MONTH)+3);
            }
            if (agreement.getPayment() == 3) {
                curr.set(Calendar.MONTH,curr.get(Calendar.MONTH)+12);
            }
            agreementBaseResult.getData().setNextTime(curr.getTimeInMillis());//设置下次缴费时间
            if (parkBuildAgreementClient.updateById(agreement).status != BaseResultEnum.SUCCESS.getStatus()) {
                return new BaseResult(BaseResultEnum.ERROR, "手动录入失败");
            }
            List<String> list = new ArrayList<>();
            list.add(bjtUserBill.getUsername());
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("title", "账单缴费");
            paramsMap.put("body", "账单缴费");
            paramsMap.put("target", "BjtUserBill");
            paramsMap.put("id", bjtUserBill.getBillId()+"");
            paramsMap.put("phoneList", list);
            systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
            if (userBillService.updateById(bjtUserBill)) {
                return new BaseResult(BaseResultEnum.SUCCESS, "手动录入成功");
            }
        return new BaseResult(BaseResultEnum.ERROR, "手动录入失败");
        }

}
