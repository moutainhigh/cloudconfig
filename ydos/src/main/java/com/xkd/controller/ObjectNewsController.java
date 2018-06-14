package com.xkd.controller;


import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.ObjectNewsService;
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

@Api(description = "报修维修、设备消息功能接口")
@Controller
@RequestMapping("/YDobjectNews")
@Transactional
public class ObjectNewsController {


    @Autowired
    private ObjectNewsService objectNewsService;

    @ApiOperation(value = "统计用户未读信息")
    @ResponseBody
    @RequestMapping(value = "/getNoReadNewsCount",method = RequestMethod.GET)
    public ResponseDbCenter getNoReadNewsCount(HttpServletRequest req, HttpServletResponse rsp){

        String loginUserId = (String)req.getAttribute("loginUserId");

        try {

            Integer num = objectNewsService.getNoReadNewsCount(loginUserId);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(num.toString());

            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

    @ApiOperation(value = "全部已读")
    @ResponseBody
    @RequestMapping(value = "/updateAllNewsRead",method = RequestMethod.GET)
    public ResponseDbCenter updateAllNewsRead(HttpServletRequest req, HttpServletResponse rsp){

        String loginUserId = (String)req.getAttribute("loginUserId");

        try {

            Integer num = objectNewsService.updateAllNewsRead(loginUserId);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(num.toString());

            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

    @ApiOperation(value = "根据ID标记已读")
    @ResponseBody
    @RequestMapping(value = "/updateReadById",method = RequestMethod.GET)
    public ResponseDbCenter updateReadById(HttpServletRequest req, HttpServletResponse rsp,
                                              @ApiParam(value="消息ID",required = false) @RequestParam(required = false) String newsId){


        try {

            Integer num = objectNewsService.updateReadById(newsId);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(num.toString());

            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }



    @ApiOperation(value = "处理消息逻辑")
    @ResponseBody
    @RequestMapping(value = "/newsCallBack",method = RequestMethod.GET)
    public ResponseDbCenter allowMessageCallBack(HttpServletRequest req, HttpServletResponse rsp,
                                           @ApiParam(value="消息Id",required = false) @RequestParam(required = false) String id){
        try {
            objectNewsService.objectNewsCallBack(id);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }








    @ApiOperation(value = "查询推送记录---客户端")
    @ResponseBody
    @RequestMapping(value = "/selectCustomerPushNews",method = RequestMethod.POST)
    public ResponseDbCenter selectCustomerPushNews(HttpServletRequest req, HttpServletResponse rsp,
                                         @ApiParam(value="当前页",required = true) @RequestParam(required = false) Integer currentPage,
                                         @ApiParam(value="每页多少条",required = true) @RequestParam(required = false) Integer pageSize){
        try {
            String loginUserId = (String)req.getAttribute("loginUserId");
            List<Map<String,Object>> list=objectNewsService.selectCustomerPushNews(loginUserId,currentPage,pageSize);
            Integer count=objectNewsService.selectCustomerPushNewsCount(loginUserId);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setTotalRows(count+"");
            responseDbCenter.setResModel(list);
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }



    @ApiOperation(value = "查询推送记录---技师端")
    @ResponseBody
    @RequestMapping(value = "/selectTechnicanPushNews",method = RequestMethod.POST)
    public ResponseDbCenter selectTechnicanPushNews(HttpServletRequest req, HttpServletResponse rsp,
                                         @ApiParam(value="当前页",required = true) @RequestParam(required = false) Integer currentPage,
                                         @ApiParam(value="每页多少条",required = true) @RequestParam(required = false) Integer pageSize){
        try {
            String loginUserId = (String)req.getAttribute("loginUserId");
            List<Map<String,Object>> list=objectNewsService.selectTechnicanPushNews(loginUserId, currentPage, pageSize);
            Integer count=objectNewsService.selectTechnicanPushNewsCount(loginUserId);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setTotalRows(count+"");
            responseDbCenter.setResModel(list);
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


    @ApiOperation(value = "根据推送任务查询已推送的消息---服务端")
    @ResponseBody
    @RequestMapping(value = "/selectPushNewsByTaskId",method = RequestMethod.POST)
    public ResponseDbCenter selectPushNewsByTaskId(HttpServletRequest req, HttpServletResponse rsp,
                                                    @ApiParam(value="任务Id",required = true) @RequestParam(required = false) String taskId,
                                                    @ApiParam(value="当前页",required = true) @RequestParam(required = false) Integer currentPage,
                                                    @ApiParam(value="每页多少条",required = true) @RequestParam(required = false) Integer pageSize){
        try {
             List<Map<String,Object>> list=objectNewsService.selectPushNewsByTaskId(taskId, currentPage, pageSize);
            Integer count=objectNewsService.selectPushNewsByTaskIdCount(taskId);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setTotalRows(count+"");
            responseDbCenter.setResModel(list);
            return responseDbCenter;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }

    /**
     *
     * @param req
     * @param rsp
     * @return
     * id,userId,title,content,createDate,createdBy,flag,status
     */
    @ApiOperation(value = "根据用户ID查询维修推送信息")
    @ResponseBody
    @RequestMapping(value = "/selectNewsByUserId",method = RequestMethod.POST)
    public ResponseDbCenter selectNewsByUserId(HttpServletRequest req, HttpServletResponse rsp,
                                               @ApiParam(value="2：服务端，3：技师端，4：客户端",required = false) @RequestParam(required = false) String appFlag,
                                               @ApiParam(value="0维修保修消息、1设备消息 2 邀请消息 3 推送消息  4 合同消息  5巡检消息",required = false)
                                                   @RequestParam(required = false) String newsTypes,
                                               @ApiParam(value = "currentPage",required = false) @RequestParam(required = false) String currentPage,
                                               @ApiParam(value = "pageSize",required = false) @RequestParam(required = false) String pageSize
    ){


        if(StringUtils.isBlank(appFlag)){
            return ResponseConstants.MISSING_PARAMTER;
        }

        if(StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize)){
            currentPage = "1";
            pageSize = "10";
        }
        int  pageSizeInt = Integer.parseInt(pageSize);
        int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;



        if(StringUtils.isBlank(appFlag) || StringUtils.isBlank(newsTypes)){
            return ResponseConstants.MISSING_PARAMTER;
        }


        String[]  newsTypess = newsTypes.split(",");
        List<Integer> newsTypeList = new ArrayList<>();
        for(int i=0;i<newsTypess.length;i++){
            newsTypeList.add(new Integer(newsTypess[i]));
        }


        String loginUserId = (String) req.getAttribute("loginUserId");
        try {

            List<Map<String, Object>> repaireNews = objectNewsService.selectObjectNewsByUserId(loginUserId,appFlag,newsTypeList,pageSizeInt,currentPageInt);
            Integer num = objectNewsService.selectTotalObjectNewsByUserId(loginUserId,appFlag,newsTypeList);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(repaireNews);
            responseDbCenter.setTotalRows(num.intValue()+"");
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseConstants.FUNC_SERVERERROR;
        }
    }


}
