package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Create by @author: wanghuijiu; @date: 18-2-28;
 */
public interface DcYdReportSheetMapper {

    Integer insert(Map<String, Object> reportNote);

    Integer deleteById(String id);

    Integer deleteByReportId(String reportId);

    Integer update(@Param(value = "completeFlag") Integer completeFlag, @Param(value = "id") String id);

    List<Map<String, Object>> list(@Param(value = "reportId") String reportId);

    String getRepairCompanyName(@Param(value = "objectId") String objectId);

    String getInspectionCompanyName(@Param(value = "objectId") String objectId);

}
