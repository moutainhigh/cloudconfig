package com.kuangchi.sdd.consumeConsole.devicePosition.action;

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
import com.kuangchi.sdd.consumeConsole.devicePosition.model.DevicePosi;
import com.kuangchi.sdd.consumeConsole.devicePosition.service.IDevicePosiService;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.ContinueFTP;

/**
 * 设备地图坐标
 * @author minting.he
 *
 */
@Controller("devicePosiAction")
public class DevicePosiAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	@Resource(name="devicePosiService")
	IDevicePosiService devicePosiService;

	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * 新增设备坐标
	 * @author minting.he
	 */
	public void addDevicePosition(){
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
			DevicePosi devicePosi = new DevicePosi();
			devicePosi.setDevice_num(device_num);
			devicePosi.setX_position(Double.parseDouble(x_position));
			devicePosi.setY_position(Double.parseDouble(y_position));
			devicePosi.setPic_id(pic_id);
			boolean r = devicePosiService.updateDevicePosition(devicePosi, login_user);
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
	public void updateDevicePosition(){
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
			DevicePosi devicePosi = new DevicePosi();
			devicePosi.setDevice_num(device_num);
			devicePosi.setX_position(Double.parseDouble(x_position));
			devicePosi.setY_position(Double.parseDouble(y_position));
			boolean r = devicePosiService.updateDevicePosition(devicePosi, login_user);
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
	public void deleteDevicePosition(){
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		JsonResult result = new JsonResult();
		result.setSuccess(false);
		if(EmptyUtil.atLeastOneIsEmpty(device_num)){
			result.setMsg("删除设备坐标失败，数据不合法");
		}else {
			String login_user = ((User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER)).getYhMc();
			boolean r = devicePosiService.deleteDevicePosition(device_num, login_user);
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
	
	/**
	 * 获取地图图片
	 * @author minting.he
	 */
	public void getImg(){
		HttpServletRequest request=getHttpServletRequest();	
    	HttpServletResponse response=getHttpServletResponse();
    	String imgPath = "";
    	try {
    		String a = request.getParameter("pic_path");
			imgPath = new String(request.getParameter("pic_path").getBytes("iso-8859-1"), "UTF-8");
			imgPath = a;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
    	ContinueFTP continueFTP=new ContinueFTP();
    	java.io.InputStream in=null;
    	OutputStream out=null;
    	try {
     		String ftpPropertiesPath=request.getSession().getServletContext().getRealPath("/"+"WEB-INF"+File.separator+"classes"+File.separator+"conf"+File.separator+"properties"+File.separator+"ftp.properties");
     		com.kuangchi.sdd.util.file.FTP ftp=com.kuangchi.sdd.util.file.FTPUtil.getFtp(ftpPropertiesPath);		
		    continueFTP.connect(ftp.getHost(), ftp.getPort(), ftp.getUserName(), ftp.getPassword());
		    if (null!=imgPath&&!imgPath.trim().equals("")&&!"undefined".equals(imgPath)) {
		    	in=continueFTP.download(imgPath);
		    }else{
		    	//如果没有上传头像，则给一个默认头像
		    	String defaultImg=request.getSession().getServletContext().getRealPath("/"+"businessConsole"+File.separator+"images"+File.separator+"defaultImg.jpg");
		    	File file=new File(defaultImg)  ;
		    	in=new FileInputStream(file);
		    }
			out=response.getOutputStream();
			byte[] buffer=new byte[1024];
			while (in.read(buffer)!=-1) {
			    out.write(buffer);			
			}
			out.flush();
			continueFTP.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (null!=in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
			if (null!=out) {
			    try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		}
	}
	

}
