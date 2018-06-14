package com.kuangchi.sdd.elevatorConsole.device.quartz.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.businessConsole.cron.service.ICronService;
import com.kuangchi.sdd.elevatorConsole.device.service.ITKDeviceService;
import com.kuangchi.sdd.elevatorConsole.holiday.dao.HolidayDao;
import com.kuangchi.sdd.elevatorConsole.holiday.service.HolidayService;

@Transactional
@Service("holidayOverStatedQuartz")
public class HolidayOverStatedQuartz {
	@Resource(name = "tkDeviceService")
	ITKDeviceService deviceService;

	@Resource(name = "cronServiceImpl")
	private ICronService cronService;

	@Resource(name = "elevatorHolidayDaoImpl")
	private HolidayDao holidayDao;

	@Resource(name = "elevatorHolidayServiceImpl")
	 private HolidayService holidayService;

	// static Semaphore semaphore2 = new Semaphore(1);

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-11-1 下午3:42:26
	 * @功能描述: 每个月月底中午12:00定时清除节假日
	 * @参数描述:
	 */
	public void cleanHolidayOverStated() {
		// boolean getResource = false;
		/*
		 * try { getResource = semaphore2.tryAcquire(1, TimeUnit.SECONDS); }
		 * catch (Exception e) { e.printStackTrace(); return; }
		 */
		// if (getResource) {
		boolean r = cronService.compareIP();
		if (r) {
			try {

				List<String> list = holidayDao.getAllHoli();
				Date date = new Date();
				SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
				String now_date = simpleDate.format(date);
				List<String> listStr = new ArrayList<String>();
				Date dt = null;
				try {
					dt = simpleDate.parse(now_date);// 格式化当前日期
				} catch (ParseException e) {
					e.printStackTrace();
				}
				long sysTime = dt.getTime();
				for (String str : list) {
					if (simpleDate.parse(str).getTime() < sysTime) {
						listStr.add(str);
					}
				}
				for (String str : listStr) {
					holidayDao.deleteHolidaybyDate(str);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				/* semaphore2.release(); */
			}
		}
	}
}
