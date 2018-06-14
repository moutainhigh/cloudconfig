package com.kuangchi.sdd.elevatorConsole.holiday.util;

import java.util.List;

public class HolidayUtil {
	public static Integer isExistHoliInList(List<String> holidayList,
			String holiday) {
		if (holidayList != null && holidayList.size() > 0) {
			for (String holi : holidayList) {
				if (holi.equals(holiday)) {
					return 1;
				}
			}
			return 0;
		} else {
			return 0;
		}
	}
}
