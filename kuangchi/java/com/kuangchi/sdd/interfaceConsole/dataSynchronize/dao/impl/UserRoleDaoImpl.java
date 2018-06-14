package com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.UserRoleDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.UserRole;

@Repository("userRoleDaoImpl")
public class UserRoleDaoImpl extends BaseDaoImpl<UserRole> implements UserRoleDao {

	@Override
	public String getNameSpace() {
		return "common.UserRole";
	}
	@Override
	public String getTableName() {
		return null;
	}
	@Override
	public void addUserRole(UserRole userRole) {
		getSqlMapClientTemplate().insert("addUser_Role", userRole);
	}
	@Override
	public void delUserRole(String userName) {
		//单个删除
		 getSqlMapClientTemplate().delete("delUserRole",userName);
		
	}

}
