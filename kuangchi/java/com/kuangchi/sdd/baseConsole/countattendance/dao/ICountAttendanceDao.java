package com.kuangchi.sdd.baseConsole.countattendance.dao;

import java.util.List;

import com.kuangchi.sdd.baseConsole.card.model.Param;
import com.kuangchi.sdd.baseConsole.countattendance.model.CountAttendance;

public interface ICountAttendanceDao {
	Integer getCountFromCountAttend(Param param);
	List<CountAttendance> getAllCountAttendByParam(Param param,String page, String size);
	/**
	 * 根据条件导出出入统计  
	 * @param param
	 */
	public List<CountAttendance> reportData(Param param);
}
