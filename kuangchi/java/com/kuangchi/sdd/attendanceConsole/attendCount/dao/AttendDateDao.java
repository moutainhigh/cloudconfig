package com.kuangchi.sdd.attendanceConsole.attendCount.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.attendCount.model.AttendDateModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.DutyInfoModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.LeaveTimeModel;
import com.kuangchi.sdd.attendanceConsole.attendCount.model.OutWorkModel;

public interface AttendDateDao {
	public List<AttendDateModel> getAllAttendDate1(Map<String,Object> map);
	public Integer countAllAttendDate1(Map<String,Object> map);
	public List<AttendDateModel> exportAllDateToExcel(Map<String,Object> map);
	
	public List<LeaveTimeModel> getLeaveDetailList(String staffNum,String startTime,String endTime);
	public List<OutWorkModel>	getOutDetailList(String staffNum,String startTime,String endTime);
 	public AttendDateModel getAttendDateByStaffNumAndDutyId(String staffNum,String dutyId,String everyDate);
 	public DutyInfoModel getDutyModel(String dutyId);
 	public Map askIfMidCheck(String dutyId);
 	
 	
 	// 根据员工和日期查询请假信息
 	public List<Map> getLeaveInfo(Map map);
 	
 	// 根据员工和日期查询外出信息
 	public List<Map> getOutInfo(Map map);
}
