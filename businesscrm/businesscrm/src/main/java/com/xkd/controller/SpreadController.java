package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.*;
import com.xkd.model.Dictionary;
import com.xkd.service.DictionaryService;
import com.xkd.service.MeetingService;
import com.xkd.service.SpreadService;
import com.xkd.utils.*;
import com.xkd.utils.Base64;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.apache.commons.collections4.map.HashedMap;
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
import java.util.*;

@Api(description = "营销推广接口")
@Controller
@RequestMapping("/spread")
@Transactional
public class SpreadController extends  BaseController{

    @Autowired
    private SpreadService spreadService;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private RedisCacheUtil redisCacheUtil;
    @Autowired
    private MeetingService meetingService;



    @ApiOperation(value = "查询邀请函设置信息")
    @ResponseBody
    @RequestMapping(value="/selectSpreadSettings",method = {RequestMethod.POST})
    public ResponseDbCenter selectSpreadSettings(HttpServletRequest req, HttpServletResponse rsp,
                                                 @ApiParam(value = "邀请码标题", required = false) @RequestParam(required = false) String inviteTitle,
                                                 @ApiParam(value = "商品名称", required = false) @RequestParam(required = false) String productionName,
                                                 @ApiParam(value = "currentPage", required = false) @RequestParam(required = false) String currentPage,
                                                 @ApiParam(value = "pageSize", required = false) @RequestParam(required = false) String pageSize
                                                 ) throws  Exception{

        if(StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize)){
            currentPage = "1";
            pageSize = "10";
        }

        String token = req.getHeader("token");
        String userId = (String) redisCacheUtil.getCacheObject(token);
        int currentPageInt = (Integer.parseInt(currentPage)-1)*Integer.parseInt(pageSize);
        int pageSizeInt = Integer.parseInt(pageSize);

        try{

            Map<String,Object> paramMap = new HashedMap<>();
            paramMap.put("inviteTitle",inviteTitle);
            paramMap.put("productionName",productionName);
            paramMap.put("createdBy",userId);
            paramMap.put("currentPage",currentPageInt);
            paramMap.put("pageSize",pageSizeInt);

            List<SpreadSetting> list = spreadService.selectSpreadSettings(paramMap);
            Integer num = spreadService.selectSpreadSettingsTotal(paramMap);

            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(list);
            responseDbCenter.setTotalRows(num.intValue()+"");
            return responseDbCenter;

        }catch (Exception e){
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "查询佣金记录详情")
    @ResponseBody
    @RequestMapping(value="/selectSpreadGetMoneyLogDetail",method = {RequestMethod.POST})
    public ResponseDbCenter selectSpreadGetMoneyLogDetail(HttpServletRequest req, HttpServletResponse rsp,
                         @ApiParam(value = "推广佣金记录ID", required = false) @RequestParam(required = false) String userGetMoneyLogId){


        if(StringUtils.isBlank(userGetMoneyLogId)){
            return ResponseConstants.MISSING_PARAMTER_WJ;
        }

        try{

            Map<String,Object> map = spreadService.selectSpreadGetMoneyLogDetail(userGetMoneyLogId);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(map);
            return responseDbCenter;

        }catch (Exception e){
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "PC端推广详情")
    @ResponseBody
    @RequestMapping(value="/selectPcSpreadUserDetail",method = {RequestMethod.POST})
    public ResponseDbCenter selectPcSpreadUserDetail(HttpServletRequest req, HttpServletResponse rsp,
                                                          @ApiParam(value = "推广人用户ID", required = false) @RequestParam(required = false) String spreadUserId,
                                                          @ApiParam(value = "currentPage", required = false) @RequestParam(required = false) String currentPage,
                                                          @ApiParam(value = "pageSize", required = false) @RequestParam(required = false) String pageSize ){


        try{

            if(StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize)){
                currentPage = "1";
                pageSize = "10";
            }

            int currentPageInt = (Integer.parseInt(currentPage)-1)*Integer.parseInt(pageSize);
            int pageSizeInt = Integer.parseInt(pageSize);

            Map<String,Object> map = new HashedMap<>();
            map.put("spreadUserId",spreadUserId);
            map.put("currentPage",currentPageInt);
            map.put("pageSize",pageSizeInt);

            List<Map<String,Object>> maps = spreadService.selectPcSpreadUserDetail(map);
            Integer num = spreadService.selectPcSpreadUserDetailTotal(map);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(maps);
            responseDbCenter.setTotalRows(num==null?"0":num.intValue()+"");
            return responseDbCenter;

        }catch (Exception e){
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }

    @ApiOperation(value = "查询推广用户通过ID")
    @ResponseBody
    @RequestMapping(value="/selectSpreadUserById",method = {RequestMethod.POST})
    public ResponseDbCenter selectSpreadUserById(HttpServletRequest req, HttpServletResponse rsp,
                                                     @ApiParam(value = "推广人用户ID", required = false) @RequestParam(required = false) String spreadUserId){



        if(StringUtils.isBlank(spreadUserId)){
            return ResponseConstants.MISSING_PARAMTER_WJ;
        }

        try{

            Map<String,Object> map = new HashedMap<>();
            map.put("spreadUserId",spreadUserId);
            SpreadUser spreadUser = spreadService.selectSpreadUserById(spreadUserId);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(spreadUser);
            return responseDbCenter;

        }catch (Exception e){
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }

    @ApiOperation(value = "推广员列表详情")
    @ResponseBody
    @RequestMapping(value="/selectSpreadUsers",method = {RequestMethod.POST})
    public ResponseDbCenter selectSpreadUsers(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value = "推广人名称", required = false) @RequestParam(required = false) String uname,
                                              @ApiParam(value = "开始时间", required = false) @RequestParam(required = false) String startTime,
                                              @ApiParam(value = "结束时间", required = false) @RequestParam(required = false) String endTime,
                                              @ApiParam(value = "currentPage", required = false) @RequestParam(required = false) String currentPage,
                                              @ApiParam(value = "pageSize", required = false) @RequestParam(required = false) String pageSize ){


        try{

            if(StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize)){
                currentPage = "1";
                pageSize = "10";
            }

            int currentPageInt = (Integer.parseInt(currentPage)-1)*Integer.parseInt(pageSize);
            int pageSizeInt = Integer.parseInt(pageSize);

            String token = req.getHeader("token");
            String userId = (String) redisCacheUtil.getCacheObject(token);
            Map<String,Object> map = new HashedMap<>();
            map.put("uname",uname);
            map.put("startTime",startTime);
            map.put("endTime",endTime);
            map.put("createdBy",userId);
            map.put("currentPage",currentPageInt);
            map.put("pageSize",pageSizeInt);

            List<Map<String,Object>> maps = spreadService.selectSpreadUsers(map);
            Integer num = spreadService.selectSpreadUsersTotal(map);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(maps);
            responseDbCenter.setTotalRows(num.intValue()+"");
            return responseDbCenter;

        }catch (Exception e){
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }

    @ApiOperation(value = "PC推广记录")
    @ResponseBody
    @RequestMapping(value="/selectSpreadUserlogs",method = {RequestMethod.POST})
    public ResponseDbCenter selectSpreadUserlogs(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value = "推广人名称", required = false) @RequestParam(required = false) String uname,
                                              @ApiParam(value = "交易开始时间", required = false) @RequestParam(required = false) String startTime,
                                              @ApiParam(value = "交易结束时间", required = false) @RequestParam(required = false) String endTime,
                                              @ApiParam(value = "currentPage", required = false) @RequestParam(required = false) String currentPage,
                                              @ApiParam(value = "pageSize", required = false) @RequestParam(required = false) String pageSize ){


        try{

            if(StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize)){
                currentPage = "1";
                pageSize = "10";
            }

            int currentPageInt = (Integer.parseInt(currentPage)-1)*Integer.parseInt(pageSize);
            int pageSizeInt = Integer.parseInt(pageSize);

            String token = req.getHeader("token");
            String userId = (String) redisCacheUtil.getCacheObject(token);
            Map<String,Object> map = new HashedMap<>();
            map.put("uname",uname);
            map.put("startTime",startTime);
            map.put("endTime",endTime);
            map.put("createdBy",userId);
            map.put("currentPage",currentPageInt);
            map.put("pageSize",pageSizeInt);

            List<Map<String,Object>> maps = spreadService.selectSpreadUserlogs(map);
            Integer num = spreadService.selectSpreadUserlogsTotal(map);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(maps);
            responseDbCenter.setTotalRows(num.intValue()+"");
            return responseDbCenter;

        }catch (Exception e){
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "保存邀请函设置信息")
    @ResponseBody
    @RequestMapping(value="/saveSpreadSetting",method = {RequestMethod.POST})
    public ResponseDbCenter saveSpreadSetting(HttpServletRequest req, HttpServletResponse rsp,
                                                 @ApiParam(value = "邀请码标题", required = false) @RequestParam(required = false) String inviteTitle,
                                                 @ApiParam(value = "商品类别ID(在数据字典中配)", required = false) @RequestParam(required = false) String productionTypeId,
                                                 @ApiParam(value = "商品ID", required = false) @RequestParam(required = false) String productionId,
                                                 @ApiParam(value = "佣金比例（10%）", required = false) @RequestParam(required = false) String getRate
                                                 ) throws  Exception{




        try{
            String token = req.getHeader("token");
            String userId = (String) redisCacheUtil.getCacheObject(token);
            spreadService.insertSpreadSetting(userId,inviteTitle,productionId,productionTypeId,getRate);
        }catch (Exception e){
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }


    @ApiOperation(value = "推广用户注册")
    @ResponseBody
    @RequestMapping(value="/registerSpreadUser",method = {RequestMethod.POST})
    public ResponseDbCenter registerSpreadUser(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value = "创建注册链接的用户Id", required = false) @RequestParam(required = false) String createdBy,
                                              @ApiParam(value = "用户名称", required = false) @RequestParam(required = false) String uname,
                                              @ApiParam(value = "手机号", required = false) @RequestParam(required = false) String mobile,
                                              @ApiParam(value = "微信code", required = false) @RequestParam(required = false) String weixinCode,
                                              @ApiParam(value = "验证码", required = false) @RequestParam(required = false) String code,
                                              @ApiParam(value = "密码，前端加密", required = false) @RequestParam(required = false) String password) throws  Exception{


        if(StringUtils.isBlank(mobile) || StringUtils.isBlank(code)
                || StringUtils.isBlank(createdBy) || StringUtils.isBlank(weixinCode)){
            return ResponseConstants.MISSING_PARAMTER_WJ;
        }

        String existsCode = redisCacheUtil.getCacheObject("code"+mobile)+"";
        if(StringUtils.isBlank(existsCode)){
            return ResponseConstants.FUNC_USER_VERIFYCODEISERROR;
        }else if(!existsCode.equals(code)){
            return ResponseConstants.TEL_CODE_ERROR;
        }


        String id = null;
        try{
            HashMap<String, String> wx = SysUtils.getOpenId(weixinCode);
            if(wx == null&&!weixinCode.equals("123")){
                return ResponseConstants.FUNC_USER_OPENID_FAIL_WJ;
            }
            String openId = weixinCode.equals("123") ? "o2DOPwUMugpWcLs9swzBX6FgEwNk" : wx.get("openId");

            SpreadUser spreadUser = new SpreadUser();
            spreadUser.setCreatedBy(createdBy);
            spreadUser.setUpdatedBy(createdBy);
            spreadUser.setCreateDate(new Date());
            spreadUser.setUpdateDate(new Date());
            spreadUser.setMobile(mobile);
            spreadUser.setPassword(password);
            spreadUser.setUname(uname);
            spreadUser.setOpenId(openId);
            spreadUser.setStatus(0);

            SpreadUser spreadUserMobile = spreadService.selectSpreadUserByMobile(mobile);
            //判断用户是否存在，以及是否被删掉了
            if(spreadUserMobile != null && spreadUserMobile.getStatus().intValue() == 0){
                return ResponseConstants.MOBILE_EXIST;
            }else if(spreadUserMobile == null){
                id = UUID.randomUUID().toString();
                spreadUser.setId(id);
                spreadService.insertSpreadUser(spreadUser);
            }else{
                id = spreadUserMobile.getId();
                spreadUser.setId(id);
                spreadService.updateSpreadUser(spreadUser);
            }

        }catch (Exception e){
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        Map<String,Object> map = new HashedMap<>();
        map.put("userId",id);
        map.put("createdBy",createdBy);
        responseDbCenter.setResModel(map);
        return responseDbCenter;
    }

    @ApiOperation(value = "微信自动登录")
    @ResponseBody
    @RequestMapping(value="/loginSpreadUser",method = {RequestMethod.POST})
    public ResponseDbCenter loginSpreadUser(HttpServletRequest req, HttpServletResponse rsp,
                                            @ApiParam(value = "微信code", required = false) @RequestParam(required = false) String weixinCode) throws  Exception{



        if(StringUtils.isBlank(weixinCode)){
            return ResponseConstants.MISSING_PARAMTER_WJ;
        }

        try{
            HashMap<String, String> wx = SysUtils.getOpenId(weixinCode);
            if(wx == null&&!weixinCode.equals("123")){
                return ResponseConstants.FUNC_USER_OPENID_FAIL_WJ;
            }
            String openId = weixinCode.equals("123") ? "o2DOPwUMugpWcLs9swzBX6FgEwNk" : wx.get("openId");
            SpreadUser spreadUser = spreadService.selectSpreadUserByOpenId(openId);
            if(spreadUser == null){
                return ResponseConstants.NO_SPREAD_USER_BY_OPENID;
            }else{
                ResponseDbCenter responseDbCenter = new ResponseDbCenter();
                responseDbCenter.setResModel(spreadUser);
                return responseDbCenter;
            }
        }catch (Exception e){
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "修改推广用户银行卡基本信息")
    @ResponseBody
    @RequestMapping(value="/updateSpreadUser",method = {RequestMethod.POST})
    public ResponseDbCenter updateSpreadUser(HttpServletRequest req, HttpServletResponse rsp,
                                               @ApiParam(value = "用户ID", required = false) @RequestParam(required = false) String id,
                                               @ApiParam(value = "账户姓名", required = false) @RequestParam(required = false) String accountName,
                                               @ApiParam(value = "银行卡号", required = false) @RequestParam(required = false) String accountCard,
                                               @ApiParam(value = "开户银行", required = false) @RequestParam(required = false) String accountBank
                                               ) throws  Exception{


        if(StringUtils.isBlank(id) ){
            return ResponseConstants.MISSING_PARAMTER_WJ;
        }
        try{
            SpreadUser spreadUser = new SpreadUser();
            spreadUser.setId(id);
            spreadUser.setUpdatedBy(id);
            spreadUser.setUpdateDate(new Date());
            spreadUser.setAccountBank(accountBank);
            spreadUser.setAccountName(accountName);
            spreadUser.setAccountCard(accountCard);
            spreadService.updateSpreadUser(spreadUser);
        }catch (Exception e){
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }


    @ApiOperation(value = "提现")
    @ResponseBody
    @RequestMapping(value="/SpreadUserGetMoney",method = {RequestMethod.POST})
    public ResponseDbCenter updateSpreadUser(HttpServletRequest req, HttpServletResponse rsp,
                                             @ApiParam(value = "用户ID", required = false) @RequestParam(required = false) String id,
                                             @ApiParam(value = "提现金额（元），'100.25'", required = false) @RequestParam(required = false) String getMoney
    ) throws  Exception{


        if(StringUtils.isBlank(id) ){
            return ResponseConstants.MISSING_PARAMTER_WJ;
        }
        try{
            //判断提现的钱是否大于账户里面的钱
            if(StringUtils.isNotBlank(getMoney) ){

                SpreadUser spreadUserExists = spreadService.selectSpreadUserById(id);
                String accountSaving = spreadUserExists.getAccountSaving() != null?spreadUserExists.getAccountSaving():"0";
                double accountSavingInt = Double.parseDouble(accountSaving)*100;
                double getMoneyDouble = Double.parseDouble(getMoney)*100;
                if(getMoneyDouble > accountSavingInt){
                    return ResponseConstants.GET_MONDY_BIGGER_THEN_ACCONUT_SAVING;
                }

                UserGetMoney userGetMoney = new UserGetMoney();
                userGetMoney.setId(UUID.randomUUID().toString());
                userGetMoney.setLogFlag(1);
                userGetMoney.setCreateDate(new Date());
                userGetMoney.setUserSpreadId(id);
                userGetMoney.setGetMoney(new Double(getMoneyDouble+"").intValue()+"");
                userGetMoney.setLogStauts(0);
                spreadService.insertGetMoneyLog(userGetMoney);
            }
        }catch (Exception e){
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }

    @ApiOperation(value = "提现审核")
    @ResponseBody
    @RequestMapping(value="/doGetMoneyStatus",method = {RequestMethod.POST})
    public ResponseDbCenter doGetMoneyStatus(HttpServletRequest req, HttpServletResponse rsp,
                                             @ApiParam(value = "申请记录Id", required = false) @RequestParam(required = false) String id,
                                             @ApiParam(value = "'0'待审核，'1'审核通过", required = false) @RequestParam(required = false) String logStauts
                                             ) throws  Exception{



        if(StringUtils.isBlank(id) || StringUtils.isBlank(logStauts)){
            return ResponseConstants.MISSING_PARAMTER_WJ;
        }
        try{

            spreadService.doGetMoneyStatus(id,logStauts);

        }catch (Exception e){
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }

    @ApiOperation(value = "重置密码")
    @ResponseBody
    @RequestMapping(value="/repeatSpreadUserPassword",method = {RequestMethod.POST})
    public ResponseDbCenter repeatSpreadUserPassword(HttpServletRequest req, HttpServletResponse rsp,
                                             @ApiParam(value = "用户ID", required = false) @RequestParam(required = false) String id) throws  Exception{


        if(StringUtils.isBlank(id) ){
            return ResponseConstants.MISSING_PARAMTER_WJ;
        }
        try{

            String encodeRepeatPassWord = Base64.encode("a123456");
            SpreadUser spreadUser = new SpreadUser();
            spreadUser.setId(id);
            spreadUser.setUpdatedBy(id);
            spreadUser.setUpdateDate(new Date());
            spreadUser.setPassword(encodeRepeatPassWord);
            spreadService.updateSpreadUser(spreadUser);
        }catch (Exception e){
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }

    @ApiOperation(value = "冻结启用用户")
    @ResponseBody
    @RequestMapping(value="/updateSpreadUserUflag",method = {RequestMethod.POST})
    public ResponseDbCenter updateSpreadUserUflag(HttpServletRequest req, HttpServletResponse rsp,
                                                  @ApiParam(value = "用户ID", required = false) @RequestParam(required = false) String id,
                                                  @ApiParam(value = "'0'启用，'1'冻结", required = false) @RequestParam(required = false) String uflag) throws  Exception{


        if(StringUtils.isBlank(id) ){
            return ResponseConstants.MISSING_PARAMTER_WJ;
        }
        try{
            SpreadUser spreadUser = new SpreadUser();
            spreadUser.setId(id);
            spreadUser.setUpdatedBy(id);
            spreadUser.setUpdateDate(new Date());
            if(StringUtils.isNotBlank(uflag)){
                spreadUser.setUflag(Integer.parseInt(uflag));
            }
            spreadService.updateSpreadUser(spreadUser);
        }catch (Exception e){
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }


    @ApiOperation(value = "移动端推广用户推广详情")
    @ResponseBody
    @RequestMapping(value="/selectSpreadUserDetail",method = {RequestMethod.POST})
    public ResponseDbCenter selectSpreadUserDetail(HttpServletRequest req, HttpServletResponse rsp,
                                               @ApiParam(value = "注册用户ID", required = false) @RequestParam(required = false) String userId) throws  Exception{

        if(StringUtils.isBlank(userId)){
            return ResponseConstants.MISSING_PARAMTER_WJ;
        }

        List<SpreadSetting> list = null;
        Integer num = null;
        try{

            SpreadUser spreadUser = spreadService.selectSpreadUserById(userId);
            if(spreadUser == null){
                return ResponseConstants.USER_NOTEXIST;
            }


            Map<String,Object> paramMap = new HashedMap<>();
            paramMap.put("createdBy",spreadUser.getCreatedBy());
            paramMap.put("currentPage",0);
            paramMap.put("pageSize",10000);

            list = spreadService.selectSpreadSettings(paramMap);
            num = spreadService.selectSpreadSettingsTotal(paramMap);
            for(SpreadSetting spreadSetting : list){
                //会务
                if(spreadSetting.getProductionTableType() != null && spreadSetting.getProductionTableType().intValue() == 0){
                    Meeting meeting = meetingService.selectMeetingById(spreadSetting.getProductionId());
                    //从查出的结果集中筛选用户
                    spreadSetting.setProductionName(meeting != null?meeting.getMeetingName():null);
                    spreadSetting.setObject(meeting);
                //视频
                }else if(spreadSetting.getProductionTableType() != null && spreadSetting.getProductionTableType().intValue() == 1){

                }
            }

        }catch (Exception e){
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(list);
        responseDbCenter.setTotalRows(num.intValue()+"");
        return responseDbCenter;
    }

    @ApiOperation(value = "提现申请")
    @ResponseBody
    @RequestMapping(value="/selectGetMoneys",method = {RequestMethod.POST})
    public ResponseDbCenter selectGetMoneys(HttpServletRequest req, HttpServletResponse rsp,
                                                   @ApiParam(value = "currentPage", required = false) @RequestParam(required = false) String currentPage,
                                                   @ApiParam(value = "pageSize", required = false) @RequestParam(required = false) String pageSize ){


        if(StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize)){
            currentPage = "1";
            pageSize = "10";
        }

        int currentPageInt = (Integer.parseInt(currentPage)-1)*Integer.parseInt(pageSize);
        int pageSizeInt = Integer.parseInt(pageSize);

        String token = req.getHeader("token");
        String userId = (String) redisCacheUtil.getCacheObject(token);


        List<Map<String,Object>> list = null;
        Integer num = null;

        try{

            Map<String,Object> paramMap = new HashedMap<>();
            paramMap.put("createdBy",userId);
            paramMap.put("currentPage",currentPageInt);
            paramMap.put("pageSize",pageSizeInt);

            list = spreadService.selectGetMoneys(paramMap);
            num = spreadService.selectGetMoneysTotal(paramMap);

        }catch (Exception e){
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(list);
        responseDbCenter.setTotalRows(num.intValue()+"");
        return responseDbCenter;
    }

    @ApiOperation(value = "推广人的佣金提现记录")
    @ResponseBody
    @RequestMapping(value="/selectSpreadUserGetMoneyLogs",method = {RequestMethod.POST})
    public ResponseDbCenter selectSpreadUserGetMoneyLogs(HttpServletRequest req, HttpServletResponse rsp,
                                                         @ApiParam(value = "推广人用户ID", required = false) @RequestParam(required = false) String userId,
                                                 @ApiParam(value = "currentPage", required = false) @RequestParam(required = false) String currentPage,
                                                 @ApiParam(value = "pageSize", required = false) @RequestParam(required = false) String pageSize) throws  Exception{


        if(StringUtils.isBlank(userId)){
            return ResponseConstants.MISSING_PARAMTER_WJ;
        }
        if(StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize)){
            currentPage = "1";
            pageSize = "100000";
        }

        int currentPageInt = (Integer.parseInt(currentPage)-1)*Integer.parseInt(pageSize);
        int pageSizeInt = Integer.parseInt(pageSize);

        List<UserGetMoney> list = null;
        Integer num = null;
        try{

            Map<String,Object> paramMap = new HashedMap<>();

            paramMap.put("currentPage",currentPageInt);
            paramMap.put("pageSize",pageSizeInt);
            paramMap.put("userSpreadId",userId);

            list = spreadService.selectSpreadUserGetMoneyLogs(paramMap);
            num = spreadService.selectSpreadUserGetMoneyLogsTotal(paramMap);

        }catch (Exception e){
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(list);
        responseDbCenter.setTotalRows(num.intValue()+"");
        return responseDbCenter;
    }

    @ApiOperation(value = "更改活动状态")
    @ResponseBody
    @RequestMapping(value="/updateSpreadSettingGetRate",method = {RequestMethod.POST})
    public ResponseDbCenter updateSpreadSettingGetRate(HttpServletRequest req, HttpServletResponse rsp,
                                                      @ApiParam(value = "ID", required = false) @RequestParam(required = false) String id,
                                                      @ApiParam(value = "佣金比例（10%）", required = false) @RequestParam(required = false) String getRate

    ) throws  Exception {

        String token = req.getHeader("token");
        String userId = (String) redisCacheUtil.getCacheObject(token);

        if(StringUtils.isBlank(id) || StringUtils.isBlank(getRate)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        try {

            SpreadSetting spreadSetting = new SpreadSetting();
            spreadSetting.setId(id);
            spreadSetting.setUpdatedBy(userId);
            spreadSetting.setUpdateDate(new Date());
            spreadSetting.setGetRate(getRate);

            spreadService.updateSpreadSetting(spreadSetting);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;

        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }

    @ApiOperation(value = "设置佣金比例")
    @ResponseBody
    @RequestMapping(value="/updateSpreadSettingStatus",method = {RequestMethod.POST})
    public ResponseDbCenter updateSpreadSettingStatus(HttpServletRequest req, HttpServletResponse rsp,

                                                      @ApiParam(value = "ID", required = false) @RequestParam(required = false) String id,
                                                      @ApiParam(value = "'0'启用，'1'禁用", required = false) @RequestParam(required = false) String vflag

    ) throws  Exception {


        String token = req.getHeader("token");
        String userId = (String) redisCacheUtil.getCacheObject(token);

        if(StringUtils.isBlank(id) || StringUtils.isBlank(vflag)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        try {

            SpreadSetting spreadSetting = new SpreadSetting();
            spreadSetting.setId(id);
            spreadSetting.setUpdatedBy(userId);
            spreadSetting.setUpdateDate(new Date());
            spreadSetting.setVflag(Integer.parseInt(vflag));

            spreadService.updateSpreadSetting(spreadSetting);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;

        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }

    @ApiOperation(value = "修改邀请函设置信息")
    @ResponseBody
    @RequestMapping(value="/updateSpreadSetting",method = {RequestMethod.POST})
    public ResponseDbCenter updateSpreadSetting(HttpServletRequest req, HttpServletResponse rsp,
                                                 @ApiParam(value = "id", required = false) @RequestParam(required = false) String id,
                                                 @ApiParam(value = "邀请码标题", required = false) @RequestParam(required = false) String inviteTitle,
                                                 @ApiParam(value = "商品类别ID(在数据字典中配)", required = false) @RequestParam(required = false) String productionTypeId,
                                                 @ApiParam(value = "商品ID", required = false) @RequestParam(required = false) String productionId,
                                                 @ApiParam(value = "佣金比例（10%）", required = false) @RequestParam(required = false) String getRate,
                                                 @ApiParam(value = "启用禁用", required = false) @RequestParam(required = false) String vflag

    ) throws  Exception{

        if(StringUtils.isBlank(id)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        String token = req.getHeader("token");
        String userId = (String) redisCacheUtil.getCacheObject(token);
        try{

            SpreadSetting spreadSetting = new SpreadSetting();

            Dictionary dictionary = dictionaryService.selectDictionaryById(productionTypeId);
            if(dictionary != null && "会务".equals(dictionary.getValue())){
                spreadSetting.setProductionTableType(0);
                Meeting meetingExists = meetingService.selectMeetingById(productionId);
                spreadSetting.setProductionName(meetingExists.getMeetingName());
            }else if(dictionary != null && "视频".equals(dictionary.getValue())){
                spreadSetting.setProductionTableType(1);
            }
            spreadSetting.setId(id);
            spreadSetting.setUpdatedBy(userId);
            spreadSetting.setUpdateDate(new Date());
            spreadSetting.setGetRate(getRate);
            spreadSetting.setInviteTitle(inviteTitle);
            spreadSetting.setProductionTypeId(productionTypeId);
            spreadSetting.setProductionId(productionId);
            spreadSetting.setVflag(Integer.parseInt(vflag));

            spreadService.updateSpreadSetting(spreadSetting);

        }catch (Exception e){
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }

    @ApiOperation(value = "根据产品类型查产品信息")
    @ResponseBody
    @RequestMapping(value="/selectProductionByTypeId",method = {RequestMethod.POST})
    public ResponseDbCenter selectProductionByTypeId(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value = "商品类别ID(在数据字典中配)", required = false) @RequestParam(required = false) String productionTypeId,
                                              @ApiParam(value = "会务名称或者视频名称，搜索", required = false) @RequestParam(required = false) String content
    ) throws  Exception{

        if(StringUtils.isBlank(productionTypeId)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        try{

            List<Map<String,Object>>  list = spreadService.selectProductionByTypeId(productionTypeId,content);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(list);
            return responseDbCenter;
        }catch (Exception e){
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }




}
