package com.kuangchi.sdd.elevator.dllInterfaces;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.consum.util.Util;
import com.kuangchi.sdd.elevator.model.Auth;
import com.kuangchi.sdd.elevator.model.Authority;
import com.kuangchi.sdd.elevator.model.BlackListBean;
import com.kuangchi.sdd.elevator.model.CheckEvent;
import com.kuangchi.sdd.elevator.model.CommParam;
import com.kuangchi.sdd.elevator.model.DelAuth;
import com.kuangchi.sdd.elevator.model.Device;
import com.kuangchi.sdd.elevator.model.FloorConfigBean;
import com.kuangchi.sdd.elevator.model.FloorOpenTimeZone;
import com.kuangchi.sdd.elevator.model.HardWareParam;
import com.kuangchi.sdd.elevator.model.OpenTimeZone;
import com.kuangchi.sdd.elevator.model.Record;
import com.kuangchi.sdd.elevator.model.Result;
import com.kuangchi.sdd.elevator.model.TimeZone;
import com.kuangchi.sdd.elevator.model.TriggerEvent;
/**
 * 
 * 梯控调用DLL的接口的协议对接类
 * 
 * 
 * **/
public class TKInterfaceFunctions {
	public static final Logger LOG = Logger.getLogger(TKInterfaceFunctions.class);
	// 函数功能 : 获取通信线程计数 获取通信线程计数 获取通信线程计数 获取通信线程计数 ;
	public static Integer KKTK_GetCommThreadCount() {
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		return tkInterfaces.KKTK_GetCommThreadCount();
	}

	// 函数功能: 停止所有通信线程;
	public static void KKTK_StopAllComm() {
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		tkInterfaces.KKTK_StopAllComm();
	}

	// 函数功能: 发送时钟;
	public static Result KKTK_SendClock(Device device) {
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		String deviceName = getDeviceName(device);
		Integer nCommID = tkInterfaces.KKTK_SendClock(deviceName);
		return KKTK_GetCommResult(nCommID);
	}

	// 接收时钟
	public static Result KKTK_RecvClock(Device device) {
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		String deviceName = getDeviceName(device);
		System.out.println(deviceName);
		Integer nCommID = tkInterfaces.KKTK_RecvClock(deviceName);
		return KKTK_GetCommResult(nCommID);
	}

	// 函数功能: 获取通信结果;
	public static Result KKTK_GetCommResult(Integer nCommID) {
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		byte[] bytes = new byte[1024];
		int resultCode = tkInterfaces.KKTK_GetCommResult(nCommID, bytes);
		while (resultCode == 0xfffffff2) {
			resultCode = tkInterfaces.KKTK_GetCommResult(nCommID, bytes);
		}
		Result result = new Result();
		result.setResultCode(resultCode);
		if (resultCode == 0) {
			result.setResultTxt(new String(bytes).trim());
		} else {
			result.setResultTxt(getErrorMsg(resultCode));
		}
		LOG.info(result.getResultCode() + "========="
				+ result.getResultTxt());
		return result;
	}

	// 初始化控制器
	public static Result KKTK_SystemInit(Device device) {
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		String deviceName = getDeviceName(device);
		Integer nCommID = tkInterfaces.KKTK_SystemInit(deviceName);
		return KKTK_GetCommResult(nCommID);
	}

	// 功能: 搜索电梯设备
	public static List<Device> KKTK_SearchControls(String ip) {
		KKTK_SetSearchIP(ip);
		List<Device> list = new ArrayList<Device>();
		byte[] bytes = new byte[2048];
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		tkInterfaces.KKTK_SearchControls(bytes,bytes.length);
		String searchResult = new String(bytes).trim();
		if (null != searchResult && !"".equals(searchResult)) {
			String[] strs = searchResult.split("\\|");
			int count = strs.length / 7;
			for (int i = 0; i < count; i++) {
				Device device = new Device();
				device.setAddress(strs[i * count]);
				device.setMac(strs[i * count + 1]);
				device.setSerialNo(strs[i * count + 2]);
				device.setIp(strs[i * count + 3]);
				device.setPort(strs[i * count + 4]);
				device.setSubnet(strs[i * count + 5]);
				device.setGateway(strs[i * count + 6]);
				list.add(device);
			}

		}
		return list;
	}

	// 功能: 修改控制器通信参数.
	public static Result KKTK_UpdateControl(Device device, String newAddress) {
		String setStr = device.getAddress() + "|" + newAddress + "|"
				+ device.getIp() + "|" + device.getPort() + "|"
				+ device.getSubnet() + "|" + device.getGateway() + "|"
				+ device.getMac() + "|";
		LOG.info(setStr);
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		Integer nCommID = tkInterfaces.KKTK_UpdateControl(setStr);
		return KKTK_GetCommResult(nCommID);
	}

	// 修改密码
	public static Result KKTK_UpdatePassword(Device device, String newPassword) {
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		String deviceName = getDeviceName(device);
		Integer nCommID = tkInterfaces.KKTK_UpdatePassword(deviceName,
				newPassword);
		return KKTK_GetCommResult(nCommID);
	}

	// 发送电梯开放时区;
	public static Result KKTK_SendElevatorOpenTimezone(Device device,
			OpenTimeZone openTimeZone) {
		List<TimeZone> timeZoneList = openTimeZone.getTimeZoneList();
		int num = timeZoneList.size();
		if (num > 5) {
			num = 5;
		}
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("0" + openTimeZone.getWeekDay()).append("|")
				.append("0").append(num).append("|");
		for (int i = 0; i < num; i++) {
			TimeZone timeZone = timeZoneList.get(i);
			stringBuffer.append(timeZone.getStartHour()).append("|")
					.append(timeZone.getStartMinute()).append("|")
					.append(timeZone.getEndHour()).append("|")
					.append(timeZone.getEndMinute()).append("|");

		}
		for (int i = num; i < 5; i++) {
			stringBuffer.append("00").append("|").append("00").append("|")
					.append("00").append("|").append("00").append("|");
		}
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		String deviceName = getDeviceName(device);
		LOG.info(stringBuffer.toString());
		Integer nCommID = tkInterfaces.KKTK_SendElevatorOpenTimezone(
				deviceName, stringBuffer.toString());
		return KKTK_GetCommResult(nCommID);

	}

	// 发送楼层开放时区

	public static Result KKTK_SendFloorOpenTimezone(Device device,
			FloorOpenTimeZone floorOpenTimeZone) {
		StringBuffer stringBuffer = new StringBuffer();
		if (floorOpenTimeZone.getFloor() > 15) {
			stringBuffer.append(
					Integer.toHexString(floorOpenTimeZone.getFloor())).append(
					"|");
		} else {
			stringBuffer.append("0")
					.append(Integer.toHexString(floorOpenTimeZone.getFloor()))
					.append("|");
		}
		List<TimeZone> timeZoneList = floorOpenTimeZone.getTimeZoneList();
		int num = timeZoneList.size();
		if (num > 4) {
			num = 4;
		}
		for (int i = 0; i < num; i++) {
			TimeZone timeZone = timeZoneList.get(i);
			stringBuffer.append(timeZone.getStartHour()).append("|")
					.append(timeZone.getStartMinute()).append("|")
					.append(timeZone.getEndHour()).append("|")
					.append(timeZone.getEndMinute()).append("|");

		}
		for (int i = num; i < 4; i++) {
			stringBuffer.append("00").append("|").append("00").append("|")
					.append("00").append("|").append("00").append("|");
		}
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		String deviceName = getDeviceName(device);
		LOG.info(stringBuffer.toString());
		Integer nCommID = tkInterfaces.KKTK_SendFloorOpenTimezone(deviceName,
				stringBuffer.toString());
		return KKTK_GetCommResult(nCommID);
	}

	// 发送 黑白名单 调不通

	public static Result KKTK_SendBlacklist(Device device,
			BlackListBean blackListBean) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(blackListBean.getMode()).append("|");
		List<String> list = blackListBean.getList();

		int num = list.size();
		stringBuffer.append(num).append("|");
		for (int i = 0; i < num; i++) {
			stringBuffer.append(list.get(i)).append("|");
		}
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		String deviceName = getDeviceName(device);
		LOG.info(stringBuffer.toString());
		Integer nCommID = tkInterfaces.KKTK_SendBlacklist(deviceName,
				blackListBean.getType(), stringBuffer.toString(),false);
		return KKTK_GetCommResult(nCommID);
	}

	// 发送楼层配置表
	public static Result KKTK_SendCongfigTable(Device device,
			FloorConfigBean configBean) {
		List<Integer> floorList = configBean.getFloorList();
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < floorList.size(); i++) {
			if (floorList.get(i) < 10) {
				stringBuffer.append("0").append(floorList.get(i)).append("|");
			} else {
				stringBuffer.append(floorList.get(i)).append("|");
			}
		}

		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		String deviceName = getDeviceName(device);
		LOG.info(stringBuffer.toString());
		Integer nCommID = tkInterfaces.KKTK_SendCongfigTable(deviceName,
				configBean.getType(), stringBuffer.toString());
		return KKTK_GetCommResult(nCommID);
	}

	// 获取楼层配置表 type 参数 0- 读取配置楼层, 1-读取对讲开放楼层配置;
	public static FloorConfigBean KKTK_RecvConfigTable(Device device,
			Integer type) {
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		String deviceName = getDeviceName(device);
		Integer nCommID = tkInterfaces.KKTK_RecvConfigTbale(deviceName, type);
		Result result = KKTK_GetCommResult(nCommID);
		if (0 != result.getResultCode()) {
			return null;
		} else {
			FloorConfigBean floorConfigBean = new FloorConfigBean();
			floorConfigBean.setType(type);
			java.util.Scanner scanner = new java.util.Scanner(
					result.getResultTxt());
			while (scanner.hasNext()) {
				floorConfigBean.getFloorList().add(
						Integer.parseInt(scanner.nextLine()));
			}
			scanner.close();

			return floorConfigBean;
		}

	}

	// 发送节假日表 如 2012-01-01
	public static Result KKTK_SendHolidayTable(Device device,
			List<String> holidayDateList) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		int[] bytes = new int[46];

		for (int i = 0; i < holidayDateList.size(); i++) {
			try {
				String dateStr = holidayDateList.get(i);
				Date date = simpleDateFormat.parse(dateStr);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
				int index = dayOfYear / 8;
				int bitPosition = dayOfYear % 8;
				if (dayOfYear % 8 == 0) {
					index--;
					bitPosition = 8;
				}
				int belongByte = bytes[index];
				belongByte = belongByte
						| (128 >> (bitPosition == 0 ? 0 : (bitPosition - 1)));
				bytes[index] = belongByte;

			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}

		}
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			stringBuffer.append(bytes[i]).append("|");
		}

		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		String deviceName = getDeviceName(device);
		LOG.info(stringBuffer.toString());
		Integer nCommID = tkInterfaces.KKTK_SendHolidayTable(deviceName,
				stringBuffer.toString());
		return KKTK_GetCommResult(nCommID);
	}

	// 发送授权人员ID
	public static Result KKTK_SendAddID(Device device, Authority authority) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(1).append("|");
		stringBuffer.append(authority.getClear()).append("|")
				.append(authority.getAdd()).append("|");
		stringBuffer.append(authority.getAuthList().size()).append("|");
		for (int i = 0; i < authority.getAuthList().size(); i++) {
			Auth auth = authority.getAuthList().get(i);
			stringBuffer.append(auth.getCardNum()).append("|")
					.append(auth.getPassword()).append("|")
					.append(auth.getCardType()).append("|");
			int[] bytes = new int[6];
			List<Integer> floorList = authority.getAuthList().get(i)
					.getFloorList();

			for (int j = 0; j < floorList.size(); j++) {
				Integer floor = floorList.get(j);
				int index = floor / 8;
				int bitPosition = floor % 8;
				if (floor % 8 == 0) {
					index--;
					bitPosition = 8;
				}
				int belongByte = bytes[index];
				belongByte = belongByte
						| (1 << (bitPosition == 0 ? 0 : (bitPosition - 1)));
				bytes[index] = belongByte;
			}

			for (int j = 0; j < bytes.length; j++) {
				stringBuffer.append(bytes[j]).append("|");
			}

			stringBuffer.append("00|00|");
			stringBuffer.append(auth.getStartDate()).append(auth.getEndDate())
					.append("|");
		}

		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		String deviceName = getDeviceName(device);
		LOG.info(stringBuffer.toString());
		Integer nCommID = tkInterfaces.KKTK_SendAddID(deviceName,
				stringBuffer.toString());
		return KKTK_GetCommResult(nCommID);
	}

	// 发送删除授权

	public static Result KKTK_SendDelID(Device device, DelAuth delAuth) {
		StringBuffer stringBuffer = new StringBuffer();
		Integer type = delAuth.getType();
		if (0 == type) {
			stringBuffer.append("0").append("|").append("0").append("|");
		} else {
			List<String> cardNumList = delAuth.getCardNumList();
			stringBuffer.append("1").append("|").append(cardNumList.size())
					.append("|");
			for (int i = 0; i < cardNumList.size(); i++) {
				stringBuffer.append(cardNumList.get(i)).append("|");
			}
		}
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		String deviceName = getDeviceName(device);
		LOG.info(stringBuffer.toString());
		Integer nCommID = tkInterfaces.KKTK_SendDelID(deviceName,
				stringBuffer.toString());
		return KKTK_GetCommResult(nCommID);

	}

	// 接收记录

	public static Record KKTK_RecvEvent(Device device) {
		byte[] bytes = new byte[1024];
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		String deviceName = getDeviceName(device);
		Integer nCommID = tkInterfaces.KKTK_RecvEvent(deviceName, bytes);
		Result result = KKTK_GetCommResult(nCommID);
		LOG.info(result.getResultTxt());
		if (0 != result.getResultCode()) {
			return null;

		} else {
			Record record = new Record();
			String[] strs = result.getResultTxt().split("\\|");
			String recordTime = strs[1].substring(0, 2) + "-"
					+ strs[1].substring(2, 4) + "-" + strs[1].substring(4, 6)
					+ " " + strs[1].substring(8, 10) + ":"
					+ strs[1].substring(10, 12) + ":"
					+ strs[1].substring(12, 14);
			Integer dayInWeek = Integer.parseInt(strs[1].substring(6, 8));
			if (Integer.parseInt(strs[2]) == 0) {
				CheckEvent checkEvent = new CheckEvent();
				checkEvent.setRecordId(String.valueOf(Util.getIntHex(strs[0])));
				checkEvent.setRecordTime(recordTime);
				checkEvent.setDayInWeek(String.valueOf(dayInWeek));
				checkEvent.setRecordType("0");
				checkEvent.setCardId(String.valueOf(Util.getIntHex(strs[3])));
				checkEvent.setRecordStatus(String.valueOf(Util
						.getIntHex(strs[4])));
				checkEvent.setRecordCount(String.valueOf(Util
						.getIntHex(strs[5])));
				checkEvent
						.setCardType(String.valueOf(Integer.parseInt(strs[6])));
				record.setCheckEvent(checkEvent);
			} else {
				TriggerEvent triggerEvent = new TriggerEvent();
				triggerEvent
						.setRecordId(String.valueOf(Util.getIntHex(strs[0])));
				triggerEvent.setRecordTime(recordTime);
				triggerEvent.setDayInWeek(String.valueOf(dayInWeek));
				triggerEvent.setRecordType("1");

				String floorStr = strs[3];
				String[] strArray = new String[6];
				for (int i = 0; i < 6; i++) {
					strArray[i] = floorStr.substring(2 * i, i * 2 + 2);
				}

				// 目前测试值发现数组的第一个字节有用，表示楼层号，其它字节还不知道代表什么

				List<String> floorList = new ArrayList<String>();

				floorList.add(String.valueOf(Util.getIntHex(strArray[0])));

				triggerEvent.setFloorList(floorList);

				triggerEvent.setFloorStatus(strs[4]);
				triggerEvent.setRecordFlag(strs[5]);
				record.setTriggerEvent(triggerEvent);

			}
			return record;
		}

	}

	// 接收参数
	private static Result KKTK_RecvParam(Device device, Integer type) {
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		String deviceName = getDeviceName(device);
		Integer nCommID = tkInterfaces.KKTK_RecvParam(deviceName, type);
		return KKTK_GetCommResult(nCommID);
	}

	// 获取硬件参数
	public static HardWareParam KKTK_RecHardWareParam(Device device) {
		Result result1 = KKTK_RecvParam(device, 0x35);
		String updownParam = result1.getResultTxt();
		if (0 != result1.getResultCode()) {
			return null;
		}
		String[] updownPs = updownParam.split("\\|");
		HardWareParam hardWareParam = new HardWareParam();
		hardWareParam.setUp(Integer.parseInt(updownPs[0]));
		hardWareParam.setDown(Integer.parseInt(updownPs[1]));
		hardWareParam.setOpen(Integer.parseInt(updownPs[2]));
		hardWareParam.setClose(Integer.parseInt(updownPs[3]));

		Result result2 = KKTK_RecvParam(device, 0x36);
		if (0 != result2.getResultCode()) {
			return null;
		}
		hardWareParam.setTotalFloor(Integer.parseInt(result2.getResultTxt()));

		Result result3 = KKTK_RecvParam(device, 0x37);
		if (0 != result3.getResultCode()) {
			return null;
		}
		hardWareParam
				.setFloorRelayTime(Integer.parseInt(result3.getResultTxt()));
		Result result4 = KKTK_RecvParam(device, 0x38);
		if (0 != result4.getResultCode()) {
			return null;
		}
		hardWareParam.setDirectRelayTime(Integer.parseInt(result4
				.getResultTxt()));
		Result result5 = KKTK_RecvParam(device, 0x39);
		if (0 != result5.getResultCode()) {
			return null;
		}
		hardWareParam.setOpositeRelayTime(Integer.parseInt(result5
				.getResultTxt()));
		return hardWareParam;
	}

	// 函数功能: 接收控制器版本;
	public static Result KKTK_RecvVer(Device device) {
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		String deviceName = getDeviceName(device);
		Integer nCommID = tkInterfaces.KKTK_RecvVer(deviceName);
		return KKTK_GetCommResult(nCommID);
	}

	// 函数功能: 发送发送硬件参数;
	public static Result KKTK_SendHardwareParam(Device device,
			HardWareParam hardWareParam) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(hardWareParam.getFloorRelayTime()).append("|")
				.append(hardWareParam.getDirectRelayTime()).append("|")
				.append(hardWareParam.getOpositeRelayTime()).append("|")
				.append(hardWareParam.getTotalFloor()).append("|")
				.append("0|0|0|").append(hardWareParam.getUp()).append("|")
				.append(hardWareParam.getDown()).append("|")
				.append(hardWareParam.getOpen()).append("|")
				.append(hardWareParam.getClose()).append("|");
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		String deviceName = getDeviceName(device);
		LOG.info(stringBuffer.toString());
		Integer nCommID = tkInterfaces.KKTK_SendHardwareParam(deviceName,
				stringBuffer.toString());
		return KKTK_GetCommResult(nCommID);

	}

	// 读取控制器工作状态
	public static String KKTK_RecvWorkStatus(Device device) {
		Result result = KKTK_RecvStatu(device);
		String[] strs = result.getResultTxt().split("\\|");
		return strs[0];
	}

	// 读取控制器未采集的记录数量
	public static Integer KKTK_RecvRecordCount(Device device) {
		Result result = KKTK_RecvStatu(device);
		String[] strs = result.getResultTxt().split("\\|");
		return Integer.parseInt(strs[1]);
	}

	// 函数功能: 发送控制板通信参数;
	public static Result KKTK_SendControlCommParam(Device device,
			CommParam commParam) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(commParam.getAddress()).append("||");
		stringBuffer.append(commParam.getIp()).append("|");
		stringBuffer.append(commParam.getPort()).append("|")
				.append(commParam.getSubnet()).append("|")
				.append(commParam.getGateway()).append("|")
				.append(commParam.getSerialNo()).append("|");
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		String deviceName = getDeviceName(device);
		LOG.info(stringBuffer);
		Integer nCommID = tkInterfaces.KKTK_SendControlCommParam(deviceName,
				stringBuffer.toString());
		return KKTK_GetCommResult(nCommID);
	}

	// 接收控制器通信参数
	public static CommParam KKTK_RecvControlCommParam(Device device) {
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		String deviceName = getDeviceName(device);
		Integer nCommID = tkInterfaces.KKTK_RecvControlCommParam(deviceName);
		Result result = KKTK_GetCommResult(nCommID);
		if (0 == result.getResultCode()) {
			String[] strs = result.getResultTxt().split("\\|");
			CommParam commParam = new CommParam();
			commParam.setAddress(strs[0]);
			commParam.setSerialNo(strs[1]);
			commParam.setIp(strs[2]);
			commParam.setPort(strs[3]);
			commParam.setSubnet(strs[4]);
			commParam.setGateway(strs[5]);
			return commParam;

		} else {
			return null;
		}

	}

	// 读取控制器状态
	private static Result KKTK_RecvStatu(Device device) {
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		String deviceName = getDeviceName(device);
		Integer nCommID = tkInterfaces.KKTK_RecvStatus(deviceName);
		return KKTK_GetCommResult(nCommID);
	}

	// 功能 : 查看动态库内部版本 查看动态库内部版本 .
	public static String KKTK_Get_DLLVersion() {
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		byte[] versionBytes = new byte[20];
		tkInterfaces.KKTK_Get_DLLVersion(versionBytes);
		return new String(versionBytes).trim();
	}

	// 功能: 绑定网卡.
	public static void KKTK_SetSearchIP(String ip) {
		TKInterfaces tkInterfaces = TKInterfacesFactory.getTkInterfaces();
		tkInterfaces.KKTK_SetSearchIP(ip);
	}

	private static String getDeviceName(Device device) {
		String deviceName = device.getCommType() + "|" + device.getComm() + "|"
				+ device.getAddress() + "|" + device.getIp() + "|"
				+ device.getPort() + "|" + device.getPassword() + "|" + "|"
				+ "|" + "|";
		LOG.info(deviceName);
		return deviceName;
	}

	private static String getErrorMsg(Integer code) {
		String resultCode = "";
		switch (code) {
		case 0x00000000:
			resultCode = "成功";
			break;
		case 0xffffff01:
			resultCode = "VER 不符,命令未执行";
			break;
		case 0xffffff02:
			resultCode = "控制器接收的命令帧累加和检查不对";
			break;
		case 0xffffff03:
			resultCode = "控制器接收的命令帧参数部分的累加和检查不对";
			break;
		case 0xffffff04:
			resultCode = " 无效的CID";
			break;
		case 0xffffff05:
			resultCode = "不能识别的命令格式";
			break;
		case 0xffffff06:
			resultCode = "控制器接收的命令帧,数据信息部分有无效数据";
			break;
		case 0xffffff07:
			resultCode = "无设置控制器的权限";
			break;
		case 0xffffffe0:
			resultCode = "控制器密码不正确";
			break;
		case 0xffffffe1:
			resultCode = "控制器内部密码修改不成功";
			break;
		case 0xffffffe2:
			resultCode = "控制器内部相应设置项存储空间已满";
			break;
		case 0xffffffe3:
			resultCode = " 控制器内部参数的修改或删除不成功";
			break;
		case 0xffffffe4:
			resultCode = "控制器内部相应设置项的存储空间已空";
			break;
		case 0xffffffe5:
			resultCode = "控制器内部无相应信息项";
			break;
		case 0xffffffe6:
			resultCode = "重复设置控制器内的相同用户";
			break;
		case 0xffffffe7:
			resultCode = "重复设置相同条码给不同的用户";
			break;
		case 0xffffffe8:
			resultCode = "重复设置完全相同的用户";
			break;
		case 0xfffffff0:
			resultCode = "控制器名错误";
			break;
		case 0xfffffff1:
			resultCode = "入口参数错误";
			break;
		case 0xfffffff2:
			resultCode = "通信未完成";
			break;
		case 0xfffffff3:
			resultCode = "CommID 不存在";
			break;
		case 0xfffffff4:
			resultCode = "串口初始化失败";
			break;
		case 0xfffffff5:
			resultCode = "MODEM 初始化失败";
			break;
		case 0xfffffff6:
			resultCode = "GPRS 监听初始化失败";
			break;
		case 0xfffffff7:
			resultCode = "控制器数据格式错误";
			break;
		case 0xfffffff8:
			resultCode = " 发送短信失败";
			break;
		case 0xfffffff9:
			resultCode = "控制器无应答";
			break;
		case 0xfffffffa:
			resultCode = "MODEM 无应答";
			break;
		case 0xfffffffb:
			resultCode = "GPRS 控制器不在线";
			break;

		case 0xfffffffc:
			resultCode = "连接失败";
			break;
		case 0xfffffffd:
			resultCode = "连接断开";
			break;
		case 0xfffffffe:
			resultCode = "通信线程错误";
			break;
		case 0xffffffff:
			resultCode = "通信超时";
			break;
		case 0xffffffd3:
			resultCode = "未知错误";
			break;
		default:
			resultCode = "";
			break;
		}
		return resultCode;
	}

}
