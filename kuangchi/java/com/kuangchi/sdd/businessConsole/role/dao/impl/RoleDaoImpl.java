package com.kuangchi.sdd.businessConsole.role.dao.impl;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.businessConsole.role.dao.IRoleDao;
import com.kuangchi.sdd.businessConsole.role.model.Role;

import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by chucun on 14-8-30.
 */
@Repository("roleDaoImpl")
public class RoleDaoImpl extends BaseDaoImpl<Role> implements IRoleDao {
    @Override
    public List<Role> getRoles(Role rolePage) {
        int skip = (rolePage.getPage() - 1)* rolePage.getRows();
        return this.getSqlMapClientTemplate().queryForList("getRoles", rolePage, skip, rolePage.getRows());
    }

    @Override
    public int countRoles(Role rolePage) {
    	/*if(rolePage.getLrryDm() == null){
    		return queryCount("countNzRoles",null);
    	} else {
    		return queryCount("countRoles", rolePage) + queryCount("countNzRoles",null);
    	}*/
    	return queryCount("countRoles", rolePage);
    }

    @Override
    public void addNewRole(Role rolePage) {
        getSqlMapClientTemplate().insert("addNewRole",rolePage);
    }

    @Override
    public String[] getRoleAssginMenu(String roleId) {
        List<String> result = getSqlMapClientTemplate().queryForList("getRoleAssginMenu", roleId);
        return result.toArray(new String[]{});
    }

    @Override
    public void deleteRoleMenu(String roleId) {
        this.getSqlMapClientTemplate().delete("deleteRoleMenu",roleId);
    }
    
    @Override
    public void deleteRoleMenuA(String roleId,String menuId) {
    	Map map=new HashMap();
    	map.put("roleId", roleId);
    	map.put("menuId", menuId);
    	this.getSqlMapClientTemplate().delete("deleteRoleMenuA",map);
    }

    @Override
    public void addRoleMenu(String roleId, String[] menuIds,String lrryDm) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("roleId",roleId);
        params.put("menuIds", Arrays.asList(menuIds));
        params.put("lrryDm", lrryDm);
        this.getSqlMapClientTemplate().insert("addRoleMenu",params);
    }

    @Override
    public String[] getRoleAssginUser(String roleId, String userIds) {
        Map<String,String> params = new HashMap<String,String>();
        params.put("roleId",roleId);
        params.put("userIds",userIds);
        List<String> result = getSqlMapClientTemplate().queryForList("getRoleAssginUser", params);
        return result.toArray(new String[]{});
    }

    @Override
    public void deleteRoleUser(String roleId, String currUserIds) {
        Map<String,String> params = new HashMap<String,String>();
        params.put("roleId",roleId);
        params.put("currUserIds",currUserIds);
        getSqlMapClientTemplate().delete("deleteRoleUser",params);
    }

    @Override
    public void addRoleUser(String roleId, String[] newUserId,String lrryDm) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("roleId",roleId);
        params.put("newUserId", Arrays.asList(newUserId));
        params.put("lrryDm", lrryDm);
        this.getSqlMapClientTemplate().insert("addRoleUser",params);
    }

    @Override
    public String getNameSpace() {
        return "common.Role";
    }

    @Override
    public String getTableName() {
        return null;
    }

	@Override
	public Role getUserDefaultRole(String yhDm) {	
		return (Role) getSqlMapClientTemplate().queryForObject("getUserDefaultRole", yhDm);
	}

	@Override
	public List<Role> getUserRoles(String yhDm) {	
		return getSqlMapClientTemplate().queryForList("getUserRoles", yhDm);
	}

	@Override
	public List<Role> getUserAllRoles(String yhDm) {
		
		return getSqlMapClientTemplate().queryForList("getUserAllRoles",yhDm );
	}

	@Override
	public void clearUserDefaultRole(String userId, String jS_DM) {
		Map<String,String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("jS_DM", jS_DM);
		getSqlMapClientTemplate().update("clearUserDefaultRole", params);
		
	}

	@Override
	public void setUserDefaultRole(String userId, String jS_DM) {
		Map<String,String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("jS_DM", jS_DM);
		getSqlMapClientTemplate().update("setUserDefaultRole", params);
		
	}

	@Override
	public List<Role> getAllRoles(String lrryDm) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("lrryDm", lrryDm);
		return this.getSqlMapClientTemplate().queryForList("getAllRoles", param);
	}

	@Override
	public void insertUserDefaultRole(String yhDm, String jsDm,String lrryDm) {
		Map<String,String> params = new HashMap<String, String>();
		params.put("yhDm", yhDm);
		params.put("jsDm", jsDm);
		params.put("lrryDm", lrryDm);
		getSqlMapClientTemplate().insert("insertUserDefaultRole",params);
	}

	@Override
	public void deleteUserRoles(String yhDm) {
		getSqlMapClientTemplate().delete("deleteUserRoles", yhDm);
		
	}

	@Override
	public void addUserRoles(String yhDm, List<Role> roles, String lrryDm) {
		
		Map<String,Object> params = new HashMap<String,Object>();
        params.put("yhDm",yhDm);
        params.put("roles", roles);
        params.put("lrryDm", lrryDm);
		getSqlMapClientTemplate().insert("addUserRoles",params);
		
	}

	@Override
	public void deleteUserSRoles(String yhDms) {
		getSqlMapClientTemplate().delete("deleteUserSRoles", yhDms);
		
	}

	@Override
	public Role getRole(String jsDm) {
		
		return (Role) getSqlMapClientTemplate().queryForObject("getRole", jsDm);
	}

	@Override
	public void updateRole(Role role) {
		getSqlMapClientTemplate().update("updateRole", role);
		
	}

	@Override
	public void deleteRoleMenus(String roleDms) {
		getSqlMapClientTemplate().delete("deleteRoleMenus", roleDms);		
	}

	@Override
	public void deleteYhJs(String roleDms) {
		getSqlMapClientTemplate().delete("deleteYhJs", roleDms);
		
	}

	@Override
	public void deleteRoles(String roleDms) {
		getSqlMapClientTemplate().update("deleteRoles", roleDms);
		
	}

	@Override
	public String[] getDeptGrant(String roleId) {
		List<String> result = getSqlMapClientTemplate().queryForList("getDeptGrant", roleId);
        return result.toArray(new String[]{});
	}

	@Override
	public void changeDeptGrant(String roleId, String[] deptId) {
		 Map<String,Object> params = new HashMap<String,Object>();
        params.put("roleId",roleId);
        params.put("deptIds", Arrays.asList(deptId));
        this.getSqlMapClientTemplate().insert("addDeptGrantToRoles",params);
	}
	
	@Override
	public void addDeptGrant(String roleId, String deptId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("roleId",roleId);
		params.put("deptId", deptId);
		this.getSqlMapClientTemplate().insert("addDeptGrantToRole",params);
	}

	@Override
	public void deleteDeptGrant(String roleId) {
		this.getSqlMapClientTemplate().delete("deleteDeptGrant",roleId);
		
	}

	@Override
	public void updateLayer(Map<String, Object> map) {
		this.update("updateLayer", map);
	}

	@Override
	public String getLayer(Map<String, Object> map) {
		return (String)this.getSqlMapClientTemplate().queryForObject("getLayer", map);
	}

	@Override
	public int getBmJs(Map<String, Object> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getBmJs", map);
	}

	@Override
	public String[] getRoleGrantByDept(String deptId) {
		List<String> result = getSqlMapClientTemplate().queryForList("getRoleGrantByDept", deptId);
        return result.toArray(new String[]{});
	}

}
