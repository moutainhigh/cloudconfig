package com.wjh.useroperateservice.service;

import com.wjh.common.model.ResponseModel;
import com.wjh.menuoperateservicemodel.model.OperateVo;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "menuoperateservice")
public interface MenuOperateService {
    @RequestMapping(value = "/operate/selectByIds", method = RequestMethod.POST)
    public ResponseModel<List<OperateVo>> selectByIds(@ApiParam(value = "id列表", required = true) @RequestBody(required = false) List<Long> idList);
}
