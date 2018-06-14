package com.kuangchi.sdd.commConsole.fire.service;

/**
 * 获取门禁控制器的接口服务
 * @author 
 *
 */
public interface IFrieService {
	/**
	 * 
	 * Description:设置消防
	 * date:2016年5月11日
	 * @return
	 */
	public String setFire(String mac,String useFireFighting,String sign);
}
