package com.baiyun.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 当有请求http://127.0.0.1:8083/refresh 节点的时候，会重新请求一次ConfigServer去拉取最新的配置文件
 * 请求http://127.0.0.1:8083/refresh  需要有几点要求：
 *      1.加actuator的依赖
 *      2.SpringCloud1.5以上需要设置 management.security.enabled=false
 *      3. post请求使用postman
 */
@RestController
@RefreshScope //开启更新功能
@RequestMapping("client")
public class ConfigClientController{

    @Value("${ftp.config.address}")
    protected String ftpAddress;

    @GetMapping("hello")
    public String hello(){
        return "springcloud-config-client, ftp address : " + ftpAddress;
    }
}
