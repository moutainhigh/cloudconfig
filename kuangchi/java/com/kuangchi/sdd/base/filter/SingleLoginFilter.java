package com.kuangchi.sdd.base.filter;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.singleLogin.dao.SingleLoginDao;
import com.kuangchi.sdd.singleLogin.model.LoginSerialNum;

@Service("SingleLoginFilter")
public class SingleLoginFilter implements Filter{
    @Resource(name="singleLoginDaoImpl")
	SingleLoginDao singleLoginDao;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest) request;
		HttpServletResponse res=(HttpServletResponse)response;
		HttpSession session=req.getSession();
		StringBuffer url=req.getRequestURL();
		if(url.toString().contains("csLogin.do")&&req.getParameter("token")!=null&&req.getParameter("yhMc")!=null){//CS端登陆URL
					String token=req.getParameter("token");
					Map loginSerialNum=singleLoginDao.getLoginSerialNum(token);
					if(loginSerialNum!=null&&((String)loginSerialNum.get("LoginSN")).equals(token)){//口令合法,存入回话，然后放行
							session.setAttribute("token",req.getParameter("token"));
							session.setAttribute("yhMc",req.getParameter("yhMc"));
							chain.doFilter(request, response);
					}else{//口令非法，重定向
						res.sendRedirect(req.getContextPath() + "/businessConsole/pages/error.jsp");
					}
		}else if((url.toString().contains("portal")||url.toString().contains("interface")||url.toString().contains("error")||url.toString().contains("findpwd/changeDialog")
					||url.toString().contains("portal/pwd"))){//门户登陆URL
			chain.doFilter(request, response);
		}else if (url.toString().contains("UserAction/getLoginUser.do")){//BS登陆
						if(session.getAttribute("bFlag")==null){
							session.setAttribute("bFlag", "b");
						}
						chain.doFilter(request, response);
		}else if("b".equals(session.getAttribute("bFlag"))){//BS 登陆过，放行
					chain.doFilter(request, response);
		}else if(session.getAttribute("token")!=null){//CS 登陆过，放行
			String token=(String) session.getAttribute("token");
			Map loginSerialNum=singleLoginDao.getLoginSerialNum(token);
			if(loginSerialNum!=null&&((String)loginSerialNum.get("LoginSN")).equals(token)){//口令合法,存入回话，然后放行
					chain.doFilter(request, response);
			}else{//口令非法，重定向
				res.sendRedirect(req.getContextPath() + "/businessConsole/pages/error.jsp");
			}
		}else{
				res.sendRedirect(req.getContextPath() +  "/businessConsole/pages/error.jsp");
		}
	}

	@Override
	public void destroy() {
		
		
	}

}

