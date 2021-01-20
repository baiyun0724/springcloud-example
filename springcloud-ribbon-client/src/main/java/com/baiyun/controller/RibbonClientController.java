package com.baiyun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 将springcloud-service-provider注册到eureka，使用ribbon进行负载均衡的调用
 */
@RestController
@RequestMapping("client")
public class RibbonClientController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("hello")
    public String ribbonHello(){
        /**
         *  使用RestTemplate类调用其他系统的url的时候，加上ribbon的注解@LoadBalanced后，不能再直接访问服务的ip，
         *  否则会报错：
         *          No instances available for 127.0.0.1
         */
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://springcloud-service-provider/provider/hello", String.class);
//        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://127.0.0.1:1008/provider/hello", String.class);
        System.out.println("调用springcloud-service-provider的接口返回：" + forEntity.getBody());
        return "hello ribbon";
    }
}
