package com.kuangchi.sdd.baseConsole.countattendance.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.card.model.Card;
import com.kuangchi.sdd.baseConsole.card.model.Param;
import com.kuangchi.sdd.baseConsole.countattendance.dao.ICountAttendanceDao;
import com.kuangchi.sdd.baseConsole.countattendance.model.CountAttendance;
@Repository("countAttendanceDaoImpl")
public class CountAttendanceDaoImpl extends BaseDaoImpl<Card> implements ICountAttendanceDao{
	@Override
	public Integer getCountFromCountAttend(Param param) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("staff_name", param.getStaff_name());
		mapParam.put("begin_time", param.getBegin_time());
		mapParam.put("end_time", param.getEnd_time());
		mapParam.put("state_dm", param.getState());
		mapParam.put("staff_no", param.getStaff_no());
		mapParam.put("card_num", param.getCard_num());
		mapParam.put("device_name", param.getDevice_name());
		mapParam.put("dept_name", param.getDept_name());
		return queryCount("getCountFromCountAttend",mapParam);
	}

	@Override
	public List<CountAttendance> getAllCountAttendByParam(Param param, String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("staff_name", param.getStaff_name());
		mapParam.put("page", (page - 1) * rows);
		mapParam.put("rows", rows);
		mapParam.put("value", param.getValue());//原本是用来存card_num的，现在已经不用
		mapParam.put("begin_time", param.getBegin_time());
		mapParam.put("end_time", param.getEnd_time());
		mapParam.put("state_dm", param.getState());

		mapParam.put("staff_no", param.getStaff_no());
		mapParam.put("card_num", param.getCard_num());
		mapParam.put("device_name", param.getDevice_name());
		mapParam.put("dept_name", param.getDept_name());
		
		return this.getSqlMapClientTemplate().queryForList("getAllCountAttendByParam", mapParam);
	}

	@Override
	public String getNameSpace() {
		return "common.Count";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<CountAttendance> reportData(Param param) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("staff_name", param.getStaff_name());
		mapParam.put("begin_time", param.getBegin_time());
		mapParam.put("end_time", param.getEnd_time());
		mapParam.put("dept_name", param.getDept_name());
		mapParam.put("device_name", param.getDevice_name());
		mapParam.put("card_num", param.getCard_num());
		mapParam.put("staff_no", param.getStaff_no());
		return this.getSqlMapClientTemplate().queryForList("reportData", mapParam);
	}


}
