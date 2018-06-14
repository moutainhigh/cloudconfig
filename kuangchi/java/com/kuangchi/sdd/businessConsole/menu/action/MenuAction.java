package com.kuangchi.sdd.businessConsole.menu.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.businessConsole.menu.model.Menu;
import com.kuangchi.sdd.businessConsole.menu.service.IMenuService;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("menuAction")
public class MenuAction extends BaseActionSupport {
	private static final Logger LOG = Logger.getLogger(MenuAction.class);

	private Menu model;

	public MenuAction() {
		model = new Menu();
	}

	private static final long serialVersionUID = -7598101673999344076L;

	@Resource(name = "menuServiceImpl")
	private IMenuService menuService;

	@Resource(name = "roleServiceImpl")
	private IRoleService roleService;
	
	@Override
	public Object getModel() {
		return model;
	}
	
	//跳转到系统管理页面
	public String toSystemPage(){
		//获取菜单标志
		String cdFlag=(String) getHttpServletRequest().getSession().getAttribute("cdFlags");
		if("2".equals(cdFlag)){
			return "door";
		}else if("3".equals(cdFlag)){
			return "attend";
		}else if("7".equals(cdFlag)){
			return "zigbee";
		}else{
			return "success";
		}
		
	}
	
	
	public void getUserMenu() {
		// 登录用户
		User loginUser = (User) getHttpSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		// 登录用户角色
		String roleId = getHttpServletRequest().getParameter("roleId");
		String cdFlag = getHttpServletRequest().getParameter("cdFlag");// 菜单所属的模块的标志
		if(cdFlag!=null){
			getHttpServletRequest().getSession().setAttribute("cdFlags", cdFlag);
		}
		/*
		 * if (null == cdFlag || "null".equals(cdFlag) || "".equals(cdFlag)) {
		 * cdFlag = "1"; }
		 */
		// 父菜单编号
		String menuPId = getHttpServletRequest().getParameter("menuPId");

		if (null == menuPId) {
			menuPId = GlobalConstant.USER_MENU_ROOT;
		}

		// 菜单层次
		String cdcc = getHttpServletRequest().getParameter("cdcc");

		if (null == cdcc || "".equals(cdcc)) {
			cdcc = "1";
		}
		List<Tree> menuTree = menuService.getUserMenu(loginUser.getYhDm(),
				roleId, menuPId, cdcc, cdFlag);

		printHttpServletResponse(new Gson().toJson(menuTree));
	}

	
	public void getSystemMenu() {
		String pid = getHttpServletRequest().getParameter("pid");
		if (null == pid) {
			pid = GlobalConstant.MENU_ROOT;
		}

		List<Menu> menus = menuService.getSystemMenu(pid);
		printHttpServletResponse(new Gson().toJson(menus));
	}

	public void addNewMenu() {
		Menu menuPage = new Menu();
		BeanUtils.copyProperties(model, menuPage);
		restModel();
		JsonResult result = new JsonResult();
		result.setMsg("");
		result.setSuccess(true);
		try {
			User loginUser = (User) getHttpServletRequest().getSession()
					.getAttribute(GlobalConstant.LOGIN_USER);
			menuPage.setLrryDm(loginUser.getYhDm());
			menuService.addNewMenu(menuPage);
		} catch (Exception e) {
			result.setMsg("");
			result.setSuccess(false);
			LOG.error("menu", e);
		}
		printHttpServletResponse(new Gson().toJson(result));
	}

	/**
	 * 获取所有菜单
	 */
	public void getAllMenu() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
		map.put("yhDm", user.getYhDm());
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
		if(isLayer){
			Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
			map.put("jsDm", role.getJsDm());
			
		} else {
			map.put("jsDm", "0");
		}
		
		Tree treeMenus = menuService.getAllMenu(GlobalConstant.USER_MENU_ROOT, 
				map);
		StringBuilder builder = new StringBuilder();

		builder.append("[");
		builder.append(new Gson().toJson(treeMenus));
		builder.append("]");
		printHttpServletResponse(builder.toString());
	}
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-7-5 15:30:00
	 * @功能描述: 根据菜单标记和用户代码查找未添加的所有菜单
	 */
	public void getMenuByFlag(){
		String CDFlag = getHttpServletRequest().getParameter("CDFlag");
		//String userNum = getHttpServletRequest().getParameter("userNum");
		 Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
		 User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
		 String userNum =user.getYhDm();
		 String jsdm=role.getJsDm();
		Tree treeMenus = menuService.getMenuByFlag(GlobalConstant.USER_MENU_ROOT,CDFlag,userNum,jsdm);
		
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(new Gson().toJson(treeMenus));
		builder.append("]");
		LOG.info(builder.toString());
		printHttpServletResponse(builder.toString());
	}

	public void getMenuByFlagA(){
		String CDFlag = getHttpServletRequest().getParameter("CDFlag");
		//String userNum = getHttpServletRequest().getParameter("userNum");
		Role role = (Role) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE);
		User user = (User) getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
		String userNum =user.getYhDm();
		String jsdm=role.getJsDm();
		Tree treeMenus = menuService.getMenuByFlagA(GlobalConstant.USER_MENU_ROOT,CDFlag,userNum,jsdm);
		
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(new Gson().toJson(treeMenus));
		builder.append("]");
		LOG.info(builder.toString());
		printHttpServletResponse(builder.toString());
	}
	
	
	

	/**
	 * 菜单详细信息
	 */
	public void menuDetails() {
		String cmDm = getHttpServletRequest().getParameter("cmDm");// 菜单代码

		printHttpServletResponse(GsonUtil.toJson(menuService
				.getMenuDetail(cmDm)));
	}

	/**
	 * 修改菜单
	 */
	public void modifyMenu() {
		Menu menuPage = new Menu();
		BeanUtils.copyProperties(model, menuPage);
		restModel();
		JsonResult result = new JsonResult();
		result.setMsg("");
		result.setSuccess(true);
		try {
			menuService.modifyMenu(menuPage);
		} catch (Exception e) {
			result.setMsg("");
			result.setSuccess(false);
			LOG.error("menu", e);
		}
		printHttpServletResponse(new Gson().toJson(result));
	}

	/**
	 * 删除菜单
	 */
	public void deleteMenu() {
		String menuId = getHttpServletRequest().getParameter("menuId");
		LOG.info(menuId);
		JsonResult result = new JsonResult();
		result.setMsg("");
		result.setSuccess(true);
		if (menuId.contains("'100'")) {
			result.setMsg("不能删除系统菜单");
			result.setSuccess(false);
		} else {
			try {
				menuService.deleteMenu(menuId);
			} catch (Exception e) {
				result.setMsg("");
				result.setSuccess(false);
				LOG.error("menu", e);
			}
		}

		printHttpServletResponse(new Gson().toJson(result));

	}

	private void restModel() {
		this.model = new Menu();
	}

	/**
	 * 获取选中菜单的父菜单2016-7-1
	 * */
	public void getFatherDM() {

		String menuId = getHttpServletRequest().getParameter("menuId");
		JsonResult result = new JsonResult();
		if (menuService.getFatherDM(menuId)) {
			result.setMsg("1");
			result.setSuccess(true);
		} else {
			result.setMsg("0");
			result.setSuccess(false);
		}

		printHttpServletResponse(new Gson().toJson(result));
	}

	/**
	 * 判断选中菜单是否为系统菜单2016-7-1
	 * */
	public void is_SysCD() {
		String menuId = getHttpServletRequest().getParameter("menuId");
		JsonResult result = new JsonResult();

		if ("100".equals(menuId)) {
			result.setMsg("1");
			result.setSuccess(true);
		} else {
			result.setMsg("0");
			result.setSuccess(false);
		}

		printHttpServletResponse(new Gson().toJson(result));
	}

}
