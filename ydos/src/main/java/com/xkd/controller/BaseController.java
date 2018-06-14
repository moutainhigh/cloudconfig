package com.xkd.controller;

import com.xkd.exception.BoxIDAlreadyExistException;
import com.xkd.exception.BoxIdInvalidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xkd.exception.GlobalException;
import com.xkd.model.ResponseConstants;
import com.xkd.model.ResponseDbCenter;
import com.xkd.utils.DateUtils;

/**
 * 创建人 巫建辉
 * 本类主要用于中转异常，以便于以Controller层切入事务 ，新实现的Controller都应该继承本类，
 * 并在类注释上添加@Transactional切入事务，同时service层不要捕获异常，以便于异常在controler层被spring捕获并回滚
 */
public class BaseController {


    /**
     * 异常处理方法,处理所有子Controller类的异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler
    @ResponseBody
    public ResponseDbCenter handlerException(Exception ex) {
        ex.printStackTrace();
        if (ex instanceof GlobalException) {// 如果是自定义的异常处理则返回自定义的响应
            return ((GlobalException) ex).getResponseDbCenter();
        } else if (ex instanceof BoxIdInvalidException) {
            return ((BoxIdInvalidException) ex).getResponseDbCenter();
        } else if (ex instanceof BoxIDAlreadyExistException) {
            return ((BoxIDAlreadyExistException) ex).getResponseDbCenter();
        }

        return ResponseConstants.FUNC_SERVERERROR;

    }
}
