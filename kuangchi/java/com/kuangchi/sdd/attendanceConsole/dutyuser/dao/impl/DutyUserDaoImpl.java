package com.kuangchi.sdd.attendanceConsole.dutyuser.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.dutyuser.dao.DutyUserDao;
import com.kuangchi.sdd.attendanceConsole.dutyuser.model.DutyUser;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.base.dao.BaseDaoSupport;

@Repository("dutyUserDaoImpl")
public class DutyUserDaoImpl extends BaseDaoImpl<DutyUser> implements DutyUserDao {

	@Override
	public List<DutyUser> getDutyUserByParam(DutyUser dutyUser) {
		return getSqlMapClientTemplate().queryForList("getDutyUserByParam", dutyUser);
	}

	@Override
	public List<DutyUser> getDutyUserByParamPage(DutyUser dutyUser) {
		return getSqlMapClientTemplate().queryForList("getDutyUserByParamPage", dutyUser);
	}

	@Override
	public int getDutyUserByParamPageCounts(DutyUser dutyUser) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getDutyUserByParamPageCounts", dutyUser);
	}

	@Override
	public boolean insertDutyUser(List<DutyUser> dutyUser) {
		return insert("insertDutyUser",dutyUser);
	}

	@Override
	public boolean deleteDutyUserById(String dutyUser_ids) {
		return delete("deleteDutyUserById",dutyUser_ids);
	}


	@Override
	public boolean updateDutyUser(DutyUser dutyUser) {
		return update("updateDutyUser",dutyUser);
	}
	
	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public DutyUser getDutyUserById(String dutyUser_id) {
		return (DutyUser) getSqlMapClientTemplate().queryForObject("getDutyUserById", dutyUser_id);
	}

	@Override
	public Integer getDutyUserByParamCounts(DutyUser dutyUser) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getDutyUserByParamCounts",dutyUser);
	}

	@Override
	public Integer getDutyIdByDutyName(String duty_name) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getDutyIdByDutyName", duty_name);
	}

	@Override
	public Integer getCountByStaffNum(String staff_num) {
		return  (Integer) getSqlMapClientTemplate().queryForObject("getCountByStaffNum", staff_num);
	}

	@Override
	public Integer getDutyUserCountsExceptId(DutyUser dutyUser) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getDutyUserCountsExceptId", dutyUser);
	}

	@Override
	public Integer getDutyUserByDutyId(String duty_id) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getDutyUserByDutyId", duty_id);
	}

	@Override
	public List<DutyUser> getDutyUserAfterToday(String staffNum,String today) {
		Map map=new HashMap();
		map.put("staffNum", staffNum);
		map.put("today", today);
		return getSqlMapClientTemplate().queryForList("getDutyUserAfterToday", map);
	}

	@Override
	public void insertOneDutyUser(DutyUser dutyUser) {
		getSqlMapClientTemplate().insert("insertOneDutyUser", dutyUser);
	}

	@Override
	public List<Map> getAllUserByDept_DM(String DeptDM) {
		
		return getSqlMapClientTemplate().queryForList("getAllUserByDept_DM", DeptDM);
	}

	@Override
	public DutyUser getDutyUserAmongToday(String staffNum, String today) {
		Map map=new HashMap();
		map.put("staffNum", staffNum);
		map.put("today", today);
		return (DutyUser) getSqlMapClientTemplate().queryForObject("getDutyUserAmongToday", map);
	}

	@Override
	public List<String> getAllDutyName() {
		return getSqlMapClientTemplate().queryForList("getAllDutyName");
	}

	@Override
	public List<String> getAllStaffNo(String layerDeptNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("layerDeptNum", layerDeptNum);
		return getSqlMapClientTemplate().queryForList("getAllStaffNo",map);
	}

	@Override
	public DutyUser getDutyIdByStaffNum(Map<String, Object> map) {
		return (DutyUser) getSqlMapClientTemplate().queryForObject("getDutyIdByStaffNum", map);
	}

	@Override
	public List<DutyUser> getDeptDutyUserAfterToday(String deptNum, String today) {		
		Map map=new HashMap();
		map.put("deptNum", deptNum);
		map.put("today", today);
		return getSqlMapClientTemplate().queryForList("getDeptDutyUserAfterToday", map);
	}

	@Override
	public DutyUser getDutyDeptAmongToday(String deptNum, String today) {
		Map map=new HashMap();
		map.put("deptNum", deptNum);
		map.put("today", today);
		return (DutyUser) getSqlMapClientTemplate().queryForObject("getDutyDeptAmongToday", map);
	}

	@Override
	public boolean deleteDutyDeptById(String dutyUser_ids) {
		return delete("deleteDutyDeptById",dutyUser_ids);
	}

	@Override
	public boolean updateDutyDept(DutyUser dutyUser) {
		return update("updateDutyDept",dutyUser);
	}

	@Override
	public void insertOneDutyDept(DutyUser dutyUser) {
		getSqlMapClientTemplate().insert("insertOneDutyDept", dutyUser);
	}

	
	/*查询部门排班信息（导出用）*/
    public  List<Map> getDutyDeptByParam(Map map){
    	return  this.getSqlMapClientTemplate().queryForList("getDutyDeptByParam", map);
    }
	 /*查询部门排班信息（分页）*/
    public  List<Map> getDutyDeptByParamPage(Map map){
    	return this.getSqlMapClientTemplate().queryForList("getDutyDeptByParamPage", map);
    }
    /*查询部门排班信息行数*/
    public  Integer countDutyDeptByParam(Map map){
    	return (Integer) this.getSqlMapClientTemplate().queryForObject("countDutyDeptByParam", map);
    }
    /*根据id查询部门排班*/
    public  Map getDutyDeptById(String id){
    	return (Map) this.getSqlMapClientTemplate().queryForObject("getDutyDeptById", id);
    }
    
    /*根据部门代码查询部门所有员工信息与排班id*/
    @Override
	public List<Map> getStaffMessByDept_DM(String DeptDM) {
		return getSqlMapClientTemplate().queryForList("getStaffMessByDept_DM", DeptDM);
	}

	/*通过id修改员工排班结束日期 */
    public boolean updateDutyUserEndDate(DutyUser dutyUser){
    	return this.update("updateDutyUserEndDate", dutyUser);
    }
    
    /*通过id修改部门排班结束日期 */
	public boolean updateDutyDeptEndDate(DutyUser dutyUser){
		return this.update("updateDutyDeptEndDate", dutyUser);
	}
    
	@Override
	public DutyUser selectDutyByTimeAndStaffNum(String yeahMonthDayHourMinute,String staff_num) {
		Map map=new HashMap();
		map.put("timePoint", yeahMonthDayHourMinute);
		map.put("staff_num", staff_num);
		return (DutyUser) getSqlMapClientTemplate().queryForObject("selectDutyByTimeAndStaffNum",map);
	}

	@Override
	public void updateDutyUserEndTime(String yeahMonthDayHourMinute,Integer dutyUserId,String dept_num) {
		Map map=new HashMap();
		map.put("end_time", yeahMonthDayHourMinute);
		map.put("id", dutyUserId);
		map.put("dept_num", dept_num);
		getSqlMapClientTemplate().update("updateDutyUserEndTime",map);
		
	}
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-12-27
	 * @功能描述: 通过排班类型id获取未来排班信息
	 * @参数描述:
	 */
	@Override
    public List<Map>  getFutureDutysByDutyId(Map map){
    	return this.getSqlMapClientTemplate().queryForList("getFutureDutysByDutyId", map);
    }
    
    /**
     * @创建人　: 潘卉贤
     * @创建时间: 2016-12-27
     * @功能描述: 通过排班类型id获取以往排班信息
     * @参数描述:
     */
	@Override
    public List<Map>  getBeforeDutysByDutyId(Map map){
		return this.getSqlMapClientTemplate().queryForList("getBeforeDutysByDutyId", map);
	}
	
	/**
     * @创建人　: 潘卉贤
     * @创建时间: 2016-12-27
     * @功能描述: 通过排班信息id更新排班类型 
     * @参数描述:
     */
    public boolean updDutyId(Map map){
    	return this.update("updDutyId", map);
    }
}
