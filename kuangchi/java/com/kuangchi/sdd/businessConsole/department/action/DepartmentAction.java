package com.kuangchi.sdd.businessConsole.department.action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.baseConsole.cardtype.model.CardType;
import com.kuangchi.sdd.baseConsole.log.dao.LogDao;
import com.kuangchi.sdd.businessConsole.department.model.Department;
import com.kuangchi.sdd.businessConsole.department.model.DepartmentPage;
import com.kuangchi.sdd.businessConsole.department.service.IDepartmentService;
import com.kuangchi.sdd.businessConsole.department.util.ExcelExportTemplate;
import com.kuangchi.sdd.businessConsole.employee.model.Employee;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.businessConsole.station.model.Station;
import com.kuangchi.sdd.businessConsole.station.service.IStationService;
import com.kuangchi.sdd.businessConsole.user.model.User;
import com.kuangchi.sdd.doorAccessConsole.authority.service.PeopleAuthorityInfoService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.ServletUtil;
import com.kuangchi.sdd.util.excel.TitleRowCell;
import com.kuangchi.sdd.util.file.DownloadFile;

@Controller("departmentAction")
public class DepartmentAction extends BaseActionSupport {
	private static final long serialVersionUID = 2983039179969826169L;

	private static final Logger LOG = Logger.getLogger(DepartmentAction.class);

	private Department model;

	@Resource(name = "departmentServiceImpl")
	private IDepartmentService departmentService;

	@Resource(name = "employeeService")
	EmployeeService employeeService;

	@Resource(name = "stationServiceImpl")
	private IStationService stationService;

	@Resource(name = "roleServiceImpl")
	private IRoleService roleService;
    
	@Resource(name = "LogDaoImpl")
	private LogDao logDao;

	
	@Autowired
	PeopleAuthorityInfoService peopleAuthorityInfoService;

	public DepartmentAction() {
		model = new Department();
	}

	@Override
	public Object getModel() {
		return model;
	}

	/**
	 * 部门管理
	 */
	public void managerDepartment() {
		String pid = getHttpServletRequest().getParameter("pid");
		if (null == pid) {
			pid = GlobalConstant.DEPARTMENT_ROOT;
		}

		List<Department> departs = departmentService.getSystemDepartment(pid);
		printHttpServletResponse(new Gson().toJson(departs));

	}

	/**
	 * 新增部门
	 */
	public void addDepartment() {
		Department page = new Department();
		BeanUtils.copyProperties(model, page);
		restModel();
		// 新增时间 录入人员
		User loginUser = (User) ServletUtil.getSessionAttr(
				getHttpServletRequest(), GlobalConstant.LOGIN_USER);

		page.setLrSj(getSysTimestamp());
		page.setLrryDm(loginUser.getYhDm());
		// 添加部门，自动添加该部门的默认岗位
		Station station = new Station();
		UUID uuid = UUID.randomUUID();
		page.setBmDm(uuid.toString());
		station.setBmDm(uuid.toString());
		station.setGwDm(page.getBmDm() + "YG");
		station.setGwMc("员工");
		station.setLrSj(getSysTimestamp());
		station.setDefaultGw("0"); // 默认岗位 0是，1否

		JsonResult result = new JsonResult();
		result.setSuccess(true);
        
		try {
			departmentService.addDepartment(page, loginUser.getYhMc());
			stationService.addStation(station, loginUser.getYhMc());

			// 查询父部门关联角色，为该角色增加本部门权限
			String[] roleDms = roleService.getRoleGrantByDept(page.getSjbmDm());
			for (String roleDm : roleDms) {
				roleService.addDeptGrant(roleDm, page.getBmDm().split(","));
			}
			// roleService.addDeptGrant("0", page.getBmDm().split(",")); //
			// 为默认角色添加新部门权限 by yuman.gao
		} catch (Exception e) {
			result.setSuccess(false);
			LOG.error("add new department error", e);
		}
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * 修改部门
	 */
	public void modifyDepartment() {
		Department page = new Department();
		BeanUtils.copyProperties(model, page);
		restModel();
		User loginUser = (User) ServletUtil.getSessionAttr(
				getHttpServletRequest(), GlobalConstant.LOGIN_USER);
		JsonResult result = new JsonResult();
		String login_user = loginUser.getYhMc();
		result.setSuccess(true);

		try {
			departmentService.modifyDepartment(page, login_user);
		} catch (Exception e) {
			result.setSuccess(false);
			LOG.error("add new department error", e);
		}

		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * 部门员工tree
	 */
	public void getDepartmentTree() {
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("id");
		Tree treeDeparts = null;

		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
		String layerDeptNum = null;
		if (isLayer) {
			Role role = (Role) getHttpSession().getAttribute(
					GlobalConstant.LOGIN_USER_CURRENT_ROLE);
			User user = (User) getHttpSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			layerDeptNum = departmentService.deptGetLayerDeptNum(user.getYhDm(),
					role.getJsDm());
		}

		if (null == id) {
			// 如果传过来的id为空的,则从根节点开始取
			treeDeparts = departmentService.getDepartmentTree(
					GlobalConstant.DEPARTMENT_ROOT_BM_DM, layerDeptNum);
		} else {
			// 否则从id为传下来的id 的结点开始取
			treeDeparts = departmentService.getDepartmentTree(id, layerDeptNum);
		}
		StringBuilder builder = new StringBuilder();

		builder.append("[");
		builder.append(new Gson().toJson(treeDeparts));
		builder.append("]");
		printHttpServletResponse(builder.toString());
	}

	/**
	 * 部门员工tree
	 */
	public void getLazyDepartmentTree() {
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("id");

		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
		boolean isLayer = roleService.isLayer();
		String layerDeptNum = null;
		if (isLayer) {
			Role role = (Role) getHttpSession().getAttribute(
					GlobalConstant.LOGIN_USER_CURRENT_ROLE);
			User user = (User) getHttpSession().getAttribute(
					GlobalConstant.LOGIN_USER);
			layerDeptNum = departmentService.deptGetLayerDeptNum(user.getYhDm(),
					role.getJsDm());
		}

		if (null == id) {
			// 如果传过来的id为空的,则从根节点开始取
			Tree treeDeparts = departmentService.getLazyDepartmentTree(
					GlobalConstant.DEPARTMENT_ROOT_BM_DM, layerDeptNum);
			StringBuilder builder = new StringBuilder();

			builder.append("[");
			builder.append(new Gson().toJson(treeDeparts));
			builder.append("]");
			printHttpServletResponse(builder.toString());
		} else {
			// 否则从id为传下来的id 的结点开始取
			List<Tree> treeDeparts = departmentService.getDepartmentTreeList(
					id, layerDeptNum);
			printHttpServletResponse(new Gson().toJson(treeDeparts));
		}

	}

	public void downWardDepartment() {
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("id");

		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
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

		List<Department> list = departmentService.downWardDepartment(id,
				layerDeptNum);
		StringBuffer stringBuffer = new StringBuffer("'").append(id)
				.append("'");

		for (int i = 0; i < list.size(); i++) {
			stringBuffer.append(",").append("'").append(list.get(i).getBmDm())
					.append("'");
		}

		printHttpServletResponse(new Gson().toJson(stringBuffer.toString()));
	}

	/**
	 * 部门员工tree
	 */
	public void getOnlyDepartmentTree() {
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("id");
		Tree treeDeparts = null;

		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
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

		if (null == id) {
			// 如果传过来的id为空的,则从根节点开始取
			treeDeparts = departmentService.getOnlyDepartmentTree(
					GlobalConstant.DEPARTMENT_ROOT_BM_DM, layerDeptNum);
		} else {
			treeDeparts = departmentService.getOnlyDepartmentTree(
					id, layerDeptNum);
		}
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		builder.append(new Gson().toJson(treeDeparts));
		builder.append("]");
		printHttpServletResponse(builder.toString());
	}
	
	public void getOnlyDepartmentTreeLazy() {
		HttpServletRequest request = getHttpServletRequest();
		String id = request.getParameter("id");
		Tree treeDeparts = null;
		
		// 判断是否开启分层功能，若无开启，则使用隐藏角色代码查询全部员工
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
		
		if (null == id) {
			// 如果传过来的id为空的,则从根节点开始取
			treeDeparts = departmentService.getOnlyDepartmentTree(
					GlobalConstant.DEPARTMENT_ROOT_BM_DM, layerDeptNum);
			StringBuilder builder = new StringBuilder();
			
			builder.append("[");
			builder.append(new Gson().toJson(treeDeparts));
			builder.append("]");
			printHttpServletResponse(builder.toString());
		} else {
			// 否则从id为传下来的id 的结点开始取
			/*	treeDeparts = departmentService.getOnlyDepartmentTree(id,
					layerDeptNum);*/
			List<Tree> list=departmentService.getOnlyDepartmentTreeList(
					id, layerDeptNum);
			printHttpServletResponse(new Gson().toJson(list));
		}
		
	}

	private void restModel() {
		this.model = new Department();
	}

	/**
	 * 用户所有部门编号
	 */
	public void getUserDepartment() {
		String yhDm = getHttpServletRequest().getParameter("userId");
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(true);
		try {
			jsonResult.setMsg(departmentService.getUserDepartment(yhDm));

		} catch (Exception e) {
			jsonResult.setSuccess(false);
			jsonResult.setMsg(null);
			LOG.error("role", e);
		}
		printHttpServletResponse(new Gson().toJson(jsonResult));
	}

	/**
	 * 部门详情
	 */
	public void departmentDetails() {
		String bmDm = getHttpServletRequest().getParameter("bmDm");// 部门代码

		Department dep = departmentService.getDepartmentDet(bmDm);

		printHttpServletResponse(GsonUtil.toJson(dep));
	}

	/**
	 * 验证部门代码
	 */
	public void validDepartment() {
		HttpServletRequest request = getHttpServletRequest();
		String bmNo = request.getParameter("bmNo");
		String bmDm = request.getParameter("bmDm");
		JsonResult result = departmentService.validDepartment(bmNo, bmDm);
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * 删除部门
	 */
	public void delDepartment() {
		String depIds = getHttpServletRequest().getParameter("depId");
		String empIds = getHttpServletRequest().getParameter("empId");
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(true);
		boolean result = false;
		User loginUser = (User) ServletUtil.getSessionAttr(
				getHttpServletRequest(), GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", login_user);
		try {
			if (depIds.contains("'" + GlobalConstant.DEPARTMENT_ROOT_BM_DM
					+ "'")) {
				jsonResult.setSuccess(result);
				jsonResult.setMsg("顶级节点不能删除");
			} else {
				if (!"".equals(depIds)) {
					log.put("V_OP_NAME", "部门管理");

					result = departmentService.delDepartment(depIds);
					if (!result) {
						jsonResult.setMsg("删除失败，该部门或下属部门存在员工，不能删除");
						log.put("V_OP_MSG", "删除部门信息失败");
						log.put("V_OP_TYPE", "业务");
					}
					log.put("V_OP_MSG", "删除部门信息成功");
					log.put("V_OP_TYPE", "业务");

				} else {
					log.put("V_OP_NAME", "员工管理");
					result = departmentService.delEmployee(empIds);
					if (!result) {
						jsonResult.setMsg("删除失败，该部门的员工有绑定卡片");
						log.put("V_OP_MSG", "删除员工信息失败");
						log.put("V_OP_TYPE", "业务");
					}
					log.put("V_OP_MSG", "删除员工信息成功");
					log.put("V_OP_TYPE", "业务");
				}
				jsonResult.setSuccess(result);
			}

		} catch (Exception e) {
			jsonResult.setSuccess(false);
			LOG.error("department or employee del", e);
			log.put("V_OP_MSG", "删除信息失败");
			log.put("V_OP_TYPE", "异常");
		}
		logDao.addLog(log);
		printHttpServletResponse(new Gson().toJson(jsonResult));
	}

	public void getEmployeeBybmDm() {
		HttpServletRequest request = getHttpServletRequest();
		String beanObject = request.getParameter("data");
		DepartmentPage departmentPage = GsonUtil.toBean(beanObject,
				DepartmentPage.class); // 将数据转化为javabean
		String page = request.getParameter("page");
		String rows = request.getParameter("rows");
		departmentPage.setPage(Integer.parseInt(page));
		departmentPage.setRows(Integer.parseInt(rows));
		Grid<Employee> employeeGrid = null;

		if (!"".equals(departmentPage.getDepId())) {
			employeeGrid = employeeService.getEmployeeBybmDm(departmentPage);
		}
		printHttpServletResponse(GsonUtil.toJson(employeeGrid));

	}

	public void delEmployee() {
		HttpServletRequest request = getHttpServletRequest();
		String yhDms = request.getParameter("yhDms");
		
		User loginUser = (User) ServletUtil.getSessionAttr(
				getHttpServletRequest(), GlobalConstant.LOGIN_USER);
		String login_user = loginUser.getYhMc();
		Map<String, String> log = new HashMap<String, String>();
		log.put("V_OP_NAME", "员工管理");
		log.put("V_OP_FUNCTION", "删除");
		log.put("V_OP_ID", login_user);
		
		boolean r = departmentService.delEmployee(yhDms);
		JsonResult result = new JsonResult();
		if (!r) {
			result.setMsg("删除失败，该员工有绑定卡片");
			result.setSuccess(false);
			log.put("V_OP_MSG", "删除员工信息失败");
			log.put("V_OP_TYPE", "业务");
		} else {
			result.setMsg("删除成功");
			result.setSuccess(true);
			log.put("V_OP_MSG", "删除员工信息成功");
			log.put("V_OP_TYPE", "业务");
		}
		logDao.addLog(log);
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-7-11 下午8:07:13
	 * @功能描述:根据部门代码查看部门信息
	 * @参数描述:
	 */
	public void getDepartmentInfoByBmDm() {
		HttpServletRequest request = getHttpServletRequest();
		String bmDm = request.getParameter("bmDm");
		Department department = departmentService.getDepartmentDet(bmDm);
		printHttpServletResponse(GsonUtil.toJson(department));
	}

	/**
	 * guofei.lian 2016-07-25 查询卡片类型
	 * */
	public void getCardtype() {
		List<Map> list = new ArrayList<Map>();
		List<CardType> cardtypeList = departmentService.getCardtype();
		for (CardType ct : cardtypeList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("VALUE", ct.getType_dm());
			map.put("TEXT", ct.getType_name());
			list.add(map);
		}
		printHttpServletResponse(GsonUtil.toJson(list));
	}

	/**
	 * 下载导入部门模版
	 * 
	 * @author minting.he
	 */
	public void downloadTemplate() {
		HttpServletResponse response = getHttpServletResponse();
		OutputStream out = null;
		ExcelExportTemplate excelExport = new ExcelExportTemplate();
		List<TitleRowCell> titleRowCells = new ArrayList<TitleRowCell>();
		// List<String> list = employeeService.getAllStaffDept();// 获取全部部门代码
		TitleRowCell t1 = new TitleRowCell("上级部门编号", false);
		/*
		 * for (String dept : list) { t1.addSuggest(dept); }
		 */
		TitleRowCell t2 = new TitleRowCell("部门编号", true);
		TitleRowCell t3 = new TitleRowCell("部门名称", true);
		TitleRowCell t4 = new TitleRowCell("简称", true);
		TitleRowCell t5 = new TitleRowCell("备注", false);

		titleRowCells.add(t2);
		titleRowCells.add(t3);
		titleRowCells.add(t4);
		titleRowCells.add(t1);
		titleRowCells.add(t5);
		excelExport.createLongTitleRow(titleRowCells);
		try {
			out = response.getOutputStream();
			response.setContentType("application/x-msexcel");
			String fileName = "部门信息模版.xls";
			response.setHeader("content-Disposition", "attachment;filename="
					+ DownloadFile.toUtf8String(fileName));
			excelExport.getWorkbook().write(out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 设置员工工号或部门编号的编码规则 2016-10-25 guofei.lian
	public void setEncodeRule() {
		HttpServletRequest request = getHttpServletRequest();
		String data = request.getParameter("data");
		Map map = GsonUtil.toBean(data, HashMap.class);
		JsonResult result = new JsonResult();
		boolean flag = departmentService.setEncodeRule(map);
		if (flag) {
			result.setSuccess(true);
			result.setMsg("设置成功");
		} else {
			result.setSuccess(false);
			result.setMsg("设置失败");
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

	// 读取编码规则 2016-10-25 guofei.lian
	public void selectRulesFromSystemParam() {
		Map<String, String> map = departmentService
				.selectRulesFromSystemParam();
		printHttpServletResponse(GsonUtil.toJson(map));
	}

	// 新增部门前，拿规则生成的部门编号 2016-10-25 guofei.lian
	public void initDeptNo() {
		String midDeptNo = peopleAuthorityInfoService.autoProdDeptNo();// 自动生成的部门编号中间部分
		Map<String, String> map = new HashMap<String, String>();
		if (midDeptNo == null || "".equals(midDeptNo)) {
			map.put("msg", "当前编码规则自动生成的部门编号已用尽，请修改编码规则或者手动输入部门编号");
		} else {
			map.put("msg", "");
		}
		String deptNoRule = departmentService.selectDeptNoRule();// 查询当前部门编号编码规则
		String[] deptNoRules = deptNoRule.split("\\|", -1);
		deptNoRules[1] = midDeptNo;// 用生成的员工号中间部分替换规则的中间部分
		String finalDeptNo = deptNoRules[0] + "|" + deptNoRules[1] + "|"
				+ deptNoRules[2];// 重新拼接成最后显示到页面的员工号
		String finalDeptNo1 = deptNoRules[0] + deptNoRules[1] + deptNoRules[2];// 拿此编号去数据库查询是否存在
		boolean flag = departmentService.selectInfoByNum(finalDeptNo1);// flag=true即该编号已存在
		if (flag) {
			// 更新查到的那一行，补充其staff_no_temp的值
			Map m = new HashMap();
			m.put("bmNo", finalDeptNo1);
			m.put("bmNoTemp", finalDeptNo);
			departmentService.updateDeptNoTemp(m);// 编号存在时，插入此编号中间列
			midDeptNo = peopleAuthorityInfoService.autoProdDeptNo();// 再自动生成部门编号中间部分
			if (midDeptNo == null || "".equals(midDeptNo)) {
				map.put("msg", "当前编码规则自动生成的部门编号已用尽，请修改编码规则或者手动输入部门编号");
			} else {
				map.put("msg", "");
			}
			deptNoRules[1] = midDeptNo;
			finalDeptNo = deptNoRules[0] + "|" + deptNoRules[1] + "|"
					+ deptNoRules[2];
		}
		map.put("deptNoRule", finalDeptNo);
		printHttpServletResponse(GsonUtil.toJson(map));
	}

}
