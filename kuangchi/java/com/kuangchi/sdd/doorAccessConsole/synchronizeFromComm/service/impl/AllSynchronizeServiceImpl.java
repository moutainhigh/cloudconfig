package com.kuangchi.sdd.doorAccessConsole.synchronizeFromComm.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibatis.sqlmap.engine.mapping.result.ResultMap;
import com.kuangchi.sdd.baseConsole.mjComm.service.MjCommService;
import com.kuangchi.sdd.doorAccessConsole.synchronizeFromComm.dao.AllSynchronizeDao;
import com.kuangchi.sdd.doorAccessConsole.synchronizeFromComm.service.AllSynchronizeService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;
import com.kuangchi.sdd.util.commonUtil.PropertiesToMap;

@Service("allSynchronizeService")
public class AllSynchronizeServiceImpl implements AllSynchronizeService {

	public static final Logger LOG = Logger.getLogger(AllSynchronizeServiceImpl.class);
	
	@Autowired
	private AllSynchronizeDao allSynchronizeDao;

	@Resource(name = "mjCommServiceImpl")
	private MjCommService mjCommService;

	/**
	 * 设备上传权限信息
	 * by gengji.yang
	 */
	public void uploadAndSynchronizeLimit(String deviceNum,String cardNum,String createUser){
		try{
			Map macAndType=allSynchronizeDao.getMacAndType(deviceNum);
			Map<String, String> map = PropertiesToMap.propertyToMap("comm_interface.properties");
			String getLimUrl = mjCommService.getMjCommUrl(deviceNum)+ "gateLimit/getGateLimit.do?";
			String resultStr = HttpRequest.sendPost(getLimUrl, "mac="+macAndType.get("deviceMac") + "&cardId=" +cardNum+"&sign="+macAndType.get("deviceType"));
			Map resultMap=GsonUtil.toBean(resultStr,HashMap.class);
			resultMap.put("deviceNum", deviceNum);
			resultMap.putAll(macAndType);
			delOld((String)resultMap.get("cardId"),(String)resultMap.get("deviceNum"));
			for(int i=1;i<=4;i++){
				if((Boolean)resultMap.get("door"+i)==true){
					synAuthority(resultMap,i,createUser);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			LOG.info("门禁权限信息上传失败");
		}
//		{cardId=70, time4=null, time3=null, time2=null, time1=null, startDateTime=2016-09-12 10:22, door1=false, door2=false, endDateTime=2016-09-30 10:22, door3=false, door4=false}
	}
	
	/**
	 * 同步权限列表
	 * by gengji.yang
	 */
	public void synAuthority(Map resultMap,int i,String createUser){
		Map map=new HashMap();
		map.put("cardNum",resultMap.get("cardId"));
		map.put("doorNum",i);
		map.put("deviceNum",resultMap.get("deviceNum"));
		map.put("startDateTime",resultMap.get("startDateTime"));
		map.put("endDateTime",resultMap.get("endDateTime"));
		map.put("groupNum",resultMap.get("time"+i).equals(false)?null:resultMap.get("time"+i));
		map.put("createUser",createUser);
		allSynchronizeDao.addSynAuthority(map);
	}
	
	/**
	 * 删除原有权限
	 * by gengji.yang
	 */
	public void delOld(String cardNum,String deviceNum){
		Map map=new HashMap();
		map.put("cardNum", cardNum);
		map.put("deviceNum", deviceNum);
		allSynchronizeDao.delSynAuthority(map);
	}
}
