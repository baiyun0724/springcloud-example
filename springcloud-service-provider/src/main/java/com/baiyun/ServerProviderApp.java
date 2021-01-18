package com.baiyun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ServerProviderApp {
    public static void main(String[] args) {
        SpringApplication.run(ServerProviderApp.class);
    }
}
