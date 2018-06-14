package com.kuangchi.sdd.baseConsole.times.timesobject.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.times.holidaytimes.model.HolidayTimesModel;
import com.kuangchi.sdd.baseConsole.times.timesobject.dao.TimesObjectDao;
import com.kuangchi.sdd.baseConsole.times.timesobject.model.TimesObject;

/**
 * @创建人　: 梁豆豆
 * @创建时间: 2016-4-5 下午6:27:22
 * @功能描述:	对象时段组模块-Dao实现类
 */
@Repository("timesObjectDaoImpl")
public class TimesObjectDaoImpl extends BaseDaoImpl<TimesObject> implements TimesObjectDao{

	@Override
	public boolean addTimesObject(List<TimesObject> timesObjectList) {
		return insert("addTimesObject", timesObjectList);
	}

	@Override
	public List<TimesObject> getTimesObjectByParam(TimesObject timesObject) {
		return getSqlMapClientTemplate().queryForList("getTimesObjectByParam", timesObject);
	}

	@Override
	public List<TimesObject> getTimesObjectByParamPage(TimesObject timesObject, int page,
			int size) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("object_type", timesObject.getObject_type());
		param.put("group_num", timesObject.getGroup_num());
		param.put("validity_flag", 0);
		param.put("page", (page - 1) * size);
		param.put("size", size);
		return this.getSqlMapClientTemplate().queryForList("getTimesObjectByParamPage", param);
	}

	@Override
	public int getTimesObjectByParamCount(TimesObject timesObject) {
		return (Integer) find("getTimesObjectByParamCount", timesObject);
	}

	@Override
    public HolidayTimesModel getHolidayByNum(String holiday_time_num){
		return (HolidayTimesModel) this.getSqlMapClientTemplate().queryForObject("getHolidayByNum", holiday_time_num);
    }
	
	@Override
    public String getGroupNameByNum(String group_num) {
		return (String)this.getSqlMapClientTemplate().queryForList("getGroupNameByNum", group_num).get(0);
    }
	
    @Override
	public String getNameSpace() {
		return "common.TimesObject";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<HolidayTimesModel> getHolidayTimeByDevicePage(
			String device_num, int page, int size) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("device_num", device_num);
		param.put("page", (page - 1) * size);
		param.put("size", size);
		return this.getSqlMapClientTemplate().queryForList("getHolidayTimeByDevicePage", param);
	}
	
	@Override
	public List<HolidayTimesModel> getHolidayTimeByDevice(String device_num) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("device_num", device_num);
		return this.getSqlMapClientTemplate().queryForList("getHolidayTimeByDevice", param);
	}

	@Override
	public int getHolidayTimeByDeviceCount(String device_num) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("device_num", device_num);
		return  (Integer) find("getHolidayTimeByDeviceCount", param);
	}

	@Override
	public void deleteTimesObjectByDevice(String object_type, String device_num) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("device_num", device_num);
		param.put("object_type", object_type);
		this.getSqlMapClientTemplate().delete("deleteAllTimesObject", param);
	}

	/**查询所有对象时段中的用户时段组（分页）*/
	@Override
	public List<TimesObject> getUserTimesGroup(TimesObject timesObject,
			int page, int size) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("group_name", timesObject.getGroup_name());
		param.put("page", (page - 1) * size);
		param.put("size", size);
		return this.getSqlMapClientTemplate().queryForList("getUserTimesGroupByParamPage", param);
	}
    
	/**查询所有对象时段中的用户时段组总行数*/
	@Override
	public int getUserTimesGroupCount(TimesObject timesObject) {
		 
		return (Integer) find("getUserTimesGroupByParamCount", timesObject);
	}
    
	/**查询所有对象时段中的节假日时段组（分页）*/
	@Override
	public List<TimesObject> getHolidayTimesGroup(Map<String, Object> map) {
		   return this.getSqlMapClientTemplate().queryForList("getHolidayTimesGroupByParamPage", map);
	}
    
	/**查询所有对象时段中的节假日时段组总行数*/
	@Override
	public int getHolidayTimesGroupCount(Map<String, Object> map) {
		return (Integer) find("getHolidayTimesGroupByParamCount",map);
	}
    
	/**根据时段组名称查询时段组编号*/
	@Override
	public String getGroupNumByName(String group_name) {
		return (String) this.getSqlMapClientTemplate().queryForObject("getGroupNumByName", group_name);
	}
}
