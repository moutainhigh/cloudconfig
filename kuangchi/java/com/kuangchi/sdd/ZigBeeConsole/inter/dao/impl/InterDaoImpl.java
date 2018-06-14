package com.kuangchi.sdd.ZigBeeConsole.inter.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.ZigBeeConsole.inter.dao.IInterDao;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;

/**
 * 光子锁对外接口 - dao实现类
 * @author yuman.gao
 */
@Repository("ZigBeeInterDaoImpl")
public class InterDaoImpl extends BaseDaoImpl<Object> implements IInterDao{

	@Override
	public String getNameSpace() {
		return "common.ZigBeeInter";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public boolean updateElectricity(Map<String, Object> map) {
		return this.update("updateElectricity", map);
	}

	@Override
	public boolean addRecord(Map<String, Object> map) {
		return this.insert("addZigBeeRecord", map);
	}

	@Override
	public Integer getDeviceByPas(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("getDeviceByPas", map);
	}

	
	@Override
	public String getStaffNamebyStaffNum(String staff_num) {
		return (String) this.getSqlMapClientTemplate().queryForObject("getNameByCard", staff_num);
	}
	
	@Override
	public Map<String, Object> getGatewayByDeviceId(String device_id) {
		return (Map) this.getSqlMapClientTemplate().queryForObject("getGatewayByDeviceId", device_id);
	}

	@Override
	public Map<String, Object> getDeviceInfoByDeviceId(String device_id) {
		return (Map) this.getSqlMapClientTemplate().queryForObject("getDeviceInfoByDeviceId", device_id);
	}

	@Override
	public boolean addUserByLightKey(Map<String, Object> map) {
		return this.insert("addUserByLightKey", map);
	}

	@Override
	public boolean updateUserByLightKey(Map<String, Object> map) {
		return this.update("updateUserByLightKey", map);
	}

	@Override
	public boolean freeUserByLightKey(Map<String, Object> map) {
		return this.update("freeUserByLightKey", map);
	}
	
	@Override
	public boolean unfreeUserByLightKey(Map<String, Object> map) {
		return this.update("unfreeUserByLightKey", map);
	}

	@Override
	public void deleteLightKeyUser() {
		this.getSqlMapClientTemplate().delete("deleteLightKeyUser");
	}

	@Override
	public boolean addCardByLightKey(Map<String, Object> map) {
		return this.insert("addCardByLightKey", map);
	}
	
	@Override
	public boolean addBoundCardMap(Map<String, Object> map) {
		return this.insert("addBoundCardMap", map);
	}

	@Override
	public void deleteLightKeyCard() {
		this.getSqlMapClientTemplate().delete("deleteLightKeyCard");
	}

	@Override
	public boolean updateCardByLightKey(Map map) {
		return this.update("updateCardByLightKey", map);
	}

	@Override
	public boolean freeCardByLightKey(Map<String, Object> map) {
		return this.update("freeCardByLightKey", map);
	}

	@Override
	public boolean unfreeCardByLightKey(Map<String, Object> map) {
		return this.update("unfreeCardByLightKey", map);
	}

	@Override
	public List<Map> getStaffByRemoteId(String remote_staff_id) {
		return this.getSqlMapClientTemplate().queryForList("getStaffByRemoteId", remote_staff_id);
	}

	@Override
	public boolean delUserByRemoteId(String remote_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("remote_id", remote_id);
		return this.delete("delUserByRemoteId", map);
	}
	
	@Override
	public boolean removeLightCard(String card_nums) {
		return this.delete("removeLightCard", card_nums);
	}

	@Override
	public List<String> getCardByRemoteId(String remote_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("remote_id", remote_id);
		return this.getSqlMapClientTemplate().queryForList("getDelCardByRemoteId", map);
	}

	@Override
	public boolean updateBoundCardByLightKey(Map<String, Object> map) {
		return this.update("updateBoundCardByLightKey", map);
	}

	@Override
	public boolean removeBoundCard(String card_nums) {
		return this.delete("removeBoundCard", card_nums);
	}


	
}
