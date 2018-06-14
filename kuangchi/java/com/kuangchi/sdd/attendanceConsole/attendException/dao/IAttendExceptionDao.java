package com.kuangchi.sdd.attendanceConsole.attendException.dao;

import java.util.List;

import com.kuangchi.sdd.attendanceConsole.attendException.model.AttendException;
import com.kuangchi.sdd.attendanceConsole.attendException.model.Param;
import com.kuangchi.sdd.attendanceConsole.attendException.model.ToEmailAddr;


public interface IAttendExceptionDao {
	/**
	 * 
	 * Description:获得符合条件的考勤异常信息的条数
	 * date:2016年5月5日
	 * @param param
	 * @return
	 */
	public Integer getAllAttExcCount(Param param);
	
	/**
	 * 
	 * Description:获得符合条件的考勤异常信息
	 * date:2016年5月5日
	 * @param param
	 * @return
	 */
	List<AttendException> getAllAttExcpByParam(Param param,String page, String size);
	
	
	/**
	 * Description:根据员工编号获得Email地址
	 * date:2016年5月16日
	 * @param 
	 */
	List<ToEmailAddr> getEmailAddr(String staff_num);
	
	/**
	 * Description:设置标识符，标识为已发送邮件
	 * date:2016年5月17日
	 * @param 
	 */
	void setDealState(String staff_num);
	
	/**
	 * Description:根据参数(查询条件)获取员工卡号
	 * date:2016年5月17日
	 * @param 
	 */
	List<String> getStaffNumByParam(Param param);
	
	
	/**
	 * Description:导出符合条件的考勤异常信息
	 * @param param
	 * @return
	 */
	List<AttendException> exportAttExcpByParam(Param param);
}
