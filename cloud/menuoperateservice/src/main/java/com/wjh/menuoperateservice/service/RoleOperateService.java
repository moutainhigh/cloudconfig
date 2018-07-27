package com.wjh.menuoperateservice.service;

import com.wjh.common.model.ResponseModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "roleoperateservice")
public interface RoleOperateService {

    @RequestMapping(value = "/roleoperate/deleteByOperateId", method = RequestMethod.DELETE)
    public ResponseModel deleteByOperateId( @RequestParam(name = "operateId",required = true) Long operateId);

}
