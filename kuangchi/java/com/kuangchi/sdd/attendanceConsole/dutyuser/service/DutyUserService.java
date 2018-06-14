package com.kuangchi.sdd.attendanceConsole.dutyuser.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.attendanceConsole.duty.model.Duty;
import com.kuangchi.sdd.attendanceConsole.dutyuser.model.DutyUser;
import com.kuangchi.sdd.base.model.easyui.Grid;

/**
 * @创建人　: 邓积辉
 * @创建时间: 2016-4-27 上午11:43:01
 * @功能描述:员工排班维护模块-业务类
 */
public interface DutyUserService {
	/**按条件查询员工排班*/
	List<DutyUser> getDutyUserByParam(DutyUser dutyUser);
	
	/**按条件查询员工排班(分页)*/
	Grid<DutyUser> getDutyUserByParamPage(DutyUser dutyUser);
	
	/**修改员工排班*/
	boolean updateDutyUser(DutyUser dutyUser,String create_user);
	
	/**修改员工排班(Action调用封装方法)
	 * @throws Exception */
	void updDutyUser(DutyUser dutyUser,String create_user,String today) throws Exception;
	
	
	/**修改员工排班(修改的排班日期跨天的情况)(Action调用封装方法)
	 * @throws Exception */
	void updDutyUserAmountToday(DutyUser updDateDutyUser,DutyUser insertDutyUser,String create_user,String today) throws Exception;

	/**删除员工排班*/
	boolean deleteDutyUserById(String dutyUser_ids,String create_user);
	
	/**删除部门排班*/
	public boolean deleteDutyDeptById(String dutyDept_ids,String create_user);
	
	/**新增员工排班*/
	public boolean insertDutyUser(List<DutyUser> dutyUser,String today,String  create_user);
	
	/**新增部门排班 */
	public boolean insertDutyDept(DutyUser dutyUser,String today,String  create_user);
	
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
	
	/**
	 *  可以解决冲突的 新增  个人  排班方法
	 * 		create by gengji.yang
	 * @param staffNum
	 * @param today 2016-04-01 06:00
	 * @param newDutyUser  欲新增的排班 排班对象
	 */
	public void insertNewDutyWithoutConflict(String staffNum,String today,DutyUser newDutyUser)throws Exception;
	
	/**
	 * 可以解决冲突的 新增 部门 排班 方法
	 * @param deptNum
	 * @param today
	 * @param newDutyUser
	 * @throws Exception
	 */
	public void insertNewDeptDutyWithoutConflict(String deptNum,String today,DutyUser newDutyUser)throws Exception;
	
	
	/**
	 * 根据部门代码筛选出选中部门的所有员工
	 * @param DeptDM 部门代码
	 * xuewen.deng 2016.6.7
	 */
	 
	 public List<Map> getAllUserByDept_DM(String DeptDM);
	
	 /**
	  * @创建人　: 邓积辉
	  * @创建时间: 2016-8-4 下午5:37:55
	  * @功能描述: 获取全部班次名称
	  * @参数描述:
	  */
	 public List<String> getAllDutyName();

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-9 下午5:42:17
	 * @功能描述:获取全部员工工号 
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
    
    /**
     * 新员工继承部门排班
     * by gengji.yang
     */
    public void getDutyFromDept(String deptNum,String staffNum);
    
    
    /*--------------------------------------部门排班后台语句起--------------------------------------------------*/
    /*查询部门排班信息*/
    public  Grid getDutyDeptByParamPage(Map map);
    /*查询部门排班信息（导出用）*/
    public  List<Map> getDutyDeptByParam(Map map);
    /*根据id查询部门排班*/
    public  Map getDutyDeptById(String ids);
    /*修改部门排班信息*/
    public  void  updDutyDept(DutyUser dutyDept,List<DutyUser> dutyUsers,String create_user,String today) throws Exception;
	/*根据部门代码查询部门所有员工信息与排班id*/
	public List<Map> getStaffMessByDept_DM(String DeptDM);
	/*删除部门以及部门下员工排班信息*/
	public boolean  deleteDutyDept(String staffIds,String deptIds,String create_user);
	/*通过id修改员工排班结束日期 */
	boolean updateDutyUserEndDate(List<DutyUser> dutyUserList,String deleteIds,String create_user) throws Exception ;
	/*通过id修改部门排班结束日期 */
	boolean updateDutyDeptEndDate(List<DutyUser> dutyUserList,String deleteIds,String create_user) throws Exception ;
    /*--------------------------------------部门排班后台语句止--------------------------------------------------*/
	/**
	 * @创建人　: 巫建辉
	 * @创建时间: 2016-12-22
	 * @功能描述: 分开员工排班信息
	 * @参数描述:yeahMonthDayHourMinute   yyyy-MM-dd HH:mm 格式
	 */
	public void splitDutyUserByTimePoint(String yeahMonthDayHourMinute,String staff_num,String dept_num);

	
	
	/**
     * @创建人　: 潘卉贤
     * @创建时间: 2016-12-27
     * @功能描述: 修改默认排班接口
     * @参数描述: oldDutyId(原来默认排班类型id)  newDutyId(新的默认排班类型id)  
     */
    public void  editDefaultDutyReg(String oldDutyId,String newDutyId) throws Exception;
}
