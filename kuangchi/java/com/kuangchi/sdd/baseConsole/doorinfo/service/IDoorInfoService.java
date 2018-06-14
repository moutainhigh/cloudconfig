package com.kuangchi.sdd.baseConsole.doorinfo.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.device.model.DeviceStateModel;
import com.kuangchi.sdd.baseConsole.doorinfo.model.DoorInfoModel;

public interface IDoorInfoService {
	public Boolean insertDoorinfo(DoorInfoModel doorinfo);//新增门信息
	
	public List<DoorInfoModel> selectDoorinfoById(Integer door_id);//通过ID查信息
	
	public Grid selectAllDoorinfos(DoorInfoModel doorinfo,String page, String size);//模糊查询卡片所有信息
	
	public Integer getAllDoorinfoCount(DoorInfoModel doorinfo);//查询总的行数
	
	public List<DoorInfoModel> selectDeviceNumAdd();//查询设备编号新增
	
	public String getDoorID(String door_num);//门编号自动设置加一
	
	public Boolean updateDoorNameAndDes(DoorInfoModel doorinfo);//修改门信息
	
	public DeviceInfo queryMacByDeviceNum(String device_num);//根据设备编号查询设备mac地址
	
	public Integer deleteDoorinfo(DoorInfoModel doorinfo);//删除门信息
	
	public Integer deleteDoorPeopleauthorityService(DoorInfoModel doorinfo);//删除门卡绑定
	
	public Integer updateInsertDoor(DoorInfoModel doorinfo);//根据设备编号和门编号新增被删除的门
	
	public String querydevicemin(String device_num);// 查询被删除的最小设备编号 

	public Integer updateDoorinfo(Integer first_open, String device_num,String door_num,String login_user);//修改首卡开门
	
	/**
	 * 根据门号和设备号查询门信息
	 * @author minting.he
	 * @param door_num 门号
	 * @param device_num 设备号
	 * @return
	 */
	public List<DoorInfoModel> selectDoorInfo(Map<String, Object> map);
	
	public Integer updatehaddenflag(Integer hadden_flag, String device_num,String door_num,String login_user);//修改是否屏蔽门
	
	/**
	 * 根据设备编号查询门信息
	 * @param device_num
	 * @return
	 */
	public List<DoorInfoModel> selectDoorInfoByDeviceNum(String device_mac);

	public Grid<Map> getCardsInfoDynamic(Map map);//修改门信息多卡开门卡号时 查询卡号

	public List<Map> getStaffInfoByMultiCardnum(String multi_open_card_num);//根据多卡开门卡号查询员工信息
	
	public Map<String, Object> selectDeviceDoor(Map<String, Object> map);
	
	/**
	 * 修改监控门
	 * @author minting.he
	 * @param device_yes
	 * @param door_yes
	 * @param device_no
	 * @param door_no
	 * @param login_user
	 * @return
	 */
	public boolean updateMonitor(String device_yes, String door_yes, String device_no, String door_no, String login_user);
	
	/**
	 * 获取监控的树
	 * @author minting.he
	 * @return
	 */
	public Tree getMonitorTree();
	
	/**
	 * 获取已经勾选监控门的树
	 * @author minting.he
	 * @return
	 */
	public Tree getCheckedTree();
	
	/**
	 * 没监控门 分页
	 * @author minting.he
	 * @return
	 */
	public Grid<Map<String, Object>> getNoMonitorParam(Map<String, Object> map);
	
	/**
	 * 监控门 
	 * @author minting.he
	 * @return
	 */
	public List<Map<String, Object>> getMonitor(Map<String, Object> map);
	
	/**
	 * 修改监控门
	 * @author minting.he
	 * @param device_yes
	 * @param door_yes
	 * @param login_user
	 * @return
	 */
	public boolean updateMonitorParam(String device_yes, String door_yes, String login_user);
	
}
