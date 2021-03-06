package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xkd.exception.GlobalException;
import com.xkd.model.*;
import com.xkd.service.*;
import com.xkd.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Api(description = "票务功能接口")
@Controller
@RequestMapping("/ticket")
@Transactional
public class TicketController extends BaseController {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private CompanyService companyServie;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private SolrService solrService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private  UserAttendMeetingService userAttendMeetingService;
    @Autowired
    private MeetingService meetingService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private MeetingPeopleService meetingPeopleService;
    @Autowired
    private SpreadService spreadService;



    @ApiOperation(value = "保存用户订票相关信息")
    @ResponseBody
    @RequestMapping(value = "/saveUserTicket", method = RequestMethod.POST)
    @Transactional
    public synchronized ResponseDbCenter saveUserTicket(HttpServletRequest req, HttpServletResponse rsp,
                                                        @ApiParam(value = "token", required = false) @RequestParam(required = false) String token,
                                                        @ApiParam(value = "票和票的数量", required = false) @RequestParam(required = false) String ticketNumbers,
                                                        @ApiParam(value = "用户姓名", required = false) @RequestParam(required = false) String uname,
                                                        @ApiParam(value = "手机号", required = false) @RequestParam(required = false) String mobile,
                                                        @ApiParam(value = "是否发送短信", required = false) @RequestParam(required = false) String sendMessageFlag,
                                                        @ApiParam(value = "验证码", required = false) @RequestParam(required = false) String mobileCode,
                                                        @ApiParam(value = "企业名称", required = false) @RequestParam(required = false) String companyName,
                                                        @ApiParam(value = "职位", required = false) @RequestParam(required = false) String station,
                                                        @ApiParam(value = "身份证号", required = false) @RequestParam(required = false) String idcard,
                                                        @ApiParam(value = "邮箱", required = false) @RequestParam(required = false) String email,
                                                        @ApiParam(value = "用户备注", required = false) @RequestParam(required = false) String remark,
                                                        @ApiParam(value = "会务ID", required = false) @RequestParam(required = false) String meetingId,
                                                        @ApiParam(value = "推广活动ID", required = false) @RequestParam(required = false) String spreadSettingId,
                                                        @ApiParam(value = "推广人ID", required = false) @RequestParam(required = false) String userSpreadId) throws Exception {


        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(uname) || StringUtils.isBlank(meetingId)) {
            return ResponseConstants.MISSING_PARAMTER;
        }

        String usercode = (String) req.getSession().getAttribute("code" + mobile);
        if (usercode == null || !usercode.equals(mobileCode)) {
            return ResponseConstants.TEL_CODE_ERROR;
        }

        //因为该用户在前面已经登录了，所以直接是编辑用户
        String loginUserId = (String)req.getAttribute("loginUserId");

        String pcCompanyId = null;
        if(StringUtils.isNotBlank(meetingId)){
            Meeting meeting = meetingService.selectMeetingById(meetingId);
            if(meeting !=null){
                pcCompanyId = meeting.getPcCompanyId();
            }
        }
        JsonParser parse = new JsonParser();  //创建json解析器
        SortedMap<Object, Object> sortedMap = null;
        Meeting meeting = null;

        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
        String openId = (String) loginUserMap.get("weixin");
        System.out.println("----------------------登录用户的openId="+openId);


        try {

            String companyId = UUID.randomUUID().toString();
            String existCompanyId = "";
            String existsUserIdd = null;
            //通过上面的代码，手机号已经绑定到企业了
            String realUserId = null;
            //是否新增用户和当前登录用户没关系，只和手机号有关系，和当前登录用户没关系
            Map<String, Object> mobileUserMap = meetingPeopleService.selectUserByMobile(mobile);

            Map<String,Object> paramMap = new HashedMap();
            if (mobileUserMap != null && mobileUserMap.size() > 0) {
                realUserId = (String) mobileUserMap.get("id");
                paramMap.put("id",realUserId);
                paramMap.put("email",email);
                paramMap.put("uname",uname);
                paramMap.put("companyName",companyName);
                paramMap.put("idcard",idcard);
                paramMap.put("station",station);
                paramMap.put("createdBy",realUserId);
                paramMap.put("createDate",new Date());
                paramMap.put("updatedBy",realUserId);
                paramMap.put("updateDate",new Date());

                meetingPeopleService.updatePeople(paramMap);

                List<UserAttendMeeting> userAttendMeetings = userAttendMeetingService.selectMeetingUserByUserIdAndMeetingId(meetingId,realUserId);
                if(userAttendMeetings != null && userAttendMeetings.size()>0){
                    return ResponseConstants.TICKET_USER_EXISTS;
                }
            }else{
                realUserId = UUID.randomUUID().toString();
                paramMap.put("id",realUserId);
                paramMap.put("email",email);
                paramMap.put("mobile",mobile);
                paramMap.put("uname",uname);
                paramMap.put("companyName",companyName);
                paramMap.put("idcard",idcard);
                paramMap.put("station",station);
                paramMap.put("createdBy",realUserId);
                paramMap.put("createDate",new Date());
                paramMap.put("updatedBy",realUserId);
                paramMap.put("updateDate",new Date());

                meetingPeopleService.savePeople(paramMap);
            }

            System.out.println("=====================ticketNumbers===="+ticketNumbers);
            List<Map<String, Object>> maps = (List<Map<String, Object>>) JSON.parseObject(ticketNumbers,Object.class);
            System.out.println("=====================maps===="+maps);

            boolean ticketFlag = false;
            boolean numberFlag = false;
            long totoPrice = 0;
            String totalTicketMessage = "";
            String mhtOrderNo = "";
            for (Map<String, Object> map : maps) {
                String ticketId = (String) map.get("id");
                Integer number = (Integer) map.get("number");

                //查出库存
                Integer num = ticketService.getTicketSavingByTicketId(ticketId);

                int saving = num.intValue();
                int numberInt = number.intValue();
                if (numberInt > saving) {
                    return ResponseConstants.TICKET_NOT_ENOUGHT;
                }
                if(numberInt > 0){
                    numberFlag = true;
                }
                if ((0 == saving)) {
                    continue;
                }

                Map<String, Object> ticketMap = ticketService.selectTicketById(ticketId);
                String ticketType = (String) ticketMap.get("ticketType");
                String price = (String) ticketMap.get("price");

                long doublePrice = Long.valueOf(price);
                totoPrice += doublePrice * (number.intValue());
                totalTicketMessage += ticketType + number.intValue() + "张,";
                ticketFlag = true;
            }

            if(!numberFlag && maps != null && maps.size() > 0){
                return ResponseConstants.TICKET_NUMBER_ZERO;
            }else if (!ticketFlag && maps != null && maps.size() > 0) {
                return ResponseConstants.TICKET_HAS_SELL_OUT;
            }

            meeting = meetingService.selectMeetingById(meetingId);
           //发送短信给该手机的用户
            if("true".equals(meeting.getSendMessageFlag()) && StringUtils.isNotBlank(meeting.getMobile())){
                SmsApi.sendSmsInTicketAttendMeeting(meeting.getMobile(),mobile,uname,meeting.getMeetingName());
            }

            if(maps != null && maps.size() > 0){

                String orderId = UUID.randomUUID().toString();
                //当金额大于0时才进行支付
                if (StringUtils.isNotBlank(totalTicketMessage)) {
                    totalTicketMessage = totalTicketMessage.substring(0, totalTicketMessage.lastIndexOf(","));
                    totalTicketMessage += "共" + totoPrice + "分";
                }

                Map<String, Object> userOrderMap = new HashMap<>();

                //如果金额大于0就调用微信支付信息
                if(totoPrice > 0){

                    Map<String, Object> weixinUserOrderMap = new HashMap<>();
                    weixinUserOrderMap.put("body", totalTicketMessage);
                    weixinUserOrderMap.put("total_fee", totoPrice);
                    weixinUserOrderMap.put("openId", openId);
                    weixinUserOrderMap.put("NOTIFY_URL", "dbcenter/ticket/savePay");
                    System.out.println("下单openId--------------:"+openId);
                    sortedMap = WeixinPlayUtils.weixinUserOrder(weixinUserOrderMap);
                    //订单号
                    mhtOrderNo = (String)sortedMap.get("mhtOrderNo");

                    userOrderMap.put("id", orderId);
                    //订单要绑在当前登录用户上
                    userOrderMap.put("userId",loginUserId);
                    userOrderMap.put("orderName", totalTicketMessage);
                    userOrderMap.put("mhtOrderNo", mhtOrderNo);
                    userOrderMap.put("orderTime", new Date());
                    userOrderMap.put("mhtOrderAmt", totoPrice);
                    userOrderMap.put("mhtOrderStartTime", WeixinPlayUtils.getTimeStamp());
                    userOrderMap.put("mhtOrderAmt", totoPrice);
                    userOrderMap.put("payStatus", "0");
                    userOrderMap.put("status", 0);
                    userOrderMap.put("meetingId", meetingId);
                    userOrderMap.put("companyId", StringUtils.isBlank(existCompanyId)?companyId:existCompanyId);
                    //被支付人，手机号用户，支付人是当前登录用户
                    userOrderMap.put("payedUserId", realUserId);
                    //推广人
                    userOrderMap.put("userSpreadId", userSpreadId);
                    userOrderMap.put("spreadSettingId", spreadSettingId);

                    orderService.saveUserOrder(userOrderMap);

                    System.out.println("===========maps=========="+maps);

                    /*
                     * 开启线程，如果用户15分钟之内没有支付的话，就取消订单，释放库存
                     */
                    OrderOvertimeThread orderOvertimeThread = new OrderOvertimeThread(mhtOrderNo);
                    Thread thread = new Thread(orderOvertimeThread);
                    thread.start();

                }else{

                    mhtOrderNo = WeixinPlayUtils.getOrderNoStr();

                    userOrderMap.put("id", orderId);
                    //订单要绑在当前登录用户上
                    userOrderMap.put("userId",loginUserId);
                    userOrderMap.put("orderName", totalTicketMessage);
                    userOrderMap.put("mhtOrderNo", mhtOrderNo);
                    userOrderMap.put("orderTime", new Date());
                    userOrderMap.put("mhtOrderAmt", totoPrice);
                    userOrderMap.put("mhtOrderStartTime", WeixinPlayUtils.getTimeStamp());
                    userOrderMap.put("mhtOrderAmt", totoPrice);
                    //因为金额为0没有调用微信，在状态中显示支付成功
                    userOrderMap.put("payStatus", "1");
                    userOrderMap.put("payTime", DateUtils.currtime());
                    userOrderMap.put("status", 0);
                    userOrderMap.put("meetingId", meetingId);
                    userOrderMap.put("companyId", StringUtils.isBlank(existCompanyId)?companyId:existCompanyId);
                    //被支付人，手机号用户，支付人是当前登录用户
                    userOrderMap.put("payedUserId", realUserId);
                    userOrderMap.put("userSpreadId", userSpreadId);
                    userOrderMap.put("spreadSettingId", spreadSettingId);

                    orderService.saveUserOrder(userOrderMap);

                    System.out.println("===========maps=========="+maps);
                }

                for (Map<String, Object> map : maps) {

                    String ticketId = (String) map.get("id");
                    Integer number = (Integer) map.get("number");

                    Map<String, Object> orderTicketMap = new HashMap<>();
                    orderTicketMap.put("id", UUID.randomUUID().toString());
                    orderTicketMap.put("orderId",orderId);
                    orderTicketMap.put("ticketId", ticketId);
                    orderTicketMap.put("ticketNumber", number);

                    System.out.println("===========map=========="+map);
                    orderService.saveOrderTicket(orderTicketMap);

                    Integer num = ticketService.getTicketSavingByTicketId(ticketId);
                    int saving = num.intValue();
                    int ticketNumberInt = number.intValue();

                    //减少库存
                    if(saving >= ticketNumberInt){
                        ticketService.updateTicketSavingById(ticketId,ticketNumberInt);
                    }else{
                        ticketService.updateTicketSavingById(ticketId,saving);
                    }
                }

                //点击支付后将用户添加到参会人、就算没有支付也会添加到参会人中
                List<UserAttendMeeting> userAttendMeetings = userAttendMeetingService.selectMeetingUserByUserIdAndMeetingId(meetingId,realUserId);
                if(userAttendMeetings != null && userAttendMeetings.size() > 0){

                    UserAttendMeeting userAttendMeeting = userAttendMeetings.get(0);
                    userAttendMeeting.setStatus("已报名");
                    userAttendMeeting.setCompanyId(StringUtils.isBlank(existCompanyId)?companyId:existCompanyId);
                    userAttendMeeting.setUpdateTime(new Date());
                    Integer learnStatusInt = 0;
                    userAttendMeeting.setLearnStatus(learnStatusInt);
                    userAttendMeeting.setUserId(realUserId);
                    userAttendMeeting.setTicketLoginUserId(loginUserId);
                    userAttendMeeting.setMeetingId(meetingId);

                    userAttendMeetingService.updateUserMeeting(userAttendMeeting);

                }else{
                    UserAttendMeeting userAttendMeeting = new UserAttendMeeting();
                    userAttendMeeting.setStatus("已报名");
                    userAttendMeeting.setCompanyId(StringUtils.isBlank(existCompanyId)?companyId:existCompanyId);
                    userAttendMeeting.setUpdateTime(new Date());
                    Integer learnStatusInt = 0;
                    userAttendMeeting.setLearnStatus(learnStatusInt);
                    userAttendMeeting.setUserId(realUserId);
                    userAttendMeeting.setTicketLoginUserId(loginUserId);
                    userAttendMeeting.setMeetingId(meetingId);

                    userAttendMeetingService.saveUserMeeting(userAttendMeeting);
                }

                ResponseDbCenter responseDbCenter = new ResponseDbCenter();

                if(sortedMap == null){
                    sortedMap = new TreeMap<Object,Object>();
                    sortedMap.put("mhtOrderNo", mhtOrderNo);//订单号
                    responseDbCenter.setResModel(sortedMap);
                }else{
                    responseDbCenter.setResModel(sortedMap);
                }
                return responseDbCenter;

            }else{

                List<UserAttendMeeting> userAttendMeetings = userAttendMeetingService.selectMeetingUserByUserIdAndMeetingId(meetingId,realUserId);
                if(userAttendMeetings != null && userAttendMeetings.size() > 0){

                    UserAttendMeeting userAttendMeeting = userAttendMeetings.get(0);
                    userAttendMeeting.setStatus("已报名");
                    userAttendMeeting.setCompanyId(StringUtils.isBlank(existCompanyId)?companyId:existCompanyId);
                    userAttendMeeting.setUpdateTime(new Date());
                    Integer learnStatusInt = 0;
                    userAttendMeeting.setLearnStatus(learnStatusInt);
                    userAttendMeeting.setUserId(realUserId);
                    userAttendMeeting.setTicketLoginUserId(loginUserId);
                    userAttendMeeting.setMeetingId(meetingId);

                    userAttendMeetingService.updateUserMeeting(userAttendMeeting);

                }else{
                    UserAttendMeeting userAttendMeeting = new UserAttendMeeting();
                    userAttendMeeting.setStatus("已报名");
                    userAttendMeeting.setCompanyId(StringUtils.isBlank(existCompanyId)?companyId:existCompanyId);
                    userAttendMeeting.setUpdateTime(new Date());
                    Integer learnStatusInt = 0;
                    userAttendMeeting.setLearnStatus(learnStatusInt);
                    userAttendMeeting.setUserId(realUserId);
                    userAttendMeeting.setTicketLoginUserId(loginUserId);
                    userAttendMeeting.setMeetingId(meetingId);

                    userAttendMeetingService.saveUserMeeting(userAttendMeeting);
                }

                ResponseDbCenter responseDbCenter = new ResponseDbCenter();
                return responseDbCenter;
            }

        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    class OrderOvertimeThread implements Runnable{

        private  String mhtOrderNo;

        public OrderOvertimeThread(String mhtOrderNo){
            this.mhtOrderNo = mhtOrderNo;
        }

        public void run() {
            try {
                //睡觉15分钟
                Thread.sleep(1000*60*15);

                //如果15分钟没有支付的话就释放库存
                Map<String, String> csOrder = ticketService.selectUserOrderByOrderNo(mhtOrderNo);
                String payStatus = csOrder.get("payStatus");
                String meetingId = csOrder.get("meetingId");
                String payedUserId = csOrder.get("payedUserId");
                String payAgain = csOrder.get("payAgain");
                //如果订单没有被支付，并且该订单没有被重新支付
                if("0".equals(payStatus) && !"2".equals(payAgain)){

                    List<Map<String,Object>> maps = ticketService.selectTicketNumberByOderId((String) csOrder.get("id"));
                    for(Map<String,Object> map : maps){
                        String ticketId = (String)map.get("ticketId");
                        Integer ticketNumber = (Integer)map.get("ticketNumber");

                        int ticketNumberInt = ticketNumber.intValue();
                        ticketService.updateTicketSavingById(ticketId,-ticketNumberInt);

                        csOrder.put("payTime", DateUtils.currtime());
                        csOrder.put("payStatus","2");
                        ticketService.updateUserTicketById(csOrder);

                    }

                    //超时如果这个人有参会那就取消参会
                    List<UserAttendMeeting> userAttendMeetings = userAttendMeetingService.selectMeetingUserByUserIdAndMeetingId(meetingId,payedUserId);
                    if(userAttendMeetings != null && userAttendMeetings.size() > 0){
                        userAttendMeetingService.deleteMeetingUserByIds("id ='"+userAttendMeetings.get(0).getId()+"'");
                    }

                }
            } catch (InterruptedException e) {
                log.error("异常栈:",e);
            }
        }
    }



    //支付成功后回调
    @ResponseBody
    @RequestMapping("/savePay")
    public   void savePay(HttpServletRequest req, HttpServletResponse res, @RequestBody String result) throws Exception{



        System.out.println("---------------------支付成功后回调:"+result);
        String result_code = result.substring(result.indexOf("<result_code><![CDATA[")+22, result.indexOf("]]></result_code>"));//支付是否成功
        String openid = result.substring(result.indexOf("<openid><![CDATA[")+17, result.indexOf("]]></openid>"));//用户openid
        String out_trade_no = result.substring(result.indexOf("<out_trade_no><![CDATA[")+23, result.indexOf("]]></out_trade_no>"));//自己提交给微信的订单号
        String transaction_id = result.substring(result.indexOf("<transaction_id><![CDATA[")+25, result.indexOf("]]></transaction_id>"));//微信的订单号

        System.out.println("结果："+result_code+"  "+openid+" "+out_trade_no+" "+transaction_id);
        String xml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA]></return_msg></xml>";

        try{

            if("FAIL".equals(result_code)){
                xml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[支付失败]]></return_msg></xml>";
            }else if(StringUtils.isBlank(result_code) || StringUtils.isBlank(openid) ||  StringUtils.isBlank(out_trade_no) || StringUtils.isBlank(transaction_id)){
                xml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[参数格式校验错误]]></return_msg></xml>";
            }else{

                Map<String, String> csOrder = ticketService.selectUserOrderByOrderNo(out_trade_no);

                csOrder.put("channelOrderNo", transaction_id);
                csOrder.put("payTime", DateUtils.currtime());
                csOrder.put("payStatus","1");
                ticketService.updateUserTicketById(csOrder);

                String userOrderId = csOrder.get("id")!=null?(String)csOrder.get("id"):null;
                String spreadSettingId = csOrder.get("spreadSettingId")!=null?(String)csOrder.get("spreadSettingId"):null;
                String userSpreadId = csOrder.get("userSpreadId")!=null?(String)csOrder.get("userSpreadId"):null;
                String mhtOrderAmt = csOrder.get("mhtOrderAmt")!=null?(String)csOrder.get("mhtOrderAmt"):null;


                SpreadSetting spreadSetting = spreadService.selectSpreadSettingById(spreadSettingId);
                if(spreadSetting != null && userOrderId != null){
                    String getGetRate = spreadSetting.getGetRate();
                    Double getRateDouble = new Double(getGetRate.replaceAll("%",""));
                    Double mhtOrderAmtDouble = new Double(mhtOrderAmt);
                    String s = (mhtOrderAmtDouble.doubleValue()*getRateDouble.doubleValue())/100+"";
                    Double getMoney = new Double(s);

                    UserGetMoney userGetMoney = new UserGetMoney();
                    userGetMoney.setId(UUID.randomUUID().toString());
                    userGetMoney.setUserOrderId(userOrderId);
                    userGetMoney.setUserSpreadId(userSpreadId);
                    userGetMoney.setCreateDate(new Date());
                    userGetMoney.setGetMoney(getMoney.intValue()+"");
                    userGetMoney.setLogFlag(0);
                    spreadService.insertGetMoneyLog(userGetMoney);
                }


                //id,userId,orderName,mhtOrderNo,mhtOrderAmt,orderTime,mhtOrderStartTime,channelOrderNo,
                //payTime,payConsumerId,payStatus,meetingId,status,companyId,payedUserId,CONCAT(payAgain,'') as payAgain
            }
            res.getOutputStream().write(xml.getBytes());

        }catch (Exception e){
            log.error("异常栈:",e);
            res.getOutputStream().write(xml.getBytes());
        }
    }

    /**
     *
     * @author: xiaoz
     * @2017年7月13日
     * @功能描述:查询所有顾问
     * @param req
     * @return
     */
    @ApiOperation(value = "pc端查询票务信息")
    @ResponseBody
    @RequestMapping(value = "/selectTicketsByContent",method = RequestMethod.POST)
    public ResponseDbCenter selectTicketsByContent(HttpServletRequest req, HttpServletResponse rsp,
                                                   @ApiParam(value="手机号或者姓名",required = false) @RequestParam(required = false) String content,
                                                   @ApiParam(value="会务ID，必传",required = false) @RequestParam(required = false) String meetingId,
                                                   @ApiParam(value="每页条数",required = false) @RequestParam(required = false) String pageSize,
                                                   @ApiParam(value="当前页",required = false) @RequestParam(required = false) String currentPage,
                                                   @ApiParam(value="票务类型",required = false) @RequestParam(required = false) String ticketType,
                                                   @ApiParam(value="状态",required = false) @RequestParam(required = false) String payStatus,
                                                   @ApiParam(value="分组",required = false) @RequestParam(required = false) String mgroup){


        if(StringUtils.isBlank(meetingId)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        if(StringUtils.isBlank(pageSize) || StringUtils.isBlank(currentPage)){
            pageSize = "10";
            currentPage = "1";
        }


        int  pageSizeInt = Integer.parseInt(pageSize);
        int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;
        Integer totalRows = 0;

        try {

            String payStatusStr = null;

            if("已付款".equals(payStatus)){
                payStatusStr = "1";
            }else if("待付款".equals(payStatus)){
                payStatusStr = "0";
            }else if("已取消".equals(payStatus)){
                payStatusStr = "2";
            }

            List<Map<String,Object>> maps = ticketService.selectTicketsByContent(meetingId,content,currentPageInt,pageSizeInt,payStatusStr,mgroup,ticketType);
            totalRows = ticketService.selectTicketsTotalByContent(meetingId,content,payStatusStr,mgroup,ticketType);
            Map<String,Object> ticketNumberMap = new HashMap<>();
            for(Map<String,Object> map : maps){
                String orderId = (String)map.get("orderId");
                List<Map<String,Object>> orderTickets = ticketService.selectorderTicketByOderId(orderId);
                map.put("orderTickets",orderTickets);
            }

            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(maps);
            responseDbCenter.setTotalRows(totalRows.toString());
            return responseDbCenter;

        } catch (Exception e) {
            log.error("异常栈:",e);
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


    /**
     *
     * @author: xiaoz
     * @2017年7月13日
     * @功能描述:pc端确定领票
     * @param req
     * @return
     */
    @ApiOperation(value = "pc端查询票务信息")
    @ResponseBody
    @RequestMapping(value = "/ensureGetTickets",method = RequestMethod.POST)
    public ResponseDbCenter ensureGetTickets(HttpServletRequest req, HttpServletResponse rsp,
                                             @ApiParam(value="orderTecketList集合orderTecketId,getTicketNumber(已经取得票)",
                                                           required = false) @RequestParam(required = false) String orderTecketList) throws  Exception{

        if(StringUtils.isBlank(orderTecketList)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        try {

            List<Map<String,Object>> maps = (List<Map<String,Object>>)JSON.parseObject(orderTecketList,Object.class);

            for(Map<String,Object> map : maps){
                ticketService.ensureGetTickets(map);
            }

            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;

        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    /**
     *
     * @author: xiaoz
     * @2017年7月13日
     * @功能描述:获得支付后信息
     * @param req
     * @return
     */
    @ApiOperation(value = "获得支付后信息")
    @ResponseBody
    @RequestMapping(value = "/selectPayMessageAfterPay",method = RequestMethod.GET)
    public ResponseDbCenter selectPayMessageAfterPay(HttpServletRequest req, HttpServletResponse rsp,
                                                     @ApiParam(value="会务ID",required = false) @RequestParam(required = false) String meetingId,
                                                     @ApiParam(value="订单号",required = false) @RequestParam(required = false) String mhtOrderNo
                                                     ) throws  Exception{

        if(StringUtils.isBlank(meetingId) || StringUtils.isBlank(mhtOrderNo)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        try {

            Map<String,Object> map = ticketService.selectPayMessageAfterPay(meetingId,mhtOrderNo);
            if(map != null ){
                String orderId = (String)map.get("id");
                List<Map<String,Object>> orderTickets = ticketService.selectorderTicketByOderId(orderId);
                map.put("ticketNumbers",orderTickets);
            }

            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(map);
            return responseDbCenter;

        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }

    /**
     *
     * @author: xiaoz
     * @2017年7月13日
     * @功能描述:获得我的支付信息
     * @param req
     * @return
     */
    @ApiOperation(value = "获得我的支付信息")
    @ResponseBody
    @RequestMapping(value = "/selectUserTicketsByToken",method = RequestMethod.GET)
    public ResponseDbCenter selectUserTicketsByToken(HttpServletRequest req, HttpServletResponse rsp,
                                                     @ApiParam(value="token",required = false) @RequestParam(required = false) String token,
                                                     @ApiParam(value="会务ID",required = false) @RequestParam(required = false) String meetingId){



        if(StringUtils.isBlank(token)){
            return ResponseConstants.MISSING_PARAMTER;
        }

       String userId = (String) req.getSession().getAttribute(token);

        try {

            List<Map<String,Object>> userTicketMaps = ticketService.selectUserTicketsByUserId(userId,meetingId);

            for(Map<String,Object> map : userTicketMaps){

                String orderId = (String)map.get("id");
                List<Map<String,Object>> orderTickets = ticketService.selectorderTicketByOderId(orderId);
                map.put("ticketNumbers",orderTickets);
            }

            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(userTicketMaps);
            return responseDbCenter;

        } catch (Exception e) {
            log.error("异常栈:",e);
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

    /**
     *
     * @author: xiaoz
     * @2017年7月13日
     * @功能描述:重新支付、立即支付
     * @param req
     * @return
     */
    @ApiOperation(value = "重新支付")
    @ResponseBody
    @RequestMapping(value = "/payAgain",method = RequestMethod.POST)
    public synchronized ResponseDbCenter payAgain(HttpServletRequest req, HttpServletResponse rsp,
                                                  @ApiParam(value="ticketNumbers",required = false) @RequestParam(required = false) String ticketNumbers,
                                                  @ApiParam(value="会务ID",required = false) @RequestParam(required = false) String meetingId,
                                                  @ApiParam(value="token",required = false) @RequestParam(required = false) String token,
                                                  @ApiParam(value="手机号对应的用户",required = false) @RequestParam(required = false) String payedUserId,
                                                  @ApiParam(value="企业ID",required = false) @RequestParam(required = false) String companyId,
                                                  @ApiParam(value="订单号",required = false) @RequestParam(required = false) String mhtOrderNo,
                                                  @ApiParam(value="推广人",required = false) @RequestParam(required = false) String userSpreadId){



        if(StringUtils.isBlank(token)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        String userIdd = (String) req.getSession().getAttribute(token);

        String openId = userService.selectUserById(userIdd).get("weixin").toString();

        SortedMap<Object, Object> sortedMap = null;

        try {

            Map<String, String> csOrder = ticketService.selectUserOrderByOrderNo(mhtOrderNo);
            String payStatus = csOrder.get("payStatus");
            if("2".equals(payStatus)){
                return ResponseConstants.USER_ORDER_CANCELED;
            }


            List<Map<String, Object>> maps = (List<Map<String, Object>>) JSON.parseObject(ticketNumbers,Object.class);

            boolean ticketFlag = false;
            boolean numberFlag = false;
            long totoPrice = 0;
            String totalTicketMessage = "";
//            String mhtOrderNo = "";
            for (Map<String, Object> map : maps) {

                String ticketId = (String) map.get("id");
                Integer number = (Integer) map.get("number");

                /*
                 * 重新支付，以前的订单就会作废掉，库存隔15分钟后就会减掉，现在要将库存加上来，
                 * 因为订单换了，所以要重新走一趟减少库存，15分钟定时取消、释放库存
                 */
                int ticketNumberInt = number.intValue();
                ticketService.updateTicketSavingById(ticketId,-ticketNumberInt);

                //查出库存
                Integer num = ticketService.getTicketSavingByTicketId(ticketId);
               /* if (num == null || num.intValue() == 0) {
                    return ResponseConstants.TICKET_HAS_SELL_OUT;
                }*/

                int saving = num.intValue();
                int numberInt = number.intValue();

                if (numberInt > saving) {
                    return ResponseConstants.TICKET_NOT_ENOUGHT;
                }

                if(numberInt > 0){
                    numberFlag = true;
                }

                if ((0 == saving)) {
                    continue;
                }


                Map<String, Object> ticketMap = ticketService.selectTicketById(ticketId);

                String ticketType = (String) ticketMap.get("ticketType");
                String price = (String) ticketMap.get("price");

                double doublePrice = Double.valueOf(price);

                totoPrice += doublePrice * (number.intValue());

                totalTicketMessage += ticketType + number.intValue() + "张,";

                ticketFlag = true;
            }

            if(!numberFlag){
                return ResponseConstants.TICKET_NUMBER_ZERO;
            }else if (!ticketFlag) {
                return ResponseConstants.TICKET_HAS_SELL_OUT;
            } else {

                String orderId = UUID.randomUUID().toString();
                if(totoPrice > 0){

                    Map<String, Object> userOrderMap = new HashMap<>();

                    Map<String, Object> weixinUserOrderMap = new HashMap<>();
                    weixinUserOrderMap.put("body", totalTicketMessage);
                    weixinUserOrderMap.put("total_fee", totoPrice);
                    weixinUserOrderMap.put("openId", openId);
                    weixinUserOrderMap.put("NOTIFY_URL", "dbcenter/ticket/savePay");
                    System.out.println("下单openId--------------:" + openId);
                    sortedMap = WeixinPlayUtils.weixinUserOrder(weixinUserOrderMap);
                    //订单号
                    String mhtOrderNoNo = (String) sortedMap.get("mhtOrderNo");

                    userOrderMap.put("id", orderId);
                    userOrderMap.put("userId", userIdd);

                    //加上企业Id
                    userOrderMap.put("companyId", companyId);
                    userOrderMap.put("orderName", totalTicketMessage);
                    userOrderMap.put("mhtOrderNo", mhtOrderNoNo);
                    userOrderMap.put("mhtOrderAmt", totoPrice);
                    userOrderMap.put("mhtOrderStartTime", WeixinPlayUtils.getTimeStamp());
                    userOrderMap.put("orderTime", new Date());
                    userOrderMap.put("payStatus", "0");
                    userOrderMap.put("status", 0);
                    userOrderMap.put("meetingId", meetingId);
                    userOrderMap.put("companyId", companyId);
                    userOrderMap.put("payedUserId", payedUserId);
                    userOrderMap.put("userSpreadId", userSpreadId);


                    orderService.saveUserOrder(userOrderMap);

                    System.out.println("===========maps==========" + maps);
                    //把新的订单号传入到定时任务中
                    OrderOvertimeThread orderOvertimeThread = new OrderOvertimeThread(mhtOrderNoNo);
                    Thread thread = new Thread(orderOvertimeThread);
                    thread.start();
                }

                for (Map<String, Object> map : maps) {

                    String ticketId = (String) map.get("id");
                    Integer number = (Integer) map.get("number");

                    Integer num = ticketService.getTicketSavingByTicketId(ticketId);
                    int saving = num.intValue();
                    int ticketNumberInt = number.intValue();

                    //减少库存
                    if(saving >= ticketNumberInt){
                        ticketService.updateTicketSavingById(ticketId,ticketNumberInt);
                    }else{
                        ticketService.updateTicketSavingById(ticketId,saving);
                    }

                    Map<String, Object> orderTicketMap = new HashMap<>();
                    orderTicketMap.put("id", UUID.randomUUID().toString());
                    orderTicketMap.put("orderId", orderId);
                    orderTicketMap.put("ticketId", ticketId);
                    orderTicketMap.put("ticketNumber", number);
                    System.out.println("===========map==========" + map);

                    orderService.saveOrderTicket(orderTicketMap);

                }

                orderService.deleteOrderBymhtOrderNoByPayAgain(mhtOrderNo);

                ResponseDbCenter responseDbCenter = new ResponseDbCenter();
                responseDbCenter.setResModel(sortedMap);
                return responseDbCenter;
            }
        } catch (Exception e) {
            log.error("异常栈:",e);
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

    /**
     *
     * @author: xiaoz
     * @2017年7月13日
     * @功能描述:获得我的支付信息
     * @param req
     * @return
     */
    @ApiOperation(value = "取消订单")
    @ResponseBody
    @RequestMapping(value = "/cancelOrder",method = RequestMethod.GET)
    public ResponseDbCenter cancelOrder(HttpServletRequest req, HttpServletResponse rsp,
                                        @ApiParam(value="订单ID",required = false) @RequestParam(required = false) String id)
                                                            throws Exception{


        try {

            Map<String, String> order = ticketService.selectUserOrderById(id);
            String meetingId = order.get("meetingId");
            String payedUserId = order.get("payedUserId");

            Map<String,String> csOrder = new HashMap<>();

            csOrder.put("id",id);
            csOrder.put("payTime", DateUtils.currtime());
            csOrder.put("payStatus","2");
            ticketService.updateUserTicketById(csOrder);

            List<Map<String,Object>> maps = ticketService.selectTicketNumberByOderId(id);
            for(Map<String,Object> map : maps){
                String ticketId = (String)map.get("ticketId");
                Integer ticketNumber = (Integer)map.get("ticketNumber");
                Integer saving = (Integer)map.get("saving");

                int ticketNumberInt = ticketNumber.intValue();
                ticketService.updateTicketSavingById(ticketId,-ticketNumberInt);

                csOrder.put("payTime", DateUtils.currtime());
                csOrder.put("payStatus","2");
                ticketService.updateUserTicketById(csOrder);

            }

            List<UserAttendMeeting> userAttendMeetings = userAttendMeetingService.selectMeetingUserByUserIdAndMeetingId(meetingId,payedUserId);
            if(userAttendMeetings != null && userAttendMeetings.size() > 0){
                userAttendMeetingService.deleteMeetingUserByIds("id ='"+userAttendMeetings.get(0).getId()+"'");
            }


            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;

        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }



}
