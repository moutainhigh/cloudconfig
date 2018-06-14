package com.xkd.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.xkd.exception.GlobalException;
import com.xkd.model.Exercise;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.MeetingExerciseService;

@Controller
@RequestMapping("/meetingExercise")
@Transactional
public class MeetingExerciseController  extends BaseController{

	@Autowired
	private MeetingExerciseService meetingExerciseService;
	
	

	
	/**
	 * 
	 * @author: gaodd
	 * @2017年4月20日 
	 * @功能描述:查询会务关联的问卷
	 * @param req
	 * @param rsp
	 * @return
	 */
	@RequestMapping("/getMeetingExercise")
	@ResponseBody
	public ResponseDbCenter getMeetingExercise(@RequestParam("token") String token,@RequestParam("meetingId") String meetingId)throws Exception{
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			List<Exercise> map = meetingExerciseService.getExerciseByMid(meetingId);
			responseDbCenter.setResModel(map);
		} catch (Exception e) {

			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: gaodd
	 * @2017年4月20日 
	 * @功能描述:查询企业关联的问卷
	 * @param req
	 * @param rsp
	 * @return
	 */
	@RequestMapping("/getCompanyExercise")
	@ResponseBody
	public ResponseDbCenter getCompanyExercise(@RequestParam("token") String token,@RequestParam("companyId") String companyId)throws Exception{
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		try {
			List<Map<String, Object>> map = meetingExerciseService.getExerciseByCompanyid(companyId);
			if(null != map && map.size() > 0){
				for (Map<String, Object> map2 : map) {
					List<HashMap<String, Object>> companyUser = meetingExerciseService.getCompanyUserExercise(map2.get("meetingId"),companyId,map2.get("id"));
					map2.put("user",companyUser);
					map2.put("cnt", companyUser.size());
				}
			}
			responseDbCenter.setResModel(map);
		} catch (Exception e) {

			e.printStackTrace();
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: gaodd
	 * @2017年4月20日 
	 * @功能描述:保存会议关联的课前课后课中问卷
	 * @param req
	 * @param rsp
	 * @return
	 */
	@RequestMapping("/saveMeetingExercise")
	@ResponseBody
	public ResponseDbCenter saveMeetingExercise(HttpServletRequest req,@RequestParam("token") String token,@RequestParam("meetingExercise") String meetingExercise,@RequestParam("id") String id)throws Exception{
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		
		try {
			List<Map<String, Object>> eMList = (List<Map<String, Object>>) JSON.parse(meetingExercise);
			for (Map<String, Object> map : eMList) {
				if(null != map){
					Object eid = map.get("eid");
					Object ttype = map.get("ttype");
					meetingExerciseService.editMeetingExerciseList(eid,ttype,id,req.getSession().getAttribute(token).toString());
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		return responseDbCenter;
	}


}
