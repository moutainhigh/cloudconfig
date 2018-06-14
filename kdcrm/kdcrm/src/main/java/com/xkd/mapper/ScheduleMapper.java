package com.xkd.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Schedule;

public interface ScheduleMapper {

	/**
	 * 查询某老师在选定的时间段已经排上的课程
	 * @return
	 */
	Integer getExistedSchedule(@Param("id")Integer id,@Param("teacherIds")List<Integer> teacherIds,
			@Param("startDate")String startDate,
			@Param("endDate")String endDate);

	/**
	 * 添加行程，排课
	 * @param schedule
	 * @return
	 */
	Integer insertSchedule(Schedule schedule);
	
	/**
	 * 修改行程，排课
	 * @param schedule
	 * @return
	 */
	Integer updateScheduleById(Schedule schedule);
	

	Integer getTotalSchedule(Map<String, Object> map);
	
	/**
	 * 跟据Schedule ID 删除行程
	 * @param id
	 * @return
	 */
	Integer deleteScheduleById(@Param("id")Integer id);
	/**
	 * 查询一个月行程
	 * @return
	 */
	List<Schedule> selectScheduleByOneMonth(
			@Param("createdBy") String createdBy,
			@Param("teacherId")List<String> teacherId,
			@Param("beginDate")String beginDate,
			@Param("endDate")String endDate,
			@Param("userId")String userId,
			@Param("pcCompanyId")String pcCompanyId);
	/**
	 * 根据Id查询出整个行程及详情
	 * @param id
	 * @return
	 */
	Schedule selectScheduleById(@Param("id")String id,@Param("userId")String userId);

	List<HashMap<String, Object>> checkUserTongXR(@Param("list")List<String> list, @Param("startDate")String startDate, @Param("endDate")String endDate,@Param("scheduleId")String scheduleId);

	List<HashMap<String, Object>> getScheduleColleagueListBySid(@Param("id")String id);

	/**
	 * 查询行程列表信息
	 * @return
	 */
	//List<Schedule> selectScheduleList(Map<String, Object> map);//？？还有客户经理
	
	List<HashMap<String, Object>>  getObjScheduleList(Map<String, Object> map);

	List<Map<String, Object>> getTeacherAll(String adviserName);

	void saveScheduleLogger(Schedule schedule);
	
	List<HashMap<String, String>> getScheduleLogger(@Param("scheduleId")String scheduleId, @Param("pageSizeInt")int pageSizeInt,@Param("currentPageInt") int currentPageInt);
	
	int getScheduleLoggerTotal(@Param("scheduleId")String scheduleId);
	
	List<HashMap<String, String>> getCompanyUserLike(@Param("content")String content,@Param("pcCompanyId")String pcCompanyId);

	void deleteScheduleById(@Param("scheduleId")String scheduleId,@Param("userId")String userId);

	List<Schedule> getScheduleXcx(Map<String, Object> map);

	List<Map<String, Object>> getXcxScheduleDynamic(@Param("userId")String userId,@Param("ttype")String ttype,
													@Param("pageNo")int pageNo,@Param("pageSize")int pageSize);

	Schedule getXcxScheduleLoggerById(String id);

	Schedule getScheduleById(String id);

    int getXcxScheduleDynamicTotal(@Param("userId")String userId,@Param("ttype")String ttype);

    List<Schedule> getUserSchedule(@Param("id")String id);

	List<Map<String,Object>> getScheduleTypeList(Map<String, Object> map);

    List<Map<String,Object>> getScheduleComp(Map<String, Object> map);

	List<Map<String,Object>> getWriteExcelScheduleTypeList(Map<String, Object> map);

	List<Map<String,Object>> getScheduleTitle(Map<String, Object> map);

	List<Map<String,Object>> getScheduleCompTitle(Map<String, Object> map);

	List<Map<String,Object>> getWriteExcelScheduleComp(Map<String, Object> map);

	List<HashMap<String,Object>> getWriteExcelCompTongJi(Map<String, Object> map);

	List<HashMap<String,Object>> getWriteExcelScheduleTongJi(Map<String, Object> map);

}
