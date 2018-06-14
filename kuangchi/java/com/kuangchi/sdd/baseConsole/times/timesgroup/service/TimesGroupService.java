package com.kuangchi.sdd.baseConsole.times.timesgroup.service;

import java.util.List;

import com.kuangchi.sdd.baseConsole.times.times.model.Times;
import com.kuangchi.sdd.baseConsole.times.timesgroup.model.TimesGroup;

/**
 * @创建人　: 陈凯颖
 * @创建时间: 2016-4-6 15:15:26
 * @功能描述: 时段组-业务类
 */

public interface TimesGroupService {
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-6 下午17:32:05
	 * @功能描述: 新增时段组
	 * @param timesGroup
	 * @return
	 */
	boolean addTimesGroup(List<TimesGroup> timesGroupList);

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 查询时段组
	 * @return
	 */
	List<TimesGroup> getTimesGroupByParam(String group_id, String group_name, String group_num,
			String times_priority, String exist_ids, int page, int size);
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 设置时段组
	 * @param timesGroup
	 * @return
	 */
	boolean modifyTimesGroup(List<TimesGroup> timesGroupList);
	/**
	 *@创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 删除时段组(伪删除)
	 * @param group_ids
	 * @return
	 */
	boolean deleteTimesGroup(String group_nums, String create_user);
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7下午15:32:05
	 * @功能描述: 查找所有时段，并按开始时间排序
	 * @return
	 */
	List<Times> getTimesByParamPageSortByBeginTime(Times times);
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-14 下午15:32:05
	 * @功能描述: 按照编号分组查询时段组总数
	 */
	Integer getTimesGroupCount(String group_id, String group_name, String group_num,
			String times_priority, String exist_nums);
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 根据组名查询时段组(不分组)
	 */
	List<TimesGroup> getTimesGroupsByName(String group_name);
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 查询时段组(不分组)
	 */
	List<TimesGroup> getTimesGroupsByParam(String group_id, String group_name, String group_num,
			String times_priority);
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 查询最大时段组编号
	 */
	Integer getMaxNum();
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-5-30  下午15:32:05
	 * @功能描述: 根据组num查询时段组(不分页)
	 */
	List<TimesGroup> getTimesGroupsByNum(String group_nums);
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-1  下午15:32:05
	 * @功能描述: 根据设备编号查询时段组
	 */
	List<TimesGroup> getTimesGroupByDevice(String object_num);
	
	 /**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-7-12  下午15:32:05
	 * @功能描述: 根据时段组编号查询对象时段组ID
	 */
	Integer getTimesObjectIdByGroupNum(String group_num);
}
