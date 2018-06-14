package com.kuangchi.sdd.elevatorConsole.departmentGrant.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.elevatorConsole.departmentGrant.model.DepartmentGrantModel;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.model.DeviceModel;
import com.kuangchi.sdd.elevatorConsole.departmentGrant.model.FloorGroupInfoModel;

public interface IDepartmentGrantDao {

	List<DepartmentGrantModel> getDepartmentGrantsByParam(String deptIds, String page,String rows);//查询所有组织授权

	Integer getDepartmentGrantsCount(String deptIds);//查询所有组织授权总数

	List<DeviceModel> getDevicesInfo(DeviceModel deviceModel, String page,
			String rows);//新增时查询所有设备信息

	Integer getDevicesInfoCount(DeviceModel deviceModel);//新增时查询所有设备总数

	List<FloorGroupInfoModel> getFloorGroupInfoForSelect();//初始化楼层组下拉框

	List<Map> selectCardNumByObjectNum(String object_num);//通过部门编号查询员工所持的卡

	boolean addDeptGrant(DepartmentGrantModel departmentGrantModel);//新增组织权限 

	boolean addDeptGrant2(DepartmentGrantModel departmentGrantModel);//新增组织权限    by huixian.pan

	boolean delDeptGrant(DepartmentGrantModel departmentGrantModel);//真删除组织权限

	boolean removeDeptGrant(Map<String,String> map);//从权限表读取权限并插入到任务表，置action_flag为1

	List<Map<String, String>> selectFloorNumByFloorGroupNum(
			String floor_group_num);//通过楼层组编号查询楼层号

	List<Map> selectGrantByDeptNum(String deptNum,String deviceNum);//查询组织权限

	DeviceModel selectInfoByDeviceNum(String device_num);//根据设备编号查询设备ip

	List<Map> selectDeptAuthorityInfoList(Map map);//查询部门权限任务信息

	int selectDeptAuthorityInfoCount(Map map);//查询部门权限任务信息总数


	List<Map> selectStaffAuthorityInfoList(Map map);//查询人员权限任务信息

	int selectStaffAuthorityInfoCount(Map map);//查询人员权限任务信息总数

	List<Map> selectDeptAuthorityForViewList(Map map);//查询部门权限

	int selectDeptAuthorityForViewCount(Map map);//查询部门权限总数

	List<Map> selectStaffAuthorityForViewList(Map map);//查询人员权限

	int selectStaffAuthorityForViewCount(Map map);//查询人员权限总数

	List<Map> searchStaffAuthority(Map map);//按条件下载人员权限

	List<Map> searchDeptAuthority(Map map);//按条件下载部门权限

	/*List<Map<String,String>> selectDeptAuthorityFromAuthority(String deptno);*///根据部门号去权限表查询部门下的权限
	List<Map> selectDeptAuthorityFromAuthority(String objectNum,String deviceNum);//根据部门号去权限表查询部门下的权限

	boolean selectCardAuthInDept(String object_nums);//复制权限前，判断源部门中员工的卡是否有权限

	boolean addToAuthorityTable(DepartmentGrantModel departmentGrantModel);//新增到任务表之前，新增到权限表并置task_state='00'

	String selectCardTypeByNum(String cardNum);//根据卡号查询卡片类型代码

	String selectBmdmByStaffNum(String staffNum);//根据员工编号查询部门编号

	Map selectDeptAuthFromAuthority(String deptno);
	
	public void delTkAuthDirect(Map map);

	

}
