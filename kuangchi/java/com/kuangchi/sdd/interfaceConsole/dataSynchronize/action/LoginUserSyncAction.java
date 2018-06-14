package com.kuangchi.sdd.interfaceConsole.dataSynchronize.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.LoginUserSyncModel;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.UserRole;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.LoginUserSyncService;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.impl.UserRoleServiceImpl;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.UUIDUtil;

@Controller("loginUserSyncAction")
public class LoginUserSyncAction extends BaseActionSupport {
 
	@Resource(name = "loginUserSyncServiceImpl")
	 private LoginUserSyncService loginUserSyncService;
	
	@Resource(name = "userRoleServiceImpl")
	 private UserRoleServiceImpl userRoleService;
	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * 添加用户(供C/S端使用)
	 */
	public void addLoginUserSync(){
		JsonResult result = new JsonResult();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("id");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String description = request.getParameter("description");
		String isSupperUser = request.getParameter("isSupperUser");//超级管理员  1是，0否
		
		try {
			if(null==id||"".equals(id.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("id不能为空");		
			}
			if(null==userName||"".equals(userName.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("用户名不能为空");		
			}if(null==password||"".equals(password.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("密码不能为空");		
			}if(null==isSupperUser||"".equals(isSupperUser.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("管理员角色不能为空");		
			}
		} catch (com.kuangchi.sdd.base.exception.MyException myException) {
			result.setMsg(myException.getMessage());
			result.setCode("1");
			resultMap.put("msg",result.getMsg());
			resultMap.put("code", result.getCode());
			printHttpServletResponse(GsonUtil.toJson(resultMap)); 
			return;
		}
		LoginUserSyncModel loginUserSync=new LoginUserSyncModel();
		loginUserSync.setRemote_user_id(id);
		loginUserSync.setYh_mc(userName);
		loginUserSync.setYh_dm(userName);
		loginUserSync.setYh_mm(password);
		loginUserSync.setBz(description);
		loginUserSync.setLr_sj(getSysTimestamp()); //自己创建
		loginUserSync.setUuid(UUIDUtil.uuidStr());
		loginUserSync.setZf_bj(GlobalConstant.ZF_BJ_N);
		
		UserRole userRole=new UserRole();
		userRole.setYh_dm(userName);
		if(null!=isSupperUser &&!"".equals(isSupperUser.trim())){
			if(Integer.valueOf(isSupperUser)==1){//超级管理员  1是，0否
				loginUserSync.setGly_bj("1");//超级管理员  1是，0否
				userRole.setJs_dm("1");   //超级管理员  1是，2否
			}else if(Integer.valueOf(isSupperUser)==0){
				loginUserSync.setGly_bj("0");
				userRole.setJs_dm("2");
			}	
		}
	    try {
	    	loginUserSyncService.addLoginUserSync(loginUserSync); 
	    	userRoleService.addUserRole(userRole);
	    	result.setMsg("添加成功");
	    	result.setCode("0");
	    	resultMap.put("msg",result.getMsg());
			resultMap.put("code", result.getCode());
        } catch (Exception e) {
        	result.setMsg("添加失败");
        	result.setCode("1");
        	resultMap.put("msg",result.getMsg());
			resultMap.put("code", result.getCode());
        }
        printHttpServletResponse(GsonUtil.toJson(resultMap));
	}
	/**
	 * 修改用户信息(供C/S端使用)
	 */
	public void modifyLoginUserSync(){
		JsonResult result = new JsonResult();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("id");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String description = request.getParameter("description");
		try {
			if(null==id||"".equals(id.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("id不能为空");		
			}
			if(null==userName||"".equals(userName.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("用户名不能为空");		
			}if(null==password||"".equals(password.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("密码不能为空");		
			}
		} catch (com.kuangchi.sdd.base.exception.MyException myException) {
			result.setMsg(myException.getMessage());
			result.setCode("1");
			resultMap.put("msg",result.getMsg());
			resultMap.put("code", result.getCode());
			printHttpServletResponse(GsonUtil.toJson(resultMap)); 
			return;
		}
		LoginUserSyncModel loginUserSync=new LoginUserSyncModel();
		loginUserSync.setRemote_user_id(id);
		loginUserSync.setYh_mc(userName);
		loginUserSync.setYh_dm(userName);
		loginUserSync.setYh_mm(password);
		loginUserSync.setBz(description);
		 try {
			 loginUserSyncService.modifyLoginUserSync(loginUserSync);
			 result.setMsg("修改成功");
			 result.setCode("0");
			 resultMap.put("msg",result.getMsg());
			 resultMap.put("code", result.getCode());
			 
	        } catch (Exception e) {
	        	result.setMsg("修改失败");
	        	result.setCode("1");
	        	resultMap.put("msg",result.getMsg());
				resultMap.put("code", result.getCode());
	        }
	        printHttpServletResponse(GsonUtil.toJson(resultMap));
	}
	
	/**
	 * 根据用户名删除用户信息(伪)和用户角色（真）(供C/S端使用)
	 *   单个删除
	 */
	public void delLoginUserSync(){
		JsonResult result = new JsonResult();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		HttpServletRequest request = getHttpServletRequest();
		String userName = request.getParameter("userName");
		try {
			if(null==userName||"".equals(userName.trim())){
				throw new com.kuangchi.sdd.base.exception.MyException("用户姓名不能为空");		
			}
		} catch (com.kuangchi.sdd.base.exception.MyException myException) {
			result.setMsg(myException.getMessage());
			result.setCode("1");
			resultMap.put("msg",result.getMsg());
			resultMap.put("code", result.getCode());
			printHttpServletResponse(GsonUtil.toJson(resultMap)); 
			return;
		}
	    try {
	    	loginUserSyncService.delLoginUserSync(userName);
	    	result.setMsg("删除成功");
	    	result.setCode("0");
	    	resultMap.put("msg",result.getMsg());
			resultMap.put("code", result.getCode());
        } catch (Exception e) {
        	result.setMsg("删除失败");
        	result.setCode("1");
        	resultMap.put("msg",result.getMsg());
			resultMap.put("code", result.getCode());
        }
        printHttpServletResponse(GsonUtil.toJson(resultMap));
	}
	
}
