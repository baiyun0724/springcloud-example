package com.baiyun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;


/**
 * 依次启动
 *      eureka-server
 *      service-provider
 *      service-provider2
 *      ribbon-client
 *      fegin-client
 *      zuul-gateway
 * 访问：
 *      http://127.0.0.1:2002/feign/client/hello
 *      http://127.0.0.1:2002/ribbon/client/hello
 *
 */
@EnableEurekaClient
@EnableZuulProxy // 该注解默认加上了@EnableCircuitBreaker和@EnableDiscoveryClient
@SpringBootApplication
public class ZuulGatawayApp {
    public static void main(String[] args) {
        SpringApplication.run(ZuulGatawayApp.class);
    }
}
