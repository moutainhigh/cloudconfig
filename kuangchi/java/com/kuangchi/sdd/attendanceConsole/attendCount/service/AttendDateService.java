package com.kuangchi.sdd.attendanceConsole.attendCount.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendDateModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendDetailInfoModel;
import com.kuangchi.sdd.base.model.easyui.Grid;

public interface AttendDateService {
	
	/**
	 * 支持多条件模糊查询，可传 null
	 * @param staffName
	 * @param staffNum
	 * @param deptNum 是经过拼装的 deptNum 如 "1,2,3"
	 * @return
	 */
	public Grid<AttendDateModel> getAllAttendDate(Map<String, Object> map);
	public List<AttendDateModel> exportAllToExcel(Map<String, Object> map);
	public List<AttendDetailInfoModel> getDetailByStaffNumAndDutyId(String staffNum,String dutyId,String everyDate);
}
