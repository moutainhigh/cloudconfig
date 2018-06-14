package com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.RoleSyncDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.RoleSyncModel;

@Repository("roleSyncDaoImpl")
public class RoleSyncDaoImpl extends BaseDaoImpl<RoleSyncModel> implements RoleSyncDao {

	@Override
	public String getNameSpace() {
		return "common.RoleSync";
	}

	@Override
	public String getTableName() {
		return null;
	}
	

	@Override
	public List<RoleSyncModel> getAllRoleSync() {
		return  this.getSqlMapClientTemplate().queryForList("getAllRoleSync");
		
	}

}
