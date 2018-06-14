package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.*;
import java.util.List;

@Api(description = "企业联系人相关接口")
@Controller
@RequestMapping("/userInfo")
@Transactional
public class UserInfoController extends BaseController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserAttendMeetingService userAttendMeetingService;
    @Autowired
    private SolrService solrService;

    @Autowired
    private CompanyService companyServie;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDynamicService userDynamicService;


    @Autowired
    private  UserDataPermissionService userDataPermissionService;
    /**
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */

    @ApiOperation(value = "添加或修改企业联系人")
    @ResponseBody
    @RequestMapping(value = "/changeUserInfo",method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter changeUserInfo(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "插入标志  insert 插入 update更新",required = true)@RequestParam(required = true) String flag,
                                           @ApiParam(value = "姓名",required = true)@RequestParam(required = true)  String uname,
                                           @ApiParam(value = "岗位",required = false)@RequestParam(required = false)     String station,
                                           @ApiParam(value = "电话",required = false)@RequestParam(required = false)  String phone,
                                           @ApiParam(value = "公司Id" ,required = true)@RequestParam (required = true)  String companyId,
                                           @ApiParam(value = "邮箱",required = false)@RequestParam(required = false)  String email,
                                           @ApiParam(value = "是否是默认联系人 0  不是， 1 是",required = false)@RequestParam(required = false)   String uflag,
                                           @ApiParam(value = "用户Id",required = false)@RequestParam(required = false)   String id,
                                           @ApiParam(value = "性别 男  女",required = false)@RequestParam(required = false)  String sex,
                                           @ApiParam(value = "描述",required = false)@RequestParam(required = false)  String desc,
                                           @ApiParam(value = "手机",required = true)@RequestParam(required = true)  String mobile,
                                           @ApiParam(value = "用户资源",required = false)@RequestParam(required = false)  String userResource,
                                           @ApiParam(value = "人物分析",required = false)@RequestParam(required = false)  String personAnalysis,
                                           @ApiParam(value = "出生日期",required = false)@RequestParam(required = false)  String birth,
                                           @ApiParam(value = "年龄",required = false)@RequestParam(required = false)  Integer age


    ) throws Exception {

        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");

        /**
         * 判断企业是否有权限被修改
         */
        boolean hasPermission=userDataPermissionService.hasPermission(companyId,loginUserId);
        if (!hasPermission){
            return  ResponseConstants.DATA_NOT_PERMITED;
        }


        return userInfoService.changeUserInfo(flag,uname,station,phone,companyId,email,uflag,id,sex,desc,mobile,userResource,personAnalysis,birth,loginUserId,age);


    }


    /**
     * @param req
     * @param rsp
     * @return
     * @author: xiaoz
     * @2017年5月8日
     * @功能描述:删除企业联系人
     */
    @ApiOperation(value = "删除联系人")
    @ResponseBody
    @RequestMapping(value = "/deleteUserInfo",method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter deleteUserInfo(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "员工公司关系Id",required = true)@RequestParam(required = true)  String userId) throws Exception {
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        String userCompanyId = userId;

        if (StringUtils.isBlank(userCompanyId)) {

            return ResponseConstants.FUNC_UPDATENOID;
        }






        try {

            Map<String, Object> userCompany = userInfoService.selectUserCompanyById(userCompanyId);

            Map<String, Object> userMap = userService.selectUserById((String) userCompany.get("userId"));

            String companyId = (String) userCompany.get("companyId");



            /**
             * 判断企业是否有权限被修改
             */
            boolean hasPermission=userDataPermissionService.hasPermission(companyId,loginUserId);
            if (!hasPermission){
                return  ResponseConstants.DATA_NOT_PERMITED;
            }



            userInfoService.deleteUserCompany(userCompanyId);

            userAttendMeetingService.deleteMeetingUserByUserIdAndCompanyId((String) userCompany.get("userId"), companyId);


            List<Map<String, Object>> userInfoList = userInfoService.selectUserInfoByCompanyId(companyId);
            if (userInfoList.size() > 0) {
                //设置列表第一个用户为默认联系人
                Map<String, Object> companyMap = new HashMap<>();
                Map<String, Object> user = userService.selectUserById((String) userInfoList.get(0).get("userId"));
                companyMap.put("id", companyId);
                companyMap.put("contactUserId", user.get("id"));
                companyMap.put("contactName", user.get("uname"));
                companyMap.put("contactPhone", StringUtils.isBlank((String) user.get("mobile")) ? (String) user.get("phone") : (String) user.get("mobile"));
                companyServie.updateCompanyInfoById(companyMap);
            } else {
                //删除企业默认联系人
                Map<String, Object> companyMap = new HashMap<>();
                companyMap.put("id", companyId);
                companyMap.put("contactUserId", "");
                companyMap.put("contactName", "");
                companyMap.put("contactPhone", "");
                companyServie.updateCompanyInfoById(companyMap);
            }


			/*
             * 将该企业维护到solr库中
			 */

            companyServie.updateInfoScore(companyId);
            solrService.updateCompanyIndex(companyId);

            userDynamicService.addUserDynamic(loginUserId, companyId, "", "删除", "删除了员工" + userMap.get("uname"), 0,null,null,null);


        } catch (Exception e) {

            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;

    }




    @ApiOperation(value = "查询企业联系人")
    @ResponseBody
    @RequestMapping(value = "/selectUserInfoByCompanyId",method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter selectUserInfoByCompanyId(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "公司Id",required = true)@RequestParam(required = true)  String companyId) throws Exception {
        List<Map<String,Object>> userInfoList=userInfoService.selectUserInfoByCompanyId(companyId);
        ResponseDbCenter responseDbCenter=new ResponseDbCenter();
        responseDbCenter.setResModel(userInfoList);
        return responseDbCenter;
    }



}
