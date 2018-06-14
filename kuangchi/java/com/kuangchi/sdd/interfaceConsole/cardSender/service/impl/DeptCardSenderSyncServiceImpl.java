package com.kuangchi.sdd.interfaceConsole.cardSender.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.interfaceConsole.cardSender.dao.DeptCardSenderSyncDao;
import com.kuangchi.sdd.interfaceConsole.cardSender.model.DepartmentSyncModel;
import com.kuangchi.sdd.interfaceConsole.cardSender.service.DeptCardSenderSyncService;

@Transactional
@Service("deptCardSenderSyncServiceImpl")
public class DeptCardSenderSyncServiceImpl extends BaseServiceSupport implements
		DeptCardSenderSyncService {

	@Resource(name = "deptCardSenderSyncDaoImpl")
	private DeptCardSenderSyncDao deptCardSenderSyncDao;
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;

	@Override
	public int addDepartmentSync(DepartmentSyncModel departmentSync) {
		// int resultCode;// 0 表示新增失败 1 成功
		// int count = this.deptCardSenderSyncDao.getCountByBm_Dm(
		// departmentSync.getBm_no()).size();
		//
		// if (count == 0) {
		// resultCode = 1;
		// this.deptCardSenderSyncDao.addDepartmentSync(departmentSync);
		// } else {
		// resultCode = 0;
		// }
		// return resultCode;
		this.deptCardSenderSyncDao.addDepartmentSync(departmentSync);
		return 1;
	}

	@Override
	public int modifyDepartmentSync(DepartmentSyncModel departmentSync) {
		this.deptCardSenderSyncDao.modifyDepartmentSync(departmentSync);
		return 1;

	}

	@Override
	public void delDepartmentSync(String ids) {
		deptCardSenderSyncDao.delDepartmentSync(ids);
	}

	@Override
	public Integer isExistParentDM(String parentDM) {
		return deptCardSenderSyncDao.isExistParentDM(parentDM);
	}

	@Override
	public String getDeptNum(String deptID) {
		return deptCardSenderSyncDao.getDeptNum(deptID);
	}

	@Override
	public List<DepartmentSyncModel> getAllBm() {

		return deptCardSenderSyncDao.getAllBm();
	}

	@Override
	public Integer isExistDeptNo(String deptNo) {
		return deptCardSenderSyncDao.isExistDeptNo(deptNo);
	}

	@Override
	public Integer getEmpCountByBmDm(String bsId) {
		return deptCardSenderSyncDao.getEmpCountByBmDm(bsId);
	}

}
