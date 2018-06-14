package com.kuangchi.sdd.base.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.businessConsole.user.model.User;

public class ConversationFilter implements Filter {

	private static final String[] ignoreUrl = new String[]{"/UserAction/getLoginUser.do","/businessConsole/sessionError.jsp","index.jsp","/businessConsole/pages/error.jsp"};
	public void init(FilterConfig arg0) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		if(isIntercept(req)){
			//ajax请求
            if (req.getHeader("x-requested-with") != null
                    && req.getHeader("x-requested-with")
                    .equalsIgnoreCase("XMLHttpRequest")) {
               // res.setHeader("sessionstatus", "sessionOut");
                res.sendError(403);
                return;
            }else{
                res.sendRedirect(req.getContextPath() + "/businessConsole/sessionError.jsp");
            }
		}else{
			chain.doFilter(request, response);
		}
		
		
		
	}
	
	private boolean isIntercept(HttpServletRequest req){
		String requestUrl = req.getRequestURL().toString();
		String queryString = req.getQueryString();
		if (queryString != null && queryString.length() > 0) {
			requestUrl += "?" + queryString;
		}
		String contextPath = req.getContextPath();
		requestUrl = requestUrl.substring(requestUrl.indexOf(contextPath)
				+ contextPath.length());
		
		if(requestUrl.indexOf(".do") >= 0 || requestUrl.indexOf(".jsp") >=0){
			if(requestUrl.indexOf(ignoreUrl[0]) >=0 ||requestUrl.indexOf(ignoreUrl[1]) >=0 || requestUrl.indexOf(ignoreUrl[2]) >=0||requestUrl.indexOf(ignoreUrl[3]) >=0 ){
				return false;
			}else{
				User loginUser = (User) req.getSession().getAttribute(GlobalConstant.LOGIN_USER);
				if(null == loginUser){
					return true;
				}
			}
		}else{
			return false;
		}
		return false;
	}

	public void destroy() {
	}

}
