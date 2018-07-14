package com.j4sc.bjt.carpark.server.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.j4sc.bjt.carpark.common.PayOrder;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkApplyDetail;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkPayment;
import com.j4sc.bjt.carpark.dao.entity.BjtCarparkSpace;
import com.j4sc.bjt.carpark.rest.api.BjtCarparkApplyDetailService;
import com.j4sc.bjt.carpark.rest.api.BjtCarparkPaymentService;
import com.j4sc.bjt.carpark.rest.api.BjtCarparkSpaceService;
import com.j4sc.bjt.carpark.server.client.PayOrderClient;
import com.j4sc.bjt.carpark.server.client.SystemApiClient;
import com.j4sc.common.base.BaseApiService;
import com.j4sc.common.base.BaseController;
import com.j4sc.common.base.BaseResult;
import com.j4sc.common.base.BaseResultEnum;
import com.j4sc.common.util.MD5Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/11 14:26
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/BjtCarparkPayment")
@ApiModel
@Api(tags = {"停车缴费服务"}, description = "停车缴费服务")
public class BjtCarparkPaymentController extends BaseController<BjtCarparkPayment, BjtCarparkPaymentService> implements BaseApiService<BjtCarparkPayment> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BjtCarparkPaymentController.class);

    @Autowired
    private BjtCarparkSpaceService carparkSpaceService;
    @Autowired
    private BjtCarparkPaymentService carparkPaymentService;
    @Autowired
    private BjtCarparkApplyDetailService carparkApplyDetailService;
    @Autowired
    private PayOrderClient payOrderClient;
    @Autowired
    private SystemApiClient systemApiClient;

    @ApiOperation("添加缴费信息")
    @RequestMapping(value = "save/CarparkPayment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResult saveCarparkPayment(@RequestBody BjtCarparkPayment carparkPayment) {
        BjtCarparkApplyDetail applyDetail = carparkApplyDetailService.selectOne(new EntityWrapper<BjtCarparkApplyDetail>()
                .eq("user_id", carparkPayment.getUserId()).eq("car_id", carparkPayment.getCarId()).eq("space_id", carparkPayment.getSpaceId()));
        if (applyDetail == null) {
            return new BaseResult(BaseResultEnum.ERROR, "用户无当前停车场的停车权限");
        }
        //状态:1、待审批；2、（已通过）但未缴费；3、未通过；4、正常（3状态在拒绝时就会被删除）
        if (applyDetail.getStatus() == 1) {
            return new BaseResult(BaseResultEnum.ERROR, "停车权限待审批");
        }
        if (applyDetail.getStatus() == 3) {
            return new BaseResult(BaseResultEnum.ERROR, "停车权限未通过");
        }
        BjtCarparkSpace carparkSpace = carparkSpaceService.selectOne(new EntityWrapper<BjtCarparkSpace>().eq("space_id", carparkPayment.getSpaceId()));
        if (carparkSpace == null) {
            return new BaseResult(BaseResultEnum.ERROR, "当前停车场不存在");
        }
        Calendar calendar = new GregorianCalendar();
        if (applyDetail.getEndTime() != null) {
            if (applyDetail.getEndTime() > carparkPayment.getEndTime()) {
                return new BaseResult(BaseResultEnum.ERROR, "缴费生效日期必须大于权限失效日期");
            }
            if (!(applyDetail.getEndTime() < carparkPayment.getBeginTime() && carparkPayment.getBeginTime() < carparkPayment.getEndTime())) {
                return new BaseResult(BaseResultEnum.ERROR, "该时间段已缴费");
            }
            Wrapper<BjtCarparkPayment> wrapper = new EntityWrapper<BjtCarparkPayment>().eq("user_id", carparkPayment.getUserId())
                    .eq("car_id", carparkPayment.getCarId())
                    .eq("space_id", carparkPayment.getSpaceId())
                    .ge("begin_time", applyDetail.getEndTime())
                    .orderBy("begin_time", true);
            //获取用户当前车辆在当前停车场的还未生效的缴费记录(.ge()表示大于等于)
            List<BjtCarparkPayment> paymentList = carparkPaymentService.selectList(wrapper);

            if (paymentList != null && paymentList.size() == 1) {
                //大于已有缴费记录的失效时间
                if (!(carparkPayment.getEndTime() < paymentList.get(0).getBeginTime() && carparkPayment.getBeginTime() > applyDetail.getEndTime()) && !(carparkPayment.getBeginTime() > paymentList.get(0).getEndTime())) {
                    return new BaseResult(BaseResultEnum.ERROR, "缴费时间存在交叉");
                }
            }
            //预交费订单有多条的情况
            boolean flag = false;
            if (paymentList != null && paymentList.size() >= 2) {
                if (carparkPayment.getBeginTime() > paymentList.get(paymentList.size() - 1).getEndTime()) {
                    flag = true;
                } else {
                    for (int i = 0; i < paymentList.size(); i++) {
                        if (i < paymentList.size() - 1) {
                            if (carparkPayment.getBeginTime() > paymentList.get(i).getEndTime() && carparkPayment.getEndTime() < paymentList.get(i + 1).getBeginTime()) {
                                flag = true;
                                break;
                            }
                        }
                    }
                }
                if (!flag) {
                    return new BaseResult(BaseResultEnum.ERROR, "缴费信息有误");
                }
            }
        }
        String totalMoney = "";
        calendar.setTimeInMillis(carparkPayment.getBeginTime());
        //缴费类型：1、按月缴费；2、按年缴费
        if (carparkPayment.getType() == 1) {
            if (carparkSpace.getMonthFee() == null) {
                return new BaseResult(BaseResultEnum.ERROR, "停车场月缴费单价为空");
            }
            calendar.add(calendar.MONTH, carparkPayment.getNumber());//把日期往后增加一个月.整数往后推,负数往前移动
            carparkPayment.setEndTime(calendar.getTimeInMillis());
            BigDecimal months = new BigDecimal(Double.toString(carparkPayment.getNumber()));
            totalMoney = String.valueOf(months.multiply(carparkSpace.getMonthFee()).doubleValue());
        }
        if (carparkPayment.getType() == 2) {
            if (carparkSpace.getYearFee() == null) {
                return new BaseResult(BaseResultEnum.ERROR, "停车场年缴费单价为空");
            }
            calendar.add(calendar.YEAR, carparkPayment.getNumber());//把日期往后增加一年.整数往后推,负数往前移动
            carparkPayment.setEndTime(calendar.getTimeInMillis());
            BigDecimal months = new BigDecimal(Double.toString(carparkPayment.getNumber()));
            totalMoney = String.valueOf(months.multiply(carparkSpace.getYearFee()).doubleValue());
        }
        if (carparkPayment.getTotalFee().compareTo(new BigDecimal(totalMoney)) != 0) {
            return new BaseResult(BaseResultEnum.ERROR, "支付金额有误");
        }
        carparkPayment.setResource(applyDetail.getResource());//设置车辆图片
        if (!carparkPaymentService.insert(carparkPayment)) {
            return new BaseResult(BaseResultEnum.ERROR, "添加缴费信息失败");
        }
        return new BaseResult(BaseResultEnum.SUCCESS, carparkPayment.getPaymentId());//返回缴费编号
    }

    @ApiOperation("支付成功之后回调修改缴费信息")
    @RequestMapping(value = "notifyUrl/CarparkPayment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String updateCarparkPayment(@RequestBody Map<String, Object> params) {
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
        //修改停车缴费信息中的状态   状态：1、待缴费；2、缴费成功；3；缴费失败
        BjtCarparkPayment payment = carparkPaymentService.selectOne(new EntityWrapper<BjtCarparkPayment>().eq("user_id", orderResult.getData().getUserId()).eq("payment_id", orderResult.getData().getNotifyJson()));
        if (payment == null) {
            return "fail";
        }
        BjtCarparkSpace space = carparkSpaceService.selectOne(new EntityWrapper<BjtCarparkSpace>().eq("space_id", payment.getSpaceId()));

        //获取停车申请权限
        BjtCarparkApplyDetail applyDetail = carparkApplyDetailService.selectOne(new EntityWrapper<BjtCarparkApplyDetail>()
                .eq("user_id", payment.getUserId()).eq("car_id", payment.getCarId()).eq("space_id", payment.getSpaceId()));
        //修改停车申请明细中的权限状态,改权限中的有效截止日期
        //PayOrder订单状态-0：交易创建，-1：未付款交易超时关闭，或支付完成后全额退款，1：交易支付完成，2：交易结束，不可退款
        if ("1".equals(params.get("state").toString())) {
            if (payment.getStatus() == 1) {
                payment.setStatus(2);//状态：1、待缴费；2、缴费成功；3；缴费失败
                if (!carparkPaymentService.updateById(payment)) {
                    return "fail";
                }
            }
            //初次缴费，生效日期和失效日期都为空
            if (applyDetail.getBeginTime() == null && applyDetail.getEndTime() == null) {
                applyDetail.setBeginTime(payment.getBeginTime());
                applyDetail.setEndTime(payment.getEndTime());
                applyDetail.setStatus(4); //停车权限的状态：状态:1、待审批；2、（已通过）但未缴费；3、未通过；4、正常
                if (!carparkApplyDetailService.updateById(applyDetail)) {
                    return "fail";
                }
                //添加用户月卡信息到停车场系统
                if (!sendPaymentToParkSystem(payment, space.getParkId())) {
                    LOGGER.info("添加用户月卡信息到停车场系统失败");
                    return "fail";
                }
            }
            //失效日期小于当前时间，状态有可能显示正常（定时任务还未执行到），有可能显示未缴费
            if (applyDetail.getEndTime() < System.currentTimeMillis()) {
                applyDetail.setStatus(4);
                applyDetail.setEndTime(payment.getEndTime());
                applyDetail.setBeginTime(payment.getBeginTime());
                //更新用户月卡信息
                if (!updateUserCard(payment, space.getParkId())) {
                    return "fail";
                }
                if (!carparkApplyDetailService.updateById(applyDetail)) {
                    return "fail";
                }
            }
            List<String> list = new ArrayList<>();
            list.add(payment.getUsername());
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("title", "停车缴费");
            paramsMap.put("body", "缴费成功");
            paramsMap.put("target", "BjtUserBill");
            paramsMap.put("id", payment.getPaymentId() + "");
            paramsMap.put("phoneList", list);
            systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ

            //停车权限失效日期大于当前时间，不做改变
            if (applyDetail.getEndTime() > System.currentTimeMillis() && applyDetail.getStatus() == 4) {
                if (payment.getPayWay() == 1) {
                    return "success";//1是支付宝
                }
                if (payment.getPayWay() == 2) {
                    return resXml;//2是微信
                }
            }
            if (payment.getPayWay() == 1) {
                return "success";//1是支付宝
            }
            if (payment.getPayWay() == 2) {
                return resXml;//2是微信
            }
        }
        return "fail";

    }

    /**
     * 生成用户月卡信息给停车场系统
     *
     * @param payment 停车缴费信息
     * @param parkId  停车场系统对应的编号
     * @return
     */
    private boolean sendPaymentToParkSystem(BjtCarparkPayment payment, String parkId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://www.dongluhitec.net/third_api/monthCardAppAction_create.action";
        Map<String, Object> postParameters = new HashMap<>();
        postParameters.put("parkId", parkId);
        postParameters.put("carNum", payment.getPlateNumber());
        postParameters.put("endTime", new SimpleDateFormat("yyyy-MM-dd").format(payment.getEndTime()));
        postParameters.put("startTime", new SimpleDateFormat("yyyy-MM-dd").format(payment.getBeginTime()));
        postParameters.put("userPhone", payment.getUsername());
        postParameters.put("userName", payment.getRealname());
        postParameters.put("loginUserName", payment.getUsername());
        postParameters.put("rechangeFee", payment.getTotalFee().intValue());
        StringBuffer content = new StringBuffer();
        StringBuffer template = new StringBuffer();
        postParameters.forEach((k, v) -> {
            try {
                content.append(k).append("=").append(v).append("&");
                template.append(k).append("=").append(v.toString()).append("&");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        String params = content.replace(content.lastIndexOf("&"), content.length(), "").toString();
        String templateParams = template.replace(template.lastIndexOf("&"), template.length(), "").toString();
        String sign = MD5Util.md5(templateParams + "e0a413df5e6611e8b441060400ef5315").toLowerCase();
        url = url + "?" + params + "&sign=" + sign + "&app_id=bangjia";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        Map<String, String> resultMap = (Map<String, String>) JSONUtils.parse(responseEntity.getBody());
        if ("0000".equals(resultMap.get("resultCode"))) {
            return true;
        }
        return false;
    }


    /**
     * 更新用户月卡信息
     *
     * @param payment
     * @return
     */
    private static boolean updateUserCard(BjtCarparkPayment payment, String parkId) {
        //查询用户月卡信息
        Map<String, Object> returnMap = selectUserCard(payment.getUsername(), "0", payment.getPlateNumber(), parkId);
        if (null == returnMap || null == returnMap.get("datas")) {
            return false;
        }
        Map<String, Object> cardMap = (Map<String, Object>) ((List<Object>) returnMap.get("datas")).get(0);
        if (null == cardMap) {
            return false;
        }
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://www.dongluhitec.net/third_api/monthCardAppAction_update.action";
        Map<String, Object> postParameters = new HashMap<>();
        postParameters.put("parkId", cardMap.get("parkId"));
        postParameters.put("id", cardMap.get("id"));
        postParameters.put("endTime", new SimpleDateFormat("yyyy-MM-dd").format(payment.getEndTime()));
        postParameters.put("startTime", new SimpleDateFormat("yyyy-MM-dd").format(payment.getBeginTime()));
        postParameters.put("carNum", payment.getPlateNumber());
        postParameters.put("userName", payment.getRealname());
        postParameters.put("loginUserName", payment.getUsername());
        postParameters.put("rechangeFee", payment.getTotalFee().intValue());
        StringBuffer content = new StringBuffer();
        StringBuffer template = new StringBuffer();
        postParameters.forEach((k, v) -> {
            try {
                content.append(k).append("=").append(v).append("&");
                template.append(k).append("=").append(v.toString()).append("&");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        String params = content.replace(content.lastIndexOf("&"), content.length(), "").toString();
        String templateParams = template.replace(template.lastIndexOf("&"), template.length(), "").toString();
        String sign = MD5Util.md5(templateParams + "e0a413df5e6611e8b441060400ef5315").toLowerCase();
        url = url + "?" + params + "&sign=" + sign + "&app_id=bangjia";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        Map<String, String> resultMap = (Map<String, String>) JSONUtils.parse(responseEntity.getBody());
        if ("0000".equals(resultMap.get("resultCode"))) {
            return true;
        }
        return false;
    }

    /**
     * 查询用户月卡信息
     *
     * @param userPhone 用户手机号
     * @param type      0：查询最近 1 条月卡记录, 1：查询所有月卡记录
     * @param carNum    车牌号
     * @param parkId    停车场编号
     * @return
     */
    private static Map selectUserCard(String userPhone, String type, String carNum, String parkId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://www.dongluhitec.net/api/queryCreatMonthCardData.action";
        Map<String, Object> postParameters = new HashMap<>();
        postParameters.put("userPhone", userPhone);
        postParameters.put("type", type);//0：查询最近 1 条月卡记录, 1：查询所有月卡记录
        postParameters.put("carNum", carNum);
        postParameters.put("parkId", parkId);
        StringBuffer template = new StringBuffer();
        postParameters.forEach((k, v) -> {
            try {
                template.append(k).append("=").append(v.toString()).append("&");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        String templateParams = template.replace(template.lastIndexOf("&"), template.length(), "").toString();
        url = url + "?" + templateParams;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        JSONUtils.parse(responseEntity.getBody());
        Map<String, Object> map = (Map<String, Object>) JSONUtils.parse(responseEntity.getBody());
        if ("0000".equals(map.get("resultCode"))) {
            try {
                String data = URLDecoder.decode(map.get("data").toString(), "utf-8");
                System.out.println("返回的月卡信息：" + data);
                Map<String, Object> returnMap = (Map<String, Object>) JSONUtils.parse(data);
                return returnMap;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 统计当天的缴费金额
     *
     * @param spaceId
     * @return
     */
    @RequestMapping(value = "select/CountPayment", method = RequestMethod.GET)
    public BaseResult<Map<String, Object>> selectCountPayment(@RequestParam("spaceId") String spaceId) {
        Map<String,Object> resultMap = new HashMap<>();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        int count = carparkPaymentService.selectCount(new EntityWrapper().eq("space_id", spaceId)
                .eq("status", 2).between(true, "ctime", c.getTimeInMillis(), c.getTimeInMillis() + 86400000L));

        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.MONTH, 0);
        c2.set(Calendar.DAY_OF_MONTH, 1);
        int count2 = carparkPaymentService.selectCount(new EntityWrapper().eq("space_id", spaceId)
                .eq("status", 2).between(true, "ctime", c2.getTimeInMillis(), System.currentTimeMillis()));

        int count3 = carparkPaymentService.selectCount(new EntityWrapper().eq("space_id", spaceId)
                .eq("status", 2));
        resultMap.put("today", count);
        resultMap.put("month", count2);
        resultMap.put("history", count3);
        return new BaseResult(BaseResultEnum.SUCCESS, resultMap);
    }



}
