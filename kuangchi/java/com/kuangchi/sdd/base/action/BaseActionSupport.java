package com.kuangchi.sdd.base.action;

import java.io.InputStream;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.HttpUtil;
import com.kuangchi.sdd.util.commonUtil.LocalesUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public abstract class BaseActionSupport extends ActionSupport
  implements ModelDriven<Object>
{
  public abstract Object getModel();

  public static InputStream getResourceAsStream(String path)
  {
    return HttpUtil.getResourceAsStream(path);
  }

  public HttpServletRequest getHttpServletRequest()
  {
    return HttpUtil.getHttpServletRequest();
  }

  public HttpServletResponse getHttpServletResponse()
  {
    return HttpUtil.getHttpServletResponse();
  }
  
  public HttpSession getHttpSession()
  {
    return getHttpServletRequest().getSession();
  }

  public String getActionName()
  {
    return HttpUtil.getActionName();
  }

  public String getLocalesCode()
  {
    return LocalesUtil.getLocalesCode();
  }

  public String getSysDate()
  {
    return DateUtil.getSysDate();
  }

  public Timestamp getSysTimestamp()
  {
    return DateUtil.getSysTimestamp();
  }

  public Timestamp getTimestamp(String strDate)
  {
    return DateUtil.getTimestamp(strDate);
  }

  public void printHttpServletResponse(Object obj)
  {
    HttpUtil.printHttpServletResponse(obj);
  }
}