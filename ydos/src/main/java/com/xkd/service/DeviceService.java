package com.xkd.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sun.xml.ws.server.ServerRtException;
import com.xkd.exception.BoxIDAlreadyExistException;
import com.xkd.mapper.DeviceMapper;
import com.xkd.mapper.MsgDeviceCurrMapper;
import com.xkd.model.BoxAndDeviceNum;
import com.xkd.model.ResponseDbCenter;
import com.xkd.utils.HttpRequestUtil;
import com.xkd.utils.PropertiesUtil;
import com.xkd.utils.RedisCacheUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dell on 2018/2/12.
 */
@Service
public class DeviceService {

    @Autowired
    DeviceMapper deviceMapper;

    @Autowired
    DeviceGroupService deviceGroupService;

    @Autowired
    ApiCallFacadeService apiCallFacadeService;

    @Autowired
    RedisCacheUtil<Map<String, String>> redisCacheUtil;


    public int selectTotalDeviceByCompanyId(String companyId) {
        return deviceMapper.selectTotalDeviceByCompanyId(companyId);
    }


    public int insertDevice(Map map) {
        return deviceMapper.insertDevice(map);
    }

    public int updateDevice(Map map)   {

       return    deviceMapper.updateDevice(map);
     }


    public void deleteDevice(String id)   {

        Map<String,Object> device=selectDeviceByDeviceId(id);
        String boxId=(String) device.get("boxId");
        if (StringUtils.isNotBlank(boxId)){
            apiCallFacadeService.deleteDevice(boxId);
            //将盒子属于哪一个服务商的信息删除掉
            redisCacheUtil.deleteCacheMap("boxId_pcCompany",boxId);
            redisCacheUtil.deleteCacheMap("offline_"+device.get("pcCompanyId"),boxId);
            redisCacheUtil.deleteCacheMap("exception_"+device.get("pcCompanyId"),boxId);
        }

        Map<String,Object> mm=new HashMap<>();
        mm.put("id",id);
        mm.put("status",2);
        deviceMapper.updateDevice(mm);


    }


//    public List<Map<String, Object>> selectDeviceByGroupIds(List<String> groupIdList, String pcCompanyId) {
//        return deviceMapper.selectDeviceByGroupIds(groupIdList, pcCompanyId);
//    }

    /**
     * 根据设备组查询Id查询设备列表,不包含子设备
     * @param groupIdList
     * @param pcCompanyIdList
     * @param companyIdList
     * @param currentPage
     * @param pageSize
     * @return
     */
    public List<Map<String, Object>> selectDeviceByGroupIds(List<String> groupIdList,
                                                            List<String> pcCompanyIdList, List<String> companyIdList, Integer currentPage, Integer pageSize) {
        if (groupIdList == null || groupIdList.size() == 0) {
            return new ArrayList<>();
        }
        Integer start = 0;
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        start = (currentPage - 1) * pageSize;
        return deviceMapper.selectDeviceByGroupIds(groupIdList, pcCompanyIdList, companyIdList, start, pageSize);
    }

    /**
     * 根据设备组Id查询设备列表
     *
     * @param groupIdList
     * @param pcCompanyIdList
     * @param companyIdList
     * @return
     */
    public Integer selectDeviceCountByGroupIds(List<String> groupIdList, List<String> pcCompanyIdList, List<String> companyIdList) {
        if (groupIdList.size()==0){
            return 0;
        }
        return deviceMapper.selectDeviceCountByGroupIds(groupIdList, pcCompanyIdList, companyIdList);
    }

    public List<String> selectCompanyUserByDeviceId(String deviceId) {
        return deviceMapper.selectCompanyUserByDeviceId(deviceId);
    }

    /**
     * @param deviceId
     * @param roleId,2:服务商，3:技师，4:客户
     * @return
     */
    public List<String> selectRoleUserByDeviceId(String deviceId, String roleId) {
        return deviceMapper.selectRoleUserByDeviceId(deviceId, roleId);
    }

    public List<Map<String, Object>> selectDeviceByIds(@Param("idList") List<String> idList) {
        if (idList.size() > 0) {
            return deviceMapper.selectDeviceByIds(idList);
        }
        return new ArrayList<>();
    }

    public Integer countByCompanyId(String companyId) {
        return deviceMapper.countByCompanyId(companyId);
    }

    public Integer countByCompanyIdAndBoxId(String companyId) {
        return deviceMapper.countByCompanyIdAndBoxId(companyId);
    }

    public List<Map<String, Object>> getDeviceByCompanyId(List<String> companyId) {
        return deviceMapper.getDeviceByCompanyId(companyId);
    }

    public List<Map<String, Object>> groupByCompanyId(List<String> groupId) {
        return deviceMapper.groupByCompanyId(groupId);
    }

    //当status不等于null时查询带云盒的设备,等于null时查询所有设备
    public BoxAndDeviceNum countDeviceByCompanyIdAndGroupId(String companyId, String groupId) {
        return deviceMapper.countDeviceByCompanyIdAndGroupId(companyId, groupId);
    }

    public List<Map<String, Object>> filterDeviceForListDevice(
            String pcCompanyId,
            List<String> companyIdList,
            List<String> groupIdList,
            List<String> boxIdList,
            String deviceName,
            String deviceType,
            Integer currentPage,
            Integer pageSize
    ) {
        Integer start;
        if (currentPage == null) {
            start = null;
            pageSize = null;
        } else {
            start = (currentPage - 1) * pageSize;
        }
        return deviceMapper.filterDeviceForListDevice(pcCompanyId, companyIdList, groupIdList, boxIdList, deviceName, deviceType, start, pageSize);
    }

    public Integer countDeviceForListDevice(
            String pcCompanyId,
            List<String> companyIdList,
            List<String> groupIdList,
            List<String> boxIdList,
            String deviceType,
            String deviceName
    ) {
        return deviceMapper.countDeviceForListDevice(pcCompanyId, companyIdList, groupIdList,boxIdList, deviceType, deviceName);
    }

    public List<Map<String, Object>> selectDeviceByCompanyIdList(List<String> companyIdList, String deviceType,
                                                                 String deviceName, Integer currentPage, Integer pageSize) {
        Integer start;
        start = (currentPage - 1) * pageSize;
        return deviceMapper.selectDeviceByCompanyIdList(companyIdList, deviceType, deviceName, start, pageSize);
    }

    public Integer countDeviceByCompanyIdList(List<String> companyIdList, String deviceType, String deviceName) {
        return deviceMapper.countDeviceByCompanyIdList(companyIdList, deviceType, deviceName);
    }



    public Map<String, Object> selectDeviceByDeviceId(String deviceId) {
        return deviceMapper.selectDeviceByDeviceId(deviceId);
    }

    public Integer getCountDownTime(String inspectionId) {
        return deviceMapper.getCountDownTime(inspectionId);
    }

    public Map<String, String> getUserAndParentId(String groupId) {
        return deviceMapper.getUserAndParentId(groupId);
    }

    public Integer checkIfDeviceInRepair(String deviceId) {
        return deviceMapper.checkIfDeviceInRepair(deviceId);
    }

    public List<Map<String, Object>> selectFinishedRepairListByDeviceId(String deviceId, String startDate, String endDate, Integer currentPage, Integer pageSize) {
        Integer start = (currentPage - 1) * pageSize;
        List<Map<String, Object>> finishedRepairList = deviceMapper.selectFinishedRepairListByDeviceId(deviceId, startDate, endDate, start, pageSize);
        for (Map<String, Object> repair : finishedRepairList) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Timestamp dueTime = (Timestamp) repair.get("dueTime");
            repair.put("dueTime", format.format(dueTime));
            Timestamp completeDate = (Timestamp) repair.get("completeDate");
            repair.put("completeDate", format.format(completeDate));
        }
        return finishedRepairList;
    }

    public Integer countFinishedRepairListByDeviceId(String deviceId, String startDate, String endDate) {
        return deviceMapper.countFinishedRepairListByDeviceId(deviceId, startDate, endDate);
    }


    /**
     * 查询某一个设备组下的设备，包含子设备组中的设备
     */
    public List<Map<String, Object>> selectDeviceByGroupId(String deviceGroupId, String pcCompanyId, List<String> companyIdList, Integer currentPage, Integer pageSize) {
             List<String> groupIdList=new ArrayList<>();
             groupIdList.add(deviceGroupId);
             List<String> pcCompanyIdList=new ArrayList<>();
            pcCompanyIdList.add(pcCompanyId);
            return selectDeviceByGroupIds(groupIdList, pcCompanyIdList, companyIdList, currentPage, pageSize);
     }




    public List<Map<String, Object>> filterDeviceForMonitor(String companyId, Integer status){
        return deviceMapper.filterDeviceForMonitor(companyId, status);
    }

    public List<Map<String, Object>> selectDeviceByCompanyIdAndGroupId(String companyId, String groupId){
        return deviceMapper.selectDeviceByCompanyIdAndGroupId(companyId, groupId);
    }

    public Map<String, Integer>  countDeviceForMonitor(){
        return deviceMapper.countDeviceForMonitor();
    }

    public List<Map<String, Object>> selectWarningDeviceByUserId(String userId, Integer currentPage, Integer pageSize){
        Integer start = (currentPage - 1) * pageSize;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<Map<String, Object>> warningDeviceList = deviceMapper.selectWarningDeviceByUserId(userId, start, pageSize);
        for (Map<String, Object> device: warningDeviceList){
            Timestamp dateTime = (Timestamp) device.get("datetime");
            device.put("dateTime", format.format(dateTime));
        }
        return warningDeviceList;
    }

    public Integer countWarningDeviceByUserId(String userId){
        return deviceMapper.countWarningDeviceByUserId(userId);
    }

    public List<Map<String, Object>> getDeviceListByPcCompanyId(String pcCompanyId){
        return deviceMapper.getDeviceListByPcCompanyId(pcCompanyId);
    }


    public   void  deleteDeviceByIds( List<String> idList){
        if (idList.size()>0){
            deviceMapper.deleteDeviceByIds(idList);
        }
    }

    public List<String>
    getRedisDataByPcCompanyIdAndStatus(String pcCompanyId, Integer status, Integer currentPage, Integer pageSize)   {

        String redisTableName;
        if (status == 1) {
            redisTableName = "exception" + pcCompanyId;
        } else if (status == 2) {
            redisTableName = "offline" + pcCompanyId;
        } else if (status == 3) {
            return new LinkedList<String>() {{
                add(null);
            }};
        } else {
            throw new RuntimeException("设备状态选择异常，只有 {1：异常；2：离线;}。 不存在此数字对应状态: " + status);
        }
        Map<String, String> tableData = redisCacheUtil.getCacheMap(redisTableName);
        List<String> resultKeys = new LinkedList<>(tableData.keySet());
        Collections.sort(resultKeys);
        Integer start = (currentPage - 1) * pageSize;
        Integer end = currentPage * pageSize;
        start = start > resultKeys.size() ? resultKeys.size() : start;
        end = end > resultKeys.size() ? resultKeys.size() : end;
        resultKeys = resultKeys.subList(start, end);
        List<String> result = new LinkedList<>();
        for (String boxId : resultKeys) {
            result.add(tableData.get(boxId));
        }
        if (result.size()==0){
            result.add("0");
        }
        return result;
    }

    public Integer getRedisDataCountByPcCompanyIdAndStatus(String pcCompanyId, Integer status){
        String redisTableName;
        if (status==1){
            redisTableName = "exception"+pcCompanyId;
        }else{
            redisTableName = "offline"+pcCompanyId;
        }
        Map<String, String> tableData = redisCacheUtil.getCacheMap(redisTableName);
        return tableData.size();
    }

    public List<Map<String, Object>> addStatus2Device(List<Map<String, Object>> deviceMapList, String status) {
        for (Map<String, Object> deviceMap : deviceMapList) {
            deviceMap.put("boxStatus", status);
        }
        return deviceMapList;
    }


    //遍历得到设备结果细节
    public List<Map<String, Object>> getStatusFilter(List<Map<String, Object>> deviceList, String status) {
        List<Map<String, Object>> result = new LinkedList<>();
        Map<String, String> boxStatusMap = redisCacheUtil.getCacheMap("box_status_table");
        for (Map<String, Object> device : deviceList) {
            Map<String, Object> deviceMap = this.addAndFilterBoxStatus(device, status, boxStatusMap);
            if (deviceMap != null) {
                result.add(deviceMap);
            }
        }
        return result;
    }

    //boxStatus正常
    private static String[] NORMAL_STATUS = {"1", "7", "9", "11", "13", "15"};
    //boxStatus异常
    private static String[] ABNORMAL_STATUS = {"0", "2", "3", "4", "8", "10", "12", "14", "16"};
    //boxStatus离线
    private static String[] OFFLINE_STATUS = {"5"};

    private Map<String, Object> addAndFilterBoxStatus(Map<String, Object> device, String status, Map<String, String> boxStatusTable) {
        String boxId = (String) device.getOrDefault("boxId", null);
        String boxStatus;
        if (StringUtils.isBlank(boxId)){
            boxStatus = "";
            device.put("haveBox", 0);
        }else {
            device.put("haveBox", 1);
            boxStatus =  boxStatusTable.getOrDefault(boxId, "0");
        }

        if (status == null || status.equals("")) {
            device.put("boxStatus", getBoxStatusString(boxStatus));
            device.put("boxCode", boxStatus);
            return device;
        }

        if (status.equals("异常")) {
            if ("异常".equals(getBoxStatusString(boxStatus))) {
                device.put("boxStatus", "异常");
                device.put("boxCode", boxStatus);
                return device;
            } else {
                return null;
            }
        } else if (status.equals("离线")) {
            if ("离线".equals(getBoxStatusString(boxStatus))) {
                device.put("boxStatus", "离线");
                device.put("boxCode",boxStatus );
                return device;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    //得到boxStatus
    private String getBoxStatusString(String boxStatus) {
        if (Arrays.asList(NORMAL_STATUS).contains(boxStatus)) {
            return "正常";
        } else if (Arrays.asList(ABNORMAL_STATUS).contains(boxStatus)) {
            return "异常";
        } else if (Arrays.asList(OFFLINE_STATUS).contains(boxStatus)) {
            return "离线";
        } else {
            return "正常";
        }
    }


    public Map<String,Object> selectDeviceByBoxIdUnDeleted(String  boxId){
        return deviceMapper.selectDeviceByBoxIdUnDeleted(boxId);
    }

    public Map<String, String> getProtocolAndType(String boxId)  {
        try {
            Map<String, String> protocolAndType = redisCacheUtil.getCacheMap(boxId+"protocolAndType");
            if (protocolAndType==null || protocolAndType.size()==0){
                throw new Exception();
            }else {
                return protocolAndType;
            }
        }catch (Exception e){
            try {
                Map<String, String> apiCall = apiCallFacadeService.getProtocolAndDevTypeByBoxId(boxId);
                redisCacheUtil.setCacheMap(boxId+"protocolAndType", apiCall);
                return apiCall;
            }catch (Exception ex){
                ex.printStackTrace();
                return new HashMap<>();
            }
        }
    }

}