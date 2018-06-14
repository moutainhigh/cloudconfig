package com.xkd.exception;

import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;

/**
 * Created by dell on 2018/3/16.
 */
public class BoxIDAlreadyExistException extends RuntimeException {
    ResponseDbCenter responseDbCenter;

    public BoxIDAlreadyExistException( ) {
        responseDbCenter= ResponseConstants.BOX_ID_ALREADY_EXIST;
    }

    public ResponseDbCenter getResponseDbCenter() {
        return responseDbCenter;
    }
}
