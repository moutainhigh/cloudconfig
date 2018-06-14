package com.xkd.controller;

import java.lang.Object;

import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.*;
import com.xkd.utils.DateUtils;
import com.xkd.utils.RedisCacheUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jdk.nashorn.internal.codegen.MapCreator;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

import com.xkd.utils.JedisPoolUtils;

/**
 * Create by @author: wanghuijiu; @date: 18-3-5;
 */

@Api(description = "监控中心计数相关接口")
@Controller
@RequestMapping(value = "/center")
@Transactional
public class MonitoringCenterController {

    @Autowired
    private UserService userService;

    @Autowired
    private YDrepaireService repaireService;


    @Autowired
    private RedisCacheUtil<String> redisCacheUtil;

    @Autowired
    private InspectionTaskService inspectionTaskService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private DcYdReportService dcYdReportService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private MsgDeviceCurrService msgDeviceCurrService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private DeviceGroupService deviceGroupService;

    @Autowired
    private CompanyContactorService companyContactorService;

    @Autowired
    private MsgUserRecordService msgUserRecordService;

    @Autowired
    private DepartmentService departmentService;

    private static String[] ABNORMAL_STATUS = {"2", "3", "4", "5", "8", "10", "12", "14", "16"};

    @ApiOperation(value = "服务端/技师端/客户端:得到监控中心计数")
    @ResponseBody
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public ResponseDbCenter getAllCount(HttpServletRequest request, HttpServletResponse response,
                                        @ApiParam(value = "app端随便传个值", required = false) @RequestParam(required = false) Integer appFlag) throws Exception {
        try {

            String loginUserId = (String) request.getAttribute("loginUserId");
            Map<String, Object> userMap = userService.selectUserById(loginUserId);
            Map<String, Integer> result = new HashMap<>();
            String roleId = (String) userMap.get("roleId");
            if ("2".equals(roleId)) {
                String pcCompanyId = (String) userMap.get("pcCompanyId");
                //获取未受理维修单数量
                //repair_apply表,对pcCompany和status=0(未受理)计数
                Map<String, Object> applyParamMap = new HashMap<String, Object>() {{
                    this.put("pcCompanyId", pcCompanyId);
                    this.put("statusFlag", "0");
                }};
                Integer repairApplyCount = repaireService.selectTotalRepaireApplys(applyParamMap);
                result.put("repairApplyCount", repairApplyCount);

                //获取设备告警数量
                Map<String,String> boxStatusMap = redisCacheUtil.getCacheMap("box_status_table");

                if (appFlag==null){
                    Integer resultCount = deviceService.countWarningDeviceByUserId(loginUserId);
                    result.put("abnormalDevice", resultCount);
                }
//                List<Map<String, Object>> totalCompanyMap = companyService.findCompanyByPcCompanyId(pcCompanyId, null, null);
//                List<Map<String, Object>> totalDeviceList = new LinkedList<>();
//                if (appFlag==null){
//                    for (Map<String, Object> companyMap : totalCompanyMap
//                            ) {
//                        String companyId = (String) companyMap.get("id");
//                        List<Map<String, Object>> deviceList = deviceService.filterDeviceForMonitor(companyId, 1);
//                        if (deviceList != null) {
//                            totalDeviceList.addAll(deviceList);
//                        }
//                    }
//
//                    Integer count = 0;
//                    for (Map<String, Object> deviceMap : totalDeviceList
//                            ) {
//                        String boxStatus = boxStatusMap.getOrDefault((String) deviceMap.get("boxId"), "0");
//                        if (Arrays.asList(ABNORMAL_STATUS).contains(boxStatus)) {
//                            count++;
//                        }
//                    }
//                    result.put("abnormalDevice", count);
//                }

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                //今日维修任务
                Map<String, Object> paramMap = new HashMap<String, Object>() {{
                    this.put("pcCompanyId", pcCompanyId);
                    this.put("dueTimeStartDate", DateUtils.dateToString(new Date(),"yyyy-MM-dd"));
                    this.put("dueTimeEndDate", DateUtils.dateToString(new Date(),"yyyy-MM-dd"));
                }};
                Integer todayRepairCount = repaireService.selectTotalSelectRepaires(paramMap);
                result.put("todayRepairCount", todayRepairCount);

                //未完成维修
                //repair表,对pcCompany和status in (1,2,3)
                paramMap.put("completeFlag", "0");
                Integer repairCount = repaireService.selectTotalSelectRepaires(paramMap);
                result.put("repairCount", repairCount);

                //今日巡检
                //inspection_task表, pcCompanyId和status不为1时,是今天全部巡检,为1时是未完成巡检
                Integer inspectionTaskCount = inspectionTaskService.searchInspectionTaskCount
                        (pcCompanyId, null, null, null, format.format(new Date()), null, null);
                result.put("inspectionTaskCount", inspectionTaskCount);

                //未完成巡检
                Integer unFinishedInspectionTask = inspectionTaskService.searchInspectionTaskCount
                        (pcCompanyId, null, null, null, null, null, 0);
                result.put("unFinishedInspectionTask", unFinishedInspectionTask);

                //合同即将到期(pc端筛选近一个月,app端筛选近0-30/30-60/60-90)
                //contract表,筛选最近一个月内到期的合同
                Integer expiringContractCount = contractService.countContractByPcCompanyId(pcCompanyId, 0, 30);
                result.put("expiringContractCount", expiringContractCount);
                if (appFlag != null) {

                    //得到额外的合同计数
                    Map<String,Object> map=contractService.selectContractStatistic(pcCompanyId);
                    result.put("dueCount",(Integer) map.get("dueCount"));
                    result.put("oneMonthCount",(Integer) map.get("oneMonthCount"));
                    result.put("twoMonthCount",(Integer) map.get("twoMonthCount"));
                    result.put("threeMonthCount",(Integer) map.get("threeMonthCount"));

                    //得到额外的设备计数
                    List<Map<String, Object>> deviceMapList = deviceService.getDeviceListByPcCompanyId(pcCompanyId);
                    Map<String, Integer> countMap = getDeviceCount(deviceMapList, boxStatusMap);
                    result.put("normal", countMap.get("normal")+countMap.get("notConfigured"));
                    result.put("abnormal", countMap.get("abnormal"));
                    result.put("offline", countMap.get("offline"));
                }

                //员工日报
                //通过pcCompanyId搜索报表, 查找出昨天的报表
                Integer reportCount = dcYdReportService.countReportByPcCompanyIdAndDate(pcCompanyId);
                result.put("reportCount", reportCount);
            } else if ("3".equals(roleId)) {
                String pcCompanyId = (String) userService.selectUserById(loginUserId).get("pcCompanyId");
                List<String> groupIdList = deviceGroupService.getGroupIdByUserId(loginUserId);
                List<Map<String, Object>> deviceMapList =  deviceService.filterDeviceForListDevice(
                        pcCompanyId,
                        null,
                        groupIdList,
                        null,
                        null,
                        null,
                        null,
                        null);

                //先取到用户对应的所有设备,再利用boxId到redis中读取数据判断状态计数,因为技师的设备来源为添加群组时设定负责人,所以需要找到companyId和groupId对应;
                //获取redis中存储的box_status_table
                Map<String,String> boxStatusMap = redisCacheUtil.getCacheMap("box_status_table");
                result.putAll(getDeviceCount(deviceMapList, boxStatusMap));
                int repair , inspection;
                repair = repaireService.countUserRepair(pcCompanyId, loginUserId);
                inspection = inspectionTaskService.countUserInspection(pcCompanyId,  loginUserId);
                result.put("repair", repair);
                result.put("inspection", inspection);
            } else if ("4".equals(roleId)) {

                List<String> userCompanyId = companyContactorService.getCompanyIdByuserId(loginUserId);
                List<Map<String, Object>> deviceList = deviceService.getDeviceByCompanyId(userCompanyId);
                Map<String,String> boxStatusMap = redisCacheUtil.getCacheMap("box_status_table");
                result.putAll(getDeviceCount(deviceList, boxStatusMap));
                Integer repair = repaireService.countCompanyRepair(loginUserId);
                result.put("repair", repair);
            }
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(result);
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

    private static Map<String, Integer> getDeviceCount(List<Map<String, Object>> deviceList, Map<String, String> boxStatusMap) {
        Map<String, Integer> result = new HashMap<>();
        int notConfigured = 0, abnormal = 0, normal = 0, offline = 0;
        for (Map<String, Object> device : deviceList
                ) {
            String boxId = (String) device.getOrDefault("boxId", "");
            Integer status = judgeBoxStatus(boxId, boxStatusMap);
            if (status == null) {
                notConfigured++;
            } else {
                switch (status) {
                    case 1:
                        abnormal++;
                        break;
                    case 2:
                        normal++;
                        break;
                    case 3:
                        offline++;
                        break;
                }
            }
        }
        result.put("notConfigured", notConfigured);
        result.put("abnormal", abnormal);
        result.put("normal", normal);
        result.put("offline", offline);
        return result;
    }

    private static Integer judgeBoxStatus(String boxId, Map<String, String> boxStatusTable) {
        if (StringUtils.isBlank(boxId)) {
            return null;
        }
        String boxStatus = boxStatusTable.getOrDefault(boxId, "0");
        if (Arrays.asList(ABNORMAL_STATUS_LIST).contains(boxStatus)) {
            System.out.println(boxId);
            return 1;
        } else if (Arrays.asList(NORMAL_STATUS).contains(boxStatus)) {
            return 2;
        } else if (Arrays.asList(OFFLINE_STATUS).contains(boxStatus)) {
            return 3;
        } else return null;
    }


    private static String[] ABNORMAL_STATUS_LIST = {"0","2", "3", "4", "8", "10", "12", "14", "16"};
    private static String[] NORMAL_STATUS = {"1", "7", "9", "11", "13", "15"};
    private static String[] OFFLINE_STATUS = {"5"};

    @ApiOperation("消息忽略")
    @ResponseBody
    @RequestMapping(value = "/ignoreMessage", method = RequestMethod.POST)
    public ResponseDbCenter ignoreMessage(HttpServletRequest request, HttpServletResponse response,
                                          @ApiParam(value = "消息id", required = true) @RequestParam(required = true) String msgId) {
        try {
            msgDeviceCurrService.ignoreMessage(msgId);
            return new ResponseDbCenter();

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

    @ApiOperation("监控中心消息列表")
    @ResponseBody
    @RequestMapping(value = "/deviceHistoryMessage", method = RequestMethod.POST)
    public ResponseDbCenter deviceHistoryMessage(HttpServletRequest request, HttpServletResponse response,
                                                 @ApiParam(value = "当前页面", required = true) @RequestParam(required = true) Integer currentPage,
                                                 @ApiParam(value = "页面大小", required = true) @RequestParam(required = true) Integer pageSize) throws Exception {
        try {

            //通过userId搜索
            String loginUserId = (String) request.getAttribute("loginUserId");
            List<Map<String, Object>> recordList = msgUserRecordService.selectRecordListByUserId(loginUserId, currentPage, pageSize);
            Integer totalCount = msgUserRecordService.countRecordByUserId(loginUserId);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(recordList);
            responseDbCenter.setTotalRows(totalCount + "");
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


    @ApiOperation(value = "获取快到期合同数量以及内容")
    @ResponseBody
    @RequestMapping(value = "/contract", method = RequestMethod.POST)
    public ResponseDbCenter selectContractForMonitor(HttpServletRequest request, HttpServletResponse response,
                                                     @ApiParam(value = "当前页面", required = true) @RequestParam(required = true) Integer currentPage,
                                                     @ApiParam(value = "页面大小", required = true) @RequestParam(required = true) Integer pageSize)
            throws Exception {
        try {
            //通过userId搜索contractor表
            String loginUserId = (String) request.getAttribute("loginUserId");
            Map<String, Object> userMap = userService.selectUserById(loginUserId);
            String pcCompanyId = (String) userMap.get("pcCompanyId");
            Integer expiringContractCount = contractService.countContractByPcCompanyId(pcCompanyId, 0, 30);
            List<Map<String, Object>> expiringContracts = contractService.listExpiringContractByPcCompanyId(pcCompanyId, currentPage, pageSize);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(expiringContracts);
            responseDbCenter.setTotalRows(expiringContractCount + "");
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

    @ApiOperation(value = "获取快到期合同数量以及内容-app端")
    @ResponseBody
    @RequestMapping(value = "/contract/app", method = RequestMethod.POST)
    public ResponseDbCenter selectContractForMonitorForApp(HttpServletRequest request, HttpServletResponse response,
                                                           @ApiParam(value = "当前页面", required = true) @RequestParam(required = true) Integer currentPage,
                                                           @ApiParam(value = "页面大小", required = true) @RequestParam(required = true) Integer pageSize)
            throws Exception {
        try {
            //通过userId搜索contractor表
            String loginUserId = (String) request.getAttribute("loginUserId");
            Map<String, Object> userMap = userService.selectUserById(loginUserId);
            String pcCompanyId = (String) userMap.get("pcCompanyId");
            Integer expiringContractCount0_30 = contractService.countContractByPcCompanyId(pcCompanyId, 0, 30);
            List<Map<String, Object>> expiringContracts = contractService.listExpiringContractByPcCompanyId(pcCompanyId, currentPage, pageSize);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(expiringContracts);
            responseDbCenter.setTotalRows(expiringContractCount0_30 + "");
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

    @ApiOperation(value = "设备告警")
    @ResponseBody
    @RequestMapping(value = "/deviceWarning", method = RequestMethod.POST)
    public ResponseDbCenter selectWarningDevice(HttpServletRequest request, HttpServletResponse response,
                                                @ApiParam(value = "当前页面", required = true) @RequestParam(required = true) Integer currentPage,
                                                @ApiParam(value = "页面大小", required = true) @RequestParam(required = true) Integer pageSize)
            throws Exception {

        //通过userId搜索msg_device_curr
        try {
            String loginUserId = (String) request.getAttribute("loginUserId");
            Integer resultCount = deviceService.countWarningDeviceByUserId(loginUserId);
            List<Map<String, Object>> result = deviceService.selectWarningDeviceByUserId(loginUserId, currentPage, pageSize);
            for (Map<String, Object> device:result){
                Map<String, String> devAndProtocol = deviceService.getProtocolAndType((String) device.get("boxId"));
                device.putAll(devAndProtocol);
            }
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(result);
            responseDbCenter.setTotalRows(resultCount + "");
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

}
