package com.kuangchi.sdd.businessConsole.department.dao.impl;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.cardtype.model.CardType;
import com.kuangchi.sdd.businessConsole.department.dao.IDepartmentDao;
import com.kuangchi.sdd.businessConsole.department.model.Department;

@Repository("departmentDaoImpl")
public class DepartmentDaoImpl extends BaseDaoImpl<Department> implements IDepartmentDao {

	@Override
	public String getNameSpace() {
		return "common.Department";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public List<Department> getSystemDepartment(String pid) {
		
		return getSqlMapClientTemplate().queryForList("getSystemDepartment", pid);
	}

	@Override
	public void addDepartment(Department dep) {
		getSqlMapClientTemplate().insert("addDepartment", dep);
		
	}

	@Override
	public int departmentHasChildren(String depId) {
		return queryCount("departmentHasChildren", depId);
	}

	@Override
	public List<Department> getAllDepart(String layerDeptNum) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("layerDeptNum", layerDeptNum);
//		return getSqlMapClientTemplate().queryForList("getAllDepartByJs", layerDeptNum);
		return getSqlMapClientTemplate().queryForList("getAllDepart", map);
	}
	
	@Override
	public List<Department> getChildDepartA(Map map) {
		return getSqlMapClientTemplate().queryForList("getChildDepartA", map);
	}
	
	@Override
	public List<Department> getAllDepart() {
		return getSqlMapClientTemplate().queryForList("getAllDepart");
	}

	@Override
	public void deleteUserSDep(String yhDms) {
		
		getSqlMapClientTemplate().delete("deleteUserSDep", yhDms);
	}

	@Override
	public String[] getUserDepartment(String yhDm) {
		 List<String> result = getSqlMapClientTemplate().queryForList("getUserDepartment", yhDm);
	     return result.toArray(new String[]{});
	}

	@Override
	public void addUserDepart(String yhDm, String bmDm, String lrryDm) {
		
		Map<String,String>  params = new HashMap<String,String>();
		params.put("yhDm", yhDm);
		params.put("bmDm", bmDm);
		params.put("lrryDm", lrryDm);
		getSqlMapClientTemplate().insert("addUserDepart", params);
		
	}

	@Override
	public void addUserDeparts(String yhDm, String[] bmDmS, String lrryhDm) {
		

		Map<String,Object>  params = new HashMap<String,Object>();
		params.put("yhDm", yhDm);
		params.put("bmDmS", Arrays.asList(bmDmS));
		params.put("lrryhDm", lrryhDm);
		getSqlMapClientTemplate().insert("addUserDeparts", params);
		
		
	}

	@Override
	public Department getDepartmentDet(String bmDm) {
		
		return (Department) getSqlMapClientTemplate().queryForObject("getDepartmentDet", bmDm);
	}

	@Override
	public boolean modifyDepartment(Department dep) {
		return update("modifyDepartment", dep);
		
	}

	@Override
	public int isContainbmDM(String bmNo,String bmDm) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("bmNo", bmNo);
		map.put("bmDm", bmDm);
		return queryCount("isContainbmDM", map);
	}

    @Override
    public void deleBmYh(String depIds) {
        getSqlMapClientTemplate().delete("deleBmYh",depIds);
    }

    @Override
    public void deleBm(String depIds) {
        getSqlMapClientTemplate().update("deleBm",depIds);
    }

    @Override
    public void deleDepGw(String depIds) {
        getSqlMapClientTemplate().delete("deleDepGw",depIds);
    }

    @Override
    public void deleGwYh(String depIds) {

        List<String> getBmGw = getSqlMapClientTemplate().queryForList("getBmGw",depIds);
        if (getBmGw.size() > 0){
            StringBuilder builder = new StringBuilder();
            for (int i = 0 ; i < getBmGw.size();i++){
                builder.append("'");
                builder.append(getBmGw.get(i));
                builder.append("'");
                if (i != getBmGw.size() - 1){
                    builder.append(",");
                }
            }
            depIds = builder.toString();
            getSqlMapClientTemplate().delete("deleGwYh",depIds);
        }

  }

	@Override
	public String selectBmdmByNo(String bmNo) {
		return (String) getSqlMapClientTemplate().queryForObject("selectBmdmByNo", bmNo);
	}

	@Override
	public Department getDepartByBmDm(String bmDm) {
		 Map<String, String> map=new HashMap<String, String>();
		 map.put("bmDm", bmDm);
		return (Department) getSqlMapClientTemplate().queryForObject("getDepartByBmDm",map);
	}

	@Override
	public List<Department> getDepartByParentBmDm(String bmDm, String layerDeptNum) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("layerDeptNum", layerDeptNum);
		map.put("bmDm", bmDm);
		return getSqlMapClientTemplate().queryForList("getDepartByParentBmDm",map);
	}
	
	@Override
	public List<Department> getDepartByParentBmDmA(String bmDm, String layerDeptNum) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("layerDeptNum", layerDeptNum);
		map.put("bmDm", bmDm);
		return getSqlMapClientTemplate().queryForList("getDepartByParentBmDmA",map);
	}

	@Override
	public List<CardType> getCardtype() {
		return this.getSqlMapClientTemplate().queryForList("getCardTypes");
	}

	@Override
	public Department getDeptTreeRoot() {
		return (Department) getSqlMapClientTemplate().queryForObject("getDeptTreeRoot");
	}

	@Override
	public List<Department> getChildrenBySjDm(String SjDm) {
		return getSqlMapClientTemplate().queryForList("getChildrenBySjDm", SjDm);
	}

	@Override
	public Department getDeptBySjDm(String SjDm) {
		return (Department) getSqlMapClientTemplate().queryForObject("getDeptBySjDm", SjDm);
	}

	@Override
	public boolean bandBalancePool(Map map) {
		return update("bandBalancePool",map);
	}

	@Override
	public List<Department> getBandDeptByZjDm(String organiztion_num) {
		return getSqlMapClientTemplate().queryForList("getBandDeptByZjDm", organiztion_num);
	}

	@Override
	public boolean updateBalancePoolByZjDm(Map<String, String> map0) {
		return update("updateBalancePoolByZjDm",map0);
	}

	@Override
	public Integer checkSelectDeptIsBand(String bmDm) {
		return (Integer) getSqlMapClientTemplate().queryForObject("checkSelectDeptIsBand", bmDm);
	}

	@Override
	public Integer getBandDeptByZjDmAndBmDm(Map map1) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getBandDeptByZjDmAndBmDm", map1);
	}

	@Override
	public Integer getOtherZjCBandDeptCount(Map map) {
		return (Integer) getSqlMapClientTemplate().queryForObject("getOtherZjCBandDeptCount", map);
	}

	@Override
	public Integer isBundDeptByOrganiztion_num(String organiztion_num) {
		return (Integer) getSqlMapClientTemplate().queryForObject("isBundDeptByOrganiztion_num", organiztion_num);
	}

	@Override
	public List<Department> getSjBmByBmDm(String bm_Dms) {
		return getSqlMapClientTemplate().queryForList("getSjBmByBmDm", bm_Dms);
	}

	@Override
	public List getOrganiztionNameByBmDm(String bmDm) {
		return getSqlMapClientTemplate().queryForList("getOrganiztionNameByBmDm", bmDm);
	}

	@Override
	public List getBandBmByOrganiztionNum(String organiztion_num) {
		return getSqlMapClientTemplate().queryForList("getBandBmByOrganiztionNum", organiztion_num);
	}

	@Override
	public boolean setStaffNoRule(Map map) {
		return update("setStaffNoRule",map);
	}

	@Override
	public boolean setDeptNoRule(Map map) {
		return update("setDeptNoRule",map);
	}

	@Override
	public String selectStaffNoRule() {
		return (String) getSqlMapClientTemplate().queryForObject("selectStaffNoRule");
	}

	@Override
	public String selectDeptNoRule() {
		return (String) getSqlMapClientTemplate().queryForObject("selectDeptNoRule");
	}

	@Override
	public boolean selectInfoByBmbh(String finalDeptNo1) {
		if((Integer)getSqlMapClientTemplate().queryForObject("selectInfoByBmbh", finalDeptNo1)>0){
			return true;
		}else{
			
			return false;
		}
	}

	

	@Override
	public void updateDeptNoTemp(Map map) {
		getSqlMapClientTemplate().update("updateDeptNoTemp", map);
	}

	@Override
	public void deleteAgricultureDeptData() {
		Map<String, String> map=new HashMap<String, String>();
		 getSqlMapClientTemplate().delete("deleteAgricultureDeptData",map);
		
	}

	@Override
	public List<Department> getDeptByLrryDm(String yhDm) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("yhDm", yhDm);
		return this.getSqlMapClientTemplate().queryForList("getDeptByLrryDm", map);
	}

	@Override
	public List<String> getDeptByJsDm(String jsDm) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("jsDm", jsDm);
		return this.getSqlMapClientTemplate().queryForList("getDeptByJsDm", map);
	}

	@Override
	public List<String> getChildDept(String bmDm) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("bmDm", bmDm);
		return this.getSqlMapClientTemplate().queryForList("getChildDept", map);
	}
	
	@Override
	public Integer getJsBmCount(String bmDms, String jsDm) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("bmDms", bmDms);
		map.put("jsDm", jsDm);
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getJsBmCount", map);
	}

	@Override
	public String getOrganiztionNumByBmdm(String bmDm) {
		return (String) getSqlMapClientTemplate().queryForObject("getOrganiztionNumByBmdm", bmDm);
	}
	
}
