package com.kuangchi.sdd.attendanceConsole.myduty.service;

import java.util.List;

import com.kuangchi.sdd.attendanceConsole.myduty.model.Duty;
import com.kuangchi.sdd.attendanceConsole.myduty.model.DutyUser;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.list.blacklist.model.BlackList;
/**
 * 
 * @创建人　: 邓积辉
 * @创建时间: 2016-5-30 下午4:34:10
 * @功能描述: 查看我的班次信息
 */
public interface MyDutyService {
	
	/**按条件查询员工排班(分页)*/
	public Grid<DutyUser> getDutyUserByParamPages(DutyUser dutyUser);
	
	
	/**根据排班类型查询排班信息*/
	public Duty getDutyByDutyId (String duty_id);

}
