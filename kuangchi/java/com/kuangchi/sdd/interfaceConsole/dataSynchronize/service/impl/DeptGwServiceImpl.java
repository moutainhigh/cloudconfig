package com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.DeptGwDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.DeptGw;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.UserRole;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.DeptGwService;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.UUIDUtil;

@Transactional
@Service("deptGwServiceImpl")
public class DeptGwServiceImpl extends BaseServiceSupport implements DeptGwService {

	@Resource(name = "deptGwDaoImpl")
	private DeptGwDao deptGwDao;
	@Resource(name="LogDaoImpl")
	private LogDao logDao;

	@Override
	public void addDeptGw(DeptGw deptGw) {
		deptGw.setUuid(UUIDUtil.uuidStr());
		deptGw.setLr_sj(DateUtil.getSysTimestamp());
		deptGwDao.addDeptGw(deptGw);
	}

	@Override
	public void delDeptGw(String deptNum) {
		deptGwDao.delDeptGw(deptNum);
		
	}
	

	
	

}
