package com.j4sc.bjt.system.server.config;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/3/29 14:24
 * @Version: 1.0
 **/
@Component
public class RabbitConfig {
    public static final String EMAIL_QUEUE = "Email-Queue";
    public static final String MESSAGE_QUEUE = "Message-Queue";
    public static final String PUSH_QUEUE = "Push-Queue";
    @Bean
    public Queue emailqueue(){
        return new Queue(EMAIL_QUEUE);
    }
    @Bean
    public Queue massagequeue(){
        return new Queue(MESSAGE_QUEUE);
    }
    @Bean
    public Queue pushqueue(){
        return new Queue(PUSH_QUEUE);
    }
}
