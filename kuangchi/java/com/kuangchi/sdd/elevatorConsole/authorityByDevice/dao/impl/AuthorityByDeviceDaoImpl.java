package com.kuangchi.sdd.elevatorConsole.authorityByDevice.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.elevatorConsole.authorityByDevice.dao.AuthorityByDeviceDao;
@Repository("authorityByDeviceDao")
public class AuthorityByDeviceDaoImpl extends BaseDaoImpl<Map> implements AuthorityByDeviceDao {

	@Override
	public String getNameSpace() {
		return "elevatorConsole.authorityBydevice";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<Map> getAllTKDevices(Map map) {
		return getSqlMapClientTemplate().queryForList("getAllTKDevices", map);
	}

	@Override
	public Integer countAllTKDevices(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("countAllTKDevices", map);
	}

	@Override
	public List<Map> getStaffCardsBydeptNums(Map map) {
		return getSqlMapClientTemplate().queryForList("getStaffCardsBydeptNums", map);
	}

	@Override
	public Integer countStaffCardsBydeptNums(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("countStaffCardsBydeptNums", map);
	}

	@Override
	public List<Map> getFloorGroups() {
		return getSqlMapClientTemplate().queryForList("getFloorGroups");
	}

	@Override
	public boolean addDeviceAuth(Map map) {
		return this.insert("addDeviceAuth", map);
	}

	@Override
	public List<Map> getAuths(Map map) {
		return getSqlMapClientTemplate().queryForList("getAuths", map);
	}

	@Override
	public Integer countAuths(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("countAuths", map);
	}

	@Override
	public boolean deleteAuths(Map map) {
		return this.update("deleteAuths", map);
	}

	@Override
	public boolean preventRepAuth(Map map) {
		return this.delete("preventRepAuth", map);
	}

	@Override
	public List<Map> getTkDeviceInfo(Map map) {
		return this.getSqlMapClientTemplate().queryForList("getTkDeviceInfo1", map);
	}

	@Override
	public Integer getTkDeviceInfoCount(Map map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getTkDeviceInfoCount1", map);
	}

	@Override
	public List<Map> getTkAuthByDeviceNum(Map map) {
		return this.getSqlMapClientTemplate().queryForList("getTkAuthByDeviceNum", map);
	}

	@Override
	public List<String> getFloorNum2(String groupNum) {
		return  this.getSqlMapClientTemplate().queryForList("getFloorNum2", groupNum);
	}

	@Override
	public Map getTkDeviceIPByDeviceNum(String device_num) {
		return (Map) this.getSqlMapClientTemplate().queryForObject("getTkDeviceIPByDeviceNum2", device_num);
	}

	@Override
	public List<Map> getAuthsNoRepeat(Map map) {
		return getSqlMapClientTemplate().queryForList("getAuthsNoRepeat", map);
	}

	@Override
	public Integer countAuthsNoRepeat(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("countAuthsNoRepeat", map);
	}

}
