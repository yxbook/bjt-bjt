package com.j4sc.bjt.user.server.util;

import com.j4sc.bjt.user.server.client.SystemApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description:
 * @Author: chengyz
 * @CreateDate: 2018/5/3 17:20
 * @Version: 1.0
 **/
@Component
public class SendToPushQueue {
    @Autowired
    private SystemApiClient systemApiClient;

    private static SendToPushQueue sendToPushQueue = null;

    private SendToPushQueue() {
    }

    public static SendToPushQueue getInstance() {
        if (sendToPushQueue == null) {
            sendToPushQueue = new SendToPushQueue();
        }
        return sendToPushQueue;
    }

    public void sendToPushQueue(String title, String body, String target, String id, List<String> phoneList, String phone) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("title", title);
        paramsMap.put("body", body);
        paramsMap.put("target", target);
        paramsMap.put("id", id);
        if (phoneList == null || phoneList.size() < 1) {
            phoneList = new ArrayList<>();
            phoneList.add(phone);
        }
        paramsMap.put("phoneList", phoneList);
        systemApiClient.sendToPushQueue(paramsMap);//发送消息到MQ
    }

}
