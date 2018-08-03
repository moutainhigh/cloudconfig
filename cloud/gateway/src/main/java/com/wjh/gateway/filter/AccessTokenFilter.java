package com.wjh.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.wjh.common.model.RedisKeyConstant;
import com.wjh.common.model.ResponseConstant;
import com.wjh.utils.redis.RedisCacheUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Component
@RefreshScope
public class AccessTokenFilter extends ZuulFilter {
    @Value("${gateway.excludedUrls}")
    String excludedUrls;

    @Autowired
    RedisCacheUtil redisCacheUtil;

    Logger logger = LogManager.getLogger();

    @Override
    public String filterType() {
        return "pre";
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
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.getResponse().setContentType("text/html;charset=UTF-8");

        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();


        logger.info("发送{}请求到{}", request.getMethod(), request.getRequestURL().toString());
        String url = request.getRequestURL().toString();
        boolean isExcluded = false;
        if (StringUtils.isNotBlank(excludedUrls)) {
            String[] excludedUrlArray = excludedUrls.split(",");
            List<String> excludedUrlList = Arrays.asList(excludedUrlArray);
            for (int i = 0; i < excludedUrlList.size(); i++) {
                if (url.contains(excludedUrlList.get(i))) {
                    isExcluded = true;
                    break;
                }
            }
        }

        boolean pass = false;
        String userId = null;
        if (!isExcluded) {


            /**
             * 获得token
             */
            String token = request.getHeader("token");
            if (StringUtils.isBlank(token)) {
                token = request.getParameter("token");
            }



            if (StringUtils.isNotBlank(token)) {
                int expireSeconds = 60 * 60 * 2;
                /**
                 * 刷新token过期时间
                 */
                redisCacheUtil.refresh(token, expireSeconds);


                //获取用户Id
                userId = (String) redisCacheUtil.getCacheObject(RedisKeyConstant.TOKEN_PREFIX + token);
                if (StringUtils.isNotBlank(userId)) {
                    pass = true;
                    redisCacheUtil.refresh(userId, expireSeconds);
                }
            }
        }

        if (pass || isExcluded) {
            //重新设置装饰类
            if (StringUtils.isNotBlank(userId)) {
                ctx.addZuulRequestHeader("loginUserId", userId);

            }
            ctx.setSendZuulResponse(true);// 对该请求进行路由
            ctx.setResponseStatusCode(200);
            ctx.set("isSuccess", true);// 设值，让下一个Filter看到上一个Filter的状态
            return null;
        } else {
            ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
            ctx.setResponseStatusCode(401);// 返回错误码
            ctx.setResponseBody(ResponseConstant.GATEWAY_TOKEN_INVALID.toString());// 返回错误内容
            ctx.set("isSuccess", false);
            return null;
        }


    }
}
