package com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao;

import java.util.List;

import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.RoleSyncModel;

public interface RoleSyncDao {

	/**
	 * 获取所有角色
	 */
	public List<RoleSyncModel> getAllRoleSync();
	
}
