package com.wjh.common.model;

public class ResponseConstant {
    
    public  static  ResponseModel TOKEN_INVALID=new ResponseModel("E0000","token无效",ServiceIdConstant.gateway,0,null,null);
    public  static  ResponseModel PASSWORD_NOT_EQUAL=new ResponseModel("E0001","两次密码输入不一致",ServiceIdConstant.gateway,0,null,null);
    public  static  ResponseModel SYSTEM_EXCEPTION=new ResponseModel("E0002","服务异常",ServiceIdConstant.gateway,0,null,null);

}
