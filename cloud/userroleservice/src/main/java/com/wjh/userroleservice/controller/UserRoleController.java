package com.wjh.userroleservice.controller;

import com.wjh.common.model.ResponseConstant;
import com.wjh.common.model.ResponseModel;
import com.wjh.common.model.ServiceIdConstant;
import com.wjh.userroleservice.service.UserRoleService;
import com.wjh.userroleservicemodel.model.UserRoleDto;
import com.wjh.userroleservicemodel.model.UserRoleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@RequestMapping("/userrole")
public class UserRoleController {
    Logger logger = LogManager.getLogger();

    @Autowired
    UserRoleService userRoleService;


    @ApiOperation(value = "获取某人拥有的角色")
    @RequestMapping(value = "/listByUserId", method = RequestMethod.GET)
    public ResponseModel<List<UserRoleVo>> listByUserId(@ApiParam(value = "用户ID", required = true) @RequestParam(required = true) Long userId) {


        try {
            List<UserRoleVo> list = userRoleService.listByUserId(userId);
            ResponseModel<List<UserRoleVo>> responseModel = new ResponseModel();
            responseModel.setResModel(list);
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.userroleservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "更新某人的角色")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseModel update(@ApiParam(value = "用户权限", required = true) @RequestBody(required = true) UserRoleDto userRoleDto,
                                HttpServletRequest httpServletRequest) {


        try {
            String loginUserId = httpServletRequest.getHeader("loginUserId");
            userRoleService.update(userRoleDto, Long.valueOf(loginUserId));
            ResponseModel responseModel = new ResponseModel();
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.userroleservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "删除某人的角色")
    @RequestMapping(value = "/deleteByUserId", method = RequestMethod.PUT)
    public ResponseModel deleteByUserId(@ApiParam(value = "用户ID", required = true) @RequestParam(required = true) Long userId,
                                        HttpServletRequest httpServletRequest) {


        try {
            String loginUserId = httpServletRequest.getHeader("loginUserId");
            userRoleService.deleteByUserId(userId);
            ResponseModel responseModel = new ResponseModel();
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.userroleservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "删除角色对应的映射")
    @RequestMapping(value = "/deleteByRoleId", method = RequestMethod.PUT)
    public ResponseModel deleteByRoleId(@ApiParam(value = "用户ID", required = true) @RequestParam(required = true) Long roleId,
                                        HttpServletRequest httpServletRequest) {

        try {
            String loginUserId = httpServletRequest.getHeader("loginUserId");
            userRoleService.deleteByRoleId(roleId);
            ResponseModel responseModel = new ResponseModel();
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.userroleservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }

}
