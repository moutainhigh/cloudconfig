package com.wjh.usercontroller.hystrix;

import com.wjh.common.model.ResponseModel;
import com.wjh.usercontroller.service.UserService;
import com.wjh.userservicemodel.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserServiceHystrix implements UserService {
    @Override
    public ResponseModel<User> detailByMobile(String mobile) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setResModel("aaaaaaaaaaaaaaaaaaaaaaaaa1");
        return responseModel;
    }

    @Override
    public ResponseModel register(String mobile, String password, String repeatPassword) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setResModel("aaaaaaaaaaaaaaaaaaaaaaaaa2");
        return responseModel;
    }

    @Override
    public ResponseModel<User> update(User user) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setResModel("aaaaaaaaaaaaaaaaaaaaaaaaa3");
        return responseModel;
    }

    @Override
    public ResponseModel<User> delete(String id) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setResModel("aaaaaaaaaaaaaaaaaaaaaaaaa3");
        return responseModel;
    }
}
