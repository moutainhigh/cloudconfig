package com.kuangchi.sdd.util.commonUtil;

import com.opensymphony.xwork2.ActionContext;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;

public class HttpUtil {

    private static SimpleDateFormat httpDateFormat = new SimpleDateFormat("EEE, dd MMM yyyyy HH:mm:ss z", Locale.US);

    public static InputStream getResourceAsStream(String path) {
        return ServletActionContext.getServletContext().getResourceAsStream(path);
    }

    public static String formatHttpDate(Date date) {
        synchronized (httpDateFormat) {
            return httpDateFormat.format(date);
        }
    }

    public static String convertEncode(String str) {
        String encodeValue = null;
        try {
            encodeValue = URLEncoder.encode(str, "UTF-8");
            encodeValue = StringUtil.replace(encodeValue, "+", "%20");
            if (encodeValue.length() > 150) {
                encodeValue = new String(str.getBytes("UTF-8"), "ISO8859-1");
                encodeValue = StringUtil.replace(encodeValue, "+", "%20");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeValue;
    }

    public static String convertEncode(String str, String charset) {
        String encodeValue = null;
        try {
            encodeValue = URLEncoder.encode(str, "UTF-8");
            encodeValue = StringUtil.replace(encodeValue, "+", "%20");
            if (encodeValue.length() > 150) {
                encodeValue = new String(str.getBytes(charset), "ISO8859-1");
                encodeValue = StringUtil.replace(encodeValue, "+", "%20");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeValue;
    }

    public static ActionContext getActionContext() {
        return ServletActionContext.getContext();
    }

    public static ServletContext getServletContext() {
        return ServletActionContext.getServletContext();
    }

    public static HttpServletRequest getHttpServletRequest() {
    	/*ServletActionContext.getContext().getValueStack();
    	ActionContext.getContext().getValueStack();*/
        return ServletActionContext.getRequest();
    }

    public static HttpServletResponse getHttpServletResponse() {
        return ServletActionContext.getResponse();
    }

    public static HttpSession getHttpSession() {
        return ServletActionContext.getRequest().getSession(false);
    }

    public static HttpSession getHttpSession(boolean flag) {
        return ServletActionContext.getRequest().getSession(flag);
    }

    public static Map getSessionMap() {
        return getActionContext().getSession();
    }

    public static Object getSession(Object key) {
        Map map = getActionContext().getSession();
        if (map == null)
            return null;
        return map.get(key);
    }

    public static void setSession(Object key, Object value) {
        Map map = getActionContext().getSession();
        map.put(key, value);
    }

    public static void removeSession(Object key) {
        Map map = getActionContext().getSession();
        map.remove(key);
    }

    public static void removeAllSession() {
        Map map = getActionContext().getSession();
        map.clear();
    }

    public static String getActionName() {
        return ServletActionContext.getRequest().getRequestURI();
    }

    public static void setAttachHeader(String str) {
        ServletActionContext.getResponse().setHeader("Content-Disposition", "attachment; filename=" + convertEncode(str));
    }

    public static void setInlineHeader(String str) {
        ServletActionContext.getResponse().setHeader("Content-Disposition", "inline; filename=" + convertEncode(str));
    }

    public static void printHttpServletResponse(Object obj) {
        PrintWriter prw = null;
        try {
        	HttpServletResponse response = ServletActionContext.getResponse(); 
        	response.setContentType("text/html;charset=utf-8");
        	
            prw = response.getWriter();
            prw.print(obj);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            prw.close();
        }
    }

    public static String encodeURL(String url, String encoding) {
        try {
            return URLEncoder.encode(url, encoding);
        } catch (Exception exception) {
            return url;
        }
    }

    public static String encodePathInfo(String pathinfo) {
        String s = encodeURL(pathinfo, "UTF-8");
        char[] chars = s.toCharArray();
        StringBuffer sb = new StringBuffer();
        char c = '\0';
        for (int i = 0; i < chars.length; ++i) {
            c = chars[i];
            if (c == '+')
                sb.append("%20");
            else
                sb.append(c);
        }

        return sb.toString();
    }

    public static String decodeURL(String url, String encoding) {
        try {
            return URLDecoder.decode(url, encoding);
        } catch (Exception exception) {
            return url;
        }
    }

    static {
        httpDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
}