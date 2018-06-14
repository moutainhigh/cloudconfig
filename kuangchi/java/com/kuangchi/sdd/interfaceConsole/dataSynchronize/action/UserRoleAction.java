package com.kuangchi.sdd.interfaceConsole.dataSynchronize.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.UserRole;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.LoginUserSyncService;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.UserRoleService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("userRoleAction")
public class UserRoleAction extends BaseActionSupport {
 
	@Resource(name = "userRoleServiceImpl")
	 private UserRoleService userRoleService;
	
	@Resource(name = "loginUserSyncServiceImpl")
	 private LoginUserSyncService loginUserSyncService;
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * 添加用户角色，先删后加(供C/S端使用)
	 */
	public void adduserRole(){
		JsonResult result = new JsonResult();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpServletRequest request = getHttpServletRequest();
		String userName = request.getParameter("userName");
		String js_dm = request.getParameter("js_dm"); //角色代码
		try {
			if(null==userName||"".equals(userName.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("用户名称不能为空");		
			}
		} catch (com.kuangchi.sdd.base.exception.MyException myException) {
			result.setMsg(myException.getMessage());
			result.setCode("1");
			resultMap.put("msg",result.getMsg());
			resultMap.put("code", result.getCode());
			printHttpServletResponse(GsonUtil.toJson(resultMap)); 
			return;
		}
		UserRole userRole=new UserRole();
		userRole.setYh_dm(userName);
		userRole.setJs_dm(js_dm);
	    try {
	    	userRoleService.delUserRole(userName);
	    	userRoleService.addUserRole(userRole);
	    	String supperRole;    //内置管理员的js_dm为1
	    	if(Integer.valueOf(js_dm)==1){
	    		supperRole="1";
	    		loginUserSyncService.modifyLoginUser_Role(userName, supperRole);
	    		result.setMsg("添加成功");
	        	result.setCode("0");
	        	resultMap.put("msg",result.getMsg());
				resultMap.put("code", result.getCode());
	    	}else{
	    		supperRole="0";
	    		loginUserSyncService.modifyLoginUser_Role(userName, supperRole);
	    		result.setMsg("添加成功");
	        	result.setCode("0");
	        	resultMap.put("msg",result.getMsg());
				resultMap.put("code", result.getCode());
	    	}
        } catch (Exception e) {
        	result.setMsg("添加失败");
        	result.setCode("1");
        	resultMap.put("msg",result.getMsg());
			resultMap.put("code", result.getCode());
        }
        printHttpServletResponse(GsonUtil.toJson(resultMap));
	}
	
	
}
