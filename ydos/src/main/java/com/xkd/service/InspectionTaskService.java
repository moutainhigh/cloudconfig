package com.xkd.service;

import com.xkd.mapper.InspectionTaskMapper;
import com.xkd.model.Exercise;
import com.xkd.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.omg.CORBA.INTERNAL;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/2/24.
 */
@Service
public class InspectionTaskService {

    @Autowired
    InspectionTaskMapper inspectionTaskMapper;
    @Autowired
    ExerciseService exerciseService;

    public int insertList(List<Map<String, Object>> tasks) {
        return inspectionTaskMapper.insertList(tasks);
    }

    public int updateInspectionTask(Map<String, Object> map) {
        return inspectionTaskMapper.updateInspectionTask(map);
    }

    public List<Map<String, Object>> selectHistoryTaskByPlanId(String inspectionPlanId, String fromDate, String toDate,  Integer isExceedTime, String completedBy, Integer currentPage, Integer pageSize) {
        Integer start = 0;
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        start = (currentPage - 1) * pageSize;
        return inspectionTaskMapper.selectHistoryTaskByPlanId(inspectionPlanId, fromDate, toDate,isExceedTime,completedBy, start, pageSize);


    }

    public int selectHistoryTaskCountByPlanId(String inspectionPlanId, String fromDate, String toDate, Integer isExceedTime, String completedBy) {
        return inspectionTaskMapper.selectHistoryTaskCountByPlanId(inspectionPlanId, fromDate, toDate,isExceedTime,completedBy);
    }


    public List<Map<String, Object>> searchInspectionTask(String pcCompanyId, Integer period, String companyId, String departmentId, String fromDate, String toDate, Integer isDone, Integer currentPage, Integer pageSize) {

        Integer start = 0;
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        start = (currentPage - 1) * pageSize;

        if (StringUtils.isNotBlank(toDate)) {
            toDate = toDate + " 23:59:59";
        }
        if (StringUtils.isNotBlank(fromDate)){
            fromDate=fromDate+" 00:00:00";
        }

        List<Map<String, Object>> list = inspectionTaskMapper.searchInspectionTask(pcCompanyId, period, companyId, departmentId,fromDate,toDate,isDone, start, pageSize);
        List<String> inspectionPlanIdList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            inspectionPlanIdList.add((String) list.get(i).get("inspectionPlanId"));
        }

        for (int i = 0; i <list.size() ; i++) {
            Map<String,Object> task=list.get(i);
            Integer taskPeriod= (Integer) task.get("period");
            if (taskPeriod!=null) {
                if (taskPeriod == 1) {
                    if ((Long) task.get("exceedHours") < 24 && (Long) task.get("exceedHours")>0) {
                        task.put("exceedDays", task.get("exceedHours") + "小时");
                    } else {
                        task.put("exceedDays", task.get("exceedDays") + "天");
                    }
                } else {
                    task.put("exceedDays", task.get("exceedDays") + "天");
                }
            }
        }

        List<Map<String, Object>> deviceGroupList = new ArrayList<>();
        if (inspectionPlanIdList.size() > 0) {
            deviceGroupList = inspectionTaskMapper.selectDeviceGroupByInspectitonIds(inspectionPlanIdList);
        }
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < deviceGroupList.size(); j++) {
                Map<String, Object> taskMap = list.get(i);
                if (taskMap.get("inspectionPlanId").equals(deviceGroupList.get(j).get("inspectionPlanId"))) {
                    if (StringUtils.isBlank((String) taskMap.get("deviceGroupNames"))) {
                        taskMap.put("deviceGroupNames", deviceGroupList.get(j).get("groupName"));
                    } else {
                        taskMap.put("deviceGroupNames", taskMap.get("deviceGroupNames") + " " + deviceGroupList.get(j).get("groupName"));

                    }
                }
            }
        }
        return list;

    }

    public int searchInspectionTaskCount(String pcCompanyId, Integer period, String companyId, String departmentId, String fromDate, String toDate, Integer isDone) {
        if (StringUtils.isNotBlank(toDate)) {
            toDate = toDate + " 23:59:59";
        }
        if (StringUtils.isNotBlank(fromDate)){
            fromDate=fromDate+" 00:00:00";
        }

        return inspectionTaskMapper.searchInspectionTaskCount(pcCompanyId, period, companyId, departmentId ,fromDate,toDate,isDone);
    }


    public List<Map<String, Object>> searchTechnicalInspectionTask(String departmentId,Integer period,String companyId, String fromDate, String toDate,Integer isDone, Integer currentPage, Integer pageSize) {
        Integer start = 0;
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        start = (currentPage - 1) * pageSize;


        if (StringUtils.isNotBlank(toDate)) {
            toDate = toDate + " 23:59:59";
        }
        if (StringUtils.isNotBlank(fromDate)){
            fromDate=fromDate+" 00:00:00";
        }

        List<Map<String, Object>> list = inspectionTaskMapper.searchTechnicalInspectionTask(departmentId,period,companyId,fromDate,toDate,isDone, start, pageSize);
        List<String> inspectionPlanIdList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            inspectionPlanIdList.add((String) list.get(i).get("inspectionPlanId"));
        }

        for (int i = 0; i <list.size() ; i++) {
            Map<String,Object> task=list.get(i);
            Integer taskPeriod= (Integer) task.get("period");
            if (taskPeriod!=null) {
                if (taskPeriod == 1) {
                    if ((Long) task.get("exceedHours") < 24 && (Long) task.get("exceedHours")>0) {
                        task.put("exceedDays", task.get("exceedHours") + "小时");
                    } else {
                        task.put("exceedDays", task.get("exceedDays") + "天");
                    }
                } else {
                    task.put("exceedDays", task.get("exceedDays") + "天");
                }
            }
        }

        List<Map<String, Object>> deviceGroupList = new ArrayList<>();
        if (inspectionPlanIdList.size() > 0) {
            deviceGroupList = inspectionTaskMapper.selectDeviceGroupByInspectitonIds(inspectionPlanIdList);
        }
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < deviceGroupList.size(); j++) {
                Map<String, Object> taskMap = list.get(i);
                if (taskMap.get("inspectionPlanId").equals(deviceGroupList.get(j).get("inspectionPlanId"))) {
                    if (StringUtils.isBlank((String) taskMap.get("deviceGroupNames"))) {
                        taskMap.put("deviceGroupNames", deviceGroupList.get(j).get("groupName"));
                    } else {
                        taskMap.put("deviceGroupNames", taskMap.get("deviceGroupNames") + " " + deviceGroupList.get(j).get("groupName"));

                    }
                }
            }
        }
        return list;

    }

    public int searchTechnicalInspectionTaskCount(String deparmentId,Integer period,String companyId,  String fromDate, String toDate,Integer isDone) {
        if (StringUtils.isNotBlank(toDate)) {
            toDate = toDate + " 23:59:59";
        }
        if (StringUtils.isNotBlank(fromDate)){
            fromDate=fromDate+" 00:00:00";
        }
        return inspectionTaskMapper.searchTechnicalInspectionTaskCount(deparmentId,period,companyId,   fromDate,   toDate,isDone);
    }



    public List<Map<String,Object>> searchCustomerInspectionTask(String dateFrom,String dateTo,List<String> companyIdList,Integer currentPage,Integer pageSize){
        Integer start = 0;
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        start = (currentPage - 1) * pageSize;
        if (companyIdList.size()==0){
            return new ArrayList<>();
        }
        List<Map<String,Object>> list= inspectionTaskMapper.searchCustomerInspectionTask(dateFrom,dateTo,companyIdList,currentPage,pageSize);
        List<String> inspectionPlanIdList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            inspectionPlanIdList.add((String) list.get(i).get("inspectionPlanId"));
        }
        List<Map<String, Object>> deviceGroupList = new ArrayList<>();
        if (inspectionPlanIdList.size() > 0) {
            deviceGroupList = inspectionTaskMapper.selectDeviceGroupByInspectitonIds(inspectionPlanIdList);
        }
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < deviceGroupList.size(); j++) {
                Map<String, Object> taskMap = list.get(i);
                if (taskMap.get("inspectionPlanId").equals(deviceGroupList.get(j).get("inspectionPlanId"))) {
                    if (StringUtils.isBlank((String) taskMap.get("deviceGroupNames"))) {
                        taskMap.put("deviceGroupNames", deviceGroupList.get(j).get("groupName"));
                    } else {
                        taskMap.put("deviceGroupNames", taskMap.get("deviceGroupNames") + " " + deviceGroupList.get(j).get("groupName"));

                    }
                }
            }
        }
        return list;
    }
    public Integer searchCustomerInspectionTaskCount(String dateFrom,String dateTo,List<String> companyIdList){
        return inspectionTaskMapper.searchCustomerInspectionTaskCount(dateFrom,dateTo,companyIdList);
    }



    public Map<String,Object>  getTaskById(String id){
        Map<String, Object> taskMap = inspectionTaskMapper.selectTaskDetail(id);
        return taskMap;
    }



    public Map<String, Object> selectTaskDetail(String id) {

        Map<String, Object> taskMap = inspectionTaskMapper.selectTaskDetail(id);
        List<String> inspectionPlanIdList = new ArrayList<>();

        inspectionPlanIdList.add((String) taskMap.get("inspectionPlanId"));

        List<Map<String, Object>> deviceGroupList = new ArrayList<>();
        if (inspectionPlanIdList.size() > 0) {
            deviceGroupList = inspectionTaskMapper.selectDeviceGroupByInspectitonIds(inspectionPlanIdList);
        }
        for (int i = 0; i <deviceGroupList.size() ; i++) {
            if (StringUtils.isBlank((String) taskMap.get("deviceGroupNames"))) {
                taskMap.put("deviceGroupNames", deviceGroupList.get(i).get("groupName"));
            } else {
                taskMap.put("deviceGroupNames", taskMap.get("deviceGroupNames") + " " + deviceGroupList.get(i).get("groupName"));

            }
        }


        //未完成的全部取出来
        List<Map<String,Object>>  taskNoteList=inspectionTaskMapper.selectInspectionTaskNote((String) taskMap.get("inspectionPlanId"),0);
        //已完成的最多给一百条
        List<Map<String,Object>>  taskNoteListCompleted=inspectionTaskMapper.selectInspectionTaskNote((String) taskMap.get("inspectionPlanId"),1);

        taskMap.put("taskNoteList",taskNoteList);
        taskMap.put("taskNoteListCompleted",taskNoteListCompleted);

        //用来代表巡检任务
        String meetingId = id;
        Exercise exercise = exerciseService.getExerciseAnswer((String) taskMap.get("templateId"), (String)taskMap.get("completedBy"),meetingId);
        taskMap.put("exercise",exercise);

        return taskMap;

    }






    public int insertTaskNote(Map<String,Object> map){
        return inspectionTaskMapper.insertTaskNote(map);
    }


    public int completeTaskNote(Map<String,Object> map) {
        return inspectionTaskMapper.completeTaskNote(map);
    }



    public List<Map<String,Object>> selectInspectionTaskByGroupId( String  groupId,String pcCompanyId,Integer currentPage,Integer pageSize){
        Integer start=0;
        if (currentPage==null){
            currentPage=1;

        }
        if (pageSize==null){
            pageSize=10;
        }

        return inspectionTaskMapper.selectInspectionTaskByGroupId(groupId,  pcCompanyId,start,pageSize);
    }

    public int selectInspectionTaskCountByGroupId( String groupId,String pcCompanyId){
        return inspectionTaskMapper.selectInspectionTaskCountByGroupId(groupId,pcCompanyId);
    }


    public List<Map<String,Object>>  selectInspectionTaskByInspectionId( String inspectionPlanId,String fromTime,String toTime ){
        List<Map<String,Object>>  list=  inspectionTaskMapper.selectInspectionTaskByInspectionId(inspectionPlanId, fromTime, toTime);


        return list;


    }

    public List<Map<String, Object>> selectFinishedInspectionTaskByInspectionPlan(List inspectionPlanList, String startDate, String endDate, Integer currentPage, Integer pageSize){
        if (inspectionPlanList==null||inspectionPlanList.size()==0){
            return new ArrayList<>();
        };
        Integer start = (currentPage - 1) * pageSize;
        List<Map<String, Object>> inspectionList =  inspectionTaskMapper.selectFinishedInspectionTaskByInspectionPlan(inspectionPlanList, startDate, endDate, start, pageSize);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh-mm");
        for (Map<String, Object> inspctionMap:inspectionList
                ) {
            Timestamp planCompletionDate = (Timestamp) inspctionMap.get("planCompletionDate");
            inspctionMap.put("planCompletionDate", format.format(planCompletionDate));
            Timestamp completionDate = (Timestamp) inspctionMap.get("completionDate");
            inspctionMap.put("completionDate", format.format(completionDate));
        }
        return inspectionList;
    }

    public Integer countFinishedInspectionTaskByInspectionPlan(List inspectionPlanList, String startDate, String endDate){
        if (inspectionPlanList.size()==0){
            return 0;
        }
        return inspectionTaskMapper.countFinishedInspectionTaskByInspectionPlan(inspectionPlanList, startDate, endDate);
    }

    public Integer countInspectionTaskByPcCompanyIdAndDate(String pcCompanyId, Integer status){
        return inspectionTaskMapper.countInspectionTaskByPcCompanyIdAndDate(pcCompanyId, status);
    }

    public List<Map<String, Object>> selectInspectionByUserAndDate(String userId, Integer type, String date){
        return inspectionTaskMapper.selectInspectionByUserAndDate(userId, type, date);
    }

    public   List<Map<String,Object>> selectToRemindInspectionTechinican(){
        return inspectionTaskMapper.selectToRemindInspectionTechinican();
    }


    public void updateRemindStatusByIds( List<String> idList){
        inspectionTaskMapper.updateRemindStatusByIds(idList);
    }

    public List<Map<String,Object>> selectHistoryInspectionTaskByGroupId(String groupId,String fromDate,String toDate,Integer currentPage,Integer pageSize){
        Integer start=0;
        if (currentPage==null){
            currentPage=1;
        }
        if (pageSize==null){
            pageSize=10;
        }

        start=(currentPage-1)*pageSize;
        return inspectionTaskMapper.selectHistoryInspectionTaskByGroupId(groupId,fromDate,toDate,start,pageSize);
    }

    public Integer selectHistoryInspectionTaskCountByGroupId(String groupId,String fromDate,String toDate){
        return inspectionTaskMapper.selectHistoryInspectionTaskCountByGroupId(groupId,fromDate,toDate);
    }

    public List<Map<String,Object>> selectHistoryInspectionAndRepaireByUserId(String pcCompanyId,String userId,String fromDate,String toDate,Integer ttype,Integer currentPage,Integer pageSize){
        Integer start=0;
        if (currentPage==null){
            currentPage=1;
        }
        if (pageSize==null){
            pageSize=10;
        }

        start=(currentPage-1)*pageSize;

        return inspectionTaskMapper.selectHistoryInspectionAndRepaireByUserId(pcCompanyId,userId,fromDate,toDate,ttype,start,pageSize);
    }

    public  Integer selectHistoryInspectionAndRepaireCountByUserId(String pcCompanyId,String userId,String fromDate,String toDate,Integer ttype){
        return inspectionTaskMapper.selectHistoryInspectionAndRepaireCountByUserId(pcCompanyId,userId,fromDate,toDate,ttype);
    }

    public List<Map<String, Object>> selectCurrentInspection(String deviceId){
        return inspectionTaskMapper.selectCurrentInspection(deviceId);
    }

    public Integer countUserInspection(String pcCompanyId, String userId){
        return inspectionTaskMapper.countUserInspection(pcCompanyId, userId);
    }


}
