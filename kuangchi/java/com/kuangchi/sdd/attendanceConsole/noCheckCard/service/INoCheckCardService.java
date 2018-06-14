package com.kuangchi.sdd.attendanceConsole.noCheckCard.service;


import java.util.List;

import com.kuangchi.sdd.attendanceConsole.noCheckCard.model.NoCheckCard;
import com.kuangchi.sdd.base.model.easyui.Grid;

public interface INoCheckCardService {

	public Grid getAllNoCheckCardByStaff(NoCheckCard noCheck_info,String page, String size);//模糊查询所有员工免打卡信息
	public Boolean insertNoCheckCard(NoCheckCard noCheck_info);//新增员工免打卡信息
	public List<NoCheckCard> selectNoCheckCardByStaff(Integer id);//通过ID查免打卡信息
	public Boolean updateNoCheckCardByStaff(NoCheckCard noCheck_info);//修改免打卡信息
	public Integer deleNoCheckCardByStaff(String id,String create_user);//删除员工免打卡信息
	public Boolean getNoCheckCardByStaffNum(NoCheckCard noCheck_info);//根据员工编号查询已存在的免打卡信息
	
	public Grid getAllNoCheckCardByDept(NoCheckCard noCheck_info,String page, String size);//模糊查询所有部门免打卡信息
	public int selectNoCheckCardByDeptNum(String dept_num);//通过部门编号查是否已存在部门
	public Boolean insertNoCheckCardByDept(NoCheckCard noCheck_info);//按部门新增免打卡信息
	public List<NoCheckCard> selectNoCheckCardByDept(Integer id);//通过ID查免打卡信息
	public Boolean updateNoCheckCardByDept(NoCheckCard noCheck_info);//修改部门免打卡信息
	public Integer deleNoCheckCardByDept(String id,String create_user);//删除部门免打卡信息
	public Boolean getNoCheckCardByDeptNum(NoCheckCard noCheck_info);//根据部门编号查询已存在的免打卡信息
	
	
	}
