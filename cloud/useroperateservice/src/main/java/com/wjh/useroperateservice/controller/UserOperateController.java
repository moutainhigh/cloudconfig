package com.wjh.useroperateservice.controller;

import com.wjh.common.model.ResponseConstant;
import com.wjh.common.model.ResponseModel;
import com.wjh.common.model.ServiceIdConstant;
import com.wjh.menuoperateservicemodel.model.OperateVo;
import com.wjh.useroperateservice.service.UserOperateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(description = "菜单相关接口")
@RefreshScope
@RestController
@RequestMapping("/useroperate")
public class UserOperateController {
    Logger logger = LogManager.getLogger();


    @Autowired
    UserOperateService userOperateService;


    @ApiOperation(value = "获取某人具有的权限")
    @RequestMapping(value = "/listByUserId", method = RequestMethod.GET)
    public ResponseModel listByUserId() {



        try {
            Long userId=1212323L;

            List<OperateVo> list = userOperateService.listByUserId(userId);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResModel(list);
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.menuoperateservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }

}
