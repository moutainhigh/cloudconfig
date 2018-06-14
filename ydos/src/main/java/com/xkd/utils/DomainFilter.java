package com.xkd.utils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by dell on 2018/3/22.
 */
public class DomainFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

         HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;
//        response.setHeader("Access-Control-Allow-Origin",request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Origin","*");  //允许跨域访问的域
//        response.setHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS,DELETE,PUT");  //允许使用的请求方法
//        response.setHeader("Access-Control-Expose-Headers","*");
//        response.setHeader("Access-Control-Allow-Headers", "*");  //允许使用的请求方法
//        response.setHeader("Access-Control-Allow-Credentials","true");//是否允许请求带有验证信息
        filterChain.doFilter(request, response);


    }

    @Override
    public void destroy() {

    }
}
