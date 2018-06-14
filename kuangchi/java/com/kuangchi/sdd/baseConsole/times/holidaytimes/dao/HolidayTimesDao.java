package com.kuangchi.sdd.baseConsole.times.holidaytimes.dao;

import java.util.List;

import com.kuangchi.sdd.baseConsole.times.holidaytimes.model.HolidayTimesInterf;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.model.HolidayTimesModel;

/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-5-24上午8:55:07
 * @功能描述:节假日时段管理dao层接口
 * @参数描述:
 */
public interface HolidayTimesDao {
	/**
	 * 根据条件查询（分页）
	 */
	 List<HolidayTimesModel> getHolidayTimesByParam(HolidayTimesModel holidayDate,String exist_nums,int page, int size);
	
	/**
	 * 增加
	 */
	 boolean insertHolidayTimes(HolidayTimesModel holidayTimes);
	
	/**
	 * 修改
	 */
	 boolean updateHolidayTimes(HolidayTimesModel holidayTimes);
	
	/**
	 * 删除
	 */
	 boolean delHolidayTimes(String holidayTimes_ids);
	
	/**
	 * 根据条件查询记录总数
	 */
	 Integer countHolidayTimes(HolidayTimesModel holidayDate, String exist_nums);
	
	/**
	 * 根据条件查询（导出）
	 */
	 List<HolidayTimesModel> exportHolidayTimes(HolidayTimesModel holidayDate);
	 
	 /**
	  * 查询所有日期记录（调用接口用）
	  */
	 List<HolidayTimesInterf> getHolidayTimesInter();
	 
	 /**
	  * 根据假日时段编号查询所有记录
	  */
	 List<HolidayTimesModel> getByholidayNumDao(String ht_num);
}
