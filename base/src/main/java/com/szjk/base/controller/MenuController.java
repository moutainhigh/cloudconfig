package com.szjk.base.controller;

import com.szjk.base.model.common.ResponseConstant;
import com.szjk.base.model.common.ResponseModel;
import com.szjk.base.model.menu.MenuAddDto;
import com.szjk.base.model.menu.MenuPo;
import com.szjk.base.model.menu.MenuUpdateDto;
import com.szjk.base.model.menu.MenuVo;
import com.szjk.base.model.user.UserVo;
import com.szjk.base.service.MenuService;
import com.szjk.base.utils.HashToTreeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "菜单管理")
@RestController
@RequestMapping("/menu")
public class MenuController {
    Logger logger = LogManager.getLogger();

    @Value("${server.servlet.context-path}")
    String contextPath;

    @Autowired
    MenuService menuService;

    @ApiOperation(value = "菜单导航")
    @RequestMapping(value = "/navi", method = RequestMethod.GET)
    public List navi() {
        List<Map<String, Object>> list = menuService.selectNavi(contextPath);
        return list;
    }


    @ApiOperation(value = "列出菜单")
    @RequestMapping(value = "/list", method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseModel<List<MenuVo>> list(@ApiParam(value = "菜单名称", required = false) @RequestParam(required = false) String menuName,
                                            @ApiParam(value = "当前页", required = false) @RequestParam(required = false) Integer page,
                                            @ApiParam(value = "每页多少条记录", required = false) @RequestParam(required = false) Integer rows) {
        List<MenuVo> list = menuService.search(menuName, page, rows);
        int total = menuService.searchCount(menuName);
        ResponseModel responseModel = new ResponseModel();
        responseModel.setRows(list);
        responseModel.setTotal(total);
        return responseModel;
    }

    @ApiOperation(value = "添加菜单")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseModel add(@ApiParam(value = "菜单名称") @RequestBody(required = true) MenuAddDto menuAddDto,
                             HttpServletRequest request) {

        logger.debug("request parameters: menuAddDto=>{}", menuAddDto);
//        String loginUserId = request.getHeader("loginUserId");
        String loginUserId ="2";
        try {
            MenuPo menuPo = new MenuPo();
            BeanUtils.copyProperties(menuPo, menuAddDto);
            MenuPo menuPoReturn = menuService.insert(menuPo, Long.valueOf(loginUserId));
            ResponseModel responseModel = new ResponseModel();
            responseModel.setRows(menuPoReturn);
            return responseModel;
        } catch (Exception e) {
            logger.error("error", e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "更新菜单")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseModel update(@ApiParam(value = "菜单名称") @RequestBody(required = true) MenuUpdateDto menuUpdateDto,
                                HttpServletRequest request) {


        logger.debug("request parameters: menuUpdateDto=>{}", menuUpdateDto);
        String loginUserId = request.getHeader("loginUserId");
        try {
            MenuPo menuPo = new MenuPo();
            BeanUtils.copyProperties(menuPo, menuUpdateDto);
            MenuPo menuPoReturn = menuService.update(menuPo, Long.valueOf(loginUserId));
            ResponseModel responseModel = new ResponseModel();
            responseModel.setRows(menuPoReturn);
            return responseModel;
        } catch (Exception e) {
            logger.error("error", e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }

}
