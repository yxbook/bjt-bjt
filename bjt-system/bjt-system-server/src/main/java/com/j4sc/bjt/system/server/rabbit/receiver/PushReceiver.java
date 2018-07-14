package com.j4sc.bjt.system.server.rabbit.receiver;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.utils.ParameterHelper;
import com.j4sc.bjt.system.server.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Description: 消息推送队列消费者
 * @Author: LongRou
 * @CreateDate: 2018 2018/3/29 14:34
 * @Version: 1.0
 **/
@Component
@RabbitListener(queues = RabbitConfig.PUSH_QUEUE)
public class PushReceiver {

    private static final String ACCESS_KEY_ID = "LTAIYPcCuB7AHMc5";
    private static final String ACCESS_KEY_SECRET = "DPbheMNo9YyG4zaAvmBksPtrRdMRiW";
    private static final Long APP_KEY_ANDROID = Long.valueOf(24869572);
    private static final Long APP_KEY_IOS = Long.valueOf(24869187);
    /**
     * extParams.put("target", "NEW_ORDER")
     * extParams.put("deliveryOrderId", deliveryOrderId);
     * String title = "新订单通知";
     * String body = "您有新订单需要处理";
     * @param params
     */
    @RabbitHandler
    public void process(Map<String, Object> params) {
        // 向Android、iOS端发送推送通知
        try {
            String title = params.get("title")+"";
            String body = params.get("body")+"";
            params.remove("title");
            params.remove("body");
            List<String> phoneList = (List<String>)params.get("phoneList");
            params.remove("phoneList");
           this.pushNoticeToBoth(title, body, params, phoneList);
        } catch (ClientException e) {
            e.printStackTrace();
        }

    }

    public void pushNoticeToAndroid(String title, String body, Map<String, Object> extParams, List<String> listAccountId) throws ClientException {
        PushRequest pushRequest = this.getNoticePushRequest(title, body, listAccountId);

        // 安卓设备通知配置
        pushRequest.setAppKey(APP_KEY_ANDROID);
        pushRequest.setAndroidOpenType("NONE");
        pushRequest.setAndroidNotifyType("BOTH"); // 通知提醒方式 声音+震动
        pushRequest.setAndroidNotificationBarType(0);
        pushRequest.setAndroidNotificationBarPriority(1);
        pushRequest.setAndroidOpenType("ACTIVITY");
        pushRequest.setAndroidActivity("com.bangjiat.bjt");//安卓包名
        pushRequest.setAndroidMusic("default");
        pushRequest.setAndroidExtParameters(JSON.toJSONString(extParams));

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        client.getAcsResponse(pushRequest);
    }

    public void pushNoticeToIOS(String title, String body, Map<String, Object> extParams, List<String> listAccountId) throws ClientException {
        PushRequest pushRequest = this.getNoticePushRequest(title, body, listAccountId);

        // iOS设备通知配置
        pushRequest.setAppKey(APP_KEY_IOS);
        pushRequest.setIOSBadge(1);
        pushRequest.setIOSMusic("default");
        pushRequest.setIOSApnsEnv("DEV");//DEV：表示开发环境,PRODUCT：表示生产环境
        pushRequest.setIOSRemind(true);
        pushRequest.setIOSRemindBody(body);
        pushRequest.setIOSExtParameters(JSON.toJSONString(extParams));

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        client.getAcsResponse(pushRequest);
    }

    public void pushNoticeToBoth(String title, String body, Map<String, Object> extParams, List<String> listAccountId) throws ClientException {
        this.pushNoticeToAndroid(title, body, extParams, listAccountId);
        this.pushNoticeToIOS(title, body, extParams, listAccountId);
    }

    private PushRequest getNoticePushRequest(String title, String body, List<String> listAccountId) {
        PushRequest pushRequest = new PushRequest();
        pushRequest.setTarget("ALL");
        pushRequest.setTargetValue("ALL");

        if (listAccountId.size() > 0) {
            pushRequest.setTarget("ACCOUNT");
            pushRequest.setTargetValue(String.join(",", listAccountId));
        }
        pushRequest.setDeviceType("ALL");
        pushRequest.setPushType("NOTICE");
        pushRequest.setTitle(title);
        pushRequest.setBody(body);

        Date pushDate = new Date(System.currentTimeMillis());
        String pushTime = ParameterHelper.getISO8601Time(pushDate);
        pushRequest.setPushTime(pushTime); // 指定推送时间 可选

        String expireTime = ParameterHelper.getISO8601Time(new Date(System.currentTimeMillis() + 12 * 3600 * 1000)); // 失效时间，过期不再发送
        pushRequest.setExpireTime(expireTime);

        pushRequest.setStoreOffline(true); // 是否保存离线消息，若保存，在推送时，用户即使不在线，下一次上线则会收到

        return pushRequest;
    }

}
