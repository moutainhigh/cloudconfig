package com.kuangchi.sdd.attendanceConsole.noCheckCard.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kuangchi.sdd.attendanceConsole.noCheckCard.dao.INoCheckCardDao;
import com.kuangchi.sdd.attendanceConsole.noCheckCard.model.NoCheckCard;
import com.kuangchi.sdd.base.dao.BaseDaoImpl;
import com.kuangchi.sdd.baseConsole.device.model.DeviceInfo;
import com.kuangchi.sdd.baseConsole.doorinfo.dao.IDoorInfoDao;
import com.kuangchi.sdd.baseConsole.doorinfo.model.DoorInfoModel;
import com.kuangchi.sdd.consumeConsole.discount.dao.IDiscountDao;
import com.kuangchi.sdd.consumeConsole.discount.model.Discount;
@Repository("NoCheckCardDaoImpl")
public class NoCheckCardDaoImpl extends BaseDaoImpl<NoCheckCard> implements INoCheckCardDao {

	@Override
	public String getNameSpace() {
		return null;
	}

	@Override
	public String getTableName() {
		return null;
	}
	
	/**
	 * 查询所有员工免打卡信息
	 */
	@Override
	public List<NoCheckCard> getAllNoCheckCardByStaff(NoCheckCard noCheck_info,String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("page", (page-1)*rows);
		mapState.put("rows", rows);
		mapState.put("staff_no",noCheck_info.getStaff_no());
		mapState.put("staff_name", noCheck_info.getStaff_name());
		mapState.put("dept_num", noCheck_info.getDept_num());
		mapState.put("staff_num", noCheck_info.getStaff_num());
		mapState.put("from_time", noCheck_info.getFrom_time());
		mapState.put("to_time", noCheck_info.getTo_time());
		mapState.put("layerDeptNum",noCheck_info.getLayerDeptNum());
		return this.getSqlMapClientTemplate().queryForList("getAllNoCheckCardByStaff", mapState);
	}
	/**
	 * 查询所有员工免打卡信息总条数
	 */
	@Override
	public Integer getAllNoCheckCardByStaffCount(NoCheckCard noCheck_info) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("staff_no",noCheck_info.getStaff_no());
		mapState.put("staff_name", noCheck_info.getStaff_name());
		mapState.put("dept_num", noCheck_info.getDept_num());
		mapState.put("staff_num", noCheck_info.getStaff_num());
		mapState.put("from_time", noCheck_info.getFrom_time());
		mapState.put("to_time", noCheck_info.getTo_time());
		mapState.put("layerDeptNum",noCheck_info.getLayerDeptNum());
		return queryCount("getAllNoCheckCardByStaffCount",mapState);
	}
	
	/**
	 * 新增员工免打卡信息
	 */
	@Override
	public Boolean insertNoCheckCard(NoCheckCard noCheck_info) {
		Object obj=this.getSqlMapClientTemplate().insert("insertNoCheckCard", noCheck_info);
		if(obj==null){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据id查询员工信息
	 */
	@Override
	public List<NoCheckCard> selectNoCheckCardByStaff(Integer id) {
		return this.getSqlMapClientTemplate().queryForList("selectNoCheckCardByStaff",id);
		
	}
	/**
	 * 根据id查询部门信息
	 */
	@Override
	public List<NoCheckCard> selectNoCheckCardByDept(Integer id) {
		return this.getSqlMapClientTemplate().queryForList("selectNoCheckCardByDept",id);
		
	}
	
	/**
	 * 修改免打卡信息
	 */
	@Override
	public Boolean updateNoCheckCardByStaff(NoCheckCard noCheck_info) {
		Object obj=this.getSqlMapClientTemplate().update("updateNoCheckCardByStaff", noCheck_info);
		if(obj==null){
			return false;
		}
		return true;
	}
	/**
	 * 删除员工免打卡信息
	 */
	@Override
	public Integer deleNoCheckCardByStaff(String id) {
		Integer obj=(Integer)this.getSqlMapClientTemplate().delete("deleNoCheckCardByStaff", id);
		//Integer obj=(Integer)this.getSqlMapClientTemplate().delete("deleteNoCheckCard", id);
		if(obj==0){
			return 0;
		}
		return 1;
	}
	
	/**
	 * 删除员工免打卡信息
	 */
	@Override
	public Integer deleNoCheckCardByDept(String id) {
		Integer obj=(Integer)this.getSqlMapClientTemplate().delete("deleNoCheckCardByDept", id);
		if(obj==0){
			return 0;
		}
		return 1;
	}
	
	
	
	
	@Override
	public List<NoCheckCard> selectNoCheckCardsByDept(NoCheckCard noCheck_info,String Page, String size) {
		int page = Integer.valueOf(Page);
		int rows = Integer.valueOf(size);
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("page", (page-1)*rows);
		mapState.put("rows", rows);
		mapState.put("BM_NO",noCheck_info.getBM_NO());
		//mapState.put("BM_MC", noCheck_info.getBM_MC());
		mapState.put("dept_num", noCheck_info.getBM_DM());
		mapState.put("from_time", noCheck_info.getFrom_time());
		mapState.put("to_time", noCheck_info.getTo_time());
		mapState.put("layerDeptNum",noCheck_info.getLayerDeptNum());
		return this.getSqlMapClientTemplate().queryForList("selectNoCheckCardsByDept",mapState);
	}

	@Override
	public Integer selectNoCheckCardsByDeptCount(NoCheckCard noCheck_info) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("BM_NO",noCheck_info.getBM_NO());
		//mapState.put("BM_MC", noCheck_info.getBM_MC());
		mapState.put("dept_num", noCheck_info.getBM_DM());
		mapState.put("from_time", noCheck_info.getFrom_time());
		mapState.put("to_time", noCheck_info.getTo_time());
		mapState.put("layerDeptNum",noCheck_info.getLayerDeptNum());
		return queryCount("selectNoCheckCardsByDeptCount",mapState);
	}
	@Override
	public int selectNoCheckCardByDeptNum(String dept_num) {
		return queryCount("selectNoCheckCardByDeptNum",dept_num);
	}
	@Override
	public Boolean insertNoCheckCardByDept(NoCheckCard noCheck_info) {
		Object obj=this.getSqlMapClientTemplate().insert("insertNoCheckCardByDept", noCheck_info);
		if(obj==null){
			return true;
		}
		return false;
	}
	@Override
	public Boolean updateNoCheckCardByDept(NoCheckCard noCheck_info) {
		Object obj=this.getSqlMapClientTemplate().update("updateNoCheckCardByDept", noCheck_info);
		if(obj==null){
			return false;
		}
		return true;
	}

	@Override
	public List<NoCheckCard> getNoCheckCardByStaffNum(NoCheckCard noCheck_info) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("staff_num",noCheck_info.getStaff_num());
		mapState.put("id", noCheck_info.getId());
		return this.getSqlMapClientTemplate().queryForList("getNoCheckCardByStaffNum", mapState);
	}

	@Override
	public List<NoCheckCard> getNoCheckCardByDeptNum(NoCheckCard noCheck_info) {
		Map<String, Object> mapState = new HashMap<String, Object>();
		mapState.put("dept_num",noCheck_info.getBM_DM());
		mapState.put("id", noCheck_info.getId());
		return this.getSqlMapClientTemplate().queryForList("getNoCheckCardByDeptNum", mapState);
	}
}
