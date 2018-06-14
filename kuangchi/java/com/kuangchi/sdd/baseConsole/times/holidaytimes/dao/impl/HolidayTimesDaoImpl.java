package com.kuangchi.sdd.baseConsole.times.holidaytimes.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.dao.HolidayTimesDao;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.model.HolidayTimesInterf;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.model.HolidayTimesModel;
/**
 * @创建人　: 肖红丽
 * @创建时间: 2016-5-24上午9:19:53
 * @功能描述:节假日时段管理dao层实现类
 * @参数描述:
 */
@Repository("holidayTimesDaoImpl")
public class HolidayTimesDaoImpl extends BaseDaoImpl<HolidayTimesModel> implements HolidayTimesDao{

	@Override
	public List<HolidayTimesModel> getHolidayTimesByParam(HolidayTimesModel holidayDate,String exist_nums, int page, int size) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("start_date",holidayDate.getStartDate());
		param.put("end_date",holidayDate.getEndDate());
		param.put("exist_nums", exist_nums);
		if(page!=0 && size!=0){
			param.put("page", (page - 1) * size);
			param.put("size", size);
		}
		return this.getSqlMapClientTemplate().queryForList("getHolidayTimesByParam",param);
	}

	@Override
	public boolean insertHolidayTimes(HolidayTimesModel holidayTimes) {
		return insert("insertHolidayTimes",holidayTimes);
		
	}

	@Override
	public boolean updateHolidayTimes(HolidayTimesModel holidayTimes) {
		return update("updateHolidayTimes",holidayTimes);
	}

	@Override
	public boolean delHolidayTimes(String holidayTimes_ids) {
		return delete("delHolidayTimes",holidayTimes_ids);
	}

	@Override
	public Integer countHolidayTimes(HolidayTimesModel holidayDate, String exist_nums) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("start_date",holidayDate.getStartDate());
		param.put("end_date",holidayDate.getEndDate());
		param.put("exist_nums", exist_nums);
		return queryCount("countHolidayTimes",param);
	}

	@Override
	public List<HolidayTimesModel> exportHolidayTimes(HolidayTimesModel holidayDate) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("start_date",holidayDate.getStartDate());
		param.put("end_date",holidayDate.getEndDate());
		return this.getSqlMapClientTemplate().queryForList("exportHolidayTimes",param);
	}

	@Override
	public String getNameSpace() {
		return "common.HolidayTimesModel";
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HolidayTimesInterf> getHolidayTimesInter() {
		return this.getSqlMapClientTemplate().queryForList("getAllHTInter");
	}

	@Override
	public List<HolidayTimesModel> getByholidayNumDao(String ht_num) {
		if(!(ht_num == null || ht_num.equals("")))
			return this.getSqlMapClientTemplate().queryForList("getByholidayNum",ht_num);
		return null;
	}

}
