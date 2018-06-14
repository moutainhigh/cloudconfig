package com.kuangchi.sdd.baseConsole.devicePosition.action;

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
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.devicePosition.model.DeviceDistributionPic;
import com.kuangchi.sdd.baseConsole.devicePosition.service.DevicePositionService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.devicePosition.service.IDevicePosiService;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.file.ContinueFTP;


/**
 * @创建人　: 高育漫
 * @创建时间: 2016-6-13 下午4:04:26
 * @功能描述: 设备坐标信息 - Action类
 */
@Controller("devicePositionAction")
public class DevicePositionAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;

	@Resource(name="devicePositionService")
	DevicePositionService devicePositionService;
	
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-13 下午3:43:57
	 * @功能描述: 新增设备坐标
	 * @参数描述:
	 */
	public void addPosition(){
		HttpServletRequest request = getHttpServletRequest();
		
		String device_num = request.getParameter("device_num");
		String x_position = request.getParameter("x_position");
		String y_position = request.getParameter("y_position");
		String pic_id = request.getParameter("pic_id");
		String login_user = ((User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER)).getYhMc();
		
		DeviceInfo device = new DeviceInfo();
		device.setDevice_num(device_num);
		device.setX_position(Double.parseDouble(x_position));
		device.setY_position(Double.parseDouble(y_position));
		device.setDistribution_pic_id(pic_id);
		devicePositionService.setDevicePosition(device, login_user);
		
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		result.setMsg("新增设备坐标成功");
	
		printHttpServletResponse(GsonUtil.toJson(result)); 
	}
	
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-13 下午3:44:19
	 * @功能描述: 修改设备坐标
	 * @参数描述:
	 */
	public void modifyPosition(){
		HttpServletRequest request = getHttpServletRequest();
		
		String device_num = request.getParameter("device_num");
		String x_position = request.getParameter("x_position");
		String y_position = request.getParameter("y_position");
		String login_user = ((User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER)).getYhMc();
		
		DeviceInfo device = new DeviceInfo();
		device.setDevice_num(device_num);
		device.setX_position(Double.parseDouble(x_position));
		device.setY_position(Double.parseDouble(y_position));
		devicePositionService.setDevicePosition(device, login_user);
		
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		result.setMsg("修改设备坐标成功");
		printHttpServletResponse(GsonUtil.toJson(result)); 
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-13 下午3:44:37
	 * @功能描述: 删除设备坐标
	 * @参数描述:
	 */
	public void removePosition(){
		HttpServletRequest request = getHttpServletRequest();
		String device_num = request.getParameter("device_num");
		String login_user = ((User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER)).getYhMc();
		
		devicePositionService.removeDevicePosition(device_num, login_user);
		
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		result.setMsg("删除设备坐标成功");
		printHttpServletResponse(GsonUtil.toJson(result)); 
	}
	
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-13 下午3:44:55
	 * @功能描述: 查询设备坐标信息
	 * @参数描述:
	 */
	public void getPositionInfo(){
		HttpServletRequest request = getHttpServletRequest();
		String flag = request.getParameter("flag");
		String group_num = request.getParameter("group_num");
		if(EmptyUtil.atLeastOneIsEmpty(group_num)){
			group_num="";
		}
		List<DeviceDistributionPic> pics = devicePositionService.getDevicePositionInfo(flag, group_num);
		printHttpServletResponse(GsonUtil.toJson(pics)); 
	}
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-6-15 下午2:52:55
	 * @功能描述: 获取图片
	 * @参数描述:
	 */
	public void getImg(){
    	HttpServletRequest request=getHttpServletRequest();	
    	HttpServletResponse response=getHttpServletResponse();
    	 
    	String imgPath="";
    	try {
    		String a = request.getParameter("pic_path");
			imgPath = new String(request.getParameter("pic_path").getBytes("iso-8859-1"), "UTF-8");
			imgPath = a;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
    	ContinueFTP continueFTP=new ContinueFTP();
    	java.io.InputStream is=null;
    	OutputStream out=null;
    	try {
    		 
     		String ftpPropertiesPath=request.getSession().getServletContext().getRealPath("/WEB-INF"+File.separator+"classes"+File.separator+"conf"+File.separator+"properties"+File.separator+"ftp.properties");
    	    com.kuangchi.sdd.util.file.FTP ftp=com.kuangchi.sdd.util.file.FTPUtil.getFtp(ftpPropertiesPath);		
    		 
		    continueFTP.connect(ftp.getHost(), ftp.getPort(), ftp.getUserName(), ftp.getPassword());
		    if (null!=imgPath&&!imgPath.trim().equals("")&&!"undefined".equals(imgPath)) {
		    	is=continueFTP.download(imgPath);
		    }else{
		    	 //如果没有上传头像，则给一个默认头像
		    	String defaultImg=request.getSession().getServletContext().getRealPath("/businessConsole"+File.separator+"images"+File.separator+"defaultImg.jpg");
		    	File file=new File(defaultImg)  ;
		    	is=new FileInputStream(file);
		      }
			out=response.getOutputStream();
			byte[] buffer=new byte[1024];
			while (is.read(buffer)!=-1) {
			    out.write(buffer);			
			}
			out.flush();
			continueFTP.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (null!=is) {
				try {
					is.close();
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
