package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.CompanyContactorService;
import com.xkd.service.DepartmentService;
import com.xkd.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import java.util.List;
import java.util.Map;

/**
 * 创建人   巫建辉
 */
@Api(description = "通讯录")
@Controller
@RequestMapping("/addressBookController")
@Transactional
public class AddressBookController {

    @Autowired
    UserService userService;
    @Autowired
    CompanyContactorService companyContactorService;

    @Autowired
    DepartmentService departmentService;

    @ApiOperation(value = "通讯录----技师端")
    @ResponseBody
    @RequestMapping(value = "/searchTechinicanContactor", method = {  RequestMethod.POST})
    public ResponseDbCenter searchTechinicanContactor(HttpServletRequest req, HttpServletResponse rsp,
                                                      @ApiParam(value = "当前页", required = true) @RequestParam(required = true) Integer currentPage,
                                                      @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize



    ) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");
//        String loginUserId = "818";
        Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);

        try {
            List<Map<String,Object>> list=null;
            int totalRows=0;
            list=userService.searchTechinicanContactor((String)loginUserMap.get("departmentId"),currentPage,pageSize);
            totalRows=userService.searchTechinicanContactorCount((String)loginUserMap.get("departmentId"));

            ResponseDbCenter responseDbCenter= new ResponseDbCenter();
            responseDbCenter.setResModel(list);
            responseDbCenter.setTotalRows(totalRows+"");
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }






    @ApiOperation(value = "通讯录----客户端")
    @ResponseBody
    @RequestMapping(value = "/selectCustomerContactor", method = {  RequestMethod.POST})
    public ResponseDbCenter selectCustomerContactor(HttpServletRequest req, HttpServletResponse rsp,
                                                      @ApiParam(value = "服务商ID", required = true) @RequestParam(required = true) String pcCompanyId,
                                                      @ApiParam(value = "当前页", required = true) @RequestParam(required = true) Integer currentPage,
                                                      @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize



    ) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");

        try {
            List<Map<String,Object>> list=null;
            int totalRows=0;
            List<String> companyIdList=companyContactorService.selectCompanyIdListByContactor(loginUserId,1);
            list=userService.selectCustomerContactor(companyIdList,pcCompanyId,currentPage,pageSize);
            totalRows=userService.selectCustomerContactorCount(companyIdList, pcCompanyId);

            ResponseDbCenter responseDbCenter= new ResponseDbCenter();
            responseDbCenter.setResModel(list);
            responseDbCenter.setTotalRows(totalRows+"");
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }



    @ApiOperation(value = "查询服务商列表--客户端")
    @ResponseBody
    @RequestMapping(value = "/searchPcCompanyCustomer", method = {  RequestMethod.POST})
    public ResponseDbCenter searchPcCompanyCustomer(HttpServletRequest req, HttpServletResponse rsp,
                                            @ApiParam(value = "当前页", required = true) @RequestParam(required = true) Integer currentPage,
                                            @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize
    ) throws Exception {
        try {

            String loginUserId = (String) req.getAttribute("loginUserId");
             List<Map<String, Object>> mapList=null;
            Integer count=0;
            List<String>  pcCompanyIdList=companyContactorService.selectPcCompanyIdListByContactor(loginUserId);

            mapList = departmentService.selectDepartmentByIds(pcCompanyIdList,currentPage,pageSize);
            count=  departmentService.selectDepartmentCountByIds(pcCompanyIdList);
            ResponseDbCenter responseDbCenter=new ResponseDbCenter();
            responseDbCenter.setResModel(mapList);
            responseDbCenter.setTotalRows(count+"");
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


}
