package com.wjh.userservice.service;

import com.wjh.idserviceapi.api.IdServiceI;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;


@FeignClient(value = "idservice")
public interface IdService extends IdServiceI {
}
