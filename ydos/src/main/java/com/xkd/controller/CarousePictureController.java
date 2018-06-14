package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.CarouseService;
import com.xkd.service.CompanyContactorService;
import com.xkd.service.UserService;
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

/**
 * 创建人 巫建辉
 * 轮播图相关业务
 */

@Api(description = "轮播图")
@Controller
@RequestMapping("/carouse")
@Transactional
public class CarousePictureController {

    @Autowired
    CarouseService carouseService;
    @Autowired
    UserService userService;

    @Autowired
    CompanyContactorService companyContactorService;

    @ApiOperation(value = "服务端添加轮播图")
    @RequestMapping(value = "/addCarouse", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseDbCenter addCarouse(HttpServletRequest req, HttpServletResponse rsp,
                                       @ApiParam(value = "urls，多个以逗号分隔  ", required = true) @RequestParam(required = true) String urls) throws Exception {
        try {
            // 当前登录用户的Id
            String loginUserId = (String) req.getAttribute("loginUserId");
            Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
            String[] strs = urls.split(",");
            /*
              先删除某一个pcCompanyId下的轮播图，再插入新的轮播图列表
             */
            carouseService.deleteByTType(4, (String) loginUserMap.get("pcCompanyId"));
            for (int i = 0; i < strs.length; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", UUID.randomUUID().toString());
                map.put("url", strs[i]);
                map.put("ttype", 4);
                map.put("pcCompanyId", loginUserMap.get("pcCompanyId"));
                carouseService.insert(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }

        ResponseDbCenter responseDbCenter = new ResponseDbCenter();

        return responseDbCenter;
    }


    @ApiOperation(value = "查询各端的轮播图----服务端")
    @RequestMapping(value = "/selectCarouse", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseDbCenter selectCarouse(HttpServletRequest req, HttpServletResponse rsp) throws Exception {
        try {
            String loginUserId = (String) req.getAttribute("loginUserId");
            Map<String, Object> loginUserMap = userService.selectUserById(loginUserId);
            List<String> pcCompanyIdList = new ArrayList<>();
            pcCompanyIdList.add((String) loginUserMap.get("pcCompanyId"));
            List<Map<String, Object>> list = carouseService.selectByTType(4, pcCompanyIdList);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            responseDbCenter.setResModel(list);
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }


    }


    @ApiOperation(value = "查询轮播图----客户端")
    @RequestMapping(value = "/selectCarouseFromMobile", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseDbCenter selectCarouseFromMobile(HttpServletRequest req, HttpServletResponse rsp) throws Exception {
        try {
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();

            String loginUserId = (String) req.getAttribute("loginUserId");
            List<String> pcCompanyIdList = companyContactorService.selectPcCompanyIdListByContactor(loginUserId);
            if (pcCompanyIdList.size() > 0) {
                List<Map<String, Object>> list = carouseService.selectByTType(4, pcCompanyIdList);
                responseDbCenter.setResModel(list);

            }
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }


    }


    @ApiOperation(value = "删除某一张轮播图")
    @RequestMapping(value = "/deleteCarouse", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseDbCenter deleteCarouse(HttpServletRequest req, HttpServletResponse rsp,
                                          @ApiParam(value = " id  ", required = true) @RequestParam(required = true) String id) throws Exception {
        try {

            carouseService.deleteById(id);
            ResponseDbCenter responseDbCenter = new ResponseDbCenter();
            return responseDbCenter;

        } catch (Exception e) {
            e.printStackTrace();
            throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
        }


    }
}
