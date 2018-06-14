package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sun.javafx.collections.MappingChange;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.xkd.exception.BoxIDAlreadyExistException;
import com.xkd.exception.BoxIdInvalidException;
import com.xkd.exception.GlobalException;
import com.xkd.model.DeviceGroupTreeNode;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.*;
import com.xkd.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.ls.LSInput;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dell on 2018/2/22.
 */

@Api(description = "设备,设备组相关接口")
@Controller
@RequestMapping("/device")
@Transactional
public class DeviceController extends BaseController {


    @Autowired
    DeviceGroupService deviceGroupService;


    @Autowired
    YDrepaireService repaireService;

    @Autowired
    DeviceService deviceService;

    @Autowired
    UserService userService;
    @Autowired
    CompanyService companyService;

    @Autowired
    CompanyContactorService companyContactorService;

    @Autowired
    YDrepaireService yDrepaireService;

    @Autowired
    ContractService contractService;

    @Autowired
    InspectionTaskService inspectionTaskService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    RedisTemplate<Serializable, Serializable> jedisTemplate;

    @Autowired
    InspectionPlanService inspectionPlanService;

    @Autowired
    RedisCacheUtil<String> redisCacheUtil;

    @Autowired
    ApiCallFacadeService apiCallFacadeService;

    @ApiOperation(value = "添加设备组，或设备--服务商")
    @ResponseBody
    @RequestMapping(value = "/addDeviceGroup", method = {RequestMethod.POST})
    public ResponseDbCenter addDeviceGroup(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "公用属性--父结点Id", required = true) @RequestParam(required = true) String parentId,
                                           @ApiParam(value = "公用属性--父结点类型", required = true) @RequestParam(required = true) Integer parentGroupType,
                                           @ApiParam(value = "公用属性--结点类型  1 服务商  2  区域  3  建筑  4 机房  5  设备   ", required = false) @RequestParam(required = false) Integer groupType,

                                           @ApiParam(value = "设备组属性--新增设备组名", required = false) @RequestParam(required = false) String groupName,
                                           @ApiParam(value = "设备组属性--地址", required = false) @RequestParam(required = false) String address,
                                           @ApiParam(value = "设备组属性--经度", required = false) @RequestParam(required = false) String longitude,
                                           @ApiParam(value = "设备组属性--纬度", required = false) @RequestParam(required = false) String latitude,
                                           @ApiParam(value = "设备组属性--负责人Id", required = false) @RequestParam(required = false) String responsibleUserId,

                                           @ApiParam(value = "设备属性--设备名", required = false) @RequestParam(required = false) String deviceName,
                                           @ApiParam(value = "设备属性--设备编号", required = false) @RequestParam(required = false) String deviceNo,
                                           @ApiParam(value = "设备属性--设备类型", required = false) @RequestParam(required = false) String deviceType,
                                           @ApiParam(value = "设备属性--出厂日期", required = false) @RequestParam(required = false) String productionDate,
                                           @ApiParam(value = "设备属性--商标", required = false) @RequestParam(required = false) String brand,
                                           @ApiParam(value = "设备属性--型号", required = false) @RequestParam(required = false) String model,
                                           @ApiParam(value = "设备属性--云盒Id", required = false) @RequestParam(required = false) String boxId,
                                           @ApiParam(value = "设备属性--序列号", required = false) @RequestParam(required = false) String serialNo,
                                           @ApiParam(value = "设备属性--协议", required = false) @RequestParam(required = false) String protocol,
                                           @ApiParam(value = "设备属性--输入相", required = false) @RequestParam(required = false) String inlines,
                                           @ApiParam(value = "设备属性--输出相", required = false) @RequestParam(required = false) String outlines,
                                           @ApiParam(value = "设备属性--配置数量 ", required = false) @RequestParam(required = false) Integer num,
                                           @ApiParam(value = "设备属性--扫码情况，数组，多个以逗号分隔", required = false) @RequestParam(required = false) String barCodes


    ) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");


            Map map = new HashMap<>();
            map.put("id", UUID.randomUUID().toString());
            map.put("parentId", parentId);
            map.put("parentGroupType", parentGroupType);
            map.put("groupType", groupType);
            map.put("groupName", groupName);
            map.put("address", address);
            map.put("longitude", longitude);
            map.put("latitude", latitude);
            map.put("responsibleUserId", responsibleUserId);
            map.put("deviceName", deviceName);
            map.put("deviceNo", deviceNo);
            map.put("deviceType", deviceType);
            map.put("productionDate", productionDate);
            map.put("brand", brand);
            map.put("model", model);
            map.put("boxId", boxId);
            map.put("serialNo", serialNo);
            map.put("protocol", protocol);
            map.put("inlines", inlines);
            map.put("outlines", outlines);
            map.put("num", num);
            map.put("barCodes", barCodes);


            deviceGroupService.insertDeviceGroup(map, loginUserId);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;

        } catch (BoxIdInvalidException be) {
            be.printStackTrace();
            throw be;
        } catch (BoxIDAlreadyExistException bee) {
            bee.printStackTrace();
            throw bee;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "修改设备组--服务商")
    @ResponseBody
    @RequestMapping(value = "/updateDeviceGroup", method = {RequestMethod.POST})
    public ResponseDbCenter updateDeviceGroup(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value = "Id", required = true) @RequestParam(required = true) String id,
                                              @ApiParam(value = "设备组属性--新增设备组名", required = false) @RequestParam(required = false) String groupName,
                                              @ApiParam(value = "设备组属性--地址", required = false) @RequestParam(required = false) String address,
                                              @ApiParam(value = "设备组属性--经度", required = false) @RequestParam(required = false) String longitude,
                                              @ApiParam(value = "设备组属性--纬度", required = false) @RequestParam(required = false) String latitude,
                                              @ApiParam(value = "设备组属性--负责人Id", required = false) @RequestParam(required = false) String responsibleUserId
    ) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");

            Map map = new HashMap<>();
            map.put("id", id);
            map.put("groupName", groupName);
            map.put("address", address);
            map.put("longitude", longitude);
            map.put("latitude", latitude);
            map.put("responsibleUserId", responsibleUserId);
            map.put("updatedBy", loginUserId);
            map.put("updateDate", new Date());
            deviceGroupService.updateDeviceGroup(map);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;

        } catch (BoxIdInvalidException be) {
            be.printStackTrace();
            throw be;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


//    @ApiOperation(value = "修改设备--技师端")
//    @ResponseBody
//    @RequestMapping(value = "/updateDevice", method = {RequestMethod.POST})
//    public ResponseDbCenter updateDevice(HttpServletRequest req, HttpServletResponse rsp,
//                                         @ApiParam(value = "Id", required = true) @RequestParam(required = true) String id,
//                                         @ApiParam(value = "设备属性--设备名", required = false) @RequestParam(required = false) String deviceName,
//                                         @ApiParam(value = "设备属性--设备编号", required = false) @RequestParam(required = false) String deviceNo,
//                                         @ApiParam(value = "设备属性--设备类型", required = false) @RequestParam(required = false) String deviceType,
//                                         @ApiParam(value = "设备属性--出厂日期", required = false) @RequestParam(required = false) String productionDate,
//                                         @ApiParam(value = "设备属性--商标", required = false) @RequestParam(required = false) String brand,
//                                         @ApiParam(value = "设备属性--型号", required = false) @RequestParam(required = false) String model,
//                                         @ApiParam(value = "设备属性--云盒Id", required = false) @RequestParam(required = false) String boxId,
//                                         @ApiParam(value = "设备属性--序列号", required = false) @RequestParam(required = false) String serialNo
//
//
//    ) throws Exception {
//        try {
//            String loginUserId = (String) req.getAttribute("loginUserId");
//
//            Map map = new HashMap<>();
//            map.put("id", id);
//            map.put("deviceName", deviceName);
//            map.put("deviceNo", deviceNo);
//            map.put("deviceType", deviceType);
//            map.put("productionDate", productionDate);
//            map.put("brand", brand);
//            map.put("model", model);
//            map.put("boxId", boxId);
//            map.put("serialNo", serialNo);
//            map.put("updatedBy", loginUserId);
//            map.put("updateDate", new Date());
//            deviceService.updateDevice(map);
//            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
//            return responseDbCenter;
//
//        }catch (BoxIDAlreadyExistException bee){
//            bee.printStackTrace();
//            throw bee;
//        }catch (Exception e) {
//            e.printStackTrace();
//            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
//        }
//    }


    @ApiOperation(value = "根据维修工单查询故障设备树---服务端，技师端")
    @ResponseBody
    @RequestMapping(value = "/applyDisableDeviceTree", method = {RequestMethod.POST})
    public ResponseDbCenter applyDisableDeviceTree(HttpServletRequest req, HttpServletResponse rsp,
                                                   @ApiParam(value = "工单Id", required = true) @RequestParam(required = true) String id,
                                                   @ApiParam(value = "标识位  0 表示维修单  1 表示报修单", required = true) @RequestParam(required = true) Integer flag
    ) throws Exception {
        try {
            List<String> deviceIdList = null;
            if (0 == flag) {
                deviceIdList = yDrepaireService.selectDeviceIdListByRepaireId(id);

            } else {
                deviceIdList = yDrepaireService.selectDeviceIdByApplyId(id);
            }

            DeviceGroupTreeNode treeNode = deviceGroupService.getDeviceGroupTreeByDevices(deviceIdList);

            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(treeNode);
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "根据维修工单查询故障设备树---客户端")
    @ResponseBody
    @RequestMapping(value = "/applyDisableDeviceTreeCustomer", method = {RequestMethod.POST})
    public ResponseDbCenter applyDisableDeviceTreeCustomer(HttpServletRequest req, HttpServletResponse rsp,
                                                           @ApiParam(value = "工单Id", required = true) @RequestParam(required = true) String id,
                                                           @ApiParam(value = "标识位  0 表示维修单  1 表示报修单", required = true) @RequestParam(required = true) Integer flag
    ) throws Exception {
        try {
            List<String> deviceIdList = null;
            if (0 == flag) {
                deviceIdList = yDrepaireService.selectDeviceIdListByRepaireId(id);

            } else {
                deviceIdList = yDrepaireService.selectDeviceIdByApplyId(id);
            }

            DeviceGroupTreeNode treeNode = deviceGroupService.getDeviceGroupTreeByDevicesFromCustomer(deviceIdList);

            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(treeNode);
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "查看设备组树--服务端")
    @ResponseBody
    @RequestMapping(value = "/deviceGroupTree", method = {RequestMethod.POST})
    public ResponseDbCenter deviceGroupTree(HttpServletRequest req, HttpServletResponse rsp,
                                            @ApiParam(value = "客户公司Id", required = false) @RequestParam(required = false) String companyId,
                                            @ApiParam(value = "搜索关键词", required = false) @RequestParam(required = false) String searchValue,
                                            @ApiParam(value = "是否显示设备  1 显示  0 不显示   默认显示", required = false) @RequestParam(required = false) Integer showDevice,
                                            @ApiParam(value = "当前第几页", required = true) @RequestParam(required = true) Integer currentPage,
                                            @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize
    ) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");
            Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
            Boolean showDeviceBoolean = true;
            if (showDevice == null) {
                showDeviceBoolean = true;
            } else {
                if (showDevice == 0) {
                    showDeviceBoolean = false;
                }
            }
            List<String> companyIdList = new ArrayList<>();
            Integer totalRows = 0;

            if (StringUtil.isBlank(companyId)) {

                    if (StringUtil.isBlank(searchValue)) {
                        companyIdList = deviceGroupService.searchCompanyIdByPcCompanyId((String) loginUserMap.get("pcCompanyId"), currentPage, pageSize);
                        totalRows = deviceGroupService.searchCompanyIdCountByPcCompanyId((String) loginUserMap.get("pcCompanyId"));
                    } else {
                        companyIdList = deviceGroupService.searchCompanyIdByDevice(searchValue, (String) loginUserMap.get("pcCompanyId"), currentPage, pageSize);
                        totalRows = deviceGroupService.searchCompanyIdCountByDevice(searchValue, (String) loginUserMap.get("pcCompanyId"));
                    }


            } else {
                companyIdList.add(companyId);
            }

            DeviceGroupTreeNode treeNode = null;

            treeNode = deviceGroupService.deviceTree1(companyIdList, (String) loginUserMap.get("pcCompanyId"), showDeviceBoolean);



            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(treeNode);
            responseDbCenter.setTotalRows(totalRows + "");
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "查看设备组树--技师端")
    @ResponseBody
    @RequestMapping(value = "/deviceGroupTreeTechnician", method = {RequestMethod.POST})
    public ResponseDbCenter deviceGroupTreeTechnician(HttpServletRequest req, HttpServletResponse rsp,
                                                      @ApiParam(value = "客户公司Id", required = false) @RequestParam(required = false) String companyId,
                                                      @ApiParam(value = "搜索关键词", required = false) @RequestParam(required = false) String searchValue,
                                                      @ApiParam(value = "是否显示设备  1 显示  0 不显示  默认显示", required = false) @RequestParam(required = false) Integer showDevice,
                                                      @ApiParam(value = "当前第几页", required = true) @RequestParam(required = true) Integer currentPage,
                                                      @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize
    ) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");
            Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);

            Boolean showDeviceBoolean = true;
            if (showDevice == null) {
                showDeviceBoolean = true;
            } else {
                if (showDevice == 0) {
                    showDeviceBoolean = false;
                }
            }
            List<String> companyIdList = new ArrayList<>();
            Integer totalRows = 0;

            if (StringUtil.isBlank(companyId)) {

                    companyIdList = deviceGroupService.searchCompanyIdByDevice(searchValue, (String) loginUserMap.get("pcCompanyId"), currentPage, pageSize);
                    totalRows = deviceGroupService.searchCompanyIdCountByDevice(searchValue, (String) loginUserMap.get("pcCompanyId"));

            } else {
                companyIdList.add(companyId);
            }

            DeviceGroupTreeNode treeNode = deviceGroupService.deviceTree3(companyIdList, loginUserId, showDeviceBoolean);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(treeNode);
            responseDbCenter.setTotalRows(totalRows + "");
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "查看设备组树--客户端")
    @ResponseBody
    @RequestMapping(value = "/deviceGroupTreeCustomer", method = {RequestMethod.POST})
    public ResponseDbCenter deviceGroupTree2(HttpServletRequest req, HttpServletResponse rsp,
                                             @ApiParam(value = "服务商Id", required = false) @RequestParam(required = false) String pcCompanyId,
                                             @ApiParam(value = "搜索关键词", required = false) @RequestParam(required = false) String searchValue,
                                             @ApiParam(value = "是否显示设备  1 显示  0 不显示 默认显示", required = false) @RequestParam(required = false) Integer showDevice,
                                             @ApiParam(value = "当前第几页", required = true) @RequestParam(required = true) Integer currentPage,
                                             @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize
    ) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");
            Boolean showDeviceBoolean = true;
            if (showDevice == null) {
                showDeviceBoolean = true;
            } else {
                if (showDevice == 0) {
                    showDeviceBoolean = false;
                }
            }
            List<String> pcCompanyIdList = new ArrayList<>();
            Integer totalRows = 0;
            List<String> companyIdList = companyContactorService.selectCompanyIdListByContactor(loginUserId, 1);
            if (StringUtil.isBlank(pcCompanyId)) {
                pcCompanyIdList = deviceGroupService.searchPcCompanyIdByDevice(searchValue, companyIdList, currentPage, pageSize);
                totalRows = deviceGroupService.searchPcCompanyIdCountByDevice(searchValue, companyIdList);

            } else {
                pcCompanyIdList.add(pcCompanyId);
            }

            DeviceGroupTreeNode treeNode = deviceGroupService.deviceTree2(pcCompanyIdList, loginUserId, showDeviceBoolean);

            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(treeNode);
            responseDbCenter.setTotalRows(totalRows + "");
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }



    @ApiOperation(value = "查看设备组树(不包含设备)--客户端")
    @ResponseBody
    @RequestMapping(value = "/deviceGroupTreeCustomerWithNoDevice", method = {RequestMethod.POST})
    public ResponseDbCenter deviceGroupTree4(HttpServletRequest req, HttpServletResponse rsp,
                                             @ApiParam(value = "服务商Id", required = false) @RequestParam(required = false) String pcCompanyId,
                                             @ApiParam(value = "搜索关键词", required = false) @RequestParam(required = false) String searchValue,
                                             @ApiParam(value = "当前第几页", required = true) @RequestParam(required = true) Integer currentPage,
                                             @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize
    ) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");

            List<String> pcCompanyIdList = new ArrayList<>();
            Integer totalRows = 0;
            List<String> companyIdList = companyContactorService.selectCompanyIdListByContactor(loginUserId, 1);
            if (StringUtil.isBlank(pcCompanyId)) {
                pcCompanyIdList = deviceGroupService.searchPcCompanyIdByDevice(searchValue, companyIdList, currentPage, pageSize);
                totalRows = deviceGroupService.searchPcCompanyIdCountByDevice(searchValue, companyIdList);

            } else {
                pcCompanyIdList.add(pcCompanyId);
            }

            DeviceGroupTreeNode treeNode = deviceGroupService.deviceTree4(pcCompanyIdList, loginUserId);

            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(treeNode);
            responseDbCenter.setTotalRows(totalRows + "");
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "删除设备组---服务端")
    @ResponseBody
    @RequestMapping(value = "/deleteDeviceGroup", method = {RequestMethod.POST})
    public ResponseDbCenter deleteDeviceGroup(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value = "id", required = false) @RequestParam(required = false) String id

    ) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");
            Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("status", 2);
            map.put("updatedBy", loginUserId);
            map.put("updateDate", new Date());


            List<String> deviceGroupIdList = deviceGroupService.selectChildDeviceGroupIdsByGroupId(id);
            List<String> pcCompanyIdList = new ArrayList<>();
            pcCompanyIdList.add((String) loginUserMap.get("pcCompanyId"));
            List<Map<String, Object>> deviceList = deviceService.selectDeviceByGroupIds(deviceGroupIdList, pcCompanyIdList, null, 1, 100000);

            List<String> boxIdList = new ArrayList<>();
            List<String> devivceIdList = new ArrayList<>();
            for (int i = 0; i < deviceList.size(); i++) {
                String boxId = (String) deviceList.get(i).get("boxId");
                if (StringUtils.isNotBlank(boxId)) {
                    boxIdList.add(boxId);
                }
                devivceIdList.add((String) deviceList.get(i).get("id"));
            }
            deviceService.deleteDeviceByIds(devivceIdList);
            deviceGroupService.updateDeviceGroup(map);

            apiCallFacadeService.deleteDevices(boxIdList);

            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;

        } catch (BoxIdInvalidException be) {
            be.printStackTrace();
            throw be;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "删除设备-----服务商")
    @ResponseBody
    @RequestMapping(value = "/deleteDevice", method = {RequestMethod.POST})
    public ResponseDbCenter deleteDevice(HttpServletRequest req, HttpServletResponse rsp,
                                         @ApiParam(value = "id", required = false) @RequestParam(required = false) String id

    ) throws Exception {
        try {
            deviceService.deleteDevice(id);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;
        } catch (BoxIdInvalidException be) {
            be.printStackTrace();
            throw be;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "查询设备组详情---通用")
    @ResponseBody
    @RequestMapping(value = "/deviceGroupDetail", method = {RequestMethod.POST})
    public ResponseDbCenter deviceGroupDetail(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value = "Id", required = false) @RequestParam(required = false) String groupId
    ) throws Exception {

        String loginUserId = (String) req.getAttribute("loginUserId");

        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
        Map<String, Object> map = deviceGroupService.selectDeviceGroupById(groupId);
        /**
         * 合同列表
         */
        List<Map<String, Object>> contractList = contractService.selectContractByGroupId(groupId, (String) loginUserMap.get("pcCompanyId"));
        map.put("contractList", contractList);
        /**
         * 当前维修工单
         */
        List<Map<String, Object>> currentRepaireList = yDrepaireService.selectCurrentRepaireByGroupId(groupId, (String) loginUserMap.get("pcCompanyId"));
        map.put("currentRepaireList", currentRepaireList);


        Integer historyTaskCount = inspectionTaskService.selectHistoryInspectionTaskCountByGroupId(groupId, null, null);
        Integer historyRepaireCount = repaireService.selectHistoryRepaireCountByGroupId(groupId, null, null);
        /**
         * 历史记录
         */
        map.put("historyTaskCount", historyTaskCount + historyRepaireCount);


        /**
         * 设备数
         */
        List<String> groupIds = new ArrayList<>();
        groupIds.add(groupId);

        List<String> pcCompanyIdList = new ArrayList<>();
        pcCompanyIdList.add((String) loginUserMap.get("pcCompanyId"));
        Integer deviceCount = deviceService.countDeviceForListDevice(null, new ArrayList<String>() {{
            add((String) map.get("companyId"));
        }}, groupIds, null, null, null);
        map.put("deviceCount", deviceCount);

        /**
         * 客户联系人
         */
        List<Map<String, Object>> contactorList = companyContactorService.selectCompanyContactorByCompanyId((String) map.get("companyId"), (String) loginUserMap.get("pcCompanyId"), null, 1, 10000);
        map.put("contactorList", contactorList);


        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(map);
        return responseDbCenter;
    }


    @ApiOperation(value = "查询某一个客户下的建筑列表---服务端")
    @ResponseBody
    @RequestMapping(value = "/selectAllBuildingsByCompanyId", method = {RequestMethod.POST})
    public ResponseDbCenter selectAllBuildingsByCompanyId(HttpServletRequest req, HttpServletResponse rsp,
                                                          @ApiParam(value = "客户公司Id", required = false) @RequestParam(required = false) String companyId
    ) throws Exception {

        String loginUserId = (String) req.getAttribute("loginUserId");

        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
        List<Map<String, Object>> deviceGroupList;

        deviceGroupList = deviceGroupService.selectAllBuildingsByCompanyId((String) loginUserMap.get("pcCompanyId"), companyId);



        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(deviceGroupList);
        return responseDbCenter;
    }


    @ApiOperation(value = "设备列表(列出客户)")
    @ResponseBody
    @RequestMapping(value = "/listByCompany", method = RequestMethod.POST)
    public ResponseDbCenter listByCompany(HttpServletRequest request, HttpServletResponse response,
                                          @ApiParam(value = "筛选大中小型客户:大>1, 中>2, 小>3", required = false) @RequestParam(required = false) String userLevel,
                                          @ApiParam(value = "客户名称搜索", required = false) @RequestParam(required = false) String companyName) throws Exception {
        try {
            String loginUserId = (String) request.getAttribute("loginUserId");
            Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
            String roleId = (String) loginUserMap.get("roleId");


            //用户以服务商形式登录时,取出用户的pcCompanyId,再通过pcCompanyId在company表中得到对应companyId
            //再到device表中用pcCompanyId和companyId检索结果;
            if ("2".equals(roleId)) {

                String pcCompanyId = (String) loginUserMap.get("pcCompanyId");
                List<Map<String, Object>> totalCompanyMap = companyService.findCompanyByPcCompanyId(pcCompanyId, userLevel, companyName);
                getCompanyDeviceCount(totalCompanyMap);
                ResponseDbCenter responseDbCenter = new ResponseDbCenter();
                responseDbCenter.setResModel(totalCompanyMap);
                responseDbCenter.setTotalRows(totalCompanyMap.size() + "");
                return responseDbCenter;
            }

            //用户以技师身份登录时,技师查看的设备来源有一个(
            //          1.服务商设置设备组时,指定技师负责人
            else if ("3".equals(roleId)) {
                //从deviceGroup和company表中获取companyId(未除重), groupId, companyName, userLevel
                String pcCompanyId = (String) loginUserMap.get("pcCompanyId");
                List<Map<String, Object>> companyIdToGroupIdList = deviceGroupService.selectGroupIdAndCompanyIdByUserId(loginUserId, userLevel, companyName, pcCompanyId);

                //为了在结果数据中给company除重建立的hashMap(companyId: 在ArrayList中的index)
                Map<String, Integer> companyIdToIndex = new HashMap<>();

                //保存结果的ArrayList
                List<Map<String, Object>> result = new ArrayList<>();

                //遍历原始数据
                for (Map<String, Object> deviceGroup : companyIdToGroupIdList
                        ) {
                    String companyId = (String) deviceGroup.get("companyId");

                    //判断companyId是否已经存在
                    if (companyIdToIndex.containsKey(companyId)) {

                        //获取ArrayList中对应company数据所在的Map索引
                        Integer index = companyIdToIndex.get(companyId);

                        //获取groupId,便于在设备表中索引
                        String groupId = (String) deviceGroup.get("groupId");
                        String groupIdString = result.get(index).get("groupIdString") + "," + groupId;
                        result.get(index).put("groupIdString", groupIdString);

                        //利用companyId和groupId获取全部设备数量,保存到结果Map
                        Integer deviceCount = deviceService.countDeviceByCompanyIdAndGroupId(companyId, groupId).getDeviceNum() +
                                (Integer) result.get(index).get("deviceCount");
                        result.get(index).put("deviceCount", deviceCount);

                        //获取带云盒设备数量,保存到结果map
                        Integer deviceWithBoxCount = deviceService.countDeviceByCompanyIdAndGroupId(companyId, groupId).getBoxNum() +
                                (Integer) result.get(index).get("deviceWithBoxCount");
                        result.get(index).put("deviceWithBoxCount", deviceWithBoxCount);
                    } else {
                        String groupId = (String) deviceGroup.get("groupId");

                        //在结果列表中初始化
                        result.add(new HashMap<String, Object>() {{
                            this.put("companyId", companyId);
                            this.put("groupIdString", deviceGroup.get("groupId"));
                            this.put("companyName", deviceGroup.get("companyName"));
                            this.put("userLevel", deviceGroup.get("userLevel"));
                            this.put("deviceCount", deviceService.countDeviceByCompanyIdAndGroupId(companyId, groupId).getDeviceNum());
                            this.put("deviceWithBoxCount", deviceService.countDeviceByCompanyIdAndGroupId(companyId, groupId).getBoxNum());
                        }});

                        companyIdToIndex.put(companyId, result.size() - 1);
                    }
                }
                ResponseDbCenter responseDbCenter = new ResponseDbCenter();
                responseDbCenter.setResModel(result);
                responseDbCenter.setTotalRows(result.size() + "");
                return responseDbCenter;
            }

            //用户以客户身份登录时,可以查看到客户下所有设备
            else {
                return new ResponseDbCenter();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }

    }


    //遍历得到设备计数
    private void getCompanyDeviceCount(List<Map<String, Object>> totalCompanyMap) {
        for (Map<String, Object> companyMap : totalCompanyMap) {
            Integer deviceCount = deviceService.countByCompanyId((String) companyMap.get("id"));
            Integer boxCount = deviceService.countByCompanyIdAndBoxId((String) companyMap.get("id"));
            companyMap.put("deviceCount", deviceCount);
            companyMap.put("boxCount", boxCount);
        }
    }

    private static Map<Object, String> periodToString = new HashMap<Object, String>() {{
        this.put(1, "每天");
        this.put(2, "每周");
        this.put(3, "每月");
        this.put(4, "季度");
        this.put(5, "每年");
        this.put(6, "");
    }};

    @ApiOperation("设备查看:无boxId")
    @ResponseBody
    @RequestMapping(value = "/device/detail", method = RequestMethod.GET)
    public ResponseDbCenter deviceDetail(HttpServletRequest request, HttpServletResponse response,
                                         @ApiParam(value = "设备Id", required = true) @RequestParam(required = true) String deviceId) throws Exception {

        try {

            //获取设备基本信息
            Map<String, Object> deviceMap = deviceService.selectDeviceByDeviceId(deviceId);
            if (deviceMap.get("boxId") != null) {
                Map<String, String> protocolAndType = deviceService.getProtocolAndType((String) deviceMap.get("boxId"));
                deviceMap.put("devtype", protocolAndType.get("devtype"));
                deviceMap.put("protocol", protocolAndType.get("protocol"));
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Timestamp createDate = (Timestamp) deviceMap.get("createDate");
            if (createDate != null) {
                deviceMap.put("createDate", format.format(createDate));
            }
            Timestamp updateDate = (Timestamp) deviceMap.get("updateDate");
            if (updateDate != null) {
                deviceMap.put("updateDate", format.format(updateDate));
            }

            //获取巡检计划信息
            // (起始时间,结束时间,保养周期, 维护工程师)
            //1.通过groupId获取inspectionPlanId, 通过inspectionPlanId获取
            String groupId = (String) deviceMap.get("groupId");
            Map<String, Object> inspectionMap = inspectionPlanService.selectInspectionPlanByGroupId(groupId);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //维保开始时间
            if (inspectionMap == null) {
                inspectionMap = new HashMap<String, Object>();
                deviceMap.put("countDownTime", "");
            } else {
                //维保倒计时
                deviceMap.put("countDownTime", deviceService.getCountDownTime((String) inspectionMap.get("id")));
            }
            deviceMap.put("inspectionStartTime", inspectionMap.getOrDefault("startTime", ""));
            //维保结束时间
            deviceMap.put("inspectionEndTime", inspectionMap.getOrDefault("endTime", ""));
            //维保周期
            Integer inspectionPeriod = (Integer) inspectionMap.getOrDefault("period", 6);
            deviceMap.put("inspectionPeriod", periodToString.getOrDefault(inspectionPeriod, ""));
            //维护工程师
            String userId = null;
            String parentId = groupId;
            while (userId == null) {
                Map<String, String> userAndParentId = deviceService.getUserAndParentId(parentId);
                userId = userAndParentId.get("responsibleUserId");
                parentId = userAndParentId.get("parentId");
            }
            deviceMap.put("responsibleUserId", userId);
            deviceMap.put("responsibleUserName", userService.selectUserById(userId).get("uname"));

            //获取上次巡检信息
            Map<String, Object> inspectionTaskMap = inspectionPlanService.selectLastInspectionTask((String) inspectionMap.get("id"));
            if (inspectionTaskMap == null) {
                deviceMap.put("lastInspectionTime", "");
                deviceMap.put("lastResponsibleUserName", "");
            } else {
                Timestamp lastInspectionTime = (Timestamp) inspectionTaskMap.get("completionDate");
                deviceMap.put("lastInspectionTime", dateFormat.format(lastInspectionTime));
                deviceMap.put("lastResponsibleUserName", userService.selectUserById(
                        (String) inspectionTaskMap.get("completedBy")).get("uname"));
            }

            List<Map<String, Object>> repairCurrent = yDrepaireService.selectCurrentOrder(deviceId);
            deviceMap.put("repairCurrent", repairCurrent);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(deviceMap);
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

    @ApiOperation(value = "设备历史巡检数据")
    @ResponseBody
    @RequestMapping(value = "/inspectionHistory", method = RequestMethod.POST)
    public ResponseDbCenter inspectionHistory(HttpServletRequest request, HttpServletResponse response,
                                              @ApiParam(value = "设备Id,设备组Id", required = true) @RequestParam(required = true) String objectId,
                                              @ApiParam(value = "类型,1 设备   2 设备组", required = true) @RequestParam(required = true) Integer ttype,
                                              @ApiParam(value = "开始时间", required = false) @RequestParam(required = false) String startDate,
                                              @ApiParam(value = "结束时间", required = false) @RequestParam(required = false) String endDate,
                                              @ApiParam(value = "当前页面", required = true) @RequestParam(required = true) Integer currentPage,
                                              @ApiParam(value = "页面大小", required = true) @RequestParam(required = true) Integer pageSize
    )
            throws Exception {
        try {

            //如果设备没有巡检计划,返回空
            List<Map<String, Object>> resultMapList = null;
            Integer totalRows = 0;

            if (ttype == 1) {
                //获取设备所在组信息,通过设备所在组,获取巡检计划信息;
                Map<String, Object> deviceMap = deviceService.selectDeviceByDeviceId(objectId);
                String groupId = (String) deviceMap.get("groupId");
                List<Map<String, Object>> inspectionList = inspectionPlanService.selectAllInspectionPlanByGroupId(groupId);
                resultMapList = inspectionTaskService.selectFinishedInspectionTaskByInspectionPlan
                        (inspectionList, startDate, endDate, currentPage, pageSize);
                totalRows = inspectionTaskService.countFinishedInspectionTaskByInspectionPlan(inspectionList, startDate, endDate);
            } else {
                resultMapList = inspectionTaskService.selectHistoryInspectionTaskByGroupId(objectId, startDate, endDate, currentPage, pageSize);
                totalRows = inspectionTaskService.selectHistoryInspectionTaskCountByGroupId(objectId, startDate, endDate);
            }
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(resultMapList);
            responseDbCenter.setTotalRows(totalRows + "");
            return responseDbCenter;
        } catch (Exception e) {
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

    @ApiOperation(value = "获取历史维修记录")
    @ResponseBody
    @RequestMapping(value = "/repairHistory", method = RequestMethod.POST)
    public ResponseDbCenter repairHistory(HttpServletRequest request, HttpServletResponse response,
                                          @ApiParam(value = "设备id,或设备组Id", required = true) @RequestParam(required = true) String objectId,
                                          @ApiParam(value = "类型  1  设备   2 设备组", required = true) @RequestParam(required = true) Integer ttype,
                                          @ApiParam(value = "开始时间 格式如 2012-10-10 10:10:10", required = false) @RequestParam(required = false) String startDate,
                                          @ApiParam(value = "结束时间 格式如 2012-10-10 10:10:10", required = false) @RequestParam(required = false) String endDate,
                                          @ApiParam(value = "当前页面", required = true) @RequestParam(required = true) Integer currentPage,
                                          @ApiParam(value = "页面大小", required = true) @RequestParam(required = true) Integer pageSize
    )
            throws Exception {
        try {
            List<Map<String, Object>> repairList = null;
            Integer totalRows = 0;
            if (ttype == 1) {
                repairList = deviceService.selectFinishedRepairListByDeviceId(objectId, startDate, endDate, currentPage, pageSize);
                totalRows = deviceService.countFinishedRepairListByDeviceId(objectId, startDate, endDate);
            } else {
                repairList = repaireService.selectHistoryRepaireByGroupId(objectId, startDate, endDate, currentPage, pageSize);
                totalRows = repaireService.selectHistoryRepaireCountByGroupId(objectId, startDate, endDate);
            }
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(repairList);
            responseDbCenter.setTotalRows(totalRows + "");
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

    @ApiOperation(value = "设备列表筛选功能")
    @ResponseBody
    @RequestMapping(value = "/listDevice", method = RequestMethod.POST)
    public ResponseDbCenter testListDevice(HttpServletRequest request, HttpServletResponse response,
                                           @ApiParam(value = "当前页面", required = true) @RequestParam(required = true) Integer currentPage,
                                           @ApiParam(value = "页面大小", required = true) @RequestParam(required = true) Integer pageSize,
                                           @ApiParam(value = "筛选：设备状态，如果传了这个参数，就不要再传其他筛选参数{异常，离线}", required = false)
                                           @RequestParam(required = false) String status,
                                           @ApiParam(value = "筛选：客户id", required = false) @RequestParam(required = false) String companyId,
                                           @ApiParam(value = "筛选：设备类型", required = false) @RequestParam(required = false) String deviceType,
                                           @ApiParam(value = "设备组Id", required = false) @RequestParam(required = false) String groupIdString,
                                           @ApiParam(value = "设备名称筛选", required = false) @RequestParam(required = false) String deviceName)
            throws Exception {
        String loginUserId = (String) request.getAttribute("loginUserId");
        Map<String, Object> userMap = userService.selectUserById(loginUserId);
        String roleId = (String) userMap.get("roleId");
        String[] companyIdArray = new String[0];
        if (StringUtils.isNotBlank(companyId)) {
            companyIdArray = companyId.split(",");
        }
        String[] groupIdArray = new String[0];
        if (StringUtils.isNotBlank(groupIdString)) {
            groupIdArray = groupIdString.split(",");
        }
        List<Map<String, Object>> deviceMapList;
        List<Map<String, Object>> result;
        Integer totalRow = 0;
        try {
            if (roleId.equals("2")) {
                String pcCompanyId = (String) userMap.get("pcCompanyId");
                if (StringUtils.isNotBlank(status)) {
                    List<String> boxIdList = deviceService.getRedisDataByPcCompanyIdAndStatus(pcCompanyId, StatusMap.get(status), currentPage, pageSize);
                    deviceMapList = deviceService.filterDeviceForListDevice(
                            pcCompanyId, //服务商公司Id
                            null, //客户公司IdList，用户在客户端登录的时候，为list，在当做条件进行筛选时只有一个
                            null, //
                            boxIdList,        //从redis获取的boxIdList
                            null,  //设备名字&设备类型&客户 的筛选与状态互斥
                            null,
                            currentPage,
                            pageSize);
                    //需要将status加入到结果map中 for map in list:  { map.put("boxStatus", StatusMap.get(status)}
                    result = deviceService.getStatusFilter(deviceMapList, null);
                    totalRow = deviceService.getRedisDataCountByPcCompanyIdAndStatus(pcCompanyId, StatusMap.get(status));
                } else {
                    deviceMapList = deviceService.filterDeviceForListDevice(
                            pcCompanyId,
                            Arrays.asList(companyIdArray),
                            Arrays.asList(groupIdArray),
                            null,
                            deviceName,
                            deviceType,
                            currentPage,
                            pageSize);
                    totalRow = deviceService.countDeviceForListDevice(
                            pcCompanyId,
                            Arrays.asList(companyIdArray),
                            Arrays.asList(groupIdArray),
                            null,
                            deviceName,
                            deviceType);

                    //需要将List中所有的boxId到redis中拿一次，判断状态；
                    result = deviceService.getStatusFilter(deviceMapList, null);
                }
            } else if (roleId.equals("3")) {
                String pcCompanyId = (String) userMap.get("pcCompanyId");
                List<String> groupIdList = deviceGroupService.getGroupIdByUserId(loginUserId);
                if (StringUtils.isNotBlank(status)) {
                    deviceMapList = deviceService.filterDeviceForListDevice(
                            pcCompanyId,
                            Arrays.asList(companyIdArray),
                            groupIdList,
                            null,
                            deviceName,
                            deviceType,
                            null, //技师端应该取出全部设备
                            null);   //然后在外面筛选
                    List<Map<String, Object>> allData = deviceService.getStatusFilter(deviceMapList, status);
                    totalRow = allData.size();
                    Integer start = (currentPage - 1) * pageSize;
                    result = allData.stream().skip(start).limit(pageSize).collect(Collectors.toList());
                } else {
                    deviceMapList = deviceService.filterDeviceForListDevice(
                            pcCompanyId,
                            Arrays.asList(companyIdArray),
                            groupIdList,
                            null,
                            deviceName,
                            deviceType,
                            currentPage,    //如果没有给定状态条件
                            pageSize);      //在数据库分页,然后得到数据
                    //这里计算最大长度，应该选择数据库中能够检索到的条目；
                    totalRow = deviceService.countDeviceForListDevice(
                            pcCompanyId,
                            Arrays.asList(companyIdArray),
                            groupIdList,
                            null,
                            deviceName,
                            deviceType);
                    result = deviceService.getStatusFilter(deviceMapList, null);
                }

            } else if (roleId.equals("4")) {
                List<String> companyIdList = companyContactorService.selectCompanyIdListByContactor(loginUserId, 1);
                if (StringUtils.isNotBlank(status)) {
                    deviceMapList = deviceService.filterDeviceForListDevice(
                            null,
                            companyIdList,
                            null,
                            null,
                            deviceName,
                            deviceType,
                            null, //客户应该取出全部设备
                            null);   //然后在外面筛选
                    List<Map<String, Object>> allData = deviceService.getStatusFilter(deviceMapList, status);
                    totalRow = allData.size();
                    Integer start = (currentPage - 1) * pageSize;
                    result = allData.stream().skip(start).limit(pageSize).collect(Collectors.toList());
                } else {
                    deviceMapList = deviceService.filterDeviceForListDevice(
                            null,
                            companyIdList,
                            Arrays.asList(groupIdArray),
                            null,
                            deviceName,
                            deviceType,
                            currentPage,
                            pageSize);
                    totalRow = deviceService.countDeviceForListDevice(
                            null,
                            companyIdList,
                            Arrays.asList(groupIdArray),
                            null,
                            deviceName,
                            deviceType);
                    result = deviceService.getStatusFilter(deviceMapList, null);
                }
            } else {
                deviceMapList = deviceService.filterDeviceForListDevice(
                        null,
                        Arrays.asList(companyIdArray),
                        Arrays.asList(groupIdArray),
                        null,
                        deviceName,
                        deviceType,
                        currentPage,
                        pageSize);
                totalRow = deviceService.countDeviceForListDevice(
                        null,
                        Arrays.asList(companyIdArray),
                        Arrays.asList(groupIdArray),
                        null,
                        deviceName,
                        deviceType);
                result = deviceService.getStatusFilter(deviceMapList, null);
            }

            //对结果进行处理,首先遍历result,1:取出含有boxId的设备;2:添加到boxId列表;
            // 3:没有boxId的直接添加进结果列表/有boxId的添加进另一个map列表;
            // 4:利用boxId,
            List<Map<String, Object>> finalResult = new LinkedList<>();
            List<String> boxIdList = new LinkedList<>();
            Map<String, Map<String, Object>> boxId2DeviceMap = new HashMap<>();
            for (Map<String, Object> device :
                    result) {
                String boxId = (String) device.getOrDefault("boxId", "");
                if (boxId.equals("")) {
                    finalResult.add(device);
                } else {
                    boxIdList.add(boxId);
                    boxId2DeviceMap.put(boxId, device);
                }
            }
            Map<String, Object> apiCall = apiCallFacadeService.getDeviceConfByBoxIdList(boxIdList);
            Iterator iterator = boxId2DeviceMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry e = (Map.Entry) iterator.next();
                String key = (String) e.getKey();
                Map<String, Object> value = (Map) e.getValue();
                Map<String, Object> temp = (Map) apiCall.getOrDefault(key, new HashMap<>());
                value.putAll(temp);
                finalResult.add(value);
            }
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(finalResult);
            responseDbCenter.setTotalRows(totalRow + "");
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


    private static Map<String, Integer> StatusMap = new HashMap<String, Integer>() {{
        put("异常", 1);
        put("离线", 2);
    }};

    @ApiOperation(value = "当前工单查看入口")
    @ResponseBody
    @RequestMapping(value = "/currentOrder", method = RequestMethod.POST)
    public ResponseDbCenter currentOrder(HttpServletRequest request, HttpServletResponse response,
                                         @ApiParam(value = "设备Id", required = true) @RequestParam(required = true) String deviceId) throws Exception {
        //获取当前巡检,和当前维修
        try {
            List<Map<String, Object>> repairCurrent = yDrepaireService.selectCurrentOrder(deviceId);
            List<Map<String, Object>> inspectionTaskCurrent = inspectionTaskService.selectCurrentInspection(deviceId);
            Map<String, Object> result = new HashMap<>();
            result.put("repairCurrent", repairCurrent);
            result.put("inspectionTaskCurrent", inspectionTaskCurrent);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(result);
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }
}
