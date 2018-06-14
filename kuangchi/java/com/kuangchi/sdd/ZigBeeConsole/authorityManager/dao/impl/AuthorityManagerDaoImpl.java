package com.kuangchi.sdd.ZigBeeConsole.authorityManager.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.ZigBeeConsole.authorityManager.dao.AuthorityManagerDao;
import com.kuangchi.sdd.ZigBeeConsole.authorityManager.model.AuthorityManagerModel;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;

/**
 * 光子锁权限管理 - dao实现类
 * @author chudan.guo
 */
@Repository("authorityManagerDaoImpl")
public class AuthorityManagerDaoImpl extends BaseDaoImpl<Object> implements AuthorityManagerDao{

	@Override
	public String getNameSpace() {
		return "common.AuthorityManager";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<Map> getAllStaffToZigBee(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getAllStaffToZigBee", map);
	}
	@Override
	public Integer getAllStaffToZigBeeCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getAllStaffToZigBeeCount", map);
	}

	@Override
	public List<Map> getZigBeeDevice(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getZigBeeDevice", map);
	}

	@Override
	public Integer getZigBeeDeviceCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getZigBeeDeviceCount", map);
	}

//	@Override
//	public boolean addPeopleAuthority(AuthorityManagerModel model) {
//		return this.insert("insertPeopleAuthority", model);
//	}
	
	@Override
	public boolean addPeopleAuthority(List<AuthorityManagerModel> authList) {
		return this.insert("insertPeopleAuthority", authList);
	}

	
	
//	@Override
//	public boolean insertZigbeeTask(AuthorityManagerModel model) {
//		return this.insert("insertZigbeeTask", model);
//	}
	
	
	@Override
	public boolean insertZigbeeTask(List<AuthorityManagerModel> taskList) {
		return this.insert("insertZigbeeTask", taskList);
	}
	
	
	@Override
	public boolean insertDeleteTask(List<AuthorityManagerModel> taskList) {
		return this.insert("insertDeleteTask", taskList);
	}

	@Override
	public boolean removeTask(Map<String, Object> map){
		return this.delete("removeTask", map);
	}
	
	@Override
	public boolean insertHisTask(Map<String, Object> map){
		return this.insert("insertHisTask", map);
	}
	
	@Override
	public Integer getTaskTryTimes(Map<String, Object> map){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("getTaskTryTimes", map);
	}
	 
	@Override
	public List<Map> getZBTasks(int taskCount){
		return this.getSqlMapClientTemplate().queryForList("getZBTasks",taskCount);
	}
	
	@Override
	public List<Map> getDelTasks(){
		return this.getSqlMapClientTemplate().queryForList("getDelTasks");
	}
	
	@Override
	public List<AuthorityManagerModel> getAuthorityByCards(Map<String, Object> map){
		return this.getSqlMapClientTemplate().queryForList("getAuthorityByCards", map);
	}
	
	
	@Override
	public Integer selectAuthorityCount(String device_id,String light_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("device_id", device_id);
		map.put("light_id", light_id);
		return (Integer) getSqlMapClientTemplate().queryForObject("selectAuthorityCount",map);
	}

	@Override
	public List<Map> getPeopleAuthority(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getPeopleAuthority", map);
	}

	@Override
	public Integer getPeopleAuthorityCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getPeopleAuthorityCount", map);
	}

	@Override
	public Map getZigbeeIpMap(String device_id) {
		return  (Map) this.getSqlMapClientTemplate().queryForObject("getZigbeeIpMap", device_id);
	}

	@Override
	public void updateAuthorityState(Map map) {
		this.update("updateAuthorityState", map);
		
	}
	
	@Override
	public void updateSomeAuthorityState(Map map) {
		this.update("updateSomeAuthorityState", map);
		
	}

	@Override
	public List<AuthorityManagerModel> getExpireAuthority(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getExpireAuthority", map);
	}

	@Override
	public void updateTask(Map<String, Object> map) {
		update("updateTask", map);
		
	}

	@Override
	public boolean removeOldAuthority(Map<String, Object> map) {
		return this.update("removeOldAuthority", map);
	}

	@Override
	public List<AuthorityManagerModel> getWaitAuthorityByCards(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getWaitAuthorityByCards", map);
	}
	@Override
	public boolean autoIssuedAuthority(Map<String, Object> map) {
		return this.insert("autoIssuedAuthority", map);
		
	}

	@Override
	public void deleAutoIssuedAuthority() {
		delete("deleAutoIssuedAuthority",null);
		
	}

	@Override
	public List<Map> getAutoIssuedAuthority() {
		return this.getSqlMapClientTemplate().queryForList("getAutoIssuedAuthority");
	}

	/*@Override
	public boolean updateAutoIssuedAuthority(String device_id) {
		return this.update("updateAutoIssuedAuthority", device_id);
	}
	*/

	@Override
	public List<Map> getAutoTask() {
		return this.getSqlMapClientTemplate().queryForList("getAutoTask");
	}
	
	@Override
	public Map<String, Object> getCardByRemoteId(String remote_staff_id) {
		return (Map) this.getSqlMapClientTemplate().queryForObject("getCardByRemoteId",remote_staff_id);
	}

	@Override
	public boolean removeAllAuthByCard(Map<String, Object> map) {
		return this.update("removeAllAuthByCard", map);
	}

	@Override
	public List<Map> getCompany() {
		return this.getSqlMapClientTemplate().queryForList("getCompany");
	}

	@Override
	public List<Map> getTimerCompany() {
		return this.getSqlMapClientTemplate().queryForList("getTimerCompany");
	}

	@Override
	public boolean setTimerObject(String company_num) {
		return this.update("setTimerObject", company_num);
	}

	@Override
	public boolean setNotTimerObject(String company_num) {
		return this.update("setNotTimerObject", company_num);
	}

	@Override
	public boolean removeCompany() {
		return this.delete("removeCompany", null);
	}

	@Override
	public boolean addCompany(Map<String, Object> map) {
		return this.insert("addCompany", map);
	}

}
