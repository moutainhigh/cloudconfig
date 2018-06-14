package com.xkd.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xkd.exception.GlobalException;
import com.xkd.model.Adviser;
import com.xkd.model.DC_PC_User;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.model.Suggestion;
import com.xkd.service.AdviserService;
import com.xkd.service.DC_PC_UserService;
import com.xkd.service.SuggestionService;
import com.xkd.service.UserService;

@Controller
@RequestMapping("suggestion")
@Transactional
public class SuggestionController  extends BaseController{

	@Autowired
	private SuggestionService suggestionService;
	
	@Autowired
	private AdviserService adviserService;
	
	@Autowired
	private DC_PC_UserService pcUserService;
	@Autowired
	private UserService userService;
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月7日 
	 * @功能描述:根据会议ID查询企业客户的反馈信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectSuggestionsByMeetingId")
	public ResponseDbCenter selectSuggestionsByMeetingId(HttpServletRequest req,HttpServletResponse rsp){
		
		String meetingId = req.getParameter("meetingId");
		
		if(StringUtils.isBlank(meetingId)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		List<HashMap<String,Object>> suggestions = null;
		
		try {
			
			suggestions = suggestionService.selectSuggestionsByMeetingId(meetingId);
			
			for(HashMap<String,Object> map : suggestions){
				
				String answerPersonId = map.get("answerPersonId") == null?"":map.get("answerPersonId").toString();
				String questionPersonId = map.get("questionPersonId") == null?"":map.get("questionPersonId").toString();
				
				if(StringUtils.isNotBlank(answerPersonId)){
					
					Adviser adviser = adviserService.selectAdviserById(answerPersonId);
					
					if(adviser != null){
						
						map.put("answerPersonName",adviser.getAdviserName());
						map.put("answerPersonLogo",adviser.getImgpath());
					}
				}
				
				if(StringUtils.isNotBlank(questionPersonId)){
					
//					DC_PC_User user = new DC_PC_User();
//				    user.setId(questionPersonId);
				    
					Map<String, Object> userResult = userService.selectUserById(questionPersonId);
				    
				    if(userResult != null){
				    
				    	map.put("questionPersonName",userResult.get("uname"));
				    }
				}
			}
			
		} catch (Exception e) {
			
			System.out.println(e);
		}
		
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		responseDbCenter.setResModel(suggestions);
		
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月7日 
	 * @功能描述:查询出需要回复的企业客户
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectSuggestionsNeedAnswer")
	public ResponseDbCenter selectSuggestionsNeedAnswer(HttpServletRequest req,HttpServletResponse rsp){
		
		String meetingId = req.getParameter("meetingId");
		
		if(StringUtils.isBlank(meetingId)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		List<HashMap<String,Object>> suggestions = null;
		
		try {
			
			suggestions = suggestionService.selectSuggestionsNeedAnswer(meetingId);
			
		} catch (Exception e) {
			
			System.out.println(e);
		}
		
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		responseDbCenter.setResModel(suggestions);
		
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月7日 
	 * @功能描述:删除回复信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteSuggestionById")
	public ResponseDbCenter deleteSuggestionById(HttpServletRequest req,HttpServletResponse rsp) throws Exception{
		
		String answerId = req.getParameter("answerId");
		String questionId = req.getParameter("questionId");
		
		if(StringUtils.isBlank(answerId) && StringUtils.isBlank(questionId)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		
		try {
			
			if(StringUtils.isNotBlank(answerId)){
				
				suggestionService.deleteSuggestionById(answerId);
			}
			
			if(StringUtils.isNotBlank(questionId)){
				
				suggestionService.deleteSuggestionById(questionId);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月7日 
	 * @功能描述:回复客户反馈信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/answerSuggestion")
	public ResponseDbCenter answerSuggestion(HttpServletRequest req,HttpServletResponse rsp) throws Exception{
		
		String pid = req.getParameter("pid");
		String meetingId = req.getParameter("meetingId");
		String companyId = req.getParameter("companyId");
		String answerPersonId = req.getParameter("answerPersonId");
		String content = req.getParameter("content");
		
		if(StringUtils.isBlank(pid) || StringUtils.isBlank(meetingId) || StringUtils.isBlank(answerPersonId)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		
		try {
			
			Suggestion suggestion = new Suggestion();
			
			suggestion.setUserId(answerPersonId);
			suggestion.setMeetingId(meetingId);
			
			if(StringUtils.isNotBlank(companyId)){
				
				suggestion.setCompanyId(companyId);
			}
			suggestion.setPid(pid);
			suggestion.setContent(content);
			suggestion.setStatus(0);
			
			suggestionService.answerSuggestion(suggestion);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月11日 
	 * @功能描述:保存客户反馈信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/saveSuggestion")
	public ResponseDbCenter saveSuggestion(HttpServletRequest req,HttpServletResponse rsp) throws Exception{
		
		String pid = req.getParameter("pid");
		String meetingId = req.getParameter("meetingId");
		String companyId = req.getParameter("companyId");
		String answerPersonId = req.getParameter("userId");
		String content = req.getParameter("content");
		
		if(StringUtils.isBlank(pid) || StringUtils.isBlank(meetingId) || StringUtils.isBlank(answerPersonId)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		
		try {
			
			Suggestion suggestion = new Suggestion();
			
			suggestion.setUserId(answerPersonId);
			suggestion.setMeetingId(meetingId);
			
			if(StringUtils.isNotBlank(companyId)){
				
				suggestion.setCompanyId(companyId);
			}
			suggestion.setPid("0");
			suggestion.setContent(content);
			suggestion.setStatus(0);
			
			suggestionService.answerSuggestion(suggestion);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	}
	
}
