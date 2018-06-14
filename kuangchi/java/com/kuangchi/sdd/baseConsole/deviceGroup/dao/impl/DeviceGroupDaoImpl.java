package com.kuangchi.sdd.baseConsole.deviceGroup.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.deviceGroup.dao.DeviceGroupDao;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceGroupModel;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceTimeGroup;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DeviceModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DoorModel;

@Repository("deviceGroupDaoImpl")
public class DeviceGroupDaoImpl extends BaseDaoImpl<Object> implements DeviceGroupDao {

	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DeviceModel> getAllDeviceModel() {
		return getSqlMapClientTemplate().queryForList("getAllDeviceModel");
	}
	
	@Override
	public List<Map> getAllDevInfo() {
		return getSqlMapClientTemplate().queryForList("getAllDevInfo");
	}
	
	@Override
	public List<DeviceModel> getAllDeviceModelA(List<DeviceGroupModel> list) {
		Map map=new HashMap();
		map.put("devDorList", list);
		return getSqlMapClientTemplate().queryForList("getAllDeviceModelA",map);
	}
	
	@Override
	public List<DoorModel> getDoorList() {
		return getSqlMapClientTemplate().queryForList("getDoorModel");
	}
	
	@Override
	public List<DoorModel> getDoorListA(String deviceNum) {
		Map map=new HashMap();
		map.put("deviceNum", deviceNum);
//		map.put("deviceNum", "\'"+deviceNum+"\'");
		return getSqlMapClientTemplate().queryForList("getDoorModelA",map);
	}
	@Override
	public List<DeviceGroupModel> getAllDeviceGroup() {
		return getSqlMapClientTemplate().queryForList("getAllDeviceGroup");
	}
	
	@Override
	public List<DeviceGroupModel> getAllDeviceGroupById(String id) {
		return getSqlMapClientTemplate().queryForList("getAllDeviceGroupById",id);
	}
	
	@Override
	public List<DeviceGroupModel> getAllDeviceGroupByIdA(String id) {
		return getSqlMapClientTemplate().queryForList("getAllDeviceGroupByIdA",id);
	}
    
	/*查询全部设备组*/
	@Override
	public List<DeviceGroupModel> searchDeviceGroup(
			String groupName, Integer skip, Integer rows) {
		Map<String,Object> map=new HashMap<String,Object>();
		//map.put("groupNum", groupNum);
		map.put("groupName", groupName);
		map.put("skip", skip);
		map.put("rows", rows);
		return getSqlMapClientTemplate().queryForList("searchDeviceGroup",map);
	}

	/*查询全部设备组列表数量*/
	@Override
	public Integer searchDeviceGroupCount(String groupName) {
		Map<String,Object> map=new HashMap<String,Object>();
		//map.put("groupNum", groupNum);
		map.put("groupName", groupName);
		return (Integer)getSqlMapClientTemplate().queryForObject("searchDeviceGroupCount",map);
	}

	/*查询全部设备组设备*/
	@Override
	public List<DeviceInfo> searchDeviceInfo(
			String device_name, Integer skip, Integer rows) {
		Map<String,Object> map=new HashMap<String,Object>();
		//map.put("device_num", device_num);
		map.put("device_name", device_name);
		map.put("skip", skip);
		map.put("rows", rows);
		return getSqlMapClientTemplate().queryForList("searchDeviceInfo",map);
	}
	
	/*查询全部设备组设备数量*/
	@Override
	public Integer searchDeviceInfoCount(String device_name) {
		Map<String,Object> map=new HashMap<String,Object>();
		//map.put("device_num", device_num);
		map.put("device_name", device_name);
		return (Integer)getSqlMapClientTemplate().queryForObject("searchDeviceInfoCount",map);
	}
	
	/*通过设备id查询设备组设备信息*/
	@Override
	public DeviceInfo searchDeviceInfoById(String device_id) {
		return (DeviceInfo) getSqlMapClientTemplate().queryForObject("searchDeviceInfoById",device_id);
	}
	
	/*通过设备编号查询子设备*/
	@Override
	public List<DeviceInfo> searchDeviceInfoByNum(String device_num,Integer skip,Integer rows) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("device_num", device_num);
		map.put("skip", skip);
		map.put("rows", rows);
		return getSqlMapClientTemplate().queryForList("searchDeviceInfoByNum", map) ;
	}
	
	/*通过设备编号查询子设备行数*/
	@Override
	public Integer searchDeviceInfoCountByNum(String device_num) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("device_num", device_num);
		return (Integer) getSqlMapClientTemplate().queryForObject("searchDeviceInfoCountByNum", map);
	}
	
	/*通过设备编号查询子设备组*/
	@Override
	public List<DeviceGroupModel> searchDeviceGroupByNum(DeviceGroupModel bgm) {
		return getSqlMapClientTemplate().queryForList("searchDeviceGroupByNum", bgm) ;
	}
	
	
	/*通过设备编号查询子设备组行数*/
	@Override
	public Integer searchDeviceGroupCountByNum(DeviceGroupModel bgm) {
		return (Integer) getSqlMapClientTemplate().queryForObject("searchDeviceGroupCountByNum", bgm);
	}

	/*通过设备编号查询单个设备组到form表单*/
	@Override
	public DeviceGroupModel searchOnlyDeviceGroupByNum(String groupNum) {
		return (DeviceGroupModel) getSqlMapClientTemplate().queryForObject("searchOnlyDeviceGroup",groupNum);
	}
	
	/*通过设备编号查询单个设备到datagrid*/
	@Override
	public List<DeviceGroupModel> searchOnlyDeviceGroup(DeviceGroupModel bgm) {
		return getSqlMapClientTemplate().queryForList("searchOnlyDeviceGroup", bgm);
	}
    /*修改设备组*/
	@Override
	public boolean editDeviceGroup(String groupName,
			String description, String groupNum) {
		 Map<String,Object> map=new HashMap<String,Object>();
		 //map.put("groupNum", groupNum);
		 map.put("groupName", groupName);
		 map.put("description", description);
		 map.put("groupNum", groupNum);
         return update("updateDeviceGroup", map);		
	}
     /*修改设备*/  
	@Override
	public void editDeviceInfo(String device_name, String description,
			String device_num) {
		 Map<String,Object> map=new HashMap<String,Object>();
		 //map.put("groupNum", groupNum);
		 map.put("device_name", device_name);
		 map.put("description", description);
		 map.put("device_num", device_num);
         getSqlMapClientTemplate().update("updateDeviceInfo", map);
	}
	
	/*修改设备组的父设备组编号*/
	@Override
	public void changeParentGroupNum(String groupNum, String parentGroupNum) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("groupNum", groupNum);
		map.put("parentGroupNum", parentGroupNum);
		getSqlMapClientTemplate().update("changeParentGroupNum", map);
	}
	
	/*修改设备的父设备编号*/
	@Override
	public void changeDeviceGroupNum(String device_num, String device_group_num) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("device_num", device_num);
		map.put("device_group_num", device_group_num);
		getSqlMapClientTemplate().update("changeDeviceGroupNum", map);
		
	}
	
    
	/*增加设备组*/
	@Override
	public void addDeviceGroup(String groupName,
			String parentGroupNum, String createUser, String createTime,
			String description) {
		 Map<String,Object> map=new HashMap<String,Object>();
		 map.put("groupName", groupName);
		 map.put("parentGroupNum", parentGroupNum);
		 map.put("createUser", createUser);
		 map.put("createTime", createTime);
		 map.put("description", description);
		 getSqlMapClientTemplate().insert("addDeviceGroup", map);
	}
	
	/*删除设备组*/
	@Override
	public boolean delDeviceGroup(String groupNum) {
		return update("delDeviceGroup",groupNum);
	}
	
	/**
	 * 修改设备时间组
	 * @return 
	 */
	@Override
	public boolean modifyDeviceTimeGroup(DeviceTimeGroup deviceTimeGroup) {
		return	update("modifyDeviceTimeGroup", deviceTimeGroup);
		
	}
	@Override
	public List<DeviceTimeGroup> getDeviceTimeGroupById(String device_num) {
		return this.getSqlMapClientTemplate().queryForList("getDeviceTimeGroupById",device_num);
	}
	@Override
	public List<DeviceGroupModel> getDeviceGroup(String flag) {
		return this.getSqlMapClientTemplate().queryForList("getDeviceGroup", flag);
	}

	@Override
	public List<String> selDeviceInGroup(String device_group_num) {
		return getSqlMapClientTemplate().queryForList("selDeviceInGroup",
				device_group_num);
	}
}
