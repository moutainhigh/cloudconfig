package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.mapper.ScheduleUserMapper;
import com.xkd.model.Company;
import com.xkd.model.Document;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.*;
import com.xkd.utils.FileUtil;
import com.xkd.utils.PropertiesUtil;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by dell on 2017/12/22.
 */


@Api(description = "企业商机相关接口")
@Controller
@RequestMapping("/businessOpportunity")
@Transactional
public class BusinessOpportunityController {

    @Autowired
    PagerFileService pagerFileService;
    @Autowired
    BusinessOpportunityService businessOpportunityService;

    @Autowired
    CompanyService companyService;

    @Autowired
    DocumentService documentService;

    @Autowired
    ScheduleUserMapper scheduleColleagueMapper;

    @Autowired
    UserService userService;

    @Autowired
    UserDataPermissionService userDataPermissionService;


    @Autowired
    UserDynamicService userDynamicService;



    @ApiOperation(value = "查询商机列表")
    @ResponseBody
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST},value = "selectBusinessOpportunity")
    public ResponseDbCenter selectBusinessOpportunity(HttpServletRequest req, HttpServletResponse rsp,
                                                                    @ApiParam(value = "商机类型Id",required = false) @RequestParam(required = false) String phaseTypeId,
                                                                     @ApiParam(value = "阶段Id",required = false) @RequestParam(required = false) String phaseId,
                                                                    @ApiParam(value = "公司Id",required = false) @RequestParam(required = false) String companyId,
                                                                     @ApiParam(value = "商机名称",required = false) @RequestParam(required = false) String opportunityName,
                                                                    @ApiParam(value = "创建时间",required = false) @RequestParam(required = false) String beginDate,
                                                                    @ApiParam(value = "创建时间",required = false) @RequestParam(required = false) String endDate,
                                                                     @ApiParam(value = "部门Id",required = false) @RequestParam(required = false) String departmentId,
                                                                    @ApiParam(value = "flag  1 我负责的  2  我创建的",required = false) @RequestParam(required = false) Integer flag,
                                                                     @ApiParam(value = "创建人",required = false) @RequestParam(required = false) String createdBy,
                                                                     @ApiParam(value = "当前页",required = true) @RequestParam(required = true) Integer currentPage,
                                                                    @ApiParam(value = "每页多少条",required = true) @RequestParam(required = true) Integer pageSize,
                                                      @ApiParam(value = "排序",required = false) @RequestParam(required = false) String orderFlag

    ) {
        try{
            // 当前登录用户的Id
            String loginUserId = (String) req.getAttribute("loginUserId");
            ResponseDbCenter responseDbCenter=new ResponseDbCenter();
            Map<String,Object> map=  businessOpportunityService.selectBusinessOpportunity(companyId,phaseId, opportunityName, departmentId, loginUserId,flag, currentPage, pageSize,loginUserId,beginDate,endDate,phaseTypeId,orderFlag);
            responseDbCenter.setResModel(map.get("list"));
            responseDbCenter.setTotalRows(map.get("count")+"");
            responseDbCenter.setResExtra(businessOpportunityService.getMyTableShow(loginUserId,"business"));
            return responseDbCenter;
        }catch (Exception e){
            return ResponseConstants.FUNC_SERVERERROR;
        }

    }



    @ApiOperation(value = "企业商机详情")
    @ResponseBody
    @RequestMapping(value = "businessOpportunityDetailUnderCompany",method = RequestMethod.POST)
    public ResponseDbCenter businessOpportunityDetailUnderCompany(HttpServletRequest req, HttpServletResponse rsp,
            @ApiParam(value = "Id",required = true) @RequestParam(required = true) String id
    ) {
        try{
            ResponseDbCenter responseDbCenter=new ResponseDbCenter();
            Map<String,Object> map= businessOpportunityService.getBusinessOpportunityById(id);
             responseDbCenter.setResModel(map);
             return responseDbCenter;
        }catch (Exception e){
            return ResponseConstants.FUNC_SERVERERROR;
        }

    }


    @ApiOperation(value = "商机详情")
    @ResponseBody
    @RequestMapping(value = "businessOpportunityDetail",method = RequestMethod.POST)
    public ResponseDbCenter businessOpportunityDetail(HttpServletRequest req, HttpServletResponse rsp,
                                                      @ApiParam(value = "Id",required = true) @RequestParam(required = true) String id) {
        try{
            ResponseDbCenter responseDbCenter=new ResponseDbCenter();
            Map<String,Object> map= businessOpportunityService.getBusinessOpportunityById(id);
            responseDbCenter.setResModel(map);
            return responseDbCenter;
        }catch (Exception e){
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }



    @ApiOperation(value = "企业下添加企业商机")
    @ResponseBody
    @RequestMapping(value = "addBusinessOpportunityUnderCompany",method = RequestMethod.POST)
    public ResponseDbCenter addBusinessOpportunityUnderCompany(HttpServletRequest req, HttpServletResponse rsp,
            @ApiParam(value = "公司Id",required = true) @RequestParam(required = true) String companyId,
            @ApiParam(value = "商机名称",required = true) @RequestParam(required = true) String opportunityName,
            @ApiParam(value = "负责人Id",required = false) @RequestParam(required = false) String responsibleUserId,
            @ApiParam(value = "联系人姓名",required = false) @RequestParam(required = false) String contact,
            @ApiParam(value = "联系人电话",required = false) @RequestParam(required = false) String contactMobile,
            @ApiParam(value = "其他联系人姓名",required = false) @RequestParam(required = false) String otherContact,
            @ApiParam(value = "其他联系人电话",required = false) @RequestParam(required = false) String otherContactMobile,
            @ApiParam(value = "其他客户名称",required = false) @RequestParam(required = false) String otherCompanyName,
            @ApiParam(value = "销售金额",required = false) @RequestParam(required = false) String salesMoney,
            @ApiParam(value = "销售阶段Id",required = false) @RequestParam(required = false) String phaseId,
            @ApiParam(value = "实施时间",required = false) @RequestParam(required = false) String implementDate,
            @ApiParam(value = "备注",required = false) @RequestParam(required = false) String remark,
            @ApiParam(value = "部门id",required = true) @RequestParam(required = true) String departmentId
    ) {
        try{
            // 当前登录用户的Id
            String id = UUID.randomUUID().toString();
            String loginUserId = (String) req.getAttribute("loginUserId");
            businessOpportunityService.insertBusinessOppotunity(id,companyId,opportunityName,responsibleUserId,contact,contactMobile,salesMoney,phaseId,implementDate,remark,loginUserId,otherContact,otherContactMobile,otherCompanyName,departmentId);
            pagerFileService.initializationCompanyFolder(companyId+"__0",loginUserId);
            pagerFileService.addBusinessOppotunityFolder(companyId,opportunityName,id,loginUserId);
            //添加到常用联系人
            if(StringUtils.isNotBlank(responsibleUserId)){
                Map<String,Object> historyUserInfo = new HashMap<>();
                historyUserInfo.put("myUserId",loginUserId);
                historyUserInfo.put("userId", responsibleUserId);
                historyUserInfo.put("time",0);
                scheduleColleagueMapper.saveHistoryUser(historyUserInfo);
            }
            List<Map<String,String>> historyUser = scheduleColleagueMapper.getMyHistoryUser(loginUserId);
            if(historyUser.size() > 20){
                for (int i = 20; i < historyUser.size(); i++) {
                    scheduleColleagueMapper.deleteHistoryUser(loginUserId,historyUser.get(i).get("userId"));
                }
            }
            ResponseDbCenter responseDbCenter=new ResponseDbCenter();
            return responseDbCenter;
        }catch (Exception e){
            return ResponseConstants.FUNC_SERVERERROR;
        }

    }




    @ApiOperation(value = "添加企业商机")
    @ResponseBody
    @RequestMapping(value = "addBusinessOpportunity",method = RequestMethod.POST)
    public ResponseDbCenter addBusinessOpportunity(HttpServletRequest req, HttpServletResponse rsp,
                                                   @ApiParam(value = "公司Id",required = true) @RequestParam(required = false) String companyId,
                                                   @ApiParam(value = "商机名称",required = true) @RequestParam(required = true) String opportunityName,
                                                   @ApiParam(value = "负责人Id",required = false) @RequestParam(required = false) String responsibleUserId,
                                                   @ApiParam(value = "联系人姓名",required = false) @RequestParam(required = false) String contact,
                                                   @ApiParam(value = "联系人电话",required = false) @RequestParam(required = false) String contactMobile,
                                                   @ApiParam(value = "其他联系人姓名",required = false) @RequestParam(required = false) String otherContact,
                                                   @ApiParam(value = "其他联系人电话",required = false) @RequestParam(required = false) String otherContactMobile,
                                                   @ApiParam(value = "其他客户名称",required = false) @RequestParam(required = false) String otherCompanyName,
                                                   @ApiParam(value = "销售金额",required = false) @RequestParam(required = false) String salesMoney,
                                                   @ApiParam(value = "销售阶段Id",required = false) @RequestParam(required = false) String phaseId,
                                                   @ApiParam(value = "实施时间",required = false) @RequestParam(required = false) String implementDate,
                                                   @ApiParam(value = "备注",required = false) @RequestParam(required = false) String remark,
                                                   @ApiParam(value = "部门",required = true) @RequestParam(required = true) String departmentId
    ) {
        //try{
            // 当前登录用户的Id
            String loginUserId = (String) req.getAttribute("loginUserId");
            String id = UUID.randomUUID().toString();
            businessOpportunityService.insertBusinessOppotunity(id,companyId,opportunityName,responsibleUserId,contact,contactMobile,salesMoney,phaseId,implementDate,remark,loginUserId,otherContact,otherContactMobile,otherCompanyName,departmentId);
            //添加到常用联系人
            if(StringUtils.isNotBlank(responsibleUserId)){
                Map<String,Object> historyUserInfo = new HashMap<>();
                historyUserInfo.put("myUserId",loginUserId);
                historyUserInfo.put("userId", responsibleUserId);
                historyUserInfo.put("time",0);
                scheduleColleagueMapper.saveHistoryUser(historyUserInfo);
            }
            List<Map<String,String>> historyUser = scheduleColleagueMapper.getMyHistoryUser(loginUserId);
            if(historyUser.size() > 20){
                for (int i = 20; i < historyUser.size(); i++) {
                    scheduleColleagueMapper.deleteHistoryUser(loginUserId,historyUser.get(i).get("userId"));
                }
            }
            if(StringUtils.isNotBlank(companyId)){
                //初始化企业文件夹跟商机文件夹
                pagerFileService.initializationCompanyFolder(companyId+"__0",loginUserId);
                //初始化当前商机
                pagerFileService.addBusinessOppotunityFolder(companyId,opportunityName,id,loginUserId);
            }



            ResponseDbCenter responseDbCenter=new ResponseDbCenter();
            return responseDbCenter;
        /*}catch (Exception e){
            return ResponseConstants.FUNC_SERVERERROR;
        }*/
    }

    @ApiOperation(value = "企业下更新企业商机")
    @ResponseBody
    @RequestMapping(value = "updateBusinessOpportunityUnderCompany",method = RequestMethod.POST)
    public ResponseDbCenter updateBusinessOpportunityUnderCompany(HttpServletRequest req, HttpServletResponse rsp,
            @ApiParam(value = "Id",required = true) @RequestParam(required = true) String id,
            @ApiParam(value = "公司Id",required = true) @RequestParam(required = true) String companyId,
            @ApiParam(value = "商机名称",required = true) @RequestParam(required = true) String opportunityName,
            @ApiParam(value = "负责人Id",required = false) @RequestParam(required = false) String responsibleUserId,
            @ApiParam(value = "联系人姓名",required = false) @RequestParam(required = false) String contact,
            @ApiParam(value = "联系人电话",required = false) @RequestParam(required = false) String contactMobile,
            @ApiParam(value = "销售金额",required = false) @RequestParam(required = false) String salesMoney,
            @ApiParam(value = "销售阶段Id",required = false) @RequestParam(required = false) String phaseId,
            @ApiParam(value = "实施时间",required = false) @RequestParam(required = false) String implementDate,
            @ApiParam(value = "备注",required = false) @RequestParam(required = false) String remark,
            @ApiParam(value = "部门",required = true) @RequestParam(required = true) String departmentId
    ) {
        try{
            String loginUserId = (String) req.getAttribute("loginUserId");
            businessOpportunityService.updateBusinessOppotunity(id,companyId,opportunityName,responsibleUserId,contact,contactMobile,salesMoney,phaseId,implementDate,remark,null,loginUserId,departmentId);

            if(StringUtils.isNotBlank(responsibleUserId)){
                Map<String,Object> historyUserInfo = new HashMap<>();
                historyUserInfo.put("myUserId",loginUserId);
                historyUserInfo.put("userId", responsibleUserId);
                historyUserInfo.put("time",0);
                scheduleColleagueMapper.saveHistoryUser(historyUserInfo);
            }
            List<Map<String,String>> historyUser = scheduleColleagueMapper.getMyHistoryUser(loginUserId);
            if(historyUser.size() > 20){
                for (int i = 20; i < historyUser.size(); i++) {
                    scheduleColleagueMapper.deleteHistoryUser(loginUserId,historyUser.get(i).get("userId"));
                }
            }
            ResponseDbCenter responseDbCenter=new ResponseDbCenter();
            return responseDbCenter;
        }catch (Exception e){
            return ResponseConstants.FUNC_SERVERERROR;
        }

    }
    @ApiOperation(value = "更新企业商机")
    @ResponseBody
    @RequestMapping(value = "updateBusinessOpportunity",method = RequestMethod.POST)
    public ResponseDbCenter updateBusinessOpportunity(HttpServletRequest req, HttpServletResponse rsp,
                                                      @ApiParam(value = "Id",required = true) @RequestParam(required = true) String id,
                                                      @ApiParam(value = "公司Id",required = false) @RequestParam(required = false) String companyId,
                                                      @ApiParam(value = "商机名称",required = true) @RequestParam(required = true) String opportunityName,
                                                      @ApiParam(value = "负责人Id",required = false) @RequestParam(required = false) String responsibleUserId,
                                                      @ApiParam(value = "联系人姓名",required = false) @RequestParam(required = false) String contact,
                                                      @ApiParam(value = "联系人电话",required = false) @RequestParam(required = false) String contactMobile,
                                                      @ApiParam(value = "销售金额",required = false) @RequestParam(required = false) String salesMoney,
                                                      @ApiParam(value = "销售阶段Id",required = false) @RequestParam(required = false) String phaseId,
                                                      @ApiParam(value = "实施时间",required = false) @RequestParam(required = false) String implementDate,
                                                      @ApiParam(value = "备注",required = false) @RequestParam(required = false) String remark,
                                                      @ApiParam(value = "部门",required = true) @RequestParam(required = true) String departmentId
    ) {
        try{
            String loginUserId = (String) req.getAttribute("loginUserId");
            businessOpportunityService.updateBusinessOppotunity(id,companyId,opportunityName,responsibleUserId,contact,contactMobile,salesMoney,phaseId,implementDate,remark,null,loginUserId,departmentId);
            //添加到常用联系人
            if(StringUtils.isNotBlank(responsibleUserId)){
                Map<String,Object> historyUserInfo = new HashMap<>();
                historyUserInfo.put("myUserId",loginUserId);
                historyUserInfo.put("userId", responsibleUserId);
                historyUserInfo.put("time",0);
                scheduleColleagueMapper.saveHistoryUser(historyUserInfo);
            }
            List<Map<String,String>> historyUser = scheduleColleagueMapper.getMyHistoryUser(loginUserId);
            if(historyUser.size() > 20){
                for (int i = 20; i < historyUser.size(); i++) {
                    scheduleColleagueMapper.deleteHistoryUser(loginUserId,historyUser.get(i).get("userId"));
                }
            }
            ResponseDbCenter responseDbCenter=new ResponseDbCenter();
            return responseDbCenter;
        }catch (Exception e){
            return ResponseConstants.FUNC_SERVERERROR;
        }

    }



    @ApiOperation(value = "删除企业商机")
    @ResponseBody
    @RequestMapping(value = "deleteBusinessOpportunityUnderCompany",method = RequestMethod.POST)
    public ResponseDbCenter deleteBusinessOpportunityUnderCompany(HttpServletRequest req, HttpServletResponse rsp,
                                                      @ApiParam(value = "ids,多个以逗号分隔",required = true) @RequestParam(required = true) String ids

    ) {
        try{
            String loginUserId = (String) req.getAttribute("loginUserId");

            if (StringUtils.isNotBlank(ids)){
                List<String> idList=new ArrayList<>();
                String[] idArray=ids.split(",");
                for (int i = 0; i <idArray.length ; i++) {
                    idList.add(idArray[i]);
                }
                businessOpportunityService.deleteBusinessOpportunities(idList);
                List<Map<String,Object>> mapList=businessOpportunityService.selectBusinessOpportunityByIds(idList);
                for (int i = 0; i <mapList.size() ; i++) {
                    userDynamicService.addUserDynamic(loginUserId, (String)mapList.get(i).get("companyId"), null, "添加", "删除企业商机\"" + mapList.get(i).get("opportunityName") + "\"", 0, null, null, null);
                }
            }
            ResponseDbCenter responseDbCenter=new ResponseDbCenter();
            return responseDbCenter;
        }catch (Exception e){
            return ResponseConstants.FUNC_SERVERERROR;
        }

    }


    @ApiOperation(value = "删除商机")
    @ResponseBody
    @RequestMapping(value = "deleteBusinessOpportunity",method = RequestMethod.POST)
    public ResponseDbCenter deleteBusinessOpportunity(HttpServletRequest req, HttpServletResponse rsp,
                                                      @ApiParam(value = "ids,多个值以逗号分隔",required = true) @RequestParam(required = true) String ids
    ) {
        try{
            String loginUserId = (String) req.getAttribute("loginUserId");

            if (StringUtils.isNotBlank(ids)){
                List<String> idList=new ArrayList<>();
                String[] idArray=ids.split(",");
                for (int i = 0; i <idArray.length ; i++) {
                    idList.add(idArray[i]);
                }
                businessOpportunityService.deleteBusinessOpportunities(idList  );


                List<Map<String,Object>> mapList=businessOpportunityService.selectBusinessOpportunityByIds(idList);
                 for (int i = 0; i <mapList.size() ; i++) {
                    userDynamicService.addUserDynamic(loginUserId, (String)mapList.get(i).get("companyId"), null, "添加", "删除企业商机\"" + mapList.get(i).get("opportunityName") + "\"", 0, null, null, null);
                }
            }
            ResponseDbCenter responseDbCenter=new ResponseDbCenter();
            return responseDbCenter;
        }catch (Exception e){
            return ResponseConstants.FUNC_SERVERERROR;
        }

    }
    @ApiOperation(value = "商机详情")
    @ResponseBody
    @RequestMapping(value = "getBusinessOpportunityName",method = RequestMethod.POST)
    public ResponseDbCenter getBusinessOpportunityName(HttpServletRequest req, HttpServletResponse rsp,String opportunityName) {
        try{
            ResponseDbCenter responseDbCenter=new ResponseDbCenter();
            String loginUserId = (String) req.getAttribute("loginUserId");
            Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);
            String pcCompanyId =loginUserMap.get("roleId").equals("1") ? null :loginUserMap.get("pcCompanyId").toString();
            String departmentId = loginUserMap.get("departmentId").toString();
            List<String> depList =  loginUserMap.get("roleId").equals("1") ? null : userDataPermissionService.getDataPermissionDepartmentIdList(departmentId,loginUserId);
            List<Map<String,Object>> map= businessOpportunityService.getBusinessOpportunityName(pcCompanyId, depList,opportunityName);
            responseDbCenter.setResModel(map);
            return responseDbCenter;
        }catch (Exception e){
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

    //获取所有要显示跟不显示的列
    @ResponseBody
    @RequestMapping(value = "getAllTabColumn",method = RequestMethod.GET)
    public ResponseDbCenter getAllTabColumn(HttpServletRequest req, HttpServletResponse rsp,String tabType) {
        try{
            ResponseDbCenter responseDbCenter=new ResponseDbCenter();
            String loginUserId = (String) req.getAttribute("loginUserId");
            responseDbCenter.setResModel(businessOpportunityService.getAllTabColumn(loginUserId,tabType));
            return responseDbCenter;
        }catch (Exception e){
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }
    //获取所有要显示跟不显示的列
    @ResponseBody
    @RequestMapping(value = "saveTabColumn",method = RequestMethod.POST)
    public ResponseDbCenter saveTabColumn(HttpServletRequest req, HttpServletResponse rsp,String tabType,String ids) {
        try{
            ResponseDbCenter responseDbCenter=new ResponseDbCenter();
            String loginUserId = (String) req.getAttribute("loginUserId");
            List<String> tableColumn = new ArrayList(Arrays.asList(ids.split(",")));
            businessOpportunityService.saveTabColumn(loginUserId,tableColumn,tabType);
            return responseDbCenter;
        }catch (Exception e){
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


}
