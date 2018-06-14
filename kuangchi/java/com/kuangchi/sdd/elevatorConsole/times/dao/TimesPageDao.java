package com.kuangchi.sdd.elevatorConsole.times.dao;

import com.kuangchi.sdd.elevatorConsole.times.model.TimesPageModel;

public interface TimesPageDao {
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-28 下午12:30:59
	 * @功能描述:设置时段信息 
	 * @参数描述:
	 */
	public boolean motifyTimesPage(TimesPageModel timesPageModel);
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-28 下午12:30:59
	 * @功能描述:新增时段信息 
	 * @参数描述:
	 */
	public boolean insertTimesPage(TimesPageModel timesPageModel);
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-28 下午2:12:14
	 * @功能描述:根据时段组编号查询时段信息条数  
	 * @参数描述:
	 */
	public Integer getTimesPageCountByTimesGroupNum(String time_group_num);
	
	
	
}
