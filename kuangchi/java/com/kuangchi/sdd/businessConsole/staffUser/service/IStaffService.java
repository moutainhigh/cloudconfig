package com.kuangchi.sdd.businessConsole.staffUser.service;

import java.sql.SQLException;

import com.kuangchi.sdd.businessConsole.staffUser.model.Staff;

public interface IStaffService {

	/**
	 * @throws SQLException 
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-12 上午10:29:46
	 * @功能描述: 查询符合信息的员工
	 * @参数描述:
	 */
	Staff getLoginStaff(Staff staff) throws SQLException;

	/**
	 * @throws SQLException 
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-12 下午4:09:05
	 * @功能描述: 员工修改密码 
	 * @参数描述:
	 * @返回值　: 0 修改成功   1 密码错误    2 账号被冻结
	 */
	int modifyPassword(Staff staff, String new_password) throws SQLException;
}
