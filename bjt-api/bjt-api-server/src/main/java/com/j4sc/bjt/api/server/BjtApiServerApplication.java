package com.j4sc.bjt.api.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description:
 * @Author: LongRou
 * @CreateDate: 2018 2018/3/29 15:01
 * @Version: 1.0
 **/
@EnableEurekaClient//Eureka服务发现
@EnableFeignClients//启动rpc
@EnableCircuitBreaker//开启容错
@EnableHystrixDashboard//监控面板
@ComponentScan(basePackages = {"com.j4sc"})//多包扫描
@EnableAutoConfiguration(exclude ={DataSourceAutoConfiguration.class,com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure.class})
@EnableSwagger2
@EnableCaching
public class BjtApiServerApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(BjtApiServerApplication.class);

    public static void main(String[] args) {

        LOGGER.info(">>>>>bjt-api-server 开始启动>>>>>");

        new SpringApplicationBuilder(BjtApiServerApplication.class).web(WebApplicationType.SERVLET).run(args);
        LOGGER.info(">>>>>bjt-api-server 启动完成>>>>>");
    }
}
