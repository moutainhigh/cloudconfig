package com.xkd.controller;

import com.xkd.exception.BoxIdInvalidException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.DeviceShareService;
import com.xkd.service.ObjectNewsService;
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
@Api(description = "分享设备")
@Controller
@RequestMapping("/deviceShare")
@Transactional
public class DeviceShareController {


    @Autowired
    DeviceShareService deviceShareService;
    @Autowired
    UserService userService;






    @ApiOperation(value = "根据手机号码查询要分享的角色为客户的人员信息-客户端")
    @ResponseBody
    @RequestMapping(value = "/selectUserToShareByMobile", method = RequestMethod.POST)
    public ResponseDbCenter selectUserToShareByMobile(HttpServletRequest req, HttpServletResponse rsp,
                                  @ApiParam(value = "手机号码   ", required = true) @RequestParam(required = true) String mobile


    ) throws Exception {
        try {
            Map<String,Object> userMap=  userService.selectUserByMobile(mobile,null);
            if (userMap==null){
                userMap=new HashMap<>();
            }
            if (!"4".equals(userMap.get("roleId"))){
                userMap=new HashMap<>();
            }
             ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(userMap);
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }




    @ApiOperation(value = "分享-手机 客户端")
    @ResponseBody
    @RequestMapping(value = "/share", method = RequestMethod.POST)
    public ResponseDbCenter share(HttpServletRequest req, HttpServletResponse rsp,
                                  @ApiParam(value = "被分享者Id 多个值用逗号分隔", required = true) @RequestParam(required = true) String sharee,
                                  @ApiParam(value = "是否允许控制设备  0  允许   1 不允许 ", required = true) @RequestParam(required = true) Integer control,
                                  @ApiParam(value = "发送告警级别  0 全部  1 紧急   2 全部以上 3 不接收  ", required = true) @RequestParam(required = true) Integer warningLevel

    ) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");

            List<Map<String,Object>> sharL=deviceShareService.selectBySharerAndSharee(sharee,loginUserId);
            if (sharL.size()>0){
                return  new ResponseDbCenter();
            }
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("id", UUID.randomUUID().toString());
            map.put("sharer", loginUserId);
            map.put("sharee", sharee);
            map.put("control", control);
            map.put("warningLevel", warningLevel);
            list.add(map);

            deviceShareService.insertList(list);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;
        }catch (BoxIdInvalidException be){
            be.printStackTrace();
            throw be;
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }







    @ApiOperation(value = "删除分享---手机客户端")
    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseDbCenter delete(HttpServletRequest req, HttpServletResponse rsp,
                                  @ApiParam(value = "被分享者Ids 多个值用逗号分隔", required = true) @RequestParam(required = true) String idList

    ) throws Exception {
        try {
            String[] strs = idList.split(",");
            List<String> list = new ArrayList<>();
            for (int i = 0; i < strs.length; i++) {

                 list.add(strs[i]);
            }
            deviceShareService.delete(list);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;
        }catch (BoxIdInvalidException be){
            be.printStackTrace();
            throw be;
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


    @ApiOperation(value = "查看分享---手机客户端")
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseDbCenter list(HttpServletRequest req, HttpServletResponse rsp

    ) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");
           List<Map<String,Object>> mapList= deviceShareService.selectShareListBySharer(loginUserId);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(mapList);
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


}
