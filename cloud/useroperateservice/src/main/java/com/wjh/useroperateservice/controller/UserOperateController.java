package com.wjh.useroperateservice.controller;

import com.wjh.common.model.ResponseConstant;
import com.wjh.common.model.ResponseModel;
import com.wjh.common.model.ServiceIdConstant;
import com.wjh.menuoperateservicemodel.model.OperateVo;
import com.wjh.useroperateservice.service.UserOperateService;
import com.wjh.useroperateservicemodel.model.UserOperateDto;
import com.wjh.useroperateservicemodel.model.UserOperateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseModel listByUserId(@ApiParam(value = "用户ID", required = true) @RequestParam(required = true) Long userId) {


        try {
            List<UserOperateVo> list = userOperateService.listByUserId(userId);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResModel(list);
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.useroperateservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "更新用户权限")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseModel update(@ApiParam(value = "用户权限", required = true) @RequestBody(required = true) UserOperateDto userOperateDto,
                                HttpServletRequest httpServletRequest) {

        try {
            String loginUserId = httpServletRequest.getHeader("loginUserId");
            userOperateService.update(userOperateDto, Long.valueOf(loginUserId));
            ResponseModel responseModel = new ResponseModel();
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.useroperateservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "根据用户Id删除权限")
    @RequestMapping(value = "/deleteByUserId", method = RequestMethod.DELETE)
    public ResponseModel deleteByUserId(@ApiParam(value = "用户ID", required = true) @RequestParam(required = true) Long userId,
                                        HttpServletRequest httpServletRequest) {

        try {
            String loginUserId = httpServletRequest.getHeader("loginUserId");
            userOperateService.deleteByUserId(userId, Long.valueOf(loginUserId));
            ResponseModel responseModel = new ResponseModel();
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.useroperateservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "根据权限Id删除权限映射")
    @RequestMapping(value = "/deleteByOperateId", method = RequestMethod.DELETE)
    public ResponseModel deleteByOperateId(@ApiParam(value = "权限ID", required = true) @RequestParam(required = true) Long operateId,
                                           HttpServletRequest httpServletRequest) {

        try {
            String loginUserId = httpServletRequest.getHeader("loginUserId");
            userOperateService.deleteByOperateId(operateId, Long.valueOf(loginUserId));
            ResponseModel responseModel = new ResponseModel();
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.useroperateservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


}
