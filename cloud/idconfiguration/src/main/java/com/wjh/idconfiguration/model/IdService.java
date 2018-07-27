 package com.wjh.idconfiguration.model;
import com.wjh.common.model.ResponseModel;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


 @FeignClient(value = "idservice")
 public interface IdService {
     @RequestMapping(value = "/id/batchGenerateId", method = RequestMethod.GET)
     public ResponseModel<List<Long>> batchGenerateId();
 }
