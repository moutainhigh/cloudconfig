package com.kuangchi.sdd.elevatorConsole.holiday.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.elevatorConsole.device.service.ITKDeviceService;
import com.kuangchi.sdd.elevatorConsole.holiday.dao.HolidayDao;
import com.kuangchi.sdd.elevatorConsole.holiday.model.Holiday;
import com.kuangchi.sdd.elevatorConsole.holiday.service.HolidayService;

/**
 * @创建人　: 杨金林
 * @创建时间: 2016-3-24 16:03:51
 * @功能描述: 节假日模块-业务实现类
 */
@Transactional
@Service("elevatorHolidayServiceImpl")
public class HolidayServiceImpl implements HolidayService {

	@Resource(name = "elevatorHolidayDaoImpl")
	private HolidayDao holidayDao;

	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	@Resource(name = "tkDeviceService")
	ITKDeviceService deviceService;

	/*
	 * @Resource(name = "tkDeviceService") private ITKDeviceService
	 * tkDeviceService;
	 */

	@Override
	public List<Holiday> getHolidayByParamPage(Map<String, Object> map) {

		try {
			List<Holiday> list1 = new ArrayList<Holiday>();
			List<Holiday> list0 = holidayDao.getHolidayByParamPage(map);

			Date day = new Date();// 获取当前日期
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 日期格式化
			String vali_date = df.format(day);

			Date date = null;
			try {
				date = df.parse(vali_date);// 格式化当前日期
			} catch (ParseException e) {
				e.printStackTrace();
			}
			long sysTime = date.getTime();// 获取当前日期的毫秒数
			for (Holiday holi : list0) {
				if (sysTime > df.parse(holi.getHoliday_date()).getTime()) {
					holi.setValid_date("1");
				} else {
					holi.setValid_date("0");
				}
				list1.add(holi);
			}

			return list1;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getHolidayByParamCount(Map<String, Object> map) {
		return holidayDao.getHolidayByParamCount(map);
	}

	@Override
	public boolean addHoliday(Map<String, Object> map, String loginUser) {
		boolean result = holidayDao.addHoliday(map);

		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "节假日信息管理");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", loginUser);
		log.put("V_OP_MSG", "新增节假日信息");
		if (result) {
			log.put("V_OP_TYPE", "业务");
		} else {
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);

		return result;
	}

	@Override
	public boolean deleteHolidayById(String holiday_ids, String loginUser) {
		boolean result = holidayDao.deleteHolidayById(holiday_ids);

		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "节假日信息管理");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", loginUser);
		log.put("V_OP_MSG", "删除节假日信息");
		if (result) {
			log.put("V_OP_TYPE", "业务");
		} else {
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);

		return result;
	}

	@Override
	public boolean modifyHoliday(Map<String, Object> map, String loginUser) {

		boolean result = holidayDao.modifyHoliday(map);
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "节假日信息管理");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", loginUser);
		log.put("V_OP_MSG", "删除节假日信息");
		if (result) {
			log.put("V_OP_TYPE", "业务");
		} else {
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);

		return result;
	}

	@Override
	public void batchAddHoliday(List<Map> holidayList) {
		// 将节假日插入设备

		/*
		 * for (int i = 0; i < holidayList.size(); i++) { Map holiday =
		 * holidayList.get(i); List<String> holiList = new ArrayList<String>();
		 * String device_num = (String) holiday.get("device_num"); String
		 * holiday_date = (String) holiday.get("holiday_date"); String
		 * description = (String) holiday.get("description");
		 * holiList.add(holiday_date); if
		 * (tkDeviceService.commHoliday(device_num, holiList)) {
		 * 
		 * } // 插入数据库sqlMapClient.insert("addElevatorHoliday",holiday); }
		 */

		// 成功后插入数据库
		holidayDao.batchAddHoliday(holidayList);
	}

	@Override
	public Holiday getHolidayByNum(Map<String, Object> map) {
		return holidayDao.getHolidayByNum(map);
	}

	@Override
	public List<String> getHoliByDevice(String device_num) {
		return holidayDao.getHoliByDevice(device_num);
	}

	@Override
	public boolean isExistHoli(String device_num, String dateStr) {

		// List<String> holiList = holidayDao.getAllHoli();
		List<String> holiList = holidayDao.getHoliByDevice(device_num);
		for (String holiCurrDate : holiList) {
			if (dateStr.equals(holiCurrDate)) {
				return true;
			}

		}
		return false;
	}

	@Override
	public boolean isSend(String dateStr) {
		Integer count = holidayDao.getSendDateCount(dateStr);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<String> getHoliByDevice2(String device_num) {
		return holidayDao.getHoliByDevice2(device_num);
	}

	@Override
	public boolean deleteHolidayById2(String device_num, String holiday_ids,
			String loginUser) {

		boolean result = false;
		try {
			List<String> holidayDateList = holidayDao.getHoliByDevForDel(
					device_num, holiday_ids);
			result = deviceService.commHoliday(device_num, holidayDateList);
			if (result) {
				result = holidayDao.deleteHolidayById(holiday_ids);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
		}

		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "节假日信息管理");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", loginUser);
		log.put("V_OP_MSG", "删除节假日信息");
		if (result) {
			log.put("V_OP_TYPE", "业务");
		} else {
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);

		return result;

	}
}
