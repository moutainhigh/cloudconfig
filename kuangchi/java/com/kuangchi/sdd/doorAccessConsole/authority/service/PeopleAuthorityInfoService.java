package com.kuangchi.sdd.doorAccessConsole.authority.service;

import java.util.List;
import java.util.Map;

import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.baseConsole.times.timesgroup.model.TimesGroup;
import com.kuangchi.sdd.doorAccessConsole.authority.exception.ImportException;
import com.kuangchi.sdd.doorAccessConsole.authority.model.AuthorityByCardModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.AuthorityByDoorModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.AuthorityByStaffModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.CardModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DeviceModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DoorModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.PeopleAuthorityInfoModel;

public interface PeopleAuthorityInfoService {
	/*
	 * 根据员工编号获取授权列表
	 * staffNum 员工工号
	 * 
	 * 
	 * ***/
     public Grid<PeopleAuthorityInfoModel> getPeopleAuthorityInfoByStaffNum(String staffNum,Integer skip, Integer rows);
     
     /*
      * 权限下发
      * 其中peopleAuthorityInfoModel中必须属性是
      * cardNum  卡编号
      * doorNum 门号
      *  
      * 
      * **/
    public void dispatchAuthority(List<PeopleAuthorityInfoModel> peopleAuthorityInfoModelList,String createUser);
    
    
    
    
    /*
     * 权限复制
     * beCopiedCardNumList 复制哪些卡的权限
     * copyCardNum     复制到哪一张卡
     * ****/
    public void copyAuthority(List<String> beCopiedCardNumList,String copyCardNum ,String createUser);
    
    
    /*
     * 按条件查询权限
     * doorNum  门编号
     * staffNum  员工编号 
     * cardNum 卡编号
     * staffNameLike  员工名称模糊查询条件
     * staffNumLike   员工编号模糊查询条件
     * cardNumLike   卡号模糊查询条件
     * ***/
    
    public Grid<PeopleAuthorityInfoModel> searchAuthority(String doorNum,String doorName,String staffName,String staffNo,String cardNum,Integer skip, Integer rows);
    
    
    
    
    /*
     * 按卡批量导入权限
     * 
     * 
     * **/

    public void batchAddAuthorityByCard(List<AuthorityByCardModel> authorityByCardModelList,String createUser) throws ImportException;
    
    /*
     * 按人批量导入权限
     * 
     * 
     * **/

    public void batchAddAuthorityByStaff(List<AuthorityByStaffModel> authorityByStaffModelList,String createUser) throws ImportException;
    
    
    /*
     * 按门批量导入权限
     * 
     * **/
    
    public void batchAddAuthorityByDoor(List<AuthorityByDoorModel> authorityByDoorModelList,String createUser) throws ImportException;
    
    
    /*
     * 按卡批量删除权限
     * 修改了返回类型为boolean by weixuan.lu
     * 
     * **/

    public boolean batchDelAuthorityByCard(List<AuthorityByCardModel> authorityByCardModelList,String createUser) throws Exception;
    
    /*
     * 按人批量删除权限
     * 修改了返回类型为boolean by weixuan.lu
     * 
     * **/

    public boolean  batchDelAuthorityByStaff(List<AuthorityByStaffModel> authorityByStaffModelList,String createUser) throws Exception;
    
    
    /*按门批量导删除权限
     * 修改了返回类型传入的model
     * 修改了返回类型为boolean 
     * by weixuan.lu
     * 2014/4/21
     * */
    public boolean batchDelAuthorityByDoor(List<AuthorityByDoorModel> authorityByDoorModelList,String createUser) throws Exception;
    
    
    /*
     * 获取某个人的卡编号，卡类型
     * 
     * ***/
    public List<CardModel> getCardListByStaffNum(String staffNum);
    
    
    
    /*
     * 获取所有门编号 ，名称
     * ***/
    
    public List<DoorModel> getDoorList();
    
    /*
     * 验证卡号是否存在
     * **
     */
    
    public Integer getCardCountByCardNum(String cardNum);
    /*
     * 
     * 获取卡有权限的门
     * 
     * 
     * ***/
   public List<Map> getDoorModelByCardNum(String cardNum,Integer skip, Integer rows);
   
   /*
    *根据门号获取门的详细信息 
    */
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
     * 获取门树
     * 
     * ***/
    public Tree getDoorTree();
   
   
   
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
   
   
   public CardModel getDesCard(String cardNum);
   
   public List<CardModel> getDesCardList(String cardNum,Integer skips,Integer rows);
   
   public List<Map> getTimesGroup(Map m);
   
   public int getDoorModelCountByCardNum(String cardNums);

   public int getDesCardListCount(String desCardNum); 
   
   public void delOldDoorAuth(List<PeopleAuthorityInfoModel> peopleAuthorityInfoModelList);
   
   public List<Map> getTimeGroupByDeviceNum(String deviceNum);
   
   public List<Map> getBeCopyCardDoors(String cardNum);
   
   public List<Map> getDevicesOnCardNum(String cardNum);
   
   public void delAuthByCards(String cards);	
   
   public Grid<Map>getDoorsInfoDynamic(Map map);
   
   /**
    * 获取 所有组织下所有的卡
    * by gengji.yang
    */
   public List<Map> getAllCards(List groupList);
   
   /**
    * 获取目标门
    * by gengji.yang
    */
   public Grid<Map> getDescDoors(String doorNum);
   
   /**
    * 获取门的权限信息，主要是卡编号
    * by gengji.yang
    */
   public List<Map> getAuthOfDoor(Map map);
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-7-6 下午5:40:51
	 * @功能描述: 根据员工代码查询权限 
	 * @参数描述:
	 */
	public List<PeopleAuthorityInfoModel> getAuthorityInfoByStaffNum(String staffNum);
	
	   /**
	    *获取设备门，展示用
	    *by gengji.yang 
	    */
	   public List<Map> getDeviceDoorList(List<LinkedTreeMap> list);
	   
	   public Grid getDeviceDoorList(Map map);
	   
	   /**
	    * 组织授权时，
	    * 判断是否有员工
	    * by gengji.yang
	    */
	   public boolean hasEmp(String deptNumStr);
	   
	   /**
	    * 保存组织权限
	    * by gengji.yang
	    */
	   public void saveOrganAuth(Map map);
	   
	   /**
	    * 删除组织权限
	    * by gengji.yang
	    */
	   public void deleteOrganAuth(String deptNum,String deviceNum,String doorNum);
	   
	   /**
	    * 查询员工所在组织权限
	    * by gengji.yang
	    */
	   public List<Map> getOrganAuth(String staffNum);
	   
	   /**
	    * 根据组织查询组织权限
	    * by gengji.yang
	    */
	   public List<Map> getOrganAuthByDept(String deptNum);	
	   
	   /**
	    * 查部门编号
	    * 与
	    * 员工在职状态
	    * by gengji.yang
	    */
	   public Map getDeptNumAndHireState(String staffNum);
	   
	   /**
	    * 初始化新增权限页面的表格
	    * by gengji.yang
	    */
	   public List<Map> initAuthTab(List<Map> list);
	   
	   /**
	    * 往授权任务表中添加任务
	    * flag 0：添加权限 1:删除权限;2:黑名单删除权限
	    * by gengji.yang
	    */
	   public void addAuthTasks(List<Map> list,Integer flag);
	
	   /**
	    * 获取按时间排序后，前50条授权任务
	    * by gengji.yang
	    */
	   public List<Map> getAuthTasks();
	   
	   /**
	    * 尝试次数+1
	    * by gengji.yang
	    */
	   public void updateTryTimes(Map map);
	   
	   /**
	    * 获取尝试次数
	    * by gengji.yang
	    */
	   public Integer getTryTimes(Map map);
	   
	   /**
	    * 删除任务
	    * by gengji.yang
	    */
	   public void delAuthTask(Map map);
	   
	   /**
	    * 新增授权任务历史记录
	    * by gengji.yang
	    */
	   public void addAuthTaskHis(Map map);
	   
	   /**
	    * 记录权限
	    * by gengji.yang
	    */
	   public void addAuthRecord(Map map);
	   
	   /**
	    * 获取单个授权任务信息
	    * by gengji.yang
	    */
	   public Map getTask(Map map);
	   
	   /**
	    * 删除权限记录
	    * by gengji.yang
	    */
		public void delAuthRecord(Map map);
		
		/**
		 * 获取员工绑定的卡
		 * by gengji.yang
		 */
		public List<Map> getEmpCards(String staffNum);
		
		/**
		 * 重写 门禁二级菜单权限查看 
		 * by gengji.yang
		 */
		public Grid searchDoorSysAuth(Map map);
		
		/**
		 * 新的 时段组初始化
		 * by gengji.yang
		 */
		public List<Map> getMjTimeGroup(String deviceNum);
		
		/**
		 * 重写 门禁二级菜单权限查看的下载按钮处的查询
		 * by gengji.yang
		 */
		public List<Map> searchAuthDownload(Map map);
		
		/**
		 * 判断设备是否在线
		 * by gengji.yang
		 */
		public boolean devOnLine(String deviceNum);
		
		/**
		 * 更新权限记录标志位
		 * 假删除
		 * by gengji.yang
		 */
		public void updAuth(Map map);
		
		/**
		 * 获取所有的设备
		 * by gengji.yang
		 */
		public List<Map> allDevices();	
		
		/**
		 * 编辑页面所看到的权限信息
		 * by gengji.yang
		 */
		public List<Map> getAuthInfo(List<Map> list);
		
		/**
		 *  查询拥有指定权限的人群
		 *  经过去重，即取并集
		 *  by gengji.yang
		 */
		public Grid getInfoOnDevDor(List<Map> list,Integer skip,Integer rows);
		
		/**
		 * 查询组织权限
		 * 组织权限管理页面中点击组织树动态查询
		 * by gengji.yang
		 */
		public Grid getOrgAuth(Map map);
		
		/**
		 * 新增组织权限时，反选设备树的数据准备
		 * by gengji.yang
		 */
		public List<Map> getOrgAuthForTree(Map map);
		
		/**
		 * 查询源组织的权限
		 * by gengji.yang
		 */
		public List<Map> getSrcOrgAuth(String deptStr);
		
		/**
		 * 根据当前规则，自动生成员工工号
		 * 若返回null,则说明已经满值，eg,已经有了5位数99999
		 * by gengji.yang
		 */
		public String autoProdStaffNo();
		
		/**
		 * 根据当前规则，自动生成部门编号
		 * 若返回null,则说明已经满值，eg,已经有了5位数99999
		 * by gengji.yang
		 */
		public String autoProdDeptNo();
		
		/**
		 * 更新权限状态
		 * by gengji.yang
		 */
		public void updateTskState(Map map);
		
		/**
		 * 供定时器使用的更新权限状态
		 * by gengji.yang
		 */
		public void quzUpdateTskState(Map map);
		
		/**
		 * 查处部门的排班信息
		 * 然后对该部门下的员工进行排班
		 * by gengji.yang
		 */
		public void makeDutyByDeptNum(String deptNum,String staffNum,String today)throws Exception;
		
		/**
		 * 员工换部门，更换排班
		 * today:yyyy-MM-dd HH:mm:ss
		 * by gengji.yang
		 */
		public void chgDutyOnDeptChg(String newDept,String oldDept,String staffNum,String today)throws Exception;

		public List<PeopleAuthorityInfoModel> getDelAuthorityInfoByStaffNum(
				String staffNum);
		
		/**
		 * 伪删除某张卡的梯控权限
		 * by gengji.yang
		 */
		public void staffLeaveDelAuth(String cardNum);
		
		/**
		 * 恢复某张卡的梯控权限
		 * by gengji.yang
		 */
		public void staffBackAuthBack(String cardNum);
	
}
