package com.kuangchi.sdd.attendanceConsole.attend.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.kuangchi.sdd.attendanceConsole.attend.dao.IAttendanceDao;
import com.kuangchi.sdd.attendanceConsole.attend.model.LeavetimeModel;
import com.kuangchi.sdd.attendanceConsole.attend.model.forgetcheckModel;
import com.kuangchi.sdd.attendanceConsole.attend.model.outworkModel;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;

@Repository("attendanceDaoImpl")
public class AttendanceDaoImpl extends BaseDaoImpl<LeavetimeModel> implements IAttendanceDao {

	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}
	
	//查询忘记打卡信息
	@Override
	public List<forgetcheckModel> selectAllforgetchecks(
			forgetcheckModel forgetcheck, String page, String size) {
		int Page = Integer.valueOf(page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("page", (Page-1)*rows);
		mapState.put("rows", rows);
		mapState.put("staff_no", forgetcheck.getStaff_no());
		mapState.put("staff_name", forgetcheck.getStaff_name());
		mapState.put("BM_DM", forgetcheck.getBM_MC());
		mapState.put("begin_time", forgetcheck.getBegin_time());
		mapState.put("end_time", forgetcheck.getEnd_time());
		mapState.put("layerDeptNum", forgetcheck.getLayerDeptNum());
		return this.getSqlMapClientTemplate().queryForList("selectAllforgetchecks", mapState); 
	}
	//查询忘记打卡信息总行数
	@Override
	public Integer getAllforgetcheckCount(forgetcheckModel forgetcheck) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("staff_no", forgetcheck.getStaff_no());
		mapState.put("staff_name", forgetcheck.getStaff_name());
		mapState.put("BM_DM", forgetcheck.getBM_MC());
		mapState.put("begin_time", forgetcheck.getBegin_time());
		mapState.put("end_time", forgetcheck.getEnd_time());
		mapState.put("layerDeptNum", forgetcheck.getLayerDeptNum());
		
		return queryCount("getAllforgetcheckCount",mapState);
	}
	
	//查询请假信息
	@Override
	public List<LeavetimeModel> selectAllleavetimes(LeavetimeModel leavetime,
			String page, String size) {
		int Page = Integer.valueOf(page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("page", (Page-1)*rows);
		mapState.put("rows", rows);
		mapState.put("staff_no", leavetime.getStaff_no());
		mapState.put("staff_name", leavetime.getStaff_name());
		mapState.put("BM_DM", leavetime.getBM_MC());
		mapState.put("begin_time", leavetime.getBegin_time());
		mapState.put("end_time", leavetime.getEnd_time());
		mapState.put("layerDeptNum", leavetime.getLayerDeptNum());
		
		return this.getSqlMapClientTemplate().queryForList("selectAllleavetimes", mapState);
	}
	//查询请假信息总行数
	@Override
	public Integer getAllleavetimeCount(LeavetimeModel leavetime) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("staff_no", leavetime.getStaff_no());
		mapState.put("staff_name", leavetime.getStaff_name());
		mapState.put("BM_DM", leavetime.getBM_MC());
		mapState.put("begin_time", leavetime.getBegin_time());
		mapState.put("end_time", leavetime.getEnd_time());
		mapState.put("layerDeptNum", leavetime.getLayerDeptNum());
		return queryCount("getAllleavetimeCount",mapState);
	}
	
	//查询外出信息
	@Override
	public List<outworkModel> selectAllOutwork(outworkModel outworks,
			String page, String rows) {
		int pages = Integer.valueOf(page);
		int rowss = Integer.valueOf(rows);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", (pages-1)*rowss);
		map.put("rows", rowss);
		map.put("staff_no", outworks.getStaff_no());
		map.put("staff_name", outworks.getStaff_name());
		map.put("BM_DM", outworks.getBM_MC());
		map.put("begin_time", outworks.getBegin_time());
		map.put("end_time", outworks.getEnd_time());
		map.put("layerDeptNum", outworks.getLayerDeptNum());
		return this.getSqlMapClientTemplate().queryForList("selectAllOutwork", map);
	}
	//查询外出信息总行数
		@Override
		public Integer getAllOutworksCount(outworkModel outworks) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("staff_no", outworks.getStaff_no());
			map.put("staff_name", outworks.getStaff_name());
			map.put("BM_DM", outworks.getBM_MC());
			map.put("begin_time", outworks.getBegin_time());
			map.put("end_time", outworks.getEnd_time());
			map.put("layerDeptNum", outworks.getLayerDeptNum());
			return queryCount("getAllOutworksCount",map);
		}
	//导出请假所有信息
	@Override
	public List<LeavetimeModel> exportLeavetime(LeavetimeModel leavetime) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("staff_no", leavetime.getStaff_no());
		mapState.put("staff_name", leavetime.getStaff_name());
		mapState.put("BM_DM", leavetime.getBM_MC());
		mapState.put("begin_time", leavetime.getBegin_time());
		mapState.put("end_time", leavetime.getEnd_time());
		mapState.put("layerDeptNum", leavetime.getLayerDeptNum());
		return this.getSqlMapClientTemplate().queryForList("exportLeavetime", mapState);
	}
	//导出外出所有信息
	@Override
	public List<outworkModel> exportOutwork(outworkModel outworks) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("staff_no", outworks.getStaff_no());
		map.put("staff_name", outworks.getStaff_name());
		map.put("BM_DM", outworks.getBM_MC());
		map.put("begin_time", outworks.getBegin_time());
		map.put("end_time", outworks.getEnd_time());
		map.put("layerDeptNum", outworks.getLayerDeptNum());
		return this.getSqlMapClientTemplate().queryForList("exportOutwork", map);
	}
	
	/*导出忘记打卡记录*/
	@Override
	public List<forgetcheckModel> exportforgetchecks(
			forgetcheckModel forgetcheck) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("staff_no", forgetcheck.getStaff_no());
		mapState.put("staff_name", forgetcheck.getStaff_name());
		mapState.put("BM_DM", forgetcheck.getBM_MC());
		mapState.put("begin_time", forgetcheck.getBegin_time());
		mapState.put("end_time", forgetcheck.getEnd_time());
		mapState.put("layerDeptNum", forgetcheck.getLayerDeptNum());
		return this.getSqlMapClientTemplate().queryForList("exportforgetchecks", mapState);
	}
	//根据ID查询请假信息
	@Override
	public List<LeavetimeModel> selectLeavetimeById(LeavetimeModel leavetime,String page, String size) {
		int Page = Integer.valueOf(page);
		int rows = Integer.valueOf(size);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", (Page-1)*rows);
		map.put("rows", rows);
		map.put("staff_num", leavetime.getStaff_num());
		map.put("begin_time", leavetime.getBegin_time());
		map.put("end_time", leavetime.getEnd_time());
		return this.getSqlMapClientTemplate().queryForList("selectLeavetimeById", map);
	}
	//根据ID查询忘打卡信息
	@Override
	public List<forgetcheckModel> selectForgetcheckById(forgetcheckModel forgetcheck,String page, String size) {
		int Page = Integer.valueOf(page);
		int rows = Integer.valueOf(size);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", (Page-1)*rows);
		map.put("rows", rows);
		map.put("staff_num", forgetcheck.getStaff_num());
		map.put("begin_time", forgetcheck.getBegin_time());
		map.put("end_time", forgetcheck.getEnd_time());
		return this.getSqlMapClientTemplate().queryForList("selectForgetcheckById", map);
	}
	//根据ID查询外出信息
	@Override
	public List<outworkModel> selectOutworkById(outworkModel outworks,String page, String size) {
		int Page = Integer.valueOf(page);
		int rows = Integer.valueOf(size);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("page", (Page-1)*rows);
		map.put("rows", rows);
		map.put("staff_num", outworks.getStaff_num());
		map.put("begin_time", outworks.getBegin_time());
		map.put("end_time", outworks.getEnd_time());
		return this.getSqlMapClientTemplate().queryForList("selectOutworkById", map);
	}

	@Override
	public Integer selectLeavetimeByIdCount(LeavetimeModel leavetime) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("staff_num", leavetime.getStaff_num());
		map.put("begin_time", leavetime.getBegin_time());
		map.put("end_time", leavetime.getEnd_time());
		return queryCount("selectLeavetimeByIdCount",map);
	}

	@Override
	public Integer selectForgetcheckByIdCount(forgetcheckModel forgetcheck) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("staff_num", forgetcheck.getStaff_num());
		map.put("begin_time", forgetcheck.getBegin_time());
		map.put("end_time", forgetcheck.getEnd_time());
		return queryCount("selectForgetcheckByIdCount",map);
	}

	@Override
	public Integer selectOutworkByIdCount(outworkModel outworks) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("staff_num", outworks.getStaff_num());
		map.put("begin_time", outworks.getBegin_time());
		map.put("end_time", outworks.getEnd_time());
		return queryCount("selectOutworkByIdCount",map);
	}

	@Override
	public List<Map> getOtRecords(Map map) {
		return getSqlMapClientTemplate().queryForList("getOtRecords", map) ;
	}

	@Override
	public Integer countOtRecords(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("countOtRecords", map);
	}

	@Override
	public List<Map> getAllOts(Map map) {
		return getSqlMapClientTemplate().queryForList("getAllOts", map);
	}

	@Override
	public Integer countAllOts(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("countAllOts", map);
	}

	@Override
	public List<Map> exportAllOts(Map map) {
		return getSqlMapClientTemplate().queryForList("exportAllOts", map);
	}

	@Override
	public List<Map> getMyCancelLeaveTime(Map map) {
		return getSqlMapClientTemplate().queryForList("getMyCancelLeaveTime", map);
	}

	@Override
	public Integer countMyCancelLeaveTime(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("countMyCancelLeaveTime", map);
	}

	@Override
	public List<Map> getAllCancelLeavetimes(Map map) {
		return getSqlMapClientTemplate().queryForList("getAllCancelLeavetimes", map);
	}

	@Override
	public Integer countAllCancelLeavetimes(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("countAllCancelLeavetimes", map);
	}

	@Override
	public List<Map> exportAllCancelLeavetimes(Map map) {
		return getSqlMapClientTemplate().queryForList("exportAllCancelLeavetimes", map);
	}
	
}
