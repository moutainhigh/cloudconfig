package com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.EmployeeSyncDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.StaffGwDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.EmployeeSyncModel;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.EmployeeSyncService;

@Transactional
@Service("employeeSyncServiceImpl")
public class EmployeeSyncServiceImpl extends BaseServiceSupport implements EmployeeSyncService {

	@Resource(name = "employeeSyncDaoImpl")
	private EmployeeSyncDao employeeSyncDao;
	
	@Resource(name = "staffGwDaoImpl")
	private StaffGwDao staffGwDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	

	@Override
	public String getDeptNum(String deptID) {
		return employeeSyncDao.getDeptNum(deptID);
	}

	@Override
	public int addEmployeeSync(EmployeeSyncModel employeeSync) {
		int resultCode;// 0 表示新增失败    1 成功
		int count=this.employeeSyncDao.getCountByStaffNum(employeeSync.getStaff_no()).size();
		if(count==0){
			resultCode=1;
			this.employeeSyncDao.addEmployeeSync(employeeSync);
		}else{
			resultCode=0;
		}
        return resultCode;
	}

	@Override
	public int modifyEmployeeSync(EmployeeSyncModel employeeSync) {
		int resultCode;// 0 表示失败    1 成功
		int count=this.employeeSyncDao.getCountByID_StaffNum(employeeSync.getRemote_staff_id(),employeeSync.getStaff_no()).size();
		if(count==0){
			resultCode=1;
			this.employeeSyncDao.modifyEmployeeSync(employeeSync);
		}else{
			resultCode=0;
		}
        return resultCode;
		
	}

	@Override
	public void delEmployeeSync(String remoteStaffIds) {
		employeeSyncDao.delEmployeeSync(remoteStaffIds);
		
	}
	@Override
	public EmployeeSyncModel defaultMC_DM(String deptNum) {
		
		return employeeSyncDao.defaultMC_DM(deptNum);
	}

	@Override
	public void delEmpFromDeptNum(String deptNum) {
		employeeSyncDao.delEmpFromDeptNum(deptNum);
		
	}

	@Override
	public String getStaffNumByID(String remote_staff_id) {
		
		return employeeSyncDao.getStaffNumByID(remote_staff_id);
	}

	@Override
	public EmployeeSyncModel getDeptNumByEmpID(String staff_id) {
		return employeeSyncDao.getDeptNumByEmpID(staff_id);
	}

	
	

}
