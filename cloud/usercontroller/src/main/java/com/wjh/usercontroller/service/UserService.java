package com.wjh.usercontroller.service;


import com.wjh.common.model.ResponseModel;
import com.wjh.usercontroller.hystrix.UserServiceHystrix;
import com.wjh.userservicemodel.model.User;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "userservice", fallback = UserServiceHystrix.class)
public interface UserService {
    @RequestMapping(value = "/user/detailByMobile", method = RequestMethod.GET)
    ResponseModel<User> detailByMobile(@RequestParam("mobile") String mobile);


    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public ResponseModel register(
            @RequestParam("mobile") String mobile,
            @RequestParam("password") String password,
            @RequestParam("repeatPassword") String repeatPassword);


    @RequestMapping(value = "/user/update", method = RequestMethod.PUT)
    public ResponseModel<User> update( @RequestBody User user);



    @RequestMapping(value = "/user/delete", method = RequestMethod.DELETE)
    public ResponseModel<User> delete( @RequestParam("id") String id);

}
