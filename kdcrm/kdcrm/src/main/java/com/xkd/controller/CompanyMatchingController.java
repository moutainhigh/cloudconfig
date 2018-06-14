package com.xkd.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xkd.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xkd.model.Adviser;
import com.xkd.model.CompanyMatching;
import com.xkd.model.Dictionary;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.AdviserService;
import com.xkd.service.CompanyMatchingService;
import com.xkd.service.DictionaryService;

@Controller
@RequestMapping("/companyMatching")
public class CompanyMatchingController  extends BaseController{

	@Autowired
	private CompanyMatchingService companyMatchingService;
	
	@Autowired
	private DictionaryService dictionaryService;
	
	@Autowired
	private AdviserService adviserService;
	@Autowired
	private UserService userService;
	
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月10日 
	 * @功能描述:根据企业ID查询已经匹配的企业信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectMatchingsByCompanyId")
	public ResponseDbCenter selectMatchingsByCompanyId(HttpServletRequest req,HttpServletResponse rsp){
		
		
		String companyId = req.getParameter("companyId");
		String currentPage = req.getParameter("currentPage");
		String pageSize = req.getParameter("pageSize");
		
		if (StringUtils.isBlank(companyId) || StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize)) {

			return ResponseConstants.MISSING_PARAMTER;
		}
		
		int  pageSizeInt = Integer.parseInt(pageSize);
		
		int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;
		
		List<Map<String, Object>>  maps = null;
		
		Integer total = null;
		
		try {
			
			maps = companyMatchingService.selectMatchingsByCompanyId(companyId,pageSizeInt,currentPageInt);
			
			total = companyMatchingService.selectTotalMatchingsByCompanyId(companyId);
			
			
			/*for(Map<String, Object> map : maps){
				
				String parentIndustryid = map.get("parent_industryid") == null?"":map.get("parent_industryid").toString();
				String companyAdviserId = map.get("companyAdviser") == null?"":map.get("companyAdviser").toString();
				
				Dictionary dictionary = dictionaryService.selectDictionaryById(parentIndustryid);
				
				if(StringUtils.isNotBlank(companyAdviserId)){
					
					Adviser adviser = adviserService.selectAdviserById(new Integer(companyAdviserId));
					
					if(adviser != null){
						
						map.put("companyAdviserName",adviser.getAdviserName());
					}
				}
				
				if(dictionary != null){
					
					map.put("parentIndustryName", dictionary.getValue());
				}
			}*/
			
		} catch (Exception e) {
			
			System.out.println(e);
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(maps);
		
		if(total != null){
			responseDbCenter.setTotalRows(total.toString());
		}
		
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月10日 
	 * @功能描述:查询没有匹配的企业
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectNotMatchingsByCompanyId")
	public ResponseDbCenter selectNotMatchingsByCompanyId(HttpServletRequest req,HttpServletResponse rsp){
		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");
		Map<String,Object>  loginUserMap=userService.selectUserById(loginUserId);
		String companyId = req.getParameter("companyId");
		String content = req.getParameter("content");
		String parentIndustry = req.getParameter("parentIndustryId");
		String sonIndustryId = req.getParameter("sonIndustryId");
		String province = req.getParameter("province");
		String city = req.getParameter("city");
		String county = req.getParameter("county");
		String userlevel = req.getParameter("userlevel");
		String usertype = req.getParameter("usertype");
		String channel = req.getParameter("channel");
		String companyAdviser = req.getParameter("companyAdviser");
		
		String currentPage = req.getParameter("currentPage");
		String pageSize = req.getParameter("pageSize");
		
		String companyNameOrder = req.getParameter("companyNameOrder");
		String companyResourceOrder = req.getParameter("companyResourceOrder");
		String companyNeedOrder = req.getParameter("companyNeedOrder");
		String userTypeOrder = req.getParameter("userTypeOrder");
		String companyAdviserOrder = req.getParameter("companyAdviserOrder");
		String enrollDateOrder = req.getParameter("enrollDateOrder");
		

		if(StringUtils.isBlank(companyId) || StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		int  pageSizeInt = Integer.parseInt(pageSize);
		
		int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;
		
		String sqlContent = "";
		
		
 
		if (StringUtils.isBlank(companyId)) {

			return ResponseConstants.MISSING_PARAMTER;
		}
		
			
		Map<String,Object> condition=new HashMap<String, Object>();
		condition.put("companyId",companyId );
		condition.put("content",content );
		condition.put("parentIndustryId", parentIndustry);
		condition.put("sonIndustry", sonIndustryId);
		condition.put("province", province);
		condition.put("city", city);
		condition.put("county", county);
		condition.put("userType", usertype);
		condition.put("userLevel", userlevel);
		condition.put("companyAdviserId", companyAdviser);
		condition.put("channel", channel);
		condition.put("currentPageInt", currentPageInt);
		condition.put("pageSizeInt", pageSizeInt);
		if (!"1".equals(loginUserMap.get("roleId"))) {  //如果不是超级管理员，则需要添加客户公司条件
			condition.put("pcCompanyId", loginUserMap.get("pcCompanyId"));
		}
		
		

		
		List<Map<String, Object>>  maps = null;
		
		Integer num = null;
		
		try {
			/*
			 * 	String companyNameOrder = req.getParameter("companyNameOrder");
		String companyResourceOrder = req.getParameter("companyResourceOrder");
		String companyNeedOrder = req.getParameter("companyNeedOrder");
		String userTypeOrder = req.getParameter("userTypeOrder");
		String companyAdviserOrder = req.getParameter("companyAdviserOrder");
		String enrollDateOrder = req.getParameter("enrollDateOrder");
			 */
			maps = companyMatchingService.selectNotMatchingsByCompanyId(condition);
			
			num = companyMatchingService.selectTotalNotMatchingsByCompanyId(condition);
			
			/*for(Map<String, Object> map : maps){
				
				String parentIndustryid = map.get("parent_industryid") == null?"":map.get("parent_industryid").toString();
				String companyAdviserId = map.get("companyAdviser") == null?"":map.get("companyAdviser").toString();
				
				Dictionary dictionary = dictionaryService.selectDictionaryById(parentIndustryid);
				
				if(dictionary != null){
					
					map.put("parentIndustryName", dictionary.getValue());
				}
			}*/
			
		} catch (Exception e) {
			
			System.out.println(e);
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(maps);
		
		if(num != null){
			
			responseDbCenter.setTotalRows(num.toString());
		}
		
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月11日 
	 * @功能描述:保存匹配的企业
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveMatchingCompanys")
	public ResponseDbCenter saveMatchingCompanys(HttpServletRequest req,HttpServletResponse rsp){
		
		
		String id = req.getParameter("companyId");
		String companyIds = req.getParameter("matchaIds");
		String userId = req.getParameter("userId");
		String rule = req.getParameter("rule");
		
		if (StringUtils.isBlank(id) || StringUtils.isBlank(userId)) {

			return ResponseConstants.MISSING_PARAMTER;
		}
		
		String[] companyIdStr = null;
		
		if(StringUtils.isNotBlank(companyIds)){
			
			companyIdStr = companyIds.split(",");
		}
		
		try {
			
			if(companyIdStr != null){
				
				CompanyMatching companyMatch = new CompanyMatching();
				
				companyMatch.setDealPersonId(userId);
				companyMatch.setCompanyId(id);
				companyMatch.setRule(rule);
				companyMatch.setStatus(0);
				
				for(int i=0 ;i<companyIdStr.length;i++){
					
					companyMatch.setMatchCompanyId(companyIdStr[i]);
					
					companyMatchingService.saveMatchingCompanys(companyMatch);
					
				}
			}
			
		} catch (Exception e) {
			
			System.out.println(e);
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月11日 
	 * @功能描述:删除匹配的相似企业
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteMatchingCompanys")
	public ResponseDbCenter deleteMatchingCompanys(HttpServletRequest req,HttpServletResponse rsp){
		
		
		String matchIds = req.getParameter("matchIds");
		
		if (StringUtils.isBlank(matchIds)) {

			return ResponseConstants.MISSING_PARAMTER;
		}
		
		String[] ids = null;
		
		if(StringUtils.isNotBlank(matchIds)){
			
			ids = matchIds.split(",");
		}
		
		String idString = "";
		
		if(ids != null){
			
			for(int i=0 ;i<ids.length;i++){
				
				idString += "'"+ids[i]+"',";
			}
			
			if(StringUtils.isNotBlank(idString)){
				
				idString = "("+idString.substring(0,idString.lastIndexOf(","))+")";
			}
		}
		
		
		try {
			
			if(StringUtils.isNotBlank(idString)){
				
				companyMatchingService.deleteMatchingCompanys(idString);
			}
			
		} catch (Exception e) {
			
			System.out.println(e);
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	}
	
}
