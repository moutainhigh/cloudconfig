package com.kuangchi.sdd.doorAccessConsole.synchronizeFromComm.service;

public interface AllSynchronizeService {
	
	/**
	 * 上传并同步权限信息
	 * by gengji.yang
	 */
	public void uploadAndSynchronizeLimit(String deviceNum,String cardNum,String createUser);

}
