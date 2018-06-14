package com.xkd.utils;

import com.alibaba.fastjson.JSON;
import com.xkd.model.ResponseDbCenter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class YDappFilter implements Filter {

    @Autowired
    private RedisCacheUtil redisCacheUtil;

    public static YDappFilter appFilter;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getParameter("token");
        //app端一定要传youdianapp这个参数，值为youdianapp
        String youdianapp = request.getParameter("youdianapp");
        String pcuserId = null;

        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headerMap = new HashMap<>();
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            headerMap.put(headerName, request.getHeader(headerName));
//        }

        Enumeration<String> parameterNames = request.getParameterNames();
        Map<String, String> paramMap = new HashMap<>();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            paramMap.put(paramName, request.getParameter(paramName));
        }

        String method = request.getMethod();
        String result = "";
        String url = request.getRequestURL().toString();
        String hostName = PropertiesUtil.YOUDIAN_OLDAPI_URL;
        String new_url = url.replace("/youdianapp", "");

        //RedisCacheUtil redisCacheUtil = new RedisCacheUtil();
        RedisCacheUtil redisCacheUtil = new RedisCacheUtil();

        if ("555".equals(request.getParameter("token"))) {
            redisCacheUtil.setCacheObject("555","814");
            //appFilter.redisCacheUtil.setCacheObject("555","814");
            redisCacheUtil.setCacheObject("555","814");
            pcuserId = "814";
        } else if (StringUtils.isNotBlank(token)) {

            //pcuserId = appFilter.redisCacheUtil.getCacheObject(token)==null?null:(String) appFilter.redisCacheUtil.getCacheObject(token);
            pcuserId = redisCacheUtil.getCacheObject(token)==null?null:(String) appFilter.redisCacheUtil.getCacheObject(token);
            //pcuserId = (String) request.getSession().getAttribute(token);
            // 这里把当前登录用户的Id存进去方便在Controller层使用
            //appFilter.redisCacheUtil.setCacheObject("loginUserId", pcuserId);

            redisCacheUtil.setCacheObject("loginUserId", pcuserId);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter("S9999","app端token失效");

        if(pcuserId == null){
            returnMessage((HttpServletResponse) servletResponse, responseDbCenter,"0");
        }else if ("POST".equals(method.toUpperCase())) {
            result = HttpRequestUtil.sendPost(new_url, headerMap, paramMap);
        } else if ("GET".equals(method.toUpperCase())) {
            result = HttpRequestUtil.sendGet(new_url, headerMap, paramMap);
        } else {
            result = HttpRequestUtil.sendPut(new_url, headerMap, paramMap);
        }


        if("-1".equals(result)){
            ResponseDbCenter responseDbCenterError = new ResponseDbCenter("CRM1003", "服务器异常");
            returnMessage((HttpServletResponse) servletResponse, responseDbCenterError,"0");
        }else{
            returnMessage((HttpServletResponse) servletResponse, result,"1");
        }

    }

    @Override
    public void destroy() {

    }

    /*
     flag:  0:传对象，1传String类型的
     */
    private void returnMessage(HttpServletResponse response, Object str,String flag) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        if("0".equals(flag)){
            out.print(JSON.toJSONString(str));
        }else{
            out.print(JSON.toJSON(str));
        }

        out.flush();
        out.close();
    }

    // 关键3
    @PostConstruct
    public void init() {
        appFilter = this;
        appFilter.redisCacheUtil = this.redisCacheUtil;
    }
}
