package com.wjh.companydemo.controller;


import com.wjh.common.model.ResponseConstant;
import com.wjh.common.model.ResponseModel;
import com.wjh.common.model.ServiceIdConstant;
import com.wjh.companydemo.service.CompanyService;
import com.wjh.companydemomodel.model.CompanyAddDto;
import com.wjh.companydemomodel.model.CompanyPo;
import com.wjh.companydemomodel.model.CompanyUpdateDto;
import com.wjh.companydemomodel.model.CompanyVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(description = "公司相关接口")
@RefreshScope
@RestController
@RequestMapping("/company")
public class CompanyController {
    Logger logger = LogManager.getLogger();

    @Autowired
    CompanyService companyService;

    @ApiOperation(value = "查询公司")
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public ResponseModel select() {


        try {
            List<CompanyVo> comopanyList = companyService.select();
            ResponseModel responseModel = new ResponseModel();
            responseModel.setResModel(comopanyList);
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.companydemo, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "插入公司")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public ResponseModel insert(@ApiParam(value = "公司信息", required = true) @RequestBody(required = true) CompanyAddDto companyAddDto,
                                HttpServletRequest httpServletRequest) {


        try {
            String loginUserId=httpServletRequest.getHeader("loginUserId");
            CompanyPo companyPo = new CompanyPo();
            BeanUtils.copyProperties(companyPo, companyAddDto);
            companyService.insert(companyPo,Long.valueOf(loginUserId));
            ResponseModel responseModel = new ResponseModel();
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.companydemo, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }


    @ApiOperation(value = "更新公司")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseModel update(@ApiParam(value = "公司信息", required = true) @RequestBody(required = true) CompanyUpdateDto companyUpdateDto,
                                HttpServletRequest httpServletRequest) {


        try {
            String loginUserId=httpServletRequest.getHeader("loginUserId");
            CompanyPo companyPo = new CompanyPo();
            BeanUtils.copyProperties(companyPo, companyUpdateDto);
            companyService.update(companyPo,Long.valueOf(loginUserId));
            ResponseModel responseModel = new ResponseModel();
            return responseModel;
        } catch (Exception e) {
            logger.error(ServiceIdConstant.companydemo, e);
            return ResponseConstant.SYSTEM_EXCEPTION;
        }

    }





}

