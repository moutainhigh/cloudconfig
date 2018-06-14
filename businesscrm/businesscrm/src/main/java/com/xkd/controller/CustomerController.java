package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.*;
import com.xkd.utils.DateUtils;
import com.xkd.utils.MailUtils;
import com.xkd.utils.PropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Created by dell on 2018/1/10.
 */
@Api(description = "账号管理")
@Controller
@RequestMapping("/customer")
@Transactional
public class CustomerController extends BaseController {

    @Autowired
    CustomerService customerService;

    @Autowired
    UserService userService;

    @Autowired
    UserDataPermissionService userDataPermissionService;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    OperateCacheService operateCacheService;

    @Autowired
    private SysUserOperateService sysOperateService;

    @ApiOperation(value = "查询公司列表")
    @ResponseBody
    @RequestMapping(value = "/searchPcCompany", method = {  RequestMethod.POST})
    public ResponseDbCenter searchPcCompany(HttpServletRequest req, HttpServletResponse rsp,
                                            @ApiParam(value = "起时间", required = false) @RequestParam(required = false) String startDate,
                                            @ApiParam(value = "止时间", required = false) @RequestParam(required = false) String endDate,
                                            @ApiParam(value = "部门名称", required = false) @RequestParam(required = false) String departmentName,
                                            @ApiParam(value = "省", required = false) @RequestParam(required = false) String province,
                                            @ApiParam(value = "市", required = false) @RequestParam(required = false) String city,
                                            @ApiParam(value = "状态   1  失效   2 有效 3 已启用 4已停用 ", required = false) @RequestParam(required = false) Integer status,
                                            @ApiParam(value = "当前页", required = true) @RequestParam(required = true) Integer currentPage,
                                            @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize
    ) throws Exception {
        if (StringUtils.isBlank(startDate)){
            startDate=null;
        }
        if (StringUtils.isBlank(endDate)){
            endDate=null;
        }
        try {

            List<Map<String, Object>> mapList = customerService.searchPcCompany(startDate, endDate, departmentName, province, city, status, currentPage, pageSize);
            Integer count = customerService.searchPcCompanyCount(startDate, endDate, departmentName, province, city, status);
            ResponseDbCenter responseDbCenter=new ResponseDbCenter();
            responseDbCenter.setResModel(mapList);
            responseDbCenter.setTotalRows(count+"");
            return responseDbCenter;
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "查询公司详情")
    @ResponseBody
    @RequestMapping(value = "/getPcCompanyDetail", method = {  RequestMethod.POST})
    public ResponseDbCenter getPcCompanyDetail(HttpServletRequest req, HttpServletResponse rsp,
                                            @ApiParam(value = "Id ", required = false) @RequestParam(required = false) String id

    ) throws Exception {
        try {
            Map<String,Object>  map=customerService.selectPcCompanyById(id);
            ResponseDbCenter responseDbCenter=new ResponseDbCenter();
            responseDbCenter.setResModel(map);
             return responseDbCenter;
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }




    @ApiOperation(value = "修改公司")
    @ResponseBody
    @RequestMapping(value = "/updatePcCompany", method = { RequestMethod.POST})
    public ResponseDbCenter updatePcCompany(HttpServletRequest req, HttpServletResponse rsp,
                                            @ApiParam(value = "Id", required = true) @RequestParam(required = true) String id,
                                            @ApiParam(value = "公司名称", required = false) @RequestParam(required = false) String departmentName,
                                            @ApiParam(value = "备注", required = false) @RequestParam(required = false) String remark,
                                            @ApiParam(value = "联系电话", required = false) @RequestParam(required = false) String mobile,
                                            @ApiParam(value = "邮箱", required = false) @RequestParam(required = false) String email,
                                            @ApiParam(value = "地址", required = false) @RequestParam(required = false) String address,
                                            @ApiParam(value = "截止时间 例:2018-12-31", required = false) @RequestParam(required = false) String dateTo,
                                            @ApiParam(value = "省", required = false) @RequestParam(required = false) String province,
                                            @ApiParam(value = "市", required = false) @RequestParam(required = false) String city,
                                            @ApiParam(value = "县", required = false) @RequestParam(required = false) String county
                                            ) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");

            Map<String,Object> map=new HashMap<>();
                map.put("id",id);
                map.put("departmentName",departmentName);
                map.put("remark",remark);
                map.put("mobile",mobile);
                map.put("email",email);
                map.put("address",address);
                map.put("dateFrom", DateUtils.dateToString(new Date(),"yyyy-MM-dd"));
                map.put("dateTo",dateTo);
                map.put("province",province);
                map.put("city",city);
                map.put("county",county);
                map.put("updatedBy",loginUserId);
                map.put("updateDate",new Date());
            customerService.updatePcCompany(map);
            return new ResponseDbCenter();
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }






    @ApiOperation(value = "添加公司")
    @ResponseBody
    @RequestMapping(value = "/addPcCompany", method = {RequestMethod.POST})
    public ResponseDbCenter addPcCompany(HttpServletRequest req, HttpServletResponse rsp,
                                            @ApiParam(value = "公司名称", required = true) @RequestParam(required = true) String departmentName,
                                            @ApiParam(value = "备注", required = false) @RequestParam(required = false) String remark,
                                            @ApiParam(value = "联系电话", required = false) @RequestParam(required = false) String mobile,
                                            @ApiParam(value = "邮箱", required = false) @RequestParam(required = false) String email,
                                            @ApiParam(value = "地址", required = false) @RequestParam(required = false) String address,
                                            @ApiParam(value = "有效期至", required = true) @RequestParam(required = true) String dateTo,
                                            @ApiParam(value = "省", required = false) @RequestParam(required = false) String province,
                                            @ApiParam(value = "市", required = false) @RequestParam(required = false) String city,
                                            @ApiParam(value = "县", required = false) @RequestParam(required = false) String county
    ) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");

            Map<String,Object> map=new HashMap<>();
            map.put("id",UUID.randomUUID().toString());
            map.put("parentId","1");
            map.put("departmentName",departmentName);
            map.put("remark",remark);
            map.put("mobile",mobile);
            map.put("email",email);
            map.put("address",address);
            map.put("dateFrom", DateUtils.dateToString(new Date(),"yyyy-MM-dd"));
            map.put("dateTo",dateTo);
            map.put("province",province);
            map.put("city",city);
            map.put("county",county);
            map.put("createDate",new Date());
            map.put("createdBy",loginUserId);
            map.put("enableStatus",1);

            customerService.savePcCompany(map,loginUserId);
            return new ResponseDbCenter();
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }




    @ApiOperation(value = "启用停用公司")
    @ResponseBody
    @RequestMapping(value = "/enablePcCompany", method = {RequestMethod.POST})
    public ResponseDbCenter enablePcCompany(HttpServletRequest req, HttpServletResponse rsp,
                                         @ApiParam(value = "Id", required = true) @RequestParam(required = true) String id,
                                            @ApiParam(value = "停用启用状态   0 启用 1  停用", required = true) @RequestParam(required = true) String enableStatus
    ) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");

            Map<String,Object> map=new HashMap<>();
            map.put("id",id);
            map.put("enableStatus",enableStatus);
            map.put("updateDate",new Date());
            map.put("updatedBy",loginUserId);
            customerService.updatePcCompany(map);
            return new ResponseDbCenter();
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }




    @ApiOperation(value = "删除公司")
    @ResponseBody
    @RequestMapping(value = "/deletePcCompany", method = {RequestMethod.POST})
    public ResponseDbCenter enablePcCompany(HttpServletRequest req, HttpServletResponse rsp,
                                            @ApiParam(value = "Id", required = true) @RequestParam(required = true) String id

    ) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");

            Map<String,Object> map=new HashMap<>();
            map.put("id",id);
            map.put("status",2);
            map.put("updateDate",new Date());
            map.put("updatedBy",loginUserId);
            customerService.updatePcCompany(map);
            return new ResponseDbCenter();
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }



    @ApiOperation(value = "修改公司菜单")
    @ResponseBody
    @RequestMapping(value = "/updatePcCompanyMenu", method = { RequestMethod.POST})
    public ResponseDbCenter updatePcCompanyMenu(HttpServletRequest req, HttpServletResponse rsp,
                                         @ApiParam(value = "菜单Id列表,逗号分隔", required = true) @RequestParam(required = true) String menuIds,
                                                @ApiParam(value = "id", required = true) @RequestParam(required = true) String id
    ) throws Exception {
        try {
            // 当前登录用户的Id
            String loginUserId = (String) req.getAttribute("loginUserId");
            List<String> list=new ArrayList<>();
            if (StringUtils.isNotBlank(menuIds)){
                String[] str=menuIds.split(",");
                for (int i = 0; i <str.length ; i++) {
                    list.add(str[i]);
                }
            }
            customerService.saveCustomerMenu(list,id,loginUserId);


            //清除旧有权限，便于在拦截器中重新加载
            operateCacheService.clear();


            return new ResponseDbCenter();
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }




    @ApiOperation(value = "查询有权限的菜单Id")
    @ResponseBody
    @RequestMapping(value = "/selectCustomerMenuByPcCompanyId", method = { RequestMethod.POST})
    public ResponseDbCenter selectCustomerMenuByPcCompanyId(HttpServletRequest req, HttpServletResponse rsp,
                                                @ApiParam(value = "客户公司Id", required = true) @RequestParam(required = true) String id
    ) throws Exception {
        try {
            // 当前登录用户的Id
             List<String> list=customerService.selectCustomerMenuByPcCompanyId(id);
            ResponseDbCenter responseDbCenter= new ResponseDbCenter();
            responseDbCenter.setResModel(list);
            return responseDbCenter;
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }

    @ApiOperation(value = "查询管理员")
    @ResponseBody
    @RequestMapping(value = "/selectPcAdminUserByPcCompanyId", method = { RequestMethod.POST})
    public ResponseDbCenter selectPcAdminUserByPcCompanyId(HttpServletRequest req, HttpServletResponse rsp,
                                                            @ApiParam(value = "客户公司Id", required = true) @RequestParam(required = true) String id
    ) throws Exception {
        try {
            // 当前登录用户的Id
            List<Map<String,Object>> list=customerService.selectPcAdminUserByPcCompanyId(id);
            ResponseDbCenter responseDbCenter= new ResponseDbCenter();
            responseDbCenter.setResModel(list);
            responseDbCenter.setTotalRows(list.size()+"");
            return responseDbCenter;
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "删除管理员")
    @ResponseBody
    @RequestMapping(value = "/deletePcAdmin", method = { RequestMethod.POST})
    public ResponseDbCenter deletePcAdmin(HttpServletRequest req, HttpServletResponse rsp,
                                                           @ApiParam(value = "id", required = true) @RequestParam(required = true) String id
    ) throws Exception {
        try {
            // 当前登录用户的Id
            String loginUserId = (String) req.getAttribute("loginUserId");
            List<String> userIdList = new ArrayList<>();
            userIdList.add(id);
            userService.deleteUserByIds(userIdList);
            /**
             * 删除用户权限
             */
            sysOperateService.deleteByUserIds(userIdList);
            ResponseDbCenter responseDbCenter= new ResponseDbCenter();
             return responseDbCenter;
        } catch (Exception e) {
            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }



    /**
     * 保存用户
     * @param req
     * @param rsp
     * @return
     */
    @ApiOperation(value = "添加，修改管理员")
    @ResponseBody
    @RequestMapping(value = "/changeAdmin", method =  { RequestMethod.POST })
    public ResponseDbCenter changeAdmin(HttpServletRequest req,HttpServletResponse rsp,
                                         @ApiParam(value = "操作状态  insert表示插入 update 表示更新",required = true)  @RequestParam(required = true) String flag,
                                         @ApiParam(value = "用户Id 修改时需要传，添加时不用传" ,required = false)  @RequestParam(required = false) String id,
                                         @ApiParam(value = "客户公司Id" ,required = false)  @RequestParam(required = false) String pcCompanyId,
                                         @ApiParam(value = "姓名" ,required = false) @RequestParam(required = false) String uname,
                                         @ApiParam(value = "邮箱" ,required = false) @RequestParam(required = false) String email,
                                         @ApiParam(value = "手机" ,required = false) @RequestParam(required = false) String mobile,
                                         @ApiParam(value = "性别 男，女",required = false) @RequestParam(required = false) String sex
    ) throws Exception{


        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");



        //传过来的密码已经是加密的了
        String token = req.getHeader("token");

        if(org.apache.commons.lang.StringUtils.isBlank(flag) || org.apache.commons.lang.StringUtils.isBlank(uname) || org.apache.commons.lang.StringUtils.isBlank(email) ||
                (org.apache.commons.lang.StringUtils.isBlank(id) && "update".equals(flag))){

            return ResponseConstants.MISSING_PARAMTER;
        }
        Map<String,Object> map = new HashedMap<>();

        Map<String,Object> existUser=userService.selectUserByOnlyMobile(mobile);
        if (existUser!=null) { //如果库中已经有，但删除状态为删除的记录，则直接更新

            if (!existUser.get("id").equals(id)&&0==(Integer)existUser.get("status")&&(1==(Integer)existUser.get("platform"))) {   //如果存在的用户不是删除状态，则报错给前台
                return ResponseConstants.MOBILE_EXIST;
            }

            flag="update";
            map.put("id", existUser.get("id"));
            map.put("platform",1);
        }


        if("0".equals(sex)){

            sex = "男";


        }else if("1".equals(sex)) {
            sex = "女";
        }

        map.put("uname",uname);
        map.put("sex",sex);
        map.put("platform",1);
        map.put("email",email);
        map.put("mobile", mobile);
        map.put("departmentId",pcCompanyId);
        map.put("pcCompanyId",pcCompanyId);
        map.put("status", "0");  //恢复为未删除状态


        List<Map<String,Object>> roleList=  customerService.selectPcAdminRole(pcCompanyId);
       if (roleList.size()>0){
           map.put("roleId",roleList.get(0).get("id"));
       }else {
           return ResponseConstants.FUNC_SERVERERROR;
       }

        try {

            if("insert".equals(flag)){
                map.put("id", UUID.randomUUID().toString());
                map.put("createDate",new Date());

                Map<String, Object> userMaps = null;
                try {

                    userMaps = userService.selectUserByEmail(email,1);

                } catch (Exception e) {

                    return ResponseConstants.EMAIL_EXIST;
                }

                if(userMaps != null ){

                    return ResponseConstants.EMAIL_EXIST;
                }


                Random random = new Random();

                char[] stonesEnglish = {'a','b','c','d','e','f','g','h','i',
                        'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

                char[] stones = {'1','2','3','4','5','6','7','8','9','0','a','b','c','d','e','f','g','h','i',
                        'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

                StringBuffer sb = new StringBuffer();

                sb.append(stonesEnglish[random.nextInt(stonesEnglish.length)]);

                for(int i = 0 ;i<5;i++){

                    sb.append(stones[random.nextInt(stones.length)]);
                }

                String password = sb.toString();

                String enPassWord = new String(com.xkd.utils.Base64.encode(password));

                map.put("password",enPassWord);

                if(org.apache.commons.lang.StringUtils.isNotBlank(loginUserId)){

                    map.put("createdBy", loginUserId);
                }

                userService.insertDcUser(map);
                userService.insertDcUserDetail(map);

                String href = PropertiesUtil.USER_LOGIN_HREF;

                String hrefStr = "<a href='"+href+"'>&nbsp;点此处登录&nbsp;</a>";
                String content = "尊敬的  "+uname+"用户，感谢您注册小蝌蚪CRM。<br>"+
                        "您的账号是:"+email+"<br>"+
                        "您的密码是:"+password+"<br>"+
                        "本邮件为系统自动邮件，请勿回复。"+
                        "<br>感谢您对小蝌蚪的支持！请访问登录界面"+hrefStr;

                MailUtils.send(email, "小蝌蚪CRM注册", content);


            }else{

                if (org.apache.commons.lang.StringUtils.isNotBlank(email)) {
                    //如果email已经存在了，则不允许添加
                    Map<String, Object> userMap = userService.selectUserByEmail(email, 1);
                    if (userMap != null) {
                        String idExists = (String) userMap.get("id");
                        if (userMap != null && userMap.size() > 0 && !idExists.equals(id)) {
                            return ResponseConstants.EMAIL_EXIST;
                        }
                    }
                }

                if (existUser==null) {//如果不存在手机码的用户


                    map.put("updatedBy", loginUserId);
                    map.put("updateDate", new Date());
                    map.put("id",id);
                }


                userService.updateDcUser(map);
                userService.updateDcUserDetail(map);
                //清 除旧的数据权限
                userDataPermissionService.clearCacheData((String)map.get("id"));

            }


            //清除权限缓存
            operateCacheService.clear(token);



        } catch (Exception e) {

            log.error("异常栈:", e);
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }




}
