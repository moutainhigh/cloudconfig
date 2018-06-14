package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.ObjectNewsTaskService;
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
import java.util.*;

/**
 * Created by dell on 2018/3/7.
 */
@Api(description = "消息任务接口")
@Controller
@RequestMapping("/YDobjectNewsTask")
@Transactional
public class ObjectNewsTaskController {

    @Autowired
    ObjectNewsTaskService objectNewsTaskService;

    @Autowired
    UserService userService;

    @ApiOperation(value = "推送消息---服务商")
    @ResponseBody
    @RequestMapping(value = "/pushNews",method = RequestMethod.POST)
    public ResponseDbCenter pushNews(HttpServletRequest req, HttpServletResponse rsp,
                                     @ApiParam(value="推送对象类型  0 客户  1 员工   ",required = true) @RequestParam(required = true) Integer objectType,
                                     @ApiParam(value="推送对象Id   公司Id  ",required = false) @RequestParam(required = false) String objectId,
                                     @ApiParam(value="推送内容",required = true) @RequestParam(required = true) String content
    ){
        try {
            String loginUserId = (String)req.getAttribute("loginUserId");
//            String loginUserId = "818";
            Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);
            Map<String,Object> map=new HashMap<>();
            map.put("id", UUID.randomUUID().toString());
            map.put("objectId",objectId);
            map.put("content",content);
            map.put("status",0);
            map.put("objectType",objectType);
            map.put("pcCompanyId",loginUserMap.get("pcCompanyId"));
            map.put("createdBy",loginUserId);
            map.put("createDate",new Date());
            objectNewsTaskService.generateTask(map);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


    @ApiOperation(value = "查询推送任务---服务商")
    @ResponseBody
    @RequestMapping(value = "/listNewsTask",method = RequestMethod.POST)
    public ResponseDbCenter listNewsTask(HttpServletRequest req, HttpServletResponse rsp,
                                     @ApiParam(value="当前页",required = true) @RequestParam(required = false) Integer currentPage,
                                                 @ApiParam(value="每页多少条",required = true) @RequestParam(required = false) Integer pageSize
    ) throws Exception{
        try {
            String loginUserId = (String)req.getAttribute("loginUserId");
//            String loginUserId = "818";
            Map<String,Object> loginUserMap=userService.selectUserById(loginUserId);
            List list=objectNewsTaskService.selectList((String) loginUserMap.get("pcCompanyId"),currentPage,pageSize);
            Integer count=objectNewsTaskService.selectListCount((String) loginUserMap.get("pcCompanyId"));
             ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(list);
            responseDbCenter.setTotalRows(count+"");
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }




    @ApiOperation(value = "重新发送---服务商")
    @ResponseBody
    @RequestMapping(value = "/rePushNews",method = RequestMethod.POST)
    public ResponseDbCenter rePushNews(HttpServletRequest req, HttpServletResponse rsp,
                                         @ApiParam(value="任务Id",required = true) @RequestParam(required = false) String taskId
    ) throws Exception{
        try {
              String loginUserId = (String)req.getAttribute("loginUserId");
//            String loginUserId = "818";
            objectNewsTaskService.rePush(taskId);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
             return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }



    @ApiOperation(value = "删除消息任务-")
    @ResponseBody
    @RequestMapping(value = "/deleteNewsTask",method = RequestMethod.POST)
    public ResponseDbCenter deleteNewsTask(HttpServletRequest req, HttpServletResponse rsp,
                                       @ApiParam(value="任务Id",required = true) @RequestParam(required = false) String taskId
    ) throws Exception{
        try {
             Map<String,Object> map=new HashMap<>();
            map.put("id",taskId);
            map.put("status",2);
            objectNewsTaskService.update(map);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


}
