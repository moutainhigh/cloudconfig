package com.wjh.roleoperateservice.service;

import com.wjh.common.model.ResponseModel;
import com.wjh.roleoperateservicemodel.model.RoleOperateVo;
import com.wjh.roleservicemodel.model.RoleVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "roleservice")
public interface RoleService {
    @RequestMapping(value = "/role/selectByIds", method = RequestMethod.GET)
    public ResponseModel<List<RoleVo>> selectByIds(@RequestBody(required = true) List<Long> idList);
}
