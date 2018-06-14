package com.kuangchi.sdd.businessConsole.menu.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.businessConsole.menu.dao.IMenuDao;
import com.kuangchi.sdd.businessConsole.menu.model.Menu;

@Repository("menuDaoImpl")
public class MenuDaoImpl extends BaseDaoImpl<Menu> implements IMenuDao {

	@Override
	public String getNameSpace() {

		return "common.Menu";
	}

	@Override
	public String getTableName() {

		return null;
	}

	public List<Menu> getUserMenu(Map<String, Object> params) {

		return queryForList("getUserMenu", params);
	}

	public int menuHasChildren(Map<String, String> params) {
		return queryCount("menuHasChildren", params);
	}

	public List<Menu> getSystemMenu(String pid) {
		return queryForList("getSystemMenu", pid);
	}

	public int sysMenuHasChildren(String menuPId) {

		return queryCount("sysMenuHasChildren", menuPId);
	}

	public String getMaxChildrenXspx(String fcdDm) {
		return (String) getSqlMapClientTemplate().queryForObject(
				"getMaxChildrenXspx", fcdDm);
	}

	public void addNewMenu(Menu menuPage) {
		getSqlMapClientTemplate().insert("addNewMenu", menuPage);

	}

	@Override
	public List<Menu> getAllMenu(Map map) {
		return this.getSqlMapClientTemplate().queryForList("getAllMenu", map);
	}
	
	@Override
	public List<Menu> getMenuByFlag(String CDFlag, String userNum,String jsdm) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("cdFlag", CDFlag);
		mapParam.put("yh_dm", userNum);
		mapParam.put("jsdm", jsdm);
		return this.getSqlMapClientTemplate().queryForList("getMenuByFlag", mapParam);
	}
	
	@Override
	public List<Menu> getMenuByFlagA(String CDFlag, String userNum,String jsdm) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("cdFlag", CDFlag);
		mapParam.put("yh_dm", userNum);
		mapParam.put("jsdm", jsdm);
		return this.getSqlMapClientTemplate().queryForList("getMenuByFlagA", mapParam);
	}

	@Override
	public void deleteUsersAdditionMenu(String yhDms) {
		getSqlMapClientTemplate().delete("deleteUsersAdditionMenu", yhDms);

	}

	@Override
	public Menu getMenuDetail(String cmDm) {

		return (Menu) getSqlMapClientTemplate().queryForObject("getMenuDetail",
				cmDm);
	}

	@Override
	public void modifyMenu(Menu menu) {
		getSqlMapClientTemplate().update("modifyMenu", menu);

	}

	@Override
	public void deleteMenu(String menuId) {
		getSqlMapClientTemplate().update("deleteMenu", menuId);

	}

	@Override
	public void deleteRoleMenu(String menuId) {
		getSqlMapClientTemplate().delete("deleteMenuRole", menuId);

	}

	@Override
	public void deleteAdditionMenu(String menuId) {
		getSqlMapClientTemplate().delete("deleteAdditionMenu", menuId);
	}

	@Override
	public List<Menu> getUserMenuSon(Map<String, Object> params) {
		return queryForList("getUserMenuSon", params);
	}

	@Override
	public Integer getFatherDM(String menuId) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"getFatherDM", menuId);
	}

	

}
