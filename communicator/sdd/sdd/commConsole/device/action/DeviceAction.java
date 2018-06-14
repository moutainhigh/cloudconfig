package com.kuangchi.sdd.commConsole.device.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.device.model.ResultMsg;
import com.kuangchi.sdd.commConsole.device.service.IDeviceService;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;

/**
 * 设备
 *
 */
@Controller("deviceAction")
public class DeviceAction extends BaseActionSupport {

	@Resource(name = "deviceServiceImpl")
	private IDeviceService deviceService;
	
	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * 清空硬件设备数据
	 * @author minting.he
	 */
	public void clearDeviceData(){
		HttpServletRequest request = getHttpServletRequest();
		String sign = request.getParameter("sign");
		String mac = request.getParameter("mac");
		String dataType = request.getParameter("dataType");
		ResultMsg rm = new ResultMsg();
		if(EmptyUtil.atLeastOneIsEmpty(sign, mac, dataType)){
			rm.setResult_code("1");
			rm.setResult_msg("清空失败，数据不合法");
		}else {
			boolean result = deviceService.clearData(sign, mac, dataType);
			if(result){
				rm.setResult_code("0");
				rm.setResult_msg("清空成功");
			}else {
				rm.setResult_code("1");
				rm.setResult_msg("清空失败");
			}
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}
	
	/**
	 * 远程开门
	 * @author minting.he
	 */
	public void remoteOpenDoor(){
		HttpServletRequest request = getHttpServletRequest();
		String sign = request.getParameter("sign");
		String mac = request.getParameter("mac");
		String door_num = request.getParameter("door_num");
		ResultMsg rm = new ResultMsg();
		if(EmptyUtil.atLeastOneIsEmpty(sign, mac, door_num)){
			rm.setResult_code("1");
			rm.setResult_msg("开门失败，数据不合法");
		}else {
			boolean result = deviceService.remoteOpenDoor(sign, mac, door_num);
			if(result){
				rm.setResult_code("0");
				rm.setResult_msg("开门成功");
			}else {
				rm.setResult_code("1");
				rm.setResult_msg("开门失败");
			}
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}
}
