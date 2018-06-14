package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.model.vo.CallbackVo;
import com.xkd.service.AppointmentService;
import com.xkd.service.UserService;
import com.xkd.utils.SmsApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.morph.context.contexts.BaseHierarchicalContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;
import org.omg.CORBA.DATA_CONVERSION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by dell on 2017/11/29.
 */

@Api(description = "客户登记相关接口")
@Controller
@RequestMapping("/apointment")
@Transactional
public class AppointmentController extends BaseController {

    @Autowired
    UserService userService;


    @Autowired
    AppointmentService appointmentService;


    Logger logger = Logger.getLogger(AppointmentController.class);


    @ApiOperation(value = "添加预约")
    @ResponseBody
    @RequestMapping(value = "/addApointment", method = {RequestMethod.POST})
    public ResponseDbCenter addApointment(HttpServletRequest req, HttpServletResponse rsp,
                                          @ApiParam(value = "姓名", required = false) @RequestParam(required = false) String name,
                                          @ApiParam(value = "手机", required = false) @RequestParam(required = false) String mobile,
                                          @ApiParam(value = "省", required = false) @RequestParam(required = false) String province,
                                          @ApiParam(value = "市", required = false) @RequestParam(required = false) String city,
                                          @ApiParam(value = "县", required = false) @RequestParam(required = false) String county,
                                          @ApiParam(value = "渠道 如博得世纪公众号", required = true) @RequestParam(required = true) String channel,
                                          @ApiParam(value = "课程 ,多个课程以逗号分隔", required = false) @RequestParam(required = false) String courseIds
    )
            throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();
            String id = UUID.randomUUID().toString();
            map.put("id", id);
            map.put("name", name);
            map.put("mobile", mobile);
            map.put("province", province);
            map.put("channel", channel);
            map.put("city", city);
            map.put("county", county);
            map.put("country", "中国");
            Date date = new Date();
            map.put("createDate", date);
            map.put("updateDate", date);


            /*
            查询预约用户是否在用户表中已经存在，如果已经存在则认为其为老客户
             */
            Map<String, Object> user = userService.selectUserByOnlyMobile(mobile);
            if (user != null) {
                map.put("newFlag", "1");
            }

            //定义接收短信的手机接口
            String sendMobile = null;

            //已经存在 的用户对应的企业下的顾问 Id列表
            List<String> adviserIdList = appointmentService.selectAdviserIdByMobile(mobile);
            if (adviserIdList.size() > 0) {
                /*
                  如果某一个预约用户已经在系统中存在，并且其所对应的企业已经分配了顾问，则从已经分配的顾问列表中随机取一条
                 */
                Integer index = Double.valueOf(Math.random() * adviserIdList.size()).intValue();
                map.put("adviserId", adviserIdList.get(index));
                Map<String, Object> uu = userService.selectUserById(adviserIdList.get(index));
                String umobile = (String) uu.get("mobile");
                sendMobile = umobile;
            } else {
                /**
                 * 查询负责某个省的顾问列表，并由第一个顾问负责
                 */

                List<String> advIdList = appointmentService.selectAdviserIdByProvince(city);//先匹配市
                if (advIdList.size()==0){
                    //如果市匹配不上，再匹配省
                    advIdList=appointmentService.selectAdviserIdByProvince(province);
                }
                if (advIdList.size() > 0) {
                    map.put("adviserId", advIdList.get(0));
                    Map<String, Object> uu = userService.selectUserById(advIdList.get(0));
                    String umobile = (String) uu.get("mobile");
                    sendMobile = umobile;
                }
            }


            //插入预约
            appointmentService.insertAppointment(map);

            List<Map<String, Object>> courseList = new ArrayList<>();

            if (!StringUtil.isBlank(courseIds)) {
                String[] cours = courseIds.split(",");
                for (int i = 0; i < cours.length; i++) {
                    Map m = new HashMap<>();
                    m.put("id", UUID.randomUUID().toString());
                    m.put("appointmentId", id);
                    m.put("courseId", cours[i]);
                    courseList.add(m);
                }
            }

            /**
             * 插入课程
             */
            if (courseList.size() > 0) {
                appointmentService.insertCourseList(courseList);
            }

            //发送短信通知顾问
            if (!StringUtil.isBlank(sendMobile)) {
                try {
                    String msg="您有新的客户登记信息，姓名："+name+"，电话："+mobile+"，来自"+province+city+county+"【蝌蚪智慧】";
                    logger.info("===="+msg);
                    SmsApi.sendSms(sendMobile, msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return new ResponseDbCenter();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);

        }

    }


    @ApiOperation(value = "更新预约")
    @ResponseBody
    @RequestMapping(value = "/updateApointment", method = {RequestMethod.POST})
    public ResponseDbCenter updateApointment(HttpServletRequest req, HttpServletResponse rsp,
                                             @ApiParam(value = "Id ", required = true) @RequestParam(required = true) String id,
                                             @ApiParam(value = "姓名", required = false) @RequestParam(required = false) String name,
                                             @ApiParam(value = "手机", required = false) @RequestParam(required = false) String mobile,
                                             @ApiParam(value = "省", required = false) @RequestParam(required = false) String province,
                                             @ApiParam(value = "市", required = false) @RequestParam(required = false) String city,
                                             @ApiParam(value = "县", required = false) @RequestParam(required = false) String county,
                                             @ApiParam(value = "课程,多个课程以逗号分隔", required = false) @RequestParam(required = false) String courseIds,
                                             @ApiParam(value = "预约渠道", required = false) @RequestParam(required = false) String channel,
                                             @ApiParam(value = "是否是新老客户", required = false) @RequestParam(required = false) String newFlag,
                                             @ApiParam(value = "顾问Id", required = false) @RequestParam(required = false) String adviserId,
                                             @ApiParam(value = "公司名称", required = false) @RequestParam(required = false) String companyName,
                                             @ApiParam(value = "职位", required = false) @RequestParam(required = false) String position,
                                             @ApiParam(value = "回访记录列表 [{\"appointmentId\":\"3434\",\"time\":\"2012-10-11 10:10:10\",\"content\":\"牛B企业\"}]", required = true) @RequestParam(required = true)  String callbackListString


    )
            throws Exception {
        try {

            List<CallbackVo> callbackList = JSON.parseObject(callbackListString, new TypeReference<List<CallbackVo>>() {
            });



// 当前登录用户的Id
            String loginUserId = (String) req.getAttribute("loginUserId");
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("name", name);
            map.put("mobile", mobile);
            map.put("province", province);
            map.put("city", city);
            map.put("county", county);
            map.put("channel", channel);
            map.put("newFlag", newFlag);
            map.put("adviserId", adviserId);
            map.put("companyName", companyName);
            map.put("position", position);
            map.put("updateDate", new Date());
            map.put("updatedBy", loginUserId);
            //更新预约
            appointmentService.updateAppointment(map);
            //删除预约课程
            appointmentService.deleteCourseByAppointmentId(id);
            /**
             * 插入预约课程
             */
            List<Map<String, Object>> courseList = new ArrayList<>();
            if (!StringUtil.isBlank(courseIds)) {
                String[] cours = courseIds.split(",");

                for (int i = 0; i < cours.length; i++) {
                    Map m = new HashMap<>();
                    m.put("id", UUID.randomUUID().toString());
                    m.put("appointmentId", id);
                    m.put("courseId", cours[i]);
                    courseList.add(m);
                }
            }

            if (courseList.size() > 0) {
                appointmentService.insertCourseList(courseList);
            }

            //先删除旧的callback列表
            appointmentService.deleteAppointmentCallbackByAppointmentId(id);

            /**
             * 插入修改后的 callback列表
             */
            List<Map<String, Object>> callbackMapList = new ArrayList<>();
            if (callbackList.size() > 0) {

                for (int i = 0; i < callbackList.size(); i++) {
                    Map<String, Object> mm = new HashMap<>();
                    mm.put("id", UUID.randomUUID().toString());
                    mm.put("time", callbackList.get(i).getTime());
                    mm.put("appointmentId", id);
                    mm.put("content", callbackList.get(i).getContent());
                    callbackMapList.add(mm);
                }
            }

            if (callbackMapList.size() > 0) {
                appointmentService.insertAppointmentCallbackList(callbackMapList);
            }


            return new ResponseDbCenter();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);

        }
    }


    @ApiOperation(value = "删除预约")
    @ResponseBody
    @RequestMapping(value = "/deleteApointment", method = {RequestMethod.POST})
    public ResponseDbCenter deleteApointment(HttpServletRequest req, HttpServletResponse rsp,
                                             @ApiParam(value = "Ids ", required = true) @RequestParam(required = true) String[] ids
    )
            throws Exception {
        try {
            if (ids != null) {
                for (int i = 0; i < ids.length; i++) {
                    appointmentService.deleteAppointment(ids[i]);
                    appointmentService.deleteCourseByAppointmentId(ids[i]);
                    appointmentService.deleteAppointmentCallbackByAppointmentId(ids[i]);
                }
            }

            return new ResponseDbCenter();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);

        }
    }


    @ApiOperation(value = "查询预约列表")
    @ResponseBody
    @RequestMapping(value = "/listAppointment", method = {RequestMethod.POST})
    public ResponseDbCenter listAppointment(HttpServletRequest req, HttpServletResponse rsp,
                                            @ApiParam(value = "时间", required = false) @RequestParam(required = false) String time,
                                            @ApiParam(value = "渠道", required = false) @RequestParam(required = false) String channel,
                                            @ApiParam(value = "顾问Id", required = false) @RequestParam(required = false) String adviserId,
                                            @ApiParam(value = "课程", required = false) @RequestParam(required = false) String courseId,
                                            @ApiParam(value = "当前页", required = true) @RequestParam(required = true) Integer currentPage,
                                            @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize
    )
            throws Exception {
        try {
            List<Map<String, Object>> list = new ArrayList<>();
            if (currentPage < 1) {
                currentPage = 1;
            }
            int start = (currentPage - 1) * pageSize;


            if (StringUtil.isBlank(courseId)){
                courseId=null;
            }
            if (StringUtil.isBlank(channel)){
                channel=null;
            }
            if (StringUtil.isBlank(adviserId)){
                adviserId=null;
            }
            if (StringUtil.isBlank(time)){
                time=null;
            }

            list = appointmentService.selectAppointList(courseId, channel, adviserId, time, start, pageSize);
            Integer total = appointmentService.selectAppointListCount(courseId, channel, adviserId, time);
            ResponseDbCenter response = new ResponseDbCenter();
            response.setTotalRows(total + "");
            response.setResModel(list);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);

        }
    }


    @ApiOperation(value = "查询预约详情")
    @ResponseBody
    @RequestMapping(value = "/getAppointmentDetail", method = {RequestMethod.POST})
    public ResponseDbCenter getAppointmentDetail(HttpServletRequest req, HttpServletResponse rsp,
                                                 @ApiParam(value = "时间", required = true) @RequestParam(required = true) String id
    )
            throws Exception {
        try {

            Map<String, Object> map = appointmentService.selectAppointmentById(id);
            List<Map<String, Object>> callbackList = appointmentService.selectAppointmentCallbackByAppointmentId(id);
            map.put("callbackList", callbackList);
            ResponseDbCenter response = new ResponseDbCenter();
            response.setResModel(map);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);

        }
    }


}