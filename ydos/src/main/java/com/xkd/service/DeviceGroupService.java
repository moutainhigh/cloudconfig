package com.xkd.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sun.xml.ws.config.management.policy.ManagementPolicyValidator;
import com.xkd.exception.BoxIDAlreadyExistException;
import com.xkd.exception.BoxIdInvalidException;
import com.xkd.mapper.DeviceGroupMapper;
import com.xkd.model.DeviceGroupTreeNode;
import com.xkd.utils.HttpRequestUtil;
import com.xkd.utils.PropertiesUtil;
import com.xkd.utils.RedisCacheUtil;
import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by dell on 2018/2/12.
 */
@Service
public class DeviceGroupService {
    @Autowired
    DeviceGroupMapper deviceGroupMapper;

    @Autowired
    DeviceService deviceService;
    @Autowired
    UserService userService;

    @Autowired
    CompanyService companyService;

    @Autowired
    DepartmentService departmentService;


    @Autowired
    CompanyContactorService companyContactorService;

    @Autowired
    ApiCallFacadeService apiCallFacadeService;

    @Autowired
    RedisCacheUtil redisCacheUtil;



    public Map<String, Object> selectDeviceGroupById(String id) {
        return deviceGroupMapper.selectDeviceGroupById(id);
    }
    public List<String> searchCompanyIdByPcCompanyId(String pcCompanyId,Integer currentPage,Integer pageSize){
        Integer start = 0;
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        start = (currentPage - 1) * pageSize;
        return deviceGroupMapper.searchCompanyIdByPcCompanyId(pcCompanyId, start, pageSize);
    }
    public Integer searchCompanyIdCountByPcCompanyId( String pcCompanyId){
        return deviceGroupMapper.searchCompanyIdCountByPcCompanyId(pcCompanyId);
    }

    public void insertDeviceGroup(Map<String, Object> map, String loginUserId)  {

        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
        map.put("createdBy", loginUserId);
        map.put("createDate", new Date());
        map.put("pcCompanyId", loginUserMap.get("pcCompanyId"));

        String parentId = (String) map.get("parentId");
        Integer parentType = (Integer) map.get("parentGroupType");
        Integer type = (Integer) map.get("groupType");
        /*
        服务商下面只能添加建筑或区域
         */
        if (1 == parentType) {
            map.put("parentId", null);
            if (2 != type && 3 != type) {
                throw new RuntimeException("该结点下只能添加建筑和区域");
            }
        }


        /*
            区域下面只能添加建筑
         */
        if (2 == parentType) {
            if (3 != type) {
                throw new RuntimeException("该结点下只能添加建筑");
            }
        }

        /*
         建筑下面只能添加机房和设备
         */
        if (3 == parentType) {
            if (4 != type && 5 != type) {
                throw new RuntimeException("该结点下只能添加机房或设备");
            }
        }

        /*
         机房下面只能添加设备
         */
        if (4 == parentType) {
            if (5 != type) {
                throw new RuntimeException("该结点下只能添加设备");
            }
        }


        /*
        客户下添加建筑或区域，则设置其客户Id
         */
        if (1 == parentType) {
            map.put("companyId", parentId);
        } else {
            Map<String, Object> parentDeviceGroup = deviceGroupMapper.selectDeviceGroupById(parentId);
            map.put("companyId", parentDeviceGroup.get("companyId"));
        }


        //添加设备组
        if (2 == type || 3 == type || 4 == type) {

            if (StringUtil.isBlank((String) map.get("groupName"))) {
                throw new RuntimeException("组名不能为空");
            }
            if (null == map.get("groupType")) {
                throw new RuntimeException("组类型不能为空");
            }
            deviceGroupMapper.insertDeviceGroup(map);

            if (StringUtils.isNotBlank((String) map.get("responsibleUserId"))){

                    //下发告警名单
                apiCallFacadeService.addDeviceWarningWhenSetResponsibleTechnican((String)map.get("id"),(String)map.get("pcCompanyId"),(String) map.get("responsibleUserId"));
            };



        }


        //添加设备
        if (5 == type) {

            if (StringUtil.isBlank((String) map.get("deviceName"))) {
                throw new RuntimeException("设备名不能为空");
            }
            if (null == map.get("deviceNo")) {
                throw new RuntimeException("设备编号不能为空");
            }
            if (null == map.get("deviceType")) {
                throw new RuntimeException("设备类型不能为空");
            }

            String boxId=(String) map.get("boxId");
            map.put("groupId", map.get("parentId"));


            /**
             * 如果是云盒设备。则要写到旧系统
             */
            if (StringUtils.isNotBlank(boxId)) {

                Map<String,Object> existInDb=  deviceService.selectDeviceByBoxIdUnDeleted(boxId);
                if (existInDb!=null){
                    throw new BoxIDAlreadyExistException();
                }


                //插入设备
                deviceService.insertDevice(map);




                //添加设备
               Map<String,Object> resultMap= apiCallFacadeService.addDevice(boxId, (String) map.get("protocol"), (String) map.get("inlines"), (String) map.get("outlines"), (String) map.get("model"));
                Map<String,Object> devMap=new HashMap<>();
                devMap.put("id",map.get("id"));
                devMap.put("notConfImg",resultMap.get("notconf"));
                devMap.put("redImg",resultMap.get("red"));
                devMap.put("yellowImg",resultMap.get("yellow"));
                devMap.put("greenImg",resultMap.get("green"));
                deviceService.updateDevice(devMap);



                Integer num= (Integer) map.get("num");
                String barCodes= (String) map.get("barCodes");
                List<String> barCodeList=new ArrayList<>();
                if (StringUtils.isNotBlank(barCodes)){
                    String[] barCodeArray=barCodes.split(",");
                    barCodeList.addAll(Arrays.asList(barCodeArray));
                }
                apiCallFacadeService.confDevices(boxId,num,barCodeList);


                //下发设备告警
                try {
                    apiCallFacadeService.addDeviceWarningWhenAddDevice((String)map.get("groupId"), boxId,(String)map.get("pcCompanyId"),(String)map.get("companyId"));
                }catch (BoxIdInvalidException be){
                    be.printStackTrace();
                    apiCallFacadeService.deleteDevice(boxId);
                    throw be;

                }catch (Exception e){
                    //出现异常就删除设备
                    e.printStackTrace();
                    apiCallFacadeService.deleteDevice(boxId);
                    throw new RuntimeException(e.getMessage());
                }

                //将盒子属于哪一个服务商的关系写入Redis
                Map<String,String>  redisMap=new HashMap<>();
                redisMap.put(boxId,(String)loginUserMap.get("pcCompanyId"));
                redisCacheUtil.setCacheMap("boxId_pcCompany",redisMap);

                //盒子的初始状态为未配置
                Map<String,Object> redisMap2=new HashMap<>();
                redisMap2.put(boxId,"0");
                redisCacheUtil.setCacheMap((String) loginUserMap.get("pcCompanyId"), redisMap2);

            }else{

                //插入设备
                deviceService.insertDevice(map);
                //修改设备Icon
                Map<String,Object> resultMap=  apiCallFacadeService.getDeviceIcons( (String) map.get("protocol"));
                Map<String,Object> devMap=new HashMap<>();
                devMap.put("id",map.get("id"));
                devMap.put("notConfImg",resultMap.get("notconf"));
                devMap.put("redImg", resultMap.get("red"));
                devMap.put("yellowImg",resultMap.get("yellow"));
                devMap.put("greenImg",resultMap.get("green"));
                deviceService.updateDevice(devMap);
            }

        }


    }

    public int updateDeviceGroup(Map<String, Object> map)   {
        Map<String,Object> oldGroup=deviceGroupMapper.selectDeviceGroupById((String) map.get("id"));
        String oldResponsibleUserId= (String) oldGroup.get("responsibleUserId");
        String newResponsibleUserId= (String) map.get("responsibleUserId");
        if (StringUtils.isNotBlank(oldResponsibleUserId)){
            if (!oldResponsibleUserId.equals(newResponsibleUserId)){
                apiCallFacadeService.deleteDeviceWarningWhenUnSetResponsibleTechnican((String)map.get("id"),(String)oldGroup.get("pcCompanyId"),oldResponsibleUserId);
            }
            if (StringUtils.isNotBlank(newResponsibleUserId)){
                apiCallFacadeService.addDeviceWarningWhenSetResponsibleTechnican((String)map.get("id"),(String)oldGroup.get("pcCompanyId"),newResponsibleUserId);
            }
        }else {
            if (StringUtils.isNotBlank(newResponsibleUserId)){
                apiCallFacadeService.addDeviceWarningWhenSetResponsibleTechnican((String)map.get("id"),(String)oldGroup.get("pcCompanyId"),newResponsibleUserId);
            }
        }
        return deviceGroupMapper.updateDeviceGroup(map);
    }


    public List<String> searchCompanyIdByDevice(String searchValue, String pcCompanyId, Integer currentPage, Integer pageSize) {
        Integer start = 0;
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        start = (currentPage - 1) * pageSize;
        return deviceGroupMapper.searchCompanyIdByDevice(searchValue, pcCompanyId, start, pageSize);
    }

    public Integer searchCompanyIdCountByDevice(String searchValue, String pcCompanyId) {
        return deviceGroupMapper.searchCompanyIdCountByDevice(searchValue, pcCompanyId);
    }


    public List<String> searchPcCompanyIdByDevice(String searchValue, List<String> companyIdList, Integer currentPage, Integer pageSize) {
        Integer start = 0;
        if (currentPage == null) {
            currentPage = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        start = (currentPage - 1) * pageSize;
        if (companyIdList.size()>0) {
            return deviceGroupMapper.searchPcCompanyIdByDevice(searchValue, companyIdList, start, pageSize);
        }
        return new ArrayList<>();
    }

    public Integer searchPcCompanyIdCountByDevice(String searchValue, List<String> companyIdList) {
        if (companyIdList.size()>0) {
            return deviceGroupMapper.searchPcCompanyIdCountByDevice(searchValue, companyIdList);
        }
        return 0;
    }



    /**
     * 服务端----查看树形结构
     * @param companyIdList
     * @param pcCompanyId
     * @return
     */
    public DeviceGroupTreeNode deviceTree1(List<String> companyIdList, String pcCompanyId,Boolean showDevice) {
        List<Map<String, Object>> mapList = new ArrayList<>();

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("id", "0");
        rootMap.put("groupType", 0);
        rootMap.put("name", "列表");
        rootMap.put("deviceType", null);
        rootMap.put("parentId", null);
        mapList.add(rootMap);
        /**
         * 构建根结点
         */
        DeviceGroupTreeNode rootNode = new DeviceGroupTreeNode();
        rootNode.setId((String) rootMap.get("id"));
        rootNode.setGroupType((Integer) rootMap.get("groupType"));
        rootNode.setName((String) rootMap.get("name"));
        rootNode.setDeviceType((String) rootMap.get("deviceType"));
        rootNode.setParentId((String) rootMap.get("parentId"));

        if (companyIdList.size() == 0) {
            return rootNode;
        }
        /**
         * 查询客户列表
         */
        List<Map<String, Object>> companyList = companyService.selectCompanyByIds(companyIdList);
        List<Map<String, Object>> deviceGroupList = new ArrayList<>();
        List<Map<String, Object>> deviceList = new ArrayList<>();

        /**
         * 查询客户下的所有设备组
         */
        if (companyIdList.size() > 0) {
            deviceGroupList = deviceGroupMapper.selectDeviceGroupByCompanyIds(companyIdList, 0);
        }

        if (showDevice) {  //是否显示设备
            if (deviceGroupList.size() > 0) {
                List<String> groupIdList = new ArrayList<>();
                for (int i = 0; i < deviceGroupList.size(); i++) {
                    groupIdList.add((String) deviceGroupList.get(i).get("id"));
                }
                List<String> pcCompanyIdList=new ArrayList<>();
                pcCompanyIdList.add(pcCompanyId);
                //查询某一个服务商自己的设备
                deviceList = deviceService.selectDeviceByGroupIds(groupIdList, pcCompanyIdList, companyIdList,1,100000);
            }
        }



        for (int i = 0; i < companyList.size(); i++) {
            Map<String, Object> companyMap = companyList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", companyMap.get("id"));
            map.put("groupType", 1);
            map.put("name", companyMap.get("companyName"));
            map.put("deviceType", null);
            map.put("parentId", "0");
            mapList.add(map);
        }
        for (int i = 0; i < deviceGroupList.size(); i++) {
            Map<String, Object> deviceGroupMap = deviceGroupList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", deviceGroupMap.get("id"));
            map.put("groupType", deviceGroupMap.get("groupType"));
            map.put("name", deviceGroupMap.get("groupName"));
            map.put("deviceType", null);
            map.put("parentId", deviceGroupMap.get("parentId"));
            mapList.add(map);
        }

        for (int i = 0; i < deviceList.size(); i++) {
            Map<String, Object> deviceMap = deviceList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", deviceMap.get("id"));
            map.put("groupType", 5);
            map.put("name", deviceMap.get("deviceName"));
            map.put("deviceType", deviceMap.get("deviceType"));
            map.put("parentId", deviceMap.get("groupId"));
            map.put("img",deviceMap.get("greenImg"));
            map.put("boxId",deviceMap.get("boxId"));
            mapList.add(map);
        }
        //查询设备 配置信息
        getDeviceConf(mapList);

//        shuffe1(rootNode, mapList);

        rootNode=mapListToDeviceGroupTreeNode(mapList);
        return rootNode;


    }


    private DeviceGroupTreeNode mapListToDeviceGroupTreeNode(List<Map<String,Object>> mapList) {

        /**
         * 创建一个map,用来存储一个  树Id----树结点 的数据结构
         */
         Map<String,DeviceGroupTreeNode> id_deviceTreeNode_Map=new HashMap<>();

        /**
         * 遍历map列表，将每个map对象转换成DeviceGroupTreeNode对象，并将其以 Id -- DeviceGroupTreeNode的结构存在hMap中
         */
        for (int i = 0; i <mapList.size() ; i++) {
            Map<String,Object> map= mapList.get(i);
            DeviceGroupTreeNode treeNode = new DeviceGroupTreeNode();
            treeNode.setId((String) map.get("id"));
            treeNode.setName((String) map.get("name"));
            treeNode.setParentId((String) map.get("parentId"));
            treeNode.setDeviceType((String) map.get("deviceType"));
            treeNode.setGroupType((Integer) map.get("groupType"));
//            treeNode.setParentGroupType(deviceGroupTreeNode.getGroupType());
            treeNode.setImg((String) map.get("img"));
            treeNode.setConfMap((Map<String, Object>) map.get("confMap"));
            treeNode.setBoxId((String) map.get("boxId"));


            //设置设备组的Icon
            if (1==treeNode.getGroupType()){
                treeNode.setImgHover(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_kehu_hover.png");
                treeNode.setImgNor(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_kehu_nor.png");
            }else if (2==treeNode.getGroupType()){
                treeNode.setImgHover(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_diqu_hover.png");
                treeNode.setImgNor(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_diqu_nor.png");
            }else  if (3==treeNode.getGroupType()){
                treeNode.setImgHover(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_dasha_hover.png");
                treeNode.setImgNor(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_dasha_nor.png");
            }else if (4==treeNode.getGroupType()){
                treeNode.setImgHover(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_loufang_hover.png");
                treeNode.setImgNor(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_loufang_nor.png");
            }

            id_deviceTreeNode_Map.put(treeNode.getId(),treeNode);

        }


        /**
         * 构建树形结构
         */
        //首先取出所有key,即所有id
        Set<String>  keySet=id_deviceTreeNode_Map.keySet();
        Iterator<String> keIterator=keySet.iterator();
        while (keIterator.hasNext()){
            //遍历key,从第一个key开始遍历
            String key=keIterator.next();
            //id_deviceTreeNode_Map当前遍历对象
            DeviceGroupTreeNode currentNode=id_deviceTreeNode_Map.get(key);
            if (currentNode==null){
                continue;
            }
            //找到当前遍历对象的父id
            String parentKey=currentNode.getParentId();
            //找到当前遍历对象的父对象
            DeviceGroupTreeNode parentNode=id_deviceTreeNode_Map.get(parentKey);
            if (parentNode==null){
               continue;
            }
            //将父对象的类型设置到当前对象的parentGroupType中
            currentNode.setParentGroupType(parentNode.getGroupType());
            //将当前对象添加到父对象的childTreeNodeList表中
            parentNode.getChildTreeNodeList().add(currentNode);
        }
        //找到根结点
        return id_deviceTreeNode_Map.get("0");
    }



//    private void shuffe1(DeviceGroupTreeNode deviceGroupTreeNode, List<Map<String, Object>> mapList) {
//        for (int i = 0; i < mapList.size(); i++) {
//            Map<String, Object> map = mapList.get(i);
//            if (deviceGroupTreeNode.getId().equals(map.get("parentId"))) {
//                DeviceGroupTreeNode treeNode = new DeviceGroupTreeNode();
//                treeNode.setId((String) map.get("id"));
//                treeNode.setName((String) map.get("name"));
//                treeNode.setParentId(deviceGroupTreeNode.getId());
//                treeNode.setDeviceType((String) map.get("deviceType"));
//                treeNode.setGroupType((Integer) map.get("groupType"));
//                treeNode.setParentGroupType(deviceGroupTreeNode.getGroupType());
//                treeNode.setImg((String) map.get("img"));
//                treeNode.setConfMap((Map<String, Object>) map.get("confMap"));
//                treeNode.setBoxId((String) map.get("boxId"));
//
//                if (1==treeNode.getGroupType()){
//                    treeNode.setImgHover(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_kehu_hover.png");
//                    treeNode.setImgNor(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_kehu_nor.png");
//                }else if (2==treeNode.getGroupType()){
//                    treeNode.setImgHover(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_diqu_hover.png");
//                    treeNode.setImgNor(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_diqu_nor.png");
//                }else  if (3==treeNode.getGroupType()){
//                    treeNode.setImgHover(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_dasha_hover.png");
//                    treeNode.setImgNor(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_dasha_nor.png");
//                }else if (4==treeNode.getGroupType()){
//                    treeNode.setImgHover(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_loufang_hover.png");
//                    treeNode.setImgNor(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_loufang_nor.png");
//                }
//
//                deviceGroupTreeNode.getChildTreeNodeList().add(treeNode);
//
//            }
//        }
//
//        List<DeviceGroupTreeNode> childList = deviceGroupTreeNode.getChildTreeNodeList();
//        for (int i = 0; i < childList.size(); i++) {
//            shuffe1(childList.get(i), mapList);
//        }
//
//    }

    private void shuffe2(DeviceGroupTreeNode deviceGroupTreeNode, List<Map<String, Object>> mapList, List<DeviceGroupTreeNode> deviceNodeList) {


        for (int i = 0; i < mapList.size(); i++) {
            Map<String, Object> map = mapList.get(i);
            if (deviceGroupTreeNode.getId().equals(map.get("parentId"))) {
                DeviceGroupTreeNode treeNode = new DeviceGroupTreeNode();
                treeNode.setId((String) map.get("id"));
                treeNode.setName((String) map.get("name"));
                treeNode.setParentId(deviceGroupTreeNode.getId());
                treeNode.setDeviceType((String) map.get("deviceType"));
                treeNode.setGroupType((Integer) map.get("groupType"));
                treeNode.setParentGroupType(deviceGroupTreeNode.getGroupType());
                treeNode.setImg((String) map.get("img"));
                treeNode.setConfMap((Map<String, Object>) map.get("confMap"));
                treeNode.setBoxId((String) map.get("boxId"));


                if (1==treeNode.getGroupType()){
                    treeNode.setImgHover(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_kehu_hover.png");
                    treeNode.setImgNor(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_kehu_nor.png");
                }else if (2==treeNode.getGroupType()){
                    treeNode.setImgHover(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_diqu_hover.png");
                    treeNode.setImgNor(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_diqu_nor.png");
                }else  if (3==treeNode.getGroupType()){
                    treeNode.setImgHover(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_dasha_hover.png");
                    treeNode.setImgNor(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_dasha_nor.png");
                }else if (4==treeNode.getGroupType()){
                    treeNode.setImgHover(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_loufang_hover.png");
                    treeNode.setImgNor(PropertiesUtil.FILE_HTTP_PATH+"icons/deviceIcons/srm_shebei_loufang_nor.png");
                }

                if (5 == (Integer) map.get("groupType")) {
                    deviceNodeList.add(treeNode);
                }
                deviceGroupTreeNode.getChildTreeNodeList().add(treeNode);

            }
        }

        List<DeviceGroupTreeNode> childList = deviceGroupTreeNode.getChildTreeNodeList();
        for (int i = 0; i < childList.size(); i++) {
            shuffe2(childList.get(i), mapList, deviceNodeList);
        }

    }



    /**
     *客户端---查看树形结构
     * @param pcCompanyIdList
     * @param loginUserId
     * @return
     */
    public DeviceGroupTreeNode deviceTree2(List<String> pcCompanyIdList, String loginUserId,Boolean showDevice) {

        List<String> companyIdList = companyContactorService.selectCompanyIdListByContactor(loginUserId,1);

        /**
         * 设备Id与设备Map之间组成的id-deviceMap 关系
         */
        Map<String, Map<String, Object>> mmapWithNoDevice = new HashMap<>();  //设备MapMap


        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("id", "0");
        rootMap.put("groupType", 0);
        rootMap.put("name", "列表");
        rootMap.put("deviceType", null);
        rootMap.put("parentId", null);
        mmapWithNoDevice.put("0", rootMap);

        //构建根结点
        DeviceGroupTreeNode rootNode = new DeviceGroupTreeNode();
        rootNode.setId((String) rootMap.get("id"));
        rootNode.setGroupType((Integer) rootMap.get("groupType"));
        rootNode.setName((String) rootMap.get("name"));
        rootNode.setDeviceType((String) rootMap.get("deviceType"));
        rootNode.setParentId((String) rootMap.get("parentId"));

        if (pcCompanyIdList.size() == 0) {
            return rootNode;
        }
        //获取所有的服务商
        List<Map<String, Object>> pcCompanyList = departmentService.selectDepartmentByIds(pcCompanyIdList,1,100000);
        List<Map<String, Object>> deviceGroupList = new ArrayList<>();
        List<Map<String, Object>> deviceList = new ArrayList<>();

        if (pcCompanyIdList.size() > 0) {
            deviceGroupList = deviceGroupMapper.selectDeviceGroupByPcCompanyIds(pcCompanyIdList, 1);
        }

        if (showDevice) { //是否显示设备
            if (deviceGroupList.size() > 0) {
                List<String> groupIdList = new ArrayList<>();
                for (int i = 0; i < deviceGroupList.size(); i++) {
                    groupIdList.add((String) deviceGroupList.get(i).get("id"));
                }
                /**
                 * 查询客户自已的设备
                 */
                deviceList = deviceService.selectDeviceByGroupIds(groupIdList, pcCompanyIdList, companyIdList,1,100000);
            }
        }

        List<Map<String, Object>> mapList = new ArrayList<>();


        for (int i = 0; i < pcCompanyList.size(); i++) {
            Map<String, Object> pcCompanyMap = pcCompanyList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", pcCompanyMap.get("id"));
            map.put("groupType", 1);
            map.put("name", pcCompanyMap.get("departmentName"));
            map.put("deviceType", null);
            map.put("parentId", "0");
            mmapWithNoDevice.put((String) map.get("id"), map);

            mapList.add(map);
        }
        for (int i = 0; i < deviceGroupList.size(); i++) {
            Map<String, Object> deviceGroupMap = deviceGroupList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", deviceGroupMap.get("id"));
            map.put("groupType", deviceGroupMap.get("groupType"));
            map.put("name", deviceGroupMap.get("groupName"));
            map.put("deviceType", null);
            map.put("parentId", deviceGroupMap.get("parentId"));
            mmapWithNoDevice.put((String) map.get("id"), map);
            mapList.add(map);
        }


        /**
         * 只包含设备MAp列表
         */
        List<Map<String, Object>> deviceMapList = new ArrayList<>();

        for (int i = 0; i < deviceList.size(); i++) {
            Map<String, Object> deviceMap = deviceList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", deviceMap.get("id"));
            map.put("groupType", 5);
            map.put("name", deviceMap.get("deviceName"));
            map.put("deviceType", deviceMap.get("deviceType"));
            map.put("parentId", deviceMap.get("groupId"));
            map.put("img",deviceMap.get("greenImg"));
            map.put("boxId",deviceMap.get("boxId"));
            mapList.add(map);
            deviceMapList.add(map);

        }

        //查询设备 配置信息
        getDeviceConf(deviceMapList);

        /**
         * 构建包含设备的树，并且获树的叶节点
         */
        List<DeviceGroupTreeNode> deviceNodeLeafList = new ArrayList<>();
        /**
         * shuffe2方法用于构建一颗包含设备的树形结构，同时将将这些树形 结构的叶节点装在一个List中
         */
        shuffe2(rootNode, mapList, deviceNodeLeafList);

        /**
         * 通过叶节点往根结点迭代，获取路过的所有结点
         */
        List<String> idNoDeviceIdList = new ArrayList<>();
        for (int i = 0; i < deviceNodeLeafList.size(); i++) {
            getMainRouteNodeMap(deviceNodeLeafList.get(i), rootNode, idNoDeviceIdList);
        }
        /**
         * 获取主干设备Map
         */
        List<Map<String, Object>> ggroupList = getMapWithNoDeviceMap(idNoDeviceIdList, mmapWithNoDevice);
        /**
         * 合并设备Map
         */
        ggroupList.addAll(deviceMapList);

        /**
         * 构建主干树形结构
         */
        DeviceGroupTreeNode mainRouteNodeWithDevice = mainRouteNodeWithDevice(ggroupList);


        return mainRouteNodeWithDevice;


    }





    /**
     *客户端---查看组树形结构
     * @param pcCompanyIdList
     * @param loginUserId
     * @return
     */
    public DeviceGroupTreeNode deviceTree4(List<String> pcCompanyIdList, String loginUserId) {

        List<String> companyIdList = companyContactorService.selectCompanyIdListByContactor(loginUserId,1);

        /**
         * 设备Id与设备Map之间组成的id-deviceMap 关系
         */
        Map<String, Map<String, Object>> mmapWithNoDevice = new HashMap<>();  //设备MapMap


        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("id", "0");
        rootMap.put("groupType", 0);
        rootMap.put("name", "列表");
        rootMap.put("deviceType", null);
        rootMap.put("parentId", null);
        mmapWithNoDevice.put("0", rootMap);

        //构建根结点
        DeviceGroupTreeNode rootNode = new DeviceGroupTreeNode();
        rootNode.setId((String) rootMap.get("id"));
        rootNode.setGroupType((Integer) rootMap.get("groupType"));
        rootNode.setName((String) rootMap.get("name"));
        rootNode.setDeviceType((String) rootMap.get("deviceType"));
        rootNode.setParentId((String) rootMap.get("parentId"));

        if (pcCompanyIdList.size() == 0) {
            return rootNode;
        }
        //获取所有的服务商
        List<Map<String, Object>> pcCompanyList = departmentService.selectDepartmentByIds(pcCompanyIdList,1,100000);
        List<Map<String, Object>> deviceGroupList = new ArrayList<>();
        List<Map<String, Object>> deviceList = new ArrayList<>();

        if (pcCompanyIdList.size() > 0) {
            deviceGroupList = deviceGroupMapper.selectDeviceGroupByPcCompanyIds(pcCompanyIdList, 1);
        }

            if (deviceGroupList.size() > 0) {
                List<String> groupIdList = new ArrayList<>();
                for (int i = 0; i < deviceGroupList.size(); i++) {
                    groupIdList.add((String) deviceGroupList.get(i).get("id"));
                }
                /**
                 * 查询客户自已的设备
                 */
                deviceList = deviceService.selectDeviceByGroupIds(groupIdList, pcCompanyIdList, companyIdList,1,100000);
            }


        List<Map<String, Object>> mapList = new ArrayList<>();


        for (int i = 0; i < pcCompanyList.size(); i++) {
            Map<String, Object> pcCompanyMap = pcCompanyList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", pcCompanyMap.get("id"));
            map.put("groupType", 1);
            map.put("name", pcCompanyMap.get("departmentName"));
            map.put("deviceType", null);
            map.put("parentId", "0");
            mmapWithNoDevice.put((String) map.get("id"), map);

            mapList.add(map);
        }
        for (int i = 0; i < deviceGroupList.size(); i++) {
            Map<String, Object> deviceGroupMap = deviceGroupList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", deviceGroupMap.get("id"));
            map.put("groupType", deviceGroupMap.get("groupType"));
            map.put("name", deviceGroupMap.get("groupName"));
            map.put("deviceType", null);
            map.put("parentId", deviceGroupMap.get("parentId"));
            mmapWithNoDevice.put((String) map.get("id"), map);
            mapList.add(map);
        }


        /**
         * 只包含设备MAp列表
         */
        List<Map<String, Object>> deviceMapList = new ArrayList<>();

        for (int i = 0; i < deviceList.size(); i++) {
            Map<String, Object> deviceMap = deviceList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", deviceMap.get("id"));
            map.put("groupType", 5);
            map.put("name", deviceMap.get("deviceName"));
            map.put("deviceType", deviceMap.get("deviceType"));
            map.put("parentId", deviceMap.get("groupId"));
            map.put("img",deviceMap.get("greenImg"));
            map.put("boxId",deviceMap.get("boxId"));
            mapList.add(map);
            deviceMapList.add(map);

        }

        //查询设备 配置信息
        getDeviceConf(deviceMapList);

        /**
         * 构建包含设备的树，并且获树的叶节点
         */
        List<DeviceGroupTreeNode> deviceNodeLeafList = new ArrayList<>();
        /**
         * shuffe2方法用于构建一颗包含设备的树形结构，同时将将这些树形 结构的叶节点装在一个List中
         */
        shuffe2(rootNode, mapList, deviceNodeLeafList);

        /**
         * 通过叶节点往根结点迭代，获取路过的所有结点
         */
        List<String> idNoDeviceIdList = new ArrayList<>();
        for (int i = 0; i < deviceNodeLeafList.size(); i++) {
            getMainRouteNodeMap(deviceNodeLeafList.get(i), rootNode, idNoDeviceIdList);
        }
        /**
         * 获取主干设备Map
         */
        List<Map<String, Object>> ggroupList = getMapWithNoDeviceMap(idNoDeviceIdList, mmapWithNoDevice);


        /**
         * 构建主干树形结构
         */
        DeviceGroupTreeNode mainRouteNodeWithNoDevice = mainRouteNodeWithDevice(ggroupList);


        return mainRouteNodeWithNoDevice;


    }





    public void  getDeviceConf(List<Map<String,Object>> deviceList){
        List<String> boxIdList=new ArrayList<>();
        for (int i = 0; i <deviceList.size() ; i++) {
            Map<String,Object> deviceMap=deviceList.get(i);
            String boxId= (String) deviceMap.get("boxId");
            if (StringUtils.isNotBlank(boxId)){
                boxIdList.add(boxId);
            }
        }

        if (boxIdList.size()>0){
            Map<String,Object> confMap=apiCallFacadeService.getDeviceConfByBoxIdList(boxIdList);
            if (confMap!=null){
                for (int i = 0; i <deviceList.size() ; i++) {
                    Map<String,Object> deviceMap=deviceList.get(i);
                    String boxId= (String) deviceMap.get("boxId");
                    if (StringUtils.isNotBlank(boxId)){
                        deviceMap.put("confMap",confMap.get(boxId));
                     }
                }
            }
        }
    }




    /**
     * 技师端---查看树形结构
     * @param companyIdList
     * @param loginUserId
     * @return
     */
    public DeviceGroupTreeNode deviceTree3(List<String> companyIdList, String loginUserId,Boolean showDevice) {
        List<Map<String, Object>> mapList = new ArrayList<>();

        /**
         * 构建根结点
         */
        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("id", "0");
        rootMap.put("groupType", 0);
        rootMap.put("name", "列表");
        rootMap.put("deviceType", null);
        rootMap.put("parentId", null);
        mapList.add(rootMap);

        DeviceGroupTreeNode rootNode = new DeviceGroupTreeNode();
        rootNode.setId((String) rootMap.get("id"));
        rootNode.setGroupType((Integer) rootMap.get("groupType"));
        rootNode.setName((String) rootMap.get("name"));
        rootNode.setDeviceType((String) rootMap.get("deviceType"));
        rootNode.setParentId((String) rootMap.get("parentId"));

        if (companyIdList.size() == 0) {
            return rootNode;
        }


        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);

        /**
         * 该技师所负责的设备组，不包含子组
         */
        List<Map<String, Object>> responsibleDeviceGroup = selectDeviceGroupByLoginTechnician(loginUserId);

        List<String> responsibleGroupIdList = new ArrayList<>();
        for (int i = 0; i < responsibleDeviceGroup.size(); i++) {
            responsibleGroupIdList.add((String) responsibleDeviceGroup.get(i).get("id"));
        }


        /**
         * 查询客户列表
         */
        List<Map<String, Object>> companyList = companyService.selectCompanyByIds(companyIdList);
        List<Map<String, Object>> deviceGroupList = new ArrayList<>();
        List<Map<String, Object>> deviceList = new ArrayList<>();

        /**
         * 获取主路径上的设备组，往上查找父结点Id，往下查找所有子节点Id，然后连同自己的Id一起组成一个列表
         */
        if (companyIdList.size() > 0) {
            deviceGroupList = selectMainDeviceGroupByGroupIds(responsibleGroupIdList, 0);
        }

        if (showDevice) { //是否显示设备
            /**
             * 查询设备组下面的所有设备
             */
            if (deviceGroupList.size() > 0) {
                List<String> groupIdList = new ArrayList<>();
                for (int i = 0; i < deviceGroupList.size(); i++) {
                    groupIdList.add((String) deviceGroupList.get(i).get("id"));
                }
                /**
                 * 查询登录用户所在服务商下面的设备
                 */
               List<String>  pcCompanyIdList=new ArrayList<>();
                pcCompanyIdList.add((String) loginUserMap.get("pcCompanyId"));
                deviceList = deviceService.selectDeviceByGroupIds(groupIdList,pcCompanyIdList , companyIdList,1,100000);
            }
        }

        for (int i = 0; i < companyList.size(); i++) {
            Map<String, Object> companyMap = companyList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", companyMap.get("id"));
            map.put("groupType", 1);
            map.put("name", companyMap.get("companyName"));
            map.put("deviceType", null);
            map.put("parentId", "0");
            mapList.add(map);
        }
        for (int i = 0; i < deviceGroupList.size(); i++) {
            Map<String, Object> deviceGroupMap = deviceGroupList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", deviceGroupMap.get("id"));
            map.put("groupType", deviceGroupMap.get("groupType"));
            map.put("name", deviceGroupMap.get("groupName"));
            map.put("deviceType", null);
            map.put("parentId", deviceGroupMap.get("parentId"));
            mapList.add(map);
        }

        for (int i = 0; i < deviceList.size(); i++) {
            Map<String, Object> deviceMap = deviceList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", deviceMap.get("id"));
            map.put("groupType", 5);
            map.put("name", deviceMap.get("deviceName"));
            map.put("deviceType", deviceMap.get("deviceType"));
            map.put("parentId", deviceMap.get("groupId"));
            map.put("img",deviceMap.get("greenImg"));
            map.put("boxId",deviceMap.get("boxId"));
            mapList.add(map);
        }
        //查询设备 配置信息
        getDeviceConf(mapList);
        /**
         * 构建整棵树
         */
      //  shuffe1(rootNode, mapList);
       rootNode=mapListToDeviceGroupTreeNode(mapList);

        //剪掉没有子点的公司节点
        List<DeviceGroupTreeNode> companyNodeList=new ArrayList<>();
        for (int i = 0; i <rootNode.getChildTreeNodeList().size() ; i++) {
             DeviceGroupTreeNode  nd=rootNode.getChildTreeNodeList().get(i);
            if (nd.getChildTreeNodeList().size()>0){
                 companyNodeList.add(nd);
            }
        }
        rootNode.setChildTreeNodeList(companyNodeList);



        return rootNode;


    }


    /**
     * 服务端，技师端------通过设备列表往上倒推设备树
     * @param deviceIdList
     * @return
     */
    public DeviceGroupTreeNode getDeviceGroupTreeByDevices(List<String> deviceIdList) {
        List<Map<String, Object>> mapList = new ArrayList<>();

        /**
         * 获取设备列表
         */
        List<Map<String, Object>> deviceList = deviceService.selectDeviceByIds(deviceIdList);

        /**
         * 查询设备所在的公司
         */
        List<String> companyIdList = new ArrayList<>();
        for (int i = 0; i < deviceList.size(); i++) {
            companyIdList.add((String) deviceList.get(i).get("companyId"));
        }
        List<Map<String, Object>> companyList = companyService.selectCompanyByIds(companyIdList);


        /**
         * 构建根结点
         */
        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("id", "0");
        rootMap.put("groupType", 0);
        rootMap.put("name", "列表");
        rootMap.put("deviceType", null);
        rootMap.put("parentId", null);
        mapList.add(rootMap);

        DeviceGroupTreeNode rootNode = new DeviceGroupTreeNode();
        rootNode.setId((String) rootMap.get("id"));
        rootNode.setGroupType((Integer) rootMap.get("groupType"));
        rootNode.setName((String) rootMap.get("name"));
        rootNode.setDeviceType((String) rootMap.get("deviceType"));
        rootNode.setParentId((String) rootMap.get("parentId"));


        if (companyIdList.size() == 0) {
            return rootNode;
        }

        /**
         * 查询设备所挂载的节点，并从叶节点往根结点倒推，获取所经历的路线上的设备组节点
         */
        List<String> parentGroupIdList = new ArrayList<>();
        for (int i = 0; i < deviceList.size(); i++) {
            parentGroupIdList.add((String) deviceList.get(i).get("groupId"));
        }
        List<Map<String, Object>> deviceGroupList = selectMainDeviceGroupByGroupIds(parentGroupIdList, 0);


        for (int i = 0; i < companyList.size(); i++) {
            Map<String, Object> companyMap = companyList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", companyMap.get("id"));
            map.put("groupType", 1);
            map.put("name", companyMap.get("companyName"));
            map.put("deviceType", null);
            map.put("parentId", "0");
            mapList.add(map);
        }
        for (int i = 0; i < deviceGroupList.size(); i++) {
            Map<String, Object> deviceGroupMap = deviceGroupList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", deviceGroupMap.get("id"));
            map.put("groupType", deviceGroupMap.get("groupType"));
            map.put("name", deviceGroupMap.get("groupName"));
            map.put("deviceType", null);
            map.put("parentId", deviceGroupMap.get("parentId"));
            mapList.add(map);
        }

        for (int i = 0; i < deviceList.size(); i++) {
            Map<String, Object> deviceMap = deviceList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", deviceMap.get("id"));
            map.put("groupType", 5);
            map.put("name", deviceMap.get("deviceName"));
            map.put("deviceType", deviceMap.get("deviceType"));
            map.put("parentId", deviceMap.get("groupId"));
            map.put("img",deviceMap.get("greenImg"));
            mapList.add(map);
        }


//        shuffe1(rootNode, mapList);
        rootNode=mapListToDeviceGroupTreeNode(mapList);
        return rootNode;

    }


    /**
     *     客户端------通过设备列表往上倒推设备树
     */
    public DeviceGroupTreeNode getDeviceGroupTreeByDevicesFromCustomer(List<String> deviceIdList) {

        List<Map<String, Object>> deviceList = deviceService.selectDeviceByIds(deviceIdList);

        /**
         * 查询设备所在的公司
         */
        List<String> pcCompanyIdList = new ArrayList<>();
        for (int i = 0; i < deviceList.size(); i++) {
            pcCompanyIdList.add((String) deviceList.get(i).get("pcCompanyId"));
        }
        //获取所有的服务商
        List<Map<String, Object>> pcCompanyList = departmentService.selectDepartmentByIds(pcCompanyIdList,1,100000);
        List<Map<String, Object>> mapList = new ArrayList<>();


        /**
         * 构建根结点
         */
        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("id", "0");
        rootMap.put("groupType", 0);
        rootMap.put("name", "列表");
        rootMap.put("deviceType", null);
        rootMap.put("parentId", null);
        mapList.add(rootMap);

        DeviceGroupTreeNode rootNode = new DeviceGroupTreeNode();
        rootNode.setId((String) rootMap.get("id"));
        rootNode.setGroupType((Integer) rootMap.get("groupType"));
        rootNode.setName((String) rootMap.get("name"));
        rootNode.setDeviceType((String) rootMap.get("deviceType"));
        rootNode.setParentId((String) rootMap.get("parentId"));


        if (pcCompanyList.size() == 0) {
            return rootNode;
        }

        /**
         * 查询设备所挂载的节点，并从叶节点往根结点倒推，获取所经历的路线上的设备组节点
         */
        List<String> parentGroupIdList = new ArrayList<>();
        for (int i = 0; i < deviceList.size(); i++) {
            parentGroupIdList.add((String) deviceList.get(i).get("groupId"));
        }
        List<Map<String, Object>> deviceGroupList = selectMainDeviceGroupByGroupIds(parentGroupIdList, 1);


        for (int i = 0; i < pcCompanyList.size(); i++) {
            Map<String, Object> companyMap = pcCompanyList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", companyMap.get("id"));
            map.put("groupType", 1);
            map.put("name", companyMap.get("departmentName"));
            map.put("deviceType", null);
            map.put("parentId", "0");
            mapList.add(map);
        }
        for (int i = 0; i < deviceGroupList.size(); i++) {
            Map<String, Object> deviceGroupMap = deviceGroupList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", deviceGroupMap.get("id"));
            map.put("groupType", deviceGroupMap.get("groupType"));
            map.put("name", deviceGroupMap.get("groupName"));
            map.put("deviceType", null);
            map.put("parentId", deviceGroupMap.get("parentId"));
            mapList.add(map);
        }

        for (int i = 0; i < deviceList.size(); i++) {
            Map<String, Object> deviceMap = deviceList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("id", deviceMap.get("id"));
            map.put("groupType", 5);
            map.put("name", deviceMap.get("deviceName"));
            map.put("deviceType", deviceMap.get("deviceType"));
            map.put("parentId", deviceMap.get("groupId"));
            map.put("img",deviceMap.get("greenImg"));

            mapList.add(map);
        }


//        shuffe1(rootNode, mapList);
        rootNode=mapListToDeviceGroupTreeNode(mapList);

        return rootNode;

    }



    /**
     * 查询某一个组及其子组的所有设备组Id
     * @param groupId
     * @return
     */
    public List<String>  selectChildDeviceGroupIdsByGroupId(String groupId){
        List<String> parentGroupIds=new ArrayList<>();
        parentGroupIds.add(groupId);
        if (StringUtil.isBlank(groupId)){
            return new ArrayList<>();
        }else {
            List<String> childIdList=new ArrayList<>();
            selectChildDeviceGroupIdsByGroupIds(parentGroupIds,childIdList);
            return childIdList;
        }
    }


    /**
     * 查询某一个组及其子组的所有设备组Id
     * @param groupIdList
     * @return
     */
    public List<String>  selectChildDeviceGroupIdsByGroupIds(List<String> groupIdList){
          if (groupIdList.size()==0){
              return new ArrayList<>();
          }
            List<String> childIdList=new ArrayList<>();
            selectChildDeviceGroupIdsByGroupIds(groupIdList,childIdList);
            return childIdList;

    }
    /**
     * 根据设备组Id列表，向上查找所有的父设备组Id
     * @param groupIds
     */
    public List<String> selectParentDeviceGroupIdsByGroupIds(List<String> groupIds){
        if (groupIds.size()==0){
            return new ArrayList<>();
        }else {
            List<String> allIdList=new ArrayList<>();
            selectParentDeviceGroupIdsByGroupIds(groupIds,allIdList);
            return allIdList;
        }
    }


    /**
     * 根据设备组Id，向上查找所有的父设备组Id
     * @param groupId
     */
    public List<String> selectParentDeviceGroupIdsByGroupId(String groupId){

        if (StringUtil.isBlank(groupId)){
            return new ArrayList<>();
        }else {
            List<String> groupIds=new ArrayList<>();
            groupIds.add(groupId);
            List<String> allIdList=new ArrayList<>();
            selectParentDeviceGroupIdsByGroupIds(groupIds,allIdList);
            return allIdList;
        }
    }

    //通过内部设备组Id要,往上查找其经过的设备组节点，往下查找其所有子设备组
    private List<String> selectGroupIdListByInnerGroupIds(List<String> groupIds) {
        List<String> childGroupIds =selectChildDeviceGroupIdsByGroupIds(groupIds);
        List<String> parentGroupIds =selectParentDeviceGroupIdsByGroupIds(groupIds);
        Set<String> set = new HashSet<>();
        set.addAll(groupIds);
        set.addAll(childGroupIds);
        set.addAll(parentGroupIds);
        List<String> list = new ArrayList<>();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }



    private List<Map<String, Object>> selectMainDeviceGroupByGroupIds(List<String> groupIds, Integer flag) {
        List<String> idList = selectGroupIdListByInnerGroupIds(groupIds);
        if (idList.size() > 0) {
            return deviceGroupMapper.selectDeviceGroupByIds(idList, flag);
        }
        return new ArrayList<>();
    }


    private void selectChildDeviceGroupIdsByGroupIds(List<String> groupIds, List<String> allIdList) {
        if (groupIds.size() > 0) {
            List<String> childList = new ArrayList<>();
            if (groupIds.size() > 0) {
                childList = deviceGroupMapper.selectDeviceGroupIdsByParentIds(groupIds);
            }
            for (int i = 0; i < childList.size(); i++) {
                if (!allIdList.contains(childList.get(i))) {
                    allIdList.add(childList.get(i));
                }
            }
            selectChildDeviceGroupIdsByGroupIds(childList, allIdList);
        }
    }




    private void selectParentDeviceGroupIdsByGroupIds(List<String> groupIds, List<String> allIdList) {
        if (groupIds.size() > 0) {
            List<String> parentIdList = new ArrayList<>();
            if (groupIds.size() > 0) {
                parentIdList = deviceGroupMapper.selectParentGroupIdsByGroupIds(groupIds);
            }
            for (int i = 0; i < parentIdList.size(); i++) {
                if (parentIdList.get(i) != null && !"".equals(parentIdList.get(i))) {
                    if (!allIdList.contains(parentIdList.get(i))) {
                        allIdList.add(parentIdList.get(i));
                    }
                }
            }
            selectParentDeviceGroupIdsByGroupIds(parentIdList, allIdList);
        }
    }


    private DeviceGroupTreeNode mainRouteNodeWithDevice(List<Map<String, Object>> mapList) {
        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put("id", "0");
        rootMap.put("groupType", 0);
        rootMap.put("name", "列表");
        rootMap.put("deviceType", null);
        rootMap.put("parentId", null);
        mapList.add(rootMap);

        DeviceGroupTreeNode rootNode2 = new DeviceGroupTreeNode();
        rootNode2.setId((String) rootMap.get("id"));
        rootNode2.setGroupType((Integer) rootMap.get("groupType"));
        rootNode2.setName((String) rootMap.get("name"));
        rootNode2.setDeviceType((String) rootMap.get("deviceType"));
        rootNode2.setParentId((String) rootMap.get("parentId"));

//        shuffe1(rootNode2, mapList);
        rootNode2=mapListToDeviceGroupTreeNode(mapList);
        return rootNode2;

    }


    private List<Map<String, Object>> getMapWithNoDeviceMap(List<String> idList, Map<String, Map<String, Object>> mmap) {
        List<Map<String, Object>> mList = new ArrayList<>();
        for (int i = 0; i < idList.size(); i++) {
            if (mmap.get(idList.get(i))!=null){
                mList.add(mmap.get(idList.get(i)));
            }
        }
        return mList;
    }


    private void findNode(DeviceGroupTreeNode rootNode, String id, Map<String, DeviceGroupTreeNode> resultMap) {
        if (rootNode.getId().equals(id)) {
            resultMap.put("result", rootNode);
            return;
        }
        for (int i = 0; i < rootNode.getChildTreeNodeList().size(); i++) {
            if (rootNode.getChildTreeNodeList().size() > 0) {
                if (rootNode.getChildTreeNodeList().get(0).getGroupType() != 5) {
                    findNode(rootNode.getChildTreeNodeList().get(i), id, resultMap);
                }
            }
        }
    }


    private void getMainRouteNodeMap(DeviceGroupTreeNode leafNode, DeviceGroupTreeNode rootNode, List<String> idList) {
        if (5 == leafNode.getGroupType() || leafNode.getChildTreeNodeList().size() > 0) {
            if (5 != leafNode.getGroupType()) {//如果不是设备，则添加
                if (!idList.contains(leafNode.getId())) {
                    idList.add(leafNode.getId());
                }
            }

            Map<String, DeviceGroupTreeNode> resultMap = new HashMap<>();
            findNode(rootNode, leafNode.getParentId(), resultMap);
            DeviceGroupTreeNode objectNode = resultMap.get("result");
            if (objectNode == null) {
                return;
            }
            getMainRouteNodeMap(objectNode, rootNode, idList);
        }

    }

    /**
     * 查询所有设备类型为建筑的设备组
     * @param pcCompanyId
     * @param companyId
     * @return
     */
    public List<Map<String, Object>> selectAllBuildingsByCompanyId(String pcCompanyId, String companyId) {
        return deviceGroupMapper.selectAllBuildingsByCompanyId(pcCompanyId, companyId);
    }

    /**
     * 根据技师Id查询该技师有关的设备组，包括其负责的设备组和其需要巡检的设备组，但是不包子组
     * @param loginUserId
     * @return
     */
    public List<Map<String, Object>> selectDeviceGroupByLoginTechnician(String loginUserId) {
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
        List<Map<String, Object>> mapList1 = deviceGroupMapper.selectDeviceGroupByDepartmentId((String) loginUserMap.get("departmentId"), (String) loginUserMap.get("pcCompanyId"), 0);
        List<Map<String, Object>> mapList2 = deviceGroupMapper.selectDeviceGroupByResponsibleUser(loginUserId, (String) loginUserMap.get("pcCompanyId"), 0);
        List<Map<String, Object>> mapListAll = new ArrayList<>();
        mapListAll.addAll(mapList1);
        mapListAll.addAll(mapList2);
        Set<String> idSet = new HashSet<>();
        for (int i = 0; i < mapListAll.size(); i++) {
            idSet.add((String) mapListAll.get(i).get("id"));
        }

        List<Map<String, Object>> newList = new ArrayList<>();

        Iterator<String> iterator = idSet.iterator();
        while (iterator.hasNext()) {
            String id = iterator.next();
            for (int i = 0; i < mapListAll.size(); i++) {
                if (id.equals(mapListAll.get(i).get("id"))) ;
                newList.add(mapListAll.get(i));
            }
        }

        return newList;
    }

    public List<Map<String, Object>> selectGroupIdAndCompanyIdByUserId(String loginUserId, String userLevel,String companyName, String pcCompanyId){
        return deviceGroupMapper.selectGroupIdAndCompanyIdByUserId(loginUserId, userLevel, companyName, pcCompanyId);
    }







    public List<Map<String, Object>> selectDeviceGroupByIds(  List<String> idList){
        if (idList.size()==0){
            return new ArrayList<>();

        }
        return deviceGroupMapper.selectDeviceGroupByIds(idList,null);
    }



public List<Map<String,Object>>  selectDeviceGroupByCompanyIds(List<String>  companyIdList){
     if (companyIdList.size()==0){
         return new ArrayList<>();
     }
    return deviceGroupMapper.selectDeviceGroupByCompanyIds(companyIdList,null);
}

public List<String> getGroupIdByUserId(String userId){
        return deviceGroupMapper.getGroupIdByUserId(userId);
}

}