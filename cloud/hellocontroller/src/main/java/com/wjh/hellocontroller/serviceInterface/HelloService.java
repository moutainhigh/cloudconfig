package com.wjh.hellocontroller.serviceInterface;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("helloService")
public interface HelloService {
    @RequestMapping(value = "/hello/sayHello",method = RequestMethod.GET)
     String sayHello(@RequestParam("name") String name);
}
