package com.kuangchi.sdd.baseConsole.doorinfo.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.doorinfo.dao.IDoorInfoDao;
import com.kuangchi.sdd.baseConsole.doorinfo.model.DoorInfoModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DoorModel;
@Repository("DoorInfoDaoImpl")
public class DoorInfoDaoImpl extends BaseDaoImpl<DoorInfoModel> implements IDoorInfoDao {
	
	//新增门信息
	@Override
	public Boolean insertDoorinfo(DoorInfoModel doorinfo) {
		Object obj=this.getSqlMapClientTemplate().insert("insertDoorinfo", doorinfo);
		if(obj==null){
			return true;
		}
		return false;
	}
	
	//删除门信息
	@Override
	public Integer deleteDoorinfo(DoorInfoModel doorinfo) {
		Integer obj=(Integer)this.getSqlMapClientTemplate().update("deleteDoorinfo", doorinfo);
		if(obj==0){
			return 0;
		}
		return 1;
	}
	
	//删除门卡表信息
	@Override
	public Integer deleteDoorPeopleauthority(DoorInfoModel doorinfo) {
		Integer obj=(Integer)this.getSqlMapClientTemplate().update("delDoorPeopleauthority", doorinfo);
		if(obj==0){
			return 0;
		}
		return 1;
	}
	
	//根据ID查门信息
	@Override
	public List<DoorInfoModel> selectDoorinfoById(Integer door_id) {
		return this.getSqlMapClientTemplate().queryForList("selectDoorinfoById",door_id);
	}
	
	//查询全部门信息
	@Override
	public List<DoorInfoModel> selectAllDoorinfos(DoorInfoModel doorinfo,String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("page", (page-1)*rows);
		mapState.put("rows", rows);
		mapState.put("device_name", doorinfo.getDevice_name());
		mapState.put("door_name", doorinfo.getDoor_name());
		mapState.put("door_num", doorinfo.getDoor_num());
		return this.getSqlMapClientTemplate().queryForList("selectAllDoorinfos", mapState);
	}
	
	//查询总条数
	@Override
	public Integer getAllDoorinfoCount(DoorInfoModel doorinfo) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("device_name", doorinfo.getDevice_name());
		mapState.put("door_name", doorinfo.getDoor_name());
		mapState.put("door_num", doorinfo.getDoor_num());
		return queryCount("getAllDoorinfoCount",mapState);
	}

	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}
	
	
	
	//门编号自增
	@Override
	public int getDoorID(String door_num) {
		return queryCount("getLastDoorId",door_num);
	}
	
	
	//修改门信息
	@Override
	public Boolean updateDoorNameAndDes(DoorInfoModel doorinfo) {
		Object obj=this.getSqlMapClientTemplate().update("updateDoorNameAndDes", doorinfo);
		if(obj==null){
			return false;
		}
		return true;
	}
	
	//新增时查询设备编号
	@Override
	public List<DoorInfoModel> selectDeviceNumAdd() {
		return this.getSqlMapClientTemplate().queryForList("selectDeviceNumAdd", "");
	}

	//根据设备编号查询设备mac地址
	@Override
	public DeviceInfo queryMacByDeviceNum(String device_num) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("device_num", device_num);
		return (DeviceInfo) this.getSqlMapClientTemplate().queryForObject("queryMacByDeviceNum",map);
	}
	
	// 查询被删除的最小设备编号 
	@Override
	public String querydevicemin(String device_num){
		return (String) this.getSqlMapClientTemplate().queryForObject("querydevicemin",device_num);
	}

	//根据设备编号和门编号新增被删除的门
	@Override
	public Integer updateInsertDoor(DoorInfoModel doorinfo) {
		Integer obj=(Integer)this.getSqlMapClientTemplate().update("updateInsertDoor", doorinfo);
		if(obj==0){
			return 0;
		}
		return 1;
	}

	@Override
	public Integer updateDoorinfo(Integer first_open, String device_num,String door_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("first_open", first_open);
		map.put("device_num", device_num);
		map.put("door_num", door_num);
		Integer obj=(Integer)this.getSqlMapClientTemplate().update("updateDoorinfo", map);
		if(obj==0){
			return 0;
		}
		return 1;
	}

	@Override
	public List<DoorInfoModel> selectDoorInfo(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("selectDoorInfo", map);
	}
	
	@Override
	public Integer updatehaddenflag(Map<String, Object> map) {
		Integer obj=(Integer)this.getSqlMapClientTemplate().update("updatehaddenflag", map);
		if(obj==0){
			return 0;
		}
		return 1;
	}
    
	/*根据设备编号查询门信息*/
	@Override
	public List<DoorInfoModel> selectDoorInfoByDeviceNum(String device_num) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("device_num", device_num);
		return this.getSqlMapClientTemplate().queryForList("selectDoorInfoByDeviceNum", map);
	}

	
	//以下两个方法，是在修改门信息多卡开门卡号时，去查卡号
	@Override
	public List<Map> getCardsInfoDynamic(Map map) {
		return getSqlMapClientTemplate().queryForList("getCardsInfoDynamic", map);
	}

	@Override
	public Integer countCardsInfoDynamic(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("countCardsInfoDynamic", map);
	}

	@Override
	public List<Map> getStaffInfoByMultiCardnum(String multi_open_card_num) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("multi_open_card_num", multi_open_card_num);
		return getSqlMapClientTemplate().queryForList("getStaffInfoByMultiCardnum", map);
	}
	
	@Override
	public Map<String, Object> selectDeviceDoor(Map<String, Object> map){
		return (Map<String, Object>) getSqlMapClientTemplate().queryForObject("selectDeviceDoor", map);
	}
	
	
	
	@Override
	public boolean updateMonitor(Map<String, Object> map){
		return update("updateMonitor", map);
	}
	
	@Override
	public boolean updateNoMonitor(Map<String, Object> map){
		return update("updateNoMonitor", map);
	}
	
	@Override
	public List<DoorModel> getMonitorDoor(){
		return getSqlMapClientTemplate().queryForList("getMonitorDoor");
	}
	
	@Override
	public boolean updateMonitorList(List<Map<String, Object>> list){
		return update("updateMonitorList", list);
	}
	
	@Override
	public boolean updateNoMonitorList(List<Map<String, Object>> list){
		return update("updateNoMonitorList", list);
	}
	
	@Override
	public List<Map<String, Object>> getNoMonitorParam(Map<String, Object> map){
		return getSqlMapClientTemplate().queryForList("getNoMonitorParam", map);
	}
	
	@Override
	public Integer getNoMonitorCount(Map<String, Object> map){
		return (Integer) getSqlMapClientTemplate().queryForObject("getNoMonitorCount", map);
	}

	@Override
	public List<Map<String, Object>> getMonitor(Map<String, Object> map){
		return getSqlMapClientTemplate().queryForList("getMonitor", map);
	}
	
	@Override
	public boolean setNoMonitor(){
		return update("setNoMonitor", "");
	}
	
}
