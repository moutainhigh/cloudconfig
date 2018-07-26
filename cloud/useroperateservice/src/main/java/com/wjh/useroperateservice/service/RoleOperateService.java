package com.wjh.useroperateservice.service;

import com.wjh.common.model.ResponseModel;
import com.wjh.roleoperateservicemodel.model.RoleOperateVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "roleoperateservice")
public interface RoleOperateService {
    @RequestMapping(value = "/roleoperate/listByRoleIds", method = RequestMethod.POST)
    public ResponseModel<List<RoleOperateVo>> listByRoleIds( @RequestBody(required = true) List<Long> roleIdList);
}
