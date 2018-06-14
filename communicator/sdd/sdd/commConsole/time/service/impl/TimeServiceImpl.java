package com.kuangchi.sdd.commConsole.time.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuangchi.sdd.comm.equipment.base.service.GetDevInfoService;
import com.kuangchi.sdd.comm.equipment.base.service.model.DeviceInfo2;
import com.kuangchi.sdd.comm.equipment.common.SendData;
import com.kuangchi.sdd.comm.equipment.common.SendHeader;
import com.kuangchi.sdd.comm.equipment.common.TimeData;
import com.kuangchi.sdd.comm.util.Util;
import com.kuangchi.sdd.commConsole.deviceGroup.service.impl.DeviceGroupServiceImpl;
import com.kuangchi.sdd.commConsole.time.model.Time;
import com.kuangchi.sdd.commConsole.time.model.TimeJson;
import com.kuangchi.sdd.commConsole.time.service.ITimeService;

@Transactional
@Service("timeServiceImpl")
public class TimeServiceImpl implements ITimeService {
	public static final Logger LOG = Logger.getLogger(TimeServiceImpl.class);
	@Resource(name = "getDevInfoService")
	GetDevInfoService getDevInfoService;
	final int LENGTH = 2;

	@Override
	public Time getDeviceTime(String mac, String sign) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		// pkg.setSign(0x04);
		pkg.setSign(Util.getIntHex(sign));// 设置设备类型
		pkg.setMac(Util.getIntHex(mac));// 设置mac地址
		// pkg.setMac(0x85000a);
		pkg.setOrder(0x12);
		pkg.setLength(0x0000);
		pkg.setOrderStatus(0x00);
		pkg.setCrc(pkg.getCrcFromSum());
		Time time = null;
		String f = "%0" + LENGTH + "d";
		try {
			DeviceInfo2 deviceInfo2 = getDevInfoService.getManager(mac);
			String result = com.kuangchi.sdd.comm.container.Service.service(
					"get_time", pkg, deviceInfo2);
			Gson gson = new Gson();
			java.lang.reflect.Type type = new TypeToken<TimeJson>() {
			}.getType();
			TimeJson tJson = gson.fromJson(result, type);
			if (tJson != null) {
				time = new Time();
				StringBuffer sbuffer = new StringBuffer();
				sbuffer.append(tJson.getYear());
				sbuffer.append("-");
				sbuffer.append(String.format(f,
						Integer.parseInt(tJson.getMonth())));
				sbuffer.append("-");
				sbuffer.append(String.format(f,
						Integer.parseInt(tJson.getDay())));
				sbuffer.append(" ");
				sbuffer.append(String.format(f,
						Integer.parseInt(tJson.getHour())));
				sbuffer.append(":");
				sbuffer.append(String.format(f,
						Integer.parseInt(tJson.getMinute())));
				sbuffer.append(":");
				sbuffer.append(String.format(f,
						Integer.parseInt(tJson.getSecond())));
				time.setDate(sbuffer.toString());
				time.setDayOfWeek(tJson.getDayOfWeek());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	@Override
	public int setDeviceTime(String mac, String date) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(0x04);
		pkg.setMac(Util.getIntHex(mac));
		pkg.setOrder(0x13);
		pkg.setLength(0x0008);
		pkg.setOrderStatus(0x00);
		String result = "";
		try {
			// 时间格式字符串转换为时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = sdf.parse(date);
			GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
			gc.setTime(d);
			int year = gc.get(Calendar.YEAR);
			int month = gc.get(Calendar.MONTH) + 1;
			int day = gc.get(Calendar.DATE);
			int hour = gc.get(Calendar.HOUR_OF_DAY);
			int minute = gc.get(Calendar.MINUTE);
			int second = gc.get(Calendar.SECOND);
			int week = gc.get(Calendar.DAY_OF_WEEK) - 1;
			// 配置有效数据
			TimeData timeData = new TimeData();
			timeData.setYear(Util.getIntHex(String.valueOf(year)));
			timeData.setMonth(Util.getIntHex(String.valueOf(month)));
			timeData.setDay(Util.getIntHex(String.valueOf(day)));
			timeData.setHour(Util.getIntHex(String.valueOf(hour)));
			timeData.setMinute(Util.getIntHex(String.valueOf(minute)));
			timeData.setSecond(Util.getIntHex(String.valueOf(second)));
			// WW取值：星期日取0x01，星期六取值0x07。
			timeData.setDayOfWeek(Util.getIntHex(String.valueOf(week + 1)));
			SendData sendData = new SendData();
			sendData.setTimeData(timeData);
			pkg.setData(sendData);
			// 校验值要求在配置有效数据后配置
			pkg.setCrc(pkg.getCrcFromSum());
			DeviceInfo2 deviceInfo2 = getDevInfoService.getManager(mac);
			result = com.kuangchi.sdd.comm.container.Service.service(
					"set_time", pkg, deviceInfo2);
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
		if (result.equals("null")) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int setDeviceTime2(String mac, String device_type) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Util.getIntHex(device_type));
		pkg.setMac(Util.getIntHex(mac));
		pkg.setOrder(0x13);
		pkg.setLength(0x0008);
		pkg.setOrderStatus(0x00);
		String result = "";
		try {
			// 时间格式字符串转换为时间
			// SimpleDateFormat sdf = new
			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d = new Date();
			GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
			gc.setTime(d);
			int year = gc.get(Calendar.YEAR);
			int month = gc.get(Calendar.MONTH) + 1;
			int day = gc.get(Calendar.DATE);
			int hour = gc.get(Calendar.HOUR_OF_DAY);
			int minute = gc.get(Calendar.MINUTE);
			int second = gc.get(Calendar.SECOND);
			int week = gc.get(Calendar.DAY_OF_WEEK) - 1;
			// 配置有效数据
			TimeData timeData = new TimeData();
			timeData.setYear(Util.getIntHex(String.valueOf(year)));
			timeData.setMonth(Util.getIntHex(String.valueOf(month)));
			timeData.setDay(Util.getIntHex(String.valueOf(day)));
			timeData.setHour(Util.getIntHex(String.valueOf(hour)));
			timeData.setMinute(Util.getIntHex(String.valueOf(minute)));
			timeData.setSecond(Util.getIntHex(String.valueOf(second)));
			// WW取值：星期日取0x01，星期六取值0x07。
			timeData.setDayOfWeek(Util.getIntHex(String.valueOf(week + 1)));
			SendData sendData = new SendData();
			sendData.setTimeData(timeData);
			pkg.setData(sendData);
			// 校验值要求在配置有效数据后配置
			pkg.setCrc(pkg.getCrcFromSum());
			DeviceInfo2 deviceInfo2 = getDevInfoService.getManager(mac);
			result = com.kuangchi.sdd.comm.container.Service.service(
					"set_time", pkg, deviceInfo2);
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
		if (result.equals("null")) {
			return 1;
		} else {
			return 0;
		}

	}

	@Override
	public int restartDevice(String mac, String device_type) {
		SendHeader pkg = new SendHeader();
		pkg.setHeader(0x55);
		pkg.setSign(Util.getIntHex(device_type));
		pkg.setMac(Util.getIntHex(mac));
		pkg.setOrder(0x11);
		pkg.setLength(0x0000);
		pkg.setOrderStatus(0x00);
		String result = "";

		SendData sendData = new SendData();
		pkg.setData(sendData);
		pkg.setCrc(pkg.getCrcFromSum());
		// connector.setHeader(pkg);
		try {
			DeviceInfo2 deviceInfo2 = getDevInfoService.getManager(mac);
			result = com.kuangchi.sdd.comm.container.Service.service(
					"restore_device", pkg, deviceInfo2);
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
		LOG.info("容器获取的值:" + result);
		if (result.equals("null")) {
			return 1;
		} else {
			return 0;
		}
	}

	/*
	 * public static void main(String[] args) { TimeServiceImpl service = new
	 * TimeServiceImpl(); System.out.println(service.setDeviceTime2("85000a",
	 * "4"));
	 * 
	 * }
	 */

}
