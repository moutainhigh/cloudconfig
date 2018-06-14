package com.kuangchi.sdd.elevatorConsole.times.service;

import java.util.List;

import com.kuangchi.sdd.elevatorConsole.times.model.TimesPageModel;

public interface TimesPageService {
	/**
	 * @return 
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-28 下午12:30:59
	 * @功能描述:设置时段信息 
	 * @参数描述:
	 */
	public boolean motifyTimesPage(List<TimesPageModel> list,String create_user);
	
	/**
	 * @return 
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-28 下午12:30:59
	 * @功能描述:新增时段信息 
	 * @参数描述:
	 */
	public boolean insertTimesPage(List<TimesPageModel> list, String create_user);
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-28 下午2:12:14
	 * @功能描述:根据时段组编号查询时段信息条数  
	 * @参数描述:
	 */
	public Integer getTimesPageCountByTimesGroupNum(String time_group_num);
}
