package com.wjh.gateway.service;


import com.wjh.common.model.ResponseModel;
import com.wjh.menuoperateservicemodel.model.OperateVo;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "useroperateservice")
public interface UserOperateService {
    @RequestMapping(value = "/useroperate/listAllByUserId", method = RequestMethod.GET)
    public ResponseModel<List<OperateVo>> listAllByUserId(@RequestParam(name = "userId",required = true) Long userId);
}
