package com.wjh.log4j2kafka.filter;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;


@Component
@WebFilter(urlPatterns = "/*")
public class Log4j2Filter implements Filter {

    @Value("${spring.application.name}")
    String applicationName;

    @Value("${spring.cloud.client.ipAddress}")
    String ip;

    @Value("${spring.cloud.client.hostname}")
    String hostname;

    @Value("${server.port}")
    String port;





    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ThreadContext.put("ip", ip);
        ThreadContext.put("hostname", hostname);
        ThreadContext.put("applicationName", applicationName);
        ThreadContext.put("port", port);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
