package com.kuangchi.sdd.attendanceConsole.staffManage.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.staffManage.dao.StaffManageDao;
import com.kuangchi.sdd.attendanceConsole.staffManage.model.StaffManage;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;

@Repository("staffManageDaoImpl")
public class StaffManageDaoImpl extends BaseDaoImpl<StaffManage> implements StaffManageDao {

	@Override
	public String getNameSpace() {
		
		return "common.StaffManage";
	}

	@Override
	public String getTableName() {
		return null;
	}

	@Override
	public int getAllStaffCount(StaffManage staffManage) {
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("staff_no", staffManage.getStaff_no());
		mapParam.put("staff_name",staffManage.getStaff_name());
		mapParam.put("layerDeptNum",staffManage.getLayerDeptNum());
		return (Integer) this.getSqlMapClientTemplate().queryForObject("getStaffManageCount",mapParam);  
	}
	

	@Override
	public List<StaffManage> getAllStaff(StaffManage staffManage, String page,String size) {
		int pages = Integer.valueOf(page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("page", (pages - 1) * rows);
		mapParam.put("rows", rows);
		mapParam.put("staff_no",staffManage.getStaff_no());
		mapParam.put("staff_name",staffManage.getStaff_name());
		mapParam.put("layerDeptNum",staffManage.getLayerDeptNum());
		return this.getSqlMapClientTemplate().queryForList("getAllStaffManage", mapParam);
	}

	@Override
	public void modifyStaffPassword(StaffManage staffManage) {
		this.getSqlMapClientTemplate().update("modifyStaffPassword", staffManage);
		
	}

	@Override
	public String getStaffPassword(String staff_num) {
		String staffPassword=(String) this.getSqlMapClientTemplate().queryForObject("getStaffPassword", staff_num);
		return staffPassword;
	}


}
