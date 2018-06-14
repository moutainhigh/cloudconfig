package com.kuangchi.sdd.baseConsole.shortcutKey.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.shortcutKey.dao.ShortcutKeyDao;
import com.kuangchi.sdd.baseConsole.shortcutKey.model.ShortcutKey;
import com.kuangchi.sdd.businessConsole.menu.model.Menu;

@Repository("shortcutKeyDaoImpl")
public class ShortcutKeyDaoImpl extends BaseDaoImpl<ShortcutKey> implements ShortcutKeyDao {

	@Override
	public String getNameSpace() {
		return "common.ShortcutKey";
	}

	@Override
	public String getTableName() {
		return null;
	}
	
	@Override
	public boolean addShortcutKey(ShortcutKey shortcutKey) {
		return insert("addShortcutKey", shortcutKey);

	}

	@Override
	public boolean delShortcutKey(String shortcutKeyID) {
		return delete("delShortcutKey",shortcutKeyID);

	}

	@Override
	public Menu getMenuByID(String cd_dm) {
		return (Menu) getSqlMapClientTemplate().queryForObject("getMenuByID", cd_dm);
	}

	@Override
	public List<ShortcutKey> getShortcutKeys(String yh_dm, String CDFlag,String jsdm) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("yh_dm", yh_dm);
		mapParam.put("CDFlag", CDFlag);
		mapParam.put("js_dm", jsdm);
		List<ShortcutKey>  shortcutKey=getSqlMapClientTemplate().queryForList("getShortcutKeys", mapParam);
		return shortcutKey;
	}

}
