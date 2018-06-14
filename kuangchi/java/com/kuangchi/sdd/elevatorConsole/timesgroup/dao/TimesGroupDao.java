package com.kuangchi.sdd.elevatorConsole.timesgroup.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.elevatorConsole.timesgroup.model.TimesGroupModel;

public interface TimesGroupDao {
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-27 上午11:20:20
	 * @功能描述: 查询时段组信息
	 * @参数描述:
	 */
	List<TimesGroupModel> getTimesGroupPageList(Map<String, Integer> map);
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-27 上午11:20:20
	 * @功能描述: 查询时段组信息总数
	 * @参数描述:
	 */
	Integer getTimesGroupPageCount(Map<String, Integer> map);
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-27 上午11:24:28
	 * @功能描述:新增时段组信息 
	 * @参数描述:
	 */
	boolean insertTimesGroup(Map map);

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-27 上午11:24:28
	 * @功能描述:检查时段组编号是否唯一
	 * @参数描述:
	 */
	Integer checkTimeGroupNumUnique(String time_group_num);
}
