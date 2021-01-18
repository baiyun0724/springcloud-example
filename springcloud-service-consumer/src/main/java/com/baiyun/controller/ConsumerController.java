package com.baiyun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("consumer")
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    private final static String PRE_HOST="http://localhost:1008";


    @GetMapping("consume")
    public String consume(){
        return  restTemplate.getForObject(PRE_HOST+"/provider/hello", String.class);
    }
}

