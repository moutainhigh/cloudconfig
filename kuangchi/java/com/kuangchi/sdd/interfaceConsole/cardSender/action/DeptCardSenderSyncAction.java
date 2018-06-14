package com.kuangchi.sdd.interfaceConsole.cardSender.action;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.kuangchi.sdd.base.action.BaseActionSupport;
import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.interfaceConsole.cardSender.model.DepartmentSyncModel;
import com.kuangchi.sdd.interfaceConsole.cardSender.model.OprateDpetModel;
import com.kuangchi.sdd.interfaceConsole.cardSender.model.Result;
import com.kuangchi.sdd.interfaceConsole.cardSender.service.DeptCardSenderSyncService;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.model.DeptGw;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.DeptGwService;
import com.kuangchi.sdd.interfaceConsole.dataSynchronize.service.EmployeeSyncService;
import com.kuangchi.sdd.util.commonUtil.GsonUtil;
import com.kuangchi.sdd.util.commonUtil.UUIDUtil;

/**
 * 同步部门服务（供发卡软件用）
 * 
 * @author xuewen.deng
 * 
 */
@Controller("deptCardSenderSyncAction")
public class DeptCardSenderSyncAction extends BaseActionSupport {

	@Resource(name = "deptCardSenderSyncServiceImpl")
	private DeptCardSenderSyncService deptCardSenderSyncService;
	@Resource(name = "deptGwServiceImpl")
	private DeptGwService deptGwService;
	@Resource(name = "employeeSyncServiceImpl")
	private EmployeeSyncService employeeSyncService;
	@Resource(name = "roleServiceImpl")
	private IRoleService roleService;

	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * 添加部门(供发卡软件使用)
	 */
	public void addDepartmentSync() {

		// JsonResult result = new JsonResult();
		Result myResult = new Result();

		HttpServletRequest request = getHttpServletRequest();
		String oprateDModelListStr = request.getParameter("oprateModelList");
		// OprateDpetModel oprateDpetModel = GsonUtil.toBean(oprateDpetModelStr,
		// OprateDpetModel.class);

		Gson gson = new Gson();
		List<LinkedTreeMap> oprateDModelTMap = gson.fromJson(
				oprateDModelListStr, ArrayList.class);
		List<OprateDpetModel> oprateDpetModelList = getOprateDpetModel(oprateDModelTMap);
		if (null == oprateDpetModelList) {
			myResult.setMsg("传参错误");
			myResult.setCode("4");
			printHttpServletResponse(GsonUtil.toJson(myResult));
			return;
		}
		List<OprateDpetModel> resultList = new ArrayList<OprateDpetModel>();
		Integer isExistSucc = 0;
		Integer isExistFail = 0;
		// String id = oprateDpetModel.getBsID();//新增不需要uuid

		for (OprateDpetModel oprateDpetModel : oprateDpetModelList) {
			String parentDM = oprateDpetModel.getParentDM();
			String code = oprateDpetModel.getCode(); // 部门编号
			String name = oprateDpetModel.getName();
			String description = oprateDpetModel.getDescription();
			String bm_dm = null;
			/*
			 * if (null == id || "".equals(id.trim())) { throw new
			 * com.kuangchi.sdd.base.exception.MyException("id不能为空"); }
			 */
			if (null == parentDM || "".equals(parentDM.trim())) {
				/*
				 * throw new com.kuangchi.sdd.base.exception.MyException(
				 * "父ID不能为空");
				 */
				oprateDpetModel.setBsID("");// 注意：将bsID设为空，发卡软件端是根据这个bsID是否为空来判断是否成功的
				oprateDpetModel.setErrorCode("2");
				resultList.add(oprateDpetModel);
				isExistFail = 1;
				continue;
			}
			if (null == code || "".equals(code.trim())) {
				/*
				 * throw new com.kuangchi.sdd.base.exception.MyException(
				 * "部门编号不能为空");
				 */
				oprateDpetModel.setBsID("");
				oprateDpetModel.setErrorCode("4");
				resultList.add(oprateDpetModel);
				isExistFail = 1;
				continue;
			}
			if (null == name || "".equals(name.trim())) {
				/*
				 * throw new com.kuangchi.sdd.base.exception.MyException(
				 * "部门名称不能为空");
				 */
				oprateDpetModel.setBsID("");
				oprateDpetModel.setErrorCode("4");
				resultList.add(oprateDpetModel);
				isExistFail = 1;
				continue;
			}
			if (null != description && description.trim().length() > 399) {
				/*
				 * throw new com.kuangchi.sdd.base.exception.MyException(
				 * "备注长度过长");
				 */
				oprateDpetModel.setBsID("");
				oprateDpetModel.setErrorCode("4");
				resultList.add(oprateDpetModel);
				isExistFail = 1;
				continue;
			}

			DepartmentSyncModel departmentSync = new DepartmentSyncModel();
			departmentSync.setBm_mc(name);
			// departmentSync.setRemote_department_id(id);
			departmentSync.setBz(description);

			UUID uuid = UUID.randomUUID();
			bm_dm = uuid.toString();
			departmentSync.setBm_dm(bm_dm);

			/*
			 * if (null != id && !"".equals(id.trim())) { String deptNum =
			 * departmentSyncService.getDeptNum(id); if (null != deptNum) {
			 * result.setMsg("已存在该ID的部门"); result.setCode("2");
			 * resultMap.put("msg", result.getMsg()); resultMap.put("code",
			 * result.getCode());
			 * printHttpServletResponse(GsonUtil.toJson(resultMap)); return; } }
			 */
			// 判断是否存在部门编号
			if (null != code && !"".equals(code.trim())) {
				Integer deptNum = deptCardSenderSyncService.isExistDeptNo(code);
				if (deptNum > 0) {
					oprateDpetModel.setBsID("");
					oprateDpetModel.setErrorCode("3");
					resultList.add(oprateDpetModel);
					isExistFail = 1;
					continue;
				}
			}

			departmentSync.setBm_no(code);

			if (null != parentDM && !"".equals(parentDM.trim())) {
				if ("0".equals(parentDM)) {
					departmentSync.setSjbm_dm("1");
				} else {

					Integer parentDMCount = deptCardSenderSyncService
							.isExistParentDM(parentDM);
					if (parentDMCount <= 0) {
						oprateDpetModel.setBsID("");
						oprateDpetModel.setErrorCode("2");
						resultList.add(oprateDpetModel);
						isExistFail = 1;
						continue;
					} else {
						departmentSync.setSjbm_dm(parentDM);
						// departmentSync.setSjbm_dm(parentDM);
					}
				}
			}

			departmentSync.setLr_sj(getSysTimestamp()); // 自己创建
			departmentSync.setUuid(UUIDUtil.uuidStr());
			departmentSync.setZf_bj(GlobalConstant.ZF_BJ_N);
			// 添加部门时，同时添加部门默认岗位
			DeptGw deptGw = new DeptGw();
			deptGw.setBm_dm(bm_dm);
			deptGw.setGw_mc("员工");
			deptGw.setGw_dm(bm_dm + "YG");
			try {
				int resultCode = deptCardSenderSyncService
						.addDepartmentSync(departmentSync);

				if (resultCode == 1) {
					deptGwService.addDeptGw(deptGw);
					String[] deptStrArr = new String[1];
					deptStrArr[0] = bm_dm;
					roleService.addDeptGrant("0", deptStrArr); // roldeId为“0”
					oprateDpetModel.setBsID(bm_dm);
					resultList.add(oprateDpetModel);
					isExistSucc = 1;
				} else {
					oprateDpetModel.setBsID("");
					oprateDpetModel.setErrorCode("1");
					resultList.add(oprateDpetModel);
					isExistFail = 1;
					continue;

				}

			} catch (Exception e) {
				oprateDpetModel.setBsID("");
				oprateDpetModel.setErrorCode("1");
				resultList.add(oprateDpetModel);
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
			myResult.setMsg("全部新增失败");
		}

		myResult.setResultList(resultList);
		printHttpServletResponse(GsonUtil.toJson(myResult));

	}

	/**
	 * 根据部门id修改部门信息(供发卡软件使用)
	 */
	public void modifyDepartmentSync() {
		Result myResult = new Result();

		HttpServletRequest request = getHttpServletRequest();
		String oprateDModelListStr = request.getParameter("oprateModelList");
		// OprateDpetModel oprateDpetModel = GsonUtil.toBean(oprateDpetModelStr,
		// OprateDpetModel.class);

		Gson gson = new Gson();
		List<LinkedTreeMap> oprateDModelTMap = gson.fromJson(
				oprateDModelListStr, ArrayList.class);
		List<OprateDpetModel> oprateDpetModelList = getOprateDpetModel(oprateDModelTMap);
		if (null == oprateDpetModelList) {
			myResult.setMsg("传参错误");
			myResult.setCode("4");// 传参错误
			printHttpServletResponse(GsonUtil.toJson(myResult));
			return;
		}
		List<OprateDpetModel> resultList = new ArrayList<OprateDpetModel>();
		Integer isExistSucc = 0;
		Integer isExistFail = 0;
		// String id = oprateDpetModel.getBsID();//新增不需要uuid

		for (OprateDpetModel oprateDpetModel : oprateDpetModelList) {

			String bsID = oprateDpetModel.getBsID();
			String parentDM = oprateDpetModel.getParentDM();
			String code = oprateDpetModel.getCode(); // 部门编号
			String name = oprateDpetModel.getName();
			String description = oprateDpetModel.getDescription();

			if (null == bsID || "".equals(bsID.trim())) {
				oprateDpetModel.setBsID("");
				oprateDpetModel.setErrorCode("4");// 传参错误
				resultList.add(oprateDpetModel);
				isExistFail = 1;
				continue;
			}

			if (null == parentDM || "".equals(parentDM.trim())) {
				/*
				 * throw new com.kuangchi.sdd.base.exception.MyException(
				 * "父ID不能为空");
				 */
				oprateDpetModel.setBsID("");
				oprateDpetModel.setErrorCode("4");// 传参错误
				resultList.add(oprateDpetModel);
				isExistFail = 1;
				continue;
			}
			if (null == code || "".equals(code.trim())) {
				/*
				 * throw new com.kuangchi.sdd.base.exception.MyException(
				 * "部门编号不能为空");
				 */
				oprateDpetModel.setBsID("");
				oprateDpetModel.setErrorCode("4");// 传参错误
				resultList.add(oprateDpetModel);
				isExistFail = 1;
				continue;
			}
			if (null == name || "".equals(name.trim())) {
				/*
				 * throw new com.kuangchi.sdd.base.exception.MyException(
				 * "部门名称不能为空");
				 */
				oprateDpetModel.setBsID("");
				oprateDpetModel.setErrorCode("4");
				resultList.add(oprateDpetModel);
				isExistFail = 1;
				continue;
			}
			if (null != description && description.trim().length() > 399) {
				/*
				 * throw new com.kuangchi.sdd.base.exception.MyException(
				 * "备注长度过长");
				 */
				oprateDpetModel.setBsID("");
				oprateDpetModel.setErrorCode("4");// 传参错误
				resultList.add(oprateDpetModel);
				isExistFail = 1;
				continue;
			}

			DepartmentSyncModel departmentSync = new DepartmentSyncModel();
			departmentSync.setBm_dm(oprateDpetModel.getBsID());
			// 根据部门代码获取部门编号
			String bmNo = deptCardSenderSyncService.getDeptNum(oprateDpetModel
					.getBsID());
			if (null == bmNo || "".equals(bmNo)) {
				oprateDpetModel.setBsID("");
				oprateDpetModel.setErrorCode("5");// 编号不存在
				resultList.add(oprateDpetModel);
				isExistFail = 1;
				continue;
			}
			if (!code.equals(bmNo)) {// 比较当前部门代码是否相同，若相同则直接set,若不同，则判断是否已经存在此部门编号

				Integer deptNoCount = deptCardSenderSyncService
						.isExistDeptNo(code);
				if (deptNoCount > 0) {
					oprateDpetModel.setBsID("");
					oprateDpetModel.setErrorCode("3");// 已存在部门编号
					resultList.add(oprateDpetModel);
					isExistFail = 1;
					continue;
				}
			}
			departmentSync.setBm_no(code);

			departmentSync.setBm_mc(name);
			departmentSync.setBz(description);

			if (null != parentDM && !"".equals(parentDM.trim())) {
				if ("0".equals(parentDM)) {
					// departmentSync.setRemote_parentId("0");
					departmentSync.setSjbm_dm("1");// 失败
				} else {

					Integer parentDMCount = deptCardSenderSyncService
							.isExistParentDM(parentDM);
					if (parentDMCount <= 0) {
						oprateDpetModel.setBsID("");
						oprateDpetModel.setErrorCode("2");// 没有存在该父级代码
						resultList.add(oprateDpetModel);
						isExistFail = 1;
						continue;
					} else {
						departmentSync.setSjbm_dm(parentDM);
						// departmentSync.setSjbm_dm(parentDM);
					}
				}
			}
			try {
				int resultCode = deptCardSenderSyncService
						.modifyDepartmentSync(departmentSync);
				if (resultCode == 1) {
					resultList.add(oprateDpetModel);
					isExistSucc = 1;
				} else {
					oprateDpetModel.setBsID("");
					oprateDpetModel.setErrorCode("1");// 失败
					resultList.add(oprateDpetModel);
					isExistFail = 1;
					continue;
				}

			} catch (Exception e) {
				oprateDpetModel.setBsID("");
				oprateDpetModel.setErrorCode("1");// 失败
				resultList.add(oprateDpetModel);
				isExistFail = 1;
				e.printStackTrace();
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
	 * 根据部门id删除部门信息(供发卡软件使用)
	 */
	public void delDepartmentSync() {
		Result myResult = new Result();
		HttpServletRequest request = getHttpServletRequest();
		String oprateDModelListStr = request.getParameter("oprateModelList");
		// OprateDpetModel oprateDpetModel = GsonUtil.toBean(oprateDpetModelStr,
		// OprateDpetModel.class);

		Gson gson = new Gson();
		List<LinkedTreeMap> oprateDModelTMap = gson.fromJson(
				oprateDModelListStr, ArrayList.class);
		List<OprateDpetModel> oprateDpetModelList = getOprateDpetModel(oprateDModelTMap);
		if (null == oprateDpetModelList) {
			myResult.setMsg("传参错误");
			myResult.setCode("4");
			printHttpServletResponse(GsonUtil.toJson(myResult));
			return;
		}
		List<OprateDpetModel> resultList = new ArrayList<OprateDpetModel>();
		Integer isExistSucc = 0;
		Integer isExistFail = 0;
		// String id = oprateDpetModel.getBsID();//新增不需要uuid

		for (OprateDpetModel oprateDpetModel : oprateDpetModelList) {
			String bsID = oprateDpetModel.getBsID();
			if (null == bsID || "".equals(bsID.trim())) {
				oprateDpetModel.setBsID("");
				oprateDpetModel.setErrorCode("4");
				resultList.add(oprateDpetModel);
				isExistFail = 1;
				continue;
			}

			try {
				String deptNum = deptCardSenderSyncService.getDeptNum(bsID);
				if (deptNum == null || "".equals(deptNum)) {
					oprateDpetModel.setBsID("");
					oprateDpetModel.setErrorCode("5");
					resultList.add(oprateDpetModel);
					isExistFail = 1;
					continue;
				}

				Integer empCount = deptCardSenderSyncService
						.getEmpCountByBmDm(bsID);
				if (empCount > 0) {// 如果部门下面有人，则删除失败
					oprateDpetModel.setBsID("");
					oprateDpetModel.setErrorCode("1");
					resultList.add(oprateDpetModel);
					isExistFail = 1;
					continue;
				}

				if (!("2".equals(bsID))) {
					deptCardSenderSyncService.delDepartmentSync(bsID); // 伪删除部门
					deptGwService.delDeptGw(deptNum); // 根据部门代码真删除部门岗位
					employeeSyncService.delEmpFromDeptNum(deptNum); // 伪删除部门下的员工,发卡软件端要求有人时部门不能删除，所以这步不用也可以
				}
				resultList.add(oprateDpetModel);
				isExistSucc = 1;

			} catch (Exception e) {
				oprateDpetModel.setBsID("");
				oprateDpetModel.setErrorCode("1");
				resultList.add(oprateDpetModel);
				isExistFail = 1;
				e.printStackTrace();
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

	public void getDepartment() {
		Result result = new Result();
		List<DepartmentSyncModel> deptList = null;
		try {
			deptList = deptCardSenderSyncService.getAllBm();
			List<OprateDpetModel> opDeptList = new ArrayList<OprateDpetModel>();
			for (DepartmentSyncModel deptModel : deptList) {
				OprateDpetModel opDModel = new OprateDpetModel();
				opDModel.setBsID(deptModel.getBm_dm());
				opDModel.setCode(deptModel.getBm_no());
				opDModel.setName(deptModel.getBm_mc());
				opDModel.setParentDM(deptModel.getSjbm_dm());
				opDModel.setDescription(deptModel.getBz());
				opDeptList.add(opDModel);
			}
			result.setCode("0");
			result.setMsg("查询成功");
			List<OprateDpetModel> newOrderDeptList = new ArrayList<OprateDpetModel>();
			initDeptOrder(opDeptList, newOrderDeptList,
					GlobalConstant.DEPARTMENT_ROOT_BM_DM);
			result.setResultList(newOrderDeptList);
		} catch (Exception e) {
			result.setCode("1");
			result.setMsg("查询失败");
			e.printStackTrace();
		}
		printHttpServletResponse(GsonUtil.toJson(result));

	}

	/**
	 * 从LinkedTreeMap中获取OprateDpetModel
	 * 
	 * @param oprateDModelTMap
	 * @return
	 */
	private List<OprateDpetModel> getOprateDpetModel(
			List<LinkedTreeMap> oprateDModelTMap) {

		if (oprateDModelTMap != null) {
			try {
				List<OprateDpetModel> oprateDpetModelList2 = new ArrayList();
				for (int i = 0; i < oprateDModelTMap.size(); i++) {
					LinkedTreeMap map = oprateDModelTMap.get(i);
					OprateDpetModel oprateDpetModel2 = new OprateDpetModel();
					oprateDpetModel2.setBsID(map.get("bsID") == null ? "" : map
							.get("bsID").toString().trim());
					oprateDpetModel2.setId(map.get("id") == null ? "" : map
							.get("id").toString().trim());
					oprateDpetModel2.setCode(map.get("code").toString().trim());
					oprateDpetModel2
							.setErrorCode(map.get("errorCode") == null ? "0"
									: map.get("errorCode").toString().trim());
					oprateDpetModel2.setName(map.get("name").toString().trim());
					oprateDpetModel2.setParentDM(map.get("parentDM").toString()
							.trim());
					oprateDpetModel2
							.setDescription(map.get("description") == null ? ""
									: map.get("description").toString().trim());

					oprateDpetModelList2.add(oprateDpetModel2);
				}
				return oprateDpetModelList2;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {

			return null;
		}

	}

	/**
	 * 初始化部门节点的顺序（保证父节点在子节点的前面）
	 * 
	 * @author xuewen.deng
	 * @param rootBm_Dm根部门的部门代码
	 */
	public void initDeptOrder(List<OprateDpetModel> oldDeptList,
			List<OprateDpetModel> newOrderDeptList, String rootBm_Dm) {
		// 找到根部门
		for (OprateDpetModel opDpetModel : oldDeptList) {
			if (rootBm_Dm.equals(opDpetModel.getBsID())) {
				newOrderDeptList.add(opDpetModel);
			}
		}
		// 找到根部门的子部门
		List<OprateDpetModel> childDeptList = new ArrayList<OprateDpetModel>();
		for (OprateDpetModel opDpetM : oldDeptList) {
			if (rootBm_Dm.equals(opDpetM.getParentDM())) {
				childDeptList.add(opDpetM);
			}
		}
		// 对子部门进行递归调用
		for (OprateDpetModel opDeptMChild : childDeptList) {
			initDeptOrder(oldDeptList, newOrderDeptList, opDeptMChild.getBsID());
		}
	}
}
