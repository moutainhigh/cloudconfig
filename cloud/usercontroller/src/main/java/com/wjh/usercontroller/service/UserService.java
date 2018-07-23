package com.wjh.usercontroller.service;


import com.wjh.common.model.ResponseModel;
import com.wjh.usercontroller.hystrix.UserServiceHystrix;
import com.wjh.userservicemodel.model.UserAddDto;
import com.wjh.userservicemodel.model.UserUpdateDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "userservice", fallback = UserServiceHystrix.class)
public interface UserService {
    @RequestMapping(value = "/user/detailByMobile", method = RequestMethod.GET)
    ResponseModel detailByMobile(@RequestParam("mobile") String mobile);


    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public ResponseModel register(
            @RequestParam("mobile") String mobile,
            @RequestParam("password") String password,
            @RequestParam("repeatPassword") String repeatPassword);


    @RequestMapping(value = "/user/update", method = RequestMethod.PUT)
    public ResponseModel update(@RequestBody UserUpdateDto user);



    @RequestMapping(value = "/user/delete", method = RequestMethod.DELETE)
    public ResponseModel delete(@RequestParam("id") String id);

}
