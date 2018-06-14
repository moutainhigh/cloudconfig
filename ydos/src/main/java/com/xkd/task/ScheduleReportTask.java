package com.xkd.task;

import com.xkd.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Create by @author: wanghuijiu; @date: 18-3-7;
 */

@Component
public class ScheduleReportTask {

    @Autowired
    private UserService userService;

    @Autowired
    private DcYdReportService dcYdReportService;

    @Autowired
    private InspectionTaskService inspectionTaskService;

    @Autowired
    private DcYdReportSheetService dcYdReportSheetService;

    @Autowired
    private YDrepaireService yDrepaireService;

    //自动生成日报(日期, 时间, 用户)
    //1:先筛选出来所有的技师用户
    //2:用技师用户Id遍历report表,

    @Scheduled(cron = "0 0 3 * * ?")
    public void  reportDaily(){
        for (Map<String, String> userInfo: userService.selectAllRoleId3()
             ) {
            try {
                System.out.println("Add Daily Report For" + userInfo.get("id"));
                dcYdReportService.insertForSchedule(UUID.randomUUID().toString(), userInfo.get("id"), userInfo.get("pcCompanyId"),1);
                Thread.sleep(20);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Add Daily Report For" + userInfo.get("id") + "Error");
            }
        }
    }

    //自动生成周报
    @Scheduled(cron = "0 0 2 ? * MON")
    public void  reportWeekly(){
        for (Map<String, String> userInfo: userService.selectAllRoleId3()
                ) {
            try {
                System.out.println("Add Weekly Report For" + userInfo.get("id"));
                dcYdReportService.insertForSchedule(UUID.randomUUID().toString(), userInfo.get("id"), userInfo.get("pcCompanyId"),2);
                Thread.sleep(20);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Add Weekly Report For" + userInfo.get("id") + "Error");
            }

        }
    }

    //自动生成月报
    @Scheduled(cron = "0 0 1 1 * ?")
    public void  reportMonthly(){
        for (Map<String, String> userInfo: userService.selectAllRoleId3()
                ) {
            try {
                dcYdReportService.insertForSchedule(UUID.randomUUID().toString(), userInfo.get("id"), userInfo.get("pcCompanyId"),3);
                System.out.println("Add Monthly Report For" + userInfo.get("id"));
                Thread.sleep(20);
            }catch (Exception e){
                System.out.println("Add Monthly Report For" + userInfo.get("id") + "Error");
                e.printStackTrace();
            }
        }
    }

    //自动为报表生成reportNoteList
    @Scheduled(cron = "0 0 1 * * ?")
    public void addReportNoteDaily(){
        //选出当天所有日报
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DATE, cal.get(Calendar.DATE)-1);
        SimpleDateFormat format = new SimpleDateFormat();
        List<Map<String, String>> dailyReportList = dcYdReportService.selectAllLastDayReport(1);
        List<String> reportIdList = dailyReportList.stream().
                map((Map<String, String> map)->map.getOrDefault("id", ""))
                .collect(Collectors.toList());
        dcYdReportService.updateReportStatus(reportIdList);
        for (Map<String, String> reportMap: dailyReportList){
            String userId = reportMap.get("createdBy");
            String reportId = reportMap.get("id");
            try {
                dcYdReportSheetService.deleteReportNoteById(reportId);
                //通过日报的userId，筛选到维修和巡检数据

                /**
                 * 巡检部分
                 */

                List<Map<String, Object>> inspectionMapList = inspectionTaskService.selectInspectionByUserAndDate
                        (userId, 1, format.format(cal.getTime()));
                addInspection(inspectionMapList, reportId);

                /**
                 * 维修部分
                 */

                List<Map<String, Object>> repairList = yDrepaireService.selectRepairByUserAndDate
                        (userId, 1,format.format(cal.getTime()));
                addRepair(repairList, reportId);
                Thread.sleep(20);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("日报添加巡检维修关联失败"+ userId);
            }
        }
    }

    @Scheduled(cron = "0 0 2 ? * MON")
    public void addReportNoteWeekly(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR)-1);
        SimpleDateFormat format = new SimpleDateFormat();
        List<Map<String, String>> AllLastWeekReport = dcYdReportService.selectAllLastDayReport(2);
        List<String> reportIdList = AllLastWeekReport.stream().
                map((Map<String, String> map)->map.getOrDefault("id", ""))
                .collect(Collectors.toList());
        dcYdReportService.updateReportStatus(reportIdList);
        for (Map<String, String> report:AllLastWeekReport){
            String userId =  report.get("createdBy");
            String reportId =  report.get("id");
            try{
                dcYdReportSheetService.deleteReportNoteById(reportId);
                //通过周报的userId，筛选维修巡检数据
                List<Map<String, Object>> inspectionMapList = inspectionTaskService.selectInspectionByUserAndDate(
                        userId, 2, format.format(cal.getTime())
                );
                //添加巡检关联；
                addInspection(inspectionMapList, reportId);
                List<Map<String, Object>> repairList = inspectionTaskService.selectInspectionByUserAndDate(
                        userId, 2, format.format(cal.getTime()));

                addRepair(repairList, reportId);
                Thread.sleep(20);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("周报添加巡检维修关联失败"+userId);
            }

        }
    }

    @Scheduled(cron = "0 0 1 1 * ?")
    public void addReportNoteMonthly(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-1);
        SimpleDateFormat format = new SimpleDateFormat();
        List<Map<String, String>> AllLastMonthReport = dcYdReportService.selectAllLastDayReport(3);
        List<String> reportIdList = AllLastMonthReport.stream().
                map((Map<String, String> map)->map.getOrDefault("id", ""))
                .collect(Collectors.toList());
        dcYdReportService.updateReportStatus(reportIdList);
        for (Map<String, String> report:AllLastMonthReport){
            String userId =  report.get("createdBy");
            String reportId =  report.get("id");
            try {
                dcYdReportSheetService.deleteReportNoteById(reportId);
                List<Map<String, Object>> inspectionMapList = inspectionTaskService.selectInspectionByUserAndDate(
                        userId, 3, format.format(cal.getTime()));
                addInspection(inspectionMapList, reportId);

                List<Map<String, Object>> repairList = yDrepaireService.selectRepairByUserAndDate(
                        userId, 3, format.format(cal.getTime())
                );
                addRepair(repairList, reportId);
                Thread.sleep(20);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("月报添加巡检维修关联失败");
            }

        }
    }

    private void addInspection(List<Map<String,Object>> inspectionMapList, String reportId){
        for (Map<String, Object> inspectionMap: inspectionMapList){
            try {
                Map<String, Object> reportNote = new HashMap<String, Object>();
                reportNote.put("id", UUID.randomUUID().toString());
                reportNote.put("reportId", reportId);
                reportNote.put("objectId", inspectionMap.get("objectId"));
                reportNote.put("flag", 2);
                reportNote.put("completeFlag", 1);
                dcYdReportSheetService.addReportNote(reportNote);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("添加报表巡检关联的时候，捕捉到错误 inspection: "+inspectionMap.get("id") + "reportId: "+ reportId);
            }

        }
    }

    private void addRepair(List<Map<String, Object>> repairList, String reportId){
        for (Map<String, Object> repair: repairList){
            try {
                Map<String, Object> reportNote = new HashMap<String, Object>();
                reportNote.put("id", UUID.randomUUID().toString());
                reportNote.put("reportId", reportId);
                reportNote.put("objectId", repair.get("objectId"));
                reportNote.put("flag", 1);
                reportNote.put("completeFlag", repair.get("completeFlag"));
                dcYdReportSheetService.addReportNote(reportNote);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("添加报表维修关联的时候，捕捉到错误 inspection: "+ repair.get("id") + "reportId: "+ reportId);
            }
        }
    }

}
