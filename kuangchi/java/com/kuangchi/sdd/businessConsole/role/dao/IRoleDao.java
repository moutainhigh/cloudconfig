package com.kuangchi.sdd.businessConsole.role.dao;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.businessConsole.role.model.Role;

/**
 * Created by chucun on 14-8-30.
 */
public interface IRoleDao {
    List<Role> getRoles(Role rolePage);

    int countRoles(Role rolePage);

    void addNewRole(Role rolePage);

    String[] getRoleAssginMenu(String roleId);

    void deleteRoleMenu(String roleId);
    
    void deleteRoleMenuA(String roleId,String menuId);

    void addRoleMenu(String roleId, String[] menudId,String lrryDm);

    String[] getRoleAssginUser(String roleId, String userIds);

    void deleteRoleUser(String roleId, String currUserIds);

    void addRoleUser(String roleId, String[] newUserId,String lrryDm);

	Role getUserDefaultRole(String yhDm);

	List<Role> getUserRoles(String yhDm);

	List<Role> getUserAllRoles(String yhDm);

	/**
	 * 清空用户现有默认角色
	 * @param userId
	 * @param jS_DM
	 */
	void clearUserDefaultRole(String userId, String jS_DM);

	/**
	 * 设置用户默认角色
	 * @param userId
	 * @param jS_DM
	 */
	void setUserDefaultRole(String userId, String jS_DM);

	List<Role> getAllRoles(String lrryDm);

	/**
	 * 新增用户角色 默认角色
	 * @param yhDm
	 * @param jsDm
	 *  @param lrryDm  录入人员代码
	 */
	void insertUserDefaultRole(String yhDm, String jsDm,String lrryDm);

	/**
	 * 删除人员所有角色
	 * @param yhDm
	 */
	void deleteUserRoles(String yhDm);

	/**
	 * 新增人员角色
	 * @param yhDm
	 * @param roles
	 * @param lrryDm
	 */
	void addUserRoles(String yhDm, List<Role> roles, String lrryDm);


	void deleteUserSRoles(String yhDms);

	Role getRole(String jsDm);

	void updateRole(Role role);

	void deleteRoleMenus(String roleDms);

	void deleteYhJs(String roleDms);

	void deleteRoles(String roleDms);
	/**
	 * 查找部门授权   by chudan.guo
	 */
	String[] getDeptGrant(String roleId);
	/**
	 * 添加部门授权   by chudan.guo
	 */
    void changeDeptGrant(String roleId, String[] deptId);
    
    void addDeptGrant(String roleId, String deptId);
    
    /**
	 * 查询有该部门权限的角色
	 * @author yuman.gao
	 */
	public String[] getRoleGrantByDept(String deptId);
	
    /**
	 * 删除部门授权   by chudan.guo
	 */
    void deleteDeptGrant(String roleId);
    
    /**
     * 更新分层功能状态
     * @author yuman.gao
     */
    void updateLayer(Map<String, Object> map);
    
    
    /**
     * 查询分层功能状态
     * @author yuman.gao
     */
    String getLayer(Map<String, Object> map);
    
    /**
     * 根据角色和部门查询该权限是否存在
     * @author yuman.gao
     */
    int getBmJs(Map<String, Object> map);
   
}
