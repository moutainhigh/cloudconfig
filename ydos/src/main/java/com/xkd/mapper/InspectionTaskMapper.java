package com.xkd.mapper;

import com.xkd.service.ObjectNewsService;
import org.apache.ibatis.annotations.Param;
import sun.nio.cs.ext.IBM037;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/2/24.
 */
public interface InspectionTaskMapper {

    public int insertList(@Param("tasks") List<Map<String,Object>> tasks);

    public int updateInspectionTask(Map<String,Object> map);

    public List<Map<String,Object>> selectHistoryTaskByPlanId(@Param("inspectionPlanId")String inspectionPlanId,@Param("fromDate")String fromDate,@Param("toDate")String toDate,@Param("isExceedTime") Integer isExceedTime,@Param("completedBy")String completedBy,@Param("start") Integer start,@Param("pageSize")Integer pageSize);

    public int selectHistoryTaskCountByPlanId(@Param("inspectionPlanId")String inspectionPlanId,@Param("fromDate")String fromDate,@Param("toDate")String toDate,@Param("isExceedTime") Integer isExceedTime,@Param("completedBy")String completedBy);


    public List<Map<String,Object>> searchInspectionTask(@Param("pcCompanyId")String pcCompanyId,@Param("period")Integer period,@Param("companyId")String companyId,@Param("departmentId")String departmentId,@Param("fromDate")String fromDate,@Param("toDate")String toDate,@Param("isDone")Integer isDone,@Param("start")Integer start,@Param("pageSize")Integer pageSize);
    public  int searchInspectionTaskCount(@Param("pcCompanyId")String pcCompanyId,@Param("period")Integer period,@Param("companyId")String companyId,@Param("departmentId")String departmentId,@Param("fromDate")String fromDate,@Param("toDate")String toDate,@Param("isDone")Integer isDone);

    public List<Map<String,Object>> selectDeviceGroupByInspectitonIds(@Param("inspectionPlanIdList")List<String> inspectionIdList);

    public List<Map<String,Object>> searchTechnicalInspectionTask(@Param("departmentId")String departmentId,@Param("period")Integer period,@Param("companyId")String companyId,@Param("fromDate")String fromDate,@Param("toDate")String toDate,@Param("isDone")Integer isDone,@Param("start") Integer start,@Param("pageSize")Integer pageSize);

    public int searchTechnicalInspectionTaskCount(@Param("departmentId")String departmentId,@Param("period")Integer period,@Param("companyId")String companyId,@Param("fromDate")String fromDate,@Param("toDate")String toDate,@Param("isDone")Integer isDone);


    public List<Map<String,Object>> searchCustomerInspectionTask(@Param("dateFrom")String dateFrom,@Param("dateTo")String dateTo,@Param("companyIdList")List<String> companyIdList,@Param("start")Integer start,@Param("pageSize")Integer pageSize);
    public Integer searchCustomerInspectionTaskCount(@Param("dateFrom")String dateFrom,@Param("dateTo")String dateTo,@Param("companyIdList")List<String> companyIdList);

    public  Map<String,Object>  selectTaskDetail(@Param("id")String id);

    List<Map<String,Object>> selectInspectionTaskNote(@Param("inspectionPlanId")String inspectionPlanId,@Param("completionStatus")Integer completionStatus);


    int insertTaskNote(Map<String,Object> map);

    int completeTaskNote(Map<String,Object> map);


    List<Map<String,Object>> selectInspectionTaskByGroupId(@Param("groupId")String  groupId,@Param("pcCompanyId")String pcCompanyId,@Param("start")Integer start,@Param("pageSize")Integer pageSize);

    int selectInspectionTaskCountByGroupId(@Param("groupId")String groupId,@Param("pcCompanyId")String pcCompanyId);


    int deleteUnCompletedTaskByPlanId(@Param("inspectionPlanId")String inspectionPlanId);


    List<Map<String,Object>>  selectNextPlanTaskByInspectionPlanIds(@Param("idList")List<String> idList);


    List<Map<String,Object>>  selectInspectionTaskByInspectionId(@Param("inspectionPlanId") String inspectionPlanId,@Param("fromTime") String fromTime,@Param("toTime") String toTime );

    Map<String, Object> selectLastInspectionTask(@Param("inspectionId") String inspectionId);

    List<Map<String, Object>> selectFinishedInspectionTaskByInspectionPlan(@Param("inspectionPlanList") List inspectionPlanId,
                                                                           @Param("startDate") String startDate,
                                                                           @Param("endDate") String endDate,
                                                                           @Param("start") Integer start,
                                                                           @Param("pageSize") Integer pageSize);

    Integer countFinishedInspectionTaskByInspectionPlan(@Param("inspectionPlanList") List inspectionPlanId,
                                                        @Param("startDate") String startDate,
                                                        @Param("endDate") String endDate);


    Integer countInspectionTaskByPcCompanyIdAndDate(@Param("pcCompanyId") String pcCompanyId, @Param("status") Integer status);

    List<Map<String, Object>> selectInspectionByUserAndDate(@Param("userId") String userId, @Param("type") Integer type, @Param("date") String date);


    List<Map<String,Object>> selectToRemindInspectionTechinican();

    void updateRemindStatusByIds(@Param("idList")List<String> idList);

    public List<Map<String,Object>> selectHistoryInspectionTaskByGroupId(@Param("groupId")String groupId,@Param("fromDate")String fromDate,@Param("toDate")String toDate,@Param("start")Integer start,@Param("pageSize")Integer pageSize);

    public Integer selectHistoryInspectionTaskCountByGroupId(@Param("groupId")String groupId,@Param("fromDate")String fromDate,@Param("toDate")String toDate);


    public List<Map<String,Object>> selectHistoryInspectionAndRepaireByUserId(@Param("pcCompanyId")String pcCompanyId,@Param("userId")String userId,@Param("fromDate")String fromDate,@Param("toDate")String toDate,@Param("ttype")Integer ttype,@Param("start")Integer start,@Param("pageSize")Integer pageSize);

    public  Integer selectHistoryInspectionAndRepaireCountByUserId(@Param("pcCompanyId")String pcCompanyId,@Param("userId")String userId,@Param("fromDate")String fromDate,@Param("toDate")String toDate,@Param("ttype")Integer ttype);

    public List<Map<String, Object>> selectCurrentInspection(@Param("deviceId") String deviceId);

    Integer countUserInspection(@Param("pcCompanyId") String pcCompanyId, @Param("userId") String userId);


}
