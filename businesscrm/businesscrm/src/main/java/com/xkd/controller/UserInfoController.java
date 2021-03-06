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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Api(description = "企业联系人相关接口")
@Controller
@RequestMapping("/userInfo")
@Transactional
public class UserInfoController extends BaseController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SolrService solrService;

    @Autowired
    private CompanyService companyServie;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDynamicService userDynamicService;


    @Autowired
    private UserDataPermissionService userDataPermissionService;

    /**
     * @param req
     * @param rsp
     * @return
     * @throws Exception
     */


    @ApiOperation(value = "添加企业联系人")
    @ResponseBody
    @RequestMapping(value = "/addUserInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter addUserInfo(HttpServletRequest req, HttpServletResponse rsp,
                                        @ApiParam(value = "姓名", required = true) @RequestParam(required = true) String uname,
                                        @ApiParam(value = "岗位", required = false) @RequestParam(required = false) String station,
                                        @ApiParam(value = "电话", required = false) @RequestParam(required = false) String phone,
                                        @ApiParam(value = "公司Id", required = true) @RequestParam(required = true) String companyId,
                                        @ApiParam(value = "邮箱", required = false) @RequestParam(required = false) String email,
                                        @ApiParam(value = "是否是默认联系人 0  不是， 1 是", required = false) @RequestParam(required = false) String uflag,
                                        @ApiParam(value = "性别 男  女", required = false) @RequestParam(required = false) String sex,
                                        @ApiParam(value = "描述", required = false) @RequestParam(required = false) String udesc,
                                        @ApiParam(value = "手机", required = true) @RequestParam(required = true) String mobile,
                                        @ApiParam(value = "用户资源", required = false) @RequestParam(required = false) String userResource,
                                        @ApiParam(value = "人物分析", required = false) @RequestParam(required = false) String personAnalysis,
                                        @ApiParam(value = "出生日期", required = false) @RequestParam(required = false) String birth,
                                        @ApiParam(value = "年龄", required = false) @RequestParam(required = false) Integer age,
                                        @ApiParam(value = "qq", required = false) @RequestParam(required = false) String qq,
                                        @ApiParam(value = "微信", required = false) @RequestParam(required = false) String weixin,
                                        @ApiParam(value = "家乡", required = false) @RequestParam(required = false) String hometown


    ) throws Exception {

        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");

        // 通过Id获取员工公司映射关系
        Map<String, Object> userCompanyInDb = userInfoService.selectUserInfoByMobileAndCompanyId(companyId, mobile);
        if (userCompanyInDb != null) {
            throw new GlobalException(ResponseConstants.MOBILE_EXIST);
        }


        userInfoService.addUserInfo(uname, station, phone, companyId, email, uflag, sex, udesc, mobile, userResource, personAnalysis, birth, loginUserId, age, qq, weixin, hometown);



        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;

    }


    @ApiOperation(value = "修改企业联系人")
    @ResponseBody
    @RequestMapping(value = "/updateUserInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter updateUserInfo(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "id", required = true) @RequestParam(required = true) String id,
                                           @ApiParam(value = "姓名", required = false) @RequestParam(required = false) String uname,
                                           @ApiParam(value = "岗位", required = false) @RequestParam(required = false) String station,
                                           @ApiParam(value = "电话", required = false) @RequestParam(required = false) String phone,
                                           @ApiParam(value = "公司Id", required = false) @RequestParam(required = false) String companyId,
                                           @ApiParam(value = "邮箱", required = false) @RequestParam(required = false) String email,
                                           @ApiParam(value = "是否是默认联系人 0  不是， 1 是", required = false) @RequestParam(required = false) String uflag,
                                           @ApiParam(value = "性别 男  女", required = false) @RequestParam(required = false) String sex,
                                           @ApiParam(value = "描述", required = false) @RequestParam(required = false) String udesc,
                                           @ApiParam(value = "手机", required = false) @RequestParam(required = false) String mobile,
                                           @ApiParam(value = "用户资源", required = false) @RequestParam(required = false) String userResource,
                                           @ApiParam(value = "人物分析", required = false) @RequestParam(required = false) String personAnalysis,
                                           @ApiParam(value = "出生日期 2012-10-11", required = false) @RequestParam(required = false) String birth,
                                           @ApiParam(value = "年龄", required = false) @RequestParam(required = false) Integer age,
                                           @ApiParam(value = "qq", required = false) @RequestParam(required = false) String qq,
                                           @ApiParam(value = "微信", required = false) @RequestParam(required = false) String weixin,
                                           @ApiParam(value = "家乡", required = false) @RequestParam(required = false) String hometown


    ) throws Exception {

        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");


        Map<String, Object> user = userInfoService.selectUserInfoById(id);

        // 通过Id获取员工公司映射关系
        Map<String, Object> userCompanyInDb = userInfoService.selectUserInfoByMobileAndCompanyId(companyId, mobile);

        if (userCompanyInDb != null) {
            if (!userCompanyInDb.get("id").equals(id)) {
                throw new GlobalException(ResponseConstants.MOBILE_EXIST);
            }
        }

        userInfoService.updateUserInfo(id, uname, station, phone, companyId, email, uflag, sex, udesc, mobile, userResource, personAnalysis, birth, loginUserId, age, qq, weixin, hometown);

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;

    }


    @ApiOperation(value = "删除联系人")
    @ResponseBody
    @RequestMapping(value = "/deleteUserInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter deleteUserInfo(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "ids", required = true) @RequestParam(required = true) String ids) throws Exception {
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        try {
            if (StringUtils.isNotBlank(ids)) {
                String[] idArray = ids.split(",");
                List<String> idList = Arrays.asList(idArray);
                for (int i = 0; i < idList.size(); i++) {
                    String id = idList.get(i);
                    Map<String, Object> userCompany = userInfoService.selectUserInfoById(id);
                    String companyId = (String) userCompany.get("companyId");
                    userInfoService.deleteUserInfo(id);
                    companyServie.updateInfoScore(companyId);
                    /**
                     * 更新企业索引
                     */
            //            solrService.updateCompanyIndex(companyId);

                    userDynamicService.addUserDynamic(loginUserId, companyId, "", "删除", "删除了员工" + userCompany.get("uname"), 0, null, null, null);
                }
            }


        } catch (Exception e) {

            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;

    }


    @ApiOperation(value = "查询企业联系人详情")
    @ResponseBody
    @RequestMapping(value = "/selectUserInfoId", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter selectUserInfoId(HttpServletRequest req, HttpServletResponse rsp,
                                             @ApiParam(value = "id", required = true) @RequestParam(required = true) String id) throws Exception {
        Map<String, Object> userMap = userInfoService.selectUserInfoById(id);
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(userMap);
        return responseDbCenter;
    }


    @ApiOperation(value = "检索企业联系人")
    @ResponseBody
    @RequestMapping(value = "/searchUserInfo", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter searchUserInfo(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "搜索值", required = false) @RequestParam(required = false) String searchValue,
                                           @ApiParam(value = "企业Id", required = false) @RequestParam(required = false) String companyId,
                                           @ApiParam(value = "标志位   1  我负责的客户 2 我参与的客户  3 总监是我的客户     5 全部客户  7 我创建的联系人  ", required = false) @RequestParam(required = false) Integer publicFlag,
                                           @ApiParam(value = "当前页", required = true) @RequestParam(required = true) Integer currentPage,
                                           @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize) throws Exception {


        String loginUserId = (String) req.getAttribute("loginUserId");
        List<Map<String, Object>> list = new ArrayList<>(0);
        Integer count = 0;
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
        List<String> departmentIdList = null;
        try {
            departmentIdList = userDataPermissionService.getDataPermissionDepartmentIdList(null, loginUserId);
            list = userInfoService.searchUserInfo(companyId, searchValue, departmentIdList,publicFlag,loginUserId, currentPage, pageSize);
            count = userInfoService.searchUserInfoCount(companyId, searchValue, departmentIdList,publicFlag,loginUserId);
        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(list);
        responseDbCenter.setTotalRows(count + "");
        return responseDbCenter;
    }


    @ApiOperation(value = "企业下的联系人列表")
    @ResponseBody
    @RequestMapping(value = "/searchUserInfoByCompanyId", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter searchUserInfoByCompanyId(HttpServletRequest req, HttpServletResponse rsp,
                                                      @ApiParam(value = "企业Id", required = true) @RequestParam(required = true) String companyId,
                                                      @ApiParam(value = "当前页", required = true) @RequestParam(required = true) Integer currentPage,
                                                      @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize) throws Exception {


        List<Map<String, Object>> list = new ArrayList<>(0);
        Integer count = 0;
        try {
            list = userInfoService.searchUserInfo(companyId, null, null,null,null, currentPage, pageSize);
            count = userInfoService.searchUserInfoCount(companyId, null, null,null,null);
        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(list);
        responseDbCenter.setTotalRows(count + "");
        return responseDbCenter;
    }


    @ApiOperation(value = "根据手机号码和公司Id查询公司联系人")
    @ResponseBody
    @RequestMapping(value = "/selectUserInfoByMobileAndCompanyId", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter selectUserInfoByMobileAndCompanyId(HttpServletRequest req, HttpServletResponse rsp,
                                                               @ApiParam(value = "手机号码", required = false) @RequestParam(required = false) String mobile,
                                                               @ApiParam(value = "公司Id", required = false) @RequestParam(required = false) String companyId) throws Exception {


        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        try {
            Map<String, Object> map = userInfoService.selectUserInfoByMobileAndCompanyId(companyId, mobile);
            responseDbCenter.setResModel(map);
        } catch (Exception e) {
            log.error("异常栈:",e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        return responseDbCenter;
    }


}
