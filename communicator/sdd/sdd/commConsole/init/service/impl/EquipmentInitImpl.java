package com.kuangchi.sdd.commConsole.init.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.EquipmentData;
import com.kuangchi.sdd.comm.equipment.common.EquipmentDataForServer;
import com.kuangchi.sdd.comm.equipment.common.SendData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.gate.init.InitEquipmentConnector;
import com.kuangchi.sdd.comm.equipment.gate.reset.ResetConnector;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.commConsole.init.service.IEquipmentInit;
@Transactional
@Service("equipmentInitImpl")
public class EquipmentInitImpl implements IEquipmentInit{
	public static final Logger LOG = Logger.getLogger(EquipmentInitImpl.class);
	@Resource(name="getDevInfoService")
	GetDevInfoService getDevInfoService;
    @Override
    public String initSearchEquipment(String mac,String deviceType, EquipmentDataForServer data) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Integer.parseInt(deviceType));
		pkg.setMac(Util.getIntHex(mac));
		pkg.setOrder(0x02);
		pkg.setLength(0x001d);
		pkg.setOrderStatus(0x00);
		String result="";
		try {
		SendData sender = new SendData();
		setEquipment(data);
		LOG.info(setEquipment(data));
		sender.setEquipmentData(setEquipment(data));
		pkg.setData(sender);
		// 校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());
		DeviceInfo2 deviceInfo2=getDevInfoService.getManager(mac);

		result = com.kuangchi.sdd.comm.container.Service.service(
				"init", pkg,deviceInfo2);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		LOG.info("容器获取的值:" + result);
		return result;
    }
    
    private EquipmentData setEquipment(EquipmentDataForServer data){
	EquipmentData equipment = new EquipmentData();
		/**
		 * 子网掩码 255.255.255.0
		 */
		String maskIP = data.getMask();
		equipment.setMask(Util.unmaskIp2Collection(maskIP));
		/**
		 * 0xGGGGGGGG：网关参数。例如：0xC0A8FE01 代表：192. 168. 254. 1
		 */
		String gatewayIP = data.getGateway();
		equipment.setGateway(Util.unmaskIp2Collection(gatewayIP));
		/**
		 * 0xLLLLLLLL：M4本地IP地址
		 */
		String mechineIP = data.getMechineIP();
		equipment.setMechineIP(Util.unmaskIp2Collection(mechineIP));
		/**
		 * 0xRRRRRRRR：M4远程IP地址
		 */
		String remoteIP = data.getRemoteIP();
		equipment.setRemoteIP(Util.unmaskIp2Collection(remoteIP));
		/**
		 * BBBB：M4状态本地端口。心跳
		 */
		String mechineStatusPort = data.getMechineStatusPort();
		equipment.setMechineStatusPort(Util
				.translateParamToShortArrayForPort(mechineStatusPort));
		/**
		 * bbbb：M4状态远程端口。
		 */
		String remoteStatusPort = data.getRemoteStatusPort();
		equipment.setRemoteStatusPort(Util
				.translateParamToShortArrayForPort(remoteStatusPort));
		/**
		 * IIII：M4指令本地端口。服务器下发指令
		 */
		String mechineOrderPort = data.getMechineOrderPort();
		/*equipment.setMechineOrderPort(Util
				.translateParamToShortArrayForPort(mechineOrderPort));*/
		equipment.setMechineOrderPort(Util
		.translateParamToShortArrayForPort("18001"));
		/**
		 * iiii：M4指令远程端口。
		 */
		String remoteOrderPort = data.getRemoteOrderPort();
		equipment.setRemoteOrderPort(Util
				.translateParamToShortArrayForPort(remoteOrderPort));
		/**
		 * EEEE：M4事件本地端口。获取设备事件，如开门
		 */
		String mechineEventPort = data.getMechineEventPort();
		equipment.setMechineEventPort(Util
				.translateParamToShortArrayForPort(mechineEventPort));
		/**
		 * eeee：M4事件远程端口。
		 */
		String remoteEventPort = data.getRemoteEventPort();
		equipment.setRemoteEventPort(Util
				.translateParamToShortArrayForPort(remoteEventPort));
		/**
		 * 设置控制器标志 3字节或4字节
		 */
		equipment.setCardSign(Short.valueOf(data.getCardSign()));
		return equipment;
}
    
    
    //初始化设备信息
    public String reset(String mac,String deviceType) {
	Connector connector = new ResetConnector();
	SendHeader pkg = new SendHeader();
	pkg.setHeader(0x55);
	pkg.setSign(Integer.parseInt(deviceType));
	pkg.setMac(Util.getIntHex(mac));
	pkg.setOrder(0x10);
	pkg.setLength(0x0000);
	pkg.setOrderStatus(0x00);
	// 入参无有效数据
	// 校验值要求在配置有效数据后配置
	pkg.setCrc(pkg.getCrcFromSum());
	connector.setHeader(pkg);
	GetDevInfoService devInfoService=new GetDevInfoService();
    DeviceInfo2 deviceInfo2=devInfoService.getManager(mac);
	String result="";
	try {
		result = com.kuangchi.sdd.comm.container.Service.service("reset", pkg,deviceInfo2);
		LOG.info("容器获取的值:" + result);
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return result;
}
    

}
