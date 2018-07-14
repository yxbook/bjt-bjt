package com.j4sc.bjt.system.server.rabbit.receiver;

import com.j4sc.bjt.system.server.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Description: 邮件队列消费者
 * @Author: LongRou
 * @CreateDate: 2018 2018/3/29 14:34
 * @Version: 1.0
 **/
@Component
@RabbitListener(queues = RabbitConfig.EMAIL_QUEUE)
public class EmailReceiver {

    @RabbitHandler
    public void process(String hello) {
        System.out.println("Receiver  : " + hello);
    }
}
