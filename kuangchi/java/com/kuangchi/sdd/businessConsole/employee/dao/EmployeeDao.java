package com.kuangchi.sdd.businessConsole.employee.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.baseConsole.card.model.Card;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.employee.model.BoundCard;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.businessConsole.employee.model.EmployeePosition;
import com.kuangchi.sdd.businessConsole.employee.model.EmployeeTreeNode;
import com.kuangchi.sdd.businessConsole.employee.model.PaperType;

/**
 * Created by jianhui.wu on 2016/2/15.
 */
public interface EmployeeDao {

	/**
	 * @param 分层部门代码
	 */
	public List<Employee> getAllEmployee(String layerDeptNum);

	public List<Employee> getAllWorkingEmployee(String layerDeptNum);

	public List<EmployeeTreeNode> getAllEmployeeTreeNode();

	public List<EmployeeTreeNode> getAllEmployeeTreeNodeA(String deptNum);

	public List<EmployeeTreeNode> getEmployeeTreeNodeByBmDm(String bmDm);

	public Employee getEmployeeDetail(String id);

	public void modifyEmployee(Employee employee);

	public void addEmployee(Employee employee);

	public int isContainYhDM(String yhNo, String yhDm);

	public boolean deleteGyDm(String empIds);

	public List<Employee> getEmployeeBybmDm(DepartmentPage departmentPage);

	public Integer getEmployeeCountBybmDm(DepartmentPage departmentPage);

	public List<Employee> searchEmployee(DepartmentPage departmentPage);

	public Integer searchEmployeeCount(DepartmentPage departmentPage);

	public void insertEmployeePosition(EmployeePosition employeePosition);

	public EmployeePosition getEmployeePositionByYhDm(String yhDm);

	public void deleteEmployeePositionByYhDm(String yhDm);

	public List<PaperType> getPaperTypes();

	public void batchAddEmployee(List<Employee> employeeList) throws Exception;

	public List<Card> selectAllCards(Map map);

	public int getAllCardsCount(Map map);

	public boolean isExistUserAndCard(String card_num, String yhdm);

	public Integer isExistUser(String yhdm);

	public Integer isExistCard(String card_num);

	public boolean insertCardBound(Map<String, String> map);

	public void updateStateDM(Card card);

	public List<BoundCard> selectBoundCardByYHDM(String yhdm);

	public List<BoundCard> selectBoundCardInfoByYHDM2(String yhdm);

	public List<BoundCard> selectBoundCardByYHDM352(String yhdm);

	public List<BoundCard> selectBoundCardByStaffNo(String yhdm);

	public List<BoundCard> selectBoundCardByStaffNo2(String yhdm);

	public List<BoundCard> selectBoundCardByForCopy(Map map);

	public boolean updateLeader(Employee employee);

	public boolean updateStation(Employee employee);

	public String selectStaffNum(String staff_no);

	public List<String> getEmployeIds(String deptIds);

	// 根据条件查询员工信息guibo.chen
	public List<Employee> getEmployeeBybmDmAndstaffNum(
			DepartmentPage departmentPage);

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-7-28 下午8:52:19
	 * @功能描述: 根据部门代码获取默认对应的岗位代码
	 * @参数描述:
	 */
	public String getDefaultGwDmByBmDm(String bmDm);

	/* 获取所有部门编号 */
	public List<String> getAllStaffDept(String layerDeptNum);

	/**
	 * 获取所有部门代码xuewen.deng 2017-05-15
	 * */
	public List<String> getAllStaffDeptNum(String layerDeptNum);

	/**
	 * 给员工添加默认账号
	 * 
	 * @param staff_num
	 * @param create_time
	 * @param create_user
	 * @return
	 */
	public boolean addAccountToEmployee(String staff_num, String create_time,
			String account_type_num, String create_user);

	/**
	 * 给卡添加默认账号
	 * 
	 * @param card_id
	 * @param card_num
	 * @param create_time
	 * @param account_num
	 * @param create_user
	 * @return
	 */
	public boolean addAccountToCard(String card_num, String create_time,
			String account_num, String create_user);

	public List<BoundCard> getAccountUnderBoundCard(String staff_num);

	public String getStaffNumbyStaffNo(String staff_no);

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-16 下午8:12:28
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

	public boolean insertStaffMailByStaffNo(Map map);

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-22 上午11:40:48
	 * @功能描述:确认用户邮箱是否唯一
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
	 * @参数描述: map
	 */
	public boolean setConsumeCard(Map map);

	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-07
	 * @功能描述: 设置员工透支权限
	 * @参数描述: map
	 */
	public boolean setOverDraftRights(Map map);

	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-07
	 * @功能描述: 改变员工绑定卡的消费卡设置
	 * @参数描述: map
	 */
	public boolean changeConsumeCard(Map map);

	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-07
	 * @功能描述: 通过员工编号获取支持消费卡的卡号
	 * @参数描述: staff_num
	 */
	public List<String> consumeCardNum(String staff_num);

	/**
	 * 删除员工前是否退卡、报损
	 * 
	 * @author minting.he
	 * @param empIds
	 * @return
	 */
	public Integer ifUnbound(String empIds);

	public String getBmdmByBmNo(String bmNo);

	/**
	 * 获取员工是否在职
	 */
	public Integer getCount_Hire_Staff(Employee e);

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

	public List<Employee> searchEmployee2(DepartmentPage departmentPage);

	public Integer searchEmployeeCount2(DepartmentPage departmentPage);

	public void deleteAgricultureEmployee(String subdept);

	public EmployeeTreeNode getEmployeeByStaffNo(String staff_no);
}
