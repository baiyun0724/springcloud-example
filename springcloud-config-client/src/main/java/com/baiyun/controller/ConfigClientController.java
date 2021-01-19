package com.baiyun.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("client")
public class ConfigClientController {

    @GetMapping("hello")
    public String hello(){
        return "springcloud-config-client";
    }
}
