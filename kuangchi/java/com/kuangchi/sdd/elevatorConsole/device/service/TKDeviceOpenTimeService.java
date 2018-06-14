package com.kuangchi.sdd.elevatorConsole.device.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.device.model.ResultMsg;
import com.kuangchi.sdd.baseConsole.device.model.TimeResultMsg;
import com.kuangchi.sdd.elevatorConsole.device.model.CommIpInfoModel;
import com.kuangchi.sdd.elevatorConsole.device.model.CommResult;
import com.kuangchi.sdd.elevatorConsole.device.model.Device;
import com.kuangchi.sdd.elevatorConsole.device.model.Floor;
import com.kuangchi.sdd.elevatorConsole.device.model.HardWareParam;
import com.kuangchi.sdd.elevatorConsole.device.model.Holiday;
import com.kuangchi.sdd.elevatorConsole.device.model.OpenTimeZone;
import com.kuangchi.sdd.elevatorConsole.device.model.TKDeviceOpenTimeModel;
import com.kuangchi.sdd.elevatorConsole.device.model.TimesGroupModel;
import com.kuangchi.sdd.elevatorConsole.device.model.TkDevAuthorityCardModel;

public interface TKDeviceOpenTimeService {
	
	/*
	 * 初始化设备
	 */
	boolean initDeviceParam(Device device,
			String login_user);
	
	/*
	 * 获取设备校时时间
	 */
	TimeResultMsg getDevTime(Device device);

	/*
	 * 设置设备校时
	 */
	CommResult setDevTime(Device device);
	
	/*
	 * 根据设备编号获取时段组
	 */
	Grid<TimesGroupModel> getAllTimesGroupByDevNum();
	
	/*
	 * 设置电梯开放时区
	 */
	boolean motifyDeviceOpenTime(OpenTimeZone openTimeZone,Device device,Map map, String login_user);
	
	Integer getDeviceOpenTimeByDevNum(String device_num);
	

	/*
	 * 新增电梯开放时区
	 */
	boolean insertDeviceOpenTime(String device_num, String login_user);

	List<TKDeviceOpenTimeModel> checkDevTimeGroup(String device_num);

	boolean deleteDevOpenTime(String device_num, String login_user);


	boolean  copyDevOpenTime(OpenTimeZone openTimeZone,TKDeviceOpenTimeModel tKDeviceOpenTimeModel,
			Device device,String login_user);
	/*
	 * 设置梯控动作参数
	 */
	boolean setEleMoveParam(Device device,Map map, String login_user);

	Device getEleMoveParam(String device_num);

	boolean clearAuthorityByDevNum(String device_num,List<TkDevAuthorityCardModel> list,String login_user);

	HardWareParam getEleLevel(Device device);
	
	public boolean copyEleOpenTime(String device_num, String copy_device_num, String login_user);

	List<TkDevAuthorityCardModel> getAuthorityCardByDevNum(String string);

	/*
	 * 清除节假日
	 */
	boolean resetHolidayTime(Map map, String login_user);
	/*
	 * 清除楼层开放时区
	 */
	boolean resetFloorTime(Map map, String login_user);
	
	/*
	 * 清除电梯开放时区
	 */
	boolean resetEleOpenTime(Map map,String login_user);
	
	/*
	 * 清除配置楼层
	 */
	boolean resetFloors(Map map,String login_user);
	
	/*
	 * 初始化楼层配置
	 */
	public boolean resetFloorConfig(Floor floor);
	
	/*
	 *初始化时重置动作参数
	 */
	boolean resetEleParam(Map eleMap, String login_user);
	/*
	 *设置电梯开放时区
	 */
	boolean updateDeviceOpenTime(OpenTimeZone openTimeZone, Device device,
			String login_user);
	/*
 	 *复制权限
	 */
	boolean copyAuthorityByDevNum(String targetDeviceNum,
			List<TkDevAuthorityCardModel> list, String login_user);
	
	/*
	 * 清除配置楼层（更新数据库）
	 */
	public boolean cleanConfigFloor(List<Floor> floors, String login_user);

	/*
	 * 清除楼层开放时区
	 */
	public boolean cleanFloorOpenArea(List<Floor> list, String login_user);
	//public boolean cleanFloorOpenArea(Floor floor, String login_user) throws Exception;
	
	/*
	 * 清除节假日
	 */
	public boolean cleanHoliday(String device_num, String login_user);
	
	/*
	 * 清除动作参数
	 */
	public boolean cleanEleMoveParam(Map map, String login_user);

	/*
	 *清除电梯设置总方法（更新数据库） 
	 */
	public boolean cleanElevatorSet(Map map, String login_user);
	
	/*
	 * 复制电梯设置总方法（更新数据库）
	 */
	public boolean copyElevatorSet(Map map, String login_user);
	
	/*
	 * 获取通讯服务器ip信息
	 */
	public Grid<CommIpInfoModel> getCommIpInfo(Map map);

	boolean addCommIpInfo(Map map, String login_user);

	CommIpInfoModel getCommIpInfoById(String id);

	boolean updateCommIpInfo(Map map, String login_user);

	boolean delCommIpInfo(String ids, String login_user);

	
}
