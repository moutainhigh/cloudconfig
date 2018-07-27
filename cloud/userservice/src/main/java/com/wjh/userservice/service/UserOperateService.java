package com.wjh.userservice.service;

import com.wjh.common.model.ResponseModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "useroperateservice")
public interface UserOperateService {
    @RequestMapping(value = "/useroperate/deleteByUserId", method = RequestMethod.DELETE)
    public ResponseModel deleteByUserId(@RequestParam(name = "userId", required = true) Long userId);
}
