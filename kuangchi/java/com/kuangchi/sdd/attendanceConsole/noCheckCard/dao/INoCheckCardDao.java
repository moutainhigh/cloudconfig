package com.kuangchi.sdd.attendanceConsole.noCheckCard.dao;


import java.util.List;

import com.kuangchi.sdd.attendanceConsole.noCheckCard.model.NoCheckCard;
public interface INoCheckCardDao {

	public List<NoCheckCard> getAllNoCheckCardByStaff(NoCheckCard noCheck_info,String page, String size);//模糊查询免打卡所有信息
	public Integer getAllNoCheckCardByStaffCount(NoCheckCard noCheck_info);//查询总的行数
	public Boolean insertNoCheckCard(NoCheckCard noCheck_info);//新增员工免打卡信息
	public List<NoCheckCard> selectNoCheckCardByStaff(Integer id);//通过ID查免打卡信息
	public Boolean updateNoCheckCardByStaff(NoCheckCard noCheck_info);//修改免打卡信息
	public Integer deleNoCheckCardByStaff(String id);//删除免打卡信息
	public List<NoCheckCard> getNoCheckCardByStaffNum(NoCheckCard noCheck_info);//根据员工编号查询已存在的免打卡信息
	
	
	public List<NoCheckCard> selectNoCheckCardsByDept(NoCheckCard noCheck_info,String Page, String size);//模糊查询所有部门免打卡信息
	public Integer selectNoCheckCardsByDeptCount(NoCheckCard noCheck_info);//模糊查询所有部门免打卡信息总条数
	public int selectNoCheckCardByDeptNum(String dept_num);//通过部门编号查是否已存在部门
	public Boolean insertNoCheckCardByDept(NoCheckCard noCheck_info);//按部门新增免打卡信息
	public List<NoCheckCard> selectNoCheckCardByDept(Integer id);//通过ID查免打卡信息
	public Boolean updateNoCheckCardByDept(NoCheckCard noCheck_info);//按部门修改免打卡信息
	public Integer deleNoCheckCardByDept(String id);//删除免打卡信息
	public List<NoCheckCard> getNoCheckCardByDeptNum(NoCheckCard noCheck_info);//根据部门编号查询已存在的免打卡信息
	
	
    
}
