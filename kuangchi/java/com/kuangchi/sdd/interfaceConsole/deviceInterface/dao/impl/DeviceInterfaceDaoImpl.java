package com.kuangchi.sdd.interfaceConsole.deviceInterface.dao.impl;



import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.interfaceConsole.deviceInterface.dao.IDeviceInterfaceDao;

/**
 * @创建人　: 高育漫
 * @创建时间: 2016-5-19 下午2:20:14
 * @功能描述: 对外接口-dao实现类
 */
@Repository("deviceInterfaceDaoImpl")
public class DeviceInterfaceDaoImpl extends BaseDaoImpl<Object> implements IDeviceInterfaceDao {

	@Override
	public String getNameSpace() {
		return "interfaceConsole.deviceInterface";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public DeviceInfo getDeviceByMac(String device_mac) {
		return (DeviceInfo) this.getSqlMapClientTemplate().queryForObject("getDeviceByMac", device_mac);
	}

	
}
