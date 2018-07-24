package com.wjh.useroperateservice.service;

import com.wjh.common.model.ResponseModel;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "userservice")
public interface UserService {

    @RequestMapping(value = "/userservice/selectByIds", method = RequestMethod.GET)
    public ResponseModel selectByIds( @RequestBody(required = true) java.util.List<Long> idList);

}
