package com.kuangchi.sdd.baseConsole.times.timesgroup.dao;

import java.util.List;

import com.kuangchi.sdd.baseConsole.times.times.model.Times;
import com.kuangchi.sdd.baseConsole.times.timesgroup.model.TimesGroup;

/**
 * @创建人　: 陈凯颖
 * @创建时间: 2016-4-6 15:15:26
 * @功能描述: 时段组-Dao类
 */
public interface TimesGroupDao {

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-6 下午17:32:05
	 * @功能描述: 新增时段组
	 */
	boolean addTimesGroup(List<TimesGroup> timesGroupList);

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 查询时段组(分页)
	 */
	List<TimesGroup> getTimesGroupByParam(String group_id, String group_name, String group_num,
			String times_priority, String exist_nums, int page, int size);

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-14 下午15:32:05
	 * @功能描述: 按照编号分组查询时段组总数(分页)
	 */
	Integer getTimesGroupCount(String group_id, String group_name, String group_num,
			String times_priority, String exist_nums);
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 查询时段组(不分页)
	 */
	List<TimesGroup> getTimesGroupsByParam(String group_id, String group_name, String group_num,
			String times_priority);
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 根据组名查询时段组(不分页)
	 */
	List<TimesGroup> getTimesGroupsByName(String group_name);
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-5-30  下午15:32:05
	 * @功能描述: 根据组num查询时段组(不分页)
	 */
	List<TimesGroup> getTimesGroupsByNum(String group_nums);
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 设置时段组
	 */
	boolean modifyTimesGroup(TimesGroup timesGroup);

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 删除时段组(伪删除)
	 */
	boolean deleteTimesGroup(String group_nums);
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7下午15:32:05
	 * @功能描述: 查找所有时段，并按开始时间排序
	 * @return
	 */
	List<Times> getTimesByParamPageSortByBeginTime(Times times);
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 查询最大时段组编号
	 */
	Integer getMaxNum();
	
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
	
    /**
     * 查询所有时段组
     * @author yuman.gao
     */
    public List<TimesGroup> getAllGroupNum();
}
