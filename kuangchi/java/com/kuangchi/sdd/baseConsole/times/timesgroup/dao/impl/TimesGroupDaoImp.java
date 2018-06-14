package com.kuangchi.sdd.baseConsole.times.timesgroup.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.times.times.model.Times;
import com.kuangchi.sdd.baseConsole.times.timesgroup.dao.TimesGroupDao;
import com.kuangchi.sdd.baseConsole.times.timesgroup.model.TimesGroup;

/**
 * @创建人　: 陈凯颖
 * @创建时间: 2016-4-6 15:15:26
 * @功能描述: 时段组-Dao实现类
 */

@Repository("timesGroupDaoImp")
public class TimesGroupDaoImp extends BaseDaoImpl<TimesGroup> implements
		TimesGroupDao {

	public String getNameSpace() {
		return "common.TimesGroup";
	}

	public String getTableName() {
		return null;
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-6 下午17:32:05
	 * @功能描述: 新增时段组
	 */
	public boolean addTimesGroup(List<TimesGroup> timesGroupList) {
		return insert("addTimesGroup", timesGroupList);
	}


	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 查询时段组
	 */
	public List<TimesGroup> getTimesGroupByParam(String group_id, String group_name, String group_num,
			String times_priority, String exist_nums, int page, int size) {
		Map param = new HashMap();
		param.put("group_id", group_id);
		param.put("group_name", group_name);
		param.put("group_num", group_num);
		param.put("times_priority", times_priority);
		param.put("exist_nums", exist_nums);
		param.put("page", (page - 1) * size);
		param.put("size", size);
		return this.getSqlMapClientTemplate().queryForList(
				"getTimesGroupByParam", param);
	}

	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 设置时段组
	 */
	public boolean modifyTimesGroup(TimesGroup timesGroup) {
		return update("modifyTimesGroup", timesGroup);
	}
	/**
	 * 删除时段组(伪删除)
	 * 
	 * @param group_ids
	 * @return
	 */
	public boolean deleteTimesGroup(String group_nums) {
		return update("deleteTimesGroup", group_nums);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7下午15:32:05
	 * @功能描述: 查找所有时段，并按开始时间排序
	 * @return
	 */
	public List<Times> getTimesByParamPageSortByBeginTime(Times times) {		
		return this.getSqlMapClientTemplate().queryForList("getTimesByParamPageSortByBeginTime",times);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-14 下午15:32:05
	 * @功能描述: 按照编号分组查询时段组总数
	 */
	public Integer getTimesGroupCount(String group_id, String group_name, String group_num,
			String times_priority, String exist_nums) {
		Map param = new HashMap();
		param.put("group_id", group_id);
		param.put("group_name", group_name);
		param.put("group_num", group_num);
		param.put("times_priority", times_priority);
		param.put("exist_nums", exist_nums);
		return (Integer) this.find("getTimesGroupCount",param);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 查询时段组(不分组)
	 */
	public List<TimesGroup> getTimesGroupsByParam(String group_id,
			String group_name, String group_num, String times_priority) {
		Map param = new HashMap();
		param.put("group_id", group_id);
		param.put("group_num", group_num);
		param.put("group_name", group_name);
		param.put("times_priority", times_priority);
		return this.getSqlMapClientTemplate().queryForList(
				"getTimesGroupsByParam", param);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 根据组名查询时段组(不分组)
	 */
	public List<TimesGroup> getTimesGroupsByName(String group_name) {
		return this.getSqlMapClientTemplate().queryForList("getTimesGroupsByName", group_name);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 查询最大时段组编号
	 */
	public Integer getMaxNum() {
		return (Integer) this.find("getMaxGroupNum",null);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-5-30  下午15:32:05
	 * @功能描述: 根据组num查询时段组(不分页)
	 */
	public List<TimesGroup> getTimesGroupsByNum(String group_nums) {
		return this.getSqlMapClientTemplate().queryForList("getTimesGroupsByNum", group_nums);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-1  下午15:32:05
	 * @功能描述: 根据设备编号查询时段组
	 */
	public List<TimesGroup> getTimesGroupByDevice(String object_num) {
		return this.getSqlMapClientTemplate().queryForList("getTimesGroupByDevice", object_num);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-7-12  下午15:32:05
	 * @功能描述: 根据时段组编号查询对象时段组ID
	 */
	public Integer getTimesObjectIdByGroupNum(String group_num) {
		return (Integer) find("getTimesObjectIdByGroupNum", group_num);
	}

	@Override
	public List<TimesGroup> getAllGroupNum() {
		return this.getSqlMapClientTemplate().queryForList("getAllGroupNum");
	}

}
