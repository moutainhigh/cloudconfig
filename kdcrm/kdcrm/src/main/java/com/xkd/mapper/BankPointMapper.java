package com.xkd.mapper;

import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BankPointMapper {

    Integer saveBankPoint(Map<String, Object> map);

    Integer updateBankPointById(Map<String, Object> map);

    List<Map<String,Object>> selectBankPointsByContent(@Param("content") String content,
                                                       @Param("pageSize")int pageSizeInt, @Param("start")int currentPageInt,
                                                       @Param("departmentIdList")List<String> departmentIdList,@Param("companyId")String companyId);

    Integer deleteBankPointByIds(@Param("idList")List<String> idList);

    Integer updateBankPointStatusByIds(@Param("idList") List<String> idList);

    Integer deleteProjectPointByPointIds(@Param("pointIdList")List<String> pointIdList);


    Integer  updateBankPointProjectRevokeStatusByPointIds(@Param("pointIdList") List<String> pointIdList);

    Map<String,Object> selectBankPointByName(@Param("pointName")String pointName,@Param("departmentIdList") List departmentIdList);

    Integer selectBankPointsTotalByContent(@Param("content")String content,@Param("departmentIdList")List<String> departmentIdList,
                                           @Param("companyId")String companyId);

    Integer deleteProjectPointByProjectIds(@Param("projectIdList")List<String> projectIdList);


    Integer updateProjectPointRevokeStatusByProjectIds(@Param("projectIdList") List<String> projectIdList);
}
