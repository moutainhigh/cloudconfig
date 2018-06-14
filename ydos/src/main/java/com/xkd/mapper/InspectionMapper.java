package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/2/12.
 */
public interface InspectionMapper {

    int insertInspectionPlan(Map<String, Object> map);

    int updateInspectionPlan(Map<String, Object> map);

    List<Map<String, Object>> selectInspectionPlanByCompanyId(@Param("companyId") String companyId);

    Integer selectInspectionPlanCountByCompanyId(@Param("companyId") String companyId);

    int insertInspectionPlanGroup(Map<String, Object> map);


    List<Map<String, Object>> searchInspectionPlan(@Param("pcCompanyId") String pcCompanyId, @Param("period") Integer period, @Param("companyId") String companyId, @Param("departmentId") String departmentId, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    int searchInspectionPlanCount(@Param("pcCompanyId") String pcCompanyId, @Param("period") Integer period, @Param("companyId") String companyId, @Param("departmentId") String departmentId);

    Map<String, Object> selectInspectionPlanByGroupId(String groupId);

    Integer inspectionCount(@Param("groupId") String groupId);

    List<Map<String, Object>> selectAllInspectionPlanByGroupId(@Param("groupId") String groupId);

    Map<String,Object> selectInspectionById(@Param("id")String id);

}
