package com.kuangchi.sdd.businessConsole.cron.dao;

import com.kuangchi.sdd.businessConsole.cron.model.Cron;

public interface ICronDao {

	/**
	 * 显示IP地址
	 * 
	 * @return
	 * @author minting.he
	 */
	public Cron selectIP(String sys_key);

	/**
	 * 更改页面定时任务执行的ip地址
	 * 
	 * @param cron
	 * @return
	 * @author minting.he
	 */
	public boolean updateCronIP(Cron cron);

	/**
	 * 比较IP地址是否相同
	 * 
	 * @param cron
	 * @return
	 * @author minting.he
	 */
	public Integer compareIP(Cron cron);
}
