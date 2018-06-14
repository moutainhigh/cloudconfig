package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/11/29.
 */
public interface AppointmentMapper {

    public int insertAppointment(Map<String,Object> map);

    public int updateAppointment(Map<String,Object> map);

    public int deleteAppointment(@Param("id")String id);

    public  int insertCourseList(@Param("list") List<Map<String,Object>> list);

    public int deleteCourseByAppointmentId(@Param("appointmentId")String appointmentId);


    public  int deleteAppointmentCallbackByAppointmentId(@Param("appointmentId") String appointmentId);

    public int insertAppointmentCallbackList(@Param("list")List<Map<String,Object>> list);

    public List<Map<String ,Object>> selectCourseValuesByAppointmentId(@Param("idList") List<String> idList);

    public List<String> selectCourseIdByAppointmentId(@Param("appointmentId")String appointmentId);

    public  int deleteAppointmentCallback(@Param("id")String id);

    public  List<Map<String,Object>> selectAppointList( @Param("courseId") String courseId,@Param("channel") String channel,@Param("adviserId") String adviserId,@Param("time")String time, @Param("start") Integer start,@Param("pageSize") Integer pageSize);

    public Integer  selectAppointListCount(@Param("courseId") String courseId,@Param("channel") String channel,@Param("adviserId") String adviserId,@Param("time")String time);

    public List<Map<String ,Object>> selectAppointmentCallbackByAppointmentId(@Param("appointmentId")String apppointmentId);

    public Map<String,Object>  selectAppointmentById(@Param("id")String id);


    public List<String> selectAdviserIdByMobile(@Param("mobile")String mobile);

    public List<String> selectAdviserIdByProvince(@Param("province")String province);
}
