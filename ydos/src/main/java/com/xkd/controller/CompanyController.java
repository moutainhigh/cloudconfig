package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xkd.exception.GlobalException;
import com.xkd.model.Company;
import com.xkd.model.Payment;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.*;
import com.xkd.utils.DateUtils;
import com.xkd.utils.FileUtil;
import com.xkd.utils.PropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;
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
 * 创建人 巫建辉
 * 客户管理相关接口
 */
@Api(description = "客户管理")
@Controller
@RequestMapping("/company")
@Transactional
public class CompanyController extends BaseController {

    @Autowired
    private CompanyService companyServie;


    @Autowired
    private UserService userService;





    @Autowired
    private CompanyContactorService companyContactorService;


    @Autowired
    DepartmentService departmentService;

    Logger logger = Logger.getLogger(CompanyController.class);


    @ApiOperation(value = "搜索客户-----服务端，技师端 ")
    @ResponseBody
    @RequestMapping(value = "/searchCompanyByName", method = {RequestMethod.POST})
    public ResponseDbCenter searchCompanyByName(HttpServletRequest req, HttpServletResponse rsp,
                                                @ApiParam(value = "公司名称", required = false) @RequestParam(required = false) String companyName,
                                                @ApiParam(value = "客户公司级别  大客户，中客户 ，小客户    传中文 ", required = false) @RequestParam(required = false) String userLevel,
                                                @ApiParam(value = "时间起  时间格式 如  2015-10-10 10:10:10 ", required = false) @RequestParam(required = false) String fromDate,
                                                @ApiParam(value = "时间止  时间格式 如  2015-10-10 10:10:10", required = false) @RequestParam(required = false) String toDate,
                                                @ApiParam(value = "当前页", required = false) @RequestParam(required = false) Integer currentPage,
                                                @ApiParam(value = "每页多少条", required = false) @RequestParam(required = false) Integer pageSize) throws Exception {


        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);

        List<Map<String, Object>> companyList = null;
        int totalRows=0;

        try {

                companyList = companyServie.searchCompanyByName(companyName, (String)loginUserMap.get("pcCompanyId"),userLevel,fromDate,toDate,currentPage , pageSize);
                totalRows=companyServie.searchCompanyCountByName(companyName, (String)loginUserMap.get("pcCompanyId"),userLevel,fromDate,toDate);


        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(companyList);
        responseDbCenter.setTotalRows(totalRows+"");
        return responseDbCenter;
    }



    /**
     * @param req
     * @param rsp
     * @return
     * @author: xiaoz
     * @2017年4月20日
     * @功能描述:删除客户信息
     */
    @ApiOperation(value = "批量删除客户")
    @RequestMapping(value = "/deleteCompanyByIds", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseDbCenter deleteCompanyByIds(HttpServletRequest req, HttpServletResponse rsp,
                                               @ApiParam(value = "ids 多个值以逗号隔开  ", required = true) @RequestParam(required = true) String ids) throws Exception {


        if (StringUtils.isBlank(ids)) {

            return ResponseConstants.MISSING_PARAMTER;
        }

        List<String> idList = new ArrayList<>();

        String[] cids = ids.split(",");

         for (int i = 0; i < cids.length; i++) {
             idList.add(cids[i]);
        }

        try {
            companyServie.deleteCompanyById(idList);
        } catch (Exception e) {
            e.printStackTrace();
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
     * @2017年5月11日
     * @功能描述:新增企业信息
     */
    @ApiOperation(value = "添加客户")
    @RequestMapping(value = "/addCompanyInfo", method = { RequestMethod.POST})
    @ResponseBody
    public ResponseDbCenter addCompanyInfo(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "公司名称", required = true) @RequestParam(required = true) String companyName,
                                           @ApiParam(value = "公司logo", required = false) @RequestParam(required = false) String englishName,
                                           @ApiParam(value = "企业标签", required = false) @RequestParam(required = false) String label,
                                           @ApiParam(value = "logo", required = false) @RequestParam(required = false) String logo,
                                           @ApiParam(value = "公司等级", required = false) @RequestParam(required = false) String userLevel,
                                           @ApiParam(value = "联系人姓名", required = false) @RequestParam(required = false) String contactName,
                                           @ApiParam(value = "联系人电话", required = false) @RequestParam(required = false) String contactPhone,
                                           @ApiParam(value = "国家", required = false) @RequestParam(required = false) String country,
                                           @ApiParam(value = "省", required = false) @RequestParam(required = false) String province,
                                           @ApiParam(value = "市", required = false) @RequestParam(required = false) String city,
                                           @ApiParam(value = "县", required = false) @RequestParam(required = false) String county,
                                           @ApiParam(value = "地址", required = false) @RequestParam(required = false) String address,
                                           @ApiParam(value = "状态  0 正常    2 删除", required = false) @RequestParam(required = false) Integer status,
                                           @ApiParam(value = "公司一句话描述", required = false) @RequestParam(required = false) String companyDesc,
                                           @ApiParam(value = "网站", required = false) @RequestParam(required = false) String website,
                                           @ApiParam(value = "电话", required = false) @RequestParam(required = false) String phone,
                                           @ApiParam(value = "公司邮箱", required = false) @RequestParam(required = false) String email,
                                           @ApiParam(value = "公司微信", required = false) @RequestParam(required = false) String wechat,
                                           @ApiParam(value = "公司描述", required = false) @RequestParam(required = false) String content
    ) throws Exception {

        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);

        Map<String, Object> company = new HashMap();
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        Map<String, Object> companyMap = departmentService.getCompanyIdByDepartmentId((String) loginUserMap.get("departmentId"));
        //设置该记录属性哪一个部门客户
        company.put("pcCompanyId", companyMap.get("id"));

        // 通过企业名称查询该企业是否存在，如果存在就返回提示信息

        List<Company> alreadyExists = companyServie.selectCompanyByNameIncludingDeleted(companyName, (String) company.get("pcCompanyId"));
        if (alreadyExists.size() > 0) {
            if (alreadyExists.get(0).getStatus() != 0) {
                //如果名称跟已经删除的数据中冲突，则将库里的旧数据名字上加上时间戳
                companyServie.deleteByCompanyById(alreadyExists.get(0).getId());
            } else {
                //如果名称与未删除的数据冲突，则不允许再添加了
                return ResponseConstants.FUNC_COMPANY_EXIST;
            }
        }

        String insertCompanyId = UUID.randomUUID().toString();
        company.put("id", insertCompanyId);
        company.put("companyName", companyName);
        company.put("englishName", englishName);
        company.put("label", label);
        company.put("logo", logo);
        company.put("userLevel", userLevel);
        company.put("contactName", contactName);
        company.put("contactPhone", contactPhone);
        company.put("country", country);
        company.put("province", province);
        company.put("city", city);
        company.put("county", county);
        company.put("address", address);
        company.put("status", status);
        company.put("companyDesc", companyDesc);
        company.put("website", website);
        company.put("phone", phone);
        company.put("email", email);
        company.put("wechat", wechat);
        company.put("content", content);
        company.put("createdBy", loginUserId);
        company.put("updatedBy", loginUserId);
        company.put("createDate", new Date());
        company.put("updateDate", new Date());




        try {

				/*
                 * 插入企业信息
				 */
            companyServie.insertCompanyInfo(company);


        } catch (Exception e) {

            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        responseDbCenter.setResModel(insertCompanyId);
        return responseDbCenter;
    }


    /**
     * @param req
     * @param rsp
     * @return
     * @author: xiaoz
     * @2017年5月11日
     * @功能描述:新增企业信息
     */
    @ApiOperation(value = "修改客户")
    @RequestMapping(value = "/updateCompanyInfo", method = { RequestMethod.POST})
    @ResponseBody
    public ResponseDbCenter updateCompanyInfo(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value = "公司Id", required = true) @RequestParam(required = true) String id,
                                              @ApiParam(value = "公司名称", required = false) @RequestParam(required = false) String companyName,
                                              @ApiParam(value = "公司logo", required = false) @RequestParam(required = false) String englishName,
                                              @ApiParam(value = "企业标签", required = false) @RequestParam(required = false) String label,
                                              @ApiParam(value = "logo", required = false) @RequestParam(required = false) String logo,
                                              @ApiParam(value = "公司等级", required = false) @RequestParam(required = false) String userLevel,
                                              @ApiParam(value = "联系人姓名", required = false) @RequestParam(required = false) String contactName,
                                              @ApiParam(value = "联系人电话", required = false) @RequestParam(required = false) String contactPhone,
                                              @ApiParam(value = "国家", required = false) @RequestParam(required = false) String country,
                                              @ApiParam(value = "省", required = false) @RequestParam(required = false) String province,
                                              @ApiParam(value = "市", required = false) @RequestParam(required = false) String city,
                                              @ApiParam(value = "县", required = false) @RequestParam(required = false) String county,
                                              @ApiParam(value = "地址", required = false) @RequestParam(required = false) String address,
                                              @ApiParam(value = "状态  0 正常    2 删除", required = false) @RequestParam(required = false) Integer status,
                                              @ApiParam(value = "公司一句话描述", required = false) @RequestParam(required = false) String companyDesc,
                                              @ApiParam(value = "网站", required = false) @RequestParam(required = false) String website,
                                              @ApiParam(value = "电话", required = false) @RequestParam(required = false) String phone,
                                              @ApiParam(value = "公司邮箱", required = false) @RequestParam(required = false) String email,
                                              @ApiParam(value = "公司微信", required = false) @RequestParam(required = false) String wechat,
                                              @ApiParam(value = "公司描述", required = false) @RequestParam(required = false) String content

    ) throws Exception {

        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);

        Map<String, Object> company = new HashMap();
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();


        Map<String, Object> companyMap = departmentService.getCompanyIdByDepartmentId((String) loginUserMap.get("departmentId"));
        //设置该记录属性哪一个部门客户
        company.put("pcCompanyId", companyMap.get("id"));

        // 通过企业名称查询该企业是否存在，如果存在就返回提示信息

        List<Company> alreadyExists = companyServie.selectCompanyByNameIncludingDeleted(companyName, (String) company.get("pcCompanyId"));
        if (alreadyExists.size() > 0) {
            if (!alreadyExists.get(0).getId().equals(id)) {
                if (alreadyExists.get(0).getStatus() != 0) {
                    //如果名称跟已经删除的数据中冲突，则将库里的旧数据名字上加上时间戳
                    companyServie.deleteByCompanyById(alreadyExists.get(0).getId());
                } else {
                    //如果名称与未删除的数据冲突，则不允许再添加了
                    return ResponseConstants.FUNC_COMPANY_EXIST;
                }
            }
        }

        try {

            company.put("id", id);
            company.put("companyName", companyName);
            company.put("englishName", englishName);
            company.put("label", label);
            company.put("logo", logo);
            company.put("userLevel", userLevel);
            company.put("contactName", contactName);
            company.put("contactPhone", contactPhone);
            company.put("country", country);
            company.put("province", province);
            company.put("city", city);
            company.put("county", county);
            company.put("address", address);
            company.put("status", status);
            company.put("companyDesc", companyDesc);
            company.put("website", website);
            company.put("phone", phone);
            company.put("email", email);
            company.put("wechat", wechat);
            company.put("content", content);
            company.put("createdBy", loginUserId);
            company.put("updatedBy", loginUserId);
            company.put("createDate", new Date());
            company.put("updateDate", new Date());



        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        try {

            //更新企业信息
            companyServie.updateCompanyInfoById(company);


        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }


        return responseDbCenter;
    }






    /**
     * @param req
     * @param rsp
     * @return
     * @author: xiaoz
     * @2017年5月11日
     * @功能描述:新增企业信息
     */
    @ApiOperation(value = "客户详情")
    @RequestMapping(value = "/companyDetail", method = { RequestMethod.POST})
    @ResponseBody
    public ResponseDbCenter companyDetail(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value = "公司Id", required = true) @RequestParam(required = true) String id


    ) throws Exception {

        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
        ResponseDbCenter responseDbCenter=new ResponseDbCenter();
        try {

           Company company=   companyServie.selectCompanyInfoById(id);
            responseDbCenter.setResModel(company);

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }


        return responseDbCenter;
    }





}
