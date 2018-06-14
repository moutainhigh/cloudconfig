package com.kuangchi.sdd.businessConsole.employee.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.card.model.Card;
import com.kuangchi.sdd.baseConsole.card.service.ICardService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.department.dao.IDepartmentDao;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.employee.dao.EmployeeDao;
import com.kuangchi.sdd.businessConsole.employee.model.BoundCard;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.businessConsole.employee.model.EmployeePosition;
import com.kuangchi.sdd.businessConsole.employee.model.EmployeeTreeNode;
import com.kuangchi.sdd.businessConsole.employee.model.PaperType;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.businessConsole.station.dao.IStationDao;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.consumeConsole.consumeGroupMap.dao.IGroupMapDao;
import com.kuangchi.sdd.consumeConsole.consumeGroupMap.model.GroupMapModel;

/**
 * Created by jianhui.wu on 2016/2/15.
 */
@Transactional
@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

	@Resource(name = "employeeDao")
	EmployeeDao employeeDao;
	@Resource(name = "departmentDaoImpl")
	private IDepartmentDao departmentDao;
	@Resource(name = "stationDaoImpl")
	private IStationDao stationDaoImpl;
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;
	@Resource(name = "groupMapDaoImpl")
	private IGroupMapDao groupMapDao;
	@Resource(name = "cardServiceImpl")
	private ICardService cardService;

	@Override
	public Employee getEmployeeDetail(String id) {
		Employee employee = employeeDao.getEmployeeDetail(id);
		if (null != employee) {
			EmployeePosition employeePosition = employeeDao
					.getEmployeePositionByYhDm(employee.getYhDm());
			if (null != employeePosition) {
				employee.setGwDm(employeePosition.getGwDm());
			}
		}
		return employee;
	}

	@Override
	public boolean modifyEmployee(Employee employee, User loginUser) {
		Integer isHire = 0;
		isHire = employeeDao.getCount_Hire_Staff(employee);
		employeeDao.modifyEmployee(employee);
		employeeDao
				.deleteEmployeePositionByYhDm("'" + employee.getYhDm() + "'");
		EmployeePosition employeePosition = new EmployeePosition();
		employeePosition.setGwDm(employee.getGwDm());
		employeePosition.setYhDm(employee.getYhDm());
		employeeDao.insertEmployeePosition(employeePosition);

		if (isHire > 0 && "1".equals(employee.getStaff_hire_state())) {

			// 1.拿到此人的所有正常卡号
			Grid<BoundCard> boundCardListG = selectBoundCardInfoByYHDM2(employee
					.getYhDm());
			List<BoundCard> boundCardList = boundCardListG.getRows();
			// 将此人的卡片改为退卡状态并删除卡片权限
			for (BoundCard bcl : boundCardList) {// 逐个删除权限
				if (cardService.frozenCard(bcl.getCard_num(),
						loginUser.getYhMc()) != 0) {
					// return false;
				}
			}
		} else if (isHire <= 0 && "0".equals(employee.getStaff_hire_state())) {
			// 将此人的卡片改为正常状态并恢复卡片权限
			// 1.拿到此人的所有冻结的卡号
			Grid<BoundCard> boundCardListG2 = selectBoundCardByYHDM352(employee
					.getYhDm());
			List<BoundCard> boundCardList2 = boundCardListG2.getRows();

			for (BoundCard bcl : boundCardList2) {// 逐个恢复权限
				if (cardService.unfreezeCard(bcl.getCard_num(),
						loginUser.getYhMc()) != 0) {
					// return false;
				}
			}
		} else {

		}

		return true;
	}

	@Override
	public void addEmployee(Employee employee) {
		employeeDao.addEmployee(employee);

		EmployeePosition employeePosition = new EmployeePosition();
		employeePosition.setGwDm(employee.getGwDm());
		employeePosition.setYhDm(employee.getYhDm());
		employeeDao.insertEmployeePosition(employeePosition);

		GroupMapModel groupMap = new GroupMapModel();
		groupMap.setStaff_num(employee.getYhDm());
		groupMap.setGroup_num("1");
		groupMapDao.addGroupMap(groupMap);
	}

	@Override
	public JsonResult validatEmployee(String yhNo, String yhDm) {
		JsonResult result = new JsonResult();
		result.setSuccess(true);

		String msg = null;
		int yhDmNum = employeeDao.isContainYhDM(yhNo, yhDm);

		if (yhDmNum > 0) {
			result.setSuccess(false);
			msg = "该代码已经被使用";
		}
		result.setMsg(msg);

		return result;
	}

	@Override
	public void deleteGyDm(String empIds) {
		employeeDao.deleteGyDm(empIds);
	}

	@Override
	public Grid<Employee> getEmployeeBybmDm(DepartmentPage departmentPage) {
		Grid<Employee> grid = new Grid<Employee>();
		List<Employee> resultList = employeeDao
				.getEmployeeBybmDm(departmentPage);
		grid.setRows(resultList);
		if (null != resultList) {
			grid.setTotal(employeeDao.getEmployeeCountBybmDm(departmentPage));
		} else {
			grid.setTotal(0);
		}
		return grid;
	}

	@Override
	public Grid<Employee> searchEmployee(DepartmentPage departmentPage) {
		Grid<Employee> grid = new Grid<Employee>();
		List<Employee> resultList = employeeDao.searchEmployee(departmentPage);
		grid.setRows(resultList);
		if (null != resultList) {
			grid.setTotal(employeeDao.searchEmployeeCount(departmentPage));
		} else {
			grid.setTotal(0);
		}
		return grid;
	}

	@Override
	public Grid<Employee> searchEmployee2(DepartmentPage departmentPage) {
		Grid<Employee> grid = new Grid<Employee>();
		List<Employee> resultList = employeeDao.searchEmployee2(departmentPage);
		grid.setRows(resultList);
		if (null != resultList) {
			grid.setTotal(employeeDao.searchEmployeeCount2(departmentPage));
		} else {
			grid.setTotal(0);
		}
		return grid;
	}

	@Override
	public List<PaperType> getPaperTypes() {

		return employeeDao.getPaperTypes();
	}

	@Override
	public List<Employee> getAllEmployee(String layerDeptNum) {

		return employeeDao.getAllEmployee(layerDeptNum);
	}

	@Override
	public List<Employee> getAllWorkingEmployee(String layerDeptNum) {

		return employeeDao.getAllEmployee(layerDeptNum);
	}

	@Override
	public void batchAddEmployee(List<Employee> employeeList) throws Exception {
		employeeDao.batchAddEmployee(employeeList);
	}

	@Override
	public List<EmployeeTreeNode> getAllEmployeeTreeNode() {
		return employeeDao.getAllEmployeeTreeNode();
	}

	@Override
	public Grid<Card> selectAllCards(Map map) {
		Grid<Card> grid = new Grid<Card>();
		List<Card> list = employeeDao.selectAllCards(map);

		Date day = new Date();// 获取当前日期
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 日期格式化
		String vdate = format.format(day);
		List<Card> list2 = new ArrayList<Card>();
		Date date = null;
		try {
			date = format.parse(vdate);// 格式化当前日期
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long sysTime = date.getTime();// 获取当前日期的毫秒数
		for (Card c : list) {
			try {
				if (sysTime > format.parse(c.getCard_validity()).getTime()) {
					c.setCard_validity_state("1");
				} else {
					c.setCard_validity_state("0");
				}

			} catch (ParseException e) {
				e.printStackTrace();
			} finally {
				list2.add(c);
			}
		}

		grid.setRows(list2);
		if (null != employeeDao.selectAllCards(map)) {
			grid.setTotal(employeeDao.getAllCardsCount(map));
		} else {
			grid.setTotal(0);
		}
		return grid;
	}

	// 插入到卡绑定表，并给日志表新增记录
	@Override
	public boolean insertCardBound(Map<String, String> map) {
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "人卡绑定新增");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", map.get("create_user"));
		log.put("V_OP_TYPE", "新增");
		try {
			boolean flag = employeeDao.insertCardBound(map);
			if (flag == false) {
				log.put("V_OP_MSG", "新增人卡绑定失败");
				logDao.addLog(log);
				return false;
			}
			log.put("V_OP_MSG", "新增人卡绑定成功");
			logDao.addLog(log);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_MSG", "异常");
			logDao.addLog(log);
			return false;
		}
	}

	@Override
	public Grid<BoundCard> selectBoundCardByYHDM(String yhdm) {
		List<BoundCard> boundCard = employeeDao.selectBoundCardByYHDM(yhdm);
		Grid<BoundCard> grid = new Grid<BoundCard>();
		grid.setRows(boundCard);
		return grid;
	}

	@Override
	public Grid<BoundCard> selectBoundCardByYHDM352(String yhdm) {
		List<BoundCard> boundCard = employeeDao.selectBoundCardByYHDM352(yhdm);
		Grid<BoundCard> grid = new Grid<BoundCard>();
		grid.setRows(boundCard);
		return grid;
	}

	@Override
	public Grid<BoundCard> selectBoundCardByStaffNo(String yhdm) {
		List<BoundCard> boundCard = employeeDao.selectBoundCardByStaffNo(yhdm);
		Grid<BoundCard> grid = new Grid<BoundCard>();
		grid.setRows(boundCard);
		return grid;
	}

	@Override
	public Grid<BoundCard> selectBoundCardByStaffNo2(String yhdm) {
		List<BoundCard> boundCard = employeeDao.selectBoundCardByStaffNo2(yhdm);
		Grid<BoundCard> grid = new Grid<BoundCard>();
		grid.setRows(boundCard);
		return grid;
	}

	@Override
	public boolean isExistUserAndCard(String card_num, String yhdm) {
		if (employeeDao.isExistUserAndCard(card_num, yhdm)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int isContainYhDM(String yhNo, String yhDm) {
		return employeeDao.isContainYhDM(yhNo, yhDm);
	}

	@Override
	public Grid<BoundCard> selectBoundCardByForCopy(Map map) {
		Grid<BoundCard> g = new Grid<BoundCard>();
		g.setRows(employeeDao.selectBoundCardByForCopy(map));
		return g;
	}

	@Override
	public boolean updateLeader(Employee employee) {
		return employeeDao.updateLeader(employee);
	}

	@Override
	public boolean updateStation(Employee employee) {
		employeeDao
				.deleteEmployeePositionByYhDm("'" + employee.getYhDm() + "'");
		EmployeePosition employeePosition = new EmployeePosition();
		employeePosition.setGwDm(employee.getGwDm());
		employeePosition.setYhDm(employee.getYhDm());
		employeeDao.insertEmployeePosition(employeePosition);
		String gwmc = stationDaoImpl.stationDetails(employee.getGwDm())
				.getGwMc();
		employee.setGwmc(gwmc);
		return employeeDao.updateStation(employee);
	}

	@Override
	public String selectStaffNum(String staff_no) {
		return employeeDao.selectStaffNum(staff_no);
	}

	// 给员工和卡添加默认账户
	@Override
	public boolean addAccountToCard(String card_num, String create_time,
			String[] account_num, String create_user) {
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "人卡绑定新增账户");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", create_user);
		log.put("V_OP_TYPE", "新增");
		try {
			for (int i = 0; i < account_num.length; i++) {
				// boolean result=employeeDao.addAccountToEmployee(staff_num,
				// create_time, account_num[i], create_user);
				boolean result1 = employeeDao.addAccountToCard(card_num,
						create_time, account_num[i], create_user);
				if (result1) {
					log.put("V_OP_MSG", "新增人卡绑定账户成功");
					logDao.addLog(log);
					return true;
				}
				log.put("V_OP_MSG", "新增人卡绑定账户失败");
				logDao.addLog(log);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_MSG", "异常");
			logDao.addLog(log);
			return false;
		}
		log.put("V_OP_MSG", "新增人卡绑定账户失败");
		logDao.addLog(log);
		return false;
	}

	// 判断员工绑定的卡下面是否已绑定账号
	@Override
	public JsonResult isExistAccountUnderBoundCard(String yhdm,
			String[] account_num, String card_num) {
		List<BoundCard> list = employeeDao.getAccountUnderBoundCard(yhdm);
		JsonResult result = new JsonResult();
		if (null == list || list.size() == 0) {
			result.setSuccess(true);
		} else if (list.size() == 1) {

			if (account_num.length == 1) {
				if (account_num[0].equals(list.get(0).getAccount_num())) {
					result.setMsg("此员工已绑定了一张卡，卡号为" + list.get(0).getCard_num()
							+ "已绑定了此账户");
					result.setSuccess(false);
				} else {
					if (card_num.equals(list.get(0).getCard_num())) {
						result.setSuccess(true);
					} else {
						result.setMsg("此员工已绑定了一张卡，卡号为"
								+ list.get(0).getCard_num() + "已绑定了此账户");
						result.setSuccess(false);
					}
				}
			} else {
				result.setMsg("此员工已绑定了一张卡，卡号为" + list.get(0).getCard_num()
						+ "已绑定了账户");
				result.setSuccess(false);
			}
		} else {
			result.setMsg("此员工已绑定了一张卡，卡号为" + list.get(0).getCard_num()
					+ "已绑定了账户");
			result.setSuccess(false);
		}
		return result;
	}

	// 给员工添加默认账户
	@Override
	public boolean addAccountToEmployee(String staff_num, String create_time,
			String create_user) {
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "新增员工账户");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", create_user);
		log.put("V_OP_TYPE", "新增");
		try {

			boolean result = employeeDao.addAccountToEmployee(staff_num,
					create_time, "1", create_user);
			boolean result1 = employeeDao.addAccountToEmployee(staff_num,
					create_time, "2", create_user);
			if (result && result1) {
				log.put("V_OP_MSG", "新增员工账户成功");
				logDao.addLog(log);
				return true;
			}
			log.put("V_OP_MSG", "新增员工账户失败");
			logDao.addLog(log);
			return false;

		} catch (Exception e) {
			e.printStackTrace();
			log.put("V_OP_MSG", "异常");
			logDao.addLog(log);
			return false;
		}

	}

	@Override
	public String getDefaultGwDmByBmDm(String bmDm) {
		return employeeDao.getDefaultGwDmByBmDm(bmDm);
	}

	@Override
	public List<String> getAllStaffDept(String layerDeptNum) {
		return employeeDao.getAllStaffDept(layerDeptNum);
	}

	@Override
	public List<String> getAllStaffDeptNum(String layerDeptNum) {
		return employeeDao.getAllStaffDeptNum(layerDeptNum);
	}

	@Override
	public boolean resetPassword(Map map) {
		return employeeDao.resetPassword(map);
	}

	@Override
	public Employee checkStaffMailByStaffNo(String staffNo) {
		return employeeDao.checkStaffMailByStaffNo(staffNo);
	}

	@Override
	public boolean insertStaffMailByStaffNo(Map map) {
		return employeeDao.insertStaffMailByStaffNo(map);
	}

	@Override
	public Integer checkStaffMailUnique(String staffMail) {
		return employeeDao.checkStaffMailUnique(staffMail);
	}

	@Override
	public String isOverDraft(String staff_num) {
		return employeeDao.isOverDraft(staff_num);
	}

	@Override
	public Integer consumeCardCount(String staff_num) {
		return employeeDao.consumeCardCount(staff_num);
	}

	@Override
	public String isConsumeCard(String card_num) {
		return employeeDao.isConsumeCard(card_num);
	}

	@Override
	public boolean setConsumeCard(String card_num, String support_consume,
			String create_user, String create_time) {
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "修改员工账户");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", create_user);
		log.put("V_OP_TYPE", "修改");

		Map<String, String> map = new HashMap<String, String>();
		map.put("card_num", card_num);
		map.put("support_consume", support_consume);
		boolean result = employeeDao.setConsumeCard(map);
		if (result) {
			log.put("V_OP_MSG", "修改员工透支权限成功");
			logDao.addLog(log);
			return true;
		} else {
			log.put("V_OP_MSG", "修改员工透支权限失败");
			logDao.addLog(log);
			return false;
		}
	}

	@Override
	public boolean setOverDraftRights2(String over_draft, String staff_num,
			String unCheckCard_num, String card_num, String create_user,
			String create_time) {
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "修改员工透支权限");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", create_user);
		log.put("V_OP_TYPE", "修改");

		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("over_draft", over_draft);
		map1.put("staff_num", staff_num);

		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("card_num", card_num);
		map2.put("staff_num", staff_num);
		map2.put("support_consume", "0");

		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("card_num", unCheckCard_num);
		map3.put("staff_num", staff_num);
		map3.put("support_consume", "1");

		boolean flag = employeeDao.setOverDraftRights(map1);
		boolean flag1 = false;
		boolean flag2 = false;
		if (unCheckCard_num != null && !"".equals(unCheckCard_num)) {
			flag1 = employeeDao.changeConsumeCard(map3);
		}
		if (card_num != null && !"".equals(card_num)) {
			flag2 = employeeDao.changeConsumeCard(map2);
		}
		if ((flag && flag1) || (flag && flag2)) {
			log.put("V_OP_MSG", "修改员工透支权限成功");
			logDao.addLog(log);
			return true;
		} else {
			log.put("V_OP_MSG", "修改员工透支权限失败");
			logDao.addLog(log);
			return false;
		}

	}

	@Override
	public boolean setOverDraftRights1(String over_draft, String staff_num,
			String create_user, String create_time) {
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "修改员工透支权限");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", create_user);
		log.put("V_OP_TYPE", "修改");

		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("over_draft", over_draft);
		map1.put("staff_num", staff_num);

		boolean flag = employeeDao.setOverDraftRights(map1);
		if (flag) {
			log.put("V_OP_MSG", "修改员工透支权限成功");
			logDao.addLog(log);
			return true;
		} else {
			log.put("V_OP_MSG", "修改员工透支权限失败");
			logDao.addLog(log);
			return false;
		}
	}

	@Override
	public List<String> consumeCardNum(String staff_num) {
		return employeeDao.consumeCardNum(staff_num);
	}

	@Override
	public String getBmdmByBmNo(String bmNo) {
		return employeeDao.getBmdmByBmNo(bmNo);
	}

	public List<String> getAllDeptNo() {
		return employeeDao.getAllDeptNo();
	}

	@Override
	public String selectStaffNoRule() {
		return employeeDao.selectStaffNoRule();
	}

	@Override
	public boolean selectEmpByNum(String finalStaffNo1) {
		return employeeDao.selectEmpByNum(finalStaffNo1);
	}

	@Override
	public void updateEmpNoTemp(String finalStaffNo, String finalStaffNo1) {
		employeeDao.updateEmpNoTemp(finalStaffNo, finalStaffNo1);
	}

	@Override
	public List<Employee> getEmployeeBybmDmAndstaffNum(
			DepartmentPage departmentPage) {
		List<Employee> resultList = employeeDao
				.getEmployeeBybmDmAndstaffNum(departmentPage);
		return resultList;
	}

	@Override
	public void deleteAgricultureEmployee(String subdept) {
		employeeDao.deleteAgricultureEmployee(subdept);

	}

	@Override
	public EmployeeTreeNode getEmployeeByStaffNo(String staff_no) {
		return employeeDao.getEmployeeByStaffNo(staff_no);
	}

	@Override
	public Grid<BoundCard> selectBoundCardInfoByYHDM2(String yhdm) {
		List<BoundCard> boundCard = employeeDao
				.selectBoundCardInfoByYHDM2(yhdm);
		Grid<BoundCard> grid = new Grid<BoundCard>();
		grid.setRows(boundCard);
		return grid;
	}
}
