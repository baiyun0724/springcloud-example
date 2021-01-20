package com.baiyun.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 如果不使用Hystrix服务容错功能，在application client端，
 * 服务接口只需要继承服务标准api接口即可实现远程服务调用。
 * 如果使用了Hystrix，则有不同的编写方式。
 */
@FeignClient("springcloud-service-provider")
public interface RemoteService {

    @RequestMapping(value = "/provider/hello")
    String hello();
}
