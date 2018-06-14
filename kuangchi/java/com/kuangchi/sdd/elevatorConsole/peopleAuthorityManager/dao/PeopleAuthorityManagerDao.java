package com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.model.FloorGroupModel;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.model.PeopleAuthorityManager;
import com.kuangchi.sdd.elevatorConsole.peopleAuthorityManager.model.TkDeviceModel;

/**
 * @创建人　: 潘卉贤
 * @创建时间: 2016-9-26 
 * @功能描述: 人员授权-dao
 */
public interface PeopleAuthorityManagerDao {
	
    /*通过卡号获取卡类型 */
	public String getCardType(String card_num);
	
	/*获取所梯控设备信息*/
	public List<TkDeviceModel> getTkDeviceInfo(Map<String, Object>  map);
	
	/*获取所梯控设备信息条数*/
	public Integer getTkDeviceInfoCount(Map<String, Object>  map);
	
	/*通过设备编号获取所梯控设备IP*/
	public Map getTkDeviceIPByDeviceNum(String device_num);
	
	/*通过设备编号获取所梯控设备信息*/
	public List<TkDeviceModel> getTkDeviceInfoByDeviceNum(Map<String, Object>  map);
	
	/*通过设备编号获取所梯控设备信息条数*/
	public Integer getTkDeviceInfoCountByDeviceNum(Map<String, Object>  map);
	
	/*通过员工编号，卡号获取已有权限*/
	public List<Map> getAuthsByStaffNum(Map map);
	
	/*通过员工编号，卡号获取已有权限行数*/
	public Integer countAuthsByStaffNum(Map map);
	
	/*通过员工编号，卡号获取已有权限(按卡号，设备号分组)*/
	public List<Map> getAuthsByStaffNum2(Map map);
	
	/*通过员工编号，卡号获取已有权限行数(按卡号，设备号分组)*/
	public Integer countAuthsByStaffNum2(Map map);
	
	/*通过员工编号，卡号获取已有权限(没分页)*/
	public List<Map> getAuthsByStaffNumNoLimit(Map map);
	
	/*获取人卡绑定信息*/
	public List<Map> getCardBoundInfo(Map map);
	 
	/*获取人卡绑定信息行数*/
	public Integer countCardBoundInfo(Map map);
	
	/*获取楼层组信息*/
    public List<FloorGroupModel> getfloorGroupSelections();
    
    /*获取楼层信息*/
    public List<String>  getFloorNum(String  floor_group_num);
    
    /*新增人员权限*/
    public boolean addPeopleAuthority(PeopleAuthorityManager pam);
    
    /*删除人员权限（伪删除）*/
    public boolean updatePeopleAuthority(PeopleAuthorityManager pam);
    
    /*删除人员权限（真删除）*/
    public boolean deletePeopleAuthority(PeopleAuthorityManager pam);
    
    /*查询所有下发权限任务*/
	public List<Map> getTkAuthorityTask(Map map);
	
	/*查询所有下发权限任务行数*/
	public Integer getTkAuthorityTaskCount(Map map);
	
	/*查询所有失败的下发权限任务*/
	public List<PeopleAuthorityManager> getTkFailureAuthorityTask();
	
	/*通过任务历史id查询任务历史详情 */
	public List<PeopleAuthorityManager> getAuthorityTaskById(String id);
	
	
	/*查询所有下发权限任务历史*/
	public List<Map> getStaffAuthorityTask(Map map);
	
	/*查询所有人员下发权限任务行数*/
	public Integer getStaffAuthorityTaskCount(Map map);
	
	/*查询所有人员下发权限任务*/
	public List<Map> getAllAuthTaskHis(Map map);
	
	/*查询所有下发权限任务历史行数 */
	public Integer getAllAuthTaskHisCount(Map map);
	
	/*查询所有人员失败的下发权限任务*/
	public List<PeopleAuthorityManager> getStaffFailureAuthorityTask();
	
	
	/*查询所有部门下发权限任务*/
	public List<Map> getDeptAuthorityTask(Map map);
	
	/*查询所有部门下发权限任务行数*/
	public Integer getDeptAuthorityTaskCount(Map map);
	
	/*查询所有部门失败的下发权限任务*/
	public List<PeopleAuthorityManager> getDeptFailureAuthorityTask();
	
	/*根据id删除下发权限任务历史历史*/
	public boolean delFailureAuthorityTaskHis(Map map);
	
	/*查询所有人员权限*/
	public List<Map> getStaffAuthority();
	
	/*查询所有部门权限*/
	public List<Map> getDeptAuthority();
	
	/*通过卡号获取楼层信息*/
	public List<Map> getFloorListByCardNum(Map map);
	
	/*通过部门编号获取楼层信息 */
	public List<Map> getFloorListByObjectNum(Map map);
	
	/* 通过卡号和设备号删除已有的权限任务 */
	public boolean  delRepeatTkAuthTask(Map map);
	
	/*------------------------------定时下发权限调用方法起-------------------------------------------*/
	/*获取所有梯控授权任务*/
	public List<Map> getTkAuthTasks();
	
	/*权限任务表尝试次数+1 */
	public  boolean  tryTimesPlus(Map map);
	
	/*获取尝试次数 */
	public Integer  getTryTime(Map map);
	
	/*获取单个授权任务 */
	public Map getSingleTask(Map map);
	
	/*删除授权任务 */
	public boolean  delTkAuthTask(Map  map);
	
	/*新增授权任务历史记录 */
	public boolean  addTkAuthTaskHis(Map map);
	
	/*添加权限记录 */
	public boolean  addTkAuthRecord(Map map);
	
	/*修改权限任务标记 (删除时用)*/
	public boolean  updTkAuthRecord(Map map);
	
	public boolean  updTkAuthRecordA(Map map);
	
	/*修改权限任务标记 （新增时 用）*/
	public boolean  updTkAuthRecord2(Map map);
	
	/* 删除权限表记录 */
	public boolean  delTkAuthRecord(Map map);
	
	/* 删除权限表记录（伪删除） */
	public boolean  updateTkAuthRecord(Map map);
	
	/* 删除重复权限表记录 */
	public boolean  delRepeatTkAuthRecord(Map map);
	
	/* 删除重复权限表记录(组织下没有卡时用) */
	public boolean  delNoCardRepeTkAuthRecord(Map map);
	
	public List<Map>getAuthsByStaffNumNew(Map map);
	
	public Integer countAuthsByStaffNumNew(Map map);
	
	public void delByID(Map map);
	
    
	/*------------------------------定时下发权限调用方法止-------------------------------------------*/

}
