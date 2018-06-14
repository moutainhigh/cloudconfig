package com.kuangchi.sdd.baseConsole.holiday.dao;

import java.util.List;

import com.kuangchi.sdd.baseConsole.holiday.model.Holiday;
import com.kuangchi.sdd.baseConsole.holiday.model.HolidayType;
import com.kuangchi.sdd.baseConsole.holiday.model.ObjectTime;

/**
 * @创建人　: 杨金林
 * @创建时间: 2016-3-24 16:10:39
 * @功能描述: 节假日模块-Dao类
 */
public interface HolidayDao {
	
    /** 新增节假日信息*/
	boolean addHoliday(Holiday holiday);
	
	/** 新增节假日类型信息*/
	boolean addHolidayType(HolidayType holidayType);
	
	/** 删除节假日信息*/
	boolean deleteHolidayById(String holiday_ids);
	
	/** 删除节假日类型信息*/
	boolean deleteHolidayTypeById(String holiday_type_ids);
	
	/** 修改节假日信息*/
	boolean modifyHoliday(Holiday holiday);
	
	/** 修改节假日类型信息*/
	boolean modifyHolidayType(HolidayType holidayType);
	
	/** 根据条件节假日信息 */
    List<Holiday> getHolidayByParam(Holiday holiday);
    
    /** 根据条件查询节假日信息 (分页) */
    List<Holiday> getHolidayByParamPage(Holiday holiday,int page, int size);
    
    /** 根据条件查询节假日信息总数 */
    int getHolidayByParamCount(Holiday holiday);
    
	/** 根据条件节假日类型信息 */
    List<HolidayType> getHolidayTypeByParam(HolidayType holidayType);
    /** 根据名字精确查询节假日*/
    List<HolidayType> getHolidayTypeByName(HolidayType holidayType);
    
    /** 根据条件查询节假日类型信息 (分页) */
    List<HolidayType> getHolidayTypeByParamPage(HolidayType holidayType,int page, int size,String begin,String end);
    
    /** 根据条件查询节假日类型信息总数 */
    int getHolidayTypeByParamCount(HolidayType holidayType,String begin,String end);
   
	/** 判断是否有符合该对象编号的对象*/
	List<ObjectTime> getObjectTimeInfo(String object_nums);
	
	/** 批量导入节假日*/
	public void batchAddHoliday(List<Holiday> olidayList);
	
	/**根据类型编号查询节假日信息*/
    List<Holiday> getHolidayByType(Holiday holiday);
    
    /**
     * @创建人　: 邓积辉
     * @创建时间: 2016-8-8 下午2:57:16
     * @功能描述: 获取所有的节假日名称
     * @参数描述:
     */
    public List<String> getAllHolidayNameType();
    
    
    public Holiday getHolidayById(String holiday_id);
    
    /**
     * 查询是否有交叉的节假日
     * @author yuman.gao
     */
    public Integer getCrossHoliday(String holiday_begin_date, String holiday_end_date, String holiday_id);
	
}
