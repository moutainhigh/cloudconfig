package com.kuangchi.sdd.baseConsole.holiday.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.holiday.dao.HolidayDao;
import com.kuangchi.sdd.baseConsole.holiday.model.Holiday;
import com.kuangchi.sdd.baseConsole.holiday.model.HolidayType;
import com.kuangchi.sdd.baseConsole.holiday.model.ObjectTime;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.businessConsole.employee.model.EmployeePosition;

/**
 * @创建人　: 杨金林
 * @创建时间: 2016-3-24 16:15:26
 * @功能描述: 节假日模块-Dao实现类
 */
@Repository("holidayDaoImpl")
public class HolidayDaoImpl extends BaseDaoImpl<Holiday> implements HolidayDao{

	@Override
	public String getNameSpace() {
		return "common.Holiday";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public boolean addHoliday(Holiday holiday) {
		return insert("addHoliday", holiday);
	}

	@Override
	public boolean addHolidayType(HolidayType holidayType) {
		return insert("addHolidayType", holidayType);
	}

	@Override
	public boolean deleteHolidayById(String holiday_ids) {
		return delete("deleteHolidayById", holiday_ids);
	}

	@Override
	public boolean deleteHolidayTypeById(String holiday_type_ids) {
		return delete("deleteHolidayTypeById", holiday_type_ids);
	}

	@Override
	public boolean modifyHoliday(Holiday holiday) {
		return update("modifyHoliday", holiday);
	}

	@Override
	public boolean modifyHolidayType(HolidayType holidayType) {
		return update("modifyHolidayType", holidayType);
	}

	@Override
	public List<Holiday> getHolidayByParam(Holiday holiday) {
		return getSqlMapClientTemplate().queryForList("getHolidayByParam", holiday);
	}

	@Override
	public List<Holiday> getHolidayByParamPage(Holiday holiday,int page, int size) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("holiday_num", holiday.getHoliday_num());
		param.put("holiday_id", holiday.getHoliday_id());
		param.put("holiday_type_num", holiday.getHoliday_type_num());
		param.put("holiday_begin_date", holiday.getHoliday_begin_date());
		param.put("holiday_end_date", holiday.getHoliday_end_date());
		param.put("holiday_scope", holiday.getHoliday_scope());
		param.put("holiday_name", holiday.getHoliday_name());
		param.put("validity_flag", holiday.getValidity_flag());
		param.put("page", (page - 1) * size);
		param.put("size", size);
		return this.getSqlMapClientTemplate().queryForList("getHolidayByParamPage", param);
	}

	@Override
	public int getHolidayByParamCount(Holiday holiday) {
		return (Integer) find("getHolidayByParamCount", holiday);
	}

	@Override
	public List<HolidayType> getHolidayTypeByParam(HolidayType holidayType) {
		return this.getSqlMapClientTemplate().queryForList("getHolidayTypeByParam", holidayType);
	}

	@Override
	public List<HolidayType> getHolidayTypeByParamPage(HolidayType holidayType,int page, int size,String begin,String end) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("holiday_type_id", holidayType.getHoliday_type_id());
		param.put("holiday_type_num", holidayType.getHoliday_type_num());
		param.put("holiday_name", holidayType.getHoliday_name());
		param.put("holiday_validity", holidayType.getHoliday_validity());
		param.put("validity_flag", holidayType.getValidity_flag());
		param.put("holiday_begin_date", begin);
		param.put("holiday_end_date", end);
		param.put("page", (page - 1) * size);
		param.put("size", size);
		return this.getSqlMapClientTemplate().queryForList("getHolidayTypeByParamPage", param);
	}

	@Override
	public int getHolidayTypeByParamCount(HolidayType holidayType,String begin,String end) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("holiday_type_id", holidayType.getHoliday_type_id());
		param.put("holiday_type_num", holidayType.getHoliday_type_num());
		param.put("holiday_name", holidayType.getHoliday_name());
		param.put("holiday_validity", holidayType.getHoliday_validity());
		param.put("validity_flag", holidayType.getValidity_flag());
		param.put("holiday_begin_date", begin);
		param.put("holiday_end_date", end);
		return (Integer) find("getHolidayTypeByParamCount", param);
	}

	@Override
	public List<Holiday> getHolidayByType(Holiday holiday) {
		return getSqlMapClientTemplate().queryForList("getHolidayByType", holiday);
	}
	
	@Override
	public List<ObjectTime> getObjectTimeInfo(String object_nums) {
		return this.getSqlMapClientTemplate().queryForList("getObjectTimeInfo", object_nums);
	}

	@Override
	public void batchAddHoliday(List<Holiday> holidayList) {
		 SqlMapClient sqlMapClient= getSqlMapClient();
	     try {
			sqlMapClient.startBatch();
			sqlMapClient.startTransaction();
			for (int i = 0; i < holidayList.size(); i++) {
				Holiday holiday=holidayList.get(i);
				sqlMapClient.insert("addHoliday",holiday);
			}
			 sqlMapClient.commitTransaction();  			
		} catch (Exception e) {
			try {
				sqlMapClient.getCurrentConnection().rollback();
			} catch (Exception e2) {
			}
			e.printStackTrace();
		}
	}

	@Override
	public List<HolidayType> getHolidayTypeByName(HolidayType holidayType) {
		return this.getSqlMapClientTemplate().queryForList("getHolidayTypeByName", holidayType);
	}

	@Override
	public List<String> getAllHolidayNameType() {
		return getSqlMapClientTemplate().queryForList("getAllHolidayNameType");
	}

	@Override
	public Holiday getHolidayById(String holiday_id) {
		return (Holiday) getSqlMapClientTemplate().queryForObject("getHolidayById",holiday_id);
	}

	@Override
	public Integer getCrossHoliday(String holiday_begin_date,
			String holiday_end_date, String holiday_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("holiday_begin_date", holiday_begin_date);
		map.put("holiday_end_date", holiday_end_date);
		if(holiday_id != null){
			map.put("holiday_id", holiday_id);
		}
		return (Integer) find("getCrossHoliday", map);
	}
	
}
