package com.kuangchi.sdd.ZigBeeConsole.device.service;



import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.ZigBeeConsole.device.model.ZBdeviceModel;
import com.kuangchi.sdd.ZigBeeConsole.gateway.model.GatewayModel;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.sun.mail.iap.ConnectionException;


/**
 * 光子锁记录 - service
 * @author guibo.chen
 */
public interface IZBdeviceService {

	public Grid getRecordByParamPage(ZBdeviceModel device,String Page, String size);//查询设备信息记录
	//public List<ZBdeviceModel> getZBgateway();//搜索设备
	public List<ZBdeviceModel> getZBgateway(String id);//搜索设备
	public Boolean deleteZBdevice(String device_id);//删除设备信息
	public List<ZBdeviceModel> getZBdeviceByIpAndPort(ZBdeviceModel device);//查询是否有重复的网关
	public Boolean updateZbPanId(ZBdeviceModel device);//根据id更新设备信息表在pan_id
	public List<ZBdeviceModel> getZBgatewayByGateway(ZBdeviceModel device);//查询网关信息
	//public List<ZBdeviceModel> getZBgatewayById(String id);//根据id查询网关信息
	public List<GatewayModel> getZBgateways();//查询网关信息
	public List<ZBdeviceModel> getGatewayAndDevice(int gateway_id);//定时查询设备在线状态
	public Boolean updateZbDeviceInfos(ZBdeviceModel device);//定时更新设备状态
	public Boolean updateZbNoState(ZBdeviceModel device);//更新光子锁设备不在线状态
	public Boolean setDevStateByID(ZBdeviceModel device);//更新光子锁设备不在线状态
	public Boolean setHouseNumber(ZBdeviceModel device);
	
	public List<ZBdeviceModel> getZBdeviceByIp(String device_id);//根据设备id查询信息
	// 设备校时	by yuman.gao
	public boolean setSystemTime(String device_id);
	

	// 更新密钥 	by yuman.gao
	public boolean setPassword(String device_id, String password);
	
	
	// 查询设备信息	by yuman.gao
	public Map<String, Object> getDeviceInfoByDeviceId(String device_id);

	// 查询设备当前时间		by yuman.gao
	public String getDeviceTime(String device_id);
	
	public Boolean reloadDevice(String device_id);//点击刷新
	
	/**
	 * 查询设备信号强度信息
	 * @author huixian.pan
	 * @return
	 */
	public List<Map> getDeviceSignalRange();
	
	
	/**
	 * 更新设备信号强度设置
	 * @author huixian.pan
	 * @param list
	 * @return
	 */
	public Boolean updDeviceSignalRange(List<Map> list);
	
	/**
	 * 查询失败设备信息
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	public Grid getFailureZBdeviceInfo(Map map);
	
}
