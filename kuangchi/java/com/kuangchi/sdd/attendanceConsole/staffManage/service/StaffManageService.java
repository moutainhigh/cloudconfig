package com.kuangchi.sdd.attendanceConsole.staffManage.service;

import com.kuangchi.sdd.attendanceConsole.staffManage.model.StaffManage;
import com.kuangchi.sdd.base.model.easyui.Grid;

public interface StaffManageService {
	/**
	 * 查询所有员工账号信息
	 * @param staffManage
	 * @param page
	 * @param size
	 * @return
	 */
	public Grid getAllStaff(StaffManage staffManage,String page,String size );
	/**
	 * 修改员工密码
	 * @param staffManage
	 */
	public void modifyStaffPassword(StaffManage staffManage,String LoginUser);
	/**
	 * 根据员工编号查找员工密码
	 * @param staff_num
	 * @return
	 */
	public String getStaffPassword(String staff_num);
}
