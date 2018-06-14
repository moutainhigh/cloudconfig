package com.kuangchi.sdd.businessConsole.common.action;

import java.util.HashSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Controller;

/**
 * SessionListener
 * @author minting.he
 *
 */
@Controller("sysSessionListener")
public class SysSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
        ServletContext application = session.getServletContext();
        // 在application范围由一个HashSet集保存所有的session
        HashSet<HttpSession> sessions = (HashSet<HttpSession>) application.getAttribute("sessions");
        if (sessions == null) {
               sessions = new HashSet<HttpSession>();
        }
  //    sessions.add(session);
        application.setAttribute("sessions", sessions);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
        ServletContext application = session.getServletContext();
        HashSet<HttpSession> sessions = (HashSet<HttpSession>) application.getAttribute("sessions");
        sessions.remove(session);
        application.setAttribute("sessions", sessions);
	}

}
