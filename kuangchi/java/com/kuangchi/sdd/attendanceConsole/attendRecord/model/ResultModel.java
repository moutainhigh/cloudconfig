package com.kuangchi.sdd.attendanceConsole.attendRecord.model;

import java.util.List;

import com.kuangchi.sdd.attendanceConsole.statistic.model.AttendanceDataDate;
import com.kuangchi.sdd.base.model.JsonResult;

public class ResultModel extends JsonResult{
	
	private List<AttendRecordModel> attendRecordList;
	private boolean noDate;
	
	public boolean isNoDate() {
		return noDate;
	}

	public void setNoDate(boolean noDate) {
		this.noDate = noDate;
	}

	public List<AttendRecordModel> getAttendRecordList() {
		return attendRecordList;
	}

	public void setAttendRecordList(List<AttendRecordModel> attendRecordList) {
		this.attendRecordList = attendRecordList;
	}
	
	
}
