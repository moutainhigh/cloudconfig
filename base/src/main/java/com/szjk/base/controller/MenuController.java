package com.szjk.base.controller;

import com.szjk.base.model.common.ResponseModel;
import com.szjk.base.model.user.UserVo;
import com.szjk.base.service.MenuService;
import com.szjk.base.utils.HashToTreeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "菜单管理")
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    MenuService menuService;
    @ApiOperation(value = "列出菜单")
    @RequestMapping(value = "/navi", method = RequestMethod.GET)
    public List navi(HttpServletRequest httpServletRequest, Model model) {
        List<Map<String,Object>> list=menuService.selectNavi();

        return list;
      }

}
