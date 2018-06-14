package com.kuangchi.sdd.baseConsole.deviceGroup.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceGroupModel;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceTimeGroup;

public interface DeviceGroupService {
		/**
		 * 拿到设备组、设备、门的树
		 * @return
		 */
		 Tree  getDeviceDoorTree();
		 
		 Tree  getDeviceDoorTreeLazy(String id);
		 
		 public  List<Tree> getDeviceDoorTreeLazyList(String groupId);
		 
		 public  List<Tree> getDeviceDoorTreeLazyListA(String deviceId);
		 
		 /**
		  * 拿到设备组、设备的树
		  * @return
		  */
		 Tree  getOnlyDeviceGroupTree();
		 
		 /**
		  * 拿到设备组、设备的树（树中包含设备类型信息）
		  * @return
		  */
		 Tree  getOnlyDevGroupTree();
		 
		 /**
		  * 拿到设备组的树
		  * @return
		  */
		 Tree  onlyGetDeviceGroupTree();
		 /*
	      *  查询全部设备组属性列表
		  *  groupName 设备组名称
		  *  skip  从第几行开始
		  *  rows  向后获取多少行
	      * **/
	    
	     public Grid<DeviceGroupModel> searchDeviceGroup(String groupName,Integer skip,Integer rows);
	     
	     /*
	      * 查询全部设备组设备信息
	      *  device_name 设备组名称
	      *  skip  从第几行开始
	      *  rows  向后获取多少行
	      * **/
	     
	     public Grid<DeviceInfo> searchDeviceInfo(String device_name,Integer skip,Integer rows);
	     
	     
	     /*
	      * 通过设备id查询设备组设备信息
	      *  device_id 设备组id
	      * **/
	     
	     public DeviceInfo searchDeviceInfoById(String device_id);
  
	     
	     /*
	      *  通过设备编号查询子设备
	      *  device_num 设备编号
	      *  skip  从第几行开始
	      *  rows  向后获取多少行
	      * **/
	     
	     public Grid<DeviceInfo> searchDeviceInfoByNum(String device_num,Integer skip,Integer rows);

	     /*
	      *  通过设备编号查询子设备组属性列表
		  *  bgm;      //设备组model
	      * **/
	    
	     public Grid<DeviceGroupModel> searchDeviceGroupByNum(DeviceGroupModel bgm);
	     
	     
	     /*
	      *  通过设备编号查询单个设备组到form表单
		  *  groupNum;      //设备组编号
	      * **/
	     public  DeviceGroupModel  searchOnlyDeviceGroupByNum(String groupNum);
	     
	     /*
	      *  通过设备编号查询单个设备组到datagrid
		  *  groupNum;      //设备组编号
	      * **/
	     public  Grid<DeviceGroupModel>  searchOnlyDeviceGroup(DeviceGroupModel bgm);
	     
	     
	     /*
	      * 添加设备组
	      * groupNum;             // 设备组编号
	      * groupName;            // 设备组名称 
	      * parentGroupNum;       // 上级设备组编号
	      * createUser;           // 创建人员代码 
	      * createTime;           // 创建时间 
	      * description;          // 描述
	      * **/
	     public void  addDeviceGroup(String groupName,String parentGroupNum, String createUser, String createTime,String description);
	     
	     
	     /*
	      * 修改设备组
	      * groupName;            // 设备组名称 
	      * groupId;              // 设备组编号
	      * description;          // 描述
	      * **/
	     public void editDeviceGroup(String groupName,String description,String  groupNum,String login_user);
	     
	   
	     /*
	      * 修改设备
	      * device_name;            // 设备名称 
	      * device_num;             // 设备编号
	      * description;            // 描述
	      * **/
	     public void editDeviceInfo(String device_name,String description,String  device_num,String login_user);
	     

	     
	     /*
	      * 修改设备组的父设备编号
	      * groupNum;             // 设备组编号
	      * parentGroupNum;       // 上级设备组编号 
	      * **/
	     public void changeParentGroupNum(String groupNum,String parentGroupNum);
	     
	     /*
	      * 修改子设备的父设备编号
	      * device_num;                //设备编号
          * device_group_num;            // 上级设备组编号
	      * **/
	     public void changeDeviceGroupNum(String device_num,String device_group_num,String login_user);
	     
	     /*
	      * 删除设备组
	      * groupNum;              //设备组编号
	      * */
	     public void delDeviceGroup(String groupNum,String login_user); 
	     
	     /**
	      * 修改设备时间组
	      */
	     public boolean modifyDeviceTimeGroup(Map<String,List<DeviceTimeGroup>> deviceTimeGroup,String createUser);
	     
	     /**
	      * 根据设备id查询设备、门、设备时间组相关数据
	      */
	    public List<DeviceTimeGroup> getDeviceTimeGroupById(String device_num);
	    
	    /**
	     * 获取设备组信息
	     * @return
	     */
	    public List<DeviceGroupModel> getDeviceGroup(String flag);
	    
		/**
		 * 返回设备时段组之前先将库里设备时段组下发下去
		 * by gengji.yang
		 */
		public Map downLoadDeviceTimeGroupFirst(List<DeviceTimeGroup> list);
	    
		/**
	     * 把设备组下的门禁设备放进未分配组
	     * @param device_group_num
	     * @return
	     */
	    public boolean selDeviceInGroup(String device_group_num, String login_user);

	    /**
	     * @创建人　: 邓积辉
	     * @创建时间: 2016-11-21 下午5:00:04
	     * @功能描述: 根据要复制的设备编号更新对应的时段组
	     * @参数描述:
	     */
		boolean  modifyTargetDeviceTimeGroup(List<DeviceTimeGroup> list, String yhMc);
		
		/**
		 * 根据选择的节点获取子节点
		 * @author minting.he
		 * @param id
		 * @param type
		 */
		public List<Object> selNodeChild(String id, String type);
	     
}
