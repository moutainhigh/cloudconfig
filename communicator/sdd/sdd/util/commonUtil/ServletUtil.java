package com.kuangchi.sdd.util.commonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * servlet 工具类
 * @author ccdt
 *
 */
public class ServletUtil {

	/**
	 * 获取session中的值
	 * @param request 
	 * @param attr session对象名称
	 * @return
	 */
	public static  Object getSessionAttr(HttpServletRequest request,String attr){
		HttpSession session = request.getSession();
		return session.getAttribute(attr);
	} 
	
}
