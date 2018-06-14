package com.kuangchi.sdd.elevatorConsole.departmentGrant.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.dao.IDepartmentGrantDao;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.model.DepartmentGrantModel;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.model.DeviceModel;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.model.FloorGroupInfoModel;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.dao.PeopleAuthorityManagerDao;

@Repository("departmentGrantDaoImpl")
public class DepartmentGrantDaoImpl extends BaseDaoImpl<DepartmentGrantModel> implements IDepartmentGrantDao {
	
	
	@Override
	public String getNameSpace() {
		return "common.DepartmentGrantModel";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<DepartmentGrantModel> getDepartmentGrantsByParam(String deptIds,String page,
			String rows) {
		Integer Page = Integer.parseInt(page);
		Integer Rows = Integer.parseInt(rows); 
		Map<String,Object> mapParam=new HashMap<String,Object>();
		mapParam.put("deptIds",deptIds );
		mapParam.put("page", (Page - 1) * Rows);
		mapParam.put("rows", Rows);
		return this.getSqlMapClientTemplate().queryForList("getDepartmentGrantsByParam", mapParam);
	}

	@Override
	public Integer getDepartmentGrantsCount(String deptIds) {
		Map<String,Object> mapParam=new HashMap<String,Object>();
		mapParam.put("deptIds",deptIds );
		return queryCount("getDepartmentGrantsCount",mapParam);
	}

	@Override
	public List<DeviceModel> getDevicesInfo(DeviceModel deviceModel,
			String page, String rows) {
		int Page = Integer.valueOf(page);
		int Rows = Integer.valueOf(rows);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", (Page - 1) * Rows);
		map.put("rows", Rows);
		map.put("device_num", deviceModel.getDevice_num());
		map.put("device_name", deviceModel.getDevice_name());
		map.put("device_ip", deviceModel.getDevice_ip());
		map.put("mac", deviceModel.getMac());
		return this.getSqlMapClientTemplate().queryForList("getDevicesInfo", map);
	}

	@Override
	public Integer getDevicesInfoCount(DeviceModel deviceModel) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("device_num", deviceModel.getDevice_num());
		map.put("device_name", deviceModel.getDevice_name());
		map.put("device_ip", deviceModel.getDevice_ip());
		map.put("mac", deviceModel.getMac());
		return queryCount("getDevicesInfoCount",map);
	}

	@Override
	public List<FloorGroupInfoModel> getFloorGroupInfoForSelect() {
		return this.getSqlMapClientTemplate().queryForList("getFloorGroupInfoForSelect");
	}

	@Override
	public List<Map> selectCardNumByObjectNum(String object_num) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("dept_nums", object_num);
		return getSqlMapClientTemplate().queryForList("selectCardNumByObjectNum", map);
	}
	
	@Override
	public boolean selectCardAuthInDept(String object_nums) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("dept_nums", object_nums);
		if(queryCount("selectCardAuthInDept",map)>0){
			return true;
		}else{
			
			return false;
		}
	}

	@Override
	public boolean addDeptGrant(DepartmentGrantModel departmentGrantModel) {
		return this.insert("addDeptGrant", departmentGrantModel);
	}
	
	/*新写的新增组织权限方法   by  huixian.pan*/
	public boolean addDeptGrant2(DepartmentGrantModel departmentGrantModel){
		return this.insert("addDeptGrant2", departmentGrantModel);
	}

	@Override
	public boolean delDeptGrant(DepartmentGrantModel departmentGrantModel) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("card_num", departmentGrantModel.getCard_num());
		map.put("card_type", departmentGrantModel.getCard_type());
		map.put("object_num", departmentGrantModel.getObject_num());
		map.put("device_num", departmentGrantModel.getDevice_num());
		map.put("floor_list", departmentGrantModel.getFloor_list());
		map.put("begin_time", departmentGrantModel.getBegin_valid_time());
		map.put("end_time", departmentGrantModel.getEnd_valid_time());
		return delete("delDeptGrant",map);
	}

	

	@Override
	public List<Map<String, String>> selectFloorNumByFloorGroupNum(
			String floor_group_num) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("floor_group_num", floor_group_num);
		return getSqlMapClientTemplate().queryForList("selectFloorNumByFloorGroupNum", map);
	}

	@Override
	public List<Map> selectGrantByDeptNum(String deptNum,String deviceNum) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("deptNum", deptNum);
		map.put("deviceNum", deviceNum);
		return getSqlMapClientTemplate().queryForList("selectGrantInfoByDeptNum", map);
	}

	@Override
	public DeviceModel selectInfoByDeviceNum(String device_num) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("device_num", device_num);
		return (DeviceModel) getSqlMapClientTemplate().queryForObject("selectInfoByDeviceNum", map);
	}

	@Override
	public List<Map> selectDeptAuthorityInfoList(Map map) {
		return this.getSqlMapClientTemplate().queryForList("selectDeptAuthorityInfoList", map);
	}

	@Override
	public int selectDeptAuthorityInfoCount(Map map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("selectDeptAuthorityInfoCount", map);
	}

	@Override
	public List<Map> selectStaffAuthorityInfoList(Map map) {
		return this.getSqlMapClientTemplate().queryForList("selectStaffAuthorityInfoList", map);
	}

	@Override
	public int selectStaffAuthorityInfoCount(Map map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("selectStaffAuthorityInfoCount", map);
	}

	@Override
	public List<Map> selectDeptAuthorityForViewList(Map map) {
		return this.getSqlMapClientTemplate().queryForList("selectDeptAuthorityForViewList", map);
	}

	@Override
	public int selectDeptAuthorityForViewCount(Map map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("selectDeptAuthorityForViewCount", map);
	}

	@Override
	public List<Map> selectStaffAuthorityForViewList(Map map) {
		return this.getSqlMapClientTemplate().queryForList("selectStaffAuthorityForViewList", map);
	}

	@Override
	public int selectStaffAuthorityForViewCount(Map map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("selectStaffAuthorityForViewCount", map);
	}

	@Override
	public List<Map> searchStaffAuthority(Map map) {
		return this.getSqlMapClientTemplate().queryForList("searchStaffAuthority", map);
	}

	@Override
	public List<Map> searchDeptAuthority(Map map) {
		return this.getSqlMapClientTemplate().queryForList("searchDeptAuthority", map);
	}

	

	@Override
	public List<Map> selectDeptAuthorityFromAuthority(
			String objectNum,String deviceNum) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("object_num", objectNum.substring(1, objectNum.length()-1));
		map.put("device_num", deviceNum.substring(1, deviceNum.length()-1));
		return this.getSqlMapClientTemplate().queryForList("selectDeptAuthorityFromAuthority", map);
	}

	@Override
	public boolean removeDeptGrant(Map<String,String> map) {
		if(getSqlMapClientTemplate().insert("addDeptAuthorityForRemove", map)!=null){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public boolean addToAuthorityTable(DepartmentGrantModel departmentGrantModel) {
		if(getSqlMapClientTemplate().insert("addToAuthorityTable", departmentGrantModel)!=null){
			return false;
		}else{
			return true;
		}
	}

	@Override
	public String selectCardTypeByNum(String cardNum) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("card_num", cardNum);
		return (String) getSqlMapClientTemplate().queryForObject("selectCardTypeByNum", map);
	}

	@Override
	public String selectBmdmByStaffNum(String staffNum) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("staff_num", staffNum);
		return (String) getSqlMapClientTemplate().queryForObject("selectBmdmByStaffNum", map);
	}

	@Override
	public Map selectDeptAuthFromAuthority(String deptno) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("dept_no", deptno);
		return (Map) this.getSqlMapClientTemplate().queryForObject("selectDeptAuthorityFromAuthority", map);
	}

	@Override
	public void delTkAuthDirect(Map map) {
		getSqlMapClientTemplate().delete("delTkAuthDirect", map);
	}

	
	

	

}
