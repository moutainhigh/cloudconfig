package com.kuangchi.sdd.baseConsole.deviceGroup.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceGroupModel;
import com.kuangchi.sdd.baseConsole.deviceGroup.model.DeviceTimeGroup;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DeviceModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DoorModel;

public interface DeviceGroupDao {
	/**
	 * 获取所有设备组
	 * 
	 */
	public List<DeviceGroupModel> getAllDeviceGroup();
	
	public List<DeviceGroupModel> getAllDeviceGroupById(String id);
	
	public List<DeviceGroupModel> getAllDeviceGroupByIdA(String id);
	
	/**
	 * 获取所有设备
	 */
	 public List<DeviceModel>  getAllDeviceModel();
	 /**
	  * 获取所有设备（包含设备类型信息）
	  */
	 public List<Map>  getAllDevInfo();
	 
	 public List<DeviceModel>  getAllDeviceModelA(List<DeviceGroupModel> list);
	 
	 /**
	  * 获取所有门
	  */
	 public List<DoorModel> getDoorList();
	 
	 public List<DoorModel> getDoorListA(String deviceNum);
	 
	 
     

	   /*
    * 查询全部设备组属性列表
	 *  groupNum 设备组编号
	 *  groupName 设备组名称
	 *  skip  从第几行开始
	 *  rows  向后获取多少行
    * **/
  
   public List<DeviceGroupModel> searchDeviceGroup(String groupName,Integer skip,Integer rows);
   
   /*
    * 查询全部设备组属性列表数量
	  *  groupName 设备组名称
    * **/
  
   public Integer searchDeviceGroupCount(String groupName);
 
   
   /*
    * 查询全部设备组设备信息
    *  device_name 设备组名称
    *  skip  从第几行开始
    *  rows  向后获取多少行
    * **/
   
   public List<DeviceInfo> searchDeviceInfo(String device_name,Integer skip,Integer rows);
   
   /*
    * 查询全部设备组设备信息
    *  device_name 设备组名称
    * **/
   
   public Integer searchDeviceInfoCount(String device_name);
   
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
   
   public List<DeviceInfo> searchDeviceInfoByNum(String device_num,Integer skip,Integer rows);
   
   
   
   /*
    *  通过设备编号查询子设备数量
    *  device_num 设备编号
    * **/
   
   public Integer searchDeviceInfoCountByNum(String device_num);
    /*
     *  通过设备编号查询子设备组属性列表
	 *  bgm;      //设备组model
     * **/
   
    public List<DeviceGroupModel> searchDeviceGroupByNum(DeviceGroupModel bgm);
    
    
    
    /*
     *  通过设备编号查询子设备组属性列表数量
	 *  bgm;      //设备组model
     * **/
   
    public Integer searchDeviceGroupCountByNum(DeviceGroupModel bgm);
    
    
    /*
     *  通过设备编号查询单个设备组到form表单
	 *  groupNum;      //设备组编号
     * **/
   
    public DeviceGroupModel searchOnlyDeviceGroupByNum(String groupNum);
    
    
    /*
     *  通过设备编号查询单个设备组到datagrid
	 *  bgm;      //设备组model
     * **/
   
    public List<DeviceGroupModel> searchOnlyDeviceGroup(DeviceGroupModel bgm);
    
    /*
     * 添加设备组
     * groupNum;             // 设备组编号
     * groupName;            // 设备组名称 
     * parentGroupNum;       // 上级设备组编号
     * createUser;           // 创建人员代码 
     * createTime;           // 创建时间 
     * description;          //描述
     * **/
   public void  addDeviceGroup(String groupName,String parentGroupNum,String createUser,String createTime,String description);
     

   
   /*
    * 修改设备组
    * groupName;            // 设备组名称 
    * groupNum;              //设备组编号
    * description;          //描述
    * **/
   public boolean editDeviceGroup(String groupName,String description,String  groupNum);
   
   /*
    * 修改设备组
    * device_name;            // 设备名称 
    * device_num;              //设备编号
    * description;          //描述
    * **/
   public void editDeviceInfo(String device_name,String description,String  device_num);
   
   
   /*
    * 修改设备组父设备编号
    * groupNum;             // 设备组编号
    * parentGroupNum;            // 上级设备组编号
    * **/
   public void changeParentGroupNum(String groupNum,String parentGroupNum);
   
   /*
    * 修改设备父设备编号
    * device_num;                //设备编号
    * device_group_num;            // 上级设备组编号
    * **/
   public void changeDeviceGroupNum(String device_num,String device_group_num);

    /*
    * 删除设备组
    * groupNum;             //设备组编号
    * */
   public boolean delDeviceGroup(String groupNum);

   /**
    * 修改设备时间组
    */
   boolean modifyDeviceTimeGroup(DeviceTimeGroup deviceTimeGroup);
   
   /**
    * 根据设备id查询设备、门、设备时间组相关数据
    */
   public List<DeviceTimeGroup> getDeviceTimeGroupById(String device_id);
   
   /**
    * 获取所有的设备组信息
    * @return
    */
   public List<DeviceGroupModel> getDeviceGroup(String flag);
   
   /**
    * 查询设备组下的门禁设备
    * @param device_group_num
    * @return
    */
   public List<String> selDeviceInGroup(String device_group_num);
   
}
