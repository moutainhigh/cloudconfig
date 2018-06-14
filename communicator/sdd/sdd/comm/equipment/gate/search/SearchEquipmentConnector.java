package com.kuangchi.sdd.comm.equipment.gate.search;

import java.util.List;

import com.kuangchi.sdd.comm.equipment.base.BaseMultiPacketHandler;
import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.Manager;

/**
 * 搜索设备
 * 
 * */

public class SearchEquipmentConnector extends Connector{
	public String run() throws Exception {
		BaseMultiPacketHandler handler = new SearchEquipmentHandler();
		Manager equipment = new SearchEquipmentManager(this.getDeviceInfo());//设备接口类
		String result = "";
		List<String> connectResult = super.connectSearch(handler, equipment);
		if(connectResult != null){
			for(String str:connectResult){
				result += str+"|";
			}
			return result.substring(0, result.length()-1);
		}else{
		    return result;
		}
	}
}
