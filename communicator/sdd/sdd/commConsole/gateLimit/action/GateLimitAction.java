package com.kuangchi.sdd.commConsole.gateLimit.action;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.comm.util.DateUtil;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.gateLimit.model.ResultMsg;
import com.kuangchi.sdd.commConsole.gateLimit.service.IGetGateLimitService;

@Controller("gateLimitAction")
public class GateLimitAction extends BaseActionSupport {
	private static final long serialVersionUID = 293788929553302791L;
	@Resource(name = "getGateLimitServiceImpl")
	private IGetGateLimitService getGateLimitService;

	@Override
	public Object getModel() {

		return null;
	}

	/**
	 * 把字符串 26转成1a的16进制 by gengji.yang
	 * 
	 * @param s
	 * @return
	 */
	private static String toHexStr(String s) {
		Integer a = Integer.parseInt(s);
		String c = a.toHexString(a);
		return c;
	}

	/**
	 * 设置门禁权限 2016.6.17
	 */
	public void setGateLimit() {
		// 获取参数
		String mac = getHttpServletRequest().getParameter("mac");// 16
		String deviceType = getHttpServletRequest().getParameter("device_type");
		String gateId = getHttpServletRequest().getParameter("gateId");// 3
		String cardId = getHttpServletRequest().getParameter("cardId");// 282f
		String start = getHttpServletRequest().getParameter("start");// 2016-05-04
																		// 15:12
		String end = getHttpServletRequest().getParameter("end");// 2016-05-12
																	// 15:12
		String group = getHttpServletRequest().getParameter("group");// f
		start = start + ":00";
		end = end + ":00";
		ResultMsg rm = new ResultMsg();
		Integer groupNum;
		if ("null".equals(group) || group == null) {// 判断前台传过来的时段组是否为空
			groupNum = null;
		} else {
			groupNum = Util.getIntHex(group);
		}
		Integer i;
		try {
			i = getGateLimitService.setGateLimit(Util.getIntHex(mac),
					Util.getIntHex(gateId), Util.getIntHex(deviceType), cardId,
					DateUtil.getDateTime(start), DateUtil.getDateTime(end),
					groupNum);
			if (i == 0) {
				rm.setResult_code("0");
				rm.setResult_msg("设置门禁权限成功");
			} else {
				rm.setResult_code("1");
				rm.setResult_msg("设置门禁权限失败");
			}
		} catch (Exception e) {
			rm.setResult_code("1");
			rm.setResult_msg("设置门禁权限失败");
			e.printStackTrace();
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}

	/**
	 * 删除门禁权限 2016.6.17
	 */
	public void delGateLimit() {
		String mac = getHttpServletRequest().getParameter("mac");
		String deviceType = getHttpServletRequest().getParameter("device_type");
		String gateId = getHttpServletRequest().getParameter("gateId");
		String cardId = getHttpServletRequest().getParameter("cardId");

		ResultMsg rm = new ResultMsg();
		int i;
		try {
			i = getGateLimitService.delGateLimit(Util.getIntHex(mac),
					Util.getIntHex(gateId), Util.getIntHex(deviceType), cardId);// 删除门禁权限

			if (i == 0) {
				rm.setResult_code("0");
				rm.setResult_msg("删除门禁权限成功");
			} else {
				rm.setResult_code("1");
				rm.setResult_msg("删除门禁权限失败");
			}
		} catch (Exception e) {
			rm.setResult_code("1");
			rm.setResult_msg("删除门禁权限失败");
			e.printStackTrace();
		}

		printHttpServletResponse(new Gson().toJson(rm));

	}
	
	/**
	 * 读取权限
	 * by gengji.yang
	 */
	public void getGateLimit(){
		HttpServletRequest request=getHttpServletRequest();
		String mac=request.getParameter("mac");
		String cardId=request.getParameter("cardId");
		String sign=request.getParameter("sign");
		
		Integer macInt=Integer.parseInt(mac,16);
		String macHexStr=Integer.toHexString(macInt);
		
		Integer cardIdInt=Integer.parseInt(cardId);
		String cardIdHexStr=Integer.toHexString(cardIdInt);
		
		try {
			Map map=getGateLimitService.readGateLimit(Integer.parseInt(macHexStr, 16),Integer.parseInt(cardIdHexStr, 16), Util.getIntHex(sign));
			printHttpServletResponse(new Gson().toJson(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

}
