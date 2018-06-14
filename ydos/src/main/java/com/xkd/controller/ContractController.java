package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.Company;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.*;
import com.xkd.utils.PropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
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

/**
 * 巫建辉
 * 合同管理相关接口
 */
@Api(description = "合同管理")
@Controller
@RequestMapping("/contract")
@Transactional
public class ContractController extends BaseController {

    @Autowired
    ContractService contractService;
    @Autowired
    UserService userService;
    @Autowired
    AttachmentService attachmentService;

    @Autowired
    CompanyContactorService companyContactorService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    CompanyService companyService;


    @Autowired
    ObjectNewsService objectNewsService;

    @ApiOperation(value = "插入合同---服务商 ")
    @ResponseBody
    @RequestMapping(value = "/insertContact", method = {RequestMethod.POST})
    public ResponseDbCenter insertContact(HttpServletRequest req, HttpServletResponse rsp,
                                          @ApiParam(value = "合同编号", required = true) @RequestParam(required = true) String contractNo,
                                          @ApiParam(value = "合同名称", required = true) @RequestParam(required = true) String contractName,
                                          @ApiParam(value = "设备数量", required = true) @RequestParam(required = true) Integer deviceNum,
                                          @ApiParam(value = "客户Id", required = true) @RequestParam(required = true) String companyId,
                                          @ApiParam(value = "合同类型 1  巡检  2 保养  4  巡检+保养", required = true) @RequestParam(required = false) Integer type,
                                          @ApiParam(value = "开始时间", required = true) @RequestParam(required = true) String startDate,
                                          @ApiParam(value = "结束时间", required = true) @RequestParam(required = true) String endDate,
                                          @ApiParam(value = "负责人", required = true) @RequestParam(required = true) String responsibleUserId,
                                          @ApiParam(value = "金额", required = true) @RequestParam(required = true) Double moneyAmount,
                                          @ApiParam(value = "附件列表,多个用逗号分隔", required = true) @RequestParam(required = true) String attachmentList

    ) throws Exception {


        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);


        /**
         * 判断服务商下合同编号是否已经被占用
         */
        Map<String,Object> existContractNos= contractService.selectContractByContractNoAndPcCompanyId(contractNo, (String) loginUserMap.get("pcCompanyId"));
        if (existContractNos!=null){
            return ResponseConstants.CONTRACT_NO_EXITS;
        }


        Map<String, Object> map = new HashMap<>();
        String id=UUID.randomUUID().toString();
        map.put("id", id);
        map.put("pcCompanyId", loginUserMap.get("pcCompanyId"));
        map.put("companyId", companyId);
        map.put("contractName", contractName);
        map.put("deviceNum", deviceNum);
        map.put("contractNo", contractNo);
        map.put("type", type);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("responsibleUserId", responsibleUserId);
        map.put("moneyAmount", moneyAmount);
        map.put("status", 0);
        map.put("createdBy", loginUserId);
        map.put("createDate", new Date());
        map.put("updatedBy", loginUserId);
        map.put("updateDate", new Date());


        /**
         * 重新更新合同附件---删除旧的合同附件，添加新的合同附件
         */
        if (StringUtils.isNotBlank(attachmentList)){
            String[] urls=attachmentList.split(",");
            List<Map<String,Object>> attachList=new ArrayList<>();
            for (int i = 0; i <urls.length ; i++) {
                Map<String,Object>  m=new HashMap<>();
                m.put("id",UUID.randomUUID().toString());
                m.put("objectId",id);
                m.put("url",urls[i]);
                m.put("imgType",4);
                m.put("createdBy",loginUserId);
                m.put("createDate",new Date());
                attachList.add(m);
            }
            attachmentService.deleteByObjectId(id);
            attachmentService.insertAttachmentList(attachList);
        }

        contractService.insertContact(map);





        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }




    @ApiOperation(value = "更新合同---服务商 ")
    @ResponseBody
    @RequestMapping(value = "/updateContact", method = {RequestMethod.POST})
    public ResponseDbCenter updateContact(HttpServletRequest req, HttpServletResponse rsp,
                                          @ApiParam(value = "ID ", required = true) @RequestParam(required = true) String id,
                                          @ApiParam(value = "合同编号", required = true) @RequestParam(required = true) String contractNo,
                                          @ApiParam(value = "合同名称", required = false) @RequestParam(required = false) String contractName,
                                          @ApiParam(value = "设备数量", required = false) @RequestParam(required = false) String deviceNum,
                                          @ApiParam(value = "合同类型 1  巡检  2 保养  4  巡检+保养", required = false) @RequestParam(required = false) Integer type,
                                          @ApiParam(value = "开始时间", required = false) @RequestParam(required = false) String startDate,
                                          @ApiParam(value = "结束时间", required = false) @RequestParam(required = false) String endDate,
                                          @ApiParam(value = "负责人", required = false) @RequestParam(required = false) String responsibleUserId,
                                          @ApiParam(value = "金额", required = false) @RequestParam(required = false) Double moneyAmount,
                                          @ApiParam(value = "附件列表,多个用逗号分隔", required = true) @RequestParam(required = true) String attachmentList

    ) throws Exception {


        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);


        /**
         * 判断合同号是否已经被占用
         */
        Map<String,Object> existContractNos= contractService.selectContractByContractNoAndPcCompanyId(contractNo, (String) loginUserMap.get("pcCompanyId"));
        if (existContractNos!=null){
            if (!existContractNos.get("id").equals(id)){
                return ResponseConstants.CONTRACT_NO_EXITS;
            }
        }



        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("pcCompanyId", loginUserMap.get("pcCompanyId"));
        map.put("contractNo", contractNo);
        map.put("contractName", contractName);
        map.put("deviceNum", deviceNum);
        map.put("type", type);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("responsibleUserId", responsibleUserId);
        map.put("moneyAmount", moneyAmount);
        map.put("status", 0);
        map.put("createdBy", loginUserId);
        map.put("createDate", new Date());


        /**
         * 删除旧的合同附件，插入新的合同附件
         */
        if (StringUtils.isNotBlank(attachmentList)){
            String[] urls=attachmentList.split(",");
            List<Map<String,Object>> attachList=new ArrayList<>();
            for (int i = 0; i <urls.length ; i++) {
                Map<String,Object>  m=new HashMap<>();
                m.put("id",UUID.randomUUID().toString());
                m.put("objectId",id);
                m.put("url",urls[i]);
                m.put("imgType",4);
                m.put("createdBy",loginUserId);
                m.put("createDate",new Date());
                attachList.add(m);
            }
            attachmentService.deleteByObjectId(id);
            attachmentService.insertAttachmentList(attachList);
        }

        contractService.updateContact(map);

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }





    @ApiOperation(value = "删除合同---服务商 ")
    @ResponseBody
    @RequestMapping(value = "/deleteContact", method = {RequestMethod.POST})
    public ResponseDbCenter deleteContact(HttpServletRequest req, HttpServletResponse rsp,
                                          @ApiParam(value = "ID ", required = true) @RequestParam(required = true) String ids

    ) throws Exception {


        if (StringUtils.isNotBlank(ids)){
            String[] idArray=ids.split(",");
            List<String> idList=new ArrayList<>();
            for (int i = 0; i < idArray.length; i++) {
                idList.add(idArray[i]);
            }
            contractService.deleteContract(idList);

        }


        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }



    @ApiOperation(value = "搜索合同--服务商 ")
    @ResponseBody
    @RequestMapping(value = "/searchContact", method = {RequestMethod.POST})
    public ResponseDbCenter searchContact(HttpServletRequest req, HttpServletResponse rsp,
                                          @ApiParam(value = "合同名称", required = false) @RequestParam(required = false) String contractName,
                                          @ApiParam(value = "合同编号 ", required = false) @RequestParam(required = false) String contractNo,
                                          @ApiParam(value = "客户Id ", required = false) @RequestParam(required = false) String companyId,
                                          @ApiParam(value = "类型   1  巡检  2  保养  4 巡检+保养  ", required = false) @RequestParam(required = false) Integer type,
                                          @ApiParam(value = "到期时间  格式请传年月日    2018-10-11", required = false) @RequestParam(required = false) String endDate,
                                          @ApiParam(value = "当前第几页 ", required = false) @RequestParam(required = false) Integer currentPage,
                                          @ApiParam(value = "每页多少条 ", required = false) @RequestParam(required = false) Integer pageSize

    ) throws Exception {


        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
        List<Map<String,Object>>  list=null;
        Integer total=0;
        List<String>  companyIdList=new ArrayList<>();
        if (StringUtils.isNotBlank(companyId)) {
            companyIdList.add(companyId);
        }

            list=contractService.searchContract((String)loginUserMap.get("pcCompanyId"),companyIdList,contractName,contractNo,type,endDate,currentPage,pageSize);
            total=contractService.searchContractCount((String)loginUserMap.get("pcCompanyId"),companyIdList,contractName,contractNo,type,endDate);

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setTotalRows(total+"");
        responseDbCenter.setResModel(list);
        return responseDbCenter;
    }


    @ApiOperation(value = "搜索合同--客户端 ")
    @ResponseBody
    @RequestMapping(value = "/searchCustomerContact", method = {RequestMethod.POST})
    public ResponseDbCenter searchCustomerContact(HttpServletRequest req, HttpServletResponse rsp,
                                                  @ApiParam(value = "合同名称", required = false) @RequestParam(required = false) String contractName,
                                                  @ApiParam(value = "合同编号 ", required = false) @RequestParam(required = false) String contractNo,
                                                  @ApiParam(value = "服务商Id ", required = false) @RequestParam(required = false) String pcCompanyId,
                                                  @ApiParam(value = "类型   1  巡检  2  保养  4 巡检+保养  ", required = false) @RequestParam(required = false) Integer type,
                                                  @ApiParam(value = "到期时间  格式请传年月日    2018-10-11", required = false) @RequestParam(required = false) String endDate,
                                                  @ApiParam(value = "当前第几页 ", required = false) @RequestParam(required = false) Integer currentPage,
                                                  @ApiParam(value = "每页多少条 ", required = false) @RequestParam(required = false) Integer pageSize

    ) throws Exception {


        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        List<Map<String,Object>>  list=null;
        Integer total=0;
        List<String> companyIdList=companyContactorService.selectCompanyIdListByContactor(loginUserId,1);
        list=contractService.searchContract(pcCompanyId,companyIdList,contractName,contractNo,type,endDate,currentPage,pageSize);
        total=contractService.searchContractCount(pcCompanyId,companyIdList,contractName,contractNo,type,endDate);
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setTotalRows(total+"");
        responseDbCenter.setResModel(list);
        return responseDbCenter;
    }



    @ApiOperation(value = "查询合同详情---通用")
    @ResponseBody
    @RequestMapping(value = "/contractDetail", method = {RequestMethod.POST})
    public ResponseDbCenter contractDetail(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "合同Id", required = false) @RequestParam(required = false) String id
    ) throws Exception {
        Map<String,Object> map=contractService.selectContractById(id);
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(map);
        return responseDbCenter;
    }


    @ApiOperation(value = "根据设备组查询合同列表")
    @ResponseBody
    @RequestMapping(value = "/selectContractByGroupId", method = {RequestMethod.POST})
    public ResponseDbCenter selectContractByGroupId(HttpServletRequest req, HttpServletResponse rsp,
                                                    @ApiParam(value = "设备组Id", required = false) @RequestParam(required = false) String groupId
    ) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
        List<Map<String,Object>> contractMapList=new ArrayList<>();

            contractMapList= contractService.selectContractByGroupId(groupId, (String) loginUserMap.get("pcCompanyId"));

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        responseDbCenter.setResModel(contractMapList);
        return responseDbCenter;
    }




    @ApiOperation(value = "查询合同到期统计--手机服务端")
    @ResponseBody
    @RequestMapping(value = "/selectContractStatistic", method = {RequestMethod.POST})
    public ResponseDbCenter selectContractStatistic(HttpServletRequest req, HttpServletResponse rsp
     ) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");
//        String loginUserId = "818";

        Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);
        Map<String,Object> map=contractService.selectContractStatistic((String) loginUserMap.get("pcCompanyId"));
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        responseDbCenter.setResModel(map);
        return responseDbCenter;
    }




    @ApiOperation(value = "查询合同到期列表--手机服务端")
    @ResponseBody
    @RequestMapping(value = "/selectDueContract", method = {RequestMethod.POST})
    public ResponseDbCenter selectDueContract(HttpServletRequest req, HttpServletResponse rsp,
                                                    @ApiParam(value = "标志位   0 已到期  1 还有一月到期 2 还有两月到期 3 还有三月到期", required = true) @RequestParam(required = true) Integer flag
    ) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");
//        String loginUserId = "818";
        Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);

        List<Map<String,Object>> list=contractService.selectDueContract(flag,(String) loginUserMap.get("pcCompanyId"));
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(list);
        return responseDbCenter;
    }





    @ApiOperation(value = "合同续签提醒--服务端")
    @ResponseBody
    @RequestMapping(value = "/renewContract", method = {RequestMethod.POST})
    public ResponseDbCenter renewContract(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value = "合同Id", required = true) @RequestParam(required = true) String contractId
    ) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");
            Map<String,Object>  contractMap=contractService.getContractById(contractId);
            Map<String,Object> pcCompanyMap=departmentService.selectDepartmentById((String) contractMap.get("pcCompanyId"));//服务商
            Company company= companyService.getCompanyById((String) contractMap.get("companyId"));//客户

          //客户端帐号列表
           List<String> customerUserIdList=  companyContactorService.selectAllUserIdByPcCompanyIdAndCompanyId((String) pcCompanyMap.get("id"), company.getId());

          //服务商帐号列表
           List<Map<String,Object>>  serverUserList= userService.selectServerAccount((String) pcCompanyMap.get("id"));

        /**
         * 向服务商发送提醒消息
         */
          List<Map<String,Object>> serverNewsMapList=new ArrayList<>();
        for (int i = 0; i <serverUserList.size() ; i++) {
            Map<String,Object>    newsMap=new HashMap<>();
            newsMap.put("id",UUID.randomUUID().toString());
            newsMap.put("objectId",contractMap.get("id"));
            newsMap.put("appFlag",2);
            newsMap.put("userId", serverUserList.get(i).get("id"));
            newsMap.put("title","合同消息");
            newsMap.put("content",company.getCompanyName()+"的合同于"+contractMap.get("endDate")+"到期，请及时联系客户续签");
            newsMap.put("createDate",new Date());
            newsMap.put("createdBy",loginUserId);
            newsMap.put("status",0);
            newsMap.put("flag",0);
            newsMap.put("pushFlag",0);
            newsMap.put("newsType",4);
            newsMap.put("imgUrl",  PropertiesUtil.FILE_HTTP_PATH+"icons/msgIcons/msg.png");
            serverNewsMapList.add(newsMap);
        }
        objectNewsService.saveObjectNews(serverNewsMapList);
        /**
         * 向客户发送提醒消息
         */
        List<Map<String,Object>> customerNewsMapList=new ArrayList<>();
        for (int i = 0; i <customerUserIdList.size() ; i++) {
            Map<String,Object>    newsMap=new HashMap<>();
            newsMap.put("id",UUID.randomUUID().toString());
            newsMap.put("objectId",contractMap.get("id"));
            newsMap.put("appFlag",4);
            newsMap.put("userId",customerUserIdList.get(i));
            newsMap.put("title","合同消息");
            newsMap.put("content","您与"+pcCompanyMap.get("departmentName")+"的合同于"+contractMap.get("endDate")+"到期，请及时联系厂商续签");
            newsMap.put("createDate",new Date());
            newsMap.put("createdBy",loginUserId);
            newsMap.put("status",0);
            newsMap.put("flag",0);
            newsMap.put("pushFlag",0);
            newsMap.put("newsType",4);
            newsMap.put("imgUrl",  PropertiesUtil.FILE_HTTP_PATH+"icons/msgIcons/msg.png");
            customerNewsMapList.add(newsMap);
        }
        objectNewsService.saveObjectNews(customerNewsMapList);

         ResponseDbCenter responseDbCenter = new ResponseDbCenter();
         return responseDbCenter;
    }


}
