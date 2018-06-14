package com.kuangchi.sdd.baseConsole.device.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.device.model.DeviceStateModel;
import com.kuangchi.sdd.baseConsole.device.service.DeviceService;
import com.kuangchi.sdd.baseConsole.log.service.LogService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

/**
 * Created by weixuan.lu on 2016/3/24.
 */
/*下面@Controller后面括号里的就是spring注入的class名，用在struts的配置文件里*/
@Controller("deviceStateAction")
public class DeviceStateAction extends BaseActionSupport {

     @Resource(name="deviceService")
     DeviceService deviceService;
     
    public String toDevicePage(){
       return  "success";
    }
  
    //weixuan.lu 查询设备状态
	public void getDeviceStateList(){
      	HttpServletRequest request=getHttpServletRequest();
      	
    	Grid<DeviceStateModel> deviceStateModel=null;
    	String beanObject = getHttpServletRequest().getParameter("data");
    	DeviceStateModel deviceStateModel1 = GsonUtil.toBean(beanObject,DeviceStateModel.class);
    	String deviceName=deviceStateModel1.getDeviceName();
    	String deviceMac=deviceStateModel1.getDeviceMac();
    			
    	Integer page = Integer.parseInt(request.getParameter("page"));
    	Integer rows = Integer.parseInt(request.getParameter("rows"));
    	Integer skip=(page-1)*rows;
    	
    	deviceStateModel =deviceService.searchDeviceState(deviceName,deviceMac,skip,rows);
        printHttpServletResponse(GsonUtil.toJson(deviceStateModel));
    }
	
	 public String toDeviceDialogPage(){
	        HttpServletRequest request=getHttpServletRequest();
	    	String beanObject = getHttpServletRequest().getParameter("data");
	    	DeviceStateModel deviceStateModel = GsonUtil.toBean(beanObject,DeviceStateModel.class);

	        String flag=request.getParameter("flag");
	        String deviceNum=request.getParameter("deviceNum");
	        DeviceStateModel deviceStateModel1=deviceService.getDeviceStateByDeviceNum(deviceNum);
	        request.setAttribute("deviceStateModel1", deviceStateModel1);
	        return "view";
	    }
    
	@Override
	public Object getModel() {
		
		return null;
	}

}