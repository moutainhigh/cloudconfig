package com.wjh.useroperateservice.service;

import com.wjh.common.model.ResponseModel;
import com.wjh.userroleservicemodel.model.UserRoleVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "userroleservice")
public interface UserRoleService {
    @RequestMapping(value = "/userrole/listByUserId", method = RequestMethod.GET)
    public ResponseModel<List<UserRoleVo>> listByUserId( @RequestParam(name = "userId",required = true) Long userId);

}
