package com.xkd.controller;

import com.xkd.exception.BoxIdInvalidException;
import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.*;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 创建人 巫建辉
 * 客户联系人相关接口
 */
@Api(description = "联系人相关接口")
@Controller
@RequestMapping("/companyContractor")
@Transactional
public class CompanyContactorController extends  BaseController {
    @Autowired
    UserService userService;

    @Autowired
    CompanyContactorService companyContactorService;

    @Autowired
    AllowHistoryService allowHistoryService;
    @Autowired
    ObjectNewsService objectNewsService;

    @Autowired
    ApiCallFacadeService apiCallFacadeService;

    @ApiOperation(value = "搜索客户联系人----服务端")
    @ResponseBody
    @RequestMapping(value = "/searchCompanyContactorByName", method = {RequestMethod.POST})
    public ResponseDbCenter searchCompanyContactorByName(HttpServletRequest req, HttpServletResponse rsp,
                                                @ApiParam(value = "公司ID  默认不传表示全部", required = false) @RequestParam(required = false) String companyId,
                                                 @ApiParam(value = "联系人和手机号码 ", required = false) @RequestParam(required = false) String uname,
                                                @ApiParam(value = "当前页", required = false) @RequestParam(required = false) Integer currentPage,
                                                @ApiParam(value = "每页多少条", required = false) @RequestParam(required = false) Integer pageSize) throws Exception {


        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);

        List<Map<String, Object>> contactorList = null;
        int totalRows=0;

        try {
                contactorList =  companyContactorService.selectCompanyContactorByCompanyId(companyId,(String)loginUserMap.get("pcCompanyId"),uname,currentPage,pageSize);
                totalRows=companyContactorService.selectCompanyContactorCountByCompanyId(companyId,(String)loginUserMap.get("pcCompanyId"),uname);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(contactorList);
        responseDbCenter.setTotalRows(totalRows+"");
        return responseDbCenter;
    }




    @ApiOperation(value = "删除联系人---服务端")
    @ResponseBody
    @RequestMapping(value = "/deleteCompanyContactor", method = {RequestMethod.POST})
    public ResponseDbCenter deleteCompanyContactor(HttpServletRequest req, HttpServletResponse rsp,
                                                          @ApiParam(value = "联系人Id", required = true) @RequestParam(required = true) String ids
                                                        ) throws Exception {


        try {

           if (StringUtils.isNotBlank(ids)){
                String[] idArray=ids.split(",");
               List<String> idList=new ArrayList<>();
               for (int i = 0; i <idArray.length ; i++) {
                   idList.add(idArray[i]);
               }
               companyContactorService.deleteByIds(idList);
           }


        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }



    @ApiOperation(value = "修改联系人公司---服务端")
    @ResponseBody
    @RequestMapping(value = "/changeContactorCompany", method = {RequestMethod.POST})
    public ResponseDbCenter changeContactorCompany(HttpServletRequest req, HttpServletResponse rsp,
                                                   @ApiParam(value = "联系人Id", required = true) @RequestParam(required = true) String id,
                                                   @ApiParam(value = "公司Id", required = true) @RequestParam(required = true) String companyId
    ) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String,Object>  loginUserMap=userService.selectUserById(loginUserId);

          try {
            Map<String,Object> map=companyContactorService.selectCompanyContactorById(id);
            Map<String,Object> map2=companyContactorService.selectCompanyContactorByCompanyIdAndUserIdAndPcCompanyId(companyId, (String) map.get("userId"), (String) map.get("pcCompanyId"));

            if (map2==null||map2.size()==0){
                map.put("companyId",companyId);
                companyContactorService.update(map);

                //下发告警名单
                apiCallFacadeService.addDeviceWarningWhenBindingCustomer(companyId, (String) loginUserMap.get("pcCompanyId"),id);


            }
        } catch (BoxIdInvalidException be) {
              be.printStackTrace();
              throw be;
          }catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }





    @ApiOperation(value = "邀请客户联系人---服务端")
    @ResponseBody
    @RequestMapping(value = "/allowContactor", method = {RequestMethod.POST})
    public ResponseDbCenter allowContactor(HttpServletRequest req, HttpServletResponse rsp,
                                            @ApiParam(value = "被邀请人员Id", required = true) @RequestParam(required = true) String userId
    ) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String,Object>  loginUserMap=userService.selectUserById(loginUserId);

        try {

            //邀请技师添加到服务商中
            Map<String,Object>  map=new HashMap<>();
            String objectId=UUID.randomUUID().toString();
            map.put("id",objectId);
            map.put("allowerId",loginUserId);
            map.put("pcCompanyId",loginUserMap.get("pcCompanyId"));
            map.put("alloweeId",userId);
            map.put("flag",2);
            map.put("createdBy",loginUserId);
            map.put("createDate",new Date());
            allowHistoryService.insert(map);


            /**
             * 推送消息到客户帐户
             */
            Map<String,Object>    newsMap=new HashMap<>();
            newsMap.put("id",UUID.randomUUID().toString());
            newsMap.put("objectId",objectId);
            newsMap.put("appFlag",4);
            newsMap.put("userId",userId);
            newsMap.put("title","邀请消息");
            newsMap.put("content",loginUserMap.get("uname")+"邀请您为"+loginUserMap.get("departmentName")+"的联系人");
            newsMap.put("createDate",new Date());
            newsMap.put("createdBy",loginUserId);
            newsMap.put("status",0);
            newsMap.put("flag",0);
            newsMap.put("pushFlag",0);
            newsMap.put("newsType",2);
            newsMap.put("imgUrl",  PropertiesUtil.FILE_HTTP_PATH+"icons/msgIcons/msg.png");

            List<Map<String,Object>> list=new ArrayList<>();
            list.add(newsMap);
            objectNewsService.saveObjectNews(list);


        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }


}
