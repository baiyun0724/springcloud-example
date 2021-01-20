package com.baiyun.controller;

import com.baiyun.service.HystrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hystrix")
public class HystrixController {

    @Autowired
    private HystrixService hystrixService;

    @RequestMapping("helloA")
    public String helloA(){
        String dataA = hystrixService.getDataA();
        return dataA;
    }
    @RequestMapping("helloB")
    public String helloB(){
        String dataB = hystrixService.getDataB();
        System.out.println(dataB);
        return dataB;
    }
    @RequestMapping("helloC")
    public String helloC(){
        String dataC = hystrixService.getDataC();
        System.out.println(dataC);
        return dataC;
    }
    @RequestMapping("helloD")
    public String helloD(){
        String dataD = hystrixService.getDataD();
        System.out.println(dataD);
        return dataD;
    }

}
