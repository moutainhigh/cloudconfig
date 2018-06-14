package com.kuangchi.sdd.interfaceConsole.dataSynchronize.service;

import java.util.List;

import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.RoleSyncModel;

public interface RoleSyncService {

	/**
	 * 获取所有角色
	 */
	public List<RoleSyncModel> getAllRoleSync();
	
}
