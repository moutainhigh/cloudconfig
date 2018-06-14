package com.kuangchi.sdd.elevatorConsole.timesgroup.service;

import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.elevatorConsole.timesgroup.model.TimesGroupModel;

public interface TimesGroupService {
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-27 上午11:20:45
	 * @功能描述:获取全部时段组信息 
	 * @参数描述:
	 */
	Grid<TimesGroupModel> getTimesGroupPage(Map<String, Integer> map);
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-27 上午11:24:28
	 * @功能描述:新增时段组信息 
	 * @参数描述:
	 */
	boolean insertTimesGroup(Map map,String create_user);

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-27 上午11:24:28
	 * @功能描述: 检查时段组编号是否唯一 
	 * @参数描述:
	 */
	Integer checkTimeGroupNumUnique(String time_group_num);
}
