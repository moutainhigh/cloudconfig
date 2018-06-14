package com.kuangchi.sdd.consumeConsole.device.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.businessConsole.cron.model.Cron;
import com.kuangchi.sdd.consumeConsole.device.model.Device;
import com.kuangchi.sdd.consumeConsole.device.model.DeviceConsumeSet;
import com.kuangchi.sdd.consumeConsole.device.model.DeviceGood;
import com.kuangchi.sdd.consumeConsole.device.model.DeviceGroup;
import com.kuangchi.sdd.consumeConsole.device.model.ParameterDownResponse;
import com.kuangchi.sdd.consumeConsole.device.model.PersonCardTask;
import com.kuangchi.sdd.consumeConsole.device.model.ResultMsg;
import com.kuangchi.sdd.consumeConsole.meal.model.MealModel;

/**
 * 设备信息维护Service
 * 
 * @author minting.he
 * 
 */
public interface IDeviceService {

	/**
	 * 根据条件查询设备信息
	 * 
	 * @param device
	 * @return
	 */
	public Grid<Device> getDeviceByParam(Device device);

	/**
	 * 新增设备
	 * 
	 * @param device
	 * @param create_user
	 * @return
	 */
	public boolean insertDevice(Device device, String is_type,
			String good_or_type_num, String create_user);

	/**
	 * 修改设备
	 * 
	 * @param device
	 * @param create_user
	 * @return
	 */
	public boolean updateDevice(Device device, String is_type,
			String good_or_type_num, String create_user);

	/**
	 * 删除设备
	 * 
	 * @param device_num
	 * @param create_user
	 * @return
	 */
	public boolean deleteDevice(String device_num, String create_user);

	/**
	 * 根据设备号获取关联商品信息
	 * 
	 * @param device_num
	 * @return
	 */
	public DeviceGood selByDeviceNum(String device_num);

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
	 * 设备类型编号是否被引用
	 * 
	 * @param device_type_num
	 * @return
	 */
	public Integer typeNumIsExist(String device_type_num);

	/**
	 * 把该设备组下的消费设备放进未分配组
	 * 
	 * @param device_group_num
	 * @return
	 */
	public boolean selXFDeviceInGroup(String device_group_num,
			String create_user);

	/**
	 * 获取设备组、设备的树
	 * 
	 * @return
	 */
	public Tree deviceAndGroupTree();

	/**
	 * 获取设备组的树
	 * 
	 * @return
	 */
	public Tree onlyGroupTree();

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
	 * @param deviceGroup
	 * @param login_user
	 * @return
	 */
	public boolean updateXFDeviceGroup(DeviceGroup deviceGroup,
			String login_user);

	/**
	 * 新增设备组
	 * 
	 * @param deviceGroup
	 * @param login_user
	 * @return
	 */
	public boolean insertXFDeviceGroup(DeviceGroup deviceGroup,
			String login_user);

	/**
	 * 删除设备组
	 * 
	 * @param group_num
	 * @return
	 */
	public boolean deleteXFDeviceGroup(String group_num, String login_user);

	/**
	 * 修改设备关联的设备组
	 * 
	 * @param device_group_num
	 * @param device_num
	 * @param login_user
	 * @return
	 */
	public boolean changeDeviceGroup(String device_group_num,
			String device_num, String login_user);

	/**
	 * 修改设备组的父设备组
	 * 
	 * @param parent_group_num
	 * @param group_num
	 * @param login_user
	 * @return
	 */
	public boolean changeParentGroup(String parent_group_num, String group_num,
			String login_user);

	/**
	 * 点击树查看设备
	 * 
	 * @param device
	 * @return
	 */
	public Grid<Device> getDeviceByTree(Device device);

	/**
	 * 下发任务接口
	 * 
	 * @author minting.he
	 * @param device_num
	 *            设备号
	 * @param card_num
	 *            卡号
	 * @param validity_flag
	 *            标志位 0:下发，1:删除
	 * @param trigger_flag
	 *            触发条件 0 充值 1 消费 2 补助 3 补扣 4 撤销消费 5 退款 6 转入 7 转出 8 下发名单 9 删除名单
	 */
	public void insertNameTask(String device_num, String card_num,
			String validity_flag, String trigger_flag);

	/**
	 * 新增下发名单任务
	 * 
	 * @param pctList
	 * @param login_user
	 * @return
	 */
	public boolean insertPCTask(List<PersonCardTask> pctList, String login_user);

	/**
	 * 获取所有绑定的卡号
	 * 
	 * @return
	 */
	public List<String> getAllCardNum();

	/**
	 * 查询设备已下发的名单
	 * 
	 * @param device_num
	 * @param page
	 * @param rows
	 * @return
	 */
	public Grid<Map> getNameByDevice(Map map);

	/**
	 * 根据mac查询消费参数xuewen.deng
	 * 
	 * @param DevNum设备编号
	 * @return ResultMsg
	 */
	public ResultMsg getParamByMac(String DevNum, String req);

	/**
	 * 设置消费终端参数xuewen.deng
	 * 
	 * @param ParameterDownResponse
	 * @return ResultMsg
	 */
	public ResultMsg setTerminalParam(ParameterDownResponse paramDown);

	/*	*//**
	 * 获取任务(获取下发名单)xuewen.deng
	 */
	/*
	 * public List<PersonCardTask> getPersonCardTask(String machine);
	 *//**
	 * 删除任务xuewen.deng
	 */
	/*
	 * public boolean deletePersonCardTask(String machine, String pack, String
	 * returnVal);
	 *//**
	 * 下发名单xuewen.deng2016/8/30
	 */
	/*
	 * public List<ResultMsg> issueName(String device_num);
	 *//**
	 * 修改任务状态和包号xuewen.deng2016/8/30
	 */
	/*
	 * public boolean updateTaskState(String idStr, String pack);
	 *//**
	 * 获取最大的包号xuewen.deng2016/8/30
	 */
	/*
	 * public Integer getMaxPack(String machine);
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
	 */
	/**
	 * 获取<=100条下发名单xuewen.deng
	 * 
	 * @return
	 */
	public List<PersonCardTask> getSomeTask(List<String> onlineDevice);

	/**
	 * 将名单从原表删除xuewen.deng
	 * */
	public boolean deletePCTask(PersonCardTask personCardTask);

	/**
	 * 将名单插入kc_xf_name_list表
	 * 
	 */
	public boolean insertToNameList(PersonCardTask personCardTask);

	/**
	 * 将名单插入历史表
	 * 
	 * @param singlePersonCardUp
	 * @return
	 */
	public boolean insertToHistory(PersonCardTask personCardTask);

	/**
	 * 更新历史表的成功标志
	 */
	public boolean setHistorySuccFlag(PersonCardTask personCardTask);

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

	public List<DeviceConsumeSet> getMealLimitTimesList(String device_num);// 获取设备对应的餐次限制次数列表

	public boolean delMealLimitTimes(String id, String device_num);// 删除设备设置的餐次限制次数

	public List<MealModel> getMealNum(String device_num);// 查询餐次信息

	public boolean addNewMealLimitTime(DeviceConsumeSet deviceConsumeSet);// 新增设备设置餐次限制次数

	public DeviceConsumeSet getDeviceConsumeSetById(String id, String device_num);// 根据id和设备编号查询餐次限制次数

	public boolean modifyMealLimitTime(DeviceConsumeSet deviceConsumeSet);// 修改餐次限制次数

	public boolean checkMealsEqualDeviceMeal(String device_num);// 新增餐次限制次数前，判断是否有可用的餐次

	public Device selectDeviceByNum(String device_num);// 通过设备编号查询设备名称

	/**
	 * 复制设备设置参数
	 * 
	 * @author minting.he
	 * @param flag
	 * @param device_num
	 * @param nums
	 *            目标设备
	 * @param login_user
	 * @return 0 成功 1 失败 2 获取终端失败
	 * @throws Exception
	 */
	public JsonResult copyParam(String flag, String device_num, String nums,
			String login_user) throws Exception;

	/**
	 * 删除已下发名单
	 * 
	 * @author minting.he
	 * @param pctList
	 * @param login_user
	 * @return
	 */
	public boolean deleteNameList(List<PersonCardTask> pctList,
			String login_user);

	/**
	 * 终端设置封装model
	 * 
	 * @author minting.he
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public ParameterDownResponse convertParam(ResultMsg msg) throws Exception;

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

	/**
	 * 获取所有的commIp
	 * 
	 * @author xuewen.deng
	 * @return
	 */
	public List<Map> getAllCommIp();

}
