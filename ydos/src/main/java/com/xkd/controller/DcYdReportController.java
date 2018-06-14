package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sun.xml.ws.server.ServerRtException;
import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.*;
import com.xkd.utils.PropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Create by @author: wanghuijiu; @date: 18-2-27;
 */

@Api(description = "日报周报月报接口以及评论功能")
@Controller
@RequestMapping(value = "/report")
public class DcYdReportController {

    @Autowired
    private DcYdReportService dcYdReportService;

    @Autowired
    private UserService userService;

    @Autowired
    private DcYdReportCommentService dcYdReportCommentService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private DcYdReportSheetService dcYdReportSheetService;

    @Autowired
    private InspectionTaskService inspectionTaskService;

    @Autowired
    private YDrepaireService yDrepaireService;

    @Autowired
    private ObjectNewsService objectNewsService;


    @ApiOperation(value = "添加日报/周报/月报,及其附件")
    @ResponseBody
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ResponseDbCenter insertReport(HttpServletRequest request, HttpServletResponse response,
                                         @ApiParam(value = "今日/周/月 总结", required = false) @RequestParam(required = false) String summary,
                                         @ApiParam(value = "报表类型", required = false) @RequestParam(required = false) Integer type,
                                         @ApiParam(value = "计划", required = false) @RequestParam(required = false) String plan,
                                         @ApiParam(value = "url列表", required = false) @RequestParam(required = false) String urlString,
                                         @ApiParam(value = "工单列表", required = false) @RequestParam(required = false) String orderList,
                                         @ApiParam(value = "报表id", required = true) @RequestParam(required = true) String id) throws Exception {
        try {
            String loginUserId = (String) request.getAttribute("loginUserId");
            Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
            //添加日报主体
            Map<String, Object> dcYdReport = new HashMap<String, Object>();
            dcYdReport.put("id", id);
            dcYdReport.put("summary", summary);
            dcYdReport.put("plan", plan);
            dcYdReport.put("status", 0);
            dcYdReport.put("updateDate", new Date());
            dcYdReport.put("updatedBy", loginUserMap.get("id"));
            dcYdReportService.updateDcYdReport(dcYdReport);
            //添加日报附件(先删除报表附件，再重新添加)
            attachmentService.deleteByObjectId(id);
            if (StringUtils.isNotBlank(urlString)) {
                List<Map<String, Object>> attachmentMapList = new LinkedList<Map<String, Object>>();
                String[] urlArray = urlString.split(",");
                for (String url : urlArray) {
                    Map<String, Object> attachmentMap = new HashMap<String, Object>();
                    attachmentMap.put("id", UUID.randomUUID().toString());
                    attachmentMap.put("objectId", id);
                    attachmentMap.put("url", url);
                    attachmentMap.put("imgType", 3);
                    attachmentMap.put("createDate", new Date());
                    attachmentMap.put("createdBy", loginUserId);
                    attachmentMapList.add(attachmentMap);
                }
                attachmentService.insertAttachmentList(attachmentMapList);
            }

            //添加巡检任务和维修任务与报表关联
            if (StringUtils.isNotBlank(orderList)){
                dcYdReportSheetService.deleteReportNoteById(id);
                List<Map<String, Object>> resultMapList = JSON.parseObject(orderList, new TypeReference<List<Map<String, Object>>>() {
                });
                if (resultMapList != null) {
                    for (Map<String, Object> json : resultMapList
                            ) {
                        Map<String, Object> reportNote = new HashMap<String, Object>();
                        reportNote.put("id", UUID.randomUUID().toString());
                        reportNote.put("reportId", id);
                        reportNote.put("objectId", json.get("objectId"));
                        reportNote.put("flag", json.get("flag"));
                        reportNote.put("completeFlag", json.get("completeFlag"));
                        dcYdReportSheetService.addReportNote(reportNote);
                    }
                }
            }
            List<String> userIdList = userService.selectUserByPcCompanyIdAndRoleId(
                    (String) loginUserMap.get("pcCompanyId"), "2");
            String reportType;
            String reportImg=null;
            if (type==null){
                type = (Integer) dcYdReportService.selectById(id).get("type");
            }
            switch (type) {
                case 1:
                    reportType = "日报";
                    reportImg="report_day.png";
                    break;
                case 2:
                    reportType = "周报";
                    reportImg="report_week.png";
                    break;
                case 3:
                    reportType = "月报";
                    reportImg="report_month.png";
                    break;
                default:
                    reportType = "未知";
            }
            List<Map<String, Object>> newsMapList = new ArrayList<Map<String, Object>>();
            Map<String, Object> newsMap;
            for (String userId : userIdList) {
                newsMap = new HashMap<String, Object>();
                newsMap.put("id", UUID.randomUUID().toString());
                newsMap.put("objectId", id);
                newsMap.put("appFlag", 0);// 0 服务端，1技师端，2客户端
                newsMap.put("userId", userId);
                newsMap.put("title", "工单待领取");
                newsMap.put("content", "工程师" + (String) loginUserMap.get("uname") + "提交了" + reportType + "，请及时领取。");
                newsMap.put("createDate", new Date());
                newsMap.put("createdBy", loginUserId);
                newsMap.put("flag", 0);  //0未读，1已读
                newsMap.put("newsType", 6);//0维修报修,1设备消息,6报表消息
                newsMap.put("status", 0);
                newsMap.put("pushFlag", "0"); //"0"激光推送消息，"1"激光不推送消息
                newsMap.put("imgUrl",  PropertiesUtil.FILE_HTTP_PATH+"icons/msgIcons/"+reportImg);
                newsMapList.add(newsMap);
            }
            objectNewsService.saveObjectNews(newsMapList);


            return new ResponseDbCenter();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

    }

    @ApiOperation(value = "删除日报/周报/月报")
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseDbCenter deleteReport(HttpServletRequest request, HttpServletResponse response,
                                         @ApiParam(value = "日报/周报/月报 id", required = true) @RequestParam(required = true) String id) throws Exception {
        try {
            dcYdReportService.deleteById(id);
            return new ResponseDbCenter();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }

    @ApiOperation(value = "通过id获取日报/周报/月报")
    @ResponseBody
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public ResponseDbCenter selectById(HttpServletRequest request, HttpServletResponse response,
                                       @ApiParam(value = "日报id", required = true) @RequestParam(required = true) String id) throws Exception {
        try {

            String loginUserId = (String) request.getAttribute("loginUserId");
            Map<String, Object> userMap = userService.selectUserById(loginUserId);
            String roleId = (String) userMap.get("roleId");
            //获取日报
            Map<String, Object> dcYdReport = dcYdReportService.selectById(id);
            Integer type = (Integer) dcYdReport.get("type");
            //获取日报评论
            List<Map<String, Object>> commentList = dcYdReportCommentService.listComment(id);
            //获取日报附件
            List<Map<String, Object>> attachmentList = attachmentService.selectAttachmentByObjectId(id);
            //获取日报与巡检/维修关联记录(如果不是服务商而且报表绑定日期是今天,就实时获取数据，不然就查表)
            Date bindDate = (Date) dcYdReport.get("bindDate");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            List<Map<String, Object>> reportNoteList = new LinkedList<>();
            if (format.format(bindDate).equals(format.format(new Date())) && !roleId.equals("2")) {
                List<Map<String, Object>> inspectionList =
                        inspectionTaskService.selectInspectionByUserAndDate(loginUserId, type, format.format(new Date()));
                List<Map<String, Object>> repairList =
                        yDrepaireService.selectRepairByUserAndDate(loginUserId, type,format.format(new Date()));
                if (inspectionList != null) {
                    reportNoteList.addAll(inspectionList);
                }
                if (repairList != null) {
                    reportNoteList.addAll(repairList);
                }
            } else {
                reportNoteList = dcYdReportSheetService.listreportNote(id);
            }

            //将检索结果添加进日报模型
            dcYdReport.put("commentList", commentList);
            dcYdReport.put("attachmentList", attachmentList);
            dcYdReport.put("reportNoteList", reportNoteList);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(dcYdReport);
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

    @ApiOperation(value = "日报筛选,通过技师Id, 状态, 类型, 开始日期, 结束日期, 当前页面, 页面大小")
    @ResponseBody
    @RequestMapping(value = "/select/list", method = RequestMethod.POST)
    public ResponseDbCenter listReport(HttpServletRequest request, HttpServletResponse response,
                                       @ApiParam(value = "报表状态:未完成:1, 已完成:0 ", required = false) @RequestParam(required = false) Integer status,
                                       @ApiParam(value = "报表类型: 日报>1,周报>2,月报>3", required = false) @RequestParam(required = false) Integer type,
                                       @ApiParam(value = "报表创建人id(服务端通过工程师筛选)", required = false) @RequestParam(required = false) String userId,
                                       @ApiParam(value = "开始日期", required = false) @RequestParam(required = false) String startDate,
                                       @ApiParam(value = "结束日期", required = false) @RequestParam(required = false) String endDate,
                                       @ApiParam(value = "app端{本周：1；本月：2；上月：3；本年：4；上年：5；}", required = false)
                                           @RequestParam(required = false) Integer dateFilter,
                                       @ApiParam(value = "当前页面", required = true) @RequestParam(required = true) Integer currentPage,
                                       @ApiParam(value = "页面大小", required = true) @RequestParam(required = true) Integer pageSize) {
        String loginUserId = (String) request.getAttribute("loginUserId");
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
        List<Map<String, Object>> reportList = new LinkedList<>();
        int total = 0;
        String roleId = (String) loginUserMap.get("roleId");
        try {
            if ("3".equals(roleId)) {
                String[] userIdArray = new String[] {loginUserId};
                if (dateFilter != null){
                    reportList = dcYdReportService.filterReport(Arrays.asList(userIdArray), status, type, startDate, endDate,dateFilter, null, currentPage, pageSize);
                    total = dcYdReportService.filterReportCount(Arrays.asList(userIdArray), status, type, null,startDate, endDate,dateFilter);
                }else {
                    reportList = dcYdReportService.filterReport(Arrays.asList(userIdArray), status, type, startDate, endDate,null,null, currentPage, pageSize);
                    total = dcYdReportService.filterReportCount(Arrays.asList(userIdArray), status, type, null,startDate, endDate,null);
                }

            } else {
                String pcCompanyId = (String) loginUserMap.get("pcCompanyId");
                if (StringUtils.isNotBlank(userId)) {
                    String[] userIdArray = new String[] {userId};
                    if (dateFilter != null){
                        reportList = dcYdReportService.filterReport(Arrays.asList(userIdArray), 0, type, startDate, endDate,dateFilter, pcCompanyId,currentPage, pageSize);
                        total = dcYdReportService.filterReportCount(Arrays.asList(userIdArray), 0, type, pcCompanyId, startDate, endDate,dateFilter);
                    }else {
                        reportList = dcYdReportService.filterReport(Arrays.asList(userIdArray), 0, type, startDate, endDate,null, pcCompanyId,currentPage, pageSize);
                        total = dcYdReportService.filterReportCount(Arrays.asList(userIdArray), 0, type, pcCompanyId, startDate, endDate,null);
                    }

                } else {
                    if (dateFilter != null){
                        reportList = dcYdReportService.filterReport(null, 0, type, startDate, endDate,dateFilter, pcCompanyId,currentPage, pageSize);
                        total = dcYdReportService.filterReportCount(null, 0, type,pcCompanyId, startDate, endDate, dateFilter);
                    }else {
                        reportList = dcYdReportService.filterReport(null, 0, type, startDate, endDate,null, pcCompanyId,currentPage, pageSize);
                        total = dcYdReportService.filterReportCount(null, 0, type, pcCompanyId, startDate, endDate, null);
                    }

                }
            }
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setTotalRows(total + "");
            responseDbCenter.setResModel(reportList);
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


    @ApiOperation(value = "添加日报评论")
    @ResponseBody
    @RequestMapping(value = "/comment/add", method = RequestMethod.POST)
    public ResponseDbCenter addComment(HttpServletRequest request, HttpServletResponse response,
                                       @ApiParam(value = "关联报表Id", required = true) @RequestParam(required = true) String reportId,
                                       @ApiParam(value = "评论内容", required = true) @RequestParam(required = true) String body) {
        try {
            String loginUserId = (String) request.getAttribute("loginUserId");
            Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
            Map<String, Object> comment = new HashMap<String, Object>();
            comment.put("id", UUID.randomUUID().toString());
            comment.put("reportId", reportId);
            comment.put("description", body);
            comment.put("createDate", new Date());
            comment.put("createBy", loginUserMap.get("id"));
            dcYdReportCommentService.addComment(comment);
            return new ResponseDbCenter();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }

    }

    @ApiOperation(value = "修改日报评论")
    @ResponseBody
    @RequestMapping(value = "/comment/update", method = RequestMethod.POST)
    public ResponseDbCenter updateComment(HttpServletRequest request, HttpServletResponse response,
                                          @ApiParam(value = "日报id", required = true) @RequestParam(required = true) String id,
                                          @ApiParam(value = "评论内容", required = true) @RequestParam(required = true) String body) {
        try {
            Map<String, Object> comment = new HashMap<String, Object>();
            comment.put("id", id);
            comment.put("description", body);
            dcYdReportCommentService.updateComment(comment);
            return new ResponseDbCenter();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }

    }

    @ApiOperation(value = "删除日报评论")
    @ResponseBody
    @RequestMapping(value = "/comment/delete", method = RequestMethod.POST)
    public ResponseDbCenter deleteComment(HttpServletRequest request, HttpServletResponse response,
                                          @ApiParam(value = "评论id", required = true) @RequestParam(required = true) String id) {
        try {
            dcYdReportCommentService.deleteComment(id);
            return new ResponseDbCenter();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


    @ApiOperation(value = "删除报表附件")
    @ResponseBody
    @RequestMapping(value = "/attachment/delete", method = RequestMethod.POST)
    public ResponseDbCenter deleteAttachment(HttpServletRequest request, HttpServletResponse response,
                                             @ApiParam(value = "通过附件id删除", required = false) @RequestParam(required = false) String id,
                                             @ApiParam(value = "通过报表id删除", required = false) @RequestParam(required = false) String reportId) {
        try {
            if (id == null || id.equals("")) {
                attachmentService.deleteByObjectId(reportId);
            } else {
                attachmentService.deleteById(id);
            }
            return new ResponseDbCenter();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

    @ApiOperation(value = "更新巡检/维修任务与当日报表关联")
    @ResponseBody
    @RequestMapping(value = "/note/update", method = RequestMethod.POST)
    public ResponseDbCenter updateReportNote(HttpServletRequest request, HttpServletResponse response,
                                             @ApiParam(value = "noteId", required = true) @RequestParam(required = true) String id,
                                             @ApiParam(value = "completeFlag", required = true) @RequestParam(required = true) Integer completeFlag) {
        try {
            dcYdReportSheetService.updateReportNote(completeFlag, id);
            return new ResponseDbCenter();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

    @ApiOperation(value = "获取巡检/维修任务与当日报表关联列表")
    @ResponseBody
    @RequestMapping(value = "/note/list", method = RequestMethod.POST)
    public ResponseDbCenter listReportNote(HttpServletRequest request, HttpServletResponse response,
                                           @ApiParam(value = "报表类型1:日报,2:周报,3:月报", required = true) @RequestParam(required = true) Integer type) {

        try {
            System.out.println(request.getAttribute("loginUserId"));
            String loginUserId = (String) request.getAttribute("loginUserId");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            List<Map<String, Object>> inspectionList =
                    inspectionTaskService.selectInspectionByUserAndDate(loginUserId, type, format.format(new Date()));
            List<Map<String, Object>> repairList =
                    yDrepaireService.selectRepairByUserAndDate(loginUserId, type, format.format(new Date()));
            Map<String, Object> result = new HashMap<>();
            result.put("inspectionList", inspectionList);
            result.put("repairList", repairList);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(result);
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

}
