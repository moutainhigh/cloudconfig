package com.kuangchi.sdd.baseConsole.countattendance.service;

import java.util.List;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.card.model.Param;
import com.kuangchi.sdd.baseConsole.countattendance.model.CountAttendance;

public interface ICountAttendanceService {
	/**
	 * 
	 * Description:根据查询条件门禁刷卡信息
	 * date:2016年3月14日
	 * @param param
	 * @return
	 */
	Grid getAllCountAttendByParam(Param param,String page, String size);
	/**
	 * 根据条件导出出入统计  
	 * @param param
	 */
	public List<CountAttendance> reportData(Param param);
}
