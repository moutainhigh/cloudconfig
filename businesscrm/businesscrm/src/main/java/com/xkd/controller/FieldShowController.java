package com.xkd.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.CompanyService;
import com.xkd.service.FieldShowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2018/4/17.
 */
@Api(description = "查询，配置列表显示哪些列")
@Controller
@RequestMapping("/fieldShow")
@Transactional
public class FieldShowController extends BaseController {

    @Autowired
    FieldShowService fieldShowService;

    @ApiOperation(value = "查询所有客户显示字段")
    @ResponseBody
    @RequestMapping(value = "/selectFieldShow",method =  {RequestMethod.POST,RequestMethod.GET})
    public ResponseDbCenter selectFieldShow(HttpServletRequest req, HttpServletResponse rsp ,
                                            @ApiParam(value = "模块名称   company 客户   project 项目",required = true) @RequestParam(required = true) String module

    ) throws Exception {


        List<Map<String,Object>> fieldShowList=fieldShowService.selectFieldShow(module);
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(fieldShowList);
        return responseDbCenter;
    }




    @ApiOperation(value = "查询用户客户显示字段")
    @ResponseBody
    @RequestMapping(value = "/selectUserFieldShow",method =  {RequestMethod.POST,RequestMethod.GET})
    public ResponseDbCenter selectUserFieldShow(HttpServletRequest req, HttpServletResponse rsp,
                                                @ApiParam(value = "模块名称   company 客户   project 项目",required = true) @RequestParam(required = true) String module
    ) throws Exception {

        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        List<Map<String,Object>>  fieldShowList=fieldShowService.selectUserFieldShow(loginUserId,module);
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        responseDbCenter.setResModel(fieldShowList);
        return responseDbCenter;
    }


    @ApiOperation(value = "设置用户显示字段配置")
    @ResponseBody
    @RequestMapping(value = "/updateFieldUserShow",method =  {RequestMethod.POST})
    public ResponseDbCenter updateFieldUserShow(HttpServletRequest req, HttpServletResponse rsp,@ApiParam(value = "字段数组 格式如：" +
            "{\n" +
            "\t\"module\": \"company\",\n" +
            "\t\"conf\": [{\n" +
            "\t\t\"field\": \"companyName\",\n" +
            "\t\t\"orderNum\": 1\n" +
            "\t}, {\n" +
            "\t\t\"field\": \"companyAdviserName\",\n" +
            "\t\t\"orderNum\": 2\n" +
            "\t}]\n" +
            "}")   @RequestBody String fieldConfStr

    ) throws Exception {


        Map<String,Object> map= JSON.parseObject(fieldConfStr, new TypeReference<Map<String, Object>>() {
        });
        List<Map<String,Object>> list= (List<Map<String, Object>>) map.get("conf");


        // 当前登录用户的Id
        String loginUserId = (String) req.getAttribute("loginUserId");
        fieldShowService.updateFieldUserShow(loginUserId,list, (String) map.get("module"));
        ResponseDbCenter responseDbCenter = new ResponseDbCenter();
        return responseDbCenter;
    }
}
