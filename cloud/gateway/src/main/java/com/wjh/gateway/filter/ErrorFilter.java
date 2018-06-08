package com.wjh.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ErrorFilter extends ZuulFilter {


    Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {

        RequestContext ctx=RequestContext.getCurrentContext();
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();

        Throwable throwable=ctx.getThrowable();
        logger.info("发送{}请求到{}报错：{}", request.getMethod(), request.getRequestURL().toString(),throwable.getCause().getCause());
        ctx.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        ctx.set("error.exception",throwable.getCause());

        return null;
    }
}
