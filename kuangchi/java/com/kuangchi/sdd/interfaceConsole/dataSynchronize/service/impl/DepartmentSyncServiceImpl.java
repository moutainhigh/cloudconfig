package com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.department.model.Department;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao.DepartmentSyncDao;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.DepartmentSyncModel;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.DepartmentSyncService;
import com.kuangchi.sdd.util.commonUtil.UUIDUtil;

@Transactional
@Service("departmentSyncServiceImpl")
public class DepartmentSyncServiceImpl extends BaseServiceSupport implements DepartmentSyncService {

	@Resource(name = "departmentSyncDaoImpl")
	private DepartmentSyncDao departmentSyncDao;
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public int addDepartmentSync(DepartmentSyncModel departmentSync) {
		int resultCode;// 0 表示新增失败    1 成功
		int count=this.departmentSyncDao.getCountByBm_Dm(departmentSync.getBm_no()).size();
		
		if(count==0){
			resultCode=1;
			this.departmentSyncDao.addDepartmentSync(departmentSync);
		}else{
			resultCode=0;
		}
        return resultCode;
	}

	@Override
	public int modifyDepartmentSync(DepartmentSyncModel departmentSync) {
		int resultCode;// 0 表示失败    1 成功
		int count=this.departmentSyncDao.getCountByID_Bm_Dm(departmentSync.getRemote_department_id(),departmentSync.getBm_no()).size();
		if(count==0){
			resultCode=1;
			this.departmentSyncDao.modifyDepartmentSync(departmentSync);
		}else{
			resultCode=0;
		}
        return resultCode;

	}

	@Override
	public void delDepartmentSync(String remoteDepartmentIds) {
		departmentSyncDao.delDepartmentSync(remoteDepartmentIds);
	}

	@Override
	public String getParentDM(String parentID) {
		return departmentSyncDao.getParentDM(parentID);
	}

	@Override
	public String getDeptNum(String deptID) {
		return departmentSyncDao.getDeptNum(deptID);
	}
	

	
	

}
