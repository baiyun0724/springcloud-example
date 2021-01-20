package com.baiyun.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("provider")
public class ProviderController {

    @GetMapping("hello")
    public String hello(){
        return "springcloud-service-provider2  :  hello world";
    }

}
