package com.kuangchi.sdd.attendanceConsole.attendCountType.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.attendCountType.dao.AttendCountTypeDao;
import com.kuangchi.sdd.attendanceConsole.attendCountType.model.AttendCountTypeModel;
import com.kuangchi.sdd.attendanceConsole.attendCountType.model.StaffAttendCount;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;

@Repository("attendCountTypeDaoImpl")
public class AttendCountTypeDaoImpl extends BaseDaoImpl<Object> implements AttendCountTypeDao {

	@Override
	public String getNameSpace() {
		return "common.attendCountType";
	}
	@Override
	public String getTableName() {
		return null;
	}
	@Override
	public List<AttendCountTypeModel> getLeaveStatis(AttendCountTypeModel model) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("staff_no", model.getStaff_no());
		map.put("staff_name", model.getStaff_name());
		map.put("bm_dm", model.getBm_dm());
		map.put("begin_time", model.getBegin_time());
		map.put("end_time", model.getEnd_time());
		map.put("layerDeptNum", model.getLayerDeptNum());
		return getSqlMapClientTemplate().queryForList("getLeaveStatis", map);
	}
	@Override
	public List<AttendCountTypeModel> getAttendExceptionTotalTime(AttendCountTypeModel model) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("staff_no", model.getStaff_no());
		map.put("staff_name", model.getStaff_name());
		map.put("bm_dm", model.getBm_dm());
		map.put("begin_time", model.getBegin_time());
		map.put("end_time", model.getEnd_time());
		map.put("exception_type", model.getException_type());
		map.put("layerDeptNum", model.getLayerDeptNum());
		return getSqlMapClientTemplate().queryForList("getAttendExceptionTotalTime", map);
	}
	@Override
	public List<AttendCountTypeModel> getAttendExceptionTotalNumber(AttendCountTypeModel model) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("staff_no", model.getStaff_no());
		map.put("staff_name", model.getStaff_name());
		map.put("bm_dm", model.getBm_dm());
		map.put("begin_time", model.getBegin_time());
		map.put("end_time", model.getEnd_time());
		map.put("exception_type", model.getException_type());
		map.put("layerDeptNum", model.getLayerDeptNum());
		return getSqlMapClientTemplate().queryForList("getAttendExceptionTotalNumber", map);
	}
	@Override
	public List<AttendCountTypeModel> getExceptionTotalTimeByStaffNum(String staff_num,String exception_type,String begin_time,String end_time) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("staff_num", staff_num);
		map.put("begin_time", begin_time);
		map.put("end_time", end_time);
		if("3".equals(exception_type) ||"4".equals(exception_type)){
			map.put("exception_type", "3,4");
		}else{
			map.put("exception_type", exception_type);
		}
		
		return getSqlMapClientTemplate().queryForList("getExceptionTotalTimeByStaffNum", map);
	}
	/*@Override
	public List<AttendCountTypeModel> getAbsenteeism(AttendCountTypeModel model) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("staff_no", model.getStaff_no());
		map.put("staff_name", model.getStaff_name());
		map.put("bm_dm", model.getBm_dm());
		map.put("begin_time", model.getBegin_time());
		map.put("end_time", model.getEnd_time());
		map.put("layerDeptNum", model.getLayerDeptNum());
		return getSqlMapClientTemplate().queryForList("getAbsenteeism", map);
	}*/
	@Override
	public List<Map> getAttendLeaveCountList(Map map) {
		return getSqlMapClientTemplate().queryForList("getAttendLeaveCountList", map) ;
	}
	@Override
	public Integer getAttendLeaveCount(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getAttendLeaveCount", map);
	}
	
	@Override
	public List<Map> getAttendOutWorkCountList(Map map) {
		return getSqlMapClientTemplate().queryForList("getAttendOutWorkCountList", map) ;
	}
	
	@Override
	public Integer getAttendOutWorkCount(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getAttendOutWorkCount", map);
	}
	
	@Override
	public List<Map> exportAttendOutWorkCount(Map map) {
		return getSqlMapClientTemplate().queryForList("exportAttendOutWorkCount", map) ;
	}
	@Override
	public List<Map> exportAttendLeaveCount(Map map) {
		return getSqlMapClientTemplate().queryForList("exportAttendLeaveCount", map) ;
	}
	
	@Override
	public List<AttendCountTypeModel> getExceptionByDept(Map map) {
		return getSqlMapClientTemplate().queryForList("getExceptionByDept", map) ;
		
	}
	@Override
	public List<StaffAttendCount> getStaffAttendCountList(Map map) {
		return getSqlMapClientTemplate().queryForList("getStaffAttendCountList", map) ;
	}
	@Override
	public Integer getStaffAttendCount(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getStaffAttendCount", map);
	}
	@Override
	public Map getFristCheckByTime(Map map) {
		return (Map)getSqlMapClientTemplate().queryForObject("getFristCheckByTime", map) ;
	}
	
	@Override
	public Map getLastCheckByTime(Map map) {
		return (Map)getSqlMapClientTemplate().queryForObject("getLastCheckByTime", map) ;
	}
	@Override
	public List<Double> getLeaveTimeById(Map map) {
		return getSqlMapClientTemplate().queryForList("getLeaveTimeById", map) ;
	}


}
