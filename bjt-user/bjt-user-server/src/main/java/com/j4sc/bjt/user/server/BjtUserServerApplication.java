package com.j4sc.bjt.user.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/3/28 17:43 (exclude ={DataSourceAutoConfiguration.class,com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure.class})
 * @Version: 1.0
 **/
@EnableEurekaClient//Eureka服务发现
@EnableAutoConfiguration
@EnableScheduling   //开启定时器
@EnableFeignClients//启动rpc
@EnableCircuitBreaker//开启容错
@EnableHystrixDashboard//监控面板
@ComponentScan(basePackages = {"com.j4sc"})//多包扫描
@EnableSwagger2
public class BjtUserServerApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(BjtUserServerApplication.class);

    public static void main(String[] args) {

        LOGGER.info(">>>>>bjt-user-server 开始启动>>>>>");

        new SpringApplicationBuilder(BjtUserServerApplication.class).web(WebApplicationType.SERVLET).run(args);

        LOGGER.info(">>>>>bjt-user-server 启动完成>>>>>");
    }
}
