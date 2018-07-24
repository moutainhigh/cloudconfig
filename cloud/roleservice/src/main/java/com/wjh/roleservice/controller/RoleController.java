package com.wjh.roleservice.controller;


import com.wjh.common.model.ResponseConstant;
import com.wjh.common.model.ResponseModel;
import com.wjh.common.model.ServiceIdConstant;
import com.wjh.roleservice.sevice.IdService;
import com.wjh.roleservice.sevice.RoleService;
import com.wjh.roleservicemodel.model.RoleAddDto;
import com.wjh.roleservicemodel.model.RolePo;
import com.wjh.roleservicemodel.model.RoleUpdateDto;
import com.wjh.roleservicemodel.model.RoleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(description = "角色相关接口")
@RefreshScope
@RestController
@RequestMapping("/role")
public class RoleController {


    Logger logger = LogManager.getLogger();

    @Autowired
    IdService idService;


    @Autowired
    RoleService roleService;

    @ApiOperation(value = "查询角色")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseModel search(@ApiParam(value = "角色名称", required = true) @RequestParam(required = true) String roleName,
                                @ApiParam(value = "当前页", required = true) @RequestParam(required = true) Integer currentPage,
                                @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize) {

        logger.debug("request parameters: roleName=>{},currentPage=>{},pageSize=>{}", roleName, currentPage, pageSize);


        try {
            List<RoleVo> roleVoList = roleService.search(roleName, currentPage, pageSize);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResModel(roleVoList);
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.userservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }

    @ApiOperation(value = "通过Ids查询")
    @RequestMapping(value = "/selectByIds", method = RequestMethod.GET)
    public ResponseModel selectByIds(@ApiParam(value = "id数组 ", required = true) @RequestBody(required = true) List<Long> idList
                             ) {

        logger.debug("request parameters:idList=>{}", idList);


        try {
            List<RoleVo> roleVoList = roleService.selectByIds(idList);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResModel(roleVoList);
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.userservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "添加角色")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseModel add(@ApiParam(value = "角色") @RequestBody(required = true) RoleAddDto roleAddDto,
                             HttpServletRequest httpServletRequest) {

        logger.debug("request parameters: roleAddDto=>{}", roleAddDto);

        String loginUserId = httpServletRequest.getHeader("loginUserId");

        try {

            RolePo rolePo = new RolePo();
            BeanUtils.copyProperties(rolePo, roleAddDto);
            RolePo rolePoReturn = roleService.insert(rolePo, Long.valueOf(loginUserId));
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResModel(rolePoReturn);
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.roleservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }



    @ApiOperation(value = "添加角色")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseModel update(@ApiParam(value = "角色") @RequestBody(required = true) RoleUpdateDto roleUpdateDto,
                             HttpServletRequest httpServletRequest) {

        logger.debug("request parameters: roleUpdateDto=>{}", roleUpdateDto);

        String loginUserId = httpServletRequest.getHeader("loginUserId");

        try {

            RolePo rolePo = new RolePo();
            BeanUtils.copyProperties(rolePo, roleUpdateDto);
            RolePo rolePoReturn = roleService.update(rolePo, Long.valueOf(loginUserId));
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResModel(rolePoReturn);
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.roleservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "删除角色")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseModel delete(@ApiParam(value = "角色ID") @RequestParam(required = true) Long id,
                                HttpServletRequest httpServletRequest) {

        logger.debug("request parameters: id=>{}", id);

        String loginUserId = httpServletRequest.getHeader("loginUserId");

        try {

            roleService.delete(id, Long.valueOf(loginUserId));
            ResponseModel responseModel = new ResponseModel();
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.roleservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }

}
