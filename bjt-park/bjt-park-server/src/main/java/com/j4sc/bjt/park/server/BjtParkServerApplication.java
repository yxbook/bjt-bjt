package com.j4sc.bjt.park.server;

import com.j4sc.common.util.AESUtil;
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
 * @CreateDate: 2018 2018/4/3 10:44
 * @Version: 1.0
 **/
@EnableEurekaClient//Eureka服务发现
@EnableScheduling   //开启定时器
@EnableFeignClients//启动rpc
@EnableCircuitBreaker//开启容错
@EnableHystrixDashboard//监控面板
@ComponentScan(basePackages = {"com.j4sc"})//多包扫描
@EnableAutoConfiguration
public class BjtParkServerApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(BjtParkServerApplication.class);

    public static void main(String[] args) {
//        System.out.println(AESUtil.aesEncode(""));
//        System.out.println(AESUtil.aesDecode("G9Dbdjoc1B5kU2oD+gZy4w=="));
//        System.out.println(AESUtil.aesDecode("mWAeTehZJrn4lLbsDRU1CQ=="));
        LOGGER.info(">>>>>bjt-system-server 开始启动>>>>>");

        new SpringApplicationBuilder(BjtParkServerApplication.class).web(WebApplicationType.SERVLET).run(args);

        LOGGER.info(">>>>>bjt-system-server 启动完成>>>>>");
    }
}
