package com.kuangchi.sdd.businessConsole.role.service;

import java.util.List;
import java.util.Map;

import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.businessConsole.role.model.Role;

/**
 * Created by chucun on 14-8-30.
 */
public interface IRoleService {
    Grid<Role> getRoles(Role rolePage);

    void addNewRole(Role rolePage);

    String[] getRoleAssginMenu(String roleId);

    void changeRoleMenu(String roleId, String[] menudId,String lrryDm);

    String[] getRoleAssginUser(String roleId, String ids);

    void changeRoleUser(String roleId, String currUserIds, String[] newUserId,String lrryDm);
    
    /**
     * 查询用户默认角色
     * @param yhDm 用户代码
     * @return
     */
    Role getUserDefaultRole(String yhDm);

    /**
     * 查询用户所有角色不包括默认角色
     * @param yhDm 用户代码
     * @return
     */
	List<Role> getUserRoles(String yhDm);

	/**
	 * 查询用户所有角色
	 * @param yhDm 用户代码
	 * @return
	 */
	List<Role> getUserAllRoles(String yhDm);

	/**
	 * 设置用户默认角色
	 * @param userId 用户代码
	 * @param jS_DM	 用户角色
	 */
	void setUserDefaultRole(String userId, String jS_DM);

	/**
	 * 系统所有角色
	 * @return
	 */
	List<Role> getAllRoles(String lrryDm);

	/**
	 * 更新用户角色
	 * @param yhDm 人员代码
	 * @param roles 角色
	 * @param lrryDm 录入人员代码
	 */
	void updateUserRoles(String yhDm, List<Role> roles, String lrryDm);

	/**
	 * 获取角色
	 * @param jsDm
	 * @return
	 */
	Role getRole(String jsDm);

	/**
	 * 修改角色
	 * @param role
	 */
	void updateRole(Role role);

	/**
	 * 删除角色
	 * @param roleDms
	 */
	void deleteRoles(String roleDms);
	/**
	 * 查找部门授权   by chudan.guo
	 */
	String[] getDeptGrant(String roleId);
	
	/**
	 * 添加部门授权（覆盖旧权限）   by chudan.guo
	 */
	public void changeDeptGrant(String roleId, String[] deptId);
	
	/**
	 * 添加部门授权（不覆盖旧权限）   by chudan.guo
	 */
	public void addDeptGrant(String roleId, String[] deptId);
	
	/**
	 * 查询有该部门权限的角色
	 * @author yuman.gao
	 */
	public String[] getRoleGrantByDept(String deptId);
	
	/**
	 * 更新分层功能状态
	 * @author yuman.gao
	 */
	public void updateLayer(Map<String, Object> map);
	

    /**
     * 查询系统是否开启分层功能
     * @author yuman.gao
     */
    public boolean isLayer();
	
}
