package com.kuangchi.sdd.businessConsole.cron.service;

import com.kuangchi.sdd.businessConsole.cron.model.Cron;

public interface ICronService {
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
	public boolean updateCronIP(Cron cron, String create_user);

	/**
	 * 比较服务端IP和数据库IP地址是否相同
	 * 
	 * @return
	 * @author minting.he
	 */
	public boolean compareIP();
}
