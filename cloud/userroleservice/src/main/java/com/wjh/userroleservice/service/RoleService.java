package com.wjh.userroleservice.service;

import com.wjh.common.model.ResponseModel;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "roleservice")
public interface RoleService {
    @RequestMapping(value = "/role/selectByIds", method = RequestMethod.GET)
    public ResponseModel selectByIds( @RequestBody(required = true) List<Long> idList);
}
