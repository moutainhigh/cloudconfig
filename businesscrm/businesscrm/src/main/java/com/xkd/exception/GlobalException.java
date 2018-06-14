package com.xkd.exception;

import com.xkd.model.ResponseDbCenter;

public class GlobalException  extends RuntimeException{
	ResponseDbCenter responseDbCenter;

	public GlobalException(ResponseDbCenter responseDbCenter) {
 		this.responseDbCenter = responseDbCenter;
	}

	public ResponseDbCenter getResponseDbCenter() {
		return responseDbCenter;
	}



	
}
