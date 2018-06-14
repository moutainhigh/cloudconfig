package com.kuangchi.sdd.businessConsole.department.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.JsonResult;
import com.kuangchi.sdd.base.model.easyui.Tree;
import com.kuangchi.sdd.baseConsole.cardtype.model.CardType;
import com.kuangchi.sdd.businessConsole.department.model.Department;
import com.kuangchi.sdd.consumeConsole.fundPool.model.JsonTreeResult;

public interface IDepartmentService {

	/**
	 * 查询部门
	 * @param pid 上级部门代码
	 * @return
	 */
	List<Department> getSystemDepartment(String pid);

	/**
	 * 新增部门
	 * @param dep
	 */
	void addDepartment(Department dep,String login_user);

	/**
	 * 部门tree
	 * @param departmentRoot 部门根代码(部门代码)
	 * @return
	 */
	Tree getDepartmentTree(String departmentRoot, String layerDeptNum);


	/**
	 * 部门tree
	 * @param departmentRoot 部门根代码(部门代码)
	 * @return
	 */
	Tree getOnlyDepartmentTree(String departmentRoot, String layerDeptNum);
	
	Tree getOnlyDepartmentTreeLazy(String departmentRoot, String layerDeptNum);
	
	List<Tree> getOnlyDepartmentTreeList(String departmentRoot, String layerDeptNum);


	/**
	 * 用户所有部门编号
	 * @param yhDm
	 * @return
	 */
	String[] getUserDepartment(String yhDm);
	
	/**
	 * 部门详情
	 * @param bmDm
	 * @return
	 */
	Department getDepartmentDet(String bmDm);

	/**
	 * 修改部门
	 * @param dep
	 */
	void modifyDepartment(Department dep,String login_user);

	JsonResult validDepartment(String bmNo,String bmDm);
	/**
	 * 删除部门
	 * @param depIds
	 */
	boolean delDepartment(String depIds);

	String selectBmdmByNo(String bmNo);
	
	
	public  List<Tree> getDepartmentTreeList(String departmentRoot,String layerDeptNum);
	
	public  List<Tree> getDepartmentCardTreeList(String departmentRoot,String layerDeptNum);
	
	public  List<Tree> getDepartmentCardTreeListA(String staffNum);
	
	public Tree getLazyDepartmentTree(String departmentRoot, String layerDeptNum) ;

	public List<Department> downWardDepartment(String bmDm, String layerDeptNum);
	public boolean delEmployee(String empIds);

	List<CardType> getCardtype();//查询卡片类型

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-7 下午1:40:32
	 * @功能描述: 资金池代码部门树 
	 */
	public JsonTreeResult getDeptTree(String organiztion_num,String layerDeptNum);
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-21 下午5:30:43
	 * @功能描述:获取选中资金池绑定的部门树 
	 * @参数描述:
	 */
	public Tree getMyDeptTree(String organiztion_num);
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-8-31 上午10:47:25
	 * @功能描述:根据部门代码绑定资金池 
	 * @参数描述:
	 */
	public boolean bandBalancePool(Map map);
	
	/**
	   * @创建人　: 邓积辉
	   * @创建时间: 2016-8-31 下午3:35:48
	   * @功能描述: 根据资金池代码获取绑定的部门
	   * @参数描述:
	   */
	List<Department> getBandDeptByZjDm(String organiztion_num);

	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-7 下午1:43:37
	 * @功能描述:	根据资金池代码绑定部门 
	 * @参数描述:
	 */
	boolean updateBalancePoolByZjDm(Map<String, String> map0);
	
	/**
	 * @return 
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-5 上午10:04:07
	 * @功能描述:获取部门全部子节点 
	 * @参数描述:
	 */
	List<String> getLeafChildrens(List<String> bmDms);

	Integer checkSelectDeptIsBand(String bmDm);
	
	/**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-21 下午4:51:41
	 * @功能描述: 查看资金池下是否有绑定的部门
	 * @参数描述:
	 */
	Integer isBundDeptByOrganiztion_num(String organiztion_num);
	
	/**
	 *构建部门，员工，卡树，在原有基础上多了卡这一层
	 *@param 
	 *by gengji.yang 
	 */
	Tree getDepartmentCardTree(String departmentRoot, String layerDeptNum);

	boolean setEncodeRule(Map map);//设置员工工号或部门编号的编码规则

	Map<String, String> selectRulesFromSystemParam();//读取编码规则

	String selectDeptNoRule();//查询部门编号编码规则

	boolean selectInfoByNum(String finalDeptNo1);//查询部门编号是否存在

	
	
	public void updateDeptNoTemp(Map map);
	public void deleteAgricultureDeptData();
	
	List<Department> getAllDepart();
	
	
	/**
	 * 查询分层授权部门（员工信息展示使用，不查询无权限父部门）
	 * @author yuman.gao
	 */
	public String getLayerDeptNum(String yhDm, String jsDm);
	
	/**
	 * 查询分层授权部门（部门树使用）
	 * @author yuman.gao
	 */
	public String deptGetLayerDeptNum(String yhDm, String jsDm);
	
	/**
	 * 查询该父部门下所有子部门
	 * @author yuman.gao
	 */
	public String getAllChildDeptByParent(String deptNum,List<Department> allDepartments);
	

	boolean bandBalancePoolBybmDm(Map<String, String> map, String bmDm,String login_user);
	
	
	

}
