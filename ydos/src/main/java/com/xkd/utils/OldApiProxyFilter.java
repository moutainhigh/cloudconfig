package com.xkd.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/3/22.
 */
public class OldApiProxyFilter  extends HandlerInterceptorAdapter {
    @Autowired
    RedisCacheUtil redisCacheUtil;
    @Override
    public boolean preHandle(HttpServletRequest servletRequest, HttpServletResponse servletResponse, Object handler) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Map<String, String> headerMap = new HashMap<>();

        Enumeration<String> parameterNames = request.getParameterNames();
        Map<String, String> paramMap = new HashMap<>();
        String requestBody=null;
        //如果参数列表中有值，表示是form请求
        if (parameterNames.hasMoreElements()){
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                paramMap.put(paramName, request.getParameter(paramName));
            }
        }else {  //否则请求内容是放在reqeustBody里面
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuffer stringBuffer=new StringBuffer("");
            List<String> strList= org.apache.commons.io.IOUtils.readLines(bufferedReader);
            for (int i = 0; i <strList.size() ; i++) {
                stringBuffer.append(strList.get(i));
            }
            requestBody=stringBuffer.toString();
        }


        String method = request.getMethod();
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("code","-1");
        resultMap.put("msg","服务端调用设备接口失败");
        String result = "";
        String url = request.getRequestURI();
        String hostName = PropertiesUtil.YOUDIAN_OLDAPI_URL;
        String new_url = url.replace("/ydos/oldApi", "");
        String newHttpUrl = hostName + new_url;
        if ("POST".equals(method.toUpperCase())) {
            if( StringUtils.isNotBlank(requestBody)){  //如果请求参数放在requestBody中
                result = HttpRequestUtil.sendPostRequestBody(newHttpUrl, headerMap, requestBody);
            }else{  //如果请求参数放在form中
                result = HttpRequestUtil.sendPost(newHttpUrl, headerMap, paramMap);
            }
        } else if ("GET".equals(method.toUpperCase())) {
            result = HttpRequestUtil.sendGet(newHttpUrl, headerMap, paramMap);
        } else if ("PUT".equals(method.toUpperCase())){
            if (StringUtils.isNotBlank(requestBody)){
                result = HttpRequestUtil.sendPutRequestBody(newHttpUrl, headerMap, requestBody);
            }else {
                result = HttpRequestUtil.sendPut(newHttpUrl, headerMap, paramMap);
            }
        }else {
            result="-1";
        }
        if ("-1".equals(result)){
            result= JSON.toJSONString(resultMap);
        }
        returnMessage((HttpServletResponse) servletResponse, result);
        return false;
    }

    private void returnMessage(HttpServletResponse response, String str) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.print(JSON.toJSON(str));
        out.flush();
        out.close();
    }

}
