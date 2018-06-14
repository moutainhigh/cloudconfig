package com.kuangchi.sdd.commConsole.gateLimit.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.GateLimitData;
import com.kuangchi.sdd.comm.equipment.common.SendData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.util.GsonUtil;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.commConsole.gateLimit.service.IGetGateLimitService;

@Service("getGateLimitServiceImpl")
public class GetGateLimitServiceImpl implements IGetGateLimitService {
	public static final Logger LOG = Logger.getLogger(GetGateLimitServiceImpl.class);
	
	@Resource(name = "getDevInfoService")
	GetDevInfoService getDevInfoService;

	@Override
	public Map<String, Object> getGateLimit(Integer mac, Integer cardId,
			int sign) throws Exception {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		// pkg.setSign(0x04);
		pkg.setSign(sign);// deviceType
		pkg.setMac(mac);
		pkg.setOrder(0x16);
		pkg.setLength(0x0004);
		pkg.setOrderStatus(0x00);
		// 有效数据
		GateLimitData data = new GateLimitData();
		data.setCardId(cardId);
		SendData sendData = new SendData();
		sendData.setGateLimitData(data);
		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		DeviceInfo2 deviceInfo2 = getDevInfoService.getManager(Integer
				.toHexString(mac));
		String result = com.kuangchi.sdd.comm.container.Service.service(
				"get_limit", pkg, deviceInfo2);
		LOG.info("容器获取的值:" + result);

		Map<String, Object> map = GsonUtil.toBean(result, HashMap.class);// {cardId=3,
																			// deleteSign=f0,
																			// start=16-9-8-15-26,
																			// retain=eb,
																			// group=ffff,
																			// password=0,
																			// limitSign=ff01,
																			// end=16-9-30-15-26}
		/*
		 * String startDateStr = (map == null ? "2005-01-01-01-01" : map.get(
		 * "start").toString());
		 */
		if (map.size() > 0) {
			//处理卡号
			int card_temp=Util.getIntHex(map.get("cardId").toString());
			int retain_temp=Util.getIntHex(map.get("retain").toString());
			card_temp=card_temp << 8;
			card_temp=card_temp | retain_temp;
			
			map.put("cardId", card_temp+"");
			
			
			String startDateStr = map.get("start").toString();
			String start[] = startDateStr.split("-");
			Map<String, Integer> startTime = new HashMap<String, Integer>();
			startTime.put("startYear", Util.getIntHex(start[0]));
			startTime.put("startMonth", Util.getIntHex(start[1]));
			startTime.put("startDay", Util.getIntHex(start[2]));
			startTime.put("startHour", Util.getIntHex(start[3]));
			startTime.put("startMinute", Util.getIntHex(start[4]));
			map.put("startTime", startTime);

			/*
			 * String endDateStr = (map == null ? "2005-01-01-01-01" :
			 * map.get("end") .toString());
			 */
			String endDateStr = map.get("end").toString();
			String end[] = endDateStr.split("-");
			Map<String, Integer> endTime = new HashMap<String, Integer>();
			endTime.put("endYear", Util.getIntHex(end[0]));
			endTime.put("endMonth", Util.getIntHex(end[1]));
			endTime.put("endDay", Util.getIntHex(end[2]));
			endTime.put("endHour", Util.getIntHex(end[3]));
			endTime.put("endMinute", Util.getIntHex(end[4]));
			map.put("endTime", endTime);
		}

		return map;

	}

	/*
	 * public static void main(String[] args) { GetGateLimitServiceImpl im = new
	 * GetGateLimitServiceImpl(); try { // Map<String, Object> i =
	 * im.getGateLimit(0x85000a, 0x00002222);
	 * 
	 * // int i = im.setGateLimit(0x85000a, 3, 4, "00002222", new Date(), // new
	 * Date(), 10);
	 * 
	 * int i = im.delGateLimit(0x85000a, 3, 4, "00002222"); //
	 * System.out.println("返回值为：" + i); } catch (Exception e) {
	 * e.printStackTrace(); } }
	 */
	@Override
	public int setGateLimit(int mac, int gateId, int deviceType,
			String cardIdString, Date startTime, Date endTime, Integer groupNum)
			throws Exception {// 0代表有效
		gateId = gateId - 1;
		int cardId = Util.getIntHex(cardIdString) >>> 8;// 右移8位
		int retain = Util.getIntHex(cardIdString) & 0x000000ff;// 保留后8位

		Map<String, Object> map = getGateLimit(mac,
				Util.getIntHex(cardIdString), deviceType);// 查询门禁权限
//		{startTime={startMinute=0, startYear=0, startDay=0, startHour=0, startMonth=0}, 
//		cardId=0, deleteSign=0, start=0-0-0-0-0, retain=0, endTime={endYear=0, endDay=0, endMinute=0, endHour=0, endMonth=0}, group=0, password=0, limitSign=0, end=0-0-0-0-0}
		long oldGroup = Long.parseLong(map.get("group").toString(), 16);
		Integer oldLimitSign = null;
//		oldLimitSign = Util.getIntHex(map.get("limitSign").toString());
		if("0".equals(map.get("cardId").toString())){
			 oldLimitSign = 65535;
		}else{
			 oldLimitSign = Util.getIntHex(map.get("limitSign").toString());
		}
		Integer oldDeleteSign = null;
//		oldDeleteSign = Util.getIntHex(map.get("deleteSign").toString());
		if("0".equals(map.get("cardId").toString())){
			oldDeleteSign = 255;
		}else{
			oldDeleteSign = Util.getIntHex(map.get("deleteSign").toString());
		}

		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(deviceType);// 设置设备类型
		pkg.setMac(mac);// 设置设备mac地址
		pkg.setOrder(0x17);
		pkg.setLength(0x0019);
		pkg.setOrderStatus(0x00);
		// 有效数据
		GateLimitData data = new GateLimitData();
		data.setGateId(gateId); // 门号
		data.setSeq(0x00);// 下载序号为00代表仅下载一条，多条累加，若下载多条则需要更新该标记，否则整批下载权限失败
		data.setCardId(cardId);
         /*
          * 说明：时段设置的4个字节，使用低位2个字节 ，从右至左每4个字节分别表示1 号门，2号门，3号门，4号门的组号 ，值为0至15 如00003F4E表示  
          * 1号门，2号门,3号门，4号门的时段组号分别为15,4,无,3
          * 
          * 权限标志位 2个字节，用低位字节， 8 个bit位，从右至左编号 ， 0-8 ，  其中0,2,4,6,8 分别表示1-4号门是否有组权限， 1，3，5，7 分别表示1-4号门的门权限
          * 
          * 
          */
		int[] start = new int[5];
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startTime);
		String startYear = (startCalendar.get(Calendar.YEAR) + "").substring(2,
				4);// 拿到日期的年份的后两位
		start[0] = Util.getIntHex("" + Integer.parseInt(startYear));
		start[1] = Util.getIntHex("" + (startCalendar.get(Calendar.MONTH) + 1));
		start[2] = Util
				.getIntHex("" + startCalendar.get(Calendar.DAY_OF_MONTH));
		start[3] = Util.getIntHex("" + startCalendar.get(Calendar.HOUR_OF_DAY));
		start[4] = Util.getIntHex("" + startCalendar.get(Calendar.MINUTE));
		// int[] start1={15,10,15,10,30};

		data.setStart(start);

		int[] end = new int[5];
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(endTime);
		String endYear = (endCalendar.get(Calendar.YEAR) + "").substring(2, 4);// 拿到日期的年份的后两位
		end[0] = Util.getIntHex("" + Integer.parseInt(endYear));
		end[1] = Util.getIntHex("" + (endCalendar.get(Calendar.MONTH) + 1));
		end[2] = Util.getIntHex("" + endCalendar.get(Calendar.DAY_OF_MONTH));
		end[3] = Util.getIntHex("" + endCalendar.get(Calendar.HOUR_OF_DAY));
		end[4] = Util.getIntHex("" + endCalendar.get(Calendar.MINUTE));
		// int[] end1={22,10,15,10,30};
		data.setEnd(end);

		data.setPassword(0x0000);
		data.setRetain(retain);

		int newLimitSign = oldLimitSign | 0xff00;
		int newDeleteSign = oldDeleteSign | 0xf0;
		// long newGroup = oldGroup>>>8;
		long newGroup = oldGroup;
		if (gateId == 0) {
			newDeleteSign = (newDeleteSign & 0xfe); // 设置0号门的删除标志位
			if (groupNum == null) {
				newLimitSign = (newLimitSign | 1); // 设置0号门的权限标志位（组标志位）
			} else {
				newGroup = ((newGroup & 0xf0ff) | groupNum << 8); // 测试 3,2,1,0低位顺序门号
				// newGroup = ((newGroup & 0xf0ff) | (groupNum << 8));//
				// 设置0号门的时段组 //测试1,0,3,2 高位顺序门号
				newLimitSign = (newLimitSign & 0xfffe);// 设置0号门的权限标志位（组标志位）
			}
			newLimitSign = newLimitSign & 0xfffd; // 设置0号门的权限标志位（下同）
		} else if (gateId == 1) {
			newDeleteSign = (newDeleteSign & 0xfd);

			if (groupNum == null) {
				newLimitSign = (newLimitSign | 4);

			} else {
				newGroup = ((newGroup & 0x0fff) | groupNum << 12); // 测试
																	// 3,2,1,0低位顺序门号
				// newGroup = ((newGroup & 0x0fff) | (groupNum << 12));
				newLimitSign = (newLimitSign & 0xFFFB);
			}
			newLimitSign = newLimitSign & 0xfff7;
		} else if (gateId == 2) {
			newDeleteSign = (newDeleteSign & 0xfb);
			if (groupNum == null) {
				newLimitSign = (newLimitSign | 16);
			} else {
				newGroup = ((newGroup & 0xfff0) | groupNum << 0); // 测试
																	// 3,2,1,0低位顺序门号
				// newGroup = ((newGroup & 0xfff0) | (groupNum << 0));
				newLimitSign = (newLimitSign & 0xffef);
			}
			newLimitSign = newLimitSign & 0xffdf;
		} else if (gateId == 3) {
			newDeleteSign = (newDeleteSign & 0xf7);
			if (groupNum == null) {
				newLimitSign = (newLimitSign | 64);
			} else {
				newGroup = ((newGroup & 0xff0f) | groupNum << 4); // 测试
																	// 3,2,1,0低位顺序门号
				// newGroup = ((newGroup & 0xff0f) | (groupNum << 4));
				newLimitSign = (newLimitSign & 0xFFBF);
			}
			newLimitSign = newLimitSign & 0xff7f;
		}

		// newGroup = ((newGroup << 16)) | 0xffff;
		newGroup = ((newGroup)) | 0xffff0000;
		data.setLimitSign(newLimitSign);
		// data.setLimitSign(0xff00);
		data.setDeleteSign(newDeleteSign);
		// data.setDeleteSign(0xf0);
		data.setGroup(newGroup);
		// data.setGroup(0xffffff0f);
		SendData sendData = new SendData();
		sendData.setGateLimitData(data);
		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		DeviceInfo2 deviceInfo2 = getDevInfoService.getManager(Integer
				.toHexString(mac));
		String result = com.kuangchi.sdd.comm.container.Service.service(
				"set_limit", pkg, deviceInfo2);
		LOG.info("容器获取的值:" + result);
		if (result == null) {
			return 1;
		} else {
			return 0;
		}

	}

	@Override
	public int delGateLimit(int mac, int gateId, int deviceType,
			String cardIdString) throws Exception {
		gateId = gateId - 1;
		int cardId = Util.getIntHex(cardIdString) >>> 8;// 右移8位
		int retain = Util.getIntHex(cardIdString) & 0x000000ff;// 保留后8位

		Map<String, Object> map = getGateLimit(mac,
				Util.getIntHex(cardIdString), deviceType);// 查询门禁权限
		long oldGroup = Long.parseLong(map.get("group").toString(), 16);
		Integer oldDeleteSign = Util
				.getIntHex(map.get("deleteSign").toString());
		Integer oldLimitSign = Util.getIntHex(map.get("limitSign").toString());

		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(deviceType);// 设置设备类型
		pkg.setMac(mac);// 设置设备mac地址
		pkg.setOrder(0x17);
		pkg.setLength(0x0019);
		pkg.setOrderStatus(0x00);
		// 有效数据
		GateLimitData data = new GateLimitData();
		data.setGateId(gateId); // 门号
		data.setSeq(0x00);// 下载序号为00代表仅下载一条，多条累加，若下载多条则需要更新该标记，否则整批下载权限失败
		data.setCardId(cardId);

		int[] start = new int[5];
		Map<String, Object> oldStart = (Map<String, Object>) map
				.get("startTime");

		start[0] = Integer.parseInt(oldStart.get("startYear").toString());
		start[1] = Integer.parseInt(oldStart.get("startMonth").toString());
		start[2] = Integer.parseInt(oldStart.get("startDay").toString());
		start[3] = Integer.parseInt(oldStart.get("startHour").toString());
		start[4] = Integer.parseInt(oldStart.get("startMinute").toString());

		data.setStart(start);

		int[] end = new int[5];
		Map<String, Object> endStart = (Map<String, Object>) map.get("endTime");
		end[0] = Integer.parseInt(endStart.get("endYear").toString());
		end[1] = Integer.parseInt(endStart.get("endMonth").toString());
		end[2] = Integer.parseInt(endStart.get("endDay").toString());
		end[3] = Integer.parseInt(endStart.get("endHour").toString());
		end[4] = Integer.parseInt(endStart.get("endMinute").toString());
		data.setEnd(end);

		data.setPassword(0x0000);
		data.setRetain(retain);

		int newLimitSign = oldLimitSign | 0xff00;
		int newDeleteSign = oldDeleteSign | 0xf0;
		long newGroup = oldGroup;
		if (gateId == 0) {
			newDeleteSign = (newDeleteSign | 1); // 设置0号门的删除标志位
			newLimitSign = newLimitSign | 2; // 设置0号门的权限标志位（下同）
		} else if (gateId == 1) {
			newDeleteSign = (newDeleteSign | 2);
			newLimitSign = newLimitSign | 8;
		} else if (gateId == 2) {
			newDeleteSign = (newDeleteSign | 4);
			newLimitSign = newLimitSign | 32;
		} else if (gateId == 3) {
			newDeleteSign = (newDeleteSign | 8);
			newLimitSign = newLimitSign | 128;
		}

		newGroup = ((newGroup << 16)) | 0xffff;
		data.setLimitSign(newLimitSign);
		data.setDeleteSign(newDeleteSign);
		data.setGroup(newGroup);

		SendData sendData = new SendData();
		sendData.setGateLimitData(data);
		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		DeviceInfo2 deviceInfo2 = getDevInfoService.getManager(Integer
				.toHexString(mac));

		String result = com.kuangchi.sdd.comm.container.Service.service(
				"set_limit", pkg, deviceInfo2);
		LOG.info("容器获取的值:" + result);
		if (result == null) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public Map readGateLimit(Integer mac, Integer cardId, int sign)
			throws Exception {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		// pkg.setSign(0x04);
		pkg.setSign(sign);// deviceType
		pkg.setMac(mac);
		pkg.setOrder(0x16);
		pkg.setLength(0x0004);
		pkg.setOrderStatus(0x00);
		// 有效数据
		GateLimitData data = new GateLimitData();
		data.setCardId(cardId);
		SendData sendData = new SendData();
		sendData.setGateLimitData(data);
		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		DeviceInfo2 deviceInfo2 = getDevInfoService.getManager(Integer
				.toHexString(mac));
		String result = com.kuangchi.sdd.comm.container.Service.service(
				"get_limit", pkg, deviceInfo2);
		LOG.info("容器获取的值:" + result);
		Map<String, Object> map = GsonUtil.toBean(result, HashMap.class);
		map = translateTheMap(map);
		return map; 
	}

	// --------------------------------------门面模式开始------------------------------------------------------------
	/**
	 * 翻译Map from {cardId=3, deleteSign=f0, start=16-9-8-15-26, retain=eb,
	 * group=ffff, password=0, limitSign=ff01, end=16-9-30-15-26} to {cardId=70,
	 * time4=null, time3=null, time2=null, time1=null, startDateTime=2016-09-12
	 * 10:22, door1=false, door2=false, endDateTime=2016-09-30 10:22,
	 * door3=false, door4=false} by gengji.yang
	 */
	private Map translateTheMap(Map map) {
		Map m = new HashMap();
		m = explainDoorAndTime(
				Integer.parseInt((String) map.get("deleteSign"), 16),
				Integer.parseInt((String) map.get("limitSign"), 16),
				(String) map.get("group"));
		String startDateTime = changeTheDateTime((String) map.get("start"));
		String endDateTime = changeTheDateTime((String) map.get("end"));
		String retain = (String) map.get("retain");
		String cardId = transHexToDecimal((String) map.get("cardId")
				+ (retain.length() < 2 ? "0" + retain : retain));
		m.put("startDateTime", startDateTime);
		m.put("endDateTime", endDateTime);
		m.put("cardId", cardId);
		return m;
	}

	/**
	 * 翻译有效期 将16-9-8-15-26转成2016-09-08 15:26 by gengji.yang
	 */
	private String changeTheDateTime(String dateTimeStr) {
		String[] nums = dateTimeStr.split("-");
		for (int i = 0; i < nums.length; i++) {
			if (i == 0) {
				nums[i] = "20" + nums[i];
			} else if (nums[i].length() == 1) {
				nums[i] = "0" + nums[i];
			}
		}
		String result = nums[0] + "-" + nums[1] + "-" + nums[2] + " " + nums[3]
				+ ":" + nums[4];
		return result;
	}

	/**
	 * 翻译时段组 by gengji.yang
	 */
	private String makeTimeGroup(String group, int i) {
		char[] doors = new StringBuffer(group).reverse().toString().toCharArray();
		Integer result = Integer.parseInt(transHexToDecimal(doors[i] + ""));
		return result + "";
	}

	/**
	 * 翻译十六进制 by gengji.yang
	 */
	private String transHexToDecimal(String hexStr) {
		return Integer.parseInt(hexStr, 16) + "";
	}

	/**
	 * 解析出门权限以及时段权限 by gengji.yang
	 */
	private Map explainDoorAndTime(Integer delSign, Integer limitSign,
			String group) {
		Map map = new HashMap();
		map.putAll(makeEachDoorMap("door1", "time1", delSign, limitSign, group,
				1, 1, 0));
		map.putAll(makeEachDoorMap("door2", "time2", delSign, limitSign, group,
				2, 4, 1));
		map.putAll(makeEachDoorMap("door3", "time3", delSign, limitSign, group,
				4, 16, 2));
		map.putAll(makeEachDoorMap("door4", "time4", delSign, limitSign, group,
				8, 64, 3));
		return map;
	}

	/**
	 * 制作每一个门Map by gengji.yang
	 */
	private Map makeEachDoorMap(String door, String time, Integer delSign,
			Integer limitSign, String group, Integer andNumForDel,
			Integer andNumForGroup, int i) {
		Map map = new HashMap();
		if ((delSign & andNumForDel) == 0) {
			map.put(door, true);
			if ((limitSign & andNumForGroup) == 0) {
				map.put(time, makeTimeGroup(group, i));
			} else {
				map.put(time, false);
			}
		} else {
			map.put(door, false);
			map.put(time, false);
		}
		return map;
	}
	// ---------------------------门面模式结束------------------------------------------------------

}
