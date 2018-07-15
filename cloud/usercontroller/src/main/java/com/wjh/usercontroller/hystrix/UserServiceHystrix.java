package com.wjh.usercontroller.hystrix;

import com.wjh.common.model.ResponseModel;
import com.wjh.usercontroller.service.UserService;
import com.wjh.userservicemodel.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserServiceHystrix  implements UserService{
    @Override
    public ResponseModel<User> detailByMobile(String mobile) {
        ResponseModel responseModel=new ResponseModel();
        responseModel.setResModel("aaaaaaaaaaaaaaaaaaaaaaaaa");
        return responseModel;
    }
}
