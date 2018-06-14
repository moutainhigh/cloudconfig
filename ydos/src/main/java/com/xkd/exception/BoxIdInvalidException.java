package com.xkd.exception;

import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;

/**
 * Created by dell on 2018/3/16.
 */
public class BoxIdInvalidException extends RuntimeException {

    ResponseDbCenter responseDbCenter;

    public BoxIdInvalidException( ) {
       responseDbCenter= ResponseConstants.INVALID_BOXID;
    }

    public ResponseDbCenter getResponseDbCenter() {
        return responseDbCenter;
    }
}
