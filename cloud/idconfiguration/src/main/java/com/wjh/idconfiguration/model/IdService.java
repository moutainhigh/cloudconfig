 package com.wjh.idconfiguration.model;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


 @FeignClient(value = "idservice")
 public interface IdService {
     @RequestMapping(value = "/id/generateId", method = RequestMethod.GET)
     public long generateId();
 }
