package com.szjk.base.model.common;

public class ResponseConstant {


    //全局异常
    public  static  ResponseModel SYSTEM_EXCEPTION=new ResponseModel("E0000","服务异常","");



     //菜单
    public  static  ResponseModel GATEWAY_TOKEN_INVALID=new ResponseModel("E0001","token无效",ServiceIdConstant.menu);


}
