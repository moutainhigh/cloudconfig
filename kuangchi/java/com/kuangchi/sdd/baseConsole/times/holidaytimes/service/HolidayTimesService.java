package com.kuangchi.sdd.baseConsole.times.holidaytimes.service;

import java.util.List;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.model.HolidayTimesInterf;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.model.HolidayTimesModel;

/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-5-24上午9:40:25
 * @功能描述:节假日时段管理-业务类（service）接口
 * @参数描述:
 */
public interface HolidayTimesService {
	/**根据条件查询*/
	public Grid<HolidayTimesModel> getHolidayTimesByParamService(HolidayTimesModel holidayDate, String exist_nums, int page, int size);
	
	/**增加
	 * @throws Exception */
	public boolean insertHolidayTimesService(HolidayTimesModel holidayTimes,String logUser) throws Exception;
	
	/**修改
	 * @throws Exception */
	public boolean updateHolidayTimesService(HolidayTimesModel holidayTimes,String logUser) throws Exception;
	
	/**删除
	 * @throws Exception */
	public  boolean delHolidayTimesService(String holidayTimes_ids,String logUser) throws Exception;
	
	/**根据条件查询（导出）*/
	public List<HolidayTimesModel> exportHolidayTimesService(HolidayTimesModel holidayDate);
	
	/**查询所有日期记录（调用接口用）*/
	 List<HolidayTimesInterf> getHolidayTimesInterService();
	 
	 /**
	  * 根据假日时段编号查询所有记录
	  */
	 List<HolidayTimesModel> getByholidayNumService(String ht_num);
	
}
