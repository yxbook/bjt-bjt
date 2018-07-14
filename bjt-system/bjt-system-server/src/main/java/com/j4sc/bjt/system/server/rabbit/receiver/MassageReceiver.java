package com.j4sc.bjt.system.server.rabbit.receiver;

import com.j4sc.bjt.system.server.config.RabbitConfig;
import com.j4sc.bjt.system.server.config.SMSConfig;
import com.j4sc.common.util.MD5Util;
import com.j4sc.common.util.TimeUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @Description: 邮件队列消费者
 * @Author: LongRou
 * @CreateDate: 2018 2018/3/29 14:34
 * @Version: 1.0
 **/
@Component
@RabbitListener(queues = RabbitConfig.MESSAGE_QUEUE)
public class MassageReceiver {
    @Autowired
    SMSConfig smsConfig;
    private static String accountSid = "1f9ff2fe2e0d4bc08fe89a0e43144d91";
    private static String authToken = "cb57d0358cfa415396fec3acdc055097";

    @RabbitHandler
    public void process(Map<String, Object> params) {
        RestTemplate restTemplate=new RestTemplate();
        String url="https://api.miaodiyun.com/20150822/industrySMS/sendSMS";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        LinkedMultiValueMap body=new LinkedMultiValueMap();
        body.add("accountSid",smsConfig.getAccountSid());
        body.add("templateid",params.get("templateid"));//225285090
        body.add("param",params.get("code"));
        body.add("to",params.get("tel"));
        String timestamp = TimeUtil.getFormatTimeFromTimestamp(System.currentTimeMillis(),"yyyyMMddHHmmss");
        body.add("timestamp",timestamp);
        body.add("sig", MD5Util.md5(smsConfig.getAccountSid()+smsConfig.getAuthToken()+timestamp).toLowerCase());
        HttpEntity entity = new HttpEntity(body, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        System.out.println(responseEntity);
    }

//    public static void main(String[] args) {
//        RestTemplate restTemplate=new RestTemplate();
//        String url="https://api.miaodiyun.com/20150822/industrySMS/sendSMS";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        LinkedMultiValueMap body=new LinkedMultiValueMap();
//        body.add("accountSid",accountSid);
//        //body.add("templateid","225285090");
//        body.add("templateid","279632942");
//        body.add("param","");
//        body.add("to","18786099722");
//        String timestamp = TimeUtil.getFormatTimeFromTimestamp(System.currentTimeMillis(),"yyyyMMddHHmmss");
//        body.add("timestamp",timestamp);
//        body.add("sig", MD5Util.md5(accountSid+authToken+timestamp).toLowerCase());
//
//        HttpEntity entity = new HttpEntity(body, headers);
//        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
//        System.out.println(responseEntity);
//    }
}
