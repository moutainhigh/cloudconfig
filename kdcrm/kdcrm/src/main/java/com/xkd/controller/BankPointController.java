package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.*;
import com.xkd.utils.HttpRequest;
import io.swagger.annotations.ApiParam;
import jdk.nashorn.internal.ir.IfNode;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
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
 * 说明
 *
 *该Controller实际上与银行业务无关，由于之前产品设计银行项目单独分出来，后来又要合上了，由于之前类名与Bank有关，现在实际上
 * 只与企业有关，实际业务中并没有银行相关业务，只有通用版的客户业务
 *
 */
@Controller
@Transactional
@RequestMapping("/bankPoint")
public class BankPointController extends BaseController{


    @Autowired
    BankPointService bankPointService;
    @Autowired
    BankProjectService bankProjectService;
    @Autowired
    UserDynamicService userDynamicService;
    @Autowired
    UserDataPermissionService userDataPermissionService;
    @Autowired
    private UserService userService;
    @Autowired
    DepartmentService departmentService;
    /**
     *
     * @author: xiaoz
     * @param req
     * @param rsp
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectBankPointsByContent",method =  {RequestMethod.POST,RequestMethod.GET})

    public ResponseDbCenter selectBankPointsByContent( @ApiParam(value = "搜索值") @RequestParam(required = false) String content,
                                                       @ApiParam(value = "公司Id") @RequestParam( required = false) String companyId,
                                                       @ApiParam(value = "当前页") @RequestParam( required = false) String currentPage,
                                                       @ApiParam(value = "每页多少数量") @RequestParam( required = false) String pageSize,
                                                       HttpServletRequest req, HttpServletResponse rsp){

       Integer currentPageInt=1;
        Integer pageSizeInt=10;

        if (StringUtils.isNotBlank(currentPage)&&StringUtils.isNotBlank(pageSize)){
              currentPageInt=Integer.parseInt(currentPage);
             pageSizeInt=Integer.parseInt(pageSize);
        }

        try {
            String loginUserId = (String) req.getAttribute("loginUserId");
            Map<String,Object> resultMap= bankPointService.selectBankPointsTotalByContent(content,loginUserId,companyId,currentPageInt,pageSizeInt);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(resultMap.get("list"));
            responseDbCenter.setTotalRows(resultMap.get("total")+"");
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }




    /**
     *
     * @author: xiaoz
     * @param req
     * @param rsp
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveBankPoint",method =  {RequestMethod.POST,RequestMethod.GET})
    public ResponseDbCenter saveBankPoint(@ApiParam("项目名")@RequestParam(required = false) String pointName,
                                          @ApiParam("项目Id")@RequestParam(required = false) String projectId,
                                          @ApiParam("公司Id")@RequestParam(required = true) String companyId,
                                          HttpServletRequest req, HttpServletResponse rsp) throws Exception{

        String loginUserId = (String) req.getAttribute("loginUserId");
        //  String loginUserId = "761";
        if (StringUtils.isBlank(pointName)) {
            return ResponseConstants.MISSING_PARAMTER;
        }

        try {



            Map<String,Object>  loginUserMap=userService.selectUserById(loginUserId);
            List<String>  departmentIdList=departmentService.selectChildDepartmentIds((String)loginUserMap.get("pcCompanyId"),loginUserMap);
            Map<String,Object> bankPointMap = bankPointService.selectBankPointByName(pointName,departmentIdList);
            if(bankPointMap != null && bankPointMap.size() > 0){
                return ResponseConstants.BANK_POINT_NAME_HAS_EXISTS;
            }

            Map<String,Object> map = new HashMap<>();
            String pointId = UUID.randomUUID().toString();
            map.put("id", pointId);
            map.put("pointName",pointName);

            map.put("companyId",companyId);
            map.put("createdBy",loginUserId);
            map.put("updatedBy",loginUserId);
            map.put("updateDate",new Date());
            map.put("createDate",new Date());

            bankPointService.saveBankPoint(map);

            if(StringUtils.isNotBlank(projectId)){
                List<String> idList = new ArrayList<>();
                idList.add(pointId);
			    bankProjectService.saveProjectPoints(projectId,idList);
                userDynamicService.addUserDynamic(loginUserId, projectId, "","新增", "新增了项目网点："+pointName, 0,null,null,null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }

    /**
     *
     * @author: xiaoz
     * @param req
     * @param rsp
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateBankPointById",method =  {RequestMethod.POST,RequestMethod.GET})
    public ResponseDbCenter updateBankPointById(@RequestParam(value = "pointName",required = false) String pointName,
                                                @RequestParam(value = "departmentId",required = false) String departmentId,
                                                @RequestParam(value = "id",required = false) String id ,HttpServletRequest req, HttpServletResponse rsp){

        String loginUserId = (String) req.getAttribute("loginUserId");
        //  String loginUserId = "761";
        if (StringUtils.isBlank(id)) {
            return ResponseConstants.MISSING_PARAMTER;
        }

        try {
            Map<String,Object>  loginUserMap=userService.selectUserById(loginUserId);
            List<String>  departmentIdList=departmentService.selectChildDepartmentIds((String)loginUserMap.get("pcCompanyId"),loginUserMap);
            Map<String,Object> bankPointMap = bankPointService.selectBankPointByName(pointName,departmentIdList);
            if(bankPointMap != null && bankPointMap.size() > 0){
                String pointId = (String)bankPointMap.get("id");
                if(!id.equals(pointId)){
                    return ResponseConstants.BANK_POINT_NAME_HAS_EXISTS;
                }
            }

            Map<String,Object> map = new HashMap<>();
            map.put("id", id);
            map.put("pointName",pointName);
            map.put("updatedBy",loginUserId);

            if(StringUtils.isBlank(departmentId)){
                Map<String, Object> mapp = userService.selectUserById(loginUserId);
                if(mapp.get("departmentId") != null){
                    map.put("departmentId",(String) mapp.get("departmentId"));
                }
            }else{
                map.put("departmentId",departmentId);
            }
            bankPointService.updateBankPointById(map);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }

//    dc_bank_project_point

    /**
     *
     * @author: xiaoz
     * @param req
     * @param rsp
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteBankPointByIds",method =  {RequestMethod.POST,RequestMethod.GET})
    public ResponseDbCenter deleteBankPointByIds(@RequestParam(value = "ids",required = false) String ids ,HttpServletRequest req, HttpServletResponse rsp) throws Exception{

        if (StringUtils.isBlank(ids)) {
            return ResponseConstants.MISSING_PARAMTER;
        }

        String[] idss = ids.split(",");

        List<String> idList = new ArrayList<>();
        for(String id : idss){
            idList.add(id);
        }

        try {

            bankPointService.deleteBankPointByIds(idList);
            bankPointService.deleteProjectPointByPointIds(idList);

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }

}
