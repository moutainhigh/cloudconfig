package com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.dao.PeopleAuthorityManagerDao;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.model.FloorGroupModel;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.model.PeopleAuthorityManager;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.model.TkDeviceModel;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-9-26 
 * @功能描述: 人员授权-dao实现层
 */

@Repository("peopleAuthorityManagerDaoImpl")
public class PeopleAuthorityManagerDaoImpl extends BaseDaoImpl<Object> implements PeopleAuthorityManagerDao{

	@Override
	public String getNameSpace() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
    /*通过卡号获取卡类型 */
	public String getCardType(String card_num){
		return  (String) this.getSqlMapClientTemplate().queryForObject("getCardType",card_num);
	}
	
	/*获取所有梯控设备信息*/
	@Override
	public List<TkDeviceModel> getTkDeviceInfo(Map<String, Object>  map) {
		return  this.getSqlMapClientTemplate().queryForList("getTkDeviceInfo",map);
	}

	/*获取所有梯控设备信息条数*/
	@Override
	public Integer getTkDeviceInfoCount(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getTkDeviceInfoCount", map);
	}
	
	/*通过设备编号获取所梯控设备IP*/
	@Override
	public Map getTkDeviceIPByDeviceNum(String device_num) {
		return (Map) this.getSqlMapClientTemplate().queryForObject("getTkDeviceIPByDeviceNum", device_num);
	}	
	
	/*通过设备编号获取所梯控设备信息*/
	@Override
	public List<TkDeviceModel> getTkDeviceInfoByDeviceNum(
			Map<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("getTkDeviceInfoByDeviceNum",map);
	}

	/*通过设备编号获取所梯控设备信息条数*/
	@Override
	public Integer getTkDeviceInfoCountByDeviceNum(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getTkDeviceInfoCountByDeviceNum", map);
	}
	
	/*通过员工编号，卡号获取已有权限*/
	@Override
	public List<Map> getAuthsByStaffNum(Map map) {
		return getSqlMapClientTemplate().queryForList("getAuthsByStaffNum", map);
	}

	/*通过员工编号，卡号获取已有权限行数*/
	@Override
	public Integer countAuthsByStaffNum(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("countAuthsByStaffNum", map);
	}
	
	/*通过员工编号，卡号获取已有权限(按卡号，设备号分组)*/
	@Override
	public List<Map> getAuthsByStaffNum2(Map map) {
		return getSqlMapClientTemplate().queryForList("getAuthsByStaffNum2", map);
	}
	
	/*通过员工编号，卡号获取已有权限行数(按卡号，设备号分组)*/
	@Override
	public Integer countAuthsByStaffNum2(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("countAuthsByStaffNum2", map);
	}

	/*通过员工编号，卡号获取已有权限(没分页)*/
	public List<Map> getAuthsByStaffNumNoLimit(Map map){
		return getSqlMapClientTemplate().queryForList("getAuthsByStaffNumNoLimit", map);
	}
	
	/*获取人卡绑定信息*/
	@Override
	public List<Map> getCardBoundInfo(Map map) {
		return getSqlMapClientTemplate().queryForList("getBoundCardInfo", map);
	}
	

	/*获取人卡绑定信息行数*/
	@Override
	public Integer countCardBoundInfo(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("countBoundCardInfo", map);
	}
	
	
	/*获取所有楼层组信息*/
	@Override
	public List<FloorGroupModel> getfloorGroupSelections() {
		return this.getSqlMapClientTemplate().queryForList("getfloorGroupInfo1");
	}
	
	/*获取楼层信息*/
	@Override
	public List<String> getFloorNum(String floor_group_num) {
		return this.getSqlMapClientTemplate().queryForList("getFloorNum1",floor_group_num);
	}
	
	/*新增人员权限*/
	@Override
	public boolean addPeopleAuthority(PeopleAuthorityManager pam) {
		return this.insert("addPeopleAuthority", pam);
	}

	/*删除人员权限（伪删除）*/
	@Override
	public boolean updatePeopleAuthority(PeopleAuthorityManager pam) {
		return this.update("updatePeopleAuthority", pam);
	}

	/*删除人员权限（真删除）*/
	@Override
	public boolean deletePeopleAuthority(PeopleAuthorityManager pam) {
		return this.delete("deletePeopleAuthority", pam);
	}

	/*查询所有下发权限任务*/
	@Override
	public List<Map> getTkAuthorityTask(Map map) {
		return this.getSqlMapClientTemplate().queryForList("getTkAuthorityTask", map);
	}

	/*查询所有下发权限任务行数*/
	@Override
	public Integer getTkAuthorityTaskCount(Map map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getTkAuthorityTaskCount", map);
	}

	/*查询所有失败的下发权限任务*/
	@Override
	public List<PeopleAuthorityManager> getTkFailureAuthorityTask() {
		return this.getSqlMapClientTemplate().queryForList("getTkFailureAuthorityTask");
	}
	
	/*查询所有人员下发权限任务*/
	public List<Map> getStaffAuthorityTask(Map map){
		return this.getSqlMapClientTemplate().queryForList("getStaffAuthorityTask", map);
	};
	
	/*查询所有下发权限任务历史行数 */
	public Integer getStaffAuthorityTaskCount(Map map){
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getStaffAuthorityTaskCount", map);
	};
	
	/*查询所有下发权限任务历史 */
	public List<Map> getAllAuthTaskHis(Map map){
		return this.getSqlMapClientTemplate().queryForList("getAllAuthTaskHis", map);
	};
	
	/*查询所有人员下发权限任务行数*/
	public Integer getAllAuthTaskHisCount(Map map){
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getAllAuthTaskHisCount", map);
	};
	
	/*查询所有人员失败的下发权限任务*/
	public List<PeopleAuthorityManager> getStaffFailureAuthorityTask(){
		return this.getSqlMapClientTemplate().queryForList("getStaffFailureAuthorityTask");
	};
	
	
	/*查询所有部门下发权限任务*/
	public List<Map> getDeptAuthorityTask(Map map){
		return this.getSqlMapClientTemplate().queryForList("getDeptAuthorityTask", map);
	};
	
	/*查询所有部门下发权限任务行数*/
	public Integer getDeptAuthorityTaskCount(Map map){
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getDeptAuthorityTaskCount", map);
	};
	
	/*查询所有部门失败的下发权限任务*/
	public List<PeopleAuthorityManager> getDeptFailureAuthorityTask(){
		return this.getSqlMapClientTemplate().queryForList("getDeptFailureAuthorityTask");
	};
	
	/*通过任务历史id查询任务历史详情 */
	public List<PeopleAuthorityManager> getAuthorityTaskById(String id){
		return this.getSqlMapClientTemplate().queryForList("getAuthorityTaskById",id);
	}
	
	/*根据id删除下发权限任务历史历史*/
	public boolean delFailureAuthorityTaskHis(Map map){
		return this.delete("delFailureAuthorityTaskHis", map);
	}
	
	/*查询所有人员权限*/
	public List<Map> getStaffAuthority(){
		return this.getSqlMapClientTemplate().queryForList("getStaffAuthority");
	};
	
	/*查询所有部门权限*/
	public List<Map> getDeptAuthority(){
		return this.getSqlMapClientTemplate().queryForList("getDeptAuthority");
	};
	
	/*通过卡号获取楼层信息*/
	public List<Map> getFloorListByCardNum(Map map){
		return this.getSqlMapClientTemplate().queryForList("getFloorListByCardNum", map);
	}
	
	/*通过部门编号获取楼层信息 */
	public List<Map> getFloorListByObjectNum(Map map){
		return this.getSqlMapClientTemplate().queryForList("getFloorListByObjectNum", map);
	}
	
	/* 通过卡号和设备号删除已有的权限任务 */
	public boolean  delRepeatTkAuthTask(Map map){
		return this.delete("delRepeatTkAuthTask", map);
	}
	

/*------------------------------定时下发权限调用方法起-------------------------------------------*/
	/*获取所有梯控授权任务*/
	@Override
	public List<Map> getTkAuthTasks() {
		return this.getSqlMapClientTemplate().queryForList("getTkAuthTasks");
	}

	/*权限任务表尝试次数+1 */
	@Override
	public boolean tryTimesPlus(Map map) {
		return this.update("tryTimesPlus", map);
	}

	/*获取尝试次数 */
	@Override
	public Integer getTryTime(Map map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getTryTime",map);
	}
	
	/*获取单个授权任务*/
	@Override
	public Map getSingleTask(Map map) {
		return (Map) this.getSqlMapClientTemplate().queryForObject("getSingleTask",map);
	}

	/*删除授权任务 */
	@Override
	public boolean delTkAuthTask(Map map) {
		return this.delete("delTkAuthTask", map);
	}

	/*新增授权任务历史记录 */
	@Override
	public boolean addTkAuthTaskHis(Map map) {
		return this.insert("addTkAuthTaskHis", map);
	}

	/*添加权限记录 */
	@Override
	public boolean addTkAuthRecord(Map map) {
		return this.insert("addTkAuthRecord", map);
	}
	
	/*修改权限任务标记 */
	public boolean  updTkAuthRecord(Map map){
		return this.update("updTkAuthRecord", map);
	}
	
	public boolean  updTkAuthRecordA(Map map){
		return this.update("updTkAuthRecordA", map);
	}
	
	/*修改权限任务标记 （新增时 用）*/
	public boolean  updTkAuthRecord2(Map map){
		return this.update("updTkAuthRecord2", map);
	}
	
	/* 删除权限表记录 */
	public boolean  delTkAuthRecord(Map map){
		return this.delete("delTkAuthRecord", map);
	}
	
	/* 删除权限表记录 （伪删除）*/
	public boolean  updateTkAuthRecord(Map map){
		return this.delete("updateTkAuthRecord", map);
	}
	
	/* 删除重复权限表记录*/
	public boolean  delRepeatTkAuthRecord(Map map){
		return this.delete("delRepeatTkAuthRecord", map);
	}
	
	/* 删除重复权限表记录(组织下没有卡时用) */
	public boolean  delNoCardRepeTkAuthRecord(Map map){
		return this.delete("delNoCardRepeTkAuthRecord", map);
	}
/*------------------------------定时下发权限调用方法止-------------------------------------------*/

	@Override
	public List<Map> getAuthsByStaffNumNew(Map map) {
		return getSqlMapClientTemplate().queryForList("getAuthsByStaffNumNew",map);
	}

	@Override
	public Integer countAuthsByStaffNumNew(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("countAuthsByStaffNumNew",map);
	}

	@Override
	public void delByID(Map map) {
		getSqlMapClientTemplate().delete("delByID", map);
	}

	

	
	

	

}
