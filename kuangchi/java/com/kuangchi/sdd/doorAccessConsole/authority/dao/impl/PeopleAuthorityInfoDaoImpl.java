package com.kuangchi.sdd.doorAccessConsole.authority.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.times.timesgroup.model.TimesGroup;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.businessConsole.employee.model.EmployeePosition;
import com.kuangchi.sdd.doorAccessConsole.authority.dao.PeopleAuthorityInfoDao;
import com.kuangchi.sdd.doorAccessConsole.authority.model.AuthorityByCardModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.AuthorityByStaffModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.AuthorityTask;
import com.kuangchi.sdd.doorAccessConsole.authority.model.CardModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DeviceModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DoorModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.PeopleAuthorityInfoModel;

@Repository("peopleAuthorityDao")
public class PeopleAuthorityInfoDaoImpl extends BaseDaoImpl<Object>  implements PeopleAuthorityInfoDao   {

	@Override
	public String getNameSpace() {
		return "common.Authority";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<PeopleAuthorityInfoModel> getPeopleAuthorityInfoByStaffNum(
			String staffNum,Integer skip, Integer rows) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("staffNum", staffNum);	
		map.put("skip",skip);	 
		map.put("rows", rows);	
		return getSqlMapClientTemplate().queryForList("getPeopleAuthorityInfoByStaffNum",map);
	}
	
	
	
	@Override
	public Integer getPeopleAuthorityInfoByStaffNumCount(String staffNum) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("staffNum", staffNum);	
		return (Integer) getSqlMapClientTemplate().queryForObject("getPeopleAuthorityInfoByStaffNumCount",map);
	}
	




	@Override
	public List<PeopleAuthorityInfoModel> searchAuthority(String doorNum,String doorName,String staffName,String staffNo,String cardNum,Integer skip, Integer rows) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("doorNum", doorNum);	 
		map.put("doorName", doorName);	 
		map.put("staffName", staffName);	 
		map.put("staffNo", staffNo);	 
		map.put("cardNum",cardNum);	 
		map.put("skip",skip);	 
		map.put("rows", rows);	 
		return getSqlMapClientTemplate().queryForList("searchAuthority",map);
		
	}

	
	
	@Override
	public Integer searchAuthorityCount(String doorNum,String doorName,String staffName,String staffNo,String cardNum) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("doorNum", doorNum);	 
		map.put("doorName", doorName);	 
		map.put("staffName", staffName);	 
		map.put("staffNo", staffNo);	 
		map.put("cardNum",cardNum);		 
		return (Integer) getSqlMapClientTemplate().queryForObject("searchAuthorityCount",map);
	}
	

	@Override
	public void insertAuthority(String cardNum,String doorNum,String deviceNum,
			String createTime,String createUser,String description,
			String validStartTime,String validEndTime,String timeGroupNum, String taskState) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("cardNum", cardNum);	
		map.put("doorNum", doorNum);	
		map.put("deviceNum", deviceNum);	
		map.put("createTime", createTime);	
		map.put("createUser", createUser);	
		map.put("description", description);	
		map.put("validStartTime", validStartTime);	
		map.put("validEndTime", validEndTime);	
		map.put("timeGroupNum", timeGroupNum);	
		map.put("taskState", taskState);	
		getSqlMapClientTemplate().insert("insertAuthority",map);
	}

	@Override
    public void copyFromCardToCard(String fromCardNum,String toCardNum ){
		Map<String,String> map=new HashMap<String, String>();
		map.put("fromCardNum", fromCardNum);	
		map.put("toCardNum", toCardNum);	
		getSqlMapClientTemplate().update("copyFromCardToCard",map);
	}

	@Override
	public String selectCardNum(String cardNum){
		Map<String,String> map=new HashMap<String, String>();
		map.put("cardNum", cardNum);	 
		return (String) getSqlMapClientTemplate().queryForObject("selectCardNum",map);
	}

	@Override
	public List<String> getCardNumByStaffNum(String staffNum) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("staffNum", staffNum);	 
		return getSqlMapClientTemplate().queryForList("getCardNumByStaffNum",map);
	}

	/*修改了返回类型为list by weixuan.lu 2016-5-16
	 **/
	@Override
	public List<String> getDeviceNumByDoorNum(String doorNum) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("doorNum", doorNum);	 
		return getSqlMapClientTemplate().queryForList("getDeviceNumByDoorNum",map);
	}
	
	//通过时段组名称获取时段组编号 by weixuan.lu
		@Override
		public String getTimesGroupNumByTimesGroupName(String timeGroupName){
			Map<String,String> map=new HashMap<String, String>();
			map.put("group_name", timeGroupName);
			return (String)getSqlMapClientTemplate().queryForObject("getTimesGroupNumByTimesGroupName",map);
		}
		//通过员工编号获取员工姓名 by weixuan.lu
		@Override
		public String getStaffNameByStaffNum(String staffNum){
			Map<String,String> map=new HashMap<String, String>();
			map.put("staff_num", staffNum);
			return (String)getSqlMapClientTemplate().queryForObject("getStaffNameByStaffNum",map);
		}
		
		//通过门编号获取门名称 by weixuan.lu  增加字段device_num
			@Override
			public String getDoorNameByDoorNum(String doorNum,String deviceNum){
				Map<String,String> map=new HashMap<String, String>();
				map.put("door_num", doorNum);
				map.put("device_num", deviceNum);
				return (String)getSqlMapClientTemplate().queryForObject("getDoorNameByDoorNum",map);
			}
	
	@Override
	public void addAuthorityByDoor(String doorNum, String deviceNum,
			String createUser, String description,String validStartTime,String validEndTime,String timeGroupNum) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("doorNum", doorNum);	 
		map.put("deviceNum", deviceNum);	 
		map.put("createUser", createUser);	 
		map.put("description", description);	
		map.put("validStartTime", validStartTime);	
		map.put("validEndTime", validEndTime);	
		map.put("timeGroupNum", timeGroupNum);	
		getSqlMapClientTemplate().insert("addAuthorityByDoor",map);
		
	}

	@Override
	public void delAuthority(String doorNum, String cardNum, String deviceNum) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("doorNum", doorNum);	 
		map.put("cardNum", cardNum);
		map.put("deviceNum", deviceNum);
		getSqlMapClientTemplate().delete("delAuthority",map);
		
	}

	@Override
	public List<Map> getDoorNumsByCardNum(String cardNum) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("cardNum", cardNum);	 
		return getSqlMapClientTemplate().queryForList("getDoorNumsByCardNum",map);
	}
	
	//增加条件deviceNum by weixuan.lu
	@Override
	public void delAuthorityByDoor(String doorNum,String deviceNum) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("doorNum", doorNum);
		map.put("deviceNum", deviceNum);
		getSqlMapClientTemplate().delete("delAuthorityByDoor",map);
		
	}
	
	 /* 删除权限
     * by weixuan.lu
     * 2016/4/25
     * */
	@Override
	public void deleteAuthority(String doorNum, String cardNum,String deviceNum) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("doorNum", doorNum);	 
		map.put("cardNum", cardNum);
		map.put("deviceNum", deviceNum);
		getSqlMapClientTemplate().delete("deleteAuthority",map);
	}
    
	
	@Override
	public List<CardModel> getCardListByStaffNum(String staffNum) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("staffNum", staffNum);	
		return getSqlMapClientTemplate().queryForList("getCardModelByStaffNum",map);
	}

	@Override
	public List<DoorModel> getDoorList() {
	
		return getSqlMapClientTemplate().queryForList("getDoorModel");
	}

	@Override
	public Integer getCardCountByCardNum(String cardNum) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("cardNum", cardNum);	
		return (Integer) getSqlMapClientTemplate().queryForObject("getCardCountByCardNum",map);
	}

	@Override
	public List<Map> getDoorModelByCardNum(String cardNum,Integer skip, Integer rows){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("cardNum", cardNum);
		map.put("skip",skip);	 
		map.put("rows", rows);	
		return getSqlMapClientTemplate().queryForList("getDoorModelByCardNum",map);
	}

	@Override
	public List<DeviceModel> getDeviceModelByCardNum(String cardNum) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("cardNum", cardNum);	
		return getSqlMapClientTemplate().queryForList("getDeviceModelByCardNum",map);
	}

	@Override
	public List<Map> getDoorModelByDeviceNum(String deviceNum,String cardNum) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("deviceNum", deviceNum);	
		map.put("cardNum", cardNum);	
		return getSqlMapClientTemplate().queryForList("getDoorModelByDeviceNum",map);
	}

	@Override
	public List<DeviceModel> getAllDeviceModel() {
		    
		return getSqlMapClientTemplate().queryForList("getAllDeviceModel");
	}

	@Override
	public List<PeopleAuthorityInfoModel> searchAuthorityDownLoad(String doorNum,
			String doorName, String staffName, String staffNo, String cardNum) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("doorNum", doorNum);	 
		map.put("doorName", doorName);	 
		map.put("staffName", staffName);	 
		map.put("staffNo", staffNo);	 
		map.put("cardNum",cardNum);	 
		return getSqlMapClientTemplate().queryForList("searchAuthorityDownload",map);
	}

	@Override
	public CardModel getDesCard(String desCardNum) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("desCardNum", desCardNum);
		return (CardModel)getSqlMapClientTemplate().queryForObject("getDesCard", map);

	}

	@Override
	public List<Map> getTimesGroup(Map m) {
		return this.getSqlMapClientTemplate().queryForList(
				"getTimesGroup", m);
	}

	@Override//==============这里的约束有问题 要修改
	public DoorModel getDoorModelByDoorNum(String doorNum) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("doorNum", doorNum);	
		return (DoorModel)getSqlMapClientTemplate().queryForObject("getDoorModelByDoorNum",map);
	}

	@Override
	public int getDoorModelCountByCardNum(String cardNums) {
		Map <String,String> map=new HashMap<String,String>();
		map.put("cardNums", cardNums);
		return (Integer) getSqlMapClientTemplate().queryForObject("getDoorModelCountByCardNum",map);
	}


	@Override
	public List<CardModel> getDesCardList(String desCardNum, Integer skips,
			Integer rows) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("desCardNum",desCardNum);
		map.put("skip",skips.toString());
		map.put("rows", rows.toString());
		return this.getSqlMapClientTemplate().queryForList(
				"getDesCardList",map);
	}

	@Override
	public int getDesCardListCount(String desCardNum) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("desCardNum", desCardNum);
		return (Integer) getSqlMapClientTemplate().queryForObject("getDesCardListCount",map);
	}

	@Override
	public void delOldDoorAuth(String cardNum,String doorNum,String deviceNum) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("cardNum", cardNum);
		map.put("doorNum", doorNum);
		map.put("deviceNum", deviceNum);
		getSqlMapClientTemplate().delete("delOldDoorAuth", map);
	}
	
	
/*	delete from     kc_people_authority_info   where    card_num=?   and    door_num=?   and    validity_flag=0  
			DEBUG 2016-05-27 22:08:16 [java.sql.PreparedStatement] - {pstm-100069} Parameters: [10363, 1]*/
	

	@Override
	public List<Map> getTimeGroupByDeviceNum(String deviceNum) {
		return getSqlMapClientTemplate().queryForList("getTimeGroupByDeviceNum", deviceNum);
	}

	@Override
	public List<Map> getBeCopyCardDoors(String cardNum) {
		return getSqlMapClientTemplate().queryForList("getBeCopyCardDoors", cardNum);
	}

	@Override
	public List<Map> getDevicesOnCardNum(String cardNum) {
		return getSqlMapClientTemplate().queryForList("getDevicesOnCardNum", cardNum);
	}

	@Override
	public void myInsertIntoTable(Map map) {
		getSqlMapClientTemplate().insert("myInsertIntoTable", map);
	}

	@Override
	public List<String> selAllCardNum() {
		return this.getSqlMapClientTemplate().queryForList("selAllCardNum");
	}

	@Override
	public List<String> getCardNumByDoorNum(String doorNum, String deviceNum) {
		Map<String,String> map=new HashMap<String, String>();
		map.put("doorNum", doorNum);
		map.put("deviceNum", deviceNum);
		return getSqlMapClientTemplate().queryForList("getCardNumByDoorNum", map);
	}

	@Override
	public void delAuthByCards(String cards) {
		getSqlMapClientTemplate().delete("delAuthByCards", cards);
	}

	@Override
	public List<Map> getDoorsInfoDynamic(Map map) {
		return getSqlMapClientTemplate().queryForList("getDoorsInfoDynamic", map);
	}

	@Override
	public Integer countDoorsInfoDynamic(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("countDoorsInfoDynamic", map);
	}

	@Override
	public List<Map> getAllCards(String groupIds) {
		return getSqlMapClientTemplate().queryForList("getAllCards", groupIds);
	}

	@Override
	public List<PeopleAuthorityInfoModel> getAuthorityInfoByStaffNum(String staffNum) {
		return getSqlMapClientTemplate().queryForList("getAuthorityInfoByStaffNum", staffNum) ;
	}

	@Override
	public List<Map> getDescDoors(String doorNum) {
		return getSqlMapClientTemplate().queryForList("getDescDoors", doorNum);
	}

	@Override
	public Integer countDescDoors(String doorNum) {
		return (Integer) getSqlMapClientTemplate().queryForObject("countDescDoors", doorNum);
	}

	@Override
	public List<Map> getAuthOfDoor(Map map) {
		return getSqlMapClientTemplate().queryForList("getAuthOfDoor", map);
	}

	@Override
	public boolean deleteAllAuth() {
		return delete("deleteAllAuth", null);
	}

/*	@Override
	public List<String> getAllTimeGroups() {
		return getSqlMapClientTemplate().queryForList("getAllTimeGroups");
	}

	@Override
	public List<String> getAllDoorNum() {
		return getSqlMapClientTemplate().queryForList("getAllDoorNum");
	}

	@Override
	public List<String> getAllDeviceNum() {
		return getSqlMapClientTemplate().queryForList("getAllDeviceNum");
	}*/

	@Override
	public Map getDeviceDoorObj(Map map) {
		return (Map) getSqlMapClientTemplate().queryForObject("getDeviceDoorObj", map);
	}
	
	@Override
	public List<Map> getDeviceDoorObjA(Map map) {
		return getSqlMapClientTemplate().queryForList("getDeviceDoorObjA", map);
	}

	@Override
	public boolean hasEmp(String deptNumStr) {
		Integer flag=(Integer) getSqlMapClientTemplate().queryForObject("hasEmp", deptNumStr);
		if(flag>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void saveOrganAuth(Map map) {
		getSqlMapClientTemplate().insert("saveOrganAuth", map);
	}

	@Override
	public void deleteOrganAuth(Map map) {
		getSqlMapClientTemplate().delete("deleteOrganAuth", map);
	}

	@Override
	public List<Map> getOrganAuth(String staffNum) {
		return getSqlMapClientTemplate().queryForList("getOrganAuth", staffNum);
	}

	@Override
	public List<Map> getOrganAuthByDept(String deptNum) {
		return getSqlMapClientTemplate().queryForList("getOrganAuthByDept", deptNum);
	}

	@Override
	public Map getDeptNumAndHireState(String staffNum) {
		return (Map) getSqlMapClientTemplate().queryForObject("getDeptNumAndHireState", staffNum);
	}

	@Override
	public void addAuthTask(Map map) {
		getSqlMapClientTemplate().insert("addAuthTask", map);
	}

	@Override
	public void delAuthTask(Map map) {
		getSqlMapClientTemplate().delete("delAuthTask", map);
	}

	@Override
	public List<Map> getAuthTasks() {
		return getSqlMapClientTemplate().queryForList("getAuthTasks");
	}

	@Override
	public void addAuthTaskHis(Map map) {
		getSqlMapClientTemplate().insert("addAuthTaskHis", map);
	}

	@Override
	public List<Map> getAuthTasksHis(Map map) {
		return getSqlMapClientTemplate().queryForList("getAuthTasksHis", map);
	}

	@Override
	public void updateTryTimes(Map map) {
		getSqlMapClientTemplate().update("updateTryTimes", map);
	}

	@Override
	public Integer getTryTimes(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("getTryTimes", map);
	}

	@Override
	public void addAuthRecord(Map map) {
		getSqlMapClientTemplate().insert("addAuthRecord", map);
	}

	@Override
	public Map getTask(Map map) {
		return (Map)getSqlMapClientTemplate().queryForObject("getTask", map);
	}

	@Override
	public void delAuthRecord(Map map) {
		getSqlMapClientTemplate().delete("delAuthRecord", map);
	}

	@Override
	public List<Map> getEmpCards(String staffNum) {
		return getSqlMapClientTemplate().queryForList("getEmpCards", staffNum);
	}

	@Override
	public List<Map> searchDoorSysAuth(Map map) {
		return getSqlMapClientTemplate().queryForList("searchDoorSysAuth", map);
	}

	@Override
	public List<Map> getMjTimeGroup(String deviceNum) {
		return getSqlMapClientTemplate().queryForList("getMjTimeGroup", deviceNum);
	}

	@Override
	public List<Map> searchAuthDownload(Map map) {
		return getSqlMapClientTemplate().queryForList("searchAuthDownload", map);
	}

	@Override
	public Integer countSearchDoorSysAuth(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("countSearchDoorSysAuth", map);
	}
	
	@Override
	public boolean insertAuthorityTask(AuthorityTask task) {
		return insert("insertAuthorityTask", task);
	}

	@Override
	public Integer devOnLine(String deviceNum) {
		return (Integer)getSqlMapClientTemplate().queryForObject("devOnLine", deviceNum);
	}

	@Override
	public void updAuth(Map map) {
		getSqlMapClientTemplate().update("updAuth", map);
	}

	@Override
	public List<Map> allDevices() {
		return getSqlMapClientTemplate().queryForList("allDevices");
	}

	@Override
	public Map getAuthInfo(Map map) {
		return (Map)getSqlMapClientTemplate().queryForObject("getAuthInfo", map);
	}

	@Override
	public List<Map> getStaffNumOnAuth(Map map) {
		return getSqlMapClientTemplate().queryForList("getStaffNumOnAuth", map);
	}

	@Override
	public List<Map> getStaffAuthOnDevDor(Map map) {
		return getSqlMapClientTemplate().queryForList("getStaffAuthOnDevDor", map);
	}

	@Override
	public Integer countStaffAuthOnDevDor(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("countStaffAuthOnDevDor", map);
	}

	@Override
	public List<Map> getStfOrdinCard() {
		return getSqlMapClientTemplate().queryForList("getStfOrdinCard");
	}
	
	@Override
	public List<Map> getStfOrdinCardA(String staffNum) {
		return getSqlMapClientTemplate().queryForList("getStfOrdinCardA",staffNum);
	}

	@Override
	public List<Map> getOrgAuth(Map map) {
		return getSqlMapClientTemplate().queryForList("getOrgAuth", map);
	}

	@Override
	public Integer countOrgAuth(Map map) {
		return (Integer)getSqlMapClientTemplate().queryForObject("countOrgAuth", map);
	}

	@Override
	public List<Map> getOrgAuthForTree(Map map) {
		return getSqlMapClientTemplate().queryForList("getOrgAuthForTree", map);
	}

	@Override
	public List<Map> getSrcOrgAuth(String deptStr) {
		return getSqlMapClientTemplate().queryForList("getSrcOrgAuth", deptStr);
	}

	@Override
	public List<String> getBitedStaffNo(String bits) {
		return getSqlMapClientTemplate().queryForList("getBitedStaffNo", bits);
	}

	@Override
	public Integer getBits(String rule) {
		return (Integer)getSqlMapClientTemplate().queryForObject("getBits",rule);
	}

	@Override
	public List<String> getBitedDeptNo(String bits) {
		return getSqlMapClientTemplate().queryForList("getBitedDeptNo", bits);
	}

	@Override
	public void updateTskState(Map map) {
		getSqlMapClientTemplate().update("updateTskState",map);
	}

	@Override
	public void quzUpdateTskState(Map map) {
		getSqlMapClientTemplate().update("quzUpdateTskState",map);
	}
	
	
	//通过设备名称，Mac地址查询设备信息  by huixian.pan
	public Map getDeviceByNumMac(String deviceName,String deviceMac){
		Map map=new HashMap();
		map.put("deviceName", deviceName);
		map.put("deviceMac", deviceMac);
		return (Map) getSqlMapClientTemplate().queryForObject("getDeviceByNumMac", map);
	}

	@Override
	public List<Map> getDeptDutyInfos(Map map) {
		return getSqlMapClientTemplate().queryForList("getDeptDutyInfos", map);
	}

	@Override
	public void delVeryDuty(Map map) {
		getSqlMapClientTemplate().delete("delVeryDuty", map);	
	}

	@Override
	public List<PeopleAuthorityInfoModel> getDelAuthorityInfoByStaffNum(
			String staffNum) {
		return getSqlMapClientTemplate().queryForList("getDelAuthorityInfoByStaffNum", staffNum);
	}

	@Override
	public List<Map> getStaffDorAuths(Map map) {
		return getSqlMapClientTemplate().queryForList("getStaffDorAuths", map);
	}

	@Override
	public List<Map> getTkCardAuths(Map map) {
		return getSqlMapClientTemplate().queryForList("getTkCardAuths", map);
	}

	@Override
	public void addTkAuthTask(Map map) {
		getSqlMapClientTemplate().insert("addTkAuthTaskA", map);	
	}

	@Override
	public List<Map> getRemainTkAuths(Map map) {
		return getSqlMapClientTemplate().queryForList("getRemainTkAuths", map);
	}

	@Override
	public void handleTkDelFlag(Map map) {
		getSqlMapClientTemplate().update("handleTkDelFlag", map);
	}

	@Override
	public List<Map> getStfOrdinCardB(Map map) {
		return getSqlMapClientTemplate().queryForList("getStfOrdinCardB", map);
	}

	@Override
	public Integer countDeviceDoorObjA(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("countDeviceDoorObjA", map);
	}

   
}
