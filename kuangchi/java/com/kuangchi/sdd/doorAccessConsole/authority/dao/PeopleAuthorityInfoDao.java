package com.kuangchi.sdd.doorAccessConsole.authority.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kuangchi.sdd.doorAccessConsole.authority.model.AuthorityTask;
import com.kuangchi.sdd.doorAccessConsole.authority.model.CardModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DeviceModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DoorModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.PeopleAuthorityInfoModel;

public interface PeopleAuthorityInfoDao {
		/*
		 * 根据员工编号获取授权列表
		 * staffNum 员工工号
		 * 
		 * 
		 * ***/
	     public List<PeopleAuthorityInfoModel> getPeopleAuthorityInfoByStaffNum(String staffNum,Integer skip, Integer rows);
	     
			/*
			 * 根据员工编号获取授权列表总记录数
			 * staffNum 员工工号
			 * 
			 * 
			 * ***/
		  public Integer getPeopleAuthorityInfoByStaffNumCount(String staffNum);
		     
	     


	    /*
	     * 按条件查询权限
	     * doorNum  门编号
	     * staffNum  员工编号 
	     * cardNum 卡编号
	     * staffNameLike  员工名称模糊查询条件
	     * staffNumLike   员工编号模糊查询条件
	     * cardNumLike   卡号模糊查询条件
	     * ***/
	    
	    public List<PeopleAuthorityInfoModel> searchAuthority(String doorNum,String doorName,String staffName,String staffNo,String cardNum,Integer skip, Integer rows);
	    
	    /*
	     * 按条件查询权限
	     * doorNum  门编号
	     * staffNum  员工编号 
	     * cardNum 卡编号
	     * staffNameLike  员工名称模糊查询条件
	     * staffNumLike   员工编号模糊查询条件
	     * cardNumLike   卡号模糊查询条件
	     * ***/
	    
	    public Integer searchAuthorityCount(String doorNum,String doorName,String staffName,String staffNo,String cardNum);
	    
	    /*
	     * 插入权限
	     * 
	     * 
	     * 
	     * ***/

	    public void insertAuthority(String cardNum,String doorNum,String deviceNum,String createTime,String createUser,String description,String validStartTime,String validEndTime,String timeGroupNum, String taskState);
	    
        /*
         * 将一张卡的权限复制到另一张卡
         * 
         * **/
	    public void copyFromCardToCard(String fromCardNum,String toCardNum );
	    
	    /*
	     * 查看卡号是否存在
	     */
	    public String selectCardNum(String cardNum);
	    
	    /*
	     * 获取某个人的卡号
	     * 
	     * **/
	    
	    public List<String> getCardNumByStaffNum(String staffNum);
	    
	    /*
	     * 获取某个门的设备编号 
	     * 修改了返回类型为list by weixuan.lu 2016-5-16
	     * **/
	    public List<String>  getDeviceNumByDoorNum(String doorNum);
	    
	    /*
	     * 按门添加权限
	     * 
	     * **/
	    public void addAuthorityByDoor(String doorNum,String deviceNum,String createUser,String description,String validStartTime,String validEndTime,String timeGroupNum);
	    
	    /*
	     * 
	     * 按门号，卡号删除权限
	     * doorNum 门编号
	     * cardNum 卡编号
	     * */
	    
	    public void  delAuthority(String doorNum,String cardNum, String deviceNum);
	    
	    
	    /*
	     * 获取某一张卡开门的权限
	     * 
	     * **/
	    
	    public List<Map> getDoorNumsByCardNum(String cardNum);
	    
	    /*
	     * 按门删除权限权限
	     * 增加deviceNum by weixuan.lu
	     * ***/
	    public void delAuthorityByDoor(String doorNum,String deviceNum);
	    
	    
	    
	    /*
	     * 获取某个人的卡编号，卡类型
	     * 
	     * ***/
	    public List<CardModel> getCardListByStaffNum(String staffNum);
	    
	    
	    
	    
	    /*
	     * 获取所有门编号 ，名称
	     * ***/
	    
	    public List<DoorModel> getDoorList();
	    
	    
	    
	    public Integer getCardCountByCardNum(String cardNum);
	    
	   public List<Map> getDoorModelByCardNum(String cardNum,Integer skip, Integer rows);
	   
	   public DoorModel getDoorModelByDoorNum(String doorNum);
	   
	   /*
	    * 获取与某张卡有关系的设备
	    * 
	    * 
	    * ****/
	    public List<DeviceModel> getDeviceModelByCardNum(String cardNum);
	    
	   
	   /*
	    * 获取某一设备下的所有门
	    * 
	    * ***/
	   
	    public List<Map> getDoorModelByDeviceNum(String deviceNum,String cardNum);
	    
	    
	    
	  /*
	   * 获取所有的设备
	   * ***/  
	    
	    public List<DeviceModel>  getAllDeviceModel();
	    /*
	     * 按条件下载
	     * doorNum  门编号
	     * staffNum  员工编号 
	     * cardNum 卡编号
	     * staffNameLike  员工名称模糊查询条件
	     * staffNumLike   员工编号模糊查询条件
	     * cardNumLike   卡号模糊查询条件
	     * ***/
	    
	    public List<PeopleAuthorityInfoModel> searchAuthorityDownLoad(String doorNum,String doorName,String staffName,String staffNo,String cardNum);
	    
	    public CardModel getDesCard(String desCardNum);
	    
	    public List<CardModel> getDesCardList(String desCardNum,Integer skips,Integer rows);
	    
	    public List<Map> getTimesGroup(Map m);
	    public int getDoorModelCountByCardNum(String cardNums);
	    
	    public int getDesCardListCount(String desCardNum);
	    
	    /*
	     * 通过时段组名称获取时段组编号
	     * by weixuan.lu
	     * */
		public String getTimesGroupNumByTimesGroupName(String timeGroupName);

		
		 /*
	     * 通过员工编号获取员工姓名
	     * by weixuan.lu
	     * */
		public String getStaffNameByStaffNum(String staffNum);
		
		
	     /* 通过门编号获取门姓名
	     * by weixuan.lu
	     * 增加设备编号作为检索条件   2016/4/25
	     * */
		public String getDoorNameByDoorNum(String doorNum,String deviceNum);
		
		 /* 删除权限
	     * by weixuan.lu
	     * 2016/4/25
	     * */
		public void deleteAuthority(String doorNum, String cardNum, String deviceNum);
		
		public void delOldDoorAuth(String cardNum,String doorNum,String deviceNum);
		
		/**
		 * 根据设备编号 拿到 设备下面的时段组
		 * @param deviceNum
		 * @return
		 */
		public List<Map> getTimeGroupByDeviceNum(String deviceNum);
		/**
		 * 根据卡编号获取被复制卡下的门权限
		 * @param cardNum
		 * @return
		 */
		public List<Map> getBeCopyCardDoors(String cardNum);
		/**
		 * 根据卡编号 获取设备
		 * @param cardNum
		 * @return
		 */
		public List<Map> getDevicesOnCardNum(String cardNum);
		
		public void myInsertIntoTable(Map map);
		
		/**
		 * 查询所有卡号
		 * @return
		 */
		public List<String> selAllCardNum();
		
		/**
		 * 通过门编号和设备编号查询卡编号
		 * @param doorNum
		 * @param deviceNum
		 * @return
		 */
		public List<String> getCardNumByDoorNum(String doorNum,String deviceNum);
		
		/**
		 * 根据 卡编号批量删除权限
		 * by gengji.yang
		 */
		public void delAuthByCards(String cards);
		
		/**
		 * 获取门信息
		 * by gengji.yang
		 */
		public List<Map>getDoorsInfoDynamic(Map map);
		
		public Integer countDoorsInfoDynamic(Map map);
		
		public List<Map> getAllCards(String groupIds);
		
		/**
		 * @创建人　: 邓积辉
		 * @创建时间: 2016-7-6 下午5:40:51
		 * @功能描述: 根据员工代码查询权限 
		 * @参数描述:
		 */
		public List<PeopleAuthorityInfoModel> getAuthorityInfoByStaffNum(String staffNum);
		
		public List<Map> getDescDoors(String doorNum);
		
		public Integer countDescDoors(String doorNum);
		
		public List<Map> getAuthOfDoor(Map map);
		
		/**
		 * 删除所有门禁数据
		 * @author minting.he
		 * @return
		 */
		public boolean deleteAllAuth();
		
	/*	*//**
		 * @创建人　: 邓积辉
		 * @创建时间: 2016-8-8 下午5:39:09
		 * @功能描述: 获取全部时段组
		 * @参数描述:
		 *//*
		public List<String> getAllTimeGroups();
		
		*//**
		 * @创建人　: 邓积辉
		 * @创建时间: 2016-8-8 下午5:39:09
		 * @功能描述: 获取全部门号
		 * @参数描述:
		 *//*
		public List<String> getAllDoorNum();
		
		*//**
		 * @创建人　: 邓积辉
		 * @创建时间: 2016-8-8 下午5:39:09
		 * @功能描述: 获取全部设备编号
		 * @参数描述:
		 *//*
		public List<String> getAllDeviceNum();*/
		
		public Map getDeviceDoorObj(Map map);
		
		public List<Map> getDeviceDoorObjA(Map map);
		
		public Integer countDeviceDoorObjA(Map map);
		
		public boolean hasEmp(String deptNumStr);
		
		public void saveOrganAuth(Map map);
		
		public void deleteOrganAuth(Map map);
		
		public List<Map> getOrganAuth(String staffNum);
		
		public List<Map> getOrganAuthByDept(String deptNum);	
		
		public Map getDeptNumAndHireState(String staffNum);
		
		public void addAuthTask(Map map);
		
		public void delAuthTask(Map map);
		
		public List<Map> getAuthTasks();
		
		public void addAuthTaskHis(Map map);
		
		public List<Map> getAuthTasksHis(Map map);
		
		public void updateTryTimes(Map map);
		
		public Integer getTryTimes(Map map);
		
		public void addAuthRecord(Map map);
		
		public Map getTask(Map map);
		
		public void delAuthRecord(Map map);
		
		public List<Map> getEmpCards(String staffNum);
		
		public List<Map> searchDoorSysAuth(Map map);
		
		public List<Map> getMjTimeGroup(String deviceNum);
		
		public List<Map> searchAuthDownload(Map map);
		
		public Integer countSearchDoorSysAuth(Map map);
		
		/**
		 * 导入新增权限任务
		 * @author minting.he
		 * @param task
		 * @return
		 */
		public boolean insertAuthorityTask(AuthorityTask task);
		
		public Integer devOnLine(String deviceNum);
		
		public void updAuth(Map map);
		
		public List<Map> allDevices();
		
		public Map getAuthInfo(Map map);
		
		public List<Map> getStaffNumOnAuth(Map map);
		
		public List<Map> getStaffAuthOnDevDor(Map map);
		
		public Integer countStaffAuthOnDevDor(Map map);
		
		public List<Map> getStfOrdinCard();
		
		public List<Map> getStfOrdinCardB(Map map);
		
		public List<Map> getStfOrdinCardA(String staffNum);
		
		public List<Map> getOrgAuth(Map map);
		
		public Integer countOrgAuth(Map map);
		
		public List<Map> getOrgAuthForTree(Map map);
		
		public List<Map> getSrcOrgAuth(String deptStr);
		
		public List<String> getBitedStaffNo(String bits);
		
		public Integer getBits(String rule);
		
		public List<String> getBitedDeptNo(String bits);
		
		public void updateTskState(Map map);
		
		public void quzUpdateTskState(Map map);
		
		//通过设备名称，Mac地址查询设备信息  by huixian.pan
		public Map getDeviceByNumMac(String deviceName,String deviceMac);
		
		public List<Map> getDeptDutyInfos(Map map);
		
		public void delVeryDuty(Map map);

		public List<PeopleAuthorityInfoModel> getDelAuthorityInfoByStaffNum(
				String staffNum);
		
		public List<Map> getStaffDorAuths(Map map);
		
		public List<Map> getTkCardAuths(Map map);
		
		public void addTkAuthTask(Map map);
		
		public List<Map> getRemainTkAuths(Map map);
		
		public void handleTkDelFlag(Map map);
}
