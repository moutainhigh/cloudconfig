package com.kuangchi.sdd.consumeConsole.devicePosition.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.devicePosition.model.DeviceDistributionPic;
import com.kuangchi.sdd.consumeConsole.devicePosition.dao.IDevicePosiDao;
import com.kuangchi.sdd.consumeConsole.devicePosition.model.DevicePosi;

/**
 * 设备地图坐标
 * @author minting.he
 *
 */
@Repository("devicePosiDao")
public class DevicePosiDaoImpl extends BaseDaoImpl<Object> implements IDevicePosiDao {

	@Override
	public String getNameSpace() {
		return "common.DevicePosi";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public boolean updateDevicePosition(DevicePosi devicePosition) {
		return update("updateDevicePosition", devicePosition);
	}

	@Override
	public boolean deleteDevicePosition(String device_num) {
		return delete("deleteDevicePosition", device_num);
	}

	@Override
	public List<DeviceDistributionPic> getAllPicInfo() {
		return this.getSqlMapClientTemplate().queryForList("getAllPicInfo");
	}

	@Override
	public List<DevicePosi> getDeviceByPicId(String pic_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pic_id", pic_id);
		return this.getSqlMapClientTemplate().queryForList("getDeviceByPicId", map);
	}

	@Override
	public List<String> getXFDeviByGroupNum(String device_group_num) {
		return this.getSqlMapClientTemplate().queryForList("getXFDeviByGroupNum", device_group_num);
	}
	@Override
	public List<String> getTKDeviByGroupNum(String device_group_num) {
		return this.getSqlMapClientTemplate().queryForList("getTKDeviByGroupNum", device_group_num);
	}

	@Override
	public List<DeviceDistributionPic> getPicByGroupNum(String flag, String group_num) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", flag);
		map.put("group_num", group_num);
		return this.getSqlMapClientTemplate().queryForList("getPicByGroupNum", map);
	}

}
