package com.kuangchi.sdd.elevatorConsole.devicePosition.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.elevatorConsole.devicePosition.dao.ITKDevicePosiDao;
import com.kuangchi.sdd.elevatorConsole.devicePosition.model.TKDevicePosi;

/**
 * 设备地图坐标
 * @author minting.he
 *
 */
@Repository("tkDevicePosiDao")
public class DevicePosiDaoImpl extends BaseDaoImpl<Object> implements ITKDevicePosiDao {

	@Override
	public String getNameSpace() {
		return "common.TKDevicePosi";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public boolean updateTKDeviPosition(TKDevicePosi devicePosition) {
		return update("updateTKDeviPosition", devicePosition);
	}

	@Override
	public boolean deleteTKDeviPosition(String device_num) {
		return delete("deleteTKDeviPosition", device_num);
	}

	@Override
	public List<TKDevicePosi> getTKDeviByPicId(String pic_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pic_id", pic_id);
		return this.getSqlMapClientTemplate().queryForList("getTKDeviByPicId", map);
	}

	@Override
	public List<String> getTKDeviByGroupNum(String device_group_num) {
		return this.getSqlMapClientTemplate().queryForList("getTKDeviByGroupNum", device_group_num);
	}

}
