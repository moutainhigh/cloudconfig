package com.xkd.controller;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.KaquanService;

@Controller
@RequestMapping("/kaquan")
public class KaquanController {
	
	@Autowired
	KaquanService kaquanService;
	
	@ResponseBody
	@RequestMapping("/getKaquan")
	public ResponseDbCenter getKaquan(String openId) throws Exception{
		if(StringUtils.isBlank(openId)){
			return ResponseConstants.MISSING_PARAMTER;
		}
		Map<String,String> map = kaquanService.getKqUserInfo(openId);
		if(null == map){
			return ResponseConstants.USER_QUAN_AUTCION;
		}
		ResponseDbCenter res = new ResponseDbCenter();
		res.setResModel(map);
		return res;
	}
	
	@ResponseBody
	@RequestMapping("/saveKaquan")
	public ResponseDbCenter saveKaquan(@RequestBody Map<String, String> kaquan) throws Exception{
		if(kaquan == null || StringUtils.isBlank(kaquan.get("openId"))){
			return ResponseConstants.MISSING_PARAMTER;
		}
		if(null == kaquanService.getKqUserInfo(kaquan.get("openId"))){
			kaquanService.saveKaquan(kaquan);
		}else{
			return ResponseConstants.EORRER_AUTCION;
		}
		return ResponseConstants.SUCCESS;
	}

	@ResponseBody
	@RequestMapping("/getKaquanUserList")
	public ResponseDbCenter getKaquanList(String currentPage,
										  String pageSize,
										  String companyName,
										  String startDate,
										  String endDate,
										  String courseId,
										  String uname) throws Exception{
		
		if(StringUtils.isBlank(pageSize) || StringUtils.isBlank(currentPage)){
			return ResponseConstants.MISSING_PARAMTER;
		}
		int  pageSizeInt = Integer.parseInt(pageSize);
		int  currentPageInt = (Integer.parseInt(currentPage)-1)*pageSizeInt;
		Map<String, Object> map = new HashMap<>();
		map.put("pageSizeInt", pageSizeInt);
		map.put("currentPageInt", currentPageInt);
		map.put("companyName", companyName);
		map.put("beginDate", startDate);
		map.put("endDate", endDate);
		map.put("course", courseId);
		map.put("uname", uname);
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		responseDbCenter.setResModel(kaquanService.getKaquanList(map));
		return responseDbCenter;
	}
	/**
	 * 获取Schedule列表信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/kaquanWriteExcel")
	public ResponseDbCenter kaquanWriteExcel() throws Exception{
		

		Map<String, Object> map = new HashMap<>();
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		List<Map<String, String>> list = kaquanService.getKaquanList(map);

		responseDbCenter.setResModel(kaquanService.kaquanWriteExcel(list));
		return responseDbCenter;
	}
	
}
