package com.kuangchi.sdd.ZigBeeConsole.gateway.dao;



import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.ZigBeeConsole.gateway.model.GatewayModel;



/**
 * 光子锁记录 - dao
 * @author guibo.chen
 */
public interface IGatewayDao {
	
	public List<GatewayModel> getGatewayInfo(GatewayModel device,String Page, String size);//查询设备信息记录
	public Integer getGatewayInfoCount(GatewayModel device);//总条数
	public Boolean addGatewayinfo(GatewayModel device);
	public List<GatewayModel> getGatewayByIpAndPort(GatewayModel device);//查询是否有重复
	public Boolean getPanIdInfo(GatewayModel device);//更新芯片网关id
	public List<GatewayModel> getGatewayById(String id);//根据id查询
	public Boolean getStateOnline(GatewayModel device);//定时更新网关在线状态
	public List<GatewayModel> getGatewayByGatewayId(String gateway_id);//根据id查询网关、设备信息表中是否有数据
	public Boolean deleteGatewayInfo(String gateway_id);//伪删除
	public Boolean updateGatewayInfo(GatewayModel device);//修改网关
	public Boolean getNoStateOnline(GatewayModel device);//定时更新网关不在线状态
	
	public Boolean updateGatewayPanIdByZigbeeId(GatewayModel device);//设置panid
	public Map<String, Object> getGatewayPanidByZigbeeId(int gateway_id);//查询通讯设备信息
	
	public List<GatewayModel> getGatewaypanId(String gateway_pan_id);//查询panid是否存在
	
	
	public boolean addPortParam(Map map);
	public boolean updatePortParam(Map map);
	
	public String getParamValueByKey(String key);
}
