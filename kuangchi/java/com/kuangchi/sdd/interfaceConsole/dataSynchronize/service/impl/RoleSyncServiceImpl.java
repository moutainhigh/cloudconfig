package com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.RoleSyncDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.RoleSyncModel;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.RoleSyncService;

@Transactional
@Service("roleSyncServiceImpl")
public class RoleSyncServiceImpl extends BaseServiceSupport implements RoleSyncService {

	@Resource(name = "roleSyncDaoImpl")
	private RoleSyncDao roleSyncDao;
	@Resource(name="LogDaoImpl")
	private LogDao logDao;

	@Override
	public List<RoleSyncModel> getAllRoleSync() {
		return roleSyncDao.getAllRoleSync();
	}
	

	
	

}
