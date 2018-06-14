package com.kuangchi.sdd.elevatorConsole.devicePosition.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.baseConsole.devicePosition.model.DeviceDistributionPic;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.devicePosition.service.IDevicePosiService;
import com.kuangchi.sdd.elevatorConsole.devicePosition.model.TKDevicePosi;
import com.kuangchi.sdd.elevatorConsole.devicePosition.service.ITKDevicePosiService;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.ContinueFTP;

/**
 * 设备地图坐标
 * @author minting.he
 *
 */
@Controller("tkDevicePosiAction")
public class DevicePosiAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	@Resource(name="tkDevicePosiService")
	ITKDevicePosiService devicePosiService;

	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * 新增设备坐标
	 * @author minting.he
	 */
	public void addTKDeviPosition(){
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		String x_position = request.getParameter("x_position");
		String y_position = request.getParameter("y_position");
		String pic_id = request.getParameter("pic_id");
		JsonResult result = new JsonResult();
		result.setSuccess(false);
		if(EmptyUtil.atLeastOneIsEmpty(device_num, x_position, y_position, pic_id)){
			result.setMsg("新增设备坐标失败，数据不合法");
		}else {
			String login_user = ((User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER)).getYhMc();
			TKDevicePosi devicePosi = new TKDevicePosi();
			devicePosi.setDevice_num(device_num);
			devicePosi.setX_position(Double.parseDouble(x_position));
			devicePosi.setY_position(Double.parseDouble(y_position));
			devicePosi.setPic_id(pic_id);
			boolean r = devicePosiService.updateTKDeviPosition(devicePosi, login_user);
			if(r){
				result.setSuccess(true);
				result.setMsg("新增设备坐标成功");
			}else {
				result.setMsg("新增设备坐标失败");
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result)); 
	}
	
	/**
	 * 修改设备的地图坐标
	 * @author minting.he
	 */
	public void updateTKDeviPosition(){
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		String x_position = request.getParameter("x_position");
		String y_position = request.getParameter("y_position");
		JsonResult result = new JsonResult();
		result.setSuccess(false);
		if(EmptyUtil.atLeastOneIsEmpty(device_num, x_position, y_position)){
			result.setMsg("修改设备坐标失败，数据不合法");
		}else {
			String login_user = ((User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER)).getYhMc();
			TKDevicePosi devicePosi = new TKDevicePosi();
			devicePosi.setDevice_num(device_num);
			devicePosi.setX_position(Double.parseDouble(x_position));
			devicePosi.setY_position(Double.parseDouble(y_position));
			boolean r = devicePosiService.updateTKDeviPosition(devicePosi, login_user);
			if(r){
				result.setSuccess(true);
				result.setMsg("修改设备坐标成功");
			}else {
				result.setMsg("修改设备坐标失败");
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result)); 
	}
	
	/**
	 * 删除设备地图坐标
	 * @author minting.he
	 */
	public void deleteTKDeviPosition(){
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		JsonResult result = new JsonResult();
		result.setSuccess(false);
		if(EmptyUtil.atLeastOneIsEmpty(device_num)){
			result.setMsg("删除设备坐标失败，数据不合法");
		}else {
			String login_user = ((User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER)).getYhMc();
			boolean r = devicePosiService.deleteTKDeviPosition(device_num, login_user);
			if(r){
				result.setSuccess(true);
				result.setMsg("删除设备坐标成功");
			}else {
				result.setMsg("删除设备坐标失败");
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result)); 
	}
	
	/**
	 * 获取地图图片和设备的信息
	 * @author minting.he
	 */
	public void getPicInfo(){
		HttpServletRequest request = getHttpServletRequest();
		String flag = request.getParameter("flag");
		String group_num = request.getParameter("group_num");
		if(EmptyUtil.atLeastOneIsEmpty(group_num)){
			group_num="";
		}
		List<DeviceDistributionPic> pics = devicePosiService.getPicInfo(flag, group_num);
		printHttpServletResponse(GsonUtil.toJson(pics)); 
	}
	

}
