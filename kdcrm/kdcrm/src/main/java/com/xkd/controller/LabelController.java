package com.xkd.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xkd.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.xkd.exception.GlobalException;
import com.xkd.model.Label;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.model.User;

@Api(value = "企业标签相关接口")
@Controller
@RequestMapping("label")
@Transactional
public class LabelController  extends BaseController{

	@Autowired
	private LabelService laberService;
	
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	UserDynamicService userDynamicService;
	@Autowired
	CompanyService companyService;
	@Autowired
	SolrService solrService;

	/**
	 * 
	 * @author: xiaoz
	 * @2017年5月11日 
	 * @功能描述:通过企业ID查询标签信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ApiOperation(value = "查询公司标签")
	@ResponseBody
	@RequestMapping(value = "/selectLabelsByCompanyId",method = {RequestMethod.GET,RequestMethod.POST})
	public ResponseDbCenter selectLabelsByCompanyId(HttpServletRequest req,HttpServletResponse rsp,
													@ApiParam(value = "公司Id",required = true)  @RequestParam(required = true) String companyId){
	

		if(StringUtils.isBlank(companyId)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		List<Label> labels = null;
		
		try {
			
			labels = laberService.selectLabelsByCompanyId(companyId);
	
		} catch (Exception e) {
			
			System.out.println(e);
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(labels);
		
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年5月11日 
	 * @功能描述:插入更新企业标签信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ApiOperation(value = "修改公司资源信息")
	@ResponseBody
	@RequestMapping(value = "/changeLabel",method = {RequestMethod.GET,RequestMethod.POST})
	public ResponseDbCenter changeLabel(HttpServletRequest req,HttpServletResponse rsp,@ApiParam(value = "{\n" +
			"    \"oweResource\": {\n" +
			"        \"companyId\": \"a966ee21-84e7-4e76-97c5-efff6c38b503\",\n" +
			"        \"userId\": \"a966ee21-84e7-4e76-97c5-efff6c38b503\",\n" +
			"        \"ttype\": 1,\n" +
			"        \"label\": \"钱 产品 \"\n" +
			"    },\n" +
			"    \"needResource\": {\n" +
			"        \"companyId\": \"a966ee21-84e7-4e76-97c5-efff6c38b503\",\n" +
			"        \"userId\": \"a966ee21-84e7-4e76-97c5-efff6c38b503\",\n" +
			"        \"ttype\": 3,\n" +
			"        \"label\": \"钱 人才\"\n" +
			"    }\n" +
			"}",required = true) @RequestParam(required = true) String result) throws Exception{
		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");
		Map<String,Object> map = (Map<String, Object>) JSON.parseObject(result,Object.class);
//		String flag = (String) map.get("flag");
		
		Map<String, Object> oweResource =  (Map<String,Object>)map.get("oweResource");
//		Map<String, Object> userResource =  (Map<String,Object>)map.get("userResource");
		Map<String, Object> needResource =  (Map<String,Object>)map.get("needResource");

		

		
		try {
 
			String cid = (String) oweResource.get("companyId");
			String labels = (String) oweResource.get("label");
			String uid = (String) oweResource.get("userId");
			Integer ttype = (Integer) oweResource.get("ttype");
			
			Label label = new Label();
			label.setId(cid);
			label.setCid(cid);
			label.setLabels(labels);
			label.setUid(uid);
			label.setTtype(new Integer(ttype));
			laberService.updateHasResourceLabelById(label);
			
			
			String cid2 = (String) needResource.get("companyId");
			String labels2 = (String) needResource.get("label");
			String uid2 = (String) needResource.get("userId");
			Integer ttype2 = (Integer) needResource.get("ttype");
			
			Label label2 = new Label();
			label2.setId(cid2);
			label2.setCid(cid2);
			label2.setLabels(labels2);
			label2.setUid(uid2);
			label2.setTtype(new Integer(ttype2));
			laberService.updateNeedResourceLabelById(label2);

			companyService.updateInfoScore(cid);
			solrService.updateCompanyIndex(cid);

			userDynamicService.addUserDynamic(loginUserId, (String)needResource.get("companyId"), "", "更新", "修改了资源需求", 0,null,null,null);
				
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	}
}
