package com.kuangchi.sdd.businessConsole.menu.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.businessConsole.menu.model.Menu;

public interface IMenuService {

	/**
	 * 登录用户菜单
	 * 
	 * @param userId
	 *            用户代码
	 * @param roleId
	 *            用户角色
	 * @param menuPId
	 *            父菜单编号
	 * @return
	 */
	List<Tree> getUserMenu(String userId, String roleId, String menuPId,
			String cdcc, String cdFlag);

	List<Menu> getSystemMenu(String pid);

	void addNewMenu(Menu menuPage);

	Tree getAllMenu(String rootId, Map map);
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-7-5 15:30:00
	 * @功能描述: 根据菜单标记和用户代码查找未添加的所有菜单
	 */
	Tree getMenuByFlag(String rootId,String CDFlag,String userNum,String jsdm);
	
	Tree getMenuByFlagA(String rootId,String CDFlag,String userNum,String jsdm);

	/**
	 * 菜单详情
	 * 
	 * @param cmDm
	 * @return
	 */
	Menu getMenuDetail(String cmDm);

	/**
	 * 修改菜单
	 * 
	 * @param menu
	 */
	void modifyMenu(Menu menu);

	/**
	 * 删除菜单
	 * 
	 * @param menuId
	 */
	void deleteMenu(String menuId);

	/**
	 * 获取选中菜单的父菜单 2016.7.1
	 * 
	 * @param menuId
	 */
	Boolean getFatherDM(String menuId);

}
