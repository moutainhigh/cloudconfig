package com.kuangchi.sdd.commConsole.init.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.comm.container.Service;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.EquipmentDataForServer;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.gate.reset.ResetConnector;
import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.commConsole.init.model.ResultMsg;
import com.kuangchi.sdd.commConsole.init.service.IEquipmentInit;

@Controller("equipmentInitAction")
public class EquipmentInitAction extends BaseActionSupport{
	public static final Logger LOG = Logger.getLogger(EquipmentInitAction.class);
    /**
     * 
     */
    private static final long serialVersionUID = -5836433585216684707L;
    
    @Resource(name="equipmentInitImpl")
    
    private IEquipmentInit equipmentInit;

    @Override
    public Object getModel() {
	return null;
    }
    
    //修改设备信息
    public void initEquipment(){
	String mac=getHttpServletRequest().getParameter("mac");
	String deviceType=getHttpServletRequest().getParameter("deviceType");
	String data=getHttpServletRequest().getParameter("data");
	Gson gson=new Gson();
	java.lang.reflect.Type type = new TypeToken<EquipmentDataForServer>() {}.getType(); 
	EquipmentDataForServer equipmentData=gson.fromJson(data, type);
	LOG.info(equipmentData);
	String result=equipmentInit.initSearchEquipment(mac,deviceType, equipmentData);
	ResultMsg rm=new ResultMsg();
	if(!result.equals("null")){
		rm.setResult_code("0");
		rm.setResult_msg("修改设备信息成功");
	}else{
		rm.setResult_code("1");
		rm.setResult_msg("修改设备信息失败");
	}
	printHttpServletResponse(new Gson().toJson(rm));
    }

    //初始化设备信息
	public void resetEquipment() throws Exception {
		String mac=getHttpServletRequest().getParameter("mac");
		String deviceType=getHttpServletRequest().getParameter("deviceType");
		String result=equipmentInit.reset(mac,deviceType);
		LOG.info(result);
		ResultMsg rm=new ResultMsg();
		if(!result.equals("null")){
			rm.setResult_code("0");
			rm.setResult_msg("初始化设备成功");
		}else{
			rm.setResult_code("1");
			rm.setResult_msg("初始化设备失败");
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}
    
    
}
