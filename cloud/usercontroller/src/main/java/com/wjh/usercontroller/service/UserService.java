package com.wjh.usercontroller.service;


import com.wjh.userserviceapi.api.UserServiceI;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "userservice")
public interface UserService extends UserServiceI {
}
