package com.wjh.common.model;

public class ResponseConstant {


    //全局异常
    public  static  ResponseModel SYSTEM_EXCEPTION=new ResponseModel("E0000","服务异常","");



    //gateway 服务
    public  static  ResponseModel GATEWAY_TOKEN_INVALID=new ResponseModel("E0001","token无效",ServiceIdConstant.gateway);


    //userservice服务
    public  static  ResponseModel USERSERVICE_PASSWORD_NOT_EQUAL=new ResponseModel("E0001","两次密码输入不一致",ServiceIdConstant.userservice);
    public  static  ResponseModel USERSERVICE_MOBILE_EXISTS=new ResponseModel("E0002","手机号码已经注册过",ServiceIdConstant.userservice);
    public  static  ResponseModel USERSERVICE_LOGIN_FAIL=new ResponseModel("E0003","登录失败，请确保手机号码或密码正确",ServiceIdConstant.userservice);
    public  static  ResponseModel USERSERVICE_LOGIN_AUTHCODE_ERROR=new ResponseModel("E0004","验证码错误",ServiceIdConstant.userservice);


    //quartz服务
    public  static  ResponseModel QUARTZ_ADD_FAIL=new ResponseModel("E0001","创建调试任务失败",ServiceIdConstant.quartz);


}
