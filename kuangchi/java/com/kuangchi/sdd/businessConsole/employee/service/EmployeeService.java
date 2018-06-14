package com.kuangchi.sdd.businessConsole.employee.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.card.model.Card;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.employee.model.BoundCard;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.businessConsole.employee.model.EmployeeTreeNode;
import com.kuangchi.sdd.businessConsole.employee.model.PaperType;
import com.kuangchi.sdd.businessConsole.user.model.User;

/**
 * Created by jianhui.wu on 2016/2/15.
 */
public interface EmployeeService {
	public Employee getEmployeeDetail(String id);

	public boolean modifyEmployee(Employee employee, User loginUser);

	public void addEmployee(Employee employee);

	public JsonResult validatEmployee(String yhNo, String yhDm);

	public void deleteGyDm(String empIds);

	public Grid<Employee> getEmployeeBybmDm(DepartmentPage departmentPage);

	public Grid<Employee> searchEmployee(DepartmentPage departmentPage);

	public List<PaperType> getPaperTypes();

	/**
	 * @param 分层部门代码
	 */
	public List<Employee> getAllEmployee(String layerDeptNum);

	public List<Employee> getAllWorkingEmployee(String layerDeptNum);

	public List<EmployeeTreeNode> getAllEmployeeTreeNode();

	public void batchAddEmployee(List<Employee> employeeList) throws Exception;

	public int isContainYhDM(String yhNo, String yhDm);

	public Grid<Card> selectAllCards(Map map);

	public boolean isExistUserAndCard(String card_num, String yhdm);

	public boolean insertCardBound(Map<String, String> map);

	public Grid<BoundCard> selectBoundCardByYHDM(String yhdm);

	public Grid<BoundCard> selectBoundCardByYHDM352(String yhdm);

	public Grid<BoundCard> selectBoundCardByStaffNo(String yhdm);

	public Grid<BoundCard> selectBoundCardByStaffNo2(String yhdm);

	// 根据条件查询员工信息guibo.chen
	public List<Employee> getEmployeeBybmDmAndstaffNum(
			DepartmentPage departmentPage);

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-7-28 下午8:54:06
	 * @功能描述: 根据部门代码获取默认对应的岗位代码
	 * @参数描述:
	 */
	public String getDefaultGwDmByBmDm(String bmDm);

	public List<String> getAllStaffDept(String layerDeptNum);// 获取所有部门编号

	public List<String> getAllStaffDeptNum(String layerDeptNum);// 获取所有部门代码xuewen.deng2017-05-15

	/**
	 * 获取 不含源卡的员工绑定卡 by gengji.yang
	 * 
	 * @param map
	 *            含有 yhdm="",copyCardNumsArr=""
	 * @return
	 */
	public Grid<BoundCard> selectBoundCardByForCopy(Map map);

	public boolean updateLeader(Employee employee);

	public boolean updateStation(Employee employee);

	public String selectStaffNum(String staff_no);

	/**
	 * 给员工和卡添加默认账户
	 * 
	 * @param card_id
	 * @param card_num
	 * @param create_time
	 * @param account_num
	 * @param create_user
	 * @return
	 */
	public boolean addAccountToCard(String card_num, String create_time,
			String[] account_num, String create_user);

	/**
	 * 判断员工绑定的卡下是否有绑定账户
	 * 
	 * @param staff_num
	 * @param account_num
	 * @return
	 */
	public JsonResult isExistAccountUnderBoundCard(String yhdm,
			String[] account_num, String card_num);

	/**
	 * 给员工添加默认账户
	 * 
	 * @param staff_num
	 * @param create_time
	 * @param create_user
	 * @return
	 */
	public boolean addAccountToEmployee(String staff_num, String create_time,
			String create_user);

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-16 下午8:11:13
	 * @功能描述: 根据用户工号重置用户密码
	 * @参数描述:
	 */
	public boolean resetPassword(Map map);

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-19 上午10:04:18
	 * @功能描述:校验用户是否有绑定邮箱
	 * @参数描述:
	 */
	public Employee checkStaffMailByStaffNo(String staffNo);

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-19 上午10:04:18
	 * @功能描述:根据用户工号绑定邮箱
	 * @参数描述:
	 */
	public boolean insertStaffMailByStaffNo(Map map);

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-22 上午11:43:53
	 * @功能描述: 确认用户邮箱是否唯一
	 * @参数描述:
	 */
	public Integer checkStaffMailUnique(String staffMail);

	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-05
	 * @功能描述: 通过设备编号获取是否允许透支信息
	 * @参数描述: staff_num
	 */
	public String isOverDraft(String staff_num);

	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-05
	 * @功能描述: 通过员工编号获取支持消费卡的数量
	 * @参数描述: staff_num
	 */
	public Integer consumeCardCount(String staff_num);

	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-05
	 * @功能描述: 判断该卡是否已设置为消费卡
	 * @参数描述: card_num
	 */
	public String isConsumeCard(String card_num);

	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-05
	 * @功能描述: 通过卡号设置消费卡
	 * @参数描述: card_num,support_consume,create_user,create_time
	 */
	public boolean setConsumeCard(String card_num, String support_consume,
			String create_user, String create_time);

	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-07
	 * @功能描述: 设置员工透支权限并修改卡消费权限
	 * @参数描述: 
	 *        over_draft,staff_num,unCheckCard_num,card_num,create_user,create_time
	 */
	public boolean setOverDraftRights2(String over_draft, String staff_num,
			String unCheckCard_num, String card_num, String create_user,
			String create_time);

	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-07
	 * @功能描述: 设置员工透支权限
	 * @参数描述: over_draft,staff_num,create_user,create_time
	 */
	public boolean setOverDraftRights1(String over_draft, String staff_num,
			String create_user, String create_time);

	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-07
	 * @功能描述: 通过员工编号获取支持消费卡的卡号
	 * @参数描述: staff_num
	 */
	public List<String> consumeCardNum(String staff_num);

	public String getBmdmByBmNo(String bmNo);

	/**
	 * 获取所有的部门编号
	 * 
	 * @author minting.he
	 * @return
	 */
	public List<String> getAllDeptNo();

	public String selectStaffNoRule();// 查询员工工号编码规则

	public boolean selectEmpByNum(String finalStaffNo1);

	public void updateEmpNoTemp(String finalStaffNo, String finalStaffNo1);

	public Grid<Employee> searchEmployee2(DepartmentPage departmentPage);

	public void deleteAgricultureEmployee(String subdept);

	public EmployeeTreeNode getEmployeeByStaffNo(String staff_no);

	public Grid<BoundCard> selectBoundCardInfoByYHDM2(String yhdm);
}
