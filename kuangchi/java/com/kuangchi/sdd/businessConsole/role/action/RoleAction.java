package com.kuangchi.sdd.businessConsole.role.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.businessConsole.user.service.IUserService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("roleAction")
public class RoleAction extends BaseActionSupport {
	
	private static final Logger LOG = Logger.getLogger(RoleAction.class);

	private static final long serialVersionUID = -6194143783922371041L;

	private Role model;

	public RoleAction() {
		this.model = new Role();
	}
	
	 @Resource(name = "roleServiceImpl")
	 private IRoleService roleService;

	 @Resource(name = "userServiceImpl")
	 private IUserService userService;
	 
	@Override
	public Object getModel() {
		
		return model;
	}
	

	/**
	 * 角色管理
	 */
	public void manageRole(){
		
		Role rolePage = new Role();
		BeanUtils.copyProperties(model, rolePage);
		
		// 判断是否开启分层功能，若无开启，则查询所有角色
		boolean isLayer = roleService.isLayer();
		if(isLayer){
			// 根据录入人员查询角色
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
			String lrryDm =  userService.getLrryDm(loginUser.getYhDm());
			if(!"".equals(lrryDm)){
				lrryDm = lrryDm.substring(0, lrryDm.length()-1);
				rolePage.setLrryDm(lrryDm);
			} 
		} 
		
		resetRole();
		Grid<Role> grid = roleService.getRoles(rolePage);
		printHttpServletResponse(GsonUtil.toJson(grid));
		
		
	}
	
	/**
	 * 系统所有角色
	 */
	public void sysAllRole(){
		
		List<Role> allRoles = new ArrayList<Role>();
		
		// 判断是否开启分层功能，若无开启，则查询所有角色
		boolean isLayer = roleService.isLayer();
		if(isLayer){
			// 根据录入人员查询角色
			User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
			String lrryDm =  userService.getLrryDm(loginUser.getYhDm());
			if(!"".equals(lrryDm)){
				lrryDm = lrryDm.substring(0, lrryDm.length()-1);
				allRoles = roleService.getAllRoles(lrryDm);
			} 
		} else {
			allRoles = roleService.getAllRoles(null);
		}
		
		printHttpServletResponse(GsonUtil.toJson(allRoles));
		
	}

    public void addNewRole(){
        Role rolePage = new Role();
        BeanUtils.copyProperties(model, rolePage);
        resetRole();

        JsonResult result = new JsonResult();
        try{
        	
        	User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);    	
        	rolePage.setLrryDm(loginUser.getYhDm());
            roleService.addNewRole(rolePage);
            result.setMsg("新增成功");
            result.setSuccess(true);
        }catch(Exception e){
            result.setMsg("新增失败");
            result.setSuccess(false);
            LOG.error("role", e);
        }
        printHttpServletResponse(new Gson().toJson(result));
    }

	private void resetRole(){
		model = new Role();
	}

    /**
     * 获得角色已经分配的菜单
     */
    public void getRoleAssginMenu(){
        String roleId = getHttpServletRequest().getParameter("roleId");
        JsonResult jsonResult = new JsonResult();
        jsonResult.setSuccess(true);
        try{
            jsonResult.setMsg(roleService.getRoleAssginMenu(roleId));

        }catch (Exception e){
            jsonResult.setSuccess(false);
            jsonResult.setMsg(null);
            LOG.error("role", e);
        }
        printHttpServletResponse(new Gson().toJson(jsonResult));

    }

    /**
     * datagrid当前页角色分配的用户
     */
    public void getRoleAssginUserCurrentPage(){
        String roleId = getHttpServletRequest().getParameter("roleId");//角色编号
        String ids = getHttpServletRequest().getParameter("ids");//用户编号

        JsonResult jsonResult = new JsonResult();
        jsonResult.setSuccess(true);
       try{
            jsonResult.setMsg(roleService.getRoleAssginUser(roleId,ids));

        }catch (Exception e){
            jsonResult.setSuccess(false);
            jsonResult.setMsg(null);
            LOG.error("role", e);
        }
        printHttpServletResponse(new Gson().toJson(jsonResult));
    }

    /**
     * 角色分配菜单
     */
    public void changeRoleMenu(){
        String roleId = getHttpServletRequest().getParameter("roleId");//角色编号
        String ids = getHttpServletRequest().getParameter("ids");//菜单编号

        JsonResult jsonResult = new JsonResult();
        jsonResult.setSuccess(true);
        try{
        	
        	User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
            roleService.changeRoleMenu(roleId,ids.split(","),loginUser.getYhDm());
        }catch (Exception e){
            jsonResult.setSuccess(false);
            jsonResult.setMsg("授权失败");
            LOG.error("role", e);
        }
        printHttpServletResponse(new Gson().toJson(jsonResult));
    }

    public void changeRoleUser() {
        String roleId = getHttpServletRequest().getParameter("roleId");
        String currUserIds = getHttpServletRequest().getParameter("currUserIds");
        String newUserIds = getHttpServletRequest().getParameter("newUserIds");

        JsonResult jsonResult = new JsonResult();
        jsonResult.setSuccess(true);
        try {
        	
        	User loginUser = (User) getHttpServletRequest().getSession().getAttribute(GlobalConstant.LOGIN_USER);
            roleService.changeRoleUser(roleId, currUserIds, newUserIds.split(","),loginUser.getYhDm());
        } catch (Exception e) {
            jsonResult.setSuccess(false);
            jsonResult.setMsg("授权失败");
            LOG.error("role", e);
        }
        printHttpServletResponse(new Gson().toJson(jsonResult));

    }
    
    /**
     * 编辑角色
     * @return
     */
    public String modifyRole(){
    	HttpServletRequest request = getHttpServletRequest();
    	String jsDm = request.getParameter("jsDm");
    	
    	Role role =roleService.getRole(jsDm);
    	
    	request.setAttribute("role", role);
    	return SUCCESS;
    }
    
    /**
     * 修改role
     */
    public void updateRole(){
    	 Role rolePage = new Role();
         BeanUtils.copyProperties(model, rolePage);
         resetRole();

         JsonResult result = new JsonResult();
         result.setMsg("");
         result.setSuccess(true);
         try{
             roleService.updateRole(rolePage);
         }catch(Exception e){
             result.setMsg("");
             result.setSuccess(false);
             LOG.error("role", e);
         }
         printHttpServletResponse(new Gson().toJson(result));
    }
    
    /**
     * 删除角色
     */
    public void deleteRoles(){
    	String roleDms = getHttpServletRequest().getParameter("roleDms");
    	 JsonResult result = new JsonResult();
        
         try{
             roleService.deleteRoles(roleDms);
             result.setMsg("删除成功");
             result.setSuccess(true);
         }catch(Exception e){
             result.setMsg("删除失败");
             result.setSuccess(false);
             LOG.error("role", e);
         }
         printHttpServletResponse(new Gson().toJson(result));
    }

    /**
     * 获得角色已经分配的部门授权  by chudan.guo
     */
    public void getDeptGrant(){
        String roleId = getHttpServletRequest().getParameter("roleId");
        JsonResult jsonResult = new JsonResult();
        jsonResult.setSuccess(true);
        try{
            jsonResult.setMsg(roleService.getDeptGrant(roleId));

        }catch (Exception e){
            jsonResult.setSuccess(false);
            jsonResult.setMsg(null);
            LOG.error("role", e);
        }
        printHttpServletResponse(new Gson().toJson(jsonResult));
    }
    /**
     * 角色部门授权
     */
    public void changeDeptGrant(){
        String roleId = getHttpServletRequest().getParameter("roleId");//角色编号
        String ids = getHttpServletRequest().getParameter("ids");//部门编号

        JsonResult jsonResult = new JsonResult();
        jsonResult.setSuccess(true);
        try{
            roleService.changeDeptGrant(roleId, ids.split(","));
        }catch (Exception e){
            jsonResult.setSuccess(false);
            jsonResult.setMsg("授权失败");
            LOG.error("role", e);
        }
        printHttpServletResponse(new Gson().toJson(jsonResult));
    }
    
    /**
     * 设置是否分层
     * @author yuman.gao
     */
    public void setLayer(){
    	String setLayer = getHttpServletRequest().getParameter("setLayer");
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("sys_value", setLayer);
    	map.put("sys_key", "isLayer");
    	
    	JsonResult result = new JsonResult();
        
        try{
        	roleService.updateLayer(map);
            result.setMsg("设置成功");
            result.setSuccess(true);
        }catch(Exception e){
            result.setMsg("设置");
            result.setSuccess(false);
            LOG.error("role", e);
        }
        printHttpServletResponse(new Gson().toJson(result));
    }
    
    /**
     * 查询分层状态
     * @author yuman.gao
     */
    public void getLayer(){
    	boolean isLayer = roleService.isLayer();
		if(isLayer){
			printHttpServletResponse(new Gson().toJson(0));
		} else {
			printHttpServletResponse(new Gson().toJson(1));
		}
        
    }
}
