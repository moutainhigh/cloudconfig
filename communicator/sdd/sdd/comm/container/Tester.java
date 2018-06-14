package com.kuangchi.sdd.comm.container;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


import com.kuangchi.sdd.comm.equipment.base.Connector;
import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.EquipmentData;
import com.kuangchi.sdd.comm.equipment.common.EquipmentDataForServer;
import com.kuangchi.sdd.comm.equipment.common.GateLimitData;
import com.kuangchi.sdd.comm.equipment.common.GateParamData;

import com.kuangchi.sdd.comm.equipment.common.ClearData;
import com.kuangchi.sdd.comm.equipment.common.DeviceTimeBlock;
import com.kuangchi.sdd.comm.equipment.common.DeviceTimeData;
import com.kuangchi.sdd.comm.equipment.common.Down;
import com.kuangchi.sdd.comm.equipment.common.Over;
import com.kuangchi.sdd.comm.equipment.common.PackageDown;
import com.kuangchi.sdd.comm.equipment.common.RemoteOpenDoor;
import com.kuangchi.sdd.comm.equipment.common.RespondRecord;
import com.kuangchi.sdd.comm.equipment.common.SendData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeBlock;
import com.kuangchi.sdd.comm.equipment.common.TimeData;
import com.kuangchi.sdd.comm.equipment.common.TimeForbid;
import com.kuangchi.sdd.comm.equipment.common.TimeGroupData;
import com.kuangchi.sdd.comm.equipment.common.TimeGroupForbid;
import com.kuangchi.sdd.comm.equipment.common.UserTimeBlock;
import com.kuangchi.sdd.comm.equipment.gate.group.GetGroupConnector;
import com.kuangchi.sdd.comm.equipment.gate.group.SetGroupConnector;
import com.kuangchi.sdd.comm.equipment.gate.holiday.GetHolidayConnector;
import com.kuangchi.sdd.comm.equipment.gate.holiday.SetHolidayConnector;
import com.kuangchi.sdd.comm.equipment.gate.init.InitEquipmentConnector;
import com.kuangchi.sdd.comm.equipment.gate.limit.DelLimitConnector;
import com.kuangchi.sdd.comm.equipment.gate.limit.GetLimitConnector;
import com.kuangchi.sdd.comm.equipment.gate.limit.SetLimitConnector;
import com.kuangchi.sdd.comm.equipment.gate.record.GetRecordConnector;
import com.kuangchi.sdd.comm.equipment.gate.reset.ResetConnector;
import com.kuangchi.sdd.comm.equipment.gate.search.SearchEquipmentConnector;
import com.kuangchi.sdd.comm.equipment.gate.time.ReadTimeConnector;
import com.kuangchi.sdd.comm.equipment.gate.time.SetTimeConnector;
import com.kuangchi.sdd.comm.equipment.gate.workparam.GetWorkParamConnector;
import com.kuangchi.sdd.comm.equipment.gate.workparam.SetWorkParamConnector;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.util.excel.ExcelExportServer;

public class Tester {/*
	public static final Logger LOG = Logger.getLogger(Tester.class);	
	public static String searchEquipment() throws Exception {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0xFFFFFF);//0x0000017
		pkg.setOrder(0x00);
		pkg.setLength(0x0000);
		pkg.setOrderStatus(0x00);
		//校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());
		GetDevInfoService devInfoService=new GetDevInfoService();
        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
		String result = Service.service("search", pkg,deviceInfo2);
		LOG.info("容器获取的值:" + result);
        return result;		
	}

	public static String getTime() throws Exception {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0x850036);
		pkg.setOrder(0x12);
		pkg.setLength(0x0000);
		pkg.setOrderStatus(0x00);
		pkg.setCrc(pkg.getCrcFromSum());
		GetDevInfoService devInfoService=new GetDevInfoService();
        DeviceInfo2 deviceInfo2=devInfoService.getManager("850036");
		String result = Service.service("get_time", pkg,deviceInfo2);
		LOG.info("容器获取的值:" + result);
        return result;		
	}

	public static String setTime() throws Exception {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0x85000a);
		pkg.setOrder(0x13);
		pkg.setLength(0x0008);
		pkg.setOrderStatus(0x00);
		// 配置有效数据
		TimeData timeData = new TimeData();
		timeData.setYear(0x2016);
		timeData.setMonth(0x07);
		timeData.setDay(0x7);
		timeData.setHour(0x17);
		timeData.setMinute(0x53);
		timeData.setSecond(0x00);
		timeData.setDayOfWeek(0x04);
		
		SendData sendData = new SendData();
		sendData.setTimeData(timeData);
		pkg.setData(sendData);
		// 校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());
		GetDevInfoService devInfoService=new GetDevInfoService();
        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
		String result = Service.service("set_time", pkg,deviceInfo2);
		LOG.info("容器获取的值:" + result);
        return result;		
	}

	// 初始化设备
	public static String init() throws Exception {
		Connector connector = new InitEquipmentConnector();
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0x85000a);
		pkg.setOrder(0x02);
		pkg.setLength(0x001d);
		pkg.setOrderStatus(0x00);

		EquipmentDataForServer data = new EquipmentDataForServer();
		data.setMask("255.255.255.0");
		data.setGateway("192.168.212.254");
		data.setMechineIP("192.168.212.16");
		data.setRemoteIP("192.168.254.8");
		data.setMechineStatusPort("18004");
		data.setRemoteStatusPort("18013");
		data.setMechineOrderPort("18001");
		data.setRemoteOrderPort("18011");
		data.setMechineEventPort("18002");
		data.setRemoteEventPort("18012");
		data.setCardSign("8");

		SendData sender = new SendData();
		EquipmentData equipment = new EquipmentData();
		*//**
		 * 子网掩码 255.255.255.0
		 *//*
		String maskIP = data.getMask();
		equipment.setMask(Util.unmaskIp2Collection(maskIP));
		*//**
		 * 0xGGGGGGGG：网关参数。例如：0xC0A8FE01 代表：192. 168. 254. 1
		 *//*
		String gatewayIP = data.getGateway();
		equipment.setGateway(Util.unmaskIp2Collection(gatewayIP));
		*//**
		 * 0xLLLLLLLL：M4本地IP地址
		 *//*
		String mechineIP = data.getMechineIP();
		equipment.setMechineIP(Util.unmaskIp2Collection(mechineIP));
		*//**
		 * 0xRRRRRRRR：M4远程IP地址
		 *//*
		String remoteIP = data.getRemoteIP();
		equipment.setRemoteIP(Util.unmaskIp2Collection(remoteIP));
		*//**
		 * BBBB：M4状态本地端口。心跳
		 *//*
		String mechineStatusPort = data.getMechineStatusPort();
		equipment.setMechineStatusPort(Util
				.translateParamToShortArrayForPort(mechineStatusPort));
		*//**
		 * bbbb：M4状态远程端口。
		 *//*
		String remoteStatusPort = data.getRemoteStatusPort();
		equipment.setRemoteStatusPort(Util
				.translateParamToShortArrayForPort(remoteStatusPort));
		*//**
		 * IIII：M4指令本地端口。服务器下发指令
		 *//*
		String mechineOrderPort = data.getMechineOrderPort();
		equipment.setMechineOrderPort(Util
				.translateParamToShortArrayForPort(mechineOrderPort));
		*//**
		 * iiii：M4指令远程端口。
		 *//*
		String remoteOrderPort = data.getRemoteOrderPort();
		equipment.setRemoteOrderPort(Util
				.translateParamToShortArrayForPort(remoteOrderPort));
		*//**
		 * EEEE：M4事件本地端口。获取设备事件，如开门
		 *//*
		String mechineEventPort = data.getMechineEventPort();
		equipment.setMechineEventPort(Util
				.translateParamToShortArrayForPort(mechineEventPort));
		*//**
		 * eeee：M4事件远程端口。
		 *//*
		String remoteEventPort = data.getRemoteEventPort();
		equipment.setRemoteEventPort(Util
				.translateParamToShortArrayForPort(remoteEventPort));
		*//**
		 * 设置控制器标志 3字节或4字节
		 *//*
		equipment.setCardSign(Short.valueOf(data.getCardSign()));
		sender.setEquipmentData(equipment);
		pkg.setData(sender);
		// 校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());
		connector.setHeader(pkg);
		GetDevInfoService devInfoService=new GetDevInfoService();
        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
		String result = Service.service("init", pkg,deviceInfo2);
		System.out.println("容器获取的值:" + result);
		return result;
	}

	public static String reset() throws Exception {
		Connector connector = new ResetConnector();
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0x85000a);
		pkg.setOrder(0x10);
		pkg.setLength(0x0000);
		pkg.setOrderStatus(0x00);
		// 入参无有效数据
		// 校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());
		connector.setHeader(pkg);
		GetDevInfoService devInfoService=new GetDevInfoService();
        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
		String result = Service.service("reset", pkg,deviceInfo2);
		System.out.println("容器获取的值:" + result);
		return result;
	}

	public static String getGateWorkParam() throws Exception {
		Connector connector = new GetWorkParamConnector();
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0x85000a);
		pkg.setOrder(0x14);
		pkg.setLength(0x0001);
		pkg.setOrderStatus(0x00);
		// 配置有效数据
		GateParamData data = new GateParamData();
		data.setGateId(0x00);
		SendData sendData = new SendData();
		sendData.setGateParamData(data);
		pkg.setData(sendData);
		// 校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());
		connector.setHeader(pkg);
		GetDevInfoService devInfoService=new GetDevInfoService();
        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
		String result = Service.service("get_workparam", pkg,deviceInfo2);
		System.out.println("容器获取的值:" + result);
		return result;
	}

	public static String setGateWorkParam() throws Exception {
		Connector connector = new SetWorkParamConnector();
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0x85000a);
		pkg.setOrder(0x15);
		pkg.setLength(0x002e);
		pkg.setOrderStatus(0x00);
		// 配置有效数据
		GateParamData data = new GateParamData();
		data.setGateId(0x00); // 门号
		data.setUseSuperPassword(0x00);// 是否起用超级开门密码
		data.setSuperPassword(0x00); // 超级开门密码 4字节
		data.setUseForcePassword(0x00);// 是否启用胁迫密码
		data.setForcePassword(0x00); // 胁迫密码 2字节
		data.setRelockPassword(0x00);// 重锁 2字节
		data.setUnlockPassword(0x00);// 解锁 2字节
		data.setPolicePassword(0x00);// 报警密码 2字节
		data.setOpenPattern(0x00);// 功能模式
		data.setCheckOpenPattern(0x00);// 验证开门模式
		data.setWorkPattern(0x00);// 工作模式 1字节
		data.setOpenDelay(0x0000); // 开门延时 2字节
		data.setOpenOvertime(0x00);// 开门超时 1字节
		data.setMultiOpenNumber(0x5);// 多卡开门数量 1字节
		long[] multiOpenCard = new long[8];
		for (int i = 0; i < 8; i++) {
			multiOpenCard[i] = 0x000000;
		}
		data.setMultiOpenCard(multiOpenCard); // 多卡开门卡号 24字节 多卡开门仅支持3字节卡号
		// 校验值要求在配置有效数据后配置
		SendData sendData = new SendData();
		sendData.setGateParamData(data);
		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		connector.setHeader(pkg);
		GetDevInfoService devInfoService=new GetDevInfoService();
        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
		String result = Service.service("set_workparam", pkg,deviceInfo2);
		System.out.println("容器获取的值:" + result);
		return result;
	}

	public static String getGateLimit() throws Exception {
		Connector connector = new GetLimitConnector();
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0x85000a);
		pkg.setOrder(0x16);
		pkg.setLength(0x0004);
		pkg.setOrderStatus(0x00);
		// 有效数据
		GateLimitData data = new GateLimitData();
		data.setCardId(0x00002a8a);
		SendData sendData = new SendData();
		sendData.setGateLimitData(data);
		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		connector.setHeader(pkg);
		GetDevInfoService devInfoService=new GetDevInfoService();
        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
		String result = Service.service("get_limit", pkg,deviceInfo2);

		//String result = connector.run();
		System.out.println("容器获取的值:" + result);
		return result;
	}

	*//**
	 * 设置门禁权限
	 * 
	 * @return
	 * @throws Exception
	 *//*
	public static String setGateLimit() throws Exception {
		Connector connector = new SetLimitConnector();
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0x85000a);
		pkg.setOrder(0x17);
		pkg.setLength(0x0019);
		pkg.setOrderStatus(0x00);
		// 有效数据
		GateLimitData data = new GateLimitData();
		data.setGateId(0x00); // 门号
		data.setSeq(0x00);// 下载序号为00代表仅下载一条，多条累加，若下载多条则需要更新该标记，否则整批下载权限失败
		data.setCardId(0x00002a);
		int[] start = { 0x06, 0x01, 0x01, 0x06, 0x01 };
		data.setStart(start);
		int[] end = { 0x17, 0x10, 0x11, 0x06, 0x01 };
		data.setEnd(end);
		data.setPassword(0x0000);
		data.setGroup(0x1111ffff);
		data.setLimitSign(0xff00);
		data.setDeleteSign(0xf0);
		data.setRetain(0x6e);
		SendData sendData = new SendData();
		sendData.setGateLimitData(data);
		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		connector.setHeader(pkg);
		String result = connector.run();
		System.out.println("容器获取的值:" + result); 
		return result;
	}

	*//**
	 * 删除门禁权限
	 * 
	 * @return
	 * @throws Exception
	 *//*
	public static String delGateLimit() throws Exception {
		Connector connector = new DelLimitConnector();
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0x000017);
		pkg.setOrder(0x18);
		pkg.setLength(0x0005);
		pkg.setOrderStatus(0x00);
		// 有效数据
		GateLimitData data = new GateLimitData();
		data.setGateId(0x00);
		data.setSeq(0x00);
		data.setCardId(0x080000);
		SendData sendData = new SendData();
		sendData.setGateLimitData(data);
		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		connector.setHeader(pkg);
		String result = connector.run();
		System.out.println("容器获取的值:" + result);
		return result;
	}

	*//**
	 * 获取门禁权限
	 * 
	 * @return
	 * @throws Exception
	 *//*
	public static String getGateRecord() throws Exception {
		Connector connector = new GetRecordConnector();
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0x0000017);
		pkg.setOrder(0x1C);
		pkg.setLength(0x0000);
		pkg.setOrderStatus(0x00);
		// GateRecordData data = new GateRecordData();
		// data.setGatePoint(0x8000000);
		// SendData sendData = new SendData();
		// sendData.setGateRecordData(data);
		// pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		connector.setHeader(pkg);
		String result = connector.run();
		System.out.println("容器获取的值:" + result);
		return result;
	}

	public static String getGroup() throws Exception {
		Connector connector = new GetGroupConnector();
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0x85000a);
		pkg.setOrder(0x21);
		pkg.setLength(0x0001);
		pkg.setOrderStatus(0x00);

		TimeBlock timeBlock = new TimeBlock();
		timeBlock.setBlock(0x00);

		SendData sendData = new SendData();
		sendData.setTimeBlock(timeBlock);
		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		connector.setHeader(pkg);
		GetDevInfoService devInfoService=new GetDevInfoService();
        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
        
		String result = Service.service("get_group", pkg,deviceInfo2);
		System.out.println("容器获取的值:" + result);
		return result;
	}

	public static String setGroup() throws Exception {
		Connector connector = new SetGroupConnector();
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0x85000a);
		pkg.setOrder(0x22);
		pkg.setLength(0x0201);
		pkg.setOrderStatus(0x00);
		// 传入时间组的块号
		TimeBlock data = new TimeBlock();
		data.setBlock(0x00);
		List<TimeGroupData> groups = new ArrayList<TimeGroupData>();
		TimeGroupData group;
		TimeData start;
		TimeData end;
		for (int i = 0; i < 128; i++) {
			group = new TimeGroupData();
			start = new TimeData();
			start.setHour(0x16);
			start.setMinute(0x17);
			end = new TimeData();
			end.setHour(0x16);
			end.setMinute(0x18);

			group.setStart(start);
			group.setEnd(end);
			groups.add(group);
		}
		data.setGroups(groups);
		
		SendData sendData = new SendData();
		sendData.setTimeBlock(data);
		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		connector.setHeader(pkg);
		GetDevInfoService devInfoService=new GetDevInfoService();
        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
        
		String result = Service.service("set_group", pkg,deviceInfo2);
		LOG.info("容器获取的值:" + result);
		return result;
	}

	public static String getHoliday() throws Exception{
		Connector connector = new GetHolidayConnector();
		
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0x0000016);
		pkg.setOrder(0x27);
		pkg.setLength(0x0000);
		pkg.setOrderStatus(0x00);
		pkg.setCrc(pkg.getCrcFromSum());
		
		connector.setHeader(pkg);
		String result = connector.run();
		LOG.info("容器获取的值:" + result);
		return result;
	}
	
	public static String setHoliday() throws Exception{
		Connector connector = new SetHolidayConnector();
		
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0x000016);
		pkg.setOrder(0x28);
		pkg.setLength(0x0200);
		pkg.setOrderStatus(0x00);
		
		TimeBlock block = new TimeBlock();
		List<TimeGroupData> groups = new ArrayList<TimeGroupData>(128);
		LOG.info("参数集合的长度="+groups.size());
		TimeGroupData group;
		TimeData time;
		for(int i=0;i<128;i++){
			group= new TimeGroupData();
			time = new TimeData();
			time.setYear(0xf00);
			time.setMonth(0x09);
			time.setDay(0x16);
			time.setDayOfWeek(0x06);
			group.setStart(time);
		    groups.add(group);
		}
		block.setGroups(groups);
		
		pkg.setCrc(pkg.getCrcFromSum());
		
		SendData sendData = new SendData();
		sendData.setTimeBlock(block);
		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		connector.setHeader(pkg);
		String result = connector.run();
		LOG.info("容器获取的值:" + result);
		return result;
	}
	
	public static String getStatus() throws Exception {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0x0000016);
		pkg.setOrder(0x35);
		pkg.setLength(0x0000);
		pkg.setOrderStatus(0x00);
		//校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());
		GetDevInfoService devInfoService=new GetDevInfoService();
        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
        
		String result = Service.service("get_status", pkg,deviceInfo2);
		LOG.info("容器获取的值:" + result);
        return result;		
	}
	
	public static String setFire() throws Exception {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		String mac="16";
		
		pkg.setMac(Util.getIntHex(mac));
		pkg.setOrder(0x2c);
		pkg.setLength(0x0001);
		pkg.setOrderStatus(0x00);
		
		//配置有效数据
		GateParamData data = new GateParamData();
		data.setUseFireFighting(0x00); // 是否启用消防
        SendData sendData = new SendData();
        sendData.setGateParamData(data);
		pkg.setData(sendData);
		//校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());
		GetDevInfoService devInfoService=new GetDevInfoService();
        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
		String result = Service.service("set_fire", pkg,deviceInfo2);
		LOG.info("容器获取的值:" + result);
        return result;		
	}
	
	public static String setTrigger() throws Exception {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0x0000016);
		pkg.setOrder(0x2D);
		pkg.setLength(0x0001);
		pkg.setOrderStatus(0x00);
		
		//配置有效数据
		GateParamData data = new GateParamData();
		data.setClientTrigger(0x01); // 是否启用上位触发 1-2-4-8
        SendData sendData = new SendData();
        sendData.setGateParamData(data);
		pkg.setData(sendData);
		//校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());
		GetDevInfoService devInfoService=new GetDevInfoService();
        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
		String result = Service.service("set_trigger", pkg,deviceInfo2);
		LOG.info("容器获取的值:" + result);
        return result;		
	}
	
	public static String setActualTime() throws Exception {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0x85000a);
		pkg.setOrder(0x34);
		pkg.setLength(0x0002);
		pkg.setOrderStatus(0x00);
		
		//配置有效数据
		GateParamData data = new GateParamData();
		
		 * 0xCC：实时上传参数。  
		 * -bit0：实时状态上传参数；	
		 * -bit1：巡更、门禁事件记录上传参数；	
		 * -bit2：刷卡记录上传参数。     
		 * -0：不上传； 1：上传。
		 * 
　　     * 0xTT： 上传间隔事件，以100毫秒为单位。最大间隔25.6秒
		 
		data.setActualTime(0x0302); // 是否启用读取实时上传参数
        SendData sendData = new SendData();
        sendData.setGateParamData(data);
		pkg.setData(sendData);
		//校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());
		GetDevInfoService devInfoService=new GetDevInfoService();
        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
		String result = Service.service("set_actualtime", pkg,deviceInfo2);
		LOG.info("容器获取的值:" + result);
        return result;		
	}
	
	public static String getActualTime() throws Exception {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0x0000016);
		pkg.setOrder(0x33);
		pkg.setLength(0x0000);
		pkg.setOrderStatus(0x00);
		
		//配置有效数据
		GateParamData data = new GateParamData();
		data.setActualTime(0x0302); // 是否启用读取实时上传参数
        SendData sendData = new SendData();
        sendData.setGateParamData(data);
		pkg.setData(sendData);
		//校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());
		GetDevInfoService devInfoService=new GetDevInfoService();
        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
		String result = Service.service("get_actualtime", pkg,deviceInfo2);
		LOG.info("容器获取的值:" + result);
        return result;		
	}
	
	public static String setFirst() throws Exception {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(0x85000a);
		pkg.setOrder(0x36);
		pkg.setLength(0x0002);
		pkg.setOrderStatus(0x00);
		
		//配置有效数据
		GateParamData data = new GateParamData();
		data.setGateId(0x00);//门号
		data.setFirst(0x00); // 是否首卡开门参数 0x00:取消首卡开门;	0x01:设置首卡开门.
        SendData sendData = new SendData();
        sendData.setGateParamData(data);
		pkg.setData(sendData);
		//校验值要求在配置有效数据后配置
		pkg.setCrc(pkg.getCrcFromSum());
		GetDevInfoService devInfoService=new GetDevInfoService();
        DeviceInfo2 deviceInfo2=devInfoService.getManager("850036");
		String result = Service.service("set_first", pkg,deviceInfo2);
		LOG.info("容器获取的值:" + result);
        return result;		
	}
	
	public static void main(String[] args) {
		try {
//Tester.searchEquipment();
			// Tester.setTime();
		//Tester.getTime();
			//getStateForbidTime();
		// Tester.init();
		   // Tester.reset();
		    //Tester.getGateWorkParam();
			 //Tester.setGateWorkParam();
			//  Tester.clearData();
			//remoteOpenDoor();
			//restoreDevice();
			//setStateForbidTime();
	//Tester.setGateLimit();
	//	System.out.println("......................................");
//	Tester.getGateLimit();
		  
		//	 Tester.delGateLimit();
		   
	//	Tester.setGroup();
	  //Tester.getGroup();
		//Tester.setUserGroup();
		//Tester.getUserGroup();
		//	Tester.setDeviceGroup();
	//	Tester.getDeviceGroup();
		//Tester.respondRecord();
		//	Tester.setHoliday();
		//	Tester.getHoliday();
	//			Tester.getStatus();
//	 Tester.setFire();
		//		Tester.setTrigger();
		//	Tester.setActualTime();
			//Tester.getActualTime();
			//Tester.setFirst();
		//	Tester.setDoubleInOut();
			
			
		  String	result1=Tester.downSoftware();
		  if (result1!=null) {
			  String result2=	Tester.packageDownSoftware();
				if ("success".equals(result2)) {
					String result3=Tester.overSoftware();
					
				}
		}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
	//设置双向开门
		public static String setDoubleInOut() throws Exception {
			SendHeader pkg = new SendHeader();
			pkg.setHeader(0x55);
			pkg.setSign(0x04);
			pkg.setMac(0x000016);
			pkg.setOrder(0x37);
			pkg.setLength(0x0001);
			pkg.setOrderStatus(0x00);

			//配置有效数据
			GateParamData data = new GateParamData();
			data.setInout(0x05); //
	        SendData sendData = new SendData();
	        sendData.setGateParamData(data);
			pkg.setData(sendData);
			//校验值要求在配置有效数据后配置
			pkg.setCrc(pkg.getCrcFromSum());
			GetDevInfoService devInfoService=new GetDevInfoService();
	        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
			String result = Service.service("set_double_in_out", pkg,deviceInfo2);
			LOG.info("容器获取的值:" + result);
			return result;		
		}


	 //读取双向开门 
		public static String getDoubleInOut() throws Exception {
			SendHeader pkg = new SendHeader();
			pkg.setHeader(0x55);
			pkg.setSign(0x04);
			pkg.setMac(0x000016);
			pkg.setOrder(0x38);
			pkg.setLength(0x0000);
			pkg.setOrderStatus(0x00);

			//配置有效数据
			GateParamData data = new GateParamData();
	        SendData sendData = new SendData();
	        sendData.setGateParamData(data);
			pkg.setData(sendData);
			//校验值要求在配置有效数据后配置
			pkg.setCrc(pkg.getCrcFromSum());
			GetDevInfoService devInfoService=new GetDevInfoService();
	        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
			String result = Service.service("get_double_in_out", pkg,deviceInfo2);
			LOG.info("容器获取的值:" + result);
	        return result;		
		}
	
	
		public static String setUserGroup() throws Exception {
			//Connector connector = new SetUserGroupConnector();
			SendHeader pkg = new SendHeader();
			pkg.setHeader(0x55);
			pkg.setSign(0x04);
			
			pkg.setMac(0x000016);
			pkg.setOrder(0x24);
			pkg.setLength(0x0201);
			pkg.setOrderStatus(0x00);
			// 传入时间组的块号
			UserTimeBlock data = new UserTimeBlock();
			data.setBlock(0x00);
			List<Integer> times = new ArrayList<Integer>();
		
			for (int i = 0; i < 512; i++) {
				times.add(0x00);
			}
			data.setTimes(times);
			
			SendData sendData = new SendData();
			sendData.setUserTimeBlock(data);
			pkg.setData(sendData);
			pkg.setCrc(pkg.getCrcFromSum());
			GetDevInfoService devInfoService=new GetDevInfoService();
	        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
			String result = Service.service("set_user_group", pkg,deviceInfo2);
			//String result = connector.run();
			LOG.info("容器获取的值:" + result);
			return result;
		}
	
		public static String getUserGroup() throws Exception {
			//Connector connector = new SetUserGroupConnector();
			SendHeader pkg = new SendHeader();
			pkg.setHeader(0x55);
			pkg.setSign(0x04);
			
			pkg.setMac(0x850036);
			pkg.setOrder(0x23);
			pkg.setLength(0x0001);
			pkg.setOrderStatus(0x00);
			// 传入时间组的块号
			UserTimeBlock data = new UserTimeBlock();
			data.setBlock(0x00);
			SendData sendData = new SendData();
			sendData.setUserTimeBlock(data);
			pkg.setData(sendData);
			pkg.setCrc(pkg.getCrcFromSum());
			//connector.setHeader(pkg);
			GetDevInfoService devInfoService=new GetDevInfoService();
	        DeviceInfo2 deviceInfo2=devInfoService.getManager("850036");
			String result = Service.service("get_user_group", pkg,deviceInfo2);
			//String result = connector.run();
			LOG.info("容器获取的值:" + result);
			return result;
		}

		public static String setDeviceGroup() throws Exception {
			//Connector connector = new SetUserGroupConnector();
			SendHeader pkg = new SendHeader();
			pkg.setHeader(0x55);
			pkg.setSign(0x04);
			
			pkg.setMac(0x000016);
			pkg.setOrder(0x26);
			pkg.setLength(0x0201);
			pkg.setOrderStatus(0x00);
			// 传入时间组的块号
			DeviceTimeBlock data = new DeviceTimeBlock();
			data.setBlock(0x01);
			List<DeviceTimeData> times = new ArrayList<DeviceTimeData>();
		
			for (int i = 0; i < 128; i++) {
				DeviceTimeData deviceTimeData=new DeviceTimeData();
				deviceTimeData.setHour(0x01);
				deviceTimeData.setMinute(0x02);
			    deviceTimeData.setActionType(0x03);
			    deviceTimeData.setRetain(0xff);
			    times.add(deviceTimeData);
			}
			data.setDeviceTimes(times);
			
			SendData sendData = new SendData();
			sendData.setDeviceTimeBlock(data);
			pkg.setData(sendData);
			pkg.setCrc(pkg.getCrcFromSum());
			//connector.setHeader(pkg);
			GetDevInfoService devInfoService=new GetDevInfoService();
	        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
			String result = Service.service("set_device_group", pkg,deviceInfo2);
			//String result = connector.run();
			LOG.info("容器获取的值:" + result);
			return result;
		}
		
		public static String getDeviceGroup() throws Exception {
			//Connector connector = new SetUserGroupConnector();
			SendHeader pkg = new SendHeader();
			pkg.setHeader(0x55);
			pkg.setSign(0x04);
			
			pkg.setMac(0x85000a);
			pkg.setOrder(0x25);
			pkg.setLength(0x0001);
			pkg.setOrderStatus(0x00);
			// 传入时间组的块号
			DeviceTimeBlock data = new DeviceTimeBlock();
			data.setBlock(0x00);
			SendData sendData = new SendData();
			sendData.setDeviceTimeBlock(data);
			pkg.setData(sendData);
			pkg.setCrc(pkg.getCrcFromSum());
			//connector.setHeader(pkg);
			GetDevInfoService devInfoService=new GetDevInfoService();
	        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
			String result = Service.service("get_device_group", pkg,deviceInfo2);
			//String result = connector.run();
			LOG.info("容器获取的值:" + result);
			return result;
		}

		*//**
		 * @return
		 * @throws Exception
		 *//*
		public static String respondRecord() throws Exception {
			//Connector connector = new SetUserGroupConnector();
			SendHeader pkg = new SendHeader();
			pkg.setHeader(0x55);
			pkg.setSign(0x04);
			
			pkg.setMac(0x85000a);
			pkg.setOrder(0x40);
			pkg.setLength(0x0005);
			pkg.setOrderStatus(0x00);
			
			RespondRecord data = new RespondRecord();
			data.setRecordType(0x00);
			data.setRecordId(184549376);
			
			SendData sendData = new SendData();
			sendData.setRespondRecord(data);
			pkg.setData(sendData);
			pkg.setCrc(pkg.getCrcFromSum());
			//connector.setHeader(pkg);
			GetDevInfoService devInfoService=new GetDevInfoService();
	        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
			String result = Service.service("respond_record", pkg,deviceInfo2);
			//String result = connector.run();
			LOG.info("容器获取的值:" + result);
			return result;
		}
	
		
		*//**
		 * @return
		 * @throws Exception
		 *//*
		public static String clearData() throws Exception {
			//Connector connector = new SetUserGroupConnector();
			SendHeader pkg = new SendHeader();
			pkg.setHeader(0x55);
			pkg.setSign(0x04);
			
			pkg.setMac(0x85000a);
			pkg.setOrder(0x2B);
			pkg.setLength(0x0001);
			pkg.setOrderStatus(0x00);
			
			ClearData data = new ClearData();
			//ox01  
			data.setDataType(0x00);
			
			
			SendData sendData = new SendData();
			sendData.setClearData(data);
			pkg.setData(sendData);
			pkg.setCrc(pkg.getCrcFromSum());
			//connector.setHeader(pkg);
			GetDevInfoService devInfoService=new GetDevInfoService();
	        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
			String result = Service.service("clear_data", pkg,deviceInfo2);
			//String result = connector.run();
			LOG.info("容器获取的值:" + result);
			return result;
		}
		
		
		*//**
		 * 远程开门
		 * @throws Exception
		 *//*
		public static String remoteOpenDoor() throws Exception {
			//Connector connector = new SetUserGroupConnector();
			SendHeader pkg = new SendHeader();
			pkg.setHeader(0x55);
			pkg.setSign(0x04);
			
			pkg.setMac(0x85000a);
			pkg.setOrder(0x2D);
			pkg.setLength(0x0001);
			pkg.setOrderStatus(0x00);
			
			RemoteOpenDoor data = new RemoteOpenDoor();
			//ox01  
			data.setDoor(0x01);
			
			
			SendData sendData = new SendData();
			sendData.setDoorData(data);
			pkg.setData(sendData);
			pkg.setCrc(pkg.getCrcFromSum());
			//connector.setHeader(pkg);
			GetDevInfoService devInfoService=new GetDevInfoService();
	        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
			String result = Service.service("set_remote_open_door", pkg,deviceInfo2);
			//String result = connector.run();
			LOG.info("容器获取的值:" + result);
			return result;
		}
		
		
		*//**
		 * 设备复位
		 * @throws Exception
		 *//*
		public static String restoreDevice() throws Exception {
			//Connector connector = new SetUserGroupConnector();
			SendHeader pkg = new SendHeader();
			pkg.setHeader(0x55);
			pkg.setSign(0x04);
			
			pkg.setMac(0x85000a);
			pkg.setOrder(0x11);
			pkg.setLength(0x0000);
			pkg.setOrderStatus(0x00);
			
	
			
			SendData sendData = new SendData();
			pkg.setData(sendData);
			pkg.setCrc(pkg.getCrcFromSum());
			//connector.setHeader(pkg);
			GetDevInfoService devInfoService=new GetDevInfoService();
	        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
			String result = Service.service("restore_device", pkg,deviceInfo2);
			//String result = connector.run();
			LOG.info("容器获取的值:" + result);
			return result;
		}
	
		*//**
		 * 设备复位
		 * @throws Exception
		 *//*
		public static String setStateForbidTime() throws Exception {
			//Connector connector = new SetUserGroupConnector();
			SendHeader pkg = new SendHeader();
			pkg.setHeader(0x55);
			pkg.setSign(0x04);
			
			pkg.setMac(0x85000a);
			pkg.setOrder(0x43);
			pkg.setLength(0x0011);
			pkg.setOrderStatus(0x00);
			TimeGroupForbid data = new TimeGroupForbid();
			//ox01  
			data.setEnable(0);
			
			TimeForbid[] timeForbids=new TimeForbid[4];
			for (int i = 0; i < 4; i++) {
				TimeForbid timeForbid=new TimeForbid();
				timeForbid.setStartHour(10);
				timeForbid.setStartMinute(10);
				timeForbid.setEndHour(10);
				timeForbid.setEndMinute(10);
				timeForbids[i]=timeForbid;
			}
	          data.setTimeForbids(timeForbids);
			
			SendData sendData = new SendData();
			sendData.setTimeGroupForbid(data);

			pkg.setData(sendData);
			pkg.setCrc(pkg.getCrcFromSum());
			//connector.setHeader(pkg);
			GetDevInfoService devInfoService=new GetDevInfoService();
	        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
			String result = Service.service("set_state_forbid_time", pkg,deviceInfo2);
			//String result = connector.run();
			LOG.info("容器获取的值:" + result);
			return result;
		}
		
		
		
		*//**
		 * 读取设备上报时段
		 * @throws Exception
		 *//*
		public static String getStateForbidTime() throws Exception {
			//Connector connector = new SetUserGroupConnector();
			SendHeader pkg = new SendHeader();
			pkg.setHeader(0x55);
			pkg.setSign(0x04);
			
			pkg.setMac(0x85000a);
			pkg.setOrder(0x44);
			pkg.setLength(0x0000);
			pkg.setOrderStatus(0x00);
			
	
			
			SendData sendData = new SendData();
			pkg.setData(sendData);
			pkg.setCrc(pkg.getCrcFromSum());
			//connector.setHeader(pkg);
			GetDevInfoService devInfoService=new GetDevInfoService();
	        DeviceInfo2 deviceInfo2=devInfoService.getManager("85000a");
			String result = Service.service("get_state_forbid_time", pkg,deviceInfo2);
			//String result = connector.run();
			LOG.info("容器获取的值:" + result);
			return result;
		}
		
		
		
		*//**
		 * 下发程序
		 * @throws Exception
		 *//*
		public static String downSoftware() throws Exception {
			//Connector connector = new SetUserGroupConnector();
			SendHeader pkg = new SendHeader();
			pkg.setHeader(0x55);
			pkg.setSign(0x04);
			
			pkg.setMac(0x850034);
			pkg.setOrder(0x50);
			pkg.setLength(0x0000);
			pkg.setOrderStatus(0x00);
			
	
			
			SendData sendData = new SendData();
			Down down=new Down();
			sendData.setDown(down);
			pkg.setData(sendData);
			pkg.setCrc(pkg.getCrcFromSum());
			//connector.setHeader(pkg);
			GetDevInfoService devInfoService=new GetDevInfoService();
	        DeviceInfo2 deviceInfo2=devInfoService.getManager("850034");
			String result = Service.service("down", pkg,deviceInfo2);
			//String result = connector.run();
			LOG.info("容器获取的值:" + result);
			return result;
		}
		
		
		
		*//**
		 * 下发程序包
		 * @throws Exception
		 *//*
		public static String packageDownSoftware()   {
			File file=new java.io.File("D:/一卡通文档/gatesoftware/main");
			 InputStream fis=null;
			 try {
				 fis=new FileInputStream(file);
			
			 long fileLength=file.length();
			 long packageSize=900;
			 long residue=fileLength%packageSize;
			 long loopTimes=residue==0?fileLength/packageSize:(fileLength/packageSize+1);
		     int packageNum=1;
			 for (int i = 0; i < loopTimes-1; i++) {
				    SendHeader pkg = new SendHeader();
					pkg.setHeader(0x55);
					pkg.setSign(0x04);
					
					pkg.setMac(0x850034);
					pkg.setOrder(0x51);
					pkg.setLength(Long.valueOf(packageSize).intValue()+2);
					pkg.setOrderStatus(0x00);
					
			        
					
					SendData sendData = new SendData();
					PackageDown packageDown=new PackageDown();
					packageDown.setPackageNum(packageNum);
					byte[] bytes=new byte[Long.valueOf(packageSize).intValue()];
					fis.read(bytes);
					packageDown.setBytes(bytes);
					sendData.setPackageDown(packageDown);
					
					pkg.setData(sendData);
					pkg.setCrc(pkg.getCrcFromSum());
					//connector.setHeader(pkg);
					GetDevInfoService devInfoService=new GetDevInfoService();
			        DeviceInfo2 deviceInfo2=devInfoService.getManager("850034");
					String result = Service.service("package_down", pkg,deviceInfo2);
					if (null==result||"null".equals(result)) {
						return "failure";
					}
					packageNum++;
					 
			}
			 
			 
			 
			 
			 //尾字节
			 
			 
			   SendHeader pkg = new SendHeader();
				pkg.setHeader(0x55);
				pkg.setSign(0x04);				
				pkg.setMac(0x850034);
				pkg.setOrder(0x51);
				pkg.setLength((residue==0?Long.valueOf(packageSize).intValue():Long.valueOf(residue).intValue())+2);
				pkg.setOrderStatus(0x00);
				SendData sendData = new SendData();
				
				PackageDown packageDown=new PackageDown();
				packageDown.setPackageNum(packageNum);
				byte[] bytes=new byte[residue==0?Long.valueOf(packageSize).intValue():Long.valueOf(residue).intValue()];
				fis.read(bytes);
				packageDown.setBytes(bytes);
				sendData.setPackageDown(packageDown);
				
				pkg.setData(sendData);
				pkg.setCrc(pkg.getCrcFromSum());
				//connector.setHeader(pkg);
				GetDevInfoService devInfoService=new GetDevInfoService();
		        DeviceInfo2 deviceInfo2=devInfoService.getManager("850034");
				String result = Service.service("package_down", pkg,deviceInfo2);
				if (null==result||"null".equals(result)) {
					return "failure";
				}

			 } catch (Exception e) {
					 e.printStackTrace();
				}finally{
					try {
						if (null!=fis) {
							fis.close();	
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			 return "success";
		}
		
		
		
		*//**
		 * 下发程序结束
		 * @throws Exception
		 *//*
		public static String overSoftware() throws Exception {
			//Connector connector = new SetUserGroupConnector();
			SendHeader pkg = new SendHeader();
			pkg.setHeader(0x55);
			pkg.setSign(0x04);
			
			pkg.setMac(0x850034);
			pkg.setOrder(0x52);
			pkg.setLength(0x0000);
			pkg.setOrderStatus(0x00);
			
	
			
			SendData sendData = new SendData();
			Over over=new Over();
			sendData.setOver(over);
			pkg.setData(sendData);
			
			pkg.setCrc(pkg.getCrcFromSum());
			//connector.setHeader(pkg);
			GetDevInfoService devInfoService=new GetDevInfoService();
	        DeviceInfo2 deviceInfo2=devInfoService.getManager("850034");
			String result = Service.service("over", pkg,deviceInfo2);
			//String result = connector.run();
			LOG.info("容器获取的值:" + result);
			return result;
		}
		
		
		
*/}
