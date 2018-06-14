package com.kuangchi.sdd.elevatorConsole.device.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.elevatorConsole.device.dao.TKDeviceOpenTimeDao;
import com.kuangchi.sdd.elevatorConsole.device.model.CommIpInfoModel;
import com.kuangchi.sdd.elevatorConsole.device.model.Device;
import com.kuangchi.sdd.elevatorConsole.device.model.Floor;
import com.kuangchi.sdd.elevatorConsole.device.model.TKDeviceOpenTimeModel;
import com.kuangchi.sdd.elevatorConsole.device.model.TimesGroupModel;
import com.kuangchi.sdd.elevatorConsole.device.model.TkDevAuthorityCardModel;
@Repository("tKDeviceOpenTimeDaoImpl")
public class TKDeviceOpenTimeDaoImpl extends BaseDaoImpl<Object> implements TKDeviceOpenTimeDao{

	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public boolean initDeviceParam(String device_num) {
		boolean result = update("initDeviceParam", device_num);
		if(!result){
			throw new RuntimeException();
		}
		return result;
		
	}

	@Override
	public List<TimesGroupModel> getAllTimesGroupByDevNumList() {
		return getSqlMapClientTemplate().queryForList("getAllTimesGroupByDevNumList");
	}

	@Override
	public Integer getAllTimesGroupByDevNumCount() {
		return (Integer) getSqlMapClientTemplate().queryForObject("getAllTimesGroupByDevNumCount");
	}

	@Override
	public boolean motifyDeviceOpenTime(Map map) {
		return update("motifyDeviceOpenTime", map);
	}

	@Override
	public Integer getDeviceOpenTimeByDevNum(String device_num) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getDeviceOpenTimeByDevNum", device_num);
	}

	@Override
	public boolean insertDeviceOpenTime(String device_num) {
		Integer week_day = 0;
		Map map = new HashMap();
		for(int i=0;i<8;i++){
			week_day = week_day+1;
			map.put("week_day", week_day);
			map.put("device_num", device_num);
			if(!insert("insertDeviceOpenTime",map)){
				return false;
			}
		  }
		return true;
	}

	@Override
	public List<TKDeviceOpenTimeModel> checkDevTimeGroup(String device_num) {
		return getSqlMapClientTemplate().queryForList("checkDevTimeGroup", device_num);
	}

	@Override
	public boolean deleteDevOpenTime(String device_nums) {
		return update("deleteDevOpenTime",device_nums);
	}

	@Override
	public boolean copyDevOpenTime(TKDeviceOpenTimeModel tKDeviceOpenTimeModel) {
		return update("copyDevOpenTime",tKDeviceOpenTimeModel);
	}

	@Override
	public boolean setEleMoveParam(Map map) {
		return update("setEleMoveParam",map);
	}

	@Override
	public Device getEleMoveParam(String device_num) {
		return (Device) getSqlMapClientTemplate().queryForObject("getEleMoveParam", device_num);
	}

	@Override
	public boolean clearAuthorityByDevNum(String device_num) {
		return update("clearAuthorityByDevNum",device_num);
	}

	@Override
	public List<TkDevAuthorityCardModel> getAuthorityCardByDevNum(String device_num) {
		return getSqlMapClientTemplate().queryForList("getAuthorityCardByDevNum", device_num);
	}

	@Override
	public boolean insertDeviceAuth(Map map) {
		return insert("insertDeviceAuth",map);
	}

	@Override
	public boolean resetHolidayTime(Map map) {
		return update("resetHolidayTime",map);
	}

	@Override
	public boolean resetFloorTime(Map map) {
		return update("resetFloorTime",map);
	}

	@Override
	public boolean resetFloors(Map map) {
		return update("resetFloors",map);
	}

	@Override
	public boolean resetEleOpenTime(Map map) {
		return update("resetEleOpenTime",map);
	}

	@Override
	public boolean resetFloorConfig(Floor floor) {
		return update("resetFloorConfig",floor);
	}

	@Override
	public boolean resetEleParam(Map eleMap) {
		return update("resetEleParam",eleMap);
	}

	@Override
	public List<CommIpInfoModel> getCommIpInfoList(Map map) {
		return getSqlMapClientTemplate().queryForList("getCommIpInfoList", map);
	}

	@Override
	public Integer getCommIpInfoCount(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getCommIpInfoCount", map);
	}

	@Override
	public boolean addCommIpInfo(Map map) {
		return insert("addCommIpInfo",map);
	}

	@Override
	public CommIpInfoModel getCommIpInfoById(String id) {
		return (CommIpInfoModel) getSqlMapClientTemplate().queryForObject("getCommIpInfoById", id);
	}

	@Override
	public boolean updateCommIpInfo(Map map) {
		return update("updateCommIpInfo",map);
	}

	@Override
	public boolean delCommIpInfo(String ids) {
		return update("delCommIpInfo",ids);
	}

	@Override
	public Integer getCountById(Integer id) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getCountById", id);
	}

	@Override
	public boolean insertAuthorityHistory(Map map1) {
		return insert("insertAuthorityHistory",map1);
	}

	@Override
	public boolean deleteAuthorityBydevNum(String deviceNum) {
		return delete("deleteAuthorityBydevNum",deviceNum);
	}

}
