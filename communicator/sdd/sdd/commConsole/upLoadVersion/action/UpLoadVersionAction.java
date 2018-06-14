package com.kuangchi.sdd.commConsole.upLoadVersion.action;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.commConsole.upLoadVersion.service.IUpLoadVersionService;

@Controller("upLoadVersionAction")
public class UpLoadVersionAction extends BaseActionSupport {
	private static final long serialVersionUID = 293788929553302791L;
	@Resource(name = "upLoadVersionServiceImpl")
	private IUpLoadVersionService upLoadVersionService;

	@Override
	public Object getModel() {

		return null;
	}

	/**
	 * 下发程序（握手）
	 * 
	 * @author xuewen.deng
	 */
	public void downSoftware() {
		String mac = getHttpServletRequest().getParameter("mac");// 16
		String deviceType = getHttpServletRequest().getParameter("device_type");

		String result;
		try {
			result = upLoadVersionService.downSoftware(mac, deviceType);
			if (result != null && !("null".equals(result))) {
				printHttpServletResponse(new Gson().toJson(0));
			} else {
				printHttpServletResponse(new Gson().toJson(1));
			}
		} catch (Exception e) {
			printHttpServletResponse(new Gson().toJson(1));
			e.printStackTrace();
		}

	}

	/**
	 * 下发程序包
	 * 
	 * @author xuewen.deng
	 * @return
	 */
	public void packageDownSoftware() {
		String mac = getHttpServletRequest().getParameter("mac");// 16
		String deviceType = getHttpServletRequest().getParameter("device_type");
		String packageNum = getHttpServletRequest().getParameter("packageNum");
		String isoString = getHttpServletRequest().getParameter("isoString");

		int result = 1;
		try {

			result = upLoadVersionService.packageDownSoftware(mac, deviceType,
					packageNum, isoString);
		} catch (Exception e) {
			result = 1;
			e.printStackTrace();
		}
		printHttpServletResponse(new Gson().toJson(result));
	}

	/**
	 * 尾字节
	 * 
	 * @author xuewen.deng
	 */
	public void packageStern() {
		String mac = getHttpServletRequest().getParameter("mac");// 16
		String deviceType = getHttpServletRequest().getParameter("device_type");
		String residueLength = getHttpServletRequest().getParameter(
				"residueLength");
		String packageNum = getHttpServletRequest().getParameter("packageNum");
		String isoString = getHttpServletRequest().getParameter("isoString");

		int result = 1;
		try {
			result = upLoadVersionService.packageStern(mac, deviceType,
					residueLength, packageNum, isoString);
		} catch (UnsupportedEncodingException e) {
			result = 1;
			e.printStackTrace();
		} catch (Exception e) {
			result = 1;
			e.printStackTrace();
		}
		printHttpServletResponse(new Gson().toJson(result));
	}

	/**
	 * 下发程序结束
	 * 
	 * @throws Exception
	 */
	public void overSoftware() {
		String mac = getHttpServletRequest().getParameter("mac");// 16
		String deviceType = getHttpServletRequest().getParameter("device_type");
		int result = 1;
		try {
			result = upLoadVersionService.overSoftware(mac, deviceType);
		} catch (Exception e) {
			result = 1;
			e.printStackTrace();
		}
		printHttpServletResponse(new Gson().toJson(result));
	}
}
