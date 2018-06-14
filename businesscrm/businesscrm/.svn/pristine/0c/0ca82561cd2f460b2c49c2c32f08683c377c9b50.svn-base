package com.xkd.service;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.xkd.mapper.AppointmentMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/11/29.
 */
@Service
public class AppointmentService {
    @Autowired
    AppointmentMapper appointmentMapper;

    public List<Map<String,Object>> selectAppointList( String courseId, String channel, String adviserId,String time,Integer start, Integer pageSize){
        List<Map<String,Object>> list=  appointmentMapper.selectAppointList(courseId,channel,adviserId,time,start,pageSize);
        List<String> idList=new ArrayList<>();
        for (int i = 0; i <list.size() ; i++) {
            idList.add((String) list.get(i).get("id"));
        }

        if (idList.size()>0){
            List<Map <String ,Object>> courseList=appointmentMapper.selectCourseValuesByAppointmentId(idList);
            for (int i = 0; i <list.size() ; i++) {
                for (int j = 0; j <courseList.size() ; j++) {
                    if (courseList.get(j)!=null) {
                        if (list.get(i).get("id").equals(courseList.get(j).get("appointmentId"))) {
                            list.get(i).put("courses", courseList.get(j).get("courses"));
                            break;
                        }
                    }
                }
            }

        }
        return list;
    }

    public List<Map<String ,Object>> selectAppointmentCallbackByAppointmentId(String apppointmentId){
        return appointmentMapper.selectAppointmentCallbackByAppointmentId(apppointmentId);
    }




    public Map<String,Object>  selectAppointmentById(String id){
        Map<String,Object>  map= appointmentMapper.selectAppointmentById(id);
        List<String> idList=new ArrayList<>();
        idList.add((String) map.get("id"));
        List<Map<String,Object>> mmList=appointmentMapper.selectCourseValuesByAppointmentId(idList);
        if (mmList.size()>0){
            if (mmList.get(0)!=null) {
                map.put("courses", mmList.get(0).get("courses"));
            }
        }
        List<String> courseIdList=appointmentMapper.selectCourseIdByAppointmentId(id);
        map.put("courseIdList",courseIdList);
        return map;
    }

    public int insertAppointment(Map<String,Object> map){
        return appointmentMapper.insertAppointment(map);
    }

    public int updateAppointment(Map<String,Object> map){
        return appointmentMapper.updateAppointment(map);
    }

    public int deleteAppointment(String id){
        return appointmentMapper.deleteAppointment(id);
    }

    public  int insertCourseList(List<Map<String,Object>> list){
        return  appointmentMapper.insertCourseList(list);
    }

    public int deleteCourseByAppointmentId(String appointmentId){
        return appointmentMapper.deleteCourseByAppointmentId(appointmentId);
    }


    public  int deleteAppointmentCallbackByAppointmentId(String appointmentId){
        return  appointmentMapper.deleteAppointmentCallbackByAppointmentId(appointmentId);
    }


    public int insertAppointmentCallbackList(List<Map<String,Object>> list){
        return appointmentMapper.insertAppointmentCallbackList(list);
    }


    public  int deleteAppointmentCallback(String id){
        return appointmentMapper.deleteAppointmentCallback(id);
    }



    public Integer  selectAppointListCount( String courseId, String channel, String adviserId,String time){
        return  appointmentMapper.selectAppointListCount(courseId,channel,adviserId,time);
    }


    public List<String> selectAdviserIdByMobile(String mobile){
        return appointmentMapper.selectAdviserIdByMobile(mobile)   ;
    }

    public List<String> selectAdviserIdByProvince(String province){
        return appointmentMapper.selectAdviserIdByProvince(province);
    }

}

