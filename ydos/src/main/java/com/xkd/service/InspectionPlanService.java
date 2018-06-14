package com.xkd.service;

import com.sun.org.apache.xpath.internal.SourceTree;
import com.xkd.mapper.InspectionMapper;
import com.xkd.mapper.InspectionTaskMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.omg.CORBA.DATA_CONVERSION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.util.resources.cldr.aa.CalendarData_aa_DJ;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dell on 2018/2/24.
 */
@Service
public class InspectionPlanService {

    @Autowired
    InspectionMapper inspectionMapper;
    @Autowired
    InspectionTaskMapper inspectionTaskMapper;

    public static List<String> getYearsBetweenTwoDate(Date startDate, Date endDate) {
        List<String> list = new ArrayList<String>();

        //开始时间当年最后一天
        startDate=getYearEndDate(startDate);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(startDate);
        //结束时间当前最后一天
        endDate=getYearEndDate(endDate);


        while (endDate.after(startDate)||endDate.equals(startDate)) {
            StringBuffer stringBuffer = new StringBuffer(datetimeToString(startDate) );
            String str = stringBuffer.toString();
            list.add(str);
            calendar.add(calendar.YEAR, 1);
            startDate = calendar.getTime();
        }
        return list;
    }


    public static List<String> getSeasonBetweenTwoDate(Date startDate, Date endDate) {
        List<String> list = new ArrayList<String>();


       //开始时间所在的季度最后一天
        startDate=getSeaonEndDate(startDate);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(startDate);

        //结束时间所在的季度的最后一天
        endDate=getSeaonEndDate(endDate);


        while (endDate.after(startDate)||endDate.equals(startDate)) {
            StringBuffer stringBuffer = new StringBuffer(datetimeToString(startDate));
            String str = stringBuffer.toString();
            list.add(str);
            calendar.add(calendar.MONTH, 3);
            startDate = getSeaonEndDate(calendar.getTime());
        }
        return list;
    }


    public static List<String> getMonthBetweenTwoDate(Date startDate, Date endDate) {
        List<String> list = new ArrayList<String>();


        //开始时间当月最后一天
        startDate=getMonthEndDate(startDate);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(startDate);

        //结束时间当月最后一天
        endDate=getMonthEndDate(endDate);




        while (endDate.after(startDate)||endDate.equals(startDate)) {
            StringBuffer stringBuffer = new StringBuffer(datetimeToString(startDate));
            String str = stringBuffer.toString();
            list.add(str);
            calendar.add(calendar.MONTH, 1);
            startDate =getMonthEndDate(calendar.getTime());
        }
        return list;
    }





    public static List<String> getWeekBetweenTwoDate(Date startDate, Date endDate) {
        List<String> list = new ArrayList<String>();
        startDate=getWeekEndDate(startDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        endDate=getWeekEndDate(endDate);

        while (endDate.after(startDate)||endDate.equals(startDate)) {
            StringBuffer stringBuffer = new StringBuffer(datetimeToString(startDate));
            String str = stringBuffer.toString();
            list.add(str);
            calendar.add(calendar.DATE, 7);
            startDate = calendar.getTime();
        }
        return list;
    }


    public static List<String> getTimeBetweenTwoDate(Date startDate, Date endDate, List<String> timePoints) {
        List<String> list = new ArrayList<String>();
        if (timePoints.size() == 0) {
            return list;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        while (endDate.after(startDate)) {
            for (int i = 0; i < timePoints.size(); i++) {
                String timep = timePoints.get(i);
                String startD = dateToString(startDate);
                String startDhm = startD + " " + timep;
                list.add(datetimeToString(stringHmtoDate(startDhm)));
                System.out.println(datetimeToString(stringHmtoDate(startDhm)));

            }

            calendar.add(calendar.DATE, 1);
            startDate = calendar.getTime();
        }
        return list;
    }


    public static Date stringHmtoDate(String dateStringHm) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return simpleDateFormat.parse(dateStringHm);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Date stringToDate(String dateStringHm) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return simpleDateFormat.parse(dateStringHm);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);

    }


    public static Date stringToDatetime(String datetimeString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.parse(datetimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    //获取当月所在季度的最后一天
    public static Date getSeaonEndDate(Date date){
        Calendar calender=Calendar.getInstance();
        calender.setTime(date);
        Integer year=calender.get(Calendar.YEAR);
        Integer month=calender.get(Calendar.MONTH);
        month=month+1;//java月份从0开始

        if (month==1||month==2||month==3){
            return     getMonthEndDate(stringToDate(year+"-03-01"));
        }else if (month==4||month==5||month==6){
            return getMonthEndDate(stringToDate(year + "-06-01"));
        }else if (month==7||month==8||month==9){
            return getMonthEndDate(stringToDate(year + "-09-01"));
        }else {
            return getMonthEndDate(stringToDate(year+"-12-01"));
        }
     }

    public static Date getYearEndDate(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR,calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        return stringToDatetime(dateToString(calendar.getTime())+" 23:59:59");
    }



    public static Date getMonthEndDate(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));;
        return stringToDatetime(dateToString(calendar.getTime())+" 23:59:59");
     }


    public static Date getWeekEndDate(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK,calendar.getActualMaximum(Calendar.DAY_OF_WEEK));
        calendar.add(calendar.DATE, 1);
        return stringToDatetime(dateToString(calendar.getTime())+" 23:59:59");
    }



    public static String datetimeToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);

    }

    public static String datetimeToString2(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(date);

    }


    public static String  generateTaskSequence(Integer num){
        String str = String.format("%05d", num);
        return str;
    }






    public static void main(String[] args) {
//        List<String> list = new ArrayList<String>();
//        list.add("10:10");
//        list.add("18:10");
//        getTimeBetweenTwoDate(stringToDatetime("2012-10-11 10:10:10"), stringToDatetime("2015-10-11 10:10:10"), list);
//        getMonthBetweenTwoDate(stringToDatetime("2012-03-31 00:00:00"), stringToDatetime("2015-10-11 10:10:10"));

//        for (int i = 0; i <1000 ; i++) {
//            System.out.println(generateTaskSequence(i));
//        }

        System.out.println(getWeekEndDate(stringToDatetime("2018-03-20 10:10:10"))); ;
//        getMonthEndDate(new Date());
    }


    public void saveInspectionPlan(Map<String, Object> map, String loginUserId) {

        String planId = UUID.randomUUID().toString();
        map.put("id", planId);
        map.put("planNo",datetimeToString2(new Date()));
        map.put("createDate",new Date());
        map.put("createdBy",loginUserId);
        inspectionMapper.insertInspectionPlan(map);
        List<String> groupIdList = (List<String>) map.get("groupIdList");

        for (int i = 0; i < groupIdList.size(); i++) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", UUID.randomUUID().toString());
            m.put("groupId", groupIdList.get(i));
            m.put("inspectionPlanId", planId);
            inspectionMapper.insertInspectionPlanGroup(m);
        }

        //生成巡检任务
        List<String> timepoints = (List<String>) map.get("timepointList");
        Integer type = (Integer) map.get("period");
        String startTime = (String) map.get("startTime");
        String endTime = (String) map.get("endTime");
        Date startD = stringToDate(startTime.trim());
        Date endD = stringToDate(endTime.trim());
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(endD);
        calendar.add(calendar.DATE, 1);
        endD=calendar.getTime();
        List<String> inspectionTimeList = new ArrayList<>();
        if (1 == type) {
            inspectionTimeList = getTimeBetweenTwoDate(startD, endD, timepoints);
        } else if (2 == type) {
            inspectionTimeList = getWeekBetweenTwoDate(startD, endD);
        } else if (3 == type) {
            inspectionTimeList = getMonthBetweenTwoDate(startD, endD);
        } else if (4 == type) {
            inspectionTimeList = getSeasonBetweenTwoDate(startD, endD);
        } else if (5 == type) {
            inspectionTimeList = getYearsBetweenTwoDate(startD, endD);
        }

        List<Map<String, Object>> taskMapList = new ArrayList<>();

        for (int i = 0; i < inspectionTimeList.size(); i++) {
            Map<String, Object> ma = new HashMap<>();
            ma.put("id", UUID.randomUUID().toString());
            ma.put("companyId", map.get("companyId"));
            ma.put("pcCompanyId", map.get("pcCompanyId"));
            ma.put("departmentId", map.get("departmentId"));
            ma.put("inspectionPlanId", planId);
            ma.put("templateId", map.get("templateId"));
            ma.put("period", map.get("period"));
            ma.put("planCompletionDate", inspectionTimeList.get(i));
            ma.put("createDate", new Date());
            ma.put("createdBy", loginUserId);
            ma.put("taskNo",((String)map.get("planNo")) +generateTaskSequence(i+1));
            taskMapList.add(ma);
        }

        List<Map<String, Object>> batchList = new ArrayList<>();
        for (int i = 0; i < taskMapList.size(); i++) {
            batchList.add(taskMapList.get(i));
            if (batchList.size() == 200) {
                inspectionTaskMapper.insertList(batchList);
                batchList.clear();
            }

        }
        //插入尾数批
        if (batchList.size() > 0) {
            inspectionTaskMapper.insertList(batchList);
        }

    }




    public   List<Map<String,Object>> searchInspectionPlan(String pcCompanyId,Integer period,String companyId,String departmentId,Integer currentPage,Integer pageSize){
        Integer start=0;
        if (currentPage==null){
            currentPage=1;
        }
        if (pageSize==null){
            pageSize=10;
        }
        start=(currentPage-1)*pageSize;
        List<Map<String,Object>> inspectionList= inspectionMapper.searchInspectionPlan(pcCompanyId,period,companyId,departmentId,start,pageSize);


        List<String> inspectionIdList=new ArrayList<>();
        for (int i = 0; i <inspectionList.size() ; i++) {
            inspectionIdList.add((String) inspectionList.get(i).get("id"));
        }



        List<Map<String, Object>> deviceGroupList = new ArrayList<>();
        if (inspectionIdList.size() > 0) {
            deviceGroupList = inspectionTaskMapper.selectDeviceGroupByInspectitonIds(inspectionIdList);
        }
        for (int i = 0; i < inspectionList.size(); i++) {
            for (int j = 0; j < deviceGroupList.size(); j++) {
                Map<String, Object> inspectionMap = inspectionList.get(i);
                if (inspectionMap.get("id").equals(deviceGroupList.get(j).get("inspectionPlanId"))) {
                    if (StringUtils.isBlank((String) inspectionMap.get("deviceGroup"))) {
                        inspectionMap.put("deviceGroup", deviceGroupList.get(j).get("groupName"));
                    } else {
                        inspectionMap.put("deviceGroup", inspectionMap.get("deviceGroup") + " " + deviceGroupList.get(j).get("groupName"));

                    }
                }
            }
        }


        if (inspectionIdList.size()>0){
        List<Map<String,Object>> nextTaskList=inspectionTaskMapper.selectNextPlanTaskByInspectionPlanIds(inspectionIdList)    ;
            for (int i = 0; i <nextTaskList.size() ; i++) {
                Map<String,Object> task=nextTaskList.get(i);
                for (int j = 0; j <inspectionList.size() ; j++) {
                    Map<String,Object> inspection=inspectionList.get(j);
                    if (inspection.get("id").equals(task.get("inspectionPlanId"))){
                        inspection.put("nextPlanCompletionDate",task.get("nextPlanCompletionDate"));
                        inspection.put("exceedLimit",task.get("exceedLimit"));
                        inspection.put("exceedLimitFlag",task.get("exceedLimitFlag"));

                    }
                }
            }
        }
        for (int i = 0; i <inspectionList.size() ; i++) {
            if (null==inspectionList.get(i).get("exceedLimitFlag")){
                inspectionList.get(i).put("exceedLimit", "否");
                inspectionList.get(i).put("exceedLimitFlag", 0);
            }
        }
        return inspectionList;
    }

    public  int searchInspectionPlanCount(String pcCompanyId,Integer period,String companyId,String departmentId){
        return inspectionMapper.searchInspectionPlanCount(pcCompanyId, period, companyId, departmentId);
    }



    public void deleteInspectionPlan(String id,String loginUserId){
        Map<String,Object> map=new HashMap<>();
        map.put("id",id);
        map.put("status",2);
        map.put("updateDate",new Date());
        map.put("updatedBy",loginUserId);
        inspectionMapper.updateInspectionPlan(map);

        inspectionTaskMapper.deleteUnCompletedTaskByPlanId(id);

    }

    public Map<String, Object> selectInspectionPlanByGroupId(String groupId){
        return inspectionMapper.selectInspectionPlanByGroupId(groupId);
    }

    public Map<String, Object> selectLastInspectionTask(String inspectionId){
        return inspectionTaskMapper.selectLastInspectionTask(inspectionId);
    }

    public Integer inspectionCount(String groupId){
        return inspectionMapper.inspectionCount(groupId);
    }

    public List<Map<String, Object>> selectAllInspectionPlanByGroupId (String groupId){
        return inspectionMapper.selectAllInspectionPlanByGroupId(groupId);
    }

    public   Map<String,Object> selectInspectionById(  java.lang.String id){
        return inspectionMapper.selectInspectionById(id);
    }


}
