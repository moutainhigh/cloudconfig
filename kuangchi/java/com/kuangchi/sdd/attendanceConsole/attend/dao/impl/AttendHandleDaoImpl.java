package com.kuangchi.sdd.attendanceConsole.attend.dao.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.kuangchi.sdd.attendanceConsole.attend.dao.IAttendHandleDao;
import com.kuangchi.sdd.attendanceConsole.attend.model.AttendModel;
import com.kuangchi.sdd.attendanceConsole.attend.model.LeavetimeModel;
import com.kuangchi.sdd.attendanceConsole.attend.model.PunchCardModel;
import com.kuangchi.sdd.attendanceConsole.duty.model.Duty;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;

@Repository("attendHandleDaoImpl")
public class AttendHandleDaoImpl extends BaseDaoImpl<LeavetimeModel> implements IAttendHandleDao {

	@Override
	public String getNameSpace() {
		return "attendanceConsole.attendHandle";
	}

	@Override
	public String getTableName() {
		return null;
	}

	//查询个人全部考勤信息
	public List<AttendModel> getMyAttend(AttendModel attendModel) {
		
		return this.getSqlMapClientTemplate().queryForList("getMyAttend", attendModel);
	}

	//查询指定员工考勤信息
	public List<AttendModel> getAllAttend(AttendModel attendModel) {
		
		return this.getSqlMapClientTemplate().queryForList("getAllAttend", attendModel);
	}

	//增加考勤信息
	public void addAttendInfo(AttendModel attendModel) {
		List list = this.queryForList("getStaffByNum", attendModel.getStaff_num());
		String staff_name = (String) list.get(0);
		attendModel.setStaff_name(staff_name);
		insert("addAttendInfo", attendModel);
	}

	//删除考勤信息
	public void deleteAttendInfoById(String ids) {
		this.delete("deleteAttendInfoById", ids);
	}
	//备份后删除月记录
	public void deleteAttendInfoByMonth(List<AttendModel> attendList) {
		this.delete("deleteAttendInfoByMonth", attendList);
	}
	
	//查询个人考勤信息总数
	public Integer getMyAttendCount(AttendModel attendModel) {
		return this.queryCount("getMyAttendCount", attendModel);
	}

	//查询指定员工考勤信息总数
	public Integer getAllAttendCount(AttendModel attendModel) {
		return this.queryCount("getAllAttendCount", attendModel);
	}

	//查询员工班次信息
	public Duty getDutyInfo(AttendModel attendModel) throws SQLException {
		List duty = this.getSqlMapClientTemplate().queryForList("getDutyId", attendModel);
		if(duty != null && duty.size() != 0){
			Integer id = (Integer)duty.get(0);
			return (Duty)this.getSqlMapClient().queryForObject("getDutyInfo", id);
		}
		return null;
	}

	@Override
	public void delNoElaExceRecord(String staffNum, String startTime,
			String endTime) {
		Map map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		getSqlMapClientTemplate().delete("delNoElaExceRecord", map);
	}

	@Override
	public void delElaExceRecord(String staffNum, String timePoint) {
		Map map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("timePoint", timePoint);
		getSqlMapClientTemplate().update("delElaExceRecord", map);
	}

	@Override
	public List<Map> getUpElaExceptionList(String staffNum, String endTime) {
		Map map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("endTime", endTime);
		return getSqlMapClientTemplate().queryForList("getUpElaExceptionList", map);
	}

	@Override
	public List<Map> getDownElaExceptionList(String staffNum, String startTime) {
		Map map=new HashMap<String,Object>();
		map.put("staffNum", staffNum);
		map.put("startTime", startTime);
		return getSqlMapClientTemplate().queryForList("getDownElaExceptionList", map);
	}

	@Override
	public boolean removeAreaRecord(String partition_name) {
		return this.delete("removeAreaRecord", partition_name);
	}
	
	@Override
	public List<PunchCardModel> getDutyUserStaffById(PunchCardModel handle,String Page,String row) {
		Map<String,Object> map=new HashMap<String,Object>();
		Integer page=Integer.valueOf(Page);
		Integer rows=Integer.valueOf(row);
		map.put("page", (page-1)*rows);
		map.put("rows", rows);
		map.put("id", handle.getId());
		map.put("checkdate", handle.getCheckdate()+" "+handle.getChecktime());
		map.put("staff_no", handle.getStaff_no());
		map.put("staff_name", handle.getStaff_name());
		map.put("dept_num", handle.getDept_num());
		map.put("layerDeptNum", handle.getLayerDeptNum());
		return getSqlMapClientTemplate().queryForList("getDutyUserStaffById", map);
	}


	@Override
	public Integer getDutyUserStaffByIdCount(PunchCardModel handle) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", handle.getId());
		map.put("checkdate", handle.getCheckdate()+" "+handle.getChecktime());
		map.put("staff_no", handle.getStaff_no());
		map.put("staff_name", handle.getStaff_name());
		map.put("dept_num", handle.getDept_num());
		map.put("layerDeptNum", handle.getLayerDeptNum());
		return queryCount("getDutyUserStaffByIdCount", map);
	}
	
	//查询默认班次
	@Override
	public List<PunchCardModel> getdutyId() {
		return getSqlMapClientTemplate().queryForList("getdutyId", "");
	}
	
	//查询非弹性时间
	@Override
	public List<PunchCardModel> getdutyTimeById(String id) {
		return getSqlMapClientTemplate().queryForList("getdutyTimeById", id);
	}

	@Override
	public String getIsElasticById(String id) {
		return (String) getSqlMapClientTemplate().queryForObject("getIsElasticById", id);
	}

	@Override
	public void addDutyAttendInfo(PunchCardModel handle) {
		insert("addDutyAttendInfo", handle);
		
	}

	@Override
	public PunchCardModel getCheckTimeById(String id) {
		return (PunchCardModel) getSqlMapClientTemplate().queryForObject("getCheckTimeById", id);
	}

	@Override
	public List<PunchCardModel> getPunchCardInfo(PunchCardModel handle,String Page,String row) {
		Map<String,Object> map=new HashMap<String,Object>();
		Integer page=Integer.valueOf(Page);
		Integer rows=Integer.valueOf(row);
		map.put("page", (page-1)*rows);
		map.put("rows", rows);
		map.put("staff_num", handle.getStaff_num());
		map.put("staff_name", handle.getStaff_name());
		return getSqlMapClientTemplate().queryForList("getPunchCardInfo", map);
	}

	@Override
	public Integer getPunchCardInfoCount(PunchCardModel handle) {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("staff_num", handle.getStaff_num());
		map.put("staff_name", handle.getStaff_name());
		return queryCount("getPunchCardInfoCount", map);
	}

	@Override
	public void PunchCardsAdd(List<PunchCardModel> punchList) {
		 SqlMapClient sqlMapClient= getSqlMapClient();
	     try {
			sqlMapClient.startBatch();
			sqlMapClient.startTransaction();
			for (int i = 0; i < punchList.size(); i++) {
				PunchCardModel punchcard=punchList.get(i);
				sqlMapClient.insert("addDutyAttendInfo", punchcard);
			}
			 sqlMapClient.commitTransaction();  			
		} catch (Exception e) {
			try {
				sqlMapClient.getCurrentConnection().rollback();
			} catch (Exception e2) {
			}
			e.printStackTrace();
		}
		
	}

	@Override
	public List<AttendModel> getAllExportAttend(AttendModel attendModel) {
		return this.getSqlMapClientTemplate().queryForList("getAllExportAttend", attendModel);
	}

	@Override
	public Map getStaffByNO(String staff_no, String staff_name) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("staff_no", staff_no);
		map.put("staff_name", staff_name);
		return (Map) this.getSqlMapClientTemplate().queryForObject("getStaffByNO", map);
	}

}
