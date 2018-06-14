package com.kuangchi.sdd.commConsole.search.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.commConsole.search.model.EquipmentBean;
import com.kuangchi.sdd.commConsole.search.model.ResultMsg;
import com.kuangchi.sdd.commConsole.search.service.ISearchEquipment;

@Controller("equipmentAction")
public class EquipmentAction extends BaseActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 293788929553302791L;
	
	@Resource(name="searchEquipmentImpl")
	
	private ISearchEquipment searchEquipemnt;

	@Override
	public Object getModel() {
		return null;
	}
	
	public void searchEquipment(){
		String doorType=getHttpServletRequest().getParameter("doorType");
		String beginIp=getHttpServletRequest().getParameter("beginIp");
		String endIp=getHttpServletRequest().getParameter("endIp");
		String[] beginIps=beginIp.split("\\.");
		String[] endIps=endIp.split("\\.");
		int ipSize=Integer.parseInt(endIps[3])-Integer.parseInt(beginIps[3]);
		String ips[] =new String[ipSize+1];
	
		for (int i = 0; i <=ipSize ; i++) {
			Integer tail=(Integer.parseInt(beginIps[3])+i);
			ips[i]=beginIps[0]+"."+ beginIps[1]+"."+beginIps[2]+"."+tail;
		}
		
		List<EquipmentBean> equipment=searchEquipemnt.searchEquipment(doorType,ips);
		ResultMsg rm=new ResultMsg();
		if(equipment!=null){
			rm.setListEquipment(equipment);
			rm.setResult_code("0");
			rm.setResult_msg("搜索设备成功");
		}else{
			rm.setResult_code("1");
			rm.setResult_msg("搜索设备失败");
		}
		printHttpServletResponse(new Gson().toJson(rm));
	}

}
