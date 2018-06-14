package com.xkd.controller;

import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class BaseController{
	Logger log= LoggerFactory.getLogger(BaseController.class);

	//所有Controller层的异常处理方法
	@ExceptionHandler
	@ResponseBody
	public ResponseDbCenter handlerException(Exception ex) {
		
		log.error("异常栈:",ex);
		if (ex instanceof GlobalException) {// 如果是自定义的异常处理则返回自定义的响应
			ResponseDbCenter responseDbCenter=((GlobalException) ex).getResponseDbCenter();
			return responseDbCenter;
		}

		return ResponseConstants.FUNC_SERVERERROR;

	}
}
