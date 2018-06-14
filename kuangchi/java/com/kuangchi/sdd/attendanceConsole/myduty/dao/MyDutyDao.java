package com.kuangchi.sdd.attendanceConsole.myduty.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.myduty.model.Duty;
import com.kuangchi.sdd.attendanceConsole.myduty.model.DutyUser;
@Repository("myDutyUserDaoImpl")
public interface MyDutyDao {
	
	/**按条件查询员工排班(分页)*/
	List<DutyUser> getDutyUserByParamPages(DutyUser dutyUser);
	
	/**按条件查询员工排班总数*/
	int getDutyUserByParamCounts(DutyUser dutyUser);
	
	/**根据排班类型查询排班信息*/
	Duty getDutyByDutyId(String duty_id);
}
