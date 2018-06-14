package com.xkd.mapper;

import com.xkd.service.LabelService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/1/9.
 */
public interface CustomerMapper {
    public int insertCustomerMenuList(@Param("list")List<Map<String,Object>> list);
    public int deleteCustomerMenuByDepartmentId(@Param("departmentId")String  departmentId);
    public List<Map<String,Object>> searchPcCompany(@Param("startDate")String startDate,@Param("endDate") String endDate,@Param("departmentName")String departmentName,@Param("province")String province,@Param("city")String city,@Param("status")Integer status,@Param("start")Integer start,@Param("pageSize")Integer pageSize);
    public int  searchPcCompanyCount(@Param("startDate")String startDate,@Param("endDate") String endDate,@Param("departmentName")String departmentName,@Param("province")String province,@Param("city")String city,@Param("status")Integer status);
    public int insertPcCompany(Map<String,Object> map);
    public int updatePcCompany(Map<String,Object> map);
    public List<Map<String,Object>> selectPcAdminUserByPcCompanyId(@Param("pcCompanyId")String pcCompanyId);
    public List<Map<String,Object>> selectPcAdminRole(@Param("pcCompanyId")String  pcCompanyId);

    public int insertAdminRole(Map<String,Object> map);


    public List<String> selectOperateIdsByPcCompanyId(@Param("pcCompanyId")String pcCompanyId);

    public Map<String,Object> selectPcCompanyById(@Param("id")String id);


    public List<String> selectCustomerMenuByPcCompanyId(@Param("pcCompanyId")String pcCompanyId);


}
