package com.kuangchi.sdd.interfaceConsole.cardSender.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.interfaceConsole.cardSender.dao.EmpCardSenSyncDao;
import com.kuangchi.sdd.interfaceConsole.cardSender.model.EmployeeSyncModel;
import com.kuangchi.sdd.interfaceConsole.cardSender.service.EmpCardSenSyncService;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.StaffGwDao;

@Transactional
@Service("empCardSenSyncServiceImpl")
public class EmpCardSenSyncServiceImpl extends BaseServiceSupport implements
		EmpCardSenSyncService {

	@Resource(name = "empCardSenSyncDaoImpl")
	private EmpCardSenSyncDao empCardSenSyncDao;

	@Resource(name = "staffGwDaoImpl")
	private StaffGwDao staffGwDao;

	@Resource(name = "LogDaoImpl")
	private LogDao logDao;

	@Override
	public int addEmployeeSync(EmployeeSyncModel employeeSync) {
		this.empCardSenSyncDao.addEmployeeSync(employeeSync);
		return 1;
	}

	@Override
	public int modifyEmployeeSync(EmployeeSyncModel employeeSync) {
		this.empCardSenSyncDao.modifyEmployeeSync(employeeSync);
		return 1;
	}

	@Override
	public void delEmployeeSync(String bsId) {
		empCardSenSyncDao.delEmployeeSync(bsId);

	}

	@Override
	public EmployeeSyncModel defaultMC_DM(String deptNum) {

		return empCardSenSyncDao.defaultMC_DM(deptNum);
	}

	@Override
	public void delEmpFromDeptNum(String deptNum) {
		empCardSenSyncDao.delEmpFromDeptNum(deptNum);

	}

	@Override
	public String getStaffNumByID(String bsId) {

		return empCardSenSyncDao.getStaffNumByID(bsId);
	}

	@Override
	public EmployeeSyncModel getDeptNumByEmpID(String staff_id) {
		return empCardSenSyncDao.getDeptNumByEmpID(staff_id);
	}

	@Override
	public Integer getStaffCountByStaff_no(String staff_no) {

		return empCardSenSyncDao.getStaffCountByStaff_no(staff_no);
	}

	@Override
	public Integer getDeptCount(String bm_dm) {
		return empCardSenSyncDao.getDeptCount(bm_dm);
	}

	@Override
	public List<EmployeeSyncModel> getAllEmp() {

		return empCardSenSyncDao.getAllEmp();
	}

	@Override
	public String getDNumByBmDm(String bm_dm) {
		return empCardSenSyncDao.getDNumByBmDm(bm_dm);
	}

	@Override
	public boolean isStaffBoundCard(String staffNum) {
		if (empCardSenSyncDao.isStaffBoundCard(staffNum) > 0) {
			return true;
		} else {
			return false;
		}
	}

}
