package com.kuangchi.sdd.baseConsole.times.timesgroup.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.baseConsole.times.times.model.Times;
import com.kuangchi.sdd.baseConsole.times.timesgroup.dao.TimesGroupDao;
import com.kuangchi.sdd.baseConsole.times.timesgroup.model.TimesGroup;
import com.kuangchi.sdd.baseConsole.times.timesgroup.service.TimesGroupService;

/**
 * @创建人　: 陈凯颖
 * @创建时间: 2016-4-6 15:15:26
 * @功能描述: 时段组-业务实现类
 */
@Service("timesGroupServiceImp")
public class TimesGroupServiceImp implements TimesGroupService {

	@Resource(name = "timesGroupDaoImp")
	private TimesGroupDao timesGroupDao;
	
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-6 下午17:32:05
	 * @功能描述: 新增时段组
	 * @param timesGroup
	 * @return
	 */
	public boolean addTimesGroup(List<TimesGroup> timesGroupList) {
		boolean result = timesGroupDao.addTimesGroup(timesGroupList);
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "时段组管理");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", timesGroupList.get(0).getCreate_user());
		log.put("V_OP_MSG", "新增时段组信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午14:32:05
	 * @功能描述: 查询时段组(分页)
	 * @param group_name
	 * @param times_priority
	 * @param page
	 * @param rows
	 * @return
	 */

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 查询时段组
	 * @param group_name
	 * @param times_priority
	 * @return
	 */
	public List<TimesGroup> getTimesGroupByParam(String group_id, String group_name, String group_num,
			String times_priority, String exist_nums, int page, int size) {
		return timesGroupDao.getTimesGroupByParam(group_id, group_name, group_num, times_priority, exist_nums, page, size);
	}
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 修改时段组
	 * @param timesGroup
	 * @return
	 */
	public boolean modifyTimesGroup(List<TimesGroup> timesGroupList) {
		boolean result = false;
		for (TimesGroup timesGroup : timesGroupList) {
			result = timesGroupDao.modifyTimesGroup(timesGroup);
		}
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "时段组管理");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", timesGroupList.get(0).getCreate_user());
		log.put("V_OP_MSG", "修改时段组信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}
	/**
	 *@创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 删除时段组(伪删除)
	 * @param group_ids
	 * @return
	 */
	public boolean deleteTimesGroup(String group_nums, String create_user) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = timesGroupDao.deleteTimesGroup(group_nums);
		log.put("V_OP_NAME", "时段组管理");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", create_user);
		log.put("V_OP_MSG", "删除时段组信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		return result;
	}
	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7下午15:32:05
	 * @功能描述: 查找所有时段，并按开始时间排序
	 * @return
	 */
	public List<Times> getTimesByParamPageSortByBeginTime(Times times) {
		return timesGroupDao.getTimesByParamPageSortByBeginTime(times);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-14 下午15:32:05
	 * @功能描述: 按照编号分组查询时段组总数
	 */
	public Integer getTimesGroupCount(String group_id, String group_name, String group_num,
			String times_priority, String exist_nums) {
		return timesGroupDao.getTimesGroupCount(group_id, group_name, group_num,
				times_priority, exist_nums);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 查询时段组(不分组)
	 */
	public List<TimesGroup> getTimesGroupsByParam(String group_id,
			String group_name, String group_num, String times_priority) {
		return timesGroupDao.getTimesGroupsByParam(group_id, group_name, group_num, times_priority);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7 下午15:32:05
	 * @功能描述: 根据组名查询时段组(不分组)
	 */
	public List<TimesGroup> getTimesGroupsByName(String group_name) {
		return timesGroupDao.getTimesGroupsByName(group_name);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-4-7下午15:32:05
	 * @功能描述: 查找所有时段，并按开始时间排序
	 * @return
	 */
	public Integer getMaxNum() {
		return timesGroupDao.getMaxNum();
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-5-30  下午15:32:05
	 * @功能描述: 根据组num查询时段组(不分页)
	 */
	public List<TimesGroup> getTimesGroupsByNum(String group_nums) {
		return timesGroupDao.getTimesGroupsByNum(group_nums);
	}

	/**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-6-1  下午15:32:05
	 * @功能描述: 根据设备编号查询时段组
	 */
	public List<TimesGroup> getTimesGroupByDevice(String object_num) {
		return timesGroupDao.getTimesGroupByDevice(object_num);
	}

	 /**
	 * @创建人　: 陈凯颖
	 * @创建时间: 2016-7-12  下午15:32:05
	 * @功能描述: 根据时段组编号查询对象时段组ID
	 */
	public Integer getTimesObjectIdByGroupNum(String group_num) {
		return timesGroupDao.getTimesObjectIdByGroupNum(group_num);
	}

}
