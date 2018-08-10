package com.szjk.base.controller;

import com.szjk.base.model.common.ResponseModel;
import com.szjk.base.model.user.UserVo;
import com.szjk.base.utils.HashToTreeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "列出菜单")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List list(HttpServletRequest httpServletRequest, Model model) {
        List<Map<String,Object>> list=new ArrayList<>();
        Map<String, Object> rootM = new HashMap<>();
        rootM.put("id", 1);
        rootM.put("parentId", null);
        rootM.put("text", "1111111111");
        list.add(rootM);

        Map<String, Object> m1 = new HashMap<>();
        m1.put("id", 2);
        m1.put("parentId", 1);
        m1.put("text", "用户管理");
        list.add(m1);

        Map<String, Object> m2 = new HashMap<>();
        m2.put("id", 3);
        m2.put("parentId", 1);
        m2.put("text", "菜单管理");
        list.add(m2);

        Map<String, Object> m3 = new HashMap<>();
        m3.put("id", 4);
        m3.put("parentId", 2);
        m3.put("text", "Pc用户管理");
        list.add(m3);

        Map<String, Object> m = HashToTreeUtil.shuffleMapListToTree(list);


        return (List) m.get("children");
      }

}
