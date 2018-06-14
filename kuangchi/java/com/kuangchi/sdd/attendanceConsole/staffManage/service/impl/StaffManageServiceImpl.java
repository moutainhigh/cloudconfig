package com.kuangchi.sdd.attendanceConsole.staffManage.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.attendanceConsole.staffManage.dao.StaffManageDao;
import com.kuangchi.sdd.attendanceConsole.staffManage.model.StaffManage;
import com.kuangchi.sdd.attendanceConsole.staffManage.service.StaffManageService;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;

@Transactional
@Service("staffManageServiceImpl")
public class StaffManageServiceImpl implements StaffManageService {

	@Resource(name="staffManageDaoImpl")
	private StaffManageDao staffManageDao;
	
	@Resource(name="LogDaoImpl")
	private LogDao logDao;
	
	@Override
	public Grid getAllStaff(StaffManage staffManage, String page,String size) {
		int count=staffManageDao.getAllStaffCount(staffManage);
		List<StaffManage>  allStaff=staffManageDao.getAllStaff(staffManage, page, size);
		Grid grid=new Grid();
		grid.setTotal(count);
		grid.setRows(allStaff);
		return grid;
	}

	@Override
	public void modifyStaffPassword(StaffManage staffManage,String LoginUser) {
		this.staffManageDao.modifyStaffPassword(staffManage);
		Map<String, String> log = new HashMap<String, String>();
        log.put("V_OP_NAME", "员工账号管理");
        log.put("V_OP_FUNCTION", "修改");
        log.put("V_OP_ID", LoginUser);
        log.put("V_OP_TYPE", "业务");
        log.put("V_OP_MSG", "修改员工密码");
        logDao.addLog(log);		
		
	}

	@Override
	public String getStaffPassword(String staff_num) {
		return staffManageDao.getStaffPassword(staff_num);
	}

}
