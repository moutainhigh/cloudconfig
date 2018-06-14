package com.kuangchi.sdd.commConsole.search.service.impl;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.commConsole.search.model.EquipmentBean;
import com.kuangchi.sdd.commConsole.search.service.ISearchEquipment;
@Transactional
@Service("searchEquipmentImpl")
public class SearchEquipmentImpl implements ISearchEquipment{
	@Resource(name="getDevInfoService")
	GetDevInfoService getDevInfoService;
	@Override
	public List<EquipmentBean> searchEquipment(String doorType,String[] ips) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		//设备类型 分别为 M1 M2 M4
		pkg.setSign(Integer.parseInt(doorType));
		pkg.setOrder(0x00);
		pkg.setLength(0x0000);
		pkg.setOrderStatus(0x00);
		//校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());
		List<EquipmentBean> allEquipment=new ArrayList<EquipmentBean>();
		try {
			DeviceInfo2 deviceInfo2=null;
			String result=null;
			List<String> allStrings=new ArrayList<String>();
			
			for (int i = 0; i < ips.length; i++) {
				deviceInfo2=new DeviceInfo2();
				deviceInfo2.setBroadcastIP(ips[i]);
				try {
					
				
				 result= com.kuangchi.sdd.comm.container.Service.service("search", pkg,deviceInfo2);
				
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(null!=result){
					StringTokenizer st=new StringTokenizer(result,"|");
					
					while(st.hasMoreTokens()){
						allStrings.add(st.nextToken());
					}
				}
					
			}
			
			Gson gson=new Gson();
			java.lang.reflect.Type type = new TypeToken<List<EquipmentBean>>() {}.getType(); 
			allEquipment=gson.fromJson(allStrings.toString(), type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allEquipment;
	}

}
