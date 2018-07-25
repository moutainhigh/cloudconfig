package com.wjh.useroperateservice.service;

import com.wjh.common.model.ResponseModel;
import com.wjh.userservicemodel.model.UserVo;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "userservice")
public interface UserService {

    @RequestMapping(value = "/user/selectByIds", method = RequestMethod.POST)
    public ResponseModel<List<UserVo>> selectByIds(@RequestBody(required = true) java.util.List<Long> idList);

}
