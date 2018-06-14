package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.model.Role;
import com.xkd.service.*;
import com.xkd.utils.DateUtils;
import com.xkd.utils.OperateCacheUtil;
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

@Api(description = "角色的增删改查，角色权限修改等")
@Controller
@RequestMapping("/role")
@Transactional
public class RoleController extends BaseController {


    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private DC_PC_UserService pcUserService;
    @Autowired
    private UserService userService;

    @Autowired
    private SysRoleOperateService sysRoleOperateService;
    @Autowired
    DepartmentService departmentService;


    /**
     * @param req
     * @param rsp
     * @return
     * @author: xiaoz
     * @2017年7月27日
     * @功能描述:查询角色
     */
    @ApiOperation(value ="分页检索角色")
    @ResponseBody
    @RequestMapping(value = "/selectRoles",method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter selectRoles(HttpServletRequest req, HttpServletResponse rsp,
                                        @ApiParam(value = "角色名称",required = false) @RequestParam(required = false)  String roleContent,
                                        @ApiParam(value = "当前页",required = true) @RequestParam(required = true)String currentPage,
                                        @ApiParam(value = "每页条数",required = true) @RequestParam(required = true) String pageSize ) {



        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);





        if (StringUtils.isBlank(pageSize) || StringUtils.isBlank(currentPage)) {

            return ResponseConstants.MISSING_PARAMTER;
        }

        List<Map<String, Object>> roleMaps = null;
        Integer total = 0;
        String contentStr = "";

        /**
         * 不论是超级管理员还是普通用户都只加载自己公司的角色
         */

        if ("1".equals(loginUserMap.get("roleId"))){  //如果是超级管理员则加载超级管理员自己公司所有的角色
            contentStr=" and  r.pcCompanyId='"+loginUserMap.get("pcCompanyId")+"'";
        }else {//如果是管理员则加载管理员自己公司所有的角色，但不加载超级管理员角色
            contentStr=" and r.id!='1' and  r.pcCompanyId='"+loginUserMap.get("pcCompanyId")+"'";
        }

        if (StringUtils.isNotBlank(roleContent)) {

            contentStr =contentStr+ " and r.roleName like '%" + roleContent + "%' ";
        }

        int pageSizeInt = Integer.parseInt(pageSize);

        int currentPageInt = (Integer.parseInt(currentPage) - 1) * pageSizeInt;

        try {

            roleMaps = roleService.selectRoles(contentStr, currentPageInt, pageSizeInt);

            total = roleService.selectRolesCount(contentStr);


        } catch (Exception e) {

           e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(roleMaps);

        if (total != null) {

            responseDbCenter.setTotalRows(total + "");

        } else {

            responseDbCenter.setTotalRows("0");
        }


        return responseDbCenter;
    }





    /**
     * @param req
     * @param rsp
     * @return
     * @author: xiaoz
     * @2017年7月27日
     * @功能描述:只查询已经启用的角色
     */
    @ApiOperation(value = "查询某一个部门所在公司的角色")
    @ResponseBody
    @RequestMapping(value = "/selectRolesUnderCompany", method = {RequestMethod.GET,RequestMethod.POST})
    public ResponseDbCenter selectRolesUnderCompany(HttpServletRequest req, HttpServletResponse rsp,
                                                    @ApiParam(value = "部门Id",required = false) @RequestParam(required = false)  String departmentId
                                                    ) {


        List<Map<String, Object>> roleMaps = null;
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);

        Map<String,Object> companyMap=departmentService.getCompanyIdByDepartmentId(departmentId);

        try {
            if ("1".equals(loginUserMap.get("roleId"))) {
                roleMaps = roleService.selectRolesUnderCompany((String) companyMap.get("id"), 1);
            }else{
                roleMaps = roleService.selectRolesUnderCompany((String) companyMap.get("id"), null);
            }

        } catch (Exception e) {

           e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(roleMaps);

        return responseDbCenter;
    }







    /**
     * 批量删除角色
     *
     * @param req
     * @param rsp
     * @return
     */
    @ApiOperation(value ="批量删除角色")
    @ResponseBody
    @RequestMapping(value = "/deleteRolesByIds",method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter deleteRolesByIds(HttpServletRequest req, HttpServletResponse rsp,
                                             @ApiParam(value = "ids 多个Id逗号分隔如 \"1\",\"2\"",required = true)@RequestParam(required = true)String ids) throws Exception {







        if (StringUtils.isBlank(ids)) {

            return ResponseConstants.MISSING_PARAMTER;
        }

        String[] iids = ids.split(",");


        String idsString = "";
        List<String> idList=new ArrayList<>();
        for (int i = 0; i < iids.length; i++) {
            if ("1".equals(iids)) {
                return ResponseConstants.SUPER_ROLE_ERROR;
            }
            idsString += "'" + iids[i] + "',";
            idList.add(iids[i]);
        }

        String roleIds = "roleId in (" + idsString.substring(0, idsString.length() - 1) + ")";

        idsString = "id in (" + idsString.substring(0, idsString.length() - 1) + ")";

        try {

            if (!"1".equals("roleId")){//如果不是超级管理员。则需要判断要删除的角色是否是公司的管理员角色

                List<Map<String,Object>> roleList=roleService.selectRolesByIds(idList);
                for (int i = 0; i <roleList.size(); i++) {
                    Map<String,Object> role=roleList.get(i);
                     if (1==(Integer)role.get("isAdmin")){
                        return ResponseConstants.ADMIN_ROLE_CANNOT_DELETE;
                    }
                }
            }


            roleService.deleteRolesByIds(idsString);

            userService.deleteUserRolesByRoles(roleIds);
            /**
             * 删除角色下的权限
             */
            for (int i = 0; i < iids.length; i++) {
                sysRoleOperateService.deleteByRoleId(iids[i]);
            }


        } catch (Exception e) {

            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }


    /**
     * 保存角色权限
     *
     * @param req
     * @param rsp
     * @return
     */
    @ApiOperation(value ="添加角色")
    @ResponseBody
    @RequestMapping(value = "/addRole",method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter changeRole(HttpServletRequest req, HttpServletResponse rsp ,
                                       @ApiParam(value = "角色名称",required = true) @RequestParam(required = true) String roleName,
                                       @ApiParam(value = "角色描述",required = false) @RequestParam(required = false)  String content) throws Exception {
        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");

        Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);


        if (StringUtils.isBlank(roleName)) {
            return ResponseConstants.MISSING_PARAMTER;
        }

//        List<Map<String, Object>> existList = roleService.selectRoleByName(roleName);
//        if (existList.size() > 0) {
//            return ResponseConstants.ROLE_EXISTS;
//        }

        try {
            Role role = new Role();
            String newRoleId = UUID.randomUUID().toString();
            role.setId(newRoleId);
            role.setContent(content);
            role.setCreateBy(loginUserId);
            role.setRoleName(roleName);
            role.setStatus(0);
            //设置客户公司Id
            role.setPcCompanyId((String)loginUserMap.get("pcCompanyId"));

            //返回插入的id
            roleService.saveRole(role);

        } catch (Exception e) {

            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }


    /**
     * 复制权限到新角色
     *
     * @param req
     * @param rsp
     * @param id       旧角色ID
     * @param roleName 新角色名称
     * @param content  新角色描述
     * @return
     * @throws Exception
     */
    @ApiOperation(value ="复制角色")
    @ResponseBody
    @RequestMapping(value = "/copyRoleOperate", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter copyRoleOperate(HttpServletRequest req, HttpServletResponse rsp,
                                            @ApiParam(value = "被复制角色Id",required = true)  @RequestParam(required = true) String id,
                                            @ApiParam(value = "新角色名称",required = true)@RequestParam(required = true) String roleName,
                                            @ApiParam(value = "新角色描述",required = false)  @RequestParam(required = false)  String content)
            throws Exception {
        try {
            // 当前登录用户的Id
            String loginUserId = (String) req.getAttribute("loginUserId");
            Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);





            Role newRole = new Role();
            String newRoleId = UUID.randomUUID().toString();
            newRole.setId(newRoleId);
            newRole.setContent(content);
            newRole.setStatus(0);
            newRole.setCreateDate(DateUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            newRole.setCreateBy(loginUserId);
            newRole.setUpdateBy(loginUserId);
            newRole.setUpdateDate(new java.sql.Date(new Date().getTime()));
            newRole.setRoleName(roleName);
            //设置客户公司Id
            newRole.setPcCompanyId((String)loginUserMap.get("pcCompanyId"));
            roleService.saveRole(newRole);

            sysRoleOperateService.copyOperatesToNewRole(newRoleId, id);


        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }


    /**
     * 添加用户到某一角色
     *
     * @param req
     * @param rsp
     * @param userIds 用户ID组成的json数据  ["1","2","3"]
     * @param roleId 角色Id
     * @return
     * @throws Exception
     */
    @ApiOperation(value ="批量添加用户到某一角色")
    @ResponseBody
    @RequestMapping(value = "/addUsersUnderRole", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter addUsersUnderRole(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value = "用户Id组成 的JSon格式数组 如  [\"1\",\"2\"]",required = true) @RequestParam(required = true)  String userIds,
                                              @ApiParam(value = "角色Id",required = true) @RequestParam(required = true) String roleId)
            throws Exception {
        try {
            List<String> list = JSON.parseObject(userIds, new TypeReference<List<String>>() {
            });

            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", list.get(i));
                map.put("roleId", roleId);
                userService.updateDcUser(map);
            }

            OperateCacheUtil.clear();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }

    /**
     * 删除角色下的用户
     *
     * @param req
     * @param rsp
     * @param userIds 用户ID组成 的json数组 ["1","2"]
     * @param roleId  角色ID
     * @return
     * @throws Exception
     */
    @ApiOperation(value ="从某一用户下批量删除角色")
    @ResponseBody
    @RequestMapping(value = "/deleteUsersUnderRole", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter deleteUsersUnderRole(HttpServletRequest req, HttpServletResponse rsp,
                                                 @ApiParam(value = "用户ID组成 的JSON数组如 [\"1\",\"2\"]",required = true) @RequestParam(required = true)  String userIds,
                                                 @ApiParam(value = "角色Id" ,required = true) @RequestParam(required = true) String roleId)
            throws Exception {
        try {
            List<String> list = JSON.parseObject(userIds, new TypeReference<List<String>>() {
            });

            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", list.get(i));
                map.put("roleId", "");
                userService.updateDcUser(map);
            }

            OperateCacheUtil.clear();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }


    /**
     * 查询某一个角色下的用户列表
     *
     * @param req
     * @param rsp
     * @param userName    模糊搜索 用户名称
     * @param roleId      角色ID
     * @param currentPage 当前页
     * @param pageSize    每页显示多少条记录
     * @return
     * @throws Exception
     */
    @ApiOperation(value ="分页检索某一个角色的用户")
    @ResponseBody
    @RequestMapping(value = "/selectUsersUnderRole", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter selectUsersUnderRole(HttpServletRequest req, HttpServletResponse rsp,
                                                 @ApiParam(value = "用户姓名",required = false) @RequestParam(required = false) String userName,
                                                @ApiParam(value = "角色Id" ,required = true) @RequestParam(required = true) String roleId,
                                                 @ApiParam(value = "当前页" ,required = true) @RequestParam(required = true) Integer currentPage,
                                                 @ApiParam(value = "每页显示多少条" ,required = true) @RequestParam(required = true) Integer pageSize)
            throws Exception {
        List<Map<String, Object>> list = null;
        Integer total = 0;
        try {


            total = userService.selectTotalUsersCountUnderRole(roleId, userName);
            list = userService.selectUsersUnderRole(roleId, userName, currentPage, pageSize);


        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(list);
        responseDbCenter.setTotalRows(total + "");
        return responseDbCenter;
    }

    /**
     * 查询不在某一个角色下的用户
     *
     * @param req
     * @param rsp
     * @param userName     模糊搜索  用户姓名
     * @param departmentId 部门Id
     * @param roleId       角色Id
     * @param currentPage  当前页
     * @param pageSize     每页显示多少条记录
     * @return
     * @throws Exception
     */
    @ApiOperation(value ="分页检索不在某一个角色下的用户")
    @ResponseBody
    @RequestMapping(value = "/selectUsersNotUnderRole", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter selectUsersNotUnderRole(HttpServletRequest req, HttpServletResponse rsp,
                                                    @ApiParam(value = "用户姓名",required = false)  @RequestParam(required = false) String userName,
                                                    @ApiParam(value = "部门Id",required = false) @RequestParam(required = false) String departmentId,
                                                    @ApiParam(value = "角色Id",required = true) @RequestParam(required = true) String roleId,
                                                    @ApiParam(value = "当前页",required = true) @RequestParam(required = true) Integer currentPage,
                                                    @ApiParam(value = "每页多少记录",required = true)@RequestParam(required = true) Integer pageSize)
            throws Exception {
        List<Map<String, Object>> list = null;
        Integer total = 0;
        try {
            // 当前登录用户的Id
            String loginUserId = (String) req.getAttribute("loginUserId");
            total = userService.selectTotalUsersCountNotUnderRole(roleId, userName, departmentId,loginUserId);
            list = userService.selectUsersNotUnderRole(roleId, userName, currentPage, pageSize, departmentId,loginUserId);


        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(list);
        responseDbCenter.setTotalRows(total + "");
        return responseDbCenter;
    }

    /**
     * 删除角色
     *
     * @param req
     * @param rsp
     * @param id  角色Id
     * @return
     * @throws Exception
     */
    @ApiOperation(value ="删除角色")
    @ResponseBody
    @RequestMapping(value = "/deleteRole", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter deleteRole(HttpServletRequest req, HttpServletResponse rsp,
                                      @ApiParam(value = "角色Id",required = true) @RequestParam(required = true) String id)
            throws Exception {
        try {
            // 当前登录用户的Id
            String loginUserId = (String) req.getAttribute("loginUserId");
            Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);
            if (!"1".equals("roleId")){//如果不是超级管理员。则需要判断要删除的角色是否是公司的管理员角色
                Map<String,Object> role= roleService.selectRoleById(id);
                if (1==(Integer)role.get("isAdmin")){
                    return ResponseConstants.ADMIN_ROLE_CANNOT_DELETE;
                }
            }

            //删除角色
            roleService.deleteRole(id);
            //删除角色下的权限
            sysRoleOperateService.deleteByRoleId(id);

            //清除旧有权限，便于在拦截器中重新加载
            OperateCacheUtil.clear();

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }

    /**
     * 更新角色
     *
     * @param req
     * @param rsp
     * @param roleId   角色Id
     * @param roleName 角色名称
     * @param content  角色描述
     * @return
     * @throws Exception
     */
    @ApiOperation(value ="更新角色")
    @ResponseBody
    @RequestMapping(value = "/updateRole", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter updateRole(HttpServletRequest req, HttpServletResponse rsp,
                                       @ApiParam(value = "角色Id",required = true) @RequestParam(required = true) String roleId,
                                       @ApiParam(value = "角色名称",required = true)@RequestParam(required = true) String roleName,
                                       @ApiParam(value = "角色描述",required = false) @RequestParam(required = false) String content)
            throws Exception {
        try {
            // 当前登录用户的Id
            String loginUserId = (String) req.getAttribute("loginUserId");

//            /**
//             * 判断库中是否有同名角色，有的话不能修改
//             */
//            List<Map<String, Object>> existList = roleService.selectRoleByName(roleName);
//            if (existList.size() > 0) {
//                if (!existList.get(0).get("id").equals(roleId)) {
//                    return ResponseConstants.ROLE_EXISTS;
//                }
//            }


            /**
             * 超级管理员不能修改
             */
            if ("1".equals(roleId)) {
                return ResponseConstants.SUPER_ROLE_ERROR;
            }

            Map<String, Object> roleMap = new HashMap<>();
            roleMap.put("roleId", roleId);
            roleMap.put("roleName", roleName);
            roleMap.put("content", content);
            roleMap.put("status", 0);
            roleMap.put("updateBy", loginUserId);
            roleService.updateRole(roleMap);
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }


    /**
     * 更新角色下的权限
     *
     * @param req
     * @param rsp
     * @param operateIds 新的权限ID组成的json数组 ["1","2"]
     * @param roleId     角色Id
     * @return
     * @throws Exception
     */
    @ApiOperation(value ="更新角色对应的权限")
    @ResponseBody
    @RequestMapping(value = "/updateRoleOperate", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseDbCenter updateRoleOperate(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value = "权限Ids,权限列表组成的JSON数组，如[\"1\",\"2\"],如为空则传入 []",required = true)  @RequestParam(required = true)  String operateIds,
                                              @ApiParam(value = "角色Id",required = true) @RequestParam(required = true) String roleId)
            throws Exception {
        try {
            if ("1".equals(roleId)) {
                return ResponseConstants.SUPER_ROLE_ERROR;
            }

            // 当前登录用户的Id
            String loginUserId = (String) req.getAttribute("loginUserId");
            Map<String, Object> roleMap = new HashMap<>();
            roleMap.put("roleId", roleId);
            roleMap.put("updateBy", loginUserId);
            roleService.updateRole(roleMap);

            //删除旧的角色权限关系
            sysRoleOperateService.deleteByRoleId(roleId);

            if (!StringUtils.isBlank(operateIds)) {

                List<String> list = JSON.parseObject(operateIds, new TypeReference<List<String>>() {
                });


                List<Map<String, Object>> mapList = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", UUID.randomUUID().toString());
                    map.put("roleId", roleId);
                    map.put("operateId", list.get(i));
                    mapList.add(map);
                }
                if (list.size() > 0) {
                    //插入新的角色权限关系
                    sysRoleOperateService.insertList(mapList);
                }

            }

            //清除旧有权限，便于在拦截器中重新加载
            OperateCacheUtil.clear();

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }


}
