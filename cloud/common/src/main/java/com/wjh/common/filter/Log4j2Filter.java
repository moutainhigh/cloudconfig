package com.wjh.common.filter;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


@Component
@WebFilter(urlPatterns = "/*")
public class Log4j2Filter implements Filter {

    static String hostname;
    static String ip;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName().toString(); //获取本机计算机名称
            ip = addr.getHostAddress().toString(); //获取本机ip

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ThreadContext.put("ip", ip);
        ;
        ThreadContext.put("hostname", hostname);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
