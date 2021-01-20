package com.baiyun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

/**
 * @EnableCircuitBreaker - 开启断路器。就是开启hystrix服务容错能力。
 * 当应用启用Hystrix服务容错的时候，必须增加的一个注解。
 */
@EnableCircuitBreaker
@SpringBootApplication
//@EnableEurekaClient
public class HystrixApp {
    public static void main(String[] args) {
        SpringApplication.run(HystrixApp.class);
    }
}
