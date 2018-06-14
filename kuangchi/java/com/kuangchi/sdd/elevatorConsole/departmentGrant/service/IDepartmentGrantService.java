package com.kuangchi.sdd.elevatorConsole.departmentGrant.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.model.DepartmentGrantModel;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.model.DeviceModel;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.model.FloorGroupInfoModel;

public interface IDepartmentGrantService {

	Grid getDepartmentGrantsByParam(String deptIds, String page, String rows);//查询所有组织授权

	Grid<DeviceModel> getDevicesInfo(DeviceModel deviceModel, String page,
			String rows);//新增时查询所有设备信息

	List<FloorGroupInfoModel> getFloorGroupInfoForSelect();//初始化楼层组下拉框

	boolean addDeptGrant(DepartmentGrantModel departmentGrantModel, String create_user);//新增组织权限
	
	public  boolean  addDeptTask(DepartmentGrantModel departmentGrantModel,String create_user);
	
	boolean addDeptGrantA(DepartmentGrantModel departmentGrantModel, String create_user);//新增组织权限

	boolean checkCardInDept(String object_nums);//新增组织权限之前验证所选部门中员工是否绑卡

	/*boolean removeDeptGrant(String object_nums, String create_user);*///删除组织权限
	boolean removeDeptGrant(Map m, String create_user);//删除组织权限

	boolean copyDeptGrant(String main_deptnums, String target_deptnums, String create_user);//复制组织权限

	Grid selectDeptAuthorityInfo(Map map, String page, String rows);//查询部门权限任务表

	Grid selectStaffAuthorityInfo(Map map, String page, String rows);//查询员工权限任务表

	Grid selectDeptAuthorityForView(Map map, String page, String rows);//查询部门权限

	Grid selectStaffAuthorityForView(Map map, String page, String rows);//查询人员权限

	List<Map> searchStaffAuthority(Map map);//按条件下载人员权限

	List<Map> searchDeptAuthority(Map map);//按条件下载部门权限

	boolean checkCardAuthInDept(String object_nums);//复制权限前，判断源部门中员工的卡是否有权限

	Map selectDeptAuthByDeptNo(String deptno);//根据部门编号查询部门下的权限,在employeeAction或cardAction中调用

	String selectCardTypeByNum(String cardNum);//根据卡号查询卡片类型代码
	boolean addDeptAuth(DepartmentGrantModel departmentGrantModel);//新增组织权限到任务表，供修改员工部门时使用
	boolean removeDeptAuth(Map<String,String> map);//删除原权限，供修改员工部门时使用

	String selectBmdmByStaffNum(String staffNum);//根据员工编号查询部门代码
	
	List<Map> selectCardNumByObjectNum(String object_num);
	
	void saveDeptAuth(List<Map> list,String create_user);
	
	public Map addFloorList3(List<Map> floorLists,String newFloorList);
	
}
