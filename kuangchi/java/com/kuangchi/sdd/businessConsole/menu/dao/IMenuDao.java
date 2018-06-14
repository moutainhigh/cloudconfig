package com.kuangchi.sdd.businessConsole.menu.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.businessConsole.menu.model.Menu;

public interface IMenuDao {

	List<Menu> getUserMenu(Map<String, Object> params);

	int menuHasChildren(Map<String, String> params);

	List<Menu> getSystemMenu(String pid);

	int sysMenuHasChildren(String menuPId);

	/**
	 * 父菜单的子菜单最大显示排序
	 * 
	 * @param fcdDm
	 *            父菜单代码
	 * @return
	 */
	String getMaxChildrenXspx(String fcdDm);

	void addNewMenu(Menu menuPage);

	List<Menu> getAllMenu(Map map);
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-7-5 15:30:00
	 * @功能描述: 根据菜单标记和用户代码查找未添加的所有菜单
	 */
	List<Menu> getMenuByFlag(String cdFlag, String userNum,String jsdm);
	
	List<Menu> getMenuByFlagA(String cdFlag, String userNum,String jsdm);

	void deleteUsersAdditionMenu(String yhDms);

	Menu getMenuDetail(String cmDm);

	void modifyMenu(Menu menu);

	void deleteMenu(String menuId);

	void deleteRoleMenu(String menuId);

	void deleteAdditionMenu(String menuId);

	List<Menu> getUserMenuSon(Map<String, Object> params);

	/**
	 * 获取选中菜单的父菜单 2016.7.1
	 * 
	 * @param menuId
	 */
	Integer getFatherDM(String menuId);

}
