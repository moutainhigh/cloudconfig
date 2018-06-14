package com.xkd.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.service.SysPagesService;

/**
 * 自动配置前端页面所有栏位
 * 页面ID
 * 栏位ID，label，类型，是否必输，提示信息
 * @author fangsj
 *
 */
@Controller
@RequestMapping("/sysPages")
public class SysPagesController  extends BaseController{
	
	@Autowired
	private SysPagesService service;
	
	/**
	 * 自动配置前端页面所有栏位
	 * 页面pageid
	 * 栏位ID，label，类型，是否必输，提示信息
	 * @param req
	 * @param rsp
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectColumnsByPageID")
	public ResponseDbCenter selectColumnsByPageID(HttpServletRequest req,HttpServletResponse rsp){
		
		String pageId = req.getParameter("pageId");
		if(StringUtils.isBlank(pageId)){
			return ResponseConstants.ACTIVE_ERROR;
		}
		
		List<Map<String,Object>> pageList = service.selectColumnsByPageID(pageId);
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		
		responseDbCenter.setResModel(pageList);
		
		return responseDbCenter;
	}

}
