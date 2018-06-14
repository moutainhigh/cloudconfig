package com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.dao.DeviceTimesDao;
import com.kuangchi.sdd.baseConsole.deviceTimes.devicetimes.model.DeviceTimes;

/**
 * @创建人　: 陈桂波
 * @创建时间: 2016-10-9 下午4:19:16
 * @功能描述: 时段模块-Dao实现类
 */
@Repository("DevicetimesDaoImpl")
public class DeviceTimesDaoImpl extends BaseDaoImpl<DeviceTimes> implements DeviceTimesDao {

	@Override
	public List<DeviceTimes> getTimesByParamPage(DeviceTimes times, int page, int size) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("begin_time", times.getBegin_time());
		param.put("end_time", times.getEnd_time());
		param.put("eare_num", times.getEare_num());
		param.put("times_num", times.getTimes_num());
		param.put("device_num", times.getDevice_num());
		if(page!=0 && size!=0){
			param.put("page", (page - 1) * size);
			param.put("size", size);
		}
		return this.getSqlMapClientTemplate().queryForList("getDeviceTimesByParamPage", param);
	}

	@Override
	public int getTimesByParamCount(DeviceTimes times) {
		return (Integer) find("getDeviceTimesByParamCount", times);
	}
	
	@Override
	public boolean addDeviceTimes(DeviceTimes times) {
		int maxNum = 0;
		if(find("getDeviceMaxNum", times)!=null){
			maxNum = (Integer) find("getDeviceMaxNum", times);
			}
			if(maxNum == 255){
				return false;
			}else if(maxNum >=127 && maxNum < 255){
				times.setEare_num(1);
			}else {
				times.setEare_num(0);
			}
		times.setTimes_num(String.valueOf(maxNum+1));
		return insert("addDeviceTimes", times);
	}
	
	//复制时新增时段
	@Override
	public boolean copeAddDeviceTimes(DeviceTimes times) {
		return insert("copeAddDeviceTimes", times);
	}
	
	@Override
	public boolean modifyTimes(DeviceTimes times) {
		return update("modifyDeviceTimes", times);
	}
	
	@Override
	public String getNameSpace() {
		return "common.deviceTimes";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<DeviceTimes> getTimesByParamInterface(DeviceTimes times) {
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("eare_num", times.getEare_num());
		param.put("device_num", times.getDevice_num());
		return this.getSqlMapClientTemplate().queryForList("getDeviceTimesByParamInterface", param);
	}

	@Override
	public List<DeviceInfo> getDeviceByNums(String device_num) {
		return this.getSqlMapClientTemplate().queryForList("getDeviceTimesByNums", device_num);
	}

	@Override
	public List<DeviceTimes> getDeviceMacByNums(String device_num) {
		return this.getSqlMapClientTemplate().queryForList("getDeviceMacByNums", device_num);
	}

	@Override
	public List<DeviceTimes> getDeviceTimesByDeviceNum(String device_num) {
		return this.getSqlMapClientTemplate().queryForList("getDeviceTimesByDeviceNum", device_num);
	}

	@Override
	public boolean deleteDeviceTimesByDeviceNum(String deviceNums) {
		// TODO Auto-generated method stub
		return delete("deleteDeviceTimesByDeviceNum",deviceNums);
	}

	@Override
	public void delRepTime(DeviceTimes times) {
		getSqlMapClientTemplate().delete("delRepTime", times);
	}

	@Override
	public void addTimeAfterDel(DeviceTimes times) {
		getSqlMapClientTemplate().insert("addTimeAfterDel", times);
	}
	
	@Override
	public boolean delTimesById(String ids){
		return delete("delTimesById", ids);
	}
	
	@Override
	public List<Map<String, Object>> getTimesByDevice(String device_num){
		return this.getSqlMapClientTemplate().queryForList("getTimesByDevice", device_num);
	}
	
	@Override
	public boolean updateTimeNum(Map<String, Object> map){
		return update("updateTimeNum", map);
	}
	
	@Override
	public Integer ifUsedTimes(Map<String, Object> map){
		return (Integer) this.getSqlMapClientTemplate().queryForObject("ifUsedTimes", map);
	}

}
