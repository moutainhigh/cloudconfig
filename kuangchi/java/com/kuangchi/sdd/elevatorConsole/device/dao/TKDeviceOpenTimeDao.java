package com.kuangchi.sdd.elevatorConsole.device.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.elevatorConsole.device.model.CommIpInfoModel;
import com.kuangchi.sdd.elevatorConsole.device.model.Device;
import com.kuangchi.sdd.elevatorConsole.device.model.Floor;
import com.kuangchi.sdd.elevatorConsole.device.model.TKDeviceOpenTimeModel;
import com.kuangchi.sdd.elevatorConsole.device.model.TimesGroupModel;

public interface TKDeviceOpenTimeDao {
	/*
	 * 初始化设备
	 */
	boolean initDeviceParam(String device_num);
	
	List<TimesGroupModel> getAllTimesGroupByDevNumList();

	Integer getAllTimesGroupByDevNumCount();

	/*
	 * 设置电梯开放时区
	 */
	boolean motifyDeviceOpenTime(Map map);

	Integer getDeviceOpenTimeByDevNum(String device_num);

	boolean insertDeviceOpenTime(String device_num);
	/*
	 * 设置开放时区前初始化
	 */
	List<TKDeviceOpenTimeModel> checkDevTimeGroup(String device_num);

	boolean deleteDevOpenTime(String device_nums);

	boolean copyDevOpenTime(TKDeviceOpenTimeModel tKDeviceOpenTimeModel);

	boolean setEleMoveParam(Map map);

	Device getEleMoveParam(String device_num);

	boolean clearAuthorityByDevNum(String device_num);

	List getAuthorityCardByDevNum(String device_num);

	boolean insertDeviceAuth(Map map);

	boolean resetEleOpenTime(Map map);
	
	boolean resetHolidayTime(Map map);

	boolean resetFloorTime(Map map);
	
	boolean resetFloors(Map map);

	boolean resetFloorConfig(Floor floor);

	boolean resetEleParam(Map eleMap);

	List<CommIpInfoModel> getCommIpInfoList(Map map);

	Integer getCommIpInfoCount(Map map);

	boolean addCommIpInfo(Map map);

	CommIpInfoModel getCommIpInfoById(String id);

	boolean updateCommIpInfo(Map map);

	boolean delCommIpInfo(String ids);

	Integer getCountById(Integer id);

	boolean insertAuthorityHistory(Map map1);

	boolean deleteAuthorityBydevNum(String deviceNum);

}
