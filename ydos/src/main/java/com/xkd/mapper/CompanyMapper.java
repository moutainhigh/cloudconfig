package com.xkd.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.xkd.model.Company;
import org.springframework.stereotype.Repository;

public interface CompanyMapper {

    List<Company> selectCompanyByName(@Param("companyName") String companyName, @Param("pcCompanyId") String pcCompanyId);

    List<Map<String, Object>> selectCompanyByIds(@Param("idList") List<String> idList);

    int updateCompanyInfoById(Map<String, Object> company);

    int deleteCompanyById(@Param("idList") List<String> idList);

    Company selectCompanyInfoById(@Param("companyId") String companyId);

    List<Map<String, Object>> searchCompanyByName(@Param("companyName") String companyName, @Param("pcCompanyId") String pcCompanyId,@Param("userLevel")String userLevel,@Param("fromDate")String fromDate,@Param("toDate")String toDate, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    int searchCompanyCountByName(@Param("companyName") String companyName, @Param("pcCompanyId") String pcCompanyId,@Param("userLevel")String userLevel,@Param("fromDate")String fromDate,@Param("toDate")String toDate);

    List<Company> selectCompanyByNameIncludingDeleted(@Param("companyName") String companyName, @Param("pcCompanyId") String pcCompanyId);

    Integer insertCompanyInfo(Map<String, Object> company);

    void deleteByCompanyById(@Param("id") String id);

    List<String> selecAllCompanyId();

    //修改企业文件夹名称
    void updatePagerFileName(Map<String, Object> company);

    List<Map<String, Object>> findCompanyByPcCompanyId(@Param("pcCompanyId") String pcCompanyId, @Param("userLevel") String userLevel, @Param("companyName") String companyName);


    List<Map<String,Object>>  selectCompanyIdByTechinicanId(@Param("responsibleUserIdList") List<String> responsibleUserId,@Param("pcCompanyId")String pcCompanyId);

    List<Map<String,Object>>  selectPcCompanyCustomer(@Param("idList")List<String> idList,@Param("start")Integer start,@Param("pageSize") Integer pageSize );

    int  selectPcCompanyCustomerCount(@Param("idList")List<String> idList);

    List<Map<String, Object>> selectAllCompany(@Param("start") Integer start, @Param("pageSize") Integer pageSize);

    Integer countAllCompany();

    List<Map<String,Object>> selectInspectionCompanyIdByDepartmentId(@Param("departmentIdList")List<String> departmentIdList,@Param("pcCompanyId")String pcCompanyId);

 }
