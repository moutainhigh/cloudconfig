package com.kuangchi.sdd.ZigBeeConsole.gateway.service;




import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.ZigBeeConsole.gateway.model.GatewayModel;
import com.kuangchi.sdd.base.model.easyui.Grid;


/**
 * 光子锁记录 - service
 * @author guibo.chen
 */
public interface IGatewayService {

	public Grid getGatewayInfo(GatewayModel device,String Page, String size);//查询设备信息记录
	public String addGatewayinfo(GatewayModel device);
	public List<GatewayModel> getGatewayByIpAndPort(GatewayModel device);//查询是否有重复
	public Boolean getPanIdInfo(GatewayModel device);//更新芯片网关id
	public List<GatewayModel> getGatewayById(String id);//根据id查询
	public Boolean NewaddGatewayinfo(GatewayModel device);//当点击确定的时候请求
	public List<GatewayModel> getGatewayByGatewayId(String gateway_id);//根据id查询网关、设备信息表中是否有数据
	public Boolean deleteGatewayInfo(String gateway_id);//伪删除
	public Boolean updateGatewayInfo(GatewayModel device);//修改网关
	public Boolean getStateOnline(GatewayModel device);//定时更新网关在线状态
	public Boolean getNoStateOnline(GatewayModel device);//定时更新网关不在线状态
	public Boolean updateGatewayPanIdByDeviceId(GatewayModel device);//设置panid
	public List<GatewayModel> getGatewaypanId(String gateway_pan_id);//查询panid是否存在
	
	
	public boolean setPortParam(Map map);  // 设置网关命令端口和上报端口
	
	public String getParamValueByKey(String key);  // 根据key查询参数Value
}
