package com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.StaffGwDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.StaffGw;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.StaffGwService;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.UUIDUtil;

@Transactional
@Service("staffGwServiceImpl")
public class StaffGwServiceImpl extends BaseServiceSupport implements StaffGwService {

	@Resource(name = "staffGwDaoImpl")
	private StaffGwDao staffGwDao;
	@Resource(name="LogDaoImpl")
	private LogDao logDao;


	@Override
	public void addStaffGw(StaffGw staffGw) {
		staffGw.setLr_sj(DateUtil.getSysTimestamp());
		staffGw.setUuid(UUIDUtil.uuidStr());
		staffGwDao.addStaffGw(staffGw);
	}

	@Override
	public void delStaffGw(String staff_num) {
		staffGwDao.delStaffGw(staff_num);
		
	}


	
	

}
