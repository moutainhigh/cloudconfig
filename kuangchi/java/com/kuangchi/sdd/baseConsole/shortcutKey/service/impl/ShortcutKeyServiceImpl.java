package com.kuangchi.sdd.baseConsole.shortcutKey.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.baseConsole.shortcutKey.dao.ShortcutKeyDao;
import com.kuangchi.sdd.baseConsole.shortcutKey.model.ShortcutKey;
import com.kuangchi.sdd.baseConsole.shortcutKey.service.ShortcutKeyService;
import com.kuangchi.sdd.businessConsole.menu.model.Menu;

@Transactional
@Service("shortcutKeyServiceImpl")
public class ShortcutKeyServiceImpl extends BaseServiceSupport implements ShortcutKeyService {
	
	@Resource(name = "shortcutKeyDaoImpl")
	private ShortcutKeyDao shortcutKeyDao;
	
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public void addShortcutKey(ShortcutKey shortcutKey,String loginUser) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = shortcutKeyDao.addShortcutKey(shortcutKey);
		log.put("V_OP_NAME", "添加快捷菜单");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", loginUser);
		log.put("V_OP_MSG", "添加快捷菜单信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
	}

	@Override
	public void delShortcutKey(String shortcutKeyID,String loginUser) {
		Map<String, String> log = new HashMap<String, String>();
		boolean result = shortcutKeyDao.delShortcutKey(shortcutKeyID);
		log.put("V_OP_NAME", "删除快捷菜单");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", loginUser);
		log.put("V_OP_MSG", "删除快捷菜单信息");
		if(result){
			log.put("V_OP_TYPE", "业务");
		}else{
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
	}

	@Override
	public Menu getMenuByID(String cd_dm) {
		return shortcutKeyDao.getMenuByID(cd_dm);
	}

	@Override
	public List<ShortcutKey> getShortcutKeys(String yh_dm, String CDFlag,String jsdm) {
		return shortcutKeyDao.getShortcutKeys(yh_dm, CDFlag,jsdm);
	}

}
