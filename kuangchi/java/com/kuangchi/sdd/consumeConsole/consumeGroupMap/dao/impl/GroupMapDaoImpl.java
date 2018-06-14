package com.kuangchi.sdd.consumeConsole.consumeGroupMap.dao.impl;



import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.attendException.dao.impl.AttendExceptionDaoImpl;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.consumeConsole.consumeGroupMap.dao.IGroupMapDao;
import com.kuangchi.sdd.consumeConsole.consumeGroupMap.model.GroupMapModel;


/**
 * @创建人　: 高育漫
 * @创建时间: 2016-7-27 下午2:48:01
 * @功能描述: 消费组管理-dao实现层
 */
@Repository("groupMapDaoImpl")
public class GroupMapDaoImpl extends BaseDaoImpl<Object> implements IGroupMapDao{

	@Override
	public String getNameSpace() {
		return "common.GroupMap";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<GroupMapModel> getGroupMapByParamPage(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getGroupMapByParamPage", map);
	}
	
	@Override
	public List<GroupMapModel> getGroupMapByParam(Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getGroupMapByParam", map);
	}

	@Override
	public boolean addGroupMap(GroupMapModel groupMap) {
		return this.insert("addGroupMap", groupMap);
	}

	@Override
	public boolean modifyGroupMap(GroupMapModel groupMap) {
		return this.update("modifyGroupMap", groupMap);
	}
	
	@Override
	public boolean newGroupMap(GroupMapModel groupMap) {
		return this.update("newGroupMap", groupMap);
	}

	@Override
	public boolean removeGroupMap(String delete_ids) {
		return this.update("removeGroupMap", delete_ids);
	}

	@Override
	public List<GroupMapModel> getStaffNameByNums(String staffNums) {
		return this.getSqlMapClientTemplate().queryForList("getStaffNameByNums", staffNums);
	}

	@Override
	public Integer getGroupMapByParamCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getGroupMapByParamCount", map);
	}

	@Override
	public List<Employee> searchEmployee(DepartmentPage departmentPage) {
		return this.getSqlMapClientTemplate().queryForList("getNoBoundEmployee",
				departmentPage);
	}
	
	@Override
	public Integer searchEmployeeCount(DepartmentPage departmentPage) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"getNoBoundEmployeeCount", departmentPage);
	}
	
	
	
	
	
	@Override
	public List<Map> getMealsByStaff(String staff_num){
		return this.getSqlMapClientTemplate().queryForList("getMealsByStaff", staff_num);
	}
	
	@Override
	public List<String> getMealsByGroup(String group_num){
		List<String> str = this.getSqlMapClientTemplate().queryForList("getMealsByGroup", group_num);
		return str;
	}
	
	@Override
	public boolean delByStaffAndMeal(Map<String, Object> map){
		return delete("delByStaffAndMeal", map);
	}
	
	@Override
	public boolean delDefaultByStaff(String staff_num){
		return delete("delDefaultByStaff", staff_num);
	}
	
	@Override
	public boolean insertGroupMap(Map<String, Object> map){
		return insert("insertGroupMap", map);
	}
	
	@Override
	public Integer ifExistGroupByStaff(String staff_num){
		return (Integer) this.getSqlMapClientTemplate().queryForObject("ifExistGroupByStaff", staff_num);
	}

}
