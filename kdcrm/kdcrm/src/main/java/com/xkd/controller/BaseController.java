package com.xkd.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.utils.DateUtils;

public class BaseController{

	//所有Controller层的异常处理方法
	@ExceptionHandler
	@ResponseBody
	public ResponseDbCenter handlerException(Exception ex) {
		
		ex.printStackTrace();
		if (ex instanceof GlobalException) {// 如果是自定义的异常处理则返回自定义的响应
			return ((GlobalException) ex).getResponseDbCenter();
		}

		return ResponseConstants.FUNC_SERVERERROR;

	}
}
