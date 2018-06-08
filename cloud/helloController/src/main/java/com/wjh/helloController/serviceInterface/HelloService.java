package com.wjh.helloController.serviceInterface;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("helloService")
public interface HelloService {
    @RequestMapping("/hello/sayHello")
     String sayHello();
}
