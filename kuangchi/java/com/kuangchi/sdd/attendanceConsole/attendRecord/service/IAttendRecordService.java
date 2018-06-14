package com.kuangchi.sdd.attendanceConsole.attendRecord.service;


import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.attendRecord.model.AttendRecordModel;
import com.kuangchi.sdd.attendanceConsole.attendRecord.model.Outwork;


public interface IAttendRecordService {
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-23 下午5:56:04
	 * @功能描述: 根据员工编号查询本周考勤记录
	 * @参数描述:
	 */
	public List<AttendRecordModel> getAttendRecord(Map map);
	
	public Boolean addOutworkInfo(Outwork work);//添加农行外出记录
	
	public Boolean deleteOutworkInfo(String id);//农行删除外出记录

	public Boolean editOutworkInfo(Outwork work);//农行修改外出记录
	
	public List<Outwork> selectOutworkInfoById(String id);//根据id查询外出记录信息
	
}
