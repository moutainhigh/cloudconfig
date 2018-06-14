package com.kuangchi.sdd.attendanceConsole.attend.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.attend.model.AttendModel;
import com.kuangchi.sdd.attendanceConsole.attend.model.PunchCardModel;
import com.kuangchi.sdd.attendanceConsole.duty.model.Duty;
public interface IAttendHandleDao {
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-4-28 上午10:03:12
	 * @功能描述: 查询个人全部考勤信息
	 * @参数描述: 
	 */
	public List<AttendModel> getMyAttend(AttendModel attendModel); 
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-3 下午3:32:30
	 * @功能描述: 查询个人全部考勤信息总数
	 * @参数描述:
	 */
	public Integer getMyAttendCount(AttendModel attendModel); 
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-4-28 上午11:20:45
	 * @功能描述: 查询指定员工考勤信息
	 * @参数描述:
	 */
	public List<AttendModel> getAllAttend(AttendModel attendModel);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-3 下午3:33:14
	 * @功能描述: 查询指定员工考勤信息总数
	 * @参数描述:
	 */
	public Integer getAllAttendCount(AttendModel attendModel);
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-4-28 下午5:21:34
	 * @功能描述: 增加考勤信息
	 * @参数描述:
	 */
	public void addAttendInfo(AttendModel attendModel);
	
	public void addDutyAttendInfo(PunchCardModel handle);//新增补打卡记录
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-4-29 下午1:45:18
	 * @功能描述: 删除考勤信息
	 * @参数描述:
	 */
	public void deleteAttendInfoById(String ids);
	/**
	 * @创建人　:陈凯颖
	 * @创建时间: 2016-5-26  下午1:45:18
	 * @功能描述: 备份后删除月记录
	 * @参数描述:
	 */
	public void deleteAttendInfoByMonth(List<AttendModel> attendList);
	
	/**
	 * @throws SQLException 
	 * @创建人　: 高育漫
	 * @创建时间: 2016-5-4 下午3:56:25
	 * @功能描述: 查询员工班次信息
	 * @参数描述:
	 */
	public Duty getDutyInfo(AttendModel attendModel) throws SQLException;
	
	
	public void delNoElaExceRecord(String staffNum,String startTime,String endTime);
	
	public void delElaExceRecord(String staffNum,String timePoint);
	
	public List<Map> getUpElaExceptionList(String staffNum,String endTime);
	
	public List<Map> getDownElaExceptionList(String staffNum,String startTime);
	
	
	 public List<PunchCardModel> getDutyUserStaffById(PunchCardModel handle,String Page,String row);//根据id查询员工排班信息guibo.chen
	 
	 public Integer getDutyUserStaffByIdCount(PunchCardModel handle);//根据id查询员工排班信息条数guibo.chen
	 
	 public List<PunchCardModel> getdutyId();//查询默认班次guibo.chen
	 
	 public List<PunchCardModel> getdutyTimeById(String id);//查询非弹性班次guibo.chen
	 
	 public String getIsElasticById(String id);//查询是否是弹性时间的标志位
	 
	 public PunchCardModel getCheckTimeById(String id);//查询打卡区间
	 
	 public List<PunchCardModel> getPunchCardInfo(PunchCardModel handle,String Page,String row);//查询补打卡信息
	 
	 public Integer getPunchCardInfoCount(PunchCardModel handle);//查询补打卡信息
	 
	/**
	 * 删除分区考勤记录
	 * @author yuman.gao
	 */
	public boolean removeAreaRecord(String partition_name);
	
	public void PunchCardsAdd(List<PunchCardModel> punchList) ;//批量补打卡
	
	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-4-28 上午11:20:45
	 * @功能描述: 查询指定员工考勤信息（不分页）
	 * @参数描述:
	 */
	public List<AttendModel> getAllExportAttend(AttendModel attendModel);
	
	/**
	 * 根据员工工号和名称查询员工（用于检查该员工信息是否存在）
	 * by yuman.gao
	 */
	public Map getStaffByNO(String staff_no, String staff_name);
	
	}
 