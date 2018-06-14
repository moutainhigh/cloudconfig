package com.kuangchi.sdd.consumeConsole.device.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.businessConsole.cron.model.Cron;
import com.kuangchi.sdd.consumeConsole.device.model.Device;
import com.kuangchi.sdd.consumeConsole.device.model.DeviceConsumeSet;
import com.kuangchi.sdd.consumeConsole.device.model.DeviceGood;
import com.kuangchi.sdd.consumeConsole.device.model.DeviceGroup;
import com.kuangchi.sdd.consumeConsole.device.model.PersonCardTask;
import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;

/**
 * 设备信息维护Dao
 * 
 * @author minting.he
 * 
 */
public interface IDeviceDao {

	/**
	 * 根据条件查询设备信息
	 * 
	 * @param device
	 *            查询条件
	 * @return
	 */
	public List<Device> getDeviceByParam(Device device);

	/**
	 * 根据条件查询设备总数
	 * 
	 * @param device
	 * @return
	 */
	public Integer getDeviceByParamCount(Device device);

	/**
	 * 根据id查询设备信息
	 * 
	 * @param id
	 * @return
	 */
	public Device selDeviceById(String id);

	/**
	 * 设备编号是否存在
	 * 
	 * @param device
	 * @return
	 */
	public Integer validNum(Device device);

	/**
	 * 新增设备
	 * 
	 * @param device
	 * @return
	 */
	public boolean insertDevice(Device device);

	/**
	 * 修改设备
	 * 
	 * @param device
	 * @return
	 */
	public boolean updateDevice(Device device);

	/**
	 * 删除设备
	 * 
	 * @param device_num
	 * @return
	 */
	public boolean deleteDevice(String device_num);

	/**
	 * 根据设备号获取关联商品信息
	 * 
	 * @param device_num
	 * @return
	 */
	public DeviceGood selByDeviceNum(String device_num);

	/**
	 * 新增设备商品关联
	 * 
	 * @param deviceGood
	 * @return
	 */
	public boolean insertDeviceGood(DeviceGood deviceGood);

	/**
	 * 删除设备商品关联
	 * 
	 * @param device_num
	 * @return
	 */
	public boolean deleteDeviceGood(String device_num);

	/**
	 * 修改设备商品关联
	 * 
	 * @param deviceGood
	 * @return
	 */
	public boolean updateDeviceGood(DeviceGood deviceGood);

	/**
	 * 设备类型编号是否被引用
	 * 
	 * @param device_type_num
	 * @return
	 */
	public Integer typeNumIsExist(String device_type_num);

	/**
	 * 查询设备组下的设备
	 * 
	 * @param device_group_num
	 * @return
	 */
	public List<String> selXFDeviceInGroup(String device_group_num);

	/**
	 * 查询所有的消费设备
	 * 
	 * @return
	 */
	public List<Device> getAllXFDevice();

	/**
	 * 查询所有的消费设备组
	 * 
	 * @return
	 */
	public List<DeviceGroup> getXFDeviceGroup();

	/**
	 * 根据设备组号显示组信息
	 * 
	 * @param group_num
	 * @return
	 */
	public DeviceGroup getXFGroupInfoByNum(String group_num);

	/**
	 * 修改设备组信息
	 * 
	 * @return
	 */
	public boolean updateXFDeviceGroup(DeviceGroup deviceGroup);

	/**
	 * 新增设备组
	 * 
	 * @param deviceGroup
	 * @return
	 */
	public boolean insertXFDeviceGroup(DeviceGroup deviceGroup);

	/**
	 * 删除设备组
	 * 
	 * @param group_num
	 * @return
	 */
	public boolean deleteXFDeviceGroup(String group_num);

	/**
	 * 修改设备的关联的设备组
	 * 
	 * @param device_group_num
	 * @param device_num
	 * @return
	 */
	public boolean changeDeviceGroup(String device_group_num, String device_num);

	/**
	 * 修改设备组的父设备组
	 * 
	 * @param parent_group_num
	 * @param group_num
	 * @return
	 */
	public boolean changeParentGroup(String parent_group_num, String group_num);

	/**
	 * 点击树查看设备
	 * 
	 * @param device
	 * @return
	 */
	public List<Device> getDeviceByTree(Device device);

	/**
	 * 点击树查看设备总数
	 * 
	 * @param device
	 * @return
	 */
	public Integer getDeviceByTreeCount(Device device);

	/**
	 * 根据卡号查询员工信息
	 * 
	 * @param card_num
	 * @return
	 */
	public Map getStaffByCard(String card_num);

	/**
	 * 获取账户余额
	 * 
	 * @param device_num
	 * @param staff_num
	 * @return
	 */
	public BigDecimal getAccountBalance(String device_num, String staff_num);

	/**
	 * 卡是否已存在名单中
	 * 
	 * @param device_num
	 * @param card_num
	 * @return
	 */
	public Integer isExistCardTask(String device_num, String card_num);

	/**
	 * 更新下发名单
	 * 
	 * @param pcTask
	 * @return
	 */
	public boolean updateNameTask(PersonCardTask pcTask);

	/**
	 * 新增下发名单任务
	 * 
	 * @author minting.he
	 * @param pcTask
	 * @return
	 */
	public boolean insertNameTask(PersonCardTask pcTask);

	/**
	 * 获取所有绑定的卡号
	 * 
	 * @author minting.he
	 * @return
	 */
	public List<String> getAllCardNum();

	/**
	 * 查询设备已下发的名单
	 * 
	 * @param map
	 * @return
	 */
	public List<Map> getNameByDevice(Map map);

	/**
	 * 查询设备已下发的名单总数
	 * 
	 * @param map
	 * @return
	 */
	public Integer getNameByDeviceCount(Map map);

	/**
	 * 查询员工对应的所有账户余额
	 * 
	 * @param staff_num
	 * @return
	 */
	public List<BigDecimal> getBalanceByStaff(String staff_num);

	public List<DeviceConsumeSet> getMealLimitTimesList(String device_num);// 获取餐次限制次数列表

	public boolean delMealLimitTimes(String id, String device_num);// 删除设备设置的餐次限制次数

	public List<MealModel> getMealNum(String device_num);// 查询餐次信息

	public boolean addNewMealLimitTime(DeviceConsumeSet deviceConsumeSet);// 新增设备设置餐次限制次数

	public DeviceConsumeSet getDeviceConsumeSetById(String id, String device_num);// 根据id和设备编号查询餐次限制次数

	public boolean modifyMealLimitTime(DeviceConsumeSet deviceConsumeSet);// 修改餐次限制次数

	public int selectMealCount();// 查询餐次总数

	public int selectDeviceMealCount(String device_num);// 查询设备消费设置表中已经设置了消费限制次数的餐次数

	/**
	 * 删除设备所有餐次
	 * 
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public boolean delAllMeal(String device_num);

	/*	*//**
	 * 获取任务xuewen.deng
	 */
	/*
	 * public List<PersonCardTask> getPersonCardTask(String machine);
	 *//**
	 * 删除任务（伪删除）xuewen.deng
	 */
	/*
	 * public boolean deletePersonCardTask(String machine, String pack, String
	 * returnVal);
	 *//**
	 * 修改任务状态和包号xuewen.deng2016/8/30
	 */
	/*
	 * public boolean updateTaskState(String isStr, String pack);
	 *//**
	 * 获取最大的包号xuewen.deng2016/8/30
	 */
	/*
	 * public Integer getMaxPack(String maxPack);
	 *//**
	 * 根据最大包号获取尝试次数xuewen.deng2016/8/30
	 */
	/*
	 * public Integer getTry_times(Integer maxPack, String machine);
	 *//**
	 * 根据机器编号获取正在执行的任务xuewen.deng2016/8/31
	 */
	/*
	 * public List<PersonCardTask> getRunningTask(String machine);
	 *//**
	 * 修改任务尝试次数xuewen.deng2016/8/31
	 */
	/*
	 * public boolean updateTaskTrytimes(String idStr, String tryTimes);
	 *//**
	 * 往历史名单下发任务表插数据xuewen.deng2016/8/31
	 */
	/*
	 * public boolean insertToHistory(String machine);
	 *//**
	 * 删除任务（真删除）xuewen.deng2016/8/31
	 */
	/*
	 * public boolean deletePCT(String machine);
	 *//**
	 * 删除任务（伪删除）xuewen.deng2016/9/6
	 */
	/*
	 * public boolean markFail(String machine);
	 */
	public boolean insertToNameList(String string, String string2,
			String create_time);

	/**
	 * 将下发成功的名单插入kc_xf_name_list表中xuewen.deng
	 */

	public boolean insertToHistory(PersonCardTask personCardTask);

	/**
	 * 获得下发成功的名单
	 * 
	 * @param machine
	 *            机器编号
	 * @param pack
	 *            目前正在处理的包号
	 * @return
	 */
	/*
	 * public List<PersonCardTask> getSuccNameList(String machine, String pack);
	 *//**
	 * 验证名单是否已存在
	 * 
	 * @param machine
	 *            机器编号
	 * @param pack
	 *            目前正在处理的包号
	 * @return
	 */

	public Integer valName(String string, String string2);

	/**
	 * 更新下发名单的更新时间xuewen.deng
	 */

	public boolean updateName(String string, String string2);

	/**
	 * 获取<=100条下发名单
	 * 
	 * @return
	 */
	public List<PersonCardTask> getSomeTask(String device_nums);

	/**
	 * 将名单从原表删除xuewen.deng
	 * */
	public boolean deletePCTask(String id);

	/**
	 * 更新历史表的成功标志
	 */
	public boolean setHistorySuccFlag(Integer succFlag, String id);

	/**
	 * 获取在线的设备
	 */
	public List<String> getOnlineDevice();

	/**
	 * 更新尝试次数
	 */
	public boolean updateTryTime(PersonCardTask personCardTask);

	/**
	 * 更新任务运行状态
	 */
	public boolean updateRunState(String id, String state);

	/**
	 * 比较服务器ip地址是否和数据库中的相同
	 */
	public Integer compareIP(Cron cron);

	public Device selectDeviceByNum(String device_num);// 通过设备编号查询设备名称

	/**
	 * 设备已下发的全部名单
	 * 
	 * @author minting.he
	 */
	public List<Map> getAllNameList(Map<String, Object> map);

	/**
	 * 删除已下发名单
	 * 
	 * @author minting.he
	 * @param map
	 * @return
	 */
	public boolean delNameList(Map<String, Object> map);

	/**
	 * 根据设备号查设备名称
	 * 
	 * @author minting.he
	 * @param device_num
	 * @return
	 */
	public String getDeviceName(String device_num);

	/**
	 * 查询该卡是否下发到消费机
	 * 
	 * @author minting.he
	 * @param card_num
	 * @return
	 */
	public List<String> cardIfExistList(String card_num);

	/**
	 * 获取所有的消费设备编号
	 * 
	 * @author xuewen.deng
	 */
	public List<String> getAllDevNum();

	/**
	 * 查询某设备已下发（存在）的名单2(kc_xf_name_list)
	 * 
	 * @return
	 */
	public List<String> getNameListByDevice(String deviceNum);

	/**
	 * 根据设备编号查询通讯服务器ip（commIp）
	 * 
	 * @return
	 */
	public Map<String, String> getCommIPByDevNum(String deviceNum);
}
