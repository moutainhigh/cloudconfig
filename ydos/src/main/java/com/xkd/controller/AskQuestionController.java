package com.xkd.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.utils.HttpRequest;

@RequestMapping("/askQuestion")
@Controller
public class AskQuestionController  extends BaseController{

	@RequestMapping("/selectAskQuestionByUserId")
	@ResponseBody
	public ResponseDbCenter selectAskQuestionByUserId(HttpServletRequest req,HttpServletResponse rsp){
		
		String userId = req.getParameter("userId");
		
		HttpRequest request = new HttpRequest();
		
		String result = null;
		
		try {
			
			result = request.getData("url", "UTF-8");
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		System.out.println(result);
		
		JSONObject  json = JSON.parseObject(result);
		
		if(json.get("access_token") == null){
			
			return ResponseConstants.FUNC_USER_OPENID_FAIL;
		}
		
		String openId = json.get("openid").toString().replace("\"", "");
		
		ResponseDbCenter responseDbCenter = new ResponseDbCenter();
		
		return responseDbCenter;
	}
	
	
}
