package com.kuangchi.sdd.attendanceConsole.staffManage.dao;

import java.util.List;

import com.kuangchi.sdd.attendanceConsole.staffManage.model.StaffManage;

public interface StaffManageDao {
	/**
	 * 查询所有员工账号信息
	 * @param staffManage
	 * @param page
	 * @param size
	 * @return
	 */
	public List<StaffManage> getAllStaff(StaffManage staffManage,String page,String size );
	/**
	 * 查询员工总数量
	 * @param staffManage
	 * @return
	 */
	public int getAllStaffCount(StaffManage staffManage);
	/**
	 * 修改员工密码
	 * @param staffManage
	 */
	public void modifyStaffPassword(StaffManage staffManage);
	/**
	 * 根据员工编号查找员工密码
	 * @param staff_num
	 * @return
	 */
	public String getStaffPassword(String staff_num);
}
