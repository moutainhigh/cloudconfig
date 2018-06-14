package com.kuangchi.sdd.interfaceConsole.cardSender.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.attendanceConsole.dutyuser.service.DutyUserService;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.baseConsole.card.service.ICardService;
import com.kuangchi.sdd.businessConsole.employee.service.EmployeeService;
import com.kuangchi.sdd.consumeConsole.consumeGroupMap.dao.IGroupMapDao;
import com.kuangchi.sdd.consumeConsole.consumeGroupMap.model.GroupMapModel;
import com.kuangchi.sdd.interfaceConsole.cardSender.model.EmployeeSyncModel;
import com.kuangchi.sdd.interfaceConsole.cardSender.model.OperateEmpModel;
import com.kuangchi.sdd.interfaceConsole.cardSender.model.ResultEmp;
import com.kuangchi.sdd.interfaceConsole.cardSender.service.EmpCardSenSyncService;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.StaffGw;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.StaffGwService;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;

@Controller("empCardSenderSyncAction")
public class EmpCardSenderSyncAction extends BaseActionSupport {

	@Resource(name = "empCardSenSyncServiceImpl")
	private EmpCardSenSyncService empCardSenSyncService;

	@Resource(name = "cardServiceImpl")
	private ICardService cardService;

	@Resource(name = "staffGwServiceImpl")
	private StaffGwService staffGwService;

	@Resource(name = "employeeService")
	private EmployeeService employeeService;
	@Resource(name = "groupMapDaoImpl")
	private IGroupMapDao groupMapDao;
	@Resource(name = "dutyUserServiceImpl")
	DutyUserService dutyUserService;

	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * 添加员工(供发卡软件使用)
	 */
	public void addEmployeeSync() {

		HttpServletRequest request = getHttpServletRequest();
		String empOpModelListStr = request.getParameter("oprateModelList");
		ResultEmp myResult = new ResultEmp();
		Gson gson = new Gson();
		List<LinkedTreeMap> oprateEModelTMap = gson.fromJson(empOpModelListStr,
				ArrayList.class);
		List<OperateEmpModel> oprateEmpModelList = getOprateEmpModel(oprateEModelTMap);
		if (null == oprateEmpModelList) {
			myResult.setMsg("传参错误");
			myResult.setCode("4");
			printHttpServletResponse(GsonUtil.toJson(myResult));
			return;
		}
		List<OperateEmpModel> resultList = new ArrayList<OperateEmpModel>();
		Integer isExistSucc = 0;
		Integer isExistFail = 0;
		for (OperateEmpModel opEModel : oprateEmpModelList) {

			String deptID = opEModel.getBsBmDm();// 部门id
			String name = opEModel.getName();
			String sex = opEModel.getSex();
			String workState = opEModel.getWorkState();
			// String birthday = opEModel.getBirthday(); // 1998/6/6
			String workNo = opEModel.getWorkNo(); // 员工工号
			String mobile = opEModel.getMobile();
			String IDNumber = opEModel.getIdNumber(); // 身份证号
			String address = opEModel.getAddress();
			String description = opEModel.getDescription();
			String room_num = opEModel.getRoom_num();
			String staff_num = null;

			JsonResult result = new JsonResult();
			Map<String, Object> resultMap = new HashMap<String, Object>();

			/*
			 * if (null == id || "".equals(id.trim())) { throw new
			 * com.kuangchi.sdd.base.exception.MyException( "员工ID不能为空"); } if
			 * (null == deptID || "".equals(deptID.trim())) { throw new
			 * com.kuangchi.sdd.base.exception.MyException( "部门ID不能为空"); } if
			 * (null == name || "".equals(name.trim())) { throw new
			 * com.kuangchi.sdd.base.exception.MyException( "员工姓名不能为空"); } if
			 * (null == workNo || "".equals(workNo.trim())) { throw new
			 * com.kuangchi.sdd.base.exception.MyException( "员工工号不能为空"); } if
			 * (null == workState || "".equals(workState.trim())) { throw new
			 * com.kuangchi.sdd.base.exception.MyException( "员工状态不能为空"); }
			 */

			if (null == deptID || "".equals(deptID.trim())) {
				/*
				 * throw new com.kuangchi.sdd.base.exception.MyException(
				 * "父ID不能为空");
				 */
				opEModel.setBsId("");
				opEModel.setErrorCode("4");
				resultList.add(opEModel);
				isExistFail = 1;
				continue;
			}
			if (null == name || "".equals(name.trim())) {
				/*
				 * throw new com.kuangchi.sdd.base.exception.MyException(
				 * "父ID不能为空");
				 */
				opEModel.setBsId("");
				opEModel.setErrorCode("4");
				resultList.add(opEModel);
				isExistFail = 1;
				continue;
			}
			if (null == workNo || "".equals(workNo.trim())) {
				/*
				 * throw new com.kuangchi.sdd.base.exception.MyException(
				 * "父ID不能为空");
				 */
				opEModel.setBsId("");
				opEModel.setErrorCode("4");
				resultList.add(opEModel);
				isExistFail = 1;
				continue;
			}
			if (null == workState || "".equals(workState.trim())) {
				/*
				 * throw new com.kuangchi.sdd.base.exception.MyException(
				 * "父ID不能为空");
				 */
				opEModel.setBsId("");
				opEModel.setErrorCode("4");
				resultList.add(opEModel);
				isExistFail = 1;
				continue;
			}

			/*
			 * } catch (com.kuangchi.sdd.base.exception.MyException myException)
			 * { result.setMsg(myException.getMessage()); result.setCode("1");
			 * resultMap.put("msg", result.getMsg()); resultMap.put("code",
			 * result.getCode());
			 * printHttpServletResponse(GsonUtil.toJson(resultMap)); return; }
			 */

			if (null != workNo && !"".equals(workNo.trim())) {
				Integer StaffCount = empCardSenSyncService
						.getStaffCountByStaff_no(workNo);
				if (StaffCount > 0) {
					opEModel.setBsId("");
					opEModel.setErrorCode("3");
					resultList.add(opEModel);
					isExistFail = 1;
					continue;
				}
			}

			EmployeeSyncModel employeeSync = new EmployeeSyncModel();
			StaffGw staffGw = new StaffGw();

			if (null != deptID && !"".equals(deptID.trim())) {
				Integer deptCount = empCardSenSyncService.getDeptCount(deptID);
				if (deptCount <= 0) {
					opEModel.setBsId("");
					opEModel.setErrorCode("2");
					resultList.add(opEModel);
					isExistFail = 1;
					continue;
				}

				employeeSync = empCardSenSyncService.defaultMC_DM(deptID);// 查到该部门的默认岗位的名称和代码
				if (employeeSync == null) {
					opEModel.setBsId("");
					opEModel.setErrorCode("1");
					resultList.add(opEModel);
					isExistFail = 1;
					continue;
				}
				UUID uuid = UUID.randomUUID();
				staff_num = uuid.toString();
				employeeSync.setStaff_num(staff_num);
				employeeSync.setStaff_no(workNo); // 员工工号
				employeeSync.setStaff_dept(deptID);
				employeeSync.setStaff_name(name);
				employeeSync.setStaff_address(address);
				employeeSync.setStaff_remark(opEModel.getDescription());
				employeeSync.setStaff_phone(opEModel.getPhone());
				employeeSync.setStaff_email(opEModel.getEmail());
				// 先正则表达式验证是身份证或护照，然后再设置证件类型

				if (IDNumber
						.matches("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$")
						|| IDNumber
								.matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$")) {
					employeeSync.setPapers_type("100"); // 身份证编号 100
				} else {

					employeeSync.setPapers_type("200"); // 护照编号 200
				}
				employeeSync.setPapers_num(IDNumber);
				employeeSync.setStaff_mobile(mobile);

				employeeSync.setStaff_remark(description);
				employeeSync.setStaff_position(employeeSync.getGw_mc());

				staffGw.setGw_dm(employeeSync.getGw_dm());
				staffGw.setYh_dm(staff_num);

			}
			// 几个特殊的 1.性别 2.状态3.年龄
			employeeSync.setStaff_sex(sex); // 默认为男
			// 0在职 1离职
			employeeSync.setStaff_hire_state(workState);
			employeeSync.setStaff_hiredate(opEModel.getCreateDate());
			employeeSync.setRoom_num(room_num);

			/*
			 * // 根据用户生日计算年龄 if (null != birthday &&
			 * !"".equals(birthday.trim())) { SimpleDateFormat formatter = new
			 * SimpleDateFormat("yyyy/MM/dd"); try { Date birth =
			 * formatter.parse(birthday); int age = getAgeByBirthday(birth);
			 * employeeSync.setStaff_age(String.valueOf(age)); } catch
			 * (ParseException e1) { e1.printStackTrace(); } }
			 */
			try {
				empCardSenSyncService.addEmployeeSync(employeeSync);
				try {
					String create_time = DateUtil.getSysDateTime();
					staffGwService.delStaffGw(staff_num); // 先删除kc_staff_gw中记录，再添加新纪录
					staffGwService.addStaffGw(staffGw);
					employeeService.addAccountToEmployee(staff_num,
							create_time, "发卡软件端_admin");// 添加员工同时添加默认账户

					GroupMapModel groupMap = new GroupMapModel();
					groupMap.setStaff_num(staff_num);
					groupMap.setGroup_num("1");
					groupMapDao.addGroupMap(groupMap);// 给新增的员工添加默认消费组
					dutyUserService.getDutyFromDept(deptID, staff_num);// 给新增的员工继承部门排班

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				opEModel.setBsId(staff_num);
				resultList.add(opEModel);
				isExistSucc = 1;
				continue;
			} catch (Exception e) {
				opEModel.setBsId("");
				opEModel.setErrorCode("1");
				resultList.add(opEModel);
				isExistFail = 1;
				continue;
			}
		}
		if (isExistSucc == 1 && isExistFail == 0) {
			myResult.setCode("0");
			myResult.setMsg("全部新增成功");
		} else if (isExistSucc == 0 && isExistFail == 1) {
			myResult.setCode("1");
			myResult.setMsg("全部新增失败");
		} else if (isExistSucc == 1 && isExistFail == 1) {
			myResult.setCode("9");
			myResult.setMsg("部分新增失败");
		} else {
			myResult.setCode("1");
			myResult.setMsg("新增失败");
		}

		myResult.setResultList(resultList);
		printHttpServletResponse(GsonUtil.toJson(myResult));
	}

	/**
	 * 根据id修改员工(供C/S端使用)
	 */
	public void modifyEmployeeSync() {
		HttpServletRequest request = getHttpServletRequest();
		String empOpModelListStr = request.getParameter("oprateModelList");
		ResultEmp myResult = new ResultEmp();
		Gson gson = new Gson();
		List<LinkedTreeMap> oprateEModelTMap = gson.fromJson(empOpModelListStr,
				ArrayList.class);
		List<OperateEmpModel> oprateEmpModelList = getOprateEmpModel(oprateEModelTMap);
		if (null == oprateEmpModelList) {
			myResult.setMsg("传参错误");
			myResult.setCode("4");
			printHttpServletResponse(GsonUtil.toJson(myResult));
			return;
		}
		List<OperateEmpModel> resultList = new ArrayList<OperateEmpModel>();
		Integer isExistSucc = 0;
		Integer isExistFail = 0;
		for (OperateEmpModel opEModel : oprateEmpModelList) {
			String bsId = opEModel.getBsId();
			String deptID = opEModel.getBsBmDm();// 部门代码
			String name = opEModel.getName();
			String sex = opEModel.getSex();
			String workState = opEModel.getWorkState();
			// String birthday = opEModel.getBirthday(); // 1998/6/6
			String workNo = opEModel.getWorkNo(); // 员工工号
			String mobile = opEModel.getMobile();
			String IDNumber = opEModel.getIdNumber(); // 身份证号
			String address = opEModel.getAddress();
			String room_num = opEModel.getRoom_num();
			String description = opEModel.getDescription();

			JsonResult result = new JsonResult();
			Map<String, Object> resultMap = new HashMap<String, Object>();

			if (null == bsId || "".equals(bsId.trim())) {
				/*
				 * throw new com.kuangchi.sdd.base.exception.MyException(
				 * "员工ID不能为空");
				 */
				opEModel.setBsId("");
				opEModel.setErrorCode("4");
				resultList.add(opEModel);
				isExistFail = 1;
				continue;
			}
			if (null == deptID || "".equals(deptID.trim())) {
				/*
				 * throw new com.kuangchi.sdd.base.exception.MyException(
				 * "部门ID不能为空");
				 */
				opEModel.setBsId("");
				opEModel.setErrorCode("4");
				resultList.add(opEModel);
				isExistFail = 1;
				continue;
			}
			if (null == name || "".equals(name.trim())) {
				/*
				 * throw new com.kuangchi.sdd.base.exception.MyException(
				 * "员工姓名不能为空");
				 */
				opEModel.setBsId("");
				opEModel.setErrorCode("4");
				resultList.add(opEModel);
				isExistFail = 1;
				continue;
			}
			if (null == workNo || "".equals(workNo.trim())) {
				/*
				 * throw new com.kuangchi.sdd.base.exception.MyException(
				 * "员工工号不能为空");
				 */
				opEModel.setBsId("");
				opEModel.setErrorCode("4");
				resultList.add(opEModel);
				isExistFail = 1;
				continue;
			}
			if (null == workState || "".equals(workState.trim())) {
				/*
				 * throw new com.kuangchi.sdd.base.exception.MyException(
				 * "员工状态不能为空");
				 */
				opEModel.setBsId("");
				opEModel.setErrorCode("4");
				resultList.add(opEModel);
				isExistFail = 1;
				continue;
			}

			String ENum = empCardSenSyncService.getStaffNumByID(bsId);
			if (ENum == null) {
				opEModel.setBsId("");
				opEModel.setErrorCode("5");
				resultList.add(opEModel);
				isExistFail = 1;
				continue;
			}

			EmployeeSyncModel employeeSync = empCardSenSyncService
					.getDeptNumByEmpID(bsId);// 根据员工id，查找员工编号和原本所属部门代码
			StaffGw staffGw = new StaffGw();

			String oldDeptNum = employeeSync.getStaff_dept();// 原本所属部门代码
			// String deptNum = empCardSenSyncService.getDNumByBmDm(deptID);
			if (!deptID.equals(oldDeptNum)) { // 新旧部门代码不相等，表示修改了部门，则员工默认岗位也必须修改
				employeeSync.setStaff_dept(deptID);
				EmployeeSyncModel empModel = empCardSenSyncService
						.defaultMC_DM(deptID);// 查到该部门的默认岗位的名称和代码
				if (empModel == null) {
					opEModel.setBsId("");
					opEModel.setErrorCode("1");
					resultList.add(opEModel);
					isExistFail = 1;
					continue;
				} else {
					staffGw.setGw_dm(empModel.getGw_dm());
					staffGw.setYh_dm(employeeSync.getStaff_num());
				}
			}
			employeeSync.setStaff_num(bsId);
			employeeSync.setStaff_dept(deptID);
			employeeSync.setStaff_no(workNo);
			employeeSync.setStaff_name(name);
			employeeSync.setStaff_address(address);
			employeeSync.setStaff_mobile(mobile);
			employeeSync.setStaff_remark(description);
			employeeSync.setStaff_email(opEModel.getEmail());

			if (IDNumber
					.matches("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$")
					|| IDNumber
							.matches("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$")) {
				employeeSync.setPapers_type("100"); // 身份证编号 100
			} else {

				employeeSync.setPapers_type("200"); // 护照编号 200
			}
			employeeSync.setPapers_num(IDNumber);
			employeeSync.setRoom_num(room_num);
			employeeSync.setStaff_phone(opEModel.getPhone());
			employeeSync.setStaff_sex(sex);
			employeeSync.setStaff_hiredate(opEModel.getCreateDate());
			employeeSync.setStaff_hire_state(opEModel.getWorkState());

			// 几个特殊的 1.性别 2.状态3.年龄
			/*
			 * if ("男".equals(sex) || null == sex) {
			 * employeeSync.setStaff_sex("1"); // 默认为男 } else if
			 * ("女".equals(sex)) { employeeSync.setStaff_sex("0"); }
			 */
			// C/S端 0在职 1离职 ,本地端 员工状态,1 正常 2 冻结
			/*
			 * if ("0".equals(workState) || null == workState) {
			 * employeeSync.setStaff_state("1"); // 默认为正常 } else if
			 * ("1".equals(workState)) { employeeSync.setStaff_state("2"); }
			 */
			/*
			 * if (null != birthday && !"".equals(birthday.trim())) { //
			 * 根据用户生日计算年龄 SimpleDateFormat formatter = new
			 * SimpleDateFormat("yyyy/MM/dd"); try { Date birth =
			 * formatter.parse(birthday); int age = getAgeByBirthday(birth);
			 * employeeSync.setStaff_age(String.valueOf(age)); } catch
			 * (ParseException e1) { e1.printStackTrace(); } }
			 */
			try {
				empCardSenSyncService.modifyEmployeeSync(employeeSync);
				if (!deptID.equals(oldDeptNum)) {
					staffGwService.delStaffGw(staffGw.getYh_dm()); // 先删除kc_staff_gw中记录，再添加新纪录
					staffGwService.addStaffGw(staffGw);
				}
				opEModel.setBsId(bsId);
				resultList.add(opEModel);
				isExistSucc = 1;
			} catch (Exception e) {
				opEModel.setBsId("");
				opEModel.setErrorCode("1");
				resultList.add(opEModel);
				isExistFail = 1;
				continue;
			}
		}
		if (isExistSucc == 1 && isExistFail == 0) {
			myResult.setCode("0");
			myResult.setMsg("全部修改成功");
		} else if (isExistSucc == 0 && isExistFail == 1) {
			myResult.setCode("1");
			myResult.setMsg("全部修改失败");
		} else if (isExistSucc == 1 && isExistFail == 1) {
			myResult.setCode("9");
			myResult.setMsg("部分修改失败");
		} else {
			myResult.setCode("1");
			myResult.setMsg("修改失败");
		}

		myResult.setResultList(resultList);
		printHttpServletResponse(GsonUtil.toJson(myResult));
	}

	/**
	 * 根据员工id删除员工信息(供C/S端使用)
	 */
	public void delEmployeeSync() {
		HttpServletRequest request = getHttpServletRequest();
		String empOpModelListStr = request.getParameter("oprateModelList");
		ResultEmp myResult = new ResultEmp();
		Gson gson = new Gson();
		List<LinkedTreeMap> oprateEModelTMap = gson.fromJson(empOpModelListStr,
				ArrayList.class);
		List<OperateEmpModel> oprateEmpModelList = getOprateEmpModel(oprateEModelTMap);
		if (null == oprateEmpModelList) {
			myResult.setMsg("传参错误");
			myResult.setCode("4");
			printHttpServletResponse(GsonUtil.toJson(myResult));
			return;
		}
		List<OperateEmpModel> resultList = new ArrayList<OperateEmpModel>();
		Integer isExistSucc = 0;
		Integer isExistFail = 0;
		for (OperateEmpModel opEModel : oprateEmpModelList) {

			try {
				// 有卡人员不能删除
				if (empCardSenSyncService.isStaffBoundCard(opEModel.getBsId())) {
					opEModel.setBsId("");
					opEModel.setErrorCode("1");
					resultList.add(opEModel);
					isExistFail = 1;
					continue;
				}

				String staffNum = empCardSenSyncService
						.getStaffNumByID(opEModel.getBsId());
				if (staffNum != null) {
					staffGwService.delStaffGw(staffNum); // 根据员工编号删除员工岗位
				} else {
					opEModel.setBsId("");
					opEModel.setErrorCode("5");
					resultList.add(opEModel);
					isExistFail = 1;
					continue;
				}
				empCardSenSyncService.delEmployeeSync(opEModel.getBsId());// 伪删除员工
				opEModel.setBsId(opEModel.getBsId());
				resultList.add(opEModel);
				isExistSucc = 1;
			} catch (Exception e) {
				opEModel.setBsId("");
				opEModel.setErrorCode("1");
				resultList.add(opEModel);
				isExistFail = 1;
				continue;
			}
		}
		if (isExistSucc == 1 && isExistFail == 0) {
			myResult.setCode("0");
			myResult.setMsg("全部删除成功");
		} else if (isExistSucc == 0 && isExistFail == 1) {
			myResult.setCode("1");
			myResult.setMsg("全部删除失败");
		} else if (isExistSucc == 1 && isExistFail == 1) {
			myResult.setCode("9");
			myResult.setMsg("部分删除失败");
		} else {
			myResult.setCode("1");
			myResult.setMsg("删除失败");
		}

		myResult.setResultList(resultList);
		printHttpServletResponse(GsonUtil.toJson(myResult));
	}

	/**
	 * 根据用户生日计算年龄
	 */
	public static int getAgeByBirthday(Date birthday) {
		Calendar cal = Calendar.getInstance(); // 01 18:53:09 EEST 2012

		if (cal.before(birthday)) {
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthday);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				// monthNow>monthBirth
				age--;
			}
		}
		return age;
	}

	private List<OperateEmpModel> getOprateEmpModel(
			List<LinkedTreeMap> oprateEModelTMap) {
		if (oprateEModelTMap != null) {
			try {
				List<OperateEmpModel> oprateEmpModelList2 = new ArrayList();
				for (int i = 0; i < oprateEModelTMap.size(); i++) {
					LinkedTreeMap map = oprateEModelTMap.get(i);
					OperateEmpModel oprateEmpModel2 = new OperateEmpModel();
					oprateEmpModel2.setBsId(map.get("bsId") == null ? "" : map
							.get("bsId").toString().trim());
					oprateEmpModel2.setId(map.get("id") == null ? "" : map
							.get("id").toString().trim());

					oprateEmpModel2.setBsBmDm(map.get("bsBmDm") == null ? ""
							: map.get("bsBmDm").toString().trim());
					oprateEmpModel2.setName(map.get("name") == null ? "" : map
							.get("name").toString().trim());
					oprateEmpModel2.setSex(map.get("sex") == null ? "" : map
							.get("sex").toString().trim());
					oprateEmpModel2
							.setWorkState(map.get("workState") == null ? ""
									: map.get("workState").toString().trim());
					oprateEmpModel2.setWorkNo(map.get("workNo") == null ? ""
							: map.get("workNo").toString().trim());
					oprateEmpModel2.setMobile(map.get("mobile") == null ? ""
							: map.get("mobile").toString().trim());
					oprateEmpModel2.setPhone(map.get("phone") == null ? ""
							: map.get("phone").toString().trim());
					oprateEmpModel2
							.setIdNumber(map.get("idNumber") == null ? "" : map
									.get("idNumber").toString().trim());
					oprateEmpModel2.setAddress(map.get("address") == null ? ""
							: map.get("address").toString().trim());
					oprateEmpModel2
							.setDescription(map.get("description") == null ? ""
									: map.get("description").toString().trim());
					oprateEmpModel2
							.setCreateDate(map.get("createDate") == null ? ""
									: map.get("createDate").toString().trim());
					oprateEmpModel2
							.setErrorCode(map.get("errorCode") == null ? ""
									: map.get("errorCode").toString().trim());
					oprateEmpModel2.setEmail(map.get("email") == null ? ""
							: map.get("email").toString().trim());
					oprateEmpModel2
							.setRoom_num(map.get("room_num") == null ? "" : map
									.get("room_num").toString().trim());

					oprateEmpModelList2.add(oprateEmpModel2);
				}
				return oprateEmpModelList2;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {

			return null;
		}

	}

	public void getAllEmp() {
		ResultEmp result = new ResultEmp();
		List<EmployeeSyncModel> empList = null;
		try {
			empList = empCardSenSyncService.getAllEmp();
			List<OperateEmpModel> opEmpList = new ArrayList<OperateEmpModel>();
			for (EmployeeSyncModel empModel : empList) {
				OperateEmpModel opEModel = new OperateEmpModel();
				opEModel.setBsId(empModel.getStaff_num());
				opEModel.setAddress(empModel.getStaff_address());
				// opEModel.setBirthday(empModel.getStaff_age());此字段没法同步
				opEModel.setBsBmDm(empModel.getStaff_dept());
				opEModel.setCreateDate(empModel.getStaff_hiredate());
				opEModel.setDescription(empModel.getStaff_remark());

				opEModel.setIdNumber(empModel.getPapers_num());
				opEModel.setMobile(empModel.getStaff_mobile());
				opEModel.setName(empModel.getStaff_name());
				opEModel.setPhone(empModel.getStaff_phone());
				opEModel.setSex(empModel.getStaff_sex());
				opEModel.setWorkNo(empModel.getStaff_no());
				opEModel.setWorkState(empModel.getStaff_hire_state());
				opEModel.setEmail(empModel.getStaff_email());
				opEModel.setRoom_num(empModel.getRoom_num());

				opEmpList.add(opEModel);
			}
			result.setCode("0");
			result.setMsg("查询成功");
			result.setResultList(opEmpList);
		} catch (Exception e) {
			result.setCode("1");
			result.setMsg("查询失败");
			e.printStackTrace();
		}
		printHttpServletResponse(GsonUtil.toJson(result));
	}

}
