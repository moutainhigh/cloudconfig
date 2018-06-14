package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xkd.exception.GlobalException;
import com.xkd.model.*;
import com.xkd.service.*;
import com.xkd.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.ls.LSInput;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;

@Api(description = "添加修改公司，删除公司，修改工商信息，企查查相关，上传公司信息等")
@Controller
@RequestMapping("/company")
@Transactional
public class CompanyController extends BaseController {

    @Autowired
    private CompanyService companyServie;

    @Autowired
    private UserInfoService userInfoService;


    @Autowired
    private UserDynamicService userDynamicService;

    @Autowired
    private SolrService solrService;

    @Autowired
    private UserService userService;

    @Autowired
    FieldShowService fieldShowService;


    @Autowired
    private CompanyRelativeUserService companyRelativeUserService;

    @Autowired
    private UserDataPermissionService userDataPermissionService;
    @Autowired
    PagerFileService pagerFileService;


    @Autowired
    DepartmentService departmentService;


    @Autowired
    DictionaryService dictionaryService;


    @ApiOperation(value = "根据名称调用企查查接口查询公司列表")
    @ResponseBody
    @RequestMapping(value = "/selectQiccCompanyInfoByName", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter selectQiccCompanyInfoByName(HttpServletRequest req, HttpServletResponse rsp,
                                                        @ApiParam(value = "公司名称", required = false) @RequestParam(required = false) String companyName,
                                                        @ApiParam(value = "当前页", required = false) @RequestParam(required = false) String pageIndex,
                                                        @ApiParam(value = "每页数量", required = false) @RequestParam(required = false) String pageSize,
                                                        @ApiParam(value = "标志位  ", required = false) @RequestParam(required = false) String flag)
            throws Exception {


        if (StringUtils.isBlank(companyName) || StringUtils.isBlank(flag)) {

            return ResponseConstants.MISSING_PARAMTER;
        }

        String qiccCompanyInfo = null;

        int pageIndexInt = 1;
        int pageSizeInt = 10;

        if (StringUtils.isNotBlank(pageIndex)) {

            pageIndexInt = Integer.parseInt(pageIndex);
            pageSizeInt = Integer.parseInt(pageSize);
        }

        try {

            if ("JQ".equals(flag)) {

                qiccCompanyInfo = CompanyInfoApi.queryCompanyInfo(CompanyInfoApi.COMANY_NAME, companyName, 1, 10);

            } else {

                qiccCompanyInfo = CompanyInfoApi.queryCompanyInfo(CompanyInfoApi.COMANY_NAME_LIKE, companyName, pageIndexInt, pageSizeInt);

            }

        } catch (Exception e) {

            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(qiccCompanyInfo);

        return responseDbCenter;
    }


    @ApiOperation(value = "根据类型查询顾问列表")
    @ResponseBody
    @RequestMapping(value = "/selectAdviserByType", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter selectAdviserByType(HttpServletRequest req, HttpServletResponse rsp,
                                                @ApiParam(value = "类型", required = false) @RequestParam(required = false) String type) throws Exception {


        List<Map<String, Object>> advisersGuwen = null;
        List<Map<String, Object>> advisersLaoshi = null;
        List<Map<String, Object>> advisersZongjian = null;
        List<Map<String, Object>> advisers = null;
        Map<String, Object> map = null;
        String loginUserId = (String) req.getAttribute("loginUserId");

        try {

            if (StringUtils.isNotBlank(type)) {

                advisers = userService.selectUserByTeacherType("", loginUserId);

            } else {

                advisersLaoshi = userService.selectUserByTeacherType("授课老师", loginUserId);

                advisersZongjian = userService.selectUserByTeacherType("总监", loginUserId);

                advisersGuwen = userService.selectUserByTeacherType("顾问", loginUserId);

                if (advisersZongjian != null && advisersLaoshi != null) {

                    advisersZongjian.addAll(advisersLaoshi);
                }
            }

        } catch (Exception e) {

            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        if (advisers != null) {

            responseDbCenter.setResModel(advisers);

        } else {

            map = new HashMap<>();
            // 为了
            map.put("teacherIdList", advisersLaoshi);
            map.put("adviserIdList", advisersGuwen);
            map.put("directorIdList", advisersZongjian);

            responseDbCenter.setResModel(map);
        }

        return responseDbCenter;
    }


    @ApiOperation(value = "根据公司名搜索----有数据权限")
    @ResponseBody
    @RequestMapping(value = "/control/searchCompanyByName", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter searchCompanyByName(HttpServletRequest req, HttpServletResponse rsp,
                                                @ApiParam(value = "公司名称", required = false) @RequestParam(required = false) String companyName) throws Exception {

        if (StringUtils.isBlank(companyName)) {
            return ResponseConstants.MISSING_PARAMTER;
        }
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);

        List<Map<String, Object>> companyList = null;

        try {
            List<String> departmentIdList = null;


            if ("1".equals(loginUserMap.get("roleId"))) {
                departmentIdList = departmentService.selectChildDepartmentIds("1", loginUserMap);
            } else {
                departmentIdList = userDataPermissionService.getDataPermissionDepartmentIdList((String) loginUserMap.get("pcCompanyId"), loginUserId);
            }
            companyList = companyServie.searchCompanyByName(companyName, departmentIdList, 0, 20);

        } catch (Exception e) {

            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(companyList);
        return responseDbCenter;
    }


    @ApiOperation(value = "根据公司名搜索----无数据权限")
    @ResponseBody
    @RequestMapping(value = "/searchCompanyByName", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter getCompanyByName(HttpServletRequest req, HttpServletResponse rsp,
                                             @ApiParam(value = "公司名称", required = false) @RequestParam(required = false) String companyName) throws Exception {

        if (StringUtils.isBlank(companyName)) {
            return ResponseConstants.MISSING_PARAMTER;
        }
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);

        List<Map<String, Object>> companyList = null;

        try {
            List<String> departmentIdList = null;


            if ("1".equals(loginUserMap.get("roleId"))) {
                departmentIdList = departmentService.selectChildDepartmentIds("1", loginUserMap);
            } else {
                departmentIdList = departmentService.selectChildDepartmentIds((String) loginUserMap.get("pcCompanyId"), loginUserMap);
            }
            companyList = companyServie.searchCompanyByName(companyName, departmentIdList, 0, 20);

        } catch (Exception e) {

            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(companyList);
        return responseDbCenter;
    }


    @ApiOperation(value = "查询公司基本信息")
    @ResponseBody
    @RequestMapping(value = "/selectBasicInfoByCompanyId", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter selectBasicInfoByCompanyId(HttpServletRequest req, HttpServletResponse rsp,
                                                       @ApiParam(value = "公司Id", required = false) @RequestParam(required = false) String companyId)
            throws Exception {
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");

        String token = req.getHeader("token");


        if (StringUtils.isBlank(companyId)) {

            return ResponseConstants.MISSING_PARAMTER;
        }

        Company company = null;
        Map<String, Object> address = new HashMap();
        List<Map<String, Object>> userInfos = null;
        Map<String, Object> map = new HashMap<>();


        try {

            // 得到企业信息，工商信息，里面包含企查查信息
            company = companyServie.selectCompanyInfoById(companyId);

            if (company == null) {

                return ResponseConstants.FUNC_GETCOMPANYERROR;

            } else {

                Map<String, Object> user = userService.selectUserById(loginUserId);

                address.put("country", company.getCountry());
                address.put("province", company.getProvince());
                address.put("city", company.getCity());
                address.put("county", company.getCounty());
                address.put("address", company.getAddress());


                // 得到企业联系人多个
                userInfos = userInfoService.selectUserInfoByCompanyId(company.getId());


                Integer dynamicCount = userDynamicService.selectUserDynamicCountByCompanyId(company.getId());

                //查询相关人员
                List<String> companyIdList = new ArrayList<>();
                companyIdList.add(companyId);
                List<Map<String, Object>> relativeUserList = companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIdList);
                company.setRelativeUserList(relativeUserList);


                /**
                 * 隐藏敏感信息
                 */

                boolean flag = true;

                /**
                 * 如果即没有总监，也没有顾问，则默认所有人都可以看
                 */
                if ((StringUtils.isBlank(company.getCompanyAdviserId()) && StringUtils.isBlank(company.getCompanyDirectorId()))) {
                    flag = true;
                } else if ((!userService.hasPrivatePermission(token, "company/private")
                        && !"1".equals(user.get("roleId"))
                        && (null == user.get("isAdmin") || (null != user.get("isAdmin") && 1 != (Integer) user.get("isAdmin")))
                        && !loginUserId.equals(company.getCompanyAdviserId())
                        && !loginUserId.equals(company.getCompanyDirectorId())
                        && !loginUserId.equals(company.getCreatedBy())
                        && !companyServie.isRelativePermission(companyId, loginUserId))) {
                    flag = false;
                }

                if (!flag) {
                    for (Map<String, Object> userMap : userInfos) {
                        if (userMap != null) {
                            userMap.put("mobile", "***");
                            userMap.put("phone", "***");

                        }
                    }
                }


                // 找出默认联系人
                String defaultContactUserId = company.getContactUserId();
                if (!StringUtils.isBlank(defaultContactUserId)) {
                    for (Map<String, Object> userMap : userInfos) {
                        if (defaultContactUserId.equals(userMap.get("userId"))) {
                            userMap.put("uflag", 1);
                        } else {
                            userMap.put("uflag", 0);
                        }
                    }
                }

                map.put("company", company);
                map.put("address", address);
                map.put("userInfos", userInfos);
                map.put("dynamicCount", dynamicCount);
            }
        } catch (Exception e) {

            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        responseDbCenter.setResModel(map);

        return responseDbCenter;
    }


    /**
     * @param req
     * @param rsp
     * @return
     * @author: wujianhui
     * @2017年10月20日
     * @功能描述:查询企业高级信息
     */
    @ApiOperation(value = "查询公司高级信息")
    @ResponseBody
    @RequestMapping(value = "/selectAdvancedInfoByCompanyId", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseDbCenter selectAdvancedInfoByCompanyId(HttpServletRequest req, HttpServletResponse rsp,
                                                          @ApiParam(value = "公司Id", required = true) @RequestParam(required = true) String companyId)
            throws Exception {

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        Map<String, Object> address = new HashMap();
        Map<String, Object> map = new HashMap<>();

        try {

            // 得到企业信息，工商信息，里面包含企查查信息
            Map<String, Object> company = companyServie.selectCompanyById(companyId);

            if (company == null) {

                return ResponseConstants.FUNC_GETCOMPANYERROR;

            } else {

                address.put("country", company.get("country"));
                address.put("province", company.get("province"));
                address.put("city", company.get("city"));
                address.put("county", company.get("county"));
                address.put("address", company.get("address"));

                //查询相关人员
                List<String> companyIdList = new ArrayList<>();
                companyIdList.add(companyId);
                List<Map<String, Object>> relativeUserList = companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIdList);
                company.put("relativeUserList", relativeUserList);
                map.put("company", company);
                map.put("address", address);
            }
        } catch (Exception e) {

            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }


        responseDbCenter.setResModel(map);

        return responseDbCenter;
    }


    /**
     * @param req
     * @param rsp
     * @return
     * @author: xiaoz
     * @2017年4月19日
     * @功能描述:通过企业名称查询企业名字
     */
    @ApiOperation(value = "根据公司名称精确查找公司")
    @RequestMapping(value = "/selectCompanyByName", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ResponseDbCenter selectCompanyByName(HttpServletRequest req, HttpServletResponse rsp,
                                                @ApiParam(value = "公司名称", required = true) @RequestParam(required = true) String companyName) throws Exception {


        if (StringUtils.isBlank(companyName)) {

            return ResponseConstants.MISSING_PARAMTER;
        }
        String loginUserId = (String) req.getAttribute("loginUserId");
        List<Company> companys = null;
        Company company = null;
        try {

            String pcCompanyId = null;
            Map<String, Object> mapp = userService.selectUserById(loginUserId);
            if (mapp != null && mapp.size() > 0) {
                pcCompanyId = (String) mapp.get("pcCompanyId");
            }

            companys = companyServie.selectCompanyByName(companyName, pcCompanyId);
            if (companys != null && companys.size() > 0) {
                company = companys.get(0);
            }

        } catch (Exception e) {

            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        responseDbCenter.setResModel(company);

        return responseDbCenter;
    }

    @ApiOperation(value = "查询是否存在同名公司")
    @RequestMapping(value = "/existsCompany", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ResponseDbCenter checkCompanyByName(HttpServletRequest req, HttpServletResponse rsp,
                                               @ApiParam(value = "公司名称", required = true) @RequestParam(required = true) String companyName) throws Exception {


        if (StringUtils.isBlank(companyName)) {

            return ResponseConstants.MISSING_PARAMTER;
        }

        String loginUserId = (String) req.getAttribute("loginUserId");
        List<Company> companys = null;

        try {

            String pcCompanyId = null;
            Map<String, Object> mapp = userService.selectUserById(loginUserId);
            if (mapp != null && mapp.size() > 0) {
                pcCompanyId = (String) mapp.get("pcCompanyId");
            }

            companys = companyServie.selectCompanyByName(companyName, pcCompanyId);

        } catch (Exception e) {

            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        if (companys != null && companys.size() > 0) {

            responseDbCenter.setResModel(true);

        } else {

            responseDbCenter.setResModel(false);
        }

        return responseDbCenter;
    }

    /**
     * @param req
     * @param rsp
     * @return
     * @author: xiaoz
     * @2017年4月20日
     * @功能描述:删除企业信息
     */
    @ApiOperation(value = "批量删除公司")
    @RequestMapping(value = "/deleteCompanyByIds", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ResponseDbCenter deleteCompanyByIds(HttpServletRequest req, HttpServletResponse rsp,
                                               @ApiParam(value = "ids 多个值以逗号隔开  ", required = true) @RequestParam(required = true) String ids) throws Exception {


        if (StringUtils.isBlank(ids)) {

            return ResponseConstants.MISSING_PARAMTER;
        }

        List<String> idList = new ArrayList<>();

        String[] cids = ids.split(",");

        String idString = "";
        for (int i = 0; i < cids.length; i++) {

            idString += "'" + cids[i] + "',";
            idList.add(cids[i]);
        }

        if (StringUtils.isNotBlank(idString)) {
            idString = "(" + idString.substring(0, idString.lastIndexOf(",")) + ")";
        }


        try {
                            /*
                 * 从索引库中删除该企业相关的信息
				 */
            solrService.deleteDocumentByCompanyId(idList);

            companyServie.deleteCompanyById(idString);
            companyRelativeUserService.deleteByCompanyIdsString(idString);

        } catch (Exception e) {

            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }

    /**
     * @param req
     * @param rsp
     * @return
     * @author: xiaoz
     * @2017年4月20日
     * @功能描述:删除企业信息
     */
    @ApiOperation(value = "更新公司标签")
    @RequestMapping(value = "/updateCompanyLabelById", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ResponseDbCenter updateCompanyLabelById(HttpServletRequest req, HttpServletResponse rsp,
                                                   @ApiParam(value = "公司Id", required = true) @RequestParam(required = true) String companyId,
                                                   @ApiParam(value = "标签值", required = false) @RequestParam(required = false) String label) throws Exception {


        if (StringUtils.isBlank(companyId)) {

            return ResponseConstants.MISSING_PARAMTER;
        }


        try {

            companyServie.updateCompanyLabelById(companyId, label);
            companyServie.updateInfoScore(companyId);
//            solrService.updateCompanyIndex(companyId);


        } catch (Exception e) {

            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }


    @ApiOperation(value = "更新公司信息完整度")
    @RequestMapping(value = "/bbbbb", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ResponseDbCenter bbbbb(HttpServletRequest req, HttpServletResponse rsp) throws Exception {

        try {

            List<String> idList = companyServie.selecAllCompanyId();
            for (int i = 0; i < idList.size(); i++) {
                companyServie.updateInfoScore(idList.get(i));
            }


        } catch (Exception e) {

            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }


    @ApiOperation(value = "修改客户")
    @RequestMapping(value = "/updateCompanyInfo", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResponseDbCenter updateCompanyInfo(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value = "公司Id", required = true) @RequestParam(required = true) String companyId,
                                              @ApiParam(value = "公司名称", required = false) @RequestParam(required = false) String companyName,
                                              @ApiParam(value = "社会信用统一编号", required = false) @RequestParam(required = false) String socialCredit,
                                              @ApiParam(value = "注册编号", required = false) @RequestParam(required = false) String registrationCode,
                                              @ApiParam(value = "组织机构编号", required = false) @RequestParam(required = false) String organizationCode,
                                              @ApiParam(value = "法人代表", required = false) @RequestParam(required = false) String representative,
                                              @ApiParam(value = "注册资金", required = false) @RequestParam(required = false) String registeredMoney,
                                              @ApiParam(value = "创建时间", required = false) @RequestParam(required = false) String establishTime,
                                              @ApiParam(value = "顾问Id", required = false) @RequestParam(required = false) String companyAdviserId,
                                              @ApiParam(value = "总监Id", required = false) @RequestParam(required = false) String companyDirectorId,
                                              @ApiParam(value = "公司logo", required = false) @RequestParam(required = false) String logo,
                                              @ApiParam(value = "开始营业时间", required = false) @RequestParam(required = false) String termStart,
                                              @ApiParam(value = "到期时间", required = false) @RequestParam(required = false) String operatingPeriod,
                                              @ApiParam(value = "注册机构", required = false) @RequestParam(required = false) String registrationAuthority,
                                              @ApiParam(value = "年营业额（万）", required = false) @RequestParam(required = false) String annualSalesVolume,
                                              @ApiParam(value = "年利润（年）", required = false) @RequestParam(required = false) String annualProfit,
                                              @ApiParam(value = "本年营收预测", required = false) @RequestParam(required = false) String thisYearSalesVolume,
                                              @ApiParam(value = "下一年营收预测", required = false) @RequestParam(required = false) String nextYearSalesVolume,
                                              @ApiParam(value = "业务范围 如 全国，华南", required = false) @RequestParam(required = false) String businessScope,
                                              @ApiParam(value = "审核日期", required = false) @RequestParam(required = false) String approveDate,
                                              @ApiParam(value = "公司行业类型", required = false) @RequestParam(required = false) String companyType,
                                              @ApiParam(value = "公司规模", required = false) @RequestParam(required = false) String companySize,
                                              @ApiParam(value = "公司英文名称", required = false) @RequestParam(required = false) String englishName,
                                              @ApiParam(value = "曾用名", required = false) @RequestParam(required = false) String beforeName,
                                              @ApiParam(value = "公司网站", required = false) @RequestParam(required = false) String website,
                                              @ApiParam(value = "公司邮箱", required = false) @RequestParam(required = false) String email,
                                              @ApiParam(value = "微信", required = false) @RequestParam(required = false) String weChat,
                                              @ApiParam(value = "企业描述", required = false) @RequestParam(required = false) String content,
                                              @ApiParam(value = "企业定位", required = false) @RequestParam(required = false) String companyPosition,
                                              @ApiParam(value = "公司类型 如:有限责任公司", required = false) @RequestParam(required = false) String econKind,
                                              @ApiParam(value = "省", required = false) @RequestParam(required = false) String province,
                                              @ApiParam(value = "市", required = false) @RequestParam(required = false) String city,
                                              @ApiParam(value = "区", required = false) @RequestParam(required = false) String county,
                                              @ApiParam(value = "公司地址", required = false) @RequestParam(required = false) String address,
                                              @ApiParam(value = "行业Id", required = false) @RequestParam(required = false) String industryId,
                                              @ApiParam(value = "公司一句话描述", required = false) @RequestParam(required = false) String companyDesc,
                                              @ApiParam(value = "获投状态 ", required = false) @RequestParam(required = false) String investStatus,
                                              @ApiParam(value = "融资状态", required = false) @RequestParam(required = false) String financeStatus,
                                              @ApiParam(value = "经营状态 如（存续）", required = false) @RequestParam(required = false) String manageType,
                                              @ApiParam(value = "经营范围", required = false) @RequestParam(required = false) String manageScope,
                                              @ApiParam(value = "公司属性Id 如 （数据字典中的 国有 私有）", required = false) @RequestParam(required = false) String companyProperty,
                                              @ApiParam(value = "客户类型Id", required = false) @RequestParam(required = false) String userTypeId,
                                              @ApiParam(value = "客户等级Id", required = false) @RequestParam(required = false) String userLevelId,
                                              @ApiParam(value = "渠道来源Id", required = false) @RequestParam(required = false) String channelId,
                                              @ApiParam(value = "报名日期", required = false) @RequestParam(required = false) String enrollDate,
                                              @ApiParam(value = "相关人员Id json数组 如 ['1','2']", required = false) @RequestParam(required = false) String relativeUserIds,
                                              @ApiParam(value = "优先级Id", required = false) @RequestParam(required = false) String priorityId,
                                              @ApiParam(value = "参会状态  未参会  已参会", required = false) @RequestParam(required = false) String attendStatus,
                                              @ApiParam(value = "所处阶段", required = false) @RequestParam(required = false) String companyPhaseId,
                                              @ApiParam(value = "部门Id", required = false) @RequestParam(required = false) String departmentId) throws Exception {

        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");

        Map<String, Object> company = new HashMap();
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();


        /**
         * 如果部门Id为空，则添加时默认以添加人的部门Id作为该条数据的部门
         */
        if (StringUtils.isBlank(departmentId)) {
            Map<String, Object> map = userService.selectUserById(loginUserId);
            departmentId = (String) map.get("departmentId");
        }


        Map<String, Object> companyMap = departmentService.getCompanyIdByDepartmentId(departmentId);
        //设置该记录属性哪一个部门客户
        company.put("pcCompanyId", companyMap.get("id"));


        // 通过企业名称查询该企业是否存在，如果存在就返回提示信息

        List<Map<String, Object>> alreadyExists = companyServie.selectCompanyByNameUnDeleted(companyName, (String) company.get("pcCompanyId"));
        if (alreadyExists.size() > 0) {
            if (!alreadyExists.get(0).get("id").equals(companyId)) {
                //如果名称与未删除的数据冲突，则不允许再添加了
                return ResponseConstants.FUNC_COMPANY_EXIST;
            }
        }

        try {
            company.put("id", companyId);
            company.put("companyName", companyName);
            company.put("englishName", englishName);
            company.put("label", null);
            company.put("representative", representative);
            company.put("industryId", industryId);
            company.put("investStatus", investStatus);
            company.put("financeStatus", financeStatus);
            company.put("phone", null);
            company.put("companyAdviserId", companyAdviserId);
            company.put("companyDirectorId", companyDirectorId);
            company.put("logo", logo);
            company.put("following", null);
            company.put("qccUpdatedDate", DateUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            company.put("province", province);
            company.put("city", city);
            company.put("county", county);
            company.put("address", address);

            company.put("userLevelId", userLevelId);
            company.put("userTypeId", userTypeId);
            company.put("enrollDate", enrollDate);
            company.put("channelId", channelId);
            company.put("departmentId", departmentId);
            company.put("priorityId", priorityId);
            company.put("attendStatus", attendStatus);
            company.put("companyPhaseId", companyPhaseId);


            // 企业详情属性
            company.put("socialCredit", socialCredit);
            company.put("registrationCode", registrationCode);
            company.put("organizationCode", organizationCode);
            company.put("manageType", manageType);
            company.put("manageScope", manageScope);
            company.put("registeredMoney", registeredMoney);
            company.put("registrationAuthority", registrationAuthority);
            company.put("annualSalesVolume", annualSalesVolume);
            company.put("annualProfit", annualProfit);
            company.put("thisYearSalesVolume", thisYearSalesVolume);
            company.put("nextYearSalesVolume", nextYearSalesVolume);
            company.put("businessScope", businessScope);
            company.put("companyType", companyType);
            company.put("companyPropertyId", companyProperty);
            company.put("companySize", companySize);
            company.put("beforeName", beforeName);
            company.put("companyDesc", companyDesc);
            company.put("website", website);
            company.put("phone", null);
            company.put("email", email);
            company.put("wechat", weChat);
            company.put("content", content);
            company.put("companyPosition", companyPosition);
            company.put("econKind", econKind);
            company.put("establishTime", establishTime);
            company.put("approveDate", approveDate);
            company.put("termStart", termStart);
            company.put("operatingPeriod", operatingPeriod);
            company.put("companyOpportunity", null);
            company.put("updatedBy", loginUserId);
            company.put("updateDate", new Date());
            //更新企业信息
            companyServie.updateCompanyInfoById(company);
            companyServie.updateCompanyDetailInfoById(company);
            //修改客户部门以后，我们需要将其对应的文件夹等的部门Id也修改了
            if (StringUtils.isNotBlank(departmentId)) {
                pagerFileService.editFolderDepartment(companyId, departmentId, loginUserId);
            }


            userDynamicService.addUserDynamic(loginUserId, companyId, companyName, "更新", "修改了企业\"" + companyName + "\"", 0, null, null, null);
            //更新企业相关人员记录
            if (StringUtils.isNotBlank(relativeUserIds)) {
                List<String> userIds = JSON.parseObject(relativeUserIds, new TypeReference<List<String>>() {
                });
                List<Map<String, Object>> relativeUserMapList = new ArrayList<>();
                if (null != userIds) {
                    for (int i = 0; i < userIds.size(); i++) {
                        Map<String, Object> map = new HashMap<>();
                        String id = UUID.randomUUID().toString();
                        map.put("id", id);
                        map.put("companyId", companyId);
                        map.put("userId", userIds.get(i));
                        relativeUserMapList.add(map);
                    }
                    companyRelativeUserService.deleteByCompanyId(companyId);
                    if (relativeUserMapList.size() > 0) {
                        companyRelativeUserService.insertList(relativeUserMapList);
                    }
                }
            }

            //更新企业信息完整度
            companyServie.updateInfoScore((String) company.get("id"));


            //将该企业维护到solr库中
//            solrService.updateCompanyIndex(companyId);


        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }


        return responseDbCenter;
    }


    @ApiOperation(value = "添加相关人员")
    @RequestMapping(value = "/addRelativeUsers", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResponseDbCenter addRelativeUsers(HttpServletRequest req, HttpServletResponse rsp,
                                             @ApiParam(value = "公司Id", required = true) @RequestParam(required = true) String companyId,
                                             @ApiParam(value = "相关人员Id 用逗号分隔", required = true) @RequestParam(required = true) String relativeUserIds
    ) throws Exception {

        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        try {

            //更新企业相关人员记录
            if (StringUtils.isNotBlank(relativeUserIds)) {
                String[] arrays = relativeUserIds.split(",");
                List<String> companyIdList = new ArrayList<>();
                companyIdList.add(companyId);
                List<Map<String, Object>> relativeList = companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIdList);
                List<String> notInDbList = new ArrayList<>();
                for (int i = 0; i < arrays.length; i++) {
                    boolean exists = false;
                    for (int j = 0; j < relativeList.size(); j++) {
                        Map m = relativeList.get(j);
                        if (arrays[i].equals(m.get("userId"))) {
                            exists = true;
                        }
                    }
                    if (!exists) {
                        notInDbList.add(arrays[i]);
                    }

                }


                List<Map<String, Object>> relativeUserMapList = new ArrayList<>();

                for (int i = 0; i < notInDbList.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    String id = UUID.randomUUID().toString();
                    map.put("id", id);
                    map.put("companyId", companyId);
                    map.put("userId", notInDbList.get(i));
                    relativeUserMapList.add(map);

                }

                if (relativeUserMapList.size() > 0) {
                    companyRelativeUserService.insertList(relativeUserMapList);
                }
            }


        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }


        return responseDbCenter;
    }


    @ApiOperation(value = "移除相关人员")
    @RequestMapping(value = "/delRelativeUser", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResponseDbCenter delRelativeUser(HttpServletRequest req, HttpServletResponse rsp,
                                            @ApiParam(value = "id", required = true) @RequestParam(required = true) String id
    ) throws Exception {


        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        try {

            companyRelativeUserService.deleteRelativeUserById(id);


        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }


        return responseDbCenter;
    }


    @ApiOperation(value = "查询相关人员")
    @RequestMapping(value = "/selectRelativeUsers", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResponseDbCenter selectRelativeUsers(HttpServletRequest req, HttpServletResponse rsp,
                                                @ApiParam(value = "companyId", required = true) @RequestParam(required = true) String companyId
    ) throws Exception {


        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        try {
            Company company = companyServie.selectCompanyInfoById(companyId);
            List<String> companyIdList = new ArrayList<>();
            companyIdList.add(companyId);
            List<Map<String, Object>> mapList = companyRelativeUserService.selectRelativeUserListByCompanyIds(companyIdList);

            for (int i = 0; i < mapList.size(); i++) {
                mapList.get(i).put("roleInGroup", "普通成员");
            }


            List<Map<String, Object>> list0 = new ArrayList<>();
            Map<String, Object> createdBy = new HashMap<>();
            createdBy.put("roleInGroup", "创建人");
            createdBy.put("uname", company.getCreatedByName());
            createdBy.put("mobile", company.getCreatedByMobile());
            list0.add(createdBy);


            Map<String, Object> companyAdviser = new HashMap<>();
            companyAdviser.put("roleInGroup", "负责人");
            companyAdviser.put("uname", company.getCompanyAdviserName());
            companyAdviser.put("mobile", company.getCompanyAdviserMobile());
            list0.add(companyAdviser);


            Map<String, Object> companyDirector = new HashMap<>();
            companyDirector.put("roleInGroup", "总监");
            companyDirector.put("uname", company.getCompanyDirectorName());
            companyDirector.put("mobile", company.getCompanyDirectorMobile());
            list0.add(companyDirector);


            list0.addAll(mapList);

            responseDbCenter.setResModel(list0);

        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }


        return responseDbCenter;
    }


    @ApiOperation(value = "修改潜在客户")
    @RequestMapping(value = "/updatePotentialCompanyInfo", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResponseDbCenter updatePotentialCompanyInfo(HttpServletRequest req, HttpServletResponse rsp,
                                                       @ApiParam(value = "公司Id", required = true) @RequestParam(required = true) String companyId,
                                                       @ApiParam(value = "公司名称", required = false) @RequestParam(required = false) String companyName,
                                                       @ApiParam(value = "社会信用统一编号", required = false) @RequestParam(required = false) String socialCredit,
                                                       @ApiParam(value = "注册编号", required = false) @RequestParam(required = false) String registrationCode,
                                                       @ApiParam(value = "组织机构编号", required = false) @RequestParam(required = false) String organizationCode,
                                                       @ApiParam(value = "法人代表", required = false) @RequestParam(required = false) String representative,
                                                       @ApiParam(value = "注册资金", required = false) @RequestParam(required = false) String registeredMoney,
                                                       @ApiParam(value = "创建时间", required = false) @RequestParam(required = false) String establishTime,
                                                       @ApiParam(value = "顾问Id", required = false) @RequestParam(required = false) String companyAdviserId,
                                                       @ApiParam(value = "总监Id", required = false) @RequestParam(required = false) String companyDirectorId,
                                                       @ApiParam(value = "公司logo", required = false) @RequestParam(required = false) String logo,
                                                       @ApiParam(value = "开始营业时间", required = false) @RequestParam(required = false) String termStart,
                                                       @ApiParam(value = "到期时间", required = false) @RequestParam(required = false) String operatingPeriod,
                                                       @ApiParam(value = "注册机构", required = false) @RequestParam(required = false) String registrationAuthority,
                                                       @ApiParam(value = "年营业额（万）", required = false) @RequestParam(required = false) String annualSalesVolume,
                                                       @ApiParam(value = "年利润（年）", required = false) @RequestParam(required = false) String annualProfit,
                                                       @ApiParam(value = "本年营收预测", required = false) @RequestParam(required = false) String thisYearSalesVolume,
                                                       @ApiParam(value = "下一年营收预测", required = false) @RequestParam(required = false) String nextYearSalesVolume,
                                                       @ApiParam(value = "业务范围 如 全国，华南", required = false) @RequestParam(required = false) String businessScope,
                                                       @ApiParam(value = "审核日期", required = false) @RequestParam(required = false) String approveDate,
                                                       @ApiParam(value = "公司行业类型", required = false) @RequestParam(required = false) String companyType,
                                                       @ApiParam(value = "公司规模", required = false) @RequestParam(required = false) String companySize,
                                                       @ApiParam(value = "公司英文名称", required = false) @RequestParam(required = false) String englishName,
                                                       @ApiParam(value = "曾用名", required = false) @RequestParam(required = false) String beforeName,
                                                       @ApiParam(value = "公司网站", required = false) @RequestParam(required = false) String website,
                                                       @ApiParam(value = "公司邮箱", required = false) @RequestParam(required = false) String email,
                                                       @ApiParam(value = "微信", required = false) @RequestParam(required = false) String weChat,
                                                       @ApiParam(value = "企业描述", required = false) @RequestParam(required = false) String content,
                                                       @ApiParam(value = "企业定位", required = false) @RequestParam(required = false) String companyPosition,
                                                       @ApiParam(value = "公司类型 如:有限责任公司", required = false) @RequestParam(required = false) String econKind,
                                                       @ApiParam(value = "省", required = false) @RequestParam(required = false) String province,
                                                       @ApiParam(value = "市", required = false) @RequestParam(required = false) String city,
                                                       @ApiParam(value = "区", required = false) @RequestParam(required = false) String county,
                                                       @ApiParam(value = "公司地址", required = false) @RequestParam(required = false) String address,
                                                       @ApiParam(value = "行业Id", required = false) @RequestParam(required = false) String industryId,
                                                       @ApiParam(value = "公司一句话描述", required = false) @RequestParam(required = false) String companyDesc,
                                                       @ApiParam(value = "获投状态 ", required = false) @RequestParam(required = false) String investStatus,
                                                       @ApiParam(value = "融资状态", required = false) @RequestParam(required = false) String financeStatus,
                                                       @ApiParam(value = "经营状态 如（存续）", required = false) @RequestParam(required = false) String manageType,
                                                       @ApiParam(value = "经营范围", required = false) @RequestParam(required = false) String manageScope,
                                                       @ApiParam(value = "公司属性Id 如 （数据字典中的 国有 私有）", required = false) @RequestParam(required = false) String companyProperty,
                                                       @ApiParam(value = "客户类型Id", required = false) @RequestParam(required = false) String userTypeId,
                                                       @ApiParam(value = "客户等级Id", required = false) @RequestParam(required = false) String userLevelId,
                                                       @ApiParam(value = "渠道来源Id", required = false) @RequestParam(required = false) String channelId,
                                                       @ApiParam(value = "报名日期", required = false) @RequestParam(required = false) String enrollDate,
                                                       @ApiParam(value = "相关人员Id json数组 如 ['1','2']", required = false) @RequestParam(required = false) String relativeUserIds,
                                                       @ApiParam(value = "优先级Id", required = false) @RequestParam(required = false) String priorityId,
                                                       @ApiParam(value = "参会状态  未参会  已参会", required = false) @RequestParam(required = false) String attendStatus,
                                                       @ApiParam(value = "所处阶段", required = false) @RequestParam(required = false) String companyPhaseId,
                                                       @ApiParam(value = "部门Id", required = false) @RequestParam(required = false) String departmentId) throws Exception {

        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");

        Map<String, Object> company = new HashMap();
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();


        /**
         * 如果部门Id为空，则添加时默认以添加人的部门Id作为该条数据的部门
         */
        if (StringUtils.isBlank(departmentId)) {
            Map<String, Object> map = userService.selectUserById(loginUserId);
            departmentId = (String) map.get("departmentId");
        }


        Map<String, Object> companyMap = departmentService.getCompanyIdByDepartmentId(departmentId);
        //设置该记录属性哪一个部门客户
        company.put("pcCompanyId", companyMap.get("id"));


        // 通过企业名称查询该企业是否存在，如果存在就返回提示信息

        List<Map<String, Object>> alreadyExists = companyServie.selectCompanyByNameUnDeleted(companyName, (String) company.get("pcCompanyId"));
        if (alreadyExists.size() > 0) {
            if (!alreadyExists.get(0).get("id").equals(companyId)) {
                //如果名称与未删除的数据冲突，则不允许再添加了
                return ResponseConstants.FUNC_COMPANY_EXIST;
            }
        }

        try {
            company.put("id", companyId);
            company.put("companyName", companyName);
            company.put("englishName", englishName);
            company.put("label", null);
            company.put("representative", representative);
            company.put("industryId", industryId);
            company.put("investStatus", investStatus);
            company.put("financeStatus", financeStatus);
            company.put("phone", null);
            company.put("companyAdviserId", companyAdviserId);
            company.put("companyDirectorId", companyDirectorId);
            company.put("logo", logo);
            company.put("following", null);
            company.put("qccUpdatedDate", DateUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            company.put("province", province);
            company.put("city", city);
            company.put("county", county);
            company.put("address", address);

            company.put("userLevelId", userLevelId);
            company.put("userTypeId", userTypeId);
            company.put("enrollDate", enrollDate);
            company.put("channelId", channelId);
            company.put("departmentId", departmentId);
            company.put("priorityId", priorityId);
            company.put("attendStatus", attendStatus);
            company.put("companyPhaseId", companyPhaseId);


            // 企业详情属性
            company.put("socialCredit", socialCredit);
            company.put("registrationCode", registrationCode);
            company.put("organizationCode", organizationCode);
            company.put("manageType", manageType);
            company.put("manageScope", manageScope);
            company.put("registeredMoney", registeredMoney);
            company.put("registrationAuthority", registrationAuthority);
            company.put("annualSalesVolume", annualSalesVolume);
            company.put("annualProfit", annualProfit);
            company.put("thisYearSalesVolume", thisYearSalesVolume);
            company.put("nextYearSalesVolume", nextYearSalesVolume);
            company.put("businessScope", businessScope);
            company.put("companyType", companyType);
            company.put("companyPropertyId", companyProperty);
            company.put("companySize", companySize);
            company.put("beforeName", beforeName);
            company.put("companyDesc", companyDesc);
            company.put("website", website);
            company.put("phone", null);
            company.put("email", email);
            company.put("wechat", weChat);
            company.put("content", content);
            company.put("companyPosition", companyPosition);
            company.put("econKind", econKind);
            company.put("establishTime", establishTime);
            company.put("approveDate", approveDate);
            company.put("termStart", termStart);
            company.put("operatingPeriod", operatingPeriod);
            company.put("companyOpportunity", null);
            company.put("updatedBy", loginUserId);
            company.put("updateDate", new Date());
            //更新企业信息
            companyServie.updateCompanyInfoById(company);
            companyServie.updateCompanyDetailInfoById(company);
            //修改客户部门以后，我们需要将其对应的文件夹等的部门Id也修改了
            if (StringUtils.isNotBlank(departmentId)) {
                pagerFileService.editFolderDepartment(companyId, departmentId, loginUserId);
            }


            userDynamicService.addUserDynamic(loginUserId, companyId, companyName, "更新", "修改了企业\"" + companyName + "\"", 0, null, null, null);
            //更新企业相关人员记录
            if (StringUtils.isNotBlank(relativeUserIds)) {
                List<String> userIds = JSON.parseObject(relativeUserIds, new TypeReference<List<String>>() {
                });
                List<Map<String, Object>> relativeUserMapList = new ArrayList<>();
                if (null != userIds) {
                    for (int i = 0; i < userIds.size(); i++) {
                        Map<String, Object> map = new HashMap<>();
                        String id = UUID.randomUUID().toString();
                        map.put("id", id);
                        map.put("companyId", companyId);
                        map.put("userId", userIds.get(i));
                        relativeUserMapList.add(map);
                    }
                    companyRelativeUserService.deleteByCompanyId(companyId);
                    if (relativeUserMapList.size() > 0) {
                        companyRelativeUserService.insertList(relativeUserMapList);
                    }
                }
            }

            //更新企业信息完整度
            companyServie.updateInfoScore((String) company.get("id"));


            //将该企业维护到solr库中
//            solrService.updateCompanyIndex(companyId);


        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }


        return responseDbCenter;
    }


    @ApiOperation(value = "添加客户")
    @RequestMapping(value = "/addCompanyInfo", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResponseDbCenter addCompanyInfo(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "公司名称", required = false) @RequestParam(required = false) String companyName,
                                           @ApiParam(value = "社会信用统一编号", required = false) @RequestParam(required = false) String socialCredit,
                                           @ApiParam(value = "注册编号", required = false) @RequestParam(required = false) String registrationCode,
                                           @ApiParam(value = "组织机构编号", required = false) @RequestParam(required = false) String organizationCode,
                                           @ApiParam(value = "法人代表", required = false) @RequestParam(required = false) String representative,
                                           @ApiParam(value = "注册资金", required = false) @RequestParam(required = false) String registeredMoney,
                                           @ApiParam(value = "创建时间", required = false) @RequestParam(required = false) String establishTime,
                                           @ApiParam(value = "顾问Id", required = false) @RequestParam(required = false) String companyAdviserId,
                                           @ApiParam(value = "总监Id", required = false) @RequestParam(required = false) String companyDirectorId,
                                           @ApiParam(value = "公司logo", required = false) @RequestParam(required = false) String logo,
                                           @ApiParam(value = "开始营业时间", required = false) @RequestParam(required = false) String termStart,
                                           @ApiParam(value = "到期时间", required = false) @RequestParam(required = false) String operatingPeriod,
                                           @ApiParam(value = "注册机构", required = false) @RequestParam(required = false) String registrationAuthority,
                                           @ApiParam(value = "年营业额（万）", required = false) @RequestParam(required = false) String annualSalesVolume,
                                           @ApiParam(value = "年利润（年）", required = false) @RequestParam(required = false) String annualProfit,
                                           @ApiParam(value = "本年营收预测", required = false) @RequestParam(required = false) String thisYearSalesVolume,
                                           @ApiParam(value = "下一年营收预测", required = false) @RequestParam(required = false) String nextYearSalesVolume,
                                           @ApiParam(value = "业务范围 如 全国，华南", required = false) @RequestParam(required = false) String businessScope,
                                           @ApiParam(value = "审核日期", required = false) @RequestParam(required = false) String approveDate,
                                           @ApiParam(value = "公司行业类型", required = false) @RequestParam(required = false) String companyType,
                                           @ApiParam(value = "公司规模", required = false) @RequestParam(required = false) String companySize,
                                           @ApiParam(value = "公司英文名称", required = false) @RequestParam(required = false) String englishName,
                                           @ApiParam(value = "曾用名", required = false) @RequestParam(required = false) String beforeName,
                                           @ApiParam(value = "公司网站", required = false) @RequestParam(required = false) String website,
                                           @ApiParam(value = "公司邮箱", required = false) @RequestParam(required = false) String email,
                                           @ApiParam(value = "微信", required = false) @RequestParam(required = false) String weChat,
                                           @ApiParam(value = "企业描述", required = false) @RequestParam(required = false) String content,
                                           @ApiParam(value = "企业定位", required = false) @RequestParam(required = false) String companyPosition,
                                           @ApiParam(value = "公司类型 如:有限责任公司", required = false) @RequestParam(required = false) String econKind,
                                           @ApiParam(value = "省", required = false) @RequestParam(required = false) String province,
                                           @ApiParam(value = "市", required = false) @RequestParam(required = false) String city,
                                           @ApiParam(value = "区", required = false) @RequestParam(required = false) String county,
                                           @ApiParam(value = "公司地址", required = false) @RequestParam(required = false) String address,
                                           @ApiParam(value = "行业Id", required = false) @RequestParam(required = false) String industryId,
                                           @ApiParam(value = "公司一句话描述", required = false) @RequestParam(required = false) String companyDesc,
                                           @ApiParam(value = "获投状态 ", required = false) @RequestParam(required = false) String investStatus,
                                           @ApiParam(value = "融资状态", required = false) @RequestParam(required = false) String financeStatus,
                                           @ApiParam(value = "经营状态 如（存续）", required = false) @RequestParam(required = false) String manageType,
                                           @ApiParam(value = "经营范围", required = false) @RequestParam(required = false) String manageScope,
                                           @ApiParam(value = "公司属性Id 如 （数据字典中的 国有 私有）", required = false) @RequestParam(required = false) String companyProperty,
                                           @ApiParam(value = "客户类型Id", required = false) @RequestParam(required = false) String userTypeId,
                                           @ApiParam(value = "客户等级Id", required = false) @RequestParam(required = false) String userLevelId,
                                           @ApiParam(value = "渠道来源Id", required = false) @RequestParam(required = false) String channelId,
                                           @ApiParam(value = "报名日期", required = false) @RequestParam(required = false) String enrollDate,
                                           @ApiParam(value = "优先级Id", required = false) @RequestParam(required = false) String priorityId,
                                           @ApiParam(value = "参会状态  未参会  已参会", required = false) @RequestParam(required = false) String attendStatus,
                                           @ApiParam(value = "联系人", required = false) @RequestParam(required = false) String contactName,
                                           @ApiParam(value = "联系电话", required = false) @RequestParam(required = false) String contactPhone,
                                           @ApiParam(value = "所处阶段", required = false) @RequestParam(required = false) String companyPhaseId,
                                           @ApiParam(value = "部门Id", required = false) @RequestParam(required = false) String departmentId) throws Exception {

        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");

        Map<String, Object> company = new HashMap();
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        /**
         * 如果部门Id为空，则添加时默认以添加人的部门Id作为该条数据的部门
         */
        if (StringUtils.isBlank(departmentId)) {
            Map<String, Object> map = userService.selectUserById(loginUserId);
            departmentId = (String) map.get("departmentId");
        }


        Map<String, Object> companyMap = departmentService.getCompanyIdByDepartmentId(departmentId);
        //设置该记录属性哪一个部门客户
        company.put("pcCompanyId", companyMap.get("id"));


        // 通过企业名称查询该企业是否存在，如果存在就返回提示信息
        List<Map<String, Object>> alreadyExists = companyServie.selectCompanyByNameUnDeleted(companyName, (String) company.get("pcCompanyId"));
        if (alreadyExists.size() > 0) {
            //如果名称与未删除的数据冲突，则不允许再添加了
            return ResponseConstants.FUNC_COMPANY_EXIST;
        }
        String insertCompanyId = UUID.randomUUID().toString();
        company.put("id", insertCompanyId);
        company.put("potentialStatus", 1);
        company.put("companyName", companyName);
        company.put("englishName", englishName);
        company.put("label", null);
        company.put("representative", representative);
        company.put("industryId", industryId);
        company.put("investStatus", investStatus);
        company.put("financeStatus", financeStatus);
        company.put("phone", null);
        company.put("companyAdviserId", companyAdviserId);
        company.put("companyDirectorId", companyDirectorId);
        company.put("logo", logo);
        company.put("following", null);
        company.put("qccUpdatedDate", DateUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        company.put("status", 0);
        company.put("province", province);
        company.put("city", city);
        company.put("county", county);
        company.put("address", address);
        company.put("createdBy", loginUserId);
        company.put("updatedBy", loginUserId);
        company.put("createDate", new Date());
        company.put("updateDate", new Date());
        company.put("userLevelId", userLevelId);
        company.put("userTypeId", userTypeId);
        company.put("enrollDate", enrollDate);
        company.put("channelId", channelId);
        company.put("departmentId", departmentId);
        company.put("priorityId", priorityId);
        company.put("attendStatus", attendStatus);
        company.put("contactName", contactName);
        company.put("contactPhone", contactPhone);
        company.put("companyPhaseId", companyPhaseId);
        // 工商信息
        company.put("socialCredit", socialCredit);
        company.put("registrationCode", registrationCode);
        company.put("organizationCode", organizationCode);
        company.put("manageType", manageType);
        company.put("manageScope", manageScope);
        company.put("registeredMoney", registeredMoney);
        company.put("registrationAuthority", registrationAuthority);
        company.put("annualSalesVolume", annualSalesVolume);
        company.put("annualProfit", annualProfit);
        company.put("thisYearSalesVolume", thisYearSalesVolume);
        company.put("nextYearSalesVolume", nextYearSalesVolume);
        company.put("businessScope", businessScope);
        company.put("companyType", companyType);
        company.put("companyPropertyId", companyProperty);
        company.put("companySize", companySize);
        company.put("beforeName", beforeName);
        company.put("companyDesc", companyDesc);
        company.put("website", website);
        company.put("phone", null);
        company.put("email", email);
        company.put("wechat", weChat);
        company.put("content", content);
        company.put("companyPosition", companyPosition);
        company.put("econKind", econKind);
        company.put("establishTime", establishTime);
        company.put("approveDate", approveDate);
        company.put("termStart", termStart);
        company.put("operatingPeriod", operatingPeriod);
        company.put("companyOpportunity", null);
        company.put("createdBy", loginUserId);
        company.put("createDate", new Date());
        try {
            //插入企业信息
            companyServie.insertCompanyInfo(company);
            //插入公司详情
            companyServie.insertCompanyDetailInfo(company);


           String contactUserId= userInfoService.addUserInfo(contactName, null, null, insertCompanyId, null, "1", null, null, contactPhone, null, null, null, loginUserId, null, null, null, null);

            userDynamicService.addUserDynamic(loginUserId, insertCompanyId, companyName, "添加", "创建了企业\"" + companyName + "\"", 0, null, null, null);
            //更新企业信息完整度
            companyServie.updateInfoScore((String) company.get("id"));

            Map<String,Object> map=new HashMap<>();
            map.put("id",insertCompanyId);
            map.put("contactUserId",contactUserId);
            companyServie.updateCompanyInfoById(map);

            //将该企业维护到solr库中
//            solrService.updateCompanyIndex(insertCompanyId);
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        responseDbCenter.setResModel(insertCompanyId);
        return responseDbCenter;
    }


    @ApiOperation(value = "添加潜在客户")
    @RequestMapping(value = "/addPotentialCompanyInfo", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResponseDbCenter addPotentialCompanyInfo(HttpServletRequest req, HttpServletResponse rsp,
                                                    @ApiParam(value = "公司名称", required = false) @RequestParam(required = false) String companyName,
                                                    @ApiParam(value = "社会信用统一编号", required = false) @RequestParam(required = false) String socialCredit,
                                                    @ApiParam(value = "注册编号", required = false) @RequestParam(required = false) String registrationCode,
                                                    @ApiParam(value = "组织机构编号", required = false) @RequestParam(required = false) String organizationCode,
                                                    @ApiParam(value = "法人代表", required = false) @RequestParam(required = false) String representative,
                                                    @ApiParam(value = "注册资金", required = false) @RequestParam(required = false) String registeredMoney,
                                                    @ApiParam(value = "创建时间", required = false) @RequestParam(required = false) String establishTime,
                                                    @ApiParam(value = "顾问Id", required = false) @RequestParam(required = false) String companyAdviserId,
                                                    @ApiParam(value = "总监Id", required = false) @RequestParam(required = false) String companyDirectorId,
                                                    @ApiParam(value = "公司logo", required = false) @RequestParam(required = false) String logo,
                                                    @ApiParam(value = "开始营业时间", required = false) @RequestParam(required = false) String termStart,
                                                    @ApiParam(value = "到期时间", required = false) @RequestParam(required = false) String operatingPeriod,
                                                    @ApiParam(value = "注册机构", required = false) @RequestParam(required = false) String registrationAuthority,
                                                    @ApiParam(value = "年营业额（万）", required = false) @RequestParam(required = false) String annualSalesVolume,
                                                    @ApiParam(value = "年利润（年）", required = false) @RequestParam(required = false) String annualProfit,
                                                    @ApiParam(value = "本年营收预测", required = false) @RequestParam(required = false) String thisYearSalesVolume,
                                                    @ApiParam(value = "下一年营收预测", required = false) @RequestParam(required = false) String nextYearSalesVolume,
                                                    @ApiParam(value = "业务范围 如 全国，华南", required = false) @RequestParam(required = false) String businessScope,
                                                    @ApiParam(value = "审核日期", required = false) @RequestParam(required = false) String approveDate,
                                                    @ApiParam(value = "公司行业类型", required = false) @RequestParam(required = false) String companyType,
                                                    @ApiParam(value = "公司规模", required = false) @RequestParam(required = false) String companySize,
                                                    @ApiParam(value = "公司英文名称", required = false) @RequestParam(required = false) String englishName,
                                                    @ApiParam(value = "曾用名", required = false) @RequestParam(required = false) String beforeName,
                                                    @ApiParam(value = "公司网站", required = false) @RequestParam(required = false) String website,
                                                    @ApiParam(value = "公司邮箱", required = false) @RequestParam(required = false) String email,
                                                    @ApiParam(value = "微信", required = false) @RequestParam(required = false) String weChat,
                                                    @ApiParam(value = "企业描述", required = false) @RequestParam(required = false) String content,
                                                    @ApiParam(value = "企业定位", required = false) @RequestParam(required = false) String companyPosition,
                                                    @ApiParam(value = "公司类型 如:有限责任公司", required = false) @RequestParam(required = false) String econKind,
                                                    @ApiParam(value = "省", required = false) @RequestParam(required = false) String province,
                                                    @ApiParam(value = "市", required = false) @RequestParam(required = false) String city,
                                                    @ApiParam(value = "区", required = false) @RequestParam(required = false) String county,
                                                    @ApiParam(value = "公司地址", required = false) @RequestParam(required = false) String address,
                                                    @ApiParam(value = "行业Id", required = false) @RequestParam(required = false) String industryId,
                                                    @ApiParam(value = "公司一句话描述", required = false) @RequestParam(required = false) String companyDesc,
                                                    @ApiParam(value = "获投状态 ", required = false) @RequestParam(required = false) String investStatus,
                                                    @ApiParam(value = "融资状态", required = false) @RequestParam(required = false) String financeStatus,
                                                    @ApiParam(value = "经营状态 如（存续）", required = false) @RequestParam(required = false) String manageType,
                                                    @ApiParam(value = "经营范围", required = false) @RequestParam(required = false) String manageScope,
                                                    @ApiParam(value = "公司属性Id 如 （数据字典中的 国有 私有）", required = false) @RequestParam(required = false) String companyProperty,
                                                    @ApiParam(value = "客户类型Id", required = false) @RequestParam(required = false) String userTypeId,
                                                    @ApiParam(value = "客户等级Id", required = false) @RequestParam(required = false) String userLevelId,
                                                    @ApiParam(value = "渠道来源Id", required = false) @RequestParam(required = false) String channelId,
                                                    @ApiParam(value = "报名日期", required = false) @RequestParam(required = false) String enrollDate,
                                                    @ApiParam(value = "优先级Id", required = false) @RequestParam(required = false) String priorityId,
                                                    @ApiParam(value = "参会状态  未参会  已参会", required = false) @RequestParam(required = false) String attendStatus,
                                                    @ApiParam(value = "联系人", required = false) @RequestParam(required = false) String contactName,
                                                    @ApiParam(value = "联系电话", required = false) @RequestParam(required = false) String contactPhone,
                                                    @ApiParam(value = "所处阶段", required = false) @RequestParam(required = false) String companyPhaseId,
                                                    @ApiParam(value = "部门Id", required = false) @RequestParam(required = false) String departmentId) throws Exception {

        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");

        Map<String, Object> company = new HashMap();
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        /**
         * 如果部门Id为空，则添加时默认以添加人的部门Id作为该条数据的部门
         */
        if (StringUtils.isBlank(departmentId)) {
            Map<String, Object> map = userService.selectUserById(loginUserId);
            departmentId = (String) map.get("departmentId");
        }


        Map<String, Object> companyMap = departmentService.getCompanyIdByDepartmentId(departmentId);
        //设置该记录属性哪一个部门客户
        company.put("pcCompanyId", companyMap.get("id"));


        // 通过企业名称查询该企业是否存在，如果存在就返回提示信息
        List<Map<String, Object>> alreadyExists = companyServie.selectCompanyByNameUnDeleted(companyName, (String) company.get("pcCompanyId"));
        if (alreadyExists.size() > 0) {
            //如果名称与未删除的数据冲突，则不允许再添加了
            return ResponseConstants.FUNC_COMPANY_EXIST;
        }
        String insertCompanyId = UUID.randomUUID().toString();
        company.put("id", insertCompanyId);
        company.put("potentialStatus", 0);
        company.put("companyName", companyName);
        company.put("englishName", englishName);
        company.put("label", null);
        company.put("representative", representative);
        company.put("industryId", industryId);
        company.put("investStatus", investStatus);
        company.put("financeStatus", financeStatus);
        company.put("phone", null);
        company.put("companyAdviserId", companyAdviserId);
        company.put("companyDirectorId", companyDirectorId);
        company.put("logo", logo);
        company.put("following", null);
        company.put("qccUpdatedDate", DateUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        company.put("status", 0);
        company.put("province", province);
        company.put("city", city);
        company.put("county", county);
        company.put("address", address);
        company.put("createdBy", loginUserId);
        company.put("updatedBy", loginUserId);
        company.put("createDate", new Date());
        company.put("updateDate", new Date());
        company.put("userLevelId", userLevelId);
        company.put("userTypeId", userTypeId);
        company.put("enrollDate", enrollDate);
        company.put("channelId", channelId);
        company.put("departmentId", departmentId);
        company.put("priorityId", priorityId);
        company.put("attendStatus", attendStatus);
        company.put("contactName", contactName);
        company.put("contactPhone", contactPhone);
        company.put("companyPhaseId", companyPhaseId);
        // 工商信息
        company.put("socialCredit", socialCredit);
        company.put("registrationCode", registrationCode);
        company.put("organizationCode", organizationCode);
        company.put("manageType", manageType);
        company.put("manageScope", manageScope);
        company.put("registeredMoney", registeredMoney);
        company.put("registrationAuthority", registrationAuthority);
        company.put("annualSalesVolume", annualSalesVolume);
        company.put("annualProfit", annualProfit);
        company.put("thisYearSalesVolume", thisYearSalesVolume);
        company.put("nextYearSalesVolume", nextYearSalesVolume);
        company.put("businessScope", businessScope);
        company.put("companyType", companyType);
        company.put("companyPropertyId", companyProperty);
        company.put("companySize", companySize);
        company.put("beforeName", beforeName);
        company.put("companyDesc", companyDesc);
        company.put("website", website);
        company.put("phone", null);
        company.put("email", email);
        company.put("wechat", weChat);
        company.put("content", content);
        company.put("companyPosition", companyPosition);
        company.put("econKind", econKind);
        company.put("establishTime", establishTime);
        company.put("approveDate", approveDate);
        company.put("termStart", termStart);
        company.put("operatingPeriod", operatingPeriod);
        company.put("companyOpportunity", null);
        company.put("createdBy", loginUserId);
        company.put("createDate", new Date());
        try {
            //插入企业信息
            companyServie.insertCompanyInfo(company);
            //插入公司详情
            companyServie.insertCompanyDetailInfo(company);


            userInfoService.addUserInfo(contactName, null, null, insertCompanyId, null, "1", null, null, contactPhone, null, null, null, loginUserId, null, null, null, null);

            userDynamicService.addUserDynamic(loginUserId, insertCompanyId, companyName, "添加", "创建了企业\"" + companyName + "\"", 0, null, null, null);
            //更新企业信息完整度
            companyServie.updateInfoScore((String) company.get("id"));
            //将该企业维护到solr库中
//            solrService.updateCompanyIndex(insertCompanyId);
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        responseDbCenter.setResModel(insertCompanyId);
        return responseDbCenter;
    }


    @ApiOperation(value = "潜在客户转为正式客户")
    @RequestMapping(value = "/convertPotentialToFormal", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ResponseDbCenter convertPotentialToFormal(HttpServletRequest req, HttpServletResponse rsp,
                                                     @ApiParam(value = "公司Id", required = false) @RequestParam(required = false) String id
    ) throws Exception {

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        try {
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("potentialStatus", 1);
            companyServie.updateCompanyInfoById(map);
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        return responseDbCenter;
    }


    @RequestMapping(value = "/transferAdviser", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @ApiOperation(value = "转顾问")
    public ResponseDbCenter transferAdviser(HttpServletRequest req, HttpServletResponse rsp,
                                            @ApiParam(value = "公司Id", required = true) @RequestParam(required = true) String companyIds,
                                            @ApiParam(value = "用户Id", required = true) @RequestParam(required = true) String adviserId

    )
            throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");
        try {
            String companyIdList[] = companyIds.split(",");
            Map<String, Object> companyMap = new HashMap<>();
            companyMap.put("companyAdviserId", adviserId);
            companyMap.put("updatedBy", loginUserId);
            for (String companyId : companyIdList) {
                companyMap.put("id", companyId);
                companyServie.updateCompanyInfoById(companyMap);
                //更新企业信息完整度
                companyServie.updateInfoScore((String) companyMap.get("id"));
                //将该企业维护到solr库中
//                solrService.updateCompanyIndex(companyId);
            }
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }


    /**
     * @param req
     * @param rsp
     * @return
     * @author: xiaoz
     * @2017年6月21日
     * @功能描述:更新企业工商信息
     */
    @RequestMapping(value = "/updateCompanyIndustryCommerce", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @ApiOperation(value = "更新公司工商信息", response = ResponseDbCenter.class, notes = "")
    public ResponseDbCenter updateCompanyIndustryCommerce(HttpServletRequest req, HttpServletResponse rsp,
                                                          @ApiParam(value = "公司Id", required = true) @RequestParam(required = true) String companyId,
                                                          @ApiParam(value = "社会信用编号", required = false) @RequestParam(required = false) String socialCredit,
                                                          @ApiParam(value = "登记册号", required = false) @RequestParam(required = false) String registrationCode,
                                                          @ApiParam(value = "组织机构号", required = false) @RequestParam(required = false) String organizationCode,
                                                          @ApiParam(value = "经营类型 如存续", required = false) @RequestParam(required = false) String manageType,
                                                          @ApiParam(value = "法人代表", required = false) @RequestParam(required = false) String representative,
                                                          @ApiParam(value = "注册资本", required = false) @RequestParam(required = false) String registeredMoney,
                                                          @ApiParam(value = "公司类型如有限责任公司", required = false) @RequestParam(required = false) String econKind,
                                                          @ApiParam(value = "创建时间", required = false) @RequestParam(required = false) String establishTime,
                                                          @ApiParam(value = "经营范围", required = false) @RequestParam(required = false) String manageScope,
                                                          @ApiParam(value = "公司描述", required = false) @RequestParam(required = false) String content,
                                                          @ApiParam(value = "营业开始时间", required = false) @RequestParam(required = false) String termStart,
                                                          @ApiParam(value = "营业结束时间", required = false) @RequestParam(required = false) String operatingPeriod,
                                                          @ApiParam(value = "注册机构", required = false) @RequestParam(required = false) String registrationAuthority,
                                                          @ApiParam(value = "核准日期", required = false) @RequestParam(required = false) String approveDate,
                                                          @ApiParam(value = "公司规模", required = false) @RequestParam(required = false) String companySize,
                                                          @ApiParam(value = "曾用名", required = false) @RequestParam(required = false) String beforeName,
                                                          @ApiParam(value = "省", required = false) @RequestParam(required = false) String province,
                                                          @ApiParam(value = "市", required = false) @RequestParam(required = false) String city,
                                                          @ApiParam(value = "县", required = false) @RequestParam(required = false) String county,
                                                          @ApiParam(value = "地址", required = false) @RequestParam(required = false) String address
    )
            throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");


        if (StringUtils.isBlank(companyId)) {

            return ResponseConstants.MISSING_PARAMTER;
        }


        /**
         * 判断企业是否有权限被修改
         */
        boolean hasPermission = userDataPermissionService.hasPermission(companyId, loginUserId);
        if (!hasPermission) {
            return ResponseConstants.DATA_NOT_PERMITED;
        }

        try {

            Map<String, Object> company = new HashMap();
            company.put("id", companyId);
            company.put("socialCredit", socialCredit);
            company.put("registrationCode", registrationCode);
            company.put("organizationCode", organizationCode);
            company.put("manageType", manageType);
            company.put("representative", representative);
            company.put("registeredMoney", registeredMoney);
            company.put("econKind", econKind);
            company.put("establishTime", establishTime);
            company.put("manageScope", manageScope);
            company.put("content", content);
            company.put("termStart", termStart);
            company.put("operatingPeriod", operatingPeriod);
            company.put("registrationAuthority", registrationAuthority);
            company.put("approveDate", approveDate);
            company.put("companySize", companySize);
            company.put("beforeName", beforeName);
            company.put("province", province);
            company.put("city", city);
            company.put("county", county);
            company.put("address", address);
            company.put("qccUpdatedDate", DateUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));


            try {
                if (!StringUtils.isBlank(address)) {
                    LngLatData lngLatData = BaiduAddressUtil.parseAddressToLngLat(address);
                    if (lngLatData != null) {
                        AddressData addressData = BaiduAddressUtil.parseLngLatToAddress(lngLatData.getLongitude(), lngLatData.getLatitude());
                        if (addressData != null) {
                            company.put("country", addressData.getCountry());
                            company.put("province", addressData.getProvince());
                            company.put("city", addressData.getCity());
                            company.put("county", addressData.getCounty());
                        }
                    }
                }
            } catch (Exception e) {
                log.error("异常栈:", e);
            }


            companyServie.updateCompanyInfoById(company);
            companyServie.updateCompanyDetailInfoById(company);


            companyServie.updateInfoScore(companyId);
//            solrService.updateCompanyIndex(companyId);

            userDynamicService.addUserDynamic(loginUserId, companyId, "", "更新", "更新了企业工商信息", 0, null, null, null);

        } catch (Exception e) {

            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }


    @ResponseBody
    @RequestMapping(value = "/uploadCompanyLogo", method = {RequestMethod.POST})
    @ApiOperation(value = "上传企业图片")
    public ResponseDbCenter uploadCompanyPicture(@RequestParam(value = "files", required = false) MultipartFile[] files,
                                                 HttpServletRequest req) throws Exception {

        if (files == null) {

            return ResponseConstants.MISSING_PARAMTER;
        }

        String uploadPath = PropertiesUtil.FILE_UPLOAD_PATH + "companysLogo";
        String httpPath = PropertiesUtil.FILE_HTTP_PATH + "companysLogo";

        List<String> fileList = FileUtil.fileUpload(files, uploadPath, httpPath);

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(fileList);

        return responseDbCenter;
    }


    @ApiOperation(value = "搜索客户")
    @ResponseBody
    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter search(HttpServletRequest req,
                                   @ApiParam(value = "检索值", required = false) @RequestParam(required = false) String queryContent,
                                   @ApiParam(value = "行业条件Id ", required = false) @RequestParam(required = false) String industryId,
                                   @ApiParam(value = "客户级别", required = false) @RequestParam(required = false) String userLevelId,
                                   @ApiParam(value = "公司名称", required = false) @RequestParam(required = false) String companyName,
                                   @ApiParam(value = "省", required = false) @RequestParam(required = false) String province,
                                   @ApiParam(value = "市 ", required = false) @RequestParam(required = false) String city,
                                   @ApiParam(value = "区", required = false) @RequestParam(required = false) String county,
                                   @ApiParam(value = "手机", required = false) @RequestParam(required = false) String mobile,
                                   @ApiParam(value = "来源", required = false) @RequestParam(required = false) String channelId,
                                   @ApiParam(value = "顾问Id", required = false) @RequestParam(required = false) String companyAdviserId,
                                   @ApiParam(value = "总监Id", required = false) @RequestParam(required = false) String companyDirectorId,
                                   @ApiParam(value = "部门Id 如果引入数据权限，此部门Id需要传值", required = false) @RequestParam(required = false) String departmentId,
                                   @ApiParam(value = "标志位   1  我负责的客户 2 我参与的客户  3 总监是我的客户  4 公海客户  5 全部客户 ", required = false) @RequestParam(required = false) Integer publicFlag,
                                   @ApiParam(value = "当前页码", required = true) @RequestParam(required = true) Integer currentPage,
                                   @ApiParam(value = "每页多少条数据", required = true) @RequestParam(required = true) Integer pageSize
    ) {
        //登录员工Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        List<Map<String, Object>> list = new ArrayList<>();
        Integer count = 0;
        List<String> departmentIdList = new ArrayList<>();
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        try {
            departmentIdList = userDataPermissionService.getDataPermissionDepartmentIdList(departmentId, loginUserId);
            list = companyServie.searchCompany(departmentIdList, queryContent, companyName, mobile, userLevelId, industryId, province, city, county, companyAdviserId, channelId, companyDirectorId, publicFlag, loginUserId, 1, currentPage, pageSize);
            count = companyServie.searchCompanyCount(departmentIdList, queryContent, companyName, mobile, userLevelId, industryId, province, city, county, companyAdviserId, channelId, companyDirectorId, publicFlag, loginUserId, 1);
            responseDbCenter.setResModel(list);
            responseDbCenter.setTotalRows(count + "");
        } catch (Exception e) {
            log.error("异常栈:", e);
            ResponseDbCenter responseCenter = ResponseConstants.FUNC_MODULE_SERVERERROR;
            responseCenter.setResModel(new ArrayList());
            return responseCenter;
        }
        return responseDbCenter;
    }


    @ApiOperation(value = "下载客户")
    @ResponseBody
    @RequestMapping(value = "/download", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter download(HttpServletRequest req, HttpServletResponse resp,
                                     @ApiParam(value = "检索值", required = false) @RequestParam(required = false) String searchValue,
                                     @ApiParam(value = "行业条件Id ", required = false) @RequestParam(required = false) String industryId,
                                     @ApiParam(value = "客户级别", required = false) @RequestParam(required = false) String userLevelId,
                                     @ApiParam(value = "公司名称", required = false) @RequestParam(required = false) String companyName,
                                     @ApiParam(value = "省", required = false) @RequestParam(required = false) String province,
                                     @ApiParam(value = "市 ", required = false) @RequestParam(required = false) String city,
                                     @ApiParam(value = "区", required = false) @RequestParam(required = false) String county,
                                     @ApiParam(value = "手机", required = false) @RequestParam(required = false) String mobile,
                                     @ApiParam(value = "来源", required = false) @RequestParam(required = false) String channelId,
                                     @ApiParam(value = "顾问Id", required = false) @RequestParam(required = false) String companyAdviserId,
                                     @ApiParam(value = "总监Id", required = false) @RequestParam(required = false) String companyDirectorId,
                                     @ApiParam(value = "部门Id 如果引入数据权限，此部门Id需要传值", required = false) @RequestParam(required = false) String departmentId,
                                     @ApiParam(value = "标志位   1  我负责的客户 2 我参与的客户  3 总监是我的客户  4 公海客户  5 全部客户 ", required = false) @RequestParam(required = false) Integer publicFlag

    ) {
        //登录员工Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        List<Map<String, Object>> list = new ArrayList<>();
        Integer count = 0;
        List<String> departmentIdList = new ArrayList<>();
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        try {
            departmentIdList = userDataPermissionService.getDataPermissionDepartmentIdList(departmentId, loginUserId);
            list = companyServie.searchCompany(departmentIdList, searchValue, companyName, mobile, userLevelId, industryId, province, city, county, companyAdviserId, channelId, companyDirectorId, publicFlag, loginUserId, 1, 1, 1000000);

            List<Map<String, Object>> fieldShowList = fieldShowService.selectFieldShow("company");


            Integer columnSize = fieldShowList.size();
            String[] columnTitles = new String[columnSize];
            String[] columnKeys = new String[columnSize];

            for (int i = 0; i < fieldShowList.size(); i++) {
                columnTitles[i] = (String) fieldShowList.get(i).get("fieldName");
                columnKeys[i] = (String) fieldShowList.get(i).get("field");
            }

            Workbook workbook = ExcelUtilSpecial.createWorkbook("企业信息", columnTitles, columnKeys, list);
            resp.setHeader("Content-Disposition", "attachment;filename= " + java.net.URLEncoder.encode("企业信息", "UTF-8") + ".xls");
            //设置导出文件的格式
            resp.setContentType("application/ms-excel");
            workbook.write(resp.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            log.error("异常栈:", e);
            ResponseDbCenter responseCenter = ResponseConstants.FUNC_MODULE_SERVERERROR;
            responseCenter.setResModel(new ArrayList());
            return responseCenter;
        }
        return responseDbCenter;
    }


    @ApiOperation(value = "搜索潜在客户")
    @ResponseBody
    @RequestMapping(value = "/searchPotentialCompany", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter searchPotentialCompany(HttpServletRequest req,
                                                   @ApiParam(value = "检索值", required = false) @RequestParam(required = false) String searchValue,
                                                   @ApiParam(value = "行业条件Id ", required = false) @RequestParam(required = false) String industryId,
                                                   @ApiParam(value = "客户级别", required = false) @RequestParam(required = false) String userLevelId,
                                                   @ApiParam(value = "公司名称", required = false) @RequestParam(required = false) String companyName,
                                                   @ApiParam(value = "省", required = false) @RequestParam(required = false) String province,
                                                   @ApiParam(value = "市 ", required = false) @RequestParam(required = false) String city,
                                                   @ApiParam(value = "区", required = false) @RequestParam(required = false) String county,
                                                   @ApiParam(value = "手机", required = false) @RequestParam(required = false) String mobile,
                                                   @ApiParam(value = "来源", required = false) @RequestParam(required = false) String channelId,
                                                   @ApiParam(value = "顾问Id", required = false) @RequestParam(required = false) String companyAdviserId,
                                                   @ApiParam(value = "总监Id", required = false) @RequestParam(required = false) String companyDirectorId,
                                                   @ApiParam(value = "部门Id 如果引入数据权限，此部门Id需要传值", required = false) @RequestParam(required = false) String departmentId,
                                                   @ApiParam(value = "标志位   1  我负责的客户 2 我参与的客户  3 总监是我的客户  4 公海客户  5 全部客户 ", required = false) @RequestParam(required = false) Integer publicFlag,
                                                   @ApiParam(value = "当前页码", required = true) @RequestParam(required = true) Integer currentPage,
                                                   @ApiParam(value = "每页多少条数据", required = true) @RequestParam(required = true) Integer pageSize
    ) {
        //登录员工Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        List<Map<String, Object>> list = new ArrayList<>();
        Integer count = 0;
        List<String> departmentIdList = new ArrayList<>();
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        try {
            departmentIdList = userDataPermissionService.getDataPermissionDepartmentIdList(departmentId, loginUserId);
            list = companyServie.searchCompany(departmentIdList, searchValue, companyName, mobile, userLevelId, industryId, province, city, county, companyAdviserId, channelId, companyDirectorId, publicFlag, loginUserId, 0, currentPage, pageSize);
            count = companyServie.searchCompanyCount(departmentIdList, searchValue, companyName, mobile, userLevelId, industryId, province, city, county, companyAdviserId, channelId, companyDirectorId, publicFlag, loginUserId, 0);
            responseDbCenter.setResModel(list);
            responseDbCenter.setTotalRows(count + "");
        } catch (Exception e) {
            log.error("异常栈:", e);
            ResponseDbCenter responseCenter = ResponseConstants.FUNC_MODULE_SERVERERROR;
            responseCenter.setResModel(new ArrayList());
            return responseCenter;
        }
        return responseDbCenter;
    }


    @ApiOperation(value = "下载公司信息模板")
    @ResponseBody
    @RequestMapping(value = "/downloadTemplate", method = {RequestMethod.GET})
    public ResponseDbCenter downloadTemplate(HttpServletRequest req, HttpServletResponse res) throws Exception {

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);


        /**
         * 查询数据字典下拉框的值
         */

        List<Map<String, Object>> userLevelList = dictionaryService.selectDictionarysByTtypes("ttype = 'userLevel'", 10000, 0, (String) loginUserMap.get("pcCompanyId"));
        List<Map<String, Object>> sourceTypeList = dictionaryService.selectDictionarysByTtypes("ttype ='sourceType'", 10000, 0, (String) loginUserMap.get("pcCompanyId"));
        List<Map<String, Object>> industryList = dictionaryService.selectDictionarysByTtypes("ttype='industry'", 10000, 0, (String) loginUserMap.get("pcCompanyId"));
        List<Map<String, Object>> userList = userService.selectUsers((String) loginUserMap.get("pcCompanyId"), null);

        List<Map<String, Object>> industryNewList = new ArrayList<>();


        for (int i = 0; i < industryList.size(); i++) {
            if (!"0".equals(industryList.get(i).get("parentId"))) {
                industryNewList.add(industryList.get(i));
            }
        }
        industryList = industryNewList;

        String[] userLevelArray = new String[userLevelList.size()];
        for (int i = 0; i < userLevelList.size(); i++) {
            userLevelArray[i] = (String) userLevelList.get(i).get("value");
        }
        String[] sourceTypeArray = new String[sourceTypeList.size()];
        for (int i = 0; i < sourceTypeList.size(); i++) {
            sourceTypeArray[i] = (String) sourceTypeList.get(i).get("value");
        }
        String[] industryArray = new String[industryList.size()];
        for (int i = 0; i < industryList.size(); i++) {
            industryArray[i] = (String) industryList.get(i).get("value");
        }
        String[] userArray = new String[userList.size()];
        for (int i = 0; i < userList.size(); i++) {
            userArray[i] = (String) userList.get(i).get("uname");
        }


        List<Map<String, Object>> provinceMapList = new ArrayList<>();
        for (int i = 0; i < CityJson.provinces.size(); i++) {
            Map<String, Object> province = JSON.parseObject(CityJson.provinces.get(i), new com.alibaba.fastjson.TypeReference<Map<String, Object>>() {
            });

            provinceMapList.add(province);

        }


        // 创建一个excel
        @SuppressWarnings("resource")
        Workbook book = new XSSFWorkbook();


        Font titleFont = book.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) (12));
        CellStyle titleCellStyle = book.createCellStyle();
        titleCellStyle.setFont(titleFont);
        titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleCellStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        titleCellStyle.setBorderBottom(BorderStyle.THIN);
        titleCellStyle.setBorderLeft(BorderStyle.THIN);
        titleCellStyle.setBorderRight(BorderStyle.THIN);
        titleCellStyle.setBorderTop(BorderStyle.THIN);

        /**
         * 创建建字符串类型样式
         */
        CellStyle css = book.createCellStyle();
        DataFormat format = book.createDataFormat();
        css.setDataFormat(format.getFormat("@"));

        // 创建需要用户填写的sheet
        XSSFSheet sheetPro = (XSSFSheet) book.createSheet("main");

        /**
         * 设置列的宽度
         */
        sheetPro.setColumnWidth(0, 7000);
        sheetPro.setColumnWidth(1, 3000);
        sheetPro.setColumnWidth(2, 3000);
        sheetPro.setColumnWidth(3, 3000);
        sheetPro.setColumnWidth(4, 7000);
        sheetPro.setColumnWidth(5, 7000);
        sheetPro.setColumnWidth(6, 3000);
        sheetPro.setColumnWidth(7, 3000);
        sheetPro.setColumnWidth(8, 3000);
        sheetPro.setColumnWidth(9, 3000);
        sheetPro.setColumnWidth(10, 3000);
        sheetPro.setColumnWidth(11, 5000);
        sheetPro.setColumnWidth(12, 7000);


        /**
         * 设置列头
         */
        Row row0 = sheetPro.createRow(0);
        Cell cell00 = row0.createCell(0);
        cell00.setCellStyle(titleCellStyle);
        cell00.setCellValue("客户名称");


        Cell cell01 = row0.createCell(1);
        cell01.setCellStyle(titleCellStyle);
        cell01.setCellValue("所属省");

        Cell cell02 = row0.createCell(2);
        cell02.setCellStyle(titleCellStyle);
        cell02.setCellValue("所属市");

        Cell cell03 = row0.createCell(3);
        cell03.setCellStyle(titleCellStyle);
        cell03.setCellValue("所属区");


        Cell cell04 = row0.createCell(4);
        cell04.setCellStyle(titleCellStyle);
        cell04.setCellValue("地址");


        Cell cell05 = row0.createCell(5);
        cell05.setCellStyle(titleCellStyle);
        cell05.setCellValue("网址");

        Cell cell06 = row0.createCell(6);
        cell06.setCellStyle(titleCellStyle);
        cell06.setCellValue("联系人");

        Cell cell07 = row0.createCell(7);
        cell07.setCellStyle(titleCellStyle);
        cell07.setCellValue("联系电话");

        Cell cell08 = row0.createCell(8);
        cell08.setCellStyle(titleCellStyle);
        cell08.setCellValue("负责人");

        Cell cell09 = row0.createCell(9);
        cell09.setCellStyle(titleCellStyle);
        cell09.setCellValue("总监");

        Cell cell10 = row0.createCell(10);
        cell10.setCellStyle(titleCellStyle);
        cell10.setCellValue("客户级别");

        Cell cell11 = row0.createCell(11);
        cell11.setCellStyle(titleCellStyle);
        cell11.setCellValue("客户来源");

        Cell cell12 = row0.createCell(12);
        cell12.setCellStyle(titleCellStyle);
        cell12.setCellValue("行业");

        /**
         * 设置0-12列为字符串类型
         */
        for (int i = 0; i < 13; i++) {
            sheetPro.setDefaultColumnStyle(i, css);
        }

        //得到第一级省名称，放在列表里

        String[] provinceArr = new String[provinceMapList.size()];

        Map<String, String[]> areaMap = new HashMap<String, String[]>();
        List<String> areaFatherNameList = new ArrayList<>();

        for (int i = 0; i < provinceMapList.size(); i++) {
            Map<String, Object> provinceMap = provinceMapList.get(i);
            provinceArr[i] = (String) provinceMap.get("name");
            List<Map<String, Object>> cityList = (List<Map<String, Object>>) provinceMap.get("childerAreas");
            String[] cityArr = new String[cityList.size()];
            if (!areaFatherNameList.contains(provinceArr[i])) {
                areaFatherNameList.add(provinceArr[i]);
            }
            for (int j = 0; j < cityList.size(); j++) {
                Map<String, Object> cityMap = cityList.get(j);
                cityArr[j] = (String) cityMap.get("name");
                List<Map<String, Object>> countyList = (List<Map<String, Object>>) cityMap.get("childerAreas");
                String[] countyArr = new String[countyList.size()];
                if (!areaFatherNameList.contains(cityArr[j])) {
                    areaFatherNameList.add(cityArr[j]);
                }
                for (int k = 0; k < countyList.size(); k++) {
                    Map<String, Object> countyMap = countyList.get(k);
                    countyArr[k] = (String) countyMap.get("name");
                }
                areaMap.put(cityArr[j], countyArr);

            }
            areaMap.put(provinceArr[i], cityArr);


        }

        String[] areaFatherNameArr = new String[areaFatherNameList.size()];
        for (int i = 0; i < areaFatherNameList.size(); i++) {
            areaFatherNameArr[i] = areaFatherNameList.get(i);
        }


        //创建一个专门用来存放地区信息的隐藏sheet页
        //因此也不能在现实页之前创建，否则无法隐藏。
        Sheet hideSheet = book.createSheet("area");


        //这一行作用是将此sheet隐藏，功能未完成时注释此行,可以查看隐藏sheet中信息是否正确
        book.setSheetHidden(book.getSheetIndex(hideSheet), true);

        int rowId = 0;
        // 设置第一行，存省的信息
        Row provinceRow = hideSheet.createRow(rowId++);
        provinceRow.createCell(0).setCellValue("省列表");
        for (int i = 0; i < provinceArr.length; i++) {
            Cell provinceCell = provinceRow.createCell(i + 1);
            provinceCell.setCellValue(provinceArr[i]);
        }
        // 将具体的数据写入到每一行中，行开头为父级区域，后面是子区域。
        for (int i = 0; i < areaFatherNameArr.length; i++) {
            String key = areaFatherNameArr[i];
            String[] son = areaMap.get(key);
            Row row = hideSheet.createRow(rowId++);
            row.createCell(0).setCellValue(key);
            for (int j = 0; j < son.length; j++) {
                Cell cell = row.createCell(j + 1);
                cell.setCellValue(son[j]);
            }

            // 添加名称管理器
            String range = getRange(1, rowId, son.length);
            Name name = book.createName();
            //key不可重复
            name.setNameName(key);
            String formula = "area!" + range;
            name.setRefersToFormula(formula);
        }

        Integer maxRow = 1001;

        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheetPro);
        // 省规则
        DataValidationConstraint provConstraint = dvHelper.createExplicitListConstraint(provinceArr);
        // 四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList provRangeAddressList = new CellRangeAddressList(1, maxRow, 1, 1);
        DataValidation provinceDataValidation = dvHelper.createValidation(provConstraint, provRangeAddressList);
        //验证
        provinceDataValidation.createErrorBox("error", "请选择正确的省份");
        provinceDataValidation.setShowErrorBox(true);
        provinceDataValidation.setSuppressDropDownArrow(true);
        sheetPro.addValidationData(provinceDataValidation);


        //对前20行设置有效性
        for (int i = 2; i < maxRow; i++) {
            setDataValidation("B", sheetPro, i, 3);
            setDataValidation("C", sheetPro, i, 4);
        }


        /**
         * 设置用户类型引用源
         */
        Sheet hideSheet2 = book.createSheet("userLevel");
        book.setSheetHidden(book.getSheetIndex(hideSheet2), true);

        for (int i = 0; i < userLevelArray.length; i++) {
            Row row = hideSheet2.createRow(i);
            row.createCell(0).setCellValue(userLevelArray[i]);
        }

        CellRangeAddressList userLevelRangeList = new CellRangeAddressList(1, maxRow, 10, 10);
        DataValidationConstraint userLevelConstraint = dvHelper.createFormulaListConstraint("userLevel!$" + "A" + "$1:$" + "A" + "$" + (userLevelArray.length));
        DataValidation userLevelDataValidation = dvHelper.createValidation(userLevelConstraint, userLevelRangeList);
        userLevelDataValidation.createErrorBox("error", "请选择正确的用户级别");
        userLevelDataValidation.setShowErrorBox(true);
        userLevelDataValidation.setSuppressDropDownArrow(true);
        sheetPro.addValidationData(userLevelDataValidation);


        /**
         * 设置客户来源引用源
         */
        Sheet hideSheet3 = book.createSheet("sourceType");
        book.setSheetHidden(book.getSheetIndex(hideSheet3), true);

        for (int i = 0; i < sourceTypeArray.length; i++) {
            Row row = hideSheet3.createRow(i);
            row.createCell(0).setCellValue(sourceTypeArray[i]);
        }

        CellRangeAddressList sourceTypeRangeList = new CellRangeAddressList(1, maxRow, 11, 11);
        DataValidationConstraint sourceTypeConstraint = dvHelper.createFormulaListConstraint("sourceType!$" + "A" + "$1:$" + "A" + "$" + (sourceTypeArray.length));
        DataValidation sourceTypeDataValidation = dvHelper.createValidation(sourceTypeConstraint, sourceTypeRangeList);
        sourceTypeDataValidation.createErrorBox("error", "请选择正确的客户来源");
        sourceTypeDataValidation.setShowErrorBox(true);
        sourceTypeDataValidation.setSuppressDropDownArrow(true);
        sheetPro.addValidationData(sourceTypeDataValidation);


        /**
         * 设置行业引用源
         */
        Sheet hideSheet4 = book.createSheet("industry");
        book.setSheetHidden(book.getSheetIndex(hideSheet4), true);

        for (int i = 0; i < industryArray.length; i++) {
            Row row = hideSheet4.createRow(i);
            hideSheet4.setColumnWidth(i, 4000); //设置每列的列宽
            row.createCell(0).setCellValue(industryArray[i]);
        }

        CellRangeAddressList industryRangeList = new CellRangeAddressList(1, maxRow, 12, 12);

        DataValidationConstraint industryConstraint = dvHelper.createFormulaListConstraint("industry!$" + "A" + "$1:$" + "A" + "$" + (industryArray.length));
        DataValidation industryDataValidation = dvHelper.createValidation(industryConstraint, industryRangeList);
        industryDataValidation.createErrorBox("error", "请选择正确的行业");
        industryDataValidation.setShowErrorBox(true);
        industryDataValidation.setSuppressDropDownArrow(true);
        sheetPro.addValidationData(industryDataValidation);


        /**
         * 设置负责人引用源
         */
        Sheet hideSheet5 = book.createSheet("user");
        book.setSheetHidden(book.getSheetIndex(hideSheet5), true);

        for (int i = 0; i < userArray.length; i++) {
            Row row = hideSheet5.createRow(i);
            row.createCell(0).setCellValue(userArray[i]);
        }

        CellRangeAddressList userRangeList = new CellRangeAddressList(1, maxRow, 8, 8);
        DataValidationConstraint userConstraint = dvHelper.createFormulaListConstraint("user!$" + "A" + "$1:$" + "A" + "$" + (userArray.length));
        DataValidation userDataValidation = dvHelper.createValidation(userConstraint, userRangeList);
        userDataValidation.createErrorBox("error", "请选择用户");
        userDataValidation.setShowErrorBox(true);
        userDataValidation.setSuppressDropDownArrow(true);
        sheetPro.addValidationData(userDataValidation);


        /**
         * 设置总监引用源
         */
        Sheet hideSheet6 = book.createSheet("user2");
        book.setSheetHidden(book.getSheetIndex(hideSheet6), true);

        for (int i = 0; i < userArray.length; i++) {
            Row row = hideSheet6.createRow(i);
            row.createCell(0).setCellValue(userArray[i]);
        }

        CellRangeAddressList userRangeList2 = new CellRangeAddressList(1, maxRow, 9, 9);
        DataValidationConstraint userConstraint2 = dvHelper.createFormulaListConstraint("user2!$" + "A" + "$1:$" + "A" + "$" + (userArray.length));


        DataValidation userDataValidation2 = dvHelper.createValidation(userConstraint2, userRangeList2);
        userDataValidation2.createErrorBox("error", "请选择用户");
        userDataValidation2.setShowErrorBox(true);
        userDataValidation2.setSuppressDropDownArrow(true);
        sheetPro.addValidationData(userDataValidation2);


        try {
            res.setHeader("Content-Disposition", "attachment;filename= " + java.net.URLEncoder.encode("企业导入模板", "UTF-8") + ".xlsx");
            //设置导出文件的格式
            res.setContentType("application/ms-excel");
            book.write(res.getOutputStream());

            book.close();
            res.getOutputStream().flush();
            res.getOutputStream().close();

        } catch (Exception e) {
            log.error("异常栈:", e);
        }

        return responseDbCenter;

    }


    @ApiOperation(value = "批量上传客户信息")
    @ResponseBody
    @RequestMapping(value = "/uploadCompany", method = {RequestMethod.POST})
    public ResponseDbCenter uploadCompany(HttpServletRequest req, HttpServletResponse res, @RequestParam(value = "file", required = true) MultipartFile file) throws Exception {

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);


        try {


            List<Map<String, Object>> userLevelList = dictionaryService.selectDictionarysByTtypes("ttype = 'userLevel'", 10000, 0, (String) loginUserMap.get("pcCompanyId"));
            List<Map<String, Object>> sourceTypeList = dictionaryService.selectDictionarysByTtypes("ttype ='sourceType'", 10000, 0, (String) loginUserMap.get("pcCompanyId"));
            List<Map<String, Object>> industryList = dictionaryService.selectDictionarysByTtypes("ttype='industry'", 10000, 0, (String) loginUserMap.get("pcCompanyId"));
            List<Map<String, Object>> userList = userService.selectUsers((String) loginUserMap.get("pcCompanyId"), null);

            Map<String, Object> userLevelMap = new HashMap<>();
            Map<String, Object> sourceTypeMap = new HashMap<>();
            Map<String, Object> industryMap = new HashMap<>();
            Map<String, Object> userMap = new HashMap<>();

            for (int i = 0; i < userLevelList.size(); i++) {
                Map<String, Object> map = userLevelList.get(i);
                userLevelMap.put((String) map.get("value"), map.get("id"));
            }

            for (int i = 0; i < sourceTypeList.size(); i++) {
                Map<String, Object> map = sourceTypeList.get(i);
                sourceTypeMap.put((String) map.get("value"), map.get("id"));
            }


            for (int i = 0; i < industryList.size(); i++) {
                Map<String, Object> map = industryList.get(i);
                industryMap.put((String) map.get("value"), map.get("id"));
            }


            for (int i = 0; i < userList.size(); i++) {
                Map<String, Object> map = userList.get(i);
                userMap.put((String) map.get("uname"), map.get("id"));
            }


            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            int rowsNum = sheet.getLastRowNum();

            if (rowsNum > 1001) {
                return ResponseConstants.NO_MORE_THAN_1000;
            }

             String msg = "";
            for (int i = 1; i <= rowsNum; i++) {
                Row row = sheet.getRow(i);
                String companyName = row.getCell(0) == null ? null : row.getCell(0).getStringCellValue();

                if (StringUtils.isBlank(companyName)) {
                    throw new GlobalException(ResponseConstants.COMPANY_NAME_NOT_NULL);
                }

                String province = row.getCell(1) == null ? null : row.getCell(1).getStringCellValue();
                String city = row.getCell(2) == null ? null : row.getCell(2).getStringCellValue();
                String county = row.getCell(3) == null ? null : row.getCell(3).getStringCellValue();
                String address = row.getCell(4) == null ? null : row.getCell(4).getStringCellValue();
                String website = row.getCell(5) == null ? null : row.getCell(5).getStringCellValue();
                String contact = row.getCell(6) == null ? null : row.getCell(6).getStringCellValue();
                String contactphone = row.getCell(7) == null ? null : row.getCell(7).getStringCellValue();
                String adviser = row.getCell(8) == null ? null : row.getCell(8).getStringCellValue();
                String director = row.getCell(9) == null ? null : row.getCell(9).getStringCellValue();
                String userLevel = row.getCell(10) == null ? null : row.getCell(10).getStringCellValue();
                String sourceType = row.getCell(11) == null ? null : row.getCell(11).getStringCellValue();
                String industry = row.getCell(12) == null ? null : row.getCell(12).getStringCellValue();
                Map<String, Object> map = new HashMap<>();
                List<Company> companyInDb = companyServie.selectCompanyByName(companyName, (String) loginUserMap.get("pcCompanyId"));
                //如果库中不存在该企业
                if (companyInDb.size() == 0) {
                    String companyId = UUID.randomUUID().toString();
                    map.put("id", companyId);
                    map.put("companyName", companyName);
                    map.put("province", province);
                    map.put("city", city);
                    map.put("county", county);
                    map.put("address", address);
                    map.put("website", website);
                    map.put("contactName", contact);
                    map.put("contactPhone", contactphone);
                    map.put("companyAdviserId", userMap.get(adviser));
                    map.put("companyDirectorId", userMap.get(director));
                    map.put("userLevelId", userLevelMap.get(userLevel));
                    map.put("channelId", sourceTypeMap.get(sourceType));
                    map.put("industryId", industryMap.get(industry));
                    map.put("departmentId",loginUserMap.get("departmentId"));
                    map.put("pcCompanyId", loginUserMap.get("pcCompanyId"));
                    map.put("potentialStatus",1);

                    try {


                        String companyInfo = CompanyInfoApi.queryCompanyInfo("http://i.yjapi.com/ECISimple/GetDetailsByName", companyName, 1, 10);
                        //企查查有数据的时候
                        if (companyInfo != null) {
                            JsonParser parse = new JsonParser();  //创建json解析器
                            JsonObject json = (JsonObject) parse.parse(companyInfo);  //创建jsonObject对象
                            if ("200".equals(json.get("Status").getAsString())) {
                                String establishTime = json.get("Result").getAsJsonObject().get("StartDate") == null ? "" : json.get("Result").getAsJsonObject().get("StartDate").getAsString().substring(0, 10);//get("StartDate").getAsString().substring(0, 10)
                                String operatingPeriod = json.get("Result").getAsJsonObject().get("TeamEnd") == null ? "" : json.get("Result").getAsJsonObject().get("TeamEnd").getAsString().substring(0, 10);
                                String approveDate = json.get("Result").getAsJsonObject().get("CheckDate") == null ? "" : json.get("Result").getAsJsonObject().get("CheckDate").getAsString().substring(0, 10);
                                String termStart = json.get("Result").getAsJsonObject().get("TermStart") == null ? "" : json.get("Result").getAsJsonObject().get("TermStart").getAsString().substring(0, 10);
                                String socialCredit = json.get("Result").getAsJsonObject().get("CreditCode").isJsonNull() ? null : json.get("Result").getAsJsonObject().get("CreditCode").getAsString();
                                String registrationCode = json.get("Result").getAsJsonObject().get("No").isJsonNull() ? null : json.get("Result").getAsJsonObject().get("No").getAsString();
                                String organizationCode = json.get("Result").getAsJsonObject().get("No").isJsonNull() ? null : json.get("Result").getAsJsonObject().get("No").getAsString();
                                String manageType = json.get("Result").getAsJsonObject().get("Status").isJsonNull() ? null : json.get("Result").getAsJsonObject().get("Status").getAsString();
                                String representative = json.get("Result").getAsJsonObject().get("OperName").isJsonNull() ? null : json.get("Result").getAsJsonObject().get("OperName").getAsString();
                                String registeredMoney = json.get("Result").getAsJsonObject().get("RegistCapi").isJsonNull() ? null : json.get("Result").getAsJsonObject().get("RegistCapi").getAsString();
                                String registrationAuthority = json.get("Result").getAsJsonObject().get("BelongOrg").isJsonNull() ? null : json.get("Result").getAsJsonObject().get("BelongOrg").getAsString();
                                String scope = json.get("Result").getAsJsonObject().get("Scope").isJsonNull() ? "" : json.get("Result").getAsJsonObject().get("Scope").getAsString();
                                String econKind = json.get("Result").getAsJsonObject().get("EconKind").isJsonNull() ? "" : json.get("Result").getAsJsonObject().get("EconKind").getAsString();
                                String name = json.get("Result").getAsJsonObject().get("Name").isJsonNull() ? "" : json.get("Result").getAsJsonObject().get("Name").getAsString();

                                map.put("companyName", name);
                                map.put("representative", representative);
                                map.put("status", "0");
                                map.put("companyName", name);
                                map.put("socialCredit", socialCredit);
                                map.put("registrationCode", registrationCode);
                                map.put("organizationCode", organizationCode);
                                map.put("manageType", manageType);
                                map.put("manageScope", scope);
                                map.put("registeredMoney", registeredMoney);
                                map.put("registrationAuthority", registrationAuthority);
                                map.put("content", scope);
                                map.put("econKind", econKind);
                                map.put("establishTime", establishTime);
                                map.put("approveDate", approveDate);
                                map.put("termStart", termStart);
                                map.put("operatingPeriod", operatingPeriod);
                                map.put("createDate", new Date());
                                map.put("updateDate", new Date());
                                map.put("qccUpdatedDate", DateUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                            }
                        }
                    } catch (Exception e) {
                        log.error("异常栈:", e);
                    }

                    companyServie.insertCompanyInfo(map);
                    companyServie.insertCompanyDetailInfo(map);
                    String contactUserId = userInfoService.addUserInfo(contact, null, contactphone, companyId, null, "1", null, null, contactphone, null, null, null, loginUserId, null, null, null, null);
                    Map<String, Object> companyUpdateMap = new HashMap<>();
                    companyUpdateMap.put("id", companyId);
                    companyUpdateMap.put("contactUserId", contactUserId);
                    companyServie.updateCompanyInfoById(companyUpdateMap);
                } else {

                    if (StringUtils.isBlank(msg)){
                        msg="<span style='padding:0px 2px'>以下企业未导入成功</span>" +
                                "<br/>";
                    }

                    msg =   msg + ""+"第"+i+"行<span style='color:red;padding:0px 2px'>"+companyName+"已经存在</span> <br/>";

                }
            }
                if (StringUtils.isNotBlank(msg)){
                    responseDbCenter.setRepNote(msg);
                    responseDbCenter.setRepCode("ERROR-1");
                }


        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof GlobalException){
                throw new GlobalException(((GlobalException)e).getResponseDbCenter());
            }else {
                throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
            }

        }
        return responseDbCenter;

    }


    /**
     * 设置有效性
     *
     * @param offset 主影响单元格所在列，即此单元格由哪个单元格影响联动
     * @param sheet
     * @param rowNum 行数
     * @param colNum 列数
     */
    public static void setDataValidation(String offset, XSSFSheet sheet, int rowNum, int colNum) {
        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
        DataValidation data_validation_list;
        data_validation_list = getDataValidationByFormula(
                "INDIRECT($" + offset + (rowNum) + ")", rowNum, colNum, dvHelper);
        sheet.addValidationData(data_validation_list);
    }

    /**
     * 加载下拉列表内容
     *
     * @param formulaString
     * @param naturalRowIndex
     * @param naturalColumnIndex
     * @param dvHelper
     * @return
     */
    private static DataValidation getDataValidationByFormula(
            String formulaString, int naturalRowIndex, int naturalColumnIndex, XSSFDataValidationHelper dvHelper) {
        // 加载下拉列表内容
        // 举例：若formulaString = "INDIRECT($A$2)" 表示规则数据会从名称管理器中获取key与单元格 A2 值相同的数据，
        //如果A2是江苏省，那么此处就是江苏省下的市信息。
        XSSFDataValidationConstraint dvConstraint = (XSSFDataValidationConstraint) dvHelper.createFormulaListConstraint(formulaString);
        // 设置数据有效性加载在哪个单元格上。
        // 四个参数分别是：起始行、终止行、起始列、终止列
        int firstRow = naturalRowIndex - 1;
        int lastRow = naturalRowIndex - 1;
        int firstCol = naturalColumnIndex - 1;
        int lastCol = naturalColumnIndex - 1;
        CellRangeAddressList regions = new CellRangeAddressList(firstRow,
                lastRow, firstCol, lastCol);
        // 数据有效性对象
        // 绑定
        XSSFDataValidation data_validation_list = (XSSFDataValidation) dvHelper.createValidation(dvConstraint, regions);
        data_validation_list.setEmptyCellAllowed(false);
        if (data_validation_list instanceof XSSFDataValidation) {
            data_validation_list.setSuppressDropDownArrow(true);
            data_validation_list.setShowErrorBox(true);
        } else {
            data_validation_list.setSuppressDropDownArrow(false);
        }
        // 设置输入信息提示信息
        data_validation_list.createPromptBox("下拉选择提示", "请使用下拉方式选择合适的值！");
        // 设置输入错误提示信息
        //data_validation_list.createErrorBox("选择错误提示", "你输入的值未在备选列表中，请下拉选择合适的值！");
        return data_validation_list;
    }

    /**
     * 计算formula
     *
     * @param offset   偏移量，如果给0，表示从A列开始，1，就是从B列
     * @param rowId    第几行
     * @param colCount 一共多少列
     * @return 如果给入参 1,1,10. 表示从B1-K1。最终返回 $B$1:$K$1
     */
    public static String getRange(int offset, int rowId, int colCount) {
        char start = (char) ('A' + offset);
        if (colCount <= 25) {
            char end = (char) (start + colCount - 1);
            return "$" + start + "$" + rowId + ":$" + end + "$" + rowId;
        } else {
            char endPrefix = 'A';
            char endSuffix = 'A';
            if ((colCount - 25) / 26 == 0 || colCount == 51) {// 26-51之间，包括边界（仅两次字母表计算）
                if ((colCount - 25) % 26 == 0) {// 边界值
                    endSuffix = (char) ('A' + 25);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                }
            } else {// 51以上
                if ((colCount - 25) % 26 == 0) {
                    endSuffix = (char) ('A' + 25);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26 - 1);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26);
                }
            }
            return "$" + start + "$" + rowId + ":$" + endPrefix + endSuffix + "$" + rowId;
        }
    }


}
