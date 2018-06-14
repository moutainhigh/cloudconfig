
package com.kuangchi.sdd.ZigBeeConsole.device.dao.impl;




import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.ZigBeeConsole.device.dao.IZBdeviceDao;
import com.kuangchi.sdd.ZigBeeConsole.device.model.ZBdeviceModel;
import com.kuangchi.sdd.ZigBeeConsole.gateway.model.GatewayModel;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;

/**
 * 光子锁记录模块 - dao实现类
 * @author guibo.chen
 */
@Repository("zBdeviceDaoImpl")
public class ZBdeviceDaoImpl extends BaseDaoImpl<Object> implements IZBdeviceDao{

	@Override
	public String getNameSpace() {
		return "common.ZBdevice";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<ZBdeviceModel> getZBdeviceInfo(ZBdeviceModel device,String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("page", (page-1)*rows);
		map.put("rows", rows);
		map.put("room_num", device.getRoom_num());
		return this.getSqlMapClientTemplate().queryForList("getZBdeviceInfo", map);
	}

	@Override
	public Integer getZBdeviceInfoCount(ZBdeviceModel device) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("room_num", device.getRoom_num());
		return queryCount("getZBdeviceInfoCount",map);
	}

	@Override
	public List<GatewayModel> getZBgateway() {
		
		return this.getSqlMapClientTemplate().queryForList("getZBgateway");
	}

	@Override
	public Boolean addZbDeviceinfo(ZBdeviceModel device) {
		return insert("addZbDeviceinfo",device);
	}

	@Override
	public List<ZBdeviceModel> getZBdeviceByIp(String device_id) {
		return this.getSqlMapClientTemplate().queryForList("getZBdeviceByIp", device_id);
	}

	@Override
	public Boolean deleteZBdevice(String device_id) {
		return update("deleteZBdevice",device_id);
	}

	@Override
	public List<ZBdeviceModel> getZBdeviceByIpAndPort(ZBdeviceModel device) {
		return this.getSqlMapClientTemplate().queryForList("getZBdeviceByIpAndPort", device);
	}

	@Override
	public Boolean updateZbPanId(ZBdeviceModel device) {
		return update("updateZbPanId",device);
	}

	@Override
	public Boolean updateZbDeviceState(ZBdeviceModel device) {
		return update("updateZbDeviceState",device);
	}

	@Override
	public List<ZBdeviceModel> getZBgatewayByGateway(ZBdeviceModel device) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("gateway_port", device.getGateway_port());
		return this.getSqlMapClientTemplate().queryForList("getZBgatewayByGateway", map);
	}

	@Override
	public List<GatewayModel> getZBgatewayById(String gateway_id) {
		return this.getSqlMapClientTemplate().queryForList("getZBgatewayById", gateway_id);
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
	public List<ZBdeviceModel> getGatewayAndDevice(int gateway_id) {
		return this.getSqlMapClientTemplate().queryForList("getGatewayAndDevice", gateway_id);
	}

	@Override
	public Boolean updateZbNoState(ZBdeviceModel device) {
		return update("updateZbNoState",device);
	}
	
	@Override
	public Boolean setDevStateByID(ZBdeviceModel device) {
		return update("setDevStateByID",device);
	}

	
	@Override
	public boolean updateDeviceTime(Map<String, Object> map) {
		return update("updateZBTime",map);
	}

	@Override
	public Map<String, Object> getGatewayByZigbeeId(String device_id) {
		return (Map<String, Object>)getSqlMapClientTemplate().queryForObject("getGatewayByZigbeeId", device_id);
	}

	//更新小区号
	@Override
	public Boolean updateZbHouseNumber(ZBdeviceModel device) {
		return update("updateZbHouseNumber",device);
	}

	@Override
	public Map<String, Object> getGatewayByDeviceIDs(String device_id) {
		return (Map<String, Object>)getSqlMapClientTemplate().queryForObject("getGatewayByDeviceIDs", device_id);
	}

	@Override
	public Boolean updateZbDeviceInfos(ZBdeviceModel device) {
		return update("updateZbDeviceInfos",device);
	}

	
	
	
	/**
	 * 查询设备信号强度信息
	 * @author huixian.pan
	 * @return
	 */
	@Override
	public List<Map> getDeviceSignalRange(){
		return this.getSqlMapClientTemplate().queryForList("getDeviceSignalRange");
	}
	
	
	/**
	 * 更新设备信号强度设置
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	@Override
	public Boolean updDeviceSignalRange(Map map){
		return this.update("updDeviceSignalRange", map);
	}
	
	
	/**
	 * 查询失败设备信息
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	@Override
	public List<Map> getFailureZBdeviceInfo(Map map){
		 return this.getSqlMapClientTemplate().queryForList("getFailureZBdeviceInfo", map);
	}
	
	/**
	 * 查询失败设备信息条数
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	@Override
	public Integer getFailureZBdeviceInfoCount(Map map){
		return  (Integer) this.getSqlMapClientTemplate().queryForObject("getFailureZBdeviceInfoCount", map);
	}
	
}
