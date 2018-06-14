package com.kuangchi.sdd.businessConsole.department.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.baseConsole.cardtype.model.CardType;
import com.kuangchi.sdd.businessConsole.department.model.Department;

public interface IDepartmentDao {

	List<Department> getSystemDepartment(String pid);

	/**
	 * 新增部门
	 * @param dep
	 */
	void addDepartment(Department dep);

	/**
	 * 子部门数量
	 * @param depId
	 * @return
	 */
	int departmentHasChildren(String depId);

	/**
	 * 查询所有部门（角色分层）
	 * @return
	 */
	List<Department> getAllDepart(String layerDeptNum);
	
	List<Department> getChildDepartA(Map map);
	/**
	 * 查询所有部门
	 * @return
	 */
	List<Department> getAllDepart();
	
	void deleteUserSDep(String yhDms);

	String[] getUserDepartment(String yhDm);

	void addUserDepart(String yhDm, String bmDm, String lrryDm);

	void addUserDeparts(String yhDm, String[] bmDmS, String lrryhDm);

	Department getDepartmentDet(String bmDm);

	boolean modifyDepartment(Department dep);

	int isContainbmDM(String bmNo,String bmDm);

    void deleBmYh(String depIds);

    void deleBm(String depIds);

    void deleDepGw(String depIds);

    void deleGwYh(String depIds);

    String selectBmdmByNo(String bmNo);
    
    
  public Department getDepartByBmDm(String bmDm);
  public	List<Department> getDepartByParentBmDm(String bmDm, String layerDeptNum);
  
  public	List<Department> getDepartByParentBmDmA(String bmDm, String layerDeptNum);

  List<CardType> getCardtype();

  Department getDeptTreeRoot();

  List<Department> getChildrenBySjDm(String SjDm);

  Department getDeptBySjDm(String SjDm);
  /**
   * @创建人　: 邓积辉
   * @创建时间: 2016-8-31 上午10:48:50
   * @功能描述:根据部门代码绑定资金池 
   * @参数描述:
   */
  boolean bandBalancePool(Map map);
  
  /**
   * @创建人　: 邓积辉
   * @创建时间: 2016-8-31 下午3:35:48
   * @功能描述: 根据资金池代码获取绑定的部门
   * @参数描述:
   */
  List<Department> getBandDeptByZjDm(String organiztion_num);

  boolean updateBalancePoolByZjDm(Map<String, String> map0);

  Integer checkSelectDeptIsBand(String bmDm);

  Integer getBandDeptByZjDmAndBmDm(Map map1);

  Integer getOtherZjCBandDeptCount(Map map);

  /**
	 * @创建人　: 邓积辉
	 * @创建时间: 2016-9-21 下午4:51:41
	 * @功能描述: 查看资金池下是否有绑定的部门
	 * @参数描述:
  */
  Integer isBundDeptByOrganiztion_num(String organiztion_num);

  /**
   * @创建人　: 邓积辉
   * @创建时间: 2016-9-21 下午9:26:08
   * @功能描述: 根据部门代码获取上级部门信息 
   * @参数描述:
   */
  List<Department> getSjBmByBmDm(String deptDms);

  /**
   * @创建人　: 邓积辉
   * @创建时间: 2016-9-21 下午9:26:08
   * @功能描述: 根据部门代码获取对应的资金池名称 
   * @参数描述:
   */
  List getOrganiztionNameByBmDm(String bmDm);
  
  /**
   * @创建人　: 邓积辉
   * @创建时间: 2016-9-21 下午9:26:08
   * @功能描述: 根据部门代码获取对应的资金池名称 
   * @参数描述:
   */
  List getBandBmByOrganiztionNum(String organiztion_num);

boolean setStaffNoRule(Map map);//设置员工工号编码规则

boolean setDeptNoRule(Map map);//设置部门编号编码规则

String selectStaffNoRule();//读取员工工号编码规则

String selectDeptNoRule();//读取部门编号编码规则

boolean selectInfoByBmbh(String finalDeptNo1);//查询部门编号是否存在



public void updateDeptNoTemp(Map map);


public void deleteAgricultureDeptData();

/**
 * 查询该用户创建的部门
 * @author yuman.gao
 */
public List<Department> getDeptByLrryDm(String yhDm);

/**
 * 查询该角色授权部门代码
 * @author yuman.gao
 */
public List<String> getDeptByJsDm(String jsDm);


/**
 * 查询该部门下所有一层子部门
 * @author yuman.gao
 */
public List<String> getChildDept(String bmDm);

/**
 * 根据角色和部门查询授权信息总数 （用于查询用户是否有某部门下所有子部门权限）
 * @author yuman.gao
 */
public Integer getJsBmCount(String bmDms, String jsDm);

/**
 * 根据部门代码获取绑定的资金池
 * @author jihui.deng
 */
String getOrganiztionNumByBmdm(String bmDm);




}
