package com.baiyun.controller;

import com.baiyun.service.RemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("client")
public class FeignController {

    @Autowired
    private RemoteService remoteService;

    @GetMapping("hello")
    public String hello(){
        String hello = remoteService.hello();
        System.out.println("调用eureka服务返回：" + hello);
        return "FeignController hello";
    }
}
