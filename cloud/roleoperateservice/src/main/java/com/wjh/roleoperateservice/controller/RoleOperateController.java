package com.wjh.roleoperateservice.controller;

import com.wjh.common.model.ResponseConstant;
import com.wjh.common.model.ResponseModel;
import com.wjh.common.model.ServiceIdConstant;
import com.wjh.menuoperateservicemodel.model.OperateVo;
import com.wjh.roleoperateservice.service.RoleOperateService;
import com.wjh.roleoperateservicemodel.model.RoleOperateDto;
import com.wjh.roleoperateservicemodel.model.RoleOperateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Api(description = "角色权限相关接口")
@RefreshScope
@RestController
@RequestMapping("/roleoperate")
public class RoleOperateController {
    Logger logger = LogManager.getLogger();

    @Autowired
    RoleOperateService roleOperateService;

    @ApiOperation(value = "获取角色具有的权限")
    @RequestMapping(value = "/listByRoleIds", method = RequestMethod.POST)
    public ResponseModel<List<RoleOperateVo>> listByRoleIds(@ApiParam(value = "角色ID列表", required = true) @RequestBody(required = true) List<Long> roleIdList) {


        try {
            List<RoleOperateVo> list = roleOperateService.listByRoleIds(roleIdList);
            ResponseModel<List<RoleOperateVo>> responseModel = new ResponseModel();
            responseModel.setResModel(list);
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.roleoperateservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "获取角色具有的权限")
    @RequestMapping(value = "/listByRoleId", method = RequestMethod.GET)
    public ResponseModel<List<RoleOperateVo>> listByRoleId(@ApiParam(value = "角色ID", required = true) @RequestParam(required = true) Long roleId) {


        try {
            List<Long> roleIdList = new ArrayList<>();
            roleIdList.add(roleId);
            List<RoleOperateVo> list = roleOperateService.listByRoleIds(roleIdList);
            ResponseModel<List<RoleOperateVo>> responseModel = new ResponseModel();
            responseModel.setResModel(list);
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.roleoperateservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "更新角色权限")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseModel update(@ApiParam(value = "角色权限", required = true) @RequestBody(required = true) RoleOperateDto roleOperateDto,
                                HttpServletRequest httpServletRequst) {


        try {
            String loginUserId = httpServletRequst.getHeader("loginUserId");
            roleOperateService.update(roleOperateDto, Long.valueOf(loginUserId));
            ResponseModel responseModel = new ResponseModel();
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.roleoperateservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "删除角色下的权限")
    @RequestMapping(value = "/deleteByRoleId", method = RequestMethod.DELETE)
    public ResponseModel deleteByRoleId(@ApiParam(value = "角色权限", required = true) @RequestParam(required = true) Long roleId,
                                        HttpServletRequest httpServletRequst) {


        try {
            String loginUserId = httpServletRequst.getHeader("loginUserId");
            roleOperateService.deleteByRoleId(roleId);
            ResponseModel responseModel = new ResponseModel();
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.roleoperateservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "根据权限Id删除权限")
    @RequestMapping(value = "/deleteByOperateId", method = RequestMethod.DELETE)
    public ResponseModel deleteByOperateId(@ApiParam(value = "角色权限", required = true) @RequestParam(required = true) Long operateId,
                                           HttpServletRequest httpServletRequst) {


        try {
            String loginUserId = httpServletRequst.getHeader("loginUserId");
            roleOperateService.deleteByOperateId(operateId);
            ResponseModel responseModel = new ResponseModel();
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.roleoperateservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }

}
