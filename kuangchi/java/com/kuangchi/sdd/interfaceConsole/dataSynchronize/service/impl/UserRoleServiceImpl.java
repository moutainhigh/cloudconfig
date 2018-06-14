package com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.UserRoleDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.UserRole;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.UserRoleService;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.UUIDUtil;

@Transactional
@Service("userRoleServiceImpl")
public class UserRoleServiceImpl extends BaseServiceSupport implements UserRoleService {

	@Resource(name = "userRoleDaoImpl")
	private UserRoleDao userRoleDao;
	@Resource(name="LogDaoImpl")
	private LogDao logDao;

	@Override
	public void addUserRole(UserRole userRole) {
		userRole.setLr_sj(DateUtil.getSysTimestamp()); 
		userRole.setUuid(UUIDUtil.uuidStr());
		userRoleDao.addUserRole(userRole);
		
	}

	@Override
	public void delUserRole(String userName) {
		userRoleDao.delUserRole(userName);
		
	}
	

	
	

}
