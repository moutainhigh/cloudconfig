package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.mapper.UserMapper;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.*;
import com.xkd.utils.PropertiesUtil;
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
import java.util.*;

/**
 * Created by dell on 2018/2/8.
 */

@Api(description = "班组管理")
@Controller
@RequestMapping("/team")
@Transactional
public class YdTeamController extends BaseController{


    @Autowired
    DepartmentService departmentService;
    @Autowired
    UserService userService;

    @Autowired
    ObjectNewsService objectNewsService;


    @Autowired
    AllowHistoryService allowHistoryService;

    @Autowired
    InspectionTaskService inspectionTaskService;


    @ApiOperation(value = "查询班组")
    @ResponseBody
    @RequestMapping(value = "/searchTeam", method = {  RequestMethod.POST})
    public ResponseDbCenter searchTeam(HttpServletRequest req, HttpServletResponse rsp,
                                    @ApiParam(value = "班组名称", required = false) @RequestParam(required = false) String departmentName,
                                       @ApiParam(value = "当前页", required = true) @RequestParam(required = true) Integer currentPage,
                                       @ApiParam(value = "每页多少数", required = true) @RequestParam(required = true) Integer pageSize
    ) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);

        try {
            Map<String,Object>  map=new HashMap<>();
            List<Map<String,Object>>  list=null;
            Integer totalRows=0;

                list=departmentService.searchTeamByPcCompanyId((String)loginUserMap.get("pcCompanyId"),departmentName,currentPage,pageSize);
                totalRows=departmentService.searchTeamCountByPcCompanyId((String)loginUserMap.get("pcCompanyId"),departmentName);



            ResponseDbCenter responseDbCenter= new ResponseDbCenter();
            responseDbCenter.setResModel(list);
            responseDbCenter.setTotalRows(totalRows+"");
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "添加班组")
    @ResponseBody
    @RequestMapping(value = "/addTeam", method = {  RequestMethod.POST})
    public ResponseDbCenter addTeam(HttpServletRequest req, HttpServletResponse rsp,
                                            @ApiParam(value = "班组名称", required = true) @RequestParam(required = true) String departmentName
    ) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);

        try {

            Map<String,Object> teamInDb=departmentService.selectTeamByNameAndPcCompanyId(departmentName, (String) loginUserMap.get("pcCompanyId"));
            if (teamInDb!=null){
                return  ResponseConstants.TEAM_ALREADY_EXISTS;
            }


            Map<String,Object>  map=new HashMap<>();
            map.put("parentId",loginUserMap.get("pcCompanyId"));
            map.put("id", UUID.randomUUID().toString());
            map.put("isTeam",1);
            map.put("departmentName",departmentName);
            map.put("createdBy",loginUserId);
             map.put("createDate",new Date());
            departmentService.insert(map);
            return new ResponseDbCenter();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }



    @ApiOperation(value = "修改班组")
    @ResponseBody
    @RequestMapping(value = "/updateTeam", method = {  RequestMethod.POST})
    public ResponseDbCenter updateTeam(HttpServletRequest req, HttpServletResponse rsp,
                                       @ApiParam(value = "id", required = true) @RequestParam(required = true) String id,
                                    @ApiParam(value = "班组名称", required = true) @RequestParam(required = true) String departmentName
    ) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);


        try {
            Map<String,Object> teamInDb=departmentService.selectTeamByNameAndPcCompanyId(departmentName, (String) loginUserMap.get("pcCompanyId"));
            if (teamInDb!=null){
                if (!teamInDb.get("id").equals(id)) {
                    return ResponseConstants.TEAM_ALREADY_EXISTS;
                }
            }

            Map<String,Object>  map=new HashMap<>();
             map.put("id",id);
             map.put("departmentName",departmentName);
            map.put("updatedBy",loginUserId);
            map.put("updateDate",new Date());
            departmentService.update(map);
            return new ResponseDbCenter();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "删除班组")
    @ResponseBody
    @RequestMapping(value = "/deleteTeam", method = {  RequestMethod.POST})
    public ResponseDbCenter deleteTeam(HttpServletRequest req, HttpServletResponse rsp,
                                       @ApiParam(value = "班组Id", required = true) @RequestParam(required = true) String id
     ) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");
        try {
            Map<String,Object>  map=new HashMap<>();
            /**
             * 如果该班组下面有员工，则不允许删除
             */
            List<Map<String ,Object>> mapList=  userService.selecUserByDepartmentId(id, null, 10000, 1, loginUserId);
            if (mapList.size()>0){
                return ResponseConstants.DEPARTMENT_HAS_USER;
            }
            map.put("id",id);
            map.put("status",2);
             map.put("updatedBy",loginUserId);
            map.put("updateDate",new Date());
            departmentService.update(map);
            return new ResponseDbCenter();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }



    @ApiOperation(value = "设置组长")
    @ResponseBody
    @RequestMapping(value = "/setTeamLeader", method = {  RequestMethod.POST})
    public ResponseDbCenter setTeamLeader(HttpServletRequest req, HttpServletResponse rsp,
                                       @ApiParam(value = "用户Id", required = true) @RequestParam(required = true) String id,
                                                      @ApiParam(value = "状态 1 设置为组长  0 取消设置为组长", required = true) @RequestParam(required = true) Integer status

    ) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");
        try {
            Map<String,Object> userMap=userService.selectUserById(id);
            Map<String,Object>  depMap=new HashMap<>();
            depMap.put("id",userMap.get("departmentId"));
            if (0==status){
                depMap.put("principalId","");
            }else {
                depMap.put("principalId",id);
            }
             depMap.put("updatedBy",loginUserId);
            depMap.put("updateDate",new Date());
            departmentService.update(depMap);
            return new ResponseDbCenter();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }


    @ApiOperation(value = "查询组员")
    @ResponseBody
    @RequestMapping(value = "/searchTeamers", method = {  RequestMethod.POST})
    public ResponseDbCenter searchTeamers(HttpServletRequest req, HttpServletResponse rsp,
                                          @ApiParam(value = "组员名称", required = false) @RequestParam(required = false) String uname,
                                          @ApiParam(value = "级别 0 技师   1 组长  不传是为全部 ", required = false) @RequestParam(required = false) Integer level,
                                           @ApiParam(value = "当前页", required = true) @RequestParam(required = true) Integer currentPage,
                                          @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize



    ) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");
        Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);

        try {
            List<Map<String,Object>> list=null;
            int totalRows=0;

                list=userService.selectStaff((String) loginUserMap.get("pcCompanyId"), uname, level,null, currentPage, pageSize);
                totalRows=userService.selectStaffCount((String) loginUserMap.get("pcCompanyId"), uname, level,null);

            ResponseDbCenter responseDbCenter= new ResponseDbCenter();
            responseDbCenter.setResModel(list);
            responseDbCenter.setTotalRows(totalRows+"");
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }





    @ApiOperation(value = "添加班组成员---服务端")
    @ResponseBody
    @RequestMapping(value = "/addStaffToTeam", method = {  RequestMethod.POST})
    public ResponseDbCenter addStaffToTeam(HttpServletRequest req, HttpServletResponse rsp,
                                          @ApiParam(value = "班组ID  ", required = false) @RequestParam(required = false) String departmentId,
                                          @ApiParam(value = "人员ID  ", required = false) @RequestParam(required = false) String userId



    ) throws Exception {

        try {
            Map<String,Object> map=new HashMap<>();
            map.put("id",userId);
            map.put("departmentId",departmentId);
            userService.updateDcUser(map);
            ResponseDbCenter responseDbCenter= new ResponseDbCenter();

            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }






    @ApiOperation(value = "删除班组成员---服务端")
    @ResponseBody
    @RequestMapping(value = "/deleteStaffFromTeam", method = {  RequestMethod.POST})
    public ResponseDbCenter deleteStaffFromTeam(HttpServletRequest req, HttpServletResponse rsp,
                                            @ApiParam(value = "人员ID  ", required = false) @RequestParam(required = false) String userId



    ) throws Exception {

        try {
            Map<String,Object> map=new HashMap<>();
            map.put("id",userId);
            map.put("departmentId","");
            Map<String,Object> userMap=userService.selectUserById(userId);
            Map<String,Object> departmentMap=departmentService.selectDepartmentById((String)userMap.get("departmentId"));
            if (userId.equals(departmentMap.get("principalId"))){  //如果删除的员工为组长的话
                Map<String,Object> depMap=new HashMap<>();
                depMap.put("id",(String)userMap.get("departmentId"));
                depMap.put("principalId","");
                departmentService.update(depMap);
            }
            userService.updateDcUser(map);

            ResponseDbCenter responseDbCenter= new ResponseDbCenter();

            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }





    @ApiOperation(value = "邀请班组人员")
    @ResponseBody
    @RequestMapping(value = "/allowTechnician", method = {RequestMethod.POST})
    public ResponseDbCenter allowTechnician(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value = "被邀请技师Id", required = true) @RequestParam(required = true) String userId
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
            map.put("flag",1);
            map.put("createdBy",loginUserId);
            map.put("createDate",new Date());
            allowHistoryService.insert(map);

            Map<String,Object>    newsMap=new HashMap<>();
            newsMap.put("id",UUID.randomUUID().toString());
            newsMap.put("objectId",objectId);
            newsMap.put("appFlag",3);
            newsMap.put("userId",userId);
            newsMap.put("title","邀请消息");
            newsMap.put("content",loginUserMap.get("uname")+"邀请您加入"+loginUserMap.get("departmentName"));
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




    @ApiOperation(value = "查询名单--服务端")
    @ResponseBody
    @RequestMapping(value = "/searchStaff", method = {  RequestMethod.POST})
    public ResponseDbCenter searchStaff(HttpServletRequest req, HttpServletResponse rsp,
                                          @ApiParam(value = "组员名称", required = false) @RequestParam(required = false) String uname,
                                        @ApiParam(value = "班组Id", required = false) @RequestParam(required = false) String departmentId,
                                        @ApiParam(value = "级别 0 技师   1 组长  不传是为全部 ", required = false) @RequestParam(required = false) Integer level,
                                          @ApiParam(value = "当前页", required = true) @RequestParam(required = true) Integer currentPage,
                                          @ApiParam(value = "每页多少条", required = true) @RequestParam(required = true) Integer pageSize



    ) throws Exception {
        String loginUserId = (String) req.getAttribute("loginUserId");
//        String loginUserId = "818";
        Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);

        try {
            List<Map<String,Object>> list=null;
            int totalRows=0;

                list=userService.selectStaff((String)loginUserMap.get("pcCompanyId"),uname,level,departmentId,currentPage,pageSize);
                totalRows=userService.selectStaffCount((String)loginUserMap.get("pcCompanyId"),uname,level,departmentId);

            ResponseDbCenter responseDbCenter= new ResponseDbCenter();
            responseDbCenter.setResModel(list);
            responseDbCenter.setTotalRows(totalRows+"");
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }














    @ApiOperation(value = "删除名单")
    @ResponseBody
    @RequestMapping(value = "/deleteStaff", method = {  RequestMethod.POST})
    public ResponseDbCenter deleteStaff(HttpServletRequest req, HttpServletResponse rsp,
                                        @ApiParam(value = "要删除的用户Id", required = false) @RequestParam(required = false) String id



    ) throws Exception {
         try {
             userService.deleteStaff(id);
            ResponseDbCenter responseDbCenter= new ResponseDbCenter();
             return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }




    @ApiOperation(value = "获取某一个员工的历史巡检，维修记录")
    @ResponseBody
    @RequestMapping(value = "/selectHistoryInspectionAndRepaireByUserId", method = {  RequestMethod.POST})
    public ResponseDbCenter selectHistoryInspectionAndRepaireByUserId(HttpServletRequest req, HttpServletResponse rsp,
                                        @ApiParam(value = "用户Id", required = true) @RequestParam(required = true) String userId,
                                        @ApiParam(value = "时间起  格式2012-10-10 10:10:10", required = false) @RequestParam(required = false) String fromDate,
                                         @ApiParam(value = "时间止  格式2012-10-10 10:10:10", required = false) @RequestParam(required = false) String toDate,
                                         @ApiParam(value = "类型  1 维修  2 巡检  不传表示全部", required = false) @RequestParam(required = false) Integer ttype,
                                         @ApiParam(value = "当前页", required = false) @RequestParam(required = false) Integer currentPage,
                                         @ApiParam(value = "每页数量", required = false) @RequestParam(required = false) Integer pageSize



    ) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");
            Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);
            List<Map<String,Object>> list= inspectionTaskService.selectHistoryInspectionAndRepaireByUserId((String)loginUserMap.get("pcCompanyId"),userId,fromDate,toDate,ttype,currentPage,pageSize);
            Integer count=inspectionTaskService.selectHistoryInspectionAndRepaireCountByUserId((String)loginUserMap.get("pcCompanyId"),userId,fromDate,toDate,ttype);
            ResponseDbCenter responseDbCenter= new ResponseDbCenter();
            responseDbCenter.setResModel(list);
            responseDbCenter.setTotalRows(count+"");
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }
    }









}
