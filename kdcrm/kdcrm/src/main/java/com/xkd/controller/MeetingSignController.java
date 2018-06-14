package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.*;
import com.xkd.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/meetingSign")
@Transactional
public class MeetingSignController  extends BaseController{

	@Autowired
	private MeetingSignService meetingSignService;
	

	@Autowired
	private AddressService addressService;
	
	@Autowired
	private SuggestionService suggestionService;
	
	@Autowired
	private UserDynamicService userDynamicService;
	@Autowired
	private DC_UserService userService;
	@Autowired
	private CompanyService companyService;
	
	
	/**
	 * 
	 * @author: gaodd
	 * @2017年4月20日 
	 * @功能描述:根据会议ID查询参会企业列表/组内成员
	 * @return
	 */
	@RequestMapping("/getSignComAndUser")
	@ResponseBody
	public ResponseDbCenter getSignComAndUser(HttpServletRequest req)throws Exception{
		String id = req.getParameter("meetingId");
		String token = req.getParameter("token");
		String pageNo1 = req.getParameter("pageNo");
		String pageSize1 = req.getParameter("pageSize");  
		String province = req.getParameter("city");
		String sonIndustryid = req.getParameter("industry");
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		if(StringUtils.isBlank(id) || StringUtils.isBlank(token)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		province = StringUtils.isBlank(province) ?null :province;
		sonIndustryid = StringUtils.isBlank(sonIndustryid) ?null :sonIndustryid;
		String uid = req.getSession().getAttribute(token).toString();
		uid = uid == null ? token :uid;
		try {
			int pageNo = 0;
			int pageSize = 100;
			if(StringUtils.isNotBlank(pageNo1)){
				pageNo = Integer.valueOf(pageNo1);
				if(StringUtils.isNotBlank(pageSize1)){
					pageSize = Integer.valueOf(pageSize1);
				}
			}
			responseDbCenter.setResModel(meetingSignService.getCompanyList(id,req.getSession().getAttribute(token).toString(),province,sonIndustryid, pageNo, pageSize));
			responseDbCenter.setTotalRows(meetingSignService.getTotalRows(id,province,sonIndustryid)+"");
			
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		} 
		
		return responseDbCenter;
	}
	/**
	 * 
	 * @author: gaodd
	 * @2017年4月20日 
	 * @功能描述:根据会议id查询当前企业信息和我需要的企业资源
	 * @return
	 */
	@RequestMapping("/getMyCompany")
	@ResponseBody
	public ResponseDbCenter getMyCompany(HttpServletRequest req,@RequestParam("meetingId") String meetingId)throws Exception{
		String companyId = req.getParameter("companyId");//别人的企业
		String token = req.getParameter("token");
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			HashMap<String, Object> map =  meetingSignService.getMyCompany(req.getSession().getAttribute(token).toString(),companyId,meetingId);
			if(map == null){
				return ResponseConstants.FUNC_GETCOMPANYERROR;
			}
			responseDbCenter.setResModel(map);
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		return responseDbCenter;
	}
	
	
	/**
	 * 
	 * @author: gaodd
	 * @2017年4月20日 
	 * @功能描述:关注企业、如果已经关注那么久取消关注
	 * @return
	 */
	@RequestMapping("/followCompany")
	@ResponseBody
	public ResponseDbCenter followCompany(HttpServletRequest req,@RequestParam("token") String token,@RequestParam("companyid") String companyid,@RequestParam("meetingid") String meetingid)throws Exception{
		String uid = req.getSession().getAttribute(token).toString();
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		try {
			int cnt =  meetingSignService.followCompany(companyid,uid,meetingid);
			if(cnt == -1){
				return ResponseConstants.NO_ROLE_MEETING;
			}
			responseDbCenter.setResModel(cnt);
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		return responseDbCenter;
	}
	/**
	 * 
	 * @author: gaodd
	 * @2017年4月20日 
	 * @功能描述:我关注、匹配的企业资源、组成员
	 * @return
	 */
	@RequestMapping("/getMyFollowCompany")
	@ResponseBody
	public ResponseDbCenter getMyFollowCompany(HttpServletRequest req,@RequestParam("token") String token,@RequestParam("ttype") String ttype,@RequestParam("meetingId") String meetingId)throws Exception{
		String uid = req.getSession().getAttribute(token).toString();
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		try {
			responseDbCenter.setResModel(meetingSignService.getMyFollowCompany(uid,ttype,meetingId));
			
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		return responseDbCenter;
	}
	/**
	 * 
	 * @author: gaodd
	 * @2017年4月20日 
	 * @功能描述:课堂问卷
	 * @return
	 */
	@RequestMapping("/meetingExercise")
	@ResponseBody
	public ResponseDbCenter meetingExercise(HttpServletRequest req,@RequestParam("token") String token,@RequestParam("meetingId") String meetingId)throws Exception{
		String uid = req.getSession().getAttribute(token).toString();
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		try {
			responseDbCenter.setResModel(meetingSignService.meetingExercise(meetingId));
			
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		return responseDbCenter;
	}
	/**
	 * 
	 * @author: gaodd
	 * @2017年4月20日 
	 * @功能描述:编辑企业信息
	 * @return
	 */
	@Transactional
	@RequestMapping("/updateCompany")
	@ResponseBody
	public ResponseDbCenter updateCompany(HttpServletRequest req)throws Exception{
		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");
		String token = req.getParameter("token");
		String id = req.getParameter("id");//企业id
		if(StringUtils.isBlank(id) || StringUtils.isBlank(token)){
			return ResponseConstants.MISSING_PARAMTER;
		}
		String uid = req.getSession().getAttribute(token).toString();
		String company_name = req.getParameter("company_name");//企业名称
		String company_desc = req.getParameter("company_desc");//企业简介
		String manageScope = req.getParameter("manageScope");//主营业务
		
		String lable = req.getParameter("label");//企业标签
		String website = req.getParameter("website");//公司官网
		String approveDate = req.getParameter("establish_time");//成立时间
		String representative = req.getParameter("representative");//法人代表
		String city = req.getParameter("address");//公司地址
		//if(StringUtils.isNotBlank(city)){
			/*Address address = addressService.selectAddressByUserId(id);
			if(address == null){
				address = new Address();
				address.setUserId(id);
				address.setAddress(city);
				addressService.saveUserAddress(address);
			}else{
				address.setAddress(city);
				addressService.updateAddressInfoByUserId(address);
			}*/
		//}
		String content = req.getParameter("content");//公司简介
		
		/*String lable1 = req.getParameter("label1");//拥有的资源
		String lable2 = req.getParameter("label2");//企业家的资源
		String lable3 = req.getParameter("label3");//需要的资源
*/		
		String projects = req.getParameter("projectList");//公司产品
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			//if(StringUtils.isNotBlank(company_name) || StringUtils.isNotBlank(manageScope) ||StringUtils.isNotBlank(company_desc)){
				HashMap<String, Object> company = new HashMap<String, Object>();
				company.put("id", id);
				company.put("companyName", company_name);
				company.put("companyDesc", company_desc);
				company.put("manageScope", manageScope);
				
				company.put("lable", lable);
				company.put("userId", uid);
				
				company.put("address", city);
				
				company.put("website", website);
				company.put("establishTime", approveDate);
				company.put("representative", representative);
				company.put("content", content);
				meetingSignService.updateCompany(company);
				
				//编辑新增企业会添加动态记录信息
				
				DC_User user = userService.getUserById(uid);
				
				Company companyDynamic = companyService.selectCompanyInfoById(id);
				
				userDynamicService.addUserDynamic(loginUserId, id, companyDynamic == null?"":companyDynamic.getCompanyName(), "更新", "手机端资本之道  "+(user == null?"":user.getUname())+"修改了\""+(companyDynamic == null?"":companyDynamic.getCompanyName())+"\"", 0,null,null,null);
				

				if(StringUtils.isNotBlank(projects)){
					/*List<Map<String, Object>> projectList = (List<Map<String, Object>>) JSON.parse(projects);
					for (Map<String, Object> project : projectList) {
					}*/
				}
			//}
			/*if(StringUtils.isNotBlank(lable1)){
				HashMap<String, Object> label = new HashMap<String, Object>();
				label.put("cid", id);
				label.put("uid", uid);
				label.put("labels", lable1);
				label.put("ttype", 1);
				
				meetingSignService.updateCompanyLabels(label);
			}
			
			if(StringUtils.isNotBlank(lable2)){
				HashMap<String, Object> label2 = new HashMap<String, Object>();
				label2.put("cid", id);
				label2.put("uid", uid);
				label2.put("labels", lable2);
				label2.put("ttype", 2);
				
				meetingSignService.updateCompanyLabels(label2);
			}
			
			if(StringUtils.isNotBlank(lable3)){
				HashMap<String, Object> label3 = new HashMap<String, Object>();
				label3.put("cid", id);
				label3.put("uid", uid);
				label3.put("labels", lable3);
				label3.put("ttype", 3);
				
				meetingSignService.updateCompanyLabels(label3);
			}*/
			
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		return responseDbCenter;
	}
	/**
	 * 
	 * @author: gaodd
	 * @2017年4月20日 
	 * @功能描述:查询会议里企业的行业
	 * @return
	 */
	@RequestMapping("/getMeetingCompanyIndustryAndCity")
	@ResponseBody
	public ResponseDbCenter getMeetingCompanyIndustry(@RequestParam("token") String token,@RequestParam("meetingId") String meetingId)throws Exception{
		//String uid = req.getSession().getAttribute(token).toString();
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		try {
			HashMap<String, List<HashMap<String, Object>>> map = meetingSignService.getMeetingCompanyIndustryAndCity(meetingId);
			responseDbCenter.setResModel(map);
			
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		return responseDbCenter;
	}
	/**
	 * 
	 * @author: gaodd
	 * @2017年4月20日 
	 * @功能描述:查询所有顾问和总监
	 * @return
	 */
	@RequestMapping("/getAdviserList")
	@ResponseBody
	public ResponseDbCenter getAdviserList(@RequestParam("token") String token)throws Exception{
		//String uid = req.getSession().getAttribute(token).toString();
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		try {
			responseDbCenter.setResModel(meetingSignService.getAdviserList());
			
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		return responseDbCenter;
	}
	/**
	 * 
	 * @author: gaodd
	 * @2017年4月20日 
	 * @功能描述:保存用户反馈
	 * @return
	 */
	@RequestMapping("/saveDictionary")
	@ResponseBody
	public ResponseDbCenter saveDictionary(HttpServletRequest req,@RequestParam("token") String token,@RequestParam("content") String content,@RequestParam("meetingId") String meetingId,@RequestParam("companyId")String companyId)throws Exception{
		//String uid = req.getSession().getAttribute(token).toString();
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		try {
			Suggestion suggestion = new Suggestion();
			suggestion.setUserId(req.getSession().getAttribute(token).toString());
			suggestion.setCompanyId(companyId);
			suggestion.setMeetingId(meetingId);
			suggestion.setContent(content);
			suggestion.setPid("0");
			suggestion.setStatus(0);
			suggestionService.answerSuggestion(suggestion);
			
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		return responseDbCenter;
	}
	/**
	 * 
	 * @author: gaodd
	 * @2017年4月20日 
	 * @功能描述:获取当前时间段的签到企业
	 * @return
	 */
	@RequestMapping("/getSignCompany")
	@ResponseBody
	public ResponseDbCenter getSignCompany(@RequestParam("meetingId") String meetingId,@RequestParam("begin_date") String begin_data,@RequestParam("end_date") String end_date)throws Exception{
		//String uid = req.getSession().getAttribute(token).toString();
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		try {
			
			//responseDbCenter.setResModel(meetingSignService.getSignCompany(meetingId,begin_data,end_date));
			
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: gaodd
	 * @2017年4月20日 
	 * @功能描述:立即匹配
	 * @return
	 */
	@RequestMapping("/getUserFilterCompnay")
	@ResponseBody
	public ResponseDbCenter getUserFilterCompnay(HttpServletRequest req,@RequestParam("meetingId") String meetingId,@RequestParam("token") String token)throws Exception{
		String uid = req.getSession().getAttribute(token).toString();
		String hy = req.getParameter("hangye");
		String xq = req.getParameter("xuqiu");
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		Map<String, Object> map = new HashMap<>();
		try {
			Company company = meetingSignService.getCompanyByMeetingIdAndUid(meetingId,uid);
			System.out.println(company);
			List<String> hangye =  new ArrayList<>();
			List<String> xuqiu = new ArrayList<>();
			
			//行业
			if(StringUtils.isNotBlank(hy)){
				hy = hy.concat(",") != null?hy:hy+",";
				String f [] = hy.split(",");
				for (String value : f) {
					if(StringUtils.isNotBlank(value)){
						hangye.add(value.replace("行业",""));
					}
				}
			}
			if(StringUtils.isNotBlank(company.getParentIndustryId()) || hangye.size()>0){
				hangye.add(company.getParentIndustryId());
				System.out.println("------------------------------------行业----------------------------------");
				map.put("hangyeList",meetingSignService.getUserFilterCompnay(meetingId, uid, hangye, null,null));
			}else{
				map.put("hangyeList",hangye);
			}
			
			System.out.println("MeetingSignController.getUserFilterCompnay()===="+(StringUtils.isBlank(xq)));
			//需求 
			if(StringUtils.isNotBlank(company.getContent())){
				xq=null == xq || StringUtils.isBlank(xq) ? company.getContent(): company.getContent()+","+xq;
			}
			System.out.println(xq);
			if(StringUtils.isNotBlank(xq)){
				xq = xq.concat(",") != null?xq:xq+",";
				String f2 [] = xq.split(",");
				for (String value : f2) {
					if(StringUtils.isNotBlank(value)){
						xuqiu.add(value);
					}
				}
			}else{
				map.put("xuqiuList",xuqiu);
			}
			if(null != xuqiu && xuqiu.size() > 0){
				System.out.println("------------------------------------需求----------------------------------");
				map.put("xuqiuList",meetingSignService.getUserFilterCompnay(meetingId, uid, null, xuqiu,null));
			}
			System.out.println("------------------------------------地区----------------------------------");
			//地区
			map.put("addressList",meetingSignService.getUserFilterCompnay(meetingId, uid, null, null,company.getProvince()));
			responseDbCenter.setResModel(map);
			
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		return responseDbCenter;
	}
}
