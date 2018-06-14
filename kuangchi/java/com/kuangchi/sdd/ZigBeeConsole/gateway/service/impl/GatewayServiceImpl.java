package com.kuangchi.sdd.ZigBeeConsole.gateway.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.ZigBeeConsole.gateway.dao.IGatewayDao;
import com.kuangchi.sdd.ZigBeeConsole.gateway.model.GatewayModel;
import com.kuangchi.sdd.ZigBeeConsole.gateway.service.IGatewayService;
import com.kuangchi.sdd.ZigBeeConsole.quartz.service.impl.ZBAuthorityManagerQuartz;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.HttpRequest;


/**
 * 光子锁记录 - service实现类
 * @author guibo.chen
 */
@Transactional
@Service("gatewayServiceImpl")
public class GatewayServiceImpl implements IGatewayService{
	
	@Resource(name = "gatewayDaoImpl")
	private IGatewayDao gatewayDao;

	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	

	@Override
	public Grid getGatewayInfo(GatewayModel device, String Page, String size) {
		Integer count=gatewayDao.getGatewayInfoCount(device);
		List<GatewayModel> list=gatewayDao.getGatewayInfo(device, Page, size);
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(list);
		return grid;
	}

	@Override
	public String addGatewayinfo(GatewayModel device) {
		
		boolean getResource=false;
		//在5秒之内获取许可，如果获取到了就执行更新状语句，否则不执行
		Semaphore semaphore = ZBAuthorityManagerQuartz.semaphore;
		try {
			getResource=semaphore.tryAcquire(5,TimeUnit.SECONDS);
		} catch (Exception e) {
			 e.printStackTrace();
			 return "false";
		}
		if(getResource){
			try {
				Map<String, String> log = new HashMap<String, String>();
		        log.put("V_OP_NAME", "设备信息维护");
		        log.put("V_OP_FUNCTION", "新增");
		        //log.put("V_OP_ID", discount_info.getCreate_user());
		        try{
		        	List<GatewayModel> list=gatewayDao.getGatewaypanId(device.getGateway_pan_id());
		        	if(list.size()!=0){
		        		return "Panidfalse";
		        	}
		        	String url="http://"+device.getRemote_ip()+":"+
			    			device.getRemote_port()+"/comm/zigBee/getPanId.do?";
					String data="remoteIp="+device.getGateway()+"&port="+device.getGateway_port();
					String str = HttpRequest.sendPost(url,data);
					
					if(str != null && !"".equals(str) && !"null".equals(str)){
						 Map<String,Object> devices=GsonUtil.toBean(str, HashMap.class);
						 device.setFirmwareVersion((String)devices.get("firmwareVersion"));
				  		 device.setSoftwareVersion((String)devices.get("softwareVersion"));
						 String zigBeeId = (String)devices.get("zigBeeId");
						 //String pan_id= (String)devices.get("panId");
						 if(zigBeeId!=null){
							 //pan_id=String.valueOf(Integer.parseInt(pan_id, 16));
							 device.setZigbee_id(zigBeeId);
							 if(setPanId(device)){//判断设置panid是不是设置成功
								 //device.setGateway_pan_id(pan_id);
								 device.setState("0");
							 }else{
								 device.setZigbee_id(null);
								 //device.setGateway_pan_id(null);
								 device.setState("1");
								 return "false";
							 }
						 } else {
							 device.setZigbee_id(null);
							 //device.setGateway_pan_id(null);
							 device.setState("1");
						 }
				         Boolean obj=gatewayDao.addGatewayinfo(device);
				    		if(obj==true){
				    	        log.put("V_OP_TYPE", "业务");
				    	        log.put("V_OP_MSG", "新增成功");
				    	        logDao.addLog(log);
				    	        return "true"; 
				    		}else{
				    			log.put("V_OP_TYPE", "业务");
				    			log.put("V_OP_MSG", "新增失败");
				    			logDao.addLog(log);
				    			return "false";
				    		}
					}else{
						return "false";
					}
					
		        }
		        catch(Exception e){
		        	e.printStackTrace();
		        	log.put("V_OP_TYPE", "异常");
		        	log.put("V_OP_MSG", "新增失败");
		    		logDao.addLog(log);
		    		
		        }
		        
			} catch (Exception e) {
				  e.printStackTrace();
			} finally{
				semaphore.release();	 
			}
		}
		
        return "false";
	}

	//当点击确定的时候请求该接口
	@Override
	public Boolean NewaddGatewayinfo(GatewayModel device) {
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "设备信息维护");
        log.put("V_OP_FUNCTION", "新增");
        //log.put("V_OP_ID", discount_info.getCreate_user());
        try{
        	device.setZigbee_id(null);
        	//device.setGateway_pan_id(null);
        	device.setState("1");
        	Boolean obj=gatewayDao.addGatewayinfo(device);
    		if(obj==true){
    	        log.put("V_OP_TYPE", "业务");
    	        log.put("V_OP_MSG", "新增成功");
    	        logDao.addLog(log);
    	        return true; 
    		}else{
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "新增失败");
    			logDao.addLog(log);
    			return false;
    		}
        }
        catch(Exception e){
        	e.printStackTrace();
        	log.put("V_OP_TYPE", "异常");
        	log.put("V_OP_MSG", "新增失败");
    		logDao.addLog(log);
    		return false;
        }
       
	}
	
	@Override
	public List<GatewayModel> getGatewayByIpAndPort(GatewayModel device) {
		return gatewayDao.getGatewayByIpAndPort(device);
	}

	public Boolean setPanId(GatewayModel device){
		String pan_id=Integer.toHexString(Integer.valueOf(device.getGateway_pan_id()));
 		String pasParam = "0000";
 		pan_id = pasParam.substring(0, 4-pan_id.length()) + pan_id;
		
 		String url="http://"+device.getRemote_ip()+":"+
 				device.getRemote_port()+"/comm/zigBee/setPanId.do?";
			String data="remoteIp="+device.getGateway()+"&port="+device.getGateway_port()+
					"&zigBeeId="+device.getZigbee_id()+"&panId="+pan_id;
			String str = HttpRequest.sendPost(url, data);//返回锁ID和状态位0x00或0x01
			if (str != null && !"".equals(str) && !"null".equals(str)){
				Map deviceMap = GsonUtil.toBean(str, HashMap.class);
				if(0.0==(Double)deviceMap.get("status")){
					return true;
				}else{
					return false;
				}
				}
		return false;
	}
	
	
	//获取芯片网关id
	@Override
	public Boolean getPanIdInfo(GatewayModel device) {
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "设备信息维护");
        log.put("V_OP_FUNCTION", "修改");
        //log.put("V_OP_ID", discount_info.getCreate_user());
        try{
        	Boolean obj=gatewayDao.getPanIdInfo(device);//修改芯片网关id
    		if(obj==true){
    	        log.put("V_OP_TYPE", "业务");
    	        log.put("V_OP_MSG", "修改成功");
    	        logDao.addLog(log);
    	        return true; 
    		}else{
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "修改失败");
    			logDao.addLog(log);
    			return false;
    		}
        }
        catch(Exception e){
        	e.printStackTrace();
        	log.put("V_OP_TYPE", "异常");
        	log.put("V_OP_MSG", "修改失败");
    		logDao.addLog(log);
    		return false;
        }
	}

	@Override
	public List<GatewayModel> getGatewayById(String id) {
		return gatewayDao.getGatewayById(id);
	}

	//伪删除
	@Override
	public Boolean deleteGatewayInfo(String gateway_id) {
	/*	List<GatewayModel> list=gatewayDao.getGatewayByGatewayId(gateway_id);
		if(list.size()==0){
			gatewayDao.deleteGatewayInfo(gateway_id);
			return true;
		}
		return false;*/
		
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "设备信息维护");
        log.put("V_OP_FUNCTION", "删除");
        //log.put("V_OP_ID", discount_info.getCreate_user());
        try{
    			Boolean obj=gatewayDao.deleteGatewayInfo(gateway_id);
        		if(obj==true){
        	        log.put("V_OP_TYPE", "业务");
        	        log.put("V_OP_MSG", "删除成功");
        	        logDao.addLog(log);
        	        return true; 
        		}else{
        			log.put("V_OP_TYPE", "业务");
        			log.put("V_OP_MSG", "删除失败");
        			logDao.addLog(log);
        			return false;
        		}
        }
        catch(Exception e){
        	e.printStackTrace();
        	log.put("V_OP_TYPE", "异常");
        	log.put("V_OP_MSG", "删除失败");
    		logDao.addLog(log);
    		return false;
        }
	}

	@Override
	public List<GatewayModel> getGatewayByGatewayId(String gateway_id) {
		return gatewayDao.getGatewayByGatewayId(gateway_id);
	}

	@Override
	public Boolean updateGatewayInfo(GatewayModel device) {
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "修改网关");
        log.put("V_OP_FUNCTION", "修改");
        //log.put("V_OP_ID", discount_info.getCreate_user());
        try{
        	Boolean obj=gatewayDao.updateGatewayInfo(device);
    		if(obj==true){
    	        log.put("V_OP_TYPE", "业务");
    	        log.put("V_OP_MSG", "修改成功");
    	        logDao.addLog(log);
    	        return true; 
    		}else{
    			log.put("V_OP_TYPE", "业务");
    			log.put("V_OP_MSG", "修改失败");
    			logDao.addLog(log);
    			return false;
    		}
        }
        catch(Exception e){
        	e.printStackTrace();
        	log.put("V_OP_TYPE", "异常");
        	log.put("V_OP_MSG", "修改失败");
    		logDao.addLog(log);
    		return false;
        }
	}

	@Override
	public Boolean getStateOnline(GatewayModel device) {
		return gatewayDao.getStateOnline(device);
	}

	@Override
	public Boolean getNoStateOnline(GatewayModel device) {
		return gatewayDao.getNoStateOnline(device);
	}
	
	//设置panid
	@Override
	public Boolean updateGatewayPanIdByDeviceId(GatewayModel device) {
		boolean getResource=false;
		//在5秒之内获取许可，如果获取到了就执行更新状语句，否则不执行
		Semaphore semaphore = new Semaphore(1);
		try {
			getResource=semaphore.tryAcquire(5,TimeUnit.SECONDS);
		} catch (Exception e) {
			 e.printStackTrace();
			 return false;
		}
		
		if(getResource){
		try{
			Map<String, String> log = new HashMap<String, String>();
	        log.put("V_OP_NAME", "设置网关panId");
	        log.put("V_OP_FUNCTION", "修改");
	        try{
			Map<String, Object> map =gatewayDao.getGatewayPanidByZigbeeId(device.getGateway_id());
			 if(map!=null){
	        		String remote_ip = (String)map.get("remote_ip");
	         		String remote_port = (String)map.get("remote_port");
	         		String gateway = (String)map.get("gateway");
	         		String gateway_port = (String)map.get("gateway_port");
	         		String zigBeeId = (String)map.get("zigbee_id");
	         		
	         		String pan_id=Integer.toHexString(Integer.valueOf(device.getGateway_pan_id()));
	         		String pasParam = "0000";
	         		pan_id = pasParam.substring(0, 4-pan_id.length()) + pan_id;
	        		
	         		String url="http://"+remote_ip+":"+
	         				remote_port+"/comm/zigBee/setPanId.do?";
	 				String data="remoteIp="+gateway+"&port="+gateway_port+
	 						"&zigBeeId="+zigBeeId+"&panId="+pan_id;
	 				String str = HttpRequest.sendPost(url, data);//返回锁ID和状态位0x00或0x01
	 				if (str != null && !"".equals(str) && !"null".equals(str)){
	 					Map deviceMap = GsonUtil.toBean(str, HashMap.class);
	 					if(0.0==(Double)deviceMap.get("status")){
	 					Boolean obj=gatewayDao.updateGatewayPanIdByZigbeeId(device);
	 					if(obj==true){
 	 		    	        log.put("V_OP_TYPE", "业务");
 	 		    	        log.put("V_OP_MSG", "修改成功");
 	 		    	        logDao.addLog(log);
 	 		    	        return true; 
 	 		    		}else{
 	 		    			log.put("V_OP_TYPE", "业务");
 	 		    			log.put("V_OP_MSG", "修改失败");
 	 		    			logDao.addLog(log);
 	 		    		}
 	 				}
 				}else{
 					log.put("V_OP_TYPE", "业务");
 	    			log.put("V_OP_MSG", "修改失败");
 	    			logDao.addLog(log);
 				}
        	 }
        }catch(Exception e){
        	e.printStackTrace();
        	log.put("V_OP_TYPE", "异常");
        	log.put("V_OP_MSG", "修改失败");
    		logDao.addLog(log);
    		return false;
        }
	} catch (Exception e) {
		  e.printStackTrace();
	} finally{
		semaphore.release();	 
	}
}
		return false;
}

	@Override
	public List<GatewayModel> getGatewaypanId(String gateway_pan_id) {
		return gatewayDao.getGatewaypanId(gateway_pan_id);
	}

	@Override
	public boolean setPortParam(Map map) {
		boolean result = false;
		String key = (String)map.get("sys_key");
		String paramList = gatewayDao.getParamValueByKey(key);
		if(paramList != null){
			result = gatewayDao.updatePortParam(map);
		} else {
			result = gatewayDao.addPortParam(map);
		}
		return result;
	}

	@Override
	public String getParamValueByKey(String key) {
		return gatewayDao.getParamValueByKey(key);
	}


	 					


}
