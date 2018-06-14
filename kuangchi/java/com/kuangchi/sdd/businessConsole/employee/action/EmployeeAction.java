package com.kuangchi.sdd.businessConsole.employee.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.baseConsole.card.model.Card;
import com.kuangchi.sdd.baseConsole.card.service.ICardService;
import com.kuangchi.sdd.baseConsole.count.model.CardHistoryModel;
import com.kuangchi.sdd.baseConsole.count.service.ICountService;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.department.model.Department;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.employee.model.BoundCard;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.businessConsole.employee.model.PaperType;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.businessConsole.employee.util.ExcelExportTemplate;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.station.model.Station;
import com.kuangchi.sdd.businessConsole.station.service.IStationService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.doorAccessConsole.authority.service.PeopleAuthorityInfoService;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.model.DepartmentGrantModel;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.service.IDepartmentGrantService;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.EmptyUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.ServletUtil;
import com.kuangchi.sdd.util.excel.ExcelUtilSpecial;
import com.kuangchi.sdd.util.excel.TitleRowCell;
import com.kuangchi.sdd.util.file.ContinueFTP;
import com.kuangchi.sdd.util.file.DownloadFile;

/**
 * Created by jianhui.wu on 2016/2/15.
 */
@Controller("employeeAction")
public class EmployeeAction extends BaseActionSupport {

	@Autowired
	PeopleAuthorityInfoService peopleAuthorityInfoService;

	@Autowired
	IDepartmentGrantService departmentGrantService;

	@Resource(name = "stationServiceImpl")
	private IStationService stationService;

	@Resource(name = "roleServiceImpl")
	private IRoleService roleService;

	@Resource(name = "LogDaoImpl")
	private LogDao logDao;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Resource(name = "employeeService")
	EmployeeService employeeService;
	@Resource(name = "departmentServiceImpl")
	IDepartmentService departmentService;
	@Resource(name = "cardServiceImpl")
	ICardService cardService;

	@Resource(name = "countServiceImpl")
	ICountService countService;

	private Employee model;

	// 文件
	private File uploadEmployeeFile;

	// 提交过来的file的名字
	private String uploadEmployeeFileFileName;

	private File imgFile;
	private String imgFileFileName;

	public EmployeeAction() {
		model = new Employee();
	}

	@Override
	public Object getModel() {
		return model;
	}

	public void employeeDetail() {
		HttpServletRequest request = getHttpServletRequest();
		String yhDm = request.getParameter("yhDm");
		Employee employee = employeeService.getEmployeeDetail(yhDm);
		printHttpServletResponse(GsonUtil.toJson(employee));
	}

	/**
	 * 修改员工
	 */
	public void modifyEmployee() {
		HttpServletRequest request = getHttpServletRequest();
		Employee page = new Employee();
		BeanUtils.copyProperties(model, page);
		restModel();
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "员工管理");
		log.put("V_OP_FUNCTION", "修改");
		log.put("V_OP_ID", login_user);
		try {
			ifChgDept(page, loginUser.getYhMc());
			if (employeeService.modifyEmployee(page, loginUser)) {
				result.setSuccess(true);
				log.put("V_OP_MSG", "修改员工信息成功");
				log.put("V_OP_TYPE", "业务");
				LOG.error("修改员工成功");
			} else {
				result.setSuccess(false);
				log.put("V_OP_MSG", "修改员工信息失败");
				log.put("V_OP_TYPE", "业务");
				LOG.error("修改员工失败");
			}
		} catch (Exception e) {
			result.setSuccess(false);
			LOG.error("修改员工失败", e);
			log.put("V_OP_MSG", "修改员工信息失败");
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 判断员工是否换部门 是的话，要更换该员工的组织权限 by gengji.yang
	 */
	private void ifChgDept(Employee emp, String createUser) {
		Map map = peopleAuthorityInfoService.getDeptNumAndHireState(emp
				.getYhDm());
		String curDeptNum = emp.getBmDm();
		String oldDeptNum = map.get("deptNum").toString();
		if (!curDeptNum.equals(oldDeptNum)) {
			// 员工继承新的部门排班
			chgDutyOnDeptChg(curDeptNum, oldDeptNum, emp.getYhDm());
			List<Map> cardList = peopleAuthorityInfoService.getEmpCards(emp
					.getYhDm());
			if (cardList.size() > 0) {
				for (Map m : cardList) {
					chgGroupAuth(curDeptNum, oldDeptNum,
							(String) m.get("cardNum"), createUser);
				}
				changeDeptAuth(curDeptNum, oldDeptNum, cardList, createUser);
			}
		}
	}

	/**
	 * 新员工继承部门排班 by gengji.yang
	 */
	private void getDutyFromDept(String deptNum, String staffNum) {
		String today = getToday();
		try {
			peopleAuthorityInfoService.makeDutyByDeptNum(deptNum, staffNum,
					today);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getToday() {
		Date date = new Date();// 创建当前系统时间
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String today = format.format(date);
		return today;
	}

	/**
	 * 员工换部门，更换排班 by gengji.yang
	 */
	private void chgDutyOnDeptChg(String newDept, String oldDept,
			String staffNum) {
		String today = getToday();
		try {
			peopleAuthorityInfoService.chgDutyOnDeptChg(newDept, oldDept,
					staffNum, today);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 新增员工前，拿规则生成的员工工号 2016-10-25 guofei.lian
	public void initStaffNo() {
		String midStaffNo = peopleAuthorityInfoService.autoProdStaffNo();// 自动生成的员工号中间部分
		Map<String, String> map = new HashMap<String, String>();
		if (midStaffNo == null || "".equals(midStaffNo)) {
			map.put("msg", "当前编码规则自动生成的员工工号已用尽，请修改编码规则或者手动输入员工工号");
		} else {
			map.put("msg", "");
		}
		String staffRule = employeeService.selectStaffNoRule();// 查询当前员工工号编码规则
		String[] staffRules = staffRule.split("\\|", -1);
		staffRules[1] = midStaffNo;// 用生成的员工号中间部分替换规则的中间部分
		String finalStaffNo = staffRules[0] + "|" + staffRules[1] + "|"
				+ staffRules[2];// 重新拼接成最后显示到页面的员工号
		String finalStaffNo1 = staffRules[0] + staffRules[1] + staffRules[2];// 重新拼接成最后显示到页面的员工号
		boolean flag = employeeService.selectEmpByNum(finalStaffNo1);
		if (flag) {
			employeeService.updateEmpNoTemp(finalStaffNo, finalStaffNo1);
			midStaffNo = peopleAuthorityInfoService.autoProdStaffNo();
			if (midStaffNo == null || "".equals(midStaffNo)) {
				map.put("msg", "当前编码规则自动生成的员工工号已用尽，请修改编码规则或者手动输入员工工号");
			} else {
				map.put("msg", "");
			}
			staffRules[1] = midStaffNo;
			finalStaffNo = staffRules[0] + "|" + staffRules[1] + "|"
					+ staffRules[2];
		}
		map.put("staffNoRule", finalStaffNo);
		printHttpServletResponse(GsonUtil.toJson(map));
	}

	/**
	 * 新增员工
	 */
	public void addEmployee() {
		HttpServletRequest request = getHttpServletRequest();
		String bmDm = request.getParameter("bmDm");
		String data = request.getParameter("data");
		Employee page = GsonUtil.toBean(data, Employee.class);
		// Employee page = new Employee();
		// BeanUtils.copyProperties(model, page);
		UUID uuid = UUID.randomUUID();
		page.setYhDm(uuid.toString());
		page.setBmDm(bmDm);
		page.setZfBj("0");
		// model.setBmDm(bmDm);
		// model.setZfBj("0");
		// restModel();
		JsonResult result = new JsonResult();
		User loginUser = (User) ServletUtil.getSessionAttr(
				getHttpServletRequest(), GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "员工管理");
		log.put("V_OP_FUNCTION", "新增");
		log.put("V_OP_ID", login_user);
		try {
			log.put("V_OP_TYPE", "业务");
			employeeService.addEmployee(page);
			// 继承部门排班 by gengji.yang
			getDutyFromDept(bmDm, uuid.toString());
			log.put("V_OP_MSG", "新增员工信息成功");
			result.setSuccess(true);
		} catch (Exception e) {
			result.setSuccess(false);
			LOG.error("add new employee error", e);
			log.put("V_OP_MSG", "新增员工信息失败");
			log.put("V_OP_TYPE", "异常");
			e.printStackTrace();
		}
		logDao.addLog(log);
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * 按条件搜索员工
	 * 
	 */
	public void searchEmployee() {
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		DepartmentPage departmentPage = GsonUtil.toBean(beanObject,
				DepartmentPage.class); // 将数据转化为javabean
		departmentPage.setPage(Integer.parseInt(request.getParameter("page")));
		departmentPage.setRows(Integer.parseInt(request.getParameter("rows")));

		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		/*
		 * boolean isLayer = roleService.isLayer(); if(isLayer){ Role role =
		 * (Role)
		 * getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE
		 * ); departmentPage.setJsDm(role.getJsDm());
		 * 
		 * User user = (User)
		 * getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
		 * departmentPage.setYhDm(user.getYhDm()); } else {
		 * departmentPage.setJsDm("0"); }
		 */

		boolean isLayer = roleService.isLayer();
		String layerDeptNum = null;
		if (isLayer) {
			Role role = (Role) getHttpSession().getAttribute(
					GlobalConstant.LOGIN_USER_CURRENT_ROLE);
			User user = (User) getHttpSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(),
					role.getJsDm());
		}
		departmentPage.setLayerDeptNum(layerDeptNum);

		Grid<Employee> employeeGrid = employeeService
				.searchEmployee(departmentPage);
		printHttpServletResponse(GsonUtil.toJson(employeeGrid));
	}

	/**
	 * 按条件搜索员工
	 * 
	 */
	public void searchEmployee2() {
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		DepartmentPage departmentPage = GsonUtil.toBean(beanObject,
				DepartmentPage.class); // 将数据转化为javabean
		departmentPage.setPage(Integer.parseInt(request.getParameter("page")));
		departmentPage.setRows(Integer.parseInt(request.getParameter("rows")));

		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		/*
		 * boolean isLayer = roleService.isLayer(); if(isLayer){ Role role =
		 * (Role)
		 * getHttpSession().getAttribute(GlobalConstant.LOGIN_USER_CURRENT_ROLE
		 * ); departmentPage.setJsDm(role.getJsDm());
		 * 
		 * User user = (User)
		 * getHttpSession().getAttribute(GlobalConstant.LOGIN_USER);
		 * departmentPage.setYhDm(user.getYhDm()); } else {
		 * departmentPage.setJsDm("0"); }
		 */
		boolean isLayer = roleService.isLayer();
		String layerDeptNum = null;
		if (isLayer) {
			Role role = (Role) getHttpSession().getAttribute(
					GlobalConstant.LOGIN_USER_CURRENT_ROLE);
			User user = (User) getHttpSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(),
					role.getJsDm());
		}
		departmentPage.setLayerDeptNum(layerDeptNum);

		User user = (User) getHttpSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		departmentPage.setYhDm(user.getYhDm());

		Grid<Employee> employeeGrid = employeeService
				.searchEmployee2(departmentPage);
		printHttpServletResponse(GsonUtil.toJson(employeeGrid));
	}

	/**
	 * 导出页面勾选项
	 * 
	 */
	public void reportData() {
		HttpServletResponse response = getHttpServletResponse();
		HttpServletRequest request = getHttpServletRequest();
		String selectDatas = request.getParameter("select");
		String columnStr = request.getParameter("columns");
		String[] columns1 = columnStr.split(",");
		String[] columns = new String[columns1.length - 1];
		for (int i = 1; i < columns1.length; i++) {
			columns[i - 1] = columns1[i];
		}
		String[] cols = new String[columns.length];
		String[] colTitles = new String[columns.length];
		for (int i = 0; i < columns.length; i++) {
			cols[i] = columns[i].split("-")[0];
			colTitles[i] = columns[i].split("-")[1];
		}
		List list = GsonUtil.getListFromJson(selectDatas, ArrayList.class);
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName = "员工信息表.xls";
			response.setHeader("content-Disposition", "attachment;filename="
					+ DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecial.exportExcel("员工表", colTitles,
					cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uploadEmployee() {
		HttpServletRequest request = getHttpServletRequest();
		// String deal = request.getParameter("deal");
		String value = request.getParameter("value");
		Integer flag0 = Integer.parseInt(request.getParameter("flag0"));
		Integer flag1 = Integer.parseInt(request.getParameter("flag1"));
		FileInputStream fis = null;
		User loginUser = (User) request.getSession().getAttribute(
				GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		JsonResult res = new JsonResult();
		try {
			if (uploadEmployeeFile == null) {
				/*
				 * request.setAttribute("message", "请选择需要上传的文件"); return
				 * "success";
				 */
				res.setSuccess(false);
				res.setMsg("请选择需要上传的文件");
				printHttpServletResponse(res);
				return;
			}
			fis = new FileInputStream(uploadEmployeeFile);
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			HSSFSheet sheet = workbook.getSheetAt(0);
			HSSFRow row0 = sheet.getRow(0);
			int totalRow = sheet.getLastRowNum();
			Cell titleCell = row0.getCell(0);
			request.setAttribute("zhi", value);
			List<String> deptNos = employeeService.getAllDeptNo(); // 已有的部门编号
			List<String> newNos = new ArrayList<String>(); // 新的部门编号
			List<String> cardNumList = new ArrayList<String>(); // 卡号数组
			// List<String> noSjbms = new ArrayList<String>();
			// 0 部门
			if ("部门信息表".equals(titleCell.getStringCellValue().trim())
					&& "0".equals(value)) {
				if (totalRow < 5) {
					res.setSuccess(false);
					res.setMsg("导入失败，文件内容为空!");
					printHttpServletResponse(res);
					return;
				}

				List<Department> list = new ArrayList<Department>();
				for (int i = 5; i <= totalRow; i++) {
					HSSFRow row = sheet.getRow(i);
					Department depart = new Department();
					String sjbmNo = null;// 上级部门编号
					String sjbmDm = null;// 上级部门代码
					String bmMc = null;// 部门名称 必填
					String bmNo = null;// 部门编号 必填
					String bmMcJ = null;// 简称 必填
					String bz = null;

					Cell bmNoCell = row.getCell(0); // 部门编号
					if (null != bmNoCell) {
						bmNoCell.setCellType(Cell.CELL_TYPE_STRING);
						bmNo = bmNoCell.getStringCellValue().trim();
						if (null == bmNo || "".equals(bmNo)) {
							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1) + "行的“部门编号”是否为空！");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						} else {
							if (bmNo.length() > 20) {
								res.setSuccess(false);
								res.setMsg("导入失败，导入失败，请检查第" + (i + 1)
										+ "行的“部门编号”是否超过20位！");
								printHttpServletResponse(GsonUtil.toJson(res));
								return;
							}
						}
					} else {
						res.setSuccess(false);
						res.setMsg("导入失败，请检查第" + (i + 1) + "行的“部门编号”是否为空！");
						printHttpServletResponse(GsonUtil.toJson(res));
						return;
					}

					Cell bmMcCell = row.getCell(1); // 部门名称
					if (null != bmMcCell) {
						bmMcCell.setCellType(Cell.CELL_TYPE_STRING);
						bmMc = bmMcCell.getStringCellValue();
						if (null == bmMc || "".equals(bmMc)) {
							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1) + "行的“部门名称”是否为空！");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						} else {
							if (bmMc.length() > 100) {
								res.setSuccess(false);
								res.setMsg("导入失败，请检查第" + (i + 1)
										+ "行的“部门名称”是否超过100位！");
								printHttpServletResponse(GsonUtil.toJson(res));
								return;
							}
						}
					} else {
						res.setSuccess(false);
						res.setMsg("导入失败，请检查第" + (i + 1) + "行的“部门名称”是否为空！");
						printHttpServletResponse(GsonUtil.toJson(res));
						return;
					}

					Cell bmMcJCell = row.getCell(2); // 简称
					if (null != bmMcJCell) {
						bmMcJCell.setCellType(Cell.CELL_TYPE_STRING);
						bmMcJ = bmMcJCell.getStringCellValue();
						if (null == bmMcJ || "".equals(bmMcJ)) {
							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1) + "行的“简称”是否为空！");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						} else {
							if (bmMcJ.length() > 100) {
								res.setSuccess(false);
								res.setMsg("导入失败，请检查第" + (i + 1)
										+ "行的“简称”是否超过100位！");
								printHttpServletResponse(GsonUtil.toJson(res));
								return;
							}
						}
					} else {
						res.setSuccess(false);
						res.setMsg("导入失败，请检查第" + (i + 1) + "行的“简称”是否为空！");
						printHttpServletResponse(GsonUtil.toJson(res));
						return;
					}

					if (flag0 == 0) {// 用于判断是否第一次上传
						Cell sjbmDmCell = row.getCell(3); // 上级部门
						if (null != sjbmDmCell) {
							sjbmDmCell.setCellType(Cell.CELL_TYPE_STRING);
							sjbmNo = sjbmDmCell.getStringCellValue().trim();
							if (null != sjbmNo || !"".equals(sjbmNo)) {
								if (sjbmNo.length() > 20) {
									res.setSuccess(false);
									res.setMsg("导入失败，请检查第" + (i + 1)
											+ "行的“上级部门编号”是否超过20位！");
									printHttpServletResponse(GsonUtil
											.toJson(res));
									return;
								}
							} else {
								res.setSuccess(true);
								res.setCode("0");
							}
						} else {
							res.setSuccess(true);
							res.setCode("0");
						}
					} else {// 第二次上传
						Cell sjbmDmCell = row.getCell(3);
						if (null != sjbmDmCell) {
							sjbmDmCell.setCellType(Cell.CELL_TYPE_STRING);
							sjbmNo = sjbmDmCell.getStringCellValue().trim();
							if ("".equals(sjbmNo)) {
								sjbmNo = "2";
							}
						} else {
							sjbmNo = "2";
						}
					}

					Cell bzCell = row.getCell(4); // 备注
					if (null != bzCell) {
						bzCell.setCellType(Cell.CELL_TYPE_STRING);
						bz = bzCell.getStringCellValue();
						if (bz.length() > 200) {
							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1)
									+ "行的“备注”是否超过200位！");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						}
					}

					if (null != sjbmNo && !"".equals(sjbmNo)) {
						if (!sjbmNo.matches("^[A-Za-z0-9]+$")) {
							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1)
									+ "行的“上级部门编号”是否不为英文字母或数字!");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						}
					}

					if (!bmNo.matches("^[A-Za-z0-9]+$")) {
						res.setSuccess(false);
						res.setMsg("导入失败，请检查第" + (i + 1)
								+ "行的“部门编号”是否不为英文字母或数字!");
						printHttpServletResponse(GsonUtil.toJson(res));
						return;
					}

					JsonResult result = departmentService.validDepartment(bmNo,
							""); // 部门编号是否重复
					if (result.isSuccess()) {
						for (String no : newNos) {
							if (bmNo.equals(no)) {
								res.setSuccess(false);
								res.setMsg("导入失败，第" + (i + 1) + "行的“部门编号”"
										+ bmNo + "已存在!");
								printHttpServletResponse(GsonUtil.toJson(res));
								return;
							}
						}
					} else {
						res.setSuccess(false);
						res.setMsg("导入失败，第" + (i + 1) + "行的“部门编号”" + bmNo
								+ "已存在!");
						printHttpServletResponse(GsonUtil.toJson(res));
						return;
					}

					UUID uuid = UUID.randomUUID();
					depart.setBmDm(uuid.toString());
					depart.setSjbmDm(sjbmNo); // 暂时将上级部门编号放在代码中
					depart.setBmNo(bmNo);
					depart.setBmMc(bmMc);
					depart.setBmMcJ(bmMcJ);
					depart.setBz(bz);
					list.add(depart);
					newNos.add(bmNo);
				}

				if (!"0".equals(res.getCode())) {// 如果已经是为空了，不需要再判断
					if (flag1 == 0) {
						for (Department d : list) {// 检查上级部门编号
							String sjbmNo = d.getSjbmDm();
							if ("2".equals(sjbmNo)) {
								continue;
							}
							String sjbmDm = "";
							int flag = 0;
							for (String no : deptNos) {
								if (sjbmNo.equals(no)) {
									flag = 1;
									break;
								}
							}
							if (flag == 1) { // 上级编号在已有的Nos中
								sjbmDm = employeeService.getBmdmByBmNo(sjbmNo);
								d.setSjbmDm(sjbmDm);
							} else {
								int j = 0;
								for (; j < newNos.size(); j++) {
									if (sjbmNo.equals(newNos.get(j))) {
										flag = 1;
										break;
									}
								}
								if (flag == 1) { // 上级编号在新的Nos中
									sjbmDm = list.get(j).getBmDm();
									d.setSjbmDm(sjbmDm);
								} else {
									res.setSuccess(true);
									res.setCode("0");
								}
							}
						}
					} else {
						for (Department d : list) {
							String sjbmNo = d.getSjbmDm();
							if ("2".equals(sjbmNo)) {
								continue;
							}
							String sjbmDm = "";
							int flag = 0;
							for (String no : deptNos) {
								if (sjbmNo.equals(no)) {
									flag = 1;
									break;
								}
							}
							if (flag == 1) { // 上级编号在已有的Nos中
								sjbmDm = employeeService.getBmdmByBmNo(sjbmNo);
								d.setSjbmDm(sjbmDm);
							} else {
								int j = 0;
								for (; j < newNos.size(); j++) {
									if (sjbmNo.equals(newNos.get(j))) {
										flag = 1;
										break;
									}
								}
								if (flag == 1) { // 上级编号在新的Nos中
									sjbmDm = list.get(j).getBmDm();
									d.setSjbmDm(sjbmDm);
								} else {
									d.setSjbmDm("2");
								}
							}
						}
					}
				}
				if ("0" == res.getCode()) {
					res.setSuccess(true);
					res.setMsg("是否将不存在上级部门的部门移入未分组?");
					printHttpServletResponse(GsonUtil.toJson(res));
				} else {
					for (Department d : list) {
						d.setLrSj(getSysTimestamp());
						d.setLrryDm(loginUser.getYhDm());
						departmentService.addDepartment(d, create_user);

						// 添加部门，自动添加该部门的默认岗位
						Station station = new Station();
						station.setBmDm(d.getBmDm());
						station.setGwDm(d.getBmDm() + "YG");
						station.setGwMc("员工");
						station.setLrSj(getSysTimestamp());
						station.setDefaultGw("0"); // 默认岗位 0是，1否
						stationService.addStation(station, loginUser.getYhMc());

						// 为默认角色添加新部门权限 by yuman.gao
						roleService.addDeptGrant("0", d.getBmDm().split(","));
					}
					res.setSuccess(true);
					res.setMsg("导入成功!");
				}
				printHttpServletResponse(GsonUtil.toJson(res));
			} else if ("员工信息表".equals(titleCell.getStringCellValue().trim())
					&& "1".equals(value)) {
				List<Employee> list = new ArrayList<Employee>();
				List<Card> cardList = new ArrayList<Card>();
				// List<CardHistoryModel> cardHistoryList = new
				// ArrayList<CardHistoryModel>();
				List<BoundCard> boundCardList = new ArrayList<BoundCard>();
				if (totalRow < 8) {
					res.setSuccess(false);
					res.setMsg("导入失败，文件内容为空！");
					printHttpServletResponse(GsonUtil.toJson(res));
					return;
				}
				for (int i = 8; i <= totalRow; i++) {
					HSSFRow row = sheet.getRow(i);
					Employee employee = new Employee();
					String yhNo = null;// 员工工号 必填
					String yhMc = null;// 员工名称 必填
					String bmNos = null;// 主属部门代码(为解析) 必填
					String[] bmNoCode = null;// 主属部门代码
					String bmNo = null;
					String gwDm = null; // 岗位代码
					String sjldyhNo = null;// 领导工号
					String zjbh = null;// 证件类型
					String sfzhm = null;// 证件号码 必填
					List<String> cardNum = new ArrayList<String>(); // 存放卡号的list
					String card_pledge = null;// 卡片押金
					List<String> type_dm = new ArrayList<String>(); // 卡片类型
					String card_validity = null; // 卡片有效期
					String operate_time = ""; // 创建时间
					String nl = null;// 年龄
					String xb = null;// 性别
					String ygzz = null;// 员工住址
					String gddh = null;// 固定电话
					String sjhm = null;// 手机号码
					String dzyj = null;// 电子邮箱
					String rzsj = null;// 入职时间
					String bz = null;// 备注
					// String staff_hire_state = null;// 备注

					String photoncardNum = null;// 光子卡片号码
					String phonetonNum = null;// 手机光钥匙号码
					String ICNum = null;// Ic卡号码
					/* 部门代码 */
					Cell bmNoCell = row.getCell(0);
					if (null != bmNoCell) {
						bmNoCell.setCellType(Cell.CELL_TYPE_STRING);
						bmNos = bmNoCell.getStringCellValue();
						if (null == bmNos || "".equals(bmNos)) {
							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1) + "行的“部门名称”是否为空！");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;

						}
						bmNoCode = bmNos.split(" :");
						bmNo = employeeService.getBmdmByBmNo(bmNoCode[1]);

					} else {
						res.setSuccess(false);
						res.setMsg("导入失败，请检查第" + (i + 1) + "行的“部门名称”是否为空！");
						printHttpServletResponse(GsonUtil.toJson(res));
						return;
					}

					/* 员工工号 */
					Cell yhNoCell = row.getCell(1);
					if (null != yhNoCell) {
						yhNoCell.setCellType(Cell.CELL_TYPE_STRING);
						yhNo = yhNoCell.getStringCellValue().trim();
						if (null == yhNo || "".equals(yhNo)) {
							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1) + "行的“员工工号”是否为空！");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						}else if (yhNo.length() > 40) {
							res.setSuccess(false);
							res.setMsg("导入失败，员工工号不能超过40个字符！");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						}
					} else {
						res.setSuccess(false);
						res.setMsg("导入失败，请检查第" + (i + 1) + "行的“员工工号”是否为空！");
						printHttpServletResponse(GsonUtil.toJson(res));
						return;
					}

					/* 员工名称 */
					Cell yhMcCell = row.getCell(2);
					if (null != yhMcCell) {
						yhMcCell.setCellType(Cell.CELL_TYPE_STRING);
						yhMc = yhMcCell.getStringCellValue();
						if (null == yhMc || "".equals(yhMc)) {
							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1) + "行的“员工名称”是否为空！");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						} else if (yhMc.length() > 40) {
							res.setSuccess(false);
							res.setMsg("导入失败，员工名称不能超过40个字符！");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						}
					} else {
						res.setSuccess(false);
						res.setMsg("导入失败，请检查第" + (i + 1) + "行的“员工名称”是否为空！");
						printHttpServletResponse(GsonUtil.toJson(res));
						return;
					}

					/* 入职时间 */
					Cell rzsjCell = row.getCell(3);
					if (null != rzsjCell) {
						rzsjCell.setCellType(Cell.CELL_TYPE_STRING);
						rzsj = rzsjCell.getStringCellValue().trim();
						if (null == rzsj || "".equals(rzsj)) {
							Date date = new Date();
							SimpleDateFormat dateformat = new SimpleDateFormat(
									"yyyy-MM-dd");
							rzsj = dateformat.format(date);
						}
					} else {
						Date date = new Date();
						SimpleDateFormat dateformat = new SimpleDateFormat(
								"yyyy-MM-dd");
						rzsj = dateformat.format(date);

					}

					/* 证件类型 */
					Cell zjbhCell = row.getCell(4);
					if (null != zjbhCell) {
						zjbhCell.setCellType(Cell.CELL_TYPE_STRING);
						zjbh = zjbhCell.getStringCellValue();
						if ("身份证".equals(zjbh)) {
							zjbh = "100";
						} else if ("护照".equals(zjbh)) {
							zjbh = "200";
						}
					}

					/* 证件号码 */
					Cell sfzhmCell = row.getCell(5);
					if (null != sfzhmCell) {
						sfzhmCell.setCellType(Cell.CELL_TYPE_STRING);
						sfzhm = sfzhmCell.getStringCellValue().trim();

						if ((null == zjbh || "".equals(zjbh))
								&& (!"".equals(sfzhm))) { // 证件编号不为空时，证件类型不能为空

							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1)
									+ "行是否没有为“证件号码”选择“证件类型”!");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						}
					}
					String cardNumTemp_shouji = null;
					String cardNumTemp_guangzi = null;
					String cardNumTemp_ic = null;
					/* 手机光钥匙号码 */
					Cell cardNumCell1 = row.getCell(6);
					if (null != cardNumCell1
							&& !"".equals(cardNumCell1.getStringCellValue()
									.trim())) {
						cardNumCell1.setCellType(Cell.CELL_TYPE_STRING);
						phonetonNum = cardNumCell1.getStringCellValue().trim();
						cardNumTemp_shouji = phonetonNum;
						cardNum.add(phonetonNum);
						type_dm.add("1");
					}

					/* 光子卡片号码 */
					Cell cardNumCell0 = row.getCell(7);
					if (null != cardNumCell0
							&& !"".equals(cardNumCell0.getStringCellValue()
									.trim())) {
						cardNumCell0.setCellType(Cell.CELL_TYPE_STRING);
						photoncardNum = cardNumCell0.getStringCellValue()
								.trim();
						cardNumTemp_guangzi = photoncardNum;
						cardNum.add(photoncardNum);
						type_dm.add("2");
					}

					/* Ic卡号码 */
					Cell cardNumCell2 = row.getCell(8);
					if (null != cardNumCell2
							&& !"".equals(cardNumCell2.getStringCellValue()
									.trim())) {
						cardNumCell2.setCellType(Cell.CELL_TYPE_STRING);
						ICNum = cardNumCell2.getStringCellValue().trim();
						cardNumTemp_ic = ICNum;
						cardNum.add(ICNum);
						type_dm.add("3");
					}
					
					if(null!=cardNumTemp_shouji){
						cardNumList.add(cardNumTemp_shouji);
					}
					if(null!=cardNumTemp_guangzi){
						cardNumList.add(cardNumTemp_guangzi);
					}
					if(null!=cardNumTemp_ic){
						cardNumList.add(cardNumTemp_ic);
					}

					for (String cardNumT : cardNumList) {
						if (cardNumList.indexOf(cardNumTemp_shouji)!=cardNumList.lastIndexOf(cardNumTemp_shouji)) {
							res.setSuccess(false);
							res.setMsg("导入失败，表格第" + (i + 1) + "行的卡号"
									+ cardNumTemp_shouji + "重复");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						}else if (cardNumList.indexOf(cardNumTemp_guangzi)!=cardNumList.lastIndexOf(cardNumTemp_guangzi)) {
							res.setSuccess(false);
							res.setMsg("导入失败，表格第" + (i + 1) + "行的卡号"
									+ cardNumTemp_guangzi + "重复");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						}else if (cardNumList.indexOf(cardNumTemp_ic)!=cardNumList.lastIndexOf(cardNumTemp_ic)) {
							res.setSuccess(false);
							res.setMsg("导入失败，表格第" + (i + 1) + "行的卡号"
									+ cardNumTemp_ic + "重复");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						}
					}
					
					/* 设置卡片有效期 如果用户填了卡但没有填有效期，则默认卡有效期是十年 */
					if ((null != cardNumCell0 && !"".equals(photoncardNum))
							|| (null != cardNumCell1 && !"".equals(phonetonNum))
							|| ((null != cardNumCell2 && !"".equals(ICNum)))) {
						SimpleDateFormat dateformat = new SimpleDateFormat(
								"yyyy-MM-dd");

						Calendar c = Calendar.getInstance();
						c.add(Calendar.YEAR, +10);
						card_validity = dateformat.format(c.getTime());
					}

					/* 设置卡片押金 */
					Cell card_pledgeCell3 = row.getCell(9);
					if (null != card_pledgeCell3
							&& !"".equals(card_pledgeCell3.getStringCellValue()
									.trim())) {
						card_pledgeCell3.setCellType(Cell.CELL_TYPE_STRING);
						card_pledge = card_pledgeCell3.getStringCellValue()
								.trim();
						if ((null == cardNumCell0 || "".equals(photoncardNum))
								&& (null == cardNumCell1 || ""
										.equals(phonetonNum))
								&& (null == cardNumCell2 || "".equals(ICNum))
								&& (!"".equals(card_pledge))) {

							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1)
									+ "行是否没有为“卡片押金”填写“卡号”！");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						}
					}

					Cell card_validityCell4 = row.getCell(10);
					if (null != card_validityCell4 && !"".equals(card_validityCell4.getStringCellValue().trim())) {
						card_validityCell4.setCellType(Cell.CELL_TYPE_STRING);
						card_validity = card_validityCell4.getStringCellValue()
								.trim();
						if ((null == cardNumCell0 || "".equals(photoncardNum))
								&& (null == cardNumCell1 || ""
										.equals(phonetonNum))
								&& (null == cardNumCell2 || "".equals(ICNum))
								&& (!"".equals(card_validity))) {
							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1)
									+ "行是否没有为“卡片有效期”填写“卡号”！");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;

						}
					}
					/* 员工性别 */
					Cell xbCell = row.getCell(11);
					if (null != xbCell) {
						xbCell.setCellType(Cell.CELL_TYPE_STRING);
						xb = xbCell.getStringCellValue();
						if ("女".equals(xb)) {
							xb = "0";
						} else if ("男".equals(xb)) {
							xb = "1";
						} else {
							xb = "";
						}
					}

					/* 员工住址 */
					Cell ygzzCell = row.getCell(12);
					if (null != ygzzCell) {
						ygzzCell.setCellType(Cell.CELL_TYPE_STRING);
						ygzz = ygzzCell.getStringCellValue().trim();
					}

					/* 固定电话 */
					Cell gddhCell = row.getCell(13);
					if (null != gddhCell) {
						gddhCell.setCellType(Cell.CELL_TYPE_STRING);
						gddh = gddhCell.getStringCellValue().trim();
					}
					/* 手机号码 */
					Cell sjhmCell = row.getCell(14);
					if (null != sjhmCell) {
						sjhmCell.setCellType(Cell.CELL_TYPE_STRING);
						sjhm = sjhmCell.getStringCellValue().trim();
					}
					/* 电子邮箱 */
					Cell dzyjCell = row.getCell(15);
					if (null != dzyjCell) {
						dzyjCell.setCellType(Cell.CELL_TYPE_STRING);
						dzyj = dzyjCell.getStringCellValue().trim();
					}

					/* 年龄 */
					Cell nlCell = row.getCell(16);
					if (null != nlCell) {
						nlCell.setCellType(Cell.CELL_TYPE_STRING);
						nl = nlCell.getStringCellValue().trim();
					}
					/* 备注 */
					Cell bzCell = row.getCell(17);
					if (null != bzCell) {
						bzCell.setCellType(Cell.CELL_TYPE_STRING);
						bz = bzCell.getStringCellValue();
					}

					if (!yhNo.matches("^[A-Za-z0-9-]+$")) {
						res.setSuccess(false);
						res.setMsg("导入失败，请检查第" + (i + 1)
								+ "行的“员工工号”是否为英文字母、数字或-!");
						printHttpServletResponse(GsonUtil.toJson(res));
						return;

					}
					if (!yhMc
							.matches("^([\u4e00-\u9fa5]|[a-zA-Z]|[0-9\\s\\,，.])+$")) {

						res.setSuccess(false);
						res.setMsg("导入失败，请检查第" + (i + 1)
								+ "行的“员工名称”是否含有除，.外的符号!");
						printHttpServletResponse(GsonUtil.toJson(res));
						return;
					}

					int result = employeeService.isContainYhDM(yhNo, "");// 验证完工号是否存在，再验证卡号是否存在
					if (result > 0) {
						res.setSuccess(false);
						res.setMsg("导入失败，第" + (i + 1) + "行的“员工工号”" + yhNo
								+ "已存在!");
						printHttpServletResponse(GsonUtil.toJson(res));
						return;

					}

					if (rzsj != null
							&& !"".equals(rzsj)
							&& !rzsj.matches("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$")) {

						res.setSuccess(false);
						res.setMsg("导入失败，请检查第" + (i + 1) + "行的“入职时间”格式是否正确!");
						printHttpServletResponse(GsonUtil.toJson(res));
						return;
					}

					if ("100".equals(zjbh)) {
						if (sfzhm != null
								&& !sfzhm
										.matches("^(^[1-9][0-9]{7}((0[0-9])|(1[0-2]))(([0|1|2][0-9])|3[0-1])[0-9]{3}$)|(^[1-9][0-9]{5}[1-9][0-9]{3}((0[0-9])|(1[0-2]))(([0|1|2][0-9])|3[0-1])(([0-9]{4})|[0-9]{3}[Xx])$)$")) {

							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1)
									+ "行的“身份证号码”格式是否正确!");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						}
					} else if ("200".equals(zjbh)) {
						if (sfzhm != null
								&& !sfzhm
										.matches("/^1[45][0-9]{7}|E[0-9]{8}|G[0-9]{8}|P[0-9]{7}|S[0-9]{7,8}|D[0-9]$/")) {

							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1)
									+ "行的“护照号码”格式是否正确!");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						}
					} else {
						zjbh = "100"; // 当为空时，默认为身份证
					}

					if (null != photoncardNum && !"".equals(photoncardNum)) {
						if (!photoncardNum.matches("^\\d*[1-9]\\d*$")) {

							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1)
									+ "行的“光子卡”号码格式是否正确!");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						} else {
							Long photoncardNum1 = Long.valueOf(photoncardNum);
							if (photoncardNum1 < 1
									|| photoncardNum1 > 2147483647) {

								res.setSuccess(false);
								res.setMsg("导入失败，请检查第" + (i + 1)
										+ "行的“光子卡”号码是否在1~2147483647 内!");
								printHttpServletResponse(GsonUtil.toJson(res));
								return;
							}
						}
					}

					if (null != phonetonNum && !"".equals(phonetonNum)) {
						if (!phonetonNum.matches("^\\d*[1-9]\\d*$")) {

							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1)
									+ "行的“手机光钥匙”号码格式是否正确!");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						} else {
							Long phonetonNum1 = Long.valueOf(phonetonNum);
							if (phonetonNum1 < 1 || phonetonNum1 > 2147483647) {

								res.setSuccess(false);
								res.setMsg("导入失败，请检查第" + (i + 1)
										+ "行的“手机光钥匙”号码是否在1~2147483647 内!");
								printHttpServletResponse(GsonUtil.toJson(res));
								return;
							}
						}
					}

					if (ICNum != null && !"".equals(ICNum)) {
						if (!ICNum.matches("^\\d*[1-9]\\d*$")) {

							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1)
									+ "行的“IC卡”号码格式是否正确!");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						} else {
							Long ICNum1 = Long.valueOf(ICNum);
							if (ICNum1 < 1 || ICNum1 > 2147483647) {

								res.setSuccess(false);
								res.setMsg("导入失败，请检查第" + (i + 1)
										+ "行的“IC卡”号码是否在1~2147483647 内!");
								printHttpServletResponse(GsonUtil.toJson(res));
								return;
							}
						}

					}

					for (String card_num : cardNum) {
						Integer boundCardCount = cardService
								.validBoundCard(card_num);// 判断导入员工下的卡片是否已经绑定
						if (boundCardCount > 0) {

							res.setSuccess(false);
							res.setMsg("导入失败，第" + (i + 1) + "行卡号为" + card_num
									+ "的卡已被绑定!");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						}
					}

					if (null != card_pledge && !"".equals(card_pledge)) {
						if (!card_pledge
								.matches("^(([1-9]{1}\\d{0,15})|([0]{1}))(\\.(\\d){0,4})?$")) {

							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1)
									+ "行的“卡片押金”格式是否正确!");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						}
					}
					if (card_validity != null && !"".equals(card_validity)) {
						if (!card_validity
								.matches("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$")) {

							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1)
									+ "行的“卡片有效期”格式是否正确!");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						}
					} else {
						card_validity = "";
					}

					if (ygzz != null && !"".equals(ygzz)) {
						if (ygzz.length() > 50) {
							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1) + "行的“住址”是否超过50位！");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						}
					}

					if (gddh != null && !"".equals(gddh)) {
						if (!gddh.matches("^[0-9]{3,4}-[0-9]{7,8}$")) {

							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1)
									+ "行的“固定电话”格式是否正确!");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						}
					}
					if (sjhm != null
							&& !"".equals(sjhm)
							&& !sjhm.matches("^(((13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$")) {

						res.setSuccess(false);
						res.setMsg("导入失败，请检查第" + (i + 1) + "行的“手机号码”格式是否正确!");
						printHttpServletResponse(GsonUtil.toJson(res));
						return;
					}

					if (dzyj != null
							&& !"".equals(dzyj)
							&& !dzyj.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
						res.setSuccess(false);
						res.setMsg("导入失败，请检查第" + (i + 1) + "行的“电子邮箱”格式是否正确!");
						printHttpServletResponse(GsonUtil.toJson(res));
						return;
					}

					if (nl != null && !"".equals(nl)
							&& !nl.matches("^\\d*[0-9]\\d*$")) {
						res.setSuccess(false);
						res.setMsg("导入失败，请检查第" + (i + 1) + "行的“年龄”格式是否正确!");
						printHttpServletResponse(GsonUtil.toJson(res));
						return;
					}

					if (nl != null && !"".equals(nl)
							&& !nl.matches("^(?:[1-9][0-9]?|1[01][0-9]|150)$")) {
						res.setSuccess(false);
						res.setMsg("导入失败，请检查第" + (i + 1)
								+ "行的“年龄”范围是否在1-150以内!");
						printHttpServletResponse(GsonUtil.toJson(res));
						return;
					}

					if (bz != null && !"".equals(bz)) {
						if (bz.length() > 75) {
							res.setSuccess(false);
							res.setMsg("导入失败，请检查第" + (i + 1) + "行的“备注”是否超过75位！");
							printHttpServletResponse(GsonUtil.toJson(res));
							return;
						}
					}

					// 插入员工信息表
					UUID uuid = UUID.randomUUID();
					employee.setYhDm(uuid.toString());
					employee.setYhNo(yhNo);
					employee.setYhMc(yhMc);
					gwDm = employeeService.getDefaultGwDmByBmDm(bmNo);// 获取部门对应的岗位代码
					employee.setBmDm(bmNo);
					employee.setGwDm(gwDm);
					employee.setSjldyhNo(sjldyhNo);
					employee.setZjbh(zjbh);
					employee.setSfzhm(sfzhm);
					employee.setNl(nl);
					employee.setXb(xb);
					employee.setYgzz(ygzz);
					employee.setGddh(gddh);
					employee.setSjhm(sjhm);
					employee.setDzyj(dzyj);
					employee.setRzsj(rzsj);
					employee.setBz(bz);
					list.add(employee);

					/* 根据表格中的三个卡号是否存在，来插入卡片信息表 */
					for (int j = 0; j < cardNum.size(); j++) {
						Card card = new Card();
						BoundCard boundCard = new BoundCard();
						if (null == cardNum.get(j) || "".equals(cardNum.get(j))) {
							continue;
						} else {
							// 设置卡片信息
							card.setCard_type_id(type_dm.get(j));// 设置卡片类型代码
							card.setCard_num((cardNum.get(j)));// 设置卡号
							card.setCard_pledge(card_pledge);// 设置卡片押金
							card.setState_dm("10");// 设置卡片默认状态码
							card.setCard_validity(card_validity);// 卡片有效时间
							card.setCreate_user(create_user);// 创建人员代码
							card.setValidity_flag("0");// 有效标示
							cardList.add(card);

							// 设置卡片日志信息
							/*
							 * Date date = new Date(); operate_time =
							 * DateUtil.getDateString(date);
							 * cardHistoryModel.setOperate_time(operate_time);//
							 * 设置创建时间 cardHistoryModel.setCard_num(Integer
							 * .parseInt((cardNum.get(j))));
							 * cardHistoryModel.setOperate("20");
							 * cardHistoryList.add(cardHistoryModel);
							 */

							// 设置绑卡信息
							boundCard.setStaff_num(yhNo);
							boundCard.setCard_num((cardNum.get(j)));
							boundCard.setBound_date(operate_time);
							boundCard.setValidity_flag("0");
							boundCard.setCreate_user(create_user);
							boundCard.setCreate_time(operate_time);
							boundCardList.add(boundCard);

						}
					}

				}
				// 判断导入的员工工号是否重复
				/*
				 * boolean confirm = true; for (int k = 0; k < list.size(); k++)
				 * { String staff_num = list.get(k).getYhNo(); int result =
				 * employeeService.isContainYhDM(staff_num, ""); if (result > 0)
				 * { confirm = false; request.setAttribute("message", "导入失败，第" +
				 * (k+7) + "行的“员工工号”" + staff_num + "已存在!"); break; } }
				 */

				/* if (confirm) { */

				/*
				 * for(String card_num:cardNum){ Integer count =
				 * cardService.validCardNum(card_num); if(count>0){
				 * request.setAttribute("message", "导入失败，第" + (i + 1) +
				 * "行的"+card_num+"卡号已存在!"); return "success"; } }
				 */
				employeeService.batchAddEmployee(list);
				for (Card card : cardList) {
					Integer count = cardService
							.validCardNum(card.getCard_num());
					if (count > 0) {
						cardService.updateCardState(card.getCard_num(), "20");
						continue;
					} else {
						cardService.addNewCard(card);// 插入卡片信息表
					}
				}

				for (BoundCard boundCard : boundCardList) {
					Integer boundCardCount = cardService
							.validBoundCard(boundCard.getCard_num());// 判断导入员工下的卡片是否已经绑定
					if (boundCardCount > 0) {
						continue;
					} else {
						Map<String, String> map = new HashMap<String, String>();
						String staff_no = boundCard.getStaff_num();
						String staff_num = employeeService
								.selectStaffNum(staff_no);
						map.put("staff_num", staff_num);
						map.put("card_num", boundCard.getCard_num());
						map.put("create_user", boundCard.getCreate_user());
						cardService.addCardBound(map);// 插入绑定卡片信息

						// 设置卡片日志信息
						Date date = new Date();
						CardHistoryModel cardHistoryModel = new CardHistoryModel();
						String operate_time = DateUtil.getDateString(date);
						
						cardHistoryModel.setStaff_no(staff_num);
						cardHistoryModel.setCreate_user(boundCard.getCreate_user());
						cardHistoryModel.setOperate_time(operate_time);// 设置创建时间
						cardHistoryModel.setCard_num((boundCard.getCard_num()));
						cardHistoryModel.setOperate("20");
						countService.insertCardHistoryInfo(cardHistoryModel,
								create_user);// 插入卡片日志信息表
					}
				}

				res.setSuccess(true);
				res.setMsg("导入成功!");
				printHttpServletResponse(GsonUtil.toJson(res));
			} else {
				res.setSuccess(false);
				res.setMsg("导入失败，请检查模版或选择项是否正确!");
				printHttpServletResponse(GsonUtil.toJson(res));
			}
		} catch (Exception e) {
			res.setSuccess(false);
			res.setMsg("导入失败!");
			printHttpServletResponse(GsonUtil.toJson(res));
			return;
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 下载批量导入模板
	 * 
	 * 
	 */
	public void downloadTemplate() {
		HttpServletResponse response = getHttpServletResponse();
		OutputStream out = null;
		ExcelExportTemplate excelExport = new ExcelExportTemplate();

		List<TitleRowCell> titleRowCells = new ArrayList<TitleRowCell>();
		// List<TitleRowCell> tRCs = new ArrayList<TitleRowCell>();
		// List<String> list = employeeService.getAllStaffDept(null);// 获取全部部门代码
		boolean isLayer = roleService.isLayer();
		String layerDeptNum = null;
		if (isLayer) {
			Role role = (Role) getHttpSession().getAttribute(
					GlobalConstant.LOGIN_USER_CURRENT_ROLE);
			User user = (User) getHttpSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			layerDeptNum = departmentService.deptGetLayerDeptNum(
					user.getYhDm(), role.getJsDm());
		}

		List<String> list = employeeService.getAllStaffDept(layerDeptNum);// 获取全部部门代码

		TitleRowCell t1t = new TitleRowCell("员工工号", true);
		TitleRowCell t2t = new TitleRowCell("员工名称", true);
		TitleRowCell t = new TitleRowCell("部门名称", true);
		for (String staffDept : list) {
			t.addSuggest(staffDept);
		}
		TitleRowCell t5t = new TitleRowCell("证件类型", false);
		t5t.addSuggest("身份证");
		t5t.addSuggest("护照");
		titleRowCells.add(t);
		TitleRowCell t6t = new TitleRowCell("证件号码", false);
		TitleRowCell tct = new TitleRowCell("光子卡", false);
		TitleRowCell tct1 = new TitleRowCell("手机光钥匙", false);
		TitleRowCell tct2 = new TitleRowCell("IC卡", false);
		TitleRowCell tct3 = new TitleRowCell("卡片押金", false);
		TitleRowCell tct4 = new TitleRowCell("卡片有效期", false);
		TitleRowCell t7t = new TitleRowCell("性别", false);

		t7t.addSuggest("男");
		t7t.addSuggest("女");
		TitleRowCell t8t = new TitleRowCell("住址", false);
		TitleRowCell t9t = new TitleRowCell("固定电话", false);
		TitleRowCell t10t = new TitleRowCell("手机号码", false);
		TitleRowCell t11t = new TitleRowCell("电子邮箱", false);
		TitleRowCell t12t = new TitleRowCell("入职时间", false);
		TitleRowCell t13t = new TitleRowCell("年龄", false);
		TitleRowCell t14t = new TitleRowCell("备注", false);
		titleRowCells.add(t1t);
		titleRowCells.add(t2t);
		titleRowCells.add(t12t);
		titleRowCells.add(t5t);
		titleRowCells.add(t6t);
		titleRowCells.add(tct1);
		titleRowCells.add(tct);
		titleRowCells.add(tct2);
		titleRowCells.add(tct3);
		titleRowCells.add(tct4);
		titleRowCells.add(t7t);
		titleRowCells.add(t8t);
		titleRowCells.add(t9t);
		titleRowCells.add(t10t);
		titleRowCells.add(t11t);

		titleRowCells.add(t13t);
		titleRowCells.add(t14t);
		excelExport.createLongTitleRow(titleRowCells);
		try {
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName = "员工信息模版.xls";
			response.setHeader("content-Disposition", "attachment;filename="
					+ DownloadFile.toUtf8String(fileName));
			excelExport.getWorkbook().write(out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 上传头像
	 * 
	 */
	public String uploadImg() {
		HttpServletRequest request = getHttpServletRequest();
		ContinueFTP continueFTP = new ContinueFTP();
		try {
			String ftpPropertiesPath = request
					.getSession()
					.getServletContext()
					.getRealPath(
							"/" + "WEB-INF" + File.separator + "classes"
									+ File.separator + "conf" + File.separator
									+ "properties" + File.separator
									+ "ftp.properties");
			com.kuangchi.sdd.util.file.FTP ftp = com.kuangchi.sdd.util.file.FTPUtil
					.getFtp(ftpPropertiesPath);
			String dateString = DateUtil.getDateString(new Date(), "yyyyMMdd");
			String dateTimeString = DateUtil.getDateString(new Date(),
					"yyyyMMddHHmmssSSS");
			// String fileName=dateTimeString+imgFileFileName;
			String fileName = dateTimeString;

			String imgPath = dateString + "/" + fileName;
			continueFTP.connect(ftp.getHost(), ftp.getPort(),
					ftp.getUserName(), ftp.getPassword());

			continueFTP.upload(imgFile, imgPath);

			request.setAttribute("message", "上传成功");
			request.setAttribute("imgPath", imgPath);
		} catch (Exception e1) {
			e1.printStackTrace();
			request.setAttribute("message", "上传失败");
		}
		return "success";
	}

	/**
	 * 获取头像
	 * 
	 */
	public void getImg() {
		HttpServletRequest request = getHttpServletRequest();
		HttpServletResponse response = getHttpServletResponse();

		String imgPath = request.getParameter("imgPath");

		ContinueFTP continueFTP = new ContinueFTP();
		java.io.InputStream is = null;
		OutputStream out = null;
		try {

			String ftpPropertiesPath = request
					.getSession()
					.getServletContext()
					.getRealPath(
							"/" + "WEB-INF" + File.separator + "classes"
									+ File.separator + "conf" + File.separator
									+ "properties" + File.separator
									+ "ftp.properties");
			com.kuangchi.sdd.util.file.FTP ftp = com.kuangchi.sdd.util.file.FTPUtil
					.getFtp(ftpPropertiesPath);
			boolean getConnection = true;
			try {
				continueFTP.connect(ftp.getHost(), ftp.getPort(),
						ftp.getUserName(), ftp.getPassword());
			} catch (Exception e) {
				getConnection = false;
			}

			if (getConnection && null != imgPath && !imgPath.trim().equals("")
					&& !"undefined".equals(imgPath)) {
				is = continueFTP.download(imgPath);
			} else {
				// 如果没有上传头像，则给一个默认头像
				String defaultImg = request
						.getSession()
						.getServletContext()
						.getRealPath(
								"/" + "businessConsole" + File.separator
										+ "images" + File.separator
										+ "defaultImg.jpg");
				File file = new File(defaultImg);
				is = new FileInputStream(file);
			}
			out = response.getOutputStream();
			byte[] buffer = new byte[1024];
			while (is.read(buffer) != -1) {
				out.write(buffer);
			}
			out.flush();
			continueFTP.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// 导出数据库中的数据
	public void downloadAllEmployee() {
		HttpServletResponse response = getHttpServletResponse();

		boolean isLayer = roleService.isLayer();
		String layerDeptNum = null;
		if (isLayer) {
			Role role = (Role) getHttpSession().getAttribute(
					GlobalConstant.LOGIN_USER_CURRENT_ROLE);
			User user = (User) getHttpSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			layerDeptNum = departmentService.getLayerDeptNum(user.getYhDm(),
					role.getJsDm());
		}

		List<Employee> employeeList0 = employeeService
				.getAllEmployee(layerDeptNum);
		List<Employee> employeeList = new ArrayList<Employee>();
		// 将0、1转换为在职、离职
		for (Employee cemployee : employeeList0) {
			if ("1".equals(cemployee.getStaff_hire_state())) {
				cemployee.setStaff_hire_state("离职");
			} else {
				cemployee.setStaff_hire_state("在职");
			}
			employeeList.add(cemployee);
		}

		String jsonList = GsonUtil.toJson(employeeList);
		// list中的map为 ： 列键----列值的方式
		List list = GsonUtil.getListFromJson(jsonList, ArrayList.class);
		OutputStream out = null;

		Field[] fields = Employee.class.getDeclaredFields();
		List<String> colList = new ArrayList<String>();
		List<String> colTitleList = new ArrayList<String>();

		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			if ("yhNo".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("员工工号");
			} else if ("yhMc".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("员工名称");
			} else if ("nl".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("年龄");
			} else if ("xbMc".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("性别");
			} else if ("ygzz".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("员工地址");
			} else if ("zjmc".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("证件类型");
			} else if ("sfzhm".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("证件号码");
			} else if ("sjhm".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("手机号码");
			} else if ("gddh".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("固定电话");
			} else if ("dzyj".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("电子邮箱");
			} else if ("bmNo".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("部门编号");
			} else if ("bmMc".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("部门名称");
			} else if ("sjldyhNo".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("上级领导工号");
			} else if ("sjldyhMc".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("上级领导名称");
			} else if ("rzsj".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("入职时间");
			} else if ("gwmc".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("岗位名称");
			} else if ("staff_hire_state".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("职位状态");
			} else if ("bz".equals(fieldName)) {
				colList.add(fieldName);
				colTitleList.add("备注");
			}
		}
		// 列数据键
		String[] cols = new String[colList.size()];
		// 列表题
		String[] colTitles = new String[colList.size()];
		for (int i = 0; i < colList.size(); i++) {
			cols[i] = colList.get(i);
			colTitles[i] = colTitleList.get(i);
		}
		try {
			HttpServletRequest request = getHttpServletRequest();
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName = "员工信息表.xls";
			response.setHeader("content-Disposition", "attachment;filename="
					+ DownloadFile.toUtf8String(fileName));
			Workbook workbook = ExcelUtilSpecial.exportExcel("员工表", colTitles,
					cols, list);
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 验证员工编号是否存在
	 */
	public void validEmployee() {
		HttpServletRequest request = getHttpServletRequest();
		String yhNo = request.getParameter("yhNo");
		String yhDm = request.getParameter("yhDm");
		printHttpServletResponse(GsonUtil.toJson(employeeService
				.validatEmployee(yhNo, yhDm)));
	}

	private void restModel() {
		this.model = new Employee();
	}

	public void setModel(Employee model) {
		this.model = model;
	}

	public void getPaperTypes() {
		List<Map> list = new ArrayList<Map>();

		List<PaperType> paperTypeList = employeeService.getPaperTypes();
		for (PaperType pt : paperTypeList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("VALUE", pt.getPapersType());
			map.put("TEXT", pt.getPapersValue());
			list.add(map);
		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}

	public File getUploadEmployeeFile() {
		return uploadEmployeeFile;
	}

	public void setUploadEmployeeFile(File uploadEmployeeFile) {
		this.uploadEmployeeFile = uploadEmployeeFile;
	}

	public String getUploadEmployeeFileFileName() {
		return uploadEmployeeFileFileName;
	}

	public void setUploadEmployeeFileFileName(String uploadEmployeeFileFileName) {
		this.uploadEmployeeFileFileName = uploadEmployeeFileFileName;
	}

	public File getImgFile() {
		return imgFile;
	}

	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public String getImgFileFileName() {
		return imgFileFileName;
	}

	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}

	// 查询所有卡片信息
	public void selectAllCards() {
		HttpServletRequest request = getHttpServletRequest();
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		String cardnum = request.getParameter("card_num");
		String cardtype = request.getParameter("card_type");
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", page);
		map.put("rows", rows);
		map.put("card_num", cardnum);
		map.put("card_type", cardtype);
		Grid<Card> cardList = employeeService.selectAllCards(map);
		printHttpServletResponse(GsonUtil.toJson(cardList));
	}

	// 查询卡，用户是否存在以及员工绑定的卡下是否已绑定账户
	public void isExistAccountAndCard() {
		HttpServletRequest request = getHttpServletRequest();
		String card_num = request.getParameter("card_num");
		String yhdm = request.getParameter("yhDm");
		String account_num = request.getParameter("account_num");
		String[] list1 = GsonUtil.toBean(account_num, String[].class);
		JsonResult result = new JsonResult();
		boolean flag = false;
		JsonResult result1 = new JsonResult();
		flag = employeeService.isExistUserAndCard(card_num, yhdm);
		result1 = employeeService.isExistAccountUnderBoundCard(yhdm, list1,
				card_num);
		if (flag && result1.isSuccess()) {
			result.setSuccess(flag);
		} else if (flag == false) {
			result.setSuccess(flag);
			result.setMsg("选择的卡或员工不存在，请刷新页面");
		} else if (result1.isSuccess() == false) {
			result.setSuccess(false);
			result.setMsg(result1.getMsg());
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	// 判断卡和用户是否存在
	public void isExistUserAndCard() {
		HttpServletRequest request = getHttpServletRequest();
		String card_num = request.getParameter("card_num");
		String yhdm = request.getParameter("yhDm");
		JsonResult result = new JsonResult();
		boolean flag = employeeService.isExistUserAndCard(card_num, yhdm);
		if (flag) {
			result.setSuccess(flag);
		} else if (flag == false) {
			result.setSuccess(flag);
			result.setMsg("选择的卡或员工不存在，请刷新页面");
		}

		printHttpServletResponse(GsonUtil.toJson(result));
	}

	// 给卡绑定表新增记录
	public void addCardBound() {
		HttpServletRequest request = getHttpServletRequest();
		String card_num = request.getParameter("card_num");
		String yhdm = request.getParameter("yhDm");
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		/*
		 * String create_user = loginUser.getYhMc(); Map<String, String> map =
		 * new HashMap<String, String>(); map.put("card_num", card_num);
		 * map.put("yhdm", yhdm); map.put("create_user", create_user);
		 */
		JsonResult result = new JsonResult();

		final String admin_id = loginUser.getYhMc();
		if (EmptyUtil.atLeastOneIsEmpty(card_num, yhdm)) {// 判断参数是否为空
			result.setMsg("绑定失败");
			result.setSuccess(false);
			printHttpServletResponse(GsonUtil.toJson(result));
			return;
		}
		String card_nums[] = new String[1];
		card_nums[0] = card_num;
		result = cardService.people_bound_card(admin_id, card_nums, yhdm);

		/*
		 * boolean flag = employeeService.insertCardBound(map); if (flag) {
		 * result.setSuccess(true); takeAuthFromG(yhdm, card_num, create_user);
		 * // result.setMsg("将自动获取组织门禁权限"); } else { result.setSuccess(false); }
		 */

		printHttpServletResponse(GsonUtil.toJson(result));
	}

	private String toHexStr(String s) {
		Integer a = Integer.parseInt(s);
		String c = a.toHexString(a);
		return c;
	}

	/**
	 * 继承组织权限 by gengji.yang
	 */
	private void takeAuthFromG(String staffNum, String cardNum,
			String createUser) {
		List<Map> oragnAuths = peopleAuthorityInfoService
				.getOrganAuth(staffNum);
		for (int i = 0; i < oragnAuths.size(); i++) {
			oragnAuths.get(i).put("cardNum", cardNum);
			oragnAuths.get(i).put("deviceMac", oragnAuths.get(i).get("mac"));
		}
		if (oragnAuths.size() > 0) {
			peopleAuthorityInfoService.addAuthTasks(oragnAuths, 0);
		}
	}

	/**
	 * 门禁系统 更换员工组织权限 by gengji.yang
	 */
	private void chgGroupAuth(String deptNum, String oldDeptNum,
			String cardNum, String createUser) {
		List<Map> organAuths = peopleAuthorityInfoService
				.getOrganAuthByDept(deptNum);
		List<Map> organAuths_old = peopleAuthorityInfoService
				.getOrganAuthByDept(oldDeptNum);
		for (int i = 0; i < organAuths.size(); i++) {
			organAuths.get(i).put("cardNum", cardNum);
			organAuths.get(i).put("deviceMac", organAuths.get(i).get("mac"));
		}
		for (int i = 0; i < organAuths_old.size(); i++) {
			organAuths_old.get(i).put("cardNum", cardNum);
			organAuths_old.get(i).put("deviceMac",
					organAuths_old.get(i).get("mac"));
		}
		if (organAuths.size() > 0) {
			peopleAuthorityInfoService.addAuthTasks(organAuths_old, 1);
			// 新增权限时, 先插入权限记录，task_state=00
			for (Map map : organAuths) {
				map.put("state", "00");
				peopleAuthorityInfoService.delAuthRecord(map);
				peopleAuthorityInfoService.addAuthRecord(map);
			}
			peopleAuthorityInfoService.addAuthTasks(organAuths, 0);
		}
	}

	/**
	 * 梯控系统 若修改员工信息时，修改了部门则要更换员工组织权限 by guofei.lian 2016-11-01
	 */
	private void changeDeptAuth(String deptNum, String oldDeptNum,
			List<Map> cardNums, String createUser) {
		Map deptAuth = departmentGrantService.selectDeptAuthByDeptNo("\'"
				+ deptNum + "\'");// 查询当前部门权限
		DepartmentGrantModel departmentGrantModel = new DepartmentGrantModel();
		// for(Map map:cardNums){
		// oldDeptAuth.put("card_num", map.get("cardNum"));
		// departmentGrantService.removeDeptAuth(oldDeptAuth);//删除组织权限，即插入到任务表并置action_flag=1
		// }
		if (deptAuth != null) {
			for (Map map : cardNums) {
				departmentGrantModel.setCard_num(map.get("cardNum").toString());
				departmentGrantModel.setCard_type((String) deptAuth
						.get("card_type"));
				departmentGrantModel.setBegin_valid_time((String) deptAuth
						.get("start_time"));
				departmentGrantModel.setEnd_valid_time((String) deptAuth
						.get("end_time"));
				departmentGrantModel.setObject_num(deptNum);
				departmentGrantModel.setDevice_num((String) deptAuth
						.get("device_num"));
				departmentGrantModel.setDevice_ip((String) deptAuth
						.get("device_ip"));
				departmentGrantModel.setDevice_port((Integer) deptAuth
						.get("device_port"));
				departmentGrantModel.setAddress((String) deptAuth
						.get("address"));
				departmentGrantModel.setFloor_group_num((String) deptAuth
						.get("floor_group_num"));
				departmentGrantModel.setFloor_list((String) deptAuth
						.get("floor_list"));
				departmentGrantModel.setObject_type("0");
				departmentGrantService.addDeptAuth(departmentGrantModel);// 新增组织权限到任务表
			}
		}
	}

	// 给卡绑定账户
	public void addCardAccountBound() {
		HttpServletRequest request = getHttpServletRequest();
		String card_num = request.getParameter("card_num");
		String account_num = request.getParameter("account_num");
		String[] list1 = GsonUtil.toBean(account_num, String[].class);
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		String totaltime = getSysTimestamp().toString();
		String create_time = totaltime.substring(0, 19);
		JsonResult result = new JsonResult();
		boolean flag1 = employeeService.addAccountToCard(card_num, create_time,
				list1, create_user);
		if (flag1) {
			result.setSuccess(true);
		} else {
			result.setSuccess(false);
		}

		printHttpServletResponse(GsonUtil.toJson(result));
	}

	// 部门员工模块的查询员工绑定的卡
	public void showBoundcardForStaff() {
		HttpServletRequest request = getHttpServletRequest();
		String yhdm = request.getParameter("yhdm");
		Grid<BoundCard> boundCard = employeeService
				.selectBoundCardByStaffNo(yhdm);
		printHttpServletResponse(GsonUtil.toJson(boundCard));
	}

	// 部门员工模块的查询员工绑定的卡
	public void showBoundcardForStaff2() {
		HttpServletRequest request = getHttpServletRequest();
		String yhdm = request.getParameter("yhdm");
		Grid<BoundCard> boundCard = employeeService
				.selectBoundCardByStaffNo2(yhdm);
		printHttpServletResponse(GsonUtil.toJson(boundCard));
	}

	public void showBoundcard() {
		HttpServletRequest request = getHttpServletRequest();
		String yhdm = request.getParameter("yhdm");
		Grid<BoundCard> boundCard = employeeService.selectBoundCardByYHDM(yhdm);
		printHttpServletResponse(GsonUtil.toJson(boundCard));
	}

	/**
	 * 复制卡权限中的员工展示，需排除源卡 by gengji.yang
	 */
	public void showBoundcardForCopy() {
		HttpServletRequest request = getHttpServletRequest();
		String yhdm = request.getParameter("yhdm");
		String copyCardNumsArr = request.getParameter("copyCardNumsArr");
		Map map = new HashMap();
		map.put("yhdm", yhdm);
		map.put("copyCardNumsArr", copyCardNumsArr);
		Grid<BoundCard> boundCard = employeeService
				.selectBoundCardByForCopy(map);
		printHttpServletResponse(GsonUtil.toJson(boundCard));
	}

	/**
	 * 更新设置审批领导
	 * 
	 * @author minting.he
	 */
	public void updateLeader() {
		HttpServletRequest request = getHttpServletRequest();
		String yhDmsStr = request.getParameter("yhDms");
		String sjldyhDm = request.getParameter("sjldyhDm");
		JsonResult result = new JsonResult();
		if (EmptyUtil.atLeastOneIsEmpty(yhDmsStr, sjldyhDm)) {
			result.setSuccess(false);
			result.setMsg("设置失败,数据不合法");
		} else {
			boolean r = false;
			String[] yhDms = yhDmsStr.split(",");
			for (String yhDm : yhDms) {
				Employee employee = new Employee();
				employee.setYhDm(yhDm);
				employee.setSjldyhDm(sjldyhDm);
				r = employeeService.updateLeader(employee);
			}
			if (r) {
				result.setSuccess(true);
				result.setMsg("设置成功");
			} else {
				result.setSuccess(false);
				result.setMsg("设置失败");
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * @创建人　: 高育漫
	 * @创建时间: 2016-8-5 上午10:48:59
	 * @功能描述: 设置岗位
	 * @参数描述:
	 */
	public void updateStation() {

		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String yhDmsStr = request.getParameter("yhDms");
		String gwDm = request.getParameter("gwDm");

		if (EmptyUtil.atLeastOneIsEmpty(yhDmsStr, gwDm)) {
			result.setSuccess(false);
			result.setMsg("设置失败,数据不合法");
		} else {
			boolean r = false;
			String[] yhDms = yhDmsStr.split(",");
			for (String yhDm : yhDms) {
				Employee employee = new Employee();
				employee.setYhDm(yhDm);
				employee.setGwDm(gwDm);
				r = employeeService.updateStation(employee);
			}
			if (r) {
				result.setSuccess(true);
				result.setMsg("设置成功");
			} else {
				result.setSuccess(false);
				result.setMsg("设置失败");
			}
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-05
	 * @功能描述: 设置消费卡
	 * @参数描述:
	 */
	public void setConsumeCard() {
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String staff_num = request.getParameter("staff_num");
		String card_num = request.getParameter("card_num");
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		String totaltime = getSysTimestamp().toString();
		String create_time = totaltime.substring(0, 19);
		String isDraft = "";
		String isConsumeCard = "";
		if (staff_num != null && !"".equals(staff_num)) {
			isDraft = employeeService.isOverDraft(staff_num);
		}
		if (card_num != null && !"".equals(card_num)) {
			isConsumeCard = employeeService.isConsumeCard(card_num);
		}
		try {
			if ("1".equals(isConsumeCard)) {
				if ("1".equals(isDraft)) {
					boolean result1 = employeeService.setConsumeCard(card_num,
							"0", create_user, create_time);
					if (result1) {
						result.setSuccess(true);
						result.setMsg("设置成功");
					} else {
						result.setSuccess(false);
						result.setMsg("设置失败");
					}
				} else if ("0".equals(isDraft)) {
					Integer consumeCardCount = employeeService
							.consumeCardCount(staff_num);
					if (consumeCardCount > 0) {
						result.setSuccess(false);
						result.setMsg("此员工设置了不允许多卡消费，其绑定的卡中只能设置一张卡为消费卡！");
					} else {
						boolean result1 = employeeService.setConsumeCard(
								card_num, "0", create_user, create_time);
						if (result1) {
							result.setSuccess(true);
							result.setMsg("设置成功");
						} else {
							result.setSuccess(false);
							result.setMsg("设置失败");
						}
					}
				} else {
					result.setSuccess(false);
					result.setMsg("设置失败");
				}
			} else {
				result.setSuccess(false);
				result.setMsg("该卡已被设置为消费卡");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("设置失败");
		}

		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-05
	 * @功能描述: 取消消费卡
	 * @参数描述:
	 */
	public void cancelConsumeCard() {
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String card_num = request.getParameter("card_num");
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		String totaltime = getSysTimestamp().toString();
		String create_time = totaltime.substring(0, 19);
		try {
			if (card_num != null && !"".equals(card_num)) {
				boolean result1 = employeeService.setConsumeCard(card_num, "1",
						create_user, create_time);
				if (result1) {
					result.setSuccess(true);
					result.setMsg("取消成功");
				} else {
					result.setSuccess(false);
					result.setMsg("取消失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMsg("取消失败");
		}

		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-07
	 * @功能描述: 设置员工透支权限并修改卡消费权限
	 * @参数描述:
	 */
	public void setOverDraftRights() {
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String over_draft = request.getParameter("over_draft");
		String staff_num = request.getParameter("staff_num");
		String unCheckCard_num = request.getParameter("uncheckNum");
		String card_num = request.getParameter("checkNum");
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		String totaltime = getSysTimestamp().toString();
		String create_time = totaltime.substring(0, 19);
		if (over_draft != null && !"".equals(over_draft) && staff_num != null
				&& !"".equals(staff_num)) {
			try {

				boolean result1 = employeeService.setOverDraftRights2(
						over_draft, staff_num, unCheckCard_num, card_num,
						create_user, create_time);
				if (result1) {
					result.setSuccess(true);
					result.setMsg("设置成功");
				} else {
					result.setSuccess(false);
					result.setMsg("设置失败");
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.setSuccess(false);
				result.setMsg("设置失败");
			}
		}

		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-07
	 * @功能描述: 设置员工透支权限
	 * @参数描述:
	 */
	public void setOverDraftRights2() {
		JsonResult result = new JsonResult();
		HttpServletRequest request = getHttpServletRequest();
		String over_draft = request.getParameter("over_draft");
		String staff_num = request.getParameter("staff_num");
		User loginUser = (User) getHttpServletRequest().getSession()
				.getAttribute(GlobalConstant.LOGIN_USER);
		String create_user = loginUser.getYhMc();
		String totaltime = getSysTimestamp().toString();
		String create_time = totaltime.substring(0, 19);
		if (over_draft != null && !"".equals(over_draft) && staff_num != null
				&& !"".equals(staff_num)) {
			try {
				boolean result1 = employeeService.setOverDraftRights1(
						over_draft, staff_num, create_user, create_time);
				if (result1) {
					result.setSuccess(true);
					result.setMsg("设置成功");
				} else {
					result.setSuccess(false);
					result.setMsg("设置失败");
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.setSuccess(false);
				result.setMsg("设置失败");
			}

		}

		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-07
	 * @功能描述: 通过员工编号获取支持消费卡的卡号
	 * @参数描述:
	 */
	public void consumeCardNum() {
		HttpServletRequest request = getHttpServletRequest();
		String staff_num = request.getParameter("staff_num");

		List<String> cardList = null;
		if (staff_num != null && !"".equals(staff_num)) {
			cardList = employeeService.consumeCardNum(staff_num);
		}

		printHttpServletResponse(GsonUtil.toJson(cardList));
	}

	/**
	 * @创建人　: 潘卉贤
	 * @创建时间: 2016-09-07
	 * @功能描述: 通过员工编号获取员工透支权限
	 * @参数描述:
	 */
	public void getOverDraft() {
		HttpServletRequest request = getHttpServletRequest();
		String staff_num = request.getParameter("staff_num");
		String isDraft = "";
		if (staff_num != null && !"".equals(staff_num)) {
			isDraft = employeeService.isOverDraft(staff_num);
		}

		printHttpServletResponse(GsonUtil.toJson(isDraft));
	}

}
