package com.xkd.mapper;

import com.xkd.model.BoxAndDeviceNum;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/2/12.
 */
public interface DeviceMapper {

    public int insertDevice(Map map);

    public int updateDevice(Map map);

//    public List<Map<String, Object>> selectDeviceByGroupIds(@Param("groupIdList") List<String> groupIdList, @Param("pcCompanyId") String pcCompanyId);


    public List<Map<String,Object>> selectDeviceByGroupIds(@Param("groupIdList")List<String> groupIdList,@Param("pcCompanyIdList")List<String> pcCompanyIdList,@Param("companyIdList")List<String> companyIdList,@Param("start")Integer start,@Param("pageSize")Integer pageSize);

    public Integer selectDeviceCountByGroupIds(@Param("groupIdList")List<String> groupIdList,@Param("pcCompanyIdList")List<String> pcCompanyIdList,@Param("companyIdList")List<String> companyIdList);

    public Integer selectTotalDeviceByCompanyId(@Param("companyId") String companyId);

    Integer countByPcCompanyId (@Param("pcCompanyId") String pcCompanyId);

    Integer countByCompanyIdAndBoxId(@Param("companyId") String companyId);

    Integer countByCompanyId(@Param("companyId") String companyId);

    List<Map<String, Object>> getDeviceByCompanyId(@Param("companyIdList") List<String> companyIdList);

    List<Map<String, Object>> groupByCompanyId(List<String> group);
    List<String> selectCompanyUserByDeviceId(@Param("deviceId")String deviceId);


    //当status等于null时,查询所有设备,不等于null时查询带云盒的设备
    BoxAndDeviceNum countDeviceByCompanyIdAndGroupId(@Param("companyId") String companyId, @Param("groupId") String groupId);

    List<Map<String, Object>> filterDeviceForListDevice(@Param("pcCompanyId") String pcCompanyId,
                                                        @Param("companyIdList") List<String> companyIdList,
                                                        @Param("groupIdList") List<String> groupIdList,
                                                        @Param("boxIdList") List<String> boxIdList,
                                                        @Param("deviceName") String deviceName,
                                                        @Param("deviceType") String deviceType,
                                                        @Param("start") Integer start,
                                                        @Param("pageSize") Integer pageSize);
    Integer countDeviceForListDevice(@Param("pcCompanyId") String pcCompanyId,
                                     @Param("companyIdList") List<String> companyIdList,
                                     @Param("groupIdList") List<String> groupIdList,
                                     @Param("boxIdList") List<String> boxIdList,
                                     @Param("deviceName") String deviceName,
                                     @Param("deviceType") String deviceType);

    List<Map<String, Object>> filterDeviceForMonitor(@Param("companyId") String companyId, @Param("status") Integer status);

    public List<Map<String,Object>> selectDeviceByIds(@Param("idList")List<String>  idList);

    List<String> selectRoleUserByDeviceId(@Param("deviceId")String deviceId, @Param("roleId")String roleId);

    Map<String, Object> selectDeviceByDeviceId(@Param("deviceId") String deviceId);

    Integer getCountDownTime(@Param("inspectionId") String inspectionId);

    Map<String, String> getUserAndParentId(String groupId);

    Integer checkIfDeviceInRepair(@Param("deviceId") String deviceId);

    List<Map<String, Object>> selectFinishedRepairListByDeviceId(@Param("deviceId") String deviceId,
                                                                 @Param("startDate") String startDate,
                                                                 @Param("endDate") String endDate,
                                                                 @Param("start") Integer start,
                                                                 @Param("pageSize") Integer pageSize);

    Integer countFinishedRepairListByDeviceId(@Param("deviceId") String deviceId,
                                              @Param("startDate") String startDate,
                                              @Param("endDate") String endDate);

    List<Map<String, Object>> selectDeviceByCompanyIdAndGroupId(@Param("companyId") String companyId, @Param("groupId") String groupId);

    List<Map<String, Object>> selectDeviceByCompanyIdList(@Param("companyIdList") List<String> companyIdList,
                                                          @Param("deviceType") String deviceType,
                                                          @Param("deviceName") String deviceName,
                                                          @Param("start") Integer start,
                                                          @Param("pageSize") Integer pageSize);
    Integer countDeviceByCompanyIdList(@Param("companyIdList") List<String> companyIdList,
                                       @Param("deviceType") String deviceType,
                                       @Param("deviceName") String deviceName);

    Map<String, Integer> countDeviceForMonitor();

    List<Map<String, Object>> selectWarningDeviceByUserId(@Param("userId") String userId, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    Integer countWarningDeviceByUserId(@Param("userId") String userId);

    List<Map<String, Object>> getDeviceListByPcCompanyId(@Param("pcCompanyId") String pcCompanyId);

    List<Map<String, String>> getDeviceId();


    void  deleteDeviceByIds(@Param("idList")List<String> idList);

    Map<String,Object> selectDeviceByBoxIdUnDeleted(@Param("boxId")String boxId);
}
