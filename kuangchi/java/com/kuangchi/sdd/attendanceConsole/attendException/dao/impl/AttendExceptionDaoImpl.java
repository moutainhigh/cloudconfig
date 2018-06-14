package com.kuangchi.sdd.attendanceConsole.attendException.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.attendException.dao.IAttendExceptionDao;
import com.kuangchi.sdd.attendanceConsole.attendException.model.AttendException;
import com.kuangchi.sdd.attendanceConsole.attendException.model.Param;
import com.kuangchi.sdd.attendanceConsole.attendException.model.ToEmailAddr;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
@Repository("attendExceptionDaoImpl")
public class AttendExceptionDaoImpl extends BaseDaoImpl<AttendExceptionDaoImpl> implements IAttendExceptionDao{
	@Resource(name="LogDaoImpl")
	private LogDao logDao;

	@Override
	public Integer getAllAttExcCount(Param param) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("staff_no", param.getStaff_no());
		mapParam.put("staff_num", param.getStaff_num());
		mapParam.put("staff_name", param.getStaff_name());
		mapParam.put("exception_type", param.getException_type());
		mapParam.put("deal_state", param.getDeal_state());
		mapParam.put("begin_time", param.getBegin_time());
		mapParam.put("end_time", param.getEnd_time());
		mapParam.put("layerDeptNum",param.getLayerDeptNum());
		return queryCount("getAllAttExcCount",mapParam);
	}

	@Override
	public List<AttendException> getAllAttExcpByParam(Param param, String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("staff_no", param.getStaff_no());
		mapParam.put("staff_num", param.getStaff_num());
		mapParam.put("exception_type", param.getException_type());
		mapParam.put("staff_name", param.getStaff_name());
		mapParam.put("deal_state", param.getDeal_state());
		mapParam.put("page", (page - 1) * rows);
		mapParam.put("rows", rows);
		mapParam.put("begin_time", param.getBegin_time());
		mapParam.put("end_time", param.getEnd_time());
		mapParam.put("layerDeptNum",param.getLayerDeptNum());
		return this.getSqlMapClientTemplate().queryForList("getAllAttExcpByParam", mapParam);
	}

	@Override
	public String getNameSpace() {
		return "attendanceConsole.attendException";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<ToEmailAddr> getEmailAddr(String staff_num) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("staff_num", staff_num);
		return this.getSqlMapClientTemplate().queryForList("getEmailAddr", mapParam);
	}

	@Override
	public void setDealState(String staff_num) {
		this.getSqlMapClientTemplate().update("setDealState", staff_num);
	}

	@Override
	public List<String> getStaffNumByParam(Param param) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("staff_num", param.getStaff_num());
		mapParam.put("staff_name", param.getStaff_name());
		//mapParam.put("deal_state", param.getDeal_state());
		mapParam.put("begin_time", param.getBegin_time());
		mapParam.put("end_time", param.getEnd_time());
		return this.getSqlMapClientTemplate().queryForList("getStaffNumByParam", mapParam);
	}
    
	/*导出异常考勤信息*/
	@Override
	public List<AttendException> exportAttExcpByParam(Param param) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("staff_no", param.getStaff_no());
		mapParam.put("staff_name", param.getStaff_name());
		mapParam.put("exception_type", param.getException_type());
		mapParam.put("deal_state", param.getDeal_state());
		mapParam.put("begin_time", param.getBegin_time());
		mapParam.put("end_time", param.getEnd_time());
		mapParam.put("layerDeptNum",param.getLayerDeptNum());
		return this.getSqlMapClientTemplate().queryForList("exportAttExcpByParam", mapParam);
	}
}
