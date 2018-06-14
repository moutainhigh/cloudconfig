package com.kuangchi.sdd.interfaceConsole.dataSynchronize.dao;

import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.StaffGw;

public interface StaffGwDao {

	/**
	 * 添加员工岗位
	 * @param staffGw
	 */
	public void addStaffGw(StaffGw staffGw);
	/**
	 * 删除员工岗位
	 * @param staff_num
	 */
	public void delStaffGw(String staff_num);
	
}
