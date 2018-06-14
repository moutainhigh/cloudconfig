package com.kuangchi.sdd.visitorConsole.checkUserLogin.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.visitorConsole.checkUserLogin.service.CheckUserLoginService;

@Controller("checkUserLoginAction")
public class CheckUserLoginAction extends BaseActionSupport {

	@Override
	public Object getModel() {
		return null;
	}
	@Resource(name = "checkUserLoginServiceImpl")
	private CheckUserLoginService checkUserLoginService;
	
	//验证用户登录  2016-11-23 guofei.lian
	public void checkUserLogin(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map map=gson.fromJson(data, HashMap.class);
		/*测试用
		 * Map map=new HashMap();
		map.put("userName", "1");
		map.put("userPwd", "123456");*/
		Map resultMap=new HashMap();
		boolean flag=checkUserLoginService.checkUserLogin(map);
		if(flag){
			resultMap.put("resultCode", "0");
			resultMap.put("resultMsg", "验证通过");
		}else{
			resultMap.put("resultCode", "1");
			resultMap.put("resultMsg", "用户名或密码不存在");
		}
		printHttpServletResponse(new Gson().toJson(resultMap));
	}
	
	//根据证件类型和证件号码判断是否为黑名单查询   2016-11-25  guofei.lian
	public void ifBlackList(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map map=gson.fromJson(data, HashMap.class);
		/*测试用
		 Map paramMap=new HashMap();
		paramMap.put("paperNum", "12345666");
		paramMap.put("paperType", "二代");*/
		Map result=new HashMap();
		boolean flag=checkUserLoginService.ifBlackList(map);
		if(flag){
			result.put("result", "0");
		}else{
			result.put("result", "1");
		}
		printHttpServletResponse(new Gson().toJson(result));
	}
	
	//根据卡号查询访客记录信息   2016-11-25  guofei.lian
	public void getRecordInfoByCardNum(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map map=gson.fromJson(data, HashMap.class);
		List<Map> recordList=checkUserLoginService.getRecordInfoByCardNum(map);
		printHttpServletResponse(GsonUtil.toJson(recordList));
	}
	
	//根据编号查询被访人是否被预约  2016-11-25 guofei.lian
	public void ifPassiveBook(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map map=gson.fromJson(data, HashMap.class);
		Map result=new HashMap();
		boolean flag=checkUserLoginService.ifPassiveBook(map);
		if(flag){
			result.put("result", "0");
		}else{
			result.put("result", "1");
		}
		printHttpServletResponse(new Gson().toJson(result));
	}
	
	//根据访客编号查询访客  2016-11-29 guofei.lian
	public void queryVisitorByNum(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("visitNum");
		HashMap map=new HashMap();
		map.put("visitNum", data);
		Map visitorList=checkUserLoginService.queryVisitorByNum(map);
		printHttpServletResponse(GsonUtil.toJson(visitorList));
	}
	
	//根据手机号、证件号码查询预约访客记录  2016-11-29  guofei.lian
	public void queryBookingVisitor(){
		HttpServletRequest request=getHttpServletRequest();
		String data=request.getParameter("data");
		Gson gson=new Gson();
		Map map=gson.fromJson(data, HashMap.class);
		Map bookingVisitor=checkUserLoginService.queryBookingVisitor(map);
		Map resultMap=new HashMap();
		if(bookingVisitor!=null){
			resultMap.putAll(bookingVisitor);
			resultMap.put("result", "0");
		}else{
			resultMap.put("result", "1");
		}
		printHttpServletResponse(GsonUtil.toJson(resultMap));
	}
}
