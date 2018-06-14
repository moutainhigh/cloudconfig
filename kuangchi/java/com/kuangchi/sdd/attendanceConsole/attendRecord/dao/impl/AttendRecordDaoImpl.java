package com.kuangchi.sdd.attendanceConsole.attendRecord.dao.impl;



import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.attendRecord.dao.IAttendRecordDao;
import com.kuangchi.sdd.attendanceConsole.attendRecord.model.AttendRecordModel;
import com.kuangchi.sdd.attendanceConsole.attendRecord.model.Outwork;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;

@Repository("attendRecordDao")
public class AttendRecordDaoImpl extends BaseDaoImpl<Object> implements IAttendRecordDao {

	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<AttendRecordModel> getAttendRecord(Map map) {
		return this.getSqlMapClientTemplate().queryForList("getWeekAttendanceData", map);
	}

	@Override
	public List<String> getExceptionType(Map map) {
		return this.getSqlMapClientTemplate().queryForList("getExceptionType", map);
	}

	@Override
	public Boolean addOutworkInfo(Outwork work) {
		return insert("addOutworkInfo",work);
	}

	@Override
	public Boolean deleteOutworkInfo(String id) {
		return delete("deleteOutworkInfo",id);
	}

	@Override
	public Boolean editOutworkInfo(Outwork work) {
		return update("editOutworkInfo",work);
	}

	@Override
	public List<Outwork> selectOutworkInfoById(String id) {
		return this.getSqlMapClientTemplate().queryForList("selectOutworkInfoById", id);
	}

	

}
