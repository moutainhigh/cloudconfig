package com.xkd.mapper;

import com.xkd.model.YDrepaire;
import com.xkd.service.ObjectNewsService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface YDrepaireMapper {

    Integer insert(Map<String,Object> map);

    Map<String,Object> selectById(@Param("id") String id);

    Integer updateById(Map<String,Object> map);

    List<Map<String,Object>> selectRepaireApplys(Map<String, Object> paramMap);

    Integer selectTotalRepaireApplys(Map<String, Object> paramMap);

    Map<String,Object> selectRepaireApplyById(String id);

    Integer insertTechnicianList(@Param("technicianList")List<Map<String,Object>> technicianList);

    List<Map<String,Object>> selectRepaires(Map<String, Object> paramMap);

    Integer selectTotalSelectRepaires(Map<String, Object> paramMap);

    List<Map<String,Object>> selectRepaireByCompanyId(@Param("companyId")String companyId);

    Integer selectRepaireCountByCompanyId(@Param("companyId")String companyId);

    Map<String,Object> selectRepaireDetailById(@Param("id")String id);

    List<Map<String,Object>> selectCurrentRepaireByGroupId(@Param("groupIdList")List<String> groupIdList,@Param("pcCompanyId")String pcCompanyId);


    List<Map<String,Object>> selectTechniciansByRepaireId(@Param("repaireId")String repaireId);

    Map<String,Object> selectRepaireStatusById(@Param("repaireId")String repaireId);

    List<String> selectDeviceIdListByRepaireId(@Param("repaireId")String repaireId);

    List<String> selectCompanyUserByRepaireId(@Param("repaireId")String repaireId);

    List<Map<String,Object>> selectOutOfDateRepaire();

    List<String> getUserIdsHasOutOfDateNews(@Param("repaireId")String repaireId);

    Integer deleteRepaireByIds(@Param("repaireIds")List<String> repaireIds);

    Integer countRepairApplyByStatusAndPcCompanyId(@Param("pcCompanyId") String pcCompanyId);

    Integer countRepairByStatusAndPcCompanyId(@Param("pcCompanyId") String pcCompanyId);

    Integer countRepairByStatusAndPcCompanyIdAndDate(@Param("pcCompanyId") String pcCompanyId);

    List<Map<String, Object>> selectRepairByUserAndDate(@Param("userId") String userId, @Param("type") Integer type, @Param("date") String date);

    List<Map<String,Object>> selectHistoryRepaireByGroupId(@Param("groupId")String groupId,@Param("fromDate")String startDate,@Param("toDate")String toDate,@Param("start")Integer start,@Param("pageSize")Integer pageSize);

   Integer selectHistoryRepaireCountByGroupId(@Param("groupId")String groupId,@Param("fromDate")String startDate,@Param("toDate")String toDate);

    Integer saveRepaireUserContent(@Param("paramMap") Map<String, Object> paramMap);

    List<Map<String, Object>> selectCurrentRepairOrder(@Param("deviceId") String deviceId);

    Integer countUserRepair(@Param("pcCompanyId") String pcCompanyId, @Param("userId") String userId);

    Integer countCompanyRepair(@Param("userId") String userId);

}