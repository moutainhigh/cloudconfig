package com.wjh.helloService.controller;

 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
 import org.springframework.cloud.client.discovery.DiscoveryClient;
 import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {


    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping(value = "/sayHello")
    public Object hello(){
        ServiceInstance serviceInstance=discoveryClient.getLocalServiceInstance();
        System.out.println(serviceInstance.getHost()+"===="+serviceInstance.getServiceId());

        return "哈喽";
    }

}
