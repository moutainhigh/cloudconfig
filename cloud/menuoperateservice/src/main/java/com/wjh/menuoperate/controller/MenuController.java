package com.wjh.menuoperate.controller;


import com.wjh.common.model.ResponseConstant;
import com.wjh.common.model.ResponseModel;
import com.wjh.common.model.ServiceIdConstant;
import com.wjh.menuoperate.service.MenuService;
import com.wjh.menuoperateservicemodel.model.MenuAddDto;
import com.wjh.menuoperateservicemodel.model.MenuPo;
import com.wjh.menuoperateservicemodel.model.MenuUpdateDto;
import com.wjh.menuoperateservicemodel.model.MenuVo;
import com.wjh.utils.redis.RedisCacheUtil;
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

@Api(description = "菜单相关接口")
@RefreshScope
@RestController
@RequestMapping("/menu")
public class MenuController {

    Logger logger = LogManager.getLogger();


    @Autowired
    MenuService menuService;


    @Autowired
    RedisCacheUtil redisCacheUtil;

    @ApiOperation(value = "搜索菜单")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseModel search(@ApiParam(value = "菜单名称", required = false) @RequestParam(required = false) String menuName,
                                @ApiParam(value = "当前页", required = true) @RequestParam(required = true) Integer currentPage,
                                @ApiParam(value = "每页多少条记录", required = true) @RequestParam(required = true) Integer pageSize) {


        logger.debug("request parameters: menuName=>{}", menuName);

        try {
            List<MenuVo> list = menuService.search(menuName, currentPage, pageSize);
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResModel(list);
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.menuoperateservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "添加菜单")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseModel add(@ApiParam(value = "菜单名称") @RequestBody(required = true) MenuAddDto menuAddDto,
                             HttpServletRequest request) {

        logger.debug("request parameters: menuAddDto=>{}", menuAddDto);
        String loginUserId=request.getHeader("loginUserId");

        try {
            MenuPo menuPo = new MenuPo();
            BeanUtils.copyProperties(menuPo, menuAddDto);
             MenuPo menuPoReturn = menuService.insert(menuPo,Long.valueOf(loginUserId));
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResModel(menuPoReturn);
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.menuoperateservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "更新菜单")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseModel update(@ApiParam(value = "菜单名称") @RequestBody(required = true) MenuUpdateDto menuUpdateDto,
                                HttpServletRequest request) {


        logger.debug("request parameters: menuUpdateDto=>{}", menuUpdateDto);
        String loginUserId=request.getHeader("loginUserId");
        try {
            MenuPo menuPo = new MenuPo();
            BeanUtils.copyProperties(menuPo, menuUpdateDto);
            MenuPo menuPoReturn = menuService.update(menuPo,Long.valueOf(loginUserId));
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResModel(menuPoReturn);
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.menuoperateservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "删除菜单")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseModel delete(@ApiParam(value = "菜单Id", required = true) @RequestParam(required = true) Long id,
                                HttpServletRequest request) {


        logger.debug("request parameters: id=>{}", id);

        String loginUserId=request.getHeader("loginUserId");
        try {
            menuService.delete(id);
            ResponseModel responseModel = new ResponseModel();
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.menuoperateservice, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


}
