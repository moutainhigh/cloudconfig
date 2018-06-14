package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.*;
import com.xkd.utils.DateUtils;
import com.xkd.utils.SmsApi;
import com.xkd.utils.SysUtils;
import com.xkd.utils.WeixinPlayUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Controller
@RequestMapping("/userPay")
@Transactional
public class PayUserController extends BaseController {

    @Autowired
    private UserPayService userPayService;

    @ResponseBody
    @RequestMapping("/getUserOpenIdAndPayStatus")
    public ResponseDbCenter getUserOpenId(HttpServletRequest req,String code){
        ResponseDbCenter db  = new ResponseDbCenter();
        HashMap<String, String> wx = SysUtils.getOpenId(code);
        String openId = wx.get("openId");
        Map<String, String> userPay = userPayService.getUserPayByOpenId(openId);
        if(null == userPay){
            userPay = new HashMap<>();
            userPay.put("openId",openId);
            userPay.put("payStatus","0");
            Map<String,String> userInfo = SysUtils.getWeiXinInfoByOpenid(wx.get("wxToken"),openId);
            userInfo.put("id", UUID.randomUUID().toString());
            userPay.put("id",userInfo.get("id"));
            userPayService.saveUserPay(userInfo);
        }
        db.setResModel(userPay);
        return db;
    }

    @ResponseBody
    @RequestMapping("/goPay")
    public ResponseDbCenter goPay(HttpServletRequest req,String openId) {
        ResponseDbCenter db  = new ResponseDbCenter();
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("body","3月27-29《资本之道》新春特惠");
            map.put("total_fee",998000);//998000
            map.put("openId",openId);
            map.put("NOTIFY_URL","dbcenter/userPay/savePay");
            db.setResModel(WeixinPlayUtils.weixinUserOrder(map));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return db;
    }

    //支付成功后回调
    @ResponseBody
    @RequestMapping("/savePay")
    public void savePay(HttpServletRequest req,HttpServletResponse res,@RequestBody String result) throws Exception{
        System.out.println("---------------------支付成功后回调:"+result);
        String result_code = result.substring(result.indexOf("<result_code><![CDATA[")+22, result.indexOf("]]></result_code>"));//支付是否成功
        String openid = result.substring(result.indexOf("<openid><![CDATA[")+17, result.indexOf("]]></openid>"));//用户openid
        String out_trade_no = result.substring(result.indexOf("<out_trade_no><![CDATA[")+23, result.indexOf("]]></out_trade_no>"));//自己提交给微信的订单号
        String transaction_id = result.substring(result.indexOf("<transaction_id><![CDATA[")+25, result.indexOf("]]></transaction_id>"));//微信的订单号

        System.out.println("结果："+result_code+"  "+openid+" "+out_trade_no+" "+transaction_id);
        String xml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        if("FAIL".equals(result_code)){
            xml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[支付失败]]></return_msg></xml>";
        }else if(StringUtils.isBlank(result_code) || StringUtils.isBlank(openid) ||  StringUtils.isBlank(out_trade_no) || StringUtils.isBlank(transaction_id)){
            xml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[参数格式校验错误]]></return_msg></xml>";
        }else{
            Map<String, String> csOrder = userPayService.getUserPayByOpenId(openid);
            csOrder.put("transaction_id", transaction_id);
            csOrder.put("out_trade_no", out_trade_no);
            csOrder.put("payStatus","1");
            userPayService.saveUserPayLogger(csOrder);
            csOrder.put("payTime", DateUtils.currtime());
            userPayService.editUserPay(csOrder);
        }
        res.getOutputStream().write(xml.getBytes());
    }

    @ResponseBody
    @RequestMapping("/saveUserPayInfo")
    public ResponseDbCenter saveUserPayInfo(HttpServletRequest req,String uname,String mobile,String openId,String code) {
        String telCode = req.getSession().getAttribute("code"+mobile)+"";
        if(StringUtils.isBlank(code) || StringUtils.isBlank(mobile) || !code.equals(telCode)){
            return  ResponseConstants.TEL_CODE_ERROR;
        }
        Map<String, String> adviser = userPayService.getUserPayAdviser(mobile);
        Map<String, String> csOrder = userPayService.getUserPayByOpenId(openId);
        csOrder.put("uname",uname);
        csOrder.put("mobile",mobile);
        String adviserName = "";
        if(adviser != null){
            adviserName = StringUtils.isNotBlank(adviser.get("adviserName")) ? adviser.get("adviserName"):"";
            csOrder.put("adviserName",adviserName);
            if(StringUtils.isNotBlank(adviser.get("adviserMobile"))){
                SmsApi.sendSms(adviser.get("adviserMobile"),"【资本之道】亲爱的"+adviserName+"老师，恭喜您又有新的订单了！您的客户："+uname+"，联系方式："+mobile+"，已成功预定专项课程。请迅速与您的客户取得联系，并确认尾款及选购课程。辛苦啦，加油！");
            }
        }
        if(StringUtils.isNotBlank(adviserName)){
            SmsApi.sendSms(mobile,"【资本之道】尊敬的"+uname+"，恭喜您成功预定博得世纪专项课程！您的专属顾问："+adviserName+"，稍后将与您联系，为您竭诚服务，请保持手机通畅。感谢您对博得世纪的支持与信任！");
        }

        userPayService.editUserPay(csOrder);
        return  ResponseConstants.SUCCESS;
    }

    @ResponseBody
    @RequestMapping("/getAllAdviser")
    public ResponseDbCenter getAllAdviser(HttpServletRequest req) {
        ResponseDbCenter dbCenter = new ResponseDbCenter();
        dbCenter.setResModel(userPayService.getAllAdviser());
        return  dbCenter;
    }

    @ResponseBody
    @RequestMapping("/getPayUserList")
    public ResponseDbCenter getPayUserList(HttpServletRequest req,String adviserName) {
        ResponseDbCenter dbCenter = new ResponseDbCenter();
        dbCenter.setResModel(userPayService.getPayUserList(adviserName));
        return  dbCenter;
    }

    @ResponseBody
    @RequestMapping("/setCourseOrAdviserName")
    public ResponseDbCenter setCourseOrAdviserName(HttpServletRequest req,String openId,String course,String adviserName) {
        if(StringUtils.isBlank(course) && StringUtils.isBlank(adviserName)){
            return ResponseConstants.MISSING_PARAMTER;
        }
        Map<String, String> csOrder = userPayService.getUserPayByOpenId(openId);
        if(StringUtils.isNotBlank(course)){
            csOrder.put("course",course);
        }
        if(StringUtils.isNotBlank(adviserName)){
            csOrder.put("adviserName",adviserName);
        }
        userPayService.editUserPay(csOrder);
        return  ResponseConstants.SUCCESS;
    }

    @ResponseBody
    @RequestMapping("/writePayUserListWord")
    public ResponseDbCenter writePayUserListWord(HttpServletRequest req,String adviserName) {
        ResponseDbCenter dbCenter = new ResponseDbCenter();
        dbCenter.setResModel(userPayService.writePayUserListWord(userPayService.getPayUserList(adviserName)));
        return  dbCenter;
    }
}
