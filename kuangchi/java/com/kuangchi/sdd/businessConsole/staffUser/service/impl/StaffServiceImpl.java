package com.kuangchi.sdd.businessConsole.staffUser.service.impl;


import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.businessConsole.staffUser.dao.IStaffDao;
import com.kuangchi.sdd.businessConsole.staffUser.model.Staff;
import com.kuangchi.sdd.businessConsole.staffUser.service.IStaffService;


@Transactional
@Service("staffServiceImpl")
public class StaffServiceImpl extends BaseServiceSupport implements IStaffService {
	
	@Resource(name = "staffDaoImpl")
	private IStaffDao staffDao;

	//查询符合信息的员工
	public Staff getLoginStaff(Staff staff) throws SQLException {
		return staffDao.getLoginStaff(staff);
	}

	//员工修改密码   return 0 修改成功   1 密码错误    2 账号被冻结
	public int modifyPassword(Staff staff, String new_password) throws SQLException {
		Staff loginStaff = getLoginStaff(staff);
		if(loginStaff == null)
			return 1;
		if(loginStaff.getStaff_state().equals("2"))
			return 2;
		staff.setStaff_password(new_password);
		staffDao.modifyPassword(staff);
		return 0;
	}
	
	
}
