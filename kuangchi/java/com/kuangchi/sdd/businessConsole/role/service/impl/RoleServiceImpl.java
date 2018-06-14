package com.kuangchi.sdd.businessConsole.role.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangchi.sdd.base.constant.GlobalConstant;
import com.kuangchi.sdd.base.model.easyui.Grid;
import com.kuangchi.sdd.base.service.BaseServiceSupport;
import com.kuangchi.sdd.businessConsole.role.dao.IRoleDao;
import com.kuangchi.sdd.businessConsole.role.model.Role;
import com.kuangchi.sdd.businessConsole.role.service.IRoleService;
import com.kuangchi.sdd.util.commonUtil.DateUtil;
import com.kuangchi.sdd.util.commonUtil.UUIDUtil;

/**
 * Created by chucun on 14-8-30.
 */
@Transactional
@Service("roleServiceImpl")
public class RoleServiceImpl extends BaseServiceSupport implements IRoleService {

    @Resource(name = "roleDaoImpl")
    private IRoleDao roleDao;
   
    
    @Override
    public Grid<Role> getRoles(Role rolePage) {
        Grid<Role> grid = new Grid<Role>();
        grid.setRows(roleDao.getRoles(rolePage));
        grid.setTotal(roleDao.countRoles(rolePage));
        return grid;
    }

    @Override
    public void addNewRole(Role role) {
    	role.setUUID(UUIDUtil.uuidStr());
    	role.setJsDm(UUIDUtil.uuidStr());
    	role.setLrSj(DateUtil.getSysTimestamp());
    	role.setZfBj(GlobalConstant.ZF_BJ_N);
        roleDao.addNewRole(role);
    }

    @Override
    public String[] getRoleAssginMenu(String roleId) {
        return roleDao.getRoleAssginMenu(roleId);
    }

    @Override
    public void changeRoleMenu(String roleId, String[] menudId,String lrryDm) {
        roleDao.deleteRoleMenu(roleId);
        roleDao.addRoleMenu(roleId, menudId, lrryDm);
    }
    
/*    @Override
    public void changeRoleMenu(String roleId, String[] menudId,String lrryDm) {
    	for(int i=0;i<menudId.length;i++){
    		roleDao.deleteRoleMenuA(roleId,menudId[i]);
    	}
    	roleDao.addRoleMenu(roleId, menudId, lrryDm);
    }*/

    @Override
    public String[] getRoleAssginUser(String roleId, String userIds) {
        return roleDao.getRoleAssginUser(roleId,userIds);
    }

    @Override
    public void changeRoleUser(String roleId, String currUserIds, String[] newUserId,String lrryDm) {
        roleDao.deleteRoleUser(roleId,currUserIds);
        roleDao.addRoleUser(roleId, newUserId,lrryDm);
    }

	@Override
	public Role getUserDefaultRole(String yhDm) {
		return roleDao.getUserDefaultRole(yhDm);
	}

	@Override
	public List<Role> getUserRoles(String yhDm) {
		
		return roleDao.getUserRoles(yhDm);
	}

	@Override
	public List<Role> getUserAllRoles(String yhDm) {
		return roleDao.getUserAllRoles(yhDm);
	}

	@Override
	public void setUserDefaultRole(String userId, String jS_DM) {
		roleDao.clearUserDefaultRole(userId,jS_DM);
		roleDao.setUserDefaultRole(userId,jS_DM);
		
		
	}

	@Override
	public List<Role> getAllRoles(String lrryDm) {
		
		return roleDao.getAllRoles(lrryDm);
	}

	@Override
	public void updateUserRoles(String yhDm, List<Role> roles, String lrryDm) {
		
		roleDao.deleteUserRoles(yhDm);
		roleDao.addUserRoles(yhDm,roles,lrryDm);
		
	}

	@Override
	public Role getRole(String jsDm) {
		
		return roleDao.getRole(jsDm);
	}

	@Override
	public void updateRole(Role role) {
		roleDao.updateRole(role);
		
	}

	@Override
	public void deleteRoles(String roleDms) {
		//删除角色菜单
		roleDao.deleteRoleMenus(roleDms);
		
		//删除角色用户关系表
		roleDao.deleteYhJs(roleDms);
		
		//删除角色表
		roleDao.deleteRoles(roleDms);
		
	}
	@Override
	public String[] getDeptGrant(String roleId) {
		return roleDao.getDeptGrant(roleId);
	}
	@Override
	public void changeDeptGrant(String roleId, String[] deptIds) {
		
		roleDao.deleteDeptGrant(roleId);
		roleDao.changeDeptGrant(roleId, deptIds);
		
	}
	
	@Override
	public void addDeptGrant(String roleId, String[] deptIds) {
		
		for (String deptId : deptIds) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("role_id", roleId);
			map.put("dept_id", deptId);
			
			int count = roleDao.getBmJs(map);
			if(count == 0){
				roleDao.addDeptGrant(roleId, deptId);
			}
		}
		
	}
	
	@Override
	public String[] getRoleGrantByDept(String deptId) {
		return roleDao.getRoleGrantByDept(deptId);
	}


	@Override
	public void updateLayer(Map<String, Object> map) {
		roleDao.updateLayer(map);
		
	}

	@Override
	public boolean isLayer() {
		boolean isLayer = false;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sys_key", "isLayer");
		String status = roleDao.getLayer(map);
		if("0".equals(status)){
			isLayer = true;
		}
		
		return isLayer;
	}


	
}
