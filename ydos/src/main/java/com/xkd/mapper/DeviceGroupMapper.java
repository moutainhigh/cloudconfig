package com.xkd.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/2/12.
 */
public interface DeviceGroupMapper {
    public int insertDeviceGroup(Map<String, Object> map);

    public int updateDeviceGroup(Map<String, Object> map);

    public List<Map<String, Object>> selectDeviceGroupByCompanyIds(@Param("companyIdList") List<String> list, @Param("flag") Integer flag);

    public List<Map<String, Object>> selectDeviceGroupByPcCompanyIds(@Param("pcCompanyIdList") List<String> list);

    public List<Map<String, Object>> selectDeviceGroupByPcCompanyIds(@Param("pcCompanyIdList") List<String> list, @Param("flag") Integer flag);

    public List<String> selectFirstLevelDeviceGroupIdsByPcCompanyIds(@Param("pcCompanyIdList") List<String> list);

    public Map<String, Object> selectDeviceGroupById(@Param("id") String id);

    public List<String> searchCompanyIdByPcCompanyId(@Param("pcCompanyId") String pcCompanyId, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    public Integer searchCompanyIdCountByPcCompanyId(@Param("pcCompanyId") String pcCompanyId);

    public List<String> searchCompanyIdByDevice(@Param("searchValue") String searchValue, @Param("pcCompanyId") String pcCompanyId, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    public Integer searchCompanyIdCountByDevice(@Param("searchValue") String searchValue, @Param("pcCompanyId") String pcCompanyId);

    public List<String> searchPcCompanyIdByDevice(@Param("searchValue") String searchValue, @Param("companyIdList") List<String> companyIdList, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    public Integer searchPcCompanyIdCountByDevice(@Param("searchValue") String searchValue, @Param("companyIdList") List<String> companyIdList);

    public List<Map<String, Object>> selectAllBuildingsByCompanyId(@Param("pcCompanyId") String pcCompanyId, @Param("companyId") String companyId);

    public List<Map<String, Object>> selectGroupIdAndCompanyIdByUserId(@Param("userId") String userId, @Param("userLevel") String userLevel, @Param("companyName") String companyName, @Param("pcCompanyId") String pcCompanyId);

    public List<Map<String, Object>> selectDeviceGroupByDepartmentId(@Param("departmentId") String departmentId, @Param("pcCompanyId") String pcCompanyId, @Param("flag") Integer flag);

    public List<Map<String, Object>> selectDeviceGroupByResponsibleUser(@Param("responsibleUserId") String responsibleUserId, @Param("pcCompanyId") String pcCompanyId, @Param("flag") Integer flag);

    public List<String> selectDeviceGroupIdsByParentIds(@Param("parentIdList") List<String> parentIdList);

    public List<String> selectParentGroupIdsByGroupIds(@Param("idList") List<String> idList);

    public List<Map<String, Object>> selectDeviceGroupByIds(@Param("idList") List<String> idList, @Param("flag") Integer flag);

    public List<String> getGroupIdByUserId(@Param("userId") String userId);



}
