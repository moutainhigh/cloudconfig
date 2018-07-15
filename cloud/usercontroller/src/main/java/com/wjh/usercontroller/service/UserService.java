package com.wjh.usercontroller.service;


import com.wjh.common.model.ResponseModel;
import com.wjh.usercontroller.hystrix.UserServiceHystrix;
import com.wjh.userservicemodel.model.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "userservice", fallback = UserServiceHystrix.class)
public interface UserService {
    @RequestMapping(value = "/user/detailByMobile", method = RequestMethod.GET)
    ResponseModel<User> detailByMobile(@RequestParam("mobile") String mobile);

}
