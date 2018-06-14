package com.kuangchi.sdd.consumeConsole.deviceType.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.consumeConsole.deviceType.dao.IDeviceTypeDao;
import com.kuangchi.sdd.consumeConsole.deviceType.model.DeviceType;
import com.kuangchi.sdd.consumeConsole.deviceType.service.IDeviceTypeService;

@Service("deviceTypeService")
public class DeviceTypeServiceImpl implements IDeviceTypeService {

	@Resource(name = "deviceTypeDao")
	IDeviceTypeDao deviceTypeDao;
	
	@Resource(name = "LogDaoImpl")
    LogDao logDao;
	
	@Override
	public Grid<DeviceType> getDeviceTypePage(DeviceType deviceType) {
		try{
			Grid<DeviceType> grid = new Grid<DeviceType>();
			List<DeviceType> resultList = deviceTypeDao.getDeviceTypePage(deviceType);
			grid.setRows(resultList);
			if(null != resultList){
				grid.setTotal(deviceTypeDao.getDeviceTypeCounts(deviceType));
			}else{
				grid.setTotal(0);
			}
			return grid;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		} 
	}

	@Override
	public DeviceType selDeviceTypeById(String id) {
		try{
			return deviceTypeDao.selDeviceTypeById(id);
		}catch(Exception e) {
			return null;
		} 
	}

	@Override
	public Integer validNum(DeviceType deviceType) {
		try{
			return deviceTypeDao.validNum(deviceType);
		}catch(Exception e) {
			e.printStackTrace();
			return 1;
		} 
	}

	@Override
	public boolean insertDeviceType(DeviceType deviceType,String create_user) {
		Map<String,String> log = new HashMap<String,String>();
		try{
			boolean result = deviceTypeDao.insertDeviceType(deviceType);
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
			log.put("V_OP_NAME", "消费设备类型管理");
			log.put("V_OP_FUNCTION", "新增");
			log.put("V_OP_ID", create_user);
			log.put("V_OP_MSG", "新增设备类型信息");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean updateDeviceType(DeviceType deviceType, String create_user) {
		Map<String,String> log = new HashMap<String,String>();
		try{
			boolean result = deviceTypeDao.updateDeviceType(deviceType);
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
			log.put("V_OP_NAME", "消费设备类型管理");
			log.put("V_OP_FUNCTION", "修改");
			log.put("V_OP_ID", create_user);
			log.put("V_OP_MSG", "修改设备类型信息");
			logDao.addLog(log);
		}
	}

	@Override
	public boolean deleteDeviceType(String ids, String create_user) {
		Map<String,String> log = new HashMap<String,String>();
		try{
			boolean result = deviceTypeDao.deleteDeviceType(ids);
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
			log.put("V_OP_NAME", "消费设备类型管理");
			log.put("V_OP_FUNCTION", "删除");
			log.put("V_OP_ID", create_user);
			log.put("V_OP_MSG", "删除设备类型信息");
			logDao.addLog(log);
		}
	}

	@Override
	public List<DeviceType> getAllDeviceType() {
		try{
			return deviceTypeDao.getAllDeviceType();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		} 
	}

}
