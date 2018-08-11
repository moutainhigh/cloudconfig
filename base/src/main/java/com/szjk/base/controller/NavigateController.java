package com.szjk.base.controller;

import com.szjk.base.model.user.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;



@Api(value = "主页")
@Controller
public class NavigateController {
    @ApiOperation(value = "主页")
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String main(HttpServletRequest httpServletRequest, Model model) {
         return "/main";
    }


    @ApiOperation(value = "流程")
    @RequestMapping(value = "/flow", method = RequestMethod.GET)
    public String index(HttpServletRequest httpServletRequest, Model model) {
        return "/flow";
    }


    @ApiOperation(value = "")
    @RequestMapping(value = "/process", method = RequestMethod.GET)
    public String process(HttpServletRequest httpServletRequest, Model model) {
        return "/process";
    }



    @ApiOperation(value = "")
    @RequestMapping(value = "/providers", method = RequestMethod.GET)
    public String providers(HttpServletRequest httpServletRequest, Model model) {
        return "/providers";
    }

    @ApiOperation(value = "")
    @RequestMapping(value = "/basicInfo", method = RequestMethod.GET)
    public String basicInfo(HttpServletRequest httpServletRequest, Model model) {
        return "/basicInfo";
    }

    @ApiOperation(value = "")
    @RequestMapping(value = "/sysmenu/list", method = RequestMethod.GET)
    public String sysMenu(HttpServletRequest httpServletRequest, Model model) {
        return "/sysmenu/list";
    }

    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest httpServletRequest, Model model) {
        return "/login";
    }

    @RequestMapping("/login")
    public String loginUser(String username, String password, HttpSession session) {
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(usernamePasswordToken);   //完成登录
            UserVo user = (UserVo) subject.getPrincipal();
            session.setAttribute("user", user);
            return "main";
        } catch (Exception e) {
            return "login";//返回登录页面
        }

    }



    @RequestMapping("/welcome")
    public String nav(HttpSession session) {
           return "welcome";
    }


    @RequestMapping("/logout")
    public String logOut(HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
//        session.removeAttribute("user");
        return "login";
    }




    @RequestMapping("/workbench")
    public String workbench(HttpSession session) {
        return "workbench";
    }





}
