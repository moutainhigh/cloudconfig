package com.kuangchi.sdd.baseConsole.devicePosition.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.devicePosition.dao.DevicePositionDao;
import com.kuangchi.sdd.baseConsole.devicePosition.model.DeviceDistributionPic;
import com.kuangchi.sdd.baseConsole.devicePosition.service.DevicePositionService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.consumeConsole.devicePosition.dao.IDevicePosiDao;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-6-13 下午4:03:45
 * @功能描述: 设备坐标信息-业务实现类
 */
@Transactional
@Service("devicePositionService")
public class DevicePositionServiceImpl  implements DevicePositionService{

	@Resource(name ="devicePositionDaoImpl")
	private DevicePositionDao devicePositionDao;
	
	@Resource(name ="devicePosiDao")
	private IDevicePosiDao devicePosiDao;

	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public void setDevicePosition(DeviceInfo device, String login_user) {
		devicePositionDao.setDevicePosition(device);
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "设备坐标管理");
		log.put("V_OP_FUNCTION", "修改业务");
		log.put("V_OP_ID", login_user);
		log.put("V_OP_MSG", "更新设备坐标信息");
		log.put("V_OP_TYPE", "业务");
		logDao.addLog(log);
	}
	
	@Override
	public void removeDevicePosition(String device_num, String login_user) {
		devicePositionDao.removeDevicePosition(device_num);
		
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "设备坐标管理");
		log.put("V_OP_FUNCTION", "删除业务");
		log.put("V_OP_ID", login_user);
		log.put("V_OP_MSG", "删除设备坐标信息");
		log.put("V_OP_TYPE", "业务");
		logDao.addLog(log);
	}
	
	@Override
	public List<DeviceDistributionPic> getDevicePositionInfo(String flag, String group_num) {
		
		List<DeviceDistributionPic> pics = devicePosiDao.getPicByGroupNum(flag, group_num);
		for (DeviceDistributionPic pic : pics) {
			List<DeviceInfo> exist_devices = devicePositionDao.getDeviceByPic(pic.getId());
			pic.setExist_devices(exist_devices);
			List<String> legal_devices = devicePositionDao.getDeviceByDeviceGroup(pic.getDevice_group_num());
			pic.setLegal_devices(legal_devices);
		}
		return pics;
	}

	@Override
	public void updateDeviceState(String device_num, int online_state) {
		devicePositionDao.updateDeviceState(device_num, online_state);
		
	}

	@Override
	public List<DeviceInfo> getDeviceByPic(String pic_id) {
		// TODO Auto-generated method stub
		return devicePositionDao.getDeviceByPic(pic_id);
	}

	
}
