package com.wjh.gateway.filter;


import com.netflix.discovery.converters.Auto;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.wjh.common.model.RedisKeyConstant;
import com.wjh.common.model.ResponseConstant;
import com.wjh.common.model.ResponseModel;
import com.wjh.gateway.service.MenuOperateService;
import com.wjh.gateway.service.UserOperateService;
import com.wjh.menuoperateservicemodel.model.OperateVo;
import com.wjh.utils.redis.RedisCacheUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@RefreshScope
public class OperatePermissionFilter extends ZuulFilter {


    Logger logger = LogManager.getLogger();

    @Autowired
    UserOperateService userOperateService;

    @Autowired
    MenuOperateService menuOperateService;

    @Autowired
    RedisCacheUtil redisCacheUtil;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
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

//     header中的属性是不区分大小写的
        String loginUserId = ctx.getZuulRequestHeaders().get("loginUserId".toLowerCase());
        logger.info("...............登录用户Id:{}", loginUserId);

        boolean shouldVerify = false;//该权限是否被控制

        boolean hasPermission = false;//是否拥有该权限


        String requestUri = request.getRequestURI().toString();
        logger.info(".............请求URL :{}", requestUri);

        if (StringUtils.isNotBlank(loginUserId)) {


            //先从缓存中去取，取不到再调用接口
            List<OperateVo> allList = (List<OperateVo>) redisCacheUtil.getCacheObject(RedisKeyConstant.ALL_OPERATE);
            if (allList == null) {
                //获取所有需要控制的权限
                ResponseModel<List<OperateVo>> allResponseModel = menuOperateService.listAll();
                allList = (List<OperateVo>) allResponseModel.getResModel();
            }


            for (int i = 0; i < allList.size(); i++) {
                OperateVo ov=allList.get(i);
                logger.info(ov.getServiceName()+"/"+ov.getUrl() + "/");
                if ((requestUri + "/").indexOf(ov.getServiceName()+ov.getUrl() + "/") >= 0) {
                    shouldVerify = true;
                    break;
                }
            }


            if (shouldVerify) {

                //先从缓存中取一次，如果取不到再调用接口
                List<OperateVo> operateVoList= (List<OperateVo>) redisCacheUtil.getCacheMapVaue(RedisKeyConstant.USER_OPERATE_TABLE,loginUserId+"");
                if (operateVoList==null){
                    //获取某个人拥有的权限
                    ResponseModel<List<OperateVo>> operateVoResponseModel = userOperateService.listAllByUserId(Long.valueOf(loginUserId));
                    operateVoList = (List<OperateVo>) operateVoResponseModel.getResModel();
                }

                for (int i = 0; i < operateVoList.size(); i++) {
                    OperateVo ov=operateVoList.get(i);
                    if ((requestUri + "/").indexOf(ov.getServiceName()+ov.getUrl() + "/") >= 0) {
                        hasPermission = true;
                        break;
                    }
                }
            }

        }


        if (shouldVerify) {
            if (hasPermission) {
                ctx.setSendZuulResponse(true);// 对该请求进行路由
                ctx.setResponseStatusCode(200);
                ctx.set("isSuccess", true);// 设值，让下一个Filter看到上一个Filter的状态
            } else {
                ctx.setSendZuulResponse(false);// 过滤该请求，不对其进行路由
                ctx.setResponseStatusCode(401);// 返回错误码
                ctx.setResponseBody(ResponseConstant.GATEWAY_HAS_NO_PERMISSION.toString());// 返回错误内容
                ctx.set("isSuccess", false);
            }
        } else {
            ctx.setSendZuulResponse(true);// 对该请求进行路由
            ctx.setResponseStatusCode(200);
            ctx.set("isSuccess", true);// 设值，让下一个Filter看到上一个Filter的状态
        }
        return null;


    }
}
