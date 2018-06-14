package com.kuangchi.sdd.commConsole.time.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.commConsole.time.model.ResultMsg;
import com.kuangchi.sdd.commConsole.time.model.Time;
import com.kuangchi.sdd.commConsole.time.service.ITimeService;

@Controller("timeAction")
public class TimeAction extends BaseActionSupport {

	/**
     * 
     */
	private static final long serialVersionUID = -6953122663918999832L;

	@Resource(name = "timeServiceImpl")
	private ITimeService timeService;

	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-7-14 下午2:54:19
	 * @功能描述: 获取设备校时时间
	 * @参数描述:
	 */
	public void getTime() {
		String mac = getHttpServletRequest().getParameter("mac");
		String sign = getHttpServletRequest().getParameter("device_type");// 设备类型
		Time time = timeService.getDeviceTime(mac, sign);
		ResultMsg rm = new ResultMsg();
		if (time==null||"null".equals(time)) {
			rm.setResult_code("1");
			rm.setResult_msg("获取时间失败");
		} else {
			rm.setTime(time);
			rm.setResult_code("0");
			rm.setResult_msg("获取时间成功");
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}

	public void setTime() {
		String mac = getHttpServletRequest().getParameter("mac");
		String date = getHttpServletRequest().getParameter("date");
		int i = timeService.setDeviceTime(mac, date);
		ResultMsg rm = new ResultMsg();
		if (i == 1) {
			rm.setResult_code("1");
			rm.setResult_msg("设置时间失败");
		} else {
			rm.setResult_code("0");
			rm.setResult_msg("设置时间成功");
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}

	public void setTime2() {
		String mac = getHttpServletRequest().getParameter("mac");
		String device_type = getHttpServletRequest()
				.getParameter("device_type");
		int i = timeService.setDeviceTime2(mac, device_type);
		ResultMsg result = new ResultMsg();
		if (i == 1) {
			result.setResult_code("1");
			result.setResult_msg("校时失败");
		} else {
			result.setResult_code("0");
			result.setResult_msg("校时成功");
		}
		printHttpServletResponse(new Gson().toJson(result));
	}

	public void restartDevice() {
		String mac = getHttpServletRequest().getParameter("mac");
		String device_type = getHttpServletRequest()
				.getParameter("device_type");
		int i = timeService.restartDevice(mac, device_type);
		ResultMsg result = new ResultMsg();
		if (i == 1) {
			result.setResult_code("1");
			result.setResult_msg("复位设备失败");
		} else {
			result.setResult_code("0");
			result.setResult_msg("复位设备成功");
		}
		printHttpServletResponse(new Gson().toJson(result));
	}
}
