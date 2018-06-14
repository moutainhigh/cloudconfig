package com.kuangchi.sdd.interfaceConsole.deviceInterface.action;



import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.interfaceConsole.deviceInterface.service.IDeviceInterfaceService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-5-19 下午2:17:54
 * @功能描述: 对外接口Action
 */
@Controller("deviceInterfaceAction")
public class DeviceInterfaceAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	@Resource(name = "deviceInterfaceServiceImpl")
	private IDeviceInterfaceService deviceInterfaceService;
	
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-7-4 上午11:49:27
	 * @功能描述: 根据mac查询设备信息
	 * @参数描述:
	 */
	public void getDeviceByMac(){
		String device_mac = getHttpServletRequest().getParameter("device_mac");
		DeviceInfo device = deviceInterfaceService.getDeviceByMac(device_mac);
		printHttpServletResponse(GsonUtil.toJson(device)); 
	}
	
	
	
}
