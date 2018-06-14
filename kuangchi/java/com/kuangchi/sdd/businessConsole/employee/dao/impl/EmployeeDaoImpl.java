package com.kuangchi.sdd.businessConsole.employee.dao.impl;

import java.sql.BatchUpdateException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.card.model.Card;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.employee.dao.EmployeeDao;
import com.kuangchi.sdd.businessConsole.employee.model.BoundCard;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.businessConsole.employee.model.EmployeePosition;
import com.kuangchi.sdd.businessConsole.employee.model.EmployeeTreeNode;
import com.kuangchi.sdd.businessConsole.employee.model.PaperType;
import com.kuangchi.sdd.consumeConsole.consumeGroupMap.dao.IGroupMapDao;
import com.kuangchi.sdd.consumeConsole.consumeGroupMap.model.GroupMapModel;
import com.kuangchi.sdd.util.commonUtil.DateUtil;

/**
 * Created by jianhui.wu on 2016/2/15.
 */
@Repository("employeeDao")
public class EmployeeDaoImpl extends BaseDaoImpl<Object> implements EmployeeDao {
	@Override
	public String getNameSpace() {
		return "common.Employee";
	}

	@Resource(name = "groupMapDaoImpl")
	private IGroupMapDao groupMapDao;

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<Employee> getAllEmployee(String layerDeptNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("layerDeptNum", layerDeptNum);
		return this.getSqlMapClientTemplate().queryForList("getAllEmployee",
				map);
	}

	@Override
	public List<Employee> getAllWorkingEmployee(String layerDeptNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("layerDeptNum", layerDeptNum);
		map.put("staff_hire_state", "0");
		return this.getSqlMapClientTemplate().queryForList("getAllEmployee",
				map);
	}

	@Override
	public Employee getEmployeeDetail(String id) {
		Map map = new HashMap();
		map.put("id", id);
		return (Employee) this.getSqlMapClientTemplate().queryForObject(
				"getEmployeeDetail", map);
	}

	@Override
	public void modifyEmployee(Employee employee) {
		getSqlMapClientTemplate().update("modifyEmployee", employee);
	}

	@Override
	public void addEmployee(Employee employee) {
		getSqlMapClientTemplate().insert("addEmployee", employee);
	}

	@Override
	public int isContainYhDM(String yhNo, String yhDm) {
		Map map = new HashMap();
		map.put("yhNo", yhNo);
		map.put("yhDm", yhDm);
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"yhDmCount", map);
	}

	@Override
	public boolean deleteGyDm(String empIds) {
		Integer count = ifUnbound(empIds);
		if (count != 0) { // 有未退卡状态
			return false;
		} else {
			getSqlMapClientTemplate().delete("deleteGyDm", empIds);
		}
		return true;
	}

	@Override
	public List<Employee> getEmployeeBybmDm(DepartmentPage departmentPage) {
		return this.getSqlMapClientTemplate().queryForList("getEmployeeBybmDm",
				departmentPage);
	}

	@Override
	public Integer getEmployeeCountBybmDm(DepartmentPage departmentPage) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"getEmployeeCountBybmDm", departmentPage);
	}

	@Override
	public List<Employee> searchEmployee(DepartmentPage departmentPage) {
		return this.getSqlMapClientTemplate().queryForList("searchEmployees",
				departmentPage);
	}

	@Override
	public List<Employee> searchEmployee2(DepartmentPage departmentPage) {
		return this.getSqlMapClientTemplate().queryForList("searchEmployee2",
				departmentPage);
	}

	@Override
	public Integer searchEmployeeCount(DepartmentPage departmentPage) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"searchEmployeeCounts1", departmentPage);
	}

	@Override
	public Integer searchEmployeeCount2(DepartmentPage departmentPage) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"searchEmployeeCount2", departmentPage);
	}

	@Override
	public void insertEmployeePosition(EmployeePosition employeePosition) {
		this.getSqlMapClientTemplate().insert("insertEmployeePosition",
				employeePosition);
	}

	@Override
	public EmployeePosition getEmployeePositionByYhDm(String yhDm) {
		return (EmployeePosition) this.getSqlMapClientTemplate()
				.queryForObject("getEmployeePositionByYhDm", yhDm);
	}

	@Override
	public void deleteEmployeePositionByYhDm(String yhDm) {
		this.getSqlMapClientTemplate().delete("deleteEmployeePositionByYhDm",
				yhDm);
	}

	@Override
	public List<PaperType> getPaperTypes() {

		return this.getSqlMapClientTemplate().queryForList("getPaperTypes");
	}

	// 批量导入员工信息
	@Override
	public void batchAddEmployee(List<Employee> employeeList) throws Exception {
		SqlMapClient sqlMapClient = getSqlMapClient();
		try {
			sqlMapClient.startBatch();
			sqlMapClient.startTransaction();

			for (int i = 0; i < employeeList.size(); i++) {
				Employee employee = employeeList.get(i);
				String leader_num = selectStaffNum(employee.getSjldyhNo());
				employee.setSjldyhDm(leader_num);
				sqlMapClient.insert("addEmployee", employee);
				EmployeePosition employeePosition = new EmployeePosition();
				employeePosition.setGwDm(employee.getGwDm());
				employeePosition.setYhDm(employee.getYhDm());
				sqlMapClient.insert("insertEmployeePosition", employeePosition);

				// 添加到默认消费组
				GroupMapModel groupMap = new GroupMapModel();
				groupMap.setStaff_num(employee.getYhDm());
				groupMap.setGroup_num("1");
				groupMapDao.addGroupMap(groupMap);
			}
			sqlMapClient.commitTransaction();
		} catch (BatchUpdateException e) {
			try {
				sqlMapClient.getCurrentConnection().rollback();
				sqlMapClient.commitTransaction();
			} catch (Exception e2) {
				throw new Exception("导入失败!");
			}
			e.printStackTrace();
			throw new Exception("导入失败，请检查是否包含已经存在的员工，部门是否存在!");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("导入失败!");

		}

	}

	@Override
	public List<EmployeeTreeNode> getAllEmployeeTreeNode() {
		return getSqlMapClientTemplate().queryForList("getAllEmployeeTreeNode");
	}

	@Override
	public List<EmployeeTreeNode> getAllEmployeeTreeNodeA(String deptNum) {
		return getSqlMapClientTemplate().queryForList(
				"getAllEmployeeTreeNodeA", deptNum);
	}

	// 查询所有卡信息
	@Override
	public List<Card> selectAllCards(Map map) {
		Integer page = Integer.parseInt((String) map.get("page"));
		Integer rows = Integer.parseInt((String) map.get("rows"));

		Integer skip = (page - 1) * rows;
		map.put("skip", skip);
		return this.getSqlMapClientTemplate().queryForList("selectAllCards1",
				map);
	}

	// 插入到卡绑定表
	@Override
	public boolean insertCardBound(Map<String, String> map) {
		if (getSqlMapClientTemplate().insert("insertCardBound", map) != null) {
			return false;
		} else {
			String card_num = map.get("card_num");
			Card card = new Card();
			Map<String, String> map2 = new HashMap<String, String>();
			map2.put("cardNum", card_num);
			map2.put("state_dm", "10");
			map2.put("date",
					DateUtil.getDateString(new Date(), "yyyy-MM-dd HH:mm:ss"));
			getSqlMapClientTemplate().insert("addCardHistory2", map2);
			card.setCard_num(card_num);
			updateStateDM(card);
			return true;
		}
	}

	@Override
	public int getAllCardsCount(Map map) {

		return queryCount("getAllCardsCount1", map);
	}

	@Override
	public void updateStateDM(Card card) {
		getSqlMapClientTemplate().update("updateStateDM", card);
	}

	@Override
	public List<BoundCard> selectBoundCardByYHDM(String yhdm) {

		return this.getSqlMapClientTemplate().queryForList(
				"selectBoundCardByYHDM", yhdm);
	}

	@Override
	public List<BoundCard> selectBoundCardByYHDM352(String yhdm) {

		return this.getSqlMapClientTemplate().queryForList(
				"selectBoundCardByYHDM352", yhdm);
	}

	@Override
	public List<BoundCard> selectBoundCardByStaffNo(String yhdm) {

		return this.getSqlMapClientTemplate().queryForList(
				"selectBoundCardByStaffNo", yhdm);
	}

	@Override
	public List<BoundCard> selectBoundCardByStaffNo2(String yhdm) {

		return this.getSqlMapClientTemplate().queryForList(
				"selectBoundCardByStaffNo2", yhdm);
	}

	@Override
	public boolean isExistUserAndCard(String card_num, String yhdm) {
		if (isExistUser(yhdm) > 0 && isExistCard(card_num) > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Integer isExistUser(String yhdm) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"isExistUser", yhdm);
	}

	@Override
	public Integer isExistCard(String card_num) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"isExistCard", card_num);
	}

	@Override
	public List<BoundCard> selectBoundCardByForCopy(Map map) {
		return getSqlMapClientTemplate().queryForList(
				"selectBoundCardByForCopy", map);
	}

	@Override
	public boolean updateLeader(Employee employee) {
		return update("updateLeader", employee);
	}

	@Override
	public boolean updateStation(Employee employee) {
		return update("updateStation", employee);
	}

	@Override
	public String selectStaffNum(String staff_no) {
		Map map = new HashMap();
		map.put("staff_no", staff_no);
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"selectStaffNum", map);
	}

	@Override
	public List<EmployeeTreeNode> getEmployeeTreeNodeByBmDm(String bmDm) {
		Map map = new HashMap();
		map.put("bmDm", bmDm);
		return getSqlMapClientTemplate().queryForList(
				"getEmployeeTreeNodeByBmDm", map);
	}

	@Override
	public List<String> getEmployeIds(String deptIds) {
		Map map = new HashMap();
		map.put("deptIds", deptIds);
		return getSqlMapClientTemplate().queryForList("getEmployeIds", map);
	}

	@Override
	public boolean addAccountToEmployee(String staff_num, String create_time,
			String account_type_num, String create_user) {
		Map map = new HashMap();
		map.put("staff_num", staff_num);
		map.put("account_type_num", account_type_num);
		map.put("create_time", create_time);
		map.put("create_user", create_user);
		return insert("addAccountToEmployee", map);
	}

	@Override
	public boolean addAccountToCard(String card_num, String create_time,
			String account_num, String create_user) {
		Map map = new HashMap();
		map.put("card_num", card_num);
		map.put("account_num", account_num);
		map.put("create_time", create_time);
		map.put("create_user", create_user);
		return insert("addAccountToCard", map);
	}

	@Override
	public List<BoundCard> getAccountUnderBoundCard(String staff_num) {
		Map map = new HashMap();
		map.put("staff_num", staff_num);
		return getSqlMapClientTemplate().queryForList(
				"getAccountUnderBoundCard", map);
	}

	@Override
	public String getDefaultGwDmByBmDm(String bmDm) {
		return (String) getSqlMapClientTemplate().queryForObject(
				"getDefaultGwDmByBmDm", bmDm);
	}

	@Override
	public List<String> getAllStaffDept(String layerDeptNum) {
		Map map = new HashMap();
		map.put("layerDeptNum", layerDeptNum);
		return getSqlMapClientTemplate().queryForList("getAllStaffDept", map);
	}

	@Override
	public List<String> getAllStaffDeptNum(String layerDeptNum) {
		Map map = new HashMap();
		map.put("layerDeptNum", layerDeptNum);
		return getSqlMapClientTemplate()
				.queryForList("getAllStaffDeptNum", map);
	}

	@Override
	public String getStaffNumbyStaffNo(String staff_no) {
		return (String) getSqlMapClientTemplate().queryForObject(
				"getStaffNumbyStaffNo", staff_no);
	}

	@Override
	public boolean resetPassword(Map map) {
		return update("resetPassword", map);
	}

	@Override
	public Employee checkStaffMailByStaffNo(String staffNo) {
		return (Employee) getSqlMapClientTemplate().queryForObject(
				"checkStaffMailByStaffNo", staffNo);
	}

	@Override
	public boolean insertStaffMailByStaffNo(Map map) {
		return insert("insertStaffMailByStaffNo", map);
	}

	@Override
	public Integer checkStaffMailUnique(String staffMail) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"checkStaffMailUnique", staffMail);
	}

	@Override
	public String isOverDraft(String staff_num) {
		return (String) getSqlMapClientTemplate().queryForObject("isOverDraft",
				staff_num);
	}

	@Override
	public Integer consumeCardCount(String staff_num) {
		return (Integer) getSqlMapClientTemplate().queryForObject(
				"consumeCardCount", staff_num);
	}

	@Override
	public String isConsumeCard(String card_num) {
		return (String) getSqlMapClientTemplate().queryForObject(
				"isConsumeCard", card_num);
	}

	@Override
	public boolean setConsumeCard(Map map) {
		return update("setConsumeCard", map);
	}

	@Override
	public boolean setOverDraftRights(Map map) {
		return update("setOverDraftRights", map);
	}

	@Override
	public boolean changeConsumeCard(Map map) {
		return update("changeConsumeCard", map);
	}

	@Override
	public List<String> consumeCardNum(String staff_num) {
		return getSqlMapClientTemplate().queryForList("consumeCardNum",
				staff_num);
	}

	@Override
	public Integer ifUnbound(String empIds) {
		return (Integer) getSqlMapClientTemplate().queryForObject("ifUnbound",
				empIds);
	}

	@Override
	public String getBmdmByBmNo(String bmNo) {
		return (String) getSqlMapClientTemplate().queryForObject(
				"getBmdmByBmNo", bmNo);
	}

	@Override
	public Integer getCount_Hire_Staff(Employee e) {
		String yhDm = e.getYhDm();
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"getCount_Hire_Staff", yhDm);
	}

	@Override
	public List<String> getAllDeptNo() {
		return getSqlMapClientTemplate().queryForList("getAllDeptNo");
	}

	@Override
	public String selectStaffNoRule() {
		return (String) getSqlMapClientTemplate().queryForObject(
				"selectStaffNoRuleInfo");
	}

	@Override
	public boolean selectEmpByNum(String finalStaffNo1) {
		if ((Integer) getSqlMapClientTemplate().queryForObject(
				"selectEmpByNum", finalStaffNo1) > 0) {
			return true;
		} else {

			return false;
		}
	}

	@Override
	public void updateEmpNoTemp(String finalStaffNo, String finalStaffNo1) {
		Map map = new HashMap();
		map.put("staff_no", finalStaffNo1);
		map.put("staff_no_temp", finalStaffNo);

		getSqlMapClientTemplate().update("updateEmpNoTemp", map);
	}

	@Override
	public List<Employee> getEmployeeBybmDmAndstaffNum(
			DepartmentPage departmentPage) {
		return this.getSqlMapClientTemplate().queryForList(
				"getEmployeeBybmDmAndstaffNum", departmentPage);
	}

	@Override
	public void deleteAgricultureEmployee(String subdept) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("subdept", subdept);
		getSqlMapClientTemplate().delete("deleteAgricultureEmployee", map);

	}

	@Override
	public EmployeeTreeNode getEmployeeByStaffNo(String staff_no) {
		return (EmployeeTreeNode) getSqlMapClientTemplate().queryForObject(
				"getEmployeeByStaffNo", staff_no);
	}

	@Override
	public List<BoundCard> selectBoundCardInfoByYHDM2(String yhdm) {

		return this.getSqlMapClientTemplate().queryForList(
				"selectBoundCardInfoByYHDM2", yhdm);
	}
}
