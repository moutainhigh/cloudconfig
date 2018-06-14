package com.kuangchi.sdd.commConsole.status.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.commConsole.status.model.GateParamData;
import com.kuangchi.sdd.commConsole.status.service.IStatusService;
@Transactional
@Service("statusServicempl")
public class StatusServicempl implements IStatusService{
	public static final Logger LOG = Logger.getLogger(StatusServicempl.class);
	@Resource(name="getDevInfoService")
	GetDevInfoService getDevInfoService;
	@Override
	public List<GateParamData> getStatus(String mac) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		//pkg.setMac(0x0000017);
		pkg.setMac(Util.getIntHex(mac));//转换为16进制
		pkg.setOrder(0x35);
		pkg.setLength(0x0000);
		pkg.setOrderStatus(0x00);
		//校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());

		String result=null;
		List<GateParamData> allStatus=new ArrayList<GateParamData>();
		try {
			LOG.info("输出在这里"+pkg.getMac());
			DeviceInfo2 deviceInfo2=getDevInfoService.getManager(mac);
			result = com.kuangchi.sdd.comm.container.Service.service("get_status", pkg,deviceInfo2);
			List<String> allStrings=new ArrayList<String>();
			StringTokenizer st=new StringTokenizer(result,"|");
			while(st.hasMoreTokens()){
				allStrings.add(st.nextToken());
			}
			Gson gson=new Gson();
			java.lang.reflect.Type type = new TypeToken<List<GateParamData>>() {}.getType(); 
			allStatus=gson.fromJson(allStrings.toString(), type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LOG.info("容器获取的值:" + result);
        return allStatus;	
	}
}
