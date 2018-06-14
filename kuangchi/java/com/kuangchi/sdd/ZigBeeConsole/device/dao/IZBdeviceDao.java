package com.kuangchi.sdd.ZigBeeConsole.device.dao;



import java.util.List;
import java.util.Map;

import org.apache.fop.render.afp.modca.MapPageOverlay;

import com.kuangchi.sdd.ZigBeeConsole.device.model.ZBdeviceModel;
import com.kuangchi.sdd.ZigBeeConsole.gateway.model.GatewayModel;


/**
 * 光子锁记录 - dao
 * @author guibo.chen
 */
public interface IZBdeviceDao {
	
	public List<ZBdeviceModel> getZBdeviceInfo(ZBdeviceModel device,String Page, String size);//查询设备信息记录
	public Integer getZBdeviceInfoCount(ZBdeviceModel device);//总条数
	public List<GatewayModel> getZBgateway();//查询网关信息
	public List<GatewayModel> getZBgatewayById(String id);//根据id查询网关信息
	
	public Boolean addZbDeviceinfo(ZBdeviceModel device);//添加设备信息
	public List<ZBdeviceModel> getZBdeviceByIp(String device_id);//判断设备表中是否有该设备
	public Boolean deleteZBdevice(String device_id);//删除设备信息
	public List<ZBdeviceModel> getZBdeviceByIpAndPort(ZBdeviceModel device);//查询是否有重复的网关
	public Boolean updateZbPanId(ZBdeviceModel device);//根据id更新设备信息表在pan_id等参数
	public Boolean updateZbDeviceState(ZBdeviceModel device);//更新光子锁设备状态等参数
	public List<ZBdeviceModel> getZBgatewayByGateway(ZBdeviceModel device);//查询网关信息
	
	
	public List<ZBdeviceModel> getGatewayAndDevice(int gateway_id);//定时查询设备在线状态
	public Boolean updateZbNoState(ZBdeviceModel device);//更新光子锁设备不在线状态
	public Boolean setDevStateByID(ZBdeviceModel device);//根据设备ID更新光子锁设备不在线状态
	public Boolean updateZbHouseNumber(ZBdeviceModel device);//更新光子锁小区号
	
	/**
	 * 根据设备ID查询设备网关信息
	 * @author yuman.gao
	 */
	public Map<String, Object> getGatewayByDeviceId(String device_id);
	
	/**
	 * 查询设备信息
	 * @author yuman.gao
	 */
	public Map<String, Object> getDeviceInfoByDeviceId(String device_id);
	
	/**
	 * 根据ZigBeeID查询设备网关信息
	 * @author yuman.gao
	 */
	public Map<String, Object> getGatewayByZigbeeId(String device_id);

	/**
	 * 更新设备时间
	 * @author yuman.gao
	 */
	public boolean updateDeviceTime(Map<String, Object> map);
	
	
	public Map<String, Object> getGatewayByDeviceIDs(String device_id);//根据设备id查询通讯信息
	
	public Boolean updateZbDeviceInfos(ZBdeviceModel device);
	
	/**
	 * 查询设备信号强度信息
	 * @author huixian.pan
	 * @return
	 */
	public List<Map> getDeviceSignalRange();
	
	/**
	 * 更新设备信号强度设置
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	public Boolean updDeviceSignalRange(Map map);
	
	/**
	 * 查询失败设备信息
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	public List<Map> getFailureZBdeviceInfo(Map map);
	
	/**
	 * 查询失败设备信息条数
	 * @author huixian.pan
	 * @param map
	 * @return
	 */
	public Integer getFailureZBdeviceInfoCount(Map map);
}
