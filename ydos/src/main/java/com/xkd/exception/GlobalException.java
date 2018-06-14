package com.xkd.exception;

import com.xkd.model.ResponseDbCenter;

public class GlobalException  extends RuntimeException{
	ResponseDbCenter responseDbCenter;

	public GlobalException(ResponseDbCenter responseDbCenter) {
		super();
		this.responseDbCenter = responseDbCenter;
	}

	public ResponseDbCenter getResponseDbCenter() {
		return responseDbCenter;
	}



	
}
