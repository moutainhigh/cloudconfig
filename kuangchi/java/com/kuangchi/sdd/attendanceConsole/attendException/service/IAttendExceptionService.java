package com.kuangchi.sdd.attendanceConsole.attendException.service;

import java.util.List;

import com.kuangchi.sdd.attendanceConsole.attendException.model.AttendException;
import com.kuangchi.sdd.attendanceConsole.attendException.model.Param;
import com.kuangchi.sdd.attendanceConsole.attendException.model.ToEmailAddr;
import com.kuangchi.sdd.base.model.easyui.Grid;

public interface IAttendExceptionService {
	/**
	 * Description:查询所有考勤异常信息
	 * date:2016年5月5日
	 * @param 
	 */
	Grid getAllAttExcpByParam(Param param,String page, String size);
	
	/**
	 * Description:发送邮件
	 * date:2016年5月16日
	 * @param 
	 */
	boolean sendEmailById(String id);
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
	 * Description:设置是否启用定时发送邮件
	 * date:2016年6月12日
	 * @param time：每天定时发送邮件的时间
	 */
	boolean setSendMailRegular(String time);
	
	/**
	 * Description:导出异常考勤信息
	 * @param param
	 * @return
	 */
	List<AttendException> exportAttExcpByParam(Param param);
}
