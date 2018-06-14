package com.kuangchi.sdd.baseConsole.doorinfo.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.doorinfo.model.DoorInfoModel;
import com.kuangchi.sdd.doorAccessConsole.authority.model.DoorModel;
public interface IDoorInfoDao {
	public Boolean insertDoorinfo(DoorInfoModel doorinfo);//新增门信息

	public Boolean updateDoorNameAndDes(DoorInfoModel doorinfo);//修改门信息
	
	public List<DoorInfoModel> selectDoorinfoById(Integer door_id);//通过ID查信息
	
	public List<DoorInfoModel> selectAllDoorinfos(DoorInfoModel doorinfo,String page, String size);//模糊查询卡片所有信息
	
	public Integer getAllDoorinfoCount(DoorInfoModel doorinfo);//查询总的行数
	
	public List<DoorInfoModel> selectDeviceNumAdd();//查询设备编号新增
		
	public int getDoorID(String door_num);//门编号自增
	
	public DeviceInfo queryMacByDeviceNum(String device_num);//根据设备编号查询设备mac地址
	
	public Integer deleteDoorinfo(DoorInfoModel doorinfo);//删除门信息
	
	public Integer deleteDoorPeopleauthority(DoorInfoModel doorinfo);//删除门卡表信息
	
	public String querydevicemin(String device_num);//查询被删除的最小设备编号 
	
	public Integer updateInsertDoor(DoorInfoModel doorinfo);//根据设备编号和门编号新增被删除的门

	public Integer updateDoorinfo(Integer first_open, String device_num,String door_num);//修改首卡开门

	/**
	 * 根据门号和设备号查询门信息
	 * @author minting.he
	 * @param door_num 门号
	 * @param device_num 设备号
	 * @return
	 */
	public List<DoorInfoModel> selectDoorInfo(Map<String, Object> map);
	
	public Integer updatehaddenflag(Map<String,Object> map);//修改是否屏蔽门
	
	/**
	 * 根据设备编号查询门信息
	 * @param device_num
	 * @return
	 */
	public List<DoorInfoModel> selectDoorInfoByDeviceNum(String device_num);

	public List<Map> getCardsInfoDynamic(Map map);

	public Integer countCardsInfoDynamic(Map map);

	public List<Map> getStaffInfoByMultiCardnum(String multi_open_card_num);//根据多卡开门卡号查询员工信息
	
	public Map selectDeviceDoor(Map<String, Object> map);
	
	/**
	 * 修改监控门
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public boolean updateMonitor(Map<String, Object> map);
	
	/**
	 * 修改不监控门
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public boolean updateNoMonitor(Map<String, Object> map);
	
	/**
	 * 获取监控的门
	 * @author minting.he
	 * @return
	 */
	public List<DoorModel> getMonitorDoor();
	
	/**
	 * 修改监控门list
	 * @author minting.he
	 * @param list
	 * @return
	 */
	public boolean updateMonitorList(List<Map<String, Object>> list);
	
	/**
	 * 修改不监控门list
	 * @author minting.he
	 * @param list
	 * @return
	 */
	public boolean updateNoMonitorList(List<Map<String, Object>> list);
	
	/**
	 * 没监控门 分页
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getNoMonitorParam(Map<String, Object> map);
	
	/**
	 * 没监控门 总数
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public Integer getNoMonitorCount(Map<String, Object> map);
	
	/**
	 * 监控门
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getMonitor(Map<String, Object> map);
	
	/**
	 * 设置全为非监控门
	 * @author minting.he
	 * @return
	 */
	public boolean setNoMonitor();
	
}

