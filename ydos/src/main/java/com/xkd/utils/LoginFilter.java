package com.xkd.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xkd.service.ObjectNewsService;
import com.xkd.service.YDrepaireService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.IfFunc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xkd.model.Operate;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.SysOperateService;
import com.xkd.service.UserService;

public class LoginFilter extends HandlerInterceptorAdapter {

	@Autowired
	private SysOperateService sysOperateService;

	@Autowired
	RedisCacheUtil redisCacheUtil;

	
	@Autowired
	UserService userService;

	Logger log = Logger.getLogger(LoginFilter.class);

    private static  Map<String,String> pvProperties=null;

	/**
	 * 页面pv  URL与模块名映射关系
	 */
     static {
		 pvProperties=PropertiesUtil.loadProperties("pv.properties");
	 }




	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String requestIP=request.getRemoteHost();
		StringBuffer url = request.getRequestURL();
		String url_new = url.toString();
		request.getSession().setAttribute("url", url_new);
		String uri=request.getRequestURI().replace("/ydos/","");
		String modelName=pvProperties.get(uri);
		if(StringUtils.isBlank(modelName)){
			modelName="---";
		}
		String pcuserId = null;

		String token = request.getParameter("token");
		String appFlag=  request.getParameter("appFlag");    //小程序为"xcx"
		String youdianapp=  request.getParameter("youdianapp");    //有点app的标志

		// token为555时这个时候就通过
		if ("555".equals(request.getParameter("token"))) {
			pcuserId = "555";
			//redisCacheUtil.setCacheObject("555","814");
			redisCacheUtil.setCacheObject("555","81c69c87-56fd-4f2b-bf11-6e4360e987f5");
			request.getSession().setAttribute("555", "81c69c87-56fd-4f2b-bf11-6e4360e987f5");
			request.getSession().setAttribute("getOpenId555", "o5mkjwG2OTNo3dBnOPcIZqZqv4KM");
			request.setAttribute("loginUserId", "81c69c87-56fd-4f2b-bf11-6e4360e987f5");

		} else if (StringUtils.isNotBlank(token)) {

			pcuserId = (String) redisCacheUtil.getCacheObject(token);
			//pcuserId = (String) request.getSession().getAttribute(token);
			// 这里把当前登录用户的Id存进去方便在Controller层使用
			if(StringUtils.isNotBlank(pcuserId)){
				request.setAttribute("loginUserId", pcuserId);
				redisCacheUtil.setCacheObject("loginUserId", pcuserId);
			}

		}

		/**
		 * swagger 在线文档直接放过、与旧系统数据同步接口要放过
		 */
		if (url_new.contains("oldApi") || url_new.contains("swagger")||url_new.contains("api-docs")){
			return true;
		}

		if ((url_new.indexOf("/user/") > 0
				|| url_new.indexOf("/kaquan/") > 0
				|| url_new.indexOf("/utils/sendVerifyCodeByMobile") > 0 || url_new.indexOf("/file/uploadPicture") > 0
				|| url_new.indexOf("/company/selectCompanyByNameMH") > 0
				|| url_new.indexOf("/meetingSign/getSignCompany") > 0
				|| url_new.indexOf("/meetingSign/getAdviserList") > 0 || url_new.indexOf("/ydos/file.html") > 0
				|| url_new.indexOf("/userGift/saveUserGift") > 0  || url_new.indexOf("/userGift/selectUserGift") > 0
				|| url_new.indexOf("/userGift/selectUserGiftsByActivityId") > 0 || url_new.indexOf("/userGift/updateUserGiftById") > 0)
				|| url_new.indexOf("/ticket/savePay") > 0
				|| url_new.indexOf("/weixinCorp/messageReceive") > 0) {
			//打印日志，便于数据分析：2017-09-25 15:02:06 [INFO ] type:kedouCRM userid: 555 trans:company method:selectAllDetailInfoByCompanyId date:2017-09-25 15:02:06
			String[] strs = url_new.split("/");
			log.info("modelName@"+modelName+"+requestIP:"+requestIP+" type:kedouCRM userid: " + pcuserId + " trans:" + strs[strs.length-2] + " method:" + strs[strs.length-1] + " date:" + DateUtils.currtime());

			//获取配置信息是否要写kafka日志
			String writeKafka=PropertiesUtil.KAFKA_WRITE;
			if ("true".equals(writeKafka)) {
				//发送日志到Kafka日志收集队列
				KafkaUtil.sendMessage("crmLog", "modelName@"+modelName+"+requestIP@"+requestIP+"+type@kedouCRM+userid@" + pcuserId + "+trans@" + strs[strs.length-2] + "+method@" + strs[strs.length-1] + "+date@" + DateUtils.currtime());
			}
			log.info("----------登录拦截-------------");

			return true;
		}




		if (pcuserId != null){

			/*MapCachePcuserId = redisCacheUtil.getCacheObject(token) == null?null:redisCacheUtil.getCacheObject(token).toString();
			//刷新时间
			if(MapCachePcuserId != null){
				redisCacheUtil.setCacheObject(token,MapCachePcuserId);
			}else{
				return false;
			}*/

			//打印日志，便于数据分析：2017-09-25 15:02:06 [INFO ] type:kedouCRM userid: 555 trans:company method:selectAllDetailInfoByCompanyId date:2017-09-25 15:02:06
			String[] strs = url_new.split("/");
			log.info("2modelName@"+modelName+"+requestIP:"+requestIP+" type:kedouCRM userid: " + pcuserId + " trans:" + strs[strs.length-2] + " method:" + strs[strs.length-1] + " date:" + DateUtils.currtime());
			//获取配置信息是否要写kafka日志
			String writeKafka=PropertiesUtil.KAFKA_WRITE;
			if ("true".equals(writeKafka)) {
				//发送日志到Kafka日志收集队列
				KafkaUtil.sendMessage("crmLog", "modelName@"+modelName+"+requestIP@"+requestIP+"+type@kedouCRM+userid@" + pcuserId + "+trans@" + strs[strs.length-2] + "+method@" + strs[strs.length-1] + "+date@" + DateUtils.currtime());
			}
  			return true;
		}

		/*
				 *app端token失效的话就返回s9999，pc端一直在请求阶段，如果网络延迟也会是一直请求状态，所以pc端后面优化
				 */
		if(StringUtils.isBlank(pcuserId)&& StringUtils.isNotBlank(youdianapp) && "youdianapp".equals(youdianapp)){
			ResponseDbCenter dbCenter = new ResponseDbCenter("S9999","app端token失效");
			returnMessage(response, dbCenter,"0");
		}

		log.info("3type:errorRequest userid: " + pcuserId + " trans:" + url_new + " date:" + DateUtils.currtime());

		return false;

	}

	
	
	   
	

	
		
		
		
	
	
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
 	}
	
	private void returnErrorMessage(HttpServletResponse response,String url) throws IOException {
        ResponseDbCenter rst = ResponseConstants.NOT_PERMITTED;
        rst.setResExtra(url);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(JSON.toJSON(rst));
        out.flush();
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
	
	
}
