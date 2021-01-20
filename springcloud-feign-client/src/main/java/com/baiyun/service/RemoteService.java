package com.baiyun.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("springcloud-service-provider")
public interface RemoteService {

    @RequestMapping(value = "/provider/hello")
    String hello();
}
