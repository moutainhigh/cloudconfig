package com.kuangchi.sdd.ZigBeeConsole.gateway.dao.impl;




import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.ZigBeeConsole.gateway.dao.IGatewayDao;
import com.kuangchi.sdd.ZigBeeConsole.gateway.model.GatewayModel;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;

/**
 * 光子锁记录模块 - dao实现类
 * @author guibo.chen
 */
@Repository("gatewayDaoImpl")
public class GatewayDaoImpl extends BaseDaoImpl<Object> implements IGatewayDao{

	@Override
	public String getNameSpace() {
		return "common.gateway";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<GatewayModel> getGatewayInfo(GatewayModel device,String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("page", (page-1)*rows);
		map.put("rows", rows);
		map.put("remote_ip", device.getRemote_ip());
		map.put("gateway", device.getGateway());
		return this.getSqlMapClientTemplate().queryForList("getGatewayInfo", map);
	}

	@Override
	public Integer getGatewayInfoCount(GatewayModel device) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("remote_ip", device.getRemote_ip());
		map.put("gateway", device.getGateway());
		return queryCount("getGatewayInfoCount",map);
	}

	//添加网关
	@Override
	public Boolean addGatewayinfo(GatewayModel device) {
		return insert("addGatewayinfo",device);
	}

	@Override
	public List<GatewayModel> getGatewayByIpAndPort(GatewayModel device) {
		return this.getSqlMapClientTemplate().queryForList("getGatewayByIpAndPort", device);
	}

	@Override
	public Boolean getPanIdInfo(GatewayModel device) {
		return update("getPanIdInfo",device);
	}

	@Override
	public List<GatewayModel> getGatewayById(String id) {
		return this.getSqlMapClientTemplate().queryForList("getGatewayById", id);
	}

	@Override
	public Boolean getStateOnline(GatewayModel device) {
		return update("getStateOnline",device);
	}

	@Override
	public List<GatewayModel> getGatewayByGatewayId(String gateway_id) {
		return this.getSqlMapClientTemplate().queryForList("getGatewayByGatewayId", gateway_id);
	}

	@Override
	public Boolean deleteGatewayInfo(String gateway_id) {
		return update("deleteGatewayInfo",gateway_id);
	}

	@Override
	public Boolean updateGatewayInfo(GatewayModel device) {
		return update("updateGatewayInfo",device);
	}

	@Override
	public Boolean getNoStateOnline(GatewayModel device) {
		return update("getNoStateOnline",device);
	}

	@Override
	public Map<String, Object> getGatewayPanidByZigbeeId(int gateway_id) {
		return (Map<String, Object>)getSqlMapClientTemplate().queryForObject("getGatewayPanidByZigbeeId", gateway_id);
	}

	@Override
	public Boolean updateGatewayPanIdByZigbeeId(GatewayModel device) {
		return update("updateGatewayPanIdByZigbeeId",device);
	}

	@Override
	public List<GatewayModel> getGatewaypanId(String gateway_pan_id) {
		return this.getSqlMapClientTemplate().queryForList("getGatewaypanId", gateway_pan_id);
	}

	@Override
	public boolean addPortParam(Map map) {
		return insert("addPortParam", map);
	}

	@Override
	public boolean updatePortParam(Map map) {
		return update("updatePortParam", map);
	}

	@Override
	public String getParamValueByKey(String sys_key) {
		return (String)this.getSqlMapClientTemplate().queryForObject("getParamValueByKey", sys_key);
	}

}
