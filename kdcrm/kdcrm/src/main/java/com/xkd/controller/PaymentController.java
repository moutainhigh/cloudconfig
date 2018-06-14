package com.xkd.controller;

import java.sql.Struct;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xkd.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xkd.exception.GlobalException;
import com.xkd.model.Payment;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;

/**
 * 创建人:巫建辉
 * 创建时间：2017-11-23
 * 功能描述：付款记录相关的功能
 */
@Api(description = "付款相关接口")
@Controller
@RequestMapping("/payment")
@Transactional
public class PaymentController extends BaseController {

    @Autowired
    private PaymentService paymentService;


    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserDynamicService userDynamicService;
    @Autowired
    SolrService solrService;

    @Autowired
    UserDataPermissionService userDataPermissionService;


    /**
     * 添加付款记录
     *
     * @param req
     * @param rsp
     * @return
     */
    @ApiOperation(value = "添加付款")
    @ResponseBody
    @RequestMapping(value = "/groupUpPaymentInfo",method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter groupUpPaymentInfo(HttpServletRequest req, HttpServletResponse rsp,
                                               @ApiParam(value = "公司Id" ,required = true)@RequestParam(required = true)String companyId,
                                               @ApiParam(value = "付款时间",required = true)  @RequestParam(required = true) String paymentDate,
                                               @ApiParam(value = "付款金额",required = true) @RequestParam(required = true) String paymentMoney,
                                               @ApiParam(value = "完款状态",required = true) @RequestParam(required = true) String moneySituation ,
                                               @ApiParam(value = "收款人",required = false) @RequestParam(required = false) String dealPerson  ,
                                               @ApiParam(value = "备注",required = false) @RequestParam(required = false) String  remark  ) {

        String loginUserId = (String) req.getAttribute("loginUserId");



        if (StringUtils.isBlank(companyId)) {

            return ResponseConstants.ILLEGAL_PARAM;
        }


        /**
         * 判断企业是否有权限被修改
         */
        boolean hasPermission=userDataPermissionService.hasPermission(companyId,loginUserId);
        if (!hasPermission){
            return  ResponseConstants.DATA_NOT_PERMITED;
        }

        String totalMoney = paymentService.selectSumMoneyByCompanyId(companyId);
        System.out.println("账户剩余总金额："+totalMoney);
        Payment payment = new Payment();

        try {
            Integer paymentMoneyInYuan = ((Double) (Double.valueOf(paymentMoney) * 10000.0)).intValue();

            if((StringUtils.isNotBlank(moneySituation) && moneySituation.equals("退款") &&( totalMoney == null || Integer.valueOf(totalMoney) < paymentMoneyInYuan ))){
                    return ResponseConstants.NO_TUIKUAN;
            }
            Map<String, Object> company = new HashMap();
            String id = UUID.randomUUID().toString();
            payment.setId(id);
            payment.setMoneySituation(moneySituation);
            payment.setPaymentDate(paymentDate);
            //退款就是负数
            if(moneySituation.equals("退款") && totalMoney!= null){
                //计算退款后还有多少金额
                System.out.println(totalMoney);
                System.out.println(paymentMoneyInYuan);
                int money = Integer.valueOf(totalMoney)-paymentMoneyInYuan;
                System.out.println(money);
                company.put("paymentMoney", money);
                paymentMoneyInYuan = -paymentMoneyInYuan;
            }else{
                company.put("paymentMoney", totalMoney==null?paymentMoneyInYuan:Integer.valueOf(totalMoney)+paymentMoneyInYuan);
            }
            payment.setPaymentMoney(String.valueOf(paymentMoneyInYuan));
            payment.setCompanyId(companyId);
            payment.setUpdatedBy(loginUserId);
            payment.setCreatedBy(loginUserId);
            Date createdDate=new Date();
            payment.setUpdateDate(createdDate);
            payment.setCreateDate(createdDate);

            payment.setDealPerson(dealPerson);
            payment.setRemark(remark);
            paymentService.insertPayment(payment);




            company.put("id", companyId);
            company.put("paymentId", id);
            company.put("paymentDate", paymentDate);
            company.put("moneySituation", moneySituation);

            companyService.updateCompanyInfoById(company);

            String status = moneySituation.equals("退款")?"退款":"付款";
            //添加动态
            userDynamicService.addUserDynamic(loginUserId, companyId, "", "添加", "添加了"+status+"记录，"+status+"金额：" + paymentMoneyInYuan + "元,"+status+"时间：" + paymentDate + ",完款状态：" + moneySituation, 0,null,null,null);

            companyService.updateInfoScore((String) company.get("id"));
            solrService.updateCompanyIndex((String) company.get("id"));

        } catch (Exception e) {

            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }

    /**
     * 删除付款记录
     *
     * @param req
     * @param rsp
     * @param id  记录Id
     * @return
     */
    @ApiOperation(value = "删除付款")
    @ResponseBody
    @RequestMapping(value = "/deletePayment", method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter deletePayment(HttpServletRequest req, HttpServletResponse rsp, String id) {
        String loginUserId = (String) req.getAttribute("loginUserId");

        try {



            Payment payment = paymentService.selectPaymentById(id);



            /**
             * 判断企业是否有权限被修改
             */
            boolean hasPermission=userDataPermissionService.hasPermission(payment.getCompanyId(),loginUserId);
            if (!hasPermission){
                return  ResponseConstants.DATA_NOT_PERMITED;
            }



            paymentService.deletePaymentById(id);

            //获取公司下的所有付款记录
            List<Payment> paymentList=  paymentService.selectPaymentByCompanyId(payment.getCompanyId());


            String totalMoney = paymentService.selectSumMoneyByCompanyId(payment.getCompanyId());
            Map<String, Object> company = new HashMap();
            company.put("id", payment.getCompanyId());
            company.put("paymentMoney", totalMoney == null ? "0" : totalMoney);
            /**
             * 取付款时间最近的一条作为冗余信息进行冗余
             */
            if (paymentList.size()>0){
                company.put("paymentId", paymentList.get(0).getId());
                company.put("paymentDate", paymentList.get(0).getPaymentDate());
                company.put("moneySituation", paymentList.get(0).getMoneySituation());
            }else {
                company.put("moneySituation", "");
            }
            companyService.updateCompanyInfoById(company);
            userDynamicService.addUserDynamic(loginUserId, payment.getCompanyId(), "", "删除", "删除了付款记录，付款金额：" + (payment.getPaymentMoney() == null ? 0 : payment.getPaymentMoney()) + "元,付款时间：" + (payment.getPaymentDate() == null ? "无" : payment.getPaymentDate()) + ",完款状态：" + payment.getMoneySituation(), 0,null,null,null);
            companyService.updateInfoScore((String) company.get("id"));
            solrService.updateCompanyIndex((String) company.get("id"));


        } catch (Exception e) {

            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }


    /**
     * 更新付款记录
     *
     * @param req
     * @param rsp
     * @return
     */
    @ApiOperation(value = "更新付款")
    @ResponseBody
    @RequestMapping(value = "/updatePayment",method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter updatePayment(HttpServletRequest req, HttpServletResponse rsp,
                                          @ApiParam(value = "Id",required = true)  @RequestParam(required = true)  String id,
                                          @ApiParam(value = "公司Id",required = false) @RequestParam(required = false) String companyId,
                                          @ApiParam(value = "付款时间",required = true) @RequestParam(required = true) String paymentDate ,
                                          @ApiParam(value = "金额",required = true) @RequestParam(required = true) String paymentMoney,
                                          @ApiParam(value = "完款状态",required = true)  @RequestParam(required = true) String moneySituation,
                                          @ApiParam(value = "收款人",required = false) @RequestParam(required = false) String dealPerson,
                                          @ApiParam(value = "备注",required = false) @RequestParam(required = false) String remark ) {

        try {


            String loginUserId = (String) req.getAttribute("loginUserId");

            /**
             * 判断企业是否有权限被修改
             */
            boolean hasPermission=userDataPermissionService.hasPermission(companyId,loginUserId);
            if (!hasPermission){
                return  ResponseConstants.DATA_NOT_PERMITED;
            }

            Payment payment = new Payment();
            payment.setId(id);
            payment.setCompanyId(companyId);
            payment.setPaymentDate(paymentDate);
            Integer paymentMoneyInYuan = ((Double) (Double.valueOf(paymentMoney) * 10000.0)).intValue();
            payment.setPaymentMoney(paymentMoneyInYuan + "");
            payment.setMoneySituation(moneySituation);
            payment.setDealPerson(dealPerson);
            payment.setRemark(remark);
            paymentService.updatePaymentInfoById(payment);
            String totalMoney = paymentService.selectSumMoneyByCompanyId(companyId);

            //获取公司下的所有付款记录,该记录分别以付款时间，创建时间降序排序
            List<Payment> paymentList=  paymentService.selectPaymentByCompanyId(payment.getCompanyId());

            Map<String, Object> company = new HashMap();
            company.put("id", payment.getCompanyId());
            company.put("paymentMoney", totalMoney==null?"0":totalMoney);
            /**
             * 取付款时间最近的一条作为冗余信息进行冗余
             */
            if (paymentList.size()>0){
                company.put("paymentId", paymentList.get(0).getId());
                company.put("paymentDate", paymentList.get(0).getPaymentDate());
                company.put("moneySituation", paymentList.get(0).getMoneySituation());
            }else{
                company.put("moneySituation", "");
            }

            companyService.updateCompanyInfoById(company);
            userDynamicService.addUserDynamic(loginUserId, payment.getCompanyId(), "", "更新", "更新了付款记录，付款金额：" + (payment.getPaymentMoney() == null ? 0 : payment.getPaymentMoney()) + "元,付款时间：" + (payment.getPaymentDate() == null ? "无" : payment.getPaymentDate()) + ",完款状态：" + payment.getMoneySituation(), 0,null,null,null);
            companyService.updateInfoScore((String) company.get("id"));
            solrService.updateCompanyIndex((String) company.get("id"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }





    /**
     * 更新付款记录
     *
     * @param req
     * @param rsp
     * @return
     */
    @ApiOperation(value = "临时-更新冗余付款")
    @ResponseBody
    @RequestMapping(value = "/updateP",method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter updateP(HttpServletRequest req, HttpServletResponse rsp) {
        try {
           List<String> idList= companyService.selecAllCompanyId();
            for (int i = 0; i <idList.size() ; i++) {
                String companyId=idList
                        .get(i);
                String totalMoney = paymentService.selectSumMoneyByCompanyId(companyId);
                //获取公司下的所有付款记录,该记录分别以付款时间，创建时间降序排序
                List<Payment> paymentList=  paymentService.selectPaymentByCompanyId(companyId);
                Map<String, Object> company = new HashMap();
                company.put("id", companyId);
                company.put("paymentMoney", totalMoney==null?"0":totalMoney);
                /**
                 * 取付款时间最近的一条作为冗余信息进行冗余
                 */
                if (paymentList.size()>0){
                    company.put("paymentId", paymentList.get(0).getId());
                    company.put("paymentDate", paymentList.get(0).getPaymentDate());
                    company.put("moneySituation", paymentList.get(0).getMoneySituation());
                }else{
                    company.put("moneySituation", "");
                }
                companyService.updateCompanyInfoById(company);
                companyService.updateInfoScore((String) company.get("id"));
            }



        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }


}
