package com.kuangchi.sdd.baseConsole.times.timesobject.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.baseConsole.times.holidaytimes.model.HolidayTimesModel;
import com.kuangchi.sdd.baseConsole.times.timesobject.model.TimesObject;

/**
 * @创建人　: 梁豆豆
 * @创建时间: 2016-4-5 下午5:54:01
 * @功能描述:	对象时段组模块-Dao类
 */
public interface TimesObjectDao {
	
	/** 新增对象时段组*/
	public boolean addTimesObject(List<TimesObject> timesObjectList);
	
	/** 根据条件查询对象时段组*/
	public List<TimesObject> getTimesObjectByParam(TimesObject timesObject);
	
	/** 根据条件查询对象时段组(分页)*/
	public List<TimesObject> getTimesObjectByParamPage(TimesObject timesObject,int page, int size);
	
	/** 根据条件查询对象时段组总数*/
	public int getTimesObjectByParamCount(TimesObject timesObject);
	
    /** 根据节假日时段编号查询日期*/
    public HolidayTimesModel getHolidayByNum(String holiday_time_num);
    
    /** 根据时段组编号查询时段组名称*/
    public String getGroupNameByNum(String group_num);
    
    /** 根据设备编号查询节假日时段(分页) */
    public List<HolidayTimesModel> getHolidayTimeByDevicePage(String device_num,int page, int size);
    
    /** 根据设备编号查询节假日时段 */
    public List<HolidayTimesModel> getHolidayTimeByDevice(String device_num);
    
    /** 根据设备编号查询节假日时段总数 */
    public int getHolidayTimeByDeviceCount(String device_num);
    
    /** 删除所有对象时段组 */
    public void deleteTimesObjectByDevice(String object_type, String device_num);
    
    /**查询所有对象时段中的用户时段组（分页）*/
    public List<TimesObject> getUserTimesGroup(TimesObject timesObject,int page, int size);
    
    /**查询所有对象时段中的用户时段组总行数*/
    public int getUserTimesGroupCount(TimesObject timesObject);
    
    /**查询所有对象时段中的节假日时段组（分页）*/
    public List<TimesObject> getHolidayTimesGroup(Map<String, Object> map);
    
    /**查询所有对象时段中的节假日时段组总行数*/
    public int getHolidayTimesGroupCount(Map<String, Object> map);
    
    /**根据时段组名称查询时段组编号*/
    public String getGroupNumByName(String group_name);
    
    
    
    
}
