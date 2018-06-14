package com.kuangchi.sdd.attendanceConsole.dutyuser.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.dutyuser.model.DutyUser;
/**
 * @创建人　: 邓积辉
 * @创建时间: 2016-4-27 上午11:37:40
 * @功能描述:员工排班维护模块-Dao类
 */
public interface DutyUserDao {
	/**按条件查询员工排班*/
	List<DutyUser> getDutyUserByParam(DutyUser dutyUser);
	
	/**按条件查询员工排班(分页)*/
	List<DutyUser> getDutyUserByParamPage(DutyUser dutyUser);
	
	/**按条件查询员工排班总数*/
	int getDutyUserByParamPageCounts(DutyUser dutyUser);
	
	/**新增员工排班*/
	boolean insertDutyUser(List<DutyUser> dutyUser);
	
	/**删除员工排班*/
	boolean deleteDutyUserById(String dutyUser_ids);
	
	/**删除部门排班*/
	boolean deleteDutyDeptById(String dutyUser_ids);
	
	/**修改员工排班*/
	boolean updateDutyUser(DutyUser dutyUser);
	
	/**修改部门排班*/
	boolean updateDutyDept(DutyUser dutyUser);
	
	/**按id查看员工排班信息*/
	DutyUser getDutyUserById(String dutyUser_id);
	
	/**根据时间条件返回员工班次的条数*/
	Integer getDutyUserByParamCounts(DutyUser dutyUser);

	/**根据排班类型名称查排班id*/
	Integer getDutyIdByDutyName(String duty_name);

	/**根据员工代码查询数量*/
	Integer getCountByStaffNum(String staff_num);

	/**根据时间条件返回员工班次的条数（id不等时）*/
	Integer getDutyUserCountsExceptId(DutyUser dutyUser);
	
	/**根据排班类型查询是否有员工排班*/
	Integer getDutyUserByDutyId(String duty_id);
	
	/**查出指定日期以后的排班list by gengji.yang*/
	public List<DutyUser> getDutyUserAfterToday(String staffNum,String today);
	
	/**查处指定日期以后的部门排班 list by gengji.yang*/
	public List<DutyUser> getDeptDutyUserAfterToday(String deptNum,String today);
	
	/**查出跨了当前日期时间的排班list by gengji.yang*/
	public DutyUser getDutyUserAmongToday(String staffNum,String today);
	
	/**查出跨了当前日期时间的部门排班list by gengji.yang*/
	public DutyUser getDutyDeptAmongToday(String deptNum,String today);
	
	public void insertOneDutyUser(DutyUser dutyUser);
	
	public void insertOneDutyDept(DutyUser dutyUser);
	
	
	/**
	 * 根据部门代码筛选出选中部门的所有员工
	 * @param DeptDM 部门代码
	 * xuewen.deng 2016.6.7
	 */
	public List<Map> getAllUserByDept_DM(String DeptDM);
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-4 下午5:39:40
	 * @功能描述: 获取全部班次名称
	 * @参数描述:
	 */
	public List<String> getAllDutyName();

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-4 下午5:39:40
	 * @功能描述: 获取全部员工工号
	 * @参数描述:
	 */
	public List<String> getAllStaffNo(String layerDeptNum);
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-8-31
	 * @功能描述: 通过员工编号获取排班类型ID
	 * @参数描述:
	 */
    public DutyUser  getDutyIdByStaffNum(Map<String, Object> map);
    
    /*--------------------------------------部门排班后台语句起   by  huixian.pan--------------------------------------------------*/
    /*查询部门排班信息（导出用）*/
    public  List<Map> getDutyDeptByParam(Map map);
    /*查询部门排班信息（分页）*/
    public  List<Map> getDutyDeptByParamPage(Map map);
    /*查询部门排班信息行数*/
    public  Integer countDutyDeptByParam(Map map);
    /*根据id查询部门排班*/
    public  Map getDutyDeptById(String id);
	/*根据部门代码查询部门所有员工信息与排班id*/
	public List<Map> getStaffMessByDept_DM(String DeptDM);
	/*通过id修改员工排班结束日期 */
	public boolean updateDutyUserEndDate(DutyUser dutyUser);
	/*通过id修改部门排班结束日期 */
	public boolean updateDutyDeptEndDate(DutyUser dutyUser);
    /*--------------------------------------部门排班后台语句止--------------------------------------------------*/
	
	
	/**
	 * @创建人　: 巫建辉
	 * @创建时间: 2016-12-22
	 * @功能描述: 获取某一时间点的用户排班
	 * @参数描述:
	 */
	public DutyUser selectDutyByTimeAndStaffNum(String yeahMonthDayHourMinute,String staff_num);
	
	
	/**
	 * @创建人　: 巫建辉
	 * @创建时间: 2016-12-22
	 * @功能描述: 更新排班的结束时间
	 * @参数描述:
	 */
	public void updateDutyUserEndTime(String yeahMonthDayHourMinute,Integer dutyUserId,String dept_num);
	
	
	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-12-27
	 * @功能描述: 通过排班类型id获取未来排班信息
	 * @参数描述:
	 */
    public List<Map>  getFutureDutysByDutyId(Map map);
    
    /**
     * @创建人　: 潘卉贤
     * @创建时间: 2016-12-27
     * @功能描述: 通过排班类型id获取以往排班信息
     * @参数描述:
     */
    public List<Map>  getBeforeDutysByDutyId(Map map);
    
    /**
     * @创建人　: 潘卉贤
     * @创建时间: 2016-12-27
     * @功能描述: 通过排班信息id更新排班类型 
     * @参数描述:
     */
    public boolean updDutyId(Map map);
	
	
}
