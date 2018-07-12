package com.wjh.userserviceapi.api;


import com.wjh.common.model.ResponseModel;
import com.wjh.userserviceapi.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/user")
public interface UserServiceI {

    @RequestMapping(value = "/detailByMobile", method = RequestMethod.GET)
    ResponseModel<User> detailByMobile(@RequestParam("mobile") String mobile);

}
