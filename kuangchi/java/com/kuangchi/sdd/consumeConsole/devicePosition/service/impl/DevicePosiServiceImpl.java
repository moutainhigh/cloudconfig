package com.kuangchi.sdd.consumeConsole.devicePosition.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.baseConsole.devicePosition.model.DeviceDistributionPic;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.consumeConsole.devicePosition.dao.IDevicePosiDao;
import com.kuangchi.sdd.consumeConsole.devicePosition.model.DevicePosi;
import com.kuangchi.sdd.consumeConsole.devicePosition.service.IDevicePosiService;

/**
 * 设备地图坐标
 * @author minting.he
 *
 */
@Transactional
@Service("devicePosiService")
public class DevicePosiServiceImpl implements IDevicePosiService {

	@Resource(name ="devicePosiDao")
	private IDevicePosiDao devicePosiDao;

	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public boolean updateDevicePosition(DevicePosi devicePosi,
			String login_user) {
		Map<String,String> log = new HashMap<String,String>();
		try{
			boolean result = devicePosiDao.updateDevicePosition(devicePosi);
			if(result){
				log.put("V_OP_TYPE", "业务");
			}else{
				log.put("V_OP_TYPE", "异常");
			}
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally{
			log.put("V_OP_NAME", "电子消费地图");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "修改设备地图坐标");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean deleteDevicePosition(String device_num, String login_user) {
		Map<String,String> log = new HashMap<String,String>();
		try{
			boolean result = devicePosiDao.deleteDevicePosition(device_num);
			if(result){
				log.put("V_OP_TYPE", "业务");
			}else{
				log.put("V_OP_TYPE", "异常");
			}
			return result;
		}catch(Exception e) {
			e.printStackTrace();
			log.put("V_OP_TYPE", "异常");
			return false;
		} finally{
			log.put("V_OP_NAME", "电子消费地图");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", login_user);
			log.put("V_OP_MSG", "删除设备地图坐标");
			logDao.addLog(log);
		}
	}

	@Override
	public List<DeviceDistributionPic> getPicInfo(String flag, String group_num) {
		try{
			List<DeviceDistributionPic> pics = devicePosiDao.getPicByGroupNum(flag, group_num);	//显示所有地图信息
			for(DeviceDistributionPic pic : pics){
				List<DevicePosi> exist_XFdevices = devicePosiDao.getDeviceByPicId(pic.getId());	//已关联到地图id的设备
				pic.setExist_XFdevices(exist_XFdevices);
				List<String> legal_devices = null;	
				legal_devices = devicePosiDao.getXFDeviByGroupNum(pic.getDevice_group_num());	//设备组下可关联到地图的设备
				pic.setLegal_devices(legal_devices);
			}
			return pics;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		} 
	}



}
