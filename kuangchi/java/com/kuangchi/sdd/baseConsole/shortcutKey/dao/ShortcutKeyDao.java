package com.kuangchi.sdd.baseConsole.shortcutKey.dao;

import java.util.List;

import com.kuangchi.sdd.baseConsole.shortcutKey.model.ShortcutKey;
import com.kuangchi.sdd.businessConsole.menu.model.Menu;

public interface ShortcutKeyDao {
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-7-5 13:59:00
	 * @功能描述: 添加快捷键
	 */
	public boolean addShortcutKey(ShortcutKey shortcutKey);
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-7-5 14:00：00
	 * @功能描述: 根据id删除快捷键
	 */
	public boolean delShortcutKey(String shortcutKeyID);
	
	/**
	 * @创建人　: chudan.guo
	 * @创建时间: 2016-7-6 10:30：00
	 * @功能描述: 根据菜单编号 查出菜单代码，名称，url
	 */
	public Menu getMenuByID(String cd_dm);
	/**
	 * chudan.guo
	 * 根据用户编号和菜单标记查找已有的快捷菜单
	 */
	public List<ShortcutKey> getShortcutKeys(String yh_dm,String CDFalg,String jsdm);
}
