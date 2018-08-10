package com.wjh.roleservice.sevice;

import com.wjh.common.model.ResponseModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "roleoperateservice")
public interface RoleOperateService {
    @RequestMapping(value = "/roleoperate/deleteByRoleId", method = RequestMethod.DELETE)
    public ResponseModel deleteByRoleId( @RequestParam(name = "roleId",required = true) Long roleId);
}
