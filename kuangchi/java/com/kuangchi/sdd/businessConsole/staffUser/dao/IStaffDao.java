package com.kuangchi.sdd.businessConsole.staffUser.dao;

import java.sql.SQLException;

import com.kuangchi.sdd.businessConsole.staffUser.model.Staff;


public interface IStaffDao {
	
	/**
	 * @throws SQLException 
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-12 上午9:53:26
	 * @功能描述:	查询符合信息的员工
	 * @参数描述:
	 */
	Staff getLoginStaff(Staff staff) throws SQLException;
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-12 下午3:08:18
	 * @功能描述: 员工修改密码
	 * @参数描述:
	 */
	void modifyPassword(Staff staff);
}
