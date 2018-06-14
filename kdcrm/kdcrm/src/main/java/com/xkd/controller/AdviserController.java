package com.xkd.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mysql.fabric.xmlrpc.base.Data;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.xkd.exception.GlobalException;
import com.xkd.model.Adviser;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.AdviserService;
import com.xkd.service.UserService;
import com.xkd.utils.FileUtil;
import com.xkd.utils.PropertiesUtil;

@Controller
@RequestMapping("/adviser")
@Transactional()
public class AdviserController  extends BaseController{

	@Autowired
	private AdviserService adviserService;
	@Autowired
	private UserService userService;
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月13日 
	 * @功能描述:查询所有顾问
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectAdvisers")
	public ResponseDbCenter selectAdvisers(HttpServletRequest req,HttpServletResponse rsp){
		
		String currentPage = req.getParameter("currentPage");
		String pageSize = req.getParameter("pageSize");
		String ttypeOrder = req.getParameter("ttypeOrder");
//		String levelOrder = req.getParameter("levelOrder");
		String adviserNameOrder = req.getParameter("adviserNameOrder");
		
		if(StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		int  pageSizeInt = Integer.parseInt(pageSize);
		
		int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;
		
		List<Adviser> advisers = null;
		List<Map<String,Object>> teachers = null;
		
		Integer num = null;
		
		try {
			// 当前登录用户的Id
			String loginUserId = (String) req.getAttribute("loginUserId");

			teachers = userService.selectTeachers(pageSizeInt,currentPageInt,loginUserId);
			
			num = userService.selectTeacherCount(loginUserId);
			
		} catch (Exception e) {
			
			System.out.println(e);
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(teachers);
		
		if(num != null){
			
			responseDbCenter.setTotalRows(num.toString());
		}
		
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月13日 
	 * @功能描述:根据类型查询顾问信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectAdvisersByType")
	public ResponseDbCenter selectAdvisersByType(HttpServletRequest req,HttpServletResponse rsp){
		
		String currentPage = req.getParameter("currentPage");
		String pageSize = req.getParameter("pageSize");
		String ttype = req.getParameter("ttype");
		
		if(StringUtils.isBlank(ttype) || StringUtils.isBlank(currentPage) || StringUtils.isBlank(pageSize)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		int  pageSizeInt = Integer.parseInt(pageSize);
		
		int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;
		
		List<Adviser> advisers = null;
		List<Adviser> teacherAdvisers = null;
		
		Integer num = null;
		
		try {
			
			advisers = adviserService.selectAdvisersByType(ttype,pageSizeInt,currentPageInt);
			
			if("2".equals(ttype)){
				
				teacherAdvisers = adviserService.selectAdvisersByType("1",pageSizeInt,currentPageInt);
			}
			
			for(Adviser adviser : advisers){
				/*
				 * 类型  授课老师（1），顾问（2），总监（3），会销老师（4）
				 */
				if(adviser.getTtype() == null ){
					
					continue;
					
				}else if (adviser.getTtype().intValue() == 1) {
				
					adviser.setTtypeName("授课老师");
				}else if (adviser.getTtype().intValue() == 2) {
				
					adviser.setTtypeName("总监");
				}else if (adviser.getTtype().intValue() == 3) {
				
					adviser.setTtypeName("顾问");
				}else if (adviser.getTtype().intValue() == 4) {
				
					adviser.setTtypeName("会销老师");
				}
			}
			
			num = adviserService.selectAdviserCount();
			
		} catch (Exception e) {
			
			System.out.println(e);
			return ResponseConstants.FUNC_SERVERERROR;
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		if(teacherAdvisers != null){
			
			advisers.addAll(teacherAdvisers);
		}
		
		responseDbCenter.setResModel(advisers);
		
		if(num != null){
			
			responseDbCenter.setTotalRows(num.toString());
		}
		
		return responseDbCenter;
	}
	
	
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月13日 
	 * @功能描述:删除
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteAdviserByIds")
	public ResponseDbCenter deleteAdviserByIds(HttpServletRequest req,HttpServletResponse rsp) throws Exception {
		
		String ids = req.getParameter("ids");
		
		if(StringUtils.isBlank(ids)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		String[] idStrings = ids.split(",");
		String ddStrings = "";
		for(int i = 0;i<idStrings.length;i++){
			
			ddStrings += "'"+idStrings[i]+"',";
		}
		
		if(StringUtils.isNotBlank(ddStrings)){
			
			ddStrings = "id in ("+ddStrings.substring(0, ddStrings.length()-1)+")";
		}
		
		
		try {
			
			userService.deleteUserAdviserByIds(ddStrings);
			
		} catch (Exception e) {
			
			System.out.println(e);
			throw new GlobalException(ResponseConstants.FUNC_SERVERERROR);
		}
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	}
	
	/**
	 * 
	 * @author: xiaoz
	 * @2017年7月13日 
	 * @功能描述:更新顾问总监
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/changeAdviser")
	public ResponseDbCenter changeAdviser(HttpServletRequest req,HttpServletResponse rsp) throws Exception {
		
//		String flag = req.getParameter("flag");
		String id = req.getParameter("id");
		String uname = req.getParameter("uname");
		String userLogo = req.getParameter("userLogo");
		String remark = req.getParameter("remark");
		String titleId = req.getParameter("titleId");
		String level = req.getParameter("level");
		String mobile = req.getParameter("mobile");
		String email = req.getParameter("email");
		
		// 当前登录用户的Id
		String loginUserId = (String) req.getAttribute("loginUserId");
		
		if(StringUtils.isBlank(id)){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		/*if(StringUtils.isBlank(mobile) && StringUtils.isBlank(email)){
			
			return ResponseConstants.MOBILE_EMAIL_MUSTHAVE;
		}*/
		
		try {
			
				/*Map<String, Object>  map =  userService.selectUserByMobile(mobile,null);
				
				if(map != null && map.size() > 0){
					
					String teacherId = (String)map.get("id");
					
					if(!id.equals(teacherId)){
						
						return ResponseConstants.MOBILE_EXIST;
					}
				}*/
				
				Map<String,Object> paramMap = new HashMap<>();
				
				paramMap.put("id", id);
				paramMap.put("uname", uname);
				paramMap.put("userLogo", userLogo);
				paramMap.put("remark", remark);
				paramMap.put("mobile", mobile);
				paramMap.put("email", email);
				paramMap.put("teacherShow", 1);
				paramMap.put("updatedBy", loginUserId);
				
				if(StringUtils.isNotBlank(titleId)){
					
					paramMap.put("titleId", titleId);
				}
				
				if(StringUtils.isNotBlank(level)){
					
					paramMap.put("level", new Integer(level));
				}
				
				userService.updateTeacherInfoById(paramMap);
				
			/*else{
				
				String userId = UUID.randomUUID().toString();
				
				Map<String, Object>  map =  userService.selectUserByMobile(mobile,null);
				
				if(map != null && map.size() > 0){
					
					String teacherId = (String)map.get("id");
					
					Map<String,Object> paramMap = new HashMap<>();
					
					paramMap.put("id", teacherId);
					paramMap.put("uname", uname);
					paramMap.put("userLogo", userLogo);
					paramMap.put("remark", remark);
					paramMap.put("email", email);
					paramMap.put("updatedBy", loginUserId);
					
					if(StringUtils.isNotBlank(ttype)){
						
						paramMap.put("titleId",ttype);
					}
					
					if(StringUtils.isNotBlank(level)){
						
						paramMap.put("level", new Integer(level));
					}
					
					userService.updateDcUser(paramMap);
					
				}else {
					
					Map<String,Object> paramMap = new HashMap<>();
					
					paramMap.put("id", userId);
					paramMap.put("uname", uname);
					paramMap.put("userLogo", userLogo);
					paramMap.put("remark", remark);
					paramMap.put("mobile", mobile);
					paramMap.put("email", email);
					paramMap.put("updatedBy", loginUserId);
					paramMap.put("createdBy", loginUserId);
					
					if(StringUtils.isNotBlank(ttype)){
						
						paramMap.put("titleId",ttype);
					}
					
					if(StringUtils.isNotBlank(level)){
						
						paramMap.put("level", new Integer(level));
					}
					
					userService.saveTeacherInfo(paramMap);
				}
			}*/
			
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
	 * @2017年7月13日 
	 * @功能描述:
	 * @param files
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/uploadAdviserLogo")
	public ResponseDbCenter uploadAdviserLogo(@RequestParam(value = "files", required = false) MultipartFile[] files,HttpServletRequest req){
		
		
		if(files == null){
			
			return ResponseConstants.MISSING_PARAMTER;
		}
		
		String uploadPath = PropertiesUtil.FILE_UPLOAD_PATH +"adviserLogo" ;
		String httpPath = PropertiesUtil.FILE_HTTP_PATH +"adviserLogo" ;
		
		List<String> fileList = FileUtil.fileUpload(files, uploadPath,httpPath);
		
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(fileList);
		
		return responseDbCenter;
	}
	
}
